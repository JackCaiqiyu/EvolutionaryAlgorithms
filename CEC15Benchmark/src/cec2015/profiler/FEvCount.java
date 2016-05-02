/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cec2015.profiler;

//~--- non-JDK imports --------------------------------------------------------

import cec2015.math.Constants;

/**
 * 
 * @author QIN
 */
public class FEvCount {
	private int[][] counts = null;
	private double[][] currentBest = null;
	private final double[][][] currentBestX;
	private final int maxDimension;
	private final int problemCount;
	private ResultRecorder recorder;
	private final int currentRun;

	public FEvCount(int problemCount, int maxDimension, int run) {
		this(problemCount, maxDimension, run, null);
	}

	public FEvCount(int problemCount, int maxDimension, int run, ResultRecorder rr) {
		recorder = rr;
		this.problemCount = problemCount;
		this.maxDimension = maxDimension;
		currentRun = run;
		counts = new int[problemCount + 1][maxDimension + 1];
		currentBest = new double[problemCount + 1][maxDimension + 1];
		currentBestX = new double[problemCount + 1][maxDimension + 1][];

		for (int i = 1; i < counts.length; i++) {
			for (int j = 1; j < counts[i].length; j++) {
				counts[i][j] = 0;
				currentBest[i][j] = Constants.INF;
				currentBestX[i][j] = null;
			}
		}
	}

	public int getCurrentRun() {
		return currentRun;
	}

	public void setRocorer(ResultRecorder rr) {
		recorder = rr;
	}

	public int getCount(int func_num, int nx) {
		assert ((func_num >= 1) || (func_num <= problemCount));
		assert ((nx <= maxDimension) || (nx >= 1));

		return counts[func_num][nx];
	}

	public double[] getCurrentBestX(int func_num, int nx) {
		assert ((func_num >= 1) || (func_num <= problemCount));
		assert ((nx <= maxDimension) || (nx >= 1));

		return currentBestX[func_num][nx];
	}

	public double getCurrentBest(int func_num, int nx) {
		assert ((func_num >= 1) || (func_num <= problemCount));
		assert ((nx <= maxDimension) || (nx >= 1));

		return currentBest[func_num][nx];
	}

	public void eval(int func_num, int nx, double[] x, double[] fx) {
		int evalcount = x.length / nx;

		assert (evalcount == fx.length);
		assert ((func_num < 1) || (func_num > problemCount));

		for (int i = 0; i < fx.length; i++) {
			if (fx[i] < currentBest[func_num][nx]) {
				currentBest[func_num][nx] = fx[i];

				double[] tx = new double[nx];

				System.arraycopy(x, i * nx, tx, 0, nx);
				currentBestX[func_num][nx] = tx;
			}

			if (recorder != null) {
				double[] tx = new double[nx];

				System.arraycopy(x, i * nx, tx, 0, nx);
				recorder.record(func_num, nx, currentRun, getCount(func_num, nx) + i + 1, currentBestX[func_num][nx],
						currentBest[func_num][nx]);
			}
		}

		counts[func_num][nx] += evalcount;
	}
}

// ~ Formatted by Jindent --- http://www.jindent.com
