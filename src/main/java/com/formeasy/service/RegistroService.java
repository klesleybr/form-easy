package com.formeasy.service;

import com.formeasy.domain.Usuario;
import com.formeasy.domain.UsuarioRole;
import com.formeasy.repository.UsuarioRepository;
import com.google.api.services.people.v1.model.Person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Service
public class RegistroService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
	private PasswordEncoder passwordEncoder;
    
    @Autowired
    private FormEasyService formEasyService;

    /**
     * Registra um novo usuário no sistema.
     *
     * @param login O login (e-mail) do usuário.
     * @param senha A senha fornecida pelo usuário.
     * @throws IllegalArgumentException Se o usuário já existir.
     */
    public Usuario registrarUsuario(@RequestBody String login, String password){

        // Verifica se o usuário já está registrado
        Usuario usuario = usuarioRepository.findByLogin(login);

        if (usuario != null) {
            return usuario; // Usuário já registrado
        } else {        	
            // Criar novo usuário
            Usuario newUsuario = new Usuario();
            newUsuario.setLogin(login);
            newUsuario.setPassword(passwordEncoder.encode(password)); // Senha aleatória segura
            newUsuario.setRole(UsuarioRole.USER); // Papel padrão

            return usuarioRepository.save(newUsuario);
        }
    }  
}


