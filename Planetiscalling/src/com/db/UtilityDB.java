package com.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.cfg.common.Info;
import com.geo.util.Geoinfo;
import com.model.Airport;
import com.model.City;
import com.model.CityWeather;
import com.model.Runway;


public class UtilityDB extends Thread implements Info {
	
	
	private static UtilityDB instance = new UtilityDB();
	
	private Airport airport;
	private CityWeather cityWeather;
	private Runway runway;
	private  List<Runway> listRunways;
	private Map<String, Airport> mapAirport;
	private List<Airport> airports;
	private List<CityWeather> cityWeathers;

	
	public static UtilityDB getInstance(){
		return instance;
	}
	private Connection connect() {
		// SQLite connection string
		String url = "jdbc:sqlite:g:\\addons\\777-tools\\Navdatareader\\airport_runway.db";
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}
	public void selectAirport(String search) {
		listRunways = new ArrayList<>();
		airports = new ArrayList<>();
		airport = new Airport();
		mapAirport = new TreeMap<String, Airport>();
		
		String sql = "SELECT airport_id, ident, iata, region, name, atis_frequency,tower_frequency,altitude, city, country, state, lonx, laty,"
				+ "runway_name, length, runway_heading, mag_var, width, surface, ils_ident, ils_frequency, ils_name, hour_zone, time_zone  "
				+ "FROM v_airport_runway ";
		if (!"".equals(search)) {
			sql += search;
		}
				
		try {
			final PreparedStatement statement = this.connect().prepareStatement(sql);

			try (ResultSet rs = statement.executeQuery()) {
				String lastAirport = "";

				while (rs.next()) {

					if (!lastAirport.equals(rs.getString("ident"))) {
						airport.setRunways(listRunways);
						if (!"".equals(lastAirport)) {
							mapAirport.put(airport.getIdent(), airport);
							airports.add(airport);
						}
						airport = new Airport();
						listRunways = new ArrayList<>();

						airport.setAirportId(rs.getInt("airport_id"));
						airport.setIdent(rs.getString("ident"));
						airport.setIata(rs.getString("iata"));
						airport.setName(rs.getString("name"));
						airport.setCity(rs.getString("city"));
						airport.setState(rs.getString("state"));
						airport.setCountry(rs.getString("country"));
						airport.setRegion(rs.getString("region"));
						airport.setAltitude(rs.getInt("altitude"));
						airport.setAtisFrequency(rs.getInt("atis_frequency"));
						airport.setTowerFrequency(rs.getInt("tower_frequency"));
						airport.setLonx(rs.getDouble("lonx"));
						airport.setLaty(rs.getDouble("laty"));
						airport.setMagVar(rs.getDouble("mag_var"));
						airport.setHourZone(rs.getDouble("hour_zone"));
						airport.setTimeZone(rs.getString("time_zone"));


						runway = new Runway();
						runway.setRunwayName(rs.getString("runway_name"));
						runway.setIlsName(rs.getString("ils_name"));
						runway.setLength(rs.getInt("length"));
						runway.setWidth(rs.getInt("width"));
						runway.setSurface(rs.getString("surface"));
						runway.setRunwayHeading(rs.getDouble("runway_heading"));
						runway.setMagVar(rs.getDouble("mag_var"));
						runway.setIlsIdent(rs.getString("ils_ident"));
						runway.setIlsName(rs.getString("ils_name"));
						runway.setIlsFrequency(rs.getInt("ils_frequency"));
						listRunways.add(runway);
						lastAirport = rs.getString("ident");
					} else {
						runway = new Runway();
						runway.setRunwayName(rs.getString("runway_name"));
						runway.setIlsName(rs.getString("ils_name"));
						runway.setLength(rs.getInt("length"));
						runway.setWidth(rs.getInt("width"));
						runway.setSurface(rs.getString("surface"));
						runway.setRunwayHeading(rs.getDouble("runway_heading"));
						runway.setMagVar(rs.getDouble("mag_var"));
						runway.setIlsIdent(rs.getString("ils_ident"));
						runway.setIlsName(rs.getString("ils_name"));
						runway.setIlsFrequency(rs.getInt("ils_frequency"));

						listRunways.add(runway);

					}
				}

				airport.setRunways(listRunways);
				if (airport.getIdent() != null) {
					mapAirport.put(airport.getIdent(), airport);
					airports.add(airport);
				}

			}
			
					
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		}
		
	}

	public CityWeather selectCityWeather(City city) {
		cityWeather = null;
		cityWeathers = new ArrayList<CityWeather>();
		Map<Double,CityWeather> orderDistance = new TreeMap<>(); 
		String sql = "SELECT distinct " + 
				"c2.id id, " + 
				"c2.name name, " + 
				"c2.iso2 iso2, " + 
				"c2.lonx lonx, " + 
				"c2.laty laty" + 
				"  FROM world_city_new c1" + 
				"  INNER JOIN  city_weather c2 ON c1.city_ascii = c2.name and c1.iso2 = c2.iso2 "
				+ "and c2.name = '"+city.getCityAscii()+"' and c2.iso2 = '"+city.getIso2()+"'";
		
		System.out.println(sql);
	
		try {
			final PreparedStatement statement = this.connect().prepareStatement(sql);

			try (ResultSet rs = statement.executeQuery()) {

				while (rs.next()) {
					cityWeather = new CityWeather(rs.getLong("id"), rs.getString("name"), rs.getString("iso2") , rs.getDouble("lonx"), rs.getDouble("laty"));
					System.out.println(Geoinfo.distance(city.getLaty(), rs.getDouble("laty"), city.getLonx(), rs.getDouble("lonx"))+" - "+cityWeather.toString());
					cityWeathers.add(cityWeather);
					orderDistance.put(Geoinfo.distance(city.getLaty(), rs.getDouble("laty"), city.getLonx(), rs.getDouble("lonx")), cityWeather);
				}

			}
			
					
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		}
		
		if (orderDistance.size() > 0) {
			 Map.Entry<Double,CityWeather> entry = orderDistance.entrySet().iterator().next();
			cityWeather = entry.getValue();
		}
		
		return cityWeather; 
		
	}

	
	public Airport getAirport() {
		return airport;
	}
	public void setAirport(Airport airport) {
		this.airport = airport;
	}
	public Runway getRunway() {
		return runway;
	}
	public void setRunway(Runway runway) {
		this.runway = runway;
	}
	public List<Runway> getListRunways() {
		return listRunways;
	}
	public void setListRunways(List<Runway> listRunways) {
		this.listRunways = listRunways;
	}
	public Map<String, Airport> getMapAirport() {
		return mapAirport;
	}
	public void setMapAirport(Map<String, Airport> mapAirport) {
		this.mapAirport = mapAirport;
	}
	public List<Airport> getAirports() {
		return airports;
	}
	public void setAirports(List<Airport> airports) {
		this.airports = airports;
	}
	public CityWeather getCityWeather() {
		return cityWeather;
	}
	public void setCityWeather(CityWeather cityWeather) {
		this.cityWeather = cityWeather;
	}
	
}
