package connectDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FindDAO extends chatCon{
	
	private Connection con;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	public FindDAO() throws ClassNotFoundException, SQLException {
		con = new DBConnectClass().getConnection();
	}
	
	public String getId(String inputName, String inputPhone) throws SQLException {
		String id = null;
		String sql = "SELECT ID FROM roomchat WHERE NAME = ? AND phone = ?";
		
		pstmt = con.prepareStatement(sql);
		pstmt.setString(1, inputName);
		pstmt.setString(2, inputPhone);
		rs = pstmt.executeQuery();
		
		if(rs.next()) 
			id = rs.getString(1);
		return id;
	}
	public String getPWQ(String inputName, String inputPhone) throws SQLException {
		String PWQ = null;
		String sql = "SELECT PWQ FROM roomchat WHERE NAME = ? AND phone = ?";
		
		pstmt = con.prepareStatement(sql);
		pstmt.setString(1, inputName);
		pstmt.setString(2, inputPhone);
		rs = pstmt.executeQuery();
		
		if(rs.next()) 
			PWQ = rs.getString(1);
		return PWQ;
	}
	
	public String getPwa(String inputPWQ) throws SQLException {
		String pwa = null;
		String sql = "SELECT PWA FROM roomchat WHERE PWQ = ?";
		
		pstmt = con.prepareStatement(sql);
		pstmt.setString(1, inputPWQ);
		rs = pstmt.executeQuery();
		
		if(rs.next())
			pwa = rs.getString(1);
		return pwa;
	}

}