package com.formeasy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.formeasy.entity.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	Optional<Usuario> findByLogin(String login);
}


