package com.cfg.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import com.cfg.util.FormUtility;

public class DistanceSpinner implements Info {
	private SpinnerModel distanceCity;
	private SpinnerModel distanceMountain;
	private SpinnerModel distanceAirport;
	private SpinnerModel distanceVorNdb;
	public JSpinner citySpinner;
	public JSpinner mountainSpinner;
	public JSpinner airportSpinner;
	public JSpinner vorNdbSpinner;
	
	public JCheckBox checkLinedist;
	public JCheckBox checkTocTod;
	
	public JPanel spinnerPanel;
	protected JPanel panelCity;
	private JPanel panelMountain;
	private JPanel panelAirport;
	private JPanel panelVorNdb;
	private JPanel panelCheckbox;
	
	

	public DistanceSpinner() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void initPanelDistances(String topic) {
		
		
		distanceCity =  new SpinnerNumberModel(10, 
      		  0, //min
      		  200, //max
      	      10);               
		
		distanceMountain =  new SpinnerNumberModel(100, 
      		  0, //min
      		  200, //max
      	      10);               
		
		distanceAirport =  new SpinnerNumberModel(10, 
      		  0, //min
      		  1000, //max
      	      10);     
		
		distanceVorNdb =  new SpinnerNumberModel(10, 
	      		  0, //min
	      		  100, //max
	      	      10);               

        
        panelMountain = new JPanel();
        panelAirport = new JPanel();
        panelVorNdb = new JPanel();
        panelCheckbox = new JPanel();
        
        spinnerPanel = new JPanel();
        spinnerPanel.setLayout(new GridBagLayout());
        FormUtility formUtility = new FormUtility();

        
        if (!"city".equals(topic)) {
	        citySpinner=new JSpinner(distanceCity);
	        citySpinner.setPreferredSize( new Dimension (80,25) );
	        citySpinner.setToolTipText("City distances in Nautical Miles");
	        formUtility.addLabel("City (nm)",spinnerPanel, Color.BLACK,fontText);
	        panelCity = new JPanel();
	        panelCity.add(citySpinner, BorderLayout.WEST);
	        formUtility.addLastField(panelCity, spinnerPanel);
        }
        
        if (!"mountain".equals(topic)) {
            mountainSpinner=new JSpinner(distanceMountain);
            mountainSpinner.setPreferredSize( new Dimension (80,25) );

            mountainSpinner.setToolTipText("Mountain distances in Nautical Miles");
            formUtility.addLabel("Mountain (nm)",spinnerPanel, Color.BLACK,fontText);
            panelMountain = new JPanel();
            panelMountain.add(mountainSpinner, BorderLayout.WEST);
            formUtility.addLastField(panelMountain, spinnerPanel);

        }
        if (!"airport".equals(topic)) {
            airportSpinner=new JSpinner(distanceAirport);
            airportSpinner.setPreferredSize( new Dimension (80,25) );
            airportSpinner.setToolTipText("Airport distances in Nautical Miles");
            formUtility.addLabel("Airport (nm)",spinnerPanel, Color.BLACK,fontText);
            panelAirport = new JPanel();
            panelAirport.add(airportSpinner, BorderLayout.WEST);
            formUtility.addLastField(panelAirport, spinnerPanel);
        }
        if ("plan".equals(topic)) {
            vorNdbSpinner =new JSpinner(distanceVorNdb);
            vorNdbSpinner.setPreferredSize( new Dimension (80,25) );

            vorNdbSpinner.setToolTipText("Vor/NDB distances in Nautical Miles");
            formUtility.addLabel("Vor/NDB (nm)",spinnerPanel, Color.BLACK,fontText);
            panelVorNdb= new JPanel();
            panelVorNdb.add(vorNdbSpinner, BorderLayout.WEST);
            formUtility.addLastField(panelVorNdb, spinnerPanel);
        }

        
                
        if (!"plan".equals(topic)) {
            checkLinedist = new JCheckBox();
            checkLinedist.setSelected(!"plan".equals(topic));
            checkLinedist.setToolTipText("Check it to see lines between placemarks");

            formUtility.addLabel("Lines between",spinnerPanel, Color.BLACK,fontText);
            panelCheckbox = new JPanel();
            panelCheckbox.add(checkLinedist, BorderLayout.WEST);
            formUtility.addLastField(panelCheckbox, spinnerPanel);
        	
        } else if ("plan".equals(topic)) {
        	checkTocTod = new JCheckBox();
        	checkTocTod.setSelected(true);
        	checkTocTod.setToolTipText("Top of Climb and Top of Descent to the Flightplan");

            formUtility.addLabel("Top of Climb or Descent",spinnerPanel, Color.BLACK,fontText);
            panelCheckbox = new JPanel();
            panelCheckbox.add(checkTocTod, BorderLayout.WEST);
            formUtility.addLastField(panelCheckbox, spinnerPanel);
     	
        }
      
		
	}

	public JSpinner getCitySpinner() {
		return citySpinner;
	}

	public void setCitySpinner(JSpinner citySpinner) {
		this.citySpinner = citySpinner;
	}

	public JSpinner getMountainSpinner() {
		return mountainSpinner;
	}

	public void setMountainSpinner(JSpinner mountainSpinner) {
		this.mountainSpinner = mountainSpinner;
	}

	public JSpinner getAirportSpinner() {
		return airportSpinner;
	}

	public void setAirportSpinner(JSpinner airportSpinner) {
		this.airportSpinner = airportSpinner;
	}

	public JPanel getSpinnerPanel() {
		return spinnerPanel;
	}

	public void setSpinnerPanel(JPanel spinnerPanel) {
		this.spinnerPanel = spinnerPanel;
	}

	public JCheckBox getCheckLinedist() {
		return checkLinedist;
	}

	public void setCheckLinedist(JCheckBox checkLinedist) {
		this.checkLinedist = checkLinedist;
	}

	public JSpinner getVorNdbSpinner() {
		return vorNdbSpinner;
	}

	public void setVorNdbSpinner(JSpinner vorNdbSpinner) {
		this.vorNdbSpinner = vorNdbSpinner;
	}

	public JCheckBox getCheckTocTod() {
		return checkTocTod;
	}

	public void setCheckTocTod(JCheckBox checkTocDod) {
		this.checkTocTod = checkTocDod;
	}

	
	
	

}
