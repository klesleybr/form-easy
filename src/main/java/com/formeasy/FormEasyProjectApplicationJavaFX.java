package com.formeasy;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.formeasy.controller.EnvioController;
import com.formeasy.controller.LoginController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;

public class FormEasyProjectApplicationJavaFX extends Application {

    private ConfigurableApplicationContext springContext;

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
        primaryStage.setTitle("Form Easy - Login");
        primaryStage.getIcons().add(icon);
        primaryStage.show();
        
        FxWeaver Weaver = springContext.getBean(FxWeaver.class);
        Parent emailRoot = Weaver.loadView(EnvioController.class);
        
        Stage emailStage = new Stage();
        Scene emailScene = new Scene(emailRoot);
        Image image = new Image(getClass().getResourceAsStream("/images/logo-quadrada2.png"));
        emailStage.setScene(emailScene);
        emailStage.setResizable(true);
        emailStage.setTitle("Form Easy - Envio de E-mail");
        emailStage.getIcons().add(image);

       
    }

    @Override
    public void stop() throws Exception {
        this.springContext.close();
        Platform.exit();
    }

}
