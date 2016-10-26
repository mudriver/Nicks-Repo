package ie.turfclub.main.pojos.directives.input;

import ie.turfclub.main.pojos.directives.Directive;
import ie.turfclub.main.pojos.directives.DirectiveTypes;

public class PendingTrueFalseDirective implements Directive{

	private String trueText;
	private String falseText;
	private String pendingText;
	private String trueEnum;
	private String falseEnum;
	private String pendingEnum;
	private String key;
	private DirectiveTypes directiveType = DirectiveTypes.TRUEFALSETICK;
	
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
	public String getPendingText() {
		return pendingText;
	}
	public void setPendingText(String pendingText) {
		this.pendingText = pendingText;
	}
	public String getTrueEnum() {
		return trueEnum;
	}
	public void setTrueEnum(String trueEnum) {
		this.trueEnum = trueEnum;
	}
	public String getFalseEnum() {
		return falseEnum;
	}
	public void setFalseEnum(String falseEnum) {
		this.falseEnum = falseEnum;
	}
	public String getPendingEnum() {
		return pendingEnum;
	}
	public void setPendingEnum(String pendingEnum) {
		this.pendingEnum = pendingEnum;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public DirectiveTypes getDirectiveType() {
		return directiveType;
	}
	public void setDirectiveType(DirectiveTypes directiveType) {
		this.directiveType = directiveType;
	}
	
	
	
	
	
	
	
}
