package com.formeasy.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.formeasy.util.FormEasyUtil;

@Service
public class FormEasyService {
	@Autowired(required = true)
	FormEasyUtil formEasyUtil;
	
	public Map<String,String> getAttributesUser() throws IOException, GeneralSecurityException{
		return formEasyUtil.getAttributesUser();
	}

}
