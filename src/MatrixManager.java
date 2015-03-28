import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Reads *.txt
 */
public class MatrixManager {
    public static double[][] readMatrix(int c, int l){
        double[][] matrix = new double[l][c];
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader("./res/Kre.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line;
        String[] split = null;
        try {
            if ((line = in.readLine()) != null) {
                line = line.replaceAll("\\s+", "");
                split = line.split(",");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i=0; i<l; i++){
            for(int j=0; j<c; j++){
                matrix[i][j] = Double.parseDouble(split[i+j]);
            }
        }

        return matrix;
    }

    public static double[][] subMatrix(double[][] m1, double[][] m2){
        int aRows = m1.length;
        int aColumns = m1[0].length;
        int bRows = m2.length;
        int bColumns = m2[0].length;

        if (aColumns != bColumns && aRows != bRows) {
            return null;
        }

        double[][] c = new double[aRows][aColumns];

        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < aColumns; j++) {
                c[i][j] = m1[i][j] - m2[i][j];
            }
        }

        return c;
    }

    public static double[][] multMatrix(double[][] m1, double[][] m2){
        int aRows = m1.length;
        int aColumns = m1[0].length;
        int bRows = m2.length;
        int bColumns = m2[0].length;

        if (aColumns != bRows) {
            return null;
        }

        double[][] c = new double[aRows][bColumns];
        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < bColumns; j++) {
                c[i][j] = 0.00000;
            }
        }

        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < bColumns; j++) {
                for (int k = 0; k < aColumns; k++) {
                    c[i][j] += m1[i][k] * m2[k][j];
                }
            }
        }

        return c;
    }

    public static String matrixToString(double[][] mat){
        String res = "";
        for(int i=0; i<mat.length; i++){
            for(int j=0; j<mat[0].length; j++){
                res+=mat[i][j];
                if(j!=mat[0].length-1){
                    res+=", ";
                }
            }
            res+="\n";
        }
        return res;
    }
}
