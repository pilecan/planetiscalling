package com.model;

import java.util.ArrayList;

import com.geo.util.Geoinfo;

public class Boundingbox {
	private double beginLaty;
	private double beginLonx;
	private double endLaty;
	private double endLonx;
	private ArrayList<Landcoord> landcoords;
	
	public Boundingbox(double beginLaty, double beginLonx, double endLaty, double endLonx) {
		super();
		this.beginLaty = beginLaty;
		this.beginLonx = beginLonx;
		this.endLaty = endLaty;
		this.endLonx = endLonx;
		createBox();
	}
	
	private void createBox() {
	     double angle = Geoinfo.calculateAngle(beginLonx,beginLaty,endLonx, endLaty);
	     System.out.println("angle = "+angle);
	     Landcoord landcoord = null;
	     
	     landcoords = new ArrayList<>();
	     int orientation = 0;
	     if (angle > 0 && angle < 90) {
	    	 orientation = 90;
	    	landcoord= Geoinfo.searchPoint(beginLaty,beginLonx, 50, angle+180);
	        beginLaty = landcoord.getLaty();
	        beginLonx = landcoord.getLonx();
	        landcoords.add(Geoinfo.searchPoint(beginLaty,beginLonx, 50, angle+orientation));

	     }

	  //   landcoords.add(Geoinfo.searchPoint(beginLaty,beginLonx, 20, angle+orientation));
	     landcoords.add(Geoinfo.searchPoint(endLaty,endLonx, 20, Geoinfo.calculateAngle( endLaty, endLonx,beginLaty, beginLonx)+orientation));
/*	     landcoords.add(Geoinfo.searchPoint(endLaty,endLonx, 20, Geoinfo.calculateAngle( endLaty, endLonx,beginLaty, beginLonx)+orientation));
	     landcoords.add(Geoinfo.searchPoint(endLaty,endLonx, 20, Geoinfo.calculateAngle( endLaty, endLonx,beginLaty, beginLonx)-orientation));
*/	}
	
	
	public boolean isInside(Landcoord landcoord ) {
		return Geoinfo.isInside(landcoord, landcoords);
	}
	
	public double getBeginLaty() {
		return beginLaty;
	}
	public void setBeginLaty(double beginLaty) {
		this.beginLaty = beginLaty;
	}
	public double getBeginLonx() {
		return beginLonx;
	}
	public void setBeginLonx(double beginLonx) {
		this.beginLonx = beginLonx;
	}
	public double getEndLaty() {
		return endLaty;
	}
	public void setEndLaty(double endLaty) {
		this.endLaty = endLaty;
	}
	public double getEndLonx() {
		return endLonx;
	}
	public void setEndLonx(double endLonx) {
		this.endLonx = endLonx;
	}

}
