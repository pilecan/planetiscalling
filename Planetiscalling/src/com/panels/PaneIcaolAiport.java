package com.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

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
import com.cfg.common.Info;
import com.cfg.file.ManageXMLFile;
import com.cfg.model.Placemark;
import com.model.Distance;
import com.model.Result;
import com.model.SortedListModel;
import com.util.ReadData;
import com.util.Utility;

import net.SelectCity;
import net.SelectMountain;
import net.SelectNdb;
import net.SelectVor;

public class PaneIcaolAiport extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Result result;
	private ManageXMLFile manageXMLFile;
	private JPanel outputIcaoPanel;
	private JPanel icaoPanel;
	private JPanel resultPanel;
	private ReadData readData;

	private List<Placemark> airports ;
	private JScrollPane jScrollPane1;
	private SelectVor selectVor;
	private SelectNdb selectNdb;
	private SelectMountain selectMountain;
	private SelectCity selectCity;
	private DistanceSpinner distanceSpin; 
	private JList listIcao;
	private SortedListModel listIcaoModel;
	private JButton deleteButton;
	private JButton googleButton;
	
	public JPanel getPanel(final ManageXMLFile manageXMLFile,  final SelectVor selectVor,  final SelectNdb selectNdb,final SelectMountain selectMountain, final SelectCity selectCity) {
		this.setManageXMLFile(manageXMLFile);
		this.selectVor = selectVor;
		this.selectNdb = selectNdb;
		this.selectCity = selectCity;
		this.selectMountain = selectMountain;
		
		distanceSpin = new DistanceSpinner();
		distanceSpin.initPanelDistances("icao");
	
		icaoPanel = new JPanel();
		icaoPanel.setLayout(null);
		
		
		outputIcaoPanel = new JPanel(new BorderLayout());
		outputIcaoPanel.setBorder(new TitledBorder(""));

		resultPanel = new JPanel(new BorderLayout());
		resultPanel.setBorder(new TitledBorder("Search Result"));

		
		 deleteButton = new JButton("Remove selected");
		 deleteButton.setEnabled(false);
		 googleButton = new JButton("Google Earth");
		 googleButton.setEnabled(false);

		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)

			{
				SortedListModel dlm = (SortedListModel) listIcao.getModel();

				if (listIcao.getSelectedIndices().length > 0) {
					int[] tmp = listIcao.getSelectedIndices();
					int[] selectedIndices = listIcao.getSelectedIndices();

					for (int i = tmp.length - 1; i >= 0; i--) {
						selectedIndices = listIcao.getSelectedIndices();
						dlm.removeElement(selectedIndices[i]);
					}
				}

				listIcao.clearSelection();

				result.setAirports(listIcao.getModel().getSize());
				setResultPanel();

			}
		});

		googleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)

			{
				//System.out.println(Utility.getInstance().getIcaoFromListModel(listIcao));
				readData.creatIcaoAirports(Utility.getInstance().getIcaoFromListModel(listIcao),
						new Distance(
			    			     (int)distanceSpin.getCitySpinner().getValue(), 
		        				 (int)distanceSpin.getMountainSpinner().getValue(), 
		        				 0,
		        				 (int)distanceSpin.getVorNdbSpinner().getValue(), 
		        				 distanceSpin.getCheckLinedist().isSelected(),
		        				 0.0)
								 ); 
				setResultPanel();
				manageXMLFile.launchGoogleEarth(new File(Utility.getInstance().getFlightPlanName(Info.flightplanName)));
			}
		});

		
	
		final JTextArea textArea = new JTextArea();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		textArea.setColumns(100);
		textArea.setLineWrap(true);
		textArea.setRows(1000);
		textArea.setWrapStyleWord(true);
		textArea.setPreferredSize(new Dimension(300,500));

		JPanel inputIcaoPanel = new JPanel(new BorderLayout());
		inputIcaoPanel.setBorder(new TitledBorder("Type or Paste you ICAO Codes here"));


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
		    	  readData = new ReadData(textArea.getText(),result, manageXMLFile, selectVor,selectNdb, selectMountain, selectCity,
		    			  new Distance(
		    			     (int)distanceSpin.getCitySpinner().getValue(), 
	        				 (int)distanceSpin.getMountainSpinner().getValue(), 
	        				 0,
	        				 (int)distanceSpin.getVorNdbSpinner().getValue(), 
	        				 distanceSpin.getCheckLinedist().isSelected(),
	        				 0.0));
		    	  
		    	 setIcaoResult();
		    	 setResultPanel();
				 googleButton.setEnabled(true);
				 
				 icaoPanel.validate();

		      }
		    });

		JButton clearButton = new JButton("Clear");
	    clearButton.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  textArea.setText("");
	    	  listIcao.removeAll();
		   	  outputIcaoPanel.removeAll();	
		   	  outputIcaoPanel.setVisible(false);
		      resultPanel.removeAll();	
		      resultPanel.setVisible(false);
		      listIcaoModel.clear();

	    	  

	      }
	    });
		

		inputIcaoPanel.setBounds(10, 8, 270, 120);
		resultPanel.setBounds(290, 8, 300, 125);
	  	outputIcaoPanel.setBounds(290, 132, 300, 149);	

		distanceSpin.getSpinnerPanel().setBounds(10, 130, 280, 140);
  	    searchButton.setBounds(10, 280, 125, 23);
		clearButton.setBounds(150, 280, 125, 23);
		deleteButton.setBounds(290, 280, 130, 23);
		googleButton.setBounds(450, 280, 130, 23);
		

//		icaoPanel.add(labelHeader);
		icaoPanel.add(inputIcaoPanel);
		icaoPanel.add(distanceSpin.getSpinnerPanel());
		icaoPanel.add(outputIcaoPanel);
		icaoPanel.add(resultPanel);
		icaoPanel.add(searchButton);
		icaoPanel.add(clearButton);
		icaoPanel.add(deleteButton);
		icaoPanel.add(googleButton);
		
		return icaoPanel;
	}

	/**
	 * 
	 */
	private void setResultPanel() {
		resultPanel.removeAll();
		resultPanel.add(result.getIcaoPanel());
		resultPanel.setVisible(true);
		resultPanel.validate();

		icaoPanel.add(resultPanel);
	}

	/**
	 * 
	 */
	private void setIcaoResult() {
		
		 listIcaoModel = result.getListIcaoModel();

		 outputIcaoPanel.setVisible(true);
	     outputIcaoPanel.removeAll();	
		 listIcao = new JList(listIcaoModel);
		 

		 listIcao.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		 listIcao.setVisibleRowCount(5);
		 
		 
		 listIcao.addListSelectionListener(new ListSelectionListener() {
			  /**
			  * {@inheritDoc}
			  */
			  @Override
			  public void valueChanged(ListSelectionEvent e) {
				  deleteButton.setEnabled(!listIcao.isSelectionEmpty());
			  }
			});
	
		 outputIcaoPanel.add(new JScrollPane(listIcao, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));

	}
	
	public ManageXMLFile getManageXMLFile() {
		return manageXMLFile;
	}


	public void setManageXMLFile(ManageXMLFile manageXMLFile) {
		this.manageXMLFile = manageXMLFile;
	}
 
	 
	

}
