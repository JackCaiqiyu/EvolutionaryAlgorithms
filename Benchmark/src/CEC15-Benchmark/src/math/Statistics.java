/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package math;

import java.util.Arrays;

/**
 *
 * @author QIN
 */
public class Statistics {

	
	

	
	//this method calculates the average value of all of the elements in the array
	public static double calculateMean(double[] array) {
		
		//add up the values in the array
		double sum = 0;
		for (int i=0; i < array.length; i++) {
			sum += array[i];
		}
		
		//divide the total by the number of elements in the array to find the mean
		double mean = sum / array.length;
		return mean;
	}
	
	//this method calculates the maximum value in the array
	public static double calculateMax(double[] array) {
		
		//assume the max is the first value, but then step through the array
		//and each time you encounter a bigger value, update the max value assumed
		double maxSeenSoFar = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] > maxSeenSoFar) {
				maxSeenSoFar = array[i];
			}
		}
		
		//return the max value seen
		return maxSeenSoFar;
	}
	
	//this method calculates the minimum value in the array
	public static double calculateMin(double[] array) {
		
		//assume the max is the first value, but then step through the array
		//and each time you encounter a bigger value, update the max value assumed
		double minSeenSoFar = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] < minSeenSoFar) {
				minSeenSoFar = array[i];
			}
		}
		
		//return the max value seen
		return minSeenSoFar;
	}
	
	//this method sorts the array given (so the contents of array are altered)
	public static void selectionSort(double[] array) {
		for (int tail = array.length - 1; tail > 0; tail--) {
			
			//find max element between positions 1 and tail 
			//(inclusive on both)
			double maxSeen = array[0];
			int posMaxSeen = 0;
			for (int i=1; i <= tail; i++) {
				if (maxSeen < array[i]) {
					maxSeen = array[i];
					posMaxSeen = i;
				}
			}
			
			//swap max and last element
			array[posMaxSeen] = array[tail];
			array[tail] = maxSeen;
		}
	}
	
	//this method returns a copy of a given array
	private static double[] copyArray(double[] array) {
		
		//make a copy of array, element by element...
		double[] copy = Arrays.copyOf(array, array.length);
                
		return copy;
	}
	
	//this method calculates the middle value of the 
	//array once it has been sorted, or (in the case of
	//an array with an even number of elements) the 
	//average of the middle two values after sorting
	public static double calculateMedian(double[] array) {
		
		//make a copy of the array to sort 
		//(otherwise original order would be affected)
		double[] copy = copyArray(array);
		
		//sort the copy of the array
		selectionSort(copy);
		
		double median;
		
		//if array has an odd number of elements, 
		//the median is the central one,
		//while if it has an even number of elements, 
		//the median is the average of the central two
		if (array.length % 2 == 1) {
			median = copy[copy.length / 2];
		}
		else {
			median = (copy[copy.length / 2 - 1] + copy[copy.length / 2]) / 2.0;
		}
		
		return median;
	}
	
	//this calculates the most frequently occuring value in the array
	public static double calculateMode(double[] array) {
		
		//THIS METHOD ASSUMES THE PRESENCE OF ONLY ONE MODE IN THE DATA SET
		//AND A NON-EMPTY ARRAY. If more than one value occurs with maximum 
		//frequency, the first encountered is reported as the mode.  If 
		//the array is empty, a run-time error will occur.

		//we will need to keep track of the value that occurs most frequently 
		//in the array (as witnessed at any one point in the process below) as well
		//as how many times it was seen
		double valueWithMaxCountSeen = array[0];
		double maxCountSeen = 1;
		
		//step through each element of the array...
		for (int i = 0; i < array.length; i++) {
			
			//count the number of times the ith element in the array occurs in the array
			int count = 0;
			for (int j = 0; j < array.length; j++) {
				if (array[j] == array[i]) {
					count++;
				}
			}
			
			//if the count of the ith element is greater than the maxCountSeen, update
			//both maxCountSean and the valueWithMaxCountSeen
			if (count > maxCountSeen) {
				maxCountSeen = count;
				valueWithMaxCountSeen = array[i];
			}
		}
		
		//return the value that occurred most frequently
		return valueWithMaxCountSeen;
	}
	
	//this method calculats the standard deviation of the values in the array
	//recall, if x represents a value in the array, then the standard deviation 
	//is defined as the square root of the quotient of (x-mean)^2 summed over all 
	//possible values of x, and the quantity (n-1) 
	public static double calculateStandardDeviation(double[] array) {
		
		//the mean of the data is used in the formula for the standard deviation
		//in several places, so calculate it once, and store it in a variable
		double mean = calculateMean(array);
		
		//find sum of the squared differences between the data values and the mean
		double sum = 0;
		for (int i = 0; i < array.length; i++) {
			sum += (array[i] - mean)*(array[i] - mean);
		}
		
		//calculate the variance (which is a bias-corrected "average" of the
		//sum of the squared differences between the data values and the mean)
                double variance = 0.0;
                if (array.length > 1)
                    variance = sum / (array.length - 1);
		
		//calculate the standard deviation (which is the square root of the variance)
		double standardDeviation = Math.sqrt(variance);
		
		return standardDeviation;
	}
	

	

	


}    

