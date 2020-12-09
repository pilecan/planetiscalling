package com.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.cfg.common.Info;
import com.cfg.util.Util;
import com.model.Preference;
import com.util.Utility;

public class PanelManage implements Info {
	
	public Map <String, Preference> hashFP;
	public Map <String, Preference> hashKML;
	
	private JComboBox<String> comboFPDir;
	private JComboBox<String> comboKMLDir;
	
	private JLabel labelDefaultFP;
	private JLabel labelDefaultKML;


	public PanelManage() {
		super();
		// TODO Auto-generated constructor stub
	}

	private JPanel panelResult;
	
	public JPanel getSettingPanel() {
		hashFP = new TreeMap<>();
		hashKML = new TreeMap<>();
		
		panelResult = new JPanel();
		panelResult.setLayout(null);
		
		comboFPDir = new JComboBox<>();
		comboKMLDir = new JComboBox<>();
		JLabel labelHeader = new JLabel("Setting Panel");
		
		labelDefaultFP  = new JLabel();
		labelDefaultKML = new JLabel();
	
		readPrefs() ;

		
		JButton buttonFPPerfs = new JButton("Add Flightplan");
		buttonFPPerfs.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  
	    	  System.out.println(e);
	
	        
	      }
	    });

		JButton buttonKMLPerfs = new JButton("Add KML Dir");
		buttonKMLPerfs.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	
	    	  System.out.println(e);
	        
	      }
	    });
		
		comboFPDir.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
		        labelDefaultFP.setText("Current dir :"+hashFP.get(e.getItem()).getPath());
		  		labelDefaultFP.setToolTipText(hashFP.get(e.getItem()).getPath());

			}
	
		});
		comboKMLDir.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
		    	labelDefaultKML.setText("Current dir :"+hashKML.get(e.getItem()).getPath());
		    	labelDefaultKML.setToolTipText(hashKML.get(e.getItem()).getPath());

			}
	
		});
		

		labelHeader.setBounds(10, 10, 120, 23);
		labelDefaultFP.setBounds(10, 30, 300, 23);
		buttonFPPerfs.setBounds(10, 50, 120, 23);
		labelDefaultKML.setBounds(10, 80, 300, 23);
		buttonKMLPerfs.setBounds(10, 100, 120, 23);
		comboFPDir.setBounds(130, 50, 120, 23);
		comboKMLDir.setBounds(130, 100, 120, 23);
		

		panelResult.add(labelHeader);
		panelResult.add(labelDefaultFP);
		panelResult.add(labelDefaultKML);
		panelResult.add(buttonFPPerfs);
		panelResult.add(buttonKMLPerfs);
		panelResult.add(comboFPDir);
		panelResult.add(comboKMLDir);
		
		return panelResult;
	}
	
	private void readPrefs() {
		
		Utility.getInstance().readPrefProperties();
		//Utility.getInstance().getPrefs();
		
		labelDefaultFP.setText("Current dir :"+Utility.getInstance().getPrefs().getProperty("flightplandir"));
		labelDefaultFP.setToolTipText(Utility.getInstance().getPrefs().getProperty("flightplandir"));
		labelDefaultKML.setText("Current dir :"+Utility.getInstance().getPrefs().getProperty("kmlflightplandir"));
		labelDefaultKML.setToolTipText(Utility.getInstance().getPrefs().getProperty("kmlflightplandir"));

		Enumeration<?> e = Utility.getInstance().getPrefs().propertyNames();

	    while (e.hasMoreElements()) {
	      String key = (String) e.nextElement();
	      //System.out.println(key + " -- " + Utility.getInstance().getPrefs().getProperty(key));
	      if (key.contains("kml")) {
	    	  hashKML.put(Util.extractLastPath(Utility.getInstance().getPrefs().getProperty(key)), 
	    			  new Preference(key,Utility.getInstance().getPrefs().getProperty(key)));
	    	  
	      } else if (key.contains("flight")) {
	    	  hashFP.put(Util.extractLastPath(Utility.getInstance().getPrefs().getProperty(key)), 
	    			  new Preference(key,Utility.getInstance().getPrefs().getProperty(key)));
	      }	
	      
	
	    }
	      comboFPDir = new JComboBox<>(hashFP.keySet().toArray(new String[hashFP.size()]));
	      comboKMLDir = new JComboBox<>(hashKML.keySet().toArray(new String[hashKML.size()]));
	      comboKMLDir.setSelectedItem(Util.extractLastPath(Utility.getInstance().getPrefs().getProperty("kmlflightplandir")));
	      comboFPDir.setSelectedItem(Util.extractLastPath(Utility.getInstance().getPrefs().getProperty("flightplandir")));

	
	}

}
