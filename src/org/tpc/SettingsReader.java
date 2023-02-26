package org.tpc;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.tpc.gui.MainWindow;

public class SettingsReader {
	
	public static Map settings;
	
	public static final String OPEN_LOG_ON_START = "openLogStartUp";
	
	public SettingsReader() throws Exception  {
		readSettings();
	}
	
	public void readSettings() throws Exception
	{
		 // parsing file "JSONExample.json"
        Object obj = new JSONParser().parse(new FileReader("settings.json"));
          
        // typecasting obj to JSONObject
        JSONObject jo = (JSONObject) obj;
	     
        // getting address
        settings = ((Map)jo.get("settings"));
          
        // iterating address Map
        Iterator<Map.Entry> itr1 = settings.entrySet().iterator();
        while (itr1.hasNext()) {
            Map.Entry pair = itr1.next();
//            System.out.println(pair.getKey() + " : " + pair.getValue());
            settings.put(pair.getKey(), pair.getValue());            
        }
	}
	
	public void writeSettings(String string, Boolean boo) throws IOException, ParseException
	{         
		 // parsing file "JSONExample.json"
		JSONObject jo = (JSONObject) new JSONParser().parse(new FileReader("settings.json"));
				
        JSONObject settings = new JSONObject();
        settings = (JSONObject) jo.get("settings");
        
        // iterating address Map
        Iterator<Map.Entry> itr1 = settings.entrySet().iterator();
        
        settings.put(string, boo);
    	MainWindow.consoleLog.log("Settings: \"" + string + "\" set to "  + settings.get(string), false);
        
    	PrintWriter pw = new PrintWriter("settings.json");
    	pw.write(jo.toJSONString());
      
    	pw.flush();
    	pw.close();
	}
	
	public boolean shouldLogOpenOnStartUp() {

		return (boolean) settings.get(OPEN_LOG_ON_START);
	}	
	
	
	
}
