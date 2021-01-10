package com.model;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;

import com.cfg.common.Info;
import com.geo.util.Geoinfo;
import com.util.CreateKML;
import com.util.FormUtility;
import com.util.Util;
import com.util.Utility;

import net.SelectAirport;

public class Result implements Info {
	private long distance;
	private long altitude;
	private String departure;
	private String destination;

	private static Document doc;
	private static HTMLEditorKit kit;
	
	private SelectAirport selectAiport;

	private SpinnerModel altitudeModel;
	public JSpinner altitudeSpinner;
	private JPanel panelAltitude;
	private JPanel resultPanel;
	private FormUtility formUtility;
	private JPanel panel;
	private JLabel label;

	private SortedListModel listResultModel;

	private Map<String, Airport> selectedMapAirports;

	private Map<String, City> selectedCities;
	private Map<String, Mountain> selectedMountains;
	private Map<Integer, Vor> selectedVors;
	private Map<Integer, Ndb> selectedNdbs;
	private LinkedList<LegPoint> legPoints;
	private Map<String, Airport> mapAirport;

	private StringBuilder builder;
	
	private String flightPlanFile;
	private Flightplan flightplan;

	private JList currentList;
	private ListModel listModel;
	private JPanel outputPanel;
	private String currentView;
	private String currentSelection;

	private JRadioButton waypointBtn;
	private JRadioButton airportBtn;
	private JRadioButton vorBtn;
	private JRadioButton ndbBtn;
	private JRadioButton cityBtn;
	private JRadioButton mountainBtn;

	private JButton leftBtn;
	private JButton askMeBt;
	private JButton landAllBt;
	private JButton delMeBt;

	public Result() {
		super();
		waypointBtn = new JRadioButton("", true);
		airportBtn = new JRadioButton();
		vorBtn = new JRadioButton();
		ndbBtn = new JRadioButton("");
		cityBtn = new JRadioButton("");
		mountainBtn = new JRadioButton("");
		selectAiport = new SelectAirport();

	}

	public void setButtons(JButton landMeBt, JButton askMe) {
		this.leftBtn = landMeBt;
		this.askMeBt = askMe;
	}
	
	public void setButtons(JButton landMeBt, JButton askMe, JButton landAllBt) {
		this.leftBtn = landMeBt;
		this.askMeBt = askMe;
		this.landAllBt = landAllBt;
	}

	public void setButtons(JButton delMeBt, JButton landMeBt, JButton askMe, JButton landAllBt) {
		this.leftBtn = landMeBt;
		this.askMeBt = askMe;
		this.landAllBt = landAllBt;
		this.delMeBt = delMeBt;
	}

	public JPanel getFlightPlanFormPanel() {
		panelAltitude = new JPanel();
		resultPanel = new JPanel();
		resultPanel.setLayout(new GridBagLayout());

		JTextField textField = new JTextField();

		formUtility = new FormUtility();

		formUtility.addLabel("Title:", resultPanel);
		label = new JLabel(this.flightplan.getTitle());
		label.setToolTipText(this.flightplan.getDepartureName() + "/" + this.flightplan.getDestinationName());
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(label, BorderLayout.WEST);
		formUtility.addLastField(panel, resultPanel);

		formUtility.addLabel("Distance:", resultPanel);
		label = new JLabel(this.distance + " nm");
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(label, BorderLayout.WEST);
		formUtility.addLastField(panel, resultPanel);

		altitudeModel = new SpinnerNumberModel(altitude, 500, // min
				100000, // max
				500);

		altitudeSpinner = new JSpinner(altitudeModel);
		altitudeSpinner.setPreferredSize(new Dimension(80, 25));
		altitudeSpinner.setToolTipText("Change altitude of flightplan");
		formUtility.addLabel("Altitude:", resultPanel);
		JPanel panelSpin = new JPanel();
		panelSpin.setLayout(new BorderLayout());
		panelSpin.add(altitudeSpinner, BorderLayout.WEST);
		formUtility.addLastField(panelSpin, resultPanel);

		altitudeSpinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				/*
				 * JSpinner s = (JSpinner) e.getSource();
				 * System.out.println("spiner value "+s.getValue().toString()); altitude =
				 * Double.valueOf(s.getValue().toString()).longValue();
				 */

			}
		});

		ButtonGroup bgroup = new ButtonGroup();

		waypointBtn = new JRadioButton("", true);
		airportBtn = new JRadioButton();
		vorBtn = new JRadioButton();
		ndbBtn = new JRadioButton("");
		cityBtn = new JRadioButton("");
		mountainBtn = new JRadioButton("");

		bgroup.add(waypointBtn);
		bgroup.add(airportBtn);
		bgroup.add(vorBtn);
		bgroup.add(ndbBtn);
		bgroup.add(cityBtn);
		bgroup.add(mountainBtn);

		RadioListener myListener = new RadioListener();
		waypointBtn.addActionListener(myListener);
		airportBtn.addActionListener(myListener);
		vorBtn.addActionListener(myListener);
		ndbBtn.addActionListener(myListener);
		cityBtn.addActionListener(myListener);
		mountainBtn.addActionListener(myListener);
		
		setformLine(resultPanel, "Waypoints:", this.legPoints.size(), waypointBtn);
		setformLine(resultPanel, "Airports:", this.mapAirport.size(), airportBtn);
		setformLine(resultPanel, "VORs:", this.selectedVors.size(), vorBtn);
		setformLine(resultPanel, "NDBs:", this.selectedNdbs.size(), ndbBtn);
		setformLine(resultPanel, "Cities:", this.selectedCities.size(), cityBtn);
		setformLine(resultPanel, "Mountains:", this.selectedMountains.size(), mountainBtn);

		resultPanel.validate();

		return resultPanel;
	}

	/** Listens to the radio buttons. */
	class RadioListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == waypointBtn) {
				getWaypointListModel();
			} else if (e.getSource() == airportBtn) {
				getAirportListModel();
			} else if (e.getSource() == vorBtn) {
				getVorListModel();
			} else if (e.getSource() == ndbBtn) {
				geNdbListModel();
			} else if (e.getSource() == cityBtn) {
				getCityListModel();
			} else if (e.getSource() == mountainBtn) {
				getMountainListModel();
			}

		}
	}

	private void setformLine(JPanel resultPanel, String strLabel, int number, JRadioButton radioBtn) {

		formUtility.addLabel(strLabel + "                         ", resultPanel);
		label = new JLabel(String.format("%03d", number) + "                 ");

		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(label, BorderLayout.WEST);
		panel.add(radioBtn);
		formUtility.addLastField(panel, resultPanel);

	}

	public JPanel getIcaoFormPanel() {
		resultPanel = new JPanel();
		resultPanel.setLayout(new GridBagLayout());

		formUtility = new FormUtility();

		ButtonGroup bgroup = new ButtonGroup();

		airportBtn = new JRadioButton("", true);
		vorBtn = new JRadioButton();
		ndbBtn = new JRadioButton("");
		cityBtn = new JRadioButton("");
		mountainBtn = new JRadioButton("");

		bgroup.add(airportBtn);
		bgroup.add(vorBtn);
		bgroup.add(ndbBtn);
		bgroup.add(cityBtn);
		bgroup.add(mountainBtn);

		RadioListener myListener = new RadioListener();
		airportBtn.addActionListener(myListener);
		vorBtn.addActionListener(myListener);
		ndbBtn.addActionListener(myListener);
		cityBtn.addActionListener(myListener);
		mountainBtn.addActionListener(myListener);

		setformLine(resultPanel, "Airports:", this.mapAirport.size(), airportBtn);
		setformLine(resultPanel, "VORs:", this.selectedVors.size(), vorBtn);
		setformLine(resultPanel, "NDBs:", this.selectedNdbs.size(), ndbBtn);
		setformLine(resultPanel, "Cities:", this.selectedCities.size(), cityBtn);
		setformLine(resultPanel, "Mountains:", this.selectedMountains.size(), mountainBtn);

		resultPanel.validate();
		return resultPanel;

	}
	public JPanel getAiportFormPanel() {
		resultPanel = new JPanel();
		resultPanel.setLayout(new GridBagLayout());

		formUtility = new FormUtility();

		ButtonGroup bgroup = new ButtonGroup();

		airportBtn = new JRadioButton("", true);
		vorBtn = new JRadioButton();
		ndbBtn = new JRadioButton("");
		cityBtn = new JRadioButton("");
		mountainBtn = new JRadioButton("");

		bgroup.add(airportBtn);
		bgroup.add(vorBtn);
		bgroup.add(ndbBtn);
		bgroup.add(cityBtn);
		bgroup.add(mountainBtn);

		RadioListener myListener = new RadioListener();
		airportBtn.addActionListener(myListener);
		vorBtn.addActionListener(myListener);
		ndbBtn.addActionListener(myListener);
		cityBtn.addActionListener(myListener);
		mountainBtn.addActionListener(myListener);

		setformLine(resultPanel, "Airports:", this.mapAirport.size(), airportBtn);
		setformLine(resultPanel, "VORs:", this.selectedVors.size(), vorBtn);
		setformLine(resultPanel, "NDBs:", this.selectedNdbs.size(), ndbBtn);
		setformLine(resultPanel, "Cities:", this.selectedCities.size(), cityBtn);
		setformLine(resultPanel, "Mountains:", this.selectedMountains.size(), mountainBtn);

		resultPanel.validate();
		return resultPanel;

	}
	public JPanel getCityFormPanel() {
		resultPanel = new JPanel();
		resultPanel.setLayout(new GridBagLayout());

		formUtility = new FormUtility();

		ButtonGroup bgroup = new ButtonGroup();

		cityBtn = new JRadioButton("", true);
		airportBtn = new JRadioButton("");
		vorBtn = new JRadioButton();
		ndbBtn = new JRadioButton("");
		mountainBtn = new JRadioButton("");

		bgroup.add(airportBtn);
		bgroup.add(vorBtn);
		bgroup.add(ndbBtn);
		bgroup.add(mountainBtn);
		bgroup.add(cityBtn);

		RadioListener myListener = new RadioListener();
		vorBtn.addActionListener(myListener);
		ndbBtn.addActionListener(myListener);
		airportBtn.addActionListener(myListener);
		cityBtn.addActionListener(myListener);
		mountainBtn.addActionListener(myListener);

		setformLine(resultPanel, "Cities:", this.selectedCities.size(), cityBtn);
	    setformLine(resultPanel, "Airports:", this.selectedMapAirports.size(), airportBtn);
		setformLine(resultPanel, "VORs:", this.selectedVors.size(), vorBtn);
		setformLine(resultPanel, "NDBs:", this.selectedNdbs.size(), ndbBtn);
		setformLine(resultPanel, "Mountains:", this.selectedMountains.size(), mountainBtn);

		resultPanel.validate();
		return resultPanel;

	}

	public JPanel getMountainFormPanel() {
		resultPanel = new JPanel();
		resultPanel.setLayout(new GridBagLayout());

		formUtility = new FormUtility();

		ButtonGroup bgroup = new ButtonGroup();

		mountainBtn = new JRadioButton("", true);
		cityBtn = new JRadioButton("");
		airportBtn = new JRadioButton("");
		vorBtn = new JRadioButton();
		ndbBtn = new JRadioButton("");

		bgroup.add(airportBtn);
		bgroup.add(vorBtn);
		bgroup.add(ndbBtn);
		bgroup.add(mountainBtn);
		bgroup.add(cityBtn);

		RadioListener myListener = new RadioListener();
		vorBtn.addActionListener(myListener);
		ndbBtn.addActionListener(myListener);
		airportBtn.addActionListener(myListener);
		cityBtn.addActionListener(myListener);
		mountainBtn.addActionListener(myListener);

		setformLine(resultPanel, "Mountains:", this.selectedMountains.size(), mountainBtn);
      	setformLine(resultPanel, "Cities:", this.selectedCities.size(), cityBtn);
	    setformLine(resultPanel, "Airports:", this.selectedMapAirports.size(), airportBtn);
		setformLine(resultPanel, "VORs:", this.selectedVors.size(), vorBtn);
		setformLine(resultPanel, "NDBs:", this.selectedNdbs.size(), ndbBtn);

		resultPanel.validate();
		return resultPanel;

	}



	public void getAirportListModel() {
		listModel = new DefaultListModel<String>();
		String info = "";

		for (Airport airport : mapAirport.values()) {
				builder = Utility.getInstance().buildLine(airport.getIdent() , airport.getName(), airport.getCountry());
			((DefaultListModel) listModel).addElement(builder.toString());
		}

		currentView = "airport";
		showList((DefaultListModel<String>) listModel);

	}
	public void getWaypointListModel() {
		listModel = new DefaultListModel<String>();
		Geoinfo.removeInvisiblePointAndInitialiseDist(legPoints);
		String info = "";
		Double distance = 0.0;
		Double distanceCumul = 0.0;
		builder = new StringBuilder();

		for (int i = 0; i < legPoints.size(); i++) {
			builder = new StringBuilder();
			if (i < legPoints.size() - 1) {

				distance = Geoinfo.distance(legPoints.get(i).getLaty(), legPoints.get(i).getLonx(),legPoints.get(i + 1).getLaty(), legPoints.get(i + 1).getLonx(), 'N');
				info = legPoints.get(i).getId() + "   " + Math.round(distance) + " - " + Math.round(distanceCumul)+ " - " + Math.round(legPoints.get(i).getAltitude() * 3.28084) + "ft";
				distanceCumul += distance;
				builder = Utility.getInstance().buildLine(legPoints.get(i).getId(), Math.round(distance), Math.round(distanceCumul));
			} else {
				builder = Utility.getInstance().buildLine(legPoints.get(legPoints.size() - 1).getId(), 0, Math.round(distanceCumul));

		}

			((DefaultListModel) listModel).addElement(builder.toString());

		}

		currentView = "waypoint";
		showList((DefaultListModel<String>) listModel);

	}
	
	

	public void getVorListModel() {
		listModel = new DefaultListModel<String>();
		String info = "";

		for (Vor vor : selectedVors.values()) {
			String cap = vor.getName();
			cap = cap.substring(0, 1) + cap.substring(1).toLowerCase();
			
			builder = Utility.getInstance().buildLine(vor.getIdent() , cap, Util.REGION_MAP.get(vor.getRegion()));

			((DefaultListModel) listModel).addElement(builder.toString());
		}

		currentView = "vor";
		showList((DefaultListModel<String>) listModel);

	}

	public void geNdbListModel() {
		listModel = new DefaultListModel<String>();
		String info = "";

		for (Ndb ndb : selectedNdbs.values()) {
			String cap = ndb.getName();
			cap = cap.substring(0, 1) + cap.substring(1).toLowerCase();
			builder = Utility.getInstance().buildLine(ndb.getIdent() , cap, Util.REGION_MAP.get(ndb.getRegion()));

			((DefaultListModel) listModel).addElement(builder.toString());
		}

		currentView = "ndb";
		showList((DefaultListModel<String>) listModel);

	}

	public void getCityListModel() {
		listModel = new DefaultListModel<String>();
		String info = "";

		for (City city : selectedCities.values()) {
			builder = Utility.getInstance().buildLine(city.getCityName() , city.getCountry(), city.getPopulation());
			((DefaultListModel) listModel).addElement(builder.toString());
		}

		currentView = "city";
		showList((DefaultListModel<String>) listModel);

	}

	public void getMountainListModel() {
		listModel = new DefaultListModel<String>();
		String info = "";

		for (Mountain mountain : selectedMountains.values()) {
			//info = mountains.getName() + "   " + mountains.getCountry();
			builder = Utility.getInstance().buildLine(mountain.getName() , mountain.getCountry(), mountain.getElevation());

			((DefaultListModel) listModel).addElement(builder.toString());
		}

		currentView = "mountain";
		showList((DefaultListModel<String>) listModel);

	}

	/**
	 * 
	 * @param listModel
	 */
	public void showList(DefaultListModel<String> listModel) {

		outputPanel.setVisible(true);
		outputPanel.removeAll();
		currentList = new JList(listModel);
		currentList.setFixedCellHeight(18);

		if (landAllBt != null) {
			this.landAllBt.setEnabled(true);
		}
			
		this.leftBtn.setEnabled(false);
		this.askMeBt.setEnabled(false);

		currentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		MouseListener mouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				if (e.getClickCount() == 2) {
					currentSelection = (String) currentList.getSelectedValue();
					askMeBt.doClick();

				}
			}
		};
		currentList.addMouseListener(mouseListener);

		currentList.addListSelectionListener(new ListSelectionListener() {
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void valueChanged(ListSelectionEvent evt) {
				try {
					delMeBt.setEnabled(!currentList.isSelectionEmpty());
					if (!"airport".equals(currentView)){
						delMeBt.setEnabled(false);
					}
				} catch (NullPointerException e) {
				}
				askMeBt.setEnabled(!currentList.isSelectionEmpty());

				currentSelection = (String) currentList.getSelectedValue();

				leftBtn.setEnabled(!currentList.isSelectionEmpty());
			}
		});

		outputPanel.add(new JScrollPane(currentList, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));

		outputPanel.validate();

	}

	public String panelWaypoint(String line) {
		String htmlString = null;
		for (int i = 0; i < legPoints.size(); i++) {
			
			line = Utility.getInstance().findKeyICAO(line);

			if (legPoints.get(i).toString().contains(line)) {
				htmlString = legPoints.get(i).getHmlString();
				if ("Airport".equals(legPoints.get(i).getType())) {
					htmlString = panelAirport(legPoints.get(i).getIcaoIdent());
				}

				break;
			}
		}

		return htmlString;

	}

	public String panelAirport(String line) {
		String varStr = null;
		line = Utility.getInstance().findKeyICAO(line);
		Airport airport = mapAirport.get(line);

		varStr = "<b>" + airport.getIdent() + " " + airport.getName() + "</b><br>";
		varStr += airport.getDescription().replaceAll("\\|", "<br>");
		return varStr;

	}

	public String panelVor(String line) {
		String htmlString = null;

		line = Utility.getInstance().findKeyICAO(line);

		for (Vor vor : selectedVors.values()) {
			if (vor.getIdent().equals(line)) {
				htmlString = CreateKML.buildVorDescription(vor).replaceAll("12px", "10px").replaceAll("\\|", "<br>");
			}
		}

		return htmlString;

	}

	public String panelNdb(String line) {
		String htmlString = null;
		line = Utility.getInstance().findKeyICAO(line);

		for (Ndb ndb : selectedNdbs.values()) {
			if (ndb.getIdent().equals(line)) {
				htmlString = CreateKML.buildNdbDescription(ndb).replaceAll("12px", "10px").replaceAll("\\|", "<br>");
			}
		}

		return htmlString;

	}

	public String panelCity(String line) {
		String htmlString = null;
		line = Utility.getInstance().findKeyICAO(line);

		for (City city : selectedCities.values()) {
			if (city.getCityName().equals(line)) {
				htmlString = CreateKML.buildCityDescription(city).replaceAll("12px", "10px").replaceAll("\\|", "<br>");

			}
		}

		return htmlString;

	}

	public String panelMountain(String line) {
		String htmlString = null;
		line = Utility.getInstance().findKeyICAO(line);

		for (Mountain mountain : selectedMountains.values()) {
			if (mountain.getName().equals(line)) {
				htmlString = CreateKML.buildMountainDescription(mountain).replaceAll("12px", "10px").replaceAll("\\|","<br>");

			}
		}

		return htmlString;

	}

	public void showAskMeAnswer( JPanel outputPanel, JEditorPane jEditorPane, JButton askMeBt, final JScrollPane askmeScrollPan ) {
		String content = null;
	    if ("Ask Me".equals(askMeBt.getText())) {
			outputPanel.setVisible(false);
			jEditorPane.setVisible(true);
			askMeBt.setText("Back");
			kit = new HTMLEditorKit();
			doc = kit.createDefaultDocument();
	        jEditorPane.setDocument(doc);
	        if ("waypoint".equals(getCurrentView())) {
	        	content = panelWaypoint(getCurrentSelection());
	        } else if ("waypoint".equals(getCurrentView())) {
	        	content = panelWaypoint(getCurrentSelection());
	        } else if ("airport".equals(getCurrentView())) {
	        	content = panelAirport(getCurrentSelection());
	        }  else if ("vor".equals(getCurrentView())) {
	        	content = panelVor(getCurrentSelection());
	        }   else if ("ndb".equals(getCurrentView())) {
	        	content = panelNdb(getCurrentSelection());
	        }   else if ("city".equals(getCurrentView())) {
	        	content = panelCity(getCurrentSelection());
	        }   else if ("mountain".equals(getCurrentView())) {
	        	content = panelMountain(getCurrentSelection());
	        }  
	        	
	        jEditorPane.setText(content);
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
	
	
	public long getDistance() {
		return distance;
	}

	public void setDistance(long distance) {
		this.distance = distance;
	}

	public long getAltitude() {
		return altitude;
	}

	public void setAltitude(long altitude) {
		this.altitude = altitude;
	}

	public SpinnerModel getAltitudeModel() {
		return altitudeModel;
	}

	public void setAltitudeModel(SpinnerModel altitudeModel) {
		this.altitudeModel = altitudeModel;
	}

	public JSpinner getAltitudeSpinner() {
		return altitudeSpinner;
	}

	public void setAltitudeSpinner(JSpinner altitudeSpinner) {
		this.altitudeSpinner = altitudeSpinner;
	}

	public JPanel getPanelAltitude() {
		return panelAltitude;
	}

	public void setPanelAltitude(JPanel panelAltitude) {
		this.panelAltitude = panelAltitude;
	}

	public FormUtility getFormUtility() {
		return formUtility;
	}

	public void setFormUtility(FormUtility formUtility) {
		this.formUtility = formUtility;
	}

	public String getDeparture() {
		return departure;
	}

	public void setDeparture(String departure) {
		this.departure = departure;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	

	public Map<String, City> getSelectedCities() {
		return selectedCities;
	}

	public void setSelectedCities(Map<String, City> selectedCities) {
		this.selectedCities = selectedCities;
	}

	public Map<String, Mountain> getSelectedMountains() {
		return selectedMountains;
	}

	public void setSelectedMountains(Map<String, Mountain> selectedMountains) {
		this.selectedMountains = selectedMountains;
	}

	public Map<Integer, Vor> getSelectedVors() {
		return selectedVors;
	}

	public void setSelectedVors(Map<Integer, Vor> selectedVors) {
		this.selectedVors = selectedVors;
	}

	public Map<Integer, Ndb> getSelectedNdbs() {
		return selectedNdbs;
	}

	public void setSelectedNdbs(Map<Integer, Ndb> selectedNdbs) {
		this.selectedNdbs = selectedNdbs;
	}


	public LinkedList<LegPoint> getLegPoints() {
		return legPoints;
	}

	public void setLegPoints(LinkedList<LegPoint> legPoints) {
		legPoints = Geoinfo.removeInvisiblePointAndInitialiseDist(legPoints);
		this.legPoints = legPoints;
	}

	public String getFlightPlanFile() {
		return flightPlanFile;
	}

	public void setFlightPlanFile(String flightPlanFile) {
		this.flightPlanFile = flightPlanFile;
	}

	public JPanel getResultPanel() {
		return resultPanel;
	}

	public void setResultPanel(JPanel resultPanel) {
		this.resultPanel = resultPanel;
	}

	public JPanel getPanel() {
		return panel;
	}

	public void setPanel(JPanel panel) {
		this.panel = panel;
	}

	public JLabel getLabel() {
		return label;
	}

	public void setLabel(JLabel label) {
		this.label = label;
	}


	public SortedListModel getListResultModel() {
		return listResultModel;
	}

	public void setListResultModel(SortedListModel listResultModel) {
		this.listResultModel = listResultModel;
	}

	public Flightplan getFlightplan() {
		return flightplan;
	}

	public void setFlightplan(Flightplan flightplan) {
		this.flightplan = flightplan;
	}

	public void setOutputPanel(JPanel outputPanel) {
		this.outputPanel = outputPanel;
	}

	public String getCurrentView() {
		return currentView;
	}

	public void setCurrentView(String currentView) {
		this.currentView = currentView;
	}

	public String getCurrentSelection() {
		return currentSelection;
	}

	public void setCurrentSelection(String currentSelection) {
		this.currentSelection = currentSelection;
	}

	

	public Map<String, Airport> getMapAirport() {
		return mapAirport;
	}

	public void setMapAirport(Map<String, Airport> mapAirport) {
		this.mapAirport = mapAirport;
	}

	public JList getCurrentList() {
		return currentList;
	}

	public void setCurrentList(JList currentList) {
		this.currentList = currentList;
	}

	public Map<String, Airport> getSelectedMapAirports() {
		return selectedMapAirports;
	}

	public void setSelectedMapAirports(Map<String, Airport> selectedMapAirports) {
		this.selectedMapAirports = selectedMapAirports;
	}

}
