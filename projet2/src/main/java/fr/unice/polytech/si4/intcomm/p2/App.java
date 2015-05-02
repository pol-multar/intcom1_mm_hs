package fr.unice.polytech.si4.intcomm.p2;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        //TODO renommer
        int maxPeriod=30;//Maximum period

        //Initialisation of the projectile
        String projFile = "projectile.txt";
        //public ProjectileMobile(float x, float y, float vx, float vy)
        ProjectileMobile proj= new ProjectileMobile(2,2,0.50f,0.75f);
        proj.computePath(maxPeriod);
        proj.toFile(projFile,false);

        //initialisation of the observers
        /*
        String obsFile = "observer.txt";
        String noisedObsFile="noisedObserver.txt";

        ObserverMobile obs = new ObserverMobile(10,10,(float) (Math.PI)*8,1,10);
        ObserverMobile noisedObs = new ObserverMobile(15,15,(float) (Math.PI)*8,1,10);
        obs.computePath(maxPeriod);
        obs.toFile(obsFile, false);
        noisedObs.computeNoisedPath(maxPeriod);
        noisedObs.toFile(noisedObsFile, true);

        SimulatorEngine mySimu = new SimulatorEngine();
*/
        //mySimu.simulate(proj,projFile,obs,noisedObs,obsFile,noisedObsFile);

    }
}
