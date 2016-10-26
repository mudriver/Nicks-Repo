package ie.turfclub.main.pojos.directives.input;

import ie.turfclub.main.pojos.directives.Directive;
import ie.turfclub.main.pojos.directives.DirectiveTypes;

public class CommentBoxWithEnumToggleDirective  implements Directive{

	private DirectiveTypes directiveType = DirectiveTypes.COMMENTBOXENUMTOGGLE;
	private String saveUrl;
	private String key;
	private String toggleButtonTitle;
	private String toggleButtonUrl;
	private String toggleButtonOnValue;
	private String toggleButtonOffValue;
	private String toggleButtonKey;
	
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

	public String getToggleButtonTitle() {
		return toggleButtonTitle;
	}

	public void setToggleButtonTitle(String toggleButtonTitle) {
		this.toggleButtonTitle = toggleButtonTitle;
	}

	public String getToggleButtonUrl() {
		return toggleButtonUrl;
	}

	public void setToggleButtonUrl(String toggleButtonUrl) {
		this.toggleButtonUrl = toggleButtonUrl;
	}

	public String getToggleButtonOnValue() {
		return toggleButtonOnValue;
	}

	public void setToggleButtonOnValue(String toggleButtonOnValue) {
		this.toggleButtonOnValue = toggleButtonOnValue;
	}

	public String getToggleButtonOffValue() {
		return toggleButtonOffValue;
	}

	public void setToggleButtonOffValue(String toggleButtonOffValue) {
		this.toggleButtonOffValue = toggleButtonOffValue;
	}

	public String getToggleButtonKey() {
		return toggleButtonKey;
	}

	public void setToggleButtonKey(String toggleButtonKey) {
		this.toggleButtonKey = toggleButtonKey;
	}
	
	

}
