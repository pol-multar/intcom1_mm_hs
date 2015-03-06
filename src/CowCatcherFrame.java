import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Observable;
import java.util.Observer;

/**
 * The main frame of the application
 */
public class CowCatcherFrame extends JFrame implements Observer {
    private static final String TITLE = "Cow Catcher !"; // The title
    // The paths of the images
    private static final String ICON_HEIGHT_PATH = "res/icon_height.png";
    private static final String ICON_SPEED_PATH = "res/icon_speed.png";
    private static final String ICON_COW_PATH = "res/icon_cow.png";
    private static final String ICON_WIND_LEFT_PATH = "res/icon_wind_left.png";
    private static final String ICON_WIND_RIGHT_PATH = "res/icon_wind_right.png";
    private static final String ICON_FUEL_PATH = "res/icon_fuel.png";
    private static final String ICON_CHRONO_PATH = "res/icon_chrono.png";
    // The buttons labels
    private static final String RESET_BUTTON = "Rejouer";
    private static final String AUTO_BUTTON = "Pilote automatique";
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance(); // The number format (for the height)
    private static final int ICON_SIZE = 60; // The size of the icons

    private JLabel heightLabel;
    private JLabel speedLabel;
    private JLabel cowsLabel;
    private JLabel windLabel;
    private JLabel chronoLabel;
    private JProgressBar fuelBar;
    private GameEngine engine;
    private int windSpeed;
    private LaserPane laserPane;

    public CowCatcherFrame(int width, int height, int mapWidth, int mapHeight, GameEngine e) {
        this.engine = e;
        // Init the number format
        NUMBER_FORMAT.setMaximumFractionDigits(1);
        // Observer/Observable
        engine.getUfo().addObserver(this);
        engine.addObserver(this);
        engine.getChrono().addObserver(this);

        // Set the frame parameters
        setTitle(TITLE);
        setSize(new Dimension(width, height));
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // The top panel
        JPanel infoPane = new JPanel(new GridLayout(2, 4));
        infoPane.setBorder(BorderFactory.createLineBorder(Color.black));
        add(infoPane);
        JButton resetButton = new JButton(RESET_BUTTON);
        resetButton.setFocusable(false);
        infoPane.add(resetButton);
        heightLabel = new JLabel("", new ImageIcon(new ImageIcon(ICON_HEIGHT_PATH).getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH)), JLabel.LEFT);
        infoPane.add(heightLabel);
        speedLabel = new JLabel("", new ImageIcon(new ImageIcon(ICON_SPEED_PATH).getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH)), JLabel.LEFT);
        infoPane.add(speedLabel);
        cowsLabel = new JLabel("", new ImageIcon(new ImageIcon(ICON_COW_PATH).getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH)), JLabel.LEFT);
        infoPane.add(cowsLabel);
        JButton autoButton = new JButton(AUTO_BUTTON);
        autoButton.setEnabled(false);
        autoButton.setFocusable(false);
        infoPane.add(autoButton);
        windLabel = new JLabel("", new ImageIcon(new ImageIcon(ICON_WIND_LEFT_PATH).getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH)), JLabel.LEFT);
        infoPane.add(windLabel);
        JPanel fuelPane = new JPanel();
        fuelPane.setLayout(new BoxLayout(fuelPane, BoxLayout.LINE_AXIS));
        fuelPane.add(new JLabel(new ImageIcon(new ImageIcon(ICON_FUEL_PATH).getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH))), BorderLayout.WEST);
        fuelBar = new JProgressBar(0, 100);
        fuelBar.setValue(100);
        fuelPane.add(fuelBar);
        infoPane.add(fuelPane);
        chronoLabel = new JLabel("", new ImageIcon(new ImageIcon(ICON_CHRONO_PATH).getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH)), JLabel.LEFT);
        infoPane.add(chronoLabel);
        // Fill the previous labels
        fillUfoLabels();
        fillEngineLabels();
        fillChronoLabel(0);

        // The main pane
        BackgroundPane backgroundPane = new BackgroundPane(mapWidth, mapHeight);
        add(backgroundPane, BorderLayout.SOUTH);
        backgroundPane.setLayout(new FlowLayout());
        backgroundPane.setLayout(null);

        // Fill the main pane
        backgroundPane.add(new UfoPane(engine.getUfo()));
        laserPane = new LaserPane(engine.getUfo());
        backgroundPane.add(laserPane);
        for (int i = 0; i < engine.getNbCows(); i++) {
            backgroundPane.add(new CowPane(engine.getCow(i)));
        }

        // The actions of the buttons
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                engine.reset();
            }
        });

        // The key listener (for the commands)
        addKeyListener(engine);

        setVisible(true);
    }

    /**
     * Fills the ufo related labels
     */
    private void fillUfoLabels() {
        heightLabel.setText(NUMBER_FORMAT.format(engine.getUfo().getHeight()) + " m");
        speedLabel.setText(engine.getUfo().getSpeed() + " m/s");
        fuelBar.setValue((int) (engine.getUfo().getFuelTank() * 100));
    }

    /**
     * Fills the chrono related label
     *
     * @param sec the number of seconds to display
     */
    public void fillChronoLabel(int sec) {
        String str = "";
        if (sec / 60 < 10) {
            str += "0";
        }
        str += sec / 60 + ":";
        if (sec % 60 < 10) {
            str += "0";
        }
        str += sec % 60;
        chronoLabel.setText(str);
    }

    /**
     * Fills the engine related label
     */
    public void fillEngineLabels() {
        if (windSpeed != engine.getWindSpeed()) {
            windSpeed = engine.getWindSpeed();
            if (engine.getWindSpeed() <= 0) {
                windLabel.setIcon(new ImageIcon(new ImageIcon(ICON_WIND_LEFT_PATH).getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH)));
                windLabel.setText(windSpeed * (-1) + " m/s");
            } else {
                windLabel.setIcon(new ImageIcon(new ImageIcon(ICON_WIND_RIGHT_PATH).getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH)));
                windLabel.setText(windSpeed + " m/s");
            }
        }
        String str = engine.getNbCowsCaptured() + "/" + engine.getNbCows() + " vache";
        if (engine.getNbCows() > 1) {
            str += "s";
        }
        cowsLabel.setText(str);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Ufo) {
            Ufo tmp = (Ufo) o;
            fillUfoLabels();
            if (tmp.isLaser() != laserPane.isFiring()) {
                laserPane.setFire(tmp.isLaser());
            }
            laserPane.setFire(((Ufo) o).isLaser());
        } else if (o instanceof Chrono) {
            fillChronoLabel(((Chrono) o).getSec());
        } else {
            fillEngineLabels();
        }
    }
}
