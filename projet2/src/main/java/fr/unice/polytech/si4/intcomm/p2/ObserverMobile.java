package fr.unice.polytech.si4.intcomm.p2;

/**
 * This class represents an observer.
 * An observer is a mobile launch at constant speed on a circular path.
 * It is responsible for finding the projectile's path.
 *
 * @author mmultari
 * @version 01/05/2015
 */
public class ObserverMobile extends Mobile {

    // The coordinate of the circle center of the observer
    private float cx;
    private float cy;

    // The radius of the circle
    private float r;

    // Initial viewpoint
    private float vp;

    // Speed
    private float speed;

    /**
     * Constructor with all parameters available except circle center
     *
     * @param initX      abscissa of the initial location
     * @param initY      ordinate of the initial location
     * @param initView   initial viewpoint
     * @param initRadius initial radius of the circle
     * @param initSpeed  the speed of the observer
     */
    public ObserverMobile(float initX, float initY, float initView, float initRadius, float initSpeed) {
        this(initX, initY, initView, initRadius, initSpeed, 0, 0);
    }

    public ObserverMobile(float initX, float initY, float initView, float initRadius, float initSpeed, float initCenterX, float initCenterY) {
        this.m_x = initX;
        this.m_y = initY;
        this.r = initRadius;
        this.vp = initView;
        this.speed = initSpeed;
        this.cx = initCenterX;
        this.cy = initCenterY;
    }

    public float getCenterX() {
        return cx;
    }

    public float getCenterY() {
        return cy;
    }

    /**
     * Method in charge of the observer movement
     *
     * @param maxPeriod
     */
    public void computePath(int maxPeriod) {
        this.locations = new float[2][maxPeriod + 1];
        for (int t = 0; t <= maxPeriod; t++) {
            // x(t) = xc + radius * cos(speed * t + phi)
            this.locations[0][t] = (float) (this.cx + this.r * Math.cos(this.speed * t + this.vp));
            // y(t) = yc + radius * sin(speed * t + phi)
            this.locations[1][t] = (float) (this.cy + this.r * Math.sin(this.speed * t + this.vp));
        }
    }

    /**
     * Method in charge of the observer movement with noise
     *
     * @param maxPeriod
     */
    public void computeNoisedPath(int maxPeriod) {
        this.noisedLocations = new float[2][maxPeriod + 1];
        for (int t = 0; t <= maxPeriod; t++) {
            this.noisedLocations[0][t] = (float) (this.cx + this.r * Math.cos(this.speed * t + this.vp)) + this.NOISE;
            this.noisedLocations[1][t] = (float) (this.cy + this.r * Math.sin(this.speed * t + this.vp)) + this.NOISE;
        }
    }

    public void updateTrajectory(int period) {
        m_x = (float) (this.cx + this.r * Math.cos(this.speed * period + this.vp));
        m_y = (float) (this.cy + this.r * Math.sin(this.speed * period + this.vp));
    }

}
