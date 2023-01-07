package Entity;

public class Report {
	private String reportType;
	private String reportDate;
	private String Area;

	public Report(String reportType, String reportDate, String area) {
		this.reportType = reportType;
		this.reportDate = reportDate;
		Area = area;
	}

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
