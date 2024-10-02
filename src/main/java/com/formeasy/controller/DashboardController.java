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

import com.google.api.services.drive.model.File;

@RestController
@RequestMapping
public class DashboardController {
	@Autowired
	FormEasyService formEasyService = new FormEasyService();
	
	@GetMapping("/attributesuser")
	public Map<String,String> getAttributesUser() throws IOException, GeneralSecurityException{
		return formEasyService.getAttributesUser();
	}
	
	@GetMapping("/sheetsuser")
	public List<File> getSheetsUser() throws IOException, GeneralSecurityException{
		return formEasyService.getSheetsUser();
	}
}
