package com.util;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cfg.util.Util;
import com.model.City;
import com.model.Mountain;
import com.model.Ndb;
import com.model.Vor;

public class CreateKML {
	private static String CASTEL_ICON ="http://maps.google.com/mapfiles/kml/shapes/ranger_station.png";
	private static String BUILDING_ICON ="http://maps.google.com/mapfiles/kml/shapes/homegardenbusiness.png";
	private static String LOOKTOWER_ICON ="http://maps.google.com/mapfiles/kml/shapes/target.png";
	private static String SILO_ICON ="http://maps.google.com/mapfiles/kml/shapes/donut.png";
	private static String AIRPORT_ICON ="http://maps.google.com/mapfiles/kml/shapes/airports.png";
	
	
	
/*	private static final Map<Object, Object> ICON_MAP =
		    Arrays.stream(new String[][] {
		        { "Airstrip", "http://maps.google.com/mapfiles/kml/shapes/airports.png" }, 
		        { "Animal", "http://maps.google.com/mapfiles/kml/shapes/camera.png" }, 
		        { "Camping", "http://maps.google.com/mapfiles/kml/shapes/campground.png" }, 
		        { "Canyon", "http://maps.google.com/mapfiles/kml/shapes/hiker.png" }, 
		        { "Cap", "http://maps.google.com/mapfiles/kml/shapes/trail.png" }, 
		        { "Chalet", "http://maps.google.com/mapfiles/kml/shapes/homegardenbusiness.png" }, 
		        { "Cove", "http://maps.google.com/mapfiles/kml/shapes/marina.png" }, 
		        { "Historic", "http://maps.google.com/mapfiles/kml/shapes/info_circle.png" }, 
		        { "Hostel", "http://maps.google.com/mapfiles/kml/shapes/lodging.png" }, 
		        { "Lake", "http://maps.google.com/mapfiles/kml/shapes/fishing.png" }, 
		        { "Lighthouse", "http://maps.google.com/mapfiles/kml/shapes/target.png" }, 
		        { "Pavillon", "http://maps.google.com/mapfiles/kml/shapes/ranger_station.png" }, 
		        { "Plane", "http://maps.google.com/mapfiles/kml/shapes/flag.png" }, 
		        { "Port", "http://maps.google.com/mapfiles/kml/shapes/ferry.png" }, 
		        { "Pourvoirie", "http://maps.google.com/mapfiles/kml/shapes/ranger_station.png" }, 
		        { "Reserve", "http://maps.google.com/mapfiles/kml/shapes/parks.png" }, 
		        { "Waterfall", "http://maps.google.com/mapfiles/kml/shapes/water.png" }, 
		        { "Wreck", "http://maps.google.com/mapfiles/kml/shapes/poi.png" }, 
		        { "River", "http://maps.google.com/mapfiles/kml/shapes/water.png" }, 
		        { "Castle", "http://maps.google.com/mapfiles/kml/shapes/ranger_station.png" }, 
		        { "Village", "http://maps.google.com/mapfiles/kml/shapes/square.png" }, 
		        { "Church", "http://maps.google.com/mapfiles/kml/shapes/homegardenbusiness.png" }, 
		        { "Abbey", "http://maps.google.com/mapfiles/kml/shapes/homegardenbusiness.png" }, 
		        { "", "http://maps.google.com/mapfiles/kml/paddle/wht-blank.png" }, 
		        { null, "http://maps.google.com/mapfiles/kml/paddle/wht-blank.png" }, 
		        { "minor", "http://maps.google.com/mapfiles/kml/paddle/blu-stars.png" }, 
		        { "admin", "http://maps.google.com/mapfiles/kml/paddle/pink-stars.png" }, 
		        { "primary", "http://maps.google.com/mapfiles/kml/paddle/ylw-stars.png" }, 
		        { "volcano", "http://maps.google.com/mapfiles/kml/shapes/volcano.png" }, 
		        { "mountain", "http://maps.google.com/mapfiles/kml/shapes/hiker.png" }, 
		    }).collect(Collectors.toMap(kv -> kv[0], kv -> kv[1]));
*/	
    private static final Map<String, String> ICON_MAP;
    static {
        Map<String, String> aMap = new HashMap<>();
        aMap.put("Canyon", "http://maps.google.com/mapfiles/kml/shapes/hiker.png" ); 
        aMap.put("Cap", "http://maps.google.com/mapfiles/kml/shapes/trail.png" ); 
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
        aMap.put("Waterfall", "http://maps.google.com/mapfiles/kml/shapes/water.png" ); 
        aMap.put("Wreck", "http://maps.google.com/mapfiles/kml/shapes/poi.png" ); 
        aMap.put("River", "http://maps.google.com/mapfiles/kml/shapes/water.png" ); 
        aMap.put("Castle", "http://maps.google.com/mapfiles/kml/shapes/ranger_station.png" ); 
        aMap.put("Village", "http://maps.google.com/mapfiles/kml/shapes/square.png" ); 
        aMap.put("Church", "http://maps.google.com/mapfiles/kml/shapes/homegardenbusiness.png" ); 
        aMap.put("Abbey", "http://maps.google.com/mapfiles/kml/shapes/homegardenbusiness.png" ); 
        aMap.put("", "http://maps.google.com/mapfiles/kml/paddle/wht-blank.png" ); 
        aMap.put(null, "http://maps.google.com/mapfiles/kml/paddle/wht-blank.png" ); 
        aMap.put("minor", "http://maps.google.com/mapfiles/kml/paddle/blu-stars.png" ); 
        aMap.put("admin", "http://maps.google.com/mapfiles/kml/paddle/pink-stars.png" ); 
        aMap.put("primary", "http://maps.google.com/mapfiles/kml/paddle/ylw-stars.png" ); 
        aMap.put("volcano", "http://maps.google.com/mapfiles/kml/shapes/volcano.png" ); 
        aMap.put("vor", "http://maps.google.com/mapfiles/kml/shapes/polygon.png" ); 
        aMap.put("ndb", "http://maps.google.com/mapfiles/kml/shapes/triangle.png" ); 
        
        aMap.put("mountain", "http://maps.google.com/mapfiles/kml/shapes/hiker.png" ); 
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
	
	private String createKMLHeader(int total, String title) {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<kml xmlns=\"http://www.opengis.net/kml/2.2\" xmlns:gx=\"http://www.google.com/kml/ext/2.2\" xmlns:kml=\"http://www.opengis.net/kml/2.2\" xmlns:atom=\"http://www.w3.org/2005/Atom\">"
				+ "<Document>"
				+ "<Folder>"
				+ "<name>"+title+"</name>"
				+ "<description><div>"+total+" "+title+"</div>"
				//+ "<div>"+fligthSimPage+"</div>"
				+ "<div>KML file create by pierre.legault@gmail.com</div>"
				+ " </description>";
	}	

	static public String buildCityPlaceMark(City city){
		
		String description = "";
		String icone = "";

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
		description += "<div>GPS: "+city.getLaty()+","+city.getLonx()+"</div>";
		description = "<div style=\"width: 300px; font-size: 12px;\">"+description+"</div>";

		String coordinates =city.getLonx()+","+city.getLaty();
		
		
		icone = "<Style id=\"silo\"><IconStyle><Icon><href>"+ICON_MAP.get(city.getCapital())+"</href></Icon></IconStyle></Style>";


		return "<Placemark><name>"+city.getCityName()+"</name>\n"
				+ "<description><![CDATA["+description+"]]></description>\n"
				+ icone
				+ "<Point><coordinates>"+city.getCoordinates()+"</coordinates></Point>\n"
				+ "</Placemark>\n";
	}
	
	
	static public String buildVorPlaceMark(Vor vor){
		
		String description = "";
		String icone = "";

    	description += "<div>Ident: "+vor.getIdent()+"</div>";
    	description += "<div>Name: "+vor.getName()+"</div>";
    	description += "<div>Frequency: "+Util.formatVorFrequency(vor.getFrequency())+" MHz</div>";
    	description += "<div>Range: "+vor.getRange()+"nm</div>";
    	description += "<div>Mag. Var.: "+Util.formatMagvar(vor.getMagVar())+"</div>";
    	description += "<div>Altitude: "+vor.getAltitude()+" ft ("+((int)Math.round(vor.getAltitude()/3.28084))+" m)</div>";
    	description += "<div>Type: "+vor.getType()+"</div>";
    	description += "<div>DME Only: "+(vor.getDmeOnly()==1?"Yes":"No")+"</div>";
    	description += "<div>Region: "+vor.getRegion()+"</div>";
		description += "<div>GPS: "+vor.getLaty()+","+vor.getLonx()+"</div>";
		description = "<div style=\"width: 300px; font-size: 12px;\">"+description+"</div>";

		String coordinates = vor.getLonx()+","+vor.getLaty()+","+vor.getAltitude();
		
		icone = "<Style id=\"silo\"><IconStyle><Icon><href>"+ICON_MAP.get("vor")+"</href></Icon></IconStyle></Style>";


		return "<Placemark><name>VOR "+vor.getIdent()+"</name>\n"
				+ "<description><![CDATA["+description+"]]></description>\n"
				+ icone
				+ "<Point><coordinates>"+vor.getCoordinates()+"</coordinates></Point>\n"
				+ "</Placemark>\n";
	}
	
	static public String buildNdbPlaceMark(Ndb ndb){
		
		String description = "";
		String icone = "";

    	description += "<div>Ident: "+ndb.getIdent()+"</div>";
    	description += "<div>Name: "+ndb.getName()+"</div>";
    	description += "<div>Frequency: "+Util.formatNdbFrequency(ndb.getFrequency())+" kHz</div>";
    	description += "<div>Range: "+ndb.getRange()+"nm</div>";
    	description += "<div>Mag. Var.: "+Util.formatMagvar(ndb.getMagVar())+"</div>";
    	description += "<div>Altitude: "+((int)Math.round(ndb.getAltitude()*3.28084))+"ft ("+ndb.getAltitude()+"m)</div>";
    	description += "<div>Type: "+ndb.getType()+"</div>";
    	description += "<div>Region: "+ndb.getRegion()+"</div>";
		description += "<div>GPS: "+ndb.getLaty()+","+ndb.getLonx()+"</div>";
		description = "<div style=\"width: 300px; font-size: 12px;\">"+description+"</div>";

		String coordinates = ndb.getLonx()+","+ndb.getLaty()+","+ndb.getAltitude();
		
		icone = "<Style id=\"silo\"><IconStyle><Icon><href>"+ICON_MAP.get("ndb")+"</href></Icon></IconStyle></Style>";


		return "<Placemark><name>NDB "+ndb.getIdent()+"</name>\n"
				+ "<description><![CDATA["+description+"]]></description>\n"
				+ icone
				+ "<Point><coordinates>"+ndb.getCoordinates()+"</coordinates></Point>\n"
				+ "</Placemark>\n";
	}

	static public String buildMountainPlaceMark(Mountain mountain){
		
		String description = "";
		String icone = "";
		String typeIcone;

		description += "<div>Mountain Name: "+Util.createHref(mountain.getName(),mountain.getName()+" wikipedia", 0)+" ("
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
		description += "<div>GPS: "+mountain.getLaty()+","+mountain.getLonx()+"</div>";
		description = "<div style=\"width: 300px; font-size: 12px;\">"+description+"</div>";

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


  public static void main(String argv[]) {
	 // new WriteKML().read("g:\\addons\\work\\FR-Dordogne-Chateaux.kml","","");

  }

}