package com.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import com.cfg.common.Dataline;
import com.cfg.common.DistanceSpinner;
import com.cfg.common.Info;
import com.cfg.file.ManageXMLFile;
import com.model.Distance;
import com.util.ReadData;

import net.SelectAiport;
import net.SelectCity;
import net.SelectDB;
import net.SelectMountain;

public class PanelLandmarks implements Info {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<String, Map<String,List<String>>> mapAll = new TreeMap<>();
	private Map<String, List<String>> mapState = new TreeMap<>();

	private JPanel panelLandMark;
	private JPanel panelCombo;

	
	private SelectDB selectDB;
	public SelectAiport selectAiport;
	ManageXMLFile manageXMLFile;
	
	private JComboBox<String> comboCountry;
	private JComboBox<String> comboState;
	private JComboBox<String> comboCity;
	private JComboBox<String> comboMountain;

	private ReadData readData;
	
	final JLabel labelResult = new JLabel();

	private Dataline dataline;


	private JLabel labelHeader;
	private JScrollPane jScrollPane1;
	//private JTextArea textArea;

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
	public JPanel getAirportWorld(final ManageXMLFile manageXMLFile,SelectCity selectCity,SelectMountain selectMountain) {
		final DistanceSpinner distanceSpin = new DistanceSpinner();
		
		distanceSpin.initPanelDistances("airport");
		
		labelHeader = new JLabel("Select Airport cities to find their surrounding Cities and Mountains :");
		panelLandMark = new JPanel();
		panelCombo = new  JPanel();
		panelLandMark.setLayout(null);
		JPanel panelResult = new JPanel(new BorderLayout());
		panelResult.setBorder(new TitledBorder("Search Result"));


		selectDB = new SelectDB();
		selectDB.selectAirportTableNew();

		this.selectAiport = selectAiport;
		this.manageXMLFile = manageXMLFile;
		
		final JComboBox<String> comboCountry = new JComboBox<>(selectDB.getCountry());
		final JComboBox<String> comboState = new JComboBox<>(selectDB.getStates(comboCountry.getSelectedItem()));
		final JComboBox<String> comboCity = new JComboBox<>();
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

		JButton searchButton = new JButton("Create");
		JButton clearButton = new JButton("Reset");
			
		labelHeader.setBounds(10, 5, 300, 23);
		panelCombo.setBounds(10, 30, 240, 90);
		
		distanceSpin.getSpinnerPanel().setBounds(10, 135, 320, 100);
		
		clearButton.setBounds(120, 245, 90, 23);
  	    searchButton.setBounds(10, 245, 90, 23);
    	panelResult.setBounds(300, 20, 280, 200);	
		panelResult.add(labelResult);

		searchButton.addActionListener(new ActionListener()
		    {
		      public void actionPerformed(ActionEvent e)
		      {
		         new ReadData().createKMLAirport(manageXMLFile,selectDB.getMapCities(), comboCountry,comboState,comboCity,
		        		 new Distance((int)distanceSpin.getCitySpinner().getValue(), 
		        				 (int)distanceSpin.getMountainSpinner().getValue(), 
		        				 0, 
		        				 distanceSpin.getCheckLinedist().isSelected()));
		      }
		    });

		clearButton.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  labelResult.setText("");
	      }
	    });
		
		panelLandMark.add(labelHeader);
		panelLandMark.add(panelCombo);
		panelLandMark.add(distanceSpin.getSpinnerPanel());
		panelLandMark.add(searchButton);
		panelLandMark.add(clearButton);
		panelLandMark.add(labelResult);
		
		return panelLandMark;
	
	}
	
	private void setResult(JLabel labelResul, int totalCity)
	{
		labelResult.setText("<html>"
				+ "Country : "+comboCountry.getSelectedItem()+"<br>"
				+ "State: "+comboState.getSelectedItem()+"<br>"
			    + "Cities: "+(totalCity-1)
			    +" </html>");

		
	}
	/**
	 * 
	 * getCityWorld
	 * 
	 * 
	 * @param manageXMLFile
	 * @return
	 */
	public JPanel getCityWorld(final ManageXMLFile manageXMLFile) {
		final DistanceSpinner distanceSpin = new DistanceSpinner();
		distanceSpin.initPanelDistances("city");

		labelHeader = new JLabel("Select Cities or States to find their surrounding Airports and Mountains :");
		panelLandMark = new JPanel();
		panelCombo = new  JPanel();
		panelLandMark.setLayout(null);

		selectDB = new SelectDB();
		selectDB.selectAirportTableNew();
		this.manageXMLFile = manageXMLFile;
		
		final SelectDB selectDB = new SelectDB();
		selectDB.selectCityTable();
		
		final JComboBox<String> comboCountry = new JComboBox<>(selectDB.getCountryCity());
		final JComboBox<String> comboState = new JComboBox<>(selectDB.getStateCity("Afghanistan"));
		final JComboBox<String> comboCity = new JComboBox<>();
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

		JButton searchButton = new JButton("Create");
		JButton clearButton = new JButton("Reset");
			
		labelHeader.setBounds(10, 5, 300, 23);
		
		panelCombo.setBounds(10, 30, 240, 90);
		
		distanceSpin.getSpinnerPanel().setBounds(10, 135, 320, 100);
		
		clearButton.setBounds(120, 245, 90, 23);
  	    searchButton.setBounds(10, 245, 90, 23);
  	  	labelResult.setBounds(260, 40, 200, 63);

		searchButton.addActionListener(new ActionListener()
		    {
		      public void actionPerformed(ActionEvent e)
		      {
		          new ReadData().createKMLCity(manageXMLFile,selectDB.getMapCities(), comboCountry,comboState,comboCity,
			        		 new Distance(0, (int)distanceSpin.getMountainSpinner().getValue(), (int)distanceSpin.getAirportSpinner().getValue(), distanceSpin.getCheckLinedist().isSelected()));
		      }
		    });

		clearButton.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  labelResult.setText("");
	      }
	    });
		
		panelLandMark.add(labelHeader);
		panelLandMark.add(panelCombo);
 		panelLandMark.add(distanceSpin.getSpinnerPanel());
		panelLandMark.add(searchButton);
		panelLandMark.add(clearButton);
		panelLandMark.add(labelResult);
		
		return panelLandMark;
	
	}
	 /**
	  * getMountainWorld
	  * 
	  * 
	  * @param manageXMLFile
	  * @return
	  */
	public JPanel getMountainWorld(final ManageXMLFile manageXMLFile) {
		
		final DistanceSpinner distanceSpin = new DistanceSpinner();
		distanceSpin.initPanelDistances("mountain");
		
		labelHeader = new JLabel("Mountain to find their surrounding Airports and Cities:");
		panelLandMark = new JPanel();
		panelCombo = new  JPanel();
		panelLandMark.setLayout(null);

		selectDB = new SelectDB();
		selectDB.selectAirportTableNew();
		this.manageXMLFile = manageXMLFile;
		
		final SelectDB selectDB = new SelectDB();
		selectDB.selectMountainTable();
		
		final JComboBox<String> comboCountry = new JComboBox<>(selectDB.getCountryMountain());
		final JComboBox<String> comboMountain = new JComboBox<>(selectDB.getMountain("Afghanistan"));
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

		JButton searchButton = new JButton("Create");
		JButton clearButton = new JButton("Reset");
			
		labelHeader.setBounds(10, 5, 330, 23);
		panelCombo.setBounds(10, 30, 240, 60);
		
		distanceSpin.getSpinnerPanel().setBounds(10, 95, 320, 100);
		
		clearButton.setBounds(120, 200, 90, 23);
  	    searchButton.setBounds(10, 200, 90, 23);
  	  	labelResult.setBounds(260, 40, 200, 63);

		searchButton.addActionListener(new ActionListener()
		    {
		      public void actionPerformed(ActionEvent e)
		      {
		          new ReadData().createKMLMountain(manageXMLFile,selectDB.getMapCities(), comboCountry,comboMountain,
			        		 new Distance((int)distanceSpin.getCitySpinner().getValue(), 0, (int)distanceSpin.getAirportSpinner().getValue(), distanceSpin.getCheckLinedist().isSelected()));
		      }
		    });

		clearButton.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  labelResult.setText("");
	      }
	    });
		
		panelLandMark.add(labelHeader);
		panelLandMark.add(panelCombo);
 		panelLandMark.add(distanceSpin.getSpinnerPanel());
		panelLandMark.add(searchButton);
		panelLandMark.add(clearButton);
		panelLandMark.add(labelResult);
		
		return panelLandMark;
	
	}
	

}
