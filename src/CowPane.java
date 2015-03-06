import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * The cow pane
 */
public class CowPane extends JPanel implements Observer {
    private static final String COW_PATH = "res/cow.png";
    private static final String ZAPPED_COW_PATH = "res/cow_zapped.png";
    private static final int COW_WIDTH = 100;
    private static final int COW_HEIGHT = 55;
    private static final Image COW_IMAGE = new ImageIcon(COW_PATH).getImage();
    private static final Image ZAPPED_COW_IMAGE = new ImageIcon(ZAPPED_COW_PATH).getImage();
    private static final int TIME_BEFORE_FADE = 500;
    private Cow cow;

    public CowPane(Cow cow) {
        this.cow = cow;
        cow.addObserver(this);
        setPreferredSize(new Dimension(COW_WIDTH, COW_HEIGHT));
        setOpaque(false);
        setBounds(cow.getX() - COW_WIDTH / 2, cow.getY() - COW_HEIGHT / 2, COW_WIDTH, COW_HEIGHT);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (cow.isCaptured()) {
            g.drawImage(ZAPPED_COW_IMAGE, 0, 0, COW_WIDTH, COW_HEIGHT, this);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(TIME_BEFORE_FADE);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    CowPane.this.setVisible(false);
                }
            }).start();
        } else {
            g.drawImage(COW_IMAGE, 0, 0, COW_WIDTH, COW_HEIGHT, this);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        cow = (Cow) o;
        setBounds(cow.getX() - COW_WIDTH / 2, cow.getY() - COW_HEIGHT / 2, COW_WIDTH, COW_HEIGHT);
        if (!cow.isCaptured() && !isVisible()) {
            setVisible(true);
        }
        repaint();
    }

    // Getters & Setters

    @Override
    public int getWidth() {
        return COW_WIDTH;
    }

    @Override
    public int getHeight() {
        return COW_HEIGHT;
    }
}
