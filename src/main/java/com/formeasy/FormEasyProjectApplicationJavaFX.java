package com.formeasy;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.formeasy.controller.LoginGoogleController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;

public class FormEasyProjectApplicationJavaFX extends Application {
	private ConfigurableApplicationContext contextoSpring;
	
	@Override
	public void init() {
		String[] args = getParameters().getRaw().toArray(new String[0]);
		this.contextoSpring = new SpringApplicationBuilder().
				sources(FormEasyProjectApplication.class).run(args);
	}
	
	@Override
	public void stop() {
		this.contextoSpring.close();
		Platform.exit();
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// O start serve para carregar apenas a primeira tela
		
		// Vamos obter o tamanho da tela do usuário
		// Rectangle2D obterDimensoesDaTela = Screen.getPrimary().getVisualBounds();
		// Agora, vamos fazer com que a tela inicial abra com tais dimensões
		/*primaryStage.setX(obterDimensoesDaTela.getMinX());
		primaryStage.setY(obterDimensoesDaTela.getMinY());
		primaryStage.setWidth(obterDimensoesDaTela.getWidth());
		primaryStage.setHeight(obterDimensoesDaTela.getHeight());*/
		
		
		// Vamos criar a cena e setar na Stage atual.
		FxWeaver fxWeaver = contextoSpring.getBean(FxWeaver.class);
		Parent root = fxWeaver.loadView(LoginGoogleController.class);
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.resizableProperty().setValue(Boolean.FALSE);
		primaryStage.setTitle("FormEasy Application");
		primaryStage.show();
	}
}
