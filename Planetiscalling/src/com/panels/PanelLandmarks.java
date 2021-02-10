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
import com.util.UtilityEarth;

public class PanelLandmarks implements Info {

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
	
	private JComboBox<String> comboCountry;
	private JComboBox<String> comboState;
	private JComboBox<String> comboGeoterm;
	private JComboBox<String> comboGeoname;
	private JComboBox<String> comboCity;
	private JComboBox<String> comboMountain;

	private ReadData readData;
	

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

	
	public PanelLandmarks() {
		
	}
	
	public void setPanelLandmark() {
		provArray = UtilityDB.getInstance().getProvinces(); 
		geoTermArray = UtilityDB.getInstance().getGeoterm(provArray[0]); 
		geoNameArray = UtilityDB.getInstance().getGeoname(provArray[0],geoTermArray[0]); 

		comboCountry = new JComboBox<>(UtilityDB.getInstance().getCountryCgn());
		comboState = new JComboBox<>(provArray);
		comboGeoterm  = new JComboBox<>(geoTermArray);
		comboGeoname = new JComboBox<>(geoNameArray);
		
	}

	public JPanel getLandMarkPanel() {
		final DistanceSpinner distanceSpin = new DistanceSpinner();
		distanceSpin.initPanelDistances("landmark");

		this.selectAirport = new SelectAirport();
		
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


		selectDB = new SelectDB();
		selectDB.selectAirportTableNew();

		provArray = UtilityDB.getInstance().getProvinces(); 
		geoTermArray = UtilityDB.getInstance().getGeoterm(provArray[0]); 
		geoNameArray = UtilityDB.getInstance().getGeoname(provArray[0],geoTermArray[0]); 

		comboCountry = new JComboBox<>(UtilityDB.getInstance().getCountryCgn());
		comboState = new JComboBox<>(provArray);
		comboGeoterm  = new JComboBox<>(geoTermArray);
		comboGeoname = new JComboBox<>(geoNameArray);

     //   selectDB.setComboCity("airport",comboCity, comboCountry.getSelectedItem(), comboState.getSelectedItem(), comboState.getItemCount());

        int widht = 240;
		comboCountry.setPreferredSize(new Dimension(widht,25));
        comboState.setPreferredSize(new Dimension(widht,25));
        comboGeoterm.setPreferredSize(new Dimension(widht,25));
        comboGeoname.setPreferredSize(new Dimension(widht,25));
        
        
		comboState.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				comboGeoterm.removeAllItems();
				comboGeoname.removeAllItems();

				int totalCity = 0;
				if (comboState.getSelectedItem() != null) {
					geoTermArray = UtilityDB.getInstance().getGeoterm((String) comboState.getSelectedItem());
					geoNameArray = UtilityDB.getInstance().getGeoname((String) comboState.getSelectedItem(),
							geoTermArray[0]);

					for (String string : geoTermArray) {
						comboGeoterm.addItem(string);
					}
					for (String string : geoNameArray) {
						comboGeoname.addItem(string);
					}

				}

			}

		});
		
		comboGeoterm.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				comboGeoname.removeAllItems();

				int totalCity = 0;
				if (comboGeoterm.getSelectedItem() != null) {
					geoNameArray = UtilityDB.getInstance().getGeoname((String) comboState.getSelectedItem(),
							(String) comboGeoterm.getSelectedItem());

					for (String string : geoNameArray) {
						comboGeoname.addItem(string);
					}

				}

			}

		});

		comboGeoname.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
			}

		});
		
   	    panelCombo.add(comboCountry);
		panelCombo.add(comboState);
		panelCombo.add(comboGeoterm);
		panelCombo.add(comboGeoname);

		searchBt = new JButton("Search");
		searchBt.addActionListener(new ActionListener()
		    {
		      public void actionPerformed(ActionEvent e)
		      {
		    	  
		    	        readData.createKMLLandmark(result, comboCountry,comboState,comboGeoterm,comboGeoname,
		        		 new Distance((int)distanceSpin.getCitySpinner().getValue(), 
		        				 (int)distanceSpin.getMountainSpinner().getValue(), 
		        				 (int)distanceSpin.getAirportSpinner().getValue(), 
		        				 0, 
		        				 0,
		        				 distanceSpin.getCheckLinedist().isSelected(),
		        				 0.0));
		         
		         searchBt.setText("Update");
		         panelResult.removeAll();	
				 panelResult.add(result.getLandmarkFormPanel());
				 panelResult.validate();
				 
				 googleBt.setEnabled(true);
				 resetBt.setEnabled(true);

				 
			  	result.getLandmarkListModel();
			  	panelLandMark.validate();

	         
		      }
		    });

		resetBt = new JButton("Reset");
		resetBt.setEnabled(false);
		
		resetBt.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	  	//	setPanelLandmark();

			distanceSpin.getCitySpinner().setValue(0);
			distanceSpin.getAirportSpinner().setValue(0);
			distanceSpin.getMountainSpinner().setValue(0);
			distanceSpin.getCheckLinedist().setSelected(false);

			readData.resetLandmarkResult();
			panelResult.removeAll();
			result.getLandmarkFormPanel();
			result.getLandmarkListModel();
			
			googleBt.setEnabled(false);
			panelResult.add(result.getLandmarkFormPanel());

			panelResult.validate();
			panelLandMark.validate();
	      }
	    });
		
		googleBt = new JButton("Land All on Google Earth");
		googleBt.setEnabled(false);
		googleBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
    	        readData.createKMLLandmark(result, comboCountry,comboState,comboGeoterm,comboGeoname,
        		 new Distance((int)distanceSpin.getCitySpinner().getValue(), 
        				 (int)distanceSpin.getMountainSpinner().getValue(), 
        				 (int)distanceSpin.getAirportSpinner().getValue(), 
        				 0, 
        				 0, 
        				 distanceSpin.getCheckLinedist().isSelected(),
        				 0.0));
         
         searchBt.setText("Update");
         panelResult.removeAll();	
		 panelResult.add(result.getLandmarkFormPanel());
		 panelResult.validate();
		 distanceSpin.getLandkmarkSpinner().setEnabled(!"".equals(UtilityDB.getInstance().getProvince()));

		 
		 googleBt.setEnabled(true);
		 resetBt.setEnabled(true);

		 
	  	result.getLandmarkListModel();
	  	panelLandMark.validate();

				Utility.getInstance().launchGoogleEarth(new File(Utility.getInstance().getFlightPlanName(Info.kmlLandmarkMountainCityAirportName)));

			}
		});
		
		
		
		landMeBt = new JButton("Land Me");
		landMeBt.setEnabled(false);
		landMeBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String keyVor = Utility.getInstance().findKeyVor(result.getCurrentSelection());
				String keyICAO = Utility.getInstance().findKeyICAO(result.getCurrentSelection());
				String keyCityMountain = Utility.getInstance().findKeyCity(result.getCurrentSelection());
				
				if ("airport".equals(result.getCurrentView())){
					selectAirport.select("where ident = '"+keyICAO+"'");
					CreateKML.makeOn(selectAirport.getAirport(), result.getCurrentView());
				 }else if ("city".equals(result.getCurrentView())){
						CreateKML.makeOn(UtilityDB.getInstance().getMapCities().get(keyCityMountain), result.getCurrentView());
				 }else if ("mountain".equals(result.getCurrentView())){
						CreateKML.makeOn(UtilityDB.getInstance().getMapMountains().get(keyCityMountain), result.getCurrentView());
				 } else if ("landmark".equals(result.getCurrentView())){
						CreateKML.makeOn(UtilityDB.getInstance().getMapLandmark().get(keyICAO), result.getCurrentView());
				 }

			}
		});		

			

	   return setLandmarkPanel(distanceSpin);
	}
	
	public JPanel getAirportPanel(JFrame  parent, final SelectCity selectCity,final SelectMountain selectMountain, final SelectVor selectVor, final SelectNdb selectNdb) {
		final DistanceSpinner distanceSpin = new DistanceSpinner();
		distanceSpin.initPanelDistances("airport");
		distanceSpin.getLandkmarkSpinner().setEnabled(false);
		
		final JDialog dialog = new JDialog(parent, true); // modal
		dialog.setUndecorated(true);
		JProgressBar bar = new JProgressBar();
		bar.setIndeterminate(true);
		bar.setStringPainted(true);
		bar.setString("Scanning the Planet...");
		dialog.add(bar);
		dialog.pack();
		   dialog.setLocationRelativeTo(parent);



		this.selectAirport = new SelectAirport();
		
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


		selectDB = new SelectDB();
		selectDB.selectAirportTableNew();

		comboCountry = new JComboBox<>(selectDB.getCountry());
		comboState = new JComboBox<>(selectDB.getStates(comboCountry.getSelectedItem()));
		comboCity = new JComboBox<>();
		comboCity.addItem(" All");

        selectDB.setComboCity("v_airport_runway",comboCity, comboCountry.getSelectedItem(), comboState.getSelectedItem(), comboState.getItemCount());

		comboCountry.setPreferredSize(new Dimension(240,25));
        comboState.setPreferredSize(new Dimension(240,25));
        comboCity.setPreferredSize(new Dimension(240,25));
        
        
		comboCountry.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				comboState.removeAllItems();
				for (String state : selectDB.getStates(comboCountry.getSelectedItem())) {
					comboState.addItem(state);
				}

				int totalCity = 0;
				if (comboState.getSelectedItem() != null && comboState.getItemCount() ==1 ) {
			        totalCity = selectDB.setComboCity("v_airport_runway",comboCity, comboCountry.getSelectedItem(), comboState.getSelectedItem(), comboState.getItemCount());
				}
				
				//setResult(labelHeader,totalCity);
			}
	
		});
		comboState.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				comboCity.removeAllItems();
				comboCity.addItem(" All");

		        int totalCity = 0;
				if (comboState.getSelectedItem() != null && !" All".equals(comboState.getSelectedItem() )) {
					totalCity =  selectDB.setComboCity("v_airport_runway",comboCity, comboCountry.getSelectedItem(), comboState.getSelectedItem(), comboState.getMaximumRowCount());
				}
				
				//setResult(labelHeader,totalCity);
				
			}

		});

   	    panelCombo.add(comboCountry);
		panelCombo.add(comboState);
		panelCombo.add(comboCity);

		searchBt = new JButton("Search");
		searchBt.addActionListener(new ActionListener()
		    {
		      public void actionPerformed(ActionEvent e)
		      {
		    	//  dialog.setVisible(true);
		    	  SwingWorker<Void,Void> worker = new SwingWorker<Void,Void>()
		  		{
		  		    protected Void doInBackground()
		  		    {

		    	  
		    	  readData.createKMLAirport(result, selectDB.getMapCities(), comboCountry,comboState,comboCity,
		        		 selectMountain, selectVor,selectNdb,
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
	  		dialog.setVisible(true); // will block but with a responsive GUI		         readData.createKMLAirport(result, selectDB.getMapCities(), comboCountry,comboState,comboCity,
		         searchBt.setText("Update");
		         panelResult.removeAll();	
				 panelResult.add(result.getAiportFormPanel());
				 panelResult.validate();
				 distanceSpin.getLandkmarkSpinner().setEnabled(!"".equals(UtilityDB.getInstance().getProvince()));

				 googleBt.setEnabled(true);
				 resetBt.setEnabled(true);

				 
			  	result.getAirportListModel();
			  	panelLandMark.validate();
			  	
		    	  UtilityEarth.getInstance().terminate();


	         
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
		         readData.createKMLAirport(result, selectDB.getMapCities(), comboCountry,comboState,comboCity,
		        		 selectMountain, selectVor,selectNdb,
		        		 new Distance((int)distanceSpin.getCitySpinner().getValue(), 
		        				 (int)distanceSpin.getMountainSpinner().getValue(), 
		        				 0, 
		        				 (int)distanceSpin.getVorNdbSpinner().getValue(), 
		        				 (int)distanceSpin.getLandkmarkSpinner().getValue(), 
	        				 distanceSpin.getCheckLinedist().isSelected(),
		        				 0.0));
		         panelResult.removeAll();	
				 panelResult.add(result.getAiportFormPanel());
				 panelResult.validate();
				 
				 distanceSpin.getLandkmarkSpinner().setEnabled(!"".equals(UtilityDB.getInstance().getProvince()));
				 
			  	result.getAirportListModel();
			  	panelLandMark.validate();

				Utility.getInstance().launchGoogleEarth(new File(Utility.getInstance().getFlightPlanName(Info.kmlAirportCityName)));

			}
		});
		
		
		
		landMeBt = new JButton("Land Me");
		landMeBt.setEnabled(false);
		landMeBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
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
				 }else if ("landmark".equals(result.getCurrentView())){
						CreateKML.makeOn(result.getSelectedLandmarks().get(keyICAO), result.getCurrentView());
				 }

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
	
	/**
	 * 
	 * getCityWorld
	 * 
	 * 
	 * @param manageXMLFile
	 * @return
	 */
	public JPanel getCityPanel(final SelectCity selectCity ,final SelectMountain selectMountain, final SelectVor selectVor, final SelectNdb selectNdb) {
		final DistanceSpinner distanceSpin = new DistanceSpinner();
		distanceSpin.initPanelDistances("city");
		distanceSpin.getLandkmarkSpinner().setEnabled(false);
		
		selectAirport = new SelectAirport();
		
		readData = new ReadData();

		jEditorPane = Utility.getInstance().initjEditorPane(jEditorPane);
		
		panelResult = new JPanel(new BorderLayout());
		panelResult.setBorder(new TitledBorder("Searh Result"));


		askMePanel = new JPanel(new BorderLayout());
		askMePanel.setBorder(new TitledBorder("Result"));

    	askmeScrollPan = new JScrollPane(jEditorPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
      	   public void run() { 
      		   askmeScrollPan.getVerticalScrollBar().setValue(0);
      	   }
      	});
    	askMePanel.add(askmeScrollPan);

		outputPanel = new JPanel(new BorderLayout());
		outputPanel.setBorder(new TitledBorder("List Result"));

		result = new Result();
	    result.setjEditorPane(jEditorPane);
	    result.setAskmeScrollPan(askmeScrollPan);
		result.setAskMePanel(askMePanel);
		result.setOutputPanel(outputPanel);
 		
		

	//	labelHeader = new JLabel("Select Cities or States to find their surrounding Airports and Mountains :");
		panelLandMark = new JPanel();
		panelCombo = new  JPanel();
		panelLandMark.setLayout(null);

		selectDB = new SelectDB();
		selectDB.selectAirportTableNew();
		
		final SelectDB selectDB = new SelectDB();
		selectDB.selectCityTable();
		
		comboCountry = new JComboBox<>(selectDB.getCountryCity());
		comboState = new JComboBox<>(selectDB.getStateCity("Afghanistan"));
		comboCity = new JComboBox<>();
		comboCity.addItem(" All");
	    comboState.addItem(" All");

        selectDB.setComboCity("world_city_new",comboCity, comboCountry.getSelectedItem(), comboState.getSelectedItem(), comboState.getItemCount());


		comboCountry.setPreferredSize(new Dimension(240,25));
        comboState.setPreferredSize(new Dimension(240,25));
        comboCity.setPreferredSize(new Dimension(240,25));
        
        
		comboCountry.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				comboState.removeAllItems();
				comboCity.removeAllItems();
/*				comboCity.addItem(" All");
				comboState.addItem(" All");
*/
				for (String state : selectDB.getStateCity((String) comboCountry.getSelectedItem())) {
					comboState.addItem(state);
				}
				

				int totalCity = 0;
		        totalCity = selectDB.setComboCity("world_city_new",comboCity, comboCountry.getSelectedItem(), comboState.getSelectedItem(), comboState.getItemCount());
				
		       // setResult(labelHeader,totalCity);
			}
	
		});
		comboState.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				comboCity.removeAllItems();
				comboCity.addItem(" All");
				if (!" All".equals(comboCity.getItemAt(1))) {
				//	comboCity.addItem(" All");
				}

		        int totalCity = 0;
				if (comboState.getSelectedItem() != null && !" All".equals(comboState.getSelectedItem() )) {
					totalCity =  selectDB.setComboCity("world_city_new",comboCity, comboCountry.getSelectedItem(), comboState.getSelectedItem(), comboState.getMaximumRowCount());
				}
				
				//setResult(labelHeader,totalCity);
				
			}

		});

   	    panelCombo.add(comboCountry);
		panelCombo.add(comboState);
		panelCombo.add(comboCity);

		searchBt = new JButton("Search");
		searchBt.addActionListener(new ActionListener()
		    {
		      public void actionPerformed(ActionEvent e)
		      {
		          new ReadData().createKMLCity(result, selectDB.getMapCities(), comboCountry,comboState,comboCity,
		        		  selectMountain,selectVor,selectNdb,
			        		 new Distance(0, 
			        				 (int)distanceSpin.getMountainSpinner().getValue(), 
			        				 (int)distanceSpin.getAirportSpinner().getValue(), 
			        				 (int)distanceSpin.getVorNdbSpinner().getValue(), 
			        				 (int)distanceSpin.getLandkmarkSpinner().getValue(), 
		        				     distanceSpin.getCheckLinedist().isSelected(),
			        				 0.0)
			        		 
		        		  
		        		  );
		             searchBt.setText("Update");
			         panelResult.removeAll();	
					 panelResult.add(result.getCityFormPanel());
					 panelResult.validate();
					 
					 distanceSpin.getLandkmarkSpinner().setEnabled(!"".equals(UtilityDB.getInstance().getProvince()));
					 
					 googleBt.setEnabled(true);
					 resetBt.setEnabled(true);
					 
				  	result.getCityListModel();
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
		          new ReadData().createKMLCity(result, selectDB.getMapCities(), comboCountry,comboState,comboCity,
		        		  selectMountain,selectVor,selectNdb,
			        		 new Distance(0, 
			        				 (int)distanceSpin.getMountainSpinner().getValue(), 
			        				 (int)distanceSpin.getAirportSpinner().getValue(), 
			        				 (int)distanceSpin.getVorNdbSpinner().getValue(), 
			        				 (int)distanceSpin.getLandkmarkSpinner().getValue(), 
			        				 distanceSpin.getCheckLinedist().isSelected(),
			        				 0.0)
			        		 
		        		  
		        		  );
		             searchBt.setText("Update");
			         panelResult.removeAll();	
					 panelResult.add(result.getCityFormPanel());
					 panelResult.validate();
					 
					 distanceSpin.getLandkmarkSpinner().setEnabled(!"".equals(UtilityDB.getInstance().getProvince()));
					 
					 googleBt.setEnabled(true);
					 resetBt.setEnabled(true);
					 
				  	result.getCityListModel();
				  	panelLandMark.validate();

				Utility.getInstance().launchGoogleEarth(new File(Utility.getInstance().getFlightPlanName(Info.kmlCityAirportMountainName)));

			}
		});
		
		
		
		landMeBt = new JButton("Land Me");
		landMeBt.setEnabled(false);
		landMeBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
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
				 } else if ("landmark".equals(result.getCurrentView())){
						CreateKML.makeOn(result.getSelectedLandmarks().get(keyICAO), result.getCurrentView());
				 }

			}
		});		

		
		return setLandmarkPanel(distanceSpin);
	
	}
	 /**
	  * getMountainWorld
	  * 
	  * 
	  * @param manageXMLFile
	  * @return
	  */
	public JPanel getMountainPanel(final SelectCity selectCity ,final SelectMountain selectMountain, final SelectVor selectVor, final SelectNdb selectNdb) {
		
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
				

				int totalCity = 0;
		      //  totalCity = selectDB.setComboCity("world_city_new",comboCity, comboCountry.getSelectedItem(), comboMountain.getSelectedItem(), comboMountain.getItemCount());
				
		       // setResult(labelHeader,totalCity);
			}
	
		});
   	    panelCombo.add(comboCountry);
		panelCombo.add(comboMountain);

		searchBt = new JButton("Search");
		searchBt.addActionListener(new ActionListener()
		    {
		      public void actionPerformed(ActionEvent e)
		      {
		          new ReadData().createKMLMountain(result, selectDB.getMapCities(), comboCountry,comboMountain, selectVor,selectNdb,
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
		          new ReadData().createKMLMountain(result, selectDB.getMapCities(), comboCountry,comboMountain, selectVor,selectNdb,
			        		 new Distance((int)distanceSpin.getCitySpinner().getValue(), 
			        				 0, 
			        				 (int)distanceSpin.getAirportSpinner().getValue(), 
			        				 (int)distanceSpin.getVorNdbSpinner().getValue(), 
			        				    (int)distanceSpin.getLandkmarkSpinner().getValue(), 
			        				 distanceSpin.getCheckLinedist().isSelected(),
			        				 0.0)
			        		 
		        		  
		        		  );
		             searchBt.setText("Update");
			         panelResult.removeAll();	
					 panelResult.add(result.getMountainFormPanel());
					 panelResult.validate();
					 
					 distanceSpin.getLandkmarkSpinner().setEnabled(!"".equals(UtilityDB.getInstance().getProvince()));
					 
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

			}
		});		

		return setLandmarkPanel(distanceSpin);
	}
	

}
