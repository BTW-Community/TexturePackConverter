package org.tpc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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
     https://www.geeksforgeeks.org/java-program-to-rotate-an-image/
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
     https://www.geeksforgeeks.org/java-program-to-rotate-an-image/
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
}
