package com.metar.download;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;


public class Download {

	String link;
	File out;
	double downloaded = 0.00;
	
	public Download(String link, File out) {
		this.link = link;
		this.out = out;
		downloaded = 0;
	}	
	
	public void start() {
		try {
	/*		URL url = new URL(link);
			HttpURLConnection http = (HttpURLConnection)url.openConnection();
			double fileSize = (double)http.getContentLengthLong();
			
			 URL oracle = new URL(link);
		        URLConnection yc = oracle.openConnection();
		        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
		        String inputLine;
		        while ((inputLine = in.readLine()) != null) 
		            System.out.println(inputLine);
		        in.close();
*/			
	
			URL url = new URL(link);
			HttpURLConnection http = (HttpURLConnection)url.openConnection();
			double fileSize = (double)http.getContentLengthLong();
			BufferedInputStream in = new BufferedInputStream(http.getInputStream());
			FileOutputStream fos = new FileOutputStream(this.out);
			BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
			byte[] buffer = new byte[1024];
			double downloaded = 0.00;
			int read = 0;
			double percentDownloaded = 0.00;
			
			while((read = in.read(buffer, 0, 1024)) >= 0) {
				bout.write(buffer, 0, read);
				downloaded += read;
				percentDownloaded = (downloaded*100)/fileSize;
				
				
/*				String percent = String.format("%.4f", percentDownloaded);
				System.out.println("Downloaded " + percent + "% of file.");
*/			}
			bout.close();
			in.close();
			System.out.println("Download complete!");

		
		}catch (Exception ex) {
			System.err.println("Download problem...");

			//ex.printStackTrace();
		}
	}
	
}
