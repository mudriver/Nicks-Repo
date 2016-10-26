package ie.turfclub.main.pojos.directives.modals;

public class DeleteModal implements Modal{

	private String deleteUrl;
	private String warningText;
	private String title;
	private ModalTypes type = ModalTypes.DELETE;

	public ModalTypes getType() {
		return type;
	}

	public void setType(ModalTypes type) {
		this.type = type;
	}
	
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDeleteUrl() {
		return deleteUrl;
	}
	public void setDeleteUrl(String deleteUrl) {
		this.deleteUrl = deleteUrl;
	}
	public String getWarningText() {
		return warningText;
	}
	public void setWarningText(String warningText) {
		this.warningText = warningText;
	}
	
	
	
	
}
