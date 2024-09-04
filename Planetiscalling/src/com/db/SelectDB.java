package com.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.JComboBox;

import com.cfg.common.Info;
import com.model.City;
import com.model.TimeZones;

/**
 *
 * @author sqlitetutorial.net
 */
public class SelectDB implements Info{

	public SelectDB() {
		super();
		// TODO Auto-generated constructor stub
	}

	static Map<String, List<String>> mapState = new TreeMap<>();
	private Map <String,City> mapCities = new TreeMap<>();
	private Set<String> countryState;
	private Set<String> countryMountain;
	
	static Map<String, Set<String>> mapCountryAirport = new TreeMap<>();
	private Map<String, Set<String>> mapCountry = new TreeMap<>();
	private Map<String, Set<String>> mapCountryMountain = new TreeMap<>();
	private List<String> listState = new ArrayList<>();
	private List<String> listMountain = new ArrayList<>();
	
	private TimeZones timeZones;


	/**
	 * Connect to the test.db database
	 * 
	 * @return the Connection object
	 */
	private Connection connect() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(dbPath);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}
	public void selectTimeZone(String where) {
		timeZones = null;
		String sql = "SELECT abbr, name, hour from time_zone where name = '"+where+"'";
				
		try {
			final PreparedStatement statement = this.connect().prepareStatement(sql);

			try (ResultSet rs = statement.executeQuery()) {
				int cpt = 0;
				String lastCountry = "";
					while (rs.next()) {
						timeZones = new TimeZones(rs.getString("abbr"), rs.getString("name"),rs.getString("hour"));
					
				}
				
				mapCountryMountain.put(lastCountry, countryMountain);

				
			}
			
					
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}

	public void selectMountainTable() {
		countryMountain = new TreeSet<String>();
		mapCountryMountain = new TreeMap<>();

		String sql = "SELECT distinct country, name from mountain_volcano order by country, name";
				
		try {
			final PreparedStatement statement = this.connect().prepareStatement(sql);

			try (ResultSet rs = statement.executeQuery()) {
				int cpt = 0;
				String lastCountry = "";
					while (rs.next()) {
					
					if (!lastCountry.equals(rs.getString("country"))) {
						if (!"".equals(lastCountry)) {
							mapCountryMountain.put(lastCountry, countryMountain);
						}
						lastCountry = rs.getString("country");
						listMountain = new ArrayList<>();
						listMountain.add(" All");
						countryMountain = new TreeSet<String>();
						countryMountain.add(" All");

					}
					
					listMountain.add(rs.getString("name"));
					countryMountain.add(rs.getString("name"));
				}
				
				mapCountryMountain.put(lastCountry, countryMountain);

				
			}
			
					
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}

	public void selectCityTable() {
		
		countryState = new TreeSet<String>();
		listState = new ArrayList<>();


		String sql = "SELECT country, admin_name from world_city_new order by country, admin_name";
		//where country in ('United States','Canada','France')
				
		try {
			final PreparedStatement statement = this.connect().prepareStatement(sql);

			try (ResultSet rs = statement.executeQuery()) {
				int cpt = 0;
				String lastCountry = "";
					while (rs.next()) {
					
					if (!lastCountry.equals(rs.getString("country"))) {
						if (!"".equals(lastCountry)) {
							mapCountry.put(lastCountry, countryState);
						}
						lastCountry = rs.getString("country");
						countryState = new TreeSet<String>();
						countryState.add(" All");

					}
					
					countryState.add(rs.getString("admin_name"));
				}
				mapCountry.put(lastCountry, countryState);
			}
			
					
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}
	
	public void selectAirportTableNew() {
		
		countryState = new TreeSet<String>();
		listState.add(" All");
		String sql = "SELECT country, state from v_airport_runway order by country, state";
				
		try {
			final PreparedStatement statement = this.connect().prepareStatement(sql);

			try (ResultSet rs = statement.executeQuery()) {
				int cpt = 0;
				String lastCountry = "";
				while (rs.next()) {


					
					if (rs.getString("country") != null ) {
						if (!lastCountry.equals(rs.getString("country"))) {
							if ("France".equals(rs.getString("country"))) {
							//	System.out.println(rs.getString("state"));
							}
							if (!"".equals(lastCountry)) {
								mapCountryAirport.put(lastCountry, countryState);
							}
							lastCountry = rs.getString("country");
							countryState = new TreeSet<String>();
							countryState.add(" All");

						}
						

						if (rs.getString("state") != null) {
							countryState.add(rs.getString("state"));
						}
						cpt++;
						
					}
					
				}
				
				mapCountryAirport.put(lastCountry, countryState);
				
			}
			
					
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		}
		
		
	}
	
	
	public List<String> selectDBCity(String sql) {

		mapCities = new TreeMap<String, City>();
	//	mapCities.put(" All", null);

		try {
			final PreparedStatement statement = this.connect()
					.prepareStatement(sql);

			try (ResultSet rs = statement.executeQuery()) {
				int cpt = 0;
				while (rs.next()) {
					if (rs.getString("city") != null) {
						// setCities.add(rs.getString("city"));
						mapCities.put(rs.getString("city"), new City(rs.getString("city"), rs.getString("country"),
								rs.getDouble("lonx"), rs.getDouble("laty")));
					}

					cpt++;

				}

			}

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		//		return new ArrayList<String>(mapCities.keySet());

	//	setCities = mapCities.keySet(); 


		return new ArrayList<String>(mapCities.keySet());
	}

	static public String[] getCountry() {
		
		//String[] countries = new String[mapAll.size()];
		Set<String> keys = mapCountryAirport.keySet();
		
		String[] countries= keys.toArray(new String[keys.size()]);

		return countries;
		
	}
	
	public String[] getCountryCity() {
		
		Set<String> keys = mapCountry.keySet();
		
		String[] countries= keys.toArray(new String[keys.size()]);

		return countries;
		
	}
	public String[] getCountryMountain() {
		
		Set<String> keys = mapCountryMountain.keySet();
		
		String[] countries= keys.toArray(new String[keys.size()]);

		return countries;
		
	}
	public String[] getStateCity(String country) {
		
		Set<String> keys = new TreeSet<String>(mapCountry.get(country));
	//	keys.add(" All");

		return keys.toArray(new String[keys.size()]);
		
	}
	public String[] getMountain(String country) {
		
		Set<String> keys = new TreeSet<String>(mapCountryMountain.get(country));
	//	keys.add(" All");

		return keys.toArray(new String[keys.size()]);
		
	}
	

	static public String[] getStates(Object object) {
		Set<String> keys = new TreeSet<String>(mapCountryAirport.get(object));
		
		return keys.toArray(new String[keys.size()]);
	}
	
	public List<String> getCities(String table, Object country, Object state, int nbState) {
		String sql = table;
		if (nbState == 1) {
		    sql += " where country = '"+((String) country).replace("'", "''") +"' ";
		} else if (nbState > 1) {
			sql += " where country = '"+((String) country).replace("'", "''") +"' and state = '"+((String) state).replace("'", "''") +"' ";
			
			if ("world_city_new".equals(table)) {
				sql = sql.replace("state", "admin_name");
			}
		}

		return selectDBCity("SELECT city, country, lonx, laty from " + sql + " order by city");

	} 
	
	public int setComboCity(String table,JComboBox<String> comboCity, Object country, Object state, int nbState) {
		int cpt = 0;
		for (String city: getCities(table, country,state,nbState)) {
			comboCity.addItem(city);
			cpt++;
		}
		
		return cpt;
		
	}

	

	public static Map<String, Set<String>> getMapCountryAirport() {
		return mapCountryAirport;
	}
	

	public Map<String, City> getMapCities() {
		return mapCities;
	}

	public void setMapCities(Map<String, City> mapCities) {
		this.mapCities = mapCities;
	}
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		SelectDB selectDB = new SelectDB();
		
		selectDB.selectCityTable();
		
		System.out.println("countryState = "+selectDB.getMapCountry().size());
		
		
        Iterator<Map.Entry<String, Set<String>>> itr = selectDB.getMapCountry().entrySet().iterator(); 

	      while(itr.hasNext()) 
	        { 
	             Map.Entry<String, Set<String>> entry = itr.next(); 
	             System.out.println("Key = " + entry.getKey() +  
	                                 ", Value = " + entry.getValue().size()); 
	        } 
	      
	      
/*		   Iterator mapCountry = mapAll.entrySet().iterator();
		    while (mapCountry.hasNext()) {
		        Map.Entry pair1 = (Map.Entry)mapCountry.next();
		        
		        mapState = new TreeMap<>();
		        mapState = (Map<String, List<String>>) pair1.getValue();
		        
		        System.out.println("->"+pair1.getKey()+"-"+mapState.size());
		    }
		    
		    String[] countries = getCountry();
		    
		       System.out.println(mapAll.size());
*/
	}

	public Set<String> getCountryState() {
		return countryState;
	}

	public void setCountryState(Set<String> countryState) {
		this.countryState = countryState;
	}

	public Map<String, Set<String>> getMapCountry() {
		return mapCountry;
	}

	public void setMapCountry(Map<String, Set<String>> mapCountry) {
		this.mapCountry = mapCountry;
	}

	public Map<String, Set<String>> getMapCountryMountain() {
		return mapCountryMountain;
	}

	public void setMapCountryMountain(Map<String, Set<String>> mapCountryMountain) {
		this.mapCountryMountain = mapCountryMountain;
	}
	public TimeZones getTimeZones() {
		return timeZones;
	}
	public void setTimeZones(TimeZones timeZones) {
		this.timeZones = timeZones;
	}

	

}