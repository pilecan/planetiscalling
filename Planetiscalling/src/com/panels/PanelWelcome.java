package com.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;

import com.cfg.common.Info;
import com.util.Util;
import com.util.Utility;
import com.util.UtilityEarthAnimation;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class PanelWelcome extends Thread implements ActionListener, Info {
	private JPanel	panelWelcome;
	private JLabel labelImage;
	private JLabel labelText;
	private JLabel labelWelcome;
	private JLabel labelSlogan;
	private JLabel labelClickMe;
	private JLabel message;
	int y = 100;
	private int direction = 0;
	private Timer t1; 
	private Thread thread;
	int x;
	
	private volatile boolean isPlaysound;
	
	private JEditorPane jEditorPane;
	private JScrollPane askmeScrollPan;
	private JPanel askMePanel;

	private static Document doc;
	private static HTMLEditorKit kit;

	private void sysout() {
		// TODO Auto-generated method stub

	}
	public JPanel getPanel()
	{

		message = new JLabel("");
		System.out.println(Util.getComputerName());
		System.out.println("cicicic");
		
		if (!Util.isFileExist((String)Utility.getInstance().getPrefs().get("googleearth"))) {
			message.setText("Important! Select Google Earth (googleearth.exe) program in the Settings Panel." );
			message.setForeground(Color.PINK);
			message.setFont(new Font("Myriad Pro",Font.PLAIN,16));
		}
		
		jEditorPane = Utility.getInstance().initjEditorPane(jEditorPane);
		
		showPanelAbout(getAbout());		

    	askmeScrollPan = new JScrollPane(jEditorPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
     	   public void run() { 
     		   askmeScrollPan.getVerticalScrollBar().setValue(0);
     	   }
     	});
        
		askMePanel = new JPanel(new BorderLayout());
		askMePanel.setBorder(new TitledBorder(""));
			
		askMePanel.add(askmeScrollPan);
		
		Icon imgIcon = new ImageIcon(this.getClass().getResource(imageEarth));
		labelImage = new JLabel(imgIcon);
		labelImage.setToolTipText("Click Me");
		labelText = new JLabel("Planet Is Calling                      ");
		labelText.setForeground(new Color(26,108,26));
		
		labelWelcome = new JLabel("Welcome to Planet is Calling");
		labelSlogan = new JLabel("Another Way to Fly Over the Planet");
		labelClickMe = new JLabel("Click on the Marble");
		labelWelcome.setBounds(180,20, 500, 35);
		labelSlogan.setBounds(230,65, 500, 25);
		labelClickMe.setBounds(300,300, 500, 25);
		labelWelcome.setForeground(new Color(255,196,52,0));
		labelSlogan.setForeground(new Color(255,196,52,0));
		labelClickMe.setForeground(new Color(255,196,52,0));
		labelWelcome.setOpaque(false);
		labelSlogan.setOpaque(false);
		labelClickMe.setOpaque(false);
		labelWelcome.setFont(new Font("Myriad Pro",Font.PLAIN,28));
		labelSlogan.setFont(new Font("Myriad Pro",Font.PLAIN,16));
		labelSlogan.setFont(new Font("Myriad Pro",Font.PLAIN,16));

		int x = 0;
		labelText.setBounds(276+x, 95+y, 220, 14); 
		labelImage.setBounds(250+x, y, 200, 200); 
		askMePanel.setBounds(100+x, 455, 500, 300); 
		jEditorPane.setBounds(100+x, 455, 300, 300);
		message.setBounds(80, 420, 580, 20);

		panelWelcome = new JPanel();
		panelWelcome.setLayout(null);

		panelWelcome.add(labelWelcome);
		panelWelcome.add(labelSlogan);
		panelWelcome.add(labelClickMe);
		panelWelcome.add(labelText);
		panelWelcome.add(labelImage);
		panelWelcome.add(askMePanel);
		panelWelcome.add(message);

	
		Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
		labelImage.setCursor(cursor);
		
	    t1 = new Timer(150, this);
	    t1.start();
	    
	    MouseListener ml = new MouseAdapter()
	    {
	        @Override
	        public void mousePressed(MouseEvent e)
	        {
	        	labelImage = (JLabel)e.getSource();
	        	
	        	System.out.println("boom");
	        	if (direction == 0 || direction == 1){
		        	direction = -1;
		        	y= 270;
					labelText.setBounds(276, 95+y, 220, 14); 
					labelImage.setBounds(250, y, 200, 200); 
					labelWelcome.setForeground(new Color(255,196,52,0));
					labelSlogan.setForeground(new Color(255,196,52,0));
					labelClickMe.setForeground(new Color(255,196,52,0));
					labelClickMe.setVisible(false);

					isPlaysound = true;


	        	} else 
	        	if (direction == -1){
		        	direction = 1;
					askMePanel.setVisible(false);
	        	} 

        	

	        }
	    };
	    labelImage.addMouseListener( ml );

/*		thread =new Thread(this);  
		thread.start();
*/		
		return panelWelcome;
		
	}

	public void actionPerformed(ActionEvent e) {

	      String oldText = labelText.getText();
	      String newText= oldText.substring(1)+ oldText.substring(0,1);
	      labelText.setText(newText);
	      if (direction == -1) {
	    	  if (y > -85) {
	    		  if (y == 270) {
					askMePanel.setVisible(true);
 	    		  }
	    		//  System.out.println(y);
	  		    y -= 3;
				labelText.setBounds(276, 95+y, 220, 14); 
				labelImage.setBounds(250, y, 200, 200); 
				askMePanel.setBounds(100, y+200, 500, 300); 
	    	  }
	      } else 	  
	      if (direction == 1) {
	    	  if (y <= 100) {
		  		    y += 3;
					labelText.setBounds(276, 95+y, 220, 14); // for example, you can use your own values
					labelImage.setBounds(250, y, 200, 200); // for example, you can use your own values
		    		  
		    	  } 
	    	  
	      }
          if(x<=240) {
        	  x += 14;
	           labelWelcome.setForeground(new Color(255,196,52,x));
   	           labelWelcome.setBackground(new Color(26,108,26,x));
               labelSlogan.setForeground(new Color(255,196,52,x));
	           labelSlogan.setBackground(new Color(26,108,26,x));
               labelClickMe.setForeground(new Color(255,196,52,x));
	           labelClickMe.setBackground(new Color(26,108,26,x));
          } else {
           	  x -= 10;
 	      //    labelWelcome.setForeground(new Color(255,196,52,x));
   	      //    labelWelcome.setBackground(new Color(26,108,26,x));
      	  
          }

	   }
	
	public void showPanelAbout( String content) {
		kit = new HTMLEditorKit();
		doc = kit.createDefaultDocument();
        jEditorPane.setDocument(doc);
    	
		jEditorPane.setVisible(true);
		
	//	resultPanel.setVisible(false);
		
		
		String setColor[] = {"#E6E6E6","ff0000"};
		
        Random rand = new Random(); 
        int rand_int1 = rand.nextInt(setColor.length); 
		String color = setColor[0];

		try {
			jEditorPane.setContentType("text/html");
			jEditorPane.setText("<html><body style='font-weight: bold; width: 200px; color: #"+color+"; background-position: 200px 0;'>"+content+"</body></html>");
            jEditorPane.validate();;

		} catch (Exception e) {
			e.printStackTrace();
		}
			
	       // jEditorPane.setText(content);
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
        	   public void run() { 
        		   askmeScrollPan.getVerticalScrollBar().setValue(0);
        	   }
        	});

    
    

}

	
	private String getAbout() {
		
		return "<html><body>"
				+ "Planet Is Calling (PIC) provide information from half million of Points of interest that you can see within the application or land them on Google Earth from flight plans, ICAOs, Weather Cities and airports, Airport cities, Country cities, High Peaks and Volcanos from all around the World!<br>" + 
				"<br>PIC is also: <br>" + 
				"<div style='display: block; width:350px; clear: left; font-size: medium; font-weight: bold; padding: 0pt 0pt;'> "
				+"<li>All 36 973 airports of MSFS2020, 87388 runways and 4076 ILS information (ATIS, Length, Altitude, Frequencies, Courses, etc.)"+
				"<li>All 4 756 VOR information (Position, Frequency, Range, Altitude, etc.)" + 
				"<li>All 764 NDB information (Position, Frequency, Range, Altitude, etc.)<br>" + 
				"<i>Source</i>: "+Util.createLink("https://www.flightsimulator.com/", "flightsimulator.com") + 
				"<li>26 562 cities information (Prominent cities (large, Capital, Population, State, Regions, Weather, Link to Wikipedia and communities etc.)<br>" + 
				"<i>Source</i>: "+Util.createLink("https://simplemaps.com/data/world-cities","simplemaps.com") + 
				"<li>209 579 cities with live weather information and link to Wikipedia that you can see within application and on Google Earth<br>" + 
				"<i>Source</i>: "+Util.createLink("https://openweathermap.org/","openweathermap.org")+"*" + 
				"<li>2 500+ peaks and volcanos information (Position, Elevation, Country, Type, Weather, Last activities, Link to Wikipedia and communities, etc.)<br>" + 
				"<i>Source</i>: "+Util.createLink("https://public.opendatasoft.com/","opendatasoft.com" )+ 
				"<li>5 000+ METAR information update on time from NOAA.<br>" + 
				"<i>Source</i>: "+Util.createLink("https://www.noaa.gov/weather","noaa.gov") + 
				"<li>351 152 Canada Landmark informations(only Canada for now) by Province, City, Park, Natural and Indian Reserves, Mountains, Caves, Under Sea, Military sites, Lakes, All with Nearest Airport and Cities with Live Weather!)<br>" + 
				"<i>Source</i>: "+Util.createLink("https://www.nrcan.gc.ca/earth-sciences/geography/download-geographical-names-data/9245?_ga=2.52506344.1741568538.1611667934-1542046856.1611417811","https://www.nrcan.gc.ca") + 
				"</div>" + 
				"</body></html)";
		//https://discord.gg/DjJWFAZ2
	}
	
	
	
	@Override
	public void run() {
        while(true) 
        {
    		if (isPlaysound) {
    			UtilityEarthAnimation.getInstance().playSound(soundAbout);
    	        isPlaysound = false;
    		}
        	
        }

	}
	
}
