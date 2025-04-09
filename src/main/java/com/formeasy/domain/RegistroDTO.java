package com.formeasy.domain;

public class RegistroDTO{
	
	private String login;
	private String password;
	private UsuarioRole role;
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public UsuarioRole getRole() {
		return role;
	}
	public void setRole(UsuarioRole role) {
		this.role = role;
	}
	
	

}
	