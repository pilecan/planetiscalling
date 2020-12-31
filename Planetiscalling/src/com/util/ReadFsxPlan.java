package com.util;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.cfg.model.LegPoint;
import com.cfg.util.Util;
import com.geo.util.Geoinfo;
import com.model.Flightplan;

public class ReadFsxPlan extends DefaultHandler{

	private String tempVal;
	private String flightPlanFile;
	private String cruisingAlt;
	
	String str;
	Double d;
	Double altitude;
	
	private Flightplan flightplan;

	private LinkedList<LegPoint> legPoints;
	
	private LegPoint legPoint;
	
	
	private Stack currentElement = new Stack();

	public ReadFsxPlan(String flightPlanFile){
		this.flightPlanFile = flightPlanFile;
		flightplan = new Flightplan();
		flightplan.setFlightplanFile(flightPlanFile);
		parseDocument();
	}
	
	public void modifyAltitude(LinkedList<LegPoint> legPoints, Double cruzeAltitude, Double newAltitude) {
		cruzeAltitude = cruzeAltitude/3.28084;
		newAltitude   = newAltitude/3.28084;
		for (int i = 1; i < legPoints.size()-1; i++) {
			legPoints.get(i).setNewAltitude(Util.modifyAltitude(cruzeAltitude, legPoints.get(i).getAltitude(), newAltitude));
		}
	}
	
	static public void changeAltitude(String filePlan, Double cruzeAltitude, Double newAltitude) throws IOException {
			
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
			//	System.out.println(strAltitude +" -> "+Util.formatAltitude(Util.modifyAltitude(cruzeAltitude, Double.parseDouble(strAltitude), newAltitude)));
				row[i] = row[i].replace(strAltitude, Util.formatAltitude(Util.modifyAltitude(cruzeAltitude, Double.parseDouble(strAltitude), newAltitude)));
			}
		}
		
	//	System.out.println(row.length);
		
		//saveNewFlightPlan(filePlan, row);
		
		//System.out.println(Util.formatAltitude(Util.modifyAltitude(cruzeAltitude, +002257.00, newAltitude)));

	}
	
	
	public  static void saveNewFlightPlan(String flightPlanFile, String[] row){
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

	

	
	public static void main(String[] args) throws FileNotFoundException, NullPointerException, IOException{
    	String filePlan = "c:\\Users\\Pierre\\AppData\\Local\\Packages\\Microsoft.FlightSimulator_8wekyb3d8bbwe\\LocalState\\IFR Zurich (LSZH) to El Prat (LEBL).pln";

    	ReadFsxPlan readPlan = new ReadFsxPlan(filePlan);

		System.out.println(readPlan.getCruisingAlt());
		
		
		
		changeAltitude(filePlan,Double.valueOf(readPlan.getCruisingAlt()),13000.00);
		
	}
	
	
	private void parseDocument() {
		
		legPoints = new LinkedList<LegPoint>();
		
		//get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			SAXParser sp = spf.newSAXParser();
			sp.parse(new File(flightPlanFile).toURI().toString(), this);
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch (IOException ie) {
			ie.printStackTrace();
		}
		
		
	}

	//Event Handlers
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		//reset
		currentElement.push(qName);
		tempVal = "";
		if(qName.equalsIgnoreCase("ATCWaypoint")) {
			legPoint = new LegPoint();
			legPoint.setId(attributes.getValue("id"));
			legPoint.setVisible("1");
		}
		
	}
	

	public void characters(char[] ch, int start, int length) throws SAXException {
		tempVal = new String(ch,start,length);
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		currentElement.pop();
		if(qName.equalsIgnoreCase("ATCWaypoint")) {
			legPoints.add(legPoint);
			//System.out.println(atcWaypoint.toString());
		}else if (qName.equalsIgnoreCase("ATCWaypointType")) {
			legPoint.setType(tempVal);
		}else  if (qName.equalsIgnoreCase("Title")) {
	        flightplan.setTitle(tempVal);
		}else  if (qName.equalsIgnoreCase("FPType")) {
	        flightplan.setFpType(tempVal);
		}else  if (qName.equalsIgnoreCase("RouteType")) {
	        flightplan.setRouteType(tempVal);
		}else  if (qName.equalsIgnoreCase("CruisingAlt")) {
	        flightplan.setCruisingAlt(tempVal);
		}else  if (qName.equalsIgnoreCase("DepartureID")) {
	        flightplan.setDepartureID(tempVal);
		}else  if (qName.equalsIgnoreCase("DepartureLLA")) {
	        flightplan.setDepartureLLA(tempVal);
		}else  if (qName.equalsIgnoreCase("DestinationID")) {
	        flightplan.setDestinationID(tempVal);
		}else  if (qName.equalsIgnoreCase("DestinationLLA")) {
	        flightplan.setDestinationLLA(tempVal);
		}else  if (qName.equalsIgnoreCase("Descr")) {
	        flightplan.setDescr(tempVal);
		}else  if (qName.equalsIgnoreCase("DepartureName")) {
	        flightplan.setDepartureName(tempVal);
		}else if (qName.equalsIgnoreCase("DestinationName")) {
	        flightplan.setDestinationName(tempVal);
		}else if (qName.equalsIgnoreCase("ICAOIdent")) {
			legPoint.setIcaoIdent(tempVal);
		}else if (qName.equalsIgnoreCase("WorldPosition")) {
			String coords[] = tempVal.split(",");
			try {
				altitude = Double.parseDouble(coords[2].trim())/3.28084;
				legPoint.setPosition(Geoinfo.convertDMSToDD(coords[1].trim())+","+Geoinfo.convertDMSToDD(coords[0].trim())+","+altitude);
			} catch (Exception e) {
			}
			//System.out.println(legPoint.showforKML());
			//System.out.println();
		}
	}
	


	public LinkedList<LegPoint> getLegPoints() {
		return legPoints;
	}

	public void setLegPoints(LinkedList<LegPoint> legPoints) {
		this.legPoints = legPoints;
	}


	public String getCruisingAlt() {
		return cruisingAlt;
	}

	public void setCruisingAlt(String cruisingAlt) {
		this.cruisingAlt = cruisingAlt;
	}

	public Flightplan getFlightplan() {
		return flightplan;
	}

	public void setFlightplan(Flightplan flightplan) {
		this.flightplan = flightplan;
	}

	

}



