package com.formeasy;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.formeasy.controller.LoginController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;

public class FormEasyProjectApplicationJavaFX extends Application {
	/*
	 * A partir do contextoSpring, acessamos as beans do Spring.
	 */
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
		
		/*
		 * O método start serve apenas para carregar a primeira tela.
		 * Apesar de algumas especificidades (por causa da junção do JavaFX com o Spring),
		 * a exibição de uma tela segue praticamente este mesmo padrão:
		 */

		FxWeaver fxWeaver = contextoSpring.getBean(FxWeaver.class);
		Parent root = fxWeaver.loadView(LoginController.class);
		Scene scene = new Scene(root);
		
		Image icon = new Image(getClass().getResourceAsStream("/images/logo-quadrada2.png"));
		primaryStage.setScene(scene);
		primaryStage.resizableProperty().setValue(Boolean.FALSE);
		primaryStage.setTitle("Form Easy - Login");
		primaryStage.getIcons().add(icon);
		primaryStage.show();
		
		/*
		 * JavaFX e Spring são praticamente "mundos opostos", já que ambos implementam inversão de controle.
		 * É como se eles forçassem a parada do fluxo normal do código a fim de tomarem o controle.
		 * O FXWeaver é usado para harmonizar esses frameworks de forma mais elegante.
		 */
	}
}
