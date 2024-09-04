package com.model;

import java.util.Map;

public class Flightplan {
    private String title;
    private String fpType;
    private String routeType;
    private String cruisingAlt;
    private String departureID;
    private String departureLLA;
    private String destinationID;
    private String destinationLLA;
    private String descr;
    private String departureName;
    private String destinationName;
    private String flightplanFile;
    
    
    
    
	public Flightplan() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public void validIcao(Map<String, Airport> hashAirport, LegPoint legPoint,boolean isDeparture, Distance dist) {
		Airport airport = hashAirport.get(legPoint.getId());
		String field = null;
		
		if (airport == null) {
			try {
				airport = hashAirport.get(legPoint.getIcaoIdent());
			} catch (NullPointerException e) {
			}
			if (airport == null) {
				if (isDeparture) {
					field = departureID;
				} else {
					field = destinationID;
				}
				airport = hashAirport.get(field);
			}
				
		}

		if (airport != null) {
			legPoint.setIcaoIdent(airport.getIdent());
			legPoint.setPosition(airport.getCoordinates());
			legPoint.correctAltitude();
		} else {
			dist.setLine(false);
		}
	}

	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getFpType() {
		return fpType;
	}


	public void setFpType(String fpType) {
		this.fpType = fpType;
	}


	public String getRouteType() {
		return routeType;
	}


	public void setRouteType(String routeType) {
		this.routeType = routeType;
	}


	public String getCruisingAlt() {
		return cruisingAlt;
	}


	public void setCruisingAlt(String cruisingAlt) {
		this.cruisingAlt = cruisingAlt;
	}


	public String getDepartureID() {
		return departureID;
	}


	public void setDepartureID(String departureID) {
		this.departureID = departureID;
	}


	public String getDepartureLLA() {
		return departureLLA;
	}


	public void setDepartureLLA(String departureLLA) {
		this.departureLLA = departureLLA;
	}


	public String getDestinationID() {
		return destinationID;
	}


	public void setDestinationID(String destinationID) {
		this.destinationID = destinationID;
	}


	public String getDestinationLLA() {
		return destinationLLA;
	}


	public void setDestinationLLA(String destinationLLA) {
		this.destinationLLA = destinationLLA;
	}


	public String getDescr() {
		return descr;
	}


	public void setDescr(String descr) {
		this.descr = descr;
	}


	public String getDepartureName() {
		return departureName;
	}


	public void setDepartureName(String departureName) {
		this.departureName = departureName;
	}


	public String getDestinationName() {
		return destinationName;
	}


	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}


	@Override
	public String toString() {
		return "Flightplan [title=" + title + ", fpType=" + fpType + ", routeType=" + routeType + ", cruisingAlt="
				+ cruisingAlt + ", departureID=" + departureID + ", departureLLA=" + departureLLA + ", destinationID="
				+ destinationID + ", destinationLLA=" + destinationLLA + ", descr=" + descr + ", departureName="
				+ departureName + ", destinationName=" + destinationName + "]";
	}


	public String getFlightplanFile() {
		return flightplanFile;
	}


	public void setFlightplanFile(String flightplanFile) {
		this.flightplanFile = flightplanFile;
	}
	
	

}
