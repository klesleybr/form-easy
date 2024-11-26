package com.formeasy.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.formeasy.service.EmailService;
import com.formeasy.service.ExcelService;

import jakarta.mail.MessagingException;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
@FxmlView("EmailView.fxml")

@Controller
public class EnvioController {

    @Autowired
    private ExcelService excelService;

    @Autowired
    private EmailService emailService;

    @FXML
    private TextField TextAssunto;

    @FXML
    private TextArea TextMensagem;

    @FXML
    private Button btnAdicionarArquivo;

    @FXML
    private Button btnEnviar;

    private File selectedFile;
    
    public EnvioController(){
    	this.excelService = new ExcelService();
    	this.emailService = new EmailService();
    }

    @FXML
    public void initialize() {
        btnAdicionarArquivo.setOnAction(e -> adicionarArquivo());
        btnEnviar.setOnAction(e -> enviarArquivo());
    }

    @FXML
    public void adicionarArquivo() {
        FileChooser fileChooser = new FileChooser();
        Stage stage = (Stage) btnAdicionarArquivo.getScene().getWindow();
        selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            System.out.println("Arquivo selecionado: " + selectedFile.getName());
        } else {
            System.out.println("Nenhum arquivo selecionado.");
        }
    }

    @FXML
    public void enviarArquivo() {
        String assunto = TextAssunto.getText();
        String descricao = TextMensagem.getText();

        // Valida os campos obrigatórios
        if (assunto == null || assunto.trim().isEmpty()) {
            System.out.println("Assunto não pode estar vazio.");
            return;
        }

        if (descricao == null || descricao.trim().isEmpty()) {
            System.out.println("Mensagem não pode estar vazia.");
            return;
        }

        if (selectedFile != null) {
            try {
                // Extrai os e-mails do arquivo Excel
                List<String> emails = excelService.getEmailsFromExcel(selectedFile);

                // Remove e-mails nulos ou inválidos
                emails.removeIf(email -> email == null || email.trim().isEmpty() || !emailService.isValidEmail(email));

                if (!emails.isEmpty()) {
                    // Envia os e-mails utilizando o EmailService
                    emailService.sendEmails(emails, assunto, descricao);
                    System.out.println(emails.size() + " e-mails enviados com sucesso!");
                } else {
                    System.out.println("Nenhum e-mail válido encontrado no arquivo.");
                }
            } catch (IOException e) {
                System.out.println("Erro ao ler o arquivo: " + e.getMessage());
                e.printStackTrace();
            } catch (MessagingException e) {
                System.out.println("Erro ao enviar e-mails: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Nenhum arquivo selecionado.");
        }
    }
}
