package connectDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainDAO {

	private Connection con;
	PreparedStatement pstmt = null;
	ResultSet rs = null; 

	public MainDAO() throws ClassNotFoundException, SQLException {
		con = new DBConnectClass().getConnection();
	}
	
	//id,pw »Æ¿Œ
	public boolean memberCheck(String ID, String PWD) throws SQLException {
		boolean check = false;
		String resultId = null;
		String resultPw = null;
		String sql = "SELECT ID, PWD FROM roomchat WHERE ID = ?";
		
		pstmt = con.prepareStatement(sql);
		pstmt.setString(1, ID);
		rs = pstmt.executeQuery();
		
		while(rs.next()) {
			resultId = rs.getString("ID");
			resultPw = rs.getString("PWD");
		}
		if(ID.equals(resultId)&&PWD.equals(resultPw)) {
			return check = true;
		}
		return check;
	}
	
	public String getNickname(String id) throws SQLException {
		String nickname = null;
		String sql = "SELECT nick FROM roomchat WHERE ID = ?";
		
		pstmt = con.prepareStatement(sql);
		pstmt.setString(1, id);
		rs = pstmt.executeQuery();
		
		if(rs.next())
			id = rs.getString(1);
		return id;
	}
	
}
