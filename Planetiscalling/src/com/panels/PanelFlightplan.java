package com.panels;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

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
	
	private JButton googleBt;
	
	private DistanceSpinner distanceSpin; 
	
	private ManageXMLFile manageXMLFile;
	private SelectAiport selectAiport;
	private SelectCity selectCity;
	private SelectMountain selectMountain;
	private SelectVor selectVor;
	private SelectNdb selectNdb;
	
	private JPanel outputPanel;
	private JPanel buttonLeftPanel;
	private JPanel buttonRightPanel;
	
	
	private String flightPlanFile;
	private JButton buttonBt;
	private JButton refreshBt;
	private JButton resetBt;
	private JButton landMeBt;
	private JButton askMeBt;
	private JButton landAllBt;

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
		
		buttonLeftPanel = new JPanel();
		buttonRightPanel = new JPanel();

		panelResult = new JPanel(new BorderLayout());
		panelResult.setBorder(new TitledBorder(this.flightPlanFile));
		outputPanel = new JPanel(new BorderLayout());
		outputPanel.setBorder(new TitledBorder(""));
		
		result.setOutputPanel(outputPanel);
		
		buttonBt = new JButton("Select Flightplan");
		buttonBt.addActionListener(new ActionListener()
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

			 buttonLeftPanel.setVisible(true); 
	      }
	    });
		
		
		refreshBt = new JButton("Update");
		refreshBt.addActionListener(new ActionListener()
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
			  	 
				  result.getWaypointListModel();


		    	 panelResult.add(result.getFlightPlanPanel());
				 panelResult.validate();
				 panelFlightplan.validate();
	      }
	    });
		
		
		
		
		resetBt = new JButton("Reset");
		resetBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				distanceSpin.getCitySpinner().setValue(0);
				distanceSpin.getAirportSpinner().setValue(0);
				distanceSpin.getVorNdbSpinner().setValue(0);
				distanceSpin.getMountainSpinner().setValue(0);
				distanceSpin.getCheckTocTod().setSelected(false);

				readData.resetResult();
				panelResult.removeAll();
				result.getFlightPlanPanel().validate();
				result.getWaypointListModel();

				panelResult.add(result.getFlightPlanPanel());

				panelResult.validate();
				panelFlightplan.validate();
			}
		});		

		
		googleBt = new JButton("Land on Google Earth");
		googleBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				readData.createFlightplan(new Distance((int) distanceSpin.getCitySpinner().getValue(),
						(int) distanceSpin.getMountainSpinner().getValue(),
						(int) distanceSpin.getAirportSpinner().getValue(),
						(int) distanceSpin.getVorNdbSpinner().getValue(), distanceSpin.getCheckTocTod().isSelected(),
						(double) result.getAltitudeModel().getValue()));
				panelResult.removeAll();

				panelResult.add(result.getFlightPlanPanel());
				panelResult.validate();
				panelFlightplan.add(panelResult);
				panelFlightplan.validate();

				manageXMLFile.launchGoogleEarth(new File(readData.getKmlFlightPlanFile()));

			}
		});
		
		landMeBt = new JButton("Land Me");
		landMeBt.setEnabled(false);
		landMeBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});		

		askMeBt = new JButton("Ask Me");
		askMeBt.setEnabled(false);
		askMeBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});	
		
		landAllBt = new JButton("Land All");
		landAllBt.setEnabled(false);
		landAllBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("land All");
			}
		});		
		result.setButtons(landMeBt, askMeBt, landAllBt);

		
		buttonLeftPanel.setVisible(false); 

		buttonLeftPanel.add(refreshBt);
		buttonLeftPanel.add(resetBt);
		buttonLeftPanel.add(googleBt);
		
		buttonRightPanel.add(askMeBt);
		buttonRightPanel.add(landMeBt);
		buttonRightPanel.add(landMeBt);
		
		buttonBt.setBounds(10, 20, 200, 23);
		distanceSpin.getSpinnerPanel().setBounds(10, 50, 260, 220);

		buttonLeftPanel.setBounds(40, 280, 200, 100);
		refreshBt.setBounds(10, 270, 200, 23);
		resetBt.setBounds(10, 300, 200, 23);
		googleBt.setBounds(10, 330, 200, 23);

		landMeBt.setBounds(295, 395, 80, 23);
		askMeBt.setBounds(395, 395, 80, 23);
		landAllBt.setBounds(500, 395, 80, 23);
		buttonRightPanel.setBounds(290, 380, 300, 23);
		
//		googleButton.setBounds(450, 290, 130, 23);


	  	panelResult.setBounds(290, 10, 300, 240);	
	  	outputPanel.setBounds(290, 250, 300, 140);	
	  	

     	panelFlightplan.add(buttonBt);
		panelFlightplan.add(panelResult);
		panelFlightplan.add(outputPanel);
		panelFlightplan.add(askMeBt);
		panelFlightplan.add(landMeBt);
		panelFlightplan.add(landAllBt);
		
		//panelFlightplan.add(buttonRightPanel);

		panelFlightplan.add(distanceSpin.getSpinnerPanel());
     	panelFlightplan.add(buttonLeftPanel);
  
		return panelFlightplan;					
	}

}
