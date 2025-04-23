package com.formeasy.service;

import com.formeasy.domain.Usuario;
import com.formeasy.domain.UsuarioRole;
import com.formeasy.repository.UsuarioRepository;
import com.google.api.services.people.v1.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class RegistroService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private FormEasyService formEasyService;

    /**
     * Registra um novo usuário no sistema.
     *
     * @param login O login (e-mail) do usuário.
     * @param senha A senha fornecida pelo usuário.
     * @throws IllegalArgumentException Se o usuário já existir.
     */
    public Usuario registrarUsuarioGoogle(@RequestBody String authCode) throws Exception {
        
    	Person userInfo = formEasyService.getUserInfoFromAuthCode(authCode);

        if (userInfo == null || userInfo.getEmailAddresses() == null || userInfo.getEmailAddresses().isEmpty()) {
            throw new IllegalArgumentException("Não foi possível obter o e-mail do usuário.");
        }

        String login = userInfo.getEmailAddresses().get(0).getValue();

        // Verifica se o usuário já está registrado
        Usuario usuario = (Usuario) usuarioRepository.findByLogin(login);

        if (usuario != null) {
            return usuario; // Usuário já registrado
        } else {
        	
            // Criar novo usuário
            Usuario newUsuario = new Usuario();
            newUsuario.setLogin(login);
            newUsuario.setPassword("google-auth2"); // Senha aleatória segura
            newUsuario.setRole(UsuarioRole.USER); // Papel padrão

            return usuarioRepository.save(newUsuario);
        }
    }
    
}

