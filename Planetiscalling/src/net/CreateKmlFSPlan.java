package net;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
import com.cfg.plan.ReadFsxPlan;
import com.cfg.plan.ReadPlanGPlan;
import com.cfg.util.Util;
import com.geo.util.Geoinfo;
import com.model.City;
import com.model.Distance;
import com.model.Mountain;
import com.model.Ndb;
import com.model.Vor;
import com.util.CreateKML;
import com.util.Utility;

public class CreateKmlFSPlan{
	
	
	private static String flightPlan = "C:\\Users\\Pierre\\AppData\\Local\\Packages\\Microsoft.FlightSimulator_8wekyb3d8bbwe\\LocalState\\PHNGPHJR.pln";

	private Map<String, Placemark> selectedAirports ;
	private Map<String, City> selectedCities ;
	private Map<String, Mountain> selectedMountains ;
	private Map<String, Placemark> addonPlacemarks ;
	private Map<Integer, Vor> selectedVors;
	private Map<Integer, Ndb> selectedNdbs;
	private List <String> addonList;
	
	private int totalFsxPlacemarks;
	private int totalPlacemarks;
	private int totalAddonPlacemarks;

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

	
	
	public CreateKmlFSPlan(String flightPlan,  Distance dist, 
			ManageXMLFile xmlfile,
			List<City> cities, 
			List<Mountain> mountains,
			List<Vor> vors,
			List<Ndb> ndbs) throws FileNotFoundException,NoPoints, NullPointerException, IOException{
		this.flightPlan = flightPlan;
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
		
		
		String text = new String(Files.readAllBytes(Paths.get(flightPlan)), StandardCharsets.UTF_8);
		
		
			fsxPlan = new ReadFsxPlan(flightPlan);
			legPoints = fsxPlan.getLegPoints();
		
			if (dist.getAltitude() != 0 && dist.getAltitude() != Double.parseDouble(fsxPlan.getCruisingAlt())) {
				fsxPlan.modifyAltitude(legPoints, Double.parseDouble(fsxPlan.getCruisingAlt()), dist.getAltitude());
				fsxPlan.setCruisingAlt(dist.getAltitude()+"");

			} else {
				System.out.println();
			}
			

		
		if (legPoints.size() > 0){
			makeFlightPlan();
		} else {
			//System.out.println("points = "+points.size());
			throw  new NoPoints("Your Flight Plan don't return any Waypoints...");
		}
		
		
		
	}
	
	
	public void makeFlightPlan() throws FileNotFoundException, NullPointerException, IOException, NoPoints{
		
		if (legPoints.size() == 0){
			throw  new NoPoints("Your Flight Plan don't return any Waypoints...");
		} 
		
		
//		System.out.println(manageXMLFile.getPlacemarks().size());
//		System.out.println(legPoints.size());
		
		long start = System.currentTimeMillis();
		
		int cptNew = 0;
		
		boolean isfound = false;
		boolean isfinish = false;
		String[] begin;
		String[] end;
			
		
		altitude = Double.parseDouble(fsxPlan.getCruisingAlt())/3.28084;
	    while (!isfinish) {
			for (int i = 0; i < legPoints.size()-1; i++) {
				isfound = false;
			
			//	System.out.println(legPoints.get(i));
				begin = legPoints.get(i).getPosition().split(",");
				end = legPoints.get(i+1).getPosition().split(",");
				
				distanceBetween = Geoinfo.distance(Double.parseDouble(begin[1]), Double.parseDouble(begin[0]), Double.parseDouble(end[1]), Double.parseDouble(end[0]), 'N');
				if (distanceBetween > 15) {
					
					 String midpoint = Geoinfo.midpoint(Double.parseDouble(begin[1]), Double.parseDouble(begin[0]), Double.parseDouble(end[1]), Double.parseDouble(end[0]));
				     LegPoint legPoint = new LegPoint("MID"+(cptNew++),"VOR",midpoint+","+legPoints.get(i+1).getPosition().split(",")[2],"0"); 
					 
				   legPoints.add(i+1,legPoint);
					 
				   i++;
				   isfound = true;
				}
			}
			isfinish = !isfound;
		}
		
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
		//	altitude = (altitude < Double.parseDouble(begin[2])?Double.parseDouble(begin[2]):altitude);

		}
		

		// search airports
		searchNeighbor();

		System.out.println(legPoints.size());

		legPoints =	Geoinfo.removeInvisiblePointAndInitialiseDist(legPoints);
		System.out.println(legPoints.size());

		if (dist.isLine()) {
			Geoinfo.createTOC(Double.parseDouble(fsxPlan.getCruisingAlt())-legPoints.get(0).getAltitude(), 
					legPoints);
			Geoinfo.createTOD(Double.parseDouble(fsxPlan.getCruisingAlt())-legPoints.get(legPoints.size()-1).getAltitude(), 
					legPoints);
		}

		totalPlacemarks = manageXMLFile.getPlacemarks().size();
		
		totalFsxPlacemarks = selectedAirports.size();
		
		System.out.println("Seconds = "+(System.currentTimeMillis()-start)/1000);
		System.out.println("Total waypoints = "+legPoints.size() );
		System.out.println("Total Airports = "+selectedAirports.size());
		System.out.println("Total selectedCities = "+selectedCities.size());
		System.out.println("Total selectedMountains = "+selectedMountains.size());
		System.out.println("Total selectedVors = "+selectedVors.size());

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
			selectedAirports.put(legPoints.get(0).getId(),manageXMLFile.getHashPlacemark().get(legPoints.get(0).getId()));
			selectedAirports.put(legPoints.get(legPoints.size()-1).getId(),manageXMLFile.getHashPlacemark().get(legPoints.get(legPoints.size()-1).getId()));
			dist.setAirportDist(selectedAirports.size());
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
	
	public boolean done(){
		return isDone;
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
		    
 			writer.write("<Folder><name> Waypoints </name>");
		    
		    for (LegPoint legPoint : legPoints){
				if ("1".equals(legPoint.getVisible())) {
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
			    	writer.write(placemark.buildXML("fsx_airport"));
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
		    
		    
/*		    if (dist.isLine()){
		    	writer.write("<Folder><name> Distances </name>"
		    			+ "<Placemark> "
		    			+ "<styleUrl>#msn_ylw-pushpin</styleUrl>"
		    			+ " <Style>" + 
		    			"  <LineStyle> " + 
		    			"   <color>"+dataline.getColor("mountain")+"</color>"
		    			+ "<width>2</width> " + 
		    			"  </LineStyle>" + 
		    			" </Style>"
		    			+ "<LineString><extrude>1</extrude>"
		    			+ "<tessellate>1</tessellate>"
		    			+ "<altitudeMode>relativeToGround</altitudeMode>"
		    			+ "<coordinates>\r\n");
		    	writer.write(dataline.getData("mountain"));
		    	writer.write("</coordinates></LineString></Placemark></Folder>");
		    }

*/		    
		    
		    //Create KML Footer
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
				+ "<name>"+new File(flightPlan).getName()+"</name>"
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
		selectAiport.selectAll("", placemarks);
		
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

	public int getTotalFsxPlacemarks() {
		return totalFsxPlacemarks;
	}

	public void setTotalFsxPlacemarks(int totalFsxPlacemarks) {
		this.totalFsxPlacemarks = totalFsxPlacemarks;
	}

	public int getTotalAddonPlacemarks() {
		return totalAddonPlacemarks;
	}

	public void setTotalAddonPlacemarks(int totalAddonPlacemarks) {
		this.totalAddonPlacemarks = totalAddonPlacemarks;
	}

	public int getTotalPlacemarks() {
		return totalPlacemarks;
	}

	public void setTotalPlacemarks(int totalPlacemarks) {
		this.totalPlacemarks = totalPlacemarks;
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


	
}



