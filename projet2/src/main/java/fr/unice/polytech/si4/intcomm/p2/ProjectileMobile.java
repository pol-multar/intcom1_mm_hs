package fr.unice.polytech.si4.intcomm.p2;

/**
 * This class represents a projectile.
 * A projectile is a mobile launch at constant speed
 * on a constant path.
 *
 * @author mmultari
 * @version 01/05/2015
 */
public class ProjectileMobile extends Mobile {

    //Initial location
    private float x0;
    private float y0;
    //speeds of the projectile
    // -> have to be estimated by the observer
    private float vx;
    private float vy;


    /**
     * Constructor with initial location
     *
     * @param x  abscissa of the initial location
     * @param y  ordinate of the initial location
     * @param vx x-relative speed
     * @param vy y-relative speed
     */
    public ProjectileMobile(float x, float y, float vx, float vy) {

        this.locations = new float[2][];

        //location initialisation
        this.x0 = x;
        this.y0 = y;

        //speed initialisation
        this.vx = vx;
        this.vy = vy;

    }


    public float getX0() {
        return this.x0;
    }


    public float getY0() {
        return this.y0;
    }


    public float getVx() {
        return this.vx;
    }


    public float getVy() {
        return this.vy;
    }

    /**
     * Method in charge of the projectile movement
     *
     * @param period the maximum period
     */
    public void computePath(int period) {
        this.locations = new float[2][period + 1];
        for (int t = 0; t <= period; t++) {
            // x(t) = x0 + Vx * t
            this.locations[0][t] = this.x0 + this.vx * t;
            // y(t) = y0 + Vy * t
            this.locations[1][t] = this.y0 + this.vy * t;
        }
    }

    /**
     * There is no noise on the projectile
     *
     * @param period the maximum period
     */
    public void computeNoisedPath(int period) {
        noisedLocations = new float[2][period + 1];
        for (int t = 0; t <= period; t++) {
            noisedLocations[0][t] = this.x0 + this.vx * t;
            noisedLocations[1][t] = this.y0 + this.vy * t;
        }
    }

    public void updateTrajectory(int period) {
        m_x = this.x0 + this.vx * period;
        m_y = this.y0 + this.vy * period;
    }
}
