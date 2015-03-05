import javax.swing.*;
import java.awt.*;

/**
 * The background panel
 */
public class BackgroundPane extends JPanel {
    private static final String BACKGROUND_PATH = "res/background.png";
    public static final int MAP_WIDTH = 1067;
    public static final int MAP_HEIGHT = 600;

    public BackgroundPane(){
        setPreferredSize(new Dimension(MAP_WIDTH, MAP_HEIGHT));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(new ImageIcon(BACKGROUND_PATH).getImage(), 0, 0, MAP_WIDTH, MAP_HEIGHT, this);
    }

    @Override
    public int getWidth(){
        return MAP_WIDTH;
    }

    @Override
    public int getHeight(){
        return MAP_HEIGHT;
    }
}
