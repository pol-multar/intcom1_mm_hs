import java.util.Observable;

/**
 * The UFO !
 */
public class Ufo extends Observable implements Runnable {
    private double fuelTank = 1.;
    private int speed;
    private int m_x;
    private int m_y;
    private boolean leftEngine;
    private boolean rightEngine;
    private boolean downEngine;
    private boolean crashed;
    private boolean stop;
    private boolean laser;

    public Ufo(int x, int y) {
        m_x = x;
        m_y = y;
    }

    public void reset(int x, int y){
        fuelTank = 1.;
        speed=0;
        m_x = x;
        m_y = y;
        leftEngine = false;
        rightEngine = false;
        crashed = false;
        laser = false;
    }


    public void stop(){
        stop = true;
    }

    public void start(){
        stop = false;
    }

    public boolean isRightEngine() {
        return rightEngine;
    }

    public double getFuelTank() {
        return fuelTank;
    }

    public int getSpeed() {
        return speed;
    }

    public int getX() {
        return m_x;
    }

    public int getY() {
        return m_y;
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
        laser = b;
        setChanged();
        notifyObservers();
    }

    public void startLeftEngine() {
        if (fuelTank > 0) {
            leftEngine = true;
        }
    }

    public void startRightEngine() {
        if (fuelTank > 0) {
            rightEngine = true;
        }
    }

    public void startDownEngine() {
        if (fuelTank > 0) {
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

    public int getHeight() {
        return BackgroundPane.MAP_HEIGHT - m_y - GameEngine.FLOOR_HEIGHT;
    }

    @Override
    public void run() {
        // TODO
        while (!crashed && !stop) {
            m_y++;
            if (downEngine) {
                m_y -= 2;
                fuelTank -= 0.001;
            }
            if (leftEngine) {
                m_x += 1;
                fuelTank -= 0.001;
            }
            if (rightEngine) {
                m_x -= 1;
                fuelTank -= 0.001;
            }

            if (getHeight() <= 0) {
                crashed = true;
                leftEngine = false;
                rightEngine = false;
                laser = false;
            }

            setChanged();
            notifyObservers();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
