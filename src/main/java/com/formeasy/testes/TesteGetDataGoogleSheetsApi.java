package com.formeasy.testes;

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
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.people.v1.PeopleServiceScopes;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

// Acesse: https://developers.google.com/sheets/api/guides/values

public class TesteGetDataGoogleSheetsApi {
	
	public static void main(String[] args) throws IOException, GeneralSecurityException {
		final String SPREADSHEET_ID = "1mXjqc3pAi68SWvrDCIyAa9M1hIkS-HEkZTxakMVawr4";
		String range = "A2:D7"; // A1 é apenas cabeçalho. Por isso, inicia de A2
		
		ValueRange resultado = getSheetsDataAsValueRange(SPREADSHEET_ID, range);
		
		Integer numeroDeLinhas = resultado.getValues().size(); // Retorna o tamanho da lista
		
		List<List<Object>> dados = resultado.getValues();
		
		for(List<Object> dado : dados) {
			System.out.printf("NOME: %s \tEMAIL: %s \tIDADE: %s\n", dado.get(0), dado.get(1), dado.get(2));
		}
		
		System.out.println();
		
		
		double maiorQue20 = 0, menorQue20 = 0, igualA20 = 0;
		
		for(List<Object> linha : dados) {
			int idadeInt = Integer.parseInt((String) linha.get(2));
			if(idadeInt> 20) {
				maiorQue20++;
			}
			else if(idadeInt < 20) {
				menorQue20++;
			}
			else {
				igualA20++;
			}			
		}
		
		maiorQue20 = maiorQue20 / numeroDeLinhas * 100;
		menorQue20 = menorQue20 / numeroDeLinhas * 100;
		igualA20 = igualA20 / numeroDeLinhas * 100;
		
		
		
		
		/*System.out.println("Nome e email dos integrantes:");
		for(List<Object> dado : dados) {
			System.out.printf("%s | %s\n", dado.get(0), dado.get(1));
		}*/
		
		System.out.printf(">> Integrantes com idade maior que 20 anos: %f %%.\n", maiorQue20);
		System.out.printf(">> Integrantes com idade igual a 20 anos: %f %%.\n", igualA20);
		System.out.printf(">> Integrantes com idade menor que 20 anos: %f %%.\n", menorQue20);
		
		
		System.out.println("CHECK!");
	}
	
	
	private static final String APPLICATION_NAME = "Ler dados do GoogleSheets";
	private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
	private static final String TOKENS_DIRECTORY_PATH = "tokens";
	
	// Nessa parte indicamos os escopos que selecionamos lá nas credenciais do Google Cloud.
	private static final List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS, SheetsScopes.DRIVE);
	
	private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
	
	
	/*
	 * Esta talvez seja a principal função, que será usada por todas as outras na hora de obter requisição
	 * de serviço.
	 */
	
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
	
	private static Sheets getSheetsService() throws IOException, GeneralSecurityException {
		NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		Sheets sheetsService = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
				.setApplicationName(APPLICATION_NAME)
				.build();
		return sheetsService;
	}
	
	public static ValueRange getSheetsDataAsValueRange(String spreadsheetId, String range) 
			throws IOException, GeneralSecurityException {
		Sheets sheets = getSheetsService();
		
		// Precisa-se do id da planilha e do range.
		
		ValueRange sheetsResult = null; // ValueRange possui o range e 
		// Os valores são colocados na forma de uma List<List<Object>>, na quak
		// As LISTAS internas são COLUNAS, dentro delas estão as linhas.
		
		try {
			sheetsResult = sheets.spreadsheets().values().get(spreadsheetId, range).execute();
		} catch(GoogleJsonResponseException e) {
			GoogleJsonError error = e.getDetails();
			if(error.getCode() == 404) {
				System.out.printf("Spreadsheets not found with id %s\n", spreadsheetId);
			}
			else {
				throw e;
			}
		}
		
		return sheetsResult;
	}
}
