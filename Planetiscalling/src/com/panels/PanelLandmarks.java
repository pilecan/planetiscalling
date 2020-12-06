package com.panels;

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

import com.cfg.common.Dataline;
import com.cfg.common.DistanceObject;
import com.cfg.file.ManageXMLFile;
import com.model.Distance;
import com.util.ReadData;

import net.SelectAiport;
import net.SelectCity;
import net.SelectDB;
import net.SelectMountain;

public class PanelLandmarks extends DistanceObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<String, Map<String,List<String>>> mapAll = new TreeMap<>();
	private Map<String, List<String>> mapState = new TreeMap<>();

	private JPanel panelResult;
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
	public JPanel getAirportWorld(ManageXMLFile manageXMLFile,SelectAiport selectAiport,SelectCity selectCity,SelectMountain selectMountain) {
		initPanelDistances("airport");
		
		labelHeader = new JLabel("Select Airport cities to find their surrounding Cities and Mountains :");
		panelResult = new JPanel();
		panelCombo = new  JPanel();
		panelResult.setLayout(null);

		selectDB = new SelectDB();
		selectDB.selectAirportTableNew();

		this.selectAiport = selectAiport;
		this.manageXMLFile = manageXMLFile;
		
		JComboBox<String> comboCountry = new JComboBox<>(selectDB.getCountry());
		JComboBox<String> comboState = new JComboBox<>(selectDB.getStates(comboCountry.getSelectedItem()));
		JComboBox<String> comboCity = new JComboBox<>();
		comboCity.addItem(" All");

        selectDB.setComboCity("airport",comboCity, comboCountry.getSelectedItem(), comboState.getSelectedItem(), comboState.getItemCount());

		comboCountry.setPreferredSize(new Dimension(240,20));
        comboState.setPreferredSize(new Dimension(240,20));
        comboCity.setPreferredSize(new Dimension(240,20));
        
        
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
		panelCombo.setBounds(10, 30, 240, 80);
		
		form.setBounds(10, 110, 320, 100);

		
		clearButton.setBounds(120, 210, 90, 23);
  	    searchButton.setBounds(10, 210, 90, 23);
  	  	labelResult.setBounds(260, 200, 200, 63);

		searchButton.addActionListener(new ActionListener()
		    {
		      public void actionPerformed(ActionEvent e)
		      {
		         new ReadData().createKMLAirport(manageXMLFile,selectDB.getMapCities(), comboCountry,comboState,comboCity,
		        		 new Distance((int)citySpinner.getValue(), (int)mountainSpinner.getValue(), (int)airportSpinner.getValue(), checkLinedist.isSelected()));
		      }
		    });

		clearButton.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  labelResult.setText("");
	      }
	    });
		
		panelResult.add(labelHeader);
		panelResult.add(panelCombo);
		panelResult.add(form);
		panelResult.add(searchButton);
		panelResult.add(clearButton);
		panelResult.add(labelResult);
		
		return panelResult;
	
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
	public JPanel getCityWorld(ManageXMLFile manageXMLFile) {
		initPanelDistances("city");

		labelHeader = new JLabel("Select Cities or States to find their surrounding Airports and Mountains :");
		panelResult = new JPanel();
		panelCombo = new  JPanel();
		panelResult.setLayout(null);

		selectDB = new SelectDB();
		selectDB.selectAirportTableNew();
		this.manageXMLFile = manageXMLFile;
		
		SelectDB selectDB = new SelectDB();
		selectDB.selectCityTable();
		
		JComboBox<String> comboCountry = new JComboBox<>(selectDB.getCountryCity());
		JComboBox<String> comboState = new JComboBox<>(selectDB.getStateCity("Afghanistan"));
		JComboBox<String> comboCity = new JComboBox<>();
		comboCity.addItem(" All");
	    comboState.addItem(" All");

        selectDB.setComboCity("world_city_new",comboCity, comboCountry.getSelectedItem(), comboState.getSelectedItem(), comboState.getItemCount());


		comboCountry.setPreferredSize(new Dimension(240,20));
        comboState.setPreferredSize(new Dimension(240,20));
        comboCity.setPreferredSize(new Dimension(240,20));
        
        
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
		
		panelCombo.setBounds(10, 30, 240, 80);
		
		form.setBounds(10, 110, 320, 100);
		
		clearButton.setBounds(120, 210, 90, 23);
  	    searchButton.setBounds(10, 210, 90, 23);
  	  	labelResult.setBounds(260, 40, 200, 63);

		searchButton.addActionListener(new ActionListener()
		    {
		      public void actionPerformed(ActionEvent e)
		      {
		          new ReadData().createKMLCity(manageXMLFile,selectDB.getMapCities(), comboCountry,comboState,comboCity,
			        		 new Distance((int)citySpinner.getValue(), (int)mountainSpinner.getValue(), (int)airportSpinner.getValue(), checkLinedist.isSelected()));
		      }
		    });

		clearButton.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  labelResult.setText("");
	      }
	    });
		
		panelResult.add(labelHeader);
		panelResult.add(panelCombo);
		panelResult.add(form);
		panelResult.add(searchButton);
		panelResult.add(clearButton);
		panelResult.add(labelResult);
		
		return panelResult;
	
	}
	 /**
	  * getMountainWorld
	  * 
	  * 
	  * @param manageXMLFile
	  * @return
	  */
	public JPanel getMountainWorld(ManageXMLFile manageXMLFile) {
		
		initPanelDistances("mountain");
		
		labelHeader = new JLabel("Mountain to find their surrounding Airports and Cities:");
		panelResult = new JPanel();
		panelCombo = new  JPanel();
		panelResult.setLayout(null);

		selectDB = new SelectDB();
		selectDB.selectAirportTableNew();
		this.manageXMLFile = manageXMLFile;
		
		SelectDB selectDB = new SelectDB();
		selectDB.selectMountainTable();
		
		JComboBox<String> comboCountry = new JComboBox<>(selectDB.getCountryMountain());
		JComboBox<String> comboMountain = new JComboBox<>(selectDB.getMountain("Afghanistan"));
		comboMountain.addItem(" All");

//        selectDB.setComboCity("world_city_new",comboCity, comboCountry.getSelectedItem(), comboMountain.getSelectedItem(), comboMountain.getItemCount());


		comboCountry.setPreferredSize(new Dimension(200,20));
        comboMountain.setPreferredSize(new Dimension(200,20));
        
        
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
		panelCombo.setBounds(5, 30, 240, 60);
		
		form.setBounds(10, 85, 320, 100);
		
		clearButton.setBounds(120, 190, 90, 23);
  	    searchButton.setBounds(10, 190, 90, 23);
  	  	labelResult.setBounds(260, 40, 200, 63);

		searchButton.addActionListener(new ActionListener()
		    {
		      public void actionPerformed(ActionEvent e)
		      {
		          new ReadData().createKMLMountain(manageXMLFile,selectDB.getMapCities(), comboCountry,comboMountain,
		        		  new Distance((int)citySpinner.getValue(), 0, (int)airportSpinner.getValue(), checkLinedist.isSelected()));
		      }
		    });

		clearButton.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  labelResult.setText("");
	      }
	    });
		
		panelResult.add(labelHeader);
		panelResult.add(panelCombo);
		panelResult.add(form);		
		panelResult.add(searchButton);
		panelResult.add(clearButton);
		panelResult.add(labelResult);
		
		return panelResult;
	
	}
	

}
