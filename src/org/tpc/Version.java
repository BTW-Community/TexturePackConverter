package org.tpc;

public class Version {

    private static final String MAIN_RELEASE = "1";
    private static final String MINOR_RELEASE = "0";
    private static final String PATCH_RELEASE = "0";

    /**
     * Returns the current version with or without the "v"
     * @param displayV
     * @return
     */
    public static String getCurrentVersion(boolean displayV) {

        String prefix = "";
        if (displayV) prefix = "v";

        return prefix + MAIN_RELEASE + "." + MINOR_RELEASE + "." + PATCH_RELEASE;
    }
}
