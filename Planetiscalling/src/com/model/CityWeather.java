package com.model;

import java.util.List;

import com.util.Util;

public class CityWeather {

	private Long id;
	private String name;
	private String iso2;
	private Double lonx;;
	private Double laty;
	
	
	
	public CityWeather() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public CityWeather(Long id, String name, String iso2, Double lonx, Double laty) {
		super();
		this.id = id;
		this.name = name;
		this.iso2 = iso2;
		this.lonx = lonx;
		this.laty = laty;
	}

	public CityWeather(CityWeather city) {
		super();
		this.id = city.getId();
		this.name = city.getName();
		this.iso2 = city.getIso2();
		this.lonx = city.getLonx();
		this.laty = city.getLaty();
	}


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIso2() {
		return iso2;
	}
	public void setIso2(String iso2) {
		this.iso2 = iso2;
	}
	public Double getLonx() {
		return lonx;
	}
	public void setLonx(Double lonx) {
		this.lonx = lonx;
	}
	public Double getLaty() {
		return laty;
	}
	public void setLaty(Double laty) {
		this.laty = laty;
	}


	@Override
	public String toString() {
		return "CityWeather [id=" + id + ", name=" + name + ", iso2=" + iso2 + ", lonx=" + lonx + ", laty=" + laty
				+ "]";
	}
	

}
