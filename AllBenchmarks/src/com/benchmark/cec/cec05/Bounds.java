package com.benchmark.cec.cec05;

import com.benchmark.Rand;

public class Bounds {
    private static float [] lowerBound = {-100, -100, -100, -100, -100, -100, 0, -32, -5, -5, -0.5f, (float)-Math.PI, -3, -100, -5, -5, -5,    -5, -5, -5,    -5, -5, -5,   -5 ,2 };
    private static float [] upperBound = {100, 100, 100, 100, 100, 100, 600, 32, 5, 5, 0.5f, (float) Math.PI, 1, 100, 5, 5, 5,     5, 5, 5,   5, 5, 5,  5 ,5};
    public static double Ter_Err = 10e-8;

    public static float getLowerBound(int nFun){
        return lowerBound[nFun-1];
    }

    public static float getUpperBound(int nFun){
        return upperBound[nFun-1];
    }


    /*
        Esta funcion reajusta las cotas de los miembros que se pasen
        x = miembro de la poblacion
        n = dimension

     */

}
