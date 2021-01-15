package com.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

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
import com.util.Util;
import com.util.Utility;
import com.util.UtilityTimer;


public class PlanetIsCalling extends JFrame implements Info {

	private static final long serialVersionUID = 1L;
	
	private	JPanel	panel6;

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
	public PlanetIsCalling() {

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
		
		itemTabPanel6();
		
		JTabbedPane tabPane = new JTabbedPane();
		tabPane.addTab( "Fightplan", new PanelFlightplan().getPanel(selectCity,selectMountain,selectVor, selectNdb));
		tabPane.addTab( "ICAO", new PaneIcaolAiport().getPanel(selectVor, selectNdb, selectMountain, selectCity));
		tabPane.addTab( "Airport", new PanelLandmarks().getAirportPanel(selectCity,selectMountain,selectVor, selectNdb));
		tabPane.addTab( "City", new PanelLandmarks().getCityPanel(selectCity, selectMountain,selectVor, selectNdb));
		tabPane.addTab( "Mountain",new PanelLandmarks().getMountainPanel(selectCity, selectMountain,selectVor, selectNdb));
		tabPane.addTab( "Setting", new PanelManage().getSettingPanel(this));
		tabPane.addTab( "About", panel6);
		mainPanel.add(tabPane);

		UtilityTimer.getInstance().initTimer();
		UtilityTimer.getInstance().startTimer(this);

	}
	

	public void itemTabPanel6()
	{
		
		JButton btn7 = new JButton("Button 7");
		btn7.setBounds(10, 11, 89, 23);
		
		JButton btn8 = new JButton("Button 8");
		btn8.setBounds(10, 45, 89, 23);
	}
	
	
}