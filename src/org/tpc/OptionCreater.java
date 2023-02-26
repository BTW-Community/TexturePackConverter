package org.tpc;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class OptionCreater {
	public OptionCreater() throws FileNotFoundException{
		
		// creating JSONObject
        JSONObject jo = new JSONObject();
          
        // putting data to JSONObject
        Map m = new LinkedHashMap(3);
        m.put("achievement", true);
        m.put("armor", true);
        m.put("armorCreeper", true);
          
        
        Map m2 = new LinkedHashMap(1);
        m2.put("achievement", true);
        
        // putting address to JSONObject
        jo.put("options", m);
        m.put("options_2", m2);
        		
        // writing JSON to file:"JSONExample.json" in cwd
        PrintWriter pw = new PrintWriter("option_test.json");
        pw.write(jo.toJSONString());
        
        pw.flush();
        pw.close();
	}
}
