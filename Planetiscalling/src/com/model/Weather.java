package com.model;

import java.text.DecimalFormat;

import javax.json.JsonObject;

import com.cfg.common.Info;
import com.util.Util;

import net.weather.UtilityWeather;

public class Weather implements Info{
	private int id;
	private String name;
	private String country;
	private String state;
	
	private long dayDate;
	private long sunrise;
	private long sunset;
	private long timezone;
	

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
	
	private String message;
	
	public Weather() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void setData(JsonObject data ) {
        DecimalFormat d = new DecimalFormat("#.#");
        

        
         
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
			sunrise = data.getJsonObject("sys").getInt("sunrise");
			sunset = data.getJsonObject("sys").getInt("sunset");
			timezone = data.getInt("timezone");
	        Util.convertUnixDate(sunrise+timezone);

		} catch (Exception e) {
		}
        dayDate = data.getInt("dt");

        Util.convertUnixDate(dayDate);
/*		
        Calendar mydate = Calendar.getInstance();
		mydate.setTimeInMillis(timestamp*1000);
		System.out.println(mydate.get(Calendar.DAY_OF_MONTH)+"."+mydate.get(Calendar.MONTH)+"."+mydate.get(Calendar.YEAR)
		+" > "+mydate.get(Calendar.HOUR_OF_DAY)+":"+mydate.get(Calendar.MINUTE));
		const sunrise = new Date((data.sys.sunrise + data.timezone) * 1000)
				You will get the local time
		
*/   

	}
	
	public String htmlData() {
		
		//System.out.println(weather.getName() +" weather at " +Util.formatDistance(Geoinfo.distance(city.getLaty(), weather.getLaty(), city.getLonx(),weather.getLonx()))+" miles from "+city.getCityAscii());

		
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
			lines += detailLine.replace("#value", "Feel Like: "+feelLike+"�C");
		}
		if (tempMax != null) {
			lines += detailLine.replace("#value", "Maximum: "+tempMax+"�C");
		}
		if (tempMin != null) {
			lines += detailLine.replace("#value", "Minimum: "+tempMin+"�C");
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
		if (message != null) {
			message = detailLine.replace("#value", "Note: <i>"+message+"</i>");
		} else {
			message = "";
		}
		
		String html = "<br>"
				+"<div style='display: block; clear: left; font-size: medium; font-weight: bold; padding: 0pt 0pt;'> Temperature now for "+name+" is "+temp+"�C</div>\r\n"  
				+ message 
				+lines +
				"  </div>\r\n" + 
				"  <div style='display: block; clear: left; color: gray; font-size: x-small;'>\r\n" + 
				"    <a href='http://openweathermap.org/city/"+id+"?utm_source=openweathermap&amp;utm_medium=widget&amp;utm_campaign=html_old' target='_blank'>More..</a>\r\n" + 
				"  </div>\r\n" + 
				"<!--"+icon+"-->";
		
		
		
		
		return html;
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

	public Long getDayDate() {
		return dayDate;
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Weather [id=" + id + ", name=" + name + ", country=" + country + ", state=" + state + ", dayDate="
				+ dayDate + ", sunrise=" + sunrise + ", sunset=" + sunset + ", timezone=" + timezone
				+ ", weatherDescription=" + weatherDescription + ", temp=" + temp + ", tempMax=" + tempMax
				+ ", tempMin=" + tempMin + ", humidity=" + humidity + ", feelLike=" + feelLike + ", pressure="
				+ pressure + ", weatherMain=" + weatherMain + ", windDirection=" + windDirection + ", windSpeed="
				+ windSpeed + ", visibility=" + visibility + ", clouds=" + clouds + ", rain=" + rain + ", lonx=" + lonx
				+ ", laty=" + laty + ", station=" + station + ", icon=" + icon + ", message=" + message + "]";
	}
	
	

		


}