package com.panels;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import com.cfg.common.DistanceSpinner;
import com.cfg.file.ManageXMLFile;
import com.cfg.model.Placemark;
import com.model.Distance;
import com.model.Result;
import com.util.ReadData;

import net.SelectAiport;
import net.SelectCity;
import net.SelectMountain;
import net.SelectNdb;
import net.SelectVor;

public class PanelPlan {

	private ReadData readData;	
	private Result result;
	private JPanel panelFlightplan = new JPanel();
	private JPanel panelResult;
	
	private JButton buttonReload;
	
	private DistanceSpinner distanceSpin; 
	
	private ManageXMLFile manageXMLFile;
	private SelectAiport selectAiport;
	private SelectCity selectCity;
	private SelectMountain selectMountain;
	private SelectVor selectVor;
	private SelectNdb selectNdb;

	private JButton buttonFP;

	public PanelPlan() {
		super();

	}

	
	public JPanel getFlightplan(final ManageXMLFile manageXMLFile,SelectAiport selectAiport,SelectCity selectCity,SelectMountain selectMountain, SelectVor selectVor, SelectNdb selectNdb) {
		this.manageXMLFile = manageXMLFile;
		this.selectAiport = selectAiport;
		this.selectCity = selectCity;
		this.selectMountain = selectMountain;
		this.selectVor = selectVor;
		this.selectNdb = selectNdb;

		return createPanel();
	}
	
	public JPanel createPanel() {
		
		distanceSpin = new DistanceSpinner();
		distanceSpin.initPanelDistances("plan");
		
		result = new Result();
		
		panelFlightplan = new JPanel();
		panelFlightplan.setLayout(null);

		panelResult = new JPanel(new BorderLayout());
		panelResult.setBorder(new TitledBorder("Search Result"));
		
		buttonFP = new JButton("Select Flightplan");
		buttonFP.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  result = new Result();
	    	  readData =  new ReadData(result, manageXMLFile, selectAiport, selectCity, selectMountain, selectVor, selectNdb,
		        		 new Distance((int)distanceSpin.getCitySpinner().getValue(), 
		        				 (int)distanceSpin.getMountainSpinner().getValue(), 
		        				 (int)distanceSpin.getAirportSpinner().getValue(),
		        				 (int)distanceSpin.getVorNdbSpinner().getValue(), 
		        				 distanceSpin.getCheckTocTod().isSelected(),
		        				 0.0));
    	  
	  		 panelResult.removeAll();	
	  		 panelResult.setBounds(290, 20, 300, 240);	
			 panelResult.add(result.getResultPanel());
			 panelResult.validate();

			 panelFlightplan.add(panelResult);
			 panelFlightplan.validate();

			 buttonReload.setVisible(true); 
	      }
	    });
		
		buttonReload = new JButton("Update and Load KML File");
		buttonReload.addActionListener(new ActionListener()
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

		  	 panelResult.setBounds(290, 20, 300, 240);	
		
	    	 panelResult.add(result.getResultPanel());
			 panelResult.validate();
			 panelFlightplan.add(panelResult);
			 panelFlightplan.validate();
	    	 
	    	 manageXMLFile.launchGoogleEarth(new File(readData.getKmlFlightPlanFile()));
	        
	      }
	    });
		buttonReload.setVisible(false); 
		
		buttonFP.setBounds(10, 20, 200, 23);
		distanceSpin.getSpinnerPanel().setBounds(10, 50, 260, 200);

    	
		buttonReload.setBounds(200, 270, 200, 23);

	  	panelResult.setBounds(290, 20, 300, 240);	

     	panelFlightplan.add(buttonFP);
		panelFlightplan.add(panelResult);
		panelFlightplan.add(distanceSpin.getSpinnerPanel());
     	panelFlightplan.add(buttonReload);

		return panelFlightplan;					
	}
	
	

}
