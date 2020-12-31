package net;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.cfg.common.Dataline;
import com.cfg.file.ManageConfigFile;
import com.cfg.file.ManageXMLFile;
import com.cfg.model.LegPoint;
import com.cfg.model.Placemark;
import com.cfg.plan.ReadFs9Plan;
import com.cfg.plan.ReadPlanGPlan;
import com.geo.util.Geoinfo;
import com.model.City;
import com.model.Distance;
import com.model.Flightplan;
import com.model.Mountain;
import com.model.Ndb;
import com.model.Vor;
import com.util.CreateKML;
import com.util.ReadFsxPlan;
import com.util.Utility;

public class CreateKmlFSPlan{
	
	
	private String flightPlanFile;// = "C:\\Users\\Pierre\\AppData\\Local\\Packages\\Microsoft.FlightSimulator_8wekyb3d8bbwe\\LocalState\\PHNGPHJR.pln";

	private Map<String, Placemark> selectedAirports ;
	private Map<String, City> selectedCities ;
	private Map<String, Mountain> selectedMountains ;
	private Map<String, Placemark> addonPlacemarks ;
	private Map<Integer, Vor> selectedVors;
	private Map<Integer, Ndb> selectedNdbs;
	private List <String> addonList;
	
	private String kmlFlightPlanFile = "/data/last_flightplan.kml";

	private ManageXMLFile manageXMLFile;

	private ManageConfigFile configFile;
	
	private LinkedList<LegPoint> legPoints;
	
	
	private LegPoint atcWaypoint;
	
	//private double distanceRequired;
	
	private Distance dist;
	
	private ReadFs9Plan fs9Plan;
	
	private ReadFsxPlan fsxPlan;
	
	private ReadPlanGPlan planGPlan;
	

	private boolean isDone;
	
	private List<City> cities;
	private List<Mountain> mountains;
	private List<Vor> vors;
	private List<Ndb> ndbs;
	
	private Dataline dataline;

	private CreateKML createKML;
	
	private int current;
	
	private int nbAirport;
	private int nbCity;
	private int nbMountain;
	private int nbVor;
	private int nbNdb;
	private double distanceBetween = 0;
	private double altitude = 0;
	private String departure;
	private String destination;
	private Flightplan flightplan;
	private String flightPlanTitle;

	
	
	public CreateKmlFSPlan(String flightPlanFile,  Distance dist, 
			ManageXMLFile xmlfile,
			List<City> cities, 
			List<Mountain> mountains,
			List<Vor> vors,
			List<Ndb> ndbs) throws FileNotFoundException,NoPoints, NullPointerException, IOException{
		this.flightPlanFile = flightPlanFile;
		this.dist = dist;
		this.manageXMLFile = xmlfile;
		this.selectedAirports = new HashMap<>();
		this.selectedCities = new HashMap<>();
		this.selectedMountains = new HashMap<>();
		this.selectedNdbs = new HashMap<>();
		this.selectedVors = new HashMap<>();
		this.addonPlacemarks = new HashMap<>();
		this.isDone = false;
		this.cities = cities;
		this.mountains = mountains;
		this.vors = vors;
		this.ndbs = ndbs;
		
		this.dataline = new Dataline();
		
		current = 0;
	
		fsxPlan = new ReadFsxPlan(flightPlanFile);
		
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
		
		
		
	}
	
	
	public void makeFlightPlan(Flightplan flightplan) throws FileNotFoundException, NullPointerException, IOException, NoPoints{
		
		if (legPoints.size() == 0){
			throw  new NoPoints("Your Flight Plan don't return any Waypoints...");
		} 
		
		List <Placemark> airports = new ArrayList<>();

		SelectAiport selectAiport = new SelectAiport(); 
		selectAiport.selectAll("");
		manageXMLFile.setPlacemarks(selectAiport.getPlacemarks());
		manageXMLFile.setHashPlacemark(selectAiport.getMapPlacemark());
		
		flightplan.validIcao(selectAiport.getMapPlacemark(), legPoints.get(0), true, dist);
		flightplan.validIcao(selectAiport.getMapPlacemark(), legPoints.get(legPoints.size()-1), false, dist);
		
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
		
		
		// search airports
		searchNeighbor();
		
		if (distanceBetween < 1000) {
			legPoints =	Geoinfo.removeInvisiblePointAndInitialiseDist(legPoints);
		}
		
		//dist.isLine = isTocTod
		if (dist.isLine()) {
			int indexToc = Geoinfo.createTOC(flightplan, legPoints);
			int indexTod = Geoinfo.createTOD(flightplan, legPoints);
			if ( indexToc >= indexTod) //Altitude too high
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

		nbAirport = selectedAirports.size();
		nbCity = selectedCities.size();
		nbMountain = selectedMountains.size();
		nbVor = selectedVors.size();
		nbNdb = selectedNdbs.size();
		
	   	createAndsaveFlightPlan();
		
		isDone = true;
		

	}
	

	
	private void searchNeighbor() {
		
		
		if (dist.isAirport()) {
			for(Placemark placemark : manageXMLFile.getPlacemarks()){
				Double[] dd1 = Geoinfo.convertDoubleLongLat(placemark.getCoordinates());
				current++;
				for(LegPoint point : legPoints){
					Double[] dd2 = Geoinfo.convertDoubleLongLat(point.getPosition());
					
					if (Geoinfo.distance(dd1[1], dd1[0], dd2[1], dd2[0], 'N') < dist.getAirportDist()){
						selectedAirports.put(placemark.getName(),new Placemark(placemark));
						if (dist.isLine()) {
							//dataline.setData("airport",dd1[0]+","+ dd1[1]+",0"+"\n\r"+dd2[0]+","+ dd2[1]+",0"+"\n\r");
						}
					}
				}
			}
		} else if (!dist.isAirport()) { //Load the last and first for the flightplan
			try {
				selectedAirports.put(legPoints.get(0).getIcaoIdent(),manageXMLFile.getHashPlacemark().get(legPoints.get(0).getIcaoIdent()));
				selectedAirports.put(legPoints.get(legPoints.size()-1).getIcaoIdent(),manageXMLFile.getHashPlacemark().get(legPoints.get(legPoints.size()-1).getIcaoIdent()));
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
					
				//	System.out.println(Geoinfo.distance(dd1[1], dd1[0], dd2[1], dd2[0], 'N'));

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

				if ("1".equals(legPoint.getVisible()) || distanceBetween > 1000) {
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
			    
			    for(Placemark placemark:selectedAirports.values()){
			    	try {
						writer.write(placemark.buildXML("fsx_airport"));
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
		ManageXMLFile manageXMLFile = new ManageXMLFile("");
		List<Placemark> placemarks = new ArrayList<>();

		SelectAiport selectAiport = new SelectAiport();
		selectAiport.selectAll("");
		
		manageXMLFile.setPlacemarks(selectAiport.getPlacemarks());
		
		SelectCity selectCity = new SelectCity();
		selectCity.selectAll("");
		
		SelectMountain selectMountain = new SelectMountain();
		selectMountain.selectAll("");
		
		Distance dist = new Distance(10, 100, 20, true);
		

	/*	new CreateKmlFSPlan(flightPlan, dist, 
				manageXMLFile, 
				selectCity.getCities(), 
				selectMountain.getMountains());
*/		
	}
	
	public LinkedList<LegPoint> getLegPoints() {
		return legPoints;
	}

	public void setLegPoints(LinkedList<LegPoint> points) {
		this.legPoints = points;
	}


	public Map<String, Placemark> getAddonPlacemarks() {
		return addonPlacemarks;
	}


	public void setAddonPlacemarks(Map<String, Placemark> addonPlacemarks) {
		this.addonPlacemarks = addonPlacemarks;
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


	public int getNbAirport() {
		return nbAirport;
	}


	public void setNbAirport(int nbAirport) {
		this.nbAirport = nbAirport;
	}


	public int getNbCity() {
		return nbCity;
	}


	public void setNbCity(int nbCity) {
		this.nbCity = nbCity;
	}


	public int getNbMountain() {
		return nbMountain;
	}


	public void setNbMountain(int nbMountain) {
		this.nbMountain = nbMountain;
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


	public int getNbVor() {
		return nbVor;
	}


	public void setNbVor(int nbVor) {
		this.nbVor = nbVor;
	}


	public int getNbNdb() {
		return nbNdb;
	}


	public void setNbNdb(int nbNdb) {
		this.nbNdb = nbNdb;
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


	public Map<String, Placemark> getSelectedAirports() {
		return selectedAirports;
	}


	public void setSelectedAirports(Map<String, Placemark> selectedAirports) {
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


	
}



