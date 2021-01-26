package net.metar;

import static it.octograve.jmetarparser.Condition.Descriptor.BLOWING;
import static it.octograve.jmetarparser.Condition.Descriptor.FREEZING;
import static it.octograve.jmetarparser.Condition.Descriptor.LOW_DRIFTING;
import static it.octograve.jmetarparser.Condition.Descriptor.PARTIAL;
import static it.octograve.jmetarparser.Condition.Descriptor.PATCHES;
import static it.octograve.jmetarparser.Condition.Descriptor.SHALLOW;
import static it.octograve.jmetarparser.Condition.Descriptor.SHOWERS;
import static it.octograve.jmetarparser.Condition.Descriptor.THUNDERSTORM;
import static it.octograve.jmetarparser.Condition.Phenomenon.DRIZZLE;
import static it.octograve.jmetarparser.Condition.Phenomenon.DUST_WHIRLS;
import static it.octograve.jmetarparser.Condition.Phenomenon.FOG;
import static it.octograve.jmetarparser.Condition.Phenomenon.FUNNEL_CLOUD;
import static it.octograve.jmetarparser.Condition.Phenomenon.HAIL;
import static it.octograve.jmetarparser.Condition.Phenomenon.HAZE;
import static it.octograve.jmetarparser.Condition.Phenomenon.ICE_CRYSTALS;
import static it.octograve.jmetarparser.Condition.Phenomenon.ICE_PELLETS;
import static it.octograve.jmetarparser.Condition.Phenomenon.MIST;
import static it.octograve.jmetarparser.Condition.Phenomenon.RAIN;
import static it.octograve.jmetarparser.Condition.Phenomenon.SAND;
import static it.octograve.jmetarparser.Condition.Phenomenon.SANDSTORM;
import static it.octograve.jmetarparser.Condition.Phenomenon.SMOKE;
import static it.octograve.jmetarparser.Condition.Phenomenon.SNOW;
import static it.octograve.jmetarparser.Condition.Phenomenon.SNOW_GRAINS;
import static it.octograve.jmetarparser.Condition.Phenomenon.SNOW_PELLETS;
import static it.octograve.jmetarparser.Condition.Phenomenon.SPRAY;
import static it.octograve.jmetarparser.Condition.Phenomenon.SQUALLS;
import static it.octograve.jmetarparser.Condition.Phenomenon.UNKNOWN;
import static it.octograve.jmetarparser.Condition.Phenomenon.VOLCANIC_ASH;
import static it.octograve.jmetarparser.Condition.Phenomenon.WIDEPREAD_DUST;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import com.cfg.common.Info;
import com.metar.decoder.Decoder;
import com.metar.download.Download;
import com.metar.download.ReadFile;
import com.util.Util;

import it.octograve.jmetarparser.Cloud;
import it.octograve.jmetarparser.Condition;
import it.octograve.jmetarparser.Condition.Descriptor;
import it.octograve.jmetarparser.Condition.Phenomenon;
import it.octograve.jmetarparser.Metar;
import it.octograve.jmetarparser.Temperature;

public class UtilityMetar implements Info{
	private static UtilityMetar instance = new UtilityMetar();
	
	private boolean isMetarLoaded; 
	File in = new File("data/metar/metar.txt");
	
	private String raw = null;
	private String metarDecoded = null;
	private String conditionCode = null;
	private String metarString = null; 
	
	private static long oneHourMillis = 3600000;
	//private static long oneHourMillis = 20000;

	private static Hashtable<String, Descriptor> descriptorTable = new Hashtable<String, Descriptor>();

	/** Association table for phenomena. */
	private static Hashtable<String, Phenomenon> phenomenaTable = new Hashtable<String, Phenomenon>();

	static {
		descriptorTable.put("MI", SHALLOW);
		descriptorTable.put("PR", PARTIAL);
		descriptorTable.put("BC", PATCHES);
		descriptorTable.put("DR", LOW_DRIFTING);
		descriptorTable.put("BL", BLOWING);
		descriptorTable.put("SH", SHOWERS);
		descriptorTable.put("TS", THUNDERSTORM);
		descriptorTable.put("FZ", FREEZING);

		phenomenaTable.put("DZ", DRIZZLE);
		phenomenaTable.put("RA", RAIN);
		phenomenaTable.put("SN", SNOW);
		phenomenaTable.put("SG", SNOW_GRAINS);
		phenomenaTable.put("IC", ICE_CRYSTALS);
		phenomenaTable.put("PL", ICE_PELLETS);
		phenomenaTable.put("GR", HAIL);
		phenomenaTable.put("GS", SNOW_PELLETS);
		phenomenaTable.put("BR", MIST);
		phenomenaTable.put("FG", FOG);
		phenomenaTable.put("FU", SMOKE);
		phenomenaTable.put("VA", VOLCANIC_ASH);
		phenomenaTable.put("DU", WIDEPREAD_DUST);
		phenomenaTable.put("SA", SAND);
		phenomenaTable.put("HZ", HAZE);
		phenomenaTable.put("PY", SPRAY);
		phenomenaTable.put("PO", DUST_WHIRLS);
		phenomenaTable.put("SQ", SQUALLS);
		phenomenaTable.put("FC", FUNNEL_CLOUD);
		phenomenaTable.put("SS", SANDSTORM);
		phenomenaTable.put("UP", UNKNOWN);
	}
	
	public static UtilityMetar getInstance(){

		return instance;
	}
	

	public String getConditionCode(String metar) {
		String conditionCode = null;
		ArrayList<Condition> conditions = Condition.parseConditions(metar);
		
		System.out.println(conditions.size());
		
		if (conditions.size() == 3) {
			conditionCode = conditions.get(2).toString();
		} else if (conditions.size() == 2) {
			conditionCode = conditions.get(1).toString();
		} if (conditions.size() == 1) {
			conditionCode = conditions.get(0).toString();
		} 
		
		try {
			System.out.println("coditions - "+conditions.get(0));
			conditionCode = conditions.get(0).toString();
			
		//	conditionCode = phenomenaTable.get(conditionCode.split(" ")[1]);
		} catch (Exception e) {
			System.out.println("condition code not find");//e.printStackTrace();
		}
		System.out.println(conditionCode);
		
		return conditionCode;
	}
	
	public String getRawMetar(String icao) {
		String metar = "";
		
		icao = icao.trim();
		icao = icao.toUpperCase();
		if (icao.equals("")) return "Please enter an airport ICAO code!";
		
		try {
			URL myConnection = new URL("https://tgftp.nws.noaa.gov/data/observations/metar/stations/"+icao+".TXT");
			URLConnection connectMe = myConnection.openConnection();
			          
			InputStreamReader lineReader = new InputStreamReader(connectMe.getInputStream());
			BufferedReader buffer = new BufferedReader(lineReader);
			
			String read = buffer.readLine();
			metar = read + "\n";
			
			read = buffer.readLine();
			metar += read;
			
			buffer.close();
			lineReader.close();
			
		}
		catch (Exception e)
		{
			//e.printStackTrace();
			metar = "An error occured while downloading the raw weather information from NOAA.";
			
		}
		
		this.metarString = metar;
		//System.out.println(metar);
		return metar;
	}

	public String getDecodedMetar(String icao) {
		String metar = "";
		
		icao = icao.trim();
		icao = icao.toUpperCase();
		if (icao.equals("")) return "Please enter an airport ICAO code!";
		
		try {
			URL myConnection = new URL("https://tgftp.nws.noaa.gov/data/observations/metar/decoded/"+icao+".TXT");
			URLConnection connectMe = myConnection.openConnection();
			          
			InputStreamReader lineReader = new InputStreamReader(connectMe.getInputStream());
			BufferedReader buffer = new BufferedReader(lineReader);
			String read = null;
			for(int i = 0; i<=15; i++)
			{
				read = buffer.readLine();
				if (i == 0) {
					read = "<div style=' border: 1px solid black; display: block; clear: left; font-size: medium; font-weight: bold; padding: 0pt 0pt;'>"
							+ "METAR for "+read;
				} else if (i == 1) {
					read += "</div>";
				} else {
					//read = Info.meteoline.replace("#value",read);
				}

				if (read == null) break;
				metar += read+"<br>";
			} 
			
			
			buffer.close();
			lineReader.close();
		}
		catch (Exception e)
		{
			//e.printStackTrace();
			metar = "An error occured while downloading the decoded weather information from NOAA.";
			
		}
		//System.out.println(metar);
		metarDecoded = metar;
		return metar;
	}
	

	public String getMetar (String icao) {
		ReadFile read = new ReadFile(in);
		Decoder decoder = new Decoder();
		
	//	validMetar();

		raw = null;
		metarDecoded = null;
		try {
			String allLine = decoder.saveAllLine(read, icao);
			raw = decoder.showRawMetar(allLine);
			metarDecoded = decoder.showDecoded(allLine);
			System.out.println(raw);
			System.out.println(metarDecoded);
			metarDecoded += "<br>"+raw;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println("metar error "+e);
		}
		
		return metarDecoded;

	}
	
	public String getMetarNoaa (final String icao) {
		ReadFile read = new ReadFile(in);
		Decoder decoder = new Decoder();
		
	//	validMetar();

		raw = null;
		metarDecoded = null;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					 URL oracle = new URL("https://tgftp.nws.noaa.gov/data/observations/metar/decoded/"+icao+".TXT");
				        URLConnection yc = oracle.openConnection();
				        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
				        String inputLine;
				        while ((inputLine = in.readLine()) != null) 
				            System.out.println(inputLine);
				        in.close();				
				        isMetarLoaded = true;
					
				} catch (Exception e) {
					//e.printStackTrace();
				}
				
			}
		});
		return metarDecoded;

	}

	
	private boolean isNeedUpdate() {
		 Date date = new Date();
         Path file = Paths.get(metarFileName);
         long lastModifiedTime = 0; 
         try {
			BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
			lastModifiedTime = attr.lastModifiedTime().toMillis();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

	      long timeMillisNow = date.getTime();

	      
	      return (timeMillisNow > oneHourMillis+lastModifiedTime) ;
	}
	
	public void validMetar() {
		
		if (isNeedUpdate()) {
			loadMetar();
		//	Util.pause(1000);
		} else {
			System.out.println("meta was loaded");
		}
	}

	
	private boolean loadMetar() {

	        try {

	            Path file = Paths.get(metarFileName);

	            BasicFileAttributes attr =
	                Files.readAttributes(file, BasicFileAttributes.class);

	            System.out.println("creationTime: " + attr.creationTime().toMillis());
	         //   String dateCreated = df.format(attr.creationTime().toMillis());

	            System.out.println("creationTime: " + attr.creationTime());
	            System.out.println("lastAccessTime: " + attr.lastAccessTime());
	            System.out.println("lastModifiedTime: " + attr.lastModifiedTime());
	            System.out.println("lastModifiedTime: " + attr.lastModifiedTime().toMillis());
	            
	            Util.getDateTime(attr);

	        } catch (IOException e) {
	           // e.printStackTrace();
	        }
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					String link = "https://aviationweather.gov/adds/dataserver_current/current/metars.cache.csv";
					//String link = "https://metar.vatsim.net/metar.php?id=all";
					File out = new File("data/metar/metar.txt");
					Download download = new Download(link, out);
					download.start();
					Decoder decoder = new Decoder();
					isMetarLoaded = true;
					
				} catch (Exception e) {
					//e.printStackTrace();
				}
				
			}
		});
		
		return isMetarLoaded;
		
	}
	
	public String detectCode() {
		String code = "";
		
		ArrayList<Cloud> clouds = new ArrayList<Cloud>();
	//	metarString = "RJOH 181700Z 29009G22KT 250V340 9999 -SH BR FEW010 SCT015 BKN030 02/M00 Q1021";

		
		System.out.println(metarString);

		try {
			Condition condition = new Condition(); 
			Metar metar = new Metar();
			metar.parseMetar(metarString);
			condition.parseConditions(metar.getDetailsString());
			
			
			code = condition.getConditionCode();
			

			if (code.length() > 2) {
				code = code.substring(code.length()-2, code.length());
			}
			
			if (code.contains("OK") || code.contains("KC") || code.contains("LR")|| code.contains("SC")|| code.contains("CD")) {
				code = "SKC";
			} else if (phenomenaTable.get(code) == null) {
				Cloud cloud = new Cloud();
				clouds = cloud.parseClouds(metarString);
			//	System.out.println(clouds.get(clouds.size()-1));
				try {
					code = clouds.get(clouds.size()-1).toString().substring(0,3);
				} catch (Exception e) {
					System.out.println(clouds);
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			code = "SKC";
		}
		
		
		System.out.println("condition code ->"+code);
		
		return code;
	}
	
    public String getImageWeather() {
    	
    	
    //	Util.listFilesForFolder(new File(Info.weatherPath+detectCode()));
    	
    	
    	URL url = null;
		try {
			File background = new File(Info.weatherPath+detectCode()+"/");
			//url = background.toURI().toURL();
			
			url = background.getCanonicalFile().toURI().toURL();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		//file:\C:\Users\Pierre\git\Planetiscalling\Planetiscalling\image\weather\SKC
		
		String folder = url.toString().replace("file:/", "").replace("\\", "/");
		

		return Util.listFilesForFolder(new File(folder));

    }
	

	public String getMetarDecoded() {
		return metarDecoded;
	}

	public void setMetarDecoded(String metarDecoded) {
		this.metarDecoded = metarDecoded;
	}
	  

   
}
