package ie.turfclub.main.pojos.jsonEdit;

import java.util.ArrayList;

public class JsonEdit {

	private String title;
	private String saveUrl;
	private String afterSaveUrl;
	private Object data;
	private ArrayList<JsonFormFields> formFields = new ArrayList<>();
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public ArrayList<JsonFormFields> getFormFields() {
		return formFields;
	}
	public void setFormFields(ArrayList<JsonFormFields> formFields) {
		this.formFields = formFields;
	}
	
	public String getSaveUrl() {
		return saveUrl;
	}
	public void setSaveUrl(String saveUrl) {
		this.saveUrl = saveUrl;
	}
	public String getAfterSaveUrl() {
		return afterSaveUrl;
	}
	public void setAfterSaveUrl(String afterSaveUrl) {
		this.afterSaveUrl = afterSaveUrl;
	}

	
	
}
