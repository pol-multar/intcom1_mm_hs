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
     * The number of the period simulation
     */
    public static final int MAXPERIOD=30;

    /* Noise computation constant */
    public static final float NOISE = (float) 0.0001;

    private float [] anglesFound;

    public SimulatorEngine(){
        anglesFound = new float[MAXPERIOD];
    }

    public float[] getAnglesFound() {
        return anglesFound;
    }

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
                return (float) Math.atan2((obs.getYat(period) - proj.getYat(period)), (obs.getXat(period) - proj.getXat(period)));
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
        float[] allViewPointAngles = new float[maxPeriod];
        for (int i = 0; i < maxPeriod; i++) {
            allViewPointAngles[i] = getViewPointAngle(proj, obs, i);
        }
        return allViewPointAngles;
    }

    /**
     * Methode de test des angles
     * @param proj
     * @param obs
     * @param maxPeriod
     */
    public void testAngles(ProjectileMobile proj, ObserverMobile obs, int maxPeriod) {
        float[] angles=getAllViewPointAngles(proj,obs,maxPeriod);
        //float theta,xProjOrig,yProjOrig,xObs,yObs,xVProj,yVProj;
        float theta,xObs,yObs,xProjOrig,yProjOrig,xVProj,yVProj;
        double result;
        for (int t = 0; t < maxPeriod ; t++) {
            theta=angles[t]+NOISE;
            System.out.println("Theta bruite = "+theta +" mesure avec un bruit = "+NOISE+"\n");
            xProjOrig=proj.getX0();
            yProjOrig=proj.getY0();
            xObs=obs.getXat(t);
            yObs=obs.getYat(t);
            xVProj=proj.getVx();
            yVProj=proj.getVy();
            result=yObs * Math.cos(theta) - xObs * Math.sin(theta) - yProjOrig * Math.cos(theta) + xProjOrig * Math.sin(theta) - yVProj * Math.cos(theta) * t
                    + xVProj * Math.sin(theta) * t;//This result is 0 when measures are perfect
            System.out.println("Erreur de l angle a l etape "+t+" = "+ result);
            System.out.println("===================================");
            this.anglesFound[t]=(float)result;
        }
    }

    /**
     * Calculates C matrix with the method of least squares
     * C[angles Number, 4]
     * [sin(angle),-cos(angle),t*sin(angle),-t*cos(angle)]
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
     * @return Matrix contain for each angles
     * the difference between xLocationAtT*sin(angles[T]) - yLocationAtT*cos(angles[T])
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
        Matrix ctB = aMatrix.transpose().times(bMatrix);//Bt*B
        Matrix result = ctA.inverse().times(ctB);//ctA-1*ctB
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

        Matrix result = new Matrix(4, 1);//4 rows 1 col
        Matrix cBis = new Matrix(4, 1);//4 rows 1 col
        Matrix pMatrix = new Matrix(4, 4);//4 rows 4 col

        for (int i = 0; i < 4; i++)
            pMatrix.set(i, i, 100000);  //10000 0 0 0
                                        //0 1000 0 0
                                        //0 0 1000 0
                                        //0 0 0 10000

        for (int i = 0; i < t; i++) {

            cBis.set(0, 0, cMatrix.get(i, 0));
            cBis.set(1, 0, cMatrix.get(i, 1));
            cBis.set(2, 0, cMatrix.get(i, 2));
            cBis.set(3, 0, cMatrix.get(i, 3));

            //Step 1 : Compute prediction error eps(T)=y(t)-cTrans.x(t)
            double epsilon = yMatrix.get(i,0) - ((cBis.transpose()).times(result)).get(0,0);

            //Step 2 : Compute saving correction k(t)=P(t)*c(T).(I+cTrans(t).P(T).c(T))^-1
            double tmp = (cBis.transpose().times(pMatrix).times(cBis)).get(0,0);
            Matrix kMatrix = pMatrix.times(cBis).times((1 / (tmp + 1)));

            //Step 3 : Bring up to date the solution x(t+1)=x(t)+k(t).eps(T)
            result.plusEquals(kMatrix.times(epsilon));

            //Step 4 : Bring up to date pMatrix with matrix inversion lemma P(T+1)=P(T)-k(T).cTrans(T).P(t)
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

        /*
        try {
            mergeFiles("simulation.txt", mobileFile, obsFile);
            mergeFiles("noisedSimulation.txt", mobileBruitFile, obsBruitFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
*/

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
