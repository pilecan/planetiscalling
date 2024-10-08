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
import com.model.Airport;
import com.model.Landcoord;
import com.model.Runway;
import com.util.UtilityMap;

/**
 *
 * @author sqlitetutorial.net
 */
public class SelectAirport implements Info{

	private Airport airport;
	private Runway runway;
	private  List<Runway> listRunways;
	private Map<String, Airport> mapAirport;
	private List<Airport> airports;

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

	public void select(String search) {
		listRunways = new ArrayList<>();
		airports = new ArrayList<>();
		airport = new Airport();
		mapAirport = new TreeMap<String, Airport>();
		
		String sql = "SELECT airport_id, ident, iata, region, name, atis_frequency,tower_frequency,altitude, city, country, state, lonx, laty,"
				+ "runway_name, length, runway_heading, mag_var, width, surface, ils_ident, ils_frequency, ils_name  "
				+ "FROM v_view_2 ";
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
						try {
							airport.setName(rs.getString("name").replace("&", "and"));
						} catch (Exception e) {}
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
			e1.printStackTrace();
		}
		
		
	}
	public void selectStateNull() {
		listRunways = new ArrayList<>();
		airports = new ArrayList<>();
		airport = new Airport();
		mapAirport = new TreeMap<String, Airport>();
		
		int cpt = 0;
		int cptCountry = 0;
		String namefound = null;
		String region = null;
		String country = null;
		String state = null;
		
		String sql = "SELECT airport_id, ident, iata, region, name, atis_frequency,tower_frequency,altitude, city, country, state, lonx, laty "
				+ "FROM airport ";
			
		Connection conn = this.connect();
		try {
			final PreparedStatement statement = conn.prepareStatement(sql);

			try (ResultSet rs = statement.executeQuery()) {
				String lastAirport = "";

				while (rs.next()) {

					if ("".equals(rs.getString("state")) || rs.getString("state") == null 
							|| "".equals(rs.getString("country")) || rs.getString("country") == null) {
						namefound = UtilityMap.getInstance().checkWichState(new Landcoord(rs.getDouble("laty"), rs.getDouble("lonx")));
						if (namefound != null) {
							
							country = namefound.split("\\|")[0];
							state = namefound.split("\\|")[1];
							
							if ("".equals(rs.getString("region"))) {
								region = UtilityDB.getInstance().getMapCountryRegion().get(country);
								if ("Alaska".equals(state)) {
									region = "PA";
								}
							} else {
								region = rs.getString("region").substring(0,2);
							}
							
							if (region == null) {
								region = rs.getString("ident").substring(0,2);

							}
							
								update(conn, rs.getString("ident"), state, country, region);
								cpt++;
							    System.out.println(cpt+" - "+ rs.getString("ident") + " " + rs.getString("name")+" "+country+" "+state+ " ("+region +")" );

						}
					}
				}

				System.out.println("total = "+cpt );
			}			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}
	
	private void update(Connection conn, String ident, String state, String country,String region) {
		String sql = "UPDATE airport SET state = ? , " + "country = ? , " + "region= ?" + "WHERE ident = ?";

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

			// set the corresponding param
			pstmt.setString(1, state);
			pstmt.setString(2, country);
			pstmt.setString(3, region);
			pstmt.setString(4, ident);
			// update
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}


	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
	}


	
	public Airport getAirport() {
		return airport;
	}

	public void setAirport(Airport airport) {
		this.airport = airport;
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

	

}