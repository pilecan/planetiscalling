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
	
	public PanelLandmarks() {
		
	}
	
	public void setPanelLandmark() {
		provArray = UtilityDB.getInstance().getProvinces(); 
		geoTermArray = UtilityDB.getInstance().getGeoterm(provArray[0]); 
		geoNameArray = UtilityDB.getInstance().getGeoname(provArray[0],geoTermArray[0]); 

		comboCountryLandmark.addItem("Canada");
		for (String str:provArray) {
			comboStateLandmark.addItem(str);
		}
		for (String str:geoTermArray) {
			comboGeoterm.addItem(str);
		}
		for (String str:geoNameArray) {
			comboGeoname.addItem(str);
		}
		isLandmarkSetted = true;
		
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

/*		provArray = UtilityDB.getInstance().getProvinces(); 
		geoTermArray = UtilityDB.getInstance().getGeoterm(provArray[0]); 
		geoNameArray = UtilityDB.getInstance().getGeoname(provArray[0],geoTermArray[0]); 

		comboCountryLandmark = new JComboBox<>(UtilityDB.getInstance().getCountryCgn());
		comboStateLandmark = new JComboBox<>(provArray);
		comboGeoterm  = new JComboBox<>(geoTermArray);
		comboGeoname = new JComboBox<>(geoNameArray);
*/
		comboCountryLandmark = new JComboBox<>();
		comboStateLandmark = new JComboBox<>();
		comboGeoterm  = new JComboBox<>();
		comboGeoname = new JComboBox<>();

        int widht = 240;
        comboCountryLandmark.setPreferredSize(new Dimension(widht,25));
        comboStateLandmark.setPreferredSize(new Dimension(widht,25));
        comboGeoterm.setPreferredSize(new Dimension(widht,25));
        comboGeoname.setPreferredSize(new Dimension(widht,25));
        
        
        comboStateLandmark.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				comboGeoterm.removeAllItems();
				comboGeoname.removeAllItems();

				int totalCity = 0;
				if (comboStateLandmark.getSelectedItem() != null) {
					geoTermArray = UtilityDB.getInstance().getGeoterm((String) comboStateLandmark.getSelectedItem());
					geoNameArray = UtilityDB.getInstance().getGeoname((String) comboStateLandmark.getSelectedItem(),
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
					geoNameArray = UtilityDB.getInstance().getGeoname((String) comboStateLandmark.getSelectedItem(),
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
		
   	    panelCombo.add(comboCountryLandmark);
		panelCombo.add(comboStateLandmark);
		panelCombo.add(comboGeoterm);
		panelCombo.add(comboGeoname);

		searchBt = new JButton("Search");
		searchBt.addActionListener(new ActionListener()
		    {
		      public void actionPerformed(ActionEvent e)
		      {
		    	  
		    	        readData.createKMLLandmark(result, comboCountryLandmark,comboStateLandmark,comboGeoterm,comboGeoname,
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
			result.resetButton();
			
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
    	        readData.createKMLLandmark(result, comboCountryLandmark,comboStateLandmark,comboGeoterm,comboGeoname,
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

				Utility.getInstance().launchGoogleEarth(new File(Utility.getInstance().getFlightPlanName(Info.kmlLandmarkMountainCityAirportName)));

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
	
	
	
	public void setPanelCity() {
/*		selectDB = new SelectDB();
		selectDB.selectAirportTableNew();
*/
		
		countryArray = UtilityDB.getInstance().getCountryCity(); 
		adminArray = UtilityDB.getInstance().getSateCity(countryArray[0]) ;
		cityArray = UtilityDB.getInstance().getCityCity(countryArray[0],adminArray[0]) ;

		for (String str:countryArray) {
			comboCountryCity.addItem(str);
		}
		for (String str:adminArray) {
			comboStateCity.addItem(str);
		}
		
		for (String str:cityArray) {
			comboCityCity.addItem(str);
		}
		
		comboCountryCity.setSelectedIndex(0);
     
        
		isCitySetted = true;

	}

	
	/**
	 * 
	 * getCityWorld
	 * 
	 * 
	 * @param manageXMLFile
	 * @return
	 */
	public JPanel getCityPanel() {
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
		
		comboCountryCity = new JComboBox<>();
		comboStateCity = new JComboBox<>();
		comboCityCity = new JComboBox<>();


		comboCountryCity.setPreferredSize(new Dimension(240,25));
        comboStateCity.setPreferredSize(new Dimension(240,25));
        comboCityCity.setPreferredSize(new Dimension(240,25));
        
        
		comboCountryCity.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				comboStateCity.removeAllItems();
				comboCityCity.removeAllItems();

				if (comboCountryCity.getSelectedItem() != null) {
					adminArray = UtilityDB.getInstance().getSateCity((String) comboCountryCity.getSelectedItem()) ;
					cityArray = UtilityDB.getInstance().getCityCity((String) comboCountryCity.getSelectedItem(),adminArray[0]) ;

					for (String str:adminArray) {
						comboStateCity.addItem(str);
					}
					
					for (String str:cityArray) {
						comboCityCity.addItem(str);
					}

				}

		}
	
		});
		comboStateCity.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				comboCityCity.removeAllItems();
				comboCityCity.addItem(" All");
				if (comboStateCity.getSelectedItem() != null) {
					cityArray = UtilityDB.getInstance().getCityCity((String) comboCountryCity.getSelectedItem(),
							(String) comboStateCity.getSelectedItem()) ;
					
					for (String str:cityArray) {
						comboCityCity.addItem(str);
					}

				}
		}

		});

   	    panelCombo.add(comboCountryCity);
		panelCombo.add(comboStateCity);
		panelCombo.add(comboCityCity);
		
		dialog = UtilityEarthAnimation.getInstance().panelWait();

		searchBt = new JButton("Search");
		searchBt.addActionListener(new ActionListener()
		    {
		      public void actionPerformed(ActionEvent e)
		      {
		          
		    	  SwingWorker<Void,Void> worker = new SwingWorker<Void,Void>()
		  		{
		  		    protected Void doInBackground()
		  		    {

		    	  
		          new ReadData().createKMLCity(result, comboCountryCity,comboStateCity,comboCityCity,
			        		 new Distance(0, 
			        				 (int)distanceSpin.getMountainSpinner().getValue(), 
			        				 (int)distanceSpin.getAirportSpinner().getValue(), 
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
		    	  SwingWorker<Void,Void> worker = new SwingWorker<Void,Void>()
		  		{
		  		    protected Void doInBackground()
		  		    {

		    	  
		          new ReadData().createKMLCity(result,  comboCountryCity,comboStateCity,comboCityCity,
			        		 new Distance(0, 
			        				 (int)distanceSpin.getMountainSpinner().getValue(), 
			        				 (int)distanceSpin.getAirportSpinner().getValue(), 
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
				result.landMe();
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
			        				 (int)distanceSpin.getLandkmarkSpinner().getValue(), 
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
