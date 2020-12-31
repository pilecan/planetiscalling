package com.model;

import java.util.List;

import com.cfg.util.Util;

public class Airport {
	private Integer airportId;
	private String ident;
	private String name;
	private String iata;
	private String state;
	private String city;
	private String country;
	private String region;
	private Integer atisFrequency;
	private Integer towerFrequency;

	private String dst;
	private Integer altitude;
	private Double magVar;
	private Double hourZone;
	private String timeZone;
	
	private Double lonx;
	private Double laty;
	
	private String PlaceMarkName;
	private String description;
	private String styleUrl;
	private String point;
	private String coordinates;	
	
   private List<Runway> runways;
   
   

public Airport() {
	super();
	// TODO Auto-generated constructor stub
}



public Integer getAirportId() {
	return airportId;
}

public void setAirportId(Integer airportId) {
	this.airportId = airportId;
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

public String getIata() {
	return iata;
}

public void setIata(String iata) {
	this.iata = iata;
}

public String getState() {
	return state;
}

public void setState(String state) {
	this.state = state;
}

public String getCity() {
	return city;
}

public void setCity(String city) {
	this.city = city;
}

public String getCountry() {
	return country;
}

public void setCountry(String country) {
	this.country = country;
}

public String getRegion() {
	return region;
}

public void setRegion(String region) {
	this.region = region;
}

public Integer getAtisFrequency() {
	return atisFrequency;
}

public void setAtisFrequency(Integer atisFrequency) {
	this.atisFrequency = atisFrequency;
}

public String getTimeZone() {
	return timeZone;
}

public void setTimeZone(String timeZone) {
	this.timeZone = timeZone;
}

public String getDst() {
	return dst;
}

public void setDst(String dst) {
	this.dst = dst;
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

public List<Runway> getRunways() {
	return runways;
}

public void setRunways(List<Runway> runways) {
	this.runways = runways;
}


public Double getHourZone() {
	return hourZone;
}

public void setHourZone(Double hourZone) {
	this.hourZone = hourZone;
}





public Double getMagVar() {
	return magVar;
}



public void setMagVar(Double magVar) {
	this.magVar = magVar;
}



public String getPlaceMarkName() {
	return name;
}



public String getDescription() {
	
	String result = "";
	try {
		result = (iata!=null?"|IATA: "+iata:"")
				+ "|Airport Name: "+name.replace("&","&amp;")
				//+ "|Airport Name: "+Util.createHref(name ,name+"airport wiki", 0)+" ("+Util.createHref("Weather",name+" "+country+" weather", 0)+ ")"
				+ "|City: "+Util.createHref(city ,city+" City", 0)+" ("+Util.createHref("Weather",city+" "+country+" weather", 0)+ ")"
				+ (state!=null?"|State: "+state:"")
				+ (country!=null?"|Country: "+country:"")
				+ (timeZone!=null?"|Time Zone: "+timeZone + " ("+ Util.formatTimeZone(hourZone)+")":"").replace("&","&amp;")
				+ "|"
				+ "|Altitude: "+altitude+"ft ("+((int)Math.round(altitude/3.28084)+"m)") 
				+ "|Magnetic Variation: "+Util.formatMagvar(magVar)
				+ "|"
				+ (atisFrequency!=null && atisFrequency > 0?"|ATIS: "+Util.formatFrequency(atisFrequency):"")
				+ (towerFrequency!=null && towerFrequency > 0?"|Tower: "+Util.formatFrequency(towerFrequency):"")
				+ "|"
				+ makeRunways()
				+ "|";
	} catch (Exception e) {
		// TODO Auto-generated catch block
		//e.printStackTrace();
	} 
	
	return result;
}



private String makeRunways(){
	String runstr="";
	if (runways != null){
		for(Runway runway:runways){
			runstr +=  "|Runway: "+runway.getRunwayName()+ " Course: "+Util.formatHeading(runway.getRunwayHeading(),runway.getMagVar(),false);
			runstr +=  "|Length: "+Util.formatLenght(runway.getLength(),runway.getWidth(),runway.getSurface());
			if (runway.getIlsIdent() != null){
				runstr +=  "|"+runway.getIlsName()+"("+runway.getIlsIdent()+") "+Util.formatFrequency(runway.getIlsFrequency());
			}
			runstr += "|";
		}
	
	}
	
	return runstr;
}


public String getStyleUrl() {
	return "|#fsx_airport";
}



public String getPoint() {
	return "|";
}



public String getCoordinates() {
	return ""+lonx+","+laty+","+altitude;
}



public Integer getTowerFrequency() {
	return towerFrequency;
}



public void setTowerFrequency(Integer towerFrequency) {
	this.towerFrequency = towerFrequency;
}



@Override
public String toString() {
	return "Airport [airportId=" + airportId + ", ident=" + ident + ", name=" + name + ", iata=" + iata + ", state="
			+ state + ", city=" + city + ", country=" + country + ", region=" + region + ", atisFrequency="
			+ atisFrequency + ", towerFrequency=" + towerFrequency + ", dst=" + dst + ", altitude=" + altitude
			+ ", magVar=" + magVar + ", hourZone=" + hourZone + ", timeZone=" + timeZone + ", lonx=" + lonx + ", laty="
			+ laty + ", PlaceMarkName=" + PlaceMarkName + ", description=" + description + ", styleUrl=" + styleUrl
			+ ", point=" + point + ", coordinates=" + coordinates + "]";
}


}
