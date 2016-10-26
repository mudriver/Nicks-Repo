package ie.turfclub.main.pojos.directives.input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ie.turfclub.main.pojos.directives.Directive;
import ie.turfclub.main.pojos.directives.DirectiveTypes;
import ie.turfclub.main.pojos.directives.subclasses.ImageUrlWithTextAndKey;

public class SelectEnumImageDirective implements Directive{

	private String placeHolder;
	private String key;

	private List<ImageUrlWithTextAndKey> imageUrls = new ArrayList<>();

private DirectiveTypes directiveType = DirectiveTypes.SELECTENUMIMAGE;
	
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
	public List<ImageUrlWithTextAndKey> getImageUrls() {
		return imageUrls;
	}
	public void setImageUrls(List<ImageUrlWithTextAndKey> imageUrls) {
		this.imageUrls = imageUrls;
	}
	
	

	



	
	
}
