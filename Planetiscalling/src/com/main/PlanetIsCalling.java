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
import com.panels.PanelFlightplan;
import com.util.Utility;

import net.SelectAirport;
import net.SelectCity;
import net.SelectMountain;
import net.SelectNdb;
import net.SelectVor;


public class PlanetIsCalling extends JFrame implements Info {

	private static final long serialVersionUID = 1L;
	
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
		setSize(700, 510);
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
	    
		setTitle("The Planet Is Calling 0.7");
	
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		getContentPane().add(mainPanel);
		
		itemTabPanel6();
		
		JTabbedPane tabPane = new JTabbedPane();
		tabPane.addTab( "Fightplan", new PanelFlightplan().getPanel(manageXMLFile,selectCity,selectMountain,selectVor, selectNdb));
		tabPane.addTab( "ICAO", new PaneIcaolAiport().getPanel(manageXMLFile,selectVor, selectNdb, selectMountain, selectCity));
		tabPane.addTab( "Airport", new PanelLandmarks().getAirportPanel(manageXMLFile,selectCity,selectMountain));
		tabPane.addTab( "City", new PanelLandmarks().getCityPanel(manageXMLFile));
		tabPane.addTab( "Mountain",new PanelLandmarks().getMountainPanel(manageXMLFile));
		tabPane.addTab( "Setting", new PanelManage().getSettingPanel());
		tabPane.addTab( "About", panel6);
		mainPanel.add(tabPane);

	}
	

	public void itemTabPanel6()
	{
		
		JButton btn7 = new JButton("Button 7");
		btn7.setBounds(10, 11, 89, 23);
		
		JButton btn8 = new JButton("Button 8");
		btn8.setBounds(10, 45, 89, 23);
	}
}