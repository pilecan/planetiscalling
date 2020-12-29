package com.model;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
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

import com.cfg.common.Info;
import com.cfg.model.LegPoint;
import com.cfg.model.Placemark;
import com.cfg.util.FormUtility;

public class Result implements Info {
	private long distance;
	private long altitude;
	private int vors;
	private int ndbs;
	private int airports;
	private int cities;
	private int mountains;
	private String departure;
	private String destination;

	private SpinnerModel altitudeModel;
	public JSpinner altitudeSpinner;
	private JPanel panelAltitude;
	private JPanel resultPanel;
	private FormUtility formUtility;
	private JPanel panel;
	private JLabel label;
	private List<Placemark> placemarks;

	private SortedListModel listResultModel;
	
	private Map<String, Placemark> selectedAirports ;
	private Map<String, City> selectedCities ;
	private Map<String, Mountain> selectedMountains ;
	private Map<String, Placemark> addonPlacemarks ;
	private Map<Integer, Vor> selectedVors;
	private Map<Integer, Ndb> selectedNdbs;
	private LinkedList<LegPoint> legPoints;
	private String flightPlanFile;
	private Flightplan flightplan;
	
	private JList list;
	private ListModel listModel;
	private JPanel outputPanel;
	
    JRadioButton waypointBtn;
    JRadioButton airportBtn;
    JRadioButton vorBtn;
    JRadioButton ndbBtn;
    JRadioButton cityBtn;
    JRadioButton mountainBtn;


	

	public Result() {
		super();
		waypointBtn   = new JRadioButton("", true);
		airportBtn    = new JRadioButton();
		vorBtn        = new JRadioButton();
		ndbBtn        = new JRadioButton("");
		cityBtn       = new JRadioButton("");
		mountainBtn   = new JRadioButton("");

	}


	public JPanel getFlightPlanPanel() {
		panelAltitude = new JPanel();
		resultPanel = new JPanel();
		resultPanel.setLayout(new GridBagLayout());

		JTextField textField = new JTextField();

		formUtility = new FormUtility();

		formUtility.addLabel("Title:", resultPanel, colorForground[0], fontText);
		label = new JLabel(this.flightplan.getTitle());
		label.setToolTipText(this.flightplan.getDepartureName()+"/"+this.flightplan.getDestinationName());
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(label, BorderLayout.WEST);
		formUtility.addLastField(panel, resultPanel);


		formUtility.addLabel("Distance:", resultPanel, colorForground[0], fontText);
		label = new JLabel(this.distance + " nm");
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(label, BorderLayout.WEST);
		formUtility.addLastField(panel, resultPanel);

		altitudeModel = new SpinnerNumberModel(altitude, 500, // min
				60000, // max
				500);

		altitudeSpinner = new JSpinner(altitudeModel);
		altitudeSpinner.setPreferredSize(new Dimension(80, 25));
		altitudeSpinner.setToolTipText("Change altitude of flightplan");
		formUtility.addLabel("Altitude:", resultPanel, colorForground[0], fontText);
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

        waypointBtn   = new JRadioButton("", true);
        airportBtn    = new JRadioButton();
        vorBtn = new JRadioButton();
        ndbBtn   = new JRadioButton("");
        cityBtn = new JRadioButton("");
        mountainBtn = new JRadioButton("");
        
        bgroup.add(waypointBtn);
        bgroup.add(airportBtn);
        bgroup.add(vorBtn);
        bgroup.add(ndbBtn);
        bgroup.add(cityBtn);
        bgroup.add(mountainBtn);
        
        // Register a listener for the radio buttons.
        RadioListener myListener = new RadioListener();
        waypointBtn.addActionListener(myListener);
        airportBtn.addActionListener(myListener);
        vorBtn.addActionListener(myListener);
        ndbBtn.addActionListener(myListener);
        cityBtn.addActionListener(myListener);
        mountainBtn.addActionListener(myListener);
        
		setformLine(resultPanel, "Waypoints:", this.legPoints.size(),waypointBtn);
		setformLine(resultPanel, "Airports:", this.airports,airportBtn);
		setformLine(resultPanel, "VORs:", this.vors,vorBtn);
		setformLine(resultPanel, "NDBs:", this.ndbs,ndbBtn);
		setformLine(resultPanel, "Cities:", this.cities,cityBtn);
		setformLine(resultPanel, "Mountains:", this.mountains,mountainBtn);
		
	

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
        	}else if (e.getSource() == vorBtn) {
        		getVorListModel();
        	}else if (e.getSource() == ndbBtn) {
        		geNdbListModel();
        	}else if (e.getSource() == cityBtn) {
        		getCityListModel();
        	}else if (e.getSource() == mountainBtn) {
        		getMountainListModel();
        	}
            
        }
    }
	
	private void setformLine(JPanel resultPanel, String strLabel,int number,  JRadioButton radioBtn) {
	
		formUtility.addLabel(strLabel+"                         ", resultPanel, colorForground[0], fontText);
		label = new JLabel(String.format("%03d", number)+"                 ");
 
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(label, BorderLayout.WEST);
		panel.add(radioBtn);
		formUtility.addLastField(panel, resultPanel);
	
	}
	
	public JPanel getIcaoPanel() {
		resultPanel = new JPanel();
		resultPanel.setLayout(new GridBagLayout());
		
		formUtility = new FormUtility();
		
        ButtonGroup bgroup = new ButtonGroup();
        
        airportBtn    = new JRadioButton("", true);
        vorBtn = new JRadioButton();
        ndbBtn   = new JRadioButton("");
        cityBtn = new JRadioButton("");
        mountainBtn = new JRadioButton("");
        
        bgroup.add(airportBtn);
        bgroup.add(vorBtn);
        bgroup.add(ndbBtn);
        bgroup.add(cityBtn);
        bgroup.add(mountainBtn);
         
		setformLine(resultPanel, "Airports:", this.airports,airportBtn);
		setformLine(resultPanel, "VORs:", this.vors,vorBtn);
		setformLine(resultPanel, "NDBs:", this.ndbs,ndbBtn);
		setformLine(resultPanel, "Cities:", this.cities,cityBtn);
		setformLine(resultPanel, "Mountains:", this.mountains,mountainBtn);

	
		resultPanel.validate();
		
		
		return resultPanel;

	}
	
	public SortedListModel  getListIcaoModel() {
		listResultModel = new SortedListModel();
		String info = "";
		for (int i = 0; i < placemarks.size(); i++) {
			try {
				info = placemarks.get(i).getDescription().substring(placemarks.get(i).getDescription().indexOf("?q=") + 3,
						placemarks.get(i).getDescription().indexOf("+wikipedia"));
				listResultModel.add(info.replace("+", " "));
			} catch (StringIndexOutOfBoundsException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
		

		return listResultModel;
	}
	
	public void getWaypointListModel() {
		listModel = new  DefaultListModel<String>();
		String info = "";
		for (int i = 0; i < legPoints.size(); i++) {
			info = legPoints.get(i).getId()+"   "+legPoints.get(i).getPosition();
			((DefaultListModel) listModel).addElement(info);
		}
		
		showList((DefaultListModel<String>) listModel);
		
	}
	public void getAirportListModel() {
		listModel = new  DefaultListModel<String>();
		String info = "";
		
		for (Placemark placemark : selectedAirports.values()) {
			info = placemark.getDescription().substring(placemark.getDescription().indexOf("?q=") + 3,
					placemark.getDescription().indexOf("+wikipedia"));
			info = info.replaceAll("\\+", "  ");
			((DefaultListModel) listModel).addElement(info);
		}

		showList((DefaultListModel<String>) listModel);
		
	}
	
	public void getVorListModel() {
		listModel = new  DefaultListModel<String>();
		String info = "";
		
		for (Vor vor: selectedVors.values()) {
			String cap = vor.getName();
			 cap = cap.substring(0,1)+cap.substring(1).toLowerCase();

			info = vor.getIdent()+"   "+cap+"-Region:"+vor.getRegion()+"-Type : "+vor.getType();
			((DefaultListModel) listModel).addElement(info);
		}

		showList((DefaultListModel<String>) listModel);
		
	}

	public void geNdbListModel() {
		listModel = new  DefaultListModel<String>();
		String info = "";
		
		for (Ndb ndb: selectedNdbs.values()) {
			String cap = ndb.getName();
			 cap = cap.substring(0,1)+cap.substring(1).toLowerCase();
			info = ndb.getIdent()+"   "+cap+"-Region:"+ndb.getRegion()+"-Type: "+ndb.getType();
			((DefaultListModel) listModel).addElement(info);
		}

		showList((DefaultListModel<String>) listModel);
		
	}
	
	public void getCityListModel() {
		listModel = new  DefaultListModel<String>();
		String info = "";
		
		for (City city: selectedCities.values()) {
			info = city.getCityName()+"   Country: "+city.getCountry()+"-Population: "+city.getPopulation()+"-Region:"+city.getRegion();
			((DefaultListModel) listModel).addElement(info);
		}

		showList((DefaultListModel<String>) listModel);
		
	}
	
	public void getMountainListModel() {
		listModel = new  DefaultListModel<String>();
		String info = "";
		
		for (Mountain mountains: selectedMountains.values()) {
			info = mountains.getName()+"   Country: "+mountains.getCountry()+"-Elevation: "+mountains.getElevation();
			((DefaultListModel) listModel).addElement(info);
		}

		showList((DefaultListModel<String>) listModel);
		
	}

	
	/**
	 * 
	 * @param listModel
	 */
	public void showList(DefaultListModel<String>  listModel) {

		 outputPanel.setVisible(true);
	     outputPanel.removeAll();	
		 list = new JList(listModel);
		 

		 list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		 list.setVisibleRowCount(5);
		 
		 list.addListSelectionListener(new ListSelectionListener() {
			  /**
			  * {@inheritDoc}
			  */
			  @Override
			  public void valueChanged(ListSelectionEvent e) {
				 // deleteButton.setEnabled(!listIcao.isSelectionEmpty());
			  }
			});
	
		 outputPanel.add(new JScrollPane(list, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		
		  outputPanel.validate();

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

	public int getVors() {
		return vors;
	}

	public void setVors(int vors) {
		this.vors = vors;
	}

	public int getNdbs() {
		return ndbs;
	}

	public void setNdbs(int ndbs) {
		this.ndbs = ndbs;
	}

	public int getAirports() {
		return airports;
	}

	public void setAirports(int airports) {
		this.airports = airports;
	}

	public int getCities() {
		return cities;
	}

	public void setCities(int cities) {
		this.cities = cities;
	}

	public int getMountains() {
		return mountains;
	}

	public void setMountains(int mountains) {
		this.mountains = mountains;
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

	@Override
	public String toString() {
		return "Result [flightplan=" + flightplan + ", distance=" + distance + ", altitude=" + altitude + ", vors="
				+ vors + ", ndbs=" + ndbs + ", airports=" + airports + ", cities=" + cities + ", mountains=" + mountains
				+ ", departure=" + departure + ", destination=" + destination + "]";
	}

	public void setListAirport(List<Placemark> placemarks) {

		this.placemarks = placemarks;

	}

	public Map<String, Placemark> getSelectedAirports() {
		return selectedAirports;
	}

	public void setSelectedAirports(Map<String, Placemark> selectedAirports) {
		this.selectedAirports = selectedAirports;
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

	public Map<String, Placemark> getAddonPlacemarks() {
		return addonPlacemarks;
	}

	public void setAddonPlacemarks(Map<String, Placemark> addonPlacemarks) {
		this.addonPlacemarks = addonPlacemarks;
	}

	public LinkedList<LegPoint> getLegPoints() {
		return legPoints;
	}

	public void setLegPoints(LinkedList<LegPoint> legPoints) {
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


	public List<Placemark> getPlacemarks() {
		return placemarks;
	}


	public void setPlacemarks(List<Placemark> placemarks) {
		this.placemarks = placemarks;
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

	
}
