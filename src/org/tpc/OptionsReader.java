package org.tpc;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class OptionsReader {
	
	public Map getOptions() {
		return options;
	}

	public Map options;
	
	public OptionsReader() throws Exception {
		 // parsing file "JSONExample.json"
        Object obj = new JSONParser().parse(new FileReader("options.json"));
          
        // typecasting obj to JSONObject
        JSONObject jo = (JSONObject) obj;
	     
        // getting address
        options = ((Map)jo.get("options"));
          
        // iterating address Map
        Iterator<Map.Entry> itr1 = options.entrySet().iterator();
        while (itr1.hasNext()) {
            Map.Entry pair = itr1.next();
//            System.out.println(pair.getKey() + " : " + pair.getValue());
            options.put(pair.getKey(), pair.getValue());
            
            
        }
	}
	
	private ArrayList<String> key = new ArrayList();
	private ArrayList<Boolean> optionsValues = new ArrayList();

	
	public ArrayList<String> getKey() {
		return key;
	}
	public ArrayList<Boolean> getOptionsValues() {
		return optionsValues;
	}
	
	
	
	
}
