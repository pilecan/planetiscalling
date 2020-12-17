package com.model;

public class Distance {
	private int mountainDist;
	private int cityDist;
	private int airportDist;
	private int vorNdbDist;
	private boolean line;
	
		
	/**
	 * @param mountainDist
	 * @param cityDist
	 * @param airportDist
	 * @param vorNdbDist
	 * @param line
	 */
	public Distance(int cityDist, int mountainDist,  int airportDist, int vorNdbDist, boolean line) {
		super();
		this.mountainDist = mountainDist;
		this.cityDist = cityDist;
		this.airportDist = airportDist;
		this.vorNdbDist = vorNdbDist;
		this.line = line;
	}


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

	public boolean isVorNdb() {
		return vorNdbDist > 0;
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
		return "Distance [mountainDist=" + mountainDist + ", cityDist=" + cityDist + ", airportDist=" + airportDist
				+ ", vorNdbDist=" + vorNdbDist + ", line=" + line + "]";
	}


	public int getVorNdbDist() {
		return vorNdbDist;
	}


	public void setVorNdbDist(int vorNdbDist) {
		this.vorNdbDist = vorNdbDist;
	}

	

}
