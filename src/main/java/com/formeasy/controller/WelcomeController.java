package com.formeasy.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import com.formeasy.FormEasyProjectApplicationJavaFX;
import com.formeasy.security.AuthSession;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;


public class WelcomeController {
	RedirectController redirect = new RedirectController();
	
	@FXML
	private Button btnChangeAccount;
	
    @FXML
    private Button btnOptionAnalyze;

    @FXML
    private Button btnOptionSend;
    
    @FXML
    private Button btnSair;

    @FXML
    private Circle circulo_header;

    @FXML
    private ImageView formeasy_foto;

    @FXML
    private Rectangle header;

    @FXML
    private ImageView mulher_prancheta;

    @FXML
    private Label texto1_header;

    @FXML
    private Label texto2_header;

    @FXML
    private Label texto_sem_formulario;
    
    @FXML
    public void initialize() {
    	btnSair.setOnAction(e-> Sair());
    	
    	String login = AuthSession.getUserLogin();
        texto_sem_formulario.setText("Olá, " + login + ", Seja Bem-vindo ao FormEasy!");
        texto_sem_formulario.setStyle("-fx-font-size: 16px;");
    }
    
    public void Sair() {
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
   	 	alert.setTitle("Confirmação de Saída");
   	 	alert.setHeaderText(null);
   	 	alert.setContentText("Tem certeza que deseja sair?");
   	 	
   	 	Image logo = new Image(getClass().getResourceAsStream("/images/logo-quadrada2.png"));
   	 	ImageView logoView = new ImageView(logo);
   	 	logoView.setFitWidth(20);
   	 	logoView.setFitHeight(20);

   	 	alert.setGraphic(logoView);

   	 	Optional<ButtonType> result = alert.showAndWait();

   	 	if (result.isPresent() && result.get() == ButtonType.OK) {
   	 		System.exit(0);
   	 	}else {
   	 		System.out.println("Saída cancelada");
   	    }
    }
    
    @FXML
    void onClickBtnOptionAnalyze(ActionEvent event) throws IOException {
    	redirect.loadNewStage("Analisar Respostas", "ShowAnswersView.fxml");
    	redirect.closeCurrentStage(btnOptionAnalyze);
    }

    @FXML
    void onClickBtnOptionSend(ActionEvent event) {
    	try {
			redirect.loadNewStage("Enviar Formulário", "EmailView.fxml");
			redirect.closeCurrentStage(btnOptionSend);
		} catch (IOException e) {
			e.printStackTrace();
		}

    }
    
    @FXML
    void onClickBtnChangeAccount(ActionEvent event) throws IOException {
    	File tokensFolder = new File("tokens");
    	
    	if(tokensFolder.exists() && tokensFolder.isDirectory()) {
    		for(File file : tokensFolder.listFiles()) {
    			file.delete();
    		}
    		
    		if(tokensFolder.delete()) {
    			System.out.println("Logout realizado com sucesso!");
    			redirect.loadNewStage("Realizar login", "LoginView.fxml");
    			redirect.closeCurrentStage(btnChangeAccount);
    		}
    		else {
    			System.out.println("Falha ao realizar logout.");
    		}
    	}
    	
    	
    	/*
    	HostServices hostServices = FormEasyProjectApplicationJavaFX.getInstance().getHostServices();
    	hostServices.showDocument("https://accounts.google.com/Logout");
    	
    	Usando este trecho, é possível abrir links no navegador com JavaFx. Observe as mudanças feitas
    	em FormEasyProjectApplicationJavaFX.java
    	*/
    	
    }

}
