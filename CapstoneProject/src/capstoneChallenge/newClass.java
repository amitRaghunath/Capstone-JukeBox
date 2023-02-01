package capstoneChallenge;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class newClass
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

    public static void main(String[] args) throws SQLException, UnsupportedAudioFileException, LineUnavailableException, IOException, ClassNotFoundException {
        Scanner s=new Scanner(System.in);
        newClass ip=new newClass();
        Connection b = DriverManager.getConnection("jdbc:mysql://localhost:3306/jukebox","root","toor1234");
        Statement state = b.createStatement();
        int result=1;
        String playName="myplay123";
        ResultSet res1=state.executeQuery("select distinct songId,songName,podcastEpisode,podcastEpisodeName from playList where ListName like'%"+playName+"%'");
        while(res1.next())
        {
            System.out.println(res1.getInt(1)+" "+res1.getString(2));
        }
        int status=0;
        ResultSet res2=state.executeQuery("select distinct songId,songName,podcastEpisode,podcastEpisodeName from playList where ListName like'%"+playName+"%'");
        while(res2.next())
        {
            status++;
        }
        int[] list=new int[status];
        int con=0;
        ResultSet res3=state.executeQuery("select distinct songId,songName,podcastEpisode,podcastEpisodeName from playList where ListName like'%"+playName+"%'");
        while(res3.next())
        {
            list[con]=res3.getInt(1);
            con++;
            System.out.println(res3.getInt(1));
        }
        System.out.println("Select Song number to play from given list");
        int a = s.nextInt();
        for(int i=0;i<list.length&&result!=0;)//0
        {
            System.out.println(list[i]);
            if(a==list[i])
            {
                result= ip.playAudio(a);

            }
            if(result==1)
            {
                if(i==list.length-1)
                {
                    i=-1;
                    a=list[0];
                }
                else
                {
                    a=list[i+1];
                }
                i++;
            }
            if(result==2)
            {
                if(i==0)
                {
                    i=list.length;
                    a=list[list.length-1];
                }
                else
                {
                    a=list[i-1];
                }
                i--;
            }
        }
    }
}
