package com.formeasy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.formeasy.domain.LoginRequestDTO;
import com.formeasy.domain.RegistroDTO;
import com.formeasy.domain.ResponseDTO;
import com.formeasy.domain.Usuario;
import com.formeasy.security.TokenService;
import com.formeasy.service.AutenticacaoService;
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
	private TokenService tokenService;
	
	@PostMapping("/login")
	public ResponseEntity login(@RequestBody @Valid LoginRequestDTO data) {
		
		try {
            // Autentica o usuário
            Usuario usuario = autenticacaoService.autenticarUsuario(data.login(), data.password());
            
            // Gera o token JWT
            String token = tokenService.generateToken(usuario);

            // Retorna o token
            return ResponseEntity.ok(new ResponseDTO(token, usuario.getLogin(), usuario.getPassword()));
        } catch (AutenticacaoService.UsuarioNaoEncontradoException | AutenticacaoService.SenhaInvalidaException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO(null, null, null));
        }
	}

	@PostMapping("/registro")
	public ResponseEntity <ResponseDTO> register(@RequestBody @Valid RegistroDTO dto) {
		
		try {
            Usuario usuario = registroService.registrarUsuario(dto.getLogin(), dto.getPassword());
            String token = tokenService.generateToken(usuario);

            return ResponseEntity.ok(new ResponseDTO(token, usuario.getLogin(), usuario.getPassword()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(null, null, null));
        }
	    }
}




