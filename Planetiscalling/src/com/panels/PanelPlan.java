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

	private List<Placemark> airports ;

	public PanelPlan() {
		super();

	}

	
	public JPanel getFlightplan(final ManageXMLFile manageXMLFile,final SelectAiport selectAiport,final SelectCity selectCity,final SelectMountain selectMountain, final SelectVor selectVor, final SelectNdb selectNdb) {
	
		final DistanceSpinner distanceSpin = new DistanceSpinner();
		distanceSpin.initPanelDistances("plan");
		
		result = new Result();
		
		final JPanel panelFlightplan = new JPanel();
		panelFlightplan.setLayout(null);

		final JPanel panelResult = new JPanel(new BorderLayout());
		panelResult.setBorder(new TitledBorder("Search Result"));

		JPanel panel = new JPanel();
		
		panel.add(new JLabel("allo"));
		
		final JLabel labelResult = new JLabel();
		
		final JButton buttonReload = new JButton("Reload");
		
		JButton buttonFP = new JButton("Select Flightplan");
		buttonFP.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  labelResult.setText("one moment please");
	    	  result = new Result();
	    	  readData =  new ReadData(result, manageXMLFile, selectAiport, selectCity, selectMountain, selectVor, selectNdb,
		        		 new Distance((int)distanceSpin.getCitySpinner().getValue(), 
		        				 (int)distanceSpin.getMountainSpinner().getValue(), 
		        				 (int)distanceSpin.getAirportSpinner().getValue(),
		        				 (int)distanceSpin.getVorNdbSpinner().getValue(), 
		        				 distanceSpin.getCheckTocTod().isSelected(),
		        				 0.0));
	    	  
	    	  
			  panelResult.add(result.getResultPanel());
			  panelFlightplan.add(panelResult);
			  panelFlightplan.validate();

		      manageXMLFile.launchGoogleEarth(new File(readData.getKmlFlightPlanFile()));
			  buttonReload.setVisible(true); 

	        
	      }
	    });
		

		buttonReload.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	 labelResult.setText("one moment please");
	    	 System.out.println(" --->"+result.getAltitudeModel().getValue());
	    	 readData.createFlightplan(
	    			 new Distance((int)distanceSpin.getCitySpinner().getValue(), 
	    			 (int)distanceSpin.getMountainSpinner().getValue(), 
	    			 (int)distanceSpin.getAirportSpinner().getValue(),
	    			 (int)distanceSpin.getVorNdbSpinner().getValue(), 
	    			 distanceSpin.getCheckTocTod().isSelected(),
	    			 (double)result.getAltitudeModel().getValue())); 
	    	 
	    	 manageXMLFile.launchGoogleEarth(new File(readData.getKmlFlightPlanFile()));
	        
	      }
	    });
		buttonReload.setVisible(false); 
		
		buttonFP.setBounds(10, 20, 200, 23);
		distanceSpin.getSpinnerPanel().setBounds(10, 50, 260, 200);
    	buttonReload.setBounds(200, 250, 200, 23);

    	panelResult.setBounds(300, 20, 280, 220);	

		
	
     	panelFlightplan.add(buttonFP);
		panelFlightplan.add(panelResult);
		panelFlightplan.add(distanceSpin.getSpinnerPanel());
     	panelFlightplan.add(buttonReload);

		return panelFlightplan;
	}
	
	public JPanel getAirport() {
		
		JPanel panelSearch = new JPanel();
		JLabel labelResult = new JLabel();
		
		JTextArea textArea = new JTextArea(5, 20);

		
		labelResult.setBounds(300, 40, 89, 63);
		
		
		JButton btn1 = new JButton("Get Airports");
		btn1.setBounds(10, 20, 120, 23);
		panelSearch.add(btn1);
		panelSearch.add(labelResult);
	
		return panelSearch;
			
	}
	
	

}
