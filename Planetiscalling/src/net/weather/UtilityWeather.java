package net.weather;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.cfg.common.Info;
import com.db.UtilityDB;
import com.geo.util.Geoinfo;
import com.model.Airport;
import com.model.City;
import com.model.CityWeather;
import com.model.Mountain;
import com.model.Weather;
import com.util.Util;

public class UtilityWeather implements Info {
	private static UtilityWeather instance = new UtilityWeather();

	private Weather weather;
	
	public static UtilityWeather getInstance() {

		return instance;
	}

	public void searchCityWeather(City city) {
		CityWeather cityWeather = UtilityDB.getInstance().selectCityWeather(city);
		if (cityWeather != null) {
			System.out.println(cityWeather.toString());
			callOpenweathermapId(cityWeather.getId());
		} else {
			callOpenweathermapObject(city);
		}

		System.out.println();
		System.out.println();
	}

	public void callOpenweathermapId(long id) {
        String currStr = null;
        weather = new Weather();
		try {
			currStr = getData("http://api.openweathermap.org/data/2.5/weather?id=" + id + "&APPID=3506dfa8bbebf7709e6fba904a68559a");
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
        JsonReader reader = Json.createReader(new StringReader(currStr));
        JsonObject data = reader.readObject();
        
        int code = 0;
        try {
			code = data.getInt("cod");
		} catch (Exception e) {
			code = Integer.parseInt(data.getString("cod"));
		}
       
       if(code == 200) {
        	System.out.println(data.getString("name"));
        	System.out.println(data);
        	weather.setData(data);
        	
        	System.out.println(weather.toString());
           // currDataBuilder.add(data);
        }
        reader.close();
		
	}
	
	public void callOpenweathermapObject(Object object) {
		double lat = 0;
		double lon = 0;
		String name = "";
		String message = "";
		if (object instanceof City) {
			lon = ((City)object).getLonx();
			lat = ((City)object).getLaty();
			name = ((City)object).getCityAscii(); 
			message = "No weather station at "+name+" but found it at ";
		} else if (object instanceof Airport) {
			lon = ((Airport)object).getLonx();
			lat = ((Airport)object).getLaty();
			name = ((Airport)object).getName(); 
			message = "No METAR at "+name+" Airport but Weather Station found at ";

		} else if (object instanceof Mountain) {
			lon = ((Mountain)object).getLonx();
			lat = ((Mountain)object).getLaty();
			name = ((Mountain)object).getName(); 
			message = "Weather Station found at ";

		} 
		
        String currStr = null;
        weather = new Weather();
        
		try {
			currStr = getData("http://api.openweathermap.org/data/2.5/find?lat="+lat+"&lon="+lon+"&cnt=1&APPID=3506dfa8bbebf7709e6fba904a68559a");
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
        JsonReader reader = Json.createReader(new StringReader(currStr));
        JsonObject data = reader.readObject();
        
        int code = 0;
         try {
			code = data.getInt("cod");
		} catch (Exception e) {
			code = Integer.parseInt(data.getString("cod"));
		}
        
        if(code == 200) {
        	weather.setData(data.getJsonArray("list").getJsonObject(0));
        	System.out.println(data.getJsonArray("list").getJsonObject(0));

        	System.out.println(weather.toString());
        	
		//	String message = weather.getName() +" at " +Util.formatDistance(Geoinfo.distance(lat, weather.getLaty(), lon,weather.getLonx()))+" miles.";
	        weather.setMessage(message+
	        		weather.getName() +" at " +Util.formatDistance(Geoinfo.distance(lat, weather.getLaty(), lon,weather.getLonx()))+" miles "
	        				+Util.formatAngle(Geoinfo.calculateAngle(lon, lat, weather.getLonx(), weather.getLaty())) +" degrees.");

		    System.out.println(weather.getMessage());
        	
        }
        reader.close();
		
	}
	
	public String getData(String urlstr) throws MalformedURLException, IOException {
		URL url = new URL(urlstr);
		URLConnection yc = url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
		String inputLine;
		String data = "";
		while ((inputLine = in.readLine()) != null)
			data += inputLine;
		in.close();
		return data;
	}
	
    public URL getCanonicalFile(String image) {
    	URL url = null;
		try {
			File background = new File(Info.weatherPath+image);
			//url = background.toURI().toURL();
			
			url = background.getCanonicalFile().toURI().toURL();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		

		return url;

    }

	public Weather getWeather() {
		return weather;
	}


}
