package capstoneChallenge;

import com.mysql.cj.xdevapi.Statement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class idCheckTest extends Exception
{
    public idCheckTest(String msg)
    {
        System.out.println("UserId and Password Already Exist");
    }
}
