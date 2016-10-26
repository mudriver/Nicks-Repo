package ie.turfclub.main.pojos.jsonEdit;

import ie.turfclub.main.pojos.directives.Directive;


public class JsonFormFields {

	

	private String fieldLabel;
	private String fieldKey;
	private String fieldId;
	private Directive directive;
	

	public String getFieldLabel() {
		return fieldLabel;
	}
	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}
	public String getFieldKey() {
		return fieldKey;
	}
	public void setFieldKey(String fieldKey) {
		this.fieldKey = fieldKey;
	}
	public String getFieldId() {
		return fieldId;
	}
	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}
	public Directive getDirective() {
		return directive;
	}
	public void setDirective(Directive directive) {
		this.directive = directive;
	}

	
	
	
	
}
