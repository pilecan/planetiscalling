package test;

import com.db.UtilityDB;
import com.geo.util.Geoinfo;
import com.model.CoordinatesDTO;
import com.model.Landcoord;
import com.util.UtilityMap;

public class TestMap {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		UtilityDB.getInstance().selectPolygone();

		
		Landcoord coord = new Landcoord(54.708759394323145, -62.647364046619906);
		
		System.out.println(UtilityMap.getInstance().checkWichState(coord));
		
		System.out.println(UtilityMap.getInstance().checkWichProvince(coord));



	}

}
