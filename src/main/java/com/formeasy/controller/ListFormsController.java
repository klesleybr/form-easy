package com.formeasy.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.api.services.drive.model.File;
import com.google.api.services.forms.v1.model.Form;
import com.google.api.services.sheets.v4.model.ValueRange;

import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.StringConverter;

public class ListFormsController {
	@Autowired
	DashboardController dashboard = new DashboardController();

    @FXML
    private Button btnSearchAnswers;

    @FXML
    private Button btnSearchForms;

    @FXML
    private ChoiceBox<File> cbxListForms;    
    
    @FXML
    private CheckBox chbxPercentual;

    @FXML
    private TableView<List<Object>> tblShowAnswers;


	@FXML
    void onClickBtnSearchAnswers(ActionEvent event) throws IOException, GeneralSecurityException {
    	String formId = cbxListForms.getValue().getId();
    	
    	System.out.println(">> T.D.T || ID do formulário: " + formId);
    	System.out.println("------------------------------------------------------------------------------");
    	
    	Form form = dashboard.getForm(formId);
    	
    	if(form.getLinkedSheetId() != null) {
    		ValueRange spreadsheetAnswers = dashboard.getSheetsDataAsValueRange(form.getLinkedSheetId(), "Respostas ao formulário 1");
        	
        	System.out.println(">> T.D.T. || Resultados da planilha: " + spreadsheetAnswers);
        	System.out.println("------------------------------------------------------------------------------");
        	
        	/*List<Object> columnsHeader = spreadsheetAnswers.getValues().get(0);
        	
        	if(!tblShowAnswers.getColumns().isEmpty())
        		tblShowAnswers.getColumns().clear();
        	
        	for(Object header : columnsHeader) {       			
        		TableColumn<List<String>, String> column = new TableColumn<>((String) header);
        		tblShowAnswers.getColumns().add(column); 
        	}*/
        	
        	
        	// Guarda um conjunto de List<Object>
        	ObservableList<List<Object>> obsListAnswers = FXCollections.observableArrayList(spreadsheetAnswers.getValues());
        	setValuesOnColumns(obsListAnswers);
    	}
    	else {
    		System.out.println(">> T.D.T. || Não foi encontrada nenhuma planilha associada ao formulário de id: " + formId);
    		if(!tblShowAnswers.getColumns().isEmpty())
        		tblShowAnswers.getColumns().clear();
    	}    
    }

    @FXML
    void onClickBtnSearchForms(ActionEvent event) throws IOException, GeneralSecurityException {
    	
    	/*
    	 * Vai buscar no Google Drive todos os formulários que o usuário possui e 
    	 * retornar num ChoiceBox.
    	 */
    	
    	List<File> listFormsUser = dashboard.getFormsUser();
    	for(File form : listFormsUser) {
    		cbxListForms.getItems().add(form);
    		cbxListForms.setConverter(new StringConverter<File>() {

    			// Ver como resolver esse problema depois
    			@Override
    			public String toString(File file) {
    				if(file != null)
    					return file.getName();
    				else
    					return null;
    			}
    			
    			@Override
    			public File fromString(String arg0) {
    				return null;
    			}
    		});
    	}
    }
    
    private Map<String, Object> createRow(String... keyValuePairs) {
    	Map<String, Object> row = new HashMap<>();
    	for(int i = 0; i < keyValuePairs.length; i += 2) {
    		row.put(keyValuePairs[i], keyValuePairs[i+1]);
    	}
    	
    	return row;
    }
    private void setValuesOnColumns(ObservableList<List<Object>> listValues) {
    	if(!tblShowAnswers.getColumns().isEmpty())
    		tblShowAnswers.getColumns().clear();
    	
    	List<Object> headerColumns = listValues.get(0);
    	listValues.remove(0);
    	
    	
    	for(int index = 0; index < headerColumns.size(); index++) {
    		final int columnIndex = index; 
    		// (TableColumn.CellDataFeatures<List<Object>, Object> cellData)
    		TableColumn<List<Object>, Object> column = new TableColumn<>((String) headerColumns.get(columnIndex));
    		column.setCellValueFactory(cellData -> {
    			return new SimpleObjectProperty<>(cellData.getValue().get(columnIndex));
    		});
    		tblShowAnswers.getColumns().add(column);    		
    	}  
    	
    	// Mudar isso aqui depois... o correto é alterar o ObservableList, não a TableView.
    	tblShowAnswers.setItems(listValues);
    }
    
}
