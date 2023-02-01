package CapstoneWorkSpace;

import capstoneChallenge.welcome;
import capstoneChallenge.workspace;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class impPlayList
{
    public static void main(String[] args) throws SQLException, ClassNotFoundException, UnsupportedAudioFileException, LineUnavailableException, IOException {
       /* Scanner sc = new Scanner(System.in);
        Scanner sca = new Scanner(System.in);
        workspace wa=new workspace();
        boolean check = false;
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
        Set checkSet=new HashSet<>();

        ResultSet rea = state.executeQuery("Select songId from songs");
        while(rea.next())
        {
            checkSet.add(rea.getInt(1));
        }
        System.out.println(checkSet);
        ResultSet a1=state.executeQuery("select songId,SongName from songs");
        while(a1.next())
        {
            System.out.println(a1.getInt(1)+" "+a1.getString(2));
        }
        System.out.println("Select id");
        int sel=sc.nextInt();
        if(checkSet.contains(sel))
        {
            System.out.println("valid input");
        }
        else
        {
            System.out.println("INVALID");
        }*/
      List demo=new ArrayList();
        demo.add(1);
        demo.add(2);
        demo.add(3);
        ListIterator temp=demo.listIterator();

        while(temp.hasNext())
        {
            System.out.println(temp.next());
        }
        temp.previous();
        System.out.println("--------------");
        System.out.println(temp.previous());
        Set hash=new TreeSet();

        welcome ob=new welcome();
        Connection a = ob.getConnection();
        Statement state = a.createStatement();
        int aa=1;

       ResultSet set55= state.executeQuery("select SongId from songs");
        while(set55.next())
        {
           demo.add(set55.getInt(1));
        }
        System.out.println(demo);


    }
}
