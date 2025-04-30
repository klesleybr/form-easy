package com.formeasy.model;

public class User {
	private static String name;
	private static String imageUrl;
	private static String email;
	private static Boolean authenticate = false;
	
	
	public User() {	
		
	}
	
	public static String getNome() {
		return name;
	}
	public static void setNome(String nome) {
		User.name = nome;
	}
	
	public static String getFotoPerfil() {
		return imageUrl;
	}
	public static void setFotoPerfil(String fotoPerfil) {
		User.imageUrl = fotoPerfil;
	}
	
	public static String getEmail() {
		return email;
	}
	public static void setEmail(String email) {
		User.email = email;
	}
	public static void setAuthenticate(Boolean status) {
		User.authenticate = status;
	}
	public static Boolean getAuthenticate() {
		return authenticate;
	}
	
	public static void limpar() {
		name = null;
		email = null;
		imageUrl = null;
		authenticate = false;
	}
}
