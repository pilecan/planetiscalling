package com.model;


import com.cfg.model.Placemark;

public class PlacemarkMountain extends Placemark {
	public void readDescription(String description) {
		String[] descriptions = description.split("=");


		this.setDescription(description);

	}
	
}
