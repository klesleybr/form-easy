package com.formeasy;

import java.util.Optional;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.formeasy.controller.LoginController;
import com.formeasy.controller.RedirectController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;

public class FormEasyProjectApplicationJavaFX extends Application {

    private ConfigurableApplicationContext springContext;
    private static FormEasyProjectApplicationJavaFX instance;

    @Override
    public void init() throws Exception {
        String[] args = getParameters().getRaw().toArray(new String[0]);
        this.springContext = new SpringApplicationBuilder()
                .sources(FormEasyProjectApplication.class)
                .run(args);
        
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FxWeaver fxWeaver = springContext.getBean(FxWeaver.class);
        Parent loginRoot = fxWeaver.loadView(LoginController.class);

        Scene loginScene = new Scene(loginRoot);
        Image icon = new Image(getClass().getResourceAsStream("/images/logo-quadrada2.png"));
        primaryStage.setScene(loginScene);
        primaryStage.setResizable(true);
        primaryStage.setTitle("Realizar Login");
        primaryStage.setMaximized(true);
        primaryStage.getIcons().add(icon);
        primaryStage.show();
       
        // A função intercepta o fechamento imediato da janela...
        primaryStage.setOnCloseRequest(event -> {
        	event.consume(); // Impede o fechamento
        	try {
				sair();
			} catch (Exception e) {		
				e.printStackTrace();
			}
        });
    }
    
    public FormEasyProjectApplicationJavaFX(){
    	instance = this;
    }
    
    public static FormEasyProjectApplicationJavaFX getInstance() {
    	return instance;    	
    }

    // @Override
    /* public void stop() throws Exception {    	
        this.springContext.close();                     
        Platform.exit();    	
    } */
    
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
    		this.springContext.close();
    		Platform.exit();       		
    	}
    }
}
