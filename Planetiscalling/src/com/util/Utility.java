package com.util;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagLayout;
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
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;
import javax.swing.text.html.HTMLEditorKit;

import com.cfg.common.Info;
import com.db.SelectAirport;
import com.main.PlanetIsCalling;
import com.metar.decoder.Decoder;
import com.metar.download.Download;
import com.model.Airport;


public class Utility implements Info{
	private static Utility instance = new Utility();
    private Properties prefs;
    
    private List <String>listDirectories = null;
    
	private SelectAirport selectAiport;
	
	private StringBuilder builder;

	private String googleEarthExec = "C:/Program Files/Google/Google Earth Pro/client/googleearth.exe";

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
	   		String file = currentRelativePath.toAbsolutePath().toString()+"/data/preferences.properties";
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
    	icaos = icaos.replaceAll("\\*", " ");
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
	
	public String getIcaoFromMapAirport(Map <String, Airport> mapAirport) {
		String icaos = "";
		
		for (String key : mapAirport.keySet()) {
			icaos += key+" ";
		}

		
		return icaos;
	}
	
	public Map <String, Airport> creatMapAirport(Map<String, Airport> selectedAirports){
		String icaos = "(";
		selectAiport = new SelectAirport();
		
		for (String key : selectedAirports.keySet()) {
		    icaos += "'"+key+"',";
		}
		
		icaos += "'') ";

		selectAiport.select("where ident in "+icaos+" ");
		
		return selectAiport.getMapAirport();
	}
	
	
	public StringBuilder buildLine (Object obj1,Object obj2,Object obj3) {
		builder = new StringBuilder();
		builder.append("<html><pre>");
		builder.append(String.format("%s \t %s \t %s", obj1, obj2,obj3));
		builder.append("</pre></html>");
		return 	builder;
	}

	public String findKeyICAO(String line) {
		try {
			line = (String) line.subSequence(0, line.indexOf("\t"));
			line = line.replace("<html><pre>", "").trim();
		} catch (StringIndexOutOfBoundsException e) {
		}
		
		return line;
	
	}
	 
	public String findKeyVor(String line) {
		try {
			line = (String) line.subSequence(0, line.lastIndexOf("\t"));
			line = line.replaceAll("\t", "");
			line = line.replaceAll(" ", "");
			line = line.replace("<html><pre>", "").trim();
		} catch (StringIndexOutOfBoundsException e) {
		}
		
		return line.toUpperCase();
	
	}

	public String findKeyCity(String line) {
		try {
			String[] array = line.split("\t"); 
			line= array[0]+array[2].replace(" ", "");
			line = line.replace("<html><pre>", "").trim();
			line = line.replace("</pre></html>", "").trim();
		} catch (StringIndexOutOfBoundsException e) {
		}
		
		return line.toUpperCase().replace(" ", "");
	
	}
	
	public JEditorPane initjEditorPane() {
		
		JEditorPane jEditorPane = new JEditorPane();
	     Border border = jEditorPane.getBorder();
		 Border margin = new EmptyBorder(5,5,5,5);
		 jEditorPane.setBorder(new CompoundBorder(border, margin));
		 
		jEditorPane.setEditable(false);
		HTMLEditorKit kit = new HTMLEditorKit();
		jEditorPane.setEditorKit(kit);
		jEditorPane.setVisible(false);
	       jEditorPane.addHyperlinkListener(new HyperlinkListener() {

	            @Override
	            public void hyperlinkUpdate(HyperlinkEvent e) {
	                HyperlinkEvent.EventType type = e.getEventType();
	                final URL url = e.getURL();
	                if (type == HyperlinkEvent.EventType.ENTERED) {
	                    // do desired highlighting
	                } else if (type == HyperlinkEvent.EventType.ACTIVATED) {
	                	Util.openURL(url.toString());
	                }
	            }
	          });
	       
	       return jEditorPane;
		
	}
	

	public void launchGoogleEarth(File file){
		try {
			Runtime.getRuntime().exec(new String[] {
					googleEarthExec ,
					file.getAbsolutePath()
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}
	
   public JButton setButton(String text) {
	   if (getPrefs().getProperty("numcolor") == null) {
		   readPrefProperties();
	   }
	   
		int numColor = Integer.parseInt(getPrefs().getProperty("numcolor"));

	   
		JButton buttonBt = new JButton(text);
		
		//buttonBt.setBackground(colorBackground[numColor]);
		//buttonBt.setForeground(colorForground[numColor]);	    
//
	//	buttonBt.setBackground(Color.BLACK);
	///	buttonBt.setForeground(Color.WHITE);	    

		return buttonBt;
   }

   
	  public void initLookAndFeel(PlanetIsCalling jFrame, int numColor) {
	      String lookAndFeel = "";
	      String LOOKANDFEEL = "Metal";
	      String THEME = "Ocean";
	      
	      if (LOOKANDFEEL != null) {
	          if (LOOKANDFEEL.equals("Metal")) {
	        	  
	              lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
	            //  an alternative way to set the Metal L&F is to replace the 
	            // previous line with:
	            lookAndFeel = "javax.swing.plaf.metal.MetalLookAndFeel";
	               
	          }
	           
	          else if (LOOKANDFEEL.equals("System")) {
	              lookAndFeel = UIManager.getSystemLookAndFeelClassName();
	          } 
	           
	          else if (LOOKANDFEEL.equals("Motif")) {
	              lookAndFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
	          } 
	           
	          else if (LOOKANDFEEL.equals("GTK")) { 
	              lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
	          } 
	           
	          else {
	              System.err.println("Unexpected value of LOOKANDFEEL specified: "
	                                 + LOOKANDFEEL);
	              lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
	          }

	          try {
	              UIManager.setLookAndFeel(lookAndFeel);
	               
	              // If L&F = "Metal", set the theme
	               
	              if (LOOKANDFEEL.equals("Metal")) {
	                if (THEME.equals("DefaultMetal"))
	                   MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
	                else if (THEME.equals(""))
	                   MetalLookAndFeel.setCurrentTheme(new OceanTheme());
	               
	                    
	                UIManager.setLookAndFeel(new MetalLookAndFeel()); 
	              }   
	                   
	          } 
	          catch (ClassNotFoundException e) {
	              System.err.println("Couldn't find class for specified look and feel:"
	                                 + lookAndFeel);
	              System.err.println("Did you include the L&F library in the class path?");
	              System.err.println("Using the default look and feel.");
	          } 
	           
	          catch (UnsupportedLookAndFeelException e) {
	              System.err.println("Can't use the specified look and feel ("
	                                 + lookAndFeel
	                                 + ") on this platform.");
	              System.err.println("Using the default look and feel.");
	          } 
	           
	          catch (Exception e) {
	              System.err.println("Couldn't get specified look and feel ("
	                                 + lookAndFeel
	                                 + "), for some reason.");
	              System.err.println("Using the default look and feel.");
	              e.printStackTrace();
	          }          
	      }
			
			UIManager.put("OptionPane.background", colorBackground[numColor]);
			UIManager.put("OptionPane.foreground", colorForground[numColor]);
			
			UIManager.put("TextArea.font", new Font("Serif",Font.BOLD,16));

			UIManager.put("Panel.background", colorBackground[numColor]);
		    UIManager.put("Panel.foreground", colorForground[numColor]);
		    UIManager.put("Panel.border", colorBackList[numColor]);
		    
			UIManager.put("ComboBox.background", colorBackground[numColor]);
		    UIManager.put("ComboBox.foreground", colorForground[numColor]);
	
		    UIManager.put("RadioButton.background", colorBackground[numColor]);
		  //  UIManager.put("TitledBorder.border", colorBackground[numColor]);

			UIManager.put("CheckBox.background", colorBackground[numColor]);
			UIManager.put("CheckBox.border", colorBackground[numColor]);
			UIManager.put("CheckBox.foreground", colorBackground[numColor]);
		    UIManager.put("CheckBox.select", Color.red);

			
		    UIManager.put("TitledBorder.titleColor", colorForground[numColor]);
		    
		    UIManager.put("TabbedPane.background", colorForground[numColor]);
		    UIManager.put("TabbedPane.foreground", colorBackground[numColor]);
		  
		    UIManager.put("EditorPane.background", colorBackList[numColor]);
		    UIManager.put("EditorPane.foreground", colorBackList[numColor]);
		    UIManager.put("EditorPane. margin", colorBackList[numColor]);
		    UIManager.put("EditorPane.border", colorBackList[numColor]);

		    UIManager.put("Button.foreground", colorForgroundBtn[numColor]);
		    UIManager.put("Button.background", colorBackground[numColor]);

			UIManager.put("List.background", colorBackList[numColor]);
		    UIManager.put("List.foreground", colorBlueText[0]);

			UIManager.put("Label.background", colorBackground[numColor]);
		    UIManager.put("Label.foreground", colorForground[numColor]);
		    
		    SwingUtilities.updateComponentTreeUI(jFrame);

	
	  }      
	  
	  public JPanel setBorderPanel(JPanel panel) {
			panel = new JPanel(new GridBagLayout());
			panel.setBorder(new TitledBorder(""));

		     Border border = panel.getBorder();
			 Border margin = new EmptyBorder(1,1,1,1);
			 panel.setBorder(new CompoundBorder(border, margin));
			 
			 return panel;
			
		}
	  
   
}
