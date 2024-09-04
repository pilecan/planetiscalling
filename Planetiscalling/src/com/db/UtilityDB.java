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
import com.model.Boundingbox;
import com.model.City;
import com.model.CityWeather;
import com.model.CoordinatesDTO;
import com.model.Landcoord;
import com.model.Landmark;
import com.model.Mountain;
import com.model.Ndb;
import com.model.Runway;
import com.model.StateCoord;
import com.model.Vor;
import com.util.Util;
import com.util.UtilityMap;

public class UtilityDB extends Thread implements Info {

	private static UtilityDB instance = new UtilityDB();
	private Airport airport;
	private CityWeather cityWeather;
	private Runway runway;
	private List<Runway> listRunways;
	private Map<String, Airport> mapAirport;
	private List<Airport> airports;
	private List<CityWeather> cityWeathers;
	private List<Landmark> landmarks;
	private List<Landcoord> landcoords;
	private List<City> cities;
	private Map<String, Landmark> mapLandmark;
	private Map<String, List<Landmark>> groupLandmark;
	private Map<String, City> mapCities;
	private Vor vor;
	private List<Vor> vors;
	private Map<String, Vor> mapVors;
	private Ndb ndb;
	private List<Ndb> ndbs;
	private Map<String, Ndb> mapNdb;
	private Map<String, StateCoord> mapStateCoords;
	private Map <String, String> mapCountryRegion; 

	private boolean isInitAll;

	private String province;

	private Mountain mountain;
	private List<Mountain> mountains;
	private Map<String, Mountain> mapMountains;

	public static UtilityDB getInstance() {
		return instance;
	}

	private Connection connect() {
		// SQLite connection string
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(dbPath);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}

	private Connection connectNavdata() {
		// SQLite connection string
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(dbNavPath);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}

	/*
	  INSERT INTO info.airport_update
	  
	  select b.* FROM info.airport a left join navdata.airport b on a.ident =
	  b.ident where b.ident is null and b.ident is not null union all select b.*
	  from navdata.airport b left join info.airport a on a.ident = b.ident where
	  a.ident is null and b.ident is not null
	  
	*/ 

	public void updateAirportState() {
		groupLandmark = new TreeMap<>();
		Landmark landmark;

		selectAirport("");
		selectCity("");
		Map<String, Airport> mapAirFound = new HashMap<>();
		double distance = 0;

		int cptState = 0;
		int cptCountry = 0;
		int cptCityfound = 0;
		int cptCityNotfound = 0;
		boolean foundState = false;
		boolean foundCountry = false;

		String state = "";
		String country = "";

		for (Airport airport : airports) {

			if (airport.getState() == null) {
				// System.out.println(airport.toString());
				cptState++;
				try {
					City city2 = selectOneCity("where city_ascii ='" + airport.getCity() + "' and country = '"
							+ airport.getCountry() + "'");
					if (city2.getAdminName() != null) {
						cptCityfound++;
						update(airport.getIdent(), city2.getAdminName(), airport.getCountry());
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					cptCityNotfound++;
				}
			}

		}

		System.out.println("cptCityfound = " + cptCityfound);
		System.out.println("cptCityNotfound = " + cptCityNotfound);
		System.out.println("cptState = " + cptState);
		System.out.println("cptCountry = " + cptCountry);
		System.out.println("mapAirFound = " + mapAirFound.size());

	}

	private void update(String ident, String state, String country) {
		String sql = "UPDATE airport SET state = ? , " + "country = ? " + "WHERE ident = ?";

		try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			// set the corresponding param
			pstmt.setString(1, state);
			pstmt.setString(2, country);
			pstmt.setString(3, ident);
			// update
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void updateNewAirport() {
		int cptGood = 0;
		int cptBad = 0;
		String sql = "INSERT INTO airport " + "" + "select b.* "
				+ "FROM airport a left join airport_update b on a.ident = b.ident "
				+ "where b.ident is null and b.ident is not null " + "union all " + "select  b.* "
				+ "from  airport_update b left join airport a on a.ident = b.ident "
				+ "where a.ident is null and b.ident is not null" + "";

		try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.executeUpdate();
			cptGood++;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			cptBad++;
		}

		System.out.println("cptGood " + cptGood);
		System.out.println("cptBad  " + cptBad);
	}

	public void selectLandmark(String search) {
		landmarks = new ArrayList<>();
		mapLandmark = new TreeMap<>();
		Landmark landmark;

		String sql = "SELECT cgn_id, geo_name, geo_term, category,code, laty, lonx, admin, decision_date, source,"
				+ "location, language, syllabic, toponomic, revelance " + "FROM cgn_all_canada ";

		if (!"".equals(search)) {
			sql += search;
		}
		try {
			final PreparedStatement statement = this.connect().prepareStatement(sql);

			try (ResultSet rs = statement.executeQuery()) {

				while (rs.next()) {
					landmark = new Landmark(rs.getString("cgn_id"), rs.getString("geo_name"), rs.getString("geo_term"),
							rs.getString("category"), rs.getString("code"), rs.getString("admin"), rs.getDouble("lonx"),
							rs.getDouble("laty"), rs.getString("decision_date"), rs.getString("source"),
							rs.getString("location"), rs.getString("language"), rs.getString("syllabic"),
							rs.getString("toponomic"), rs.getString("revelance"));
					landmarks.add(landmark);
					mapLandmark.put(rs.getString("geo_name"), landmark);
				}

			}

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			// e1.printStackTrace();
		}

	}

	
	public void UpdateUnknow(Connection connect, double laty, double lonx, String admin) {
		String sql = "update world_city_big set admin_name ='"+admin+"' where laty="+laty+" and lonx="+lonx+" and admin_name = 'UNKNOW'";

		ResultSet rs = null;
		PreparedStatement statement = null;

		try {
			statement = connect.prepareStatement(sql);
			statement.executeUpdate();

		} catch (Exception a) {
			a.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}
		

	
	
	public void selectUnknow(String country) {
		String sql = "SELECT city, admin_name, laty, lonx FROM world_city_big where country in ("+country+") and admin_name = 'UNKNOW'";

		UtilityDB.getInstance().selectPolygone();

		
		Connection connect = this.connect();
		try {
			final PreparedStatement statement = connect.prepareStatement(sql);

			try (ResultSet rs = statement.executeQuery()) {
				while (rs.next()) {

					String admin = UtilityMap.getInstance().checkWichState(new Landcoord(rs.getDouble("laty"), rs.getDouble("lonx")));

					if (admin == null) {
					   admin = UtilityMap.getInstance().checkWichProvince(new Landcoord(rs.getDouble("laty"), rs.getDouble("lonx")));
					}
					if (admin == null) {
	/*					System.out.println(rs.getString("city") + " - " + rs.getDouble("laty") + ","
								+ rs.getDouble("lonx") + " Not found!");
*/
					}
					
					if (admin != null) {
						System.out.println(rs.getString("city") + " - " + rs.getDouble("laty") + ","
								+ rs.getDouble("lonx") + " Found: "+admin );
						UpdateUnknow(connect, rs.getDouble("laty") , rs.getDouble("lonx") , admin); 
					}


				}

			}
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
	}
	public void selectPolygone() {
		mapStateCoords = new HashMap<>();
		
		StateCoord stateCoord = null;
		String lastState = "";
		String lastCountry = null;
		CoordinatesDTO coordinatesDTO;


		String sql = "SELECT state," + 
				"       region," + 
				"       description," + 
				"       laty,lonx" + 
				"  FROM state_coord coord, state_coords coords  " + 
				"      where coord.id = coords.state_id order by region";

		try {
			final PreparedStatement statement = this.connect().prepareStatement(sql);

			try (ResultSet rs = statement.executeQuery()) {

				while (rs.next()) {
					if (!rs.getString("state").equals(lastState)) {
						if (!"".equals(lastState)) {
							mapStateCoords.put(lastState, stateCoord);
						}
						stateCoord = new StateCoord(rs.getString("region"), rs.getString("state"), rs.getString("description"));
						stateCoord.setCoordinatesDTOs(new CoordinatesDTO(rs.getDouble("laty"), rs.getDouble("lonx")));
						lastState = rs.getString("state");
					} else {
						stateCoord.setCoordinatesDTOs(new CoordinatesDTO(rs.getDouble("laty"), rs.getDouble("lonx")));
					}
					

				}
				mapStateCoords.put(lastState, stateCoord);
			}
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	
	public void selectLandmarkInBox(String search) {
		this.mapLandmark = new TreeMap<String, Landmark>();
		Landmark landmark;

		String sql = "SELECT cgn_id, geo_name, geo_term, category,code, laty, lonx, admin, decision_date, source,"
				+ "location, language, syllabic, toponomic, revelance " + "FROM cgn_all_canada ";

		if (!"".equals(search)) {
			sql += search;
		}
		try {
			final PreparedStatement statement = this.connect().prepareStatement(sql);

			try (ResultSet rs = statement.executeQuery()) {

				while (rs.next()) {
					landmark = new Landmark(rs.getString("cgn_id"), rs.getString("geo_name"), rs.getString("geo_term"),
							rs.getString("category"), rs.getString("code"), rs.getString("admin"), rs.getDouble("lonx"),
							rs.getDouble("laty"), rs.getString("decision_date"), rs.getString("source"),
							rs.getString("location"), rs.getString("language"), rs.getString("syllabic"),
							rs.getString("toponomic"), rs.getString("revelance"));

					if (Boundingbox.getInstance().isInside(rs.getDouble("laty"), rs.getDouble("lonx"))) {
						mapLandmark.put(rs.getString("geo_name"), landmark);
					}
				}
			}

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			// e1.printStackTrace();
		}

	}

	public void selectCityInBox(String search) {
		this.mapCities = new TreeMap<String, City>();
		City city;

		String sql = "SELECT id,city, city_ascii, lonx,laty, country,iso2,iso3,region,admin_name,capital, population, region "
				+ "FROM world_city_big ";
		if (!"".equals(sql)) {
			sql += search;
		}
		try {
			final PreparedStatement statement = this.connect().prepareStatement(sql);

			try (ResultSet rs = statement.executeQuery()) {

				while (rs.next()) {
					city = new City(rs.getLong("id"), rs.getString("city"), rs.getString("city_ascii"),
							rs.getDouble("lonx"), rs.getDouble("laty"), rs.getString("country"), rs.getString("iso2"),
							rs.getString("iso3"), rs.getString("region"), rs.getString("admin_name"),
							rs.getString("capital"), rs.getLong("population"));

					if (Boundingbox.getInstance().isInside(rs.getDouble("laty"), rs.getDouble("lonx"))) {
					    mapCities.put(rs.getString("city").replace(" ", "").toUpperCase()+rs.getLong("population"), city);

					}
				}
			}

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			// e1.printStackTrace();
		}

	}

	public String selectNearestLandmark(Double laty, Double lonx) {
		groupLandmark = new TreeMap<>();
		Landmark landmark;

		String sql = "SELECT laty, lonx, admin FROM cgn_all_canada ";

		try {
			final PreparedStatement statement = this.connect().prepareStatement(sql);

			try (ResultSet rs = statement.executeQuery()) {
				int cpt = 0;
				double distance = 0;
				while (rs.next()) {
					distance = Geoinfo.distance(laty, rs.getDouble("laty"), lonx, rs.getDouble("lonx"));

					if (distance < 10) {
						// System.out.println( rs.getString("admin"));
						return rs.getString("admin");
					}

				}

			}

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			// e1.printStackTrace();
		}

		return null;

	}

	public List<String> selectCombo(String sql) {
		List<String> list = new ArrayList<>();

		try {
			final PreparedStatement statement = this.connect().prepareStatement(sql);

			try (ResultSet rs = statement.executeQuery()) {

				while (rs.next()) {
					list.add(rs.getString(1));
				}

			}

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			// e1.printStackTrace();
		}

		return list;

	}

	public void selectAirport(String search) {
		listRunways = new ArrayList<>();
		airports = new ArrayList<>();
		airport = new Airport();
		mapAirport = new TreeMap<String, Airport>();

		String sql = "SELECT airport_id, ident, iata, region, name, atis_frequency,tower_frequency,altitude, city, country, state, lonx, laty,"
				+ "runway_name, length, runway_heading, mag_var, width, surface, ils_ident, ils_frequency, ils_name "
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
						try {
							airport.setName(rs.getString("name").replace("&", "and"));
						} catch (Exception e) {
						}
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
			// e1.printStackTrace();
		}

	}

	public List<City> selectCity(String search) {

		City city = new City();
		cities = new ArrayList<>();
		mapCities = new TreeMap<>();

		String sql = "SELECT id,city, city_ascii, lonx,laty, country,iso2,iso3,region,admin_name,capital, population, region "
				+ "FROM world_city_big ";
		if (!"".equals(sql)) {
			sql += search;
		}

		try {
			final PreparedStatement statement = this.connect().prepareStatement(sql);

			try (ResultSet rs = statement.executeQuery()) {
				String lastAirport = "";

				while (rs.next()) {
					city = new City(rs.getLong("id"), rs.getString("city"), rs.getString("city_ascii"),
							rs.getDouble("lonx"), rs.getDouble("laty"), rs.getString("country"), rs.getString("iso2"),
							rs.getString("iso3"), rs.getString("region"), rs.getString("admin_name"),
							rs.getString("capital"), rs.getLong("population"));
					cities.add(city);
					mapCities.put(rs.getString("city").replace(" ", "").toUpperCase() + rs.getLong("population"), city);
				}

			}

		} catch (SQLException e1) {
			// TODO Auto-generated catch bloc
			e1.printStackTrace();

		}

		return cities;
	}

	public City selectOneCity(String search) {
		City city = new City();

		String sql = "SELECT id,city, city_ascii, lonx,laty, country,iso2,iso3,region,admin_name,capital, population, region "
				+ "FROM world_city_new ";
		if (!"".equals(sql)) {
			sql += search;
		}

		try {
			final PreparedStatement statement = this.connect().prepareStatement(sql);

			try (ResultSet rs = statement.executeQuery()) {
				String lastAirport = "";

				while (rs.next()) {
					city = new City(rs.getLong("id"), rs.getString("city"), rs.getString("city_ascii"),
							rs.getDouble("lonx"), rs.getDouble("laty"), rs.getString("country"), rs.getString("iso2"),
							rs.getString("iso3"), rs.getString("region"), rs.getString("admin_name"),
							rs.getString("capital").trim(), rs.getLong("population"));
				}

			}

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();

		}

		return city;
	}

	public List<Mountain> selectMountain(String search) {
		mountain = new Mountain();
		mountains = new ArrayList<>();
		mapMountains = new TreeMap<>();

		String sql = "SELECT id," + " name," + " alt_name," + " elevation," + " prominence," + " comment," + " author,"
				+ " country," + " lonx," + " laty, " + " location," + " type," + " last_activite"
				+ "  FROM mountain_volcano ";

		if (!"".equals(search)) {
			sql += search;
		}

		try {
			final PreparedStatement statement = this.connect().prepareStatement(sql);

			try (ResultSet rs = statement.executeQuery()) {

				while (rs.next()) {
					mountain = new Mountain(rs.getString("id"), rs.getString("name"), rs.getInt("elevation"),
							rs.getInt("prominence"), rs.getString("alt_name"), rs.getString("comment"),
							rs.getString("author"), rs.getString("country"), rs.getDouble("lonx"), rs.getDouble("laty"),
							rs.getString("location"), rs.getString("type"), rs.getInt("last_activite"));
					mountains.add(mountain);
					mapMountains.put(rs.getString("name").replace(" ", "").toUpperCase() + rs.getInt("elevation"),
							mountain);

				}

			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();

		}

		return mountains;

	}

	public List<Vor> selectVor(String search) {
		vor = new Vor();
		vors = new ArrayList<>();
		mapVors = new TreeMap<>();

		String sql = "SELECT vor_id," + "       file_id," + "       ident," + "       name," + "       region,"
				+ "       airport_id," + "       type," + "       frequency," + "       channel," + "       range,"
				+ "       mag_var," + "       dme_only," + "       dme_altitude," + "       dme_lonx,"
				+ "       dme_laty," + "       altitude," + "       lonx," + "       laty" + "  FROM vor ";

		if (!"".equals(search)) {
			sql += search;

		}
		try {
			final PreparedStatement statement = this.connect().prepareStatement(sql);

			try (ResultSet rs = statement.executeQuery()) {
				String lastAirport = "";

				while (rs.next()) {
					vor = new Vor(rs.getInt("vor_id"), rs.getInt("file_id"), rs.getString("ident"),
							rs.getString("name"), rs.getString("region"), rs.getInt("airport_id"), rs.getString("type"),
							rs.getInt("frequency"), rs.getString("channel"), rs.getInt("range"),
							rs.getDouble("mag_var"), rs.getInt("dme_only"), rs.getInt("dme_altitude"),
							rs.getDouble("dme_lonx"), rs.getDouble("dme_laty"), rs.getInt("altitude"),
							rs.getDouble("lonx"), rs.getDouble("laty"));

					vors.add(vor);
					mapVors.put(rs.getString("ident") + rs.getString("name").replace(" ", ""), vor);
				}

			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();

		}

		return vors;
	}

	public List<Ndb> selectNdb(String search) {
		ndb = new Ndb();
		ndbs = new ArrayList<>();
		mapNdb = new TreeMap<>();

		String sql = "SELECT ndb_id," + "       file_id," + "       ident," + "       name," + "       region,"
				+ "       airport_id," + "       type," + "       frequency," + "       range," + "       mag_var,"
				+ "       altitude," + "       lonx," + "       laty" + "  FROM ndb " + " ";

		if (!"".equals(search)) {
			sql += search;

		}
		try {
			final PreparedStatement statement = this.connect().prepareStatement(sql);

			try (ResultSet rs = statement.executeQuery()) {

				while (rs.next()) {
					ndb = new Ndb(rs.getInt("ndb_id"), rs.getInt("file_id"), rs.getString("ident"),
							rs.getString("name"), rs.getString("region"), rs.getInt("airport_id"), rs.getString("type"),
							rs.getInt("frequency"), rs.getInt("range"), rs.getDouble("mag_var"), rs.getInt("altitude"),
							rs.getDouble("lonx"), rs.getDouble("laty"));

					ndbs.add(ndb);
					mapNdb.put(rs.getString("ident") + rs.getString("name").replace(" ", ""), ndb);
				}

			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();

		}

		return ndbs;
	}

	public CityWeather selectCityWeather(City city) {
		cityWeather = null;
		cityWeathers = new ArrayList<CityWeather>();
		Map<Double, CityWeather> orderDistance = new TreeMap<>();
		String sql = "SELECT distinct c2.id id, c2.name name,  c2.iso2 iso2, c2.lonx lonx, c2.laty laty FROM world_city_new c1 INNER JOIN  city_weather c2 ON c1.city_ascii = c2.name and c1.iso2 = c2.iso2 and c2.name = '"
				+ city.getCityAscii() + "' and c2.iso2 = '" + city.getIso2() + "'";

		// System.out.println(sql);

		try {
			final PreparedStatement statement = this.connect().prepareStatement(sql);

			try (ResultSet rs = statement.executeQuery()) {

				while (rs.next()) {
					cityWeather = new CityWeather(rs.getLong("id"), rs.getString("name"), rs.getString("iso2"),
							rs.getDouble("lonx"), rs.getDouble("laty"));
					cityWeathers.add(cityWeather);
					orderDistance.put(Geoinfo.distance(city.getLaty(), rs.getDouble("laty"), city.getLonx(),
							rs.getDouble("lonx")), cityWeather);
				}

			}

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			// e1.printStackTrace();
		}

		if (orderDistance.size() > 0) {
			Map.Entry<Double, CityWeather> entry = orderDistance.entrySet().iterator().next();
			cityWeather = entry.getValue();
		}

		return cityWeather;

	}

	public String[] getCountryCgn() {
		String[] countries = { "Canada" };
		return countries;
	}

	public String[] getProvinces() {
		List<String> list = selectCombo("SELECT admin FROM cgn_all_canada group by admin");
		return list.toArray(new String[list.size()]);
	}

	public String[] getGeoterm(String admin) {
		List<String> list = selectCombo("SELECT geo_term FROM cgn_all_canada where admin = '" + admin
				+ "' group by geo_term order by geo_term ");
		return list.toArray(new String[list.size()]);
	}

	public String[] getGeoname(String admin, String geoTerm) {
		List<String> list = selectCombo("SELECT geo_name FROM cgn_all_canada  where admin = '" + admin + "' and geo_term = '" + geoTerm + "'");
		list.add(0, "All");

		return list.toArray(new String[list.size()]);
	}

	public String[] getCountryCity() {
		List<String> list = selectCombo("SELECT distinct country from world_city_big where country is not null order by country");
		return list.toArray(new String[list.size()]);
	}

	public String[] getSateCity(String country) {
		List<String> list = selectCombo("SELECT distinct admin_name from world_city_big where country is not null and country = '"+country+"' order by admin_name");
		return list.toArray(new String[list.size()]);
	}
	
	public String[] getCityCity(String country, String admin) {
		List<String> list = selectCombo("SELECT distinct city_ascii from world_city_big where country is not null and country = '"+country+"' and admin_name = '"+admin+"' order by city_ascii");
		return list.toArray(new String[list.size()]);
	}
	
	/**
	 * 
	 * @param laty
	 * @param lonx
	 */
	public String selectCountryPoint(double laty, double lonx) {

		double distance = 1000;
		double shortDistance = 1000;

		String country = "";

		String sql = "SELECT lonx,laty, country FROM world_city_new ";

		try {
			final PreparedStatement statement = this.connect().prepareStatement(sql);

			try (ResultSet rs = statement.executeQuery()) {

				while (rs.next()) {
					if (distance > Geoinfo.distance(laty, rs.getDouble("laty"), lonx, rs.getDouble("lonx"))) {
						distance = Geoinfo.distance(laty, rs.getDouble("laty"), lonx, rs.getDouble("lonx"));
						if (shortDistance > distance) {
							shortDistance = distance;
							country = rs.getString("country");
						}
					}
				}

			}

			/*
			 * System.out.println(country); System.out.println("distance = "+distance);
			 * System.out.println("---------------------------------------------------");
			 */
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();

		}
		return country;
	}

	public String selectRegion(String region) {
		String country = "";

		String sql = "SELECT country, country FROM region_country where region = '" + region + "'";

		try {
			final PreparedStatement statement = this.connect().prepareStatement(sql);

			try (ResultSet rs = statement.executeQuery()) {

				while (rs.next()) {
					country = rs.getString("country");

				}
			}

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			// e1.printStackTrace();

		}


		return country;

	}
	
	public void selectCountryRegion() {
		mapCountryRegion = new HashMap<String, String>();

		String sql = "SELECT country, region FROM region_country";

		try {
			final PreparedStatement statement = this.connect().prepareStatement(sql);

			try (ResultSet rs = statement.executeQuery()) {

				while (rs.next()) {
					mapCountryRegion.put(rs.getString("country"),rs.getString("region"));

				}
			}

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			// e1.printStackTrace();

		}



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

	public List<Landmark> getLandmarks() {
		return landmarks;
	}

	public Map<String, Landmark> getMapLandmark() {
		return mapLandmark;
	}

	public Mountain getMountain() {
		return mountain;
	}

	public List<Mountain> getMountains() {
		return mountains;
	}

	public Map<String, Mountain> getMapMountains() {
		return mapMountains;
	}

	public Map<String, City> getMapCities() {
		return mapCities;
	}

	public List<City> getCities() {
		return cities;
	}

	public List<Landcoord> getLandcoords() {
		return landcoords;
	}

	public Map<String, List<Landmark>> getGroupLandmark() {
		return groupLandmark;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public void setGroupLandmark(Map<String, List<Landmark>> groupLandmark) {
		this.groupLandmark = groupLandmark;
	}

	public Vor getVor() {
		return vor;
	}

	public List<Vor> getVors() {
		return vors;
	}

	public Map<String, Vor> getMapVors() {
		return mapVors;
	}

	public List<Ndb> getNdbs() {
		return ndbs;
	}

	public void setNdbs(List<Ndb> ndbs) {
		this.ndbs = ndbs;
	}

	public Ndb getNdb() {
		return ndb;
	}

	public Map<String, Ndb> getMapNdb() {
		return mapNdb;
	}

	public boolean isInitAll() {
		return isInitAll;
	}

	public void setInitAll(boolean isInitAll) {
		this.isInitAll = isInitAll;
	}

	public Map<String, StateCoord> getMapStateCoords() {
		return mapStateCoords;
	}

	public void setMapStateCoords(Map<String, StateCoord> mapStateCoords) {
		this.mapStateCoords = mapStateCoords;
	}

	public Map<String, String> getMapCountryRegion() {
		return mapCountryRegion;
	}

	public void setMapCountryRegion(Map<String, String> mapCountryRegion) {
		this.mapCountryRegion = mapCountryRegion;
	}

}
