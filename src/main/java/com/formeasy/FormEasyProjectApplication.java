package com.formeasy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

import javafx.application.Application;

//Eu vou adicionar esses dois para testar:
@EntityScan("com.formeasy")
@ComponentScan("com.formeasy")
// --------------------------------------

@SpringBootApplication
public class FormEasyProjectApplication {

	public static void main(String[] args) {
		// SpringApplication.run(FormEasyProjectApplication.class, args);
		
		// Alteração: adição do Application (passar o controle para a aplicação JAVAFX)
		Application.launch(FormEasyProjectApplicationJavaFX.class, args);
	}

}
