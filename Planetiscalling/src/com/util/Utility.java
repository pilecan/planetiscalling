package com.util;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
import javax.swing.JList;

import com.cfg.common.Info;
import com.cfg.util.Util;
import com.main.PlanetIsCalling;


public class Utility implements Info{
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
    
    
	public void changeAltitude(String filePlan, Double cruzeAltitude, Double newAltitude) throws IOException {
		
		String flightplan = new String(Files.readAllBytes(Paths.get(filePlan)), StandardCharsets.UTF_8);
		boolean isLastWpAirport = false;
		String[] row = flightplan.split("\\r\\n");
		
		for (int i = 0; i < row.length; i++) {
			
			if (row[i].contains("ATCWaypointType")) {
				isLastWpAirport = (row[i].contains("Airport"));
			}

			if (row[i].contains("CruisingAlt")) {
			
				row[i] = row[i].replace(cruzeAltitude.intValue()+"", newAltitude.intValue()+"");
			}

			if (row[i].contains("WorldPosition") && !isLastWpAirport) {
				String[] line = row[i].split(",");
				String strAltitude = line[2].replace("</WorldPosition>", "");
				System.out.println(strAltitude +" -> "+Util.formatAltitude(Util.modifyAltitude(cruzeAltitude, Double.parseDouble(strAltitude), newAltitude)));
				row[i] = row[i].replace(strAltitude, Util.formatAltitude(Util.modifyAltitude(cruzeAltitude, Double.parseDouble(strAltitude), newAltitude)));
			}
		}
		
		System.out.println(row.length);
		
		saveNewFlightPlan(filePlan, row);
		
		//System.out.println(Util.formatAltitude(Util.modifyAltitude(cruzeAltitude, +002257.00, newAltitude)));

	}
	
	
	public void saveNewFlightPlan(String flightPlanFile, String[] row){
		Writer writer = null;
		
 			
			try {

				writer = new BufferedWriter(new OutputStreamWriter(
				      new FileOutputStream(flightPlanFile), "utf-8"));
				for (int i = 0; i < row.length; i++) {
					writer.write(row[i]+"\n");
				}
				
				//Create KML Header
				
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				   try {writer.close();} catch (Exception ex) {}
			}		
		    
		    
		    
 		}
	
	public String valideIcao(String icaos) {
		icaos = icaos.replaceAll("\\s+"," ");
    	icaos = icaos.replaceAll("\\,", " ");
    	icaos = icaos.replaceAll(">", " ");
    	icaos = icaos.replaceAll("–", " ");
    	icaos = icaos.replaceAll("-", " ");
    	icaos = icaos.replaceAll("\\("," ");
    	icaos = icaos.replaceAll("\\)", " ");
    	icaos = icaos.replaceAll("\\["," ");
    	icaos = icaos.replaceAll("\\]", " ");
    	icaos = icaos.replaceAll("_", " ");
    	icaos = icaos.replaceAll("\\.", " ");
    	icaos = icaos.replaceAll("\\\\", " ");
    	icaos = icaos.replaceAll("'", "");
    	icaos = icaos.replaceAll("=", " ");
    	icaos = icaos.replaceAll(",", " ");
    	icaos = icaos.replaceAll(":", " ");
    	icaos = icaos.replaceAll(";", " ");
    	icaos = icaos.replaceAll("\\.", " ");
    	icaos = icaos.replaceAll("\\|", " ");



	    String[] list = icaos.split(" ");
     	String search = "";		
	    
	    for (String str:list) {
	    	str = str.trim();
	    	
	    	if (str.length()  == 4) {
	    		search += str+" ";
		    	//System.out.println(str);
	    	}
	    	
	    }
	    
	    return search;

	}
	
	public String getIcaoFromListModel( JList listIcao) {
		String result = "";

		for (int i = 0; i < listIcao.getModel().getSize(); i++) {
			result += listIcao.getModel().getElementAt(i).toString().substring(0,5);
		}
		
		return result;
	}

  

	


}
