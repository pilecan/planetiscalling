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

public class PanelMountains implements Info {

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

	private boolean isLandmarkSetted;
	private JComboBox<String> comboCountryLandmark;
	private JComboBox<String> comboStateLandmark;
	private JComboBox<String> comboGeoterm;
	private JComboBox<String> comboGeoname;

	private boolean isAirportSetted;
	private JComboBox<String> comboCountryAirport;
	private JComboBox<String> comboStateAirport;
	private JComboBox<String> comboCityAirport;
	
	private JComboBox<String> comboCountry;
	private JComboBox<String> comboState;
	private JComboBox<String> comboCity;
	private JComboBox<String> comboMountain;

	private boolean isCitySetted;

	private JComboBox<String> comboCountryCity;
	private JComboBox<String> comboStateCity;
	private JComboBox<String> comboCityCity;

	private ReadData readData;
	private JDialog dialog;
	

	private JButton landMeBt;
	private JButton askMeBt;
	private JButton googleBt;
	private JButton resetBt;
	private JButton searchBt;

	private JEditorPane jEditorPane;
	
	private JScrollPane askmeScrollPan;
	private SelectAirport selectAirport;
	
	String [] provArray;
	String [] geoTermArray; 
	String [] geoNameArray;
	String [] countryArray; 
	String [] adminArray;
	String [] cityArray;
	
	public PanelMountains() {
		
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
	
	
	
	 /**
	  * getMountainWorld
	  * 
	  * 
	  * @param manageXMLFile
	  * @return
	  */
	public JPanel getMountainPanel() {
		
		final DistanceSpinner distanceSpin = new DistanceSpinner();
		distanceSpin.initPanelDistances("mountain");
		
		selectAirport = new SelectAirport();
		readData = new ReadData();

		jEditorPane = Utility.getInstance().initjEditorPane(jEditorPane);
	   	askmeScrollPan = new JScrollPane(jEditorPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
	        javax.swing.SwingUtilities.invokeLater(new Runnable() {
	      	   public void run() { 
	      		   askmeScrollPan.getVerticalScrollBar().setValue(0);
	      	   }
	     });
	        
		panelResult = new JPanel(new BorderLayout());
		panelResult.setBorder(new TitledBorder("Searh Result"));


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
		
		panelLandMark = new JPanel();
		panelCombo = new  JPanel();
		panelLandMark.setLayout(null);

		selectDB = new SelectDB();
		selectDB.selectAirportTableNew();
		
		final SelectDB selectDB = new SelectDB();
		selectDB.selectMountainTable();
		
		comboCountry = new JComboBox<>(selectDB.getCountryMountain());
		comboMountain = new JComboBox<>(selectDB.getMountain("Afghanistan"));
		comboMountain.addItem(" All");
		
		comboCountry.setPreferredSize(new Dimension(240,25));
		comboMountain.setPreferredSize(new Dimension(240,25));

        
		comboCountry.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				comboMountain.removeAllItems();
				//comboMountain.addItem(" All");

				for (String state : selectDB.getMountain((String) comboCountry.getSelectedItem())) {
					comboMountain.addItem(state);
				}

			}
	
		});
   	    panelCombo.add(comboCountry);
		panelCombo.add(comboMountain);

		searchBt = new JButton("Search");
		searchBt.addActionListener(new ActionListener()
		    {
		      public void actionPerformed(ActionEvent e)
		      {
		          new ReadData().createKMLMountain(result, comboCountry,comboMountain,
			        		 new Distance((int)distanceSpin.getCitySpinner().getValue(), 
			        				 0, 
			        				 (int)distanceSpin.getAirportSpinner().getValue(), 
			        				 (int)distanceSpin.getVorNdbSpinner().getValue(), 
			        				 0, 
			        				 distanceSpin.getCheckLinedist().isSelected(),
			        				 0.0)
			        		 
		        		  
		        		  );
		             searchBt.setText("Update");
			         panelResult.removeAll();	
					 panelResult.add(result.getMountainFormPanel());
					 panelResult.validate();
					 
					 googleBt.setEnabled(true);
					 resetBt.setEnabled(true);
					 
				  	result.getMountainListModel();
				  	panelLandMark.validate();
		      }
		    });

		resetBt = new JButton("Reset");
		resetBt.setEnabled(false);
		
		resetBt.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
				distanceSpin.getAirportSpinner().setValue(0);
				distanceSpin.getVorNdbSpinner().setValue(0);
				distanceSpin.getMountainSpinner().setValue(0);
				distanceSpin.getLandkmarkSpinner().setValue(0);
				distanceSpin.getCheckLinedist().setSelected(false);

				readData.resetCityResult(result);
				panelResult.removeAll();
				result.getCityFormPanel();
				result.getCityListModel();
				result.resetButton();

				
				googleBt.setEnabled(false);
				resetBt.setEnabled(false);

				panelResult.add(result.getCityFormPanel());

				panelResult.validate();
				panelLandMark.validate();
	      }
	    });
		
		googleBt = new JButton("Land All on Google Earth");
		googleBt.setEnabled(false);
		googleBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		          new ReadData().createKMLMountain(result, comboCountry,comboMountain, 
			        		 new Distance((int)distanceSpin.getCitySpinner().getValue(), 
			        				 0, 
			        				 (int)distanceSpin.getAirportSpinner().getValue(), 
			        				 (int)distanceSpin.getVorNdbSpinner().getValue(), 
			        				 0, 
			        				 distanceSpin.getCheckLinedist().isSelected(),
			        				 0.0)
			        		 
		        		  
		        		  );
		             searchBt.setText("Update");
			         panelResult.removeAll();	
					 panelResult.add(result.getMountainFormPanel());
					 panelResult.validate();
					 
					 googleBt.setEnabled(true);
					 resetBt.setEnabled(true);
					 
				  	result.getMountainListModel();
				  	panelLandMark.validate();

				Utility.getInstance().launchGoogleEarth(new File(Utility.getInstance().getFlightPlanName(Info.kmlMountainCityAirportName)));

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

	public boolean isLandmarkSetted() {
		return isLandmarkSetted;
	}

	public boolean isAirportSetted() {
		return isAirportSetted;
	}

	public boolean isCitySetted() {
		return isCitySetted;
	}
	

}
