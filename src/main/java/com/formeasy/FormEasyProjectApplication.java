package com.formeasy;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

import javafx.application.Application;

@EntityScan("com.formeasy")
@ComponentScan("com.formeasy")
@SpringBootApplication
public class FormEasyProjectApplication {
	
	public static void main(String[] args) {
		Application.launch(FormEasyProjectApplicationJavaFX.class, args);
	}

}