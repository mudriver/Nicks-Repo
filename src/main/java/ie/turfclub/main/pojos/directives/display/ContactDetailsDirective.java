package ie.turfclub.main.pojos.directives.display;

import ie.turfclub.main.pojos.directives.Directive;
import ie.turfclub.main.pojos.directives.DirectiveTypes;

public class ContactDetailsDirective implements Directive{

	private String nameKey;
	private String mobileKey;
	private String faxKey;
	private String phoneKey;
	private String emailKey;
	private String extraTitle;
	private String extraKey;
	private String extraKeySplitter;
	private DirectiveTypes directiveType = DirectiveTypes.CONTACTDETAILS;
	
	
	
	public String getNameKey() {
		return nameKey;
	}
	public void setNameKey(String nameKey) {
		this.nameKey = nameKey;
	}
	public String getMobileKey() {
		return mobileKey;
	}
	public void setMobileKey(String mobileKey) {
		this.mobileKey = mobileKey;
	}
	public String getFaxKey() {
		return faxKey;
	}
	public void setFaxKey(String faxKey) {
		this.faxKey = faxKey;
	}
	public String getPhoneKey() {
		return phoneKey;
	}
	public void setPhoneKey(String phoneKey) {
		this.phoneKey = phoneKey;
	}
	public String getEmailKey() {
		return emailKey;
	}
	public void setEmailKey(String emailKey) {
		this.emailKey = emailKey;
	}

	public DirectiveTypes getDirectiveType() {
		return directiveType;
	}
	public void setDirectiveType(DirectiveTypes directiveType) {
		this.directiveType = directiveType;
	}
	public String getExtraTitle() {
		return extraTitle;
	}
	public void setExtraTitle(String extraTitle) {
		this.extraTitle = extraTitle;
	}
	public String getExtraKey() {
		return extraKey;
	}
	public void setExtraKey(String extraKey) {
		this.extraKey = extraKey;
	}
	public String getExtraKeySplitter() {
		return extraKeySplitter;
	}
	public void setExtraKeySplitter(String extraKeySplitter) {
		this.extraKeySplitter = extraKeySplitter;
	}


	
	
	
}
