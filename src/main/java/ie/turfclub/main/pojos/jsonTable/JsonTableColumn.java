package ie.turfclub.main.pojos.jsonTable;


import ie.turfclub.main.pojos.directives.Directive;

public class JsonTableColumn {

	private String title;
	private boolean sortable = false;
	private Directive directive;
	private String columnWidth = "auto";
	private JsonColumnSearchConfig searchConfig;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	
	
	public JsonColumnSearchConfig getSearchConfig() {
		return searchConfig;
	}
	public void setSearchConfig(JsonColumnSearchConfig searchConfig) {
		this.searchConfig = searchConfig;
	}
	public Directive getDirective() {
		return directive;
	}
	public void setDirective(Directive directive) {
		this.directive = directive;
	}
	public String getColumnWidth() {
		return columnWidth;
	}
	public void setColumnWidth(String columnWidth) {
		this.columnWidth = columnWidth;
	}
	public boolean isSortable() {
		return sortable;
	}
	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}
	

	
	
	
	 
	
	
}
