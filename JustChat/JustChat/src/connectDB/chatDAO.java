package connectDB;

import java.util.ArrayList;
import java.util.List;

public class chatDAO extends chatCon{

	public chatDAO() {}
	// 회원가입 db insert
	public int SignUpInsert(ChatVO vo) {
		int result = 0;
		try{
			getConn();
			sql = "insert into roomchat (ID,PWD,name,nick,phone,PWQ,PWA) values(?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vo.getID());
			pstmt.setString(2, vo.getPWD());
			pstmt.setString(3, vo.getName());
			pstmt.setString(4, vo.getNick());
			pstmt.setString(5, vo.getPhone());
			pstmt.setString(6, vo.getPwq());
			pstmt.setString(7, vo.getPwa());
			
			
			result = pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally{
			dbClose();
		}
		return result;
	}
	// 회원 아이디 비밀번호 검색, 회원 유무 확인
	public List<ChatVO> getidCheck(String ID){
		List<ChatVO> lst = new ArrayList<ChatVO>();		
		try {
			getConn();
			sql = "select ID from roomchat where ID =?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ID);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				ChatVO vo = new ChatVO();
				vo.setID(rs.getString(1));
				
				lst.add(vo);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}finally {
			dbClose();
		}
		
		return lst;
	}
}