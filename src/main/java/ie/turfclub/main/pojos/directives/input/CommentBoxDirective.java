package ie.turfclub.main.pojos.directives.input;

import ie.turfclub.main.pojos.directives.Directive;
import ie.turfclub.main.pojos.directives.DirectiveTypes;

public class CommentBoxDirective  implements Directive{

	private DirectiveTypes directiveType = DirectiveTypes.COMMENTBOX;
	private String saveUrl;
	private String key;
	
	
	public DirectiveTypes getDirectiveType() {
		return directiveType;
	}

	public void setDirectiveType(DirectiveTypes directiveType) {
		this.directiveType = directiveType;
	}

	public String getSaveUrl() {
		return saveUrl;
	}

	public void setSaveUrl(String saveUrl) {
		this.saveUrl = saveUrl;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	

}
