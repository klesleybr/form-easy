package com.formeasy.model;

public class Usuario {
	private String atHash = new String(); // Acces Token Hash
	private String sub;// ID do usuário no google 
	private String nome;
	private String fotoPerfil;
	private String email;
	private boolean emailVerificado;
	
	public Usuario(String atHash, String sub, String nome, String fotoPerfil, String email, boolean emailVerificado) {
		this.atHash = atHash;
		this.sub = sub;
		this.nome = nome;
		this.fotoPerfil = fotoPerfil;
		this.email = email;
		this.emailVerificado = emailVerificado;
	}
	
	public String getAtHash() {
		return atHash;
	}
	public void setAtHash(String atHash) {
		this.atHash = atHash;
	}
	
	public String getSub() {
		return sub;
	}
	public void setSub(String sub) {
		this.sub = sub;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getFotoPerfil() {
		return fotoPerfil;
	}
	public void setFotoPerfil(String fotoPerfil) {
		this.fotoPerfil = fotoPerfil;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public boolean getEmailVerificado() {
		return emailVerificado;
	}
	public void setEmailVerificado(boolean emailVerificado) {
		this.emailVerificado = emailVerificado;
	}
}
