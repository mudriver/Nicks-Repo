package ie.turfclub.main.pojos.directives.display;

import java.util.HashMap;

import ie.turfclub.main.pojos.directives.Directive;
import ie.turfclub.main.pojos.directives.DirectiveTypes;

public class EnumToImageDirective implements Directive{

	private String key;
	private boolean inUse = false;
	private HashMap<String, String> enumToImageMap = new HashMap<String, String>();
	private DirectiveTypes directiveType = DirectiveTypes.ENUMTOIMAGE;
	
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
	
	public HashMap<String, String> getEnumToImageMap() {
		return enumToImageMap;
	}
	public void setEnumToImageMap(HashMap<String, String> enumToImageMap) {
		this.enumToImageMap = enumToImageMap;
	}
	public boolean isInUse() {
		return inUse;
	}
	public void setInUse(boolean inUse) {
		this.inUse = inUse;
	}

	
	
	
	
}
