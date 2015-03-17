import java.util.Observable;

/**
 * The UFO !
 */
public class Ufo extends Observable implements Runnable {
    private static final int Te = 40; // period
    private static final int THRUST = 25; // engines thrust (kg*m/s^2)
    private static final double FUEL_CONSUMPTION = 0.001;
    private static final int MASS = 6000; // mass (kg)
    private static final int FUEL_MASS = 800; // fuel mass (kg)
    private static final int SPECIFIC_I = 4500; // specific impulse (m/s)
    private static final int LASER_WIDTH = (int) (UfoPane.UFO_WIDTH / 7.8); // The laser hitbox width
    private static final int LASER_HEIGHT = UfoPane.UFO_HEIGHT; // The laser hitbox height

    private GameEngine context;
    private double fuelTank = 1.;
    private double vX;
    private double vY;
    private double m_x;
    private double m_y;
    private boolean leftEngine;
    private boolean rightEngine;
    private boolean downEngine;
    private boolean crashed;
    private boolean stop;
    private boolean laser;
    private boolean auto;

    public Ufo(GameEngine context, int x, int y) {
        this.context = context;
        reset(x, y);
    }

    /**
     * Resets the ufo
     *
     * @param x the x position
     * @param y the y position
     */
    public void reset(int x, int y) {
        fuelTank = 1.;
        vX = 0;
        vY = 0;
        m_x = x;
        m_y = context.getMapHeight() - y;
        crashed = false;
        auto = false;

        shutDownAll();
    }

    private void shutDownAll() {
        leftEngine = false;
        rightEngine = false;
        downEngine = false;
        laser = false;
    }

    /**
     * Stops the ufo
     */
    public void stop() {
        stop = true;
        vX = 0;
        vY = 0;
        shutDownAll();

        setChanged();
        notifyObservers();
    }

    /**
     * Crashes the ufo
     */
    public void crash() {
        crashed = true;
        stop();
    }

    /**
     * Processes the movements of the ufo
     */
    @Override
    public void run() {
        while (!stop) {
            double dt = Te / 1000.;
            double aY, taY, aX, taX;
            int comX = 0, comY = 0;

            Cow c = null;
            for(int i=0; i<context.getNbCows(); i++){
                if(!context.getCow(i).isCaptured()){
                    c = context.getCow(i);
                }
            }
            if(c==null){
                return;
            }

            if (auto) {
                shutDownAll();
                if(c.getX() < m_x){
                    leftEngine = false;
                    rightEngine=true;
                } else if(c.getX() > m_x){
                    leftEngine=true;
                    rightEngine=false;
                }

                if(m_y < 300){
                    downEngine=true;
                    laser = true;
                } else {
                    downEngine=false;
                    laser=false;
                }
            }

            if (fuelTank <= 0) {
                leftEngine = false;
                rightEngine = false;
                downEngine = false;
                laser = false;
            }

            if (downEngine) {
                comY += THRUST;
                fuelTank -= FUEL_CONSUMPTION;
            }
            if (leftEngine) {
                comX += THRUST;
                fuelTank -= FUEL_CONSUMPTION;
            }
            if (rightEngine) {
                comX -= THRUST;
                fuelTank -= FUEL_CONSUMPTION;
            }
            if (laser) {
                fuelTank -= FUEL_CONSUMPTION;
            }

            double erg = SPECIFIC_I / (MASS + FUEL_MASS * fuelTank);
            taY = erg * comY;
            taX = erg * comX;
            aY = taY - context.getG();
            aX = taX + context.getWindSpeed();
            m_x = (m_x / GameEngine.PX_M_RATIO + dt * vX + (0.5) * dt * dt * aX) * GameEngine.PX_M_RATIO;
            m_y = (m_y / GameEngine.PX_M_RATIO + dt * vY + (0.5) * dt * dt * aY) * GameEngine.PX_M_RATIO;
            vX = vX + aX * dt;
            vY = vY + aY * dt;

            if (getHeight() <= 0) {
                m_y = context.getFloorHeight();
                crash();
                break;
            }

            setChanged();
            notifyObservers();
            try {
                Thread.sleep(Te);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Getters & Setters

    public double getHeight() {
        return (m_y - context.getFloorHeight()) / GameEngine.PX_M_RATIO;
    }

    public int getLaserHeight() {
        return LASER_HEIGHT;
    }

    public int getLaserWidth() {
        return LASER_WIDTH;
    }

    public boolean isStopped() {
        return stop;
    }

    public void start() {
        stop = false;
    }

    public boolean isRightEngine() {
        return rightEngine;
    }

    public double getFuelTank() {
        return fuelTank;
    }

    public int getSpeed() {
        return (int) Math.sqrt(vX * vX + vY * vY);
    }

    public int getX() {
        return (int) Math.round(m_x);
    }

    public int getY() {
        return (int) Math.round(context.getMapHeight() - m_y);
    }

    public boolean isLeftEngine() {
        return leftEngine;
    }

    public boolean isDownEngine() {
        return downEngine;
    }

    public boolean isLaser() {
        return laser;
    }

    public boolean isCrashed() {
        return crashed;
    }

    public void setLaser(boolean b) {
        if (fuelTank > 0 && !auto) {
            laser = b;
        }
    }

    public void startLeftEngine() {
        if (fuelTank > 0 && !auto) {
            leftEngine = true;
        }
    }

    public void startRightEngine() {
        if (fuelTank > 0 && !auto) {
            rightEngine = true;
        }
    }

    public void startDownEngine() {
        if (fuelTank > 0 && !auto) {
            downEngine = true;
        }
    }

    public void stopLeftEngine() {
        leftEngine = false;
    }

    public void stopRightEngine() {
        rightEngine = false;
    }

    public void stopDownEngine() {
        downEngine = false;
    }

    public void setAuto(boolean b) {
        auto = b;
        shutDownAll();
    }
}
