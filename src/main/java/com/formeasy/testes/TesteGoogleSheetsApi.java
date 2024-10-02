package com.formeasy.testes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import com.formeasy.util.FormEasyUtil;
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
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.PeopleServiceScopes;
import com.google.api.services.sheets.v4.SheetsScopes;

public class TesteGoogleSheetsApi {
	
	private static final String APPLICATION_NAME = "Teste Google Sheets";
	private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
	private static final String TOKENS_DIRECTORY_PATH = "tokens";
	
	// Nessa parte indicamos os escopos que selecionamos lá nas credenciais do Google Cloud.
	private static final List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS, SheetsScopes.DRIVE, 
			PeopleServiceScopes.USERINFO_EMAIL, PeopleServiceScopes.USERINFO_PROFILE);
	
	private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
	
	private static Credential getCredentials(NetHttpTransport HTTP_TRANSPORT) throws IOException {
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
	
	private static Drive getDriveService() throws GeneralSecurityException, IOException {
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		Drive driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
				.build();
		
		return driveService;
	}
	
	public static List<File> getUserSheets() throws IOException, GeneralSecurityException {
		Drive drive = getDriveService();
		
		// Esta string serve como uma espécie de filtro, para retornar os arquivos desta pasta 
		// especial, que contém apenas as planilhas do google sheets.
		String queryFilter = "mimeType='application/vnd.google-apps.spreadsheet'";
		FileList result = drive.files().list().setQ(queryFilter).setFields("files(id, name)")
				.execute();
		
		List<File> files = result.getFiles();
		return files;
	}
	
	
	public static void main(String[] args) throws IOException, GeneralSecurityException {
		System.out.println("Olá. Esta é uma aplicação do Drive mais Sheets.");
		
		List<File> filesSheets = getUserSheets();
		
		if(filesSheets == null || filesSheets.isEmpty()) {
			System.out.println("Nenhum arquivo foi encontrado.");
		} else {
			for(File files : filesSheets) {
				System.out.printf("%s (%s)\n", files.getName(), files.getId());
			}
		}
		
	}
}
