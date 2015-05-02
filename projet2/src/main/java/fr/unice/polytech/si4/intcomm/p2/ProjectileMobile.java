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
    private float m_x0;
    private float m_y0;

    //speeds of the projectile
    // -> have to be estimated by the observer
    private float m_vx;
    private float m_vy;

    /**
     * Constructor with initial location
     * @param x abscissa of the initial location
     * @param y ordinate of the initial location
     * @param vx x-relative speed
     * @param vy y-relative speed
     */
    public ProjectileMobile(float x, float y, float vx, float vy) {

        this.locations = new float[2][];

        //location initialisation
        m_x0 = x;
        m_y0 = y;

        //speed initialisation
        m_vx = vx;
        m_vy = vy;
    }

    /**
     * Default constructor with [0,0] for initial location
     * @param vx x-relative speed
     * @param vy y-relative speed
     */
    public ProjectileMobile(float vx, float vy) {
        this(0, 0, vx, vy);
        this.locations = new float[1][];
    }

    //Accessors
    public float getX0() {
        return m_x0;
    }

    public float getY0() {
        return m_y0;
    }

    public float getVX() {
        return m_vx;
    }

    public float getVY() {
        return m_vy;
    }

    /**
     * Method in charge of the projectile movement
     *
     * @param period the maximum period
     */
    @Override
    public void computePath(int period) {
        this.locations = new float[2][period+1];
        for (int i = 0; i <= period; i++) {
            // x(t) = x0 + Vx * t
            locations[0][i] = m_x0 + m_vx * i;
            // y(t) = y0 + Vy * t
            locations[1][i] = m_y0 + m_vy * i;
        }

    }

    /**
     * There is no noise on the projectile
     *
     * @param period the maximum period
     */
    @Override
    public void computeNoisedPath(int period) {
    //TODO to complete if usefull
    }
}
