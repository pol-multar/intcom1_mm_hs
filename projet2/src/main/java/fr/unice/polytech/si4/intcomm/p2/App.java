package fr.unice.polytech.si4.intcomm.p2;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {


        SimulatorEngine mySimu = new SimulatorEngine();

        //Initialisation of the projectile

        String projFile = "projectile.txt";
        String nprojFile = "nprojectile.txt";
        ProjectileMobile proj= new ProjectileMobile(2, 2, (float) 0.5, (float) 0.75);
        proj.computePath(SimulatorEngine.MAXPERIOD);
        proj.toFile(projFile, false);
        ProjectileMobile proj2 = proj;
        proj2.computeNoisedPath(SimulatorEngine.MAXPERIOD);
        proj2.toFile(nprojFile,true);

        //initialisation of the observers

        String obsFile = "observer.txt";
        String noisedObsFile="noisedObserver.txt";

        ObserverMobile obs = new ObserverMobile(10, 10, 10, (float) (Math.PI)*8, 1);
        ObserverMobile noisedObs = new ObserverMobile(10, 10, 10, (float) (Math.PI)*8, 1);
        obs.computePath(SimulatorEngine.MAXPERIOD);
        obs.toFile(obsFile, false);
        noisedObs.computeNoisedPath(SimulatorEngine.MAXPERIOD);
        noisedObs.toFile(noisedObsFile, true);

        mySimu.simulate(proj,projFile,nprojFile,obs,noisedObs,obsFile,noisedObsFile);

    }
}
