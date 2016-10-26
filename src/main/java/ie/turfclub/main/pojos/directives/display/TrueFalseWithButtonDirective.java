package ie.turfclub.main.pojos.directives.display;

import ie.turfclub.main.pojos.directives.Directive;
import ie.turfclub.main.pojos.directives.DirectiveTypes;

public class TrueFalseWithButtonDirective extends ButtonDirective implements Directive {

	
	
	private String trueFalseKey;
	
	public TrueFalseWithButtonDirective(){
		this.setDirectiveType(DirectiveTypes.TRUEFALSETICKWITHBUTTON);
	}
	
	public String getTrueFalseKey() {
		return trueFalseKey;
	}
	public void setTrueFalseKey(String trueFalseKey) {
		this.trueFalseKey = trueFalseKey;
	}
	
	
	
	
	

	
	
	
	
}
