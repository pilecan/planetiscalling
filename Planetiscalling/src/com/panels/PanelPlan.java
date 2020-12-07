package com.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.cfg.common.DistanceSpinner;
import com.cfg.file.ManageXMLFile;
import com.cfg.model.Placemark;
import com.model.Distance;
import com.util.ReadData;

import net.SelectAiport;
import net.SelectCity;
import net.SelectMountain;

public class PanelPlan {

	private ManageXMLFile manageXMLFile;
	private SelectAiport selectAiport;
	private SelectCity selectCity;
	private SelectMountain selectMountain;
	private Map <String, JCheckBox> mapSelector;	
	
	private ReadData readData;	

	private List<Placemark> airports ;

	public PanelPlan() {
		super();

	}

	
	public JPanel getFlightplan(final ManageXMLFile manageXMLFile,final SelectAiport selectAiport,final SelectCity selectCity,final SelectMountain selectMountain) {
		final DistanceSpinner distanceSpin = new DistanceSpinner();
		
		distanceSpin.initPanelDistances("plan");
		
		
		JPanel panelFlightplan = new JPanel();
		panelFlightplan.setLayout(null);
		
		JPanel panelResult = new JPanel();
		JPanel panel = new JPanel();
		
		panel.add(new JLabel("allo"));
		
		final JLabel labelResult = new JLabel();
		
		final JButton buttonGoogle = new JButton("Reload");
		
		JButton buttonFP = new JButton("Flightplan");
		buttonFP.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  labelResult.setText("one moment please");

	    	  readData =  new ReadData(labelResult, manageXMLFile, selectAiport, selectCity, selectMountain,
		        		 new Distance((int)distanceSpin.getCitySpinner().getValue(), (int)distanceSpin.getMountainSpinner().getValue(), (int)distanceSpin.getAirportSpinner().getValue(), distanceSpin.getCheckLinedist().isSelected()));
	    	  
		      manageXMLFile.launchGoogleEarth(new File(readData.getKmlFlightPlanFile()));
			  buttonGoogle.setVisible(true); 

	        
	      }
	    });
		

		buttonGoogle.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  labelResult.setText("one moment please");

	    	 readData.createFlightplan(new Distance((int)distanceSpin.getCitySpinner().getValue(), (int)distanceSpin.getMountainSpinner().getValue(), (int)distanceSpin.getAirportSpinner().getValue(), distanceSpin.getCheckLinedist().isSelected())); 
	    	 manageXMLFile.launchGoogleEarth(new File(readData.getKmlFlightPlanFile()));
	        
	      }
	    });
		buttonGoogle.setVisible(false); 
		
		
		buttonFP.setBounds(10, 20, 120, 23);
		distanceSpin.getSpinnerPanel().setBounds(10, 50, 170, 140);
    	buttonGoogle.setBounds(10, 200, 120, 23);

		labelResult.setBounds(300, 20, 300, 200);		
	
     	panelFlightplan.add(buttonFP);
		panelFlightplan.add(labelResult);
		panelFlightplan.add(distanceSpin.getSpinnerPanel());
     	panelFlightplan.add(buttonGoogle);

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
