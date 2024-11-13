package com.formeasy.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.formeasy.service.EmailService;
import com.formeasy.service.ExcelService;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

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

    @FXML
    private File selectedFile;
    
    public EnvioController() { // Adicionei o construtor, por que só com a injeção do serviço não tava funcionado(ERROR: NULL POINTER EXCEPTION)
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
        
        if (selectedFile != null) {
            try {
                // Extrai os e-mails do arquivo Excel usando o ExcelService injetado
                List<String> emails = excelService.getEmailsFromExcel(selectedFile);

                // Remove e-mails nulos ou inválidos da lista
                emails.removeIf(email -> email == null || email.trim().isEmpty() || !emailService.isValidEmail(email));

                // Envia e-mails apenas se a lista não estiver vazia
                if (!emails.isEmpty()) {
                    emailService.sendEmails(emails, assunto, descricao); // Envia e-mails utilizando o EmailService
                    System.out.println(emails.size() + " e-mails enviados com sucesso!");
                } else {
                    System.out.println("Nenhum e-mail válido encontrado no arquivo."); 
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Nenhum arquivo selecionado.");
        }
    }

}