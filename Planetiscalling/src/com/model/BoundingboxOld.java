package com.model;

import java.util.ArrayList;

import com.geo.util.Geoinfo;

public class BoundingboxOld{
	private double beginLaty;
	private double beginLonx;
	private double endLaty;
	private double endLonx;
	private int distance;
	private ArrayList<Landcoord> landcoords;
	private double flpAngle;
	
	public BoundingboxOld(double beginLaty, double beginLonx, double endLaty, double endLonx, int distance) {
		super();
		this.beginLaty = beginLaty;
		this.beginLonx = beginLonx;
		this.endLaty = endLaty;
		this.endLonx = endLonx;
		this.distance = distance;
		createBox();
	}
	
	private void createBox() {
	     double angle = Geoinfo.calculateAngle(beginLonx,beginLaty,endLonx, endLaty);
	     flpAngle = angle;
	     System.out.println("angle = "+angle);
	     Landcoord landcoord = null;
	     int offestBack = 180;
	     int offestCorner = 20;
	     
	     landcoords = new ArrayList<>();
	     int cornerBoxOrientation = 90;
	     if (angle > 0 && angle < 180) {
		     if ((angle > 0 && angle < 45)) {
		    	 cornerBoxOrientation = -90;
		     } else if ((angle > 45 && angle < 90)) {
		    	 cornerBoxOrientation = -90;
		     }else if ((angle >= 90 && angle <= 135)) {
		    	 cornerBoxOrientation = 90;
		     } else if ((angle >= 135 && angle <= 180)) {
		    	 cornerBoxOrientation = -90;
		     } 
	     } else if (angle >= 180 && angle <= 360) {
		     offestBack = -180;
	    	 cornerBoxOrientation = -90;
	    	 if (angle > 180 &&  angle < 255) {
		    	 cornerBoxOrientation = 90;
	    	 } else if (angle > 270 &&  angle < 315) {
		    	 cornerBoxOrientation = 90;
	    	 } else if (angle >315 && angle <= 360) {
		    	 cornerBoxOrientation = -90;
	    	 }
	     
	     }
	     
	        angle = angle+offestBack;
	        angle = angle+Math.ceil( -angle / 360 ) * 360;

	    	landcoord= Geoinfo.searchPoint(beginLaty,beginLonx, distance, angle);
	        beginLaty = landcoord.getLaty();
	        beginLonx = landcoord.getLonx();
	        landcoords.add(Geoinfo.searchPoint(beginLaty,beginLonx, offestCorner, angle+cornerBoxOrientation));

		    angle = Geoinfo.calculateAngle(endLonx, endLaty, beginLonx,beginLaty);
	        angle = angle+offestBack;
	        angle = angle + Math.ceil( -angle / 360 ) * 360;

	    	landcoord= Geoinfo.searchPoint(endLaty,endLonx, distance, angle);
	    	endLaty = landcoord.getLaty();
	    	endLonx = landcoord.getLonx();
	        landcoords.add(Geoinfo.searchPoint(endLaty,endLonx, offestCorner, angle+cornerBoxOrientation));

	}
	
	
	public boolean isInside(Landcoord landcoord ) {
		return Geoinfo.isInside(landcoord, landcoords, flpAngle);
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
