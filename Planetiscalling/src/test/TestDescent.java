package test;

import com.geo.util.Geoinfo;

public class TestDescent {
	
    static private double AngleToRadians(double angle)
	{
	     return (Math.PI / 180) * angle;
	}
    
    public static double calculateAngle(double x1, double y1, double x2, double y2)
    {
        double angle = Math.toDegrees(Math.atan2(x2 - x1, y2 - y1));
        // Keep angle between 0 and 360
        angle = angle + Math.ceil( -angle / 360 ) * 360;

        return angle;
    }
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		

	double altitude = 4000; 
	double tod = Math.round(altitude/0.333/1000);
		
	double lon1 = 102.5632476806641;
	double lat1 = 17.98833084106445;
	double lon2 = 102.3788888888889;
	double lat2 = 19.09944444444444;
	
	//Geoinfo.calculateTOD(altitude, 'U', lon1, lat1, lon2, lat2);
	Geoinfo.calculateTocTod(altitude, 'D', lon2, lat2, lon1, lat1);
	
/*	double angle = Geoinfo.calculateAngle(lon1, lat1, lon2, lat2 );
	System.out.println(angle);
	
	double dist1 = Geoinfo.distance(lat1, lon1,lat2, lon2, 'N');
	double todKM = tod*1.852/100;
	double todLon = todKM * Math.sin(Math.toRadians(angle))+lon1;  
	double todLat = todKM * Math.cos(Math.toRadians(angle))+lat1;  
	
	System.out.println("todKM TOD= "+todKM);
	System.out.println("distance TOD= "+tod);
	System.out.println("distance = "+dist1);
	System.out.println("<coordinates>"+todLon+","+todLat+","+Math.round(altitude/3.2804)+"</coordinates>");
*/
	}
	
	

}
