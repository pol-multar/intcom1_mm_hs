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
        ProjectileMobile proj= new ProjectileMobile(2, 2, (float) 0.5, (float) 0.75);
        proj.computePath(maxPeriod);
        proj.toFile(projFile,false);

        /*

        String mobile = "Mobile.data";
        String mobileBruit = "MobileAvecBruit.data";
        ProjectileMobile m1 = new ProjectileMobile(2, 2, (float) 0.5, (float) 0.75);
        ProjectileMobile m2 = new ProjectileMobile(2, 2, (float) 0.5, (float) 0.75);
        m1.computePath(maxPeriod);
        m1.toFile(mobile, false);
        m2.computeNoisedPath(maxPeriod);
        m2.toFile(mobileBruit, true);
*/
        //initialisation of the observers

        String obsFile = "observer.txt";
        String noisedObsFile="noisedObserver.txt";

        ObserverMobile obs = new ObserverMobile(10, 10, 10, (float) (Math.PI)*8, 1);
        ObserverMobile noisedObs = new ObserverMobile(10, 10, 10, (float) (Math.PI)*8, 1);
        obs.computePath(maxPeriod);
        obs.toFile(obsFile, false);
        noisedObs.computeNoisedPath(maxPeriod);
        noisedObs.toFile(noisedObsFile, true);

        SimulatorEngine mySimu = new SimulatorEngine();

        mySimu.simulate(proj,projFile,obs,noisedObs,obsFile,noisedObsFile);

    }
}
