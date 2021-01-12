package com.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.cfg.common.Info;
import com.model.Airport;
import com.model.TimeZones;
import com.panels.PaneIcaolAiport;
import com.panels.PanelFlightplan;
import com.panels.PanelLandmarks;
import com.panels.PanelManage;
import com.util.Util;
import com.util.Utility;

import net.SelectCity;
import net.SelectDB;
import net.SelectMountain;
import net.SelectNdb;
import net.SelectVor;


public class PlanetIsCalling extends JFrame implements Info, Runnable {

	private static final long serialVersionUID = 1L;
	
	private	JPanel	panel6;

	private SelectCity selectCity;
	private SelectMountain selectMountain;
	private SelectVor selectVor;
	private SelectNdb selectNdb;
    private String time;
    private TimeZones timeZones;	


	
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
        Thread t1 =new Thread(this);    
        t1.start();   

        selectCity = new SelectCity();
		selectMountain = new SelectMountain();
		selectVor = new SelectVor();
		selectNdb = new SelectNdb();
		airports = new ArrayList<>();
		timeZones = new TimeZones();
		
		TimeZone timeZone = TimeZone.getDefault();
		
		SelectDB selectDB = new SelectDB();
		
		selectDB.selectTimeZone(timeZone.getDisplayName());
		
		System.out.println(selectDB.getTimeZones().toString());
		
		selectCity.selectAll("");
		selectMountain.selectAll("");
		selectVor.selectAll("");
		selectNdb.selectAll("");

		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 510);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    setLocation(dim.width/2 - getWidth()/2, dim.height/2 - getHeight()/2);
	    setResizable(false);
	    setTitle("The Planet Is Calling 0.888 ");
	    
	    
	    Utility.getInstance().initLookAndFeel(this);
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
		tabPane.addTab( "Fightplan", new PanelFlightplan().getPanel(timeZones, selectCity,selectMountain,selectVor, selectNdb));
		tabPane.addTab( "ICAO", new PaneIcaolAiport().getPanel(selectVor, selectNdb, selectMountain, selectCity));
		tabPane.addTab( "Airport", new PanelLandmarks().getAirportPanel(selectCity,selectMountain,selectVor, selectNdb));
		tabPane.addTab( "City", new PanelLandmarks().getCityPanel(selectCity, selectMountain,selectVor, selectNdb));
		tabPane.addTab( "Mountain",new PanelLandmarks().getMountainPanel(selectCity, selectMountain,selectVor, selectNdb));
		tabPane.addTab( "Setting", new PanelManage().getSettingPanel(this));
		tabPane.addTab( "About", panel6);
		mainPanel.add(tabPane);

	}
	
	 public void run()
	    {
	        Calendar calUTC;
	        Calendar calLocal;
	        int hour;
	        int min;
	        int sec;
	        String timeUTC;
	        String timeLocal;
	        while(true) 
	        {
/*	        	TimeZone timeZone = TimeZone.getTimeZone("local");

	            calUTC = new GregorianCalendar(timeZone);
	            hour = calUTC.get(Calendar.HOUR_OF_DAY);
	            min = calUTC.get(Calendar.MINUTE);
	            sec = calUTC.get(Calendar.SECOND);
*/
	            timeUTC = Util.getTime("UTC");
	            timeLocal = Util.getTime("local");
	            //delay the loop for 1 sec
	            try {
	                Thread.currentThread().sleep(1000);
	                //System.out.println(time);
	               // timeZones.setUTC(time);
	        	    setTitle("The Planet Is Calling 0.888                                              "+timeUTC+ "         "+timeLocal );

	                } catch (InterruptedException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }
	        }
	    }

	public void itemTabPanel6()
	{
		
		JButton btn7 = new JButton("Button 7");
		btn7.setBounds(10, 11, 89, 23);
		
		JButton btn8 = new JButton("Button 8");
		btn8.setBounds(10, 45, 89, 23);
	}
	
	
}