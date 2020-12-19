package com.model;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import com.cfg.common.Info;
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
	
	private SpinnerModel altitudeModel;
	public JSpinner altitudeSpinner;
	private JPanel panelAltitude;
	private JPanel resultPanel; 
    private FormUtility formUtility;

	
	
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
		JPanel panel;
		JLabel label;
      panelAltitude = new JPanel();
      resultPanel = new JPanel();
      resultPanel.setLayout(new GridBagLayout());
      formUtility = new FormUtility();
	
      formUtility.addLabel("FlightPlan:",resultPanel,colorForground[0],fontText);
      label = new JLabel(this.flightplan);
      label.setToolTipText(this.flightplan);
      panel = new JPanel();
      panel.setLayout(new BorderLayout());
      panel.add(label, BorderLayout.WEST);
      formUtility.addLastField(panel, resultPanel);
		
      formUtility.addLabel("Distance:",resultPanel,colorForground[0],fontText);
      label = new JLabel(this.distance+" nm");
      panel = new JPanel();
      panel.setLayout(new BorderLayout());
      panel.add(label, BorderLayout.WEST);
      formUtility.addLastField(panel, resultPanel);

	  altitudeModel =  new SpinnerNumberModel(altitude, 
      		  500, //min
      		  60000, //max
      	      500);     
	  
	  
    // altitudeModel.setValue(altitude);
      altitudeSpinner=new JSpinner(altitudeModel);
      altitudeSpinner.setToolTipText("Change altitude of flightplan");
      formUtility.addLabel("Altitude:", resultPanel,colorForground[0],fontText);
      panel = new JPanel();
      panel.setLayout(new BorderLayout());
      panel.add(altitudeSpinner, BorderLayout.WEST);
      formUtility.addLastField(panel, resultPanel);
   
      formUtility.addLabel("Found on the route:",resultPanel,colorForground[0],fontText);
      label = new JLabel("");
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
     
      formUtility.addLabel("Airports:",resultPanel,colorForground[0],fontText);
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
     
		
      return resultPanel;
	}
	
	

	@Override
	public String toString() {
		return "Result [flightplan=" + flightplan + ", distance=" + distance + ", altitude=" + altitude + ", vors="
				+ vors + ", ndbs=" + ndbs + ", airports=" + airports + ", cities=" + cities + ", mountains=" + mountains
				+ "]";
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
	
	

/*	panelResult.setText("<html>"
			+ "Flightplan: "+new File(flightplan).getName()+"<br>"
			+ "Distance: "+Math.round(createKmlFSPlan.getDistanceBetween())+" nm<br>"
			+ "Altitude: "+Math.round(createKmlFSPlan.getAltitude()*3.28084)+" ft<br>"
		    + "VORs: "+createKmlFSPlan.getNbVor()+"<br>"
		    + "NDBs: "+createKmlFSPlan.getNbNdb()+"<br>"
			+ "Airports: "+createKmlFSPlan.getNbAirport()+"<br>"
			+ "Cities: "+createKmlFSPlan.getNbCity()+"<br>"
		    + "Mountains: "+createKmlFSPlan.getNbMountain()
		    +" </html>");
*/
	
/*	panelResult.setText("<html>"
			+ "Airports found: "+placemarks.size()+"<br>"
		    +" </html>");
*/
}
