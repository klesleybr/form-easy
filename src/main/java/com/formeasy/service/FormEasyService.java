package com.formeasy.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.formeasy.util.FormEasyUtil;
import com.google.api.services.drive.model.File;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

@Service
public class FormEasyService {
	@Autowired(required = true)
	FormEasyUtil formEasyUtil = new FormEasyUtil();
	
	public Map<String,String> getAttributesUser() throws IOException, GeneralSecurityException{
		return formEasyUtil.getAttributesUser();
	}
	
	public List<File> getSheetsUser() throws IOException, GeneralSecurityException{
		return formEasyUtil.getSheetsUser();
	}
	
	public ValueRange getSheetsDataAsValueRange(String spreadsheetId, String range) 
			throws GeneralSecurityException, IOException {
		return formEasyUtil.getSheetsDataAsValueRange(spreadsheetId, range);
	}
	
	public BatchGetValuesResponse getSheetsDataAsBatchGet(String spreadsheetId, List<String> ranges) 
			throws GeneralSecurityException, IOException{
		return formEasyUtil.getSheetsDataAsBatchGet(spreadsheetId, ranges);
	}
}
