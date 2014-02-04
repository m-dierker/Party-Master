package com.partyrock.anim.ledpanel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Shell;

import com.partyrock.LightMaster;
import com.partyrock.anim.ElementAnimation;
import com.partyrock.element.ElementController;
import com.partyrock.element.ElementType;
import com.partyrock.element.led.LEDPanelController;
import com.partyrock.tools.PartyToolkit;


// Written by Spencer Liolios

public class animatedGif extends ElementAnimation {    
	private Color color;
	
    // The number of rows we've faded
    private int fadedRows = -1;
        
    //Number of images in the gif
    private int numberofImages = 0;
    
    private int LEDDimensions = 16;
    
    Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("gif");
    ImageReader reader = readers.next();

    
    public animatedGif(LightMaster master, int startTime, ArrayList<ElementController> elementList, double duration) {
        super(master, startTime, elementList, duration);
        
        // Tell the animation system to call our animation's step() method repeatedly so we can animate over time
        needsIncrements();

        // Set a new default color of white
        color = sysColor(255, 255, 255);
    }

    /**
     * This method is called once when the animation is created. You can use it to get user configurable settings like a
     * Color (as we do in this case)
     */
    @Override
    public void setup(Shell window) {
    	String fileName = "";
    	String baseFilePath = "";
		String filePath = ""; 
		
		File sourceFile;
	    ImageInputStream iis = null;
		
	    //get CWD/images/
		try {
			baseFilePath = new java.io.File(".").getCanonicalPath() + "/images/";
	   	} catch (IOException e) {
	  		e.printStackTrace();
	    }
	    		
		//Get the filename from user
    	while(fileName == null || fileName.trim().equals("")){
    		fileName += PartyToolkit.openInput(window, "Enter the name of the GIF you wish to display",
                    "GIF Name");  		
    	}
    	
    	//add file extension if missing
    	if (!fileName.endsWith(".gif")) {
			fileName += ".gif";
        }
    	
		//Combine for filePath
		filePath = baseFilePath + fileName;
		
		//Check if file exists - if not use failed.gif
		sourceFile = new File(filePath);
		if(!sourceFile.exists()){
			filePath = baseFilePath + "failed.gif";
			sourceFile = new File(filePath);	
		}
		
		//converts image file into ImageInputStream
		try {
			iis = ImageIO.createImageInputStream(sourceFile);
		} catch (IOException e) {
			
		}
				
		//false flag for file containing multiple images (GIFs)
		reader.setInput(iis, false);
		
		//Get number of images contained in .gif
		try {
			numberofImages = reader.getNumImages(true);
		} catch (IOException e) {

		}
		
    }

    /**
     * Since we're doing something over time, we need to implement increment()
     * 
     * @param percentage The percentage of the way through the animation we are. This is between 0 and 1
     */
    public void increment(double percentage) {
        int newFadedRows = 0;
        
        int imageIndex = (int) Math.floor(percentage * numberofImages);

        BufferedImage image = null;
		
        //get current image from GIF
		try {
			image = reader.read(imageIndex);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//re-scale - flag 2 is for the compression method
		if(image.getHeight() > LEDDimensions || image.getWidth() > LEDDimensions){
			image.getScaledInstance(16,16, 2);
		}
		
        // For every element we're given
        for (ElementController controller : getElements()) {
            // We only put LEDS in our getSupportedTypes(), so that's all we're going to get.
            LEDPanelController panel = (LEDPanelController) controller;

            // How many rows should be on based on our percentage?
            int rowsOn = (int) (percentage * panel.getPanelHeight());
            
            //Change all the rows
            for (int r = 0; r < panel.getPanelHeight() && r < image.getHeight(); r++) {
                    // and every column in that row
                    for (int c = 0; c < panel.getPanelWidth() && c < image.getWidth(); c++) {
                        
                    	// Set the color to the color to that of the images pixel
                    	int temp = image.getRGB(c, r);
                    	int red = (temp >> 16) & 0x000000FF;
                    	int green = (temp >>8 ) & 0x000000FF;
                    	int blue = (temp) & 0x000000FF;
                    	
                    	//create color object using values from image
                    	color = sysColor(red, green, blue);
                    	
                    	//set pixel color
                        panel.setColor(r, c, color);
                    }
                }

            newFadedRows = rowsOn;
        }

        fadedRows = newFadedRows;
    }

    /**
     * This returns the kind of elements we support with this animation. In this case, it's simply LED Panels
     */
    public static EnumSet<ElementType> getSupportedTypes() {
        return EnumSet.of(ElementType.LEDS);
    }
}
