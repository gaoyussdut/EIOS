package top.toptimus;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

import java.util.Arrays;

public class EigTest {
    public static void main(String[] args) {
//        double[][] array = {
//                {1, 1, 1},
//                {2, 3, 1},
//                {3, 0, 2}
//        };
//        //定义一个矩阵
//        Matrix target = new Matrix(array);
//
//        double[][] arrayB = {
//                {1, 2, 0},
//                {2, 2, 0},
//                {3, 2, 2}
//        };
//        Matrix plan = new Matrix(
//                arrayB
//        );
//
//        //由特征值组成的对角矩阵
////        target.eig().getD().print(4, 2);
//        //每一列对应的是一个特征向量
////        target.eig().getV().print(4, 2);
////        System.out.println(JSON.toJSONString(target.eig().getV().getArray()));
//
//        Matrix C = plan.minus(target);
//        System.out.println(JSON.toJSONString(C.eig().getV().getArray()));


//        double[][] matrix = {{2.5, 0.5, 2.2, 1.9, 3.1, 2.3, 2, 1, 1.5, 1.1},
//                {2.4, 0.7, 2.9, 2.2, 3.0, 2.7, 1.6, 1.1, 1.6, 0.9}};
        double[][] matrix = {
                {1 * 0.05, 1 * 3, 1 * 1, 1 * 7, 1 * 2, 1}
//                , {2.4, 0.7, 17}
        };
        xxx(matrix);
        System.out.println();
        double[][] matrix1 = {
                {0.998 * 0.05, 1 * 3, 1 * 1, 1 * 7, 1 * 2, 1}
//                , {2.41, 0.7, 15}
        };
        xxx(matrix1);
    }

    private static void xxx(double[][] matrix) {

        System.out.println("Column 1: " + Arrays.toString(matrix[0]));
//        System.out.println("Column 2: " + Arrays.toString(matrix[1]));

        //  this is already centralized
        double[] meanVector = getDMeanVector(matrix);
//        System.out.println("meanVector: " + Arrays.toString(meanVector));

        /*
         * V= eigenVectors
         * Vr = V(all the rows of first "2" columns - maybe an algorithmic approach
         * Projection = (Centralzied Data) * (Vr)
         */


        double[][] covarianceMatrix = getCovarianceMatrix(matrix, meanVector);
        for (int i = 0; i < covarianceMatrix.length; i++) {
            System.out.print("covarianceMatrix row " + i + ": " + Arrays.toString(covarianceMatrix[i]) + "\n");
        }

        Matrix m = new Matrix(covarianceMatrix);
        EigenvalueDecomposition eig = m.eig();
        double[] realEigValues = eig.getRealEigenvalues();
        m = eig.getV();
        double[][] diagonalEigMatrix = m.getArray();

        //new addition to compute projection
        m = eig.getD();
        double[][] eigenVectors = m.getArray();
        double[][] finalMatrix = new double[covarianceMatrix.length][eigenVectors[0].length];
        for (int a = 0; a < covarianceMatrix.length; a++) {
            for (int i = 0; i < covarianceMatrix.length; i++) {
                for (int j = 0; j < covarianceMatrix[i].length; j++) {
                    finalMatrix[a][i] += (matrix[a][j] * eigenVectors[j][i]);
                }
            }
        }


        System.out.print("realEigValues: " + Arrays.toString(realEigValues) + "\n");
//        for (int i = 0; i < diagonalEigMatrix.length; i++) {
//            System.out.print("diagonalEigMatrix: " + Arrays.toString(diagonalEigMatrix[i]) + "\n");
//        }
        for (int i = 0; i < diagonalEigMatrix.length; i++) {
            System.out.print("EigenVectors: " + Arrays.toString(eigenVectors[i]) + "\n");
        }
        for (int i = 0; i < diagonalEigMatrix.length; i++) {
            System.out.print("finalMatrix: " + Arrays.toString(finalMatrix[i]) + "\n");
        }


    }

    //  dimensional vector calculation
    private static double[] getDMeanVector(double[][] matrix) {
        double[] meanMatrix = new double[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
//            System.out.print("\ni:" + i);
            for (int j = 0; j < matrix[i].length; j++) {
//                System.out.print("j:" + j);
                meanMatrix[i] += (matrix[i][j] / matrix[i].length);
            }
        }

//        System.out.print("\n");
        return meanMatrix;
    }


    //Computing the covariance matrix
    private static double[][] getCovarianceMatrix(double[][] matrix, double[] meanVector) {
        //matrix multiplication	-> [A]ab * [B]cd	= [X]ad
        //matrix transposition	-> [A]ab 			= [AT]ba
        //Therefore, 			-> [A]ab * [AT]ba 	= [X]aa
        double[][] covarianceMatrix = new double[matrix.length][matrix.length];

        //next two steps may be combined for efficiency's sake
        //create "R"
        for (int i = 0; i < matrix.length; i++) {
//            System.out.print("\ni:" + i);
            for (int j = 0; j < matrix[i].length; j++) {
//                System.out.print("j:" + j);
                matrix[i][j] -= meanVector[i];///matrix[0].length;
            }
        }
        //(R transpose)*(R)
        double[][] matrixT = new double[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
//            System.out.print("\ni:" + i);
            for (int j = 0; j < matrix[i].length; j++) {
//                System.out.print("j:" + j);
                matrixT[j][i] = matrix[i][j];
            }
        }
        //for every value in the scatter matrix
        for (int a = 0; a < covarianceMatrix.length; a++) {
//            System.out.print("\na:" + a);
            //compute corresponding value of RT*R result
            for (int i = 0; i < matrix.length; i++) {
//                System.out.print("\ni:" + i);
                for (int j = 0; j < matrix[i].length; j++) {
//                    System.out.println("j:" + j);
//                    System.out.println("matrix  " + matrix[i][j]);
//                    System.out.println("matrixT " + matrixT[j][i]);
                    covarianceMatrix[a][i] += (matrix[a][j] * matrixT[j][i]);
                }
            }
        }

        for (int i = 0; i < matrix.length; i++) {
//            System.out.print("\ni:" + i);
            for (int j = 0; j < covarianceMatrix[i].length; j++) {
//                System.out.print("j:" + j);
                covarianceMatrix[i][j] /= matrix[0].length - 1;
            }
        }

//        System.out.print("\n");
        return covarianceMatrix;
    }
}
