import javax.swing.*;
import java.awt.*;

/**
 * Created by Hugo on 03/03/2015.
 */
public class CowCatcherFrame extends JFrame {
    private static final String TITLE = "Cow Catcher !";

    public CowCatcherFrame(){
        // The properties of the frame
        setTitle(TITLE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1920, 1080));
        add(layeredPane);
        JPanel infoPane = new JPanel(new BorderLayout());
        infoPane.setSize(new Dimension(1920, 1080));
        infoPane.setBackground(Color.RED);
        layeredPane.add(infoPane, JLayeredPane.PALETTE_LAYER);
        JPanel eastPane = new JPanel();
        eastPane.setBackground(Color.BLUE);
        infoPane.add(eastPane, BorderLayout.EAST);

        pack();
        setVisible(true);
    }
}
