package fr.unice.polytech.si4.intcomm.p2;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugo on 02/05/2015.
 */
public class GraphPanel extends JPanel{
    private List<Point> projectile;
    private List<Point> observer;

    public GraphPanel(Point projectile, Point observer){
        this.projectile = new ArrayList<Point>();
        this.observer = new ArrayList<Point>();
        this.projectile.add(projectile);
        this.observer.add(observer);
        setBackground(null);
        setOpaque(false);
    }

    public void setProjectileLocation(int x, int y){
        projectile.add(new Point(x, y));
        repaint();
    }

    public void setObserverLocation(int x, int y){
        observer.add(new Point(x, y));
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.RED);
        for (int i=0; i<projectile.size()-1; i++){
            g.fillOval((int) (getWidth() / 2. + projectile.get(i).getX() - 5), (int) (getHeight() / 2. - projectile.get(i).getY() - 5), 10, 10);
            g.drawOval((int) (getWidth() / 2. + projectile.get(i).getX() - 5), (int) (getHeight() / 2. - projectile.get(i).getY() - 5), 10, 10);
        }
        g.fillOval((int) (getWidth() / 2. + projectile.get(projectile.size()-1).getX() - 10), (int) (getHeight() / 2. - projectile.get(projectile.size()-1).getY() - 10), 20, 20);
        g.drawOval((int) (getWidth() / 2. + projectile.get(projectile.size()-1).getX() - 10), (int) (getHeight() / 2. - projectile.get(projectile.size()-1).getY() - 10), 20, 20);



        g.setColor(Color.BLUE);
        for (int i=0; i<observer.size()-1; i++){
            g.fillOval((int) (getWidth() / 2. + observer.get(i).getX() - 5), (int) (getHeight() / 2. - observer.get(i).getY() - 5), 10, 10);
            g.drawOval((int) (getWidth() / 2. + observer.get(i).getX() - 5), (int) (getHeight() / 2. - observer.get(i).getY() - 5), 10, 10);
        }
        g.fillOval((int) (getWidth() / 2. + observer.get(observer.size()-1).getX() - 10), (int) (getHeight() / 2. - observer.get(observer.size()-1).getY() - 10), 20, 20);
        g.drawOval((int) (getWidth() / 2. + observer.get(observer.size()-1).getX() - 10), (int) (getHeight() / 2. - observer.get(observer.size()-1).getY() - 10), 20, 20);
    }
}
