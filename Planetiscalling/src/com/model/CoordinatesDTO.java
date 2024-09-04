package com.model;

public class CoordinatesDTO {
	private Long id;
	private double latitude;
	private double longnitude;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public CoordinatesDTO(double latitude, double longnitude) {
		super();
		this.latitude = latitude;
		this.longnitude = longnitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongnitude() {
		return longnitude;
	}
	public void setLongnitude(double longnitude) {
		this.longnitude = longnitude;
	}

}
