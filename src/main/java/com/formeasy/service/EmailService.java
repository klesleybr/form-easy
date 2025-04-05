package com.formeasy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.formeasy.controller.DashboardController;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.*;

import com.sun.mail.smtp.SMTPTransport;

@Service
public class EmailService {
	
	@Autowired
	DashboardController dashboard = new DashboardController();		
	
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
    public void sendEmails(List<String> recipients, String assunto, String descricao) 
    		throws MessagingException, IOException, GeneralSecurityException {
    	
    	    Set<String> uniqueRecipients = new HashSet<>(recipients); // Remove duplicatas
    	    
    	    /**
    	     * O email usado como USERNAME passa, agora, a ser dinâmico e se altera
    	     * conforme o usuário logado na aplicação.
    	     * 
    	     * Para enviar emails, basta o email e o token do usuário.
    	     */
    	    
    	    String username = getEmailUser();    	    

    	    Properties props = new Properties();
    	    props.put("mail.smtp.auth", "false"); // desabilita o login padrão (com email e senha)
    	    props.put("mail.smtp.starttls.enable", "true");
    	    props.put("mail.smtp.auth.mechanisms", "XOAUTH2"); // habilita o login via token
    	    props.put("mail.smtp.host", "smtp.gmail.com");
    	    props.put("mail.smtp.port", "587");
    	    // props.put("mail.debug", "true"); // dispensável
    	    props.put("mail.smtp.ssl.trust", "smtp.gmail.com");     	    
    	    Session session = Session.getInstance(props, null);    	        	        	       	    	 
    	    
    	    SMTPTransport transport = (SMTPTransport) session.getTransport("smtp");
            // transport.connect("smtp.gmail.com", username, null);
            transport.connect("smtp.gmail.com", 587, null, null);
            transport.issueCommand("AUTH XOAUTH2 " + buildOAuth2Token(username, getAccessToken()), 235); 
    	    
    	    for (String recipient : uniqueRecipients) {
    	        if (isValidEmail(recipient)) { // Verifica se o e-mail é válido      	        	
    	        	Message msg = new MimeMessage(session);
    	    	    msg.setFrom(new InternetAddress(username));
    	    	    msg.setSubject(assunto);
    	    	    msg.setText(descricao);
    	        	msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
    	        	transport.sendMessage(msg, msg.getAllRecipients());    	        	    	        	
    	        } else {
    	            System.out.println("E-mail inválido ou nulo: " + recipient);
    	        }
    	    }            
            transport.close();                	        	        	    
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
    
    private String getEmailUser() throws IOException, GeneralSecurityException {
    	return dashboard.getAttributesUser().get("email");
    }        
    
    private String getAccessToken() throws IOException, GeneralSecurityException {    	
    	return dashboard.getAccessToken();
    }
    
    private static String buildOAuth2Token(String email, String accessToken) {
        String format = "user=%s\u0001auth=Bearer %s\u0001\u0001";
        String authString = String.format(format, email, accessToken);
               
        return Base64.getEncoder().encodeToString(authString.getBytes(StandardCharsets.UTF_8));
    }

}
