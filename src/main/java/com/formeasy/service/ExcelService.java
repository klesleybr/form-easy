package com.formeasy.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;

@Service
public class ExcelService {

    // Valida o formato de e-mails
    public boolean isValidEmail(String email) {
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
            return true;
        } catch (AddressException ex) {
            return false;
        }
    }

    // Lê os e-mails de um arquivo Excel
    public List<String> getEmailsFromExcel(File file) throws IOException {
    	    Set<String> emails = new HashSet<>(); // Usa Set para evitar duplicação de emails
    	    
    	    try (FileInputStream fis = new FileInputStream(file); Workbook workbook = WorkbookFactory.create(fis)) {
    	        Sheet sheet = workbook.getSheetAt(0);
    	        if (sheet == null) {
    	            throw new IOException("A planilha não contém nenhuma aba.");
    	        }

    	        for (Row row : sheet) {
    	            for (Cell cell : row) {
    	                if (cell != null && cell.getCellType() == CellType.STRING) {
    	                    String email = cell.getStringCellValue().trim().toLowerCase();
    	                    if (isValidEmail(email)) {
    	                        emails.add(email); // Apenas emails válidos são adicionados
    	                    }
    	                }
    	            }
    	        }
    	    }
    	    return new ArrayList<>(emails); // Retorna uma lista livre de valores nulos e duplicados
    	}

    }
