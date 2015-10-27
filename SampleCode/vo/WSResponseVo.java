package mx.com.webtrack.qbo.webservices.vo;

import java.io.Serializable;

public class WSResponseVo implements Serializable {
	private static final long serialVersionUID = -6533767889236425226L;
	
	private Integer code;
	private String description;
	
	public WSResponseVo() {
		
	}
	
	public WSResponseVo(Integer code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String toString(){
		String s = "Code: #" + code + " ,Description: " + description;
		return s;
	}
}
