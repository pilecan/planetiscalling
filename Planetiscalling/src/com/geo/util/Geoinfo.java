package com.geo.util;

import java.util.LinkedList;

import com.cfg.model.LegPoint;

public class Geoinfo {
	public static double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
		  double theta = lon1 - lon2;
		  double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		  dist = Math.acos(dist);
		  dist = rad2deg(dist);
		  dist = dist * 60 * 1.1515;
		  if (unit == 'K') {
		    dist = dist * 1.609344;
		  } else if (unit == 'N') {
		  	dist = dist * 0.8684;
		    }
		  return (dist);
	}
	


	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::  This function converts decimal degrees to radians             :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	public static double deg2rad(double deg) {
	  return (deg * Math.PI / 180.0);
	}

	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::  This function converts radians to decimal degrees             :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private static double rad2deg(double rad) {
	  return (rad * 180 / Math.PI);
	}

	public static String convertDMSToDD(String input) {
		String direction = ""; 
		input = input.replace("°", "");
		input = input.replace("'", "");
		input = input.replace("\"", "");
		input = input.replace("  ", " ");

	    String[] parts = input.split(" ");

		String deg = parts[0];
		String min = parts[1];
		String sec = "";;
		try {
			sec = parts[2];
		} catch (Exception e) {
			sec = "0";
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

		if (deg.contains("E")){
			deg = deg.replace("E", "");
			direction = "E";
		} else if (deg.contains("W")){
			deg = deg.replace("W", "");
			direction = "W";
		} else if (deg.contains("N")){
			deg = deg.replace("N","");
			direction = "N";
		} else if (deg.contains("S")){
			deg = deg.replace("S", "");
			direction = "S";
		}
		
	    Double dd = Double.parseDouble(deg) + Double.parseDouble(min)/60 + Double.parseDouble(sec)/(60*60);
	    if (direction == "S" || direction == "W") {
	        dd = dd * -1;
	    } // Don't do anything for N or E
	    
       return dd.toString();
	    
	}	
	
	public static Double[] convertDoubleLongLat(String value){
		Double[] dd = new Double[3];
		String[] coords = value.split(",");
		
		dd[0] = Double.parseDouble(coords[0]);
		dd[1] = Double.parseDouble(coords[1]);
		dd[2] = Double.parseDouble(coords[2]);
		
		return dd;
	}
	
    public static double calculateAngle(double x1, double y1, double x2, double y2)
    {
        double angle = Math.toDegrees(Math.atan2(x2 - x1, y2 - y1));
        // Keep angle between 0 and 360
        angle = angle + Math.ceil( -angle / 360 ) * 360;

        return angle;
    }
    
    public static String calculateTocTod(double altitude, char direction, double lon1,double lat1,double lon2,double lat2) {
    	altitude = altitude*3.2804;
    	double rate = 0.333;
    	if (direction == 'U') {
    		rate = 0.250;
    	} else if (direction == 'D') {
    		rate = 0.333;
    	} 
    	double tod = 0;
    	double dist1 = 10;
    	String coordinates = "";
        	tod = Math.round(altitude/rate/1000);
        	
        	double angle = Geoinfo.calculateAngle(lon1, lat1, lon2, lat2 );
        	System.out.println(angle);
        	
        	dist1 = Geoinfo.distance(lat1, lon1,lat2, lon2, 'N');
        	double todKM = tod*1.852/100;
        	double todLon = todKM * Math.sin(Math.toRadians(angle))+lon1;  
        	double todLat = todKM * Math.cos(Math.toRadians(angle))+lat1;  
        	
        	rate += 0.100;
    		
           	System.out.println("todKM TOD= "+todKM);
        	System.out.println("distance TOD= "+tod);
        	System.out.println("distance = "+dist1);
        	
        	coordinates = todLon+","+todLat+","+Math.round(altitude/3.2804);
         	System.out.println(coordinates);
    	

    	
    	return coordinates;
    }
    
	public static void removeInvisiblePointAndInitialiseDist( LinkedList<LegPoint> legPoints) {
		//remove intermediate point
		for (int i = 0; i < legPoints.size(); i++) {
			if ("0".equals(legPoints.get(i).getVisible())) {
				legPoints.remove(i);
			} else {
				legPoints.get(i).setDistFrom(0);
			}
		}
		
	}
	
	public static  void createTOC(String alt, LinkedList<LegPoint> legPoints) {
		removeInvisiblePointAndInitialiseDist(legPoints);
		
		double altitude = Double.parseDouble(alt);

		double rate = 0.444;
		if (altitude <= 10000) {
			rate = 0.444;
		} else 	if (altitude > 10000 && altitude <= 25000) {
			rate = 0.333;
		} else if (altitude > 25000) {
			rate = 0.222;
		}
		
    	double distForAltitude = Math.round(altitude/rate/1000);
		
		int i = 0;
		boolean isfound = false;
		legPoints.get(i).setDistFrom(0);
		double distCumulate = 0;
		double currentDist = 0;
		while (!isfound && i <= legPoints.size()-1) {
			
			currentDist = distance(legPoints.get(i).getLaty(), legPoints.get(i).getLonx(), legPoints.get(i+1).getLaty(), legPoints.get(i+1).getLonx(),'N');
			distCumulate += currentDist;
			legPoints.get(i).setDistFrom(distCumulate+legPoints.get(i).getDistFrom());
		
			if (distForAltitude < distCumulate) {
				isfound = true;
				legPoints.get(i).setDistFrom(legPoints.get(i).getDistFrom()-currentDist);
			}
			
			i++;

		}
		
		if (isfound) {
			System.out.println(legPoints.get(i-1).getId());
			System.out.println(legPoints.get(i).getId());
        	double angle = Geoinfo.calculateAngle(legPoints.get(i-1).getLonx(), legPoints.get(i-1).getLaty(), legPoints.get(i).getLonx(), legPoints.get(i).getLaty() );
        	System.out.println(angle);
        	
        	System.out.println(legPoints.get(i-1).getId()+" - "+legPoints.get(i-1).getDistFrom());
        	
        	double distRest = distForAltitude -legPoints.get(i-1).getDistFrom();

        	double todKM = distRest*1.852/100;
        	double todLon = todKM * Math.sin(Math.toRadians(angle))+legPoints.get(i-1).getLonx();  
        	double todLat = todKM * Math.cos(Math.toRadians(angle))+legPoints.get(i-1).getLaty(); 
        	
        	String coordinates = todLon+","+todLat+","+Math.round(Math.round(altitude/3.2808));

    		LegPoint legPoint = new LegPoint("TOC","TOC",coordinates,"1"); 
    		legPoints.add(i,legPoint);

 		}
		
	
	}
	
	public static  void createTOD(String alt, LinkedList<LegPoint> legPoints) {
		Geoinfo.removeInvisiblePointAndInitialiseDist(legPoints);
		
		double altitude = Double.parseDouble(alt);

		double rate = 0.300;
		
    	double distForAltitude = Math.round(altitude/rate/1000);
		
		int i = legPoints.size()-1;
		boolean isfound = false;
		double distCumulate = 0;
		double currentDist = 0;
		while (!isfound && i >= 0) {
			
			currentDist = distance(legPoints.get(i).getLaty(), legPoints.get(i).getLonx(), legPoints.get(i-1).getLaty(), legPoints.get(i-1).getLonx(),'N');
			distCumulate += currentDist;
			legPoints.get(i).setDistFrom(distCumulate+legPoints.get(i).getDistFrom());
		
			if (distForAltitude < distCumulate) {
				isfound = true;
				legPoints.get(i).setDistFrom(legPoints.get(i).getDistFrom()-currentDist);
			}
			
			i--;

		}
		
		if (isfound) {
			System.out.println(legPoints.get(i+1).getId());
			System.out.println(legPoints.get(i).getId());
        	double angle = Geoinfo.calculateAngle(legPoints.get(i+1).getLonx(), legPoints.get(i+1).getLaty(), legPoints.get(i).getLonx(), legPoints.get(i).getLaty() );
        	System.out.println(angle);
        	
        	System.out.println(legPoints.get(i+1).getId()+" - "+legPoints.get(i+1).getDistFrom());
        	
        	double distRest = distForAltitude -legPoints.get(i+1).getDistFrom();

        	double todKM = distRest*1.852/100;
        	double todLon = todKM * Math.sin(Math.toRadians(angle))+legPoints.get(i+1).getLonx();  
        	double todLat = todKM * Math.cos(Math.toRadians(angle))+legPoints.get(i+1).getLaty(); 
        	
        	String coordinates = todLon+","+todLat+","+Math.round(Math.round(altitude/3.2808));

    		LegPoint legPoint = new LegPoint("TOD","TOD",coordinates,"1"); 
    		legPoints.add(i+1,legPoint);

 		}
		
	
	}

    
	public static void setTocTod(double altitude, LinkedList<LegPoint> legPoints) {
		removeInvisiblePointAndInitialiseDist(legPoints);
			
		// Set top of climb
		System.out.println("set toc-------------------------");
		String[] first = legPoints.get(0).getPosition().split(",");
		String[] second = legPoints.get(1).getPosition().split(",");
		
		String coordinates = Geoinfo.calculateTocTod(altitude-Double.parseDouble(first[2]), 'U', Double.parseDouble(first[0]), Double.parseDouble(first[1]), Double.parseDouble(second[0]), Double.parseDouble(second[1]));
		LegPoint legPoint = new LegPoint("TOC","TOC",coordinates,"1"); 
		legPoints.add(1,legPoint);
		
/*		// Set top of decent
		
		System.out.println("set top-------------------------");
		first = legPoints.get(legPoints.size()-1).getPosition().split(",");
		second = legPoints.get(legPoints.size()-2).getPosition().split(",");
		coordinates = Geoinfo.calculateTocTod(altitude-Double.parseDouble(first[2]), 'D', Double.parseDouble(first[0]), Double.parseDouble(first[1]), Double.parseDouble(second[0]), Double.parseDouble(second[1]));
		legPoint = new LegPoint("TOD","TOD",coordinates,"1"); 
		legPoints.add(legPoints.size()-1,legPoint);
*/		
	}
    
	public static String convertDMtoDD(String input) {
		String direction = ""; 
		input = input.trim().replace("*", "");
		input = input.replace("'", "");

	    String[] parts = input.split(" ");
	    
		String deg = parts[0];
		String min = parts[1];

		if (deg.contains("E")){
			deg = deg.replace("E", "");
			direction = "E";
		} else if (deg.contains("W")){
			deg = deg.replace("W", "");
			direction = "W";
		} else if (deg.contains("N")){
			deg = deg.replace("N","");
			direction = "N";
		} else if (deg.contains("S")){
			deg = deg.replace("S", "");
			direction = "S";
		}
		
	    Double dd = Double.parseDouble(deg) + Double.parseDouble(min)/60;
	    if (direction == "S" || direction == "W") {
	        dd = dd * -1;
	    } // Don't do anything for N or E
	    
       return dd.toString();
	    
	}	

	
	public static boolean isIcaoValid(String icao){
		boolean isValid = icao.length() == 4 && icao.toUpperCase().equals(icao);
		if (isValid) isValid = !icao.matches("[+-]?\\d*(\\.\\d+)?");
		return isValid;
		
	}
	
	public static String stripSearchText(String text){
		text = text.replace("."," ");
		text = text.replace("-"," ");
		text = text.replace("_"," ");
		text = text.replace("("," ");
		text = text.replace(")"," ");
		text = text.replace(","," ");
		
		return text;
		
	}
	
	public static String getIcao(String title){
		String[] elementTitle = stripSearchText(title).split(" ");

		String icao = "";
		for (int i=0; i<elementTitle.length;i++){
			//System.out.print(elementTitle[i]+" ");
			if (isIcaoValid(elementTitle[i])){
				icao = elementTitle[i];
				break;
			}
		}
		
		return icao;
	}
	
	// distance(double lat1, double lon1, double lat2, double lon2, char unit) {
	
	public static String  midpoint(double y1, double x1,  double y2, double x2) {
		//System.out.print((x1 + x2) / 2 + " , " + (y1 + y2) / 2);
		return (x1 + x2) / 2 + "," + (y1 + y2) / 2;
	}
	
	public static double distance(double lat1, double lat2, double lon1, double lon2) {

		// The math module contains a function
		// named toRadians which converts from
		// degrees to radians.
		lon1 = Math.toRadians(lon1);
		lon2 = Math.toRadians(lon2);
		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);

		// Haversine formula
		double dlon = lon2 - lon1;
		double dlat = lat2 - lat1;
		double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);

		double c = 2 * Math.asin(Math.sqrt(a));

		// Radius of earth in kilometers. Use 3956
		// for miles
		double r = 6371;

		// calculate the result
		return (c * r) * 0.539957;
	}	
	
	
}
