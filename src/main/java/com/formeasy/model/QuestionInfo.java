package com.formeasy.model;

import java.util.List;

import com.google.api.services.forms.v1.model.Option;

public class QuestionInfo {
	private String title;
	private Boolean isChoiceQuestion;
	
	private String type;
	private List<Option> options;
	
	
	public QuestionInfo(String title, Boolean isChoiceQuestion, String type, List<Option> options) {
		this.title = title;
		this.isChoiceQuestion = isChoiceQuestion;
		this.type = type;
		this.options = options;
	}
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public Boolean getIsChoiceQuestion() {
		return isChoiceQuestion;
	}
	public void setIsChoiceQuestion(Boolean isChoiceQuestion) {
		this.isChoiceQuestion = isChoiceQuestion;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<Option> getOptions() {
		return options;
	}
	public void setOptions(List<Option> options) {
		this.options = options;
	}
}
