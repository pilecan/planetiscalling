package com.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.cfg.common.Info;
import com.main.PlanetIsCalling;
import com.model.Preference;
import com.util.SwingUtils;
import com.util.Util;
import com.util.Utility;
import com.util.UtilityTimer;

public class PanelManage implements Info, Runnable {
	
	public Map <String, Preference> hashFP;
	public Map <String, Preference> hashKML;
	
	private JComboBox<String> comboFPDir;
	private JComboBox<String> comboKMLDir;
	private JComboBox<String> comboColor;
	
	private JLabel labelFP;
	private JLabel labelKML;
	private JLabel labelColor;
	private JLabel labelGoogle;
	private JLabel labelTimer;
	private JLabel labelSound;
	
	private JPanel timerPanel;
	private JPanel folderPanel;
	private JPanel googlePanel;
	private JPanel colorPanel;
	private JPanel soundPanel;

	private String timerSartAt = "";
	
	PlanetIsCalling frame;


	private boolean isFromAdd = false;

	public PanelManage() {
		super();
		// TODO Auto-generated constructor stub
	}

	private JPanel panelManage;
	
	public JPanel getSettingPanel(final PlanetIsCalling frame) {
        Thread t1 =new Thread(this);  
        t1.start();
		panelManage = new JPanel();
		panelManage.setLayout(null);

		hashFP = new TreeMap<>();
		hashKML = new TreeMap<>();
		
		this.frame = frame;

		timerSartAt = UtilityTimer.getInstance().getTime("local", true);
		timerPanel = new JPanel(new BorderLayout());
		timerPanel.setBorder(new TitledBorder("Timer"));

		folderPanel = new JPanel(new BorderLayout());
		folderPanel.setBorder(new TitledBorder("Folders"));
		
		googlePanel = new JPanel(new BorderLayout());
		googlePanel.setBorder(new TitledBorder("Google Earth"));
		
		colorPanel = new JPanel(new BorderLayout());
		colorPanel.setBorder(new TitledBorder("Colors of the Day"));
		
		soundPanel = new JPanel(new BorderLayout());
		soundPanel.setBorder(new TitledBorder("Enable/Disable Sound"));
		
		
		JLabel labelHeader = new JLabel("Setting Panel");

		labelGoogle  = new JLabel();
		labelFP  = new JLabel();
		labelKML = new JLabel();
		labelColor = new JLabel();
		labelTimer = new JLabel();
		labelSound = new JLabel();
		
		comboFPDir = new JComboBox<>();
		comboKMLDir = new JComboBox<>();
		comboColor = new JComboBox<>();
		
	
		readPreferences() ;

		JButton saveButton = new JButton("Save");
		JButton resetButton = new JButton("Reset");
		resetButton.setBounds(120, 245, 90, 23);
  	    saveButton.setBounds(10, 245, 90, 23);

		
	    JButton buttonFPPerfs = new JButton("Select Flightplan Folder");
		buttonFPPerfs.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
			isFromAdd = true;
		  	selectFlightplan("Select Flightplan Directory","flightplandir");
	     	Utility.getInstance().savePrefProperties();
	      }
	    });

		JButton buttonKMLPerfs = new JButton("Select KML Folder");
		buttonKMLPerfs.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
			isFromAdd = true;
	  		selectFlightplan("Select KML folder","kmlflightplandir");
	     	Utility.getInstance().savePrefProperties();
	      }
	    });
		
		labelFP.setText("Current Flight Plan folder (?)");
		comboFPDir.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
		        try {
					if (!isFromAdd) {
						labelFP.setToolTipText(hashFP.get(e.getItem()).getPath());
						comboFPDir.setToolTipText(hashFP.get(e.getItem()).getPath());
						Utility.getInstance().getPrefs().put("flightplandir", hashFP.get(e.getItem()).getPath());
				     	Utility.getInstance().savePrefProperties();
					}
					isFromAdd = false;

				} catch (Exception e1) {
				}
		  		

			}
	
		});

		labelKML.setText("Current KML folder (?)");
		comboKMLDir.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				try {
					if (!isFromAdd) {
						labelKML.setToolTipText(hashKML.get(e.getItem()).getPath());
						comboKMLDir.setToolTipText(hashKML.get(e.getItem()).getPath());
						Utility.getInstance().getPrefs().put("kmlflightplandir", hashKML.get(e.getItem()).getPath());
				    	Utility.getInstance().savePrefProperties();

					}
					isFromAdd = false;
				} catch (Exception e1) {
					// TODO Auto-generated catch block
				}

			}

		});
		
		labelGoogle.setText("Current Google Earth.exe (?)");
		JButton buttonGoogle = new JButton("Select Google Earth (googleearth.exe)");
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

		labelColor.setText("Time of Day Now");
		labelColor.setToolTipText("Morning, Afternoon, Evening, Night");
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

		
		timerPanel.add(labelTimer);

		JButton resetTimerBt = new JButton("Reset Timer");
		resetTimerBt.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  UtilityTimer.getInstance().setMillis(0);

	  		  timerSartAt = UtilityTimer.getInstance().getTime("local", true);
	      }
		});
		timerPanel.add(resetTimerBt);

 
		saveButton.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  Utility.getInstance().savePrefProperties();
	      }
	    });

		
		int x = 10;
		int y = 60;
		int w = 320;
		
		
		labelHeader.setFont( new Font("SansSerif", Font.BOLD, 18));

		labelHeader.setBounds(310, 12, 200, 40);

		
		folderPanel.setBounds(x,y, 340, 160);
		
		labelFP.setBounds(      x+20, y+20, 320, 23);
		buttonFPPerfs.setBounds(x+20, y+45, 180, 23);
		comboFPDir.setBounds(    210, y+45, 120, 23);
		
		labelKML.setBounds(      x+20, y+90, 320, 23);
		buttonKMLPerfs.setBounds(x+20, y+115, 180, 23);
		comboKMLDir.setBounds(    210, y+115, 120, 23);
		
		googlePanel.setBounds(x,y+180, 340, 90);

		labelGoogle.setBounds(x+20, y+200, 320, 23);
		buttonGoogle.setBounds(x+20, y+225, 280, 23);

		
		timerPanel.setBounds(380, y, 300, 100);
		labelTimer.setBounds(400, y+20, 200, 23);
		resetTimerBt.setBounds(400, y+50, 120, 23);

		colorPanel.setBounds(380, y+110, 300, 100);
		labelColor.setBounds(400, y+135, 300, 23);
		comboColor.setBounds(400, y+160, 150, 23);

		panelManage.add(labelHeader);
		panelManage.add(labelFP);
		panelManage.add(labelKML);
		panelManage.add(buttonFPPerfs);
		panelManage.add(buttonKMLPerfs);
		panelManage.add(comboFPDir);
		panelManage.add(comboKMLDir);
		panelManage.add(labelGoogle);
		//panelResult.add(saveButton);
		//panelResult.add(resetButton);
		panelManage.add(buttonGoogle);
		panelManage.add(labelColor);
		panelManage.add(comboColor);
		panelManage.add(labelTimer);
		panelManage.add(resetTimerBt);
		panelManage.add(timerPanel);
		panelManage.add(folderPanel);
		panelManage.add(googlePanel);
		panelManage.add(colorPanel);

		return panelManage;
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
					 setNewDir(chooser, key, labelFP, comboFPDir, hashFP); 
				 } else if ("kmlflightplandir".equals(key)) {
				     setNewDir(chooser, key, labelKML, comboKMLDir, hashKML); 
				 } else if ("googleearth".equals(key)) {
						System.out.println("key = "+key);
						Utility.getInstance().getPrefs().put(key, chooser.getSelectedFile().toString());
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
	
	private void readPreferences() {
		
		Utility.getInstance().readPrefProperties();
		
		labelFP.setToolTipText(Utility.getInstance().getPrefs().getProperty("flightplandir"));
		labelKML.setToolTipText(Utility.getInstance().getPrefs().getProperty("kmlflightplandir"));
		labelGoogle.setToolTipText(Utility.getInstance().getPrefs().getProperty("googleearth"));
		labelGoogle.setText(Utility.getInstance().getPrefs().getProperty("googleearth"));
		labelColor.setText(Utility.getInstance().getPrefs().getProperty("numcolor"));

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
		  comboFPDir.setToolTipText(Utility.getInstance().getPrefs().getProperty("flightplandir"));

	      comboKMLDir = new JComboBox<>(hashKML.keySet().toArray(new String[hashKML.size()]));
	      comboKMLDir.setSelectedItem(Util.extractLastPath(Utility.getInstance().getPrefs().getProperty("kmlflightplandir")));
	      comboKMLDir.setToolTipText(Utility.getInstance().getPrefs().getProperty("kmlflightplandir"));
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

	@Override
	public void run() {
        while(true) 
        {
             try {
                Thread.currentThread().sleep(1000);
		  		labelTimer.setText("Started at "+timerSartAt+"    ["+UtilityTimer.getInstance().getTimer(UtilityTimer.getInstance().getMillis())+"]");
/*		  		if (Util.DAY_PERIOD.get(Util.getPeriod()) !=UtilityTimer.getInstance().getCurrentPeriod() ){
			  		UtilityTimer.getInstance().setCurrentPeriod(Util.DAY_PERIOD.get(Util.getPeriod())) ;
		  		    Utility.getInstance().initLookAndFeel(frame, Util.DAY_PERIOD.get(Util.getPeriod()) );
			  		System.out.println("new period of day!");
		  			Utility.getInstance().getPrefs().put("day.period", Util.getPeriod());
		  	    	Utility.getInstance().savePrefProperties();
		  	    	UtilityTimer.getInstance().setCurrentPeriod(Util.DAY_PERIOD.get(Util.getPeriod()));
			  		System.out.println(Util.DAY_PERIOD.get(Util.getPeriod()));
			  		System.out.println(UtilityTimer.getInstance().getCurrentPeriod());

		  		}
*/            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
	
	}

}
