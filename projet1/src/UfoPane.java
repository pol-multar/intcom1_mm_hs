import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * The ufo panel
 */
public class UfoPane extends JPanel implements Observer {
    private static final String UFO_PATH = "res/UFO.png";
    private static final String UFO_CRASHED_PATH = "res/UFO_crashed.png";
    private static final String LEFT_ENGINE_PATH = "res/left_flame.png";
    private static final String RIGHT_ENGINE_PATH = "res/right_flame.png";
    public static final int UFO_WIDTH = 120;
    public static final int UFO_HEIGHT = 120;
    private static final Image UFO_IMAGE = new ImageIcon(UFO_PATH).getImage();
    private static final Image UFO_CRASHED_IMAGE = new ImageIcon(UFO_CRASHED_PATH).getImage();
    private static final Image LEFT_ENGINE_IMAGE = new ImageIcon(LEFT_ENGINE_PATH).getImage();
    private static final Image RIGHT_ENGINE_IMAGE = new ImageIcon(RIGHT_ENGINE_PATH).getImage();
    private Ufo ufo;

    public UfoPane(Ufo ufo) {
        this.ufo = ufo;
        ufo.addObserver(this);
        setPreferredSize(new Dimension(UFO_WIDTH, UFO_HEIGHT));
        setOpaque(false);
        setBounds(ufo.getX() - UFO_WIDTH / 2, ufo.getY() - UFO_HEIGHT / 2, UFO_WIDTH, UFO_HEIGHT);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!ufo.isCrashed()) {
            g.drawImage(UFO_IMAGE, 0, 0, UFO_WIDTH, UFO_HEIGHT, this);
        } else {
            g.drawImage(UFO_CRASHED_IMAGE, 0, 0, UFO_WIDTH, UFO_HEIGHT, this);
        }
        if (ufo.isLeftEngine() || ufo.isDownEngine()) {
            g.drawImage(LEFT_ENGINE_IMAGE, 0, 0, UFO_WIDTH, UFO_HEIGHT, this);
        }
        if (ufo.isRightEngine() || ufo.isDownEngine()) {
            g.drawImage(RIGHT_ENGINE_IMAGE, 0, 0, UFO_WIDTH, UFO_HEIGHT, this);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        ufo = (Ufo) o;
        setBounds(ufo.getX() - UFO_WIDTH / 2, ufo.getY() - UFO_HEIGHT / 2, UFO_WIDTH, UFO_HEIGHT);
    }

    // Getters & Setters

    @Override
    public int getWidth() {
        return UFO_WIDTH;
    }

    @Override
    public int getHeight() {
        return UFO_HEIGHT;
    }
}
