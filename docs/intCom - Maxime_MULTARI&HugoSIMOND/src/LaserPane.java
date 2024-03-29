import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * The laser panel
 */
public class LaserPane extends JPanel implements Observer {
    private static final String LASER_PATH = "res/laser.png";
    private static final int LASER_WIDTH = UfoPane.UFO_WIDTH;
    private static final int LASER_HEIGHT = UfoPane.UFO_HEIGHT;
    private static final Image LASER_IMAGE = new ImageIcon(LASER_PATH).getImage();
    private Ufo ufo;
    private boolean isFiring;

    public LaserPane(Ufo ufo) {
        this.ufo = ufo;
        ufo.addObserver(this);
        setPreferredSize(new Dimension(LASER_WIDTH, LASER_HEIGHT));
        setOpaque(false);
        setBounds(ufo.getX() - LASER_WIDTH / 2, ufo.getY(), LASER_WIDTH, LASER_HEIGHT);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isFiring) {
            g.drawImage(LASER_IMAGE, 0, 0, LASER_WIDTH, LASER_HEIGHT, this);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (((Ufo) o).isLaser() != isFiring()) {
            setFire(((Ufo) o).isLaser());
        }
        setFire(((Ufo) o).isLaser());
    }

    // Getters & Setters

    @Override
    public int getWidth() {
        return LASER_WIDTH;
    }

    @Override
    public int getHeight() {
        return LASER_HEIGHT;
    }

    public void setFire(boolean b) {
        isFiring = b;
        if (b) {
            setBounds(ufo.getX() - LASER_WIDTH / 2, ufo.getY(), LASER_WIDTH, LASER_HEIGHT);
        }
        repaint();
    }

    public boolean isFiring() {
        return isFiring;
    }
}