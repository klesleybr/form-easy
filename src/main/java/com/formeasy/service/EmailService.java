package com.formeasy.service;

import org.springframework.stereotype.Service;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Service
public class EmailService {
	
	private final String username = "viniciusalves081105@gmail.com";
	private final String password = "gorz rvwu nyzg tdog"; 
	
	
    // Método para validar se um e-mail tem um formato válido
    public boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
            return true;
        } catch (AddressException ex) {
            return false;
        }
    }

    // Método para enviar e-mails
    public void sendEmails(List<String> recipients, String assunto, String descricao) throws MessagingException {
    	    Set<String> uniqueRecipients = new HashSet<>(recipients); // Remove duplicatas

    	    Properties props = new Properties();
    	    props.put("mail.smtp.auth", "true");
    	    props.put("mail.smtp.starttls.enable", "true");
    	    props.put("mail.smtp.host", "smtp.gmail.com");
    	    props.put("mail.smtp.port", "587");

    	    Session session = Session.getDefaultInstance(props, new Authenticator() {
    	        @Override
    	        protected PasswordAuthentication getPasswordAuthentication() {
    	            return new PasswordAuthentication(username, password);
    	        }
    	    });

    	    for (String recipient : uniqueRecipients) {
    	        if (isValidEmail(recipient)) { // Verifica se o e-mail é válido
    	            try {
    	                Message msg = new MimeMessage(session);
    	                msg.setFrom(new InternetAddress(username)); // A linha que estava dando erro era essa (ERROR: NULL POINTER EXCEPTION)
    	                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
    	                msg.setSubject(assunto);
    	                msg.setText(descricao);
    	                Transport.send(msg);
    	                System.out.println("E-mail enviado para: " + recipient);
    	        	    
    	            } catch (MessagingException e) {
    	                System.out.println("Erro ao enviar e-mail para: " + recipient + " - " + e.getMessage());
    	            }
    	        } else {
    	            System.out.println("E-mail inválido ou nulo: " + recipient);
    	        }
    	    }
    	    
    	}


    // Método para carregar e-mails de um arquivo
    public List<String> getEmailsFromFile(String filePath) throws IOException {
        List<String> emailList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String email = line.trim();

                if (!email.isEmpty() && isValidEmail(email)) {
                    emailList.add(email);
                } else {
                    System.out.println("E-mail inválido no arquivo: " + email);
                }
            }
        }

        return emailList;
    }
}
