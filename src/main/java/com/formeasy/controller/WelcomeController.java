package com.formeasy.controller;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.formeasy.FormEasyProjectApplicationJavaFX;
import com.formeasy.model.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;


public class WelcomeController {
	
	RedirectController redirect = new RedirectController();
	
	@Autowired
	DashboardController dashboard = new DashboardController();
	
	private Boolean isVisible = false;
	
	private String nameUser = User.getNome();
	private String emailUser = User.getEmail();
	private String photoUser = User.getFotoPerfil();
	
	
	@FXML
	private AnchorPane aboutUser;
	
	@FXML
	private Button btnChangeAccount;
	
    @FXML
    private Button btnOptionAnalyze;

    @FXML
    private Button btnOptionSend;

    @FXML
    private ImageView formeasy_foto;

    @FXML
    private Rectangle header;
    
    @FXML
    private ImageView imageUser;

    @FXML
    private ImageView mulher_prancheta;

    @FXML
    private Label texto1_header;

    @FXML
    private Label texto2_header;

    @FXML
    private Label texto_sem_formulario;
    
    @FXML
    private Label lblNameUser;
    
    @FXML
    private Label lblEmailUser;
    
    @FXML
    private Button btnLogout;
    
    @FXML
    private Button btnExit;
    
    @FXML
    private Button btnAjuda;
    
    @FXML
    public void initialize() throws IOException, GeneralSecurityException {
    	    
    	
    	lblEmailUser.setText(this.emailUser);
    	lblNameUser.setText(this.nameUser);
    	texto_sem_formulario.setText(String.format("Bem-vindo, %s! O que faremos hoje?", this.nameUser));
    	
    	setImage();
    }
    
    private void setImage() throws IOException, GeneralSecurityException {    	   
    	
    	Rectangle clip = new Rectangle(imageUser.getFitWidth(), imageUser.getFitHeight());
    	clip.setArcWidth(imageUser.getFitWidth());
    	clip.setArcHeight(imageUser.getFitWidth());
    	
    	if(photoUser != null) {
    		Image image = new Image(this.photoUser);
    		imageUser.setImage(image);
    	} else {
    		Image image = new Image("/images/padrao.png");
    		imageUser.setImage(image);
    	}
    	
    	imageUser.setClip(clip);
    	
    	imageUser.setOnMouseClicked(event -> {
    		isVisible = !isVisible;
    		aboutUser.setVisible(isVisible);
    	});
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
   	 	 User.limpar();
   		 // Platform.exit();
   	 	 System.exit(0);
   		FormEasyProjectApplicationJavaFX.closeSpring();
   	 	}else {
   		 System.out.println("Saída cancelada");
   	    }
    }
    
    @FXML
    void onClickBtnAjuda(ActionEvent event) {
    	try {
	        redirect.loadWebViewStage("Ajuda", "/html/index.html");
	    } catch (IOException e) {
	        e.printStackTrace();
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
    			User.limpar();
    			redirect.loadNewStage("Realizar login", "LoginView.fxml");
    			redirect.closeCurrentStage(imageUser);
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
