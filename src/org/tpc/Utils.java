package org.tpc;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.tools.JavaCompiler;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Utils {

    public static void openSite(String uriStr) {

        try {
            URI uri = new URI(uriStr);

            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(uri);
                } catch (IOException e) {
                    /* TODO: error handling */
                }
            } else {
                /* TODO: error handling */
            }
        } catch (URISyntaxException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    /**
     Credit: https://www.geeksforgeeks.org/java-program-to-rotate-an-image/
     */
    public static boolean rotateImage(String fileName, String format, int degree) {
        File image = new File(fileName + "." + format);

        BufferedImage originalImg = null;
        try {
            originalImg = ImageIO.read(image);
        } catch (IOException e) {
            //throw new RuntimeException(e);
            return false;
        }

        BufferedImage SubImg = rotate(originalImg, degree);

        File outputfile = new File(fileName + "." + format);

        try {
            ImageIO.write(SubImg, format, outputfile);
        } catch (IOException e) {
            //throw new RuntimeException(e);
            return false;
        }

        return true;
    }

    /**
     Credit: https://www.geeksforgeeks.org/java-program-to-rotate-an-image/
     */
    private static BufferedImage rotate(BufferedImage img, int degree)
    {

        // Getting Dimensions of image
        int width = img.getWidth();
        int height = img.getHeight();

        // Creating a new buffered image
        BufferedImage newImage = new BufferedImage(
                img.getWidth(), img.getHeight(), img.getType());

        // creating Graphics in buffered image
        Graphics2D g2 = newImage.createGraphics();

        // Rotating image by degrees using toradians()
        // method
        // and setting new dimension t it
        g2.rotate(Math.toRadians(degree), width / 2,
                height / 2);
        g2.drawImage(img, null, 0, 0);

        // Return rotated buffer image
        return newImage;
    }

    public static ImageIcon getImage(Class cl, String src)
    {
        ImageIcon icon;
        BufferedImage image;
        InputStream is = cl.getClass().getResourceAsStream(src);
        try {
            image = ImageIO.read(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        icon = new ImageIcon(image);

        return icon;
        /*if (isObfuscated())
        {

        }
        else {
            return new ImageIcon(src);
        }*/

    }

    public static BufferedReader getFile(Class cl, String src)
    {
        BufferedReader file;
        InputStream is = cl.getClass().getResourceAsStream(src);
        try {
            file = new BufferedReader(new InputStreamReader(cl.getResourceAsStream(src), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return file;

        /*if (isObfuscated())
        {

        }
        else {
            try {
                return new BufferedReader(new InputStreamReader(new FileInputStream(src), "UTF-8"));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }*/

    }

    public static ArrayList<String> listFilesForFolder(final File folder) {
        ArrayList<String> list = new ArrayList<String>();

        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                final int lastPeriodPos = fileEntry.getName().lastIndexOf('.');
                list.add(fileEntry.getName().substring(0, lastPeriodPos));
            }
        }
        return list;
    }

    public static HashMap<Integer, String> formats = new HashMap<>();

    static{
        formats.put(0, "1.5.2");
        formats.put(4, "1.6.1");
    }

    public static int getPackFormat(String input) throws IOException {

        //unzip pack.mcmeta to temp folder
        File file = new File("temp/pack.mcmeta");
        if (!file.getParentFile().exists());
        {
            file.getParentFile().mkdirs();
        }
        String fileToBeExtracted="pack.mcmeta";

        if (!file.exists())
        {
            String zipPackage=input;
            OutputStream out = new FileOutputStream("temp/" + fileToBeExtracted);
            FileInputStream fileInputStream = new FileInputStream(zipPackage);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream );
            ZipInputStream zin = new ZipInputStream(bufferedInputStream);
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
                if (ze.getName().equals(fileToBeExtracted)) {
                    byte[] buffer = new byte[9000];
                    int len;
                    while ((len = zin.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }
                    out.close();
                    break;
                }
            }
            zin.close();
            fileInputStream.close();
            bufferedInputStream.close();
        }

        JsonElement format = null;

        //get packformat
        if (file.exists())
        {
            JsonObject pack = JsonHelper.getMcMeta("temp/pack.mcmeta", "pack");
            format = pack.get("pack_format");

        }

        try {
            Files.deleteIfExists(file.toPath());
        }
        catch (NoSuchFileException e) {
            System.out.println(
                    "No such file/directory exists");
        }
        catch (DirectoryNotEmptyException e) {
            System.out.println("Directory is not empty.");
        }
        catch (IOException e) {
            System.out.println("Invalid permissions.");
        }

        System.out.println("Deletion successful.");

        return format.getAsInt();
    }
    private static boolean hasCheckedForObfuscation = false;
    private static boolean isObfuscated = false;

    /**
    Credit: https://github.com/BTW-Community/BTW-Public
     */
    public static boolean isObfuscated() {
        if (hasCheckedForObfuscation) {
            return isObfuscated;
        }
        else {
            hasCheckedForObfuscation = true;

            try {
                //Field will only be found if running from source
                //Obfuscated code uses different names
                Field f = Main.class.getDeclaredField("mainWindow");
            } catch (NoSuchFieldException e) {
                isObfuscated = true;
                return true;
            }

            return false;
        }
    }
}
