package com.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;

import com.cfg.file.ManageXMLFile;
import com.cfg.model.Placemark;
import com.util.ReadData;

import net.SelectAiport;
import net.SelectCity;
import net.SelectMountain;

public class PanelAiport extends JFrame {

	private ManageXMLFile manageXMLFile;
	private SelectAiport selectAiport;

	private List<Placemark> airports ;
	private JLabel labelHeader;
	private JScrollPane jScrollPane1;
	//private JTextArea textArea;

	public PanelAiport() {
		/*		
		manageXMLFile = new ManageXMLFile("");
		selectAiport = new SelectAiport();
		airports = new ArrayList<>();
		selectAiport.selectAll("", airports);
		manageXMLFile.setPlacemarks(selectAiport.getPlacemarks());
		
*/		// TODO Auto-generated constructor stub
	}

	
	public JPanel getIcao(final ManageXMLFile manageXMLFile,final SelectAiport selectAiport,SelectCity selectCity,final SelectMountain selectMountain) {
		
		JPanel panelSearch = new JPanel();
		panelSearch.setLayout(null);
		
		JPanel panelResult = new JPanel();
		
		final JLabel labelResult = new JLabel();
		
		
		JButton searchButton = new JButton("Search");
		JButton clearButton = new JButton("Clear");
		
		final JTextArea textArea = new JTextArea(500, 500);

		labelHeader = new JLabel("Type or Paste you ICAO Codes here");
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		textArea.setColumns(500);
		textArea.setLineWrap(true);
		textArea.setRows(500);
		textArea.setWrapStyleWord(true);
		textArea.setPreferredSize(new Dimension(300,500));

		JPanel content = new JPanel(new BorderLayout());

	//	content.add(textArea);
		content.add(new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		content.setVisible(true);
		content.setPreferredSize(new Dimension(300,500));

		
		labelHeader.setBounds(10, 5, 300, 23);
		content.setBounds(10, 30, 250, 160);
		clearButton.setBounds(140, 200, 90, 23);
  	    searchButton.setBounds(10, 200, 90, 23);
  	  	labelResult.setBounds(270, 40, 120, 63);

		searchButton.addActionListener(new ActionListener()
		    {
		      public void actionPerformed(ActionEvent e)
		      {
		    	  new ReadData(textArea.getText(),labelResult, manageXMLFile, selectAiport,selectMountain);
		      }
		    });

		clearButton.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  textArea.setText("");
	    	  labelResult.setText("");
	      }
	    });
		
		panelSearch.add(labelHeader);
		panelSearch.add(content);
		panelSearch.add(searchButton);
		panelSearch.add(clearButton);
		panelSearch.add(labelResult);
		
		return panelSearch;
	}
 
	 
	

}
