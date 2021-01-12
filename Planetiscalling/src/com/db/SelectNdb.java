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
import com.model.Ndb;

public class SelectNdb implements Info{
	
	private Ndb ndb;
	private List <Ndb> ndbs;
	private Map<String, Ndb> mapNdb;

	

	public SelectNdb() {
		// TODO Auto-generated constructor stub
	}
	public void selectAll(String search ) {
		ndb = new Ndb();
		ndbs = new ArrayList<>();
		mapNdb = new TreeMap<>();

		

		String sql = "SELECT ndb_id," + 
				"       file_id," + 
				"       ident," + 
				"       name," + 
				"       region," + 
				"       airport_id," + 
				"       type," + 
				"       frequency," + 
				"       range," + 
				"       mag_var," + 
				"       altitude," + 
				"       lonx," + 
				"       laty" + 
				"  FROM ndb " + 
				" ";
		
		if (!"".equals(search)) {
			 sql += search;

		}
		try {
			final PreparedStatement statement = this.connect().prepareStatement(sql);

			try (ResultSet rs = statement.executeQuery()) {

				while (rs.next()) {
				    ndb = new Ndb(rs.getInt("ndb_id"),      
				    	    rs.getInt("file_id"),      
				    	    rs.getString("ident"), 
				    	    rs.getString("name"), 
				    	    rs.getString("region"), 
				    	    rs.getInt("airport_id"),
				    	    rs.getString("type"), 
				    	    rs.getInt("frequency"),
				    	    rs.getInt("range"),
				    	    rs.getDouble("mag_var"),  
				    	    rs.getInt("altitude"),
				    	    rs.getDouble("lonx"),  
				    	    rs.getDouble("laty")  );

				    ndbs.add(ndb);
				    mapNdb.put(rs.getString("ident")+rs.getString("name").replace(" ", ""), ndb);
				}
				
				

		}
			System.out.println(ndbs.size());
			System.out.println(mapNdb.size());
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
     	String search = "LF LI LE".toUpperCase();	
     	
     	search = "";

		SelectNdb selectVor = new SelectNdb();
		selectVor.selectAll(search );
		
	    	String kmlRelative = "data\\vor"+search+".kml";

	    
		 
//	 new CreateKML().write(selectVor.getVors(), search, kmlRelative);
		 
	//	 Utility.getInstance().launchGoogleEarth(new File(kmlRelative));
		
	}
	public List<Ndb> getNdbs() {
		return ndbs;
	}
	public void setNdbs(List<Ndb> cities) {
		this.ndbs = cities;
	}
	public Map<String, Ndb> getMapNdb() {
		return mapNdb;
	}
	public void setMapNdb(Map<String, Ndb> mapNdb) {
		this.mapNdb = mapNdb;
	}


}
