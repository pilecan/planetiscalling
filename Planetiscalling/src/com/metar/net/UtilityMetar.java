package com.metar.net;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

import com.cfg.common.Info;
import com.metar.decoder.Decoder;
import com.metar.download.Download;
import com.metar.download.ReadFile;
import com.util.Util;


public class UtilityMetar implements Info{
	private static UtilityMetar instance = new UtilityMetar();
	
	private boolean isMetarLoaded; 
	File in = new File("data/metar/metar.txt");
	
	private String raw = null;
	private String metarDecoded = null;
	
	private static long oneHourMillis = 3600000;
	//private static long oneHourMillis = 20000;

	
	public static UtilityMetar getInstance(){

		return instance;
	}
	
	public String getMetar (String icao) {
		ReadFile read = new ReadFile(in);
		Decoder decoder = new Decoder();
		
		validMetar();

		raw = null;
		metarDecoded = null;
		try {
			String allLine = decoder.saveAllLine(read, icao);
			raw = decoder.showRawMetar(allLine);
			metarDecoded = decoder.showDecoded(allLine);
			System.out.println(raw);
			System.out.println(metarDecoded);
			metarDecoded += "<br>"+raw;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println("metar error "+e);
		}
		
		return metarDecoded;

	}
	
	private boolean isNeedUpdate() {
		 Date date = new Date();
         Path file = Paths.get(metarFileName);
         long lastModifiedTime = 0; 
         try {
			BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
			lastModifiedTime = attr.lastModifiedTime().toMillis();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	      long timeMillisNow = date.getTime();

	      
	      return (timeMillisNow > oneHourMillis+lastModifiedTime) ;
	}
	
	public void validMetar() {
		
		if (isNeedUpdate()) {
			loadMetar();
		} else {
			System.out.println("meta was loaded");
		}
	}

	
	private boolean loadMetar() {

	        try {

	            Path file = Paths.get(metarFileName);

	            BasicFileAttributes attr =
	                Files.readAttributes(file, BasicFileAttributes.class);

	            System.out.println("creationTime: " + attr.creationTime().toMillis());
	         //   String dateCreated = df.format(attr.creationTime().toMillis());

	            System.out.println("creationTime: " + attr.creationTime());
	            System.out.println("lastAccessTime: " + attr.lastAccessTime());
	            System.out.println("lastModifiedTime: " + attr.lastModifiedTime());
	            System.out.println("lastModifiedTime: " + attr.lastModifiedTime().toMillis());
	            
	            Util.getDateTime(attr);

	        } catch (IOException e) {
	           // e.printStackTrace();
	        }
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					String link = "https://aviationweather.gov/adds/dataserver_current/current/metars.cache.csv";
					File out = new File("data/metar/metar.txt");
					Download download = new Download(link, out);
					download.start();
					Decoder decoder = new Decoder();
					isMetarLoaded = true;
					
				} catch (Exception e) {
					//e.printStackTrace();
				}
				
			}
		});
		
		return isMetarLoaded;
		
	}

	public String getMetarDecoded() {
		return metarDecoded;
	}

	public void setMetarDecoded(String metarDecoded) {
		this.metarDecoded = metarDecoded;
	}
	  

   
}
