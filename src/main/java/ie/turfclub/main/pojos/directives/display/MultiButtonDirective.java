package ie.turfclub.main.pojos.directives.display;

import java.util.ArrayList;

import ie.turfclub.main.pojos.directives.Directive;
import ie.turfclub.main.pojos.directives.DirectiveTypes;

public class MultiButtonDirective implements Directive{

	private DirectiveTypes directiveType = DirectiveTypes.MULTIBUTTON;
	private ArrayList<ButtonDirective> buttons = new ArrayList<>();
	
	public ArrayList<ButtonDirective> getButtons() {
		return buttons;
	}
	public void setButtons(ArrayList<ButtonDirective> buttons) {
		this.buttons = buttons;
	}
	@Override
	public DirectiveTypes getDirectiveType() {
		return directiveType;
	}
	@Override
	public void setDirectiveType(DirectiveTypes directiveType) {
		
		
	}
	
	
	
}
