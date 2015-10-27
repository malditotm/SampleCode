package mx.com.webtrack.qbo.webservices.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlType;

import mx.com.webtrack.qbo.to.Usuario;

@XmlType(propOrder={"userId","user","userValidFlag","userValidString"})
public class UserVo implements Serializable {
	private static final long serialVersionUID = 6288641853418227644L;
	
	private Integer userId;
	private String user;
	private boolean userValidFlag;
	private String userValidString;
	
	public UserVo(){
	}
	
	public UserVo(Usuario usuario) {
		this.userId = usuario.getIdUsuario();
		this.user = usuario.getUsuario();
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
	public boolean isUserValidFlag() {
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
	}
	@Override
	public String toString() {
		return "UserVo [userId=" + userId + ", user=" + user + "]";
	}
}
