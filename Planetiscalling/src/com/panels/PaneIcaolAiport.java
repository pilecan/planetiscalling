package com.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;

import com.backend.CreateKML;
import com.backend.ReadData;
import com.cfg.common.DistanceSpinner;
import com.cfg.common.Info;
import com.db.SelectAirport;
import com.db.SelectCity;
import com.db.SelectMountain;
import com.db.SelectNdb;
import com.db.SelectVor;
import com.main.form.Result;
import com.model.Distance;
import com.util.Util;
import com.util.Utility;
import com.util.UtilityEarthAnimation;

public class PaneIcaolAiport extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Result result;
	private JPanel outputPanel;
	private JPanel icaoPanel;
	private JPanel panelResult;
	private ReadData readData;

	private JScrollPane jScrollPane1;
	private SelectVor selectVor;
	private SelectNdb selectNdb;
	private SelectMountain selectMountain;
	private SelectCity selectCity;
	private DistanceSpinner distanceSpin; 
	
	private JDialog dialog;
	
	private JPanel askMePanel;
	private JPanel buttonLeftPanel;
	private JPanel buttonRightPanel;

	private JButton googleBt;
	private JButton buttonBt;
	private JButton refreshBt;
	private JButton resetBt;
	private JButton landMeBt;
	private JButton askMeBt;
	private JButton delMeBt;


	private JButton searchBt;

	private JEditorPane jEditorPane;
	private HTMLEditorKit kit;
	private Document doc;
	
	private JScrollPane askmeScrollPan;

	private SelectAirport selectAirport;
	
	
	public JPanel getPanel() {
		this.selectAirport = new SelectAirport();
		
		distanceSpin = new DistanceSpinner();
		distanceSpin.initPanelDistances("icao");
		
        jEditorPane = new JEditorPane();
		jEditorPane.setEditable(false);
		kit = new HTMLEditorKit();
		jEditorPane.setEditorKit(kit);
		jEditorPane.setVisible(false);
        jEditorPane.addHyperlinkListener(new HyperlinkListener() {

            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                HyperlinkEvent.EventType type = e.getEventType();
                final URL url = e.getURL();
                if (type == HyperlinkEvent.EventType.ENTERED) {
                    // do desired highlighting
                } else if (type == HyperlinkEvent.EventType.ACTIVATED) {
                	try {
						Util.openURL(url.toString());
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						//e1.printStackTrace();
					}
                }
            }
          });
        
  	    result = new Result();


	askMePanel = new JPanel(new BorderLayout());
	askMePanel.setBorder(new TitledBorder(""));
	
	askmeScrollPan = new JScrollPane(jEditorPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
	askMePanel.add(askmeScrollPan);
	
	icaoPanel = new JPanel();
	icaoPanel.setLayout(null);

	outputPanel = new JPanel(new BorderLayout());
	outputPanel.setBorder(new TitledBorder("List Result"));
	result.setOutputPanel(outputPanel);

	panelResult = new JPanel(new BorderLayout());
	panelResult.setBorder(new TitledBorder("Search Result"));
	
	googleBt = new JButton("Land All to Google Earth");
	googleBt.setEnabled(false);
	googleBt.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e)

		{
			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
				protected Void doInBackground() {

					readData.creatIcaoAirports(Utility.getInstance().getIcaoFromMapAirport(result.getMapAirport()),
							new Distance((int) distanceSpin.getCitySpinner().getValue(),
									(int) distanceSpin.getMountainSpinner().getValue(), 0,
									(int) distanceSpin.getVorNdbSpinner().getValue(),
									(int) distanceSpin.getLandkmarkSpinner().getValue(),
									distanceSpin.getCheckLinedist().isSelected(), 0.0));

					return null;
				}

				@Override
				protected void done() {
					dialog.dispose();
				}
			};
			worker.execute();
			dialog.setVisible(true); // will block but with a responsive GUI

			setResultPanel();
			Utility.getInstance().launchGoogleEarth(new File(Utility.getInstance().getFlightPlanName(Info.kmlFlightplanName)));
		}
	});


	final JTextArea textArea = new JTextArea();
	//textArea.setFont(textArea.getFont().deriveFont(14f));
	textArea.setText("");
	setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	textArea.setColumns(100);
	textArea.setLineWrap(true);
	textArea.setRows(1000);
	textArea.setWrapStyleWord(true);
	textArea.setPreferredSize(new Dimension(300,500));
	
	textArea.getDocument().addDocumentListener(new DocumentListener() {

        @Override
        public void removeUpdate(DocumentEvent e) {
        	resetBt.setEnabled(!"".equals(textArea.getText()));
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
        	resetBt.setEnabled(!"".equals(textArea.getText()));

        }

        @Override
        public void changedUpdate(DocumentEvent arg0) {
        	resetBt.setEnabled(!"".equals(textArea.getText()));

        }
    });

	JPanel inputIcaoPanel = new JPanel(new BorderLayout());
	inputIcaoPanel.setBorder(new TitledBorder("Type or Paste you ICAO Code(s) here"));
	inputIcaoPanel.add(new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
	inputIcaoPanel.setVisible(true);
	inputIcaoPanel.setPreferredSize(new Dimension(300,150));
    Border border = inputIcaoPanel.getBorder();
	Border margin = new EmptyBorder(3,3,3,3);
	inputIcaoPanel.setBorder(new CompoundBorder(border, margin));

	
	resetBt = new JButton("Reset");
	resetBt.setEnabled(false);

    resetBt.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
			distanceSpin.getCitySpinner().setValue(0);
			distanceSpin.getVorNdbSpinner().setValue(0);
			distanceSpin.getMountainSpinner().setValue(0);
			distanceSpin.getLandkmarkSpinner().setValue(0);
			distanceSpin.getCheckLinedist().setSelected(false);

			if (readData != null) {
				readData.resetIcaoResult();
				panelResult.removeAll();
				result.getIcaoFormPanel().validate();
				result.getAirportListModel();

				panelResult.add(result.getIcaoFormPanel());
				result.resetButton();
				
			}
			

			textArea.setText("");
			
			googleBt.setEnabled(false);
			resetBt.setEnabled(false);


			panelResult.validate();
			icaoPanel.validate();
   	  

      }
    });
	dialog = UtilityEarthAnimation.getInstance().panelWait();

    searchBt = new JButton("Search");
	searchBt.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  SwingWorker<Void,Void> worker = new SwingWorker<Void,Void>()
	  		{
	  		    protected Void doInBackground()
	  		    {

	    	  
	 	    	  readData = new ReadData(textArea.getText(),result, 
    			  new Distance(
    			     (int)distanceSpin.getCitySpinner().getValue(), 
    				 (int)distanceSpin.getMountainSpinner().getValue(), 
    				 0,
    				 (int)distanceSpin.getVorNdbSpinner().getValue(), 
   				     (int)distanceSpin.getLandkmarkSpinner().getValue(), 
    				 distanceSpin.getCheckLinedist().isSelected(),
    				 0.0));
     
  		        return null;
  		    }
  		 
  		    @Override
  		    protected void done()
  		    {
  		        dialog.dispose();
  		    }
  		};
  		worker.execute();
  		dialog.setVisible(true); // will block but with a responsive GUI		         
	    	  
			  result.getAirportListModel();

	    	  
	    	//  setIcaoResult();
		  	 //result.getAirportListModel();

	    	 setResultPanel();
			 googleBt.setEnabled(true);	
			 resetBt.setEnabled(true);
			 
			 icaoPanel.validate();

	      }
	    });

	landMeBt = new JButton("Land Me");
	landMeBt.setEnabled(false);
	landMeBt.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			result.landMe();
		}
	});		

	askMeBt = new JButton("Ask Me");
	askMeBt.setEnabled(false);
	
	askMePanel.setVisible(false);

	 delMeBt = new JButton("Del Me");
	 delMeBt.setEnabled(false);
	 delMeBt.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e)

		{
			String key;
			ListModel<?> dlm = (ListModel) result.getCurrentList().getModel();

			if (result.getCurrentList().getSelectedIndices().length > 0) {
				int[] tmp = result.getCurrentList().getSelectedIndices();
				int[] selectedIndices = result.getCurrentList().getSelectedIndices();

				for (int i = tmp.length - 1; i >= 0; i--) {
					selectedIndices = result.getCurrentList().getSelectedIndices();
				     key = Utility.getInstance().findKeyICAO( (String) result.getCurrentList().getSelectedValue());

					result.getMapAirport().remove(key);
					((DefaultListModel)result.getCurrentList().getModel()).remove(selectedIndices[i]);

				}
			}
			result.getCurrentList().clearSelection();

			//result.setAirports(listIcao.getModel().getSize());
			setResultPanel();

		}
	});
	 
    result.setButtons(delMeBt,landMeBt, askMeBt);
    result.setjEditorPane(jEditorPane);
    result.setAskmeScrollPan(askmeScrollPan);
	result.setAskMePanel(askMePanel);
	result.setOutputPanel(outputPanel);

	
	int x = 330;
	int y = 370;
	
	panelResult.setBounds(x, 10, 340, 190);	
  	outputPanel.setBounds(x, 210, 340, 150);	
  	//askMePanel.setBounds(x, 210, 340, 150);	
  	askMePanel.setBounds(x, 10, 340, 190);	

  	landMeBt.setBounds(x, y, 82, 23);
	askMeBt.setBounds(x+130, y, 88, 23);
	delMeBt.setBounds(x+258, y, 82, 23);
  	
	distanceSpin.getSpinnerPanel().setBounds(10, 10, 310, 190);
	inputIcaoPanel.setBounds(10, 200, 310, 120);
    searchBt.setBounds(30, 330, 125, 23);
	resetBt.setBounds(180, 330, 125, 23);
	googleBt.setBounds(90, 370, 180, 23);
	
	icaoPanel.add(inputIcaoPanel);
	icaoPanel.add(distanceSpin.getSpinnerPanel());
	icaoPanel.add(outputPanel);
	icaoPanel.add(panelResult);
	icaoPanel.add(askMePanel);
	icaoPanel.add(searchBt);
	icaoPanel.add(resetBt);
	icaoPanel.add(delMeBt);
	icaoPanel.add(landMeBt);
	icaoPanel.add(askMeBt);
	icaoPanel.add(googleBt);
	
	return icaoPanel;
	}

	/**
	 * 
	 */
	private void setResultPanel() {
		panelResult.removeAll();
		panelResult.add(result.getIcaoFormPanel());
		panelResult.setVisible(true);
		panelResult.validate();

		icaoPanel.add(panelResult);
	}

	
}
