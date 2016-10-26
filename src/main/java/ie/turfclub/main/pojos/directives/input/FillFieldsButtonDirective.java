package ie.turfclub.main.pojos.directives.input;

import ie.turfclub.main.pojos.directives.Directive;
import ie.turfclub.main.pojos.directives.DirectiveTypes;

import java.util.ArrayList;

/**
 * @author chrisk
 * This class inserts a button into the form on the display screen
 * When the button is clicked it retrieves data from the json url specified and inserts it into the data model
 * as specified in the fields to fill. The JSON returned must be in the order that the fieldsToFill are set
 * If the json url requires parameters they can be mapped from the data model using paramFields 
 */

public class FillFieldsButtonDirective implements Directive{
	
	private String label;
	private String buttonText;
	private ArrayList<String> fieldsToFill = new ArrayList<>();
	private ArrayList<String> dataKeys = new ArrayList<>();
	private ArrayList<String> paramFields = new ArrayList<>();
	private ArrayList<String> paramNames = new ArrayList<>();
	private String dataUrl;
	private DirectiveTypes directiveType = DirectiveTypes.FILLFIELDSBUTTON;
	
	public DirectiveTypes getDirectiveType() {
		return directiveType;
	}
	public void setDirectiveType(DirectiveTypes directiveType) {
		this.directiveType = directiveType;
	}
	
	
	
	/**
	 * @return the label for the button if any
	 */
	public String getLabel() {
		return label;
	}
	
	
	/**
	 * @param label the label for the button if any
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	
	
	/**
	 * @return the text which will be displayed on the button
	 */
	public String getButtonText() {
		return buttonText;
	}
	
	/**
	 * @param buttonText the text which will be displayed on the button
	 */
	
	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
	}
	
	
	/**
	 * @return a map of the fields in the data model to fill
	 */
	public ArrayList<String> getFieldsToFill() {
		return fieldsToFill;
	}
	
	
	/**
	 * @param fieldsToFill a map of the fields in the data model to fill from the json url which returns an array of data 
	 * this is inserted into each field in the specified order
	 */
	public void setFieldsToFill(ArrayList<String> fieldsToFill) {
		this.fieldsToFill = fieldsToFill;
	}
	
	/**
	 * @return a string of a url that returns json data to fill the fields specified in the arraylist
	 */
	public String getDataUrl() {
		return dataUrl;
	}
	
	/**
	 * @param dataUrl string of a url that returns json data to fill the fields specified in the arraylist
	 */
	public void setDataUrl(String dataUrl) {
		this.dataUrl = dataUrl;
	}


	/**
	 * @return the data model fields used as parameters for the json url 
	 */
	public ArrayList<String> getParamFields() {
		return paramFields;
	}


	/**
	 * @param paramFields the data model fields used as parameters for the json url 
	 */
	public void setParamFields(ArrayList<String> paramFields) {
		this.paramFields = paramFields;
	}


	/**
	 * @return the names applied to the parameters as they are attached to the url
	 */
	public ArrayList<String> getParamNames() {
		return paramNames;
	}


	/**
	 * @param paramNames the names applied to the parameters as they are attached to the url
	 */
	public void setParamNames(ArrayList<String> paramNames) {
		this.paramNames = paramNames;
	}


	/**
	 * @return the keys to access the data returned this should be the same number as the fieldsToFill
	 */
	public ArrayList<String> getDataKeys() {
		return dataKeys;
	}


	/**
	 * @param dataKeys the keys to access the data returned this should be the same number as the fieldsToFill
	 */
	public void setDataKeys(ArrayList<String> dataKeys) {
		this.dataKeys = dataKeys;
	}
	
	
	
	
}
