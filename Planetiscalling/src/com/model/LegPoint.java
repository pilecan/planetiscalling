package com.model;

import java.text.DecimalFormat;

import com.util.Util;

public class LegPoint {
	private String id;
	private Integer order;
	private String type;
	private String position;
	private Double altitude;
	private double distFrom;
	private String visible;
	private double lonx;
	private double laty;
	private String icaoIdent;
	private String icaoRegion;
    private static DecimalFormat df = new DecimalFormat("0.00");


	public LegPoint(double lonx, double laty) {
		super();
		this.lonx = lonx;
		this.laty = laty;
	}

	public LegPoint(String id, String type, String position, String visible) {
		super();
		this.id = id;
		this.type = type;
		this.position = position;
		this.lonx = Double.parseDouble(position.split(",")[0]);
		this.laty = Double.parseDouble(position.split(",")[1]);
		this.altitude = altitude;
		try {
			this.altitude = Double.parseDouble(position.split(",")[2]);
		} catch (NumberFormatException e) {
			this.altitude  = 0.0;
		}
		this.visible = visible;
	}

	public LegPoint(String id, String type, String position, String visible, Double altitude) {
		super();
		this.id = id;
		this.type = type;
		this.position = position;
		this.lonx = Double.parseDouble(position.split(",")[0]);
		this.laty = Double.parseDouble(position.split(",")[1]);
		this.altitude = altitude;
		this.visible = visible;
	}
	public LegPoint() {
		super();
		// TODO Auto-generated constructor stub
	}
		
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
		this.lonx = Double.parseDouble(position.split(",")[0]);
		this.laty = Double.parseDouble(position.split(",")[1]);
		try {
			this.altitude = Double.parseDouble(position.split(",")[2]);
		} catch (NumberFormatException e) {
			this.altitude  = 0.0;
		}
	}
	
	public void correctAltitude() {
		this.lonx = Double.parseDouble(position.split(",")[0]);
		this.laty = Double.parseDouble(position.split(",")[1]);
		try {
			this.altitude = Double.parseDouble(position.split(",")[2])/3.28084;
		} catch (NumberFormatException e) {
			this.altitude  = 0.0;
		}
		
		updatePosition();
	}
	
	private void updatePosition() {
		position = lonx+","+laty+","+altitude;
	}

	public String buildPoint(){
		String info = getId()+"   "+getAltitude()*3.28084+"ft";

		return "<Placemark><name>"+id+"</name>\n"
				+ "<description><![CDATA[ Type: "+getType()+" <br> Altitude: "+getAltitude()*3.28084+" ft]]></description>\n"

				+ "<Style id=\"Point\">" + 
				"		<IconStyle>"
				+ "			<scale>0.7</scale>\r\n" + 
				"			<Icon>" + 
				"				<href>http://maps.google.com/mapfiles/kml/shapes/triangle.png</href>" + 
				"			</Icon>" + 
				"		</IconStyle>" + 
				"		<color>ff0080ff</color>"
				+ "</Style>"
				+ "  <visibility>"+visible+"</visibility>"  
				+ "<Point>"
				+ "<altitudeMode>absolute</altitudeMode>"  
				+ "<coordinates>"+position+"</coordinates>"
				+ "</Point>\n"
				+ "</Placemark>\n";
	}
	
	public String  getHmlString() {
		return "<html>\n"
	            + "<body>\n"
	            + "<h3>"+id+"</h3>\n"
	            + "Type: "+getType()+"<br>\n"
	            + "Altitude: "+Math.round(getAltitude()*3.28084)+"<br>\n"
	            + "GPS: "+Util.formatGPS(getLonx())+","+Util.formatGPS(getLaty())+"<br>\n"
	            + "";
	}

	public String getCoordinates() {
		return ""+lonx+","+laty+","+altitude;
	}

	
	public String getVisible() {
		return visible;
	}
	public void setVisible(String visible) {
		this.visible = visible;
	}
	public String showforKML() {
		return id+"|"+"<coordinates>"+position+"</coordinates>";
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public Double getAltitude() {
		return altitude;
	}
	public double getDistFrom() {
		return distFrom;
	}
	public void setDistFrom(double distFrom) {
		this.distFrom = distFrom;
	}
	public double getLonx() {
		return lonx;
	}
	public void setLonx(double lonx) {
		this.lonx = lonx;
	}
	public double getLaty() {
		return laty;
	}
	public void setLaty(double laty) {
		this.laty = laty;
	}
	public void setNewAltitude(Double altitude) {
		this.altitude = altitude;
		updatePosition(); 
	}
	public String getIcaoIdent() {
		return icaoIdent;
	}
	public void setIcaoIdent(String icaoIdent) {
		this.icaoIdent = icaoIdent;
	}

	public String getIcaoRegion() {
		return icaoRegion;
	}

	public void setIcaoRegion(String icaoRegion) {
		this.icaoRegion = icaoRegion;
	}

	@Override
	public String toString() {
		return "LegPoint [id=" + id + ", order=" + order + ", type=" + type + ", position=" + position + ", altitude="
				+ altitude + ", distFrom=" + distFrom + ", visible=" + visible + ", lonx=" + lonx + ", laty=" + laty
				+ ", icaoIdent=" + icaoIdent + ", icaoRegion=" + icaoRegion + "]";
	}

}
