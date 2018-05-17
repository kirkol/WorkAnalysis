package work_charts;
import java.sql.*;

import javax.swing.*;
public class RCPdatabaseConnection {
	Connection conn=null;
	static String adresSerwera = "192.168.90.123";
	int User = 0;
	public static Connection dbConnector(String user, String pass)
	{
		try {
			System.out.println(user);
			System.out.println(pass);
			Class.forName("org.mariadb.jdbc.Driver");
			Connection conn=DriverManager.getConnection("jdbc:mariadb://"+adresSerwera+"/test",user,pass);
			return conn;
		}catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, e);
			return null;
		}
	}
}



