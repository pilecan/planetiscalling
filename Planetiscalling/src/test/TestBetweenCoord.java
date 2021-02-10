package test;

import java.util.ArrayList;

import com.geo.util.Geoinfo;
import com.model.BoundingboxOld;
import com.model.Landcoord;

public class TestBetweenCoord {
	static private ArrayList<Landcoord> landcoords;

	public static void main(String[] args) {
		landcoords = new ArrayList<>();
		
		Double lonx = 72.750000;
		Double laty = 46.550000;
		
		//GPS: 46.550000,-72.750000

		//46.888272279891474, -73.51230843546905
		//45.71457089041536, -71.85620500514422
		
		Landcoord coorMid = new Landcoord(46.67362751661421, -71.63714277001444);
	

	     
	     BoundingboxOld boundingbox = new BoundingboxOld(45.475021083140675, -73.7326933141436,46.791645713514306, -71.38659191296856,20);
	     
	     System.out.println(boundingbox.isInside(new Landcoord(46.12240542431005, -72.69675633241985)));
	}
	


}
