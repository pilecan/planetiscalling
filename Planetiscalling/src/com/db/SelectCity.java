package com.db;

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

import com.backend.CreateKML;
import com.cfg.common.Info;
import com.geo.util.Geoinfo;
import com.model.City;
import com.model.Landcoord;
import com.util.Utility;
import com.util.UtilityMap;

public class SelectCity implements Info{
	
	private City city;
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
				+ "FROM world_city_big ";
		
		if (!"".equals(search)) {
			 sql += search;
		}
		try {
			final PreparedStatement statement = this.connect().prepareStatement(sql);

			try (ResultSet rs = statement.executeQuery()) {
				String lastAirport = "";

				while (rs.next()) {
				    city = new City(rs.getLong("id"), rs.getString("city"), rs.getString("city_ascii"), rs.getDouble("lonx"), rs.getDouble("laty"), rs.getString("country"), rs.getString("iso2"), rs.getString("iso3"),rs.getString("region"), rs.getString("admin_name"), rs.getString("capital"), rs.getLong("population"));
				    cities.add(city);
				    mapCities.put(rs.getString("city").replace(" ", "").toUpperCase()+rs.getLong("population"), city);
				}
				
				

		}
			
			} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();

		}
		

   }
	public void selectStateCity(String search, City citySource ) {
		city = new City();
		cities = new ArrayList<>();
		mapCities = new TreeMap<>();
		
		double distance = 1000;
		double shortDistance = 1000;

		String sql = "SELECT id,city, city_ascii, lonx,laty, country,iso2,iso3,region,admin_name,capital, population, region "
				+ "FROM world_city_new ";
		
		if (!"".equals(search)) {
			 sql += search;
		}
		try {
			final PreparedStatement statement = this.connect().prepareStatement(sql);

			try (ResultSet rs = statement.executeQuery()) {

				while (rs.next()) {
					//System.out.println(Geoinfo.distance(citySource.getLaty(), rs.getDouble("laty"), citySource.getLonx(), rs.getDouble("lonx")));
					if (distance > Geoinfo.distance(citySource.getLaty(), rs.getDouble("laty"), citySource.getLonx(), rs.getDouble("lonx"))) {
						distance = Geoinfo.distance(citySource.getLaty(), rs.getDouble("laty"), citySource.getLonx(), rs.getDouble("lonx"));
						if (shortDistance > distance) {		
							shortDistance = distance;
							city = new City(rs.getLong("id"), rs.getString("city"), rs.getString("city_ascii"), rs.getDouble("lonx"), rs.getDouble("laty"), rs.getString("country"), rs.getString("iso2"), rs.getString("iso3"),rs.getString("region"), rs.getString("admin_name"), rs.getString("capital"), rs.getLong("population"));
							System.out.println(distance);
						}
					}
				}
				
				

		}
			
			System.out.println(citySource.getCityName()+" -  "+citySource.getAdminName());
			System.out.println(city.getCityName()+" -  "+city.getAdminName());
			System.out.println("distance between = "+distance);
			System.out.println("---------------------------------------------------");
			
			} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		

		}
		

   }
	public void selectCountryPoint(double laty, double lonx) {
		city = new City();
		cities = new ArrayList<>();
		mapCities = new TreeMap<>();
		
		double distance = 1000;
		double shortDistance = 1000;

		String sql = "SELECT lonx,laty, country FROM world_city_new ";

		try {
			final PreparedStatement statement = this.connect().prepareStatement(sql);

			try (ResultSet rs = statement.executeQuery()) {

				while (rs.next()) {
					//System.out.println(Geoinfo.distance(citySource.getLaty(), rs.getDouble("laty"), citySource.getLonx(), rs.getDouble("lonx")));
					if (distance > Geoinfo.distance(laty, rs.getDouble("laty"), lonx, rs.getDouble("lonx"))) {
						distance = Geoinfo.distance(laty, rs.getDouble("laty"), lonx, rs.getDouble("lonx"));
						if (shortDistance > distance) {		
							shortDistance = distance;
							city = new City(rs.getLong("id"), rs.getString("city"), rs.getString("city_ascii"), rs.getDouble("lonx"), rs.getDouble("laty"), rs.getString("country"), rs.getString("iso2"), rs.getString("iso3"),rs.getString("region"), rs.getString("admin_name"), rs.getString("capital"), rs.getLong("population"));
							System.out.println(distance);
						}
					}
				}
				
				

		}
			
			System.out.println(city.getCityName()+" -  "+city.getCountry());
			System.out.println("distance between = "+distance);
			System.out.println("---------------------------------------------------");
			
			} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		

		}
		

   }
	public List<String>  selectCountryCity( ) {
		List<String> listCountries = new ArrayList<>();
		
		String sql = "SELECT country FROM world_city_new group by country ";

		try {
			final PreparedStatement statement = this.connect().prepareStatement(sql);

			try (ResultSet rs = statement.executeQuery()) {
				String lastAirport = "";

				while (rs.next()) {
					listCountries.add(rs.getString("country"));
				}
		}
			
			} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();

		}
		
		
		return listCountries;
		

   }
	
	public List<String> selectStateCityNull(String country ) {
		List<String> listCountries = new ArrayList<>();
		
		City city = new City();
		
		
		String sql = "SELECT id,city, city_ascii, lonx,laty, country,iso2,region,admin_name " + 
				"FROM world_city_big  where country = '"+country+"'";
		
		int cpt = 0;

		Connection conn = this.connect();
		
		String adminFound = "";
		String region = null;
		String admin = null;

		try {
			final PreparedStatement statement = conn.prepareStatement(sql);

			try (ResultSet rs = statement.executeQuery()) {

				while (rs.next()) {
				if ("".equals(rs.getString("admin_name")) || "UNKNOW".equals(rs.getString("admin_name")) 
							|| rs.getString("admin_name") == null) {
						adminFound = UtilityMap.getInstance().checkWichState(new Landcoord(rs.getDouble("laty"), rs.getDouble("lonx")));
						if (adminFound != null) {
							cpt++;
							country = adminFound.split("\\|")[0];
							admin = adminFound.split("\\|")[1];
							if ("".equals(rs.getString("region")) || rs.getString("region") == null) {
								region = UtilityDB.getInstance().getMapCountryRegion().get(country);
							} else {
								region = rs.getString("region");
							}
							update(conn, rs.getString("city"),rs.getDouble("laty"), admin, country, region);
							//System.out.println(cpt + " " +rs.getString("country") + " "+rs.getString("city") + " - " + admin+ " - " + region);
						}
					}
				}
			}
			
			
			System.out.println(country+ "changed = "+cpt);
			} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();

		}
		
		
		return listCountries;
		

   }

	private void update(Connection conn, String city, double lat, String admin, String country,String region) {
		String sql = "UPDATE world_city_big SET admin_name = ? , " + "country = ? , " + "region= ?" + " WHERE city = ? and laty = ?";

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

			// set the corresponding param
			pstmt.setString(1, admin);
			pstmt.setString(2, country);
			pstmt.setString(3, region);
			pstmt.setString(4, city);
			pstmt.setDouble(5, lat);
			// update
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	private Connection connect() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(dbPath);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}
	
	
	public static void main(String[] args) {
     	String search = "LF LI LE".toUpperCase();	
     	
     	//search = "";

		SelectCity selectCity = new SelectCity();
		selectCity.selectAll(search );
		
	    	String kmlRelative = "data\\city_"+search+".kml";

		 
		 new CreateKML().write(selectCity.getCities(), search, kmlRelative);
		 
		 Utility.getInstance().launchGoogleEarth(new File(kmlRelative));
		
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
