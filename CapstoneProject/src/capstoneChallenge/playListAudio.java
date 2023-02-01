package capstoneChallenge;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class playListAudio extends  welcome
{
    public void playMyPlaylist() throws SQLException, ClassNotFoundException {
        Connection a = getConnection();
        Statement state = a.createStatement();

    }
}
