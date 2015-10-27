package mx.com.webtrack.qbo.webservices.vo;

import java.io.Serializable;

import mx.com.webtrack.qbo.to.Usuario;

public class UserWSVo implements Serializable {
	private static final long serialVersionUID = 6288641853418227644L;
	
	private Integer userId;
	private String user;
	private String password;
	private String type;
	private String ip;
	
	public UserWSVo(){
	}
	
	public UserWSVo(Usuario usuario) {
		this.userId = usuario.getIdUsuario();
		this.user = usuario.getUsuario();
		this.password = usuario.getContrasena();
	}

	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	/*public boolean isUserValidFlag() {
		return userValidFlag;
	}
	public void setUserValidFlag(boolean userValidFlag) {
		this.userValidFlag = userValidFlag;
	}
	public String getUserValidString() {
		return userValidString;
	}
	public void setUserValidString(String userValidString) {
		this.userValidString = userValidString;
	}/**/
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String toString(){
		String s = "UserWS[User: " + user + " #Password: " + password + " #Type::" + type + "]";
		return s;
	}
}
