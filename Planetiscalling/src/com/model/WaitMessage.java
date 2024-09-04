package com.model;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

import com.cfg.common.Info;
import com.main.PlanetIsCalling;

public class WaitMessage implements Info{
	
	private JFrame frame;
	private Timer timer;
	
	
	
	//http://stackoverflow.com/questions/18909203/jlabel-text-gets-overwritten-on-transparent-bg

    public static void main(String[] args) {
       // new WaitMessage(true);
    }

    public WaitMessage(final Boolean typeofwait) {
    	String msg = "Launching...";
    	if (typeofwait == null){
    		msg = "Loading Addon Data..."; 
    	} else if (!typeofwait){
    		msg = "";
    	} else if (typeofwait){
    		msg = "One Moment Please...";
    	}
    	
    	final String message = msg;
    	
        EventQueue.invokeLater(new Runnable() {

            public void run() {
            	final JLabel label = new JLabel(message);
            	
            	if (typeofwait == null ||typeofwait) {
                	timer = showLauchhing(label);
            		
            	} else if(!typeofwait) {
                	timer = showCount(label);
            	}

                frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.setUndecorated(true);
                frame.setBackground(new Color(0, 255, 0, 0));
                frame.add(label);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
    
    
    private Timer showLauchhing( final JLabel label){
        
		Font labelFont = label.getFont();
		label.setFont(new Font(labelFont.getName(), Font.ITALIC, 30));
		
        timer = new Timer(300, new ActionListener() {
        	int numcolor = 0;
            public void actionPerformed(ActionEvent e) {
            	label.setForeground(colorForground[numcolor]);
            	
            	numcolor = (++numcolor % (colorForground.length-1) );
            }
        });
        timer.start();
  	
    	return timer;
    }
    
    private Timer showCount(final JLabel label){
        
 		Font labelFont = label.getFont();
 		label.setFont(new Font(labelFont.getName(), Font.BOLD, 30));
 		
         timer = new Timer(800, new ActionListener() {
         	int numcolor = 10;
             public void actionPerformed(ActionEvent e) {
             //	label.setForeground(colorForground[numcolor]);
             	label.setText(""+numcolor);
             	numcolor--;
             	if (numcolor == 0){
             		timer.stop();
             	}
             }
         });
         timer.start();
   	
     	return timer;
     }

    
    public void setVisible(boolean show){
    	frame.setVisible(show);
    	timer.stop();
    }
}