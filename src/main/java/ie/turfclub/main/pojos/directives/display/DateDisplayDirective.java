package ie.turfclub.main.pojos.directives.display;

import ie.turfclub.main.pojos.directives.Directive;
import ie.turfclub.main.pojos.directives.DirectiveTypes;

public class DateDisplayDirective implements Directive {

	private String key;
	private String preLabel;
	private DirectiveTypes directiveType = DirectiveTypes.DATESPAN;
	
	public DirectiveTypes getDirectiveType() {
		return directiveType;
	}
	public void setDirectiveType(DirectiveTypes directiveType) {
		this.directiveType = directiveType;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getPreLabel() {
		return preLabel;
	}

	public void setPreLabel(String preLabel) {
		this.preLabel = preLabel;
	}
	
//	
}
