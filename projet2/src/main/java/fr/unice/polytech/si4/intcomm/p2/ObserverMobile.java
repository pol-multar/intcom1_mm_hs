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
    private float x;
    private float y;

    // The radius of the circle
    private float r;

    // Initial viewpoint
    private float vp;

    // Speed
    private float speed;

    /**
     * Constructor with all parameters available
     * @param initX abscissa of the initial location
     * @param initY ordinate of the initial location
     * @param initView initial viewpoint
     * @param initRadius initial radius of the circle
     * @param initSpeed the speed of the observer
     */
    public ObserverMobile(float initX, float initY, float initView, float initRadius, float initSpeed){
        this.x = initX;
        this.y = initY;
        this.r = initRadius;
        this.vp = initView;
        this.speed = initSpeed;
    }

    /**
     * Method in charge of the observer movement
     * @param period
     */
    public void computePath(int period) {
        this.locations = new float[2][period +1];
        for (int t = 0; t <= period; t++) {
            // x(t) = xc + radius * cos(speed * t + phi)
            this.locations[0][t] = (float) (this.x + this.r * Math.cos(this.speed * t + this.vp));
            // y(t) = yc + radius * sin(speed * t + phi)
            this.locations[1][t] = (float) (this.y + this.r * Math.sin(this.speed * t + this.vp));
        }
    }

    /**
     * Method in charge of the observer movement with noise
     * @param period
     */
    public void computeNoisedPath(int period) {
        this.noisedLocations = new float[2][period + 1];
        for (int i = 0; i <= period; i++) {
            this.noisedLocations[0][i] = (float) (this.x + this.r * Math.cos(this.speed * i + this.vp)) + this.NOISE;
            this.noisedLocations[1][i] = (float) (this.y + this.r * Math.sin(this.speed * i + this.vp)) + this.NOISE;
        }
    }

}
