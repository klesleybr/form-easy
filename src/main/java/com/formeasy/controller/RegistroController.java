package com.formeasy.controller;

import java.io.IOException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import com.formeasy.domain.RegistroDTO;
import com.formeasy.domain.ResponseDTO;
import com.formeasy.security.AuthSession;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import net.rgielen.fxweaver.core.FxmlView;

@Controller
@FxmlView("RegistroView.fxml")
public class RegistroController {
	
	RedirectController redirect = new RedirectController();
	
	@FXML
	private TextField txtLogin;
	
	@FXML
	private PasswordField txtSenha;
	
	@FXML
	private PasswordField txtConfirmar;
	
	@FXML
	private Button btnSalvar;
	
	@FXML
	private Button btnVoltar;
	
	@FXML
    public void initialize() {
		btnVoltar.setOnAction(e -> Voltar());
		btnSalvar.setOnAction(e -> Salvar());
	}
	
	@FXML
	public void Voltar() {
		try {
			redirect.loadNewStage("Login", "LoginView.fxml");
            redirect.closeCurrentStage(btnVoltar); 
   	}catch(IOException e) {
   		System.out.println("Ocorreu um erro ao tentar voltar para a tela de login.");
           e.printStackTrace();
   	}
	}
	
	@FXML
	public void Salvar() {
		String login = txtLogin.getText().trim();
		String password = txtSenha.getText().trim();
        String confirmarPassword = txtConfirmar.getText().trim();

        // Validação básica
        if (login.isEmpty() || password.isEmpty() || confirmarPassword.isEmpty()) {
            exibirAlerta("Erro", "Todos os campos são obrigatórios.");
            return;
        }

        if (!password.equals(confirmarPassword)) {
            exibirAlerta("Erro", "As senhas não coincidem.");
            return;
        }
        
        // Cria o DTO
        RegistroDTO registroDTO = new RegistroDTO();
        registroDTO.setLogin(login);
        registroDTO.setPassword(password);

        try {
            
            ResponseEntity<ResponseDTO> response = fazerRequisicaoRegistro(registroDTO);

            if (response.getStatusCode() == HttpStatus.OK) {                
                // Login bem-sucedido
                String token = response.getBody().token();
                
                // Inicia a sessão, buscando as credenciais que o usuário já registrou (entrada rápida)
                AuthSession.setToken(token);
                AuthSession.setUserLogin(txtLogin.getText());
                AuthSession.setUserPassword(txtSenha.getText());  
                
                redirect.loadNewStage("Login", "LoginView.fxml");
                redirect.closeCurrentStage(btnSalvar); 
                
                exibirAlerta("Sucesso", "Usuário registrado com sucesso!");
                limparCampos();
            } else {
                exibirAlerta("Erro", "Falha ao registrar o usuário.");
            }

        } catch (Exception ex) {
            exibirAlerta("Erro", "Erro ao conectar com o servidor: " + ex.getMessage());
        }
    }
	
	private ResponseEntity<ResponseDTO> fazerRequisicaoRegistro(RegistroDTO registroDTO){
		// Envia requisição
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<RegistroDTO> request = new HttpEntity<>(registroDTO, headers);

        ResponseEntity<ResponseDTO> response = restTemplate.postForEntity("http://localhost:8080/auth/registro", request, ResponseDTO.class
        );
		
        return response;
	}

	    private void exibirAlerta(String titulo, String mensagem) {
	        Alert alert = new Alert(Alert.AlertType.INFORMATION);
	        alert.setTitle(titulo);
	        alert.setHeaderText(null);
	        alert.setContentText(mensagem);
	        alert.showAndWait();
	    }

	    private void limparCampos() {
	        txtLogin.clear();
	        txtSenha.clear();
	        txtConfirmar.clear();
	    }

}
	


