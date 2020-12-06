package com.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.cfg.common.DistanceObject;
import com.cfg.file.ManageXMLFile;
import com.cfg.model.Placemark;
import com.model.Distance;
import com.util.ReadData;

import net.SelectAiport;
import net.SelectCity;
import net.SelectMountain;

public class PanelPlan extends DistanceObject {

	private ManageXMLFile manageXMLFile;
	private SelectAiport selectAiport;
	private SelectCity selectCity;
	private SelectMountain selectMountain;
	private Map <String, JCheckBox> mapSelector;	
	

	private List<Placemark> airports ;

	public PanelPlan() {
		super();

	}

	
	public JPanel getFlightplan(ManageXMLFile manageXMLFile,SelectAiport selectAiport,SelectCity selectCity,SelectMountain selectMountain) {
		
		initPanelDistances("plan");
		
		JPanel panelSearch = new JPanel();
		panelSearch.setLayout(null);
		
		JPanel panelResult = new JPanel();
		JPanel panel = new JPanel();
		
		panel.add(new JLabel("allo"));
		
		final JLabel labelResult = new JLabel();
		
		labelResult.setBounds(300, 40, 89, 63);
		
		JButton buttonFP = new JButton("Flightplan");
		buttonFP.setBounds(10, 20, 120, 23);
		buttonFP.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  new ReadData(labelResult, manageXMLFile, selectAiport, selectCity, selectMountain,new Distance((int)citySpinner.getValue(), (int)mountainSpinner.getValue(), (int)airportSpinner.getValue(), checkLinedist.isSelected()));	        
	    	 // System.out.println(textArea.getText());
	        
	      }
	    });
     	panelSearch.add(buttonFP);
		panelSearch.add(labelResult);
	
		form.setBounds(10, 50, 130, 140);
	
		panelSearch.add(form);

		return panelSearch;
	}
	
	public JPanel getAirport() {
		
		JPanel panelSearch = new JPanel();
		JLabel labelResult = new JLabel();
		
		JTextArea textArea = new JTextArea(5, 20);

		
		labelResult.setBounds(300, 40, 89, 63);
		
		JButton btn1 = new JButton("Get Airports");
		btn1.setBounds(10, 20, 120, 23);
		panelSearch.add(btn1);
		panelSearch.add(labelResult);
	
		return panelSearch;
			
	}
	
	

}
