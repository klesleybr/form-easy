package com.formeasy.model;

public class Usuario {
	private String nome;
	private String fotoPerfil;
	private String email;
	
	
	public Usuario(String nome, String fotoPerfil, String email) {	
		this.nome = nome;
		this.fotoPerfil = fotoPerfil;
		this.email = email;
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
}
