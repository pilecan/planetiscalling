package com.panels;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.backend.CreateKML;
import com.backend.ReadData;
import com.cfg.common.DistanceSpinner;
import com.db.SelectAirport;
import com.db.SelectCity;
import com.db.SelectMountain;
import com.db.SelectNdb;
import com.db.SelectVor;
import com.db.UtilityDB;
import com.main.form.Result;
import com.model.Airport;
import com.model.Distance;
import com.model.WaitMessage;
import com.util.Utility;
import com.util.UtilityEarth;

public class PanelFlightplan {

	private ReadData readData;	
	private Result result;
	private JPanel panelFlightplan;
	private JPanel panelResult;
	
	private JButton googleBt;
	
	private DistanceSpinner distanceSpin; 
	
	private SelectCity selectCity;
	private SelectMountain selectMountain;
	private SelectVor selectVor;
	private SelectNdb selectNdb;
	
	private JPanel outputPanel;
	private JPanel askMePanel;
	private JPanel buttonLeftPanel;
	private JPanel buttonRightPanel;
	
	
	private String flightPlanFile;
	private JButton flightPlanBt;
	private JButton refreshBt;
	private JButton resetBt;
	private JButton landMeBt;
	private JButton askMeBt;
	
	private SelectAirport selectAirport;
	
	private JEditorPane jEditorPane;
	
	private JScrollPane askmeScrollPan;

	public PanelFlightplan() {
		super();
	}

	public JPanel getPanel(SelectCity selectCity,SelectMountain selectMountain, SelectVor selectVor, SelectNdb selectNdb) {
		this.selectCity = selectCity;
		this.selectMountain = selectMountain;
		this.selectVor = selectVor;
		this.selectNdb = selectNdb;
		this.flightPlanFile = "";
		this.selectAirport = new SelectAirport();

		return createPanel();
	}
	
	public JPanel createPanel() {
		distanceSpin = new DistanceSpinner();
		distanceSpin.initPanelDistances("plan");
		distanceSpin.getLandkmarkSpinner().setEnabled(false);
		
		jEditorPane = Utility.getInstance().initjEditorPane(jEditorPane);
		
    	askmeScrollPan = new JScrollPane(jEditorPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
     	   public void run() { 
     		   askmeScrollPan.getVerticalScrollBar().setValue(0);
     	   }
     	});
    	
        
		panelFlightplan = new JPanel();
		panelFlightplan.setLayout(null);
		
		buttonLeftPanel = new JPanel();
		buttonRightPanel = new JPanel();

		panelResult = new JPanel(new BorderLayout());
		panelResult.setBorder(new TitledBorder("Searh Result"));
		
		outputPanel = new JPanel(new BorderLayout());
		outputPanel.setBorder(new TitledBorder("List Result"));
	     Border border = outputPanel.getBorder();
		 Border margin = new EmptyBorder(5,5,5,5);
		 outputPanel.setBorder(new CompoundBorder(border, margin));

		
//		outputPanel = Utility.getInstance().setBorderPanel(outputPanel);
		
		askMePanel = new JPanel(new BorderLayout());
		askMePanel.setBorder(new TitledBorder(""));
			
		askMePanel.add(askmeScrollPan);

        result = new Result();
        result.setjEditorPane(jEditorPane);
        result.setAskmeScrollPan(askmeScrollPan);
		result.setAskMePanel(askMePanel);
		result.setOutputPanel(outputPanel);
		
		flightPlanBt = new JButton("Select Flightplan");
		
		flightPlanBt.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      
	      {
	          

	    	  readData =  new ReadData(result, selectCity, selectMountain, selectVor, selectNdb,
		        		 new Distance((int)distanceSpin.getCitySpinner().getValue(), 
		        				 (int)distanceSpin.getMountainSpinner().getValue(), 
		        				 (int)distanceSpin.getAirportSpinner().getValue(),
		        				 (int)distanceSpin.getVorNdbSpinner().getValue(), 
		        				 (int)distanceSpin.getLandkmarkSpinner().getValue(), 
		        				 distanceSpin.getCheckTocTod().isSelected(),
		        				 0.0));
    	  
	    	 if (result.getFlightplan() != null) {
		    	 flightPlanFile = new File(result.getFlightplan().getFlightplanFile()).getName();
			    	
		 		 panelResult.setBorder(new TitledBorder(flightPlanFile));
		 	//	 panelResult.setToolTipText((result.getFlightplan().getDescr()!= null?result.getFlightplan().getDescr():""));

		  		 panelResult.removeAll();	
				 panelResult.add(result.getFlightPlanFormPanel());
				 panelResult.validate();
				 
				 distanceSpin.getLandkmarkSpinner().setEnabled(!"".equals(UtilityDB.getInstance().getProvince()));
				 
			  	 result.getWaypointListModel();

				 panelFlightplan.add(panelResult);
				 panelFlightplan.validate();

				 resetBt.setEnabled(true);
				 googleBt.setEnabled(true);
				 refreshBt.setEnabled(true);

	    	 }
	      }
	    });
		
		
		refreshBt = new JButton("Update");
		refreshBt.setEnabled(false);

		refreshBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				//UtilityEarth.getInstance().terminate();

				readData.createFlightplan(new Distance((int) distanceSpin.getCitySpinner().getValue(),
						(int) distanceSpin.getMountainSpinner().getValue(),
						(int) distanceSpin.getAirportSpinner().getValue(),
						(int) distanceSpin.getVorNdbSpinner().getValue(),
						(int) distanceSpin.getLandkmarkSpinner().getValue(), distanceSpin.getCheckTocTod().isSelected(),
						(double) result.getAltitudeModel().getValue()));

				panelResult.removeAll();

				result.getWaypointListModel();

				distanceSpin.getLandkmarkSpinner().setEnabled(!"".equals(UtilityDB.getInstance().getProvince()));

				resetBt.setEnabled(true);

				panelResult.add(result.getFlightPlanFormPanel());
				panelResult.validate();
				panelFlightplan.validate();

			}
		});		
		
		
		
		resetBt = new JButton("Reset");
		resetBt.setEnabled(false);
		resetBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				distanceSpin.getCitySpinner().setValue(0);
				distanceSpin.getAirportSpinner().setValue(0);
				distanceSpin.getVorNdbSpinner().setValue(0);
				distanceSpin.getMountainSpinner().setValue(0);
				distanceSpin.getLandkmarkSpinner().setValue(0);
				distanceSpin.getCheckTocTod().setSelected(false);

				readData.resetFlightPlanResult();
				panelResult.removeAll();
				result.getFlightPlanFormPanel().validate();
				result.getWaypointListModel();

				panelResult.add(result.getFlightPlanFormPanel());
				
				resetBt.setEnabled(false);

				panelResult.validate();
				panelFlightplan.validate();
			}
		});		

		
		googleBt = new JButton("Land All on Google Earth");
		googleBt.setEnabled(false);
		googleBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//UtilityDB.getInstance().updateAirportState();
				
				readData.createFlightplan(new Distance((int) distanceSpin.getCitySpinner().getValue(),
						(int) distanceSpin.getMountainSpinner().getValue(),
						(int) distanceSpin.getAirportSpinner().getValue(),
						(int) distanceSpin.getVorNdbSpinner().getValue(), 
       				    (int)distanceSpin.getLandkmarkSpinner().getValue(), 
						distanceSpin.getCheckTocTod().isSelected(),
						(double) result.getAltitudeModel().getValue()));
				panelResult.removeAll();

				panelResult.add(result.getFlightPlanFormPanel());
				panelResult.validate();
				panelFlightplan.add(panelResult);
				panelFlightplan.validate();

				Utility.getInstance().launchGoogleEarth(new File(readData.getKmlFlightPlanFile()));

			}
		});
		
//////
		landMeBt = new JButton("Land Me");
		landMeBt.setEnabled(false);
		landMeBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String keyVor = Utility.getInstance().findKeyVor(result.getCurrentSelection());
				String keyICAO = Utility.getInstance().findKeyICAO(result.getCurrentSelection());
				String keyCityMountain = Utility.getInstance().findKeyCity(result.getCurrentSelection());
				
				if ("waypoint".equals(result.getCurrentView())){
					UtilityDB.getInstance().selectAirport("where ident = '"+keyICAO+"'");
					Airport airport = UtilityDB.getInstance().getAirport()	;
					if (airport.getIdent() != null) {
						CreateKML.makeOn(airport, result.getCurrentView());
					} else {
						CreateKML.makeOn(result, result.getCurrentView()+"-"+keyICAO);
					}
					
				}  else if ("airport".equals(result.getCurrentView())){
					selectAirport.select("where ident = '"+keyICAO+"'");
					CreateKML.makeOn(selectAirport.getAirport(), result.getCurrentView());
				}  else if ("vor".equals(result.getCurrentView())){
					CreateKML.makeOn(selectVor.getMapVors().get(keyVor), result.getCurrentView());
				 } else if ("ndb".equals(result.getCurrentView())){
					CreateKML.makeOn(selectNdb.getMapNdb().get(keyVor), result.getCurrentView());
				 }else if ("city".equals(result.getCurrentView())){
					CreateKML.makeOn(selectCity.getMapCities().get(keyCityMountain), result.getCurrentView());
				 }else if ("mountain".equals(result.getCurrentView())){
					CreateKML.makeOn(selectMountain.getMapMountains().get(keyCityMountain), result.getCurrentView());
				 }else if ("landmark".equals(result.getCurrentView())){
						CreateKML.makeOn(result.getSelectedLandmarks().get(keyICAO), result.getCurrentView());
				 }
				
				}
		});		

		askMeBt = new JButton("Ask Me");
		askMeBt.setEnabled(false);
			
		askMePanel.setVisible(false);

		result.setButtons(landMeBt, askMeBt);
		
		refreshBt.setBounds(30, 310, 125, 23);
		resetBt.setBounds(180, 310, 125, 23);
		googleBt.setBounds(90, 350, 180, 23);
	
		buttonRightPanel.add(askMeBt);
		buttonRightPanel.add(landMeBt);
		

		distanceSpin.getSpinnerPanel().setBounds(10, 10,  310, 220);
		flightPlanBt.setBounds(45, 260, 240, 23);

		buttonLeftPanel.setBounds(60, 280, 200, 100);
	
		buttonRightPanel.setBounds(290, 380, 300, 23);

		int x = 330;
		
	  	panelResult.setBounds(x, 10, 340, 270);	
		outputPanel.setBounds(x, 282, 340, 130);	
		askMePanel.setBounds(x, 10, 340, 270);	

	  	x += 0;
		landMeBt.setBounds(x, 419, 94, 23);
		askMeBt.setBounds(x+243, 420, 94, 23);
    	panelFlightplan.add(flightPlanBt);
		panelFlightplan.add(panelResult);
		panelFlightplan.add(outputPanel);
		panelFlightplan.add(askMePanel);
		
		panelFlightplan.add(refreshBt);
		panelFlightplan.add(resetBt);
		panelFlightplan.add(landMeBt);
		panelFlightplan.add(googleBt);
		panelFlightplan.add(askMeBt);
		panelFlightplan.add(landMeBt);


		UtilityEarth.getInstance().createEarth();
		
		UtilityEarth.getInstance().getPanelImage().setBounds(250, 77, 200, 200);
		panelFlightplan.add(UtilityEarth.getInstance().createEarth());

		
 
		
		
		//panelFlightplan.add(buttonRightPanel);

		panelFlightplan.add(distanceSpin.getSpinnerPanel());
     //	panelFlightplan.add(buttonLeftPanel);
  
		return panelFlightplan;					
	}
	
}


