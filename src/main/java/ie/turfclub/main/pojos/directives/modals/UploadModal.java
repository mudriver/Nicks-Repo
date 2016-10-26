package ie.turfclub.main.pojos.directives.modals;



public class UploadModal implements Modal{



	
	private ModalTypes type = ModalTypes.UPLOAD;
	private String uploadUrl;
	private String uploadTitle;
	
	public ModalTypes getType() {
		return type;
	}

	public void setType(ModalTypes type) {
		this.type = type;
	}
	
	
	

	public String getUploadTitle() {
		return uploadTitle;
	}

	public void setUploadTitle(String uploadTitle) {
		this.uploadTitle = uploadTitle;
	}

	public String getUploadUrl() {
		return uploadUrl;
	}

	public void setUploadUrl(String uploadUrl) {
		this.uploadUrl = uploadUrl;
	}
	
	
	
}
