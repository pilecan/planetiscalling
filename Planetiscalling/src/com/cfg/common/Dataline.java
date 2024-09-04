package com.cfg.common;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Dataline {
    private Map <String, String> mapData;

    private static final Map<String, String> mapColor;
    static {
        Map<String, String> aMap = new HashMap<>();
        aMap.put("city", "#ffff00ff");
        aMap.put("airport", "#ffff0000");
        aMap.put("mountain", "#ff00ff00");
        aMap.put("landmark", "#ff00ff00");
        mapColor = Collections.unmodifiableMap(aMap);
    }
    
    //test
  	public Dataline() {
		super();
		mapData = new HashMap<>();
	}

	public String getData(String topic) {
		return mapData.get(topic);
	}

	public void setData(String topic, String strData) {
		if (mapData.get(topic) != null) {
			strData += mapData.get(topic);
		}
		mapData.put(topic,strData);
	}

	public static String getColor(String topic) {
		return mapColor.get(topic);
	}

	public Map<String, String> getMapData() {
		return mapData;
	}

	public void setMapData(Map<String, String> mapData) {
		this.mapData = mapData;
	}
    
}
