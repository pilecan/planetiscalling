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

public class DistanceObject implements Info {
	private SpinnerModel distanceCity;
	private SpinnerModel distanceMountain;
	private SpinnerModel distanceAirport;
	public JSpinner citySpinner;
	public JSpinner mountainSpinner;
	public JSpinner airportSpinner;
	
	public JCheckBox checkLinedist;
	
	public JPanel form;
	protected JPanel panelCity;
	private JPanel panelMountain;
	private JPanel panelAirport;
	private JPanel panelLineDistance;

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
        
        form = new JPanel();
        form.setLayout(new GridBagLayout());
        FormUtility formUtility = new FormUtility();

        
        if (!"city".equals(topic)) {
	        citySpinner=new JSpinner(distanceCity);
	     //   citySpinner.setPreferredSize( new Dimension (10, 10) );
	        citySpinner.setToolTipText("City distances in Nautical Miles");
	        formUtility.addLabel("City ",form, Color.BLACK,new Font("arial", Font.BOLD, 12));
	        panelCity = new JPanel();
	        panelCity.add(citySpinner, BorderLayout.WEST);
	        formUtility.addLastField(panelCity, form);
        }
        
        if (!"mountain".equals(topic)) {
            mountainSpinner=new JSpinner(distanceMountain);
            mountainSpinner.setToolTipText("Mountain distances in Nautical Miles");
            formUtility.addLabel("Mountain ",form, Color.BLACK,new Font("arial", Font.BOLD, 12));
            panelMountain = new JPanel();
            panelMountain.add(mountainSpinner, BorderLayout.WEST);
            formUtility.addLastField(panelMountain, form);

        }
        if (!"airport".equals(topic)) {
            airportSpinner=new JSpinner(distanceAirport);
            //    mountainSpinner.setPreferredSize( new Dimension (10, 10) );
            airportSpinner.setToolTipText("Airport distances in Nautical Miles");
            formUtility.addLabel("Airport ",form, Color.BLACK,new Font("arial", Font.BOLD, 12));
            panelAirport = new JPanel();
            panelAirport.add(airportSpinner, BorderLayout.WEST);
            formUtility.addLastField(panelAirport, form);
        }

        
        checkLinedist = new JCheckBox();
        checkLinedist.setSelected(true);
        checkLinedist.setToolTipText("Check it for lines between Objects");
     //   checkLinedist.setPreferredSize( new Dimension (10, 10) );

        formUtility.addLabel("Lines ",form, Color.BLACK,new Font("arial", Font.BOLD, 12));
        panelLineDistance = new JPanel();
        panelLineDistance.add(checkLinedist, BorderLayout.WEST);
        formUtility.addLastField(panelLineDistance, form);
      
		
	}
	
	

}
