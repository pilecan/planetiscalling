package com.cfg.common;

import java.awt.BorderLayout;
import java.awt.Color;
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
	public JSpinner citySpinner;
	public JSpinner mountainSpinner;
	public JSpinner airportSpinner;
	
	public JCheckBox checkLinedist;
	
	public JPanel spinnerPanel;
	protected JPanel panelCity;
	private JPanel panelMountain;
	private JPanel panelAirport;
	private JPanel panelLineDistance;
	
	

	public DistanceSpinner() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void initPanelDistances(String topic) {
		
		
		distanceCity =  new SpinnerNumberModel(20, 
      		  0, //min
      		  200, //max
      	      10);               
		
		distanceMountain =  new SpinnerNumberModel(100, 
      		  0, //min
      		  200, //max
      	      10);               
		
		distanceAirport =  new SpinnerNumberModel(50, 
      		  0, //min
      		  1000, //max
      	      10);               
        
        panelMountain = new JPanel();
        panelAirport = new JPanel();
        panelLineDistance = new JPanel();
        
        spinnerPanel = new JPanel();
        spinnerPanel.setLayout(new GridBagLayout());
        FormUtility formUtility = new FormUtility();

        
        if (!"city".equals(topic)) {
	        citySpinner=new JSpinner(distanceCity);
	     //   citySpinner.setPreferredSize( new Dimension (10, 10) );
	        citySpinner.setToolTipText("City distances in Nautical Miles");
	        formUtility.addLabel("City (nm)",spinnerPanel, Color.BLACK,new Font("arial", Font.BOLD, 12));
	        panelCity = new JPanel();
	        panelCity.add(citySpinner, BorderLayout.WEST);
	        formUtility.addLastField(panelCity, spinnerPanel);
        }
        
        if (!"mountain".equals(topic)) {
            mountainSpinner=new JSpinner(distanceMountain);
            mountainSpinner.setToolTipText("Mountain distances in Nautical Miles");
            formUtility.addLabel("Mountain (nm)",spinnerPanel, Color.BLACK,new Font("arial", Font.BOLD, 12));
            panelMountain = new JPanel();
            panelMountain.add(mountainSpinner, BorderLayout.WEST);
            formUtility.addLastField(panelMountain, spinnerPanel);

        }
        if (!"airport".equals(topic)) {
            airportSpinner=new JSpinner(distanceAirport);
            //    mountainSpinner.setPreferredSize( new Dimension (10, 10) );
            airportSpinner.setToolTipText("Airport distances in Nautical Miles");
            formUtility.addLabel("Airport (nm)",spinnerPanel, Color.BLACK,new Font("arial", Font.BOLD, 12));
            panelAirport = new JPanel();
            panelAirport.add(airportSpinner, BorderLayout.WEST);
            formUtility.addLastField(panelAirport, spinnerPanel);
        }

        
        checkLinedist = new JCheckBox();
        checkLinedist.setSelected(true);
        checkLinedist.setToolTipText("Check it to see lines between placemarks");
     //   checkLinedist.setPreferredSize( new Dimension (10, 10) );

        formUtility.addLabel("Lines between",spinnerPanel, Color.BLACK,new Font("arial", Font.BOLD, 12));
        panelLineDistance = new JPanel();
        panelLineDistance.add(checkLinedist, BorderLayout.WEST);
        formUtility.addLastField(panelLineDistance, spinnerPanel);
      
		
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
	
	

}
