package com.model;

public class Landcoord {
	private double lonx;
	private double laty;
	
	
	public Landcoord() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public Landcoord(double laty, double lonx) {
		super();
		this.lonx = lonx;
		this.laty = laty;
	}
	
	public double getLonx() {
		return lonx;
	}
	public void setLonx(double lonx) {
		this.lonx = lonx;
	}
	public double getLaty() {
		return laty;
	}


	public void setLaty(double laty) {
		this.laty = laty;
	}

}
