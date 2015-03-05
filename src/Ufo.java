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
        downEngine = false;
        crashed = false;
        laser = false;
    }


    public void stop(){
        stop = true;
        speed=0;
        leftEngine = false;
        rightEngine = false;
        downEngine = false;
        laser = false;

        setChanged();
        notifyObservers();
    }

    public boolean isStopped(){
        return stop;
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

    public double getHeight() {
        return BackgroundPane.MAP_HEIGHT - m_y - GameEngine.FLOOR_HEIGHT;
    }

    public void crash(){
        crashed = true;
        leftEngine = false;
        rightEngine = false;
        downEngine = false;
        laser = false;
    }

    @Override
    public void run() {
        // TODO
        while (!crashed && !stop) {
            speed = 0;
            m_y++;
            if (downEngine) {
                speed++;
                m_y -= 2;
                fuelTank -= 0.001;
            }
            if (leftEngine) {
                speed++;
                m_x ++;
                fuelTank -= 0.001;
            }
            if (rightEngine) {
                speed++;
                m_x --;
                fuelTank -= 0.001;
            }
            if(laser){
                fuelTank -= 0.001;
            }

            if (getHeight() <= 0) {
                crash();
            }
            if(fuelTank <= 0){
                leftEngine=false;
                rightEngine=false;
                downEngine=false;
                laser=false;
            }

            setChanged();
            notifyObservers();
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
