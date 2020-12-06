package com.cfg.common;

import java.awt.Color;
import java.awt.Font;

import javax.swing.plaf.ColorUIResource;

public interface Info {
	Color[] colorBackground = {new ColorUIResource(183,132,85),new ColorUIResource(222,231,239),new ColorUIResource(38,30,18),new ColorUIResource(229,228,200),new ColorUIResource(22,105,23)};
	Color[] colorForground = {new ColorUIResource(255,253,195),new Color(11,40,117),Color.lightGray,new ColorUIResource(22,105,23),new ColorUIResource(229,228,200)};
	Color[] colorBack = {new Color(165,181,214),new Color(165,181,214),new Color(165,181,214)};
	Color[] colorBackList = {new Color(222,231,239),new Color(222,231,239),new Color(222,231,239)};
	Color[] colorBackMenu = {new Color(222,231,239),new Color(222,231,239),new Color(222,231,239)};
	Color[] colorBackArea = {new Color(247,247,255),new Color(247,247,255),new Color(247,247,255)};
	Color[] colorBlueText = {new Color(11,40,117),new Color(11,40,117),new Color(11,40,117)};
    Font fontText 	= new Font("arial", Font.BOLD, 12);
    Font fontList 	= new Font("arial", Font.BOLD, 12);
    Font fontTitle 	= new Font("arial", Font.BOLD, 13);
    
    

	String imagesPath = "/images/";
	//String path = "C:/projects/fsx/readconfig/data/";
	String sceneryWork = "Scenery_work.CFG";
	String sceneryOriginal = "Scenery_original_before_FSL.CFG";
	String sceneryBak = "Scenery_bak.CFG";
	String sceneryArea = "SceneryArea.dat";
	String ADDON_SCENERY = "Addon Scenery";

	String SCENERY_ROOT_LM = "C:\\ProgramData\\Lockheed Martin\\";
	String SCENERY_ROOT_FSX = "C:\\ProgramData\\Microsoft/FSX\\";
	String SIM_P3D_V3 = "Prepar3D v3";
	String SIM_FSX = "Microsoft Flight Simulator X";
	
	String url = "jdbc:sqlite:g:\\addons\\777-tools\\Navdatareader\\airport_runway.db";

	String fsl = "./FSLaunchPad.exe";
	
    String imageLogo = "/images/fslLogo.jpg";
	
}
