package com.formeasy.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.formeasy.model.Usuario;
import com.formeasy.util.FormEasyUtil;

// http://localhost:8080/login/oauth2/code/google

@RestController
@RequestMapping
public class DashboardController {
	@Autowired
	FormEasyUtil formEasyUtil;
	
	@GetMapping("/attributesuser")
	public Map<String,String> getAttributesUser() throws IOException, GeneralSecurityException{
		return formEasyUtil.getAttributesUser();
	}
}
