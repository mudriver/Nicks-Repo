package ie.turfclub.main.pojos.directives.input;

import ie.turfclub.main.pojos.directives.Directive;
import ie.turfclub.main.pojos.directives.DirectiveTypes;

public class InputDirective implements Directive{

	private String placeHolder;
	private String key;
	private boolean required = true;
	private String requiredMessage = "Required Field";
private DirectiveTypes directiveType = DirectiveTypes.INPUT;
	
	public DirectiveTypes getDirectiveType() {
		return directiveType;
	}
	public void setDirectiveType(DirectiveTypes directiveType) {
		this.directiveType = directiveType;
	}
	
	public String getPlaceHolder() {
		return placeHolder;
	}
	public void setPlaceHolder(String placeHolder) {
		this.placeHolder = placeHolder;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public String getRequiredMessage() {
		return requiredMessage;
	}
	public void setRequiredMessage(String requiredMessage) {
		this.requiredMessage = requiredMessage;
	}
	
	
	
	
}
