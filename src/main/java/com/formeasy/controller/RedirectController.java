package com.formeasy.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class RedirectController {	
	
	public void loadNewStage(String title, String pathToView) throws IOException {		
		Stage newStage = new Stage();

		Parent root = FXMLLoader.load(getClass().getResource(pathToView));
		Scene newScene = new Scene(root);
		
		Image icon = new Image(getClass().getResourceAsStream("/images/logo-quadrada2.png"));
    	newStage.getIcons().add(icon);
		newStage.setTitle(title);
		newStage.resizableProperty().setValue(Boolean.TRUE);
		newStage.setMaximized(true);
		newStage.setScene(newScene);
		
		// Interceptação de fechamento...
		newStage.setOnCloseRequest(event ->{
			event.consume(); // Impede o fechamento imediato
			try {
				sair();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		newStage.show();
	}
	
	public void loadWebViewStage(String title, String pathToHtml) throws IOException {
	    Stage newStage = new Stage();
	    newStage.initModality(Modality.APPLICATION_MODAL);

	    WebView webView = new WebView();
	    WebEngine webEngine = webView.getEngine();

	    URL url = getClass().getResource(pathToHtml);
	    if (url != null) {
	        webEngine.load(url.toExternalForm());
	    } else {
	        throw new IOException("Arquivo HTML não encontrado: " + pathToHtml);
	    }

	    Scene newScene = new Scene(webView);
	    
	    Image icon = new Image(getClass().getResourceAsStream("/images/logo-quadrada2.png"));
	    newStage.getIcons().add(icon);
	    newStage.setTitle(title);
	    newStage.resizableProperty().setValue(Boolean.TRUE);
		newStage.setMaximized(true);
	    newStage.setResizable(true);
	    newStage.setScene(newScene);

	    newStage.show();
	}

	
	public void closeCurrentStage(Node viewElement) {
		Stage currentStage = (Stage) viewElement.getScene().getWindow();
		currentStage.close();
	}
	
	private void sair() throws Exception {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    	
    	alert.setTitle("Confirmação de Saída");
    	alert.setHeaderText(null);
    	alert.setContentText("Tem certeza que deseja sair?");
    	
    	Image logo = new Image(getClass().getResourceAsStream("/images/logo-quadrada2.png"));
    	ImageView logoView = new ImageView(logo);
    	logoView.setFitHeight(20);
    	logoView.setFitWidth(20);
    	
    	alert.setGraphic(logoView);
    	
    	Optional<ButtonType> result = alert.showAndWait();
    	
    	if(result.isPresent() && result.get() == ButtonType.OK) {
    		System.exit(0);
    	}
	}
}
