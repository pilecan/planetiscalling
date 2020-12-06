package com.model;

public class Distance {
	private int mountainDist;
	private int cityDist;
	private int airportDist;
	private boolean line;
	
		
	/**
	 * @param city
	 * @param mountain
	 * @param airport
	 * @param line
	 */
	public Distance(int city, int mountain, int airport, boolean line) {
		super();
		this.cityDist = city;
		this.mountainDist = mountain;
		this.airportDist = airport;
		this.line = line;
	}

	public boolean isMountain() {
		return mountainDist > 0;
	}
	
	public boolean isCity() {
		return cityDist > 0;
	}
	
	public boolean isAirport() {
		return airportDist > 0;
	}
	
	public int getMountainDist() {
		return mountainDist;
	}
	public void setMountainDist(int mountain) {
		this.mountainDist = mountain;
	}
	public int getCityDist() {
		return cityDist;
	}
	public void setCityDist(int city) {
		this.cityDist = city;
	}
	public int getAirportDist() {
		return airportDist;
	}
	public void setAirportDist(int airport) {
		this.airportDist = airport;
	}
	public boolean isLine() {
		return line;
	}
	public void setLine(boolean line) {
		this.line = line;
	}

	@Override
	public String toString() {
		return "Distance [mountain=" + mountainDist + ", city=" + cityDist + ", airport=" + airportDist + ", line=" + line + "]";
	}

	

}
