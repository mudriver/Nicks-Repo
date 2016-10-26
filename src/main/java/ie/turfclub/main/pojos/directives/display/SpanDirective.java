package ie.turfclub.main.pojos.directives.display;

import ie.turfclub.main.pojos.directives.Directive;
import ie.turfclub.main.pojos.directives.DirectiveTypes;

public class SpanDirective implements Directive{

	private String key;
	private String splitter;
	private int splitAfter;
	private DirectiveTypes directiveType = DirectiveTypes.SPAN;
	
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

	public String getSplitter() {
		return splitter;
	}

	public void setSplitter(String splitter) {
		this.splitter = splitter;
	}

	public int getSplitAfter() {
		return splitAfter;
	}

	public void setSplitAfter(int splitAfter) {
		this.splitAfter = splitAfter;
	}
	
	
	
}
