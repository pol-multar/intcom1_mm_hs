import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

/**
 * The main frame of the application
 */
public class CowCatcherFrame extends JFrame implements Observer {
    private static final String TITLE = "Cow Catcher !";
    private static final String ICON_HEIGHT_PATH = "res/icon_height.png";
    private static final String ICON_SPEED_PATH = "res/icon_speed.png";
    private static final String ICON_COW_PATH = "res/icon_cow.png";
    private static final String ICON_WIND_LEFT_PATH = "res/icon_wind_left.png";
    private static final String ICON_WIND_RIGHT_PATH = "res/icon_wind_right.png";
    private static final String ICON_FUEL_PATH = "res/icon_fuel.png";
    private static final String ICON_CHRONO_PATH = "res/icon_chrono.png";
    private static final String RESET_BUTTON = "Remettre à 0";
    private static final String AUTO_BUTTON = "Pilote automatique";
    private static final int ICON_SIZE = 60;
    private static final int FRAME_WIDTH = 1067;
    private static final int FRAME_HEIGHT = 734;

    private JLabel heightLabel;
    private JLabel speedLabel;
    private JLabel cowsLabel;
    private JLabel windLabel;
    private JLabel chronoLabel;
    private JProgressBar fuelBar;
    private GameEngine engine;
    private int windSpeed;

    public CowCatcherFrame(GameEngine e) {
        this.engine = e;

        engine.getUfo().addObserver(this);
        engine.addObserver(this);
        engine.getChrono().addObserver(this);

        setTitle(TITLE);
        setSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JPanel infoPane = new JPanel(new GridLayout(2,4));
        infoPane.setBorder(BorderFactory.createLineBorder(Color.black));
        add(infoPane);
        JButton resetButton = new JButton(RESET_BUTTON);
        resetButton.setFocusable(false);
        infoPane.add(resetButton);
        heightLabel = new JLabel("", new ImageIcon(new ImageIcon(ICON_HEIGHT_PATH).getImage().getScaledInstance(ICON_SIZE,ICON_SIZE,Image.SCALE_SMOOTH)), JLabel.LEFT);
        infoPane.add(heightLabel);
        speedLabel = new JLabel("", new ImageIcon(new ImageIcon(ICON_SPEED_PATH).getImage().getScaledInstance(ICON_SIZE,ICON_SIZE,Image.SCALE_SMOOTH)), JLabel.LEFT);
        infoPane.add(speedLabel);
        cowsLabel = new JLabel("", new ImageIcon(new ImageIcon(ICON_COW_PATH).getImage().getScaledInstance(ICON_SIZE,ICON_SIZE,Image.SCALE_SMOOTH)), JLabel.LEFT);
        infoPane.add(cowsLabel);
        JButton autoButton = new JButton(AUTO_BUTTON);
        autoButton.setEnabled(false);
        autoButton.setFocusable(false);
        infoPane.add(autoButton);
        windLabel = new JLabel("", new ImageIcon(new ImageIcon(ICON_WIND_LEFT_PATH).getImage().getScaledInstance(ICON_SIZE,ICON_SIZE,Image.SCALE_SMOOTH)), JLabel.LEFT);
        infoPane.add(windLabel);
        JPanel fuelPane = new JPanel();
        fuelPane.setLayout(new BoxLayout(fuelPane, BoxLayout.LINE_AXIS));
        fuelPane.add(new JLabel(new ImageIcon(new ImageIcon(ICON_FUEL_PATH).getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH))), BorderLayout.WEST);
        fuelBar = new JProgressBar(0, 100);
        fuelBar.setValue(100);
        fuelPane.add(fuelBar);
        infoPane.add(fuelPane);
        chronoLabel = new JLabel("", new ImageIcon(new ImageIcon(ICON_CHRONO_PATH).getImage().getScaledInstance(ICON_SIZE,ICON_SIZE,Image.SCALE_SMOOTH)), JLabel.LEFT);
        infoPane.add(chronoLabel);

        fillUfoLabels();
        fillEngineLabels();
        fillChronoLabel(0);

        BackgroundPane backgroundPane = new BackgroundPane();
        add(backgroundPane, BorderLayout.SOUTH);
        backgroundPane.setLayout(new FlowLayout());
        backgroundPane.setLayout(null);

        UfoPane ufoPane = new UfoPane(engine.getUfo());
        backgroundPane.add(ufoPane);

        CowPane cowPane = new CowPane(engine.getCow(0));
        backgroundPane.add(cowPane);

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                engine.reset();
            }
        });

        addKeyListener(engine);

        setVisible(true);
    }

    private void fillUfoLabels() {
        heightLabel.setText(engine.getUfo().getHeight()+" px");
        speedLabel.setText(engine.getUfo().getSpeed()+" km/h");
        fuelBar.setValue((int)(engine.getUfo().getFuelTank()*100));
    }

    public void fillChronoLabel(int sec){
        String str = "";
        if(sec/60 < 10){
            str+="0";
        }
        str+=sec/60+":";
        if(sec%60 < 10){
            str+="0";
        }
        str+=sec%60;
        chronoLabel.setText(str);
    }

    public void fillEngineLabels(){
        if(windSpeed != engine.getWindSpeed()){
            windSpeed = engine.getWindSpeed();
            if(engine.getWindSpeed() < 0){
                windLabel.setIcon(new ImageIcon(new ImageIcon(ICON_WIND_LEFT_PATH).getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH)));
                windLabel.setText(windSpeed * (-1) + " km/h");
            }else{
                windLabel.setIcon(new ImageIcon(new ImageIcon(ICON_WIND_RIGHT_PATH).getImage().getScaledInstance(ICON_SIZE,ICON_SIZE,Image.SCALE_SMOOTH)));
                windLabel.setText(windSpeed + " km/h");
            }
        }
        cowsLabel.setText(engine.getCowsCaptured() + "/" + engine.getNbCows()+ " cow(s)");
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof Ufo){
            fillUfoLabels();
        }else if(o instanceof Chrono){
            fillChronoLabel(((Chrono) o).getSec());
        }else{
            fillEngineLabels();
        }
    }
}
