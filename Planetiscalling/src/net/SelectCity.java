package net;

import java.io.File;
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
import com.cfg.file.ManageXMLFile;
import com.cfg.model.Placemark;
import com.model.City;
import com.util.CreateKML;

public class SelectCity implements Info{
	
	private City city;
	private  List<Placemark> placemarks;
	private List <City> cities;
	private Map<String, City> mapCities;

	

	public SelectCity() {
		// TODO Auto-generated constructor stub
	}
	public void selectAll(String search ) {
		city = new City();
		cities = new ArrayList<>();
		mapCities = new TreeMap<>();

		

		String sql = "SELECT id,city, city_ascii, lonx,laty, country,iso2,iso3,region,admin_name,capital, population, region "
				+ "FROM world_city_new ";
		
		if (!"".equals(search)) {
			 sql += search;
		}
		try {
			final PreparedStatement statement = this.connect().prepareStatement(sql);

			try (ResultSet rs = statement.executeQuery()) {
				String lastAirport = "";

				while (rs.next()) {
				    city = new City(rs.getLong("id"), rs.getString("city"), rs.getString("city_ascii"), rs.getDouble("lonx"), rs.getDouble("laty"), rs.getString("country"), rs.getString("iso2"), rs.getString("iso3"),rs.getString("region"), rs.getString("admin_name"), rs.getString("capital").trim(), rs.getLong("population"));
				    cities.add(city);
				    mapCities.put(rs.getString("city"), city);
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
		List<Placemark> placemarks = new ArrayList<>();
		ManageXMLFile manageXMLFile = new ManageXMLFile();
     	String search = "LF LI LE".toUpperCase();	
     	
     	//search = "";

		SelectCity selectCity = new SelectCity();
		selectCity.selectAll(search );
		
	    	String kmlRelative = "data\\city_"+search+".kml";

		 
		 new CreateKML().write(selectCity.getCities(), search, kmlRelative);
		 
		 manageXMLFile.launchGoogleEarth(new File(kmlRelative));
		
	}
	public List<Placemark> getPlacemarks() {
		return placemarks;
	}
	public void setPlacemarks(List<Placemark> placemarks) {
		this.placemarks = placemarks;
	}
	public List<City> getCities() {
		return cities;
	}
	public void setCities(List<City> cities) {
		this.cities = cities;
	}
	public Map<String, City> getMapCities() {
		return mapCities;
	}
	public void setMapCities(Map<String, City> mapCities) {
		this.mapCities = mapCities;
	}


}
