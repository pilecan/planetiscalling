package com.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;

import com.cfg.common.DistanceSpinner;
import com.cfg.common.Info;
import com.model.Distance;
import com.model.Result;
import com.util.CreateKML;
import com.util.ReadData;
import com.util.Util;
import com.util.Utility;

import net.SelectAirport;
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
	private JPanel outputPanel;
	private JPanel icaoPanel;
	private JPanel resultPanel;
	private ReadData readData;

	private JScrollPane jScrollPane1;
	private SelectVor selectVor;
	private SelectNdb selectNdb;
	private SelectMountain selectMountain;
	private SelectCity selectCity;
	private DistanceSpinner distanceSpin; 
	
	
	
	private JPanel askMePanel;
	private JPanel buttonLeftPanel;
	private JPanel buttonRightPanel;

	private JButton googleBt;
	private JButton buttonBt;
	private JButton refreshBt;
	private JButton resetBt;
	private JButton landMeBt;
	private JButton askMeBt;
	private JButton landItBt;
	private JButton delMeBt;

	private JButton searchBt;

	private JEditorPane jEditorPane;
	private HTMLEditorKit kit;
	private Document doc;
	
	private JScrollPane askmeScrollPan;

	private SelectAirport selectAirport;
	
	
	public JPanel getPanel(final SelectVor selectVor,  final SelectNdb selectNdb,final SelectMountain selectMountain, final SelectCity selectCity) {
		this.selectVor = selectVor;
		this.selectNdb = selectNdb;
		this.selectCity = selectCity;
		this.selectMountain = selectMountain;
		this.selectAirport = new SelectAirport();

		
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
	
	googleBt = new JButton("Land All to Google Earth");
	googleBt.setEnabled(false);
	googleBt.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e)

		{
			//System.out.println(Utility.getInstance().getIcaoFromListModel(listIcao));
			readData.creatIcaoAirports(Utility.getInstance().getIcaoFromMapAirport(result.getMapAirport()),
					new Distance(
		    			     (int)distanceSpin.getCitySpinner().getValue(), 
	        				 (int)distanceSpin.getMountainSpinner().getValue(), 
	        				 0,
	        				 (int)distanceSpin.getVorNdbSpinner().getValue(), 
	        				 distanceSpin.getCheckLinedist().isSelected(),
	        				 0.0)
							 ); 
			setResultPanel();
			Utility.getInstance().launchGoogleEarth(new File(Utility.getInstance().getFlightPlanName(Info.kmlFlightplanName)));
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

			readData.resetIcaoResult();
			resultPanel.removeAll();
			result.getIcaoFormPanel().validate();
			result.getAirportListModel();

			resultPanel.add(result.getIcaoFormPanel());
			textArea.setText("");
			
			landItBt.setEnabled(false);
			googleBt.setEnabled(false);
			resetBt.setEnabled(false);


			resultPanel.validate();
			icaoPanel.validate();
   	  

      }
    });

    searchBt = new JButton("Search");
	searchBt.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  readData = new ReadData(textArea.getText(),result, selectVor,selectNdb, selectMountain, selectCity,
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
			 googleBt.setEnabled(true);	
			 resetBt.setEnabled(true);
			 
			 icaoPanel.validate();

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
			
			Distance dist = null;
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

	askMeBt = new JButton("Ask Me");
	askMeBt.setEnabled(false);
	askMeBt.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			result.showAskMeAnswer(outputPanel, jEditorPane, askMeBt, askmeScrollPan);
		}
	});	
		
	landItBt = new JButton("Land It");
	landItBt.setEnabled(false);
	
	landItBt.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			System.out.println("land It");
			System.out.println(result.getCurrentView());
			Distance dist = null;
			if ("airport".equals(result.getCurrentView())){
				dist = new Distance(0, 0, 1, 0, false, 0);
			} else if ("vor".equals(result.getCurrentView())){
				dist = new Distance(0, 0, 0, 1, false, 0);
			} else if ("ndb".equals(result.getCurrentView())){
				dist = new Distance(0, 0, 0, 1, false, 0);
			}else if ("city".equals(result.getCurrentView())){
				dist = new Distance(1, 0, 0, 0, false, 0);
			}else if ("mountain".equals(result.getCurrentView())){
				dist = new Distance(0, 1, 0, 0, false, 0);
			}
		   readData.saveKMLFileICAO( result.getMapAirport(), Utility.getInstance().getFlightPlanName(Info.kmlFlightplanName),dist);
		   Utility.getInstance().launchGoogleEarth(new File(Utility.getInstance().getFlightPlanName(Info.kmlFlightplanName)));
		}
	});		
	
	 delMeBt = new JButton("Del Me");
	 delMeBt.setEnabled(false);
	 delMeBt.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e)

		{
			String key;
			ListModel<?> dlm = (ListModel) result.getCurrentList().getModel();

			if (result.getCurrentList().getSelectedIndices().length > 0) {
				int[] tmp = result.getCurrentList().getSelectedIndices();
				int[] selectedIndices = result.getCurrentList().getSelectedIndices();

				for (int i = tmp.length - 1; i >= 0; i--) {
					selectedIndices = result.getCurrentList().getSelectedIndices();
				     key = Utility.getInstance().findKeyICAO( (String) result.getCurrentList().getSelectedValue());

					result.getMapAirport().remove(key);
					((DefaultListModel)result.getCurrentList().getModel()).remove(selectedIndices[i]);

				}
			}
			result.getCurrentList().clearSelection();

			//result.setAirports(listIcao.getModel().getSize());
			setResultPanel();

		}
	});
	
    result.setButtons(delMeBt,landMeBt, askMeBt, landItBt);


	inputIcaoPanel.setBounds(10, 8, 270, 120);
	resultPanel.setBounds(290, 8, 300, 139);
  	outputPanel.setBounds(290, 145, 300, 149);	
  	askMePanel.setBounds(290, 145, 300, 149);	

	distanceSpin.getSpinnerPanel().setBounds(10, 130, 280, 190);
    searchBt.setBounds(10, 330, 125, 23);
	resetBt.setBounds(150, 330, 125, 23);
	delMeBt.setBounds(290, 290, 70, 23);
	askMeBt.setBounds(365, 290, 70, 23);
	landMeBt.setBounds(441, 290, 75, 23);
	landItBt.setBounds(521, 290, 70, 23);
	googleBt.setBounds(100, 360, 160, 23);
	
	icaoPanel.add(inputIcaoPanel);
	icaoPanel.add(distanceSpin.getSpinnerPanel());
	icaoPanel.add(outputPanel);
	icaoPanel.add(resultPanel);
	icaoPanel.add(askMePanel);
	icaoPanel.add(searchBt);
	icaoPanel.add(resetBt);
	icaoPanel.add(delMeBt);
	icaoPanel.add(landMeBt);
	icaoPanel.add(askMeBt);
	icaoPanel.add(landItBt);
	icaoPanel.add(googleBt);
	
	return icaoPanel;
	}

	/**
	 * 
	 */
	private void setResultPanel() {
		resultPanel.removeAll();
		resultPanel.add(result.getIcaoFormPanel());
		resultPanel.setVisible(true);
		resultPanel.validate();

		icaoPanel.add(resultPanel);
	}

	
}
