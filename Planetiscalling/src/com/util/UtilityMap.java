package com.util;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cfg.common.Info;
import com.db.UtilityDB;
import com.model.Airport;
import com.model.City;
import com.model.CityWeather;
import com.model.CoordinatesDTO;
import com.model.Landcoord;
import com.model.Landmark;
import com.model.LegPoint;
import com.model.Mountain;
import com.model.Runway;

public class UtilityMap {
	private static UtilityMap instance = new UtilityMap();
	public static UtilityMap getInstance() {
		return instance;
	}
	
	public  boolean isLocationInsideTheFencing(CoordinatesDTO location, List<CoordinatesDTO> fencingCoordinates) { //this is important method for Checking the point exist inside the fence or not.
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
	
	public String  checkProvinces(LinkedList<LegPoint> legPoints) throws Exception {
		Set <String> setProv = new HashSet<>();

		for (int i = 0; i < legPoints.size(); i++) {
			setProv.add(checkProvince(legPoints.get(i).getLaty(), legPoints.get(i).getLonx()));
		}

		String provinces = "";
		if (setProv.size() > 0) {
	        Iterator<String> value = setProv.iterator(); 
			while (value.hasNext()) {
				String str = value.next();
				if (str != null) {
		            provinces += "'"+str+"',";
				}
		    } 
			 try {
				provinces = provinces.substring(0, provinces.length()-1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw e;
			}
			
		}

		return provinces;
		
	}
	
	public String checkProvince(double lat1, double lon1) {
		return UtilityMap.getInstance().checkWichProvince(new Landcoord(lat1, lon1));
	}

	public String check2Provinces(double lat1, double lon1,double lat2, double lon2) throws Exception {
		LinkedList<LegPoint> newLegs = new LinkedList<LegPoint>();
		newLegs.add(new LegPoint(lon1, lat1));
		newLegs.add(new LegPoint(lon2, lat2));
		
	    return checkProvinces(newLegs);
	}

	
	public String checkWichProvince(Landcoord coord) {
		String province = null;
		
		if (isLocationInsideTheFencing(new CoordinatesDTO(coord.getLaty(), coord.getLonx()), coordBritishColumbia())) {
			province = "British Columbia";
		} else if (isLocationInsideTheFencing(new CoordinatesDTO(coord.getLaty(), coord.getLonx()), coordOntatio())){
			province = "Ontario";
		}else if (isLocationInsideTheFencing(new CoordinatesDTO(coord.getLaty(), coord.getLonx()), coordAlberta())){
			province = "Alberta";
		}else if (isLocationInsideTheFencing(new CoordinatesDTO(coord.getLaty(), coord.getLonx()), coordSaskatchewan())){
			province = "Saskatchewan";
		}else if (isLocationInsideTheFencing(new CoordinatesDTO(coord.getLaty(), coord.getLonx()), coordManitoba())){
			province = "Manitoba";
		}else if (isLocationInsideTheFencing(new CoordinatesDTO(coord.getLaty(), coord.getLonx()), coordYukon())){
			province = "Yukon";
		}else if (isLocationInsideTheFencing(new CoordinatesDTO(coord.getLaty(), coord.getLonx()), coordNorthWest())){
			province = "North West Territory";
		}else if (isLocationInsideTheFencing(new CoordinatesDTO(coord.getLaty(), coord.getLonx()), coordNuvanut())){
			province = "Nuvanut";
		}else if (isLocationInsideTheFencing(new CoordinatesDTO(coord.getLaty(), coord.getLonx()), coordQuebec())){
			province = "Quebec";
		}else if (isLocationInsideTheFencing(new CoordinatesDTO(coord.getLaty(), coord.getLonx()), coordNewfoundLandLabrador())){
			province = "Newfoundland and Labrador";
		}else if (isLocationInsideTheFencing(new CoordinatesDTO(coord.getLaty(), coord.getLonx()), coordNewfoundLand())){
			province = "Newfoundland";
		}else if (isLocationInsideTheFencing(new CoordinatesDTO(coord.getLaty(), coord.getLonx()), coordPrinceEdward())){
			province = "Prince Edward Island";
		}else if (isLocationInsideTheFencing(new CoordinatesDTO(coord.getLaty(), coord.getLonx()), coordNovaScotia())){
			province = "Nova Scotia";
		}else if (isLocationInsideTheFencing(new CoordinatesDTO(coord.getLaty(), coord.getLonx()), coordNewBrunswick())){
			province = "New Brunswick";
		}
		
		
		//System.out.println(province); 
		
		return province;
	}
	
	public void selectProvince(double lat1, double lon1, double lat2, double lon2) {
		
	}
	
	public  List<CoordinatesDTO> coordManitoba() {
		List<CoordinatesDTO> list = new ArrayList<CoordinatesDTO>();

		list.add(new CoordinatesDTO(49.16325310958394, -101.3635401998698));
		list.add(new CoordinatesDTO(60.08081648306394, -101.92107583743366));
		list.add(new CoordinatesDTO(60.013943847065285, -94.87101999169981));
		list.add(new CoordinatesDTO(56.89059589041594, -88.9756991586904));
		list.add(new CoordinatesDTO(52.84630155492344, -95.21099903581343));
		list.add(new CoordinatesDTO(49.11247432299795, -95.16223805466778));

		return list;
	}
	public  List<CoordinatesDTO> coordOntatio() {
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
	
	public  List<CoordinatesDTO> coordSaskatchewan() {
		List<CoordinatesDTO> list = new ArrayList<CoordinatesDTO>();
		list.add(new CoordinatesDTO(59.94428881136924, -110.01150610422545));
		list.add(new CoordinatesDTO(60.06308930717493, -102.04565169989583));
		list.add(new CoordinatesDTO(49.04647530469038, -101.339471910838));
		list.add(new CoordinatesDTO(49.06669443134574, -110.1094098259901));
		
		return list;
	}
	public  List<CoordinatesDTO> coordAlberta() {
		List<CoordinatesDTO> list = new ArrayList<CoordinatesDTO>();
		list.add(new CoordinatesDTO(60.00872596280032, -119.92052295705979));
		list.add(new CoordinatesDTO(59.993094828192795, -110.00028029975213));
		list.add(new CoordinatesDTO(49.048438527329765, -110.01578210470056));
		list.add(new CoordinatesDTO(49.00295143665085, -114.08476092008524));
		list.add(new CoordinatesDTO(49.62985638638018, -114.68331232809047));
		list.add(new CoordinatesDTO(50.434905094934955, -114.83558343921075));
		list.add(new CoordinatesDTO(51.83863247024028, -116.73200245930967));
		list.add(new CoordinatesDTO(51.71276981753783, -116.89819459407767));
		list.add(new CoordinatesDTO(52.13178784552697, -117.32485804766921));
		list.add(new CoordinatesDTO(52.270104197900594, -117.86457507778985));
		list.add(new CoordinatesDTO(52.378633690876214, -117.7319793789927));
		list.add(new CoordinatesDTO(52.37778145638933, -117.76323217507199));
		list.add(new CoordinatesDTO(52.40400222572092, -118.17283623538863));
		list.add(new CoordinatesDTO(53.81588936978401, -120.05838351597573));
		list.add(new CoordinatesDTO(59.997475628529905, -119.97657318706973));
		return list;
	}
	public  List<CoordinatesDTO> coordBritishColumbia() {
		List<CoordinatesDTO> list = new ArrayList<CoordinatesDTO>();
		list.add(new CoordinatesDTO(60.00167493802728, -119.96293577806122));
		list.add(new CoordinatesDTO(60.00479498280504, -139.0122213503945));
		list.add(new CoordinatesDTO(58.996679608225165, -137.54075936975624));
		list.add(new CoordinatesDTO(59.77000679379995, -135.56798836744247));
		list.add(new CoordinatesDTO(56.59920476747421, -131.85424793439498));
		list.add(new CoordinatesDTO(56.09585522208135, -130.06311869054923));
		list.add(new CoordinatesDTO(55.232483402315154, -130.08489726066682));
		list.add(new CoordinatesDTO(54.79300958403719, -130.66150899373523));
		list.add(new CoordinatesDTO(54.28392446812328, -134.31486004585275));
		list.add(new CoordinatesDTO(50.52072271530836, -129.5736893435047));
		list.add(new CoordinatesDTO(48.237663380236015, -123.62290552183377));
		list.add(new CoordinatesDTO(48.41930280518063, -123.1624132330164));
		list.add(new CoordinatesDTO(48.71676292301189, -123.28496080497453));
		list.add(new CoordinatesDTO(48.83685152659265, -123.01497370035771));
		list.add(new CoordinatesDTO(49.014211183133156, -123.34848105033286));
		list.add(new CoordinatesDTO(49.01109557547156, -114.10094084758776));
		
		list.add(new CoordinatesDTO(49.00295143665085, -114.08476092008524));
		list.add(new CoordinatesDTO(49.62985638638018, -114.68331232809047));
		list.add(new CoordinatesDTO(50.434905094934955, -114.83558343921075));
		list.add(new CoordinatesDTO(51.83863247024028, -116.73200245930967));
		list.add(new CoordinatesDTO(51.71276981753783, -116.89819459407767));
		list.add(new CoordinatesDTO(52.13178784552697, -117.32485804766921));
		list.add(new CoordinatesDTO(52.270104197900594, -117.86457507778985));
		list.add(new CoordinatesDTO(52.378633690876214, -117.7319793789927));
		list.add(new CoordinatesDTO(52.37778145638933, -117.76323217507199));
		list.add(new CoordinatesDTO(52.40400222572092, -118.17283623538863));
		list.add(new CoordinatesDTO(53.81588936978401, -120.05838351597573));
		list.add(new CoordinatesDTO(59.997475628529905, -119.97657318706973));
		return list;
	}
	
	public  List<CoordinatesDTO> coordYukon() {
		List<CoordinatesDTO> list = new ArrayList<CoordinatesDTO>();

		list.add(new CoordinatesDTO(69.48229172800555, -141.07099441820688));
		list.add(new CoordinatesDTO(60.25259469123802, -140.91903879953162));
		list.add(new CoordinatesDTO(60.16368144725077, -139.04023831808342));
		list.add(new CoordinatesDTO(60.111968461265704, -124.00753372921751));

		list.add(new CoordinatesDTO(60.16265872973902, -123.89635670992497));
		list.add(new CoordinatesDTO(60.8068310944076, -126.69905728577048));
		list.add(new CoordinatesDTO(62.070243197067434, -128.64379776099568));
		list.add(new CoordinatesDTO(62.46236068576795, -129.23381457788687));
		list.add(new CoordinatesDTO(62.86713257142192, -129.7826052934066));
		list.add(new CoordinatesDTO(63.046994067260904, -129.64227997161092));
		list.add(new CoordinatesDTO(63.29789638449202, -130.11553954271582));
		list.add(new CoordinatesDTO(63.489907143991644, -129.897865842024));
		list.add(new CoordinatesDTO(64.20347552893644, -130.93313886565187));
		list.add(new CoordinatesDTO(64.83032635424536, -132.68503295321864));
		list.add(new CoordinatesDTO(65.24509147322954, -132.71064977598803));
		list.add(new CoordinatesDTO(65.45833969932383, -132.28759782603484));
		list.add(new CoordinatesDTO(65.66257651180575, -132.27341778962855));
		list.add(new CoordinatesDTO(65.85040477061567, -132.58204802351244));
		list.add(new CoordinatesDTO(66.2919878028592, -133.77255384086243));
		list.add(new CoordinatesDTO(66.98491292955974, -133.9468223605557));
		list.add(new CoordinatesDTO(67.02726818875843, -136.10967163554798));
		list.add(new CoordinatesDTO(67.65291117666553, -136.4507223617683));
		list.add(new CoordinatesDTO(70.47522368815562, -136.5406506752691));	
		
		return list;
	}
	
	public  List<CoordinatesDTO> coordNorthWest() {
		List<CoordinatesDTO> list = new ArrayList<CoordinatesDTO>();
		list.add(new CoordinatesDTO(70.57033272695321, -136.40254179737613));
		list.add(new CoordinatesDTO(67.14588993842624, -136.08354105088208));
		list.add(new CoordinatesDTO(67.00857041907318, -134.129081086311));
		list.add(new CoordinatesDTO(62.36726435378997, -129.49039853395053));
		list.add(new CoordinatesDTO(60.881240739907106, -126.95127618163693));
		list.add(new CoordinatesDTO(60.76994854783681, -124.74840968267115));
		list.add(new CoordinatesDTO(60.039560887546706, -123.96778102868545));
	
		list.add(new CoordinatesDTO(60.07700768173146, -101.99743375164195));
		list.add(new CoordinatesDTO(64.19210871170054, -101.95097309014487));
		list.add(new CoordinatesDTO(64.8255169022438, -109.32933340263675));
		list.add(new CoordinatesDTO(65.40869497305182, -112.77423134355631));
		list.add(new CoordinatesDTO(67.90627683513723, -120.91743785191137));
		list.add(new CoordinatesDTO(69.47016397763173, -120.8864336688057));
		list.add(new CoordinatesDTO(69.93959540458675, -117.40160996628131));
		list.add(new CoordinatesDTO(69.92488223860005, -110.08800870909398));
		list.add(new CoordinatesDTO(80.5331246469902, -110.29069117434076));
		
		
		return list;
	}
	
	public  List<CoordinatesDTO> coordNuvanut() {
		List<CoordinatesDTO> list = new ArrayList<CoordinatesDTO>();
		list.add(new CoordinatesDTO(83.36683795858804, -54.29606282022294));
		list.add(new CoordinatesDTO(74.53401598738567, -77.35421123911964));
		list.add(new CoordinatesDTO(66.81594975727509, -58.89013719947662));
		list.add(new CoordinatesDTO(61.311133425947595, -62.02740093721954));
		list.add(new CoordinatesDTO(58.848355872105586, -67.36031308472037));
		list.add(new CoordinatesDTO(61.19134664451945, -69.25634386651635));
		list.add(new CoordinatesDTO(62.76508507693693, -73.2925065791369));
		list.add(new CoordinatesDTO(62.93406723247309, -78.61836164361746));
		list.add(new CoordinatesDTO(59.01631218246772, -79.11992913954334));
		list.add(new CoordinatesDTO(55.56638803080534, -78.94621191804059));
		list.add(new CoordinatesDTO(52.28732385215266, -79.4073822201643));
		list.add(new CoordinatesDTO(53.0605167985287, -82.16015239000657));
		list.add(new CoordinatesDTO(55.31310126574127, -81.51518470453425));
		list.add(new CoordinatesDTO(58.1489698993239, -93.1099938075882));
		list.add(new CoordinatesDTO(60.0572934351926, -94.79501500024804));
		list.add(new CoordinatesDTO(60.07700768173146, -101.99743375164195));
		list.add(new CoordinatesDTO(64.19210871170054, -101.95097309014487));
		list.add(new CoordinatesDTO(64.8255169022438, -109.32933340263675));
		list.add(new CoordinatesDTO(65.40869497305182, -112.77423134355631));
		list.add(new CoordinatesDTO(67.90627683513723, -120.91743785191137));
		list.add(new CoordinatesDTO(69.47016397763173, -120.8864336688057));
		list.add(new CoordinatesDTO(69.93959540458675, -117.40160996628131));
		list.add(new CoordinatesDTO(69.92488223860005, -110.08800870909398));
		list.add(new CoordinatesDTO(80.5331246469902, -110.29069117434076));
		
		
		
		return list;
	}
	public  List<CoordinatesDTO> coordQuebec() {
		List<CoordinatesDTO> list = new ArrayList<CoordinatesDTO>();
		list.add(new CoordinatesDTO(60.417260875947676, -64.7267504342034));
			list.add(new CoordinatesDTO(57.26060703309579, -63.86412346004352));
			list.add(new CoordinatesDTO(56.28177490676255, -64.11111674049735));
			list.add(new CoordinatesDTO(54.752714966005534, -63.92007972709496));
			list.add(new CoordinatesDTO(55.18818984785586, -67.26016155977094));
			list.add(new CoordinatesDTO(54.156863151216086, -67.7672562464753));
			list.add(new CoordinatesDTO(52.979033983744515, -67.35107047538834));
			list.add(new CoordinatesDTO(52.26663781166877, -66.42260740238628));
			list.add(new CoordinatesDTO(51.668906948463345, -64.47791137914402));
			list.add(new CoordinatesDTO(52.86155356996913, -64.25689447917318));
			list.add(new CoordinatesDTO(52.73942129270873, -63.40766724925855));
			list.add(new CoordinatesDTO(52.49310213433193, -63.98400151539662));
			list.add(new CoordinatesDTO(52.04529883658521, -63.795141074013706));
			list.add(new CoordinatesDTO(51.985713834687004, -57.179294439442955));
			list.add(new CoordinatesDTO(51.41818706281442, -57.117846936322366));

			list.add(new CoordinatesDTO(46.85879837527435, -61.67628379013645));
			list.add(new CoordinatesDTO(48.09750983270656, -64.45762808494074));
			list.add(new CoordinatesDTO(48.04562409895735, -66.5512535139864));
			list.add(new CoordinatesDTO(47.92092994850096, -67.54530846006986));
			list.add(new CoordinatesDTO(47.94287721904032, -68.3632523479568));
			list.add(new CoordinatesDTO(47.44476208620955, -69.2337407396888));
			list.add(new CoordinatesDTO(45.05982419744241, -71.51225277748453));
			list.add(new CoordinatesDTO(45.02118199493748, -74.73138600176877));
			list.add(new CoordinatesDTO(45.213514346627434, -74.39788926418616));
			list.add(new CoordinatesDTO(45.33447077557668, -74.46442397544341));
			list.add(new CoordinatesDTO(45.55974558474102, -74.41685744691166));
			list.add(new CoordinatesDTO(45.6312834845755, -74.97043060686062));
			list.add(new CoordinatesDTO(45.453764982538026, -75.73223611763095));
			list.add(new CoordinatesDTO(45.392447879550204, -75.86608280198499));
			list.add(new CoordinatesDTO(45.55542393534233, -76.19013768011287));
			list.add(new CoordinatesDTO(45.45805854896659, -76.34189433120903));
			list.add(new CoordinatesDTO(45.56222356675284, -76.67382034145218));
			list.add(new CoordinatesDTO(45.891404555828046, -76.82051475598756));
			list.add(new CoordinatesDTO(45.84710787368224, -77.06368003811929));
			list.add(new CoordinatesDTO(46.00148298253443, -77.26207851181626));
			list.add(new CoordinatesDTO(46.20385534557218, -77.66465033471717));
			list.add(new CoordinatesDTO(46.342045268956106, -78.66016391891871));
			list.add(new CoordinatesDTO(47.104490683616305, -79.44057624754187));
			list.add(new CoordinatesDTO(47.279362168682, -79.43910821676734));
			list.add(new CoordinatesDTO(47.44397759803197, -79.58633684753333));
			list.add(new CoordinatesDTO(47.56995133201416, -79.51325855596292));
			list.add(new CoordinatesDTO(51.58375316945923, -79.54756876400717));
			list.add(new CoordinatesDTO(51.76401030496185, -79.10557346491743));
			list.add(new CoordinatesDTO(52.37802104155307, -78.52866881587312));
			list.add(new CoordinatesDTO(52.98020284477353, -79.02377798504783));
			list.add(new CoordinatesDTO(53.01686078336673, -78.98978912700295));
			list.add(new CoordinatesDTO(54.026093623436566, -79.12738496910195));
			list.add(new CoordinatesDTO(54.66199600598573, -79.80220291228467));
			list.add(new CoordinatesDTO(55.32984972348328, -77.84341550897649));
			list.add(new CoordinatesDTO(56.07907459490651, -76.69281973644898));
			list.add(new CoordinatesDTO(56.96165486672711, -76.62660787795353));
			list.add(new CoordinatesDTO(57.50505615722365, -76.79080818366009));
			list.add(new CoordinatesDTO(58.01415466529536, -77.2328193972423));
			list.add(new CoordinatesDTO(58.17876416042872, -77.47536921306659));
			list.add(new CoordinatesDTO(58.45178814258202, -78.12904789879678));
			list.add(new CoordinatesDTO(58.614735790159145, -78.60377287044943));
			list.add(new CoordinatesDTO(58.925420193739065, -78.59831008376689));
			list.add(new CoordinatesDTO(59.2704258053124, -78.02293285895166));
			list.add(new CoordinatesDTO(59.749954198206325, -78.04970026652907));
			list.add(new CoordinatesDTO(60.00157591454002, -77.40371354744977));
			list.add(new CoordinatesDTO(59.99300490094054, -77.66043471642037));
			list.add(new CoordinatesDTO(60.79021090411879, -78.24874450253532));
			list.add(new CoordinatesDTO(62.43959171708202, -78.0815668296533));
			list.add(new CoordinatesDTO(62.60013042209284, -77.34857663210461));
			list.add(new CoordinatesDTO(62.551648173321624, -73.6426762524337));
			list.add(new CoordinatesDTO(62.24152530246576, -73.1414878694419));
			list.add(new CoordinatesDTO(62.139931851368345, -72.57067440036204));
			list.add(new CoordinatesDTO(61.602991648313086, -71.43486845007844));
			list.add(new CoordinatesDTO(61.29343288184857, -71.69484671238058));
			list.add(new CoordinatesDTO(61.0962643952135, -70.07583574356968));
			list.add(new CoordinatesDTO(60.89631416204123, -69.98126206733356));
			list.add(new CoordinatesDTO(61.194439480388795, -69.29517043022237));
			list.add(new CoordinatesDTO(58.36107148701624, -67.41016721731332));
			list.add(new CoordinatesDTO(60.36800480075125, -64.89284580550878));

		return list;
	}
	
	public  List<CoordinatesDTO> coordNewfoundLandLabrador() {
		List<CoordinatesDTO> list = new ArrayList<CoordinatesDTO>();
		list.add(new CoordinatesDTO(60.417260875947676, -64.7267504342034));
			list.add(new CoordinatesDTO(57.26060703309579, -63.86412346004352));
			list.add(new CoordinatesDTO(56.28177490676255, -64.11111674049735));
			list.add(new CoordinatesDTO(54.752714966005534, -63.92007972709496));
			list.add(new CoordinatesDTO(55.18818984785586, -67.26016155977094));
			list.add(new CoordinatesDTO(54.156863151216086, -67.7672562464753));
			list.add(new CoordinatesDTO(52.979033983744515, -67.35107047538834));
			list.add(new CoordinatesDTO(52.26663781166877, -66.42260740238628));
			list.add(new CoordinatesDTO(51.668906948463345, -64.47791137914402));
			list.add(new CoordinatesDTO(52.86155356996913, -64.25689447917318));
			list.add(new CoordinatesDTO(52.73942129270873, -63.40766724925855));
			list.add(new CoordinatesDTO(52.49310213433193, -63.98400151539662));
			list.add(new CoordinatesDTO(52.04529883658521, -63.795141074013706));
			list.add(new CoordinatesDTO(51.985713834687004, -57.179294439442955));
			list.add(new CoordinatesDTO(51.41818706281442, -57.117846936322366));
			list.add(new CoordinatesDTO(52.77373762820643, -53.4526111518242));
			list.add(new CoordinatesDTO(60.56644177900971, -64.32957608439246));
			
		return list;
	}
	
	public  List<CoordinatesDTO> coordNewfoundLand() {
		List<CoordinatesDTO> list = new ArrayList<CoordinatesDTO>();
		list.add(new CoordinatesDTO(51.40744592572032, -57.123001560579866));
		list.add(new CoordinatesDTO(51.95789769712626, -54.5887420345233));
		list.add(new CoordinatesDTO(46.19636407632065, -51.181231224369114));
		list.add(new CoordinatesDTO(46.82853226987371, -55.9970928400476));
		list.add(new CoordinatesDTO(47.28867441659064, -56.21022130925138));
		list.add(new CoordinatesDTO(47.51640138259039, -60.33091979730681));
		list.add(new CoordinatesDTO(51.42086817950343, -57.18946634816305));

			
		return list;
	}
	public  List<CoordinatesDTO> coordNovaScotia() {
		List<CoordinatesDTO> list = new ArrayList<CoordinatesDTO>();
		list.add(new CoordinatesDTO(46.754013559868675, -61.720507497921815));
		list.add(new CoordinatesDTO(46.35733939354394, -61.64417279706867));
		list.add(new CoordinatesDTO(45.88813252989284, -62.29802382873078));
		list.add(new CoordinatesDTO(45.86186374656335, -62.71269029862646));
		list.add(new CoordinatesDTO(45.94200379182479, -63.016313284365644));
		list.add(new CoordinatesDTO(46.09648124800615, -63.57469034630141));
		list.add(new CoordinatesDTO(45.99185490536912, -64.04044075709592));
		list.add(new CoordinatesDTO(45.976792614030295, -64.15759736907498));
		list.add(new CoordinatesDTO(45.841434888891506, -64.27539123128611));
		list.add(new CoordinatesDTO(45.33560923900191, -65.21558932233462));
		list.add(new CoordinatesDTO(44.20974507649306, -66.86031613566419));
		list.add(new CoordinatesDTO(43.16466858019313, -65.62735540783817));
		list.add(new CoordinatesDTO(44.49675890233199, -63.405894241609595));
		list.add(new CoordinatesDTO(46.13058999684428, -59.28836265248863));
		list.add(new CoordinatesDTO(46.50028478972634, -60.18825550025889));
		list.add(new CoordinatesDTO(47.18790025142264, -60.337041346698456));
		list.add(new CoordinatesDTO(46.7927120622858, -61.73402015469788));		
		return list;
	}
	
	public  List<CoordinatesDTO> coordPrinceEdward() {
		List<CoordinatesDTO> list = new ArrayList<CoordinatesDTO>();
		list.add(new CoordinatesDTO(45.86168247777564, -62.71468724858859));
		list.add(new CoordinatesDTO(45.89617723352317, -62.2626988835416));
		list.add(new CoordinatesDTO(46.34842002121956, -61.649023403041625));
		list.add(new CoordinatesDTO(46.73249604739188, -61.729407838393556));
		list.add(new CoordinatesDTO(46.75784705404249, -63.14016499733922));
		list.add(new CoordinatesDTO(47.25501551125612, -64.01724729346611));
		list.add(new CoordinatesDTO(46.72721983927138, -64.62134097757));
		list.add(new CoordinatesDTO(46.36830883140985, -64.2990043849436));
		list.add(new CoordinatesDTO(46.12679670245748, -63.592835575275565));
		list.add(new CoordinatesDTO(45.85993463249875, -62.737445282138125));	
		return list;
	}
	public  List<CoordinatesDTO> coordNewBrunswick() {
		List<CoordinatesDTO> list = new ArrayList<CoordinatesDTO>();
		list.add(new CoordinatesDTO(45.99391161826003, -64.05222538816));
		list.add(new CoordinatesDTO(46.10868549435191, -63.657022084664995));
		list.add(new CoordinatesDTO(46.62911258888883, -64.56480841164367));
		list.add(new CoordinatesDTO(48.14578270959206, -64.2960264036491));
		list.add(new CoordinatesDTO(48.072761868564285, -66.26565758831438));
		list.add(new CoordinatesDTO(48.00763426549616, -66.76137138746337));
		list.add(new CoordinatesDTO(47.86372856320114, -67.41282301733202));
		list.add(new CoordinatesDTO(47.98538854999911, -67.64159709635824));
		list.add(new CoordinatesDTO(48.012910704137866, -67.60754115440666));
		list.add(new CoordinatesDTO(47.99456145530315, -68.11767287653865));
		list.add(new CoordinatesDTO(47.936226794747796, -68.127141844374));
		list.add(new CoordinatesDTO(47.91980178715528, -68.38838117589353));
		list.add(new CoordinatesDTO(47.521693984907074, -68.40366060563838));
		list.add(new CoordinatesDTO(47.30783183982756, -69.0411638553259));
		list.add(new CoordinatesDTO(47.36191208862278, -68.33371989613343));
		list.add(new CoordinatesDTO(47.045718809783104, -67.79453379662976));
		list.add(new CoordinatesDTO(45.69404080097604, -67.80071291719253));
		list.add(new CoordinatesDTO(45.59564977354906, -67.44195599676296));
		list.add(new CoordinatesDTO(45.26813032691618, -67.45889211831012));
		list.add(new CoordinatesDTO(45.17742926915822, -67.18236278776355));
		list.add(new CoordinatesDTO(44.684521083476966, -67.00313828788511));
		list.add(new CoordinatesDTO(44.42663728899979, -66.68272908599445));
		list.add(new CoordinatesDTO(45.518797606836465, -64.90597753663747));
		list.add(new CoordinatesDTO(45.82578856447828, -64.29999114790166));
		list.add(new CoordinatesDTO(45.976588989541916, -64.15633205655483));

		return list;
	}
	


}
