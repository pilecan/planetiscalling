package com.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.cfg.common.Info;
import com.cfg.file.ManageXMLFile;
import com.cfg.model.Placemark;
import com.panels.PaneIcaolAiport;
import com.panels.PanelLandmarks;
import com.panels.PanelManage;
import com.panels.PanelPlan;
import com.util.Utility;

import net.SelectAiport;
import net.SelectCity;
import net.SelectMountain;
import net.SelectNdb;
import net.SelectVor;


public class PlanetIsCalling extends JFrame implements Info {

	private static final long serialVersionUID = 1L;
	
	private	JPanel	panelSearch;
	private	JPanel	panel2;
	private	JPanel	panel3;
	private	JPanel	panel4;
	private	JPanel	panel5;
	private	JPanel	panel6;

	private ManageXMLFile manageXMLFile;
	private SelectCity selectCity;
	private SelectMountain selectMountain;
	private SelectVor selectVor;
	private SelectNdb selectNdb;

	
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
		selectCity = new SelectCity();
		selectMountain = new SelectMountain();
		selectVor = new SelectVor();
		selectNdb = new SelectNdb();
		airports = new ArrayList<>();
		
		selectCity.selectAll("");
		selectMountain.selectAll("");
		selectVor.selectAll("");
		selectNdb.selectAll("");

		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(620, 420);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    setLocation(dim.width/2 - getWidth()/2, dim.height/2 - getHeight()/2);
		
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, fall back to cross-platform
			try {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			} catch (Exception ex) {
				// not worth my time
			}
		}

		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
		
        Utility.getInstance().setIcon(this, imageLogo);

	    
		setTitle("The Planet Is Calling You and Me Too");
	
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		getContentPane().add(mainPanel);
		
		itemTabPanel2();
		itemTabPanel3();
		itemTabPanel4();
		itemTabPanel5();
		itemTabPanel6();
		
		JTabbedPane tabPane = new JTabbedPane();
		tabPane.addTab( "Fightplan", new PanelPlan().getFlightplan(manageXMLFile,selectCity,selectMountain,selectVor, selectNdb));
		tabPane.addTab( "ICAO", new PaneIcaolAiport().getPanel(manageXMLFile,selectVor, selectNdb, selectMountain, selectCity));
		tabPane.addTab( "Airport", new PanelLandmarks().getAirportWorld(manageXMLFile,selectCity,selectMountain));
		tabPane.addTab( "City", new PanelLandmarks().getCityWorld(manageXMLFile));
		tabPane.addTab( "Mountain",new PanelLandmarks().getMountainWorld(manageXMLFile));
		tabPane.addTab( "Setting", new PanelManage().getSettingPanel());
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