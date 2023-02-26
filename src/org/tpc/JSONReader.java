package org.tpc;

import java.io.FileReader;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.tpc.gui.MainWindow;

@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
public class JSONReader {
	
	private final int SRC = 0;	
	private final int DEST = 1;
	
	private static String version = "";
	private static String description = "";
    
	
	public JSONReader() throws Exception  
    {
		
    }
	
	public void parse(String file) throws Exception
	{
		Object obj = new JSONParser().parse(new FileReader(file));
        
        JSONObject jo = (JSONObject) obj;

        Map info = ((Map)jo.get("mappingInfo"));
        
        Iterator<Map.Entry> itr1 = info.entrySet().iterator();
        while (itr1.hasNext()) {
            Map.Entry pair = itr1.next();
            
            if (pair.getKey().toString().equals("name"))
            {
            	version = pair.getValue().toString();
            }
            else if (pair.getKey().toString().equals("desc"))
            {
            	description = pair.getValue().toString();
            }
        }
        
        String[] achievement = getLocations(info, "achievement");
        String[] armor = getLocations(info, "armor");
        String[] armorCreeper = getLocations(info, "armorCreeper");
        String[] armorWither = getLocations(info, "armorWither"); 
        String[] art = getLocations(info, "art");
        String[] enviroment = getLocations(info, "enviroment");
        String[] font = getLocations(info, "font");
        String[] gui = getLocations(info, "gui");
        String[] unknown_pack = getLocations(info, "unknown_pack");
        String[] item = getLocations(info, "item");
        String[] colormap = getLocations(info, "colormap");
        String[] map = getLocations(info, "map");
        String[] misc = getLocations(info, "misc");
        String[] footprint = getLocations(info, "footprint");
        String[] mob = getLocations(info, "mob");
        String[] blocks = getLocations(info, "blocks");
        String[] items = getLocations(info, "items");
        String[] title = getLocations(info, "title");
        String[] particles = getLocations(info, "particles");

        MainWindow.consoleLog.log("Loaded Mappings for version " + version, false);
        MainWindow.consoleLog.log(description, false);
    }	
	
	private String[] getLocations(Map info, String string)
	{
		Map ach = ((Map)info.get(string));
        
        Iterator<Map.Entry> itr2 = ach.entrySet().iterator();
        
        Map.Entry pair2 = itr2.next();
        
        //System.out.println(pair2.getKey() + " : " + pair2.getValue() );
        
        return new String[] {(String) pair2.getKey(), (String) pair2.getValue()};
	}
	
	public static String getMappingVersion() {
		return version;
	}
	
	public static String getMappingDescription() {
		return description;
	}
}
