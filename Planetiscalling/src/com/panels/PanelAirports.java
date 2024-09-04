package com.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;
import javax.swing.border.TitledBorder;

import com.backend.CreateKML;
import com.backend.ReadData;
import com.cfg.common.DistanceSpinner;
import com.cfg.common.Info;
import com.db.SelectAirport;
import com.db.SelectCity;
import com.db.SelectDB;
import com.db.SelectMountain;
import com.db.SelectNdb;
import com.db.SelectVor;
import com.db.UtilityDB;
import com.main.form.Result;
import com.model.Distance;
import com.util.Utility;
import com.util.UtilityEarthAnimation;

public class PanelAirports implements Info {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<String, Map<String,List<String>>> mapAll = new TreeMap<>();
	private Map<String, List<String>> mapState = new TreeMap<>();

	private JPanel panelLandMark;
	private JPanel panelCombo;
	private JPanel outputPanel;

	private JPanel panelResult;
	private Result result;

	private JPanel askMePanel;
	
	private SelectDB selectDB;
	public SelectAirport selectAiport;


	private boolean isAirportSetted;
	private JComboBox<String> comboCountryAirport;
	private JComboBox<String> comboStateAirport;
	private JComboBox<String> comboCityAirport;
	

	private ReadData readData;
	private JDialog dialog;
	

	private JButton landMeBt;
	private JButton askMeBt;
	private JButton googleBt;
	private JButton resetBt;
	private JButton searchBt;

	private JEditorPane jEditorPane;
	
	private JScrollPane askmeScrollPan;
	
	String [] provArray;
	String [] geoTermArray; 
	String [] geoNameArray;
	String [] countryArray; 
	String [] adminArray;
	String [] cityArray;
	
	public PanelAirports() {
		
	}
	
	
	public void setPanelAirport() {
		selectDB = new SelectDB();
		selectDB.selectAirportTableNew();
	
		for (String str:selectDB.getCountry()) {
			comboCountryAirport.addItem(str);
		}
		comboCountryAirport.setSelectedIndex(0);
		
		for (String str:selectDB.getStates(comboCountryAirport.getSelectedItem())) {
			comboStateAirport.addItem(str);
		}

		comboCityAirport.addItem(" All");

        selectDB.setComboCity("v_airport_runway",comboCityAirport, comboCountryAirport.getSelectedItem(), comboStateAirport.getSelectedItem(), comboStateAirport.getItemCount());
        
		isAirportSetted = true;
		
	}
	
	public JPanel getAirportPanel() {
		final DistanceSpinner distanceSpin = new DistanceSpinner();
		distanceSpin.initPanelDistances("airport");
		distanceSpin.getLandkmarkSpinner().setEnabled(false);

		readData = new ReadData();
		
		jEditorPane = Utility.getInstance().initjEditorPane(jEditorPane);

    	askmeScrollPan = new JScrollPane(jEditorPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
      	   public void run() { 
      		   askmeScrollPan.getVerticalScrollBar().setValue(0);
      	   }
      	});
    	
        
		askMePanel = new JPanel(new BorderLayout());
		askMePanel.setBorder(new TitledBorder("Result"));
        askMePanel.add(askmeScrollPan);

    	
    	outputPanel = new JPanel(new BorderLayout());
		outputPanel.setBorder(new TitledBorder("List Result"));

    	result = new Result();
	    result.setjEditorPane(jEditorPane);
	    result.setAskmeScrollPan(askmeScrollPan);
		result.setAskMePanel(askMePanel);
		result.setOutputPanel(outputPanel);
		
		panelResult = new JPanel(new BorderLayout());
		panelResult.setBorder(new TitledBorder("Searh Result"));

		panelLandMark = new JPanel();
		panelCombo = new  JPanel();
		panelLandMark.setLayout(null);

		comboCountryAirport = new JComboBox<>();
		comboStateAirport = new JComboBox<>();
		comboCityAirport = new JComboBox<>();

		comboCountryAirport.setPreferredSize(new Dimension(240,25));
        comboStateAirport.setPreferredSize(new Dimension(240,25));
        comboCityAirport.setPreferredSize(new Dimension(240,25));
        
		comboCountryAirport.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				comboStateAirport.removeAllItems();
				for (String state : selectDB.getStates(comboCountryAirport.getSelectedItem())) {
					comboStateAirport.addItem(state);
				}

				int totalCity = 0;
				if (comboStateAirport.getSelectedItem() != null && comboStateAirport.getItemCount() ==1 ) {
			        totalCity = selectDB.setComboCity("v_airport_runway",comboCityAirport, comboCountryAirport.getSelectedItem(), comboStateAirport.getSelectedItem(), comboStateAirport.getItemCount());
				}
				
				if ("Canada".equals(comboCountryAirport.getSelectedItem())) {
					distanceSpin.getLandkmarkSpinner().setEnabled(true);
				} else {
					distanceSpin.getLandkmarkSpinner().setValue(0);
					distanceSpin.getLandkmarkSpinner().setEnabled(false);
				}
				
				//setResult(labelHeader,totalCity);
			}
	
		});
		comboStateAirport.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				comboCityAirport.removeAllItems();
				comboCityAirport.addItem(" All");

		        int totalCity = 0;
				if (comboStateAirport.getSelectedItem() != null && !" All".equals(comboStateAirport.getSelectedItem() )) {
					totalCity =  selectDB.setComboCity("v_airport_runway",comboCityAirport, comboCountryAirport.getSelectedItem(), comboStateAirport.getSelectedItem(), comboStateAirport.getMaximumRowCount());
				}
				
				//setResult(labelHeader,totalCity);
				
			}

		});

   	    panelCombo.add(comboCountryAirport);
		panelCombo.add(comboStateAirport);
		panelCombo.add(comboCityAirport);

		searchBt = new JButton("Search");
		
		dialog = UtilityEarthAnimation.getInstance().panelWait();

		searchBt.addActionListener(new ActionListener()
		    {
		      public void actionPerformed(ActionEvent e)
		      {
		    	  SwingWorker<Void,Void> worker = new SwingWorker<Void,Void>()
		  		{
		  		    protected Void doInBackground()
		  		    {

		    	  
		    	  readData.createKMLAirport(result, selectDB.getMapCities(), comboCountryAirport,comboStateAirport,comboCityAirport,
		        		 new Distance((int)distanceSpin.getCitySpinner().getValue(), 
		        				 (int)distanceSpin.getMountainSpinner().getValue(), 
		        				 0, 
		        				 (int)distanceSpin.getVorNdbSpinner().getValue(), 
		        				    (int)distanceSpin.getLandkmarkSpinner().getValue(), 
		        				 distanceSpin.getCheckLinedist().isSelected(),
		        				 0.0));
		         
	  		        return null;
	  		    }
	  		 
	  		    @Override
	  		    protected void done()
	  		    {
	  		        dialog.dispose();
	  		    }
	  		};
	  		worker.execute();
	  		dialog.setVisible(true); // will block but with a responsive GUI		         
		         searchBt.setText("Update");
		         panelResult.removeAll();	
				 panelResult.add(result.getAiportFormPanel());
				 panelResult.validate();
				 googleBt.setEnabled(true);
				 resetBt.setEnabled(true);

				 
			  	result.getAirportListModel();
			  	panelLandMark.validate();
		      }
		    });

		resetBt = new JButton("Reset");
		resetBt.setEnabled(false);
		
		resetBt.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
				distanceSpin.getCitySpinner().setValue(0);
				distanceSpin.getVorNdbSpinner().setValue(0);
				distanceSpin.getMountainSpinner().setValue(0);
				distanceSpin.getLandkmarkSpinner().setValue(0);
		     	distanceSpin.getCheckLinedist().setSelected(false);

				readData.resetAirportResult();
				panelResult.removeAll();
				result.getAiportFormPanel();
				result.getAirportListModel();
				result.resetButton();
				
				googleBt.setEnabled(false);
				panelResult.add(result.getAiportFormPanel());

				panelResult.validate();
				panelLandMark.validate();
	      }
	    });
		
		googleBt = new JButton("Land All on Google Earth");
		googleBt.setEnabled(false);
		googleBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	
		    	  SwingWorker<Void,Void> worker = new SwingWorker<Void,Void>()
		  		{
		  		    protected Void doInBackground()
		  		    {

		    	  
		         readData.createKMLAirport(result, selectDB.getMapCities(), comboCountryAirport,comboStateAirport,comboCityAirport,
		        		 new Distance((int)distanceSpin.getCitySpinner().getValue(), 
		        				 (int)distanceSpin.getMountainSpinner().getValue(), 
		        				 0, 
		        				 (int)distanceSpin.getVorNdbSpinner().getValue(), 
		        				 (int)distanceSpin.getLandkmarkSpinner().getValue(), 
	        				 distanceSpin.getCheckLinedist().isSelected(),
		        				 0.0));
		         
		         
		         
	  		        return null;
	  		    }
	  		 
	  		    @Override
	  		    protected void done()
	  		    {
	  		        dialog.dispose();
	  		    }
	  		};
	  		worker.execute();
	  		dialog.setVisible(true); // will block but with a responsive GUI		         

				
		         panelResult.removeAll();	
				 panelResult.add(result.getAiportFormPanel());
				 panelResult.validate();
				 
			  	result.getAirportListModel();
			  	panelLandMark.validate();

				Utility.getInstance().launchGoogleEarth(new File(Utility.getInstance().getFlightPlanName(Info.kmlAirportCityName)));

			}
		});
		
		
		
		landMeBt = new JButton("Land Me");
		landMeBt.setEnabled(false);
		landMeBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				result.landMe();
			}
		});		

			

	   return setLandmarkPanel(distanceSpin);
	}
	
	private JPanel setLandmarkPanel(DistanceSpinner distanceSpin) {

		askMeBt = new JButton("Ask Me");
		askMeBt.setEnabled(false);

		askMePanel.setVisible(false);
		
	    result.setButtons(landMeBt, askMeBt);

		
		distanceSpin.getSpinnerPanel().setBounds(10, 10, 310, 190);
		panelCombo.setBounds(10, 210, 310, 120);
		
		int x = 330;
		
	  	panelResult.setBounds(x, 10, 340, 190);	
	  	outputPanel.setBounds(x, 210, 340, 150);	
	  	askMePanel.setBounds(x, 10, 340, 190);	

	  	x += 0;
		landMeBt.setBounds(x, 370, 94, 23);
		askMeBt.setBounds(x+244, 370, 94, 23);

		searchBt.setBounds(30, 350, 125, 23);
		resetBt.setBounds(180, 350, 125, 23);
		googleBt.setBounds(90, 390, 180, 23);

		
		panelLandMark.add(panelCombo);
		panelLandMark.add(distanceSpin.getSpinnerPanel());
		panelLandMark.add(searchBt);
		panelLandMark.add(resetBt);
		panelLandMark.add(panelResult);
		panelLandMark.add(outputPanel);
		panelLandMark.add(askMePanel);
		panelLandMark.add(landMeBt);
		panelLandMark.add(askMeBt);
  	    panelLandMark.add(googleBt);
		
		return panelLandMark;
		
	}
	

	public boolean isAirportSetted() {
		return isAirportSetted;
	}


}
