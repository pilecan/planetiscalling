package com.geo.util;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.model.Airport;
import com.model.CoordinatesDTO;
import com.model.Flightplan;
import com.model.Landcoord;
import com.model.LegPoint;



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
	public static double rad2deg(double rad) {
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
	
	public static boolean isInside(Landcoord coorMid, ArrayList <Landcoord> landcoords, double angle) {
		 boolean isfoundInAngle = false;

	     if (angle > 0 && angle < 90) {
	    	 isfoundInAngle =  (coorMid.getLaty() > landcoords.get(0).getLaty()
		    		 && coorMid.getLaty() < landcoords.get(1).getLaty()
		    		 && Math.abs(coorMid.getLonx()) < Math.abs(landcoords.get(0).getLonx())
		    		 && Math.abs(coorMid.getLonx()) > Math.abs(landcoords.get(1).getLonx())
		    		 
		    		 );
		    	 
		 } else if (angle >= 90  && angle <= 180) {
			 isfoundInAngle = (coorMid.getLaty() < landcoords.get(0).getLaty()
		    		 && coorMid.getLaty() > landcoords.get(1).getLaty()
		    		 && Math.abs(coorMid.getLonx()) < Math.abs(landcoords.get(0).getLonx())
		    		 && Math.abs(coorMid.getLonx()) > Math.abs(landcoords.get(1).getLonx())
		    		 
		    		 );
	     	 
	     } else if (angle >= 180  && angle <= 275) {
			 isfoundInAngle = (coorMid.getLaty() < landcoords.get(0).getLaty()
		    		 && coorMid.getLaty() > landcoords.get(1).getLaty()
		    		 && Math.abs(coorMid.getLonx()) > Math.abs(landcoords.get(0).getLonx())
		    		 && Math.abs(coorMid.getLonx()) < Math.abs(landcoords.get(1).getLonx())
		    		 
		    		 );
	     	 
	     } else if (angle >= 275  && angle <= 360) {
			 isfoundInAngle = (coorMid.getLaty() > landcoords.get(0).getLaty()
		    		 && coorMid.getLaty() < landcoords.get(1).getLaty()
		    		 && Math.abs(coorMid.getLonx()) > Math.abs(landcoords.get(0).getLonx())
		    		 && Math.abs(coorMid.getLonx()) < Math.abs(landcoords.get(1).getLonx())
		    		 
		    		 );
	     	 
	     }

	     return isfoundInAngle;
	
	
	}
	
	public static Landcoord searchPoint(double latitude, double longitude, double distanceInNm, double bearing) {
	    double brngRad = deg2rad(bearing);
	    double latRad = deg2rad(latitude);
	    double lonRad = deg2rad(longitude);
	    int earthRadiusInMetres = 3440;
	    double distFrac = distanceInNm / earthRadiusInMetres;

	    double latitudeResult = Math.asin(Math.sin(latRad) * Math.cos(distFrac) + Math.cos(latRad) * Math.sin(distFrac) * Math.cos(brngRad));
	    double a = Math.atan2(Math.sin(brngRad) * Math.sin(distFrac) * Math.cos(latRad), Math.cos(distFrac) - Math.sin(latRad) * Math.sin(latitudeResult));
	    double longitudeResult = (lonRad + a + 3 * Math.PI) % (2 * Math.PI) - Math.PI;
	    
	  //  System.out.println(rad2deg(latitudeResult)+","+rad2deg(longitudeResult));
        return new Landcoord(rad2deg(latitudeResult), rad2deg(longitudeResult));

	}
	
	String sql = "select ai.id, ai.icao,  ((ACOS(SIN(ai.Latitude * PI() / 180) * SIN(geo.Latitude * PI() / 180) + "
			+ "COS(ai.Latitude * PI() / 180) * COS(geo.Latitude * PI() / 180)"
			+ " * COS((ai.Longitude - geo.Longitude) * PI() / 180)) * 180 / PI()) * 60 * 1.1515) AS distance "
			+ "from airport ai, geonames geo " 
			+ "where ai.iso = geo.country_code " +
				"and ai.admin1 = geo.admin1_code "+
				"and geo.geonameid = ? "+
				"HAVING distance <= 20 "+
				"order by icao";
	
	
	public static boolean isNearEnough(Airport ai, Landcoord lm) {
		
		return ((Math.acos((Math.sin(ai.getLaty()*Math.PI/180) * Math.sin(lm.getLaty()*Math.PI/180) 
				+ Math.cos(ai.getLaty() * Math.PI / 180) * Math.cos(lm.getLaty() * Math.PI / 180)
				* Math.cos((ai.getLonx() - lm.getLonx()) * Math.PI / 180)) * 180 / Math.PI)) * 60 * 1.1515) < 30 ;
	}
	
	
	public static Double[] convertDoubleLongLat(String value){
		Double[] dd = new Double[3];
		String[] coords = value.split(",");
		
		dd[0] = Double.parseDouble(coords[0]);
		dd[1] = Double.parseDouble(coords[1]);
		dd[2] = Double.parseDouble(coords[2]);
		
		return dd;
	}

    public static double correctAngle(double angle)    {
        return angle + Math.ceil( -angle / 360 ) * 360;
    }

	
	
    public static double calculateAngle(double x1, double y1, double x2, double y2)
    {
        double angle = Math.toDegrees(Math.atan2(x2 - x1, y2 - y1));
        // Keep angle between 0 and 360
        angle = angle + Math.ceil( -angle / 360 ) * 360;

        return angle;
    }
    
    public static double calculateAngle2(double y1, double x1, double y2, double x2)
    {
        double angle = Math.toDegrees(Math.atan2(x2 - x1, y2 - y1));
        // Keep angle between 0 and 360
        angle = angle + Math.ceil( -angle / 360 ) * 360;

        return angle;
    }
    
    public static double getAngleOfLineBetweenTwoPoints(double y1, double x1, double y2, double x2)
    {
        double xDiff = x1 -x2;
        double yDiff = y1 -y2;
        return Math.toDegrees(Math.atan2(yDiff, xDiff));
    }    
  
    public static  double angleFromCoordinate(double lat1, double long1, double lat2, double long2) {

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;
        //brng = 360 - brng; // count degrees counter-clockwise - remove to make clockwise
        
        
        float dy = (float) (lat2 - lat1);
        float dx = (float) (Math.cos(Math.PI/180*lat1)*(long2 - long1));
        float angle = (float) Math.atan2(dy, dx);

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
    
	public static LinkedList<LegPoint> removeInvisiblePointAndInitialiseDist( LinkedList<LegPoint> legPoints) {
		//remove intermediate point
		LinkedList<LegPoint> newLegs = new LinkedList<LegPoint>();
		for (int i = 0; i < legPoints.size(); i++) {
			if ("1".equals(legPoints.get(i).getVisible())) {
				newLegs.add(legPoints.get(i));
			}
		}
		
		return newLegs;
		
	}
	
	public static  int createTOC(Flightplan flightplan, LinkedList<LegPoint> legPoints) {
		
		double altitude = Double.parseDouble(flightplan.getCruisingAlt())-legPoints.get(0).getAltitude();
		double rate = 0.444;
		if (altitude <= 10000) {
			rate = 0.444;
		} else 	if (altitude > 10000 && altitude <= 25000) {
			rate = 0.333;
		} else if (altitude > 25000) {
			rate = 0.222;
		}
		
    	double distForAltitude = Math.round(altitude/rate/1000);
		
		int index = 0;
		boolean isfound = false;
		legPoints.get(index).setDistFrom(0);
		double distCumulate = 0;
		double currentDist = 0;
		while (!isfound && index < legPoints.size()-1) {
			
			currentDist = distance(legPoints.get(index).getLaty(), legPoints.get(index).getLonx(), legPoints.get(index+1).getLaty(), legPoints.get(index+1).getLonx(),'N');
			distCumulate += currentDist;
			legPoints.get(index).setDistFrom(distCumulate+legPoints.get(index).getDistFrom());
		
			if (distForAltitude < distCumulate) {
				isfound = true;
				legPoints.get(index).setDistFrom(legPoints.get(index).getDistFrom()-currentDist);
			}
			
			index++;

		}
		
		if (isfound) {
        	double angle = Geoinfo.calculateAngle(legPoints.get(index-1).getLonx(), legPoints.get(index-1).getLaty(), legPoints.get(index).getLonx(), legPoints.get(index).getLaty() );
        	
        	double distRest = distForAltitude -legPoints.get(index-1).getDistFrom();

        	double todKM = distRest*1.852/100;
        	double todLon = todKM * Math.sin(Math.toRadians(angle))+legPoints.get(index-1).getLonx();  
        	double todLat = todKM * Math.cos(Math.toRadians(angle))+legPoints.get(index-1).getLaty(); 
        	
        	String coordinates = todLon+","+todLat+","+Math.round(Math.round(altitude/3.28084));

    		LegPoint legPoint = new LegPoint("TOC","TOC",coordinates,"1",Double.parseDouble(flightplan.getCruisingAlt())/3.28084); 
    		legPoints.add(index,legPoint);
    	     correctSlopeUp(legPoints, index, distForAltitude);

 		}
		
		return index;
		
	
	}
	
	private static void correctSlopeUp(LinkedList<LegPoint> legPoints, int toc, double distForAltitude) {
		double diffElev1 =  legPoints.get(toc).getAltitude()-legPoints.get(0).getAltitude();
	    double factorbase = diffElev1/distForAltitude;
	    double distance = 0;

	    for (int i = 0; i < toc-1; i++) {
	    	distance += Geoinfo.distance(legPoints.get(i).getLaty(), legPoints.get(i+1).getLaty(), legPoints.get(i).getLonx(), legPoints.get(i+1).getLonx());
		    legPoints.get(i+1).setNewAltitude(legPoints.get(0).getAltitude()+(distance * factorbase));

		}
		
	}
	
	private static void correctSlopeTOD(LinkedList<LegPoint> legPoints, int tod, double distForAltitude) {
		double diffElev1 =  legPoints.get(tod).getAltitude()-legPoints.get(legPoints.size()-1).getAltitude();
	    double factorbase = diffElev1/distForAltitude;
	    double distance = 0;
	    
	    for (int i = tod; i < legPoints.size()-2; i++) {
	    	distance += Geoinfo.distance(legPoints.get(i).getLaty(), legPoints.get(i+1).getLaty(), legPoints.get(i).getLonx(), legPoints.get(i+1).getLonx());
		    legPoints.get(i+1).setNewAltitude(diffElev1-(distance * factorbase));

		}
	   

	    
	}

	
	public static  int createTOD(Flightplan flightplan, LinkedList<LegPoint> legPoints) {
		double altitude = Double.parseDouble(flightplan.getCruisingAlt())-legPoints.get(legPoints.size()-1).getAltitude();
		double rate = 0.300;
		
    	double distForAltitude = Math.round(altitude/rate/1000);
		
		int index = legPoints.size()-1;
		boolean isfound = false;
		double distCumulate = 0;
		double currentDist = 0;
		while (!isfound && index >= 0) {
			
			try {
				currentDist = distance(legPoints.get(index).getLaty(), legPoints.get(index).getLonx(), legPoints.get(index-1).getLaty(), legPoints.get(index-1).getLonx(),'N');
				distCumulate += currentDist;
				legPoints.get(index).setDistFrom(distCumulate+legPoints.get(index).getDistFrom());

				if (distForAltitude < distCumulate) {
					isfound = true;
					legPoints.get(index).setDistFrom(legPoints.get(index).getDistFrom()-currentDist);
				}
				
				index--;
			} catch (Exception e) {
				index = 1;
				// TODO Auto-generated catch block
			}

		}
		
		if (isfound) {
        	double angle = Geoinfo.calculateAngle(legPoints.get(index+1).getLonx(), legPoints.get(index+1).getLaty(), legPoints.get(index).getLonx(), legPoints.get(index).getLaty() );
        	
        	double distRest = distForAltitude -legPoints.get(index+1).getDistFrom();

        	double todKM = distRest*1.852/100;
        	double todLon = todKM * Math.sin(Math.toRadians(angle))+legPoints.get(index+1).getLonx();  
        	double todLat = todKM * Math.cos(Math.toRadians(angle))+legPoints.get(index+1).getLaty(); 
        	
        	String coordinates = todLon+","+todLat+","+Math.round(Math.round(altitude/3.28084));

    		LegPoint legPoint = new LegPoint("TOD","TOD",coordinates,"1",Double.parseDouble(flightplan.getCruisingAlt())/3.28084); 
    		legPoints.add(index+1,legPoint);
    		
    	  correctSlopeTOD(legPoints, index+1,distForAltitude);


 		}
		return index+1;

	}

	public static void setTocTod(double altitude, LinkedList<LegPoint> legPoints) {
		removeInvisiblePointAndInitialiseDist(legPoints);
			
		// Set top of climb
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
	
	public static boolean isLocationInsideTheFencing(CoordinatesDTO location, List<CoordinatesDTO> fencingCoordinates) { //this is important method for Checking the point exist inside the fence or not.
	    boolean blnIsinside = false;

	    List<CoordinatesDTO> lstCoordinatesDTO = fencingCoordinates;

	    Path2D myPolygon = new Path2D.Double();
	    myPolygon.moveTo(lstCoordinatesDTO.get(0).getLatitude(), lstCoordinatesDTO.get(0).getLongnitude()); // first
	                                                                                                        // point
	    for (int i = 1; i < lstCoordinatesDTO.size(); i++) {
	        myPolygon.lineTo(lstCoordinatesDTO.get(i).getLatitude(), lstCoordinatesDTO.get(i).getLongnitude()); // draw
	                                                                                                            // lines
	    }
	    myPolygon.closePath(); // draw last line

	    // myPolygon.contains(p);
	    Point2D P2D2 = new Point2D.Double();
	    P2D2.setLocation(location.getLatitude(), location.getLongnitude());

	    if (myPolygon.contains(P2D2)) {
	        blnIsinside = true;
	    } else {
	        blnIsinside = false;
	    }

	    return blnIsinside;
	}
	
	public static List<CoordinatesDTO> manitobaCoords() {
		List<CoordinatesDTO> list = new ArrayList<CoordinatesDTO>();

		list.add(new CoordinatesDTO(49.16325310958394, -101.3635401998698));
		list.add(new CoordinatesDTO(60.08081648306394, -101.92107583743366));
		list.add(new CoordinatesDTO(60.013943847065285, -94.87101999169981));
		list.add(new CoordinatesDTO(56.89059589041594, -88.9756991586904));
		list.add(new CoordinatesDTO(52.84630155492344, -95.21099903581343));
		list.add(new CoordinatesDTO(49.11247432299795, -95.16223805466778));

		return list;
	}
	public static List<CoordinatesDTO> ontarioCoords() {
		List<CoordinatesDTO> list = new ArrayList<CoordinatesDTO>();
		list.add(new CoordinatesDTO(56.891703794970624, -89.11251036790122));
		list.add(new CoordinatesDTO(52.81380563156915, -95.08320068156674));
		list.add(new CoordinatesDTO(49.152092749861964, -95.10895288847276));
		list.add(new CoordinatesDTO(46.406725952167115, -84.2481575503475));
		list.add(new CoordinatesDTO(43.020021661883106, -82.2966919809097));
		list.add(new CoordinatesDTO(41.86656689790429, -83.19584546369572));
		list.add(new CoordinatesDTO(42.77833570900693, -79.09115236399617));
		list.add(new CoordinatesDTO(43.48612176119141, -79.22562812924087));
		list.add(new CoordinatesDTO(43.688971943610895, -76.84666901288871));
		list.add(new CoordinatesDTO(45.16520240789509, -74.38187517183053));
		list.add(new CoordinatesDTO(45.58697884610369, -74.43019370827935));
		list.add(new CoordinatesDTO(45.62222749320456, -75.12753632848944));
		list.add(new CoordinatesDTO(45.474447316097006, -75.69706994279473));
		list.add(new CoordinatesDTO(47.01462396202919, -79.3706725744501));
		list.add(new CoordinatesDTO(51.81360703880206, -79.60756511374221));
		list.add(new CoordinatesDTO(53.09841154301055, -82.28576726441892));
		list.add(new CoordinatesDTO(55.21565806661516, -82.12800845032245));
		list.add(new CoordinatesDTO(56.92037839423947, -88.85298440649987));
		return list;
	}
	
	
}
