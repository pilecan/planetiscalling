package com.panels;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.cfg.common.Info;
import com.main.PlanetIsCalling;
import com.model.Preference;
import com.util.SwingUtils;
import com.util.Util;
import com.util.Utility;

public class PanelManage implements Info {
	
	public Map <String, Preference> hashFP;
	public Map <String, Preference> hashKML;
	
	private JComboBox<String> comboFPDir;
	private JComboBox<String> comboKMLDir;
	private JComboBox<String> comboColor;
	
	private JLabel labelDefaultFP;
	private JLabel labelDefaultKML;
	private JLabel labelDefaultColor;
	private JLabel labelGoogle;

	private boolean isFromAdd = false;

	public PanelManage() {
		super();
		// TODO Auto-generated constructor stub
	}

	private JPanel panelResult;
	
	public JPanel getSettingPanel(final PlanetIsCalling frame) {
		hashFP = new TreeMap<>();
		hashKML = new TreeMap<>();
		
		panelResult = new JPanel();
		panelResult.setLayout(null);
		
		JLabel labelHeader = new JLabel("Setting Panel");
		
		
		labelGoogle  = new JLabel();
		labelDefaultFP  = new JLabel();
		labelDefaultKML = new JLabel();
		labelDefaultColor = new JLabel();
		
		comboFPDir = new JComboBox<>();
		comboKMLDir = new JComboBox<>();
		comboColor = new JComboBox<>();
	
		readPrefs() ;

		JButton saveButton = new JButton("Save");
		JButton resetButton = new JButton("Reset");
		resetButton.setBounds(120, 245, 90, 23);
  	    saveButton.setBounds(10, 245, 90, 23);

		
	    JButton buttonFPPerfs = new JButton("Add Flightplan Dir");
		buttonFPPerfs.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
			isFromAdd = true;
		  	selectFlightplan("Select Flightplan Directory","flightplandir");
	     	Utility.getInstance().savePrefProperties();
	      }
	    });

		JButton buttonKMLPerfs = new JButton("Add KML Dir");
		buttonKMLPerfs.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
			isFromAdd = true;
	  		selectFlightplan("Select KML Directory","kmlflightplandir");
	     	Utility.getInstance().savePrefProperties();
	      }
	    });
		
		comboFPDir.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
		        try {
					if (!isFromAdd) {
						labelDefaultFP.setText("Current dir :"+hashFP.get(e.getItem()).getPath());
						labelDefaultFP.setToolTipText(hashFP.get(e.getItem()).getPath());
						Utility.getInstance().getPrefs().put("flightplandir", hashFP.get(e.getItem()).getPath());
				     	Utility.getInstance().savePrefProperties();
					}
					isFromAdd = false;

				} catch (Exception e1) {
				}
		  		

			}
	
		});

		comboKMLDir.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				try {
					if (!isFromAdd) {
						labelDefaultKML.setText("Current dir :" + hashKML.get(e.getItem()).getPath());
						labelDefaultKML.setToolTipText(hashKML.get(e.getItem()).getPath());
						Utility.getInstance().getPrefs().put("kmlflightplandir", hashKML.get(e.getItem()).getPath());
				    	Utility.getInstance().savePrefProperties();

					}
					isFromAdd = false;
				} catch (Exception e1) {
					// TODO Auto-generated catch block
				}

			}

		});
		
		JButton buttonGoogle = new JButton("Google Earth Dir");
		buttonGoogle.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
		  	    selectFlightplan("Select Google Earth Directory","googleearth");
/*				for (Entry<Object, Object> e1 : Utility.getInstance().getPrefs().entrySet()) {
					System.out.println(e1);
				}
*/		  	
	      }
	    });

		labelDefaultColor.setText("GUI Colors");
		labelDefaultColor.setToolTipText("Set color of GUI here");
		comboColor.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				try {
					Utility.getInstance().getPrefs().put("day.period", e.getItem());
			    	Utility.getInstance().savePrefProperties();
				    Utility.getInstance().initLookAndFeel(frame,Util.getStoredPeriodNumber());

				} catch (Exception e1) {
				//	System.err.println(e1);
					// TODO Auto-generated catch block
				}

			}

		});
	
		saveButton.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  Utility.getInstance().savePrefProperties();
	      }
	    });

		labelHeader.setBounds(10, 10, 120, 23);
		
		labelDefaultFP.setBounds(10, 30, 300, 23);
		buttonFPPerfs.setBounds(10, 50, 120, 23);
		comboFPDir.setBounds(130, 50, 120, 23);

		labelDefaultKML.setBounds(10, 80, 300, 23);
		buttonKMLPerfs.setBounds(10, 100, 120, 23);
		comboKMLDir.setBounds(130, 100, 120, 23);
		
		labelGoogle.setBounds(10, 130, 300, 23);
		buttonGoogle.setBounds(10, 150, 120, 23);

		labelDefaultColor.setBounds(10, 180, 300, 23);
		comboColor.setBounds(10, 200, 120, 23);


		panelResult.add(labelHeader);
		panelResult.add(labelDefaultFP);
		panelResult.add(labelDefaultKML);
		panelResult.add(buttonFPPerfs);
		panelResult.add(buttonKMLPerfs);
		panelResult.add(comboFPDir);
		panelResult.add(comboKMLDir);
		panelResult.add(labelGoogle);
		//panelResult.add(saveButton);
		//panelResult.add(resetButton);
		panelResult.add(buttonGoogle);
		panelResult.add(labelDefaultColor);
		panelResult.add(comboColor);
		
		return panelResult;
	}
	
	private void selectFlightplan(String title,String key) {
		
		Utility.getInstance().readPrefProperties();
		
		JFileChooser chooser = selectDirectoryProgram(title, key);
	       // setAlwaysOnTop(false);
		
		chooser.setPreferredSize(new Dimension(550,400));

			Action details = chooser.getActionMap().get("viewTypeDetails");
			details.actionPerformed(null);
			
			JTable table = SwingUtils.getDescendantsOfType(JTable.class, chooser).get(0);
			table.getRowSorter().toggleSortOrder(3);		
			
			if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				
				 if ("flightplandir".equals(key)) {
					 setNewDir(chooser, key, labelDefaultFP, comboFPDir, hashFP); 
				 } else if ("kmlflightplandir".equals(key)) {
				     setNewDir(chooser, key, labelDefaultKML, comboKMLDir, hashKML); 
				 } else if ("googleearth".equals(key)) {
						System.out.println("key = "+key);
						Utility.getInstance().getPrefs().put(key, chooser.getSelectedFile().toString());
						labelGoogle.setText("Current dir :"+chooser.getSelectedFile().toString());
						labelGoogle.setToolTipText(chooser.getSelectedFile().toString());
						
					       for(Entry<Object, Object> e : Utility.getInstance().getPrefs().entrySet()) {
					            System.out.println(e);
					        }
					       System.out.println();
				 }


		    } else {
		    	//kmlFlightPlanFile = "";
		    }
			
		
	}
	
	private void setNewDir(JFileChooser chooser, String key, JLabel label, JComboBox<String> combo, Map <String,Preference> map) {
		String newDir = chooser.getSelectedFile().toString();
		String newKey = Util.extractLastElement(newDir);
		
		label.setText("Current dir :"+newDir);
		label.setToolTipText(newDir);
		
		combo.addItem((String)newKey);
	    combo.setSelectedIndex(combo.getItemCount()-1);

		map.put(newKey, new Preference(key+map.size(),newDir ));

		Utility.getInstance().getPrefs().put(key+map.size(), newDir);
		Utility.getInstance().getPrefs().put(key, newDir);
		
	 
		
	}

    private JFileChooser selectDirectoryProgram(String title,String key){
		 String[] EXTENSION=null;
		 FileNameExtensionFilter filter  = null;
		 String directory = Utility.getInstance().getPrefs().getProperty(key);

		 
		 JFileChooser chooser = new JFileChooser();
 	 	 chooser.setCurrentDirectory(new java.io.File(directory));
		 chooser.setDialogTitle(title);
		 chooser.setAcceptAllFileFilterUsed(false);
		 chooser.setMultiSelectionEnabled(false);
		 
		 if ("flightplandir".equals(key) || "kmlflightplandir".equals(key)) {
			 chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	     } else if ("googleearth".equals(key)) {
			 EXTENSION=new String[]{"exe"};
			 filter=new FileNameExtensionFilter("googleearth.exe",EXTENSION);
			 chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			 chooser.setFileFilter(filter);
         }

	
		return chooser;
    	
    }
	
	private void readPrefs() {
		
		Utility.getInstance().readPrefProperties();
		//Utility.getInstance().getPrefs();
		
		labelDefaultFP.setText("Current dir :"+Utility.getInstance().getPrefs().getProperty("flightplandir"));
		labelDefaultFP.setToolTipText(Utility.getInstance().getPrefs().getProperty("flightplandir"));
		labelDefaultKML.setText("Current dir :"+Utility.getInstance().getPrefs().getProperty("kmlflightplandir"));
		labelDefaultKML.setToolTipText(Utility.getInstance().getPrefs().getProperty("kmlflightplandir"));
		labelGoogle.setToolTipText(Utility.getInstance().getPrefs().getProperty("googleearth"));
		labelGoogle.setText(Utility.getInstance().getPrefs().getProperty("googleearth"));
		labelDefaultColor.setText(Utility.getInstance().getPrefs().getProperty("numcolor"));

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
	   
	      comboFPDir = new JComboBox<>(hashFP.keySet().toArray(new String[hashFP.size()]));
	      comboFPDir.setSelectedItem(Util.extractLastPath(Utility.getInstance().getPrefs().getProperty("flightplandir")));
	      comboKMLDir = new JComboBox<>(hashKML.keySet().toArray(new String[hashKML.size()]));
	      comboKMLDir.setSelectedItem(Util.extractLastPath(Utility.getInstance().getPrefs().getProperty("kmlflightplandir")));
	 }
	    
       String[] periods = {"Morning","Afternoon","Evening","Night"};
      comboColor = new JComboBox<String>(periods);
      
      comboColor.setSelectedItem(Utility.getInstance().getPrefs().getProperty("day.period"));
      

	
	}
	
	
	private synchronized void LaunchPIC() {
		Util.pause(3000);
		Runtime rt = Runtime.getRuntime();
		try {
		  	Process ps = rt.exec("PlanetIsCalling.exe");
		  
		} catch (IOException e) {
			System.err.println(e);
		}
		
	}

}
