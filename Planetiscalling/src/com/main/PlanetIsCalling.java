package com.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.cfg.common.Info;
import com.db.UtilityDB;
import com.panels.PaneIcaolAiport;
import com.panels.PanelAirports;
import com.panels.PanelCities;
import com.panels.PanelFlightplan;
import com.panels.PanelLandmarks;
import com.panels.PanelManage;
import com.panels.PanelMountains;
import com.panels.PanelWelcome;
import com.util.Util;
import com.util.Utility;
import com.util.UtilityEarth;
import com.util.UtilityTimer;


public class PlanetIsCalling extends JFrame implements  Info {

	private static final long serialVersionUID = 1L;
	private PanelLandmarks panelLandmarks;
	private PanelCities panelCities;
	private PanelAirports panelAirports;
	private PanelMountains panelMountains; 
	private PanelFlightplan panelFlightplan;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
			EventQueue.invokeLater(new Runnable() {
			public void run() {
				PlanetIsCalling planetIsCalling = new PlanetIsCalling();
				planetIsCalling.setVisible(true);
				}
		});
	}

	/**
	 * Create the frame.
	 */
	public PlanetIsCalling(){

		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 510);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    setLocation(dim.width/2 - getWidth()/2, dim.height/2 - getHeight()/2);
	    setResizable(false);
	    setTitle("The Planet Is Calling 0.910 ");
	    
		Utility.getInstance().readPrefProperties();
	//	int numColor = Util.getStoredPeriodNumber();//Utility.getInstance().getPrefs().getProperty("day.period");
		
		
		UtilityTimer.getInstance().setTimerSartAt(UtilityTimer.getInstance().getTime("local", true));

	    Utility.getInstance().initLookAndFeel(this, Util.DAY_PERIOD.get(Util.getPeriod()) );
		Utility.getInstance().getPrefs().put("day.period", Util.getPeriod());
    	Utility.getInstance().savePrefProperties();
    	UtilityTimer.getInstance().setCurrentPeriod(Util.DAY_PERIOD.get(Util.getPeriod()));

        Utility.getInstance().setIcon(this, imageLogo);
	 
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
		
	
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		getContentPane().add(mainPanel);
		
		
		panelLandmarks = new PanelLandmarks();
		panelCities = new PanelCities();
		panelAirports = new PanelAirports();
		panelMountains = new PanelMountains();
		panelFlightplan = new PanelFlightplan();
		
		
		
		JTabbedPane tabPane = new JTabbedPane();
		tabPane.addTab( "Welcome", new PanelWelcome().getPanel());
		tabPane.addTab( "Flightplan", null,panelFlightplan.getPanel(),"Select your Flight Plan Here");
		tabPane.addTab( "ICAO",null, new PaneIcaolAiport().getPanel(),"Search Airport and More by ICAO Code(s)");
		tabPane.addTab( "Airport",null, panelAirports.getAirportPanel(),"Search Airport(s) and More by City airport");
		tabPane.addTab( "City", null, panelCities.getCityPanel(),"Search Cities and More by Country and State");
		tabPane.addTab( "Mountain",null,panelMountains.getMountainPanel(),"Search Moutnains and More by Country");
		tabPane.addTab( "Landmark",null, panelLandmarks.getLandMarkPanel(),"Search Airport(s) and More by City airport");
		tabPane.addTab( "Setting",null, new PanelManage().getSettingPanel(this),"Setup Application Folders and More");
		mainPanel.add(tabPane);
		
	
		
	    ChangeListener changeListener = new ChangeListener() {
	        public void stateChanged(ChangeEvent changeEvent) {
	          JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
	          int index = sourceTabbedPane.getSelectedIndex();
	          System.out.println("Tab changed to: " + sourceTabbedPane.getTitleAt(index));
				if ("Flightplan".equals(sourceTabbedPane.getTitleAt(index))) {
					if (UtilityDB.getInstance().getMapStateCoords() == null) {
						UtilityDB.getInstance().selectPolygone();
					}
				} else if ("ICAO".equals(sourceTabbedPane.getTitleAt(index))) {
					if (UtilityDB.getInstance().getMapStateCoords() == null) {
						UtilityDB.getInstance().selectPolygone();
					}
				} else if ("Landmark".equals(sourceTabbedPane.getTitleAt(index)) && !panelLandmarks.isLandmarkSetted()) {
					panelLandmarks.setPanelLandmark();
				} else if ("Airport".equals(sourceTabbedPane.getTitleAt(index)) && !panelAirports.isAirportSetted()) {
					panelAirports.setPanelAirport();
				} else if ("City".equals(sourceTabbedPane.getTitleAt(index)) && !panelCities.isCitySetted()) {
					panelCities.setPanelCity();
					;
				}
	          
	        }
	      };	

	  	tabPane.addChangeListener(changeListener);
	      
		UtilityTimer.getInstance().initTimer();
		UtilityTimer.getInstance().startTimer(this);
		
		UtilityEarth.getInstance().initPanelWait(this);

	}
	


	
}