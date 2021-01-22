package com.util;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.cfg.common.Info;

public class Util implements Info {

	private static String decoration = "style=\"a:visited:color:red; text-decoration: none;background-color:white;\" onMouseOver=\"this.style.backgroundColor='#999999'\" onMouseOut=\"this.style.backgroundColor='#FFFFFF'\"";
	private static String decorationFake = "style=\"text-decoration: none;background-color:gray;\" onMouseOver=\"this.style.backgroundColor='#999999'\" onMouseOut=\"this.style.backgroundColor='#FFFFFF'\"";

	public static String aviationWeather = " <a "
			+ decoration
			+ " href=\"http://www.aviationweather.gov/adds/tafs?station_ids=xxxx&std_trans=translated&submit_taf=Get+TAFs\">(Weather)"
			+ "</a>";
	public static String windyWeather = " <a "
			+ decoration
			+ " href=\"https://www.windy.com/xxxx\">(windy.com)"
			+ "</a>";
	public static String flightradar24 = " <a "
			+ decoration
			+ " http://www.flightradar24.com/data/airports/xxxx\">(flightradar24.com)"
			+ "</a>";

	static private String[] WEB_PAGE = {
			"https://www.google.com/search?q=",
			"https://www.google.com/maps/search/",
			"http://www.flightradar24.com/data/airports/",
			"http://www.checkplane.com",
			"http://www.fslauncher.com",
			"http://en.wikipedia.org/wiki/Special:Search?search=",
			"http://library.avsim.net/search.php?CatID=root&SearchTerm=xxxx&Sort=Added&ScanMode=1",
			"http://www.freewarescenery.com/" };

	public static Map<String, String> PREFS_MAP = new HashMap<String, String>();
	static {
		PREFS_MAP.put("Microsoft Flight Simulator 9", "fs9");
		PREFS_MAP.put("Microsoft Flight Simulator X", "fsx");
		PREFS_MAP.put("Prepar3D", "p3d");
		PREFS_MAP.put("Prepar3D v2", "p3d2");
		PREFS_MAP.put("Prepar3D v3", "p3d3");
		PREFS_MAP.put("Prepar3D v4", "p3d4");
		Collections.unmodifiableMap(PREFS_MAP);
	}


	static final String[] browsers = { "x-www-browser", "google-chrome", "firefox", "opera", "epiphany", "konqueror",
			"conkeror", "midori", "kazehakase", "mozilla" };
	static final String errMsg = "Error attempting to launch web browser";
	

	public static void openURL(String url) {
		try { // attempt to use Desktop library from JDK 1.6+
			Class<?> d = Class.forName("java.awt.Desktop");
			d.getDeclaredMethod("browse", new Class<?>[] { java.net.URI.class })
					.invoke(d.getDeclaredMethod("getDesktop").invoke(null), new Object[] { java.net.URI.create(url) });
		} catch (Exception ignore) { // library not available or failed
			String osName = System.getProperty("os.name");
			try {
				if (osName.startsWith("Mac OS")) {
					Class.forName("com.apple.eio.FileManager")
							.getDeclaredMethod("openURL", new Class<?>[] { String.class })
							.invoke(null, new Object[] { url });
				} else if (osName.startsWith("Windows"))
					Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
				else { // assume Unix or Linux
					String browser = null;
					for (String b : browsers)
						if (browser == null
								&& Runtime.getRuntime().exec(new String[] { "which", b }).getInputStream().read() != -1)
							Runtime.getRuntime().exec(new String[] { browser = b, url });
					if (browser == null)
						throw new Exception(Arrays.toString(browsers));
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, errMsg + "\n" + e.toString());
			}
		}
	}

	
	
	private static Map<String, String> COUNTRY_MAP = new HashMap<String, String>();
	static {
		COUNTRY_MAP.put("dominican depublic", "dominicanrepublic");
		COUNTRY_MAP.put("new zealand", "newzealand");
		COUNTRY_MAP.put("australia", "australia");
		COUNTRY_MAP.put("denmark", "denmark");
		COUNTRY_MAP.put("czech republic", "czech");
		COUNTRY_MAP.put("alaska", "alaska");
		COUNTRY_MAP.put("costa rica", "costarica");
		COUNTRY_MAP.put("south Africa", "southafrica");
		COUNTRY_MAP.put("north Korea", "northkorea");
		COUNTRY_MAP.put("korea", "southkorea");
		COUNTRY_MAP.put("united kingom", "uk");
		COUNTRY_MAP.put("united states", "us");
		COUNTRY_MAP.put("spain", "spain");
		COUNTRY_MAP.put("antilles", "antilles");
		COUNTRY_MAP.put("argentina", "argentina");
		COUNTRY_MAP.put("austria", "austria");
		COUNTRY_MAP.put("bahamas", "bahamas");
		COUNTRY_MAP.put("belgium", "belgium");
		COUNTRY_MAP.put("brazil", "brazil");
		COUNTRY_MAP.put("canada", "canada");
		COUNTRY_MAP.put("chile", "chile");
		COUNTRY_MAP.put("china", "china");
		COUNTRY_MAP.put("colombia", "colombia");
		COUNTRY_MAP.put("denmark", "denmark");
		COUNTRY_MAP.put("dominicanrepublic", "dominicanrepublic");
		COUNTRY_MAP.put("estonia", "estonia");
		COUNTRY_MAP.put("finland", "finland");
		COUNTRY_MAP.put("france", "france");
		COUNTRY_MAP.put("germany", "germany");
		COUNTRY_MAP.put("greece", "greece");
		COUNTRY_MAP.put("guatemala", "guatemala");
		COUNTRY_MAP.put("hawaii", "hawaii");
		COUNTRY_MAP.put("hungary", "hungary");
		COUNTRY_MAP.put("india", "india");
		COUNTRY_MAP.put("indonesia", "indonesia");
		COUNTRY_MAP.put("ireland", "ireland");
		COUNTRY_MAP.put("israel", "israel");
		COUNTRY_MAP.put("italy", "italy");
		COUNTRY_MAP.put("japan", "japan");
		COUNTRY_MAP.put("mexico", "mexico");
		COUNTRY_MAP.put("netherlands", "netherlands");
		COUNTRY_MAP.put("newzealand", "newzealand");
		COUNTRY_MAP.put("norway", "norway");
		COUNTRY_MAP.put("poland", "poland");
		COUNTRY_MAP.put("portugal", "portugal");
		COUNTRY_MAP.put("russia", "russia");
		COUNTRY_MAP.put("spain", "spain");
		COUNTRY_MAP.put("sweden", "sweden");
		COUNTRY_MAP.put("switzerland", "switzerland");
		COUNTRY_MAP.put("turkey", "turkey");
		COUNTRY_MAP.put("ukraine", "ukraine");
		COUNTRY_MAP.put("venezuela", "venezuela");
		COUNTRY_MAP.put("puerto rico", "puertorico");

		Collections.unmodifiableMap(COUNTRY_MAP);
	}
	
    public static Map<String, Integer> DAY_PERIOD = new HashMap<String, Integer>();
 	static {
 		DAY_PERIOD.put("Morning", new Integer(0));
 		DAY_PERIOD.put("Afternoon", new Integer(1));
 		DAY_PERIOD.put("Evening", new Integer(2));
 		DAY_PERIOD.put("Night", new Integer(3));

 		Collections.unmodifiableMap(DAY_PERIOD);
 	}

	public static Map<String, String> REGION_MAP = new HashMap<String, String>();
	static {
		REGION_MAP.put("AG","Solomon Islands");
		REGION_MAP.put("AN","Nauru Islands");
		REGION_MAP.put("AY","Papua New Guinea");
		REGION_MAP.put("BG","Greenland");
		REGION_MAP.put("BI","Iceland");
		REGION_MAP.put("CY","Canada");
		REGION_MAP.put("DA","Algeria");
		REGION_MAP.put("DB","Benin");
		REGION_MAP.put("DF","Burkina Faso");
		REGION_MAP.put("DG","Ghana");
		REGION_MAP.put("DI","Côte D’Ivoire");
		REGION_MAP.put("DN","Nigeria");
		REGION_MAP.put("DR","Niger");
		REGION_MAP.put("DT","Tunisia");
		REGION_MAP.put("DX","Togo");
		REGION_MAP.put("EB","Belgium");
		REGION_MAP.put("ED","Germany");
		REGION_MAP.put("EE","Estonia");
		REGION_MAP.put("EF","Finland");
		REGION_MAP.put("EG","United Kingdom");
		REGION_MAP.put("EH","Netherlands");
		REGION_MAP.put("EI","Ireland");
		REGION_MAP.put("EK","Denmark");
		REGION_MAP.put("EL","Luxembourg");
		REGION_MAP.put("EN","Norway");
		REGION_MAP.put("EP","Poland");
		REGION_MAP.put("ES","Sweden");
		REGION_MAP.put("ET","Germany - Military");
		REGION_MAP.put("EV","Latvia");
		REGION_MAP.put("EY","Lithuania");
		REGION_MAP.put("FA","South Africa");
		REGION_MAP.put("FB","Botswana");
		REGION_MAP.put("FC","Congo (Brazzaville)");
		REGION_MAP.put("FC","Congo (Kinshasa)");
		REGION_MAP.put("FD","Swaziland");
		REGION_MAP.put("FE","Central African Republic");
		REGION_MAP.put("FG","Equatorial Guinea");
		REGION_MAP.put("FH","Ascension Islands");
		REGION_MAP.put("FI","Mauritius");
		REGION_MAP.put("FJ","Diego Garcia");
		REGION_MAP.put("FK","Cameroon");
		REGION_MAP.put("FL","Zambia");
		REGION_MAP.put("FM","Madagascar");
		REGION_MAP.put("FN","Angola");
		REGION_MAP.put("FO","Gabon");
		REGION_MAP.put("FP","Sao Tome");
		REGION_MAP.put("FQ","Mozambique");
		REGION_MAP.put("FS","Seychelles");
		REGION_MAP.put("FT","Chad");
		REGION_MAP.put("FV","Zimbabwe");
		REGION_MAP.put("FW","Malawi");
		REGION_MAP.put("FX","Lesotho");
		REGION_MAP.put("FY","Namibia");
		REGION_MAP.put("FZ","Democratic Republic of Congo");
		REGION_MAP.put("GA","Mali");
		REGION_MAP.put("GB","Gambia");
		REGION_MAP.put("GC","Canary Islands");
		REGION_MAP.put("GE","Melilla");
		REGION_MAP.put("GF","Sierra Leone");
		REGION_MAP.put("GG","Guinea-Bissau");
		REGION_MAP.put("GL","Liberia");
		REGION_MAP.put("GM","Morocco");
		REGION_MAP.put("GO","Senegal");
		REGION_MAP.put("GQ","Mauritania");
		REGION_MAP.put("GU","Guinea");
		REGION_MAP.put("GV","Cabo Verde");
		REGION_MAP.put("HA","Ethiopia");
		REGION_MAP.put("HB","Burundi");
		REGION_MAP.put("HD","Djibouti");
		REGION_MAP.put("HE","Egypt");
		REGION_MAP.put("HH","Eritrea");
		REGION_MAP.put("HK","Kenya");
		REGION_MAP.put("HL","Libya");
		REGION_MAP.put("HR","Rwanda");
		REGION_MAP.put("HS","South Sudan");
		REGION_MAP.put("HT","Tanzania");
		REGION_MAP.put("HU","Uganda");
		REGION_MAP.put(null,"Colorado");
		REGION_MAP.put("K1","Washington");
		REGION_MAP.put("K2","Arizona");
		REGION_MAP.put("K3","Missouri");
		REGION_MAP.put("K4","Louisiana");
		REGION_MAP.put("K5","Ohio");
		REGION_MAP.put("K6","New York");
		REGION_MAP.put("K7","Florida");
		REGION_MAP.put("LA","Albania");
		REGION_MAP.put("LB","Bulgaria");
		REGION_MAP.put("LC","Cyprus");
		REGION_MAP.put("LD","Croatia");
		REGION_MAP.put("LE","Spain");
		REGION_MAP.put("LF","France");
		REGION_MAP.put("LG","Greece");
		REGION_MAP.put("LH","Hungary");
		REGION_MAP.put("LI","Italy");
		REGION_MAP.put("LJ","Slovenia");
		REGION_MAP.put("LK","Czechia");
		REGION_MAP.put("LL","Israel");
		REGION_MAP.put("LM","Malta");
		REGION_MAP.put("LO","Austria");
		REGION_MAP.put("LP","Portugal");
		REGION_MAP.put("LQ","Bosnia And Herzegovina");
		REGION_MAP.put("LR","Romania");
		REGION_MAP.put("LS","Switzerland");
		REGION_MAP.put("LT","Turkey");
		REGION_MAP.put("LU","Moldova");
		REGION_MAP.put("LW","Macedonia");
		REGION_MAP.put("LX","Gibralter");
		REGION_MAP.put("LY","Serbia");
		REGION_MAP.put("LZ","Slovakia");
		REGION_MAP.put("MB","Turks and Caicos Islands");
		REGION_MAP.put("MD","Dominican Republic");
		REGION_MAP.put("MG","Guatemala");
		REGION_MAP.put("MH","Honduras");
		REGION_MAP.put("MK","Jamaica");
		REGION_MAP.put("MM","Mexico");
		REGION_MAP.put("MN","Nicaragua");
		REGION_MAP.put("MP","Panama");
		REGION_MAP.put("MR","Costa Rica");
		REGION_MAP.put("MS","El Salvador");
		REGION_MAP.put("MT","Haiti");
		REGION_MAP.put("MU","Cuba");
		REGION_MAP.put("MW","Cayman Islands");
		REGION_MAP.put("MY","Bahamas");
		REGION_MAP.put("MZ","Belize");
		REGION_MAP.put("NC","Cook Islands");
		REGION_MAP.put("NF","Fiji");
		REGION_MAP.put("NG","Kiribati Islands");
		REGION_MAP.put("NI","Niue Islands");
		REGION_MAP.put("NL","Wallis Islands");
		REGION_MAP.put("NS","American Samoa");
		REGION_MAP.put("NT","Tahiti");
		REGION_MAP.put("NV","Vanuatu");
		REGION_MAP.put("NW","Noumea");
		REGION_MAP.put("NZ","New Zealand");
		REGION_MAP.put("OA","Afghanistan");
		REGION_MAP.put("OB","Bahrain");
		REGION_MAP.put("OE","Saudi Arabia");
		REGION_MAP.put("OI","Iran");
		REGION_MAP.put("OJ","Jordan");
		REGION_MAP.put("OK","Kuwait");
		REGION_MAP.put("OL","Lebanon");
		REGION_MAP.put("OM","United Arab Emirates");
		REGION_MAP.put("OO","Oman");
		REGION_MAP.put("OP","Pakistan");
		REGION_MAP.put("OR","Iraq");
		REGION_MAP.put("OS","Syria");
		REGION_MAP.put("OT","Qatar");
		REGION_MAP.put("OY","Yemen");
		REGION_MAP.put("PA","Alaska");
		REGION_MAP.put("PG","Guam");
		REGION_MAP.put("PH","Hawaii");
		REGION_MAP.put("PJ","Johnston Islands");
		REGION_MAP.put("PK","Marshall Islands");
		REGION_MAP.put("PL","Line Islands");
		REGION_MAP.put("PM","Midway Islands");
		REGION_MAP.put("PT","Micronesia");
		REGION_MAP.put("PW","Wake Islands");
		REGION_MAP.put("RC","Taiwan");
		REGION_MAP.put("RJ","Japan");
		REGION_MAP.put("RK","Korea North");
		REGION_MAP.put("RK","Korea South");
		REGION_MAP.put("RO","Okinawa Islands");
		REGION_MAP.put("RP","Philippines");
		REGION_MAP.put("SA","Argentina");
		REGION_MAP.put("SB","Brazil");
		REGION_MAP.put("SC","Chile");
		REGION_MAP.put("SE","Ecuador");
		REGION_MAP.put("SG","Paraguay");
		REGION_MAP.put("SK","Colombia");
		REGION_MAP.put("SL","Bolivia");
		REGION_MAP.put("SM","Suriname");
		REGION_MAP.put("SO","French Guiana");
		REGION_MAP.put("SP","Peru");
		REGION_MAP.put("SU","Uruguay");
		REGION_MAP.put("SV","Venezuela");
		REGION_MAP.put("SY","Guyana");
		REGION_MAP.put("TA","Antigua And Barbuda");
		REGION_MAP.put("TB","Barbados");
		REGION_MAP.put("TD","Dominica");
		REGION_MAP.put("TF","French Antilles");
		REGION_MAP.put("TG","Grenada");
		REGION_MAP.put("TI","U.S. Virgin Islands");
		REGION_MAP.put("TJ","Puerto Rico");
		REGION_MAP.put("TK","St. Kitts Islands");
		REGION_MAP.put("TL","St. Lucia");
		REGION_MAP.put("TN","Aruba");
		REGION_MAP.put("TQ","Anguilla");
		REGION_MAP.put("TT","Monserrat Islands");
		REGION_MAP.put("TU","Trinidad And Tobago");
		REGION_MAP.put("TV","U.K Virgin Islands");
		REGION_MAP.put("TX","Bermuda");
		REGION_MAP.put("UA","Kazakhstan");
		REGION_MAP.put("UA","Kyrgyzstan");
		REGION_MAP.put("UB","Azerbaijan");
		REGION_MAP.put("UE","Russia");
		REGION_MAP.put("UG","Georgia");
		REGION_MAP.put("UG","Armenia");
		REGION_MAP.put("UH","Russia");
		REGION_MAP.put("UI","Russia");
		REGION_MAP.put("UK","Moldovia");
		REGION_MAP.put("UK","Ukraine");
		REGION_MAP.put("UL","Russia");
		REGION_MAP.put("UM","Belarus");
		REGION_MAP.put("UM","Latvia");
		REGION_MAP.put("UM","Lithuania");
		REGION_MAP.put("UN","Russia");
		REGION_MAP.put("UR","Russia");
		REGION_MAP.put("US","Russia");
		REGION_MAP.put("UT","Uzbekistan");
		REGION_MAP.put("UT","Turkmenistan");
		REGION_MAP.put("UT","Tajikistan");
		REGION_MAP.put("UU","Russia");
		REGION_MAP.put("UW","Russia");
		REGION_MAP.put("VA","India - West");
		REGION_MAP.put("VC","Sri Lanka");
		REGION_MAP.put("VD","Cambodia");
		REGION_MAP.put("VE","India - East");
		REGION_MAP.put("VG","Bangladesh");
		REGION_MAP.put("VH","Hongkong");
		REGION_MAP.put("VI","India - North");
		REGION_MAP.put("VL","Laos");
		REGION_MAP.put("VM","Macau");
		REGION_MAP.put("VN","Nepal");
		REGION_MAP.put("VO","India");
		REGION_MAP.put("VQ","Bhutan");
		REGION_MAP.put("VR","Maldives");
		REGION_MAP.put("VT","Thailand");
		REGION_MAP.put("VV","Vietnam");
		REGION_MAP.put("VY","Burma");
		REGION_MAP.put("WA","Indonesia");
		REGION_MAP.put("WB","Brunei");
		REGION_MAP.put("WI","Indonesia");
		REGION_MAP.put("WM","Malaysia");
		REGION_MAP.put("WR","Indonesia - Bali");
		REGION_MAP.put("WS","Singapore");
		REGION_MAP.put("YB","Australia");
		REGION_MAP.put("YM","Australia");
		REGION_MAP.put("ZB","China");
		REGION_MAP.put("ZG","China");
		REGION_MAP.put("ZH","China");
		REGION_MAP.put("ZK","North Korea");
		REGION_MAP.put("ZL","China");
		REGION_MAP.put("ZM","Mongolia");
		REGION_MAP.put("ZP","China");
		REGION_MAP.put("ZS","China");
		REGION_MAP.put("ZU","China");
		REGION_MAP.put("ZW","China");
		REGION_MAP.put("ZY","China");
		
		Collections.unmodifiableMap(REGION_MAP);
	}


	public static String formatMagvar(Double value){
		 DecimalFormat myFormatter = new DecimalFormat("0.0");
		 return myFormatter.format(Math.abs(value))+(value <0?"&deg; WEST":"&#176; EAST");
	}
	
	public static String formatFrequency(Integer value){
		 Double result = new Double(value)/1000;
		 DecimalFormat myFormatter = new DecimalFormat("000.000");
		 return myFormatter.format(result);
   }
	
	public static String formatVorFrequency(Integer value){
		 Double result = new Double(value)/1000;
		 DecimalFormat myFormatter = new DecimalFormat("000.00");
		 return myFormatter.format(result);
   }
	
	public static String formatNdbFrequency(Integer value){
		 Double result = new Double(value)/100;
		 DecimalFormat myFormatter = new DecimalFormat("0000.00");
		 return myFormatter.format(result);
    }
	
	public static String formatAltitude(Double value){
		 DecimalFormat myFormatter = new DecimalFormat("000000.00");
		 return myFormatter.format(value).replace(",", ".");
  }

	public static String formatDistance(Double value){
		 DecimalFormat myFormatter = new DecimalFormat("0.0");
		 return myFormatter.format(value).replace(",", ".");
  }

	public static String formatRain(Double value){
		 DecimalFormat myFormatter = new DecimalFormat("0.00");
		 return myFormatter.format(value).replace(",", ".");
 }
	
	public static String formatTimeZone(Double hourZone) {
		DecimalFormat myFormatter = new DecimalFormat("00.00");
		String hz = myFormatter.format(hourZone);

		if (!hz.contains("-")) {
			hz = "+" + hz;
		}
		return "UTC" + hz.replace(".", ":");
	}
	public static String formatGPS(Double coord) {
		DecimalFormat myFormatter = new DecimalFormat("0.000000");
		return myFormatter.format(coord);
	}

	public static String formatGPS(String coord) {
		String[] array = coord.split(",");
		return formatGPS(Double.valueOf(array[0]))+","+formatGPS(Double.valueOf(array[1]));
		
	}

	public static String formatHeading(Double heading, Double magvar, boolean inverse){
		int aux = (int)(magvar*100);
		double result = aux/100d;

		
		int val = (int) (heading - (inverse?180:0)-result);
		return Integer.toString(val);
	}

	public static String formatLenght(Integer length,Integer width, String surface){
		
		return Integer.toString(length)+"ft ("+(int) Math.round(length/3.28084)+"M) X "+Integer.toString(width)+"ft ("+surface+")";
	}
	

	private static String makeAbbreviation(String text) {
		
		String[] line = text.split(" ");
		//System.out.println(text);
		try {
			text = line[0].substring(0,1)+line[1].substring(0,1)+line[2].substring(0,1);
		} catch (ArrayIndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return text;
		
	}
	
	
	public static void getDateTime(BasicFileAttributes attr) {
		TimeZone timeZone = null ;
		timeZone = TimeZone.getTimeZone("UTC");

		
		Calendar calUTC = new GregorianCalendar(timeZone);
        int year = calUTC.get(Calendar.DAY_OF_MONTH);
        int month = calUTC.get(Calendar.MONTH);
        int day = calUTC.get(Calendar.DAY_OF_MONTH);
        int hour = calUTC.get(Calendar.HOUR_OF_DAY);
        int min = calUTC.get(Calendar.MINUTE);
        int sec = calUTC.get(Calendar.SECOND);
        
        System.out.println(String.format("%02d",hour) + ":" +  String.format("%02d",min) + ":" + String.format("%02d",sec));
        
   	
        //return String.format("%02d",hour) + ":" +  String.format("%02d",min) + ":" + String.format("%02d",sec);
        //return String.format("%02d",hour) + ":" +  String.format("%02d",min);

	}
	
	public static String getTime(String where ) {
		TimeZone timeZone = null ;
		if ("local".equals(where)){
	    	timeZone = TimeZone.getDefault();
		} else if ("UTC".equals(where)){
			timeZone = TimeZone.getTimeZone("UTC");
		}

		
		Calendar calUTC = new GregorianCalendar(timeZone);
        int hour = calUTC.get(Calendar.HOUR_OF_DAY);
        int min = calUTC.get(Calendar.MINUTE);
        int sec = calUTC.get(Calendar.SECOND);
        
        if (min ==0 && sec == 0 ) {
        	System.out.println(getPeriod());
        }
	
        //return String.format("%02d",hour) + ":" +  String.format("%02d",min) + ":" + String.format("%02d",sec);
        return String.format("%02d",hour) + ":" +  String.format("%02d",min);

		
	}
	
	  public static String getPeriod() {
		  String dayPeriod = "";
		  String[] currentTimes = getTime("local").split(":");
		  int hour = Integer.parseInt(currentTimes[0]);
		  
/*		  String[] periods = storePeriod.split(":");
		  int min = Integer.parseInt(periods[0]);
		  int max = Integer.parseInt(periods[1]);
*/		  
		  if (hour >= 6 && hour < 12 ) {
			  dayPeriod = "Morning";
		  } else if (hour >= 12 && hour < 17 ) {
			  dayPeriod = "Afternoon";
		  }else if (hour >= 17 && hour < 20 ) {
			  dayPeriod = "Evening";
		  }else if (hour >= 20 || hour >= 0) {
			  dayPeriod = "Night";
		  }
		  
		  return dayPeriod;
	  }

	  public static int getStoredPeriodNumber() {
		  String storePeriod = Utility.getInstance().getPrefs().getProperty("day.period");
		  return DAY_PERIOD.get(storePeriod);
	  }
	  
	  public static String validgetDisplayName(String name) {
		  
		  int ind = name.indexOf("(");
		  
		  if (ind != -1) {
			  name = name.substring(0,ind-1);
		  }
		  
		  return name.trim();
	  }

	 public static String getTimer(long millis) {
 	    String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
 	            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
 	            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
 	    //System.out.println(hms);
 	    
 	    return hms;
		 
	 }
	public static String formatMessageError(String message) {
		if (message.length() > 40) {
			message = message.substring(0, 40) + "...";
		}

		return message;
	}
	
	public static Double modifyAltitude(Double cruzeAltitude, Double pointAltitude, Double newCruizeAltitude) {
		//System.out.println(pointAltitude/cruzeAltitude*newCruizeAltitude);
		return (pointAltitude/cruzeAltitude)*newCruizeAltitude;
	}

    
	private static String avoidDouble(String icao){
		
		return null;
	}

	private static String formatDescription(String description) {
		String result = "";

		String[] descs = description.split("\\|");
		for (int i = 1; i < descs.length; i++) {
			result = result.concat(descs[i] + " - ");
			// System.out.println(result);
			if (i > 3)
				break;
		}
		return result.substring(0, result.length() - 2);
	}

	public static String addZero(int i) {
		String areaNum = "";
		if (i < 10) {
			areaNum = "00" + i;
		} else if (i < 100) {
			areaNum = "0" + i;
		} else {
			areaNum = "" + i;
		}

		return areaNum;
	}

	public static String formatAreaNum(String num, int max) {
		String formatted;
		int i = Integer.parseInt(num);

		i = (max - i) + 1;

		
		if (max > 999) {
			formatted = String.format("%04d", i);
		} else {
			formatted = String.format("%03d", i);
		}		 
		
		//formatted = String.format("%04d", i);

		return formatted;
	}
	
	public static String formatAreaNumBase(String num, int max) {
		String formatted;
		int i = Integer.parseInt(num);

//		i = (max - i) + 1;

		
		if (max > 999) {
			formatted = String.format("%04d", i);
		} else {
			formatted = String.format("%03d", i);
		}		 
		
		//formatted = String.format("%04d", i);

		return formatted;
	}

	
	public static String convertP3DLine(String line){
		if (line == null){
			return null;
		}
		char[] c_arr = line.toCharArray();
		
		StringBuffer sb = new StringBuffer();
		for (char car :c_arr){
			int value = (int)car;
			if (value > 31 && value < 256 || value==13){
				sb.append(car);
			}
		}
		return sb.toString();
	}
	/*
	 * Extract last element of path
	 * 
	 */
	public static String extractLastElement(String str){
		return str.substring(str.lastIndexOf("\\")+1);
	}
	
	public static String extractLastPath(String str){
		str = str.replace("\\", "/");
		return str.substring(str.lastIndexOf("/")+1);
	}
	
	public static String extractBeforeLastElement(String str){
		String lastElement = extractLastElement(str);
		return str.substring(0, str.length()-lastElement.length());
		//return str.replace(lastElement, "");
	}

	public static String extractBeforeLastElement2(String str){
		int index = str.lastIndexOf(extractLastElement(str));
		String elementBefore = str;
		if (index != -1){
			elementBefore = str.substring(0,index-1);
		}
		return extractLastElement(elementBefore);
	}

	public static String extractLocal(String str){
		String lastElement = extractLastElement(str);
		return str.substring(0, str.length()-lastElement.length()-1);
		//return str.replace(lastElement, "");
	}

	
	public static String createSceneryRoot(){
		
		return "";
	}
	
	
	public static synchronized void reStartFLS(){
		try {
			pause(80);
			Runtime.getRuntime().exec("FSLaunchPad.exe");
			System.exit(0);
		} catch (IOException er) {
			System.out.println(er);
		}
		
	}
	
	public static String createPrefsFileName(Properties prefs){
		return "preferences_"+PREFS_MAP.get(extractLastElement(prefs.getProperty("fsRoot")))+".properties";
	}
	
	
	
	/*public static String formatAreaNum(int num, int max) {
		String formatted;

		formatted = String.format("%04d", num);
		
		 * if ( max > 999){ formatted = String.format("%04d", num); } else {
		 * formatted = String.format("%03d", num); }
		 

		return formatted;
	}
*/
	public static String createHref(String title, String search, int numPage) {
		if (numPage == 7) {
			if (COUNTRY_MAP.get(search) != null) {
				search = COUNTRY_MAP.get(search);
				search = "fsx/" + search + ".html";
			} else {
				search = "fsx.html#" + search;
			}
		}

		return "<a " + decoration + " href=\"" + WEB_PAGE[numPage]
				+ search.replace(" ", "+") + "\">" + title + "</a>";

	}
	
	public static String createLink(String url) {
		return "<a " + decoration + " href=\"" +url + "\">  Google Maps </a>";
	}
	public static String createLink(String url,String message) {
		return "<a " + decoration + " href=\"" +url + "\"> "+message+"  </a>";
	}

	public static String createFakeLink(String text) {
		return "<a " + decorationFake + " href=\"\"> "+text+"  </a>";
	}

	
	public static void openWebpage(String search, int numPage) {
		try {

			Desktop.getDesktop().browse(
					new URL(WEB_PAGE[numPage] + search.replace(" ", "+"))
							.toURI());
		} catch (Exception e) {
		}
	}

	public static boolean isHaveNumber(String str) {
		return (str.matches(".*\\d.*"));

	}

	/**
	 * @return the googleSearch
	 */
	public static String getFlight24() {
		return WEB_PAGE[2];
	}

	public static void copyFolder(File src, File dest) throws IOException {

		if (src.isDirectory()) {

			// if directory not exists, create it
			if (!dest.exists()) {
				dest.mkdir();
				System.out.println("Directory copied from " + src + "  to "
						+ dest);
			}

			// list all the directory contents
			String files[] = src.list();

			for (String file : files) {
				// construct the src and dest file structure
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				// recursive copy
				copyFolder(srcFile, destFile);
			}

		} else {
			// if file, then copy it
			// Use bytes stream to support all file types
			InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(dest);

			byte[] buffer = new byte[1024];

			int length;
			// copy the file content in bytes
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}

			in.close();
			out.close();
			System.out.println("File copied from " + src + " to " + dest);
		}
	}

	/**
	 * 
	 * @param num
	 * @throws Exception
	 */
	public static void pause(long num) {
		try {
			Thread.sleep(num);
		} catch (InterruptedException e) {
			System.err.println(e);
		}

	}

	/**
	 * 
	 * 
	 * @param folder
	 * @return
	 */

	public static JLabel getLabel(String text, int numColor) {
		JLabel label = new JLabel();
		label.setText(text);
		label.setForeground(colorForground[numColor]);
		label.setFont(fontText);
		return label;
	}

	/**
	 * 
	 * @param localkey
	 */
	public static void adjustMaplistArea(String localkey, Map<String, List<Object>> mapListArea) {
		Map<String, List<Object>> mapListWork = new TreeMap<String, List<Object>>();
		List<Object> listWork;

		try {
			for (Map.Entry<String, List<Object>> entry : mapListArea.entrySet()) {
				String key = entry.getKey();
				List<Object> list = (List<Object>) entry.getValue();
				listWork = new ArrayList<Object>();

				for (int i = 0; i < list.size(); i++) {
					if (localkey.compareTo((String) list.get(i)) != 0) {
						listWork.add(list.get(i));
					}
				}
				mapListWork.put(key, listWork);
			}
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

		mapListArea.clear();
		mapListArea.putAll(mapListWork);
	}
	
	public static String listFilesForFolder(final File folder) {
		List <String> list = new ArrayList<String>();
        Random rand = new Random(); 

	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	        } else {
	            System.out.println(fileEntry.getName());
	            list.add(fileEntry.getName());
	        }
	    }
	    
	    int rand_int1 = 0;
	    if (list.size() > 1) {
	        rand_int1 = rand.nextInt(list.size()); 
	    }
	    
	    System.out.println(list.get(rand_int1));
	    
	    return folder+"\\"+list.get(rand_int1);
/*
		   InputStream is = PlanetIsCalling.class.getResourceAsStream("/images/weather/va");
       	InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
       	BufferedReader reader = new BufferedReader(streamReader);
       	for (String line; (line = reader.readLine()) != null;) {
       		System.out.println(line);
       	}    			

*/	
	}

	
	public static String readAndTakePic() {
		
		
		return "";
	}

	public static String getComputerName() {

		String computerName = System.getenv("COMPUTERNAME");
		if ("".equals(computerName) || computerName == null) {
			computerName = "unknkow";
		}
		return computerName;
	}


	public static int[] toPrimitive(List<Integer> IntegerArray) {
		 
		int[] result = new int[IntegerArray.size()];
		for (int i = 0; i < IntegerArray.size(); i++) {
			result[i] = IntegerArray.get(i).intValue();
		}
		return result;
	}

	
	
}
