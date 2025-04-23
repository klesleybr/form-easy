package com.formeasy.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.formeasy.domain.LoginRequestDTO;
import com.formeasy.domain.ResponseDTO;
import com.formeasy.domain.Usuario;
import com.formeasy.service.AutenticacaoService;
import com.formeasy.service.FormEasyService;
import com.formeasy.service.RegistroService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("auth")
public class AuthController {
	
	@Autowired
    private AutenticacaoService autenticacaoService;

    @Autowired
    private RegistroService registroService;
	
	@Autowired
	private FormEasyService formEasyService;
	
	@PostMapping("/login")
	public ResponseEntity login(@RequestBody @Valid LoginRequestDTO data) throws IOException, GeneralSecurityException {
		
		try {
            // Autentica o usuário
            Usuario usuario = autenticacaoService.autenticarUsuario(data.login(), data.password());
            
            // Gera o token
            String token = formEasyService.getAccessToken();

            // Retorna o token
            return ResponseEntity.ok(new ResponseDTO(token, usuario.getLogin(), usuario.getPassword()));
        } catch (AutenticacaoService.UsuarioNaoEncontradoException | AutenticacaoService.SenhaInvalidaException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO(null, null, null));
        }
    }
	
	@PostMapping("/registro")
	public ResponseEntity <ResponseDTO> register(@RequestBody @Valid String authCode) {
		
	     try {
	         	// Registra o usuário usando o código de autorização do Google
	            Usuario usuario = registroService.registrarUsuarioGoogle(authCode);

	            // Gera o token
	            String token = formEasyService.getAccessToken();

	            // Retorna o token
	            return ResponseEntity.ok(new ResponseDTO(token, usuario.getLogin(), usuario.getPassword()));
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(null, null, null));
	        }
	    }
    }



