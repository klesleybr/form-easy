package com.formeasy.controller;

import java.io.IOException;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class RedirectController {
	public void loadNextView(String titleStage, String pathView) throws IOException {
		Stage newStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource(pathView));
		Scene scene = new Scene(root);
		
		newStage.setTitle(titleStage);
		newStage.resizableProperty().setValue(Boolean.FALSE);
		newStage.setScene(scene);
		newStage.show();
	}
}
