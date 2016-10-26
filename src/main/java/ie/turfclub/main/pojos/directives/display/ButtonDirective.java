package ie.turfclub.main.pojos.directives.display;

import ie.turfclub.main.pojos.directives.Directive;
import ie.turfclub.main.pojos.directives.DirectiveTypes;
import ie.turfclub.main.pojos.directives.modals.Modal;

public class ButtonDirective implements Directive{

	private String key;
	private String text;
	private String onOffKey;
	private String offText;
	private String buttonClass;
	private String buttonDisabledClass;
	private String buttonGlyphiconClass;
	private String buttonOffGlyphiconClass;
	private String buttonOffClass;
	private String url;
	private String enabledDisabledKey;
	private boolean onOffUseRouteParam = false;
	private Modal modal;
	private DirectiveTypes directiveType = DirectiveTypes.BUTTON;
	private FunctionTypes functionType = FunctionTypes.URL;
	
	
	public enum FunctionTypes {
		URL,EXTERNALURL,EXTERNALURLNEWTAB,MODAL,ONOFF
	}
	
	
	
	public Modal getModal() {
		return modal;
	}
	public void setModal(Modal modal) {
		this.modal = modal;
	}

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getButtonClass() {
		return buttonClass;
	}
	public void setButtonClass(String buttonClass) {
		this.buttonClass = buttonClass;
	}
	public String getButtonGlyphiconClass() {
		return buttonGlyphiconClass;
	}
	public void setButtonGlyphiconClass(String buttonGlyphiconClass) {
		this.buttonGlyphiconClass = buttonGlyphiconClass;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getEnabledDisabledKey() {
		return enabledDisabledKey;
	}
	public void setEnabledDisabledKey(String enabledDisabledKey) {
		this.enabledDisabledKey = enabledDisabledKey;
	}
	public String getButtonDisabledClass() {
		return buttonDisabledClass;
	}
	public void setButtonDisabledClass(String buttonDisabledClass) {
		this.buttonDisabledClass = buttonDisabledClass;
	}
	public DirectiveTypes getDirectiveType() {
		return directiveType;
	}
	public void setDirectiveType(DirectiveTypes directiveType) {
		this.directiveType = directiveType;
	}
	public FunctionTypes getFunctionType() {
		return functionType;
	}
	public void setFunctionType(FunctionTypes functionType) {
		this.functionType = functionType;
	}
	public String getButtonOffGlyphiconClass() {
		return buttonOffGlyphiconClass;
	}
	public void setButtonOffGlyphiconClass(String buttonOffGlyphiconClass) {
		this.buttonOffGlyphiconClass = buttonOffGlyphiconClass;
	}
	public String getButtonOffClass() {
		return buttonOffClass;
	}
	public void setButtonOffClass(String buttonOffClass) {
		this.buttonOffClass = buttonOffClass;
	}
	public String getOffText() {
		return offText;
	}
	public void setOffText(String offText) {
		this.offText = offText;
	}
	public String getOnOffKey() {
		return onOffKey;
	}
	public void setOnOffKey(String onOffKey) {
		this.onOffKey = onOffKey;
	}
	public boolean isOnOffUseRouteParam() {
		return onOffUseRouteParam;
	}
	public void setOnOffUseRouteParam(boolean onOffUseRouteParam) {
		this.onOffUseRouteParam = onOffUseRouteParam;
	}
	
	
	

	
	
	
	
}
