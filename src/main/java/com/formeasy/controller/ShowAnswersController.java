package com.formeasy.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.api.services.drive.model.File;
import com.google.api.services.forms.v1.model.ChoiceQuestion;
import com.google.api.services.forms.v1.model.Form;
import com.google.api.services.forms.v1.model.Item;
import com.google.api.services.forms.v1.model.Option;
import com.google.api.services.sheets.v4.model.ValueRange;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.StringConverter;

public class ShowAnswersController {
	@Autowired
	DashboardController dashboard = new DashboardController();

    @FXML
    private Button btnSearchAnswers;

    @FXML
    private Button btnSearchForms;

    @FXML
    private ChoiceBox<File> cbxListForms;    
    
    @FXML
    private CheckBox chbxPercentage;

    @FXML
    private TableView<List<Object>> tblShowAnswers;
    
    
	/*
	 * A função abaixo está relacionada ao botão BUSCAR FORMULÁRIOS. Serve para listar
	 * os formulários dentro de um ChoiceBox.
	 */    
    
    @FXML
    void onClickBtnSearchForms(ActionEvent event) throws IOException, GeneralSecurityException {
    	
    	/*
    	 * Vai buscar no Google Drive todos os formulários que o usuário possui e 
    	 * retornar num ChoiceBox, na forma: [nome_formulario, id_formulario].
    	 */
    	
    	List<File> listFormsUser = dashboard.getFormsUser();
    	for(File form : listFormsUser) {
    		cbxListForms.getItems().add(form);
    		
    		/*
    		 * A função .setConverter() permite exibir apenas o NOME do formulário
    		 * nas opções do ChoiceBox, sobrescrevendo o método toString() para
    		 * retornar apenas o nome.
    		 */
    		
    		cbxListForms.setConverter(new StringConverter<File>() { 
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

	@FXML
    void onClickBtnSearchAnswers(ActionEvent event) throws IOException, GeneralSecurityException {
    	String formId = cbxListForms.getValue().getId();    	    	
    	Form form = dashboard.getForm(formId);
    	
    	if(form.getLinkedSheetId() != null) {
    		
    		/*
    		 * Na função .getSheetsDataAsValueRange, como segundo parâmetro pode-se passar algum range (ex.: A1:E4) 
    		 * ou o nome da folha (sheet). Neste último caso, são retornados todos os dados da folha. 
    		 */
    		
    		ValueRange spreadsheetAnswers = dashboard.getSheetsDataAsValueRange(form.getLinkedSheetId(), "Respostas ao formulário 1");
        	
    		////////////////////////////////////////////////////////////////////////////////////////////////////////
        	System.out.println(">> T.D.T. || Resultados da planilha de respostas: " + spreadsheetAnswers);
        	System.out.println("------------------------------------------------------------------------------");
        	////////////////////////////////////////////////////////////////////////////////////////////////////////        	
        	
        	if(!chbxPercentage.isSelected()) {
        		
        		/*
        		 * O spreadsheetAnswers.getValues() retorna um List<List<Object>>
        		 */
        		
            	ObservableList<List<Object>> obsListAnswers = FXCollections.observableArrayList(spreadsheetAnswers.getValues());
            	setValuesOnColumns(obsListAnswers);        		
        	} else {
        		setPercentualsOnColumns(form, spreadsheetAnswers.getValues());
        	}
        	
    	}
    	else {
    		System.out.println(">> T.D.T. || Não foi encontrada nenhuma planilha associada ao formulário: " + cbxListForms.getValue().getName()
    				+ " (" + formId + ").");
    		if(!tblShowAnswers.getColumns().isEmpty())
        		tblShowAnswers.getColumns().clear();
    		
    		tblShowAnswers.setPlaceholder(new Label("O formulário selecionado não possui nenhuma planilha de respostas associada."));
    	}    
    }
	
	 @FXML
	    void initialize() {
	        tblShowAnswers.setPlaceholder(new Label("A tabela está vazia. Indique o formulário desejado e busque pelas respostas."));

	    }
   
	
    private void setValuesOnColumns(ObservableList<List<Object>> listValues) {
    	// Limpa a tabela, caso ela não esteja vazia.
    	if(!tblShowAnswers.getColumns().isEmpty())
    		tblShowAnswers.getColumns().clear();
    	
    	/*
    	 * A lista de índice 0 deste ObservableList<> contém o título de cada coluna da planilha
    	 * de respostas (as quais, por sua vez, correspondem às questões do formulário).
    	 * Lembre-se de que cada elemento da lista é do tipo Object, não String.
    	 */
    	
    	List<Object> headerColumns = listValues.get(0);
    	listValues.remove(0);
    	
    	// Definição das colunas...
    	for(int index = 0; index < headerColumns.size(); index++) {
    		final int columnIndex = index; 

    		TableColumn<List<Object>, Object> column = new TableColumn<>((String) headerColumns.get(columnIndex));
    		
    		/*
    		 * O método setCellValueFactory, quando aliado ao cellData, permite que o desenvolvedor defina quais dados
    		 * serão exibidos em uma coluna SEM PRECISAR CRIAR UMA CLASSE ESPECÍFICA. Isso é útil quando se usa
    		 * estruturas de dados dinâmicas (como é este caso).
    		 * 
    		 * Note que o cellData corresponde ao valor GENÉRICO da célula. Neste caso, um List<Object>.
    		 */
    		
    		column.setCellValueFactory(cellData -> {
    			return new SimpleObjectProperty<>(cellData.getValue().get(columnIndex));
    		});
    		tblShowAnswers.getColumns().add(column);    		
    	}  
    	
    	// Rever isto: seria melhor alterar o ObservableList<> que a TableView<> inteira.
    	tblShowAnswers.setItems(listValues);
    }
    
    private void setPercentualsOnColumns(Form form, List<List<Object>> spreadsheet) {
    	List<Item> items = form.getItems();    	
    	List<List<Object>> majorListQuestionOptions = joinQuestionAndOptions(items);    	
    	
    	int sizeMajorList = sizeMajorList(majorListQuestionOptions);
    	
    	List<List<Object>> majorListAnswersInPercentage = convertAnswersInPercentageValues(majorListQuestionOptions, spreadsheet);
    	    	    	    	
    	/*
    	 * A repetição abaixo serve para corrigir o problema das listas com tamanhos desiguais. Aquelas listas com opções
    	 * faltando (ou seja, menores que a maior lista), serão acrescidas do elemento OPÇÃO INEXISTENTE.
    	 */
    	
    	for(int index = 0; index < majorListAnswersInPercentage.size(); index++) {
    		while(majorListAnswersInPercentage.get(index).size() < sizeMajorList) {
    			majorListAnswersInPercentage.get(index).add((Object)"Opção Inexistente");
    		}
    	}
    	
    	ObservableList<List<Object>> obsAnswersInPercentage = FXCollections.observableArrayList(majorListAnswersInPercentage);
    	
    	if(!tblShowAnswers.getColumns().isEmpty())
    		tblShowAnswers.getColumns().clear();
    	
    	for(int index = 0; index < sizeMajorList; index++) {
    		final int indexFinal = index;
    		String columnTitle = "Opção " + (index);
    		
    		TableColumn<List<Object>, Object> column = new TableColumn<>(columnTitle);
    		if(indexFinal == 0) column.setText("Descrição da Pergunta");
    		
    		column.setCellValueFactory(cellData ->{
    			Object newValue = cellData.getValue().get(indexFinal);
    			return new SimpleObjectProperty<>(newValue);
    		});
    		
    		tblShowAnswers.getColumns().add(column);    		
    	}
    	
    	tblShowAnswers.setItems(obsAnswersInPercentage); 	
    }
    
    /*
     * Para essa função, há de se propor o teste de selecionar as respostas por coluna e não por linha. Talvez, selecioná-las assim
     * e usar o Collection.frequency() seja mais vantajoso.
     */
    
    private List<List<Object>> joinQuestionAndOptions(List<Item> items){
    	
    	/*
    	 * Foi pensada a criação de uma estrutura de lista aninhada da seguinte forma:
    	 * [[Pergunta1, Opção1, Opção2, ..., OpçãoN], [Pergunta2, Opção1, Opção2, ..., OpçãoN], ...].
    	 * O objeto inteiro sizeMajorList é criado para resolver um problema dessa abordagem: as listas internas
    	 * possuirão tamanhos diferentes. Isso pode ser contornado se soubermos o tamanho da lista maior.
    	 * 
    	 * Detalhe: se numa questão de múltipla escolha tipo RADIO houver a opção OUTROS, esta sempre será
    	 * a última da lista.
    	 */
    	
    	List<List<Object>> majorListQuestionOptions = new ArrayList<>();
    	
    	for(Item item : items) {    		
    		ChoiceQuestion choiceQuestion = item.getQuestionItem().getQuestion().getChoiceQuestion();    		    		
    		if(choiceQuestion != null && choiceQuestion.getType().equals("RADIO")) {
    			// Essa é a lista menor que ficará dentro da lista maior (criada acima).
    			List<Object> questionOptions = new ArrayList<>();
    			
    			// Adiciona o primeiro elemento, que sempre é a pergunta.
    			Object question = (Object) item.getTitle();
    			questionOptions.add(question);
    			
    			// Captura as respostas e, usando um loop, passa elas para a lista questionOptions, para unir à pergunta.
        		List<Option> options = item.getQuestionItem().getQuestion().getChoiceQuestion().getOptions();           		
        		for(Option option : options) {
        			Object possibleAnswer = (Object) option.getValue();
        			// A opção "Outros" (para RADIO) sempre retorna null.        			
        			if(possibleAnswer == null)
        				possibleAnswer = (Object) "Outro";
        			questionOptions.add(possibleAnswer);
        		}
        		majorListQuestionOptions.add(questionOptions);
    		}    	     		    		
    	}
    	
    	return majorListQuestionOptions;    	
    }
    
    private int sizeMajorList(List<List<Object>> nestedList) {
    	int sizeMajorList = 0;
    	
    	for(List<Object> list : nestedList) {
    		if(list.size() > sizeMajorList)
    			sizeMajorList = list.size();    		
    	}
    	
    	return sizeMajorList;
    }
    
    private List<List<Object>> convertAnswersInPercentageValues(List<List<Object>> listQuestionOptions, List<List<Object>> listAnswers){
    	List<List<Object>> majorListAnswersInPercent = new ArrayList<>();
    	
    	for(List<Object> questionOptions : listQuestionOptions) {
    		List<Object> answersInPercent = new ArrayList<>();
    		
    		Object question = questionOptions.get(0);
    		questionOptions.remove(0);    	    	    		
    		answersInPercent.add(question);
    		
    		List<Object> headerSpreadsheet = listAnswers.get(0);
    		int indexCurrentQuestion = headerSpreadsheet.indexOf(question); 
    		
    		float amountAnswers = 0;
    		float amountTotalAnwers = listAnswers.size() - 1;    		
        	float amountNotOther = 0;
    		
    		for(Object option : questionOptions) {
    			amountAnswers = 0;
    			    		    			
    			if(option.equals((Object) "Outro")){    				
    				amountAnswers = amountTotalAnwers - amountNotOther;    				
				}
    			else {
    				for(List<Object> listaSheets : listAnswers) {
        				if(listaSheets.get(indexCurrentQuestion).equals(option)) {
        					amountAnswers ++;
        					amountNotOther ++;
        				}           				
        			}       				
    			}
    			
    			float valueInPercentage = amountAnswers / amountTotalAnwers * 100;
    			String valuePercentageInString = String.format("%.2f %%", valueInPercentage);
    			answersInPercent.add((Object) valuePercentageInString);     		
    		}
    		majorListAnswersInPercent.add(answersInPercent);
    	}
    	
    	return majorListAnswersInPercent;
    }
}