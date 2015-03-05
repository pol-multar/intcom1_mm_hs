import java.util.Observable;

/**
 * The cow
 */
public class Cow extends Observable{
    public boolean zapped;
    private int m_x;
    private int m_y;

    public Cow(int x, int y) {
        m_x = x;
        m_y = y;
    }

    public boolean isZapped(){
        return zapped;
    }

    public void zapp(){
        zapped = true;
        setChanged();
        notifyObservers();
    }

    public int getX() {
        return m_x;
    }

    public int getY() {
        return m_y;
    }

    public void reset(int x, int y) {
        m_x = x;
        m_y = y;
        zapped = false;
        setChanged();
        notifyObservers();
    }
}
