import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * The core of the project.
 */
public class GameEngine extends Observable implements KeyListener, Observer {
    private static final int FRAME_WIDTH = 1067; // The window width
    private static final int FRAME_HEIGHT = 734; // The window height
    private static final int MAP_WIDTH = FRAME_WIDTH; // The game pane width
    private static final int MAP_HEIGHT = (int) (FRAME_HEIGHT / 1.2234); // The game pane height
    private static final int FLOOR_HEIGHT = 50; // The height of the floor (px)
    private static final int UFO_X = MAP_WIDTH / 2; // The init x location of the ufo
    private static final int UFO_Y = MAP_HEIGHT / 3; // The init y location of the ufo
    private static final int NB_COWS = 5; // The number of cows
    private static final int WIND_MAX_SPEED = 10; // The maximum speed of wind (m/s)
    private static final double G = 9.807; // The gravity (m/s^2)
    private static final double PX_M_RATIO = 42.3; // The ratio pixel/meter
    private static final int COW_HITBOX_SIZE = 30; // The hitbox of the cows

    private Ufo ufo; // The ufo
    private List<Cow> cows; // The cows
    private int windSpeed; // The speed of the wind
    private int nbCowsCaptured; // The number of cows captured
    private Chrono chrono; // The chrono runnable
    private Thread chronoThread; // The thread of the chrono
    private Thread ufoThread; // The thread of the ufo
    private CowCatcherFrame frame; // The main view

    public GameEngine() {
        // Init the ufo
        ufo = new Ufo(this, UFO_X, UFO_Y);
        ufo.addObserver(this);
        // Init the cows
        cows = new LinkedList<Cow>();
        for (int i = 0; i < NB_COWS; i++) {
            cows.add(new Cow((int) (Math.random() * MAP_WIDTH), MAP_HEIGHT - FLOOR_HEIGHT));
        }
        // Init the chrono
        chrono = new Chrono();
        // Init the wind
        processWindSpeed();

        // Init the view
        frame = new CowCatcherFrame(FRAME_WIDTH, FRAME_HEIGHT, MAP_WIDTH, MAP_HEIGHT, this);

        // Start the threads
        ufoThread = new Thread(ufo);
        chronoThread = new Thread(chrono);
        ufoThread.start();
        chronoThread.start();
    }

    /**
     * Processes the speed of the wind (random)
     */
    private void processWindSpeed() {
        windSpeed = (int) (Math.random() * WIND_MAX_SPEED);
        if (Math.random() < 0.5) {
            windSpeed *= -1;
        }
    }

    /**
     * Restarts the chrono thread
     */
    private void resetChrono() {
        chrono.stop();
        try {
            chronoThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        chrono.reset();
        chrono.start();
        chronoThread = new Thread(chrono);
        chronoThread.start();
    }

    /**
     * Restarts the ufo thread
     */
    private void resetUfo() {
        ufo.stop();
        try {
            ufoThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ufo.start();
        ufoThread = new Thread(ufo);
        ufoThread.start();
    }

    /**
     * Restarts the game
     */
    public void reset() {
        nbCowsCaptured = 0;
        ufo.reset(UFO_X, UFO_Y);

        for (Cow c : cows) {
            c.reset((int) (Math.random() * MAP_WIDTH), MAP_HEIGHT - FLOOR_HEIGHT);
        }

        processWindSpeed();
        resetChrono();
        resetUfo();

        setChanged();
        notifyObservers();
    }

    /**
     * The ufo is firing his laser, zapp the cows touched by the laser !
     */
    public void zapp() {
        Rectangle rect = new Rectangle(ufo.getX() - ufo.getLaserWidth() / 2, ufo.getY(), ufo.getLaserWidth(), ufo.getLaserHeight());

        for (Cow c : cows) {
            if (!c.isCaptured() && rect.intersects(new Rectangle(c.getX() - COW_HITBOX_SIZE / 2, c.getY() - COW_HITBOX_SIZE / 2, COW_HITBOX_SIZE, COW_HITBOX_SIZE))) {
                c.zapp();
                nbCowsCaptured++;
                setChanged();
                notifyObservers();
            }
        }

        if (nbCowsCaptured >= getNbCows()) {
            win();
        }
    }

    /**
     * The victory message
     */
    public void win() {
        chrono.stop();
        ufo.stop();
        String str = "Bravo ! Vous avez capturé ";
        if (getNbCows() == 1) {
            str += "la vache en ";
        } else {
            str += "les " + getNbCows() + " vaches en ";
        }
        str += chrono.getSec() + " seconde";
        if (chrono.getSec() > 1) {
            str += "s";
        }
        str += " !";
        JOptionPane.showMessageDialog(frame, str, "Gagné !", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * The loss message
     */
    public void lose() {
        chrono.stop();
        JOptionPane.showMessageDialog(frame, "Vous vous êtes crashé... (vaches capturées : " + nbCowsCaptured + "/" + getNbCows() + ").", "Perdu !", JOptionPane.PLAIN_MESSAGE);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!ufo.isCrashed() && !ufo.isStopped()) {
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                ufo.startDownEngine();
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                ufo.startRightEngine();
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                ufo.startLeftEngine();
            } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                ufo.setLaser(true);
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_R) {
            reset();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            ufo.stopDownEngine();
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            ufo.stopRightEngine();
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            ufo.stopLeftEngine();
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            ufo.setLaser(false);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        Ufo tmp = (Ufo) o;
        if (tmp.isCrashed()) {
            lose();
        }
        if (tmp.isLaser()) {
            zapp();
        }
    }

    // Getters & Setters

    public Ufo getUfo() {
        return ufo;
    }

    public Cow getCow(int i) {
        return cows.get(i);
    }

    public int getNbCows() {
        return cows.size();
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public double getPxMRatio() {
        return PX_M_RATIO;
    }

    public double getG() {
        return G;
    }

    public Chrono getChrono() {
        return chrono;
    }

    public int getMapHeight() {
        return MAP_HEIGHT;
    }

    public int getNbCowsCaptured() {
        return nbCowsCaptured;
    }

    public int getFloorHeight() {
        return FLOOR_HEIGHT;
    }
}
