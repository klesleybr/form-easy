package com.formeasy.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.PeopleServiceScopes;
import com.google.api.services.people.v1.model.Person;
import com.google.api.services.sheets.v4.SheetsScopes;

@Component
public class FormEasyUtil {
	private static final String APPLICATION_NAME = "Form Easy";
	private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
	private static final String TOKENS_DIRECTORY_PATH = "tokens";
	
	// Nessa parte indicamos os escopos que selecionamos lá nas credenciais do Google Cloud.
	private static final List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS, SheetsScopes.DRIVE, 
			PeopleServiceScopes.USERINFO_EMAIL, PeopleServiceScopes.USERINFO_PROFILE);
	
	private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
	
	
	/*
	 * Esta talvez seja a principal função, que será usada por todas as outras na hora de obter requisição
	 * de serviço.
	 */
	
	private Credential getCredentials(NetHttpTransport HTTP_TRANSPORT) throws IOException {
		/*
		 * Carregamento das chaves do arquivo credentials.json, para autenticação com o OAuth2 e 
		 * permissão de credenciais.
		 */
		InputStream in = FormEasyUtil.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
		if(in == null) {
			throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
		}
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
		
		/*
		 * Em alguma parte abaixo, as credenciais são armazenadas para que o usuário não precise ficar
		 * permitindo toda vez que entrar.
		 */
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
				.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
				.setAccessType("offline").build();
		/*
		 * Considere uma outra possibilidade: new FileDataStoreFactory(new java.io.File(System.getProperty("user.home"), 
		 * TOKENS_DIRECTORY_PATH)...
		 */
		
		LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
		return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
	}
	
	/*
	 * A partir daqui, as funções funcionarão em pares onde uma provê o serviço ("ligação com a API") 
	 * e outra a ação. As estruturas serão semelhantes.
	 */
	
	private PeopleService getPeopleService() throws GeneralSecurityException, IOException {
		NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		
		PeopleService peopleService = new PeopleService.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
				.setApplicationName(APPLICATION_NAME).build();
		return peopleService;
	}
	public Map<String,String> getAttributesUser() throws IOException, GeneralSecurityException{
		Person profile = getPeopleService().people().get("people/me").setPersonFields("names,emailAddresses,photos").execute();
		
		Map<String,String> attributesUser = new HashMap<>();
		attributesUser.put("name", profile.getNames().get(0).getDisplayName());
		attributesUser.put("email", profile.getEmailAddresses().get(0).getValue());
		attributesUser.put("userPhotoUrl", profile.getPhotos().get(0).getUrl());
		
		return attributesUser;
	}
}
