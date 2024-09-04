package com.model;

import java.util.ArrayList;
import java.util.List;

import javax.json.JsonNumber;

public class StateCoord {
	public StateCoord() {
		super();
		// TODO Auto-generated constructor stub
	}
	private int id;
	private String region;
	private String state;
	private String description;
	private CoordinatesDTO coordinatesDTO;
	private List<CoordinatesDTO> coordinatesDTOs;
	
	public StateCoord(String region, String state, String description) {
		super();
		this.region = region;
		this.state = state;
		this.description = description;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	public CoordinatesDTO getCoordinatesDTO() {
		return coordinatesDTO;
	}
	public void setCoordinatesDTO(CoordinatesDTO coordinatesDTO) {
		this.coordinatesDTO = coordinatesDTO;
	}
	public List<CoordinatesDTO> getCoordinatesDTOs() {
		return coordinatesDTOs;
	}
	public void setCoordinatesDTOs(CoordinatesDTO coordinatesDTO) {
		if (coordinatesDTOs == null ) {
			coordinatesDTOs = new ArrayList<CoordinatesDTO>();
		}
		this.coordinatesDTOs.add(coordinatesDTO);
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "StateCoord [region=" + region + ", state=" + state + ", description=" + description
				+ ", coordinatesDTO=" + coordinatesDTO + ", coordinatesDTOs=" + coordinatesDTOs + "]";
	}
	public void setCoordinatesDTOs(List<CoordinatesDTO> coordinatesDTOs) {
		this.coordinatesDTOs = coordinatesDTOs;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}


}
