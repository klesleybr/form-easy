package com.formeasy.DTO;

public class AcessDTO {
		
	private String token;
		
		//Implementar, retornar o usuário, liberações (authorities)
		
	public AcessDTO(String token) {
		super();
		this.token = token;
	}
		
	public String getToken() {
		return token;
	}
		
	public void setToken(String token) {
		this.token = token;
	}
}
