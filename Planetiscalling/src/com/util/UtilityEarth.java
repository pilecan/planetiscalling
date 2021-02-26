package com.util;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.plaf.ColorUIResource;

import com.cfg.common.Info;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class UtilityEarth implements ActionListener , Info{
	private static UtilityEarth instance = new UtilityEarth();
	
	private JPanel	panelWelcome;
	private JLabel labelImage;
	private JLabel labelText;
	private JFrame parent;
	int y = 77;
	private Player player;


      
	public static UtilityEarth getInstance() {
		return instance;
	}
	
	
	public JPanel createEarth()
	{
		
		Icon imgIcon = new ImageIcon(this.getClass().getResource(imageEarth));
		labelImage = new JLabel(imgIcon);
		labelText = new JLabel("Planet Is Calling                      ");
		labelText.setForeground(new ColorUIResource(26,108,26));

		int x = 0;
		labelText.setBounds(276+x, 95+y, 220, 14); // for example, you can use your own values
		labelImage.setBounds(250+x, y, 200, 200); // for example, you can use your own values
		
		panelWelcome = new JPanel();
		panelWelcome.setLayout(null);

		panelWelcome.add(labelText);
		panelWelcome.add(labelImage);
		
		Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
		labelImage.setCursor(cursor);
		
		panelWelcome.setPreferredSize(new Dimension(200,200));

		
	    Timer t = new Timer(150, this);
	    t.start();
	    
	    MouseListener ml = new MouseAdapter()
	    {
	        @Override
	        public void mousePressed(MouseEvent e)
	        {
	        	labelImage = (JLabel)e.getSource();
	        	
	        	System.out.println("boom");

        	

	        }
	    };
	    labelImage.addMouseListener( ml );
  
		
		return panelWelcome;
		
	}
	
	public void actionPerformed(ActionEvent e) {
	      String oldText = labelText.getText();
	      String newText= oldText.substring(1)+ oldText.substring(0,1);
           
	      labelText.setText(newText);
	   }
	public void initPanelWait(JFrame parent) {
		this.parent = parent;
	}
	public JDialog panelWait() {
		final JDialog dialog = new JDialog(parent, true); // modal
		dialog.setUndecorated(true);
		JProgressBar bar = new JProgressBar();
		bar.setIndeterminate(true);
		bar.setStringPainted(true);
		bar.setString("Planet is Scanning...");
		dialog.add(bar);
		dialog.pack();
		dialog.setLocationRelativeTo(parent);

		return dialog;
		
	}
	
	public void hideEarth() {
		panelWelcome.setVisible(false);
		panelWelcome.setVisible(false);
			
	}
	public void showEarth() {
		panelWelcome.setVisible(true);
		panelWelcome.setVisible(true);
			
	}

	public void playSound(String sound) {
    	URL url = null;
/*
       	try {
			File soundfile = new File(sound);
			//url = background.toURI().toURL();
			try {
				url  = soundfile.getCanonicalFile().toURI().toURL();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			sound = url.toString().replace("file:/", "").replace("%20", " ");
    			FileInputStream fileInputStream = new FileInputStream(sound);
    			
    			if (player != null){
    				player.close();
    			}
    			
    			player = new Player(fileInputStream);
    			player.play();

    		} catch (FileNotFoundException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (JavaLayerException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
*/       	
	}

	

	public JPanel getPanelWelcome() {
		return panelWelcome;
	}


	public void setPanelWelcome(JPanel panelImage) {
		this.panelWelcome = panelImage;
	}


	public Player getPlayer() {
		return player;
	}


	public void setPlayer(Player player) {
		this.player = player;
	}

			
	
		
}
