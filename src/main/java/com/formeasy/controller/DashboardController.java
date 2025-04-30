package com.formeasy.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.formeasy.service.FormEasyService;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.drive.model.File;
import com.google.api.services.forms.v1.model.Form;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

@RestController
@RequestMapping
public class DashboardController {
	@Autowired
	FormEasyService formEasyService = new FormEasyService();
	
	public Credential getCredentials() throws IOException, GeneralSecurityException {
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		return formEasyService.getCredentials(HTTP_TRANSPORT);
	}	
	
	@GetMapping("/google/api/token")
	public String getAccessToken() throws IOException, GeneralSecurityException {
		return formEasyService.getAccessToken();
	}
	
	@GetMapping("/attributesuser")
	public Map<String,String> getAttributesUser() throws IOException, GeneralSecurityException{
		return formEasyService.getAttributesUser();
	}
	
	@GetMapping("/sheetsuser")
	public List<File> getSheetsUser() throws IOException, GeneralSecurityException{
		return formEasyService.getSheetsUser();
	}
	
	@GetMapping("/formsuser")
	public List<File> getFormsUser() throws IOException, GeneralSecurityException{
		return formEasyService.getFormsUser();
	}
	
	@GetMapping("/sheetsdata_valuerange")
	public ValueRange getSheetsDataAsValueRange(String spreadsheetId, String range) 
			throws GeneralSecurityException, IOException {
		return formEasyService.getSheetsDataAsValueRange(spreadsheetId, range);
	}
	
	@GetMapping("/sheetsdata_batchget")
	public BatchGetValuesResponse getSheetsDataAsBatchGet(String spreadsheetId, List<String> ranges) 
			throws GeneralSecurityException, IOException{
		return formEasyService.getSheetsDataAsBatchGet(spreadsheetId, ranges);
	}
	
	@GetMapping("/getforms")
	public Form getForm(String formId) throws IOException, GeneralSecurityException{
		return formEasyService.getForm(formId);
	}
}
