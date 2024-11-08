package com.formeasy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.formeasy.service.EmailService;
import com.formeasy.service.ExcelService;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/api/emails")
public class EmailController {

    @Autowired
    private ExcelService excelService; // Injeta o serviço de leitura de Excel

    @Autowired
    private EmailService emailService; // Injeta o serviço de envio de e-mail


    // Endpoint para enviar e-mails a partir de uma planilha ou um arquivo de texto
    @PostMapping(value = "/enviar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> sendEmailsFromFile(@RequestParam("file") MultipartFile file, @RequestParam("assunto") String assunto, @RequestParam("descricao") String descricao) throws IOException, MessagingException {

        File tempFile = null;

        try {
            // Cria um arquivo temporário para armazenar a planilha
            tempFile = File.createTempFile("temp", null);
            file.transferTo(tempFile); // Transfere o conteúdo do MultipartFile para o arquivo temporário

            List<String> emails;

            // Tenta extrair e-mails da planilha
            try {
                emails = excelService.getEmailsFromExcel(tempFile);
            } catch (Exception e) {
                // Se a extração de e-mails do Excel falhar, tenta carregar e-mails do arquivo de texto
                emails = emailService.getEmailsFromFile(tempFile.getAbsolutePath());
            }

            if (emails.isEmpty()) {
                return ResponseEntity.badRequest().body("Nenhum e-mail válido encontrado.");
            }
            
            // Envia os e-mails
            emailService.sendEmails(emails, assunto, descricao);

            return ResponseEntity.ok(emails.size() + " e-mails válidos enviados com sucesso!");
        } finally {
            // Exclui o arquivo temporário, garantindo que sempre será removido
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }
}
