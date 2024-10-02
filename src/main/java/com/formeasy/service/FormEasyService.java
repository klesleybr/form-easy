package com.formeasy.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.formeasy.util.FormEasyUtil;
import com.google.api.services.drive.model.File;

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

}
