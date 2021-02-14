package com.main;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.Timer;
import javax.swing.plaf.ColorUIResource;

import com.cfg.common.Info;
import com.db.SelectCity;
import com.db.SelectDB;
import com.db.SelectMountain;
import com.db.SelectNdb;
import com.db.SelectVor;
import com.model.Airport;
import com.panels.PaneIcaolAiport;
import com.panels.PanelFlightplan;
import com.panels.PanelLandmarks;
import com.panels.PanelManage;
import com.panels.PanelWelcome;
import com.util.Util;
import com.util.Utility;
import com.util.UtilityEarth;
import com.util.UtilityTimer;


public class PlanetIsCalling extends JFrame implements  Info {

	private static final long serialVersionUID = 1L;

	private SelectCity selectCity;
	private SelectMountain selectMountain;
	private SelectVor selectVor;
	private SelectNdb selectNdb;
    private String time;
	private SelectDB selectDB;
	private String localAbbreviation;
	
	private String timeUTC;
	private String timeLocal;
	private long millis = 0;
	private String timer = "";

	private JPanel	panelWelcome;
	private JLabel labelImage;
	private JLabel labelText;


	
	private List<Airport> airports ;
	
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

        selectCity = new SelectCity();
		selectMountain = new SelectMountain();
		selectVor = new SelectVor();
		selectNdb = new SelectNdb();
		airports = new ArrayList<>();
		
		selectCity.selectAll("");
		selectMountain.selectAll("");
		selectVor.selectAll("");
		selectNdb.selectAll("");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 510);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    setLocation(dim.width/2 - getWidth()/2, dim.height/2 - getHeight()/2);
	    setResizable(false);
	    setTitle("The Planet Is Calling 0.9 ");
	    
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
		
		
		
		JTabbedPane tabPane = new JTabbedPane();
		tabPane.addTab( "Welcome", new PanelWelcome().getPanel());
		tabPane.addTab( "Flightplan", null,new PanelFlightplan().getPanel(selectCity,selectMountain,selectVor, selectNdb),"Select your Flight Plan Here");
		tabPane.addTab( "ICAO",null, new PaneIcaolAiport().getPanel(selectVor, selectNdb, selectMountain, selectCity),"Search Airport and More by ICAO Code(s)");
		tabPane.addTab( "Airport",null, new PanelLandmarks().getAirportPanel(selectCity,selectMountain,selectVor, selectNdb),"Search Airport(s) and More by City airport");
		tabPane.addTab( "City", null,new PanelLandmarks().getCityPanel(selectCity, selectMountain,selectVor, selectNdb),"Search Cities and More by Country and State");
		tabPane.addTab( "Mountain",null,new PanelLandmarks().getMountainPanel(selectCity, selectMountain,selectVor, selectNdb),"Search Moutnains and More by Country");
		tabPane.addTab( "Landmark",null, new PanelLandmarks().getLandMarkPanel(),"Search Airport(s) and More by City airport");
		tabPane.addTab( "Setting",null, new PanelManage().getSettingPanel(this),"Setup Application Folders and More");
		mainPanel.add(tabPane);

		UtilityTimer.getInstance().initTimer();
		UtilityTimer.getInstance().startTimer(this);
		
		UtilityEarth.getInstance().initPanelWait(this);

	}
	


	
}