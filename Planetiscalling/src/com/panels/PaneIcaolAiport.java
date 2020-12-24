package com.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.cfg.common.DistanceSpinner;
import com.cfg.file.ManageXMLFile;
import com.cfg.model.Placemark;
import com.model.Distance;
import com.model.Result;
import com.util.ReadData;

import net.SelectCity;
import net.SelectMountain;
import net.SelectNdb;
import net.SelectVor;

public class PaneIcaolAiport extends JFrame {

	private Result result;
	private ManageXMLFile manageXMLFile;
	private JPanel outputIcaoPanel;
	private JPanel icaoPanel;
	private JPanel resultPanel;

	private List<Placemark> airports ;
	private JLabel labelHeader;
	private JScrollPane jScrollPane1;
	private SelectVor selectVor;
	private SelectNdb selectNdb;
	private SelectMountain selectMountain;
	private SelectCity selectCity;
	private DistanceSpinner distanceSpin; 
	private JList listIcao;
	private DefaultListModel<String> listResultModel;
	private JButton deleteButton;
	private JButton UpdateButton;
	private JPanel buttonPane;
	private int[] listToRemove;

	
	public JPanel getPanel(final ManageXMLFile manageXMLFile,  final SelectVor selectVor,  final SelectNdb selectNdb,final SelectMountain selectMountain, final SelectCity selectCity) {
		this.setManageXMLFile(manageXMLFile);
		this.selectVor = selectVor;
		this.selectNdb = selectNdb;
		this.selectCity = selectCity;
		this.selectMountain = selectMountain;
		
		this.listToRemove = new int[10];
	
		distanceSpin = new DistanceSpinner();
		distanceSpin.initPanelDistances("icao");
	
		icaoPanel = new JPanel();
		icaoPanel.setLayout(null);
		
		
		outputIcaoPanel = new JPanel(new BorderLayout());
		outputIcaoPanel.setBorder(new TitledBorder("Search Result"));

		resultPanel = new JPanel(new BorderLayout());
		resultPanel.setBorder(new TitledBorder(""));

		
		 deleteButton = new JButton("Delete");
		 deleteButton.setEnabled(false);
		 deleteButton.addActionListener(new ActionListener()
		    {
		      public void actionPerformed(ActionEvent e)
		      
		      {
		    	  for (int i = 0; i < listToRemove.length; i++) {
					System.out.println(listToRemove[i]);
				}
		      }
		    });

		 
		 UpdateButton = new JButton("Update");

		
		final JLabel labelResult = new JLabel();
		
		final JTextArea textArea = new JTextArea();

		labelHeader = new JLabel("Type or Paste you ICAO Codes here");
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		textArea.setColumns(500);
		textArea.setLineWrap(true);
		textArea.setRows(500);
		textArea.setWrapStyleWord(true);
		textArea.setPreferredSize(new Dimension(300,500));

		JPanel inputIcaoPanel = new JPanel(new BorderLayout());

	//	content.add(textArea);
		inputIcaoPanel.add(new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		inputIcaoPanel.setVisible(true);
		inputIcaoPanel.setPreferredSize(new Dimension(300,150));
	
		JButton searchButton = new JButton("Search");
		searchButton.addActionListener(new ActionListener()
		    {
		      public void actionPerformed(ActionEvent e)
		      {
		    	  result = new Result();
		    	  new ReadData(textArea.getText(),result, manageXMLFile, selectVor,selectNdb, selectMountain, selectCity,
		    			  new Distance(
		    			     (int)distanceSpin.getCitySpinner().getValue(), 
	        				 (int)distanceSpin.getMountainSpinner().getValue(), 
	        				 0,
	        				 (int)distanceSpin.getVorNdbSpinner().getValue(), 
	        				 distanceSpin.getCheckLinedist().isSelected(),
	        				 0.0));
		    	  
	   		   	 outputIcaoPanel.setVisible(true);
			     outputIcaoPanel.removeAll();	
				 listResultModel = result.getListModel();

				 listIcao = new JList(listResultModel);
				 listIcao.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				 listIcao.setVisibleRowCount(5);
				 
				 listIcao.addListSelectionListener(new ListSelectionListener() {
					  /**
					  * {@inheritDoc}
					  */
					  @Override
					  public void valueChanged(ListSelectionEvent e) {
					      int index[] = listIcao.getSelectedIndices();
						 // System.out.println(e);
						  for (int i = 0; i < index.length; i++) {
							 System.out.println(index[i]);
							 listToRemove[i] = index[i];
							
						}
						  
						  deleteButton.setEnabled(!listIcao.isSelectionEmpty());
						  
					  }
					});
			
				 outputIcaoPanel.add(new JScrollPane(listIcao, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
						ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
			     
				buttonPane = new JPanel();
				buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
				buttonPane.add(deleteButton);
				buttonPane.add(Box.createHorizontalStrut(132));
				buttonPane.add(UpdateButton);
				buttonPane.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

				outputIcaoPanel.add(buttonPane, BorderLayout.PAGE_END);			 				 
							
		  		 resultPanel.removeAll();	
		  		 resultPanel.add(result.getIcaoPanel());
		  		 resultPanel.setVisible(true);
				 resultPanel.validate();

				 icaoPanel.add(resultPanel);
				 
				 icaoPanel.validate();

		      }
		    });

		JButton clearButton = new JButton("Clear");
	    clearButton.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  textArea.setText("");
	    	  labelResult.setText("");
	    	  listIcao.removeAll();
		   	  outputIcaoPanel.removeAll();	
		   	  outputIcaoPanel.setVisible(false);
		    	   resultPanel.removeAll();	
		     	resultPanel.setVisible(false);
	    	  

	      }
	    });
		

		labelHeader.setBounds(10, 5, 300, 23);
		inputIcaoPanel.setBounds(10, 30, 250, 80);
		resultPanel.setBounds(10, 100, 250, 120);

		distanceSpin.getSpinnerPanel().setBounds(10, 130, 280, 140);
		clearButton.setBounds(160, 280, 90, 23);
  	    searchButton.setBounds(10, 280, 90, 23);
	  	outputIcaoPanel.setBounds(290, 20, 300, 165);	
		resultPanel.setBounds(290, 190, 300, 110);

		icaoPanel.add(labelHeader);
		icaoPanel.add(inputIcaoPanel);
		icaoPanel.add(distanceSpin.getSpinnerPanel());
		icaoPanel.add(outputIcaoPanel);
		icaoPanel.add(resultPanel);
		icaoPanel.add(searchButton);
		icaoPanel.add(clearButton);
		icaoPanel.add(labelResult);
		
		return icaoPanel;
	}


	public ManageXMLFile getManageXMLFile() {
		return manageXMLFile;
	}


	public void setManageXMLFile(ManageXMLFile manageXMLFile) {
		this.manageXMLFile = manageXMLFile;
	}
 
	 
	

}
