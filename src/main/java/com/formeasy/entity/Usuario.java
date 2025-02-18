package com.formeasy.entity;

import java.util.Objects;

import org.springframework.beans.BeanUtils;

import com.formeasy.DTO.UsuarioDTO;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    
	    @Column(nullable = false)
		private String nome;
	    
	    @Column(nullable = false, unique = true)
		private String login;
	    
	    @Column(nullable = false)
	    private String senha;

	    @Column(unique = true, nullable = false)
	    private String email;

	    public Usuario(UsuarioDTO usuario) {
			BeanUtils.copyProperties(usuario, this);
		}
	    
	    public Usuario() {
			
		}

		public Long getId() {
			return id;
		}
		
		public void setId(Long id) {
			this.id = id;
		}
		
		public String getNome() {
			return nome;
		}

		public void setNome(String nome) {
			this.nome = nome;
		}

		public String getLogin() {
			return login;
		}

		public void setLogin(String login) {
			this.login = login;
		}

	    
	    public String getEmail() {
	    	return email;
	    }
	    
	    public void setEmail(String email) {
	    	this.email = email;
	    }
	    
	    public String getSenha() {
	    	return senha;
	    }
	    
	    public void setSenha(String senha) {
	    	this.senha = senha;
	    }
		
	    @Override
		public int hashCode() {
			return Objects.hash(id);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Usuario other = (Usuario) obj;
			return Objects.equals(id, other.id);
		}	
	}


