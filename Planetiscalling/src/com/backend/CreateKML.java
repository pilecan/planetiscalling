package com.backend;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.main.form.Result;
import com.model.Airport;
import com.model.City;
import com.model.Landmark;
import com.model.LegPoint;
import com.model.Mountain;
import com.model.Ndb;
import com.model.Vor;
import com.util.Util;
import com.util.Utility;

public class CreateKML {
	private static String CASTEL_ICON ="http://maps.google.com/mapfiles/kml/shapes/ranger_station.png";
	private static String BUILDING_ICON ="http://maps.google.com/mapfiles/kml/shapes/homegardenbusiness.png";
	private static String LOOKTOWER_ICON ="http://maps.google.com/mapfiles/kml/shapes/target.png";
	private static String SILO_ICON ="http://maps.google.com/mapfiles/kml/shapes/donut.png";
	private static String AIRPORT_ICON ="http://maps.google.com/mapfiles/kml/shapes/airports.png";
	
	
    private static final Map<String, String> ICON_MAP;
    static {
        Map<String, String> aMap = new HashMap<>();
        aMap.put("Canyon", "http://maps.google.com/mapfiles/kml/shapes/hiker.png" ); 
        aMap.put("Cap", "http://maps.google.com/mapfiles/kml/shapes/trail.png" ); 
        aMap.put("CAMP", "http://maps.google.com/mapfiles/kml/shapes/campground.png" ); 
        aMap.put("Chalet", "http://maps.google.com/mapfiles/kml/shapes/homegardenbusiness.png" ); 
        aMap.put("Cove", "http://maps.google.com/mapfiles/kml/shapes/marina.png" ); 
        aMap.put("Historic", "http://maps.google.com/mapfiles/kml/shapes/info_circle.png" ); 
        aMap.put("Hostel", "http://maps.google.com/mapfiles/kml/shapes/lodging.png" ); 
        aMap.put("Lake", "http://maps.google.com/mapfiles/kml/shapes/fishing.png" ); 
        aMap.put("Lighthouse", "http://maps.google.com/mapfiles/kml/shapes/target.png" ); 
        aMap.put("Pavillon", "http://maps.google.com/mapfiles/kml/shapes/ranger_station.png" ); 
        aMap.put("Plane", "http://maps.google.com/mapfiles/kml/shapes/flag.png" ); 
        aMap.put("Port", "http://maps.google.com/mapfiles/kml/shapes/ferry.png" ); 
        aMap.put("Pourvoirie", "http://maps.google.com/mapfiles/kml/shapes/ranger_station.png" ); 
        aMap.put("Reserve", "http://maps.google.com/mapfiles/kml/shapes/parks.png" ); 
        aMap.put("PARK", "http://maps.google.com/mapfiles/kml/shapes/parks.png" ); 
        aMap.put("Waterfall", "http://maps.google.com/mapfiles/kml/shapes/water.png" ); 
        aMap.put("BCH", "http://maps.google.com/mapfiles/kml/shapes/water.png" ); 
        aMap.put("CHAN", "http://maps.google.com/mapfiles/kml/shapes/water.png" ); 
        aMap.put("FALL", "http://maps.google.com/mapfiles/kml/shapes/water.png" ); 
        aMap.put("ISL", "http://maps.google.com/mapfiles/kml/shapes/water.png" ); 
        aMap.put("RAP", "http://maps.google.com/mapfiles/kml/shapes/water.png" ); 
        aMap.put("LAKE", "http://maps.google.com/mapfiles/kml/shapes/water.png" ); 
        aMap.put("FOR", "http://maps.google.com/mapfiles/kml/shapes/parks.png" ); 
        aMap.put("RIV", "http://maps.google.com/mapfiles/kml/shapes/parks.png" ); 
        aMap.put("HYDR", "http://maps.google.com/mapfiles/kml/shapes/parks.png" ); 
        aMap.put("SITE", "http://maps.google.com/mapfiles/kml/shapes/parks.png" ); 
        aMap.put("RIVF", "http://maps.google.com/mapfiles/kml/shapes/parks.png" ); 
        aMap.put("Wreck", "http://maps.google.com/mapfiles/kml/shapes/poi.png" ); 
        aMap.put("River", "http://maps.google.com/mapfiles/kml/shapes/water.png" ); 
        aMap.put("SEA", "http://maps.google.com/mapfiles/kml/shapes/water.png" ); 
        aMap.put("SEAF", "http://maps.google.com/mapfiles/kml/shapes/water.png" ); 
        aMap.put("BAY", "http://maps.google.com/mapfiles/kml/shapes/water.png" ); 
        aMap.put("SEAU", "http://maps.google.com/mapfiles/kml/shapes/water.png" ); 
        aMap.put("SPRG", "http://maps.google.com/mapfiles/kml/shapes/water.png" ); 
        aMap.put("VEGL", "http://maps.google.com/mapfiles/kml/shapes/water.png" ); 
        aMap.put("SHL", "http://maps.google.com/mapfiles/kml/shapes/water.png" ); 
        aMap.put("Castle", "http://maps.google.com/mapfiles/kml/shapes/ranger_station.png" ); 
        aMap.put("GEOG", "http://maps.google.com/mapfiles/kml/shapes/ranger_station.png" ); 
        aMap.put("Village", "http://maps.google.com/mapfiles/kml/shapes/square.png" ); 
        aMap.put("Church", "http://maps.google.com/mapfiles/kml/shapes/homegardenbusiness.png" ); 
        aMap.put("Abbey", "http://maps.google.com/mapfiles/kml/shapes/homegardenbusiness.png" ); 
        aMap.put("", "http://maps.google.com/mapfiles/kml/paddle/wht-blank.png" ); 
        aMap.put("VILG", "http://maps.google.com/mapfiles/kml/paddle/wht-blank.png" ); 
        aMap.put("CITY", "http://maps.google.com/mapfiles/kml/paddle/wht-blank.png" ); 
        aMap.put(null, "http://maps.google.com/mapfiles/kml/paddle/wht-pushpin.png" ); 
        aMap.put("minor", "http://maps.google.com/mapfiles/kml/paddle/blu-stars.png" ); 
        aMap.put("TER", "http://maps.google.com/mapfiles/kml/paddle/blu-stars.png" ); 
        aMap.put("admin", "http://maps.google.com/mapfiles/kml/paddle/pink-stars.png" ); 
        aMap.put("primary", "http://maps.google.com/mapfiles/kml/paddle/ylw-stars.png" ); 
        aMap.put("volcano", "http://maps.google.com/mapfiles/kml/shapes/volcano.png" ); 
        aMap.put("vor", "http://maps.google.com/mapfiles/kml/shapes/polygon.png" ); 
        aMap.put("ndb", "http://maps.google.com/mapfiles/kml/shapes/triangle.png" ); 
        aMap.put("airport", "http://maps.google.com/mapfiles/kml/shapes/airports.png" ); 
        aMap.put("AIR", "http://maps.google.com/mapfiles/kml/shapes/airports.png" ); 
        aMap.put("mountain", "http://maps.google.com/mapfiles/kml/shapes/hiker.png" ); 
        aMap.put("MTN", "http://maps.google.com/mapfiles/kml/shapes/hiker.png" ); 
        aMap.put("CLF", "http://maps.google.com/mapfiles/kml/shapes/hiker.png" ); 
        aMap.put("CRAT", "http://maps.google.com/mapfiles/kml/shapes/poi.png" ); 
        aMap.put("VALL", "http://maps.google.com/mapfiles/kml/shapes/poi.png" ); 
        aMap.put("CAVE", "http://maps.google.com/mapfiles/kml/shapes/poi.png" ); 
        aMap.put("RES", "http://maps.google.com/mapfiles/kml/shapes/poi.png" ); 
        aMap.put("GLAC", "http://maps.google.com/mapfiles/kml/shapes/snowflake_simple.png" ); 
        aMap.put("CAPE", "http://maps.google.com/mapfiles/kml/shapes/camera.png" ); 
        aMap.put("PLN", "http://maps.google.com/mapfiles/kml/shapes/camera.png" ); 
        aMap.put("HAM", "http://maps.google.com/mapfiles/kml/shapes/homegardenbusiness.png" ); 
        aMap.put("HAM", "http://maps.google.com/mapfiles/kml/shapes/homegardenbusiness.png" ); 
        aMap.put("TOWN", "http://maps.google.com/mapfiles/kml/shapes/homegardenbusiness.png" ); 
        aMap.put("IR", "http://maps.google.com/mapfiles/kml/shapes/star.png" ); 
        aMap.put("MAR", "http://maps.google.com/mapfiles/kml/shapes/ferry.png" ); 
        aMap.put("MISC", "http://maps.google.com/mapfiles/kml/pushpin/wht-pushpin.png" ); 
        aMap.put("MIL", "http://maps.google.com/mapfiles/kml/shapes/police.png" ); 
        aMap.put("MUN1", "http://maps.google.com/mapfiles/kml/pushpin/wht-pushpin.png" ); 
        aMap.put("MUN2", "http://maps.google.com/mapfiles/kml/pushpin/wht-pushpin.png" ); 
        aMap.put("PROV", "http://maps.google.com/mapfiles/kml/paddle/ylw-stars.png" ); 
        aMap.put("RAIL", "http://maps.google.com/mapfiles/kml/shapes/tram.png" );
        aMap.put("RECR", "http://maps.google.com/mapfiles/kml/shapes/play.png" );
        aMap.put("CAMP", "http://maps.google.com/mapfiles/kml/shapes/ranger_station.png" );
        aMap.put("UNP", "http://maps.google.com/mapfiles/kml/shapes/ranger_station.png" );
        aMap.put("ROAD", "http://maps.google.com/mapfiles/kml/shapes/truck.png" );
        
        
        ICON_MAP = Collections.unmodifiableMap(aMap);
    }

	public CreateKML() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public void write(List<City> cities, String title, String fileName) {
		Writer writer = null;

		try {
			//writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("g:\\addons\\work\\castel_czech.kml"), "utf-8"));
			
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"));
			
			writer.write(createKMLHeader(cities.size(), title));
			
			for (int i = 0; i < cities.size(); i++) {
				//System.out.println(buildXML(cities.get(i)));
				writer.write(buildCityPlaceMark(cities.get(i)));
			}

			writer.write("</Folder></Document></kml>");

		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {
			}
		}

		
	}
	
	public static void makeOn(Object object, String title) {
		Writer writer = null;
     	try {
			//writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("g:\\addons\\work\\castel_czech.kml"), "utf-8"));
			
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(Utility.getInstance().getFlightPlanName(title+".kml")), "utf-8"));
			
			writer.write(createKMLHeader(1,title));
			
			if (object instanceof Result) {
				Result result = (Result)object;
				for (LegPoint legPoint : result.getLegPoints()) {
					///System.out.println(legPoint.getType()+" - "+legPoint.getIcaoIdent());
						if (title.split("-")[1].equals(legPoint.getIcaoIdent())){
							writer.write(legPoint.buildPoint());
							break;
						}
					}
				}
				

			 else if (object instanceof Airport) {
				writer.write(buildAirportPlaceMark((Airport)object));

			} else if (object instanceof Vor) {
				writer.write(buildVorPlaceMark((Vor)object));

			} else if (object instanceof City) {
				writer.write(buildCityPlaceMark((City)object));
			} else if (object instanceof Ndb) {
				writer.write(buildNdbPlaceMark((Ndb)object));
			} else if (object instanceof Mountain) {
				writer.write(buildMountainPlaceMark((Mountain)object));
			} else if (object instanceof Landmark) {
				writer.write(buildLandmarkPlaceMark((Landmark)object));
			}

			writer.write("</Folder></Document></kml>");

		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {
			}
		}

       Utility.getInstance().launchGoogleEarth(new File(Utility.getInstance().getFlightPlanName(title+".kml")));

	
	}
	
	private static String createKMLHeader(int total, String title) {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<kml xmlns=\"http://www.opengis.net/kml/2.2\" xmlns:gx=\"http://www.google.com/kml/ext/2.2\" xmlns:kml=\"http://www.opengis.net/kml/2.2\" xmlns:atom=\"http://www.w3.org/2005/Atom\">"
				+ "<Document>"
				+ "<Folder>"
				+ "<name>"+title+"</name>"
				+ "<description><div>"+total+" "+title+"</div>"
				//+ "<div>"+fligthSimPage+"</div>"
				+ "<div>KML file create by PlanetIsCalling</div>"
				+ " </description>";
	}	
	
	public static String createHeader(int total) {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<kml xmlns=\"http://earth.google.com/kml/2.2\">"
				+ "<Document><Style id=\"fsx_airport\"><IconStyle><Icon><href>http://maps.google.com/mapfiles/kml/pal2/icon48.png</href></Icon></IconStyle></Style><Folder>"
				+ "<name>KML file create by PlanetIsCalling</name>"
				+ "<description>"+total+" placemarks found!</description>";
	}	

	static public String buildCityPlaceMark(City city){
		
		String description = buildCityDescriptionLink(city);
		String icone = "";

		String coordinates =city.getLonx()+","+city.getLaty();
		
		icone = "<Style id=\"silo\"><IconStyle><Icon><href>"+ICON_MAP.get(city.getCapital())+"</href></Icon></IconStyle></Style>";


		return "<Placemark><name>"+city.getCityName()+"</name>\n"
				+ "<description><![CDATA["+description+"]]></description>\n"
				+ icone
				+ "<Point><coordinates>"+city.getCoordinates()+"</coordinates></Point>\n"
				+ "</Placemark>\n";
	}
	

	
	static public String buildCityDescriptionLink(City city){
		
		String description = "";

		description += "<div>City Name: "+Util.createHref(city.getCityName(),city.getCityName()+" "+city.getAdminName()+" "+city.getCountry()+" wikipedia", 0)+" ("
				+Util.createHref("Weather",city.getCityName()+" "+city.getAdminName()+" "+city.getCountry()+" weather", 0)+ ")</div>";
		if (!city.getCityAscii().equals(city.getCityName())) {
			description += "<div>City Acsii: "+city.getCityAscii()+"</div>";
		}
		if (!"".equals(city.getAdminName())) {
			description += "<div>State:"+Util.createHref(city.getAdminName(),city.getAdminName()+" "+city.getCountry()+" wikipedia", 0)+"</div>";
		}

		description += "<div>Country: "+Util.createHref(city.getCountry(),city.getCountry()+" wikipedia", 0)+"</div>";
		description += "<div>Population: "+city.getPopulation()+"</div>";
		if (!"".equals(city.getCapital())) {
			description += "<div>Capital: "+city.getCapital()+"</div>";
		}
    	description += "<div>Abreviations: "+city.getIso2()+"/"+city.getIso3()+"</div>";
		description += "<div>GPS: "+Util.formatGPS(city.getLaty()+","+city.getLonx())+"</div>";
		description = "<div style=\"width: 300px; font-size: 12px;\">"+description+"</div>";

		return description;
		
		}
	static public String buildCityDescriptionPlane(City city){
		
		String description = "";

		description += "<div>City Name: "+city.getCityName()+ "</div>";
		if (!city.getCityAscii().equals(city.getCityName())) {
			description += "<div>City Acsii: "+city.getCityAscii()+"</div>";
		}
		if (!"".equals(city.getAdminName())) {
			description += "<div>State:"+city.getAdminName()+"</div>";
		}

		description += "<div>Country: "+city.getCountry()+"</div>";
		description += "<div>Population: "+city.getPopulation()+"</div>";
		if (!"".equals(city.getCapital())) {
			description += "<div>Capital: "+city.getCapital()+"</div>";
		}
    	description += "<div>Abreviations: "+city.getIso2()+"/"+city.getIso3()+"</div>";
		description += "<div>GPS: "+Util.formatGPS(city.getLaty()+" , "+city.getLonx())+"</div>";
		description = "<div style=\"width: 300px; font-size: 12px;\">"+description+"</div>";

		return description;
		
		}
	
	static public String buildLandmarkDescriptionPlane(Landmark landmark){
		
		String description = "";

		//Util.createHref(landmark.getGeoName(),landmark.getGeoName()+" "+landmark.getAdmin()+" Canada wikipedia", 0)
		
		description += "<div>Name: "+Util.createHref(landmark.getGeoName(),landmark.getGeoName()+" "+landmark.getAdmin()+" Canada wikipedia", 0)+ "</div>";
		description += "<div>Category: "+landmark.getCategory()+"</div>";
		description += "<div>Description: "+landmark.getGeoTerm()+"</div>";
	
		if (!"".equals(landmark.getLanguage())) {
			description += "<div>Language: "+landmark.getLanguage()+"</div>";
		}
		if (!"".equals(landmark.getSyllabic())) {
			description += "<div>Syllabic: "+landmark.getSyllabic()+"</div>";
		}
		if (!"".equals(landmark.getLocation())) {
			description += "<div>Location: "+landmark.getLocation()+"</div>";
		}
		description += "<div>Province/Territory: "+landmark.getAdmin()+"</div>";
		
		if (!"".equals(landmark.getDecisionDate())) {
			description += "<div>Decision Date: "+landmark.getDecisionDate()+"</div>";
		}
		if (!"".equals(landmark.getSource())) {
			description += "<div>Source: "+landmark.getSource()+"</div>";
		}

		description += "<div>GPS: "+Util.formatGPS(landmark.getLaty()+" , "+landmark.getLonx())+"</div>";
		description = "<div style=\"width: 300px; font-size: 12px;\">"+description+"</div>";

		return description;
		
		}
	
	
	static public String buildLandmarkPlaceMark(Landmark landmark){

		String description =  buildLandmarkDescriptionPlane(landmark);
		String icone = "";

		String coordinates = landmark.getLonx()+","+landmark.getLaty()+","+"0";
		
		icone = "<Style id=\"silo\"><IconStyle><Icon><href>"+ICON_MAP.get(landmark.getCode())+"</href></Icon></IconStyle></Style>";


		return "<Placemark><name>"+landmark.getGeoName()+"</name>\n"
				+ "<description><![CDATA["+description+"]]></description>\n"
				+ icone
				+ "<Point><coordinates>"+coordinates+"</coordinates></Point>\n"
				+ "</Placemark>\n";
	}

	
	
	static public String buildVorPlaceMark(Vor vor){
		
		String description =  buildVorDescription(vor);
		String icone = "";

		String coordinates = vor.getLonx()+","+vor.getLaty()+","+vor.getAltitude();
		
		icone = "<Style id=\"silo\"><IconStyle><Icon><href>"+ICON_MAP.get("vor")+"</href></Icon></IconStyle></Style>";


		return "<Placemark><name>VOR "+vor.getIdent()+"</name>\n"
				+ "<description><![CDATA["+description+"]]></description>\n"
				+ icone
				+ "<Point><coordinates>"+vor.getCoordinates()+"</coordinates></Point>\n"
				+ "</Placemark>\n";
	}
	
	static public String buildVorDescription(Vor vor){
		
		String description = "";

    	description += "<div>Ident: "+vor.getIdent()+"</div>";
    	description += "<div>Name: "+vor.getName()+"</div>";
    	description += "<div>Frequency: "+Util.formatVorFrequency(vor.getFrequency())+" MHz</div>";
    	description += "<div>Range: "+vor.getRange()+"nm</div>";
    	description += "<div>Mag. Var.: "+Util.formatMagvar(vor.getMagVar())+"</div>";
    	description += "<div>Altitude: "+vor.getAltitude()+" ft ("+((int)Math.round(vor.getAltitude()/3.28084))+" m)</div>";
    	description += "<div>Type: "+vor.getType()+"</div>";
    	description += "<div>DME Only: "+(vor.getDmeOnly()==1?"Yes":"No")+"</div>";
    	description += "<div>Region: "+vor.getRegion()+"</div>";
		description += "<div>GPS: "+Util.formatGPS(vor.getLaty()+","+vor.getLonx())+"</div>";
		description = "<div style=\"width: 300px; font-size: 12px;\">"+description+"</div>";


		return description;
			
	}
/*	
	static public String buildWaypoint(LegPoint legPoint){
		
		String description = "";

    	description += "<div>Ident: "+legPoint.getIdent()+"</div>";
    	description += "<div>Name: "+legPoint.getName()+"</div>";
    	description += "<div>Frequency: "+Util.formatVorFrequency(vor.getFrequency())+" MHz</div>";
    	description += "<div>Range: "+vor.getRange()+"nm</div>";
    	description += "<div>Mag. Var.: "+Util.formatMagvar(vor.getMagVar())+"</div>";
    	description += "<div>Altitude: "+vor.getAltitude()+" ft ("+((int)Math.round(vor.getAltitude()/3.28084))+" m)</div>";
    	description += "<div>Type: "+vor.getType()+"</div>";
    	description += "<div>DME Only: "+(vor.getDmeOnly()==1?"Yes":"No")+"</div>";
    	description += "<div>Region: "+vor.getRegion()+"</div>";
		description += "<div>GPS: "+Util.formatGPS(vor.getLaty()+","+vor.getLonx())+"</div>";
		description = "<div style=\"width: 300px; font-size: 12px;\">"+description+"</div>";


		return description;
			
	}

*/	
	
	
	static public String buildNdbPlaceMark(Ndb ndb){
		String icone = "";
		String description = buildNdbDescription(ndb);

		String coordinates = ndb.getLonx()+","+ndb.getLaty()+","+ndb.getAltitude();
		
		icone = "<Style id=\"silo\"><IconStyle><Icon><href>"+ICON_MAP.get("ndb")+"</href></Icon></IconStyle></Style>";


		return "<Placemark><name>NDB "+ndb.getIdent()+"</name>\n"
				+ "<description><![CDATA["+description+"]]></description>\n"
				+ icone
				+ "<Point><coordinates>"+ndb.getCoordinates()+"</coordinates></Point>\n"
				+ "</Placemark>\n";
	}
	
	static public String buildNdbDescription(Ndb ndb){
		String description = "";

    	description += "<div>Ident: "+ndb.getIdent()+"</div>";
    	description += "<div>Name: "+ndb.getName()+"</div>";
    	description += "<div>Frequency: "+Util.formatNdbFrequency(ndb.getFrequency())+" kHz</div>";
    	description += "<div>Range: "+ndb.getRange()+"nm</div>";
    	description += "<div>Mag. Var.: "+Util.formatMagvar(ndb.getMagVar())+"</div>";
    	description += "<div>Altitude: "+((int)Math.round(ndb.getAltitude()*3.28084))+"ft ("+ndb.getAltitude()+"m)</div>";
    	description += "<div>Type: "+ndb.getType()+"</div>";
    	description += "<div>Region: "+ndb.getRegion()+"</div>";
		description += "<div>GPS: "+Util.formatGPS(ndb.getLaty()+","+ndb.getLonx())+"</div>";
		description = "<div style=\"width: 300px; font-size: 12px;\">"+description+"</div>";

		
		return description;
	}


	static public String buildMountainPlaceMark(Mountain mountain){
		
		String description = buildMountainDescription(mountain);
		String icone = "";
		String typeIcone;

		String coordinates =mountain.getLonx()+","+mountain.getLaty();
		//String coordinates =mountain.getLaty()+","+mountain.getLonx();

		if (mountain.getName().toLowerCase().contains("volcano") || !"".equals(mountain.getType())) {
		//if (mountain.getName().toLowerCase().contains("volcano") ) {
			typeIcone = "volcano";
		} else {
			typeIcone = "mountain";
		}
		
		icone = "<Style id=\"silo\"><IconStyle><Icon><href>"+ICON_MAP.get(typeIcone)+"</href></Icon></IconStyle></Style>";


		return "<Placemark><name>"+mountain.getName()+"</name>\n"
				+ "<description><![CDATA["+description+"]]></description>\n"
				+ icone
				+ "<Point><coordinates>"+coordinates+"</coordinates></Point>\n"
				+ "</Placemark>\n";
	}

	static public String buildMountainDescription(Mountain mountain){
		
		String description = "";

		description += "<div>Mountain Name: "+Util.createHref(mountain.getName(),mountain.getName()+" "+mountain.getCountry()+" wikipedia", 0)+" ("
				+Util.createHref("Weather",mountain.getName()+" "+mountain.getName()+ " "+mountain.getCountry()+" weather", 0)+ ")</div>";
		if (!"no".equals(mountain.getAlt_name())) {
			description += "<div>Alternate Name:"+Util.createHref(mountain.getAlt_name(),mountain.getAlt_name()+" "+mountain.getCountry()+" wikipedia", 0)+"</div>";
		}
		description += "<div>Elevation: "+mountain.getElevation()+"m"+" ("+Math.round(mountain.getElevation()*3.28084)+"ft)</div>";
		if (mountain.getProminence() > 0) {
			description += "<div>Prominence: "+mountain.getProminence()+"m"+" ("+Math.round(mountain.getProminence()*3.28084)+"ft)"+Util.createHref(" (?)","prominence wikipedia", 0)+" </div>";
		}

		description += "<div>Country: "+mountain.getCountry()+"</div>";
		if (!"no".equals(mountain.getComment())) {
			description += "<div>Comment: "+mountain.getComment()+"</div>";
		}
    	description += "<div>Author: "+mountain.getAuthor()+"</div>";
		description += "<div>GPS: "+Util.formatGPS(mountain.getLaty()+","+mountain.getLonx())+"</div>";
		description = "<div style=\"width: 300px; font-size: 12px;\">"+description+"</div>";

		
		return description;
		
		
		
	}
	static public String buildMountainDescriptionNoWeather(Mountain mountain){
		
		String description = "";

		description += "<div>Mountain Name: "+Util.createHref(mountain.getName(),mountain.getName()+" "+mountain.getCountry()+" wikipedia", 0);
		if (!"no".equals(mountain.getAlt_name())) {
			description += "<div>Alternate Name:"+Util.createHref(mountain.getAlt_name(),mountain.getAlt_name()+" "+mountain.getCountry()+" wikipedia", 0)+"</div>";
		}
		description += "<div>Elevation: "+mountain.getElevation()+"m"+" ("+Math.round(mountain.getElevation()*3.28084)+"ft)</div>";
		if (mountain.getProminence() > 0) {
			description += "<div>Prominence: "+mountain.getProminence()+"m"+" ("+Math.round(mountain.getProminence()*3.28084)+"ft)"+Util.createHref(" (?)","prominence wikipedia", 0)+" </div>";
		}

		description += "<div>Country: "+mountain.getCountry()+"</div>";
		if (!"no".equals(mountain.getComment())) {
			description += "<div>Comment: "+mountain.getComment()+"</div>";
		}
    	description += "<div>Author: "+mountain.getAuthor()+"</div>";
		description += "<div>GPS: "+Util.formatGPS(mountain.getLaty()+","+mountain.getLonx())+"</div>";
		description = "<div style=\"width: 300px; font-size: 12px;\">"+description+"</div>";

		
		return description;
		
		
		
	}

	static public String buildAirportPlaceMark(Airport airport){
		return "<Placemark><name>"+airport.getIdent()+" "+airport.getName()+"</name>\n"
				+ "<description><![CDATA["+airport.getDescription().replaceAll("\\|", "<br>")+"]]></description>\n"
				+ "<Style id=\"airport\"><IconStyle><Icon><href>"+ICON_MAP.get("airport")+"</href></Icon></IconStyle></Style>"
				+ "<Point><coordinates>"+airport.getCoordinates()+"</coordinates></Point>\n"
				+ "</Placemark>\n";
	}

	
	


  public static void main(String argv[]) {
	 // new WriteKML().read("g:\\addons\\work\\FR-Dordogne-Chateaux.kml","","");

  }

}