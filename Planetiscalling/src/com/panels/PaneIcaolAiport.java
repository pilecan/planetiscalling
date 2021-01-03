package com.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;

import com.cfg.common.DistanceSpinner;
import com.cfg.common.Info;
import com.cfg.file.ManageXMLFile;
import com.cfg.model.Placemark;
import com.cfg.util.Util;
import com.model.Distance;
import com.model.Result;
import com.model.SortedListModel;
import com.util.ReadData;
import com.util.Utility;

import net.SelectCity;
import net.SelectMountain;
import net.SelectNdb;
import net.SelectVor;

public class PaneIcaolAiport extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Result result;
	private ManageXMLFile manageXMLFile;
	private JPanel outputPanel;
	private JPanel icaoPanel;
	private JPanel resultPanel;
	private ReadData readData;

	private List<Placemark> airports ;
	private JScrollPane jScrollPane1;
	private SelectVor selectVor;
	private SelectNdb selectNdb;
	private SelectMountain selectMountain;
	private SelectCity selectCity;
	private DistanceSpinner distanceSpin; 
	private JList listIcao;
	private SortedListModel listIcaoModel;
	
	
	private JButton googleButton;
	
	private JPanel askMePanel;
	private JPanel buttonLeftPanel;
	private JPanel buttonRightPanel;

	private JButton buttonBt;
	private JButton refreshBt;
	private JButton resetBt;
	private JButton landMeBt;
	private JButton askMeBt;
	private JButton landItBt;
	private JButton delMeBt;

	
	private JEditorPane jEditorPane;
	private HTMLEditorKit kit;
	private Document doc;
	
	JScrollPane askmeScrollPan;

	
	
	
	public JPanel getPanel(final ManageXMLFile manageXMLFile,  final SelectVor selectVor,  final SelectNdb selectNdb,final SelectMountain selectMountain, final SelectCity selectCity) {
		this.setManageXMLFile(manageXMLFile);
		this.selectVor = selectVor;
		this.selectNdb = selectNdb;
		this.selectCity = selectCity;
		this.selectMountain = selectMountain;
		
		distanceSpin = new DistanceSpinner();
		distanceSpin.initPanelDistances("icao");

		
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
		
		landItBt = new JButton("Land It");
		landItBt.setEnabled(false);
		landItBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("land All");
/*	?? ->			readData.creatIcaoAirports(Utility.getInstance().getIcaoFromListModel(listIcao),
						new Distance(
			    			     (int)distanceSpin.getCitySpinner().getValue(), 
		        				 (int)distanceSpin.getMountainSpinner().getValue(), 
		        				 0,
		        				 (int)distanceSpin.getVorNdbSpinner().getValue(), 
		        				 distanceSpin.getCheckLinedist().isSelected(),
		        				 0.0)
								 ); 
*/				setResultPanel();
				manageXMLFile.launchGoogleEarth(new File(Utility.getInstance().getFlightPlanName(Info.flightplanName)));
			}
		});		
		
		 delMeBt = new JButton("Del Me");
		 delMeBt.setEnabled(false);
		 delMeBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)

			{
				SortedListModel dlm = (SortedListModel) listIcao.getModel();

				if (listIcao.getSelectedIndices().length > 0) {
					int[] tmp = listIcao.getSelectedIndices();
					int[] selectedIndices = listIcao.getSelectedIndices();

					for (int i = tmp.length - 1; i >= 0; i--) {
						selectedIndices = listIcao.getSelectedIndices();
						dlm.removeElement(selectedIndices[i]);
					}
				}

				listIcao.clearSelection();

				//result.setAirports(listIcao.getModel().getSize());
				setResultPanel();

			}
		});
		
	result.setButtons(delMeBt,landMeBt, askMeBt, landItBt);

	askMePanel = new JPanel(new BorderLayout());
	askMePanel.setBorder(new TitledBorder(""));
	
	askmeScrollPan = new JScrollPane(jEditorPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
	askMePanel.add(askmeScrollPan);
	
	icaoPanel = new JPanel();
	icaoPanel.setLayout(null);

	outputPanel = new JPanel(new BorderLayout());
	outputPanel.setBorder(new TitledBorder(""));
	result.setOutputPanel(outputPanel);

	resultPanel = new JPanel(new BorderLayout());
	resultPanel.setBorder(new TitledBorder("Search Result"));
	
	googleButton = new JButton("Google Earth");
	googleButton.setEnabled(false);


	googleButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e)

		{
			//System.out.println(Utility.getInstance().getIcaoFromListModel(listIcao));
			readData.creatIcaoAirports(Utility.getInstance().getIcaoFromListModel(listIcao),
					new Distance(
		    			     (int)distanceSpin.getCitySpinner().getValue(), 
	        				 (int)distanceSpin.getMountainSpinner().getValue(), 
	        				 0,
	        				 (int)distanceSpin.getVorNdbSpinner().getValue(), 
	        				 distanceSpin.getCheckLinedist().isSelected(),
	        				 0.0)
							 ); 
			setResultPanel();
			manageXMLFile.launchGoogleEarth(new File(Utility.getInstance().getFlightPlanName(Info.flightplanName)));
		}
	});

	

	final JTextArea textArea = new JTextArea();
	textArea.setText("[oc-australia-airport-ybdg-Bendigo Airport, oc-australia-airport-ydpo-devponport, oc-australia-airport-ykbn-kooralbyn, oc-australia-airport-ymhb-hobart, oc-australia-airport-ysbk-tollrescue, oc-australia-bridge-anzac, oc-australia-city-hammerheadh-melcbdone, oc-australia-city-hammerheadh-melcbdthree, oc-australia-city-hammerheadh-melcbdtwo, oc-australia-city-NZA-Discord_Brisbane-CBD, oc-australia-city-NZA-Discord_Brisbane-Gabba, oc-australia-city-NZA-Discord_Brisbane-Gateway, oc-australia-city-NZA-Discord_Brisbane-StoreyBridge, oc-australia-city-NZA-Discord_Brisbane-Suncorp, oc-australia-city-perth, oc-australia-city-port-melbourne, oc-australia-city-sydney, oc-australia-city-sydneyharbour, oc-french-polynisia-aerial-ntam-rimatara, oc-french-polynisia-aerial-ntav-raivavae, oc-french-polynisia-aerial-ntte-tetiaroa, oc-french-polynisia-aerial-nttp-maupiti, oc-french-polynisia-airport-ntaa-papeete, oc-french-polynisia-airport-ntav-raivavae, oc-french-polynisia-airport-nttb-borabora, oc-french-polynisia-airport-ntte-tetiaroa, oc-french-polynisia-airport-nttp-maupiti, oc-new-zealand-aiprort-ARNZ-NZAA-Auckland, oc-new-zealand-aiprort-nzou-NZA-Discord_NZOU_Oamaru-Scenery-Pack-1.0, oc-new-zealand-aiprort-nzwn-ARNZ-Wellington, oc-new-zealand-airport-nzmf-milford, oc-new-zealand-airport-nznd-ARNZ-NZDN-Dunedin, oc-new-zealand-airport-nznr-napier-NZ_AUS_MFS2020_Discord, oc-new-zealand-airport-nztl-lake-tekapo, oc-new-zealand-airport-nzws-Westport-Scenery-Pack-1.0, oc-new-zealand-calvinpg-nzsupremecourt, oc-new-zealand-calvinpg-vicuni-cbd, oc-new-zealand-calvinpg-vicuni-lawschool, oc-new-zealand-calvinpg-wellingtonrailwaystation, oc-new-zealand-calvinpg-wellingtonstadium, oc-new-zealand-landmark-wellingtonparliament, oc-new-zealand-mountain-mtcook, oc-new-zealand-NZA-Discord_Auckland-AucklandHarbourBridge, oc-new-zealand-NZA-Discord_Auckland-AucklandMuseum, oc-new-zealand-NZA-Discord_Auckland-CBD01, oc-new-zealand-NZA-Discord_Auckland-CBD02, oc-new-zealand-NZA-Discord_Auckland-CBD03, oc-new-zealand-NZA-Discord_Auckland-CBD04, oc-new-zealand-NZA-Discord_Auckland-CBD05, oc-new-zealand-NZA-Discord_Auckland-CBD06, oc-new-zealand-NZA-Discord_Auckland-CBD07, oc-new-zealand-NZA-Discord_Auckland-CBD08, oc-new-zealand-NZA-Discord_Auckland-CBD09, oc-new-zealand-NZA-Discord_Auckland-CBD10, oc-new-zealand-NZA-Discord_Auckland-CBD11, oc-new-zealand-NZA-Discord_Auckland-CBD12, oc-new-zealand-NZA-Discord_Auckland-CBD13, oc-new-zealand-NZA-Discord_Auckland-CBD14, oc-new-zealand-NZA-Discord_Auckland-CBD15, oc-new-zealand-NZA-Discord_Auckland-CBD16, oc-new-zealand-NZA-Discord_Auckland-CBD17, oc-new-zealand-NZA-Discord_Auckland-CBD18, oc-new-zealand-NZA-Discord_Auckland-CBD19, oc-new-zealand-NZA-Discord_Auckland-CBD20, oc-new-zealand-NZA-Discord_Auckland-CBD21, oc-new-zealand-NZA-Discord_Auckland-CBD22, oc-new-zealand-NZA-Discord_Auckland-CBD23, oc-new-zealand-NZA-Discord_Auckland-CBD24, oc-new-zealand-NZA-Discord_Auckland-CBD25, oc-new-zealand-NZA-Discord_Auckland-CBD26, oc-new-zealand-NZA-Discord_Auckland-CBD27, oc-new-zealand-NZA-Discord_Auckland-CBD28, oc-new-zealand-NZA-Discord_Auckland-CBD29, oc-new-zealand-NZA-Discord_Auckland-CBDSkyTower, oc-new-zealand-NZA-Discord_Auckland-EdenPark, oc-new-zealand-NZA-Discord_Auckland-Port01, oc-new-zealand-NZA-Discord_Auckland-Port02, oc-new-zealand-NZA-Discord_Auckland-SparkArena, oc-papua-landmarks-bush-trip]");
	setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	textArea.setColumns(100);
	textArea.setLineWrap(true);
	textArea.setRows(1000);
	textArea.setWrapStyleWord(true);
	textArea.setPreferredSize(new Dimension(300,500));

	JPanel inputIcaoPanel = new JPanel(new BorderLayout());
	inputIcaoPanel.setBorder(new TitledBorder("Type or Paste you ICAO Codes here"));
	inputIcaoPanel.add(new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
	inputIcaoPanel.setVisible(true);
	inputIcaoPanel.setPreferredSize(new Dimension(300,150));
	
	

	JButton searchButton = new JButton("Search");
	searchButton.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  readData = new ReadData(textArea.getText(),result, manageXMLFile, selectVor,selectNdb, selectMountain, selectCity,
	    			  new Distance(
	    			     (int)distanceSpin.getCitySpinner().getValue(), 
        				 (int)distanceSpin.getMountainSpinner().getValue(), 
        				 0,
        				 (int)distanceSpin.getVorNdbSpinner().getValue(), 
        				 distanceSpin.getCheckLinedist().isSelected(),
        				 0.0));
	    	  
			  result.getAirportListModel();

	    	  
	    	//  setIcaoResult();
		  	 //result.getAirportListModel();

	    	 setResultPanel();
			 googleButton.setEnabled(true);
			 
			 icaoPanel.validate();

	      }
	    });

	JButton clearButton = new JButton("Clear");
    clearButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
 /*   	  textArea.setText("");
    	  listIcao.removeAll();
	   	  outputPanel.removeAll();	
	   	  outputPanel.setVisible(false);
	      resultPanel.removeAll();	
	      resultPanel.setVisible(false);
	      listIcaoModel.clear();

*/    	  

      }
    });
	

	inputIcaoPanel.setBounds(10, 8, 270, 120);
	resultPanel.setBounds(290, 8, 300, 139);
  	outputPanel.setBounds(290, 145, 300, 149);	
  	askMePanel.setBounds(290, 145, 300, 149);	

	distanceSpin.getSpinnerPanel().setBounds(10, 130, 280, 190);
    searchButton.setBounds(10, 330, 125, 23);
	clearButton.setBounds(150, 330, 125, 23);
	delMeBt.setBounds(290, 290, 70, 23);
	askMeBt.setBounds(365, 290, 70, 23);
	landMeBt.setBounds(441, 290, 75, 23);
	landItBt.setBounds(521, 290, 70, 23);
	googleButton.setBounds(100, 290, 130, 23);
	
	icaoPanel.add(inputIcaoPanel);
	icaoPanel.add(distanceSpin.getSpinnerPanel());
	icaoPanel.add(outputPanel);
	icaoPanel.add(resultPanel);
	icaoPanel.add(askMePanel);
	icaoPanel.add(searchButton);
	icaoPanel.add(clearButton);
	icaoPanel.add(delMeBt);
	icaoPanel.add(delMeBt);
	icaoPanel.add(askMeBt);
	icaoPanel.add(landItBt);
	icaoPanel.add(googleButton);
	
	return icaoPanel;
	}

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

	
	/**
	 * 
	 */
	private void setResultPanel() {
		resultPanel.removeAll();
		resultPanel.add(result.getIcaoPanel());
		resultPanel.setVisible(true);
		resultPanel.validate();

		icaoPanel.add(resultPanel);
	}

	
	public ManageXMLFile getManageXMLFile() {
		return manageXMLFile;
	}


	public void setManageXMLFile(ManageXMLFile manageXMLFile) {
		this.manageXMLFile = manageXMLFile;
	}
 
	 
	

}
