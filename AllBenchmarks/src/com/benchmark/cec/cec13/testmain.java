package com.benchmark.cec.cec13;


import java.io.File;
import java.util.Scanner;

public class testmain {

	public void test() throws Exception{
		int i,j,k,n,m,func_num;
		double[] x;
		double[] f;
		File fpt = new File("Benchmark/src/CEC13-Benchmark/src/cec15.input_data/shift_data.txt");
		
		m=2;
		n=10;
		
		testfunc tf = new testfunc();
		
		Scanner input = new Scanner(fpt);
		x = new double[n*m];
		for(i=0;i<n;i++){
			x[i]=input.nextDouble();
		}
		for(i=0;i<n;i++){
			System.out.println(x[i]);
		}
		input.close();
		
		for(i=1;i<m;i++){
			for(j=0;j<n;j++){
				x[i*n+j]=0.0;
				System.out.println(x[i*n+j]);
			}
		}
		
		f= new double[m];
		for(i=0;i<28;i++){
			func_num=i+1;
			for(k=0;k<1;k++){
				tf.test_func(x,f,n,m,func_num);
				for(j=0;j<m;j++){
					System.out.println("f"+func_num+"(x["+(j+1)+"])="+f[j]);
				}
			}
		}
		
		
	}
}


		