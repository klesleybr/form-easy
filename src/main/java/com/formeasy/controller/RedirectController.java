package com.formeasy.controller;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


// Classe incompleta

public class RedirectController {	
	public void loadNewStage(String title, String pathToView) throws IOException {
		Stage newStage = new Stage();
	
		Parent root = FXMLLoader.load(getClass().getResource(pathToView));
		Scene newScene = new Scene(root);
		
		Image icon = new Image(getClass().getResourceAsStream("/images/logo-quadrada2.png"));
    	newStage.getIcons().add(icon);
		newStage.setTitle(title);
		newStage.resizableProperty().setValue(Boolean.FALSE);
		newStage.setScene(newScene);
		newStage.show();
	}
	
	public void closeCurrentStage(Node viewElement) {
		Stage currentStage = (Stage) viewElement.getScene().getWindow();
		currentStage.close();
	}
}
