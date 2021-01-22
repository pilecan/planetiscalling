package com.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.cfg.common.Info;
import com.model.Mountain;

public class SelectMountain implements Info{
	
	private Mountain mountain;
	private List <Mountain> mountains;
	private Map <String,Mountain> mapMountains;

	public SelectMountain() {
		// TODO Auto-generated constructor stub
	}
	public void selectAll(String search) {
		mountain = new Mountain();
		mountains = new ArrayList<>();
		mapMountains = new TreeMap<>();

		String sql = "SELECT id," + 
				" name," + 
				" alt_name," + 
				" elevation," + 
				" prominence," + 
				" comment," + 
				" author," + 
				" country," + 
				" lonx," + 
				" laty, " + 
				" location," + 
				" type," + 
				" last_activite" + 
				"  FROM mountain_volcano ";
		
	    if (!"".equals(search)) {
	    	sql += search;
		}
	    
		try {
			final PreparedStatement statement = this.connect().prepareStatement(sql);

			try (ResultSet rs = statement.executeQuery()) {

				while (rs.next()) {
				    mountain = new Mountain(rs.getString("id"), rs.getString("name"), rs.getInt("elevation"), rs.getInt("prominence"), 
				    		rs.getString("alt_name"), rs.getString("comment"),
				    		rs.getString("author"), rs.getString("country"), rs.getDouble("lonx"), rs.getDouble("laty"),
				    		rs.getString("location"),rs.getString("type"), rs.getInt("last_activite"));
				    mountains.add(mountain);
				    mapMountains.put(rs.getString("name").replace(" ", "").toUpperCase()+rs.getInt("elevation"), mountain);

				}
				
				

			}
			} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();

		}
		

   }

	private Connection connect() {
		String url = "jdbc:sqlite:g:\\addons\\777-tools\\Navdatareader\\airport_runway.db";
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}
	
	
	public static void main(String[] args) {
     	String search = "usa";	
     	
     	//search = "";

		SelectMountain selectMountain = new SelectMountain();
		selectMountain.selectAll(search );
		
		String kmlRelative = "data\\city_"+search+".kml";
		 
	    // new WriteMountainKML().write(selectMountain.getMountains(), search, kmlRelative);
	     
		// new CreateKML().write(selectCity.getCities(), search, kmlRelative);

		 
		// Utility.getInstance().launchGoogleEarth(new File(kmlRelative));
		
	}
	public List<Mountain> getMountains() {
		return mountains;
	}
	public void setMountains(List<Mountain> cities) {
		this.mountains = cities;
	}
	public Map<String, Mountain> getMapMountains() {
		return mapMountains;
	}
	public void setMapMountains(Map<String, Mountain> mapMountains) {
		this.mapMountains = mapMountains;
	}


}
