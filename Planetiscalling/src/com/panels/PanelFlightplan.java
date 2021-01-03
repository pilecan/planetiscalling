package com.panels;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;

import com.cfg.common.DistanceSpinner;
import com.cfg.file.ManageXMLFile;
import com.cfg.util.Util;
import com.model.Distance;
import com.model.Result;
import com.util.ReadData;

import net.SelectAirport;
import net.SelectCity;
import net.SelectMountain;
import net.SelectNdb;
import net.SelectVor;

public class PanelFlightplan {

	private ReadData readData;	
	private Result result;
	private JPanel panelFlightplan = new JPanel();
	private JPanel panelResult;
	
	private JButton googleBt;
	
	private DistanceSpinner distanceSpin; 
	
	private ManageXMLFile manageXMLFile;
	private SelectAirport selectAiport;
	private SelectCity selectCity;
	private SelectMountain selectMountain;
	private SelectVor selectVor;
	private SelectNdb selectNdb;
	
	private JPanel outputPanel;
	private JPanel askMePanel;
	private JPanel buttonLeftPanel;
	private JPanel buttonRightPanel;
	
	
	private String flightPlanFile;
	private JButton buttonBt;
	private JButton refreshBt;
	private JButton resetBt;
	private JButton landMeBt;
	private JButton askMeBt;
	private JButton landAllBt;
	
	private JEditorPane jEditorPane;
	private HTMLEditorKit kit;
	private Document doc;
	
	JScrollPane askmeScrollPan;

	public PanelFlightplan() {
		super();
	}

	public JPanel getPanel(final ManageXMLFile manageXMLFile,SelectCity selectCity,SelectMountain selectMountain, SelectVor selectVor, SelectNdb selectNdb) {
		this.manageXMLFile = manageXMLFile;
		this.selectCity = selectCity;
		this.selectMountain = selectMountain;
		this.selectVor = selectVor;
		this.selectNdb = selectNdb;
		this.flightPlanFile = "";

		return createPanel();
	}
	
	public JPanel createPanel() {
		
		distanceSpin = new DistanceSpinner();
		distanceSpin.initPanelDistances("plan");
		
		
        jEditorPane = new JEditorPane();
		jEditorPane.setEditable(false);
		kit = new HTMLEditorKit();
		jEditorPane.setEditorKit(kit);
		jEditorPane.setVisible(false);
        jEditorPane.addHyperlinkListener(new HyperlinkListener() {

            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                HyperlinkEvent.EventType type = e.getEventType();
                final URL url = e.getURL();
                if (type == HyperlinkEvent.EventType.ENTERED) {
                    // do desired highlighting
                } else if (type == HyperlinkEvent.EventType.ACTIVATED) {
                	Util.openURL(url.toString());
                }
            }
          });

		
		result = new Result();
		
		
		panelFlightplan = new JPanel();
		panelFlightplan.setLayout(null);
		
		buttonLeftPanel = new JPanel();
		buttonRightPanel = new JPanel();

		panelResult = new JPanel(new BorderLayout());
		panelResult.setBorder(new TitledBorder("Searh Result"));
		
		outputPanel = new JPanel(new BorderLayout());
		outputPanel.setBorder(new TitledBorder(""));
		
		askMePanel = new JPanel(new BorderLayout());
		askMePanel.setBorder(new TitledBorder(""));
		
		askmeScrollPan = new JScrollPane(jEditorPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		askMePanel.add(askmeScrollPan);


		result.setOutputPanel(outputPanel);
		
		buttonBt = new JButton("Select Flightplan");
		buttonBt.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  readData =  new ReadData(result, manageXMLFile, selectCity, selectMountain, selectVor, selectNdb,
		        		 new Distance((int)distanceSpin.getCitySpinner().getValue(), 
		        				 (int)distanceSpin.getMountainSpinner().getValue(), 
		        				 (int)distanceSpin.getAirportSpinner().getValue(),
		        				 (int)distanceSpin.getVorNdbSpinner().getValue(), 
		        				 distanceSpin.getCheckTocTod().isSelected(),
		        				 0.0));
    	  
	    	 flightPlanFile = new File(result.getFlightplan().getFlightplanFile()).getName();
	    	
	 		 panelResult.setBorder(new TitledBorder(flightPlanFile));
	 		 panelResult.setToolTipText((result.getFlightplan().getDescr()!= null?result.getFlightplan().getDescr():""));

	  		 panelResult.removeAll();	
			 panelResult.add(result.getFlightPlanPanel());
			 panelResult.validate();
			 
		  	 result.getWaypointListModel();

			 panelFlightplan.add(panelResult);
			 panelFlightplan.validate();

			 buttonLeftPanel.setVisible(true); 
	      }
	    });
		
		
		refreshBt = new JButton("Update");
		refreshBt.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
		    	 readData.createFlightplan(
		    			 new Distance((int)distanceSpin.getCitySpinner().getValue(), 
		    			 (int)distanceSpin.getMountainSpinner().getValue(), 
		    			 (int)distanceSpin.getAirportSpinner().getValue(),
		    			 (int)distanceSpin.getVorNdbSpinner().getValue(), 
		    			 distanceSpin.getCheckTocTod().isSelected(),
		    			 (double)result.getAltitudeModel().getValue())); 
		    	 
			  	 panelResult.removeAll();	
			  	 
				  result.getAirportListModel();


		    	 panelResult.add(result.getFlightPlanPanel());
				 panelResult.validate();
				 panelFlightplan.validate();
	      }
	    });
		
		
		
		
		resetBt = new JButton("Reset");
		resetBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				distanceSpin.getCitySpinner().setValue(0);
				distanceSpin.getAirportSpinner().setValue(0);
				distanceSpin.getVorNdbSpinner().setValue(0);
				distanceSpin.getMountainSpinner().setValue(0);
				distanceSpin.getCheckTocTod().setSelected(false);

				readData.resetResult();
				panelResult.removeAll();
				result.getFlightPlanPanel().validate();
				result.getWaypointListModel();

				panelResult.add(result.getFlightPlanPanel());

				panelResult.validate();
				panelFlightplan.validate();
			}
		});		

		
		googleBt = new JButton("Land All on Google Earth");
		googleBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				readData.createFlightplan(new Distance((int) distanceSpin.getCitySpinner().getValue(),
						(int) distanceSpin.getMountainSpinner().getValue(),
						(int) distanceSpin.getAirportSpinner().getValue(),
						(int) distanceSpin.getVorNdbSpinner().getValue(), distanceSpin.getCheckTocTod().isSelected(),
						(double) result.getAltitudeModel().getValue()));
				panelResult.removeAll();

				panelResult.add(result.getFlightPlanPanel());
				panelResult.validate();
				panelFlightplan.add(panelResult);
				panelFlightplan.validate();

				manageXMLFile.launchGoogleEarth(new File(readData.getKmlFlightPlanFile()));

			}
		});
		
		landMeBt = new JButton("Land Me");
		landMeBt.setEnabled(false);
		landMeBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(result.getCurrentView());
				System.out.println(result.getCurrentSelection());
			}
		});		

		askMeBt = new JButton("Ask Me");
		askMeBt.setEnabled(false);
		askMeBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showAskMeAnswer(result.getCurrentView());

			}
		});	
		
		landAllBt = new JButton("Land It");
		landAllBt.setEnabled(false);
		landAllBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("land All");
			}
		});		
		result.setButtons(landMeBt, askMeBt, landAllBt);

		
		buttonLeftPanel.setVisible(false); 

		buttonLeftPanel.add(refreshBt);
		buttonLeftPanel.add(resetBt);
		buttonLeftPanel.add(googleBt);
		
		buttonRightPanel.add(askMeBt);
		buttonRightPanel.add(landMeBt);
		buttonRightPanel.add(landMeBt);
		
		buttonBt.setBounds(10, 20, 200, 23);
		distanceSpin.getSpinnerPanel().setBounds(10, 50, 260, 220);

		buttonLeftPanel.setBounds(40, 280, 200, 100);
		refreshBt.setBounds(10, 270, 200, 23);
		resetBt.setBounds(10, 300, 200, 23);
		googleBt.setBounds(10, 330, 200, 23);

		landMeBt.setBounds(295, 395, 94, 23);
		askMeBt.setBounds(395, 395, 94, 23);
		landAllBt.setBounds(500, 395, 94, 23);
		buttonRightPanel.setBounds(290, 380, 300, 23);

	  	panelResult.setBounds(290, 10, 380, 240);	
	  	outputPanel.setBounds(290, 250, 380, 140);	
	  	askMePanel.setBounds(290, 250, 380, 140);	
	  	

     	panelFlightplan.add(buttonBt);
		panelFlightplan.add(panelResult);
		panelFlightplan.add(outputPanel);
		panelFlightplan.add(askMePanel);
		
		panelFlightplan.add(askMeBt);
		panelFlightplan.add(landMeBt);
		panelFlightplan.add(landAllBt);
		
		//panelFlightplan.add(buttonRightPanel);

		panelFlightplan.add(distanceSpin.getSpinnerPanel());
     	panelFlightplan.add(buttonLeftPanel);
  
		return panelFlightplan;					
	}
	
	/**
	 * 
	 * 
	 */

	private void showAskMeAnswer(String topic) {
	    if ("Ask Me".equals(askMeBt.getText())) {
			outputPanel.setVisible(false);
			jEditorPane.setVisible(true);
			askMeBt.setText("Back");
	        doc = kit.createDefaultDocument();
	        jEditorPane.setDocument(doc);
	        if ("waypoint".equals(topic)) {
	        	topic = result.panelWaypoint(result.getCurrentSelection());
	        } else if ("waypoint".equals(topic)) {
	        	topic = result.panelWaypoint(result.getCurrentSelection());
	        } else if ("airport".equals(topic)) {
	        	topic = result.panelAirport(result.getCurrentSelection());
	        }  else if ("vor".equals(topic)) {
	        	topic = result.panelVor(result.getCurrentSelection());
	        }   else if ("ndb".equals(topic)) {
	        	topic = result.panelNdb(result.getCurrentSelection());
	        }   else if ("city".equals(topic)) {
	        	topic = result.panelCity(result.getCurrentSelection());
	        }   else if ("mountain".equals(topic)) {
	        	topic = result.panelMountain(result.getCurrentSelection());
	        }  
	        	
	        jEditorPane.setText(topic);
	        javax.swing.SwingUtilities.invokeLater(new Runnable() {
	        	   public void run() { 
	        		   askmeScrollPan.getVerticalScrollBar().setValue(0);
	        	   }
	        	});
	    	
	    } else {
			outputPanel.setVisible(true);
			jEditorPane.setVisible(false);
			askMeBt.setText("Ask Me");
	    }
	
	}
	
		
	}



