package com.formeasy.service;

import com.formeasy.domain.Usuario;
import com.formeasy.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public Usuario autenticarUsuario(String login, String passwordDigitada) throws UsuarioNaoEncontradoException, SenhaInvalidaException{
        // Buscar o usuário pelo login (email)
        Usuario usuario = usuarioRepository.findByLogin(login);

        if (usuario == null) {
            throw new UsuarioNaoEncontradoException("Usuário não encontrado.");
        }

        System.out.println("Senha salva no banco: " + usuario.getPassword());
        System.out.println("Usuário encontrado: ID = " + usuario.getId() + ", Login = " + usuario.getLogin());
        System.out.println("Senha digitada: " + passwordDigitada);

        // Verificar se a senha fornecida corresponde à senha armazenada
        if (!passwordEncoder.matches(passwordDigitada, usuario.getPassword())) {
            throw new SenhaInvalidaException("Senha incorreta.");
        }

        return usuario; // Autenticação bem-sucedida
    }

    // Exceção para usuário não encontrado
    public static class UsuarioNaoEncontradoException extends RuntimeException {
        public UsuarioNaoEncontradoException(String mensagem) {
            super(mensagem);
        }
    }

    // Exceção para senha inválida
    public static class SenhaInvalidaException extends RuntimeException {
        public SenhaInvalidaException(String mensagem) {
            super(mensagem);
        }
    }
}

