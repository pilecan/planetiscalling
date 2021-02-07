package com.model;

import java.util.ArrayList;
import java.util.List;

import com.geo.util.Geoinfo;
import com.util.UtilityMap;

public class Boundingbox{
	private double lat1;
	private double lon1;
	private double lat2;
	private double lon2;
	private int distance;
	private List<Landcoord> landcoords;
	private Landcoord landcoord;
	private List<CoordinatesDTO> coords;
	private double flpAngle;
	
	private static Boundingbox instance = new Boundingbox();
	public static Boundingbox getInstance() {
		return instance;
	}
	
	public Boundingbox() {
		super();
		// TODO Auto-generated constructor stub
	}


	public void createBox(double lat1, double lon1, double lat2, double lon2, int distance) {
		this.lat1 = lat1;
		this.lon1 = lon1;
		this.lat2 = lat2;
		this.lon2 = lon2;
		this.distance = distance;

		landcoords = new ArrayList<Landcoord>();

	    double angleForward = Geoinfo.calculateAngle(lon1,lat1,lon2, lat2);
	    double angleBackward = Geoinfo.calculateAngle(lon2, lat2,lon1,lat1);
	     
	    Landcoord coordBack = Geoinfo.searchPoint(lat1,lon1, distance, angleBackward); 
	    Landcoord coordFoward = Geoinfo.searchPoint(lat2,lon2, distance, angleForward);
	     
	    landcoords.add(Geoinfo.searchPoint(coordBack.getLaty(),coordBack.getLonx(), distance, Geoinfo.correctAngle(angleForward-90)));
	    landcoords.add(Geoinfo.searchPoint(coordBack.getLaty(),coordBack.getLonx(), distance, Geoinfo.correctAngle(angleForward+90)));
	    landcoords.add(Geoinfo.searchPoint(coordFoward.getLaty(),coordFoward.getLonx(), distance, Geoinfo.correctAngle(angleForward+90)));
	    landcoords.add(Geoinfo.searchPoint(coordFoward.getLaty(),coordFoward.getLonx(), distance, Geoinfo.correctAngle(angleForward-90)));
	     
		coords = new ArrayList<CoordinatesDTO>();
	    coords.add(new CoordinatesDTO(landcoords.get(0).getLaty(), landcoords.get(0).getLonx()));
	    coords.add(new CoordinatesDTO(landcoords.get(1).getLaty(), landcoords.get(1).getLonx()));
	    coords.add(new CoordinatesDTO(landcoords.get(2).getLaty(), landcoords.get(2).getLonx()));
	    coords.add(new CoordinatesDTO(landcoords.get(3).getLaty(), landcoords.get(3).getLonx()));

	}


	
	public boolean isInside(double laty, double lonx ) {
		return UtilityMap.getInstance().isLocationInsideTheFencing(new CoordinatesDTO(laty, lonx), coords);
	}
	


	public double getLat1() {
		return lat1;
	}


	public void setLat1(double lat1) {
		this.lat1 = lat1;
	}


	public double getLon1() {
		return lon1;
	}


	public void setLon1(double lon1) {
		this.lon1 = lon1;
	}


	public double getLat2() {
		return lat2;
	}


	public void setLat2(double lat2) {
		this.lat2 = lat2;
	}


	public double getLon2() {
		return lon2;
	}


	public void setLon2(double lon2) {
		this.lon2 = lon2;
	}


	public int getDistance() {
		return distance;
	}


	public void setDistance(int distance) {
		this.distance = distance;
	}
	

}
