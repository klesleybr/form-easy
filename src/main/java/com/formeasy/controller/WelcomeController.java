package com.formeasy.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
