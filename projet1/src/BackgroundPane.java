import javax.swing.*;
import java.awt.*;

/**
 * The background panel
 */
public class BackgroundPane extends JPanel {
    private static final String BACKGROUND_PATH = "res/background.png";
    private int width;
    private int height;

    public BackgroundPane(int width, int height) {
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(new ImageIcon(BACKGROUND_PATH).getImage(), 0, 0, width, height, this);
    }

    // Getters & Setters

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
