package com.db;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.backend.CreateKML;
import com.backend.ReadFsxPlan;
import com.cfg.common.Dataline;
import com.geo.util.Geoinfo;
import com.model.Airport;
import com.model.Boundingbox;
import com.model.City;
import com.model.Distance;
import com.model.Flightplan;
import com.model.Landmark;
import com.model.LegPoint;
import com.model.Mountain;
import com.model.Ndb;
import com.model.Vor;
import com.util.Utility;
import com.util.UtilityMap;


public class CreateKmlFSPlan{
	
	
	private String flightPlanFile;// = "C:\\Users\\Pierre\\AppData\\Local\\Packages\\Microsoft.FlightSimulator_8wekyb3d8bbwe\\LocalState\\PHNGPHJR.pln";

	private Map<String, Airport> selectedAirports ;
	private Map<String, Landmark> selectedLandmarks ;

	private Map<String, City> selectedCities ;
	private Map<String, Mountain> selectedMountains ;
	private Map<Integer, Vor> selectedVors;
	private Map<Integer, Ndb> selectedNdbs;
	private List <String> addonList;
	
	private String kmlFlightPlanFile = "/data/last_flightplan.kml";

	
	private LinkedList<LegPoint> legPoints;
	
	
	private LegPoint atcWaypoint;
	
	//private double distanceRequired;
	
	private Distance dist;
	
	private ReadFsxPlan fsxPlan;
	

	private boolean isDone;
	
	private List<City> cities;
	private List<Mountain> mountains;
	private List<Vor> vors;
	private List<Ndb> ndbs;
	
	private Dataline dataline;

	
	private SelectAirport selectAirport;
	private CreateKML createKML;
	
	private int current;
	
	private double distanceBetween = 0;
	private double altitude = 0;
	private String departure;
	private String destination;
	private Flightplan flightplan;
	private String flightPlanTitle;

	
	
	public CreateKmlFSPlan(String flightPlanFile,  Distance dist, 
			List<City> cities, 
			List<Mountain> mountains,
			List<Vor> vors,
			List<Ndb> ndbs) throws FileNotFoundException,NoPoints, NullPointerException, IOException{
		this.flightPlanFile = flightPlanFile;
		this.dist = dist;
		this.selectedAirports = new HashMap<>();
		this.selectedLandmarks = new TreeMap<>();
		this.selectedCities = new HashMap<>();
		this.selectedMountains = new HashMap<>();
		this.selectedNdbs = new HashMap<>();
		this.selectedVors = new HashMap<>();
		this.isDone = false;
		this.cities = cities;
		this.mountains = mountains;
		this.vors = vors;
		this.ndbs = ndbs;
		
		this.dataline = new Dataline();
		
		current = 0;
	
		fsxPlan = new ReadFsxPlan(flightPlanFile);
		
		long startTime = System.currentTimeMillis();
		
		
		this.flightplan = fsxPlan.getFlightplan();
		
		legPoints = fsxPlan.getLegPoints();
	
		//check if altitude as been changed 
		if (dist.getAltitude() != 0 && dist.getAltitude() != Double.parseDouble(flightplan.getCruisingAlt())) {
			fsxPlan.modifyAltitude(legPoints, Double.parseDouble(flightplan.getCruisingAlt()), dist.getAltitude());
			flightplan.setCruisingAlt(dist.getAltitude()+"");

		} else {
		}
			

		
		if (legPoints.size() > 0){
			makeFlightPlan(flightplan);
		} else {
			//System.out.println("points = "+points.size());
			throw  new NoPoints("Your Flight Plan don't return any Waypoints...");
		}
		
		long stopTime = System.currentTimeMillis();
		System.out.println(" stopTime - startTime = "+(stopTime - startTime));

		
	}
	
	
	public void makeFlightPlan(Flightplan flightplan) throws FileNotFoundException, NullPointerException, IOException, NoPoints{
		
		if (legPoints.size() == 0){
			throw  new NoPoints("Your Flight Plan don't return any Waypoints...");
		} 
		

		selectAirport = new SelectAirport(); 
		selectAirport.select("");
		
		flightplan.validIcao(selectAirport.getMapAirport(), legPoints.get(0), true, dist);
		flightplan.validIcao(selectAirport.getMapAirport(), legPoints.get(legPoints.size()-1), false, dist);
		
		int cptNew = 0;
		
		boolean isfound = false;
		boolean isfinish = false;
		String[] begin = null;
		String[] end = null;

		altitude = Double.parseDouble(flightplan.getCruisingAlt())/3.28084;
	    while (!isfinish) {
			for (int i = 0; i < legPoints.size()-1; i++) {
				isfound = false;

				begin = legPoints.get(i).getPosition().split(",");
				end = legPoints.get(i+1).getPosition().split(",");


				distanceBetween = Geoinfo.distance(Double.parseDouble(begin[1]), Double.parseDouble(begin[0]), Double.parseDouble(end[1]), Double.parseDouble(end[0]), 'N');
				if (distanceBetween > 15) {
					
					 String midpoint = Geoinfo.midpoint(Double.parseDouble(begin[1]), Double.parseDouble(begin[0]), Double.parseDouble(end[1]), Double.parseDouble(end[0]));
				     LegPoint legPoint = new LegPoint("MID","MID",midpoint+","+legPoints.get(i+1).getPosition().split(",")[2],"0"); 
					 
				   legPoints.add(i+1,legPoint);
					 
				   i++;
				   isfound = true;
				}
			}
			isfinish = !isfound;
		}
		
	    //Search distance of flightplan
		distanceBetween = 0;
		for (int i = 0; i < legPoints.size()-1; i++) {
			begin = legPoints.get(i).getPosition().split(",");
			//Correct altitude if = 0
			try {
				double alt = Double.parseDouble(begin[2]);
				if (alt == 0.0) {
					legPoints.get(i).setPosition(begin[0]+","+begin[1]+","+altitude);
				}
			} catch (NumberFormatException e) {
			}
			end = legPoints.get(i+1).getPosition().split(",");

			distanceBetween += Geoinfo.distance(Double.parseDouble(begin[1]), Double.parseDouble(begin[0]), Double.parseDouble(end[1]), Double.parseDouble(end[0]), 'N');

		}

		searchFlightPlanNeighbor(selectAirport);
		
		Utility.getInstance().createLandmarkByGroup(selectedLandmarks);
		
		System.out.println("selectedLandmarks "+selectedLandmarks.size());
		

		if (distanceBetween < 1000) {
			legPoints =	Geoinfo.removeInvisiblePointAndInitialiseDist(legPoints);
		}
		
		//dist.isLine = isTocTod
		if (dist.isLine()) {
			int indexToc = Geoinfo.createTOC(flightplan, legPoints);
			int indexTod = Geoinfo.createTOD(flightplan, legPoints);
			if ( indexToc >= indexTod) //Altitude too high and tod is before toc
			{
				legPoints.remove(indexTod);
				legPoints.remove(indexToc);
			}
		}

		try {
			this.departure = selectedAirports.get(legPoints.get(0).getIcaoIdent()).getName();
			this.destination = selectedAirports.get(legPoints.get(legPoints.size()-1).getIcaoIdent()).getName();
		} catch (NullPointerException e) {
		}

	   	createAndsaveFlightPlan();
		
		isDone = true;
		

	}
	

	
	private void searchFlightPlanNeighbor(SelectAirport selectAirport) {
		
		
		if (dist.isAirport()) {
			for(Airport	 airport : selectAirport.getMapAirport().values()){
				Double[] dd1 = Geoinfo.convertDoubleLongLat(airport.getCoordinates());
				current++;
				for(LegPoint point : legPoints){
					Double[] dd2 = Geoinfo.convertDoubleLongLat(point.getPosition());
					
					if (Geoinfo.distance(dd1[1], dd1[0], dd2[1], dd2[0], 'N') < dist.getAirportDist()){
						selectedAirports.put(airport.getIdent(),new Airport(airport));
						if (dist.isLine()) {
							//dataline.setData("airport",dd1[0]+","+ dd1[1]+",0"+"\n\r"+dd2[0]+","+ dd2[1]+",0"+"\n\r");
						}
					}
				}
			}
		} else if (!dist.isAirport()) { //Load the last and first for the flightplan
			try {
				selectedAirports.put(legPoints.get(0).getIcaoIdent(),selectAirport.getMapAirport().get(legPoints.get(0).getIcaoIdent()));
				selectedAirports.put(legPoints.get(legPoints.size()-1).getIcaoIdent(),selectAirport.getMapAirport().get(legPoints.get(legPoints.size()-1).getIcaoIdent()));
				dist.setAirportDist(selectedAirports.size());
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
			//	e.printStackTrace();
			}
		}

		
		// search cities
		if (dist.isCity()) {
			for(City city : cities){
				Double[] dd1 = Geoinfo.convertDoubleLongLat(city.getCoordinates());
				current++;
				
				for(LegPoint point : legPoints){
					Double[] dd2 = Geoinfo.convertDoubleLongLat(point.getPosition());
					
					if (Geoinfo.distance(dd1[1], dd1[0], dd2[1], dd2[0], 'N') < dist.getCityDist()){
						selectedCities.put(city.getCityName(),new City(city));
						if (dist.isLine()) {
							//dataline.setData("city",dd1[0]+","+ dd1[1]+",0"+"\n\r"+dd2[0]+","+ dd2[1]+",0"+"\n\r");
						}
					}
				}
			}
		}
			
		// search mountains
		if (dist.isMountain()) {
			for(Mountain mountain : mountains){
				Double[] dd1 = Geoinfo.convertDoubleLongLat(mountain.getCoordinates());
				current++;
				
				for(LegPoint point : legPoints){
					Double[] dd2 = Geoinfo.convertDoubleLongLat(point.getPosition());
					
					if (Geoinfo.distance(dd1[1], dd1[0], dd2[1], dd2[0], 'N') < dist.getMountainDist()){
						selectedMountains.put(mountain.getName(),new Mountain(mountain));
						if (dist.isLine()) {
							//dataline.setData("mountain",dd1[0]+","+ dd1[1]+",0"+"\n\r"+dd2[0]+","+ dd2[1]+",0"+"\n\r");
						}
						
					}
				}
			}
		}
		// search Vor
		if (dist.isVorNdb()) {
			for(Vor vor : vors){
				Double[] dd1 = Geoinfo.convertDoubleLongLat(vor.getCoordinates());
				current++;
				
				for(LegPoint point : legPoints){
					Double[] dd2 = Geoinfo.convertDoubleLongLat(point.getPosition());
					
					if (Geoinfo.distance(dd1[1], dd1[0], dd2[1], dd2[0], 'N') < dist.getVorNdbDist()){
						selectedVors.put(vor.getVorId(),new Vor(vor));
						if (dist.isLine()) {
							//dataline.setData("vor",dd1[0]+","+ dd1[1]+",0"+"\n\r"+dd2[0]+","+ dd2[1]+",0"+"\n\r");
						}
						
					}
				}
			}
		}
		
		//Search NDB
		if (dist.isVorNdb()) {
			for(Ndb ndb : ndbs){
				Double[] dd1 = Geoinfo.convertDoubleLongLat(ndb.getCoordinates());
				current++;
				
				for(LegPoint point : legPoints){
					Double[] dd2 = Geoinfo.convertDoubleLongLat(point.getPosition());
					
					if (Geoinfo.distance(dd1[1], dd1[0], dd2[1], dd2[0], 'N') < dist.getVorNdbDist()){
						selectedNdbs.put(ndb.getNdbId(),new Ndb(ndb));
						if (dist.isLine()) {
							//dataline.setData("ndb",dd1[0]+","+ dd1[1]+",0"+"\n\r"+dd2[0]+","+ dd2[1]+",0"+"\n\r");
						}
						
					}
				}
			}
		}

		
		if (dist.isLandmark()) {
			
			legPoints =	Geoinfo.removeInvisiblePointAndInitialiseDist(legPoints);
			for (int i = 0; i < legPoints.size()-1; i++) {
				try {
					String provinces =  UtilityMap.getInstance().check2Provinces(legPoints.get(i).getLaty(),legPoints.get(i).getLonx(),
																				legPoints.get(i+1).getLaty(),legPoints.get(i+1).getLonx());
					
					Boundingbox.getInstance().createBox(legPoints.get(i).getLaty(),legPoints.get(i).getLonx(),
														legPoints.get(i+1).getLaty(),legPoints.get(i+1).getLonx(), 
														dist.getLandmarkDist());
					
					UtilityDB.getInstance().selectProvinceLandmark("where admin in ("+provinces+")");
					selectedLandmarks.putAll(UtilityDB.getInstance().getMapLandmark());
					System.out.println(provinces);
				} catch (Exception e) {
				}
				
			}
		}

	}
	
	public  synchronized void createAndsaveFlightPlan(){
		Writer writer = null;
		
		createKML = new CreateKML();
				
 		try {
 			
			kmlFlightPlanFile = Utility.getInstance().getPrefs().getProperty("kmlflightplandir")+"/last_flightplan.kml";
 			if (kmlFlightPlanFile.contains("/data/")){
 	 			Path currentRelativePath = Paths.get("");
	 	   		kmlFlightPlanFile = currentRelativePath.toAbsolutePath().toString()+kmlFlightPlanFile.replace("\\", "/");
 			}

		    writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream(kmlFlightPlanFile), "utf-8"));
		    
		    
		    //Create KML Header
		    writer.write(createKMLHeader());
		    
 			writer.write("<Folder><name> Waypoints ("+legPoints.size()+") </name>");
		    
		    for (LegPoint legPoint : legPoints){

				if ("1".equals(legPoint.getVisible())) {
				//	System.out.println(selectedAirports.get(legPoint.getIcaoIdent()));
					
			    	writer.write(legPoint.buildPoint());
				}
		    	
		    }

		    //"+(altitude < 10000?"clampToGround":"absolute")+"
			   writer.write("<Placemark> <styleUrl>#msn_ylw-pushpin</styleUrl><LineString><extrude>1</extrude><tessellate>1</tessellate><altitudeMode>absolute</altitudeMode><coordinates>"); 

		    for (LegPoint legPoint : legPoints){
				if ("1".equals(legPoint.getVisible())) {
			    	writer.write(legPoint.getPosition()+"\n");
				}

		    }

		    writer.write("</coordinates></LineString></Placemark>");
		    
		    
		    writer.write("</Folder>");
		    
		    if (dist.isAirport()){
			    writer.write("<Folder><name> FS2020 Airports found ("+selectedAirports.size()+") </name>");
			    
			    for(Airport airport:selectedAirports.values()){
			    	try {
						writer.write(CreateKML.buildAirportPlaceMark(airport));
					} catch (NullPointerException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
			    }
		    	
			    writer.write("</Folder>"); 
		    }
		    if (dist.isCity()){
			    writer.write("<Folder><name> Cities found ("+selectedCities.size()+") </name>");
			    
			    
			    for(City city: selectedCities.values()){
			    	writer.write(createKML.buildCityPlaceMark(city));
			    }
		    	
			    writer.write("</Folder>"); 
		    }
		    
		    if (dist.isMountain()){
			    writer.write("<Folder><name> Mountains found ("+selectedMountains.size()+") </name>");
			    
			    
			    for(Mountain mountain: selectedMountains.values()){
			    	writer.write(createKML.buildMountainPlaceMark(mountain));
			    }
		    	
			    writer.write("</Folder>"); 
		    }
		    if (dist.isVorNdb()){
			    writer.write("<Folder><name> VOR found ("+selectedVors.size()+") </name>");
			    
			    
			    for(Vor vor: selectedVors.values()){
			    	writer.write(createKML.buildVorPlaceMark(vor));
			    }
		    	
			    writer.write("</Folder>"); 
		    }
		    if (dist.isVorNdb()){
			    writer.write("<Folder><name> NDB found ("+selectedNdbs.size()+") </name>");
			    
			    
			    for(Ndb ndb: selectedNdbs.values()){
			    	writer.write(createKML.buildNdbPlaceMark(ndb));
			    }
		    	
			    writer.write("</Folder>"); 
		    }
		    
		    

		    
		    if (dist.isLandmark() && UtilityDB.getInstance().getGroupLandmark() != null && UtilityDB.getInstance().getGroupLandmark().size() > 0){
			    writer.write("<Folder><name>Landmark groups found ("+UtilityDB.getInstance().getGroupLandmark().size()+") </name>");

					for (Map.Entry<String, List<Landmark>> entry :UtilityDB.getInstance().getGroupLandmark().entrySet()) {
					    writer.write("<Folder><name>"+ entry.getKey()+" ("+entry.getValue().size()+")</name>");
					    	for (Landmark landmark :entry.getValue()) {
						    	writer.write(createKML.buildLandmarkPlaceMark(landmark,"0"));
	
					    	}
						 writer.write("</Folder>"); 
	
					}

				 writer.write("</Folder>"); 
		    }

	    	
		    
		    
		    if (dist.isLine()){
		    	for (String key: dataline.getMapData().keySet()) {
			    	writer.write("<Folder><name>"+key+" distance </name>"
			    			+ "<Placemark> "
			    			+ "<styleUrl>#msn_ylw-pushpin</styleUrl>"
			    			+ " <Style>" + 
			    			"  <LineStyle> " + 
			    			"   <color>"+dataline.getColor(key)+"</color>"
			    			+ "<width>2</width> " + 
			    			"  </LineStyle>" +
			    			" </Style>"
			    			+ "<LineString><extrude>1</extrude>"
			    			+ "<tessellate>1</tessellate>"
			    			+ "<altitudeMode>"+(altitude < 10000?"clampToGround":"relativeToGround")+"</altitudeMode>"
			    			+ "<coordinates>\r\n");
			    	writer.write(dataline.getMapData().get(key));
			    	writer.write("</coordinates></LineString></Placemark></Folder>");
		    		
		    	}
		    	
		    }
		    
		    
		    writer.write("</Document></kml>");
		
		} catch (IOException ex) {
		  System.err.println(ex.getMessage());
		} finally {
		   try {writer.close();} catch (Exception ex) {}
		}			
    	
    }
  
	public void createPlan(Writer writer) throws IOException{
		writer.write("<Placemark> <styleUrl>#msn_ylw-pushpin</styleUrl><LineString><extrude>1</extrude><tessellate>1</tessellate><altitudeMode>absolute</altitudeMode><coordinates>"); 

	    for (LegPoint legPoint : legPoints){
	    	writer.write(legPoint.getPosition()+"\n");
	    }

	    writer.write("</coordinates></LineString></Placemark>");

	}
    

	public String createKMLHeader() {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<kml xmlns=\"http://www.opengis.net/kml/2.2\" xmlns:gx=\"http://www.google.com/kml/ext/2.2\" xmlns:kml=\"http://www.opengis.net/kml/2.2\" xmlns:atom=\"http://www.w3.org/2005/Atom\">"	
				+ "<Document>"
				+ "<name>"+new File(flightPlanFile).getName()+"</name>"
				+ "<open>1</open><Style id=\"s_ylw-pushpin\"><IconStyle><color>b200ffff</color><Icon><href>http://maps.google.com/mapfiles/kml/paddle/ylw-stars.png</href></Icon><hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/></IconStyle><LabelStyle><color>7fffff55</color></LabelStyle><ListStyle><ItemIcon><href>http://maps.google.com/mapfiles/kml/paddle/ylw-stars-lv.png</href></ItemIcon></ListStyle></Style><StyleMap id=\"m_grn-pushpin\"><Pair><key>normal</key><styleUrl>#s_ylw-pushpin</styleUrl></Pair><Pair><key>highlight</key><styleUrl>#s_ylw-pushpin_hl</styleUrl></Pair></StyleMap><StyleMap id=\"msn_ylw-pushpin\"><Pair><key>normal</key><styleUrl>#sn_ylw-pushpin</styleUrl></Pair><Pair><key>highlight</key><styleUrl>#sh_ylw-pushpin</styleUrl></Pair></StyleMap>"
				+ "<Style id=\"sh_ylw-pushpin\"><IconStyle><scale>1.2</scale></IconStyle><LineStyle><color>7f0000ff</color><width>3</width></LineStyle><PolyStyle><color>7f00ff55</color></PolyStyle></Style>"
				+ "<Style id=\"s_ylw-pushpin_hl\"><IconStyle><color>7fffffff</color><scale>1.18182</scale><Icon><href>http://maps.google.com/mapfiles/kml/paddle/ylw-stars.png</href></Icon><hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/></IconStyle><LabelStyle><color>7fffff55</color></LabelStyle><ListStyle><ItemIcon><href>http://maps.google.com/mapfiles/kml/paddle/ylw-stars-lv.png</href></ItemIcon></ListStyle></Style>"
				+ "<Style id=\"sn_ylw-pushpin\"><LineStyle><color>7f0000ff</color><width>4</width></LineStyle><PolyStyle><color>7f00ff55</color></PolyStyle></Style>"
				+ "<Style id=\"fsx_airport\"><IconStyle><Icon><href>http://maps.google.com/mapfiles/kml/shapes/airports.png</href></Icon></IconStyle></Style>"
				+ "<Style id=\"addon_airport\"><IconStyle><Icon><href>http://maps.google.com/mapfiles/kml/shapes/airports.png</href></Icon></IconStyle></Style>"
				;	
		}	

	

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) throws FileNotFoundException, NullPointerException, NoPoints, IOException {
	}
	
	public LinkedList<LegPoint> getLegPoints() {
		return legPoints;
	}

	public void setLegPoints(LinkedList<LegPoint> points) {
		this.legPoints = points;
	}


		public class NoPoints extends Exception {
	    public NoPoints(String message) {
	        super(message);
	    }
	}

	public List<String> getAddonList() {
		return addonList;
	}


	public void setAddonList(List<String> addonList) {
		this.addonList = addonList;
	}


	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}




	public String getKmlFlightPlanFile() {
		return kmlFlightPlanFile;
	}


	public void setKmlFlightPlanFile(String kmlFlightPlanFile) {
		this.kmlFlightPlanFile = kmlFlightPlanFile;
	}


	public double getDistanceBetween() {
		return distanceBetween;
	}


	public double getAltitude() {
		return altitude;
	}


	public String getDeparture() {
		return departure;
	}


	public void setDeparture(String departure) {
		this.departure = departure;
	}


	public String getDestination() {
		return destination;
	}


	public void setDestination(String destination) {
		this.destination = destination;
	}


	public String getFlightPlanFile() {
		return flightPlanFile;
	}


/*	public static void setFlightPlanFile(String flightPlanFile) {
		CreateKmlFSPlan.flightPlanFile = flightPlanFile;
	}
*/

	public String getFlightPlanTitle() {
		return flightPlanTitle;
	}


	public void setFlightPlanTitle(String flightPlanTitle) {
		this.flightPlanTitle = flightPlanTitle;
	}


	public Flightplan getFlightplan() {
		return flightplan;
	}


	public void setFlightplan(Flightplan flightplan) {
		this.flightplan = flightplan;
	}


	public Map<String, Airport> getSelectedAirports() {
		return selectedAirports;
	}


	public void setSelectedAirports(Map<String, Airport> selectedAirports) {
		this.selectedAirports = selectedAirports;
	}


	public Map<String, City> getSelectedCities() {
		return selectedCities;
	}


	public void setSelectedCities(Map<String, City> selectedCities) {
		this.selectedCities = selectedCities;
	}


	public Map<String, Mountain> getSelectedMountains() {
		return selectedMountains;
	}


	public void setSelectedMountains(Map<String, Mountain> selectedMountains) {
		this.selectedMountains = selectedMountains;
	}


	public Map<Integer, Vor> getSelectedVors() {
		return selectedVors;
	}


	public void setSelectedVors(Map<Integer, Vor> selectedVors) {
		this.selectedVors = selectedVors;
	}


	public Map<Integer, Ndb> getSelectedNdbs() {
		return selectedNdbs;
	}


	public void setSelectedNdbs(Map<Integer, Ndb> selectedNdbs) {
		this.selectedNdbs = selectedNdbs;
	}


	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}


	public Map<String, Landmark> getSelectedLandmarks() {
		return selectedLandmarks;
	}


	public void setSelectedLandmarks(Map<String, Landmark> selectedLandmarks) {
		this.selectedLandmarks = selectedLandmarks;
	}


	
}



