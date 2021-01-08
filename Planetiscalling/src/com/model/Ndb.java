package com.model;

import com.util.Util;

public class Ndb {
    Integer ndbId;      
    Integer fileId;      
    String ident; 
    String name; 
    String region; 
    Integer airportId ;
    String type; 
    Integer frequency;
    Integer range;
    Double magVar;  
    Integer altitude;
    Double lonx;  
    Double laty;
    
    
	public Ndb() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Ndb(Integer ndbId, Integer fileId, String ident, String name, String region, Integer airportId, String type,
			Integer frequency, Integer range, Double magVar, Integer altitude, Double lonx, Double laty) {
		super();
		this.ndbId = ndbId;
		this.fileId = fileId;
		this.ident = ident;
		this.name = name;
		this.region = region;
		this.airportId = airportId;
		this.type = type;
		this.frequency = frequency;
		this.range = range;
		this.magVar = magVar;
		this.altitude = altitude;
		this.lonx = lonx;
		this.laty = laty;
	}
	public Ndb(Ndb ndb) {
		super();
		this.ndbId = ndb.getNdbId();
		this.fileId = ndb.getFileId();
		this.ident = ndb.getIdent();
		this.name = ndb.getName();
		this.region = ndb.getRegion();
		this.airportId = ndb.getAirportId();
		this.type = ndb.getType();
		this.frequency = ndb.getFrequency();
		this.range = ndb.getRange();
		this.magVar = ndb.getMagVar();
		this.altitude = ndb.getAltitude();
		this.lonx = ndb.getLonx();
		this.laty = ndb.getLaty();
	}
	public String getDescription() {
		
		return "|Ident: "+ident
				+"|Name: "+name
				+"|Frequency: "+Util.formatNdbFrequency(frequency)
				+"|Range: "+range+" nm"
				+"|Magnetic Variation: "+Util.formatMagvar(magVar)
				+"|Altitude: "+((int)Math.round(altitude*3.28084)+"ft ("+altitude+"m)") 
				+"|Type: "+type
				+"|Region: "+region
				
				+"";
		
	}

	public String getCoordinates() {
		return ""+lonx+","+laty+","+altitude;
	}
	
	public Integer getNdbId() {
		return ndbId;
	}
	public void setNdbId(Integer ndbId) {
		this.ndbId = ndbId;
	}
	public Integer getFileId() {
		return fileId;
	}
	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}
	public String getIdent() {
		return ident;
	}
	public void setIdent(String ident) {
		this.ident = ident;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public Integer getAirportId() {
		return airportId;
	}
	public void setAirportId(Integer airportId) {
		this.airportId = airportId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getFrequency() {
		return frequency;
	}
	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}
	public Integer getRange() {
		return range;
	}
	public void setRange(Integer range) {
		this.range = range;
	}
	public Double getMagVar() {
		return magVar;
	}
	public void setMagVar(Double magVar) {
		this.magVar = magVar;
	}
	public Integer getAltitude() {
		return altitude;
	}
	public void setAltitude(Integer altitude) {
		this.altitude = altitude;
	}
	public Double getLonx() {
		return lonx;
	}
	public void setLonx(Double lonx) {
		this.lonx = lonx;
	}
	public Double getLaty() {
		return laty;
	}
	public void setLaty(Double laty) {
		this.laty = laty;
	}
	@Override
	public String toString() {
		return "Ndb [ndbId=" + ndbId + ", fileId=" + fileId + ", ident=" + ident + ", name=" + name + ", region="
				+ region + ", airportId=" + airportId + ", type=" + type + ", frequency=" + frequency + ", range="
				+ range + ", magVar=" + magVar + ", altitude=" + altitude + ", lonx=" + lonx + ", laty=" + laty + "]";
	}  
}
