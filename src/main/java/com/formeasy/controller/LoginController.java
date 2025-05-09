package com.formeasy.controller;

import java.io.IOException;

import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import org.controlsfx.control.Notifications;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.formeasy.model.User;
import com.google.api.services.forms.v1.FormsScopes;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.people.v1.PeopleServiceScopes;
import com.google.api.services.sheets.v4.SheetsScopes;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.rgielen.fxweaver.core.FxmlView;

@Component
@FxmlView("LoginView.fxml")
@Service
public class LoginController{
	RedirectController redirect = new RedirectController();
	
	@FXML
	private Button btnLogin;
	 
    @FXML
    private Button btnLoginGoogle;   
    	
    
    @FXML
    void onClickLogin(ActionEvent event) throws IOException {
    	
    	// O path funcionou somente com "WelcomeView.fxml"
    	// Este método ficará assim apenas para fins de teste.
    	
    	String title = "Menu";
    	String path = "WelcomeView.fxml";
    	redirect.loadNewStage(title, path);
    	redirect.closeCurrentStage(btnLogin);
    }
    
    @FXML
    void onClickLoginGoogle(ActionEvent event) throws IOException, URISyntaxException{
    	System.out.println("Botão clicado!");    	
    	openSuperimposedLoginView("http://localhost:8080/attributesuser"); 	
    }
    
    @FXML
    public void initialize() {
    	
	   
    }
    
    private void openSuperimposedLoginView(String url) {
    	final String CLIENT_ID = "714573222235-bavel8mv55lm80o9e18gbdo1rms32kfk.apps.googleusercontent.com";
    	final String REDIRECT_URI = "http://localhost:8888/Callback";
    	final List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS, SheetsScopes.DRIVE, 
    			PeopleServiceScopes.USERINFO_EMAIL, PeopleServiceScopes.USERINFO_PROFILE, FormsScopes.FORMS_BODY, 
    			GmailScopes.GMAIL_SEND, GmailScopes.GMAIL_READONLY, GmailScopes.GMAIL_MODIFY, 
    			GmailScopes.MAIL_GOOGLE_COM);
    	
    	WebView view = new WebView();    
    	WebEngine engine = view.getEngine();
    	BorderPane root = new BorderPane(view);
    	TextField redirecionamentoUrl = new TextField(url);
    	
    	redirecionamentoUrl.setOnAction(event -> engine.load(redirecionamentoUrl.getText()));    	
    	engine.locationProperty().addListener((observable, oldValue, newValue) ->
    			redirecionamentoUrl.setText(newValue));
    	
    	Stage stage = new Stage();
    	Image icon = new Image(getClass().getResourceAsStream("/images/logo-quadrada2.png"));
    	stage.setTitle("Fazer login com o Google");
    	stage.getIcons().add(icon);
    	stage.setScene(new Scene(root, 1000, 700));
    	stage.resizableProperty().setValue(Boolean.TRUE);
    	stage.show();
		
		String urlAuthentication = "https://accounts.google.com/o/oauth2/auth?access_type=offline&"
                + "prompt=consent&" + "client_id=" + CLIENT_ID
                + "&redirect_uri=" + REDIRECT_URI
                + "&response_type=code&scope=" + String.join("%20", SCOPES);
                
                /* + SCOPES.get(0) + "%20" + SCOPES.get(1) + "%20" 
                + SCOPES.get(2) + "%20" + SCOPES.get(3) + "%20" + SCOPES.get(4) + "%20" + SCOPES.get(5)
                + "%20" + SCOPES.get(6) + "%20" + SCOPES.get(7) + "%20" + SCOPES.get(8);*/
    	
    	engine.load(url);
    	engine.load(urlAuthentication);    	
    	
    	// Se colocar "prompt=consent" após o "type=offline", força o usuário a aceitar os escopos da 
    	// aplicação novamente (mesmo que ele já o tenha feito).
    	// Se colocar "prompt=selec_account" após o "type=offline", ele força a escolher uma conta.
    	
    	new Thread(() -> {
    		                
        	engine.locationProperty().addListener((observable, oldValue, newValue) -> {
        		if(newValue.contains("code=")) {
        			String authCode = newValue.split("code=")[1];
        			
        			if(authCode.contains("&")) {
        				authCode = authCode.split("&")[0];
        			}        			        			        			       		        	
        			        		       		
            		stage.close();
            		
            		DashboardController dashboard = new DashboardController();
            		
            		Platform.runLater(() -> {
            			
            			
            			try {
            				while(User.getAuthenticate() == false) {
            					            					
            					Thread.sleep(1000);
            					
                				try {
            						User.setNome(dashboard.getAttributesUser().get("name"));
            						User.setEmail(dashboard.getAttributesUser().get("email"));
            						User.setFotoPerfil(dashboard.getAttributesUser().get("userPhotoUrl"));
            						
            						if(User.getNome() != null && User.getEmail() != null && User.getFotoPerfil() != null) {            							
            							User.setAuthenticate(true);
            						}    						
            					} catch (IOException | GeneralSecurityException e) {
            						e.printStackTrace();
            					}      				
                			}
                			            				
        					redirect.loadNewStage("Menu", "WelcomeView.fxml");
        					redirect.closeCurrentStage(btnLoginGoogle);
        				} catch (IOException e) {					
        					e.printStackTrace();
        				} catch (InterruptedException e1) {
							e1.printStackTrace();
						}            		
            		});
            		
            		
        		} else {
        			// showNotification("Erro", "Nenhum código encontrado ainda...", false);
        		} 
        		
        	});
    		
    		
    	}).start();
    	    	
    }
    
    public void showNotification(String titulo, String mensagem, boolean sucesso) {
        
        String imagePath = sucesso ? "/images/sucess.png" : "/images/error.png";

        // Carregar imagens
        Image image = new Image(getClass().getResource(imagePath).toExternalForm());
        
        ImageView imageViewStatus = new ImageView(image);
        if (sucesso) {
            imageViewStatus.setFitWidth(50);  // Tamanho para imagem de sucesso
            imageViewStatus.setFitHeight(50);
        } else {
            imageViewStatus.setFitWidth(80);  // Tamanho para imagem de erro
            imageViewStatus.setFitHeight(80);
        }
        imageViewStatus.setPreserveRatio(true); 

        // Criar e exibir a notificação
        Notifications.create()
              .title(titulo)
              .text(mensagem)
              .graphic(imageViewStatus) 
              .position(Pos.BASELINE_RIGHT)  // Posição no canto inferior direito da tela
              .hideAfter(Duration.seconds(5))  // Duração da notificação
              .show();
         }    

    	
}