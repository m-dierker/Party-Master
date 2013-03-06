package com.partyrock.settings;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.swt.graphics.Color;

import com.partyrock.anim.ElementAnimation;
import com.partyrock.element.ElementController;

/**
 * Handles saving and loading various types as a string so we can embed it in SectionSettings
 * 
 * @author Matthew
 * 
 */
public class Saver {

    public static String saveInt(int x) {
        return x + "";
    }

    public static int loadInt(String x, ElementAnimation animation) {
        return Integer.parseInt(x);
    }

    public static String saveDouble(double x) {
        return x + "";
    }

    public static double loadDouble(String x, ElementAnimation animation) {
        return Double.parseDouble(x);
    }

    public static String saveColor(Color c) {
        return saveInt(c.getRed()) + "/" + saveInt(c.getGreen()) + "/" + saveInt(c.getBlue());
    }

    public static Color loadColor(String x, ElementAnimation animation) {
        String[] arr = x.split("/");
        return new Color(animation.getMainWindow().getDisplay(), loadInt(arr[0], animation),
                loadInt(arr[1], animation), loadInt(arr[2], animation));
    }

    /**
     * This can only be used to save files in the Party-Master directory
     * 
     * @param f The file to save
     */
    public static String saveLocalFile(File f) {
        return f.getPath();
    }

    public static File loadLocalFile(String x, ElementAnimation animation) {
        return new File(x);
    }

    public static String saveElementsList(ArrayList<ElementController> elements) {
        String ret = "";
        for (int a = 0; a < elements.size(); a++) {
            ret += elements.get(a).getInternalID() + (a == elements.size() - 1 ? "" : "/");
        }
        return ret;
    }

    public static ArrayList<ElementController> loadElementsList(String x, ElementAnimation animation) {
        String[] arr = x.split("/");
        ArrayList<ElementController> ret = new ArrayList<ElementController>();
        for (int a = 0; a < arr.length; a++) {
            ElementController element = animation.getMaster().getLocationManager().getElementByInternalID(arr[a]);
            if (element == null) {
                System.err.println("WARNING: Could not find element with id " + arr[a]
                        + ". This animation may be missing elements.");
            } else {
                ret.add(element);
            }
        }
        return ret;
    }
}
