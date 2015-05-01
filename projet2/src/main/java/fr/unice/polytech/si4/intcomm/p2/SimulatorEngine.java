package fr.unice.polytech.si4.intcomm.p2;

import org.jblas.FloatMatrix;

/**
 * This class contains all the methods for the simulation
 *
 * @author mmultari
 * @version 01/05/2015
 */
public class SimulatorEngine {


    /**
     * Calculates viewpoint angles between a projectile and an observer at one period
     *
     * @param proj   the projectile
     * @param obs    the observer
     * @param period the period of time
     * @return the viewpoint angle
     */
    public float getViewPointAngle(ProjectileMobile proj, ObserverMobile obs, int period) {
        if (period < proj.getLocations()[0].length) {
            if (period < obs.getLocations()[0].length) {
                return (float) Math.atan2((obs.getYVector(period) - proj.getYVector(period)), (obs.getXVector(period) - proj.getXVector(period)));
            }
        }
        return -1000f;
    }

    /**
     * Calculates viewpoint angles between a projectile and an observer for all periods
     *
     * @param proj      the projectile
     * @param obs       the observer
     * @param maxPeriod the maximum period
     * @return
     */
    public float[] getAllViewPointAngles(ProjectileMobile proj, ObserverMobile obs, int maxPeriod) {
        float[] allViewPointAngles = new float[maxPeriod + 1];
        for (int i = 0; i <= maxPeriod; i++) {
            allViewPointAngles[i] = getViewPointAngle(proj, obs, maxPeriod);
        }
        return allViewPointAngles;
    }

    /**
     * Calculates C matrix with the method of least squares
     *
     * @param angles
     * @return FloatMatrix
     */

    public FloatMatrix CMatrix(float[] angles){
        FloatMatrix c = new FloatMatrix(angles.length,4);
        for (int i = 0; i < angles.length ; i++) {
            c.put(i,0,(float)Math.sin(angles[i]));
            c.put(i,1,(float)((-1)*Math.cos(angles[i])));
            c.put(i,2,(float)(i*Math.sin(angles[i])));
            c.put(i,3,(float)(-i*Math.cos(angles[i])));
        }
        return c;
    }

/**
 * Calculates Y matrix from X matrix with the method of least squares
 * @param
  */

    public FloatMatrix YMatrix(float[] angles, float[][] locations){
        FloatMatrix y = new FloatMatrix(angles.length,1);
        for (int i = 0; i < angles.length ; i++) {
            y.put(i,0,(float)(locations[0][i]*Math.sin(angles[i])-locations[1][i]*Math.cos(angles[i])));
        }
        return y;
    }


}
