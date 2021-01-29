package com.main.form;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;

import com.backend.CreateKML;
import com.cfg.common.Info;
import com.db.SelectAirport;
import com.geo.util.Geoinfo;
import com.model.Airport;
import com.model.City;
import com.model.Flightplan;
import com.model.Landmark;
import com.model.LegPoint;
import com.model.Mountain;
import com.model.Ndb;
import com.model.SortedListModel;
import com.model.Vor;
import com.util.FormUtility;
import com.util.Util;
import com.util.Utility;

import net.metar.UtilityMetar;
import net.weather.UtilityWeather;

public class Result implements Info {
	private long distance;
	private long altitude;
	private String departure;
	private String destination;
	private String metar;
	
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
	private JCheckBox checkAltitude;

	private JEditorPane jEditorPane;
	private JScrollPane askmeScrollPan;
	private JPanel askMePanel;

	private SortedListModel listResultModel;

	private Map<String, Airport> selectedMapAirports;

	private Map<String, City> selectedCities;
	private Map<String, Mountain> selectedMountains;
	private Map<Integer, Vor> selectedVors;
	private Map<Integer, Ndb> selectedNdbs;
	private LinkedList<LegPoint> legPoints;
	private Map<String, Airport> mapAirport;
	
	private Map<String, Landmark> selectedLandmarks;

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
	private JRadioButton landmarkBtn;

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
		askMeBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ("Ask Me".equals(askMeBt.getText())) {
					 resultPanel.setVisible(false);
					showAskMeAnswer();
					 askMePanel.setVisible(true);
			} else {
				  askMeBt.setText("Ask Me");
				  askMePanel.setVisible(false);
				  resultPanel.setVisible(true);
				}

			}
		});	
	}

	public void setButtons(JButton delMeBt, JButton landMeBt, JButton askMe) {
		this.leftBtn = landMeBt;
		this.askMeBt = askMe;
		this.delMeBt = delMeBt;
		askMeBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ("Ask Me".equals(askMeBt.getText())) {
					resultPanel.setVisible(false);
					 askMePanel.setVisible(true);
					showAskMeAnswer();
				} else {
				  askMeBt.setText("Ask Me");
				  askMePanel.setVisible(false);
				  resultPanel.setVisible(true);
				}

			}
		});	
	}

	private JPanel setBorderPanel(JPanel resultPanel) {
		resultPanel = new JPanel();
		resultPanel.setLayout(new GridBagLayout());
		
	     Border border = resultPanel.getBorder();
		 Border margin = new EmptyBorder(10,10,10,10);
		 resultPanel.setBorder(new CompoundBorder(border, margin));
		 
		 return resultPanel;
		
	}
	
	public JPanel getFlightPlanFormPanel() {
		panelAltitude = new JPanel();
		
		
		resultPanel = setBorderPanel(resultPanel);

		checkAltitude = new JCheckBox();
		checkAltitude.setText("?");
		//checkAltitude.setToolTipText("Change Flight Plan Altitude");

		formUtility = new FormUtility();

		formUtility.addLabel("Title:", resultPanel);
		label = new JLabel(this.flightplan.getTitle());
	//	label.setToolTipText(this.flightplan.getDepartureName() + "/" + this.flightplan.getDestinationName());
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
		altitudeSpinner.setPreferredSize(new Dimension(65, 25));
	//	altitudeSpinner.setToolTipText("Change KML Altitude");
		formUtility.addLabel("Altitude:", resultPanel);
		JPanel panelAltitude = new JPanel();
		panelAltitude.setLayout(new BorderLayout());
		panelAltitude.add(checkAltitude, BorderLayout.EAST);
		panelAltitude.add(altitudeSpinner, BorderLayout.WEST);

		formUtility.addLastField(panelAltitude, resultPanel);

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
			initButtons() ;
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
			} else if (e.getSource() == landmarkBtn) {
				 getLandmarkListModel();
			}

		}
	}

	private void setformLine(JPanel resultPanel, String strLabel, int number, JRadioButton radioBtn) {

		formUtility.addLabel(strLabel + "                            ", resultPanel);
		label = new JLabel(String.format("%03d", number) + "                                    ");

		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(label, BorderLayout.WEST);
		panel.add(radioBtn);
		formUtility.addLastField(panel, resultPanel);

	}
	
	private void initButtons() {
		askMeBt.setEnabled(false);
		askMeBt.setText("Ask Me");
		if (delMeBt != null) {
			delMeBt.setEnabled(false);
			delMeBt.setText("Del Me");

		}
		
	}

	public JPanel getIcaoFormPanel() {
		resultPanel = setBorderPanel(resultPanel);

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
		resultPanel = setBorderPanel(resultPanel);

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
		resultPanel = setBorderPanel(resultPanel);

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
		resultPanel = setBorderPanel(resultPanel);

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

	public JPanel getLandmarkFormPanel() {
		resultPanel = setBorderPanel(resultPanel);

		formUtility = new FormUtility();

		ButtonGroup bgroup = new ButtonGroup();

		landmarkBtn = new JRadioButton("", true);
		mountainBtn = new JRadioButton("");
		cityBtn = new JRadioButton("");
		airportBtn = new JRadioButton("");

		bgroup.add(landmarkBtn);
		bgroup.add(airportBtn);
		bgroup.add(mountainBtn);
		bgroup.add(cityBtn);

		RadioListener myListener = new RadioListener();
		landmarkBtn.addActionListener(myListener);
		airportBtn.addActionListener(myListener);
		cityBtn.addActionListener(myListener);
		mountainBtn.addActionListener(myListener);

	    setformLine(resultPanel, "Landmarks:", this.selectedLandmarks.size(), landmarkBtn);
	    setformLine(resultPanel, "Airports:", this.selectedMapAirports.size(), airportBtn);
		setformLine(resultPanel, "Mountains:", this.selectedMountains.size(), mountainBtn);
      	setformLine(resultPanel, "Cities:", this.selectedCities.size(), cityBtn);
	
		resultPanel.validate();
		return resultPanel;

	}



	public void getAirportListModel() {
		listModel = new DefaultListModel<String>();
	//	UtilityMetar.getInstance().validMetar();
		outputPanel.setBorder(new TitledBorder("Airports ("+mapAirport.size()+")"));

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
		Double distance = 0.0;
		Double distanceCumul = 0.0;
		builder = new StringBuilder();

		outputPanel.setBorder(new TitledBorder("Waypoints ("+legPoints.size()+")"));
		for (int i = 0; i < legPoints.size(); i++) {
			builder = new StringBuilder();
			if (i < legPoints.size() - 1) {

				if ("1".equals(legPoints.get(i).getVisible())){
					distance = Geoinfo.distance(legPoints.get(i).getLaty(), legPoints.get(i).getLonx(),legPoints.get(i + 1).getLaty(), legPoints.get(i + 1).getLonx(), 'N');
					distanceCumul += distance;
					builder = Utility.getInstance().buildLine(legPoints.get(i).getId(), Math.round(distance), Math.round(distanceCumul));
				}
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

		outputPanel.setBorder(new TitledBorder("VORs ("+selectedVors.size()+")"));
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
		outputPanel.setBorder(new TitledBorder("VORs ("+selectedNdbs.size()+")"));

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

		outputPanel.setBorder(new TitledBorder("Cities ("+selectedCities.size()+")"));
		for (City city : selectedCities.values()) {
			builder = Utility.getInstance().buildLine(city.getCityName() , city.getCountry(), city.getPopulation());
			((DefaultListModel) listModel).addElement(builder.toString());
		}

		currentView = "city";
		showList((DefaultListModel<String>) listModel);

	}

	public void getMountainListModel() {
		listModel = new DefaultListModel<String>();
		outputPanel.setBorder(new TitledBorder("Mountains ("+selectedMountains.size()+")"));

		for (Mountain mountain : selectedMountains.values()) {
			//info = mountains.getName() + "   " + mountains.getCountry();
			builder = Utility.getInstance().buildLine(mountain.getName() , mountain.getCountry(), mountain.getElevation());

			((DefaultListModel) listModel).addElement(builder.toString());
		}

		currentView = "mountain";
		showList((DefaultListModel<String>) listModel);

	}
	public void getLandmarkListModel() {
		listModel = new DefaultListModel<String>();
		outputPanel.setBorder(new TitledBorder("Landmarks ("+selectedLandmarks.size()+")"));

		for (Landmark landmark: selectedLandmarks.values()) {
			//info = mountains.getName() + "   " + mountains.getCountry();
			builder = Utility.getInstance().buildLine(landmark.getGeoName() , landmark.getGeoTerm(), landmark.getCode());

			((DefaultListModel) listModel).addElement(builder.toString());
		}

		currentView = "landmark";
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
		if (leftBtn != null) {
			this.leftBtn.setEnabled(false);
		}
		
		this.askMeBt.setEnabled(false);

		currentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		MouseListener mouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				 resultPanel.setVisible(false);
				showAskMeAnswer();
				if (e.getClickCount() == 2) {
					currentSelection = (String) currentList.getSelectedValue();
					//showAskMeAnswer();
				//askMeBt.doClick();

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
					askMeBt.setText("Close Me");
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

	public String panelWaypoint(String icao) {
		String htmlString = null;
		for (int i = 0; i < legPoints.size(); i++) {
			
			icao = Utility.getInstance().findKeyICAO(icao);

			if (legPoints.get(i).toString().contains(icao)) {
				htmlString = legPoints.get(i).getHmlString();
				if ("Airport".equals(legPoints.get(i).getType())) {
					htmlString = panelAirport(legPoints.get(i).getIcaoIdent());
				} else {
					try {
						UtilityWeather.getInstance().callOpenweathermapObject(legPoints.get(i));
						htmlString += UtilityWeather.getInstance().getWeather().htmlData();
					} catch (Exception e) {
					}
				} 

				break;
			}
		}

		return htmlString;

	}

	public String panelAirport(String icao) {
		String varStr = null;
		icao = Utility.getInstance().findKeyICAO(icao);
		
		Airport airport;
		try {
			airport = selectedMapAirports.get(icao);
		} catch (NullPointerException e1) {
			airport = mapAirport.get(icao);
		}

		metar = UtilityMetar.getInstance().getDecodedMetar(icao);
		boolean isMetarVadid = (UtilityMetar.getInstance().getDecodedMetar(icao) != null && (!metar.contains("error")));
		
		try {
			varStr = "<b>" + airport.getIdent() + " " + airport.getName() + "</b><br>";
			varStr += airport.getDescriptionNoWeather().replaceAll("\\|", "<br>");
			
			if (isMetarVadid) {
				UtilityWeather.getInstance().callOpenweathermapObject(airport);
				varStr += metar+"<!--"+UtilityWeather.getInstance().getWeather().getIcon()+"-->";
			} else {
				UtilityWeather.getInstance().callOpenweathermapObject(airport);
				varStr += UtilityWeather.getInstance().getWeather().htmlData();
			}
		} catch (Exception e) {
		}
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
		String htmlString = "";
		line = Utility.getInstance().findKeyICAO(line);

		for (City city : selectedCities.values()) {
			if (city.getCityName().equals(line)) {
				htmlString += CreateKML.buildCityDescriptionPlane(city).replaceAll("12px", "10px").replaceAll("\\|", "<br>");
				UtilityWeather.getInstance().searchCityWeather(city);
				htmlString += UtilityWeather.getInstance().getWeather().htmlData();
			}
		}

		return htmlString;

	}

	public String panelMountain(String line) {
		String htmlString = null;
		line = Utility.getInstance().findKeyICAO(line);

		for (Mountain mountain : selectedMountains.values()) {
			if (mountain.getName().equals(line)) {
				htmlString = CreateKML.buildMountainDescriptionNoWeather(mountain).replaceAll("12px", "10px").replaceAll("\\|","<br>");
				try {
					UtilityWeather.getInstance().callOpenweathermapObject(mountain);
					htmlString += UtilityWeather.getInstance().getWeather().htmlData();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}

			}
		}

		return htmlString;

	}

	public String panelLandmark(String line) {
		String htmlString = null;
		line = Utility.getInstance().findKeyICAO(line);

		for (Landmark landmark: selectedLandmarks.values()) {
			if (landmark.getGeoName().equals(line)) {
				htmlString = CreateKML.buildLandmarkDescriptionPlane(landmark).replaceAll("12px", "10px").replaceAll("\\|","<br>");
				try {
					UtilityWeather.getInstance().callOpenweathermapObject(landmark);
					htmlString += UtilityWeather.getInstance().getWeather().htmlData();
				} catch (Exception e) {
				}

			}
		}

		return htmlString;

	}


	public void showAskMeAnswer() {
		String content = null;
		jEditorPane.setVisible(true);
		askMePanel.setVisible(true);
		askMeBt.setText("Close Me");
		
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
        }   else if ("landmark".equals(getCurrentView())) {
        	content = panelLandmark(getCurrentSelection());
        }  
        	
       jEditorPane.setText(content);
        showPanelInfo(content);

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
     	   public void run() { 
     		   askmeScrollPan.getVerticalScrollBar().setValue(0);
     	   }
     	});
	
	}
	
	
		public void showPanelInfo( String content) {
			kit = new HTMLEditorKit();
			doc = kit.createDefaultDocument();
	        jEditorPane.setDocument(doc);
	    	
			jEditorPane.setVisible(true);
			
		//	resultPanel.setVisible(false);
			
			String image = "";
			try {
				//System.out.println("image ->"+content.substring(content.indexOf("<!--")+4,content.indexOf("-->")));
				image = content.substring(content.indexOf("<!--")+4,content.indexOf("-->"));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			}
			
			if ("".equals(image)) {
				System.out.println();
				image = "01n";
			}
			
	    	URL url = null;
			try {
				File background = new File(Info.weatherPath+image+"@2x.png");
				//url = background.toURI().toURL();
				url = background.getCanonicalFile().toURI().toURL();
			} catch (IOException e) {
			}
			
			
			String urlString = url.toString().replace("\\", "/");

			
			String setColor[] = {"#E6E6E6","ff0000"};
			
	        Random rand = new Random(); 
	        int rand_int1 = rand.nextInt(setColor.length); 
			String color = setColor[0];

			try {
            	//jEditorPane.setToolTipText("Control A to Higthlight Text");
				jEditorPane.setContentType("text/html");
				jEditorPane.setText("<html><body style='font-weight: bold; color: #"+color+"; background-position: 200px 0; background-image: url(" + urlString + "); background-repeat: no-repeat;'>"+content+"</body></html>");
	            jEditorPane.validate();;

			} catch (Exception e) {
				e.printStackTrace();
			}
 			
 	       // jEditorPane.setText(content);
	        javax.swing.SwingUtilities.invokeLater(new Runnable() {
	        	   public void run() { 
	        		   askmeScrollPan.getVerticalScrollBar().setValue(0);
	        	   }
	        	});

	    
	    
	
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
	     Border border = outputPanel.getBorder();
		 Border margin = new EmptyBorder(5,5,5,5);
		 outputPanel.setBorder(new CompoundBorder(border, margin));

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


	public void setjEditorPane(JEditorPane jEditorPane) {
		this.jEditorPane = jEditorPane;
	}


	public void setAskmeScrollPan(JScrollPane askmeScrollPan) {
		this.askmeScrollPan = askmeScrollPan;
	}


	public void setAskMePanel(JPanel askMePanel) {
		this.askMePanel = askMePanel;
	}


	public Map<String, Landmark> getSelectedLandmarks() {
		return selectedLandmarks;
	}


	public void setSelectedLandmarks(Map<String, Landmark> selectedLandmarks) {
		this.selectedLandmarks = selectedLandmarks;
	}

}
