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
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;

import com.backend.CreateKML;
import com.backend.ReadData;
import com.cfg.common.Dataline;
import com.cfg.common.DistanceSpinner;
import com.cfg.common.Info;
import com.db.SelectAirport;
import com.db.SelectCity;
import com.db.SelectDB;
import com.db.SelectMountain;
import com.db.SelectNdb;
import com.db.SelectVor;
import com.model.Distance;
import com.model.Result;
import com.util.Utility;

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

	
	public PanelLandmarks() {
		
	}
	/**
	 * getCityAirport
	 * 
	 * 
	 * 
	 * 
	 * @return
	 */
	public JPanel getAirportPanel(final SelectCity selectCity,final SelectMountain selectMountain, final SelectVor selectVor, final SelectNdb selectNdb) {
		final DistanceSpinner distanceSpin = new DistanceSpinner();
		distanceSpin.initPanelDistances("airport");

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
		askMePanel.setBorder(new TitledBorder(""));
        askMePanel.add(askmeScrollPan);

    	
    	outputPanel = new JPanel(new BorderLayout());
		outputPanel.setBorder(new TitledBorder(""));

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

        selectDB.setComboCity("airport",comboCity, comboCountry.getSelectedItem(), comboState.getSelectedItem(), comboState.getItemCount());

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
			        totalCity = selectDB.setComboCity("airport",comboCity, comboCountry.getSelectedItem(), comboState.getSelectedItem(), comboState.getItemCount());
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
					totalCity =  selectDB.setComboCity("airport",comboCity, comboCountry.getSelectedItem(), comboState.getSelectedItem(), comboState.getMaximumRowCount());
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
		         readData.createKMLAirport(result, selectDB.getMapCities(), comboCountry,comboState,comboCity,
		        		 selectMountain, selectVor,selectNdb,
		        		 new Distance((int)distanceSpin.getCitySpinner().getValue(), 
		        				 (int)distanceSpin.getMountainSpinner().getValue(), 
		        				 0, 
		        				 (int)distanceSpin.getVorNdbSpinner().getValue(), 
		        				 distanceSpin.getCheckLinedist().isSelected(),
		        				 0.0));
		         
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
		        				 distanceSpin.getCheckLinedist().isSelected(),
		        				 0.0));
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

			

	   return setLandmarkPanel(distanceSpin);
	}
	
	private JPanel setLandmarkPanel(DistanceSpinner distanceSpin) {

		askMeBt = new JButton("Ask Me");
		askMeBt.setEnabled(false);

		askMePanel.setVisible(false);
		
	    result.setButtons(landMeBt, askMeBt);

		
		distanceSpin.getSpinnerPanel().setBounds(10, 10, 310, 190);
		panelCombo.setBounds(10, 200, 240, 90);
		
		int x = 330;
		
	  	panelResult.setBounds(x, 10, 340, 190);	
	  	outputPanel.setBounds(x, 210, 340, 150);	
	  	askMePanel.setBounds(x, 10, 340, 190);	

	  	x += 0;
		landMeBt.setBounds(x, 370, 94, 23);
		askMeBt.setBounds(x+244, 370, 94, 23);

		resetBt.setBounds(120, 300, 90, 23);
  	    searchBt.setBounds(10, 300, 90, 23);
		googleBt.setBounds(10, 330, 200, 23);

		
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
		
		selectAirport = new SelectAirport();
		
		readData = new ReadData();

		jEditorPane = Utility.getInstance().initjEditorPane(jEditorPane);
		
		panelResult = new JPanel(new BorderLayout());
		panelResult.setBorder(new TitledBorder("Searh Result"));


		askMePanel = new JPanel(new BorderLayout());
		askMePanel.setBorder(new TitledBorder(""));

    	askmeScrollPan = new JScrollPane(jEditorPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
      	   public void run() { 
      		   askmeScrollPan.getVerticalScrollBar().setValue(0);
      	   }
      	});
    	askMePanel.add(askmeScrollPan);

		outputPanel = new JPanel(new BorderLayout());
		outputPanel.setBorder(new TitledBorder(""));

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
			        				 distanceSpin.getCheckLinedist().isSelected(),
			        				 0.0)
			        		 
		        		  
		        		  );
		             searchBt.setText("Update");
			         panelResult.removeAll();	
					 panelResult.add(result.getCityFormPanel());
					 panelResult.validate();
					 
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
			        				 distanceSpin.getCheckLinedist().isSelected(),
			        				 0.0)
			        		 
		        		  
		        		  );
		             searchBt.setText("Update");
			         panelResult.removeAll();	
					 panelResult.add(result.getCityFormPanel());
					 panelResult.validate();
					 
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
		askMePanel.setBorder(new TitledBorder(""));

 
    	askMePanel.add(askmeScrollPan);

    	outputPanel = new JPanel(new BorderLayout());
		outputPanel.setBorder(new TitledBorder(""));

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
				comboMountain.addItem(" All");

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

		return setLandmarkPanel(distanceSpin);
	}
	

}
