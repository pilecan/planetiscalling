package com.util;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.ColorUIResource;

import com.cfg.common.Info;
import com.main.PlanetIsCalling;

public class UtilityEarth extends Thread  implements Info{
	private static UtilityEarth instance = new UtilityEarth();
	
	private JPanel	panelImage;
	private JLabel labelImage;
	private JLabel labelText;
	private Thread t1;
    private volatile boolean running = true;


      
	public static UtilityEarth getInstance() {
		return instance;
	}
	
	
	public JPanel createEarth()
	{
		
		Icon imgIcon = new ImageIcon(this.getClass().getResource(imageEarth));
		labelImage = new JLabel(imgIcon);
		labelText = new JLabel("Planet Is Calling");
		labelText.setForeground(new ColorUIResource(26,108,26));

		int x = 0;
		int y = 0;
		labelText.setBounds(310+x, 172+y, 106, 14); // for example, you can use your own values
		labelImage.setBounds(250+x, 77+y, 200, 200); // for example, you can use your own values
		
		panelImage = new JPanel();
		panelImage.setLayout(null);

		panelImage.add(labelText);
		panelImage.add(labelImage);
		
		//hideEarth();
		
		return panelImage;
		
	}
	
	public void hideEarth() {
		panelImage.setVisible(false);
		panelImage.setVisible(false);
			
	}
	public void showEarth() {
		panelImage.setVisible(true);
		panelImage.setVisible(true);
			
	}


	public JPanel getPanelImage() {
		return panelImage;
	}


	public void setPanelImage(JPanel panelImage) {
		this.panelImage = panelImage;
	}

	public void startTimer() {
		t1 = new Thread(this);
		t1.start();

	}

	public void terminate() {
		running = false;
	}	  
		
	@Override
	public void run() {
        while(running) 
        {
        	try {
               Thread.currentThread().sleep(1000);
               System.out.println("search...");
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
            }
        }
	}

		

}
