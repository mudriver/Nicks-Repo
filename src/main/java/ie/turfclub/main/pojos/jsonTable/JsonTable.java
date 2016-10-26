package ie.turfclub.main.pojos.jsonTable;

import java.util.ArrayList;

public class JsonTable {

	private String title;
	private String dataUrl;
	private String noDataTemplateUrl;
	private String cachedDataUrl;
	private String cachedDataPrefix;
	private String cachedDataUserId;
	private ArrayList<JsonTableColumn> columns = new ArrayList<JsonTableColumn>();
	private Object savedSearch;
	private JsonSavedSearchConfig savedSearchConfig;
	private String lastSearchPrefix;
	private ArrayList<Integer> pageSizeOptions = new ArrayList<>();
	
	/**
	 * The title of the table on the page
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * Set the title of the table on the page
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * get the URL from which the table will retrieve json data to fill the page
	 */
	public String getDataUrl() {
		return dataUrl;
	}
	/**
	 * set the URL from which the table will retrieve json data to fill the page
	 * @param  dataUrl  a URL giving the http location of the data
	 */
	public void setDataUrl(String dataUrl) {
		this.dataUrl = dataUrl;
	}
	/**
	 * get the Columns that will be applied to the table
	 */
	public ArrayList<JsonTableColumn> getColumns() {
		return columns;
	}
	/**
	 * set the URL from which the table will retrieve json data to fill the page
	 * @param  columns the columns which will be applied to the table
	 */
	public void setColumns(ArrayList<JsonTableColumn> columns) {
		this.columns = columns;
	}
	
	
	
	public Object getSavedSearch() {
		return savedSearch;
	}
	public void setSavedSearch(Object savedSearch) {
		this.savedSearch = savedSearch;
	}
	public ArrayList<Integer> getPageSizeOptions() {
		return pageSizeOptions;
	}
	public void setPageSizeOptions(ArrayList<Integer> pageSizeOptions) {
		this.pageSizeOptions = pageSizeOptions;
	}
	public JsonSavedSearchConfig getSavedSearchConfig() {
		return savedSearchConfig;
	}
	public void setSavedSearchConfig(JsonSavedSearchConfig savedSearchConfig) {
		this.savedSearchConfig = savedSearchConfig;
	}
	public String getCachedDataUrl() {
		return cachedDataUrl;
	}
	public void setCachedDataUrl(String cachedDataUrl) {
		this.cachedDataUrl = cachedDataUrl;
	}
	public String getCachedDataPrefix() {
		return cachedDataPrefix;
	}
	public void setCachedDataPrefix(String cachedDataPrefix) {
		this.cachedDataPrefix = cachedDataPrefix;
	}
	public String getCachedDataUserId() {
		return cachedDataUserId;
	}
	public void setCachedDataUserId(String cachedDataUserId) {
		this.cachedDataUserId = cachedDataUserId;
	}
	public String getNoDataTemplateUrl() {
		return noDataTemplateUrl;
	}
	public void setNoDataTemplateUrl(String noDataTemplateUrl) {
		this.noDataTemplateUrl = noDataTemplateUrl;
	}
	public String getLastSearchPrefix() {
		return lastSearchPrefix;
	}
	public void setLastSearchPrefix(String lastSearchPrefix) {
		this.lastSearchPrefix = lastSearchPrefix;
	}
	
	
	
}
