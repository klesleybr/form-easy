package com.formeasy.controller;

import java.io.IOException;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import org.controlsfx.control.Notifications;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.formeasy.entity.Usuario;
import com.formeasy.repository.UsuarioRepository;
import com.google.api.services.people.v1.PeopleServiceScopes;
import com.google.api.services.sheets.v4.SheetsScopes;

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

// Nova notação Service
@Service
public class LoginController{
	RedirectController redirect = new RedirectController();
	
	private UsuarioRepository usuarioRepository;
	
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
    			PeopleServiceScopes.USERINFO_EMAIL, PeopleServiceScopes.USERINFO_PROFILE);
    	
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
    	stage.resizableProperty().setValue(Boolean.FALSE);
    	stage.show();
    	
    	String urlAuthentication = "https://accounts.google.com/o/oauth2/auth?access_type=offline&"
                + "client_id=" + CLIENT_ID
                + "&redirect_uri=" + REDIRECT_URI
                + "&response_type=code&scope=" + SCOPES.get(0) + "%20" + SCOPES.get(1) + "%20" 
                + SCOPES.get(2) + "%20" + SCOPES.get(3);
    	
    	engine.load(url);
    	engine.load(urlAuthentication);
    	
    	
    	engine.locationProperty().addListener((observable, oldValue, newValue) -> {
    		if(newValue.contains("code=")) {
    			String authCode = newValue.split("code=")[1];
    			
    			if(authCode.contains("&")) {
    				authCode = authCode.split("&")[0];
    			}
    			
        		System.out.println("Código de autorização: " + authCode);        		
        		stage.close();
        		
        		try {

					redirect.loadNewStage("Menu", "WelcomeView.fxml");
					redirect.closeCurrentStage(btnLogin);
				} catch (IOException e) {					
					e.printStackTrace();
				}
    		} else {
    			showNotification("Erro", "Nenhum código encontrado ainda...", false);
    		} 
    		
    	});
    }
    	
    	public void registrarUsuario(String login, String nome, String email, String senha) {
    	    Usuario usuario = new Usuario();
    	    usuario.setLogin(login);
    	    usuario.setNome(nome);
    	    usuario.setEmail(email);
    	    usuario.setSenha(senha);
    	    usuarioRepository.save(usuario);
    }
    	
    	public void showNotification(String titulo, String mensagem, boolean sucesso) {
            
        	String imagePath = sucesso ? "/images/sucess.png" : "/images/error.png";

            // Carregar imagens
            Image image = new Image(getClass().getResource(imagePath).toExternalForm());
            
            ImageView imageViewStatus = new ImageView(image);
            imageViewStatus.setFitWidth(50);
            imageViewStatus.setFitHeight(50);

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
