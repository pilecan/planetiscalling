package com.model;

public class City {

	private Long id;
	private String cityName;
	private String cityAscii;
	private Double lonx;
	private Double laty;
	private String country;
	private String iso2;
	private String iso3;
	private String adminName;
	private String capital;
	private Long population;

	
	public City(String cityName, String country, Double lonx, Double laty) {
		super();
		this.cityName = cityName;
		this.country = country;
		this.lonx = lonx;
		this.laty = laty;
	}

	public City(Long id, String city, String cityAscii, Double lonx, Double laty, String country, String iso2,
			String iso3, String adminName, String capital, Long population) {
		super();
		this.id = id;
		this.cityName = city;
		this.cityAscii = cityAscii;
		this.lonx = lonx;
		this.laty = laty;
		this.country = country;
		this.iso2 = iso2;
		this.iso3 = iso3;
		this.adminName = adminName;
		this.capital = capital;
		this.population = population;
	}

    public City(City city) {
		this.id = city.getId();
		this.cityName = city.cityName;
		this.cityAscii = city.getCityAscii();
		this.lonx = city.getLonx();
		this.laty = city.getLaty();
		this.country = city.getCountry();
		this.iso2 = city.getIso2();
		this.iso3 = city.getIso3();
		this.adminName = city.getAdminName();
		this.capital = city.getCapital();
		this.population = city.getPopulation();
    	
    }



	public City(String cityName, String cityAscii, String adminName, Double laty, Double lonx, Long population, Long id) {
		super();
		this.cityName = cityName;
		this.cityAscii = cityAscii;
		this.adminName = adminName;
		this.laty = laty;
		this.lonx = lonx;
		this.population = population;
		this.id = id;
	}

	public City() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getCityName() {
		return cityName;
	}



	public void setCityName(String city) {
		this.cityName = city;
	}



	public String getCityAscii() {
		return cityAscii;
	}



	public void setCityAscii(String cityAscii) {
		this.cityAscii = cityAscii;
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



	public String getCountry() {
		return country;
	}



	public void setCountry(String countty) {
		this.country = countty;
	}



	public String getIso2() {
		return iso2;
	}



	public void setIso2(String iso2) {
		this.iso2 = iso2;
	}



	public String getIso3() {
		return iso3;
	}



	public void setIso3(String iso3) {
		this.iso3 = iso3;
	}



	public String getAdminName() {
		return adminName;
	}



	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}



	public String getCapital() {
		return capital;
	}



	public void setCapital(String capital) {
		this.capital = capital;
	}



	public Long getPopulation() {
		return population;
	}



	public void setPopulation(Long population) {
		this.population = population;
	}

	public String getDescription() {
		
		return " |Name: "+cityName
				+ (!cityName.equals(cityAscii)?"|International Name: "+cityAscii:"")
				+ (adminName!=null?"|State: "+adminName:"")
				+ "|Country: "+country
				+ "|Abreviation short: "+iso2
				+ "|Abreviation long: "+iso3
				+ "|Population : "+population
				+ "|";
	}
	
	public String buildKML() {
		return "<Placemark><name>"+cityName+"</name>\n"
				+ "<description><![CDATA["+getDescription()+"]]></description>\n"
				+ "<styleUrl>"+getStyleUrl()+"</styleUrl>\n"
				+ "<Point><coordinates>"+getCoordinates()+"</coordinates></Point>\n"
				+ "</Placemark>\n";		
	}

	public String getStyleUrl() {
		return "|#fsx_airport";
	}



	public String getPoint() {
		return "|";
	}



	public String getCoordinates() {
		return ""+lonx+","+laty+",0";
	}

	public String getCoordinatesInvert() {
		return ""+laty+","+lonx+",0";
	}

	@Override
	public String toString() {
		return "City [id=" + id + ", city=" + cityName + ", cityAscii=" + cityAscii + ", lonx=" + lonx + ", laty=" + laty
				+ ", country=" + country + ", iso2=" + iso2 + ", iso3=" + iso3 + ", adminName=" + adminName
				+ ", capital=" + capital + ", population=" + population + "]";
	}

}
