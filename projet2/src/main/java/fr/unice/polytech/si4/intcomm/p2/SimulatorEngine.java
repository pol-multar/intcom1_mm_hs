package fr.unice.polytech.si4.intcomm.p2;


import Jama.Matrix;

import java.io.*;

/**
 * This class contains all the methods for the simulation
 *
 * @author mmultari
 * @version 01/05/2015
 */
public class SimulatorEngine {


    /**
     * Calculates viewpoint angles between a projectile and an observer at one period
     *
     * @param proj   the projectile
     * @param obs    the observer
     * @param period the period of time
     * @return the viewpoint angle
     */
    private float getViewPointAngle(ProjectileMobile proj, ObserverMobile obs, int period) {
        if (period < proj.getLocations()[0].length) {
            if (period < obs.getLocations()[0].length) {
                return (float) Math.atan2((obs.getY(period) - proj.getY(period)), (obs.getX(period) - proj.getX(period)));
            }
        }
        return -100;
    }

    /**
     * Calculates viewpoint angles between a projectile and an observer for all periods
     *
     * @param proj      the projectile
     * @param obs       the observer
     * @param maxPeriod the maximum period
     * @return
     */
    public float[] getAllViewPointAngles(ProjectileMobile proj, ObserverMobile obs, int maxPeriod) {
        float[] allViewPointAngles = new float[maxPeriod + 1];
        for (int i = 0; i <= maxPeriod; i++) {
            allViewPointAngles[i] = getViewPointAngle(proj, obs, maxPeriod);
        }
        return allViewPointAngles;
    }

    /**
     * Calculates C matrix with the method of least squares
     *
     * @param angles
     * @return Matrix
     */

    public Matrix generateCMatrix(float[] angles) {
        Matrix c = new Matrix(angles.length, 4);
        for (int i = 0; i < angles.length; i++) {
            c.set(i, 0, (float) Math.sin(angles[i]));
            c.set(i, 1, (float) ((-1) * Math.cos(angles[i])));
            c.set(i, 2, (float) (i * Math.sin(angles[i])));
            c.set(i, 3, (float) (-i * Math.cos(angles[i])));
        }
        return c;
    }

    /**
     * Calculates Y matrix from X matrix with the method of least squares
     *
     * @param angles    the angles viewpoint
     * @param locations the locations of the observer
     * @return Matrix
     */

    public Matrix generateYMatrix(float[] angles, float[][] locations) {
        Matrix y = new Matrix(angles.length, 1);
        for (int i = 0; i < angles.length; i++) {
            y.set(i, 0, (float) (locations[0][i] * Math.sin(angles[i]) - locations[1][i] * Math.cos(angles[i])));
        }
        return y;
    }

    /**
     * A method to merge files into one file
     *
     * @param dest the file with all merged
     * @param src  a list of source file
     * @throws IOException if Stream errors
     */
    public void mergeFiles(String dest, String... src) throws IOException {
        OutputStream out = new FileOutputStream(dest);
        try {
            final int MAX_SIZE = 8192;
            byte[] buf = new byte[MAX_SIZE];
            int len;
            for (String filename : src) {
                InputStream in = new FileInputStream(filename);
                try {
                    while ((len = in.read(buf)) >= 0) {
                        out.write(buf, 0, len);
                    }
                } finally { //anyways
                    in.close();
                }
            }
        } finally { //anyways
            out.close();
        }
    }

    /**
     * A simply method to transform a matrix into string
     *
     * @param matrix the matrix to transform
     * @return
     */
    public String toStringMatrix(Matrix matrix) {
        String s = "";
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                s += String.format("%9.4f\n", matrix.get(i, j));
            }
            s += "\n";
        }
        return s;
    }

    /**
     * A simply method to display a matrix
     *
     * @param matrix the matrix to display
     */
    public void printMatrix(Matrix matrix) {
        System.out.println(toStringMatrix(matrix));
    }

    ////////////////////////////////////////the different transformations /////////////////////////////////

    /**
     * Solving least squares equation with inverse transformation
     *
     * @param aMatrix
     * @param bMatrix
     * @return
     */
    public Matrix inverseTrans(Matrix aMatrix, Matrix bMatrix) {
        Matrix aTxa = aMatrix.transpose().times(aMatrix);//at*a
        Matrix bTxb = aMatrix.transpose().times(bMatrix);//bt*b
        return aTxa.inverse().times(bTxb);
    }

    /**
     * The method to estimates projectile location with least squares method
     *
     * @param t
     * @param cMatrix
     * @param yMatrix
     * @return
     */
    public Matrix recursiveUpdate(Integer t, Matrix cMatrix, Matrix yMatrix) {
        Matrix result = new Matrix(4, 1);
        Matrix Cbis = new Matrix(4, 1);

        Matrix pMatrix = new Matrix(4, 4);
        for (int i = 0; i < 4; i++)
            pMatrix.set(i, i, 100000);

        for (int i = 0; i < t; i++) {
            Cbis.set(0, 0, cMatrix.get(i, 0));
            Cbis.set(1, 0, cMatrix.get(i, 1));
            Cbis.set(2, 0, cMatrix.get(i, 2));
            Cbis.set(3, 0, cMatrix.get(i, 3));

            // Compute prediction error
            double predictionError = yMatrix.get(i, 0) - ((Cbis.transpose()).times(result)).get(0, 0);

            // Compute saving correction
            double tmp = (Cbis.transpose().times(pMatrix).times(Cbis)).get(0, 0);

            Matrix kMatrix = pMatrix.times(Cbis).times((1 / (tmp + 1)));

            // Bring up to date the solution
            result.plusEquals(kMatrix.times(predictionError));

            // Brinf up to date P matrix with matrix inversion lemma
            pMatrix.minusEquals(kMatrix.times(Cbis.transpose()).times(pMatrix));
        }
        return result;
    }


    /**
     * A method to find the prediction errors of the observer
     *
     * @param theoric
     * @param computed
     * @return the prediction error
     */
    public String predictionError(Matrix theoric, Matrix computed) {
        String result = "";
        for (int i = 0; i < computed.getRowDimension(); i++) {
            for (int j = 0; j < computed.getColumnDimension(); j++)
                result += (Math.abs(computed.get(i, j) - theoric.get(i, j))) + "\n";
        }
        return result;
    }


    //////////////////////////////////////// the simulation scenario ////////////////////////////////////////////

    /**
     * The main method of the simulation
     * -> a main method ?
     *
     * @param proj
     * @param projFile
     * @param obs
     * @param obs2
     * @param obsFile
     * @param noisedObsFile
     */
    public void simulate(ProjectileMobile proj, String projFile, ObserverMobile obs, ObserverMobile obs2, String obsFile, String noisedObsFile) {

        try {
            mergeFiles("Simulation.data", projFile, obsFile);    // On joint les resultats des 2 fichiers
            mergeFiles("SimulationAvecBruit.data", projFile, noisedObsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }


        float[] angles = getAllViewPointAngles(proj, obs, 30);


        Matrix cMatrix = generateCMatrix(angles);
        // Noiseless matrix
        Matrix yMatrix = generateYMatrix(angles, obs.getLocations());
        // Noised matrix
        Matrix noisedYMatrix = generateYMatrix(angles, obs2.getNoisedLocations());

        // Theoric matrix to find
        Matrix projMatrix = new Matrix(new double[][]{{proj.getX0()}, {proj.getY0()}, {proj.getVx()}, {proj.getVy()}});

        System.out.println("/***************************************************************************/");
        System.out.println("/************** least squares operation with inverse transform *************/");
        System.out.println("/***************************************************************************/");
        System.out.println("/*** Noiseless ***/");
        Matrix result = inverseTrans(cMatrix, yMatrix);
        printMatrix(result);
        System.out.println("/*** With noise ***/");
        Matrix resultB = inverseTrans(cMatrix, noisedYMatrix);
        printMatrix(resultB);
        System.out.println(">> Prediction error : \t" + predictionError(resultB, projMatrix));

        System.out.println("/***************************************************************************/");
        System.out.println("/************** least squares operation with recursive update **************/");
        System.out.println("/***************************************************************************/");
        System.out.println("/*** Noiseless ***/");
        result = recursiveUpdate(angles.length, cMatrix, yMatrix);
        printMatrix(result);
        System.out.println("/*** With noise ***/");
        resultB = recursiveUpdate(angles.length, cMatrix, noisedYMatrix);
        printMatrix(resultB);
        System.out.println(">> Prediction error : \t" + predictionError(resultB, projMatrix));

    }
}
