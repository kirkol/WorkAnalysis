package work_charts;

import java.util.ArrayList;

public class Project500 {
	
	private String projectNr = "";
	private String dateTimeBegin = "";
	private String dateTimeEnd = "";
	
	public Project500(String project500, String date, int beginH, int beginMIN, int endH, int endMIN){
		
		String beginHString = String.format("%1$02d", beginH);
		String beginMINString = String.format("%1$02d", beginMIN);
		String endHString = String.format("%1$02d", endH);
		String endMINString = String.format("%1$02d", endMIN);
		
		this.projectNr = project500;
		this.dateTimeBegin = date + " " + beginHString + ":" + beginMINString;
		this.dateTimeEnd = date + " " + endHString + ":" + endMINString;
		
	}
	
	public String getProjectNr() {
		return projectNr;
	}
	public void setProjectNr(String projectNr) {
		this.projectNr = projectNr;
	}
	public String getDateTimeBegin() {
		return dateTimeBegin;
	}
	public void setDateTimeBegin(String dateTimeBegin) {
		this.dateTimeBegin = dateTimeBegin;
	}
	public String getDateTimeEnd() {
		return dateTimeEnd;
	}
	public void setDateTimeEnd(String dateTimeEnd) {
		this.dateTimeEnd = dateTimeEnd;
	}
	
	@Override
	
	public String toString(){
		return "Projekt: " + projectNr + " Start: " + dateTimeBegin + " End: " + dateTimeEnd;
	}
	
	
	
}
