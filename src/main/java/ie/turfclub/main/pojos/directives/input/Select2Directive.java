package ie.turfclub.main.pojos.directives.input;

import ie.turfclub.main.pojos.directives.Directive;
import ie.turfclub.main.pojos.directives.DirectiveTypes;

public class Select2Directive implements Directive{

	private String placeHolder;
	private String searchUrl;
	private String linkedFormFieldModel;
	private String key;
	private String idKey;
	private boolean loadOnInitOnly = false;
	private String requiredMessage = "Required field";
	private boolean required = true;
private DirectiveTypes directiveType = DirectiveTypes.SELECT2MULTI;
	
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
	public String getSearchUrl() {
		return searchUrl;
	}
	public void setSearchUrl(String searchUrl) {
		this.searchUrl = searchUrl;
	}
	public boolean isLoadOnInitOnly() {
		return loadOnInitOnly;
	}
	public void setLoadOnInitOnly(boolean loadOnInitOnly) {
		this.loadOnInitOnly = loadOnInitOnly;
	}
	public String getLinkedFormFieldModel() {
		return linkedFormFieldModel;
	}
	public void setLinkedFormFieldModel(String linkedFormFieldModel) {
		this.linkedFormFieldModel = linkedFormFieldModel;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getIdKey() {
		return idKey;
	}
	public void setIdKey(String idKey) {
		this.idKey = idKey;
	}
	public String getRequiredMessage() {
		return requiredMessage;
	}
	public void setRequiredMessage(String requiredMessage) {
		this.requiredMessage = requiredMessage;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}

	
	
}
