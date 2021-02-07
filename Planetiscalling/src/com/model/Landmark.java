package com.model;

public class Landmark {
	private String cgnId;	
	private String geoName;
	private String geoTerm;
	private String category;
	private String code;
	private String admin;
	private Double lonx;
	private Double laty;
	private String decisionDate;
	private String source;
	private String location;
	private String language;
	private String syllabic;
	private String toponomic;
	private String revelance;
	
	public Landmark() {
		super();
	}


	public Landmark(String cgnId, String geoName, String geoTerm, String category, String code, String admin,
			Double lonx, Double laty, String decisionDate, String source, String location, String language,
			String syllabic, String toponomic, String revelance) {
		super();
		this.cgnId = cgnId;
		this.geoName = geoName.replace("&", "");
		this.geoTerm = geoTerm.replace("&", "");
		this.category = category.replace("&", "");
		this.code = code;
		this.admin = admin;
		this.lonx = lonx;
		this.laty = laty;
		this.decisionDate = decisionDate;
		this.source = source;
		this.location = location;
		this.language = language;
		this.syllabic = syllabic;
		this.toponomic = toponomic;
		this.revelance = revelance;
	}

	public Landmark(String cgnId, String geoName, String geoTerm, String category, String code, String admin,
			Double lonx, Double laty, String decisionDate, String source) {
		super();
		this.cgnId = cgnId;
		this.geoName = geoName;
		this.geoTerm = geoTerm;
		this.category = category;
		this.code = code;
		this.admin = admin;
		this.lonx = lonx;
		this.laty = laty;
		this.decisionDate = decisionDate;
		this.source = source;
	}
		



	public Landmark(Landmark landmark) {
		super();
		this.cgnId = landmark.getCgnId();
		this.geoName = landmark.getGeoName();
		this.geoTerm = landmark.getGeoTerm();
		this.category = landmark.getCategory();
		this.code = landmark.getCode();
		this.admin = landmark.getAdmin();
		this.lonx = landmark.getLonx();
		this.laty = landmark.getLaty();
		this.decisionDate = landmark.getDecisionDate();
		this.source = landmark.getSource();
		this.location = landmark.getLocation();
		this.toponomic = landmark.getToponomic();
		this.language = landmark.getLanguage();
		this.syllabic = landmark.getSyllabic();
		this.revelance = landmark.getRevelance();

	}

	public String getCoordinates() {
		return ""+lonx+","+laty+","+"0";
	}

	public String getGeoName() {
		return geoName;
	}
	public void setGeoName(String geoName) {
		this.geoName = geoName;
	}
	public String getGeoTerm() {
		return geoTerm;
	}
	public void setGeoTerm(String geoTerm) {
		this.geoTerm = geoTerm;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getAdmin() {
		return admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
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
		return "Landmark [cgnId=" + cgnId + ", geoName=" + geoName + ", geoTerm=" + geoTerm + ", category=" + category
				+ ", code=" + code + ", admin=" + admin + ", lonx=" + lonx + ", laty=" + laty + ", decisionDate="
				+ decisionDate + ", source=" + source + ", location=" + location + ", language=" + language
				+ ", syllabic=" + syllabic + ", toponomic=" + toponomic + ", revelance=" + revelance + "]";
	}

	public String getCgnId() {
		return cgnId;
	}

	public void setCgnId(String cgnId) {
		this.cgnId = cgnId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}



	public String getDecisionDate() {
		return decisionDate;
	}



	public void setDecisionDate(String decisionDate) {
		this.decisionDate = decisionDate;
	}



	public String getLanguage() {
		return language;
	}



	public void setLanguage(String language) {
		this.language = language;
	}



	public String getSyllabic() {
		return syllabic;
	}



	public void setSyllabic(String syllabic) {
		this.syllabic = syllabic;
	}



	public String getToponomic() {
		return toponomic;
	}



	public void setToponomic(String toponomic) {
		this.toponomic = toponomic;
	}



	public String getLocation() {
		return location;
	}



	public void setLocation(String location) {
		this.location = location;
	}



	public String getRevelance() {
		return revelance;
	}



	public void setRevelance(String revelance) {
		this.revelance = revelance;
	}

}
