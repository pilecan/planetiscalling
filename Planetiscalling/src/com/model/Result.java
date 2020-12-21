package com.model;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.cfg.common.Info;
import com.cfg.model.Placemark;
import com.cfg.util.FormUtility;

public class Result implements Info{
	private String flightplan;
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

    private JList list;
    private DefaultListModel listAirport;
	
	public Result() {
		super();
		// TODO Auto-generated constructor stub

	}


	public Result(String flightplan, long distance, long altitude, int vors, int ndbs, int airports, int cities,
			int mountains) {
		super();
		this.flightplan = flightplan;
		this.distance = distance;
		this.altitude = altitude;
		this.vors = vors;
		this.ndbs = ndbs;
		this.airports = airports;
		this.cities = cities;
		this.mountains = mountains;
	}

	public JPanel getResultPanel() {
      panelAltitude = new JPanel();
      resultPanel = new JPanel();
      resultPanel.setLayout(new GridBagLayout());
      
      JTextField textField = new JTextField();
        
      
      formUtility = new FormUtility();
	
      formUtility.addLabel("FlightPlan:",resultPanel,colorForground[0],fontText);
      textField.setToolTipText(this.flightplan);
      textField.setText(this.flightplan);
      textField.setColumns(15);
      textField.setEditable(false);
      textField.setBackground(Color.lightGray);
      textField.getCaret().setDot(0);
 
      panel = new JPanel();
      panel.setLayout(new BorderLayout());
      panel.add(textField, BorderLayout.WEST);
      formUtility.addLastField(panel, resultPanel);
		
      formUtility.addLabel("Distance:",resultPanel,colorForground[0],fontText);
      label = new JLabel(this.distance+" nm");
      panel = new JPanel();
      panel.setLayout(new BorderLayout());
      panel.add(label, BorderLayout.WEST);
      formUtility.addLastField(panel, resultPanel);
      
      formUtility.addLabel("Departure/Destinaton:",resultPanel,colorForground[0],fontText);
      label = new JLabel(this.departure+" -> "+this.destination);
      panel = new JPanel();
      panel.setLayout(new BorderLayout());
      panel.add(label, BorderLayout.WEST);
      formUtility.addLastField(panel, resultPanel);


	  altitudeModel =  new SpinnerNumberModel(altitude, 
      		  500, //min
      		  60000, //max
      	      500);     
	  
	  
      altitudeSpinner=new JSpinner(altitudeModel);
	  altitudeSpinner.setPreferredSize( new Dimension (80,10) );
      altitudeSpinner.setToolTipText("Change altitude of flightplan");
      formUtility.addLabel("Altitude:", resultPanel,colorForground[0],fontText);
      JPanel panelSpin = new JPanel();
      panelSpin.setLayout(new BorderLayout());
      panelSpin.add(altitudeSpinner, BorderLayout.WEST);
      formUtility.addLastField(panelSpin, resultPanel);
      
      altitudeSpinner.addChangeListener(new ChangeListener() {

          @Override
          public void stateChanged(ChangeEvent e) {
/*              JSpinner s = (JSpinner) e.getSource();
              System.out.println("spiner value "+s.getValue().toString());
              altitude = Double.valueOf(s.getValue().toString()).longValue();
*/

          }
      });    
      
   
      formUtility.addLabel("Nearest on the route       ",resultPanel,colorForground[0],fontTextItalic);
      label = new JLabel("");
      panel = new JPanel();
      panel.setLayout(new BorderLayout());
      panel.add(label, BorderLayout.WEST);
      formUtility.addLastField(panel, resultPanel);

      formUtility.addLabel("Airports:",resultPanel,colorForground[0],fontText);
      label = new JLabel(this.airports+"");
      panel = new JPanel();
      panel.setLayout(new BorderLayout());
      panel.add(label, BorderLayout.WEST);
      formUtility.addLastField(panel, resultPanel);
     
      formUtility.addLabel("VORs:",resultPanel,colorForground[0],fontText);
      label = new JLabel(this.vors+"");
      panel = new JPanel();
      panel.setLayout(new BorderLayout());
      panel.add(label, BorderLayout.WEST);
      formUtility.addLastField(panel, resultPanel);
     
      formUtility.addLabel("NDBs:",resultPanel,colorForground[0],fontText);
      label = new JLabel(this.ndbs+"");
      panel = new JPanel();
      panel.setLayout(new BorderLayout());
      panel.add(label, BorderLayout.WEST);
      formUtility.addLastField(panel, resultPanel);
      
      formUtility.addLabel("Cities:",resultPanel,colorForground[0],fontText);
      label = new JLabel(this.cities+"");
      panel = new JPanel();
      panel.setLayout(new BorderLayout());
      panel.add(label, BorderLayout.WEST);
      formUtility.addLastField(panel, resultPanel);
     
      formUtility.addLabel("Mountains:",resultPanel,colorForground[0],fontText);
      label = new JLabel(this.mountains+"");
      panel = new JPanel();
      panel.setLayout(new BorderLayout());
      panel.add(label, BorderLayout.WEST);
      formUtility.addLastField(panel, resultPanel);
      
     
      resultPanel.validate();
      
      return resultPanel;
	}
	
	


	public String getFlightplan() {
		return flightplan;
	}


	public void setFlightplan(String flightplan) {
		this.flightplan = flightplan;
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


	public DefaultListModel getListAirport() {
		return listAirport;
	}


	public void setListAirport(List<Placemark> placemarks) {
		
		//this.listAirport.addElement(airport);
	}

}
