package fr.unice.polytech.si4.intcomm.p2;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        int maxPeriod=30;//Maximum period

        //Initialisation of the projectile

        String projFile = "projectile.txt";
        String nprojFile = "nprojectile.txt";
        ProjectileMobile proj= new ProjectileMobile(2, 2, (float) 0.5, (float) 0.75);
        proj.computePath(maxPeriod);
        proj.toFile(projFile, false);
        ProjectileMobile proj2 = proj;
        proj2.computeNoisedPath(maxPeriod);
        proj2.toFile(nprojFile,true);

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

        mySimu.simulate(proj,projFile,nprojFile,obs,noisedObs,obsFile,noisedObsFile);

        new AppView(proj, obs).run();
    }
}
