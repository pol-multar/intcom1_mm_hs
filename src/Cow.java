import java.util.Observable;

/**
 * The cow
 */
public class Cow extends Observable{
    public boolean captured;
    private int m_x;

    public Cow(int x) {
        m_x = x;
    }

    public boolean isCaptured(){
        return captured;
    }

    public void zapp(){
        captured = true;
        setChanged();
        notifyObservers();
    }

    public int getX() {
        return m_x;
    }

    public int getY() {
        return BackgroundPane.MAP_HEIGHT - GameEngine.FLOOR_HEIGHT;
    }

    public void reset(int x) {
        m_x = x;
        captured = false;
        setChanged();
        notifyObservers();
    }
}
