package fr.unice.polytech.si4.intcomm.p2;

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

    //The initial location of the mobile
    protected float m_x;
    protected float m_y;

    /**
     * Matrix containing mobile locations as a function of time t
     * [0][t] contains x(t) vector
     * [1][t] contains y(t) vector
     */
    protected float[][] locations;

    /* The same matrix with computation noise */
    protected float[][] noisedLocations;




    public float[][] getLocations(){
        return this.locations;
    }

    public float[][] getNoisedLocations() {
        return this.noisedLocations;
    }


    public float getM_x() {
        return m_x;
    }

    public float getM_y() {
        return m_y;
    }

    public float getXat(int time) {
        if (this.locations[0].length < time) {
            return -1;
        }
        return this.locations[0][time];
    }

    public float getYat(int time) {
        if (this.locations[1].length < time) {
            return -1;
        }
        return this.locations[1][time];
    }



    public float getNoisedY(int t) {
        if (this.noisedLocations[0].length < t) {
            return -1;
        }
        return this.noisedLocations[0][t];
    }

    public float getYBruit(int t) {
        if (this.noisedLocations[1].length < t) {
            return -1;
        }
        return this.noisedLocations[1][t];
    }

    /* This method depends of the mobile type */
    public abstract void computePath(int period);
    /* This method depends of the mobile type */
    public abstract void computeNoisedPath(int period);

    public abstract void updateTrajectory(int period);


    public String toStringLocations() {
        String s = "";
        // Affichage des locations
        for (int i = 0; i < this.locations[0].length; i++) {
            s += this.locations[0][i] + " " + this.locations[1][i] + "\n";
        }
        return s;
    }

    public String toStringBruit() {
        String s = "";
        for (int i = 0; i < this.noisedLocations[0].length; i++) {
            s += this.noisedLocations[0][i] + " " + this.noisedLocations[1][i] + "\n";
        }
        return s;
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

        if ( !noise ) allLocations = toStringLocations();
        else allLocations = toStringBruit();

        try {
            FileWriter fw = new FileWriter(filePath, false);
            BufferedWriter output = new BufferedWriter(fw);
            output.write(allLocations);
            output.flush();
            output.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}
