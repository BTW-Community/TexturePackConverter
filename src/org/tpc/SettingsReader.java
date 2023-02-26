package org.tpc;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SettingsReader {
	
	public static Map settings;
	
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
            if (string == "openLogStartUp" && pair.getKey() == string)
            {
            	System.out.println("test");
            	settings.replace("openLogStartUp", boo);
            }
        }
        
      PrintWriter pw = new PrintWriter("settings.json");
      pw.write(jo.toJSONString());
      
      pw.flush();
      pw.close();
		
//		// creating JSONObject
//        JSONObject jo = new JSONObject();
//          
//        // putting data to JSONObject
//        Map m = new LinkedHashMap(3);
//        m.put("achievement", true);
//        m.put("armor", true);
//        m.put("armorCreeper", true);
//          
//        
//        Map m2 = new LinkedHashMap(1);
//        m2.put("achievement", true);
//        
//        // putting address to JSONObject
//        jo.put("options", m);
//        m.put("options_2", m2);
//        		
//        // writing JSON to file:"JSONExample.json" in cwd
//        PrintWriter pw = new PrintWriter("option_test.json");
//        pw.write(jo.toJSONString());
//        
//        pw.flush();
//        pw.close();
	}
	
	public boolean shouldLogOpenOnStartUp() {

		return (boolean) settings.get("openLogStartUp");
	}	
	
	
	
}
