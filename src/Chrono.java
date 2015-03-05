import java.util.Observable;

/**
 * The chrono
 */
public class Chrono extends Observable implements Runnable {
    private int sec = -1;
    private boolean stop;

    public void stop(){
        stop = true;
    }
    public void start(){
        stop = false;
    }

    public void reset(){
        sec = -1;
    }

    public int getSec(){
        return sec;
    }

    @Override
    public void run() {
        while(!stop) {
            try {
                sec++;
                setChanged();
                notifyObservers();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
