package Entity;

public class Report {
	private String reportType;
	private String reportDate;
	private String Area;
	
	/**
	 * Constructor for creating a new Report object.
	 * @param reportType- a string representing the type of report
	 * @param reportDate- a string representing the date the report was generated
	 * @param area- a string representing the area the report pertains to
	 */
	public Report(String reportType, String reportDate, String Area) {
		this.reportType = reportType;
		this.reportDate = reportDate;
		this.Area = Area;
	}
	/**
	 * getters and setters for the report entity
	 * @author David
	 * */
	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getReportDate() {
		return reportDate;
	}

	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}

	public String getArea() {
		return Area;
	}

	public void setArea(String area) {
		Area = area;
	}

}
