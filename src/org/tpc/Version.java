package org.tpc;

public class Version {
	
	private static String release = "0";
	private static String subrelease = "0";
	private static String patchrelease = "0";
	
	/**
	 * Returns the current version with or without the "v"
	 * @param displayV
	 * @return
	 */
	public static String getCurrentVersion(boolean displayV) {
		
		String prefix = "";
		if (displayV) prefix = "v";
		
		return prefix + release + "." + subrelease + "." + patchrelease;
	}
}
