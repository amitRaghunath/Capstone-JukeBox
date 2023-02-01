package capstoneChallenge;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class welcome extends userDetails
{
    Set check3=new HashSet();

    Scanner s=new Scanner(System.in);

    public welcome(int userId, String password)
    {
        super(userId, password);
    }
    public static String query="select * from songs";
    public welcome()
    {

    }

    public void userChoice()
    {
        System.out.println("WELCOME TO THE WORLD OF MUSIC USER");
        System.out.println("1 :SONG LIST");
        System.out.println("2 :ARTIST NAME");
        System.out.println("3 :GENRE");
        System.out.println("4 :PLAYLIST");
        System.out.println("5: PODCAST ");
    }
    public void createAccount() throws SQLException, ClassNotFoundException
    {
        System.out.println("=======================SIGN UP==========================");
        System.out.println();
        System.out.println("ENTER USER ID");
        int IDs=s.nextInt();
        System.out.println("ENTER USER NAME");
        String userName=s.next();
        System.out.println("ENTER USER PASSWORD");
        String userPassword=s.next();
        Connection a = getConnection();
        Statement state = a.createStatement();
        ResultSet check=state.executeQuery("Select userId from userDetails");
        while(check.next())
        {
            check3.add(check.getInt(1));
        }
        if(check3.contains(IDs))
        {
            System.out.println("USER ID IS TAKEN TRY AGAIN ");
            createAccount();
        }
        else
        {
            state.executeUpdate("insert into userDetails value(" + IDs + ",'" + userName + "'," + userPassword + ")");
            System.out.println("SIGN IN SUCCESSFUL");
        }
    }
    public Connection getConnection()throws SQLException,ClassNotFoundException
    {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/jukebox","root","toor1234");
    }
    public void Menu() throws SQLException, ClassNotFoundException
    {
        System.out.println("===================* Songs List*=================== ");

        Connection a=getConnection();
        Statement state=a.createStatement();
        ResultSet result=state.executeQuery("Select * from songs");
        while(result.next())
        {
            System.out.println(result.getInt(1)+" "+result.getString(2));
        }

    }
    public int playAudio(int a) throws SQLException, ClassNotFoundException, UnsupportedAudioFileException, IOException, LineUnavailableException {
            long pause = 0L;
            long length = 0L;
            Connection con =getConnection();// DriverManager.getConnection("jdbc:mysql://localhost:3306/jukebox", "root", "toor1234");
            Statement state = con.createStatement();
            System.out.println("song Id:" + a);
            ResultSet result = state.executeQuery("Select URL from songs where songID=" + a);
            result.next();
            String path = result.getString(1);
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File(path).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
            int pre = 0;
            long songLength = clip.getMicrosecondLength() / 1000000;
            System.out.println(songLength / 60 + " : Minutes");
            // Clip play = AudioSystem.getClip();
            while (pre != 4) {
                System.out.println("*PRESS 1 TO PAUSE*,*PRESS 2 TO RESUME* ,*PRESS 3 TO RE-PLAY*,*PRESS 4 TO STOP AND QUIT*,5.FOR NEXT SONG,6.FOR PREVIOUS SONG");
                pre = s.nextInt();
                switch (pre) {
                    case 1:
                        pause = clip.getMicrosecondPosition();
                        length = clip.getMicrosecondLength();
                        System.out.println("Played seconds : " + pause / 1000000);
                        System.out.println("Remaining seconds:" + (length - pause) / 1000000);
                        clip.stop();
                        break;
                    case 2:
                        clip.setMicrosecondPosition(pause);
                        System.out.println("Remaining seconds :" + (length - pause) / 1000000);
                        clip.start();
                        break;
                    case 3:
                        clip.setMicrosecondPosition(0);
                        break;
                    case 4:
                        clip.stop();
                        clip.close();
                        return 0;

                    case 5:
                        clip.stop();
                        clip.close();
                        return 1;

                    case 6:
                        clip.stop();
                        clip.close();
                        return 2;

                }
            }
            return 0;
    }


    public void displayGenre() throws SQLException, ClassNotFoundException
    {

        Connection a=getConnection();
        Statement state=a.createStatement();
        ResultSet result=state.executeQuery("Select distinct Genre from songs");
        while(result.next())
        {
            System.out.println(result.getString(1));
        }
    }
    public void playListCreate() throws SQLException, ClassNotFoundException, UnsupportedAudioFileException, LineUnavailableException, IOException {
        Connection a = getConnection();
        Statement state = a.createStatement();
        int num = 0;
        int sID ;
        int uID ;
        int i=1;
        String name="";
        System.out.println("ENTER 1 TO ADD FROM SONG*:::::*ENTER 2 TO ADD FROM PODCAST");
        int playChoice=s.nextInt();
        if(playChoice==1)
        {
            while (num != 2) {
                Menu();
                System.out.println("Enter SongId TO ADD");
                sID = s.nextInt();
                uID = userId;
                while (i != 2) {
                    System.out.println("Enter playList Name");
                    name = s.next();
                    ++i;
                }
                ResultSet rest = state.executeQuery("select songName from songs where songId=" + sID);
                rest.next();
                String Songname = rest.getString(1);
                state.executeUpdate("insert into playList(listName,userId,songId,podcastId,songName)values('" + name + "'," + uID + "," + sID + "," + null + ",'" + Songname + "')");
                System.out.println("Press 1 to add more 2 to finish");
                num = s.nextInt();
            }
        }else if(playChoice==2)
        {
            Podcast();
            System.out.println("SELECT PODCAST NUMBER TO VIEW EPISODES");
            ResultSet podcast=state.executeQuery("select podcastId from podcast");
            while(podcast.next())
            {
                check3.add(podcast.getInt(1));
            }
            int podcastEp=s.nextInt();
            if(check3.contains(podcastEp))
            {
               if(podcastEp==1)
               {
                   podcastEpisode1(podcastEp);
               }
            }
        }
    }
    public void playListAudioPlay(int songId,String playName) throws SQLException, ClassNotFoundException, UnsupportedAudioFileException, IOException, LineUnavailableException {
        Connection a = getConnection();
        Statement state = a.createStatement();
        System.out.println("song Id:" + songId);
        ResultSet result3 = state.executeQuery("Select URL from songs where songID=" + songId);
        result3.next();
        String path = result3.getString(1);
        AudioInputStream audio = AudioSystem.getAudioInputStream(new File(path).getAbsoluteFile());
        Clip clip = AudioSystem.getClip();
        clip.open(audio);
        clip.start();
        long pause = 0L;
        long length ;
        List playList=new ArrayList();
        ResultSet set2=state.executeQuery("select distinct songId from playList where ListName like'%"+playName+"%'");
        while(set2.next())
        {
            playList.add(set2.getInt(1));//3,5,8
        }
        int desc=0;
        ListIterator temp=playList.listIterator();
        while(desc!=6) {
            System.out.println("ENTER 1 FOR NEXT ::::ENTER 2 FOR PREVIOUS::::ENTER 3 TO PAUSE::::ENTER 4 TO PLAY::::ENTER 5 TO REPLAY::::ENTER 6 TO STOP AND QUIT");
            desc = s.nextInt();
            switch (desc) {
                case 1:
                    System.out.println("case1");
                    clip.stop();
                    clip.close();

                        if (temp.hasNext()) {
                            for (int i = 0; i < 1; i++) {
                                temp.next();
                            }

                            songId = (int) temp.next();
                            playListAudioPlay(songId, playName);
                        }
                     else
                        {
                            for (int i = 0; i <playList.size(); i++)
                            {
                                temp.hasPrevious();
                            }
                            songId=(int)temp.next();
                            playListAudioPlay(songId,playName);

                        //System.out.println("END OF PLAYLIST");
                    }
                    break;
                case 2:
                    clip.stop();
                    clip.close();
                    if (temp.hasPrevious())
                    {
                        temp.previous();
                        songId = (int) temp.previous();
                        playListAudioPlay(songId, playName);
                    }
                    break;
                case 3:
                    pause = clip.getMicrosecondPosition();
                    length = clip.getMicrosecondLength();
                    System.out.println("Played seconds : " + pause / 1000000);
                    System.out.println("Remaining seconds:" + (length - pause) / 1000000);
                    clip.stop();
                    break;
                case 4:
                    clip.setMicrosecondPosition(pause);
                    clip.start();
                    break;
                case 5:
                    clip.setMicrosecondPosition(0);
                    break;
                case 6:
                    clip.stop();
                    clip.close();
                    break;
            }

        }
    }
    public void displayPlayList(int uID) throws SQLException, ClassNotFoundException, UnsupportedAudioFileException, LineUnavailableException, IOException
    {
        Connection a = getConnection();
        Statement state = a.createStatement();
        int resultt=1;
        Set display=new HashSet();
        ResultSet result= state.executeQuery("select distinct ListName from playList where userId="+uID);
        if(!result.next())
        {
            System.out.println("NO PLAYLIST CREATED");
        }
        else
        {
            System.out.println(result.getString(1));
            while(result.next())
            {
                System.out.println(result.getString(1));
            }
            System.out.println("ENTER PLAYlIST NAME TO VIEW");
            String playName=s.next();
            ResultSet set1=null;

                set1 = state.executeQuery("select distinct songId,songName,podcastEpisode,podcastEpisodeName from playList where ListName='"+playName+"'");


               while (set1.next())
               {
                   System.out.println(set1.getInt(1) + " " + set1.getString(2) + " " + set1.getDouble(3) + " " + set1.getString(4));
               }


            ResultSet setPlay=state.executeQuery("select distinct songId from playList where ListName like'%"+playName+"%'");
            //setPlay.next();
            while(setPlay.next())
            {
                display.add(setPlay.getInt(1));
            }
            int status=0;
            ResultSet res2=state.executeQuery("select distinct songId,songName,podcastEpisode,podcastEpisodeName from playList where ListName='"+playName+"'");
            while(res2.next())
            {
                status++;
            }
            int[] list=new int[status];
            int con=0;
            ResultSet res3=state.executeQuery("select distinct songId,songName,podcastEpisode,podcastEpisodeName from playList where ListName='"+playName+"'");
            while(res3.next())
            {
                list[con]=res3.getInt(1);//2,7
                con++;
            }
           /* System.out.println(display);
            for(int j=0;j<list.length;j++)
            {
                System.out.println(list[j]);
            }*/

            System.out.println("ENTER SONG ID TO PLAY SONG ");
            int songId=s.nextInt();

            //System.out.println(display);
            if(display.contains(songId))
            {

               // int songId1= (int) songId;
                for (int i = 0; i < list.length && resultt != 0; )//0
                {

                    if (songId == list[i]) {
                        resultt = playAudio1(songId);

                    }
                    if (resultt == 1) {
                        if (i == list.length - 1) {
                            i = -1;
                            songId = list[0];
                        } else {
                            songId = list[i + 1];
                        }
                        i++;
                    }
                    if (resultt == 2) {
                        if (i == 0) {
                            i = list.length;
                            songId = list[list.length - 1];
                        } else {
                            songId = list[i - 1];
                        }
                        i--;
                    }
                }
            }
            else
            {
                System.out.println("INVALID INPUT");
            }
        }

    }
    public void artistName()throws SQLException, ClassNotFoundException
    {
        Connection a = getConnection();
        Statement state = a.createStatement();
        ResultSet result= state.executeQuery("select distinct artistName from songs");
        while(result.next())
        {
            System.out.println(result.getString(1));
        }

    }
    public void artistSongs(String name) throws SQLException, ClassNotFoundException, UnsupportedAudioFileException, LineUnavailableException, IOException {
        Connection a = getConnection();
        Statement state = a.createStatement();
        int status=0;
        Set artist=new HashSet();

        ResultSet res2=state.executeQuery("Select songId from songs where ArtistName like'%" + name + "%'");
        while(res2.next())
        {
            status++;
        }
        int[] list=new int[status];
        int con=0;
        int resultt=1;
        ResultSet res3=state.executeQuery("Select songId from songs where ArtistName like'%" + name + "%'");
        while(res3.next())
        {
            list[con]=res3.getInt(1);
            con++;
        }
           ResultSet result = state.executeQuery("Select SongId,songname from songs where ArtistName like'%" + name + "%'");
           while (result.next())
           {
               System.out.println(result.getInt(1) + "  " + result.getString(2));
           }
           System.out.println("ENTER 1 to play song or 2 to add in PLAYLIST");
           ResultSet res1=state.executeQuery("Select songId from songs where ArtistName like'%" + name + "%'");
           while(res1.next())
           {
               artist.add(res1.getInt(1));
           }

           int achoice = s.nextInt();
        System.out.println(artist);
           if (achoice == 1)
           {
               System.out.println("ENTER SONG ID TO PLAY");
               int sID = s.nextInt();
               System.out.println(artist);
               if(artist.contains(sID))
               {
                   for(int i=0;i<list.length&&resultt!=0;)//0
                   {
                       System.out.println(list[i]);
                       if(sID==list[i])
                       {
                           resultt=playAudio1(sID);

                       }
                       if(resultt==1)
                       {
                           if(i==list.length-1)
                           {
                               i=-1;
                               sID=list[0];
                           }
                           else
                           {
                               sID=list[i+1];
                           }
                           i++;
                       }
                       if(resultt==2)
                       {
                           if(i==0)
                           {
                               i=list.length;
                               sID=list[list.length-1];
                           }
                           else
                           {
                               sID=list[i-1];
                           }
                           i--;
                       }
                   }
               }
               else
               {
                   String name1=name;
                   System.out.println("INVALID");
                   artistSongs(name1);
               }
               artist.clear();

           } else if (achoice == 2)
           {
               int sID;
               int uID;
               String Aname="";
               int coun=0;
               while(coun!=2) {
                   System.out.println("Enter SongId TO ADD");
                   sID = s.nextInt();
                   System.out.println(artist);
                   if(artist.contains(sID))
                   {
                       artist.clear();
                       ResultSet rest = state.executeQuery("select songName from songs where songId=" + sID);
                       rest.next();
                       String Songname = rest.getString(1);
                       uID = userId;
                       int i = 0;
                       while (i != 1) {
                           System.out.println("Enter playList Name");
                           Aname = s.next();
                           i++;
                       }
                       state.executeUpdate("insert into playList value(ID,'" + Aname + "'," + uID + "," + sID + "," + null + ",'" + Songname + "',"+null+",'"+null+"')");
                       System.out.println("*::::ENTER 1 TO ADD MORE 2 TO EXIT::::*");
                       coun = s.nextInt();
                   }
               }
           }


    }
    public void genreSongs(String name) throws SQLException, ClassNotFoundException, UnsupportedAudioFileException, LineUnavailableException, IOException {
        Connection a = getConnection();
        Statement state = a.createStatement();
        Set genre=new HashSet();
        int con=0;
        int status=0;

        ResultSet res2=state.executeQuery("select songID,SongName from songs where Genre='"+name+"'");
        while(res2.next())
        {
            status++;
        }
        int[] list=new int[status];
        int con1=0;
        int resultt=1;
        ResultSet res3=state.executeQuery("select songID,SongName from songs where Genre='"+name+"'");
        while(res3.next())
        {
            list[con1]=res3.getInt(1);
            con1++;
        }
        ResultSet result=state.executeQuery("select songID,SongName from songs where Genre='"+name+"'");
        while(result.next())
        {
            System.out.println(result.getInt(1)+" "+result.getString(2));
        }
        System.out.println("ENTER 1 to play song or 2 to add in PLAYLIST");
        int achoice=s.nextInt();
        ResultSet setGenre=state.executeQuery("select songID,SongName from songs where Genre='"+name+"'");
        while(setGenre.next())
        {
            genre.add(setGenre.getInt(1));
        }
        int sID;
        if (achoice == 1)
        {
            System.out.println("ENTER SONG ID");
            sID = s.nextInt();
            if(genre.contains(sID))
            {
                for(int i=0;i<list.length&&resultt!=0;)//0
                {
                    System.out.println(list[i]);
                    if(sID==list[i])
                    {
                        resultt=playAudio1(sID);

                    }
                    if(resultt==1)
                    {
                        if(i==list.length-1)
                        {
                            i=-1;
                            sID=list[0];
                        }
                        else
                        {
                            sID=list[i+1];
                        }
                        i++;
                    }
                    if(resultt==2)
                    {
                        if(i==0)
                        {
                            i=list.length;
                            sID=list[list.length-1];
                        }
                        else
                        {
                            sID=list[i-1];
                        }
                        i--;
                    }
                }
            }
        }
        else if(achoice==2)
        {
            while(con!=1) {
                int sID1;
                int uID;
                String Aname = "";
                System.out.println("Enter SongId TO ADD");
                sID1 = s.nextInt();
                if (genre.contains(sID1)) {
                    uID = userId;
                    int i = 0;
                    while (i != 1) {
                        System.out.println("Enter playList Name");
                        Aname = s.next();
                        i++;
                    }
                    state.executeUpdate("insert into playList value(ID,'" + Aname + "'," + uID + "," + sID1 + "," + null + ",'" + null + "'," + null + ",'" + null + "')");
                }
                System.out.println("ENTER 1 TO FINISH");
                con=s.nextInt();
            }
            //state.executeUpdate("insert into playList value(ID,'" +podListName+ "'," + uid + "," + null+ "," + null + ",'" + null +"',"+number+",'"+podNmae+"')");

        }

    }
    public void Podcast() throws SQLException, ClassNotFoundException
    {
        System.out.println("===================* Podcast List*=================== ");

        Connection a=getConnection();
        Statement state=a.createStatement();
        ResultSet result=state.executeQuery("Select * from podcast");
        while(result.next())
        {
            System.out.println(result.getInt(1)+" "+result.getString(2)+" "+result.getString(3));
        }

    }
    public void podcastEpisode(int Pid) throws SQLException, ClassNotFoundException, UnsupportedAudioFileException, LineUnavailableException, IOException {
        System.out.println("===================* EPISODES LIST*=================== ");

        Connection a=getConnection();
        Statement state=a.createStatement();
        Set podEp=new HashSet();
        ResultSet result=state.executeQuery("select episodeNumber,episodeName from episode where podcastID="+Pid);
        while(result.next())
        {
            System.out.println(result.getDouble(1)+" "+result.getString(2));
        }
        System.out.println("ENTER EPISODE NUMBER TO PLAY");
        double number = s.nextDouble();
        ResultSet set55=state.executeQuery("select episodeNumber,episodeName from episode where podcastID="+Pid);
        while(set55.next())
        {
            podEp.add(set55.getDouble(1));
        }
        System.out.println(podEp);
        if(podEp.contains(number))
        {
            podEp.clear();
            playEpisode(number);
        }
        else{
            System.out.println("ENTER VALID INPUT");
        }

    }
    public void podcastEpisode1(int Pid) throws SQLException, ClassNotFoundException, UnsupportedAudioFileException, LineUnavailableException, IOException {
        System.out.println("===================* EPISODES LIST*=================== ");

        Connection a=getConnection();
        Statement state=a.createStatement();
        Set podEp=new HashSet();
        ResultSet result=state.executeQuery("select episodeNumber,episodeName from episode where podcastID="+Pid);
        while(result.next())
        {
            System.out.println(result.getDouble(1)+" "+result.getString(2));
        }
        int con=0;
        while(con!=2)
        {
            System.out.println("ENTER EPISODE NUMBER TO ADD");
            double number=s.nextDouble();
            ResultSet set55=state.executeQuery("select episodeNumber from episode");
            while(set55.next())
            {
                podEp.add(set55.getDouble(1));
            }
            ResultSet resEp=state.executeQuery("select episodeName from episode where episodeNumber ="+number);
            resEp.next();
            String podNmae=resEp.getString(1);
            int uid=userId;
            int i=0;
            String podListName="";

            if(podEp.contains(number))
            {
                while(i!=1) {
                    System.out.println("ENTER PODCAST PLAYLIST NAME");
                    podListName=s.next();
                    i++;
                }
                state.executeUpdate("insert into playList value(ID,'" +podListName+ "'," + uid + "," + null+ "," + null + ",'" + null +"',"+number+",'"+podNmae+"')");
            }
            else{
                System.out.println("INVALID EPISODE NUMBER");
            }
            System.out.println("ENTER 1 TO ADD 2 TO EXIT");
            con=s.nextInt();

        }
    }
    public void playEpisode(double a) throws SQLException, ClassNotFoundException, UnsupportedAudioFileException, IOException, LineUnavailableException
    {

        long pause = 0L;

        long length = 0L;
        Connection con = getConnection();
        Statement state = con.createStatement();
        ResultSet result = state.executeQuery("Select URL from episode where episodeNumber=" + a);
        result.next();
        String path = result.getString(1);
        AudioInputStream audio = AudioSystem.getAudioInputStream(new File(path).getAbsoluteFile());
        Clip clip = AudioSystem.getClip();
        clip.open(audio);
        clip.start();
        int pre = 0;
        long songLength = clip.getMicrosecondLength()/1000000;
        System.out.println(songLength / 60 + " : Minutes");

        while(pre!=4)
        {
            System.out.println("*PRESS 1 TO PAUSE*,*PRESS 2 TO RESUME* ,*PRESS 3 TO RE-PLAY*,*PRESS 4 TO STOP AND QUIT*");
            pre=s.nextInt();
            switch(pre)
            {
                case 1:
                    pause=clip.getMicrosecondPosition();
                    length=clip.getMicrosecondLength();
                    System.out.println("Played seconds : "+pause/1000000);
                    System.out.println("Remaining seconds:"+(length-pause)/1000000);
                    clip.stop();

                    break;
                case 2:
                    clip.setMicrosecondPosition(pause);
                    System.out.println("Remaining seconds :"+(length-pause)/1000000);
                    clip.start();
                    break;
                case 3:
                    clip.setMicrosecondPosition(0);
                    break;
                case 4:
                    clip.stop();

            }
        }
        clip.close();
    }
    public void podcastPlayListCreate() throws SQLException, ClassNotFoundException {
        Connection con = getConnection();
        Statement state = con.createStatement();
        state.executeQuery("");
    }
    public int playAudio1(int a) throws SQLException, ClassNotFoundException, UnsupportedAudioFileException, IOException, LineUnavailableException {
        long pause = 0L;

        long length = 0L;
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/jukebox","root","toor1234");
        Statement state = con.createStatement();
        System.out.println("song Id:"+a);
        ResultSet result = state.executeQuery("Select URL from songs where songID=" + a);
        result.next();
        String path = result.getString(1);
        AudioInputStream audio = AudioSystem.getAudioInputStream(new File(path).getAbsoluteFile());
        Clip clip = AudioSystem.getClip();
        clip.open(audio);
        clip.start();
        int pre = 0;
        long songLength = clip.getMicrosecondLength() / 1000000;
        System.out.println(songLength / 60 + " : Minutes");
        // Clip play = AudioSystem.getClip();
        while (pre != 4)
        {
            System.out.println("*PRESS 1 TO PAUSE*,*PRESS 2 TO RESUME* ,*PRESS 3 TO RE-PLAY*,*PRESS 4 TO STOP AND QUIT*,5.FOR NEXT SONG,6.FOR PREVIOUS SONG");
            pre = s.nextInt();
            switch (pre)
            {
                case 1:
                    pause = clip.getMicrosecondPosition();
                    length = clip.getMicrosecondLength();
                    System.out.println("Played seconds : " + pause / 1000000);
                    System.out.println("Remaining seconds:" + (length - pause) / 1000000);
                    clip.stop();

                    break;
                case 2:
                    clip.setMicrosecondPosition(pause);
                    System.out.println("Remaining seconds :" + (length - pause) / 1000000);
                    clip.start();
                    break;
                case 3:
                    clip.setMicrosecondPosition(0);
                    break;
                case 4:
                    clip.stop();
                    clip.close();
                    return 0;

                case 5:
                    clip.stop();
                    clip.close();
                    return 1;

                case 6:
                    clip.stop();
                    clip.close();
                    return 2;
            }
        }
        return 0;
    }
    public int playAudio2(double a) throws SQLException, ClassNotFoundException, UnsupportedAudioFileException, IOException, LineUnavailableException {
        long pause = 0L;

        long length = 0L;
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/jukebox","root","toor1234");
        Statement state = con.createStatement();
        System.out.println("song Id:"+a);
        ResultSet result = state.executeQuery("Select URL from songs where songID=" + a);
        result.next();
        String path = result.getString(1);
        AudioInputStream audio = AudioSystem.getAudioInputStream(new File(path).getAbsoluteFile());
        Clip clip = AudioSystem.getClip();
        clip.open(audio);
        clip.start();
        int pre = 0;
        long songLength = clip.getMicrosecondLength() / 1000000;
        System.out.println(songLength / 60 + " : Minutes");
        // Clip play = AudioSystem.getClip();
        while (pre != 4)
        {
            System.out.println("*PRESS 1 TO PAUSE*,*PRESS 2 TO RESUME* ,*PRESS 3 TO RE-PLAY*,*PRESS 4 TO STOP AND QUIT*,5.FOR NEXT SONG,6.FOR PREVIOUS SONG");
            pre = s.nextInt();
            switch (pre)
            {
                case 1:
                    pause = clip.getMicrosecondPosition();
                    length = clip.getMicrosecondLength();
                    System.out.println("Played seconds : " + pause / 1000000);
                    System.out.println("Remaining seconds:" + (length - pause) / 1000000);
                    clip.stop();

                    break;
                case 2:
                    clip.setMicrosecondPosition(pause);
                    System.out.println("Remaining seconds :" + (length - pause) / 1000000);
                    clip.start();
                    break;
                case 3:
                    clip.setMicrosecondPosition(0);
                    break;
                case 4:
                    clip.stop();
                    clip.close();
                    return 0;

                case 5:
                    clip.stop();
                    clip.close();
                    return 1;

                case 6:
                    clip.stop();
                    clip.close();
                    return 2;
            }
        }
        return 0;
    }




}
