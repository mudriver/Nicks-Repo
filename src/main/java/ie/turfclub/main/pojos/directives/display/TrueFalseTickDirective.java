package ie.turfclub.main.pojos.directives.display;

import ie.turfclub.main.pojos.directives.Directive;
import ie.turfclub.main.pojos.directives.DirectiveTypes;

public class TrueFalseTickDirective implements Directive{

	private String key;
	private DirectiveTypes directiveType = DirectiveTypes.TRUEFALSETICK;
	
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


	
	
	
}
