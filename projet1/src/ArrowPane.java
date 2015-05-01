import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * The arrow that shows the ufo when not visible
 */
public class ArrowPane extends JLabel implements Observer {
    private static final String ARROW_UP_PATH = "res/arrow_up.png";
    private static final String ARROW_LEFT_PATH = "res/arrow_left.png";
    private static final String ARROW_RIGHT_PATH = "res/arrow_right.png";
    private static final Image ARROW_UP_IMAGE = new ImageIcon(ARROW_UP_PATH).getImage();
    private static final Image ARROW_LEFT_IMAGE = new ImageIcon(ARROW_LEFT_PATH).getImage();
    private static final Image ARROW_RIGHT_IMAGE = new ImageIcon(ARROW_RIGHT_PATH).getImage();
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final int ARROW_SIZE = 100;
    private ArrowState state;
    private Ufo ufo;
    private JPanel context;

    public ArrowPane(Ufo ufo, JPanel context) {
        this.context = context;
        this.ufo = ufo;
        state = ArrowState.NONE;
        ufo.addObserver(this);
        setPreferredSize(new Dimension(ARROW_SIZE, ARROW_SIZE));
        setHorizontalAlignment(SwingConstants.CENTER);
        setForeground(TEXT_COLOR);
        setOpaque(false);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (state == ArrowState.LEFT) {
            g.drawImage(ARROW_LEFT_IMAGE, 0, 0, ARROW_SIZE, ARROW_SIZE, this);
        } else if (state == ArrowState.RIGHT) {
            g.drawImage(ARROW_RIGHT_IMAGE, 0, 0, ARROW_SIZE, ARROW_SIZE, this);
        } else if (state == ArrowState.UP) {
            g.drawImage(ARROW_UP_IMAGE, 0, 0, ARROW_SIZE, ARROW_SIZE, this);
        }
        super.paintComponent(g);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (ufo.getY() < 0) {
            display(ufo.getX(), 0, ArrowState.UP);
            setText((int) ((ufo.getY() * -1) / GameEngine.PX_M_RATIO) + " m");
        } else if (ufo.getX() < 0) {
            display(0, ufo.getY(), ArrowState.LEFT);
            setText((int) ((ufo.getX() * -1) / GameEngine.PX_M_RATIO) + " m");
        } else if (ufo.getX() > context.getWidth()) {
            display(context.getWidth() - ARROW_SIZE, ufo.getY(), ArrowState.RIGHT);
            setText((int) ((ufo.getX() - context.getWidth()) / GameEngine.PX_M_RATIO) + " m");
        } else {
            hideArrow();
            setText("");
        }
    }

    /**
     * Hides the arrow if it isn't already hidden
     */
    public void hideArrow() {
        if (state != ArrowState.NONE) {
            state = ArrowState.NONE;
            repaint();
        }
    }

    /**
     * Correctly displays the arrow depending on the direction
     *
     * @param x     position where the arrow points
     * @param y     position where the arrow points
     * @param state direction of the arrow
     */
    public void display(int x, int y, ArrowState state) {
        this.state = state;
        int resX = 0, resY = 0;
        if (state == ArrowState.LEFT || state == ArrowState.RIGHT) {
            resX = x;
            resY = y - ARROW_SIZE / 2;
        } else if (state == ArrowState.UP) {
            resX = x - ARROW_SIZE / 2;
            resY = y;
        }
        setBounds(resX, resY, ARROW_SIZE, ARROW_SIZE);

        repaint();
    }

    // Getters & Setters

    @Override
    public int getWidth() {
        return ARROW_SIZE;
    }

    @Override
    public int getHeight() {
        return ARROW_SIZE;
    }
}