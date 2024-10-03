package com.formeasy.controller;

import java.util.Map;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.formeasy.model.Usuario;

@RestController
@RequestMapping("/")
public class AuthenticationGoogleController {
	@GetMapping
	public Map<String,Object> getDadosUsuario(OAuth2AuthenticationToken oAuth2AuthenticationToken){
		Map<String, Object> atributosUsuario = oAuth2AuthenticationToken.getPrincipal().getAttributes();
		
		String atHash = (String) atributosUsuario.get("at_hash");
		String sub = (String) atributosUsuario.get("sub");
		String fotoPerfil = (String) atributosUsuario.get("picture");
		String nome = (String) atributosUsuario.get("name");
		String email = (String) atributosUsuario.get("email");
		boolean emailVerificado = (boolean) atributosUsuario.get("email_verified");
		
		Usuario usuario = new Usuario(atHash, sub, nome, fotoPerfil, email, emailVerificado);
		
		exibirDadosUsuario(usuario);
		
		return atributosUsuario;
	}
	
	private void exibirDadosUsuario(Usuario usuario) { // Teste de terminal
		System.out.println("================= Exibindo informações do usuário ===============");
		System.out.println("|| ID do usuário no google: " + usuario.getSub());
		System.out.println("|| Access Token Hash: " + usuario.getAtHash());
		System.out.println("|| Nome: " + usuario.getNome());
		System.out.println("|| Email: " + usuario.getEmail());
		System.out.print("|| Email Verificado? >> ");
		System.out.println(usuario.getEmailVerificado() ? "SIM" : "NÃO");
		System.out.println("|| Foto de Perfil: " + usuario.getFotoPerfil());
	}
}
