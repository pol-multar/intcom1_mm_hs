package fr.unice.polytech.si4.intcomm.p2;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Hugo on 02/05/2015.
 */
public class AppView extends JFrame implements Runnable{
    private static final String TITLE = "Aperçu des résultats";
    private static final int scale = 10;
    private ProjectileMobile projectile;
    private ObserverMobile observer;
    private GraphPanel graphPane;
    private JLabel obsLocationLabel;
    private JLabel projLocationLabel;

    public AppView(ProjectileMobile projectile, ObserverMobile observer){
        setTitle(TITLE);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        this.projectile = projectile;
        this.observer = observer;

        JLayeredPane layeredPane = new JLayeredPane();
        graphPane = new GraphPanel(new Point((int)(scale*projectile.getX0()), (int)(scale*projectile.getY0())), new Point((int)(scale*observer.getX(0)), (int)(scale*observer.getY(0))));
        JPanel background = new JPanel(new GridLayout(2,2));
        JPanel pane1 = new JPanel();
        pane1.setBackground(Color.WHITE);
        pane1.setBorder(BorderFactory.createLineBorder(Color.black));
        JPanel pane2 = new JPanel();
        pane2.setBackground(Color.WHITE);
        pane2.setBorder(BorderFactory.createLineBorder(Color.black));
        JPanel pane3 = new JPanel();
        pane3.setBackground(Color.WHITE);
        pane3.setBorder(BorderFactory.createLineBorder(Color.black));
        JPanel pane4 = new JPanel();
        pane4.setBackground(Color.WHITE);
        pane4.setBorder(BorderFactory.createLineBorder(Color.black));
        background.add(pane1);
        background.add(pane2);
        background.add(pane3);
        background.add(pane4);

        layeredPane.add(background, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(graphPane, JLayeredPane.MODAL_LAYER);

        add(layeredPane);

        JPanel eastPane = new JPanel(new BorderLayout());
        eastPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(eastPane, BorderLayout.EAST);

        JPanel locationPane = new JPanel(new GridLayout(2,2));
        JLabel obsLabel = new JLabel("Observateur");
        obsLabel.setForeground(Color.BLUE);
        obsLocationLabel = new JLabel(observer.getX(0)+" ; "+observer.getY(0));
        JLabel projLabel = new JLabel("Projectile");
        projLabel.setForeground(Color.RED);
        projLocationLabel = new JLabel(projectile.getX0()+" ; "+projectile.getY0());
        locationPane.add(obsLabel);
        locationPane.add(obsLocationLabel);
        locationPane.add(projLabel);
        locationPane.add(projLocationLabel);
        locationPane.setBorder(BorderFactory.createTitledBorder("Positions"));
        eastPane.add(locationPane, BorderLayout.NORTH);

        setVisible(true);

        Dimension graphDim = new Dimension(4*getWidth()/5, getHeight()-42);
        layeredPane.setPreferredSize(graphDim);
        background.setSize(graphDim);
        graphPane.setSize(graphDim);


    }

    @Override
    public void run() {
        for(int i=0; i< Math.min(projectile.getLocations()[0].length, observer.getLocations()[0].length); i++){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            obsLocationLabel.setText(observer.getX(i)+" ; "+observer.getY(i));
            projLocationLabel.setText(projectile.getX(i)+" ; "+projectile.getY(i));
            graphPane.setObserverLocation((int)(scale*observer.getX(i)), (int)(scale*observer.getY(i)));
            graphPane.setProjectileLocation((int)(scale*projectile.getX(i)), (int)(scale*projectile.getY(i)));
        }
    }
}
