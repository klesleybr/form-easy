package com.formeasy.controller;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;



import org.springframework.stereotype.Component;

import com.formeasy.model.Usuario;
import com.google.api.services.people.v1.PeopleServiceScopes;
import com.google.api.services.sheets.v4.SheetsScopes;

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

// Link URI --> http://localhost:8080/login/oauth2/code/google

// O JavaFx Weaver conecta os dois mundos, inicialmente opostos: JAVAFX E SPRING.
@Component
@FxmlView("ViewLoginGoogle.fxml")
public class LoginGoogleController {
	@Autowired
	private DashboardController dashboardController;
	
    @FXML
    private Button btnLoginGoogle;   
    	
    @FXML
    void onClickLoginGoogle(ActionEvent event) throws IOException, URISyntaxException{
    	System.out.println("Botão clicado");
    	
    	// http://localhost:8080/    	
    	abrirTelaDeLogin("http://localhost:8080/attributesuser");
    	
    
   
    	
    }
    
    void initializite() { 	
	   /* Map<String,Object> atributosUsuario = authenticationGoogleController.getDadosUsuario(null);
	    
	    String atHash = (String) atributosUsuario.get("at_hash");
	    String sub = (String) atributosUsuario.get("sub");
		String fotoPerfil = (String) atributosUsuario.get("picture");
		String nome = (String) atributosUsuario.get("name");
		String email = (String) atributosUsuario.get("email");
		boolean emailVerificado = (boolean) atributosUsuario.get("email_verified");
		
		Usuario usuario = new Usuario(atHash, sub, nome, fotoPerfil, email, emailVerificado);*/
    }
    
    private void abrirTelaDeLogin(String url) {
    	final String CLIENT_ID = "714573222235-bavel8mv55lm80o9e18gbdo1rms32kfk.apps.googleusercontent.com";
    	final String REDIRECT_URI = "http://localhost:8888/Callback";
    	final List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS, SheetsScopes.DRIVE, PeopleServiceScopes.USERINFO_EMAIL, 
				PeopleServiceScopes.USERINFO_PROFILE);
    	
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
    	
    	
    	// Deixar mais bonito (concatenar Strings)
    	/*String urlAuthentication = "https://accounts.google.com/o/oauth2/auth?access_type=offline&"
    			+ "client_id=714573222235-bavel8mv55lm80o9e18gbdo1rms32kfk.apps.googleusercontent.com&"
    			+ "redirect_uri=http://localhost:8888/Callback&response_type=code&"
    			+ "scope=https://www.googleapis.com/auth/spreadsheets%20https://www.googleapis.com/auth/drive%20"
    			+ "https://www.googleapis.com/auth/userinfo.email%20"
    			+ "https://www.googleapis.com/auth/userinfo.profile"; */
    	
    	String urlAuthentication = "https://accounts.google.com/o/oauth2/auth?access_type=offline&"
                + "client_id=" + CLIENT_ID
                + "&redirect_uri=" + REDIRECT_URI
                + "&response_type=code&scope=" + SCOPES.get(0) + " " + SCOPES.get(1) + " " 
                + SCOPES.get(2) + " " + SCOPES.get(3);
    	
    	// engine.load(redirecionamentoUrl.getText());
    	engine.load(urlAuthentication);
    	
    	engine.locationProperty().addListener((observable, oldValue, newValue) -> {
    		if(newValue.contains("code=")) {
    			String authCode = newValue.split("code=")[1];
    			
    			if(authCode.contains("&")) {
    				authCode = authCode.split("&")[0];
    			}
    			
        		System.out.println("Código de autorização: " + authCode);
        		
        		stage.close();
    		}
    		else {
    			System.out.println("Nenhum código encontrado ainda...");
    		}  
    	});    	
    }
    
  
}
