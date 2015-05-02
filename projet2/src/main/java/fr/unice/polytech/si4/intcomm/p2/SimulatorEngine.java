package fr.unice.polytech.si4.intcomm.p2;

import Jama.Matrix;

import java.io.*;

/**
 * This class contains all the methods to perform the simulation
 * @author mmultari
 * @version 02/05/2015
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
    public float getViewPointAngle(ProjectileMobile proj, ObserverMobile obs, int period) {
        if (period < proj.getLocations()[0].length) {
            if (period < obs.getLocations()[0].length) {
                // o.y - p.y , o.x - p.x
                return (float) Math.atan2((obs.getY(period) - proj.getY(period)), (obs.getX(period) - proj.getX(period)));
            }
        }//If index error
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
            allViewPointAngles[i] = getViewPointAngle(proj, obs, i);
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
        Matrix cMatrix = new Matrix(angles.length, 4);
        for (int i = 0; i < angles.length; i++) {
            cMatrix.set(i, 0, Math.sin(angles[i]));
            cMatrix.set(i, 1, -Math.cos(angles[i]));
            cMatrix.set(i, 2, i * Math.sin(angles[i]));
            cMatrix.set(i, 3, -i * Math.cos(angles[i]));
        }
        return cMatrix;
    }


    /**
     * Calculates Y matrix from X matrix with the method of least squares
     *
     * @param angles    the angles viewpoint
     * @param locations the locations of the observer
     * @return Matrix
     */
    public Matrix generateYMatrix(float[] angles, float[][] locations) {
        Matrix yMatrix = new Matrix(angles.length, 1);
        for (int i = 0; i < angles.length; i++) {
            yMatrix.set(i, 0, locations[0][i] * Math.sin(angles[i]) - locations[1][i] * Math.cos(angles[i]));
        }
        return yMatrix;
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
                } finally {
                    in.close();
                }
            }
        } finally {
            out.close();
        }
    }


    /**
     * A simply method to display a matrix
     *
     * @param matrix the matrix to display
     */
    public void printMatrix(Matrix matrix) {
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++){
                System.out.printf("%9.4f", matrix.getArray()[i][j]);
                System.out.print("\t");
            }
            System.out.println();
        }
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
        Matrix ctA = aMatrix.transpose().times(aMatrix);//At*A
        Matrix ctB = aMatrix.transpose().times(bMatrix);
        Matrix result = ctA.inverse().times(ctB);
        return result;
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
        Matrix cBis = new Matrix(4, 1);
        Matrix pMatrix = new Matrix(4, 4);

        for (int i = 0; i < 4; i++)
            pMatrix.set(i, i, 100000);

        for (int i = 0; i < t; i++) {
            cBis.set(0, 0, cMatrix.get(i, 0));
            cBis.set(1, 0, cMatrix.get(i, 1));
            cBis.set(2, 0, cMatrix.get(i, 2));
            cBis.set(3, 0, cMatrix.get(i, 3));

            // Compute prediction error
            double predictionError = yMatrix.get(i,0) - ((cBis.transpose()).times(result)).get(0,0);

            // Compute saving correction
            double tmp = (cBis.transpose().times(pMatrix).times(cBis)).get(0,0);

            Matrix kMatrix = pMatrix.times(cBis).times((1 / (tmp + 1)));

            // Bring up to date the solution
            result.plusEquals(kMatrix.times(predictionError));

            // Brinf up to date pMatrix with matrix inversion lemma
            pMatrix.minusEquals(kMatrix.times(cBis.transpose()).times(pMatrix));
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
    public String predictionError(Matrix theoric,Matrix computed) {
        String s = "";
        for (int i = 0; i < computed.getRowDimension(); i++) {
            for (int j = 0; j < computed.getColumnDimension(); j++)
                s += (Math.abs(computed.get(i, j) - theoric.get(i, j))) + "\n\t";
        }
        return s;
    }

    //////////////////////////////////////// the simulation scenario ////////////////////////////////////////////

    /**
     * The main method of the simulation
     * @param proj
     * @param mobileFile
     * @param mobileBruitFile
     * @param o
     * @param o2
     * @param obsFile
     * @param obsBruitFile
     */
    public void simulate(ProjectileMobile proj, String mobileFile, String mobileBruitFile,
                         ObserverMobile o, ObserverMobile o2, String obsFile, String obsBruitFile){

        try {
            mergeFiles("simulation.txt", mobileFile, obsFile);
            mergeFiles("noisedSimulation.txt", mobileBruitFile, obsBruitFile);
        } catch (IOException e) {
            e.printStackTrace();
        }


        float[] angles = getAllViewPointAngles(proj, o, 30);


        Matrix cMatrix = generateCMatrix(angles);
        // Noiseless Matrix
        Matrix yMatrix = generateYMatrix(angles, o.getLocations());
        // MAtrix with noise
        Matrix noisedYMatrix = generateYMatrix(angles, o2.getNoisedLocations());

        // Theoric Matrix
        Matrix projMatrix = new Matrix( new double[][] { { proj.getX0() }, { proj.getY0() }, { proj.getVx() }, { proj.getVy() } } );

        System.out.println("/***************************************************************************/");
        System.out.println("/************** least squares operation with inverse transform *************/");
        System.out.println("/***************************************************************************/");
        System.out.println("/*** Noiseless ***/");
        Matrix result = inverseTrans(cMatrix, yMatrix);
        printMatrix(result);
        System.out.println("/*** With noise ***/");
        Matrix resultB = inverseTrans(cMatrix, noisedYMatrix);
        printMatrix(resultB);
        System.out.println(">> Prediction error : \n\t" + predictionError(resultB, projMatrix));

        System.out.println("/***************************************************************************/");
        System.out.println("/************** least squares operation with recursive update **************/");
        System.out.println("/***************************************************************************/");
        System.out.println("/*** Noiseless ***/");
        result = recursiveUpdate(angles.length, cMatrix, yMatrix);
        printMatrix(result);
        System.out.println("/*** With noise ***/");
        resultB = recursiveUpdate(angles.length, cMatrix, noisedYMatrix);
        printMatrix(resultB);
        System.out.println(">> Prediction error : \n\t" + predictionError(resultB, projMatrix));

    }

}
