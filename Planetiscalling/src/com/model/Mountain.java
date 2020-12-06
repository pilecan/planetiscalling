package com.model;

public class Mountain {

	private String id;
	private String name;
	private Integer elevation;
	private Integer prominence;
	private String alt_name;
	private String comment;
	private String author;
	private String country;
	private Double lonx;
	private Double laty;
	private String location;
	private String type;
	private Integer lastActivity;

	public Mountain() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Mountain(Mountain mountain) {
		this.id = mountain.getId();
		this.name = mountain.getName();
		this.elevation = mountain.getElevation();
		this.prominence = mountain.getProminence();
		this.alt_name = mountain.getAlt_name();
		this.comment = mountain.getComment();
		this.author = mountain.getAuthor();
		this.country = mountain.getCountry();
		this.lonx = mountain.getLonx();
		this.laty = mountain.getLaty();
		this.location = mountain.getLocation();
		this.type = mountain.getType();
		this.lastActivity = mountain.getLastActivity();
		
	}

public Mountain(String id, String name, Integer elevation, Integer prominence, String alt_name, String comment,
		String author, String country, Double lonx, Double laty, String location, String type, Integer lastActivity) {
	super();
	this.id = id;
	this.name = name;
	this.elevation = elevation;
	this.prominence = prominence;
	this.alt_name = alt_name;
	this.comment = comment;
	this.author = author;
	this.country = country;
	this.lonx = lonx;
	this.laty = laty;
	this.location = location;
	this.type = type;
	this.lastActivity = lastActivity;
}
public Mountain(String id, String name, Integer elevation, Integer prominence, String alt_name, String comment,
		String author, String country, Double lonx, Double laty) {
	super();
	this.id = id;
	this.name = name;
	this.elevation = elevation;
	this.prominence = prominence;
	this.alt_name = alt_name;
	this.comment = comment;
	this.author = author;
	this.country = country;
	this.lonx = lonx;
	this.laty = laty;
}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getElevation() {
		return elevation;
	}
	public void setElevation(Integer elevation) {
		this.elevation = elevation;
	}
	public Integer getProminence() {
		return prominence;
	}
	public void setProminence(Integer prominence) {
		this.prominence = prominence;
	}
	public String getAlt_name() {
		return alt_name;
	}
	public void setAlt_name(String alt_name) {
		this.alt_name = alt_name;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getCoordinates() {
		return ""+lonx+","+laty+","+elevation;
	}

	
	public String toCSV() {
		return "\""+id +"\",\""+ name +"\",\""+ alt_name +"\",\""+ elevation +"\",\""+prominence+"\",\""+ comment +"\",\""+ author +"\",\""+ country +"\",\""+ lonx +"\",\""+ laty+"\"";
	}
	public String toCSV2() {
		if (country.contains(",")) {
			country = country.replace(",", "-");
		}
		if (comment.contains(",")) {
			comment = comment.replace(",", "-");
		}
		if (alt_name.contains(",")) {
			alt_name = alt_name.replace(",", "-");
		}
		return ("".equals(id)?"no":id) +","+ name +","+("".equals(alt_name)?"no":alt_name)+","+ elevation +","+prominence+","+ ("".equals(comment)?"no":comment) +","+ author +","+ country +","+ lonx +","+ laty;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getLastActivity() {
		return lastActivity;
	}

	public void setLastActivity(Integer lastActivity) {
		this.lastActivity = lastActivity;
	}

	@Override
	public String toString() {
		return "Mountain [id=" + id + ", name=" + name + ", elevation=" + elevation + ", prominence=" + prominence
				+ ", alt_name=" + alt_name + ", comment=" + comment + ", author=" + author + ", country=" + country
				+ ", lonx=" + lonx + ", laty=" + laty + ", location=" + location + ", type=" + type + ", lastActivity="
				+ lastActivity + "]";
	}

}
