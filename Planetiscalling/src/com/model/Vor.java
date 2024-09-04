package com.model;

import com.util.Util;

public class Vor {
    Integer vorId;
    Integer fileId;
    String ident;
    String name;
    String region;
    Integer airportId;
    String type;
    Integer frequency;
    String channel;
    Integer range;
    Double magVar;
    Integer dmeOnly;
    Integer dmeAltitude;
    Double dmeLonx;
    Double dmeLaty;
    Integer altitude;
    Double lonx;
    Double laty;
    
	public Vor() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Vor(Integer vorId, Integer fileId, String ident, String name, String region, Integer airportId, String type,
			Integer frequency, String channel, Integer range, Double magVar, Integer dmeOnly, Integer dmeAltitude,
			Double dmeLonx, Double dmeLaty, Integer altitude, Double lonx, Double laty) {
		super();
		this.vorId = vorId;
		this.fileId = fileId;
		this.ident = ident;
		this.name = name;
		this.region = region;
		this.airportId = airportId;
		this.type = type;
		this.frequency = frequency;
		this.channel = channel;
		this.range = range;
		this.magVar = magVar;
		this.dmeOnly = dmeOnly;
		this.dmeAltitude = dmeAltitude;
		this.dmeLonx = dmeLonx;
		this.dmeLaty = dmeLaty;
		this.altitude = altitude;
		this.lonx = lonx;
		this.laty = laty;
	}
	public Vor(Vor vor) {
		super();
		this.vorId = vor.getVorId();
		this.fileId = vor.getFileId();
		this.ident = vor.getIdent();
		this.name = vor.getName();
		this.region = vor.getRegion();
		this.airportId = vor.getAirportId();
		this.type = vor.getType();
		this.frequency = vor.getFrequency();
		this.channel = vor.getChannel();
		this.range = vor.getRange();
		this.magVar = vor.getMagVar();
		this.dmeOnly = vor.getDmeOnly();
		this.dmeAltitude = vor.getDmeAltitude();
		this.dmeLonx = vor.getDmeLonx();
		this.dmeLaty = vor.getDmeLaty();
		this.altitude = vor.getAltitude();
		this.lonx = vor.getLonx();
		this.laty = vor.getLaty();
	}
	public String getDescription() {
		
		return "|Ident: "+ident
				+"|Name: "+name
				+"|Frequency: "+Util.formatFrequency(frequency)
				+"|Range: "+range+" nm"
				+"|Magnetic Variation: "+Util.formatMagvar(magVar)
				+"|Altitude: "+((int)Math.round(altitude*3.28084)+"ft ("+altitude+"m)") 
				+"|Type: "+type
				+"|DME Only:  "+(dmeOnly==1?"Yes":"No")
				+"|Region: "+region
				
				+"";
		
	}
	public String buildKML() {
		return "<Placemark><name>"+ident+"</name>\n"
				+ "<description><![CDATA["+getDescription()+"]]></description>\n"
				+ "<styleUrl>"+getStyleUrl()+"</styleUrl>\n"
				+ "<Point><coordinates>"+getCoordinates()+"</coordinates></Point>\n"
				+ "</Placemark>\n";		
	}

	public String getStyleUrl() {
		return "|#fsx_airport";
	}


	
	public Integer getVorId() {
		return vorId;
	}
	public void setVorId(Integer vorId) {
		this.vorId = vorId;
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
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
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
	public Integer getDmeOnly() {
		return dmeOnly;
	}
	public void setDmeOnly(Integer dmeOnly) {
		this.dmeOnly = dmeOnly;
	}
	public Integer getDmeAltitude() {
		return dmeAltitude;
	}
	public void setDmeAltitude(Integer dmeAltitude) {
		this.dmeAltitude = dmeAltitude;
	}
	public Double getDmeLonx() {
		return dmeLonx;
	}
	public void setDmeLonx(Double dmeLonx) {
		this.dmeLonx = dmeLonx;
	}
	public Double getDmeLaty() {
		return dmeLaty;
	}
	public void setDmeLaty(Double dmeLaty) {
		this.dmeLaty = dmeLaty;
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
	public String getCoordinates() {
		return ""+lonx+","+laty+","+altitude;
	}

	@Override
	public String toString() {
		return "Vor [vorId=" + vorId + ", fileId=" + fileId + ", ident=" + ident + ", name=" + name + ", region="
				+ region + ", airportId=" + airportId + ", type=" + type + ", frequency=" + frequency + ", channel="
				+ channel + ", range=" + range + ", magVar=" + magVar + ", dmeOnly=" + dmeOnly + ", dmeAltitude="
				+ dmeAltitude + ", dmeLonx=" + dmeLonx + ", dmeLaty=" + dmeLaty + ", altitude=" + altitude + ", lonx="
				+ lonx + ", laty=" + laty + "]";
	}
}
