package com.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

import com.cfg.common.Info;
import com.db.SelectDB;
import com.main.PlanetIsCalling;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;


public class UtilityTimer extends Thread implements Info {
	
	private JFrame planetIsCalling;
	private String timeUTC;
	private String timeLocal;
	private long millis = 0;
	private String timer = "";
	private String localAbbreviation;
	private Calendar calUTC;
	private TimeZone timeZone;
	private Thread t1;
	private long sequence = 1000;
	private String timerSartAt = "";
	private int currentPeriod;
	
	private static UtilityTimer instance = new UtilityTimer();
	public static UtilityTimer getInstance(){
		return instance;
	}
	
	 public String getTimer(long millis) {
	 	    String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
	 	            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
	 	            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
	 	    //System.out.println(hms);
	 	    
	 	    return hms;
			 
		 }
  
		public  String getTime(String where, boolean isSecond ) {
			String time = "";
			TimeZone timeZone = null ;
			if ("local".equals(where)){
		    	timeZone = TimeZone.getDefault();
			} else if ("UTC".equals(where)){
				timeZone = TimeZone.getTimeZone("UTC");
			}

			
			try {
				calUTC = new GregorianCalendar(timeZone);
				int hour = calUTC.get(Calendar.HOUR_OF_DAY);
				int min = calUTC.get(Calendar.MINUTE);
				int sec = calUTC.get(Calendar.SECOND);
				
				if (isSecond) {
					time = String.format("%02d",hour) + ":" +  String.format("%02d",min) + ":" +   String.format("%02d",sec);
				} else {
					time = String.format("%02d",hour) + ":" +  String.format("%02d",min);

				}
			} catch (Exception e) {
				time = "????";
				//e.printStackTrace();
			}
	        
	        return time;

			
		}
		  public String getPeriod() {
			  String dayPeriod = "";
			  String[] currentTimes = getTime("local",false).split(":");
			  int hour = Integer.parseInt(currentTimes[0]);
	  
			  if (hour >= 6 && hour < 12 ) {
				  dayPeriod = "Morning";
			  } else if (hour >= 12 && hour < 17 ) {
				  dayPeriod = "Afternoon";
			  }else if (hour >= 17 && hour < 20 ) {
				  dayPeriod = "Evening";
			  }else if (hour >= 20) {
				  dayPeriod = "Night";
			  }
			  
			  return dayPeriod;
		  }
		  public void initTimer( ) {
				timeZone = TimeZone.getDefault();
				String name = Util.validgetDisplayName(timeZone.getDisplayName());
				SelectDB selectDB = new SelectDB();
				selectDB.selectTimeZone(name);
				
				try {
					localAbbreviation = selectDB.getTimeZones().getAbbr();
				} catch (NullPointerException e1) {
					System.out.println("Oups "+ timeZone.getDisplayName());
					localAbbreviation = timeZone.getDisplayName();
				}

		  }

		  public void startTimer(PlanetIsCalling planetIsCalling) {
		        t1 =new Thread(this);  
		        t1.start();
		        
		        this.planetIsCalling = planetIsCalling;
		  }

			
		  
			@Override
			public void run() {
				   
			       Object o =  planetIsCalling;
			       PlanetIsCalling mc = null;
			        if(o != null && o instanceof PlanetIsCalling)
			        	mc = (PlanetIsCalling) o;
			        mc.setTitle("Good "+Util.getPeriod()+" World!");
			        UtilityEarthAnimation.getInstance().playSound(soundIntro);
			      //  Util.pause(2000);
		  
		        while(true) 
		        {
		        	
		        	millis += 1000;
		        	timer = getTimer(millis);
		            timeUTC = getTime("UTC",false);
		            timeLocal = getTime("local",false);
		            //delay the loop for 1 sec
		            try {
		                Thread.currentThread().sleep(sequence);
		                mc.setTitle("The Planet Is Calling 0.910" + getTime("UTC",false)
		               +"                                    "+ timeUTC+ " UTC    "
		               +timeLocal+" "+localAbbreviation+"             "+timer);

		                } catch (InterruptedException e) {
		                    // TODO Auto-generated catch block
		                    e.printStackTrace();
		                }
		        }
			
			}

		public long getMillis() {
			return millis;
		}

		public void setMillis(long millis) {
			this.millis = millis;
		}

		public String getTimerSartAt() {
			return timerSartAt;
		}

		public void setTimerSartAt(String timerSartAt) {
			this.timerSartAt = timerSartAt;
		}

		public int getCurrentPeriod() {
			return currentPeriod;
		}

		public void setCurrentPeriod(int currentPeriod) {
			this.currentPeriod = currentPeriod;
		}
   
}
