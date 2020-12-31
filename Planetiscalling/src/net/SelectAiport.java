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
import com.model.Airport;
import com.model.Runway;

/**
 *
 * @author sqlitetutorial.net
 */
public class SelectAiport implements Info{

	private Airport airport;
	private Runway runway;
	private  List<Runway> listRunways;
	private List<Placemark> placemarks; 
	private Map<String, Placemark> mapPlacemark;


	/**
	 * Connect to the test.db database
	 * 
	 * @return the Connection object
	 */
	private Connection connect() {
		// SQLite connection string
		//String url = "jdbc:sqlite:g:\\addons\\777-tools\\Navdatareader\\airport_runway.db";
		//String url = "jdbc:sqlite:data\\airport_runway.db";
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}

	/**
	 * select all rows in the warehouses table
	 */
	public void selectAll(String search) {
		//this.placemarks = placemarks;
		
    	placemarks = new ArrayList<>();


		listRunways = new ArrayList<>();
		airport = new Airport();
		mapPlacemark = new TreeMap<String, Placemark>();
		

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
							placemarks.add(new Placemark(airport.getIdent(), airport.getDescription(), airport.getStyleUrl(), airport.getPoint(), airport.getCoordinates()));
							mapPlacemark.put(airport.getIdent(), new Placemark(airport.getIdent(), airport.getDescription(), airport.getStyleUrl(), airport.getPoint(), airport.getCoordinates()));
							
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
					placemarks.add(new Placemark(airport.getIdent(), airport.getDescription(), airport.getStyleUrl(), airport.getPoint(), airport.getCoordinates()));
					mapPlacemark.put(airport.getIdent(), new Placemark(airport.getIdent(), airport.getDescription(), airport.getStyleUrl(), airport.getPoint(), airport.getCoordinates()));

				}

			}
			
					
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		List<Placemark> placemarks = new ArrayList<>();
		ManageXMLFile manageXMLFile = new ManageXMLFile();
     	String search = "CYVR CYPN".toUpperCase();		
		
    	String kmlRelative = "data\\airport.kml";

		SelectAiport selectAiport = new SelectAiport();
		//selectAiport.setPlacemarks(placemarks);

		 
		selectAiport.selectAll(search);
		placemarks = selectAiport.getPlacemarks();
		
		 manageXMLFile.saveKMLFile(placemarks,kmlRelative);
		 manageXMLFile.launchGoogleEarth(new File(kmlRelative));
	}


	public List<Placemark> getPlacemarks() {
		return placemarks;
	}

	public void setPlacemarks(List<Placemark> placemarks) {
		this.placemarks = placemarks;
	}

	public Map<String, Placemark> getMapPlacemark() {
		return mapPlacemark;
	}

	public void setMapPlacemark(Map<String, Placemark> mapPlacemark) {
		this.mapPlacemark = mapPlacemark;
	}

	public Airport getAirport() {
		return airport;
	}

	public void setAirport(Airport airport) {
		this.airport = airport;
	}

}