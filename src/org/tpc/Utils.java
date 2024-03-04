package org.tpc;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.tools.JavaCompiler;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImagingOpException;
import java.awt.image.WritableRaster;
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



    public static ImageIcon getImage(Class cl, String src)
    {
        ImageIcon icon;
        BufferedImage image;
        InputStream is = cl.getResourceAsStream(src);
        try {
            image = ImageIO.read(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        icon = new ImageIcon(image);

        return icon;
        /*
        if (isObfuscated())
        {

        }
        else {
            return new ImageIcon(src);
        }
        */
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

        /*
        if (isObfuscated())
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
        }
        */
    }

    public static ArrayList<File> getFilesForFolder(final File folder) {
        ArrayList<File> files = new ArrayList<File>();

        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                files.add(fileEntry);
                getFilesForFolder(fileEntry);
            } else {
                //System.out.println(fileEntry.getPath());
                files.add(fileEntry);
            }
        }

        return files;
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

    /**
     Credit: https://www.geeksforgeeks.org/java-program-to-rotate-an-image/
     */
    public static BufferedImage rotateImage(BufferedImage img, int degree)
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

    /**
     Credit: https://www.geeksforgeeks.org/image-processing-in-java-creating-a-mirror-image/
     */
    public static BufferedImage mirrorImage(BufferedImage img) {

        // Get source image dimension
        int width = img.getWidth();
        int height = img.getHeight();

        // BufferedImage for mirror image
        BufferedImage mimg = new BufferedImage(
                width, height, BufferedImage.TYPE_INT_ARGB);

        // Create mirror image pixel by pixel
        for (int y = 0; y < height; y++) {
            for (int lx = 0, rx = width - 1; lx < width; lx++, rx--) {

                // lx starts from the left side of the image
                // rx starts from the right side of the
                // image lx is used since we are getting
                // pixel from left side rx is used to set
                // from right side get source pixel value
                int p = img.getRGB(lx, y);

                // set mirror image pixel value
                mimg.setRGB(rx, y, p);
            }
        }

        return mimg;
    }

    public static BufferedImage tintImage(BufferedImage img, int colorTint) {
        int width = img.getWidth();
        int height = img.getHeight();

        //Split hex tint int RGB
        int red = Color.decode(Integer.toString(colorTint)).getRed();
        int green = Color.decode(Integer.toString(colorTint)).getGreen();
        int blue = Color.decode(Integer.toString(colorTint)).getBlue();

        //For each pixel
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                //get the color and split int RGB
                Color color = new Color(img.getRGB(col, row), true);


                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();
                int a = color.getAlpha();

                if (a != 255)
                {
                    //Multiply tint color with pixel color
                    img.setRGB(col, row, new Color((red*r)/255, (green*g)/255, (blue*b)/255).getRGB());
                }
            }
        }

        return img;
    }

    public static BufferedImage overlayImage(BufferedImage img, BufferedImage overlayImg) {
        Graphics2D g = img.createGraphics();
        g.drawImage(overlayImg, 0, 0, null);
        g.dispose();
        return img;
    }

    //CREDIT: https://stackoverflow.com/questions/8662349/convert-negative-image-to-positive
    public static BufferedImage invertColors(BufferedImage img) {
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                int rgba = img.getRGB(x, y);
                Color col = new Color(rgba, true);
                col = new Color(255 - col.getRed(),
                        255 - col.getGreen(),
                        255 - col.getBlue());
                img.setRGB(x, y, col.getRGB());
            }
        }
        return img;
    }

    /*
    Code to modify image
    {
        String imagePath = "temp/sapling.png";
        BufferedImage myPicture = ImageIO.read(new File(imagePath));

        myPicture = Utils.rotateImage(myPicture, 180);
        myPicture = Utils.mirrorImage(myPicture);
        myPicture = Utils.tintImage(myPicture, 0x990000);

        File output_file = new File("temp/export.png");

        ImageIO.write(myPicture, "png", output_file);
    }
    */

}
