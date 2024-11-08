package com.formeasy;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import com.formeasy.controller.LoginController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
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
        primaryStage.setResizable(false);
        primaryStage.setTitle("Form Easy - Login");
        primaryStage.getIcons().add(icon);
        primaryStage.show();
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/formeasy/controller/EmailView.fxml"));
        loader.setControllerFactory(springContext::getBean);
        Parent emailRoot = loader.load();

        Stage emailStage = new Stage();
        Scene emailScene = new Scene(emailRoot);
        emailStage.setScene(emailScene);
        emailStage.setTitle("Form Easy - Envio de E-mail");
        emailStage.setResizable(false);
    }

    @Override
    public void stop() throws Exception {
        this.springContext.close();
        Platform.exit();
    }

}
