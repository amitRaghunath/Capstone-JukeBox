package capstoneChallenge;

import java.sql.*;

public class artist
{
    public Connection getConnection()throws SQLException,ClassNotFoundException
    {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/jukebox","root","toor1234");
    }
    public void artistName()throws SQLException, ClassNotFoundException
    {
        Connection a =getConnection();
        Statement state = a.createStatement();
        ResultSet result= state.executeQuery("select distinct artistName from songs");
        while(result.next())
        {
            System.out.println(result.getString(1));
        }
    }

}
