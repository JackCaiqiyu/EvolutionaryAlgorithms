public class Bounds {
    private static float [] lowerBound = {-100, -100, -100, -100, -100, -100, 0, -32, -5, -5, -0.5f, (float)-Math.PI, -3, -100, -5, -5, -5,    -5, -5, -5,    -5, -5, -5,   -5 ,2 };
    private static float [] upperBound = {100, 100, 100, 100, 100, 100, 600, 32, 5, 5, 0.5f, (float) Math.PI, 1, 100, 5, 5, 5,     5, 5, 5,   5, 5, 5,  5 ,5};


    public static float getLowerBound(int nFun){
        return lowerBound[nFun-1];
    }

    public static float getUpperBound(int nFun){
        return upperBound[nFun-1];
    }

    public static float [][] han_boun(float [][] x, int n, float xmax, float xmin, int I_fno, int i){
        /*
                for j=1: n
            if( x(i,j) <xmin (j))
                x(i,j)=   xmin (j) +rand*(xmax(j)-xmin(j));
            else if ( x(i,j)>xmax (j))
                    x(i,j)=   xmin (j) +rand*(xmax(j)-xmin(j));
                end
            end
        end
         */
        for(int j=1; j<n; j++){
            if(x[i][j] < get) //TODO que es xmin y xman????
        }
    }
}
