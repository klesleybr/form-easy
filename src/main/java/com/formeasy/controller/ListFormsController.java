package com.formeasy.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.formeasy.model.QuestionInfo;
import com.google.api.services.drive.model.File;
import com.google.api.services.forms.v1.model.ChoiceQuestion;
import com.google.api.services.forms.v1.model.Form;
import com.google.api.services.forms.v1.model.Item;
import com.google.api.services.forms.v1.model.Option;
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
        	
        	if(!chbxPercentual.isSelected()) {
        		// Guarda um conjunto de List<Object>
            	ObservableList<List<Object>> obsListAnswers = FXCollections.observableArrayList(spreadsheetAnswers.getValues());
            	setValuesOnColumns(obsListAnswers);        		
        	} else {
        		setPercentualsOnColumns(form, spreadsheetAnswers.getValues());
        	}
        	
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
    
    private void setPercentualsOnColumns(Form form, List<List<Object>> spreadsheet) {
    	System.out.println(">> T.D.T. || Estrutura do formulário: " + form);
    	System.out.println("------------------------------------------------------------------------------");
    	
    	ObservableList<QuestionInfo> obsValuesPercentuals = FXCollections.observableArrayList();
    	    	
    	
    	List<Item> items = form.getItems();
    	
    	List<List<Object>> listaQuestaoRespostaMaior = new ArrayList<>();
    	
    	int maiorLista = 0;
    	
    	for(Item item : items) {    		
    		ChoiceQuestion choiceQuestion = item.getQuestionItem().getQuestion().getChoiceQuestion();
    		    		
    		if(choiceQuestion != null && choiceQuestion.getType().equals("RADIO")) {
    			List<Object> listaQuestaoRespostasMenor = new ArrayList<>();
    			
    			Object pergunta = (Object) item.getTitle();
    			listaQuestaoRespostasMenor.add(pergunta);
    			
        		List<Option> respostas = item.getQuestionItem().getQuestion().getChoiceQuestion().getOptions();           		
        		for(Option opcao : respostas) {
        			Object answer = (Object) opcao.getValue();
        			if(answer == null)
        				answer = (Object) "Outro";
        			listaQuestaoRespostasMenor.add(answer);
        		}
        		
        		if(listaQuestaoRespostasMenor.size() > maiorLista)
        			maiorLista = listaQuestaoRespostasMenor.size(); 
        		
        		listaQuestaoRespostaMaior.add(listaQuestaoRespostasMenor);
    		}    	     		
    	}
    	
    	
    	List<List<Object>> listaPerguntasPercentual = new ArrayList<>();
    	
    	
    	
    	for(List<Object> listaForms : listaQuestaoRespostaMaior) {
    		List<Object> perguntasPercentual = new ArrayList<>();
    		
    		Object perguntaForm = listaForms.get(0);
    		listaForms.remove(0);    	    	
    		
    		perguntasPercentual.add(perguntaForm);
    		
    		List<Object> cabecalhoPlanilha = spreadsheet.get(0);
    		int indiceDaColunaCerta = cabecalhoPlanilha.indexOf(perguntaForm); 
    		
    		float quantidadeDeRespostas = 0;
    		float totalDeRespostas = spreadsheet.size() - 1;
    		
    		//Teste = O que estava dando erro era esse objeto mal alocado.
    		// Ele estava, antes, fora deste loop...
        	float quantidadeNaoOutro = 0;
    		
    		for(Object possivelResposta : listaForms) {
    			quantidadeDeRespostas = 0;
    			
    			if(possivelResposta.equals((Object) "Outro")){
    				for(List<Object> possivelOutro : spreadsheet) {
						Object possivelObjetoOutro = possivelOutro.get(indiceDaColunaCerta);
						System.out.println("Outro: " + possivelObjetoOutro);
						for(List<Object> verificarOutro : listaQuestaoRespostaMaior) {
							if(verificarOutro.contains(possivelObjetoOutro)) {								
								break;
							}
							
						}
						System.out.println("Quantidade de respostas|: " + quantidadeDeRespostas);
						
					}
    				quantidadeDeRespostas = totalDeRespostas - quantidadeNaoOutro;
    				System.out.println(quantidadeDeRespostas + ", " + totalDeRespostas + ", " + quantidadeNaoOutro);					
				}
    			else {
    				for(List<Object> listaSheets : spreadsheet) {
        				if(listaSheets.get(indiceDaColunaCerta).equals(possivelResposta)) {
        					quantidadeDeRespostas ++;
        					quantidadeNaoOutro ++;
        				}   
        				
        			}   
    				
    			}
    			
    			float valorEmFormaPercentual = quantidadeDeRespostas / totalDeRespostas * 100;
    			String valorPercentualString = String.format("%.2f %%", valorEmFormaPercentual);
    			perguntasPercentual.add((Object) valorPercentualString);
 
    			System.out.println(">> Possível resposta: " + possivelResposta);
    			System.out.println(">> Quantidade de seleções: " + quantidadeDeRespostas);
    			System.out.println(">> Quantidade percentual: " + quantidadeDeRespostas/totalDeRespostas * 100);
    			System.out.println("-----------------------------------------------------------------------------------------");    		
    		}
    		listaPerguntasPercentual.add(perguntasPercentual);
    	}
    	
    	System.out.println(">> Como ficou a lista final: " + listaPerguntasPercentual);
    	System.out.println("-----------------------------------------------------------------------------------------");   
    	
    	
    	
    	System.out.println(">> T.D.T. || Lista Maior: " + listaQuestaoRespostaMaior);
    	System.out.println(">> T.D.T. || Numero de elementos da maior lista: " + maiorLista);
    	
    	for(int index = 0; index < listaPerguntasPercentual.size(); index++) {
    		while(listaPerguntasPercentual.get(index).size() < maiorLista) {
    			listaPerguntasPercentual.get(index).add((Object)"Opção Inexistente");
    		}
    	}
    	
    	ObservableList<List<Object>> obsRespostasPercentuais = FXCollections.observableArrayList(listaPerguntasPercentual);
    	
    	if(!tblShowAnswers.getColumns().isEmpty())
    		tblShowAnswers.getColumns().clear();
    	
    	for(int index = 0; index < maiorLista; index++) {
    		final int indexFinal = index;
    		String tituloDaColuna = "Opção " + (index);
    		TableColumn<List<Object>, Object> column = new TableColumn<>(tituloDaColuna);
    		if(indexFinal == 0) column.setText("Descrição da Pergunta");
    		
    		column.setCellValueFactory(cellData ->{
    			Object novoValor = cellData.getValue().get(indexFinal);
    			
    			if(novoValor == null) {
    				return new SimpleObjectProperty<>((Object) "");    		
    			}
    			else {
    				return new SimpleObjectProperty<>(novoValor);
    			}
    			
    		});
    		
    		tblShowAnswers.getColumns().add(column);    		
    	}
    	
    	tblShowAnswers.setItems(obsRespostasPercentuais);
 	
    }
    // Ver depois se selecionar as respostas por coluna e usar o Collection.frequency() não é mais vantajoso.
}
