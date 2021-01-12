package com.model;

public class TimeZones {
	private String abbr;
	private String name;
	private String hour;
	
	
	public TimeZones() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TimeZones(String abbr, String name, String hour) {
		super();
		this.abbr = abbr;
		this.name = name;
		this.hour = hour;
	}
	public String getAbbr() {
		return abbr;
	}
	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}
	@Override
	public String toString() {
		return "TimeZones [abbr=" + abbr + ", name=" + name + ", hour=" + hour + "]";
	}
	

}
