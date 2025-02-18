package com.formeasy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.formeasy.DTO.AcessDTO;
import com.formeasy.DTO.AuthenticationDTO;
import com.formeasy.security.jwt.JwtUtils;

@Service
public class AuthService {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	public AcessDTO login(AuthenticationDTO authDto) {
		
		try {
		//Cria o mecanismo de credential para o spring
		UsernamePasswordAuthenticationToken userAuth = new UsernamePasswordAuthenticationToken(authDto.getUsername(), authDto.getPassword());
		
		//Prepara o mecanismo para autenticação
		Authentication authentication = authenticationManager.authenticate(userAuth);
		
		//Busca usuário logado
		UserDetailsImpl userAuthenticate  = (UserDetailsImpl)authentication.getPrincipal();
		
		String token = jwtUtils.generateTokenFromUserDetailsImpl(userAuthenticate);
		
		AcessDTO accessDto = new AcessDTO(token);
		
		return accessDto;
		
		}catch(BadCredentialsException e) {
			// TODO LOGIN OU SENHA INVÁLIDOS
		}
		return null;
		
	}
}