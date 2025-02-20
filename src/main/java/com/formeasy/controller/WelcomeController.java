package com.formeasy.controller;

import java.io.IOException;
import java.util.Optional;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;


public class WelcomeController {
	RedirectController redirect = new RedirectController();

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
    public void inicialize() {
    	btnSair.setOnAction(e-> Sair());
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
   		 Platform.exit();
   	 	}else {
   		 System.out.println("Saída cancelada");
   	    }
    }
    
    @FXML
    void onClickBtnOptionAnalyze(ActionEvent event) throws IOException {
    	redirect.loadNewStage("", "ShowAnswersView.fxml");
    	redirect.closeCurrentStage(btnOptionAnalyze);
    }

    @FXML
    void onClickBtnOptionSend(ActionEvent event) {
    	try {
			redirect.loadNewStage("", "EmailView.fxml");
			redirect.closeCurrentStage(btnOptionSend);
		} catch (IOException e) {
			e.printStackTrace();
		}

    }

}
