package com.formeasy.model;

public class Email {
	private String assunto;
	private String descricao;
	    
	public Email(String assunto, String descricao) {
		this.assunto = assunto;
	    this.descricao = descricao;
	}
	    
	public String getAssunto() {
	    return assunto;
	}
	    
	public String getDescricao() {
	    return descricao;
    }

}
