package com.formeasy.model;

import javafx.beans.property.SimpleStringProperty;

public class SpreadsheetsInfo {
	private final SimpleStringProperty name;
	private final SimpleStringProperty id;
	private final SimpleStringProperty url;
	private final SimpleStringProperty createdTime;
	
	public SpreadsheetsInfo(String name, String id, 
			String url, String dateTimeCreated) {
		this.name = new SimpleStringProperty(name);
		this.id = new SimpleStringProperty(id);
		this.url = new SimpleStringProperty(url);
		this.createdTime = new SimpleStringProperty(dateTimeCreated);
	}
	
	public String getName() {
		return name.get();
	}
	public void setName(String name) {
		this.name.set(name);
	}
	
	public String getId() {
		return id.get();
	}
	public void setId(String id) {
		this.id.set(id);;
	}
	
	public String getUrl() {
		return url.get();
	}
	public void setUrl(String url) {
		this.url.set(url);;
	}
	
	public String getCreatedTime() {
		return createdTime.get();
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime.set(createdTime);;
	}
	
	
}
