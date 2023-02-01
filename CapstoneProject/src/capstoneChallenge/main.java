package capstoneChallenge;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class main {

    public static void main(String[] args) throws ClassNotFoundException, SQLException, UnsupportedAudioFileException, LineUnavailableException, IOException
    {
        int exit=1;
        do {
        System.out.println("========================================================================================");
        System.out.println("========================================*::WELCOME::*=====================================");
        System.out.println("========================================================================================");
        System.out.println("     " + "        PRESS 1 TO SIGN IN || PRESS 2 TO REGISTER    " + "        ");
        Scanner sc = new Scanner(System.in);
        Scanner sca = new Scanner(System.in);
        int choice;
        choice = sc.nextInt();
        boolean check = false;
        welcome wel = new welcome();
        int count=0;
        int result=1;
        Set checkSet=new HashSet<>();
        Set check2=new HashSet();
        Set check3=new HashSet();


            if (choice == 1)
            {
                System.out.println("ENTER USER ID ");
                int id = sc.nextInt();
                System.out.println("ENTER USER PASSWORD ");
                String passWord = sca.nextLine();
                int userId;
                String UserPassword;
                welcome ob = new welcome(id, passWord);
                //new userDetails(id, passWord);
                Connection a = ob.getConnection();
                Statement state = a.createStatement();
                ResultSet resultSet = state.executeQuery("select * from userDetails");
                while (resultSet.next()) {
                    userId = resultSet.getInt(1);
                    UserPassword = resultSet.getString(3);
                    if (id == userId && passWord.equalsIgnoreCase(UserPassword)) {
                        check = true;
                    }
                }
                int ex = 0;
                do {
                    if (check) {
                        ob.userChoice();
                        System.out.println();
                        int preference = sca.nextInt();
                        if (preference == 1) {
                            ob.Menu();
                            System.out.println("Select Song number to play from given list");
                            int sNo = sca.nextInt();
                            ResultSet a1 = state.executeQuery("select songId from songs");
                            while (a1.next()) {
                                checkSet.add(a1.getInt(1));
                            }
                            if (checkSet.contains(sNo)) {
                                ResultSet re1 = state.executeQuery("select songId,SongName from songs");
                                while (re1.next()) {
                                    count++;//9
                                }
                                int i = 0;
                                int[] list = new int[count];//[2,0,0,0,0,0,0,0,0]
                                ResultSet re2 = state.executeQuery("select songId from songs where songId=" + sNo);
                                while (re2.next()) {
                                    list[i] = re2.getInt(1);
                                    i++;
                                }
                                for (int j = 0; j < list.length && result != 0; j++)//1
                                {
                                    if (sNo == list[0]) {
                                        result = ob.playAudio(sNo);
                                    }
                                    if (result == 1) {
                                        if (sNo < list.length) {
                                           // System.out.println(sNo);
                                            sNo = sNo + 1;
                                            result = ob.playAudio(sNo);
                                        } else {
                                            sNo = 1;
                                            j = 0;
                                            result = ob.playAudio(sNo);
                                        }
                                    } else if (result == 2) {
                                        if (sNo >= 2) {
                                            sNo = sNo - 1;
                                            result = ob.playAudio(sNo);
                                        } else {
                                            sNo = 9;
                                            j = 0;
                                            result = ob.playAudio(sNo);
                                        }
                                    }
                                }
                            } else {
                                System.out.println("INVALID INPUT");
                            }

                        } else if (preference == 2) {
                            ob.artistName();
                            Set hashName=new HashSet();
                            ResultSet res2=state.executeQuery("Select ArtistName from songs ");
                            while(res2.next())
                            {
                                hashName.add(res2.getString(1));
                            }
                            System.out.println("ENTER ARTIST NAME TO VIEW SONGS");
                            String name = sca.next();
                            ResultSet res5 = null;
                            String name1="";
                            try {
                                res5 = state.executeQuery("Select Artistname from songs where ArtistName like'%" + name + "%'");
                                res5.next();
                                name1=res5.getString(1);
                            }catch(Exception E)
                            {
                                System.out.println("PLEASE ENTER VALID NAME");
                            }
                            if(hashName.contains(name1))
                            {
                                ob.artistSongs(name1);
                            }

                        } else if (preference == 3) {
                            ob.displayGenre();
                            Set hashName=new HashSet();
                            System.out.println();
                            ResultSet res2=state.executeQuery("Select Genre from songs ");
                            while(res2.next())
                            {
                                hashName.add(res2.getString(1));
                            }
                            System.out.println("ENTER GENRE NAME TO VIEW SONGS");
                            String name = sca.next();
                            ResultSet res5 ;
                            String name1 ="";
                            try {
                                res5 = state.executeQuery("select Genre from songs where Genre='" + name + "'");
                                res5.next();
                                name1 = res5.getString(1);
                            }catch(Exception e)
                            {
                                System.out.println("ENTER VALID GENRE NAME");
                            }
                            if(hashName.contains(name1))
                            {
                            ob.genreSongs(name1);}

                        } else if (preference == 4) {
                            System.out.println("Enter 1 to view playList Enter 2 to create");
                            int chc = sc.nextInt();
                            if (chc == 1) {
                                ob.displayPlayList(id);
                            }
                            if (chc == 2) {
                                ob.playListCreate();
                            }
                        } else if (preference == 5)
                        {
                            ob.Podcast();
                            System.out.println("SELECT PODCAST NUMBER TO VIEW EPISODES");
                            int pEp = sca.nextInt();
                            ResultSet set5=state.executeQuery("select podcastId from podcast");
                            while(set5.next())
                            {
                                check2.add(set5.getInt(1));
                            }
                            if(check2.contains(pEp)) {
                                if (pEp == 1) {
                                    ob.podcastEpisode(pEp);
                                } else if (pEp == 2) {
                                    ob.podcastEpisode(pEp);
                                } else if (pEp == 3) {
                                    ob.podcastEpisode(pEp);
                                }
                            }
                        }

                    } else {
                        System.out.println("INVALID USER");
                    }
                    System.out.println("PRESS 01 TO CONTINUE OR 0 TO EXIT WINDOW");
                    ex = sca.nextInt();
                } while (ex == 1);
            }
            else if (choice == 2)
            {
                wel.createAccount();
            }
            System.out.println("TO ENTER SIGN IN PAGE PRESS 1 OR 2 TO EXIT");
            exit=sca.nextInt();
        }while(exit==1);
    }
}
