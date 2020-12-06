package com.main;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.cfg.file.ManageXMLFile;
import com.cfg.model.Placemark;
import com.panels.PanelAiport;
import com.panels.PanelLandmarks;
import com.panels.PanelPlan;
import com.util.ReadData;

import net.SelectAiport;
import net.SelectCity;
import net.SelectMountain;


public class PlanetIsCalling extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private	JPanel	panelSearch;
	private	JPanel	panel2;
	private	JPanel	panel3;
	private	JPanel	panel4;
	private	JPanel	panel5;
	private	JPanel	panel6;

	private ManageXMLFile manageXMLFile;
	private SelectAiport selectAiport;
	private SelectCity selectCity;
	private SelectMountain selectMountain;

	
	private List<Placemark> airports ;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
			EventQueue.invokeLater(new Runnable() {
			public void run() {
				PlanetIsCalling frame = new PlanetIsCalling();
				frame.setVisible(true);
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public PlanetIsCalling() {
		manageXMLFile = new ManageXMLFile("");
		selectAiport = new SelectAiport();
		selectCity = new SelectCity();
		selectMountain = new SelectMountain();
		airports = new ArrayList<>();

		selectAiport.selectAll("", airports);
		manageXMLFile.setPlacemarks(selectAiport.getPlacemarks());
		
		selectCity.selectAll("");
		selectMountain.selectAll("");

		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 458, 333);
		setTitle("The Planet Is Calling You and Me");
	
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		getContentPane().add(mainPanel);
		
		itemTabPanel2();
		itemTabPanel3();
		itemTabPanel4();
		itemTabPanel5();
		itemTabPanel6();
		
		PanelPlan panelPlan = new PanelPlan();
		
		PanelAiport panelAiport = new PanelAiport();
		PanelLandmarks panelLandmarks = new PanelLandmarks();

		JTabbedPane tabPane = new JTabbedPane();
		tabPane.addTab( "Fightplan", panelPlan.getFlightplan(manageXMLFile,selectAiport,selectCity,selectMountain));
		tabPane.addTab( "ICAO", panelAiport.getIcao(manageXMLFile,selectAiport,selectCity,selectMountain));
		tabPane.addTab( "Airport", panelLandmarks.getAirportWorld(manageXMLFile,selectAiport,selectCity,selectMountain));
		tabPane.addTab( "City", panelLandmarks.getCityWorld(manageXMLFile));
		tabPane.addTab( "Mountain",panelLandmarks.getMountainWorld(manageXMLFile));
		tabPane.addTab( "Setting", panel5);
		tabPane.addTab( "About", panel6);
		mainPanel.add(tabPane);

	}
	

	
	public void itemTabPanel2()
	{
		panel2 = new JPanel();
		panel2.setLayout(null);
		
		JButton btn3 = new JButton("Button 3");
		btn3.setBounds(19, 11, 89, 23);
		panel2.add(btn3);
		
		JButton btn4 = new JButton("Button 4");
		btn4.setBounds(10, 45, 89, 23);
		panel2.add(btn4);	
	}

	public void itemTabPanel3()
	{
		panel3 = new JPanel();
		panel3.setLayout(null);;
		
		JButton btn5 = new JButton("Button 5");
		btn5.setBounds(10, 11, 89, 23);
		panel3.add(btn5);
		
		JButton btn6 = new JButton("Button 6");
		btn6.setBounds(10, 45, 89, 23);
		panel3.add(btn6);		
		
	}
	
	public void itemTabPanel4()
	{
		panel4 = new JPanel();
		panel4.setLayout(null);;
		
		JButton btn7 = new JButton("Button 7");
		btn7.setBounds(10, 11, 89, 23);
		panel4.add(btn7);
		
		JButton btn8 = new JButton("Button 8");
		btn8.setBounds(10, 45, 89, 23);
		panel4.add(btn8);	
	}
	
	public void itemTabPanel5()
	{
		panel4 = new JPanel();
		panel4.setLayout(null);;
		
		JButton btn7 = new JButton("Button 7");
		btn7.setBounds(10, 11, 89, 23);
		panel4.add(btn7);
		
		JButton btn8 = new JButton("Button 8");
		btn8.setBounds(10, 45, 89, 23);
		panel4.add(btn8);	
	}	public void itemTabPanel6()
	{
		panel4 = new JPanel();
		panel4.setLayout(null);;
		
		JButton btn7 = new JButton("Button 7");
		btn7.setBounds(10, 11, 89, 23);
		panel4.add(btn7);
		
		JButton btn8 = new JButton("Button 8");
		btn8.setBounds(10, 45, 89, 23);
		panel4.add(btn8);	
	}
}