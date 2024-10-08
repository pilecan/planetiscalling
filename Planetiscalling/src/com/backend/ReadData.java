package com.backend;

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
import java.util.TreeMap;

import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.cfg.common.Dataline;
import com.cfg.common.Info;
import com.db.CreateKmlFSPlan;
import com.db.CreateKmlFSPlan.NoPoints;
import com.db.SelectAirport;
import com.db.SelectCity;
import com.db.SelectMountain;
import com.db.SelectNdb;
import com.db.SelectVor;
import com.db.UtilityDB;
import com.geo.util.Geoinfo;
import com.main.form.Result;
import com.model.Airport;
import com.model.Boundingbox;
import com.model.City;
import com.model.Distance;
import com.model.Landmark;
import com.model.Mountain;
import com.model.Ndb;
import com.model.Vor;
import com.util.SwingUtils;
import com.util.Utility;
import com.util.UtilityMap;

public class ReadData implements Info{
	private SelectAirport selectAiport;
	private SelectCity selectCity;
	private SelectMountain selectMountain;
	private SelectVor selectVor;
	private SelectNdb selectNdb;
	private String kmlFlightPlanFile;
	private Map<String, City> selectedCities ;
	private Map<String, Mountain> selectedMountains ;
	private Map<String, Landmark> selectedLandmarks;
	private Map<String, Airport> selectedAirports ;
	private Map<String, Airport> selectedMapAirports;

	private Map<Integer, Vor> selectedVors;
	private Map<Integer, Ndb> selectedNdbs;
	private CreateKmlFSPlan createKmlFSPlan;
	
	private CreateKML createKML;
	
	private String flightplanName ;
	
	private String KmlFlightplanName;
	
	private Result result;
	
	private Dataline dataline;
	private Distance dist;

	private String icaos;
	private boolean isCity = true;
	private boolean isMountain = true;
	private boolean isDistance = true;
	
	public ReadData(Result result,   Distance dist){
			this.result = result;
		this.dataline = new Dataline();
		selectFlightplan(dist);
		
	}
	
	private void initDB() {
		if (!UtilityDB.getInstance().isInitAll()) {
			UtilityDB.getInstance().selectMountain("");
			UtilityDB.getInstance().selectVor("");
			UtilityDB.getInstance().selectNdb("");
			UtilityDB.getInstance().setInitAll(true);
		} 
		
	}
	
	public ReadData(String icaos, Result result, Distance dist){
		this.result = result;
		this.dataline = new Dataline();
		this.dist = dist;
		this.icaos = icaos;

		creatIcaoAirports(icaos,dist);

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
				flightplanName = chooser.getSelectedFile().toString();
				createFlightplan(dist);
		    } else {
		    	flightplanName = "";
		    	kmlFlightPlanFile = "";
		    }
			
		
	}
	  private JFileChooser selectDirectoryProgram(String title,String directory){
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new java.io.File(directory));
			chooser.setDialogTitle(title);
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);
			String[] EXTENSION=new String[]{"pln"};
			 FileNameExtensionFilter filter=new FileNameExtensionFilter("FS2020/FSX (.pln)",EXTENSION);
			 chooser.setFileFilter(filter);
			 chooser.setMultiSelectionEnabled(false);
		
			return chooser;
	    	
	    }

	public void createFlightplan( Distance dist) {
		
		try {

			this.createKmlFSPlan =  new CreateKmlFSPlan(flightplanName, dist);
			
			kmlFlightPlanFile = createKmlFSPlan.getKmlFlightPlanFile();
			
			result.resetButton();
		   
			setResult();
			result.setLegPoints(createKmlFSPlan.getLegPoints());

			
		} catch (NullPointerException | NoPoints | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	        
			
	}
	
	private void setResult() {
		result.setDistance(Math.round(createKmlFSPlan.getDistanceBetween())); 
		result.setAltitude(Math.round(createKmlFSPlan.getAltitude()*3.28084)); 
		result.setDeparture(createKmlFSPlan.getDeparture());
		result.setDestination(createKmlFSPlan.getDestination());
		
		result.setFlightplan(createKmlFSPlan.getFlightplan());
		result.setMapAirport(Utility.getInstance().creatMapAirport(createKmlFSPlan.getSelectedAirports()));
		result.setSelectedCities(createKmlFSPlan.getSelectedCities());
		result.setSelectedMountains(createKmlFSPlan.getSelectedMountains());
		result.setSelectedNdbs(createKmlFSPlan.getSelectedNdbs());
		result.setSelectedVors(createKmlFSPlan.getSelectedVors());
		result.setSelectedLandmarks(createKmlFSPlan.getSelectedLandmarks());

		
	}
	
	public void resetFlightPlanResult() {
		
		//Flightplan
	    createKmlFSPlan.setAltitude(Math.round(Double.parseDouble(result.getFlightplan().getCruisingAlt())/3.28084));
		createKmlFSPlan.setSelectedAirports(new HashMap<String, Airport>() );
		createKmlFSPlan.setSelectedCities(new HashMap<String, City>());
		createKmlFSPlan.setSelectedMountains(new HashMap<String, Mountain>());
		createKmlFSPlan.setSelectedNdbs(new HashMap<Integer, Ndb>());
		createKmlFSPlan.setSelectedVors(new HashMap<Integer, Vor>());
		createKmlFSPlan.setSelectedLandmarks(new HashMap<String, Landmark>());
		setResult();
	}
	
      /**
     * 
     */
    
	public void resetIcaoResult() {
		
		result.setMapAirport(new HashMap<String, Airport>());
		result.setSelectedCities(new HashMap<String, City>());
		result.setSelectedMountains(new HashMap<String, Mountain>());
		result.setSelectedNdbs(new HashMap<Integer, Ndb>());
		result.setSelectedVors(new HashMap<Integer, Vor>());
		result.setSelectedLandmarks(new HashMap<String, Landmark>());
	}

	public void resetAirportResult() {
		
	//	result.setMapAirport(new HashMap<String, Airport>());
		result.setSelectedCities(new HashMap<String, City>());
		result.setSelectedMountains(new HashMap<String, Mountain>());
		result.setSelectedNdbs(new HashMap<Integer, Ndb>());
		result.setSelectedVors(new HashMap<Integer, Vor>());
		result.setSelectedLandmarks(new HashMap<String, Landmark>());
	}
	
	public void resetLandmarkResult() {
		result.setMapAirport(new HashMap<String, Airport>());
	    result.setSelectedMapAirports(new HashMap<String, Airport>());
		result.setSelectedCities(new HashMap<String, City>());
		result.setSelectedLandmarks(new HashMap<String, Landmark>());
		result.setSelectedMountains(new HashMap<String, Mountain>());
	}
	public void resetCityResult(Result result) {
		
	    result.setMapAirport(new HashMap<String, Airport>());
	    result.setSelectedMapAirports(new HashMap<String, Airport>());
		//result.setSelectedCities(new HashMap<String, City>());
		result.setSelectedMountains(new HashMap<String, Mountain>());
		result.setSelectedNdbs(new HashMap<Integer, Ndb>());
		result.setSelectedVors(new HashMap<Integer, Vor>());
		result.setSelectedLandmarks(new HashMap<String, Landmark>());
		//setResult();
	}
	
    public void creatIcaoAirports(String icaos, Distance dist) {
    	
    	this.dist = dist;
		createKML = new CreateKML();
		selectedMountains = new HashMap<>();
		selectedLandmarks = new HashMap<>();
		selectedCities = new HashMap<>();
		selectedNdbs = new HashMap<>();
		selectedVors = new HashMap<>();
		selectedLandmarks = new HashMap<>();
		UtilityDB.getInstance().setGroupLandmark(new TreeMap<String, List<Landmark>>()) ;
		result.resetButton();
		
		initDB();

		String search = Utility.getInstance().valideIcao(icaos);
		
	    if (!"".equals(search)) {
	     	search = search.toUpperCase();		
			
			search = search.replaceAll("\\s+", "','");
			String sql = "where ident in ('"+ search + "') ";

	    	SelectAirport selectAirport = new SelectAirport();	
			selectAirport.select(sql);
			
			searchAirportNeighbor(new ArrayList<Airport>(selectAirport.getMapAirport().values()));
			searchIcaoVorNdb(new ArrayList<Airport>(selectAirport.getMapAirport().values()));
			
			result.setMapAirport(selectAirport.getMapAirport());
			result.setSelectedCities(selectedCities);
			result.setSelectedMountains(selectedMountains);
			result.setSelectedNdbs(selectedNdbs);
			result.setSelectedVors(selectedVors);
			result.setSelectedLandmarks(selectedLandmarks);

			
			saveKMLFileICAO(selectAirport.getMapAirport(),Utility.getInstance().getFlightPlanName(Info.kmlFlightplanName),dist);
	    }
		
    }
    /**
     * 
     * @param airports
     * @param selectVor
     * @param selectNdb
     */
    private void searchIcaoVorNdb(List<Airport> airports) {
    	
		selectedVors = new HashMap<>();

		if (dist.isVorNdb()) {
    		for(Vor vor : UtilityDB.getInstance().getVors()){
				Double[] dd1 = Geoinfo.convertDoubleLongLat(vor.getCoordinates());
				
 				for(Airport airport : airports){
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
		selectedNdbs = new HashMap<>();
		if (dist.isVorNdb()) {
			for(Ndb ndb : UtilityDB.getInstance().getNdbs()){
				Double[] dd1 = Geoinfo.convertDoubleLongLat(ndb.getCoordinates());
				
 				for(Airport airport : airports){
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

    private void searchCityVorNdb() {
    	
		selectedVors = new HashMap<>();

		if (dist.isVorNdb()) {
    		for(Vor vor : UtilityDB.getInstance().getVors()){
				Double[] dd1 = Geoinfo.convertDoubleLongLat(vor.getCoordinates());
				
 				for(City city : selectCity.getCities()){
					Double[] dd2 = Geoinfo.convertDoubleLongLat(city.getCoordinates());
					
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
		selectedNdbs = new HashMap<>();
		if (dist.isVorNdb()) {
		    for(Ndb ndb : UtilityDB.getInstance().getNdbs()){
				Double[] dd1 = Geoinfo.convertDoubleLongLat(ndb.getCoordinates());
				
 				for(City city : selectCity.getCities()){
					Double[] dd2 = Geoinfo.convertDoubleLongLat(city.getCoordinates());
					
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
    /**
     * 
     * @param airports
     * @param selectCity
     * @param selectMoutain
     */
    private void searchLandmarkNeighbor() {

    	if (dist.isCity()) {
 			for(City city : UtilityDB.getInstance().getCities()){
 				Double[] dd1 = Geoinfo.convertDoubleLongLat(city.getCoordinates());
 				
 				for(Landmark landmark : UtilityDB.getInstance().getLandmarks()){
 					Double[] dd2 = Geoinfo.convertDoubleLongLat(landmark.getCoordinates());
 					
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
			for(Mountain mountain : UtilityDB.getInstance().getMountains()){
				Double[] dd1 = Geoinfo.convertDoubleLongLat(mountain.getCoordinates());

 				for(Landmark landmark : UtilityDB.getInstance().getLandmarks()){
					Double[] dd2 = Geoinfo.convertDoubleLongLat(landmark.getCoordinates());

					if (Geoinfo.distance(dd1[1], dd1[0], dd2[1], dd2[0], 'N') < dist.getMountainDist()) {
						selectedMountains.put(mountain.getName(), new Mountain(mountain));
						if (dist.isLine()) {
							dataline.setData("mountain",dd1[0]+","+ dd1[1]+",0"+"\n\r"+dd2[0]+","+ dd2[1]+",0"+"\n\r");
						}
					}
				}
			}
		}
		if (dist.isAirport()) {
			for(Airport airport: UtilityDB.getInstance().getMapAirport().values()){
				Double[] dd1 = Geoinfo.convertDoubleLongLat(airport.getCoordinates());

 				for(Landmark landmark : UtilityDB.getInstance().getLandmarks()){
					Double[] dd2 = Geoinfo.convertDoubleLongLat(landmark.getCoordinates());

					if (Geoinfo.distance(dd1[1], dd1[0], dd2[1], dd2[0], 'N') < dist.getAirportDist()) {
						selectedAirports.put(airport.getIdent(), new Airport(airport));
						if (dist.isLine()) {
							dataline.setData("airport",dd1[0]+","+ dd1[1]+",0"+"\n\r"+dd2[0]+","+ dd2[1]+",0"+"\n\r");
						}
					}
				}
			}
		}

 		
     }	

    private void searchAirportNeighbor(List<Airport> airports) {
		
    	selectedCities = new HashMap<String, City>();
  		
		if (dist.isCity()) {
			for (int i = 0; i < airports.size(); i++) {
				String countries = UtilityMap.getInstance().checkCountry(airports.get(i).getRegion());
				Boundingbox.getInstance().createBox(airports.get(i).getLaty(),airports.get(i).getLonx(),
						airports.get(i).getLaty(),airports.get(i).getLonx(), 
						dist.getCityDist());
				UtilityDB.getInstance().selectCityInBox("where country in ("+countries+")");
				selectedCities.putAll(UtilityDB.getInstance().getMapCities());

			}

		}
		
		selectedMountains = new HashMap<>();
		if (dist.isMountain()) {
			for(Mountain mountain : UtilityDB.getInstance().getMountains()){
				Double[] dd1 = Geoinfo.convertDoubleLongLat(mountain.getCoordinates());

 				for(Airport airport : airports){
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
	
		if (dist.isLandmark()) {
			
			for (int i = 0; i < airports.size(); i++) {
				try {
					String province =  UtilityMap.getInstance().checkProvince(airports.get(i).getLaty(),airports.get(i).getLonx());
					
					if (province != null) {
						Boundingbox.getInstance().createBox(airports.get(i).getLaty(),airports.get(i).getLonx(),
								airports.get(i).getLaty(),airports.get(i).getLonx(), 
															dist.getLandmarkDist());
						
						UtilityDB.getInstance().selectLandmarkInBox("where admin = '"+province+"'");
						selectedLandmarks.putAll(UtilityDB.getInstance().getMapLandmark());
											
					}
					

				} catch (Exception e) {
				}
				
				Utility.getInstance().createLandmarkByGroup(selectedLandmarks);

				
			}
		}
		
		
     }	
    /**
     * 
     * @param placemarks
     * @param selectCity
     * @param selectMoutain
     */
	private void searchCityNeighbor(String sql, JComboBox<String>comboCountry) {
		selectedAirports = new HashMap<String, Airport>();
		selectedMapAirports  = new HashMap<>();
		if (dist.isAirport()) {
			sql ="where country = '"+ ((String) comboCountry.getSelectedItem()).replace("'", "''")+"'";

			UtilityDB.getInstance().selectAirport(sql);
			for (Airport airport : UtilityDB.getInstance().getAirports()) {
				Double[] dd1 = Geoinfo.convertDoubleLongLat(airport.getCoordinates());

				for (City city : UtilityDB.getInstance().getCities()) {
					Double[] dd2 = Geoinfo.convertDoubleLongLat(city.getCoordinates());

					if (Geoinfo.distance(dd1[1], dd1[0], dd2[1], dd2[0], 'N') < dist.getAirportDist()) {
						selectedAirports.put(airport.getIdent(), new Airport(airport));
						if (dist.isLine()) {
							dataline.setData("airport",dd1[0]+","+ dd1[1]+",0"+"\n\r"+dd2[0]+","+ dd2[1]+",0"+"\n\r");
						}
					}
				}
			}
		}
		selectedMountains = new HashMap<>();
		if (dist.isMountain()) {
			selectedMountains = new HashMap<>();
			for(Mountain mountain : UtilityDB.getInstance().getMountains()){
				Double[] dd1 = Geoinfo.convertDoubleLongLat(mountain.getCoordinates());

				for (City city : UtilityDB.getInstance().getCities()) {
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

	    if (dist.isLandmark()){
			
			for (City city : selectCity.getCities()) {
				try {
					String province =  UtilityMap.getInstance().checkProvince(city.getLaty(),city.getLonx());
					if (province != null) {
						Boundingbox.getInstance().createBox(city.getLaty(),city.getLonx(),
								city.getLaty(),city.getLonx(), dist.getLandmarkDist());
						
						UtilityDB.getInstance().selectLandmarkInBox("where admin = '"+province+"'");
						selectedLandmarks.putAll(UtilityDB.getInstance().getMapLandmark());
						
					}
					
				} catch (Exception e) {
				}
				
			}
			
			Utility.getInstance().createLandmarkByGroup(selectedLandmarks);

		}
		


	}
	/**
	 * 
	 * @param airports
	 * @param selectCity
	 * @param selectMoutain
	 */
	private void searchMountainNeighbor(List<Airport> airports, SelectCity selectCity, SelectMountain selectMoutain, SelectVor selectVor,SelectNdb selectNdb) {
		selectedAirports = new HashMap<String, Airport>();
	
		if (dist.isAirport()) {
			for (Airport airport : airports) {
				Double[] dd1 = Geoinfo.convertDoubleLongLat(airport.getCoordinates());

				for (Mountain mountain : selectMoutain.getMountains()) {
					Double[] dd2 = Geoinfo.convertDoubleLongLat(mountain.getCoordinates());

					if (Geoinfo.distance(dd1[1], dd1[0], dd2[1], dd2[0], 'N') < dist.getAirportDist()) {
						selectedAirports.put(airport.getIdent(), new Airport(airport));
						if (dist.isLine()) {
							dataline.setData("airport",dd1[0]+","+ dd1[1]+",0"+"\n\r"+dd2[0]+","+ dd2[1]+",0"+"\n\r");
						}
					}
				}
			}

		}
		selectedCities = new HashMap<>();
    	if (dist.isCity()) {
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

		selectedVors = new HashMap<>();

		if (dist.isVorNdb()) {
    		for(Vor vor : UtilityDB.getInstance().getVors()){
				Double[] dd1 = Geoinfo.convertDoubleLongLat(vor.getCoordinates());
				
				for (Mountain mountain : selectMoutain.getMountains()) {
					Double[] dd2 = Geoinfo.convertDoubleLongLat(mountain.getCoordinates());
					
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
		selectedNdbs = new HashMap<>();
		if (dist.isVorNdb()) {
		    for(Ndb ndb : UtilityDB.getInstance().getNdbs()){
				Double[] dd1 = Geoinfo.convertDoubleLongLat(ndb.getCoordinates());
				
				for (Mountain mountain : selectMoutain.getMountains()) {
					Double[] dd2 = Geoinfo.convertDoubleLongLat(mountain.getCoordinates());
					
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
	
/*	*//**
	 * 
	 * @param airports
	 * @param selectCity
	 * @param selectMoutain
	 *//*
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
*/    
	
    /**
     * 
     * @param manageXMLFile
     * @param mapCities
     * @param comboCountry
     * @param comboMountain
     * @param dist
     */
	public void createKMLMountain(Result result,  JComboBox comboCountry, JComboBox comboMountain,Distance dist) {
		String sql = "";	
    	SelectCity selectCity = new SelectCity();
    	SelectMountain selectMountain = new SelectMountain();
		this.dataline = new Dataline();
		this.result = result;
		this.dist = dist;
		result.resetButton();
		initDB();
    	
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
			//System.out.println(comboMountain.getSelectedItem());
	    	sql ="where country = '"+ ((String) comboCountry.getSelectedItem()).replace("'", "''")+"' and name = '"+((String) comboMountain.getSelectedItem()).replace("'", "''") +"'";
	
		} else {
			System.out.println("Oups sais pas quoi faire....");
		}

		selectMountain.selectAll(sql);


    	UtilityDB.getInstance().selectAirport("where country = '"+ ((String) comboCountry.getSelectedItem()).replace("'", "''")+"'");

    	selectedMountains = selectMountain.getMapMountains();
 	
    	searchMountainNeighbor(new ArrayList<Airport>(UtilityDB.getInstance().getMapAirport().values()),selectCity,selectMountain,selectVor,selectNdb);
    	
		if (selectedAirports.size() == 0) {
			UtilityDB.getInstance().setMapAirport(new HashMap<String, Airport>());
		} 

    	result.setSelectedMapAirports(selectedAirports);
	    result.setMapAirport(selectedAirports);
		result.setSelectedCities(selectedCities);
		result.setSelectedMountains(selectedMountains);
		result.setSelectedNdbs(selectedNdbs);
		result.setSelectedVors(selectedVors);
			

		saveKMLFile(new ArrayList<Airport>(UtilityDB.getInstance().getMapAirport().values()), Utility.getInstance().getFlightPlanName(Info.kmlMountainCityAirportName), dist);
	//	Utility.getInstance().launchGoogleEarth(Utilithy);
		
		//System.out.println(airports.size());

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
	public void createKMLCity(Result result, JComboBox comboCountry, JComboBox comboState, JComboBox comboCity, 
			 Distance dist) {
		this.dataline = new Dataline();
		this.dist = dist;
		this.result = result;
		selectedLandmarks = new HashMap<>();
		UtilityDB.getInstance().setGroupLandmark(new TreeMap<String, List<Landmark>>()) ;
		result.resetButton();
		
		initDB();

		String sql = "";	
		String strQuote = "";

		strQuote = ((String) comboCity.getSelectedItem()).replace("'", "''");
		
		if (!" All".equals(comboCity.getSelectedItem())) {
			
			if (!" All".equals(comboState.getSelectedItem())) { 
				sql = "where country = '"+ ((String) comboCountry.getSelectedItem()).replace("'", "''")+"' and admin_name = '"+((String) comboState.getSelectedItem()).replace("'", "''")+"' and city_ascii = '"+strQuote+"'";

			} else {
				sql = "where country = '"+ ((String) comboCountry.getSelectedItem()).replace("'", "''")+"' and city_ascii = '"+strQuote+"'";
			}
			
		} else if (" All".equals(comboCity.getSelectedItem())) { 
		//	System.out.println(comboCity.getSelectedItem());
			// System.out.println(mapCities.get(comboCity.getSelectedItem()));
			
			if (" All".equals(comboState.getSelectedItem())){
		    	sql ="where country = '"+ ((String) comboCountry.getSelectedItem()).replace("'", "''")+"'";
			} else {
		    	sql ="where country = '"+ ((String) comboCountry.getSelectedItem()).replace("'", "''")+"' and admin_name = '"+((String) comboState.getSelectedItem()).replace("'", "''") +"'";
			}
	
		} else {
			System.out.println("Oups sais pas quoi faire....");
		}
	
		UtilityDB.getInstance().selectCity(sql);
	

		searchCityNeighbor(sql, comboCountry);
		searchCityVorNdb();

		
		if (selectedAirports.size() == 0) {
			UtilityDB.getInstance().setMapAirport(new HashMap<String, Airport>());
		} 
		
		selectedCities = UtilityDB.getInstance().getMapCities();
	
		result.setSelectedMapAirports(selectedAirports);
	    result.setMapAirport(selectedAirports);
		result.setSelectedCities(selectedCities);
		result.setSelectedMountains(selectedMountains);
		result.setSelectedNdbs(selectedNdbs);
		result.setSelectedVors(selectedVors);
		result.setSelectedLandmarks(selectedLandmarks);
		
		saveKMLFile(new ArrayList<Airport>(selectedAirports.values()),Utility.getInstance().getFlightPlanName(Info.kmlCityAirportMountainName),dist);

	}
	
	public void createKMLLandmark(Result result, 
			JComboBox<?> comboCountry, JComboBox<?> comboState, JComboBox<?> comboGeoterm, JComboBox<?> comboGeoname, Distance dist) {
		this.dataline = new Dataline();
		this.dist = dist;
		this.result = result;
	   	selectedCities = new HashMap<>();
		selectedMountains = new HashMap<>();
		selectedAirports = new HashMap<>();
		result.resetButton();


		String sql = "";	
		String strQuote = "";

		strQuote = ((String) comboGeoterm.getSelectedItem()).replace("'", "''");
		
		 if ("All".equals(comboGeoname.getSelectedItem())) { 
		    	sql = "where admin = '"+((String) comboState.getSelectedItem()).replace("'", "''")+"' "
		    			+ "and geo_term = '"+((String) comboGeoterm.getSelectedItem()).replace("'", "''")+"'";
		} else {
			strQuote = ((String) comboGeoname.getSelectedItem()).replace("'", "''");

	    	sql = "where admin = '"+((String) comboState.getSelectedItem()).replace("'", "''")+"' "
	    			+ "and geo_term = '"+((String) comboGeoterm.getSelectedItem()).replace("'", "''")+"'"
	    			+ "and geo_name = '"+strQuote+"'";

		}
		 
		UtilityDB.getInstance().selectLandmark(sql);
		selectedLandmarks = UtilityDB.getInstance().getMapLandmark();
		
		sql = "where country = '"+((String) comboCountry.getSelectedItem()).replace("'", "''")+
				"' and admin_name ='"+((String) comboState.getSelectedItem()).replace("'", "''")+"'";
		
		UtilityDB.getInstance().setInitAll(false);
		UtilityDB.getInstance().selectCity(sql);
		UtilityDB.getInstance().getCities();
		
    	UtilityDB.getInstance().selectAirport(sql.replace("admin_name", "state"));
    	UtilityDB.getInstance().getAirports();
    	
    	UtilityDB.getInstance().selectMountain("where country = '"+((String) comboCountry.getSelectedItem()).replace("'", "''")+"' ");
    	UtilityDB.getInstance().getMountains();

		searchLandmarkNeighbor();
		
		result.setSelectedLandmarks(selectedLandmarks);
		result.setSelectedMapAirports(selectedAirports);
	    result.setMapAirport(selectedAirports);
		result.setSelectedCities(selectedCities);
		result.setSelectedMountains(selectedMountains);
		
		saveKMLFileLandmark(Utility.getInstance().getFlightPlanName(Info.kmlLandmarkMountainCityAirportName),dist);

	}
	/**
	 * 
	 * @param manageXMLFile
	 * @param mapCities
	 * @param comboCountry
	 * @param comboState
	 * @param comboCity
	 */
	public void createKMLAirport(Result result, Map<String, City> mapCities, JComboBox comboCountry, JComboBox comboState, JComboBox comboCity,
			 Distance dist) {
		this.dataline = new Dataline();
		String sql = "";	
		String strQuote = "";
		this.result = result;
		selectedLandmarks = new HashMap<>();
		UtilityDB.getInstance().setGroupLandmark(new TreeMap<String, List<Landmark>>()) ;
		result.resetButton();

	
		this.dist = dist;

		strQuote = ((String) comboCity.getSelectedItem()).replace("'", "''");
		
		if (!" All".equals(comboCity.getSelectedItem())) {
			
			if (!" All".equals(comboState.getSelectedItem())) { 
				sql = "where country = '"+ ((String) comboCountry.getSelectedItem()).replace("'", "''")+"' and state = '"+((String) comboState.getSelectedItem()).replace("'", "''")+"' and city = '"+strQuote+"'";

			} else {
				sql = "where country = '"+ ((String) comboCountry.getSelectedItem()).replace("'", "''")+"' and city = '"+strQuote+"'";
			}
			
		} else if (" All".equals(comboCity.getSelectedItem())) { 
			//System.out.println(comboCity.getSelectedItem());
			//System.out.println(mapCities.get(comboCity.getSelectedItem()));
			
			if (" All".equals(comboState.getSelectedItem())){
		    	sql ="where country = '"+ ((String) comboCountry.getSelectedItem()).replace("'", "''")+"'";
			} else {
		    	sql ="where country = '"+ ((String) comboCountry.getSelectedItem()).replace("'", "''")+"' and state = '"+((String) comboState.getSelectedItem()).replace("'", "''") +"'";
			}
	
		} else {
			System.out.println("Oups sais pas quoi faire....");
		}
		
/*		SelectCity selectCity = new SelectCity();
		selectCity.selectAll(sql.replace("state", "admin_name"));
*/
    	SelectAirport selectAirport = new SelectAirport();
		
		selectAirport.select(sql);
		selectedAirports = selectAirport.getMapAirport();
		
		initDB();
		
		searchAirportNeighbor(new ArrayList<Airport>(selectAirport.getMapAirport().values()));
		searchIcaoVorNdb(new ArrayList<Airport>(selectAirport.getMapAirport().values()));
		
		result.setMapAirport(selectAirport.getMapAirport());
		result.setSelectedCities(selectedCities);
		result.setSelectedLandmarks(selectedLandmarks);
		result.setSelectedMountains(selectedMountains);
		result.setSelectedNdbs(selectedNdbs);
		result.setSelectedVors(selectedVors);
		
		saveKMLFile(new ArrayList<Airport>(selectAirport.getMapAirport().values()),Utility.getInstance().getFlightPlanName(Info.kmlAirportCityName),dist);
		
		//Utility.getInstance().launchGoogleEarth(new File(KmlFlightplanName));
		
		//System.out.println("airports found = "+ placemarks.size());

	}

    
    public  synchronized void saveKMLFile(List<Airport> airports, String kmlRelative, Distance dist){
		Writer writer = null;
						
 		try {
 		    writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream(kmlRelative), "utf-8"));
		    
		    writer.write(CreateKML.createHeader(
		    		(selectedAirports != null ?selectedAirports.size():0)+
		    		(selectedCities != null?selectedCities.size():0)+
		    		(selectedMountains != null?selectedMountains.size():0)+
		    		(selectedVors != null?selectedVors.size():0)+
		    		(selectedNdbs != null?selectedNdbs.size():0)
		    		));
		    
		    
		    writer.write("<Folder><name> Airports found ("+selectedAirports.size()+") </name>");
		    for(Airport airport:selectedAirports.values()){
		    	//writer.write(placemark.buildXML("fsx_airport"));
				writer.write(createKML.buildAirportPlaceMark(airport));

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
	    
		    writer.write("<Folder><name> VOR found ("+selectedVors.size()+") </name>");
		    
		    
		    for(Vor vor: selectedVors.values()){
		    	writer.write(createKML.buildVorPlaceMark(vor));
		    }
	    	
		    writer.write("</Folder>"); 
		    writer.write("<Folder><name> NDB found ("+selectedNdbs.size()+") </name>");
		    
		    
		    for(Ndb ndb: selectedNdbs.values()){
		    	writer.write(createKML.buildNdbPlaceMark(ndb));
		    }
	    	
		    writer.write("</Folder>"); 


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
    
    public  void saveKMLFileLandmark(String kmlRelative, Distance dist){
		
 		Writer writer = null;
 						
  		try {
 		    writer = new BufferedWriter(new OutputStreamWriter(
 		          new FileOutputStream(kmlRelative), "utf-8"));
		    writer.write(CreateKML.createHeader(
		    		(selectedAirports != null ?selectedAirports.size():0)+
		    		(UtilityDB.getInstance().getLandmarks() != null?UtilityDB.getInstance().getLandmarks().size():0)+
		    		(selectedCities != null?selectedCities.size():0)+
		    		(selectedMountains != null?selectedMountains.size():0)
		    		));
		    
 		    writer.write("<Folder><name>Landmarks found ("+UtilityDB.getInstance().getLandmarks().size()+") </name>");
 		    for(Landmark landmark:UtilityDB.getInstance().getLandmarks()){
		    	writer.write(createKML.buildLandmarkPlaceMark(landmark,"1"));
 		    }

 		    writer.write("</Folder>"); 

 		    if (dist.isAirport()){
	 		    writer.write("<Folder><name> Airports found ("+selectedAirports.size()+") </name>");
	 		    for(Airport airport:selectedAirports.values()){
			    	writer.write(createKML.buildAirportPlaceMark(airport));
	
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


    public  void saveKMLFileICAO(Map<String, Airport> mapAirports, String kmlRelative, Distance dist){
 		Writer writer = null;
 						
  		try {
 		    writer = new BufferedWriter(new OutputStreamWriter(
 		          new FileOutputStream(kmlRelative), "utf-8"));
 		    
 		    writer.write(CreateKML.createHeader((mapAirports!= null?mapAirports.size():1)));
 		    
			writer.write("<Folder><name> Airports found ("+mapAirports.size()+") </name>");

 		    for(Airport airport:mapAirports.values()){
 		    	//writer.write(placemark.buildXML("fsx_airport"));
		    	writer.write(createKML.buildAirportPlaceMark(airport));

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
        	Utility.getInstance().launchGoogleEarth(new File(kmlFlightPlanFile));
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

	public void setSelectedVors(Map<Integer, Vor> selectedVors) {
		this.selectedVors = selectedVors;
	}

}
