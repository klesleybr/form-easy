package com.formeasy.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;



import org.springframework.stereotype.Component;

import com.formeasy.model.Usuario;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;


// O JavaFx Weaver conecta os dois mundos, inicialmente opostos: JAVAFX E SPRING.
@Component
@FxmlView("ViewLoginGoogle.fxml")
public class LoginGoogleController {
	
	@Autowired
	private AuthenticationGoogleController authenticationGoogleController;
	
    @FXML
    private Button btnLoginGoogle;

    @FXML
    private WebView webViewOAuth2Google;   
    	
    @FXML
    void onClickLoginGoogle(ActionEvent event) throws IOException{
    	System.out.println("Botão clicado");
    	
    	// http://localhost:8080/    	
    	abrirTelaDeLogin("http://localhost:8080/");   
    }
    
    void initializite() { 	
	    Map<String,Object> atributosUsuario = authenticationGoogleController.getDadosUsuario(null);
	    
	    String atHash = (String) atributosUsuario.get("at_hash");
	    String sub = (String) atributosUsuario.get("sub");
		String fotoPerfil = (String) atributosUsuario.get("picture");
		String nome = (String) atributosUsuario.get("name");
		String email = (String) atributosUsuario.get("email");
		boolean emailVerificado = (boolean) atributosUsuario.get("email_verified");
		
		Usuario usuario = new Usuario(atHash, sub, nome, fotoPerfil, email, emailVerificado);
    }
    
    void abrirTelaDeLogin(String url) {
    	WebView view = new WebView();
    	WebEngine engine = view.getEngine();
    	
    	BorderPane root = new BorderPane(view);
    	
    	TextField redirecionamentoUrl = new TextField(url);
    	redirecionamentoUrl.setOnAction(event -> engine.load(redirecionamentoUrl.getText()));
    	// root.setTop(redirecionamentoUrl);
    	
    	engine.locationProperty().addListener((observable, oldValue, newValue) ->
    			redirecionamentoUrl.setText(newValue));
    	
    	Stage stage = new Stage();
    	stage.setTitle("Fazer login com o Google");
    	stage.setScene(new Scene(root, 1000, 600));
    	stage.resizableProperty().setValue(Boolean.FALSE);
    	stage.show();
    	
    	engine.load(redirecionamentoUrl.getText());
    }
}
