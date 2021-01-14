package com.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

import com.cfg.common.Info;
import com.db.SelectDB;
import com.main.PlanetIsCalling;


public class UtilityTimer implements Info, Runnable {
	
	private JFrame planetIsCalling;
	private String timeUTC;
	private String timeLocal;
	private long millis = 0;
	private String timer = "";
	private String localAbbreviation;
	
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
  
		public  String getTime(String where ) {
			TimeZone timeZone = null ;
			if ("local".equals(where)){
		    	timeZone = TimeZone.getDefault();
			} else if ("UTC".equals(where)){
				timeZone = TimeZone.getTimeZone("UTC");
			}

			
			Calendar calUTC = new GregorianCalendar(timeZone);
	        int hour = calUTC.get(Calendar.HOUR_OF_DAY);
	        int min = calUTC.get(Calendar.MINUTE);
	        int sec = calUTC.get(Calendar.SECOND);
	        
	        if (min ==0 && sec == 0 ) {
	        	System.out.println(getPeriod());
	        }
		
	        //return String.format("%02d",hour) + ":" +  String.format("%02d",min) + ":" + String.format("%02d",sec);
	        return String.format("%02d",hour) + ":" +  String.format("%02d",min);

			
		}
		  public String getPeriod() {
			  String dayPeriod = "";
			  String[] currentTimes = getTime("local").split(":");
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
				TimeZone timeZone = TimeZone.getDefault();
				
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
		        Thread t1 =new Thread(this);  
		        t1.start();
		        
		        this.planetIsCalling = planetIsCalling;
		  }

		@Override
		public void run() {
			   
		       Object o =  planetIsCalling;
		       PlanetIsCalling mc = null;
		        if(o != null && o instanceof PlanetIsCalling)
		        	mc = (PlanetIsCalling) o;
		        mc.setTitle("Hello Word!");
		        Util.pause(2000);
	  
	        while(true) 
	        {
	        	
	        	millis += 1000;
	        	timer = getTimer(millis);
	            timeUTC = getTime("UTC");
	            timeLocal = getTime("local");
	            //delay the loop for 1 sec
	            try {
	                Thread.currentThread().sleep(1000);
	                mc.setTitle("The Planet Is Calling 0.910"
	               +"                                                               "+ timeUTC+ " UTC    "
	               +timeLocal+" "+localAbbreviation+"                "+timer);
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
   
}
