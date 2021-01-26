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
	
	public Landmark() {
		super();
		// TODO Auto-generated constructor stub
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
		return "Landmark [geoName=" + geoName + ", geoTerm=" + geoTerm + ", category=" + category + ", code=" + code
				+ ", admin=" + admin + ", lonx=" + lonx + ", laty=" + laty + "]";
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

}
