package com.wymessi.vo.user;
/**
 * 
 * @author 王冶
 *
 */
public class LoginVo {

	private int role; //  0：系统工作人员 ，1：评审专家，2：项目申请者
	private String username;
	private String password;

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}