package com.cfg.common;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.util.FormUtility;

public class DistanceSpinner implements Info {
	
	private SpinnerModel distanceCity;
	private SpinnerModel distanceMountain;
	private SpinnerModel distanceLandmark;
	private SpinnerModel distanceAirport;
	private SpinnerModel distanceVorNdb;
	
	private boolean isSpinnerChanged = false;
	
	public JSpinner citySpinner;
	public JSpinner mountainSpinner;
	public JSpinner landkmarkSpinner;
	public JSpinner airportSpinner;
	public JSpinner vorNdbSpinner;
	
	public JCheckBox checkLinedist;
	public JCheckBox checkTocTod;
	
	public JPanel spinnerPanel;
	
	
	private JPanel panel;
	
	private FormUtility formUtility;
	

	public DistanceSpinner() {
		super();
		// TODO Auto-generated constructor stub
	}
	class SpinnerListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			isSpinnerChanged = true;

		}
	}

	public void initPanelDistances(String topic) {
		
		SpinnerListener spinnerListener = new SpinnerListener();
		
		
		distanceCity =  new SpinnerNumberModel(0, 
      		  0, //min
      		  200, //max
      	      10);               
		distanceCity.addChangeListener(spinnerListener);
	
		distanceMountain =  new SpinnerNumberModel(0, 
	      		  0, //min
	      		  200, //max
	      	      10);               

		distanceMountain.addChangeListener(spinnerListener);

		distanceLandmark =  new SpinnerNumberModel(0, 
	      		  0, //min
	      		  200, //max
	      	      10);               
		distanceLandmark.addChangeListener(spinnerListener);

		distanceAirport =  new SpinnerNumberModel(0, 
      		  0, //min
      		  1000, //max
      	      10);     
		distanceAirport.addChangeListener(spinnerListener);
	
		distanceVorNdb =  new SpinnerNumberModel(0, 
	      		  0, //min
	      		  100, //max
	      	      10);               

		distanceVorNdb.addChangeListener(spinnerListener);
   //spinnerPanel = new JPanel();
		
         
         spinnerPanel = new JPanel(new BorderLayout());
         spinnerPanel.setBorder(new TitledBorder("Search Criteria"));
 	     Border border = spinnerPanel.getBorder();
 		 Border margin = new EmptyBorder(10,10,10,10);
         spinnerPanel.setBorder(new CompoundBorder(border, margin));
         
        spinnerPanel.setLayout(new GridBagLayout());
        
        formUtility = new FormUtility();
        
        if ("icao".equals(topic)) {
        	vorNdbSpinner =  makeSpinner("Vor/NDB distances in Nautical Miles", "Vor/NDB (nm)", vorNdbSpinner, distanceVorNdb, panel, formUtility);
        	citySpinner = makeSpinner("City distances in Nautical Miles", "City (nm)", citySpinner, distanceCity, panel, formUtility);
        	mountainSpinner = makeSpinner("Moutain distances in Nautical Miles", "Mountain (nm)", mountainSpinner, distanceMountain, panel, formUtility);
        	landkmarkSpinner = makeSpinner("Canada Landmark distances in Nautical Miles", "Landmark (nm)", landkmarkSpinner, distanceLandmark, panel, formUtility);
      	    checkLinedist = makeCheckbox("Check it to see lines between placemarks", "Line Between", checkLinedist, false, panel, formUtility);
        } else  if ("plan".equals(topic)) {
        	airportSpinner = makeSpinner("Airport distances in Nautical Miles", "Airport (nm)", airportSpinner, distanceAirport, panel, formUtility);
        	vorNdbSpinner =  makeSpinner("Vor/NDB distances in Nautical Miles", "Vor/NDB (nm)", vorNdbSpinner, distanceVorNdb, panel, formUtility);
        	citySpinner = makeSpinner("City distances in Nautical Miles", "City (nm)", citySpinner, distanceCity, panel, formUtility);
        	mountainSpinner = makeSpinner("Moutain distances in Nautical Miles", "Mountain (nm)", mountainSpinner, distanceMountain, panel, formUtility);
        	landkmarkSpinner = makeSpinner("Canada Landmark distances in Nautical Miles", "Landmark (nm)", landkmarkSpinner, distanceLandmark, panel, formUtility);
        	checkTocTod = makeCheckbox("Top of Climb and Top of Descent to the Flightplan", "Top of Climb and Descent", checkTocTod, true, panel, formUtility);

         } else if ("airport".equals(topic)) {
         	vorNdbSpinner =  makeSpinner("Vor/NDB distances in Nautical Miles", "Vor/NDB (nm)", vorNdbSpinner, distanceVorNdb, panel, formUtility);
         	citySpinner = makeSpinner("City distances in Nautical Miles", "City (nm)", citySpinner, distanceCity, panel, formUtility);
        	mountainSpinner = makeSpinner("Moutain distances in Nautical Miles", "Mountain (nm)", mountainSpinner, distanceMountain, panel, formUtility);
        	landkmarkSpinner = makeSpinner("Canada Landmark distances in Nautical Miles", "Landmark (nm)", landkmarkSpinner, distanceLandmark, panel, formUtility);
      	    checkLinedist = makeCheckbox("Check it to see lines between placemarks", "Line Between", checkLinedist, false, panel, formUtility);
         } else if ("city".equals(topic)) {
         	airportSpinner = makeSpinner("Airport distances in Nautical Miles", "Airport (nm)", airportSpinner, distanceAirport, panel, formUtility);
        	vorNdbSpinner =  makeSpinner("Vor/NDB distances in Nautical Miles", "Vor/NDB (nm)", vorNdbSpinner, distanceVorNdb, panel, formUtility);
        	mountainSpinner = makeSpinner("Moutain distances in Nautical Miles", "Mountain (nm)", mountainSpinner, distanceMountain, panel, formUtility);
        	landkmarkSpinner = makeSpinner("Canada Landmark distances in Nautical Miles", "Landmark (nm)", landkmarkSpinner, distanceLandmark, panel, formUtility);
     	    checkLinedist = makeCheckbox("Check it to see lines between placemarks", "Line Between", checkLinedist, false, panel, formUtility);
       
         } else  if ("mountain".equals(topic)) {
          	airportSpinner = makeSpinner("Airport distances in Nautical Miles", "Airport (nm)", airportSpinner, distanceAirport, panel, formUtility);
         	vorNdbSpinner =  makeSpinner("Vor/NDB distances in Nautical Miles", "Vor/NDB (nm)", vorNdbSpinner, distanceVorNdb, panel, formUtility);
          	citySpinner = makeSpinner("City distances in Nautical Miles", "City (nm)", citySpinner, distanceCity, panel, formUtility);
       	    checkLinedist = makeCheckbox("Check it to see lines between placemarks", "Line Between", checkLinedist, false, panel, formUtility);

          }else  if ("landmark".equals(topic)) {
           	airportSpinner = makeSpinner("Airport distances in Nautical Miles", "Airport (nm)", airportSpinner, distanceAirport, panel, formUtility);
           	citySpinner = makeSpinner("City distances in Nautical Miles", "City (nm)", citySpinner, distanceCity, panel, formUtility);
        	mountainSpinner = makeSpinner("Moutain distances in Nautical Miles", "Mountain (nm)", mountainSpinner, distanceMountain, panel, formUtility);
        	checkLinedist = makeCheckbox("Check it to see lines between placemarks", "Line Between", checkLinedist, false, panel, formUtility);

         }
 
 
		
	}
	

	
    private JSpinner makeSpinner(String text1, String text2, JSpinner spin, SpinnerModel model, JPanel panel, FormUtility formUtility) {
        spin=new JSpinner(model);
        spin.setPreferredSize( new Dimension (45,20) );
     //   spin.setToolTipText(text1);
        formUtility.addLabel(text2+"                                       ",spinnerPanel);
        panel = new JPanel();
        panel.setPreferredSize( new Dimension (300,30) );

        panel.add(spin, BorderLayout.WEST);
        formUtility.addLastField(panel, spinnerPanel);
   	
        return spin;
    }

    
    private JCheckBox makeCheckbox(String text1, String text2, JCheckBox check, boolean isChecked, JPanel panel, FormUtility formUtility) {
     	check = new JCheckBox();
    	check.setSelected(isChecked);
    //	check.setToolTipText(text1);

        formUtility.addLabel(text2,spinnerPanel);
        panel = new JPanel();
        panel.add(check, BorderLayout.WEST);
        formUtility.addLastField(panel, spinnerPanel);

        return check;
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

	public JSpinner getLandkmarkSpinner() {
		return landkmarkSpinner;
	}

	public void setLandkmarkSpinner(JSpinner landkmarkSpinner) {
		this.landkmarkSpinner = landkmarkSpinner;
	}



	public boolean isSpinnerChanged() {
		return isSpinnerChanged;
	}



	public void setSpinnerChanged(boolean isSpinnerChanged) {
		this.isSpinnerChanged = isSpinnerChanged;
	}

	
	
	

}
