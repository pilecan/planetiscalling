package com.cfg.common;

import java.awt.Color;
import java.awt.Font;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.plaf.ColorUIResource;

public interface Info {
	Color[] colorBackground = {new ColorUIResource(26,108,26), new ColorUIResource(12,133,222),new ColorUIResource(153,59,21),new ColorUIResource(38,30,18),new ColorUIResource(229,228,200)};
	Color[] colorForground = {new Color(255,196,52),new ColorUIResource(222,231,239),new ColorUIResource(255,253,195),new ColorUIResource(222,231,239),Color.lightGray,new ColorUIResource(22,105,23)};
	Color[] colorForgroundBtn = {Color.DARK_GRAY,Color.DARK_GRAY,Color.DARK_GRAY,Color.DARK_GRAY,Color.DARK_GRAY};
	Color[] colorBack = {new Color(165,181,214),new Color(165,181,214),new Color(165,181,214)};
	Color[] colorBackList = {new Color(253,248,138),new Color(222,231,239),new Color(222,231,239),new Color(253,248,138),new Color(146,63,22)};
	Color[] colorFrontList = {new ColorUIResource(255,253,195),new ColorUIResource(222,231,239),Color.lightGray,new ColorUIResource(22,105,23),new Color(255,196,52)};
	Color[] colorBackMenu = {new Color(222,231,239),new Color(222,231,239),new Color(222,231,239)};
	Color[] colorBackArea = {new Color(247,247,255),new Color(247,247,255),new Color(247,247,255)};
	Color[] colorBlueText = {new Color(11,40,117),new Color(11,40,117),new Color(11,40,117)};
    Font fontText 	= new Font("arial", Font.BOLD, 12);
    Font fontTextItalic 	= new Font("arial", Font.BOLD+Font.ITALIC, 13);

    Font fontList 	= new Font("arial", Font.BOLD, 12);
    Font fontTitle 	= new Font("arial", Font.BOLD, 13);
    
	String imagesPath = "/images/";
	//String path = "C:/projects/fsx/readconfig/data/";

	String SCENERY_ROOT_LM = "C:\\ProgramData\\Lockheed Martin\\";
	String SCENERY_ROOT_FSX = "C:\\ProgramData\\Microsoft/FSX\\";
	String SIM_FSX = "Microsoft Flight Simulator X";
	
	String url = "jdbc:sqlite:g:\\addons\\777-tools\\Navdatareader\\airport_runway.db";

	String kmlFlightplanName = "icao_airports.kml";
	String kmlAirportCityMountainName = "airport_city_mountain.kml";
	String kmlAirportCityName = "airport_city.kml";
	String kmlCityAirportMountainName = "city_airport_mountain.kml";
	String kmlMountainCityAirportName = "mountain_city_airport.kml";

    String imageLogo = "/images/hello_here.jpg";
    String datadir  = "/data/";
    String metarFileName = "data/metar/metar.txt";
 	

}
