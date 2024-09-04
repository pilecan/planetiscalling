package test;

import com.db.UtilityDB;
import com.geo.util.Geoinfo;
import com.model.CoordinatesDTO;
import com.model.Landcoord;
import com.util.UtilityMap;

public class UpdateUnknow {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		UtilityDB.getInstance().selectUnknow("'Canada','United States'");

		
/*		Landcoord coord = new Landcoord(49.09440854917373, -67.612158904783);
		
		System.out.println(UtilityMap.getInstance().checkWichState(coord).getState());
		
		UtilityMap.getInstance().checkWichProvince(coord);
*/


	}

}
