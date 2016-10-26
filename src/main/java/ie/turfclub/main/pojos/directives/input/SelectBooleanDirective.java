package ie.turfclub.main.pojos.directives.input;

import ie.turfclub.main.pojos.directives.Directive;
import ie.turfclub.main.pojos.directives.DirectiveTypes;

public class SelectBooleanDirective implements Directive{

	private String placeHolder;
	private String key;
	private String trueText;
	private String falseText;

private DirectiveTypes directiveType = DirectiveTypes.SELECTBOOLEAN;
	
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

	public String getTrueText() {
		return trueText;
	}
	public void setTrueText(String trueText) {
		this.trueText = trueText;
	}
	public String getFalseText() {
		return falseText;
	}
	public void setFalseText(String falseText) {
		this.falseText = falseText;
	}



	
	
}
