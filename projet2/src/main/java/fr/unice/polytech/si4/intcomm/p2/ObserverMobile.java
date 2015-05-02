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

    //The coordinate of the circle center of the observer
    private float m_x;
    private float m_y;

    //The radius of the circle
    private float m_r;

    //Initial viewpoint
    private float m_vp;

    //Speed
    private float m_sp;

    /**
     * Constructor with all parameters available
     * @param initX abscissa of the initial location
     * @param initY ordinate of the initial location
     * @param initRadius initial radius of the circle
     * @param initSpeed the speed of the observer
     * @param initView initial viewpoint
     */
    public ObserverMobile(float initX, float initY, float initRadius, float initSpeed, float initView){
        m_x=initX;
        m_y=initY;
        m_r =initRadius;
        m_sp =initSpeed;
        m_vp =initView;
    }

    /**
     * Constructor without initial viewpoint
     * @param initX abscissa of the initial location
     * @param initY ordinate of the initial location
     * @param initRadius initial radius of the circle
     * @param initSpeed the speed of the observer
     */
    public ObserverMobile(float initX, float initY, float initRadius, float initSpeed){
        this(initX,initY,initRadius,initSpeed,0f);
    }

    /**
     * Constructor with only radius and speed
     * @param initRadius initial radius of the circle
     * @param initSpeed the speed of the observer
     */
    public ObserverMobile(float initRadius,float initSpeed){
        this(0,0,initRadius,initSpeed,0);
    }

    /**
     * Method in charge of the observer movement
     * @param period
     */
    @Override
    public void computePath(int period) {
        locations= new float[2][period+1];
        for (int i = 0; i <= period ; i++) {
            // x(t) = xc + radius * cos(v * t + phi)
            locations[0][i]=(float) (m_x+m_r*Math.cos(m_sp*i+m_vp));
            // y(t) = yc + radius * sin(v * t + phi)
            locations[1][i]=(float) (m_y+m_r*Math.cos(m_sp*i+m_vp));
        }

    }

    /**
     * Method in charge of the observer movement with noise
     * @param period
     */
    @Override
    public void computeNoisedPath(int period) {
        noisedLocations= new float[2][period+1];
        for (int i = 0; i <=period ; i++) {
            noisedLocations[0][i]=(float) (m_x+m_r*Math.cos(m_sp*i+m_vp))+ NOISE;
            noisedLocations[1][i]=(float) (m_y+m_r*Math.cos(m_sp*i+m_vp))+ NOISE;
        }

    }
}
