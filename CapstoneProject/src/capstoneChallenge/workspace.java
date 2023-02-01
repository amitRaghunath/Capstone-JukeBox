package capstoneChallenge;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class workspace
{
    Scanner s=new Scanner(System.in);
    public int playAudio(int a) throws SQLException, ClassNotFoundException, UnsupportedAudioFileException, IOException, LineUnavailableException {
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
    public void playPrev(int a)
    {

    }
    public static void main(String[] args) throws SQLException, ClassNotFoundException, UnsupportedAudioFileException, LineUnavailableException, IOException {
        Scanner sca = new Scanner(System.in);
        workspace ws=new workspace();
        welcome wel=new welcome();
        Connection a = wel.getConnection();
        Statement state = a.createStatement();
        ResultSet re = state.executeQuery("select songId,SongName from songs");

        while(re.next())
        {
            System.out.println(re.getInt(1)+" "+re.getString(2));
        }
        int cId=sca.nextInt();//5

        int count=0;
       // int count1=0;
        ResultSet re1 = state.executeQuery("select songId,SongName from songs");
        while(re1.next())
        {
            count++;//9
        }
        int result=1;
        int i=0;
        int[] list=new int[count];
        ResultSet re2 = state.executeQuery("select songId from songs where songId="+cId);
        while (re2.next())
        {
            list[i]=re2.getInt(1);
            i++;
        }

       System.out.println("list 1 length"+list.length);
        for(int j=0;j<list.length&&result!=0;j++)//1
        {
            System.out.println(j);
            if(cId==list[0])
            {

                result=ws.playAudio(cId);
            }
            if(result==1)
            {
                if(cId<list.length)
                {
                    System.out.println(cId);
                    cId=cId+1;
                    result=ws.playAudio(cId);
                }
                else
                {
                   cId=1;
                   j=0;
                   result=ws.playAudio(cId);
                }
            }
            else if(result==2)
            {
                if(cId>=2)
                {
                    cId=cId-1;
                    result=ws.playAudio(cId);
                }
                else
                {
                    cId=9;
                    j=0;
                    result=ws.playAudio(cId);
                }
            }

        }
    }
}
