package com.panels;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.cfg.common.DistanceSpinner;
import com.cfg.file.ManageXMLFile;
import com.model.Distance;
import com.model.Result;
import com.util.ReadData;

import net.SelectAiport;
import net.SelectCity;
import net.SelectMountain;
import net.SelectNdb;
import net.SelectVor;

public class PanelFlightplan {

	private ReadData readData;	
	private Result result;
	private JPanel panelFlightplan = new JPanel();
	private JPanel panelResult;
	
	private JButton buttonGoogle;
	
	private DistanceSpinner distanceSpin; 
	
	private ManageXMLFile manageXMLFile;
	private SelectAiport selectAiport;
	private SelectCity selectCity;
	private SelectMountain selectMountain;
	private SelectVor selectVor;
	private SelectNdb selectNdb;
	
	private JPanel outputPanel;
	
	private String flightPlanFile;
	private JButton buttonFP;

	public PanelFlightplan() {
		super();

	}

	public JPanel getPanel(final ManageXMLFile manageXMLFile,SelectCity selectCity,SelectMountain selectMountain, SelectVor selectVor, SelectNdb selectNdb) {
		this.manageXMLFile = manageXMLFile;
		this.selectAiport = selectAiport;
		this.selectCity = selectCity;
		this.selectMountain = selectMountain;
		this.selectVor = selectVor;
		this.selectNdb = selectNdb;
		this.flightPlanFile = "";

		return createPanel();
	}
	
	public JPanel createPanel() {
		
		distanceSpin = new DistanceSpinner();
		distanceSpin.initPanelDistances("plan");
		
		result = new Result();
		
		panelFlightplan = new JPanel();
		panelFlightplan.setLayout(null);

		panelResult = new JPanel(new BorderLayout());
		panelResult.setBorder(new TitledBorder(this.flightPlanFile));
		outputPanel = new JPanel(new BorderLayout());
		outputPanel.setBorder(new TitledBorder(""));
		
		result.setOutputPanel(outputPanel);
		
		buttonFP = new JButton("Select Flightplan");
		buttonFP.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  readData =  new ReadData(result, manageXMLFile, selectCity, selectMountain, selectVor, selectNdb,
		        		 new Distance((int)distanceSpin.getCitySpinner().getValue(), 
		        				 (int)distanceSpin.getMountainSpinner().getValue(), 
		        				 (int)distanceSpin.getAirportSpinner().getValue(),
		        				 (int)distanceSpin.getVorNdbSpinner().getValue(), 
		        				 distanceSpin.getCheckTocTod().isSelected(),
		        				 0.0));
    	  
	    	 flightPlanFile = new File(result.getFlightplan().getFlightplanFile()).getName();
	 		 panelResult.setBorder(new TitledBorder(new File(flightPlanFile).getName()));

	  		 panelResult.removeAll();	
			 panelResult.add(result.getFlightPlanPanel());
			 panelResult.validate();
			 
		  	 result.getWaypointListModel();

			 panelFlightplan.add(panelResult);
			 panelFlightplan.validate();

			 buttonGoogle.setVisible(true); 
	      }
	    });
		
		buttonGoogle = new JButton("See on Google Earth");
		buttonGoogle.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	 readData.createFlightplan(
	    			 new Distance((int)distanceSpin.getCitySpinner().getValue(), 
	    			 (int)distanceSpin.getMountainSpinner().getValue(), 
	    			 (int)distanceSpin.getAirportSpinner().getValue(),
	    			 (int)distanceSpin.getVorNdbSpinner().getValue(), 
	    			 distanceSpin.getCheckTocTod().isSelected(),
	    			 (double)result.getAltitudeModel().getValue())); 
		  	panelResult.removeAll();	

		  	 panelResult.setBounds(290, 20, 300, 260);	


		
	    	 panelResult.add(result.getFlightPlanPanel());
			 panelResult.validate();
			 panelFlightplan.add(panelResult);
			 panelFlightplan.validate();
	    	 
	    	 manageXMLFile.launchGoogleEarth(new File(readData.getKmlFlightPlanFile()));
	        
	      }
	    });
		buttonGoogle.setVisible(false); 
		
		buttonFP.setBounds(10, 20, 200, 23);
		distanceSpin.getSpinnerPanel().setBounds(10, 50, 260, 200);

    	
		buttonGoogle.setBounds(10, 270, 200, 23);

	  	panelResult.setBounds(290, 10, 300, 240);	
	  	outputPanel.setBounds(290, 250, 300, 140);	

     	panelFlightplan.add(buttonFP);
		panelFlightplan.add(panelResult);
		panelFlightplan.add(outputPanel);
		panelFlightplan.add(distanceSpin.getSpinnerPanel());
     	panelFlightplan.add(buttonGoogle);

		return panelFlightplan;					
	}

}
