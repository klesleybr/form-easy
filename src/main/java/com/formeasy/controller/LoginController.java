package com.formeasy.controller;

import java.io.IOException;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import org.controlsfx.control.Notifications;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.formeasy.domain.LoginRequestDTO;
import com.formeasy.domain.ResponseDTO;
import com.formeasy.security.AuthSession;
import com.google.api.services.forms.v1.FormsScopes;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.people.v1.PeopleServiceScopes;
import com.google.api.services.sheets.v4.SheetsScopes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
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
    private TextField txtLogin;
    
    @FXML
    private PasswordField txtPassword;
    
    @FXML
    private Button btnCriar;
    	
    
    @FXML
    void onClickLogin(ActionEvent event) throws IOException {
    	
    	String login = txtLogin.getText().trim();
    	String password = txtPassword.getText().trim();
    	
    	 if (login.isEmpty() || password.isEmpty()) {
             showNotification("Erro", "Por favor, preencha todos os campos.", false);
             return;
         }
    	 
    	// Dados para autenticação
         LoginRequestDTO requestDTO = new LoginRequestDTO(login, password);
    	 
    	 try {
    		// Faz a requisição HTTP para o AuthController
             ResponseEntity<ResponseDTO> response = fazerRequisicaoLogin(requestDTO);

             if (response.getStatusCode() == HttpStatus.OK) {
                 // Login bem-sucedido
                 String token = response.getBody().token();
                 
                 // Inicia a sessão, buscando as credenciais que o usuário já registrou (entrada rápida)
                 AuthSession.setToken(token);
                 AuthSession.setUserLogin(txtLogin.getText());
                 AuthSession.setUserPassword(txtPassword.getText());                 

                 // Redireciona para a próxima tela
                 redirect.loadNewStage("Menu", "WelcomeView.fxml");
                 redirect.closeCurrentStage(btnLogin);
             } else {
                 // Exibe mensagem de erro
                 showNotification("Erro", "Login ou senha incorretos.", false);
             }
         } catch (Exception e) {
             showNotification("Erro", "Ocorreu um erro ao tentar fazer login.", false);
             e.printStackTrace();
         }

    }
    
    @FXML
    void onClickLoginGoogle(ActionEvent event) throws IOException, URISyntaxException{
    	System.out.println("Botão clicado!");    	
    	openSuperimposedLoginView("http://localhost:8080/attributesuser"); 	
    }
    
    @FXML
    public void initialize() {
    	btnCriar.setOnAction(e -> CriarConta());
    }
    
    public void CriarConta() {
    	try {
    		 redirect.loadNewStage("Criar Conta", "RegistroView.fxml");
             redirect.closeCurrentStage(btnCriar); 
    	}catch(IOException e) {
    		System.out.println("Ocorreu um erro ao tentar acessar a tela de registro.");
            e.printStackTrace();
    	}
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
    	
    	
    	// Se colocar "prompt=consent" após o "type=offline", força o usuário a aceitar os escopos da 
    	// aplicação novamente (mesmo que ele já o tenha feito).
    	// Se colocar "prompt=selec_account" após o "type=offline", ele força a escolher uma conta.
    	
    	String urlAuthentication = "https://accounts.google.com/o/oauth2/auth?access_type=offline&"
                + "prompt=consent&" + "client_id=" + CLIENT_ID
                + "&redirect_uri=" + REDIRECT_URI
                + "&response_type=code&scope=" + String.join("%20", SCOPES);
                
                /* + SCOPES.get(0) + "%20" + SCOPES.get(1) + "%20" 
                + SCOPES.get(2) + "%20" + SCOPES.get(3) + "%20" + SCOPES.get(4) + "%20" + SCOPES.get(5)
                + "%20" + SCOPES.get(6) + "%20" + SCOPES.get(7) + "%20" + SCOPES.get(8);*/
    	
    	engine.load(url);
    	engine.load(urlAuthentication);
    	
    	
    	engine.locationProperty().addListener((observable, oldValue, newValue) -> {
    		if(newValue.contains("code=")) {
    			String authCode = newValue.split("code=")[1];
    			
    			if(authCode.contains("&")) {
    				authCode = authCode.split("&")[0];
    			}
    			        		       		
        		stage.close();
        		
        		try {                            
                        // Redireciona para a próxima tela
                        redirect.loadNewStage("Menu", "WelcomeView.fxml");
                        redirect.closeCurrentStage(btnLogin);
                        
        		} catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Nenhum código encontrado ainda...");
            }
    		
    	});
    }
    
    //Envia requisições HTTP para o backend para autenticação
    private ResponseEntity<ResponseDTO> fazerRequisicaoLogin(LoginRequestDTO requestDTO) {
        // Configuração do cliente HTTP
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Corpo da requisição
        HttpEntity<LoginRequestDTO> request = new HttpEntity<>(requestDTO, headers);
        
        ResponseEntity<ResponseDTO> response = restTemplate.postForEntity("http://localhost:8080/auth/login", request, ResponseDTO.class);
        
        if (response.getStatusCode() == HttpStatus.OK) {
            ResponseDTO loginData = response.getBody();
            System.out.println("Token: " + loginData.token());
            System.out.println("Login: " + loginData.login());
            System.out.println("Senha: " +loginData.password());
            
        }

        return response;
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

