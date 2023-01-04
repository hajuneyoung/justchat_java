package connectDB;


public class ChatVO {
	
	private String ID;
	private String PWD;
	private String name;
	private String nick;
	private String phone;
	private String pwq;
	private String pwa;
	
	public ChatVO() {};
	public ChatVO(String ID, String PWD, 
			String name, String nick, String phone,String pwq, String pwa) {
		this.ID = ID;
		this.PWD = PWD;
		this.name = name;
		this.nick = nick;
		this.phone = phone;
		this.pwq = pwq;
		this.pwa = pwa;
	}
	
	public ChatVO(String ID) {
		this.ID = ID;
	}

	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public String getPWD() {
		return PWD;
	}

	public void setPWD(String PWD) {
		this.PWD = PWD;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPwq() {
		return pwq;
	}
	public void setPwq(String pwq) {
		this.pwq = pwq;
	}
	public String getPwa() {
		return pwa;
	}
	public void setPwa(String pwa) {
		this.pwa = pwa;
	}


}
