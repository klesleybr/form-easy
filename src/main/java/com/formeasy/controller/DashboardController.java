package com.formeasy.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.formeasy.util.FormEasyUtil;

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
