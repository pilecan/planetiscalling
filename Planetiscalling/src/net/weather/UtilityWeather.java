package net.weather;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.json.simple.JSONObject;

import com.cfg.common.Info;
import com.geo.util.Geoinfo;
import com.model.Airport;
import com.model.City;
import com.model.Landmark;
import com.model.LegPoint;
import com.model.Mountain;
import com.model.Weather;
import com.util.Util;

import test.OpenStreetMapUtils;

public class UtilityWeather implements Info {
	private static UtilityWeather instance = new UtilityWeather();

	private Weather weather;
	
	public static UtilityWeather getInstance() {

		return instance;
	}

	public void searchCityWeather(Object object) {
		double lat = 0;
		double lon = 0;
		String name = "";
		String location = "";
		if (object instanceof City) {
			lon = ((City)object).getLonx();
			lat = ((City)object).getLaty();
			name = ((City)object).getCityAscii(); 
			location = ((City)object).getIso2(); 
		} else if (object instanceof Landmark) {
			lon = ((Landmark)object).getLonx();
			lat = ((Landmark)object).getLaty();
			name = ((Landmark)object).getGeoName(); 
			location = ((City)object).getAdminName(); 
		} 
		
		 try {
			callOpenweatherName(name, location);
		} catch (Exception e) {
			try {
				callOpenweathermapObject(object);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			}
		}
	}

	public void callOpenweathermapId(long id) {
        String currStr = null;
        weather = new Weather();
		weather.setSamePlace(false);
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
        	weather.setData(data);
        	
        	//System.out.println(weather.toString());
           // currDataBuilder.add(data);
        }
        reader.close();
		
	}
	public void callOpenweatherName(String cityName, String country) throws NullPointerException {
        String currStr = null;
        weather = new Weather();

		try {
			currStr = getData("http://api.openweathermap.org/data/2.5/weather?q=" + cityName+","+country +"&APPID=3506dfa8bbebf7709e6fba904a68559a");
			weather.setSamePlace(true);
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
        JsonReader reader = null;
		reader = Json.createReader(new StringReader(currStr));
		JsonObject data = reader.readObject();
		
		int code = 0;
		try {
			code = data.getInt("cod");
		} catch (Exception e) {
			code = Integer.parseInt(data.getString("cod"));
			}
  
			if(code == 200) {
			weather.setData(data);
		}
        reader.close();
		
	}
	
	public void callOpenweathermapObject(Object object) throws Exception {
		double lat = 0;
		double lon = 0;
		String name = "";
		String message = "";
        weather = new Weather();

		if (object instanceof City) {
			lon = ((City)object).getLonx();
			lat = ((City)object).getLaty();
			name = ((City)object).getCityAscii(); 
			message = "No weather station at "+name+" but found it at ";
			weather.setSamePlace(false);
		} else if (object instanceof Airport) {
			lon = ((Airport)object).getLonx();
			lat = ((Airport)object).getLaty();
			name = ((Airport)object).getName(); 
			message = "No METAR at "+name+" Airport but Weather Station found at ";
			weather.setSamePlace(false);

		} else if (object instanceof Mountain) {
			lon = ((Mountain)object).getLonx();
			lat = ((Mountain)object).getLaty();
			name = ((Mountain)object).getName(); 
			message = "Weather Station found at ";
			weather.setSamePlace(false);

		}  else if (object instanceof LegPoint) {
			lon = ((LegPoint)object).getLonx();
			lat = ((LegPoint)object).getLaty();
			name = ((LegPoint)object).getIcaoIdent(); 
			message = "Weather Station found at ";
			weather.setSamePlace(false);

		}  else if (object instanceof Landmark) {
			lon = ((Landmark)object).getLonx();
			lat = ((Landmark)object).getLaty();
			name = ((Landmark)object).getGeoName(); 
			message = "No weather station at "+name+" but found it at ";
			weather.setSamePlace(false);
		} 
		
        String currStr = null;
        JsonObject data = null;
        JsonReader reader = null;
        
		try {
			currStr = getData("http://api.openweathermap.org/data/2.5/find?lat="+lat+"&lon="+lon+"&cnt=1&APPID=3506dfa8bbebf7709e6fba904a68559a");
		     reader = Json.createReader(new StringReader(currStr));
		     data = reader.readObject();
		} catch (Exception e) {
		}
         
        int code = 0;
         try {
			code = data.getInt("cod");
		} catch (Exception e) {
			code = Integer.parseInt(data.getString("cod"));
		}
        
        if(code == 200) {
        	weather.setData(data.getJsonArray("list").getJsonObject(0));
	        weather.setMessage(message+
	        		weather.getName() +" at " +Util.formatDistance(Geoinfo.distance(lat, weather.getLaty(), lon,weather.getLonx()))+" nm "
	        				+Util.formatAngle(Geoinfo.calculateAngle(lon, lat, weather.getLonx(), weather.getLaty())) +" degrees.");
        }
        reader.close();
		
	}
	public void callOpenStreetmapObject(Object object) throws Exception {
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

		}  else if (object instanceof LegPoint) {
			lon = ((LegPoint)object).getLonx();
			lat = ((LegPoint)object).getLaty();
			name = ((LegPoint)object).getIcaoIdent(); 
			message = "Weather Station found at ";

		}  else if (object instanceof Landmark) {
			lon = ((Landmark)object).getLonx();
			lat = ((Landmark)object).getLaty();
			name = ((Landmark)object).getGeoName(); 
			message = "No weather station at "+name+" but found it at ";
		} 
		
        String currStr = null;
        JsonObject data = null;
        JsonReader reader = null;
        
		try {
			currStr ="https://nominatim.openstreetmap.org/reverse?format=json&lat="+lat+"&lon="+lon;
		     OpenStreetMapUtils.getInstance().getRequest(currStr);
		     
		    // jsonObject.
		    
		} catch (Exception e) {
		}
         
		System.out.println("openmap = "+data);
		System.out.println("https://nominatim.openstreetmap.org/reverse?format=json&lat="+lat+"&lon="+lon);
         reader.close();
		
	}
	
	public String getData(String urlstr) throws MalformedURLException, IOException {
		URL url = new URL(urlstr);
		URLConnection yc = url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream(),StandardCharsets.UTF_8));
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
