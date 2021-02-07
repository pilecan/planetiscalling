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
import com.model.Vor;

public class SelectVor implements Info{
	
	private Vor vor;
	private List <Vor> vors;
	private Map<String, Vor> mapVors;

	

	public SelectVor() {
		// TODO Auto-generated constructor stub
	}
	public void selectAll(String search ) {
		vor = new Vor();
		vors = new ArrayList<>();
		mapVors = new TreeMap<>();

		

		String sql = "SELECT vor_id," + 
				"       file_id," + 
				"       ident," + 
				"       name," + 
				"       region," + 
				"       airport_id," + 
				"       type," + 
				"       frequency," + 
				"       channel," + 
				"       range," + 
				"       mag_var," + 
				"       dme_only," + 
				"       dme_altitude," + 
				"       dme_lonx," + 
				"       dme_laty," + 
				"       altitude," + 
				"       lonx," + 
				"       laty" + 
				"  FROM vor ";
		
		if (!"".equals(search)) {
			 sql += search;

		}
		try {
			final PreparedStatement statement = this.connect().prepareStatement(sql);

			try (ResultSet rs = statement.executeQuery()) {
				String lastAirport = "";

				while (rs.next()) {
				    vor = new Vor(rs.getInt("vor_id"), rs.getInt("file_id"), 
				    		rs.getString("ident"), rs.getString("name"),
				    		rs.getString("region"), 
				    		rs.getInt("airport_id"), rs.getString("type"), 
				    		rs.getInt("frequency"), rs.getString("channel"), 
				    		rs.getInt("range"), rs.getDouble("mag_var"), 
				    		rs.getInt("dme_only"), rs.getInt("dme_altitude"), 
				    		rs.getDouble("dme_lonx"), rs.getDouble("dme_laty"), 
				    		rs.getInt("altitude"), rs.getDouble("lonx"), 
				    		rs.getDouble("laty"));

				    vors.add(vor);
				    mapVors.put(rs.getString("ident")+rs.getString("name").replace(" ", ""), vor);
				}
				
				

		}
			} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();

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
     	
     	search = "where ident = 'TOL'";

		SelectVor selectVor = new SelectVor();
		selectVor.selectAll(search );
		
	    	String kmlRelative = "data\\vor"+search+".kml";

	    
		 
//	 new CreateKML().write(selectVor.getVors(), search, kmlRelative);
		 
	//	 Utility.getInstance().launchGoogleEarth(new File(kmlRelative));
		
	}
	public List<Vor> getVors() {
		return vors;
	}
	public void setVors(List<Vor> vors) {
		this.vors = vors;
	}
	public Map<String, Vor> getMapVors() {
		return mapVors;
	}
	public void setMapVors(Map<String, Vor> mapVors) {
		this.mapVors = mapVors;
	}
	
	public List <Vor> listOfOneVor(Vor vor){
		List< Vor> listOfOneVor = new ArrayList<Vor>(); 
		listOfOneVor.add(vor);
		return listOfOneVor;
		
	}


}
