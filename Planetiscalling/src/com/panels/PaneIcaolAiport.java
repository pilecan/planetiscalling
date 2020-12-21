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

import com.cfg.common.DistanceSpinner;
import com.cfg.file.ManageXMLFile;
import com.cfg.model.Placemark;
import com.model.Distance;
import com.model.Result;
import com.util.ReadData;

import net.SelectAiport;
import net.SelectCity;
import net.SelectMountain;
import net.SelectNdb;
import net.SelectVor;

public class PaneIcaolAiport extends JFrame {

	private Result result;
	private ManageXMLFile manageXMLFile;
	private SelectAiport selectAiport;

	private List<Placemark> airports ;
	private JLabel labelHeader;
	private JScrollPane jScrollPane1;
	private SelectVor selectVor;
	private SelectNdb selectNdb;
	private DistanceSpinner distanceSpin; 

	
	public JPanel getPanel(final ManageXMLFile manageXMLFile, final SelectAiport selectAiport, final SelectVor selectVor,  final SelectNdb selectNdb) {
		this.setManageXMLFile(manageXMLFile);
		this.selectAiport = selectAiport;
		this.selectVor = selectVor;
		this.selectNdb = selectNdb;
		
		distanceSpin = new DistanceSpinner();
		distanceSpin.initPanelDistances("icao");
	
		JPanel panelSearch = new JPanel();
		panelSearch.setLayout(null);
		
		final JLabel labelResult = new JLabel();
		
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

		
	
		JButton searchButton = new JButton("Search");
		searchButton.addActionListener(new ActionListener()
		    {
		      public void actionPerformed(ActionEvent e)
		      {
		    	  result = new Result();
		    	  new ReadData(textArea.getText(),result, manageXMLFile, selectAiport, selectVor,selectNdb,new Distance());
		      }
		    });

		JButton clearButton = new JButton("Clear");
	    clearButton.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  textArea.setText("");
	    	  labelResult.setText("");
	      }
	    });
		

		labelHeader.setBounds(10, 5, 300, 23);
		content.setBounds(10, 30, 250, 160);
		distanceSpin.getSpinnerPanel().setBounds(10, 200, 320, 40);
		clearButton.setBounds(160, 250, 90, 23);
  	    searchButton.setBounds(10, 250, 90, 23);
  	  	labelResult.setBounds(270, 40, 120, 63);
	
		panelSearch.add(labelHeader);
		panelSearch.add(content);
		panelSearch.add(distanceSpin.getSpinnerPanel());
		panelSearch.add(searchButton);
		panelSearch.add(clearButton);
		panelSearch.add(labelResult);
		
		return panelSearch;
	}


	public ManageXMLFile getManageXMLFile() {
		return manageXMLFile;
	}


	public void setManageXMLFile(ManageXMLFile manageXMLFile) {
		this.manageXMLFile = manageXMLFile;
	}
 
	 
	

}
