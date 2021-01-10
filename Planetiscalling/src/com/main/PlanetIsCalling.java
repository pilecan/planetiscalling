package com.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

import com.cfg.common.Info;
import com.model.Airport;
import com.panels.PaneIcaolAiport;
import com.panels.PanelFlightplan;
import com.panels.PanelLandmarks;
import com.panels.PanelManage;
import com.util.Utility;

import net.SelectCity;
import net.SelectMountain;
import net.SelectNdb;
import net.SelectVor;


public class PlanetIsCalling extends JFrame implements Info {

	private static final long serialVersionUID = 1L;
	
	private	JPanel	panel6;

	private SelectCity selectCity;
	private SelectMountain selectMountain;
	private SelectVor selectVor;
	private SelectNdb selectNdb;

	
	private List<Airport> airports ;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
			EventQueue.invokeLater(new Runnable() {
			public void run() {
				PlanetIsCalling frame = new PlanetIsCalling();
				frame.setVisible(true);
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
	    
	    initLookAndFeel();
	
	 
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
		
        Utility.getInstance().setIcon(this, imageLogo);
	    
		setTitle("The Planet Is Calling 0.888");
	
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
		tabPane.addTab( "Setting", new PanelManage().getSettingPanel());
		tabPane.addTab( "About", panel6);
		mainPanel.add(tabPane);

	}
	
	  private void initLookAndFeel() {
	      String lookAndFeel = "";
	      String LOOKANDFEEL = "Metal";
	      String THEME = "Ocean";
	      
	      if (LOOKANDFEEL != null) {
	          if (LOOKANDFEEL.equals("Metal")) {
	        	  
	              lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
	            //  an alternative way to set the Metal L&F is to replace the 
	            // previous line with:
	            lookAndFeel = "javax.swing.plaf.metal.MetalLookAndFeel";
	               
	          }
	           
	          else if (LOOKANDFEEL.equals("System")) {
	              lookAndFeel = UIManager.getSystemLookAndFeelClassName();
	          } 
	           
	          else if (LOOKANDFEEL.equals("Motif")) {
	              lookAndFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
	          } 
	           
	          else if (LOOKANDFEEL.equals("GTK")) { 
	              lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
	          } 
	           
	          else {
	              System.err.println("Unexpected value of LOOKANDFEEL specified: "
	                                 + LOOKANDFEEL);
	              lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
	          }

	          try {
	              UIManager.setLookAndFeel(lookAndFeel);
	               
	              // If L&F = "Metal", set the theme
	               
	              if (LOOKANDFEEL.equals("Metal")) {
	                if (THEME.equals("DefaultMetal"))
	                   MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
	                else if (THEME.equals(""))
	                   MetalLookAndFeel.setCurrentTheme(new OceanTheme());
	               
	                    
	                UIManager.setLookAndFeel(new MetalLookAndFeel()); 
	              }   
	                   
	          } 
	          catch (ClassNotFoundException e) {
	              System.err.println("Couldn't find class for specified look and feel:"
	                                 + lookAndFeel);
	              System.err.println("Did you include the L&F library in the class path?");
	              System.err.println("Using the default look and feel.");
	          } 
	           
	          catch (UnsupportedLookAndFeelException e) {
	              System.err.println("Can't use the specified look and feel ("
	                                 + lookAndFeel
	                                 + ") on this platform.");
	              System.err.println("Using the default look and feel.");
	          } 
	           
	          catch (Exception e) {
	              System.err.println("Couldn't get specified look and feel ("
	                                 + lookAndFeel
	                                 + "), for some reason.");
	              System.err.println("Using the default look and feel.");
	              e.printStackTrace();
	          }          
	      }
			Utility.getInstance().readPrefProperties();
			int numColor = Integer.parseInt(Utility.getInstance().getPrefs().getProperty("numcolor"));

			
			UIManager.put("OptionPane.background", colorBackground[numColor]);
			UIManager.put("OptionPane.foreground", colorForground[numColor]);
			UIManager.put("Panel.background", colorBackground[numColor]);
		    UIManager.put("Panel.foreground", colorForground[numColor]);
			UIManager.put("ComboBox.background", colorBackground[numColor]);
		    UIManager.put("ComboBox.foreground", colorForground[numColor]);
			UIManager.put("RadioButton.background", colorBackground[numColor]);

		    UIManager.put("Panel.foreground", colorForground[numColor]);
		    UIManager.put("TitledBorder.titleColor", colorForground[numColor]);

		    UIManager.put("Button.foreground", colorForgroundBtn[numColor]);
		  //  UIManager.put("Button.background", Color.WHITE);

			UIManager.put("List.background", colorBackList[numColor]);
		    UIManager.put("List.foreground", colorBlueText[0]);

			UIManager.put("Label.background", colorBackground[numColor]);
		    UIManager.put("Label.foreground", colorForground[numColor]);
		    UIManager.put("CheckBox.select", Color.red);
		    
	
	  }       

	public void itemTabPanel6()
	{
		
		JButton btn7 = new JButton("Button 7");
		btn7.setBounds(10, 11, 89, 23);
		
		JButton btn8 = new JButton("Button 8");
		btn8.setBounds(10, 45, 89, 23);
	}
	
	
}