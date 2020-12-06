package com.model;

public class Runway {
    private String runwayName;
	private Integer length;
	private Integer width;
	private Double runwayHeading;
	private Double magVar;
	private String surface;
    private String ilsIdent;
    private String ilsName;
	private Integer ilsFrequency;
	
	public Runway() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public String getRunwayName() {
		return runwayName;
	}
	public void setRunwayName(String runwayName) {
		this.runwayName = runwayName;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	public Double getRunwayHeading() {
		return runwayHeading;
	}
	public void setRunwayHeading(Double runwayHeading) {
		this.runwayHeading = runwayHeading;
	}
	public Double getMagVar() {
		return magVar;
	}
	public void setMagVar(Double magVar) {
		this.magVar = magVar;
	}
	public String getIlsIdent() {
		return ilsIdent;
	}
	public void setIlsIdent(String ilsIdent) {
		this.ilsIdent = ilsIdent;
	}
	public String getIlsName() {
		return ilsName;
	}
	public void setIlsName(String ilsName) {
		this.ilsName = ilsName;
	}
	public Integer getIlsFrequency() {
		return ilsFrequency;
	}
	public void setIlsFrequency(Integer ilsFrequency) {
		this.ilsFrequency = ilsFrequency;
	}
	
	public String getSurface() {
		return surface;
	}
	public void setSurface(String surface) {
		this.surface = surface;
	}
	@Override
	public String toString() {
		return "Runway [runwayName=" + runwayName + ", length=" + length + ", width=" + width + ", runwayHeading="
				+ runwayHeading + ", magVar=" + magVar + ", surface=" + surface + ", ilsIdent=" + ilsIdent
				+ ", ilsName=" + ilsName + ", ilsFrequency=" + ilsFrequency + "]";
	}
	

}
