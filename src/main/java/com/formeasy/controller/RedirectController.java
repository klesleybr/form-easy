package com.formeasy.controller;

import java.io.IOException;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.formeasy.FormEasyProjectApplication;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


// Classe incompleta

public class RedirectController {	
	// Novo objeto
	ConfigurableApplicationContext springContext;
	
	
	public void loadNewStage(String title, String pathToView) throws IOException {
		Stage newStage = new Stage();
	
		Parent root = FXMLLoader.load(getClass().getResource(pathToView));
		Scene newScene = new Scene(root);
		
		newStage.setTitle(title);
		newStage.resizableProperty().setValue(Boolean.FALSE);
		newStage.setScene(newScene);
		newStage.show();
	}
	
	
	// Novo método
	@FXML
	public void initialize() {
		String[] args = new String[0];
		
		this.springContext = new SpringApplicationBuilder()
				.sources(FormEasyProjectApplication.class)
				.run(args);
	}
	// Novo método
	public void loadNewSceneWeaver(Node viewElement, Class controller) {
		
		// Em produção...
		Stage atualStage = (Stage) viewElement.getScene().getWindow();
	
	}
}
