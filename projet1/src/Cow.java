import java.util.Observable;

/**
 * The cow
 */
public class Cow extends Observable {
    public boolean captured;
    private int m_x;
    private int m_y;

    public Cow(int x, int y) {
        reset(x, y);
    }

    /**
     * Reset the cow
     *
     * @param x the x location
     * @param y the y location
     */
    public void reset(int x, int y) {
        m_x = x;
        m_y = y;
        captured = false;
        setChanged();
        notifyObservers();
    }

    // Getters & Setters

    public boolean isCaptured() {
        return captured;
    }

    public int getX() {
        return m_x;
    }

    public int getY() {
        return m_y;
    }

    public void zapp() {
        captured = true;
        setChanged();
        notifyObservers();
    }
}
