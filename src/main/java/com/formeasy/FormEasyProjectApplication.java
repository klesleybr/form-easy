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
		// Adiciona-se o Application a fim de passar o controle para o JavaFX
		Application.launch(FormEasyProjectApplicationJavaFX.class, args);
	}

}

/* 
 * java --module-path "C:\Program Files\Java\javafx-sdk-22.0.2\lib" 
 * --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.web 
 * -jar formeasyproject02.jar
 */
