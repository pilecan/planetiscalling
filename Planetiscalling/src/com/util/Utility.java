package com.util;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.main.PlanetIsCalling;


public class Utility {
	private static Utility instance = new Utility();
    private Properties prefs;
    
    List <String>listDirectories = null;


	public static Utility getInstance(){
		return instance;
	}
	
	public  void readPrefProperties(){
		
		prefs = new Properties();
    	
	     Path currentRelativePath = Paths.get("");
	     String file = currentRelativePath.toAbsolutePath().toString()+"\\data\\preferences.properties";
	     try {
	    	 BufferedReader is = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file), "UTF8"));
			   prefs.load(is);
				try {
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
	     } catch (IOException e) { 
	         System.err.println ("Properties error "+e);
	     } catch (NullPointerException e) { 
	         System.err.println ("Properties null "+e);
	     }
	     
	    
	}
	
	   public void savePrefProperties(){
		   Writer out = null;
	   		Path currentRelativePath = Paths.get("");
	   		String file = currentRelativePath.toAbsolutePath().toString()+"\\data\\preferences.properties";
		    File f = new File(file);
	   		
	 		try {
		        out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "utf-8"));
		        prefs.store(out, "Planetiscalling Application Preferences");

			} catch (IOException ex) {
					  System.err.println(ex.getMessage());
			} finally {
			   try {out.close();} catch (Exception ex) {}
			}			
	    }

	public List<String> geListDirectories(String dirSource) {
		listDirectories = new LinkedList<String>();

		File name = new File(dirSource);

		if (name.isDirectory()) {
			listDirectories = new ArrayList<String>(Arrays.asList(name.list()));
		}

		return listDirectories;

	}
	public void readFileOrDirectoy(String dirSource) {
		File name = new File(dirSource);

		if (name.exists()) {
/*			System.out.println(name.getName()
					+ " exists\n"
					+ (name.isFile() ? "is a file\n" : "is not a file\n")
					+ (name.isDirectory() ? "is a directory\n"
							: "is not a directory\n")
					+ (name.isAbsolute() ? "is absolute path\n"
							: "is not absolute path\n") + "Last modified: "
					+ name.lastModified() + "\nLength: " + name.length()
					+ "\nPath: " + name.getPath() + "\nAbsolute path: "
					+ name.getAbsolutePath() + "\nParent: " + name.getParent());
*/
			if (name.isFile()) {
				try {
					RandomAccessFile r = new RandomAccessFile(name, "r");

					StringBuffer buf = new StringBuffer();
					String text;
					//System.out.println( "\n" );

					int cpt = 0;
					while ((text = r.readLine()) != null) {
						listDirectories.add(text);
					}
				} catch (IOException e2) {
					System.out.println("Error....");
				}
			} else if (name.isDirectory()) {
				//System.out.println("Lecture du répertoire.");
				listDirectories = new ArrayList(Arrays.asList(name.list()));
					
			}
		}

   }
	public void setIcon(JFrame frame, String image) {
		// jframe.setIconImage(new
		// ImageIcon("C:/projects/fsx/ReadCfgFile/job/FSLauncpad/src/images/bnc-logo.png").getImage()
		// );
		try {
			java.net.URL u = PlanetIsCalling.class.getResource(image);
			// System.out.println(u);
			BufferedImage myPicture = ImageIO.read(u);
			frame.setIconImage(myPicture);
		} catch (IOException e1) {
		}
	}

	   public int openExplorer(String pathScenery){
		    int result = 0;
		    if (pathScenery != null) {
		   		pathScenery = pathScenery.replace("/", "\\")+"\\";

				 try {
					Runtime.getRuntime().exec("explorer /select,  " + pathScenery);
				 } catch (IOException e) {
					  System.err.println(e);
					 result = -1;
				 } catch (Exception e) {
					 result = -1;
					  System.err.println(e);
				 }
				 
		    } else {
		    	result = -1;
		    }
 			 return result;
	    	
	    }
	public Properties getPrefs() {
		return prefs;
	}
	
	public String convertP3DLine(String line){
		if (line == null){
			return null;
		}
		char[] c_arr = line.toCharArray();
		
		StringBuffer sb = new StringBuffer();
		for (char car :c_arr){
			int value = (int)car;
			if (value > 31 && value < 256 || value==13){
				sb.append(car);
			}
		}
		return sb.toString();
	}
	
	public String getKey (Map <String, String> map,String target) {
		String keys = "";
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String k = entry.getKey();
			String v = entry.getValue();
			if (target.equals(v)) {
				keys += k+" ";
			} else if ("".equals(target)) {
				keys += k+" ";
			}
			//System.out.println("Key: " + k + ", Value: " + v);
		}
		return keys;
	}
	
    public String getFlightPlanName(String flightplanName) {
    	if (getPrefs() == null) {
        	readPrefProperties();
    	}
    	flightplanName = getPrefs().getProperty("kmlflightplandir")+"/"+flightplanName;		
		if (flightplanName.contains("/data/")){
 			Path currentRelativePath = Paths.get("");
 			flightplanName = currentRelativePath.toAbsolutePath().toString()+flightplanName.replace("\\", "/");
		}
		
		return flightplanName;
    }

	


}
