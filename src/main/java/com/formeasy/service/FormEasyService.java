package com.formeasy.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.formeasy.util.FormEasyUtil;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.drive.model.File;
import com.google.api.services.forms.v1.model.Form;
import com.google.api.services.people.v1.model.Person;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

@Service
public class FormEasyService {
	@Autowired(required = true)
	FormEasyUtil formEasyUtil = new FormEasyUtil();
	
	public Credential getCredentials(NetHttpTransport HTTP_TRANSPORT) throws IOException {		
		return formEasyUtil.getCredentials(HTTP_TRANSPORT);
	}
	
	public String getAccessToken() throws IOException, GeneralSecurityException {
		return formEasyUtil.getAccessToken();
	}
	
	public Map<String,String> getAttributesUser() throws IOException, GeneralSecurityException{
		return formEasyUtil.getAttributesUser();
	}
	
	public List<File> getSheetsUser() throws IOException, GeneralSecurityException{
		return formEasyUtil.getSheetsUser();
	}
	
	public List<File> getFormsUser() throws IOException, GeneralSecurityException{
		return formEasyUtil.getFormsUser();
	}
	
	public ValueRange getSheetsDataAsValueRange(String spreadsheetId, String range) 
			throws GeneralSecurityException, IOException {
		return formEasyUtil.getSheetsDataAsValueRange(spreadsheetId, range);
	}
	
	public BatchGetValuesResponse getSheetsDataAsBatchGet(String spreadsheetId, List<String> ranges) 
			throws GeneralSecurityException, IOException{
		return formEasyUtil.getSheetsDataAsBatchGet(spreadsheetId, ranges);
	}
	
	public Form getForm(String formId) throws IOException, GeneralSecurityException{
		return formEasyUtil.getForm(formId);
	}
	
	public Person getUserInfoFromAuthCode(String authCode) throws Exception {
	    
		formEasyUtil.createCredentialFromAuthCode(authCode);
	    return formEasyUtil.getPeopleService().people().get("people/me")
	                     .setPersonFields("names,emailAddresses,photos").execute();
	}
	
}

