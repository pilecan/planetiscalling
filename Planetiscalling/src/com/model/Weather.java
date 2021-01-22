package com.model;

import java.text.DecimalFormat;
import java.util.Date;

import javax.json.JsonObject;

import com.cfg.common.Info;
import com.util.Util;

import net.weather.UtilityWeather;

public class Weather implements Info{
	private int id;
	private String name;
	private String country;
	private String state;
	
	private String date;
	private String timezone;
	private String sunrise;
	private String sunset;
	

	private String weatherDescription;
	private String temp;
	private String tempMax;
	private String tempMin;
	private String humidity;
	private String feelLike;
	
	private String pressure;
	private String weatherMain;
	private String windDirection;
	private String windSpeed;
	private String visibility;
	private String clouds;
	private Double rain;
	
	
	private Double lonx;
	private Double laty;
	
	private String station;

	private String icon;
	
	public Weather() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void setData(JsonObject data ) {
        DecimalFormat d = new DecimalFormat("#.#");
        
        
        Date date = new Date(1611252833);
        System.out.println(date);
        
        name =  data.getString("name");
        id =  data.getInt("id");
        
		pressure = d.format(data.getJsonObject("main").getJsonNumber("pressure").doubleValue());
        temp = d.format(data.getJsonObject("main").getJsonNumber("temp").doubleValue()-273);
        feelLike = d.format(data.getJsonObject("main").getJsonNumber("feels_like").doubleValue()-273);
        tempMax = d.format(data.getJsonObject("main").getJsonNumber("temp_max").doubleValue()-273);
        tempMin = d.format(data.getJsonObject("main").getJsonNumber("temp_min").doubleValue()-273);
        humidity = d.format(data.getJsonObject("main").getJsonNumber("humidity").doubleValue());
		try {
			visibility = (data.getJsonNumber("visibility").doubleValue()/1000)+" km";
		} catch (Exception e1) {
			visibility = null;
		}
        
        lonx = data.getJsonObject("coord").getJsonNumber("lon").doubleValue();
        laty = data.getJsonObject("coord").getJsonNumber("lat").doubleValue();

        weatherDescription = data.getJsonArray("weather").getJsonObject(0).getString("description");
        icon = data.getJsonArray("weather").getJsonObject(0).getString("icon");
        
        weatherMain = data.getJsonArray("weather").getJsonObject(0).getString("main");
        clouds = data.getJsonObject("clouds").getJsonNumber("all").intValue()+"";
        try {
			rain = data.getJsonObject("rain").getJsonNumber("1h").doubleValue();
		} catch (Exception e1) {
			rain = null;
		}

        
        final String DEGREE = "\u00b0";
        
        if (data.getJsonObject("wind").getJsonNumber("deg") != null){
            windDirection = data.getJsonObject("wind").getJsonNumber("deg").intValue() + DEGREE;     
        }
        else {
            windDirection= null;
        }
        windSpeed = d.format(data.getJsonObject("wind").getJsonNumber("speed").doubleValue()*3.6) + " km";
        
        
        
        try {
            country = data.getJsonObject("sys").getString("country");
			sunrise = d.format(data.getJsonObject("sys").getJsonNumber("sunrise").doubleValue());
			sunset = d.format(data.getJsonObject("sys").getJsonNumber("sunset").doubleValue());
			timezone = d.format(data.getJsonObject("sys").getJsonNumber("sunset").doubleValue());
		} catch (Exception e) {
		}

        

	}
	
	public String htmlData() {
		String url1 = UtilityWeather.getInstance().getCanonicalFile(icon+"@2x.png").toString();
		//url1 = "http://openweathermap.org/imgxx/w/04d.png";
		String url2 = UtilityWeather.getInstance().getCanonicalFile("transparent.png").toString();
		//url2 = "./image/weather/transparent.png";
		String detailLine = Info.meteoline;
		String lines = "";
		if (weatherDescription != null) {
			lines += detailLine.replace("#value", "Summary: "+weatherDescription);
		}
		if (clouds != null) {
			lines += detailLine.replace("#value", "Clouds: "+clouds+"%");
		}
		if (rain != null) {
			lines += detailLine.replace("#value", "Rain: "+Util.formatRain(rain)+"/h");
		}
		if (humidity != null) {
			lines += detailLine.replace("#value", "Humidity: "+humidity+"%");
		}
		if (visibility!= null) {
			lines += detailLine.replace("#value", "Visibility: "+visibility);
		}
		if (feelLike != null) {
			lines += detailLine.replace("#value", "Feel Like: "+feelLike+"°C");
		}
		if (tempMax != null) {
			lines += detailLine.replace("#value", "Maximum: "+tempMax+"°C");
		}
		if (tempMin != null) {
			lines += detailLine.replace("#value", "Minimum: "+tempMin+"°C");
		}
		if (windSpeed != null) {
			lines += detailLine.replace("#value", "Wind: "+windSpeed);
		}
		if (windDirection != null) {
			lines += detailLine.replace("#value", "Wind Direction: "+windDirection);
		}
		if (pressure != null) {
			lines += detailLine.replace("#value", "Pressure: "+pressure+" hPa");
		}
		
		
		String html = "" + 
				" <div style='float: left; width: 130px;'>\r\n" + 
				"    <div style='display: block; clear: left;'>\r\n" + 
				"      <div style='float: left;' title='Titel'>\r\n" + 
				"                <img height='45' width='45' style='border: medium none; width: 45px; height: 45px; background:url(\""+url1+"\") repeat scroll 0% 0% transparent;' alt='title' src='"+url2+"'>\r\n" + 
				"      </div>\r\n" + 
				"      <div style='float: left;'>\r\n" + 
				"        <div style='display: block; clear: left; font-size: medium; font-weight: bold; padding: 0pt 3pt;' title='Current Temperature'>"+temp+"°C</div>\r\n" + 
				"        <div style='display: block; width: 85px; overflow: visible;'></div>\r\n" + 
				"      </div>\r\n" + 
				"    </div>\r\n" +  lines +
				"  </div>\r\n" + 
				"  <div style='display: block; clear: left; color: gray; font-size: x-small;'>\r\n" + 
				"    <a href='http://openweathermap.org/city/"+id+"?utm_source=openweathermap&amp;utm_medium=widget&amp;utm_campaign=html_old' target='_blank'>More..</a>\r\n" + 
				"  </div>\r\n" + 
				"<!--"+icon+"-->";
		
		
		
		
		return html;
	}
	


	@Override
	public String toString() {
		return "Weather [id=" + id + ", name=" + name + ", country=" + country + ", state=" + state + ", date=" + date
				+ ", timezone=" + timezone + ", sunrise=" + sunrise + ", sunset=" + sunset + ", weatherDescription="
				+ weatherDescription + ", temp=" + temp + ", tempMax=" + tempMax + ", tempMin=" + tempMin
				+ ", humidity=" + humidity + ", feelLike=" + feelLike + ", pressure=" + pressure + ", weatherMain="
				+ weatherMain + ", windDirection=" + windDirection + ", windSpeed=" + windSpeed + ", visibility="
				+ visibility + ", clouds=" + clouds + ", rain=" + rain + ", lonx=" + lonx + ", laty=" + laty
				+ ", station=" + station + ", icon=" + icon + "]";
	}

	public Double getLonx() {
		return lonx;
	}

	public Double getLaty() {
		return laty;
	}

	public String getName() {
		return name;
	}

	public String getCountry() {
		return country;
	}

	public String getState() {
		return state;
	}

	public String getDate() {
		return date;
	}

	public String getTimezone() {
		return timezone;
	}

	public String getSunrise() {
		return sunrise;
	}

	public String getSunset() {
		return sunset;
	}

	public String getWeatherDescription() {
		return weatherDescription;
	}

	public String getTemp() {
		return temp;
	}

	public String getTempMax() {
		return tempMax;
	}

	public String getTempMin() {
		return tempMin;
	}

	public String getHumidity() {
		return humidity;
	}

	public String getFeelLike() {
		return feelLike;
	}

	public String getPressure() {
		return pressure;
	}

	public String getWeatherMain() {
		return weatherMain;
	}

	public String getWindDirection() {
		return windDirection;
	}

	public String getWindSpeed() {
		return windSpeed;
	}

	public String getVisibility() {
		return visibility;
	}

	public String getStation() {
		return station;
	}

	public String getIcon() {
		return icon;
	}

	public String getClouds() {
		return clouds;
	}
	
	

		


}
