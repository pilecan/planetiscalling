package com.util;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.cfg.common.Dataline;
import com.cfg.common.Info;
import com.cfg.file.ManageXMLFile;
import com.cfg.model.LegPoint;
import com.cfg.model.Placemark;
import com.geo.util.Geoinfo;
import com.model.City;
import com.model.Distance;
import com.model.Mountain;
import com.model.Ndb;
import com.model.Result;
import com.model.Vor;

import net.CreateKmlFSPlan;
import net.CreateKmlFSPlan.NoPoints;
import net.SelectAiport;
import net.SelectCity;
import net.SelectMountain;
import net.SelectNdb;
import net.SelectVor;

public class ReadData implements Info{
	private ManageXMLFile manageXMLFile;
	private SelectAiport selectAiport;
	private SelectCity selectCity;
	private SelectMountain selectMountain;
	private SelectVor selectVor;
	private SelectNdb selectNdb;
	private String kmlFlightPlanFile;
	private Map<String, City> selectedCities ;
	private Map<String, Mountain> selectedMountains ;
	private Map<String, Placemark> selectedAirports ;
	private Map<Integer, Vor> selectedVors;
	private Map<Integer, Ndb> selectedNdbs;

	
	private CreateKML createKML;
	
	private String flightplan ;
	
	private String flightplanName;
	
	private Result result;
	
	private Dataline dataline;
	private Distance dist;

	private String icaos;
	private boolean isCity = true;
	private boolean isMountain = true;
	private boolean isDistance = true;
	
	public ReadData(Result result, ManageXMLFile manageXMLFile, SelectCity selectCity, SelectMountain selectMountain, SelectVor selectVor, SelectNdb selectNdb, Distance dist){
		this.manageXMLFile = manageXMLFile;
		this.selectCity = selectCity;
		this.selectMountain = selectMountain;
		this.selectVor =selectVor;
		this.selectNdb	= selectNdb;
		
		this.result = result;
		this.dataline = new Dataline();
		selectFlightplan(dist);
		
	}
	
	public ReadData(String icaos, Result result, ManageXMLFile manageXMLFile, SelectVor selectVor, SelectNdb selectNdb, SelectMountain selectMountain, SelectCity selectCity, Distance dist){
		this.manageXMLFile = manageXMLFile;
		this.selectVor =selectVor;
		this.selectNdb	= selectNdb;
		this.selectCity = selectCity;
		this.selectMountain = selectMountain;
		this.result = result;
		this.dataline = new Dataline();
		this.dist = dist;
		this.icaos = icaos;

		creatIcaoAirports(dist);

	}

	public ReadData(){
		

	}

	
	
	private void selectFlightplan( Distance dist) {
		
		Utility.getInstance().readPrefProperties();
		
		JFileChooser chooser = selectDirectoryProgram("Flightplan selection", Utility.getInstance().getPrefs().getProperty("flightplandir"));
	       // setAlwaysOnTop(false);
		
		chooser.setPreferredSize(new Dimension(550,400));

			Action details = chooser.getActionMap().get("viewTypeDetails");
			details.actionPerformed(null);
			
			JTable table = SwingUtils.getDescendantsOfType(JTable.class, chooser).get(0);
			table.getRowSorter().toggleSortOrder(3);		
			
			if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				//System.out.println(chooser.getSelectedFile());
				flightplan = chooser.getSelectedFile().toString();
				createFlightplan(dist);
		    } else {
		    	kmlFlightPlanFile = "";
		    }
			
		
	}
	
	public void createFlightplan( Distance dist) {
		
		try {

			CreateKmlFSPlan createKmlFSPlan =  new CreateKmlFSPlan(flightplan, dist, 
					manageXMLFile,
					selectCity.getCities(), 
					selectMountain.getMountains(),
					selectVor.getVors(),
					selectNdb.getNdbs());
			
			kmlFlightPlanFile = createKmlFSPlan.getKmlFlightPlanFile();

			result.setFlightplan(new File(flightplan).getName()); 
			result.setDistance(Math.round(createKmlFSPlan.getDistanceBetween())); 
			result.setAltitude(Math.round(createKmlFSPlan.getAltitude()*3.28084)); 
			result.setVors(createKmlFSPlan.getNbVor()); 
			result.setNdbs(createKmlFSPlan.getNbNdb()); 
			result.setAirports(createKmlFSPlan.getNbAirport()); 
			result.setCities(createKmlFSPlan.getNbCity()); 
			result.setMountains(createKmlFSPlan.getNbMountain());
			result.setDeparture(createKmlFSPlan.getDeparture());
			result.setDestination(createKmlFSPlan.getDestination());
			
			
		} catch (NullPointerException | NoPoints | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	        
			
	}
	
    private JFileChooser selectDirectoryProgram(String title,String directory){
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File(directory));
		chooser.setDialogTitle(title);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		String[] EXTENSION=new String[]{"plg","pln"};
		 FileNameExtensionFilter filter=new FileNameExtensionFilter("FS2020/FSX/FS9 (.pln), Plan-G (.plg)",EXTENSION);
		 chooser.setFileFilter(filter);
		 chooser.setMultiSelectionEnabled(false);
	
		return chooser;
    	
    }
    /**
     * 
     */
    private void creatIcaoAirports(Distance dist) {
    	
    	
    	List<Placemark> placemarks = new ArrayList<>();
		manageXMLFile = new ManageXMLFile("");
		createKML = new CreateKML();
		selectedMountains = new HashMap<>();
		selectedCities = new HashMap<>();
		selectedNdbs = new HashMap<>();
		selectedVors = new HashMap<>();
		
		String search = Utility.getInstance().valideIcao(icaos);
		
	    if (!"".equals(search)) {
	     	search = search.toUpperCase();		
			
			search = search.replaceAll("\\s+", "','");
			String sql = "where ident in ('"+ search + "') ";

	    	SelectAiport selectAiport = new SelectAiport();
	 	
			selectAiport.selectAll(sql, placemarks);
			placemarks = selectAiport.getPlacemarks();
			
			result.setListAirport(placemarks);

			searchAiportNeighbor(placemarks,selectCity,selectMountain);
			searchIcaoVorNdb(placemarks, selectVor, selectNdb);
				
			
			result.setAirports(placemarks.size());
			result.setVors(selectedVors.size()); 
			result.setNdbs(selectedNdbs.size()); 
			result.setCities(selectedCities.size()); 
			result.setMountains(selectedMountains.size());

			
			search = search.replaceAll("\\s+", "");
	    	
	    	flightplanName = Utility.getInstance().getFlightPlanName("icao_airports.kml");
			
			if (placemarks.size() > 0) {
			  	//searchAiportNeighbor(placemarks,selectCity,selectMoutain);
				saveKMLFileICAO(manageXMLFile, placemarks,flightplanName);
				manageXMLFile.launchGoogleEarth(new File(flightplanName));
			}

			
	    }
		
    }
    
    private void searchIcaoVorNdb(List<Placemark> placemarks,SelectVor selectVor,SelectNdb selectNdb) {
		if (dist.isVorNdb()) {
			for(Vor vor : selectVor.getVors()){
				Double[] dd1 = Geoinfo.convertDoubleLongLat(vor.getCoordinates());
				
 				for(Placemark airport : placemarks){
					Double[] dd2 = Geoinfo.convertDoubleLongLat(airport.getCoordinates());
					
					if (Geoinfo.distance(dd1[1], dd1[0], dd2[1], dd2[0], 'N') < dist.getVorNdbDist()){
						selectedVors.put(vor.getVorId(),new Vor(vor));
						if (dist.isLine()) {
							dataline.setData("vor",dd1[0]+","+ dd1[1]+",0"+"\n\r"+dd2[0]+","+ dd2[1]+",0"+"\n\r");
						}
						
					}
				}
			}
		}
		
		//Search NDB
		if (dist.isVorNdb()) {
			for(Ndb ndb : selectNdb.getNdbs()){
				Double[] dd1 = Geoinfo.convertDoubleLongLat(ndb.getCoordinates());
				
 				for(Placemark airport : placemarks){
					Double[] dd2 = Geoinfo.convertDoubleLongLat(airport.getCoordinates());
					
					if (Geoinfo.distance(dd1[1], dd1[0], dd2[1], dd2[0], 'N') < dist.getVorNdbDist()){
						selectedNdbs.put(ndb.getNdbId(),new Ndb(ndb));
						if (dist.isLine()) {
							dataline.setData("ndb",dd1[0]+","+ dd1[1]+",0"+"\n\r"+dd2[0]+","+ dd2[1]+",0"+"\n\r");
						}
						
					}
				}
			}
		}
		
   	
    }

    
    private void searchAiportNeighbor(List<Placemark> airports,SelectCity selectCity,SelectMountain selectMoutain) {
 		if (dist.isCity()) {
 			selectedCities = new HashMap<String, City>();
 			for(City city : selectCity.getCities()){
 				Double[] dd1 = Geoinfo.convertDoubleLongLat(city.getCoordinates());
 				
 				for(Placemark airport : airports){
 					Double[] dd2 = Geoinfo.convertDoubleLongLat(airport.getCoordinates());
 					
 					if (Geoinfo.distance(dd1[1], dd1[0], dd2[1], dd2[0], 'N') < dist.getCityDist()){
 						selectedCities.put(city.getCityName(),new City(city));
						if (dist.isLine()) {
							dataline.setData("city",dd1[0]+","+ dd1[1]+",0"+"\n\r"+dd2[0]+","+ dd2[1]+",0"+"\n\r");
						}
					}
 				}
 			}
 		}
		if (dist.isMountain()) {
			selectedMountains = new HashMap<>();
			for(Mountain mountain : selectMoutain.getMountains()){
				Double[] dd1 = Geoinfo.convertDoubleLongLat(mountain.getCoordinates());

 				for(Placemark airport : airports){
					Double[] dd2 = Geoinfo.convertDoubleLongLat(airport.getCoordinates());

					if (Geoinfo.distance(dd1[1], dd1[0], dd2[1], dd2[0], 'N') < dist.getMountainDist()) {
						selectedMountains.put(mountain.getName(), new Mountain(mountain));
						if (dist.isLine()) {
							dataline.setData("mountain",dd1[0]+","+ dd1[1]+",0"+"\n\r"+dd2[0]+","+ dd2[1]+",0"+"\n\r");
						}
					}
				}
			}
		}

 		
     }	

    
	private void searchCityNeighbor(List<Placemark> airports, SelectCity selectCity, SelectMountain selectMoutain) {
		if (dist.isAirport()) {
			selectedAirports = new HashMap<String, Placemark>();
			for (Placemark airport : airports) {
				Double[] dd1 = Geoinfo.convertDoubleLongLat(airport.getCoordinates());

				for (City city : selectCity.getCities()) {
					Double[] dd2 = Geoinfo.convertDoubleLongLat(city.getCoordinates());

					if (Geoinfo.distance(dd1[1], dd1[0], dd2[1], dd2[0], 'N') < dist.getAirportDist()) {
						selectedAirports.put(airport.getName(), new Placemark(airport));
						if (dist.isLine()) {
							dataline.setData("airport",dd1[0]+","+ dd1[1]+",0"+"\n\r"+dd2[0]+","+ dd2[1]+",0"+"\n\r");
						}
					}
				}
			}
		}
		if (dist.isMountain()) {
			selectedMountains = new HashMap<>();
			for(Mountain mountain : selectMoutain.getMountains()){
				Double[] dd1 = Geoinfo.convertDoubleLongLat(mountain.getCoordinates());

				for (City city : selectCity.getCities()) {
					Double[] dd2 = Geoinfo.convertDoubleLongLat(city.getCoordinates());

					if (Geoinfo.distance(dd1[1], dd1[0], dd2[1], dd2[0], 'N') < dist.getMountainDist()) {
						selectedMountains.put(mountain.getName(), new Mountain(mountain));
						if (dist.isLine()) {
							dataline.setData("mountain",dd1[0]+","+ dd1[1]+",0"+"\n\r"+dd2[0]+","+ dd2[1]+",0"+"\n\r");
						}
					}
				}
			}
		}

	}
	
	private void searchMountainNeighbor(List<Placemark> airports, SelectCity selectCity, SelectMountain selectMoutain) {
		
		if (dist.isAirport()) {
			selectedAirports = new HashMap<String, Placemark>();
			for (Placemark airport : airports) {
				Double[] dd1 = Geoinfo.convertDoubleLongLat(airport.getCoordinates());

				for (Mountain mountain : selectMoutain.getMountains()) {
					Double[] dd2 = Geoinfo.convertDoubleLongLat(mountain.getCoordinates());

					if (Geoinfo.distance(dd1[1], dd1[0], dd2[1], dd2[0], 'N') < dist.getAirportDist()) {
						selectedAirports.put(airport.getName(), new Placemark(airport));
						if (dist.isLine()) {
							dataline.setData("airport",dd1[0]+","+ dd1[1]+",0"+"\n\r"+dd2[0]+","+ dd2[1]+",0"+"\n\r");
						}
					}
				}
			}

		}
		if (dist.isCity()) {
			selectedCities = new HashMap<>();
			for(City city: selectCity.getCities()){
				Double[] dd1 = Geoinfo.convertDoubleLongLat(city.getCoordinates());

				for (Mountain mountain : selectMoutain.getMountains()) {
					Double[] dd2 = Geoinfo.convertDoubleLongLat(mountain.getCoordinates());

					if (Geoinfo.distance(dd1[1], dd1[0], dd2[1], dd2[0], 'N') < dist.getCityDist()) {
						selectedCities.put(city.getCityName(), new City(city));
						
						if (dist.isLine()) {
							dataline.setData("city",dd1[0]+","+ dd1[1]+",0"+"\n\r"+dd2[0]+","+ dd2[1]+",0"+"\n\r");
						}
					}
				}
			}
		}

	}
	
	
    private void searchAiport(List<Placemark> airports,SelectCity selectCity,SelectMountain selectMoutain) {
				if (isCity) {
					selectedCities = new HashMap<String, City>();
					for(City city : selectCity.getCities()){
						Double[] dd1 = Geoinfo.convertDoubleLongLat(city.getCoordinates());
						
						for(Placemark airport : airports){
							Double[] dd2 = Geoinfo.convertDoubleLongLat(airport.getCoordinates());
							
							if (Geoinfo.distance(dd1[1], dd1[0], dd2[1], dd2[0], 'N') < 20){
								selectedCities.put(city.getCityName(),new City(city));
							}
						}
					}
				}
				
			
		// search mountains
		if (isMountain) {
			selectedMountains = new HashMap<>();
			//System.out.println(selectMountain.getMountains().size());
			
			for(Mountain mountain : selectMoutain.getMountains()){
				Double[] dd1 = Geoinfo.convertDoubleLongLat(mountain.getCoordinates());
			
				for(Placemark airport : airports){
					Double[] dd2 = Geoinfo.convertDoubleLongLat(airport.getCoordinates());
					
					if (Geoinfo.distance(dd1[1], dd1[0], dd2[1], dd2[0], 'N') < 40){
						selectedMountains.put(mountain.getName(),new Mountain(mountain));
					}
				}
			}
		}
		
    	
    }
    
	public void createKMLMountain(ManageXMLFile manageXMLFile,Map<String, City> mapCities, JComboBox comboCountry, JComboBox comboMountain,Distance dist) {
		String sql = "";	
    	SelectCity selectCity = new SelectCity();
    	SelectMountain selectMountain = new SelectMountain();
		this.dataline = new Dataline();

		this.dist = dist;
    	
    	String sqlCountry = "";
    	String str = ((String) comboCountry.getSelectedItem());
    	if (str.contains("- ")) {
    		String[] array = str.split("- ");
    		array[0] = array[0].replace("'", "''");
    		array[1] = array[1].replace("'", "''");
    		sqlCountry = "where country in ('"+array[0]+"','"+array[1]+"') ";
    		
    	} else {
    		sqlCountry = "where country = '"+((String) comboCountry.getSelectedItem()).replace("'", "''") +"'";
    	}
		
		if (" All".equals(comboMountain.getSelectedItem())) {
	    	sql ="where country = '"+ ((String) comboCountry.getSelectedItem()).replace("'", "''")+"'";
		} else if (!" All".equals(comboMountain.getSelectedItem())) { 
			System.out.println(comboMountain.getSelectedItem());
	    	sql ="where country = '"+ ((String) comboCountry.getSelectedItem()).replace("'", "''")+"' and name = '"+((String) comboMountain.getSelectedItem()).replace("'", "''") +"'";
	
		} else {
			System.out.println("Oups sais pas quoi faire....");
		}

		selectMountain.selectAll(sql);
		

    	List<Placemark> airports = new ArrayList<>();
    	SelectAiport selectAiport = new SelectAiport();
		selectAiport.selectAll(sqlCountry, airports);
		airports = selectAiport.getPlacemarks();
		
    	selectCity.selectAll(sqlCountry);
		
    	searchMountainNeighbor(airports,selectCity,selectMountain);
		
		selectedMountains = selectMountain.getMapMountains();
		
    	flightplanName = Utility.getInstance().getFlightPlanName("mountain_city_airport.kml");

		saveKMLFile(manageXMLFile,airports,flightplanName);
		manageXMLFile.launchGoogleEarth(new File(flightplanName));
		
		System.out.println(airports.size());

	}
    
    /**
     * createKMLCity
     * 
     * 
     * @param mapCities
     * @param comboCountry
     * @param comboState
     * @param comboCity
     */
	public void createKMLCity(ManageXMLFile manageXMLFile,Map<String, City> mapCities, JComboBox comboCountry, JComboBox comboState, JComboBox comboCity, Distance dist) {
		this.dataline = new Dataline();
		this.dist = dist;

		String sql = "";	
		String strQuote = "";
    	SelectCity selectCity = new SelectCity();

		strQuote = ((String) comboCity.getSelectedItem()).replace("'", "''");
		
		if (!" All".equals(comboCity.getSelectedItem())) {
			
			if (!" All".equals(comboState.getSelectedItem())) { 
				sql = "where country = '"+ ((String) comboCountry.getSelectedItem()).replace("'", "''")+"' and admin_name = '"+((String) comboState.getSelectedItem()).replace("'", "''")+"' and city = '"+strQuote+"'";

			} else {
				sql = "where country = '"+ ((String) comboCountry.getSelectedItem()).replace("'", "''")+"' and city = '"+strQuote+"'";
			}
			
		} else if (" All".equals(comboCity.getSelectedItem())) { 
			System.out.println(comboCity.getSelectedItem());
			System.out.println(mapCities.get(comboCity.getSelectedItem()));
			
			if (" All".equals(comboState.getSelectedItem())){
		    	sql ="where country = '"+ ((String) comboCountry.getSelectedItem()).replace("'", "''")+"'";
			} else {
		    	sql ="where country = '"+ ((String) comboCountry.getSelectedItem()).replace("'", "''")+"' and admin_name = '"+((String) comboState.getSelectedItem()).replace("'", "''") +"'";
			}
	
		} else {
			System.out.println("Oups sais pas quoi faire....");
		}
		
		
		selectCity.selectAll(sql);
		

		SelectMountain selectMoutain = new SelectMountain();
		selectMoutain.selectAll("");

    	List<Placemark> airports = new ArrayList<>();
    	SelectAiport selectAiport = new SelectAiport();
    	sql = "where country = '"+ ((String) comboCountry.getSelectedItem()).replace("'", "''")+"'";
		selectAiport.selectAll(sql, airports);
		airports = selectAiport.getPlacemarks();
		
		searchCityNeighbor(airports,selectCity,selectMoutain);
		
		selectedCities = selectCity.getMapCities();
		
    	flightplanName = Utility.getInstance().getFlightPlanName("city_airport_mountain.kml");
    	
		saveKMLFile(manageXMLFile,airports,flightplanName);
		
		manageXMLFile.launchGoogleEarth(new File(flightplanName));
		
		System.out.println(airports.size());

	}
	
	/**
	 * 
	 * @param manageXMLFile
	 * @param mapCities
	 * @param comboCountry
	 * @param comboState
	 * @param comboCity
	 */
	public void createKMLAirport(ManageXMLFile manageXMLFile,Map<String, City> mapCities, JComboBox comboCountry, JComboBox comboState, JComboBox comboCity, Distance dist) {
		this.dataline = new Dataline();
		String sql = "";	
		String strQuote = "";
		SelectCity selectCity = new SelectCity();
	
		this.dist = dist;

		strQuote = ((String) comboCity.getSelectedItem()).replace("'", "''");
		
		if (!" All".equals(comboCity.getSelectedItem())) {
			
			if (!" All".equals(comboState.getSelectedItem())) { 
				sql = "where country = '"+ ((String) comboCountry.getSelectedItem()).replace("'", "''")+"' and state = '"+((String) comboState.getSelectedItem()).replace("'", "''")+"' and city = '"+strQuote+"'";

			} else {
				sql = "where country = '"+ ((String) comboCountry.getSelectedItem()).replace("'", "''")+"' and city = '"+strQuote+"'";
			}
			
		} else if (" All".equals(comboCity.getSelectedItem())) { 
			System.out.println(comboCity.getSelectedItem());
			System.out.println(mapCities.get(comboCity.getSelectedItem()));
			
			if (" All".equals(comboState.getSelectedItem())){
		    	sql ="where country = '"+ ((String) comboCountry.getSelectedItem()).replace("'", "''")+"'";
			} else {
		    	sql ="where country = '"+ ((String) comboCountry.getSelectedItem()).replace("'", "''")+"' and state = '"+((String) comboState.getSelectedItem()).replace("'", "''") +"'";
			}
	
		} else {
			System.out.println("Oups sais pas quoi faire....");
		}
		
		
		selectCity.selectAll(sql.replace("state", "admin_name"));
   	
    	SelectMountain selectMoutain = new SelectMountain();
		selectMoutain.selectAll("");
		
		//selectedMountains = selectMoutain.getMapMountains();

    	SelectAiport selectAiport = new SelectAiport();
		
    	List<Placemark> placemarks = new ArrayList<>();
		selectAiport.selectAll(sql, placemarks);
		placemarks = selectAiport.getPlacemarks();
		selectedAirports = selectAiport.getMapPlacemark();
	
		searchAiportNeighbor(placemarks,selectCity,selectMoutain);
	
    	flightplanName = Utility.getInstance().getFlightPlanName("airport_city_mountain.kml");
		
		saveKMLFile(manageXMLFile,placemarks,flightplanName);
		
		manageXMLFile.launchGoogleEarth(new File(flightplanName));
		
		System.out.println("airports found = "+ placemarks.size());

	}

    
    public  synchronized void saveKMLFile(ManageXMLFile manageXMLFile,List<Placemark> placemarks, String kmlRelative){
		Writer writer = null;
						
 		try {
 			
			
		    writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream(kmlRelative), "utf-8"));
		    
		    writer.write(manageXMLFile.createKMLHeader(
		    		(selectedAirports != null ?selectedAirports.size():0)+
		    		(selectedCities != null?selectedCities.size():0)+
		    		(selectedMountains != null?selectedMountains.size():0)));
		    
		    
		    writer.write("<Folder><name> Airports found ("+selectedAirports.size()+") </name>");
		    for(Placemark placemark:selectedAirports.values()){
		    	writer.write(placemark.buildXML("fsx_airport"));
		    }   
	        writer.write("</Folder>");

		    writer.write("<Folder><name> Cities found ("+selectedCities.size()+") </name>");
		    for(City city: selectedCities.values()){
		    	writer.write(createKML.buildCityPlaceMark(city));
		    }
		    writer.write("</Folder>"); 
	    
		    writer.write("<Folder><name> Mountains found ("+selectedMountains.size()+") </name>");
		    for(Mountain mountain: selectedMountains.values()){
		    	writer.write(createKML.buildMountainPlaceMark(mountain));
		    }
		    writer.write("</Folder>"); 
	    

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
			    			+ "<altitudeMode>relativeToGround</altitudeMode>"
			    			+ "<coordinates>\r\n");
			    	writer.write(dataline.getMapData().get(key));
			    	writer.write("</coordinates></LineString></Placemark></Folder>");
		    		
		    	}
		    	
		    }
	    

		    writer.write("</Folder></Document></kml>");
	   
		} catch (IOException ex) {
		  System.err.println(ex.getMessage());
		} finally {
		   try {writer.close();} catch (Exception ex) {}
		}			
    	
    }
    

    public  synchronized void saveKMLFileICAO(ManageXMLFile manageXMLFile,List<Placemark> placemarks, String kmlRelative){
 		Writer writer = null;
 						
  		try {
 		    writer = new BufferedWriter(new OutputStreamWriter(
 		          new FileOutputStream(kmlRelative), "utf-8"));
 		    
 		    writer.write(manageXMLFile.createKMLHeader(placemarks.size()));
 		    
			writer.write("<Folder><name> Airports found ("+placemarks.size()+") </name>");

 		    for(Placemark placemark:placemarks){
 		    	writer.write(placemark.buildXML("fsx_airport"));
 		    }
			 writer.write("</Folder>"); 
		    
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
			    			+ "<altitudeMode>relativeToGround</altitudeMode>"
			    			+ "<coordinates>\r\n");
			    	writer.write(dataline.getMapData().get(key));
			    	writer.write("</coordinates></LineString></Placemark></Folder>");
		    		
		    	}
		    	
		    }

 		    writer.write("</Folder></Document></kml>");
 	   
 		} catch (IOException ex) {
 		  System.err.println(ex.getMessage());
 		} finally {
 		   try {writer.close();} catch (Exception ex) {}
 		}			
     	
     }

    
    public ActionListener launchGoogleEarth() {
    	
    	if (kmlFlightPlanFile != null) {
        	manageXMLFile.launchGoogleEarth(new File(kmlFlightPlanFile));
    	}
		return null;
    }

	public String getKmlFlightPlanFile() {
		return kmlFlightPlanFile;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

}
