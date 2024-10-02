package com.formeasy.controller;


import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;

import com.formeasy.model.SpreadsheetsInfo;
import com.google.api.services.drive.model.File;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import net.rgielen.fxweaver.core.FxmlView;

@FxmlView("GetSpreadsheetsExampleView.fxml")
public class GetSpreadsheetsController implements Initializable {
	@Autowired
	DashboardController dashboard = new DashboardController();
	
    @FXML
    private Button btnSearchSheets;

    @FXML
    private TableColumn<SpreadsheetsInfo, String> columnSheetsCreatedTime;
    
    @FXML
    private TableColumn<SpreadsheetsInfo, String> columnSheetsName;
    
    @FXML
    private TableColumn<SpreadsheetsInfo, String> columnSheetsId;
    
    @FXML
    private TableColumn<SpreadsheetsInfo, String> columnSheetsUrl;
    
    @FXML
    private TableView<SpreadsheetsInfo> tblAnswersSheets;
    
    
    private ObservableList<SpreadsheetsInfo> obsSpreadsheetsInfo;
    
    private List<SpreadsheetsInfo> listSpreadsheetsInfo = new ArrayList<>();
    
    @FXML
    void onClickSearchAnswers(ActionEvent event) throws IOException, GeneralSecurityException {
    	listSpreadsheetsUser();
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		obsSpreadsheetsInfo = FXCollections.observableArrayList();
		
		columnSheetsCreatedTime.setCellValueFactory(
				new PropertyValueFactory<>("createdTime"));
		
		columnSheetsName.setCellValueFactory(
				new PropertyValueFactory<>("name"));
		
		columnSheetsId.setCellValueFactory(
				new PropertyValueFactory<>("id"));
		
		columnSheetsUrl.setCellValueFactory(
				new PropertyValueFactory<>("url"));
		
		tblAnswersSheets.setItems(obsSpreadsheetsInfo);
	}
	
	private void listSpreadsheetsUser() throws IOException, GeneralSecurityException{		
		List<File> spreadsheets = dashboard.getSheetsUser();
						
		if(spreadsheets == null || spreadsheets.isEmpty()) {
			System.out.println("ERRO: Nenhuma planilha encontrada.");
		} else {
			if(!listSpreadsheetsInfo.isEmpty())
				listSpreadsheetsInfo.clear();

    		if(!obsSpreadsheetsInfo.isEmpty())
    			obsSpreadsheetsInfo.clear();
			
			for(File spreadsheet : spreadsheets) {
				String name = spreadsheet.getName();
				String id = spreadsheet.getId();
				String url = spreadsheet.getWebViewLink();
				
				long formatCreatedTime = spreadsheet.getCreatedTime().getValue();
				String createdTimeString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(new Date(formatCreatedTime));
				
				listSpreadsheetsInfo.add(new SpreadsheetsInfo(name, id, url, createdTimeString));			
			}
			
			obsSpreadsheetsInfo.addAll(listSpreadsheetsInfo);
		}		
	}
}
