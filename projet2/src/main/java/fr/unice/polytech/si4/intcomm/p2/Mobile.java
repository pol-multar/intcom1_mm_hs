package fr.unice.polytech.si4.intcomm.p2;

import org.jblas.DoubleMatrix;
import org.jblas.FloatMatrix;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Abstract class used to define the same elements between mobiles types
 *
 * @author mmultari
 * @version 01/05/2015
 */
public abstract class Mobile {

    /**
     * Matrix containing mobile locations as a function of time t
     * [0][t] contains x(t) vector
     * [1][t] contains y(t) vector
     */
    protected float[][] locations;

    /* The same matrix with computation noise */
    protected float[][] noisedLocations;

    /* Noise computation constant */
    protected static final float NOISE = 0.001f;


    public float[][] getLocations() {
        return locations;
    }

    public float[][] getNoisedLocations() {
        return noisedLocations;
    }

    public static float getNOISE() {
        return NOISE;
    }

    public float getXVector(int time) {
        if (locations[0].length < time) {
            return -1;//Index error
        } else {
            return locations[1][time];
        }
    }

    public float getYVector(int time) {
        if (locations[1].length < time) {
            return -1;//Index error
        } else {
            return locations[1][time];
        }
    }

    public float getNoisedXVector(int time) {
        if (noisedLocations[0].length < time) {
            return -1;//Index error
        } else {
            return noisedLocations[1][time];
        }
    }

    public float getNoisedYVector(int time) {
        if (noisedLocations[1].length < time) {
            return -1;//Index error
        } else {
            return noisedLocations[1][time];
        }
    }

    public abstract void computePath(int period);
    public abstract void computeNoisedPath(int period);

    public String toStringLocations() {
        String s = "";
        for (int i = 0; i < locations.length; i++) {
            s += locations[0][i] + " " + locations[1][i] + "\n";
        }
        return s;
    }

    public String toStringNoisedLocations() {
        String s = "";
        for (int i = 0; i < noisedLocations.length; i++) {
            s += noisedLocations[0][i] + " " + noisedLocations[1][i] + "\n";
        }
        return s;
    }

    public String toString() {
        return this.toStringLocations() + "\n" + this.toStringNoisedLocations();
    }

    /**
     * Method used to print locations into a file
     * @param fileName the name of the file to be created
     * @param noise if you want noised locations or not
     */
    public void toFile(String fileName, boolean noise) {

        // "current folder"/"fileName"
        String filePath = System.getProperty("user.dir")+"/"+fileName;
        String allLocations;

        if ( !noise ) {
            allLocations = toStringLocations();
        }
        else{
            allLocations = toStringNoisedLocations();
        }

        try {
            FileWriter fw = new FileWriter(filePath, false);
            BufferedWriter output = new BufferedWriter(fw);
            output.write(allLocations);
            output.flush();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
