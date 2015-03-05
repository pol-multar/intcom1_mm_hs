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
    public static final int FLOOR_HEIGHT = 50;
    private static final int UFO_X = BackgroundPane.MAP_WIDTH / 2;
    private static final int UFO_Y = BackgroundPane.MAP_HEIGHT / 3;
    private static final int NB_COWS = 3;
    private static final int WIND_MAX_SPEED = 50;
    private static final int LASER_WIDTH = (int)(UfoPane.UFO_WIDTH/7.8);
    private static final int LASER_HEIGHT = UfoPane.UFO_HEIGHT;

    private Ufo ufo;
    private List<Cow> cows;
    private int windSpeed;
    private int cowsCaptured;
    private Chrono chrono;
    private Thread chronoThread;
    private Thread ufoThread;
    private CowCatcherFrame frame;

    public GameEngine() {
        ufo = new Ufo(UFO_X, UFO_Y);
        ufo.addObserver(this);
        cows = new LinkedList<Cow>();
        for(int i=0; i<NB_COWS; i++){
            cows.add(new Cow((int)(Math.random()*BackgroundPane.MAP_WIDTH)));
        }
        chrono = new Chrono();
        processWindSpeed();

        frame = new CowCatcherFrame(this);

        ufoThread = new Thread(ufo);
        chronoThread = new Thread(chrono);
        ufoThread.start();
        chronoThread.start();
    }

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

    public Chrono getChrono() {
        return chrono;
    }

    public int getCowsCaptured() {
        return cowsCaptured;
    }

    private void processWindSpeed() {
        windSpeed = (int) (Math.random() * WIND_MAX_SPEED);
        if (Math.random() < 0.5) {
            windSpeed *= -1;
        }
    }

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

    public void reset() {
        cowsCaptured = 0;
        ufo.reset(UFO_X, UFO_Y);

        for (int i = 0; i < cows.size(); i++) {
            cows.get(i).reset((int)(Math.random()*BackgroundPane.MAP_WIDTH));
        }

        processWindSpeed();
        resetChrono();
        resetUfo();

        setChanged();
        notifyObservers();
    }

    public void zapp(){
        Rectangle rect = new Rectangle(ufo.getX()-LASER_WIDTH/2, ufo.getY(), LASER_WIDTH, LASER_HEIGHT);

        for(Cow c : cows){
            if(!c.isCaptured() && rect.contains(c.getX(), c.getY())){
                c.zapp();
                cowsCaptured++;
                setChanged();
                notifyObservers();
            }
        }

        if(cowsCaptured >= getNbCows()){
            win();
        }
    }

    public void win(){
        chrono.stop();
        ufo.stop();
        String str = "Bravo ! Vous avez capturé ";
        if(getNbCows() == 1){
            str+= "la vache en ";
        }else{
            str+="les "+getNbCows()+" vaches en ";
        }
        str+=chrono.getSec()+" seconde";
        if(chrono.getSec() > 1){
            str+="s";
        }
        str+=" !";
        JOptionPane.showMessageDialog(frame, str);
    }

    public void lose(){
        chrono.stop();
        JOptionPane.showMessageDialog(frame, "Vous vous êtes crashé... (vaches capturées : "+cowsCaptured+"/"+getNbCows()+").");
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
}
