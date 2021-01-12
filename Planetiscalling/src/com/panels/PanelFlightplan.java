package com.panels;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.cfg.common.DistanceSpinner;
import com.model.Distance;
import com.model.Result;
import com.model.TimeZones;
import com.util.CreateKML;
import com.util.ReadData;
import com.util.Utility;

import net.SelectAirport;
import net.SelectCity;
import net.SelectMountain;
import net.SelectNdb;
import net.SelectVor;

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
	private TimeZones timeZones;

	
	private JEditorPane jEditorPane;
	
	JScrollPane askmeScrollPan;

	public PanelFlightplan() {
		super();
	}

	public JPanel getPanel(TimeZones timeZones, SelectCity selectCity,SelectMountain selectMountain, SelectVor selectVor, SelectNdb selectNdb) {
		this.selectCity = selectCity;
		this.selectMountain = selectMountain;
		this.selectVor = selectVor;
		this.selectNdb = selectNdb;
		this.flightPlanFile = "";
		this.timeZones = timeZones;
		this.selectAirport = new SelectAirport();

		return createPanel();
	}
	
	public JPanel createPanel() {
		
		distanceSpin = new DistanceSpinner();
		distanceSpin.initPanelDistances("plan");
		
		jEditorPane = Utility.getInstance().initjEditorPane();

		

    	askmeScrollPan = new JScrollPane(jEditorPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
        result = new Result();
		
		
		panelFlightplan = new JPanel();
		panelFlightplan.setLayout(null);
		
		buttonLeftPanel = new JPanel();
		buttonRightPanel = new JPanel();

		panelResult = new JPanel(new BorderLayout());
		panelResult.setBorder(new TitledBorder("Searh Result"));
		
		outputPanel = new JPanel(new BorderLayout());
		outputPanel.setBorder(new TitledBorder(""));
	     Border border = outputPanel.getBorder();
		 Border margin = new EmptyBorder(5,5,5,5);
		 outputPanel.setBorder(new CompoundBorder(border, margin));

		
//		outputPanel = Utility.getInstance().setBorderPanel(outputPanel);
		
		askMePanel = new JPanel(new BorderLayout());
		askMePanel.setBorder(new TitledBorder(""));
			
		askMePanel.add(askmeScrollPan);

		result.setOutputPanel(outputPanel);
		
		flightPlanBt = Utility.getInstance().setButton("Select Flightplan");
		
		flightPlanBt.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  readData =  new ReadData(result, selectCity, selectMountain, selectVor, selectNdb,
		        		 new Distance((int)distanceSpin.getCitySpinner().getValue(), 
		        				 (int)distanceSpin.getMountainSpinner().getValue(), 
		        				 (int)distanceSpin.getAirportSpinner().getValue(),
		        				 (int)distanceSpin.getVorNdbSpinner().getValue(), 
		        				 distanceSpin.getCheckTocTod().isSelected(),
		        				 0.0));
    	  
	    	 if (result.getFlightplan() != null) {
		    	 flightPlanFile = new File(result.getFlightplan().getFlightplanFile()).getName();
			    	
		 		 panelResult.setBorder(new TitledBorder(flightPlanFile));
		 		 panelResult.setToolTipText((result.getFlightplan().getDescr()!= null?result.getFlightplan().getDescr():""));

		  		 panelResult.removeAll();	
				 panelResult.add(result.getFlightPlanFormPanel());
				 panelResult.validate();
				 
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
				readData.createFlightplan(new Distance((int) distanceSpin.getCitySpinner().getValue(),
						(int) distanceSpin.getMountainSpinner().getValue(),
						(int) distanceSpin.getAirportSpinner().getValue(),
						(int) distanceSpin.getVorNdbSpinner().getValue(), distanceSpin.getCheckTocTod().isSelected(),
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
				System.out.println(result.getCurrentView());
				System.out.println(result.getCurrentSelection());
				
				String keyVor = Utility.getInstance().findKeyVor(result.getCurrentSelection());
				String keyICAO = Utility.getInstance().findKeyICAO(result.getCurrentSelection());
				String keyCityMountain = Utility.getInstance().findKeyCity(result.getCurrentSelection());

				if ("airport".equals(result.getCurrentView())){
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
				 }
				
				
		        Utility.getInstance().launchGoogleEarth(new File(Utility.getInstance().getFlightPlanName(result.getCurrentView()+".kml")));


			}
		});		

		askMeBt = new JButton("Ask Me");
		askMeBt.setEnabled(false);
		askMeBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				result.showAskMeAnswer(outputPanel, jEditorPane, askMeBt, askmeScrollPan);
			}
		});	
			
		
		
		
		
		result.setButtons(landMeBt, askMeBt);

		buttonLeftPanel.add(refreshBt);
		buttonLeftPanel.add(resetBt);
		buttonLeftPanel.add(googleBt);
		
		buttonRightPanel.add(askMeBt);
		buttonRightPanel.add(landMeBt);
		

		flightPlanBt.setBounds(50, 20, 200, 23);
		distanceSpin.getSpinnerPanel().setBounds(10, 60,  310, 190);

		buttonLeftPanel.setBounds(50, 260, 200, 100);
		
/*		refreshBt.setBounds(10, 270, 200, 23);
		resetBt.setBounds(10, 300, 200, 23);
		googleBt.setBounds(10, 330, 200, 23);
*/		
		buttonRightPanel.setBounds(290, 380, 300, 23);

		int x = 330;
		
	  	panelResult.setBounds(x, 10, 340, 240);	
	  	outputPanel.setBounds(x, 260, 340, 130);	
	  	askMePanel.setBounds(x, 260, 340, 130);	

	  	x += 40;
		landMeBt.setBounds(x, 400, 94, 23);
		askMeBt.setBounds(x+120, 400, 94, 23);
	  	

     	panelFlightplan.add(flightPlanBt);
		panelFlightplan.add(panelResult);
		panelFlightplan.add(outputPanel);
		panelFlightplan.add(askMePanel);
		
		panelFlightplan.add(askMeBt);
		panelFlightplan.add(landMeBt);
		
		//panelFlightplan.add(buttonRightPanel);

		panelFlightplan.add(distanceSpin.getSpinnerPanel());
     	panelFlightplan.add(buttonLeftPanel);
  
		return panelFlightplan;					
	}
	

		
	}



