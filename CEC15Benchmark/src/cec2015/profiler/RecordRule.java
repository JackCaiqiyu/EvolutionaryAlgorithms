/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cec2015.profiler;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 * @author QIN
 */
public class RecordRule {
	private final ArrayList<int[]> recordPoints = new ArrayList<int[]>();
	private final ArrayList<Integer> dims = new ArrayList<Integer>();

	public void addRule(int dim, int[] points) {
		for (int d : dims) {
			if (d == dim) {
				recordPoints.set(dims.indexOf(d), points);

				return;
			}
		}

		dims.add(dim);
		recordPoints.add(points);
	}

	private int[] getRecordRule(int dim) {
		for (int d : dims) {
			if (d == dim) {
				return recordPoints.get(dims.indexOf(d));
			}
		}

		return null;
	}

	public boolean isRecord(int dim, int count) {
		int[] rr = getRecordRule(dim);

		if (rr == null) {
			return false;
		}

		for (int p : rr) {
			if (count == p) {
				return true;
			}
		}

		return false;
	}
}

// ~ Formatted by Jindent --- http://www.jindent.com
