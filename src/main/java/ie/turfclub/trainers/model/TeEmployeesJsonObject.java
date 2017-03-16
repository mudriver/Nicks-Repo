package ie.turfclub.trainers.model;

import java.util.List;

import com.google.gson.annotations.Expose;

public class TeEmployeesJsonObject {
	@Expose 	
	long iTotalRecords;
	@Expose 
	    long iTotalDisplayRecords;
	@Expose 
	    String sEcho;
	@Expose 
	    String sColumns;
	@Expose 
	    List<TeEmployees> aaData;

		public long getiTotalRecords() {
			return iTotalRecords;
		}

		public void setiTotalRecords(long iTotalRecords) {
			this.iTotalRecords = iTotalRecords;
		}

		public long getiTotalDisplayRecords() {
			return iTotalDisplayRecords;
		}

		public void setiTotalDisplayRecords(long iTotalDisplayRecords) {
			this.iTotalDisplayRecords = iTotalDisplayRecords;
		}

		public String getsEcho() {
			return sEcho;
		}

		public void setsEcho(String sEcho) {
			this.sEcho = sEcho;
		}

		public String getsColumns() {
			return sColumns;
		}

		public void setsColumns(String sColumns) {
			this.sColumns = sColumns;
		}

		public List<TeEmployees> getAaData() {
			return aaData;
		}

		public void setAaData(List<TeEmployees> aaData) {
			this.aaData = aaData;
		}
	    
	    
}
