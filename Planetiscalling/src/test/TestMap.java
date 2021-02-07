package test;

import com.geo.util.Geoinfo;
import com.model.CoordinatesDTO;
import com.model.Landcoord;
import com.util.UtilityMap;

public class TestMap {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Landcoord coord = new Landcoord(64.75141803688189, -83.74012543898037);
		
		UtilityMap.getInstance().checkWichProvince(coord);
		
		


	}

}
