package org.main.UtilsClass;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageLoader {

    /**
     * Loads an image into the supplied JLabel.<br><br>
     *
     * @param jlabel                  (JLabel) The variable name of the JLabel
     *                                to load image into<br>
     *
     * @param imagePath               (String) The full path and file name of
     *                                the image to load into JLabel. This path
     *                                can be either from the local file system
     *                                or from a from a JAR's resources or from a
     *                                http or https web link, for example:<pre>
     *
     *      "C:/My Gifs/flowers.gif"
     *
     *                OR
     *
     *      "/resources/images/flowers.gif"
     *
     *                OR
     *
     *      "https://thumbs.gfycat.com/cool.gif"</pre>
     *
     * To retrieve your images from a JAR's resources you need to ensure that
     * your resource folder is properly located during development. The resource
     * folder (resources) should be located within the source (src) folder. The
     * path tree should be:
     * <pre>
     *
     *  * PROJECT NAME
     *      * src
     *          * PACKAGE YOU NAMED IN PROJECT
     *               yourApp.java
     *          * resources
     *              * images
     *                  yourImageFile1.jpg
     *                  yourImageFile2.gif
     *                  yourImageFile3.png
     *                  yourImageFile4.jpeg
     *                  yourImageFile5.bmp
     *                  yourImageFile6.wbmp
     *              * text
     *                  myText1.txt
     *                  myText2.txt
     *                  myText3.txt
     *              * etc..........</pre>
     *
     * Whenever loading anything from resources the path must always start with
     * a forward slash (/), for example:
     * <pre>
     *
     *      loadImageToJLabel(jLabel1, "/resources/inages/yourImageFile1.jpg", true);</pre>
     *
     * @param autoSetImageToLabelSize
     */
    public static void loadImageToJLabel(JLabel jlabel, String imagePath, boolean... autoSetImageToLabelSize) {
        String ls = System.lineSeparator();
        boolean autoSize = false;
        if (autoSetImageToLabelSize.length > 0) {
            autoSize = autoSetImageToLabelSize[0];
        }
        javax.swing.ImageIcon image;

        try {
            if (imagePath.toLowerCase().startsWith("http") || imagePath.toLowerCase().startsWith("https") ) {
                final java.net.URL url = new java.net.URL(imagePath);
                java.net.HttpURLConnection huc = (java.net.HttpURLConnection) url.openConnection();
                int responseCode = huc.getResponseCode();
                huc.disconnect();
                //Does responseCode fall into the 200 to 299 range (which are the codes for: SUCCESS)
                // If NO then....
                if (responseCode < 200 || responseCode > 299) {
                    System.err.println("loadImageToJLabel() Method Error! Can not "
                            + "locate the image file specified!" + ls + "Ensure "
                            + "that your HTTP or HTTPS link exists!" + ls
                            + "Supplied Link Path:  \"" + imagePath + "\"" + ls);
                    return;
                }
                image = new javax.swing.ImageIcon(url); //new URL(imagePath));
            }
            else if (imagePath.startsWith("/") || imagePath.startsWith("\\")) {
                Class currentClass = new Object() {
                }.getClass().getEnclosingClass();
                try {
                    image = new javax.swing.ImageIcon(currentClass.getClass().getResource(imagePath));
                }
                catch (NullPointerException npe) {
                    System.err.println("loadImageToJLabel() Method Error! Can not "
                            + "locate the Resource Path or the resource file!" + ls + "Ensure "
                            + "that your resources are properly located." + ls
                            + "Supplied Resource Path:  \"" + imagePath + "\"" + ls);
                    return;
                }
            }
            else {
                java.io.File f = new java.io.File(imagePath);
                if (!f.exists()) {
                    System.err.println("loadImageToJLabel() Method Error! Can not "
                            + "locate the image file specified!" + ls + "Ensure "
                            + "that your Image file exists within the local file system path provided!" + ls
                            + "Supplied File Path:  \"" + imagePath + "\"" + ls);
                    return;
                }
                image = new javax.swing.ImageIcon(new java.net.URL("file:" + imagePath));
            }

            if (autoSize) {
                Image img = image.getImage();
                Image resizedImage = img.getScaledInstance(jlabel.getWidth(), jlabel.getHeight(), java.awt.Image.SCALE_SMOOTH);
                jlabel.setIcon(new ImageIcon(resizedImage));
            }
            else {
                jlabel.setIcon(image);
            }
            jlabel.validate();
        }
        catch (java.net.MalformedURLException ex) {
            System.err.println("loadImageToJLabel() Method Error! Improper Image file link supplied!" + ls
                    + "Supplied Link:  \"" + imagePath + "\"" + ls);

        }
        catch (java.io.IOException ex) {
            System.err.println("loadImageToJLabel() Method Error! Can not access Image File!" + ls
                    + "Supplied File Path:  \"" + imagePath + "\"" + ls);
        }
    }

    public static void loadImage(JLabel jlabel, String imagePath, boolean... autoSetImageToLabelSize) {
        String ls = System.lineSeparator();
        boolean autoSize = false;
        if (autoSetImageToLabelSize.length > 0) {
            autoSize = autoSetImageToLabelSize[0];
        }
        javax.swing.ImageIcon image;

        try {
            if (imagePath.toLowerCase().startsWith("http") || imagePath.toLowerCase().startsWith("https") ) {

                Image imageJ = null;
                URL url = null;
                try {
                    url = new URL(imagePath);
                    imageJ = ImageIO.read(url);
                } catch (MalformedURLException ex) {
                    System.out.println("Malformed URL");
                } catch (IOException iox) {
                    System.out.println("Can not load file");
                }

                image = new javax.swing.ImageIcon(imageJ);

                jlabel.setIcon(image);



            }
            else if (imagePath.startsWith("/") || imagePath.startsWith("\\")) {
                Class currentClass = new Object() {
                }.getClass().getEnclosingClass();
                try {
                    image = new javax.swing.ImageIcon(currentClass.getClass().getResource(imagePath));
                }
                catch (NullPointerException npe) {
                    System.err.println("loadImageToJLabel() Method Error! Can not "
                            + "locate the Resource Path or the resource file!" + ls + "Ensure "
                            + "that your resources are properly located." + ls
                            + "Supplied Resource Path:  \"" + imagePath + "\"" + ls);
                    return;
                }
            }
            else {
                java.io.File f = new java.io.File(imagePath);
                if (!f.exists()) {
                    System.err.println("loadImageToJLabel() Method Error! Can not "
                            + "locate the image file specified!" + ls + "Ensure "
                            + "that your Image file exists within the local file system path provided!" + ls
                            + "Supplied File Path:  \"" + imagePath + "\"" + ls);
                    return;
                }
                image = new javax.swing.ImageIcon(new java.net.URL("file:" + imagePath));
            }

            if (autoSize) {
                Image img = image.getImage();
                Image resizedImage = img.getScaledInstance(jlabel.getWidth(), jlabel.getHeight(), java.awt.Image.SCALE_SMOOTH);
                jlabel.setIcon(new ImageIcon(resizedImage));
            }
            else {
                jlabel.setIcon(image);
            }
            jlabel.validate();
        }
        catch (java.net.MalformedURLException ex) {
            System.err.println("loadImageToJLabel() Method Error! Improper Image file link supplied!" + ls
                    + "Supplied Link:  \"" + imagePath + "\"" + ls);

        }
        catch (java.io.IOException ex) {
            System.err.println("loadImageToJLabel() Method Error! Can not access Image File!" + ls
                    + "Supplied File Path:  \"" + imagePath + "\"" + ls);
        }
    }


















}
