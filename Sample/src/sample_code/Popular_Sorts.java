package sample_code;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Sample code demonstration:
 * 
 *  Takes an input.txt, reads and sorts it two different ways- giving runtime and outputs the sorted arrays
 * 
 * 
 * 1) Read of input - For this case, sample input files will be provided
 * 	                    if the code needs to be tested (Whole numbers only for more simplicity)
 * 
 * 2) Analysis/Processing of data - List of integers will be converted into an array of integers, 
 * 									then sorted in two different ways
 * 
 * 3) Output of data - Will be the array of sorted elements, and with the runtime of the corresponding sort
 * 						
 * 
 * Will include all methods in one class for simplicity's sake
 * All methods will also be static to negate the need for instantiation of this object
 * 
 * Not too useful of a program unless you wish to observe execution time growths
 * 
 * @author Vincent Feng
 */
public class Popular_Sorts {
	
	//Unnessesary but feels right to have a constructor
	Popular_Sorts(){
	}
	
	public static void main(String[] args) {
		
		/*
		 * Calculate runtime for reading input size
		 */
		
		long beforeRead = System.nanoTime();
		
		//If path is given via command line, we would just take it from args and place it into the paremeter here
		Integer[] array = readWholeInput("<DIRECTORY PATH OF THE INPUT FILE GOES HERE>");
		//The method of input can be tweaked to adjust for needs, but there was no initial specification
		
		
		long afterRead = System.nanoTime();
		
		long readElapsed = afterRead - beforeRead;
		System.out.println("File read time in: " + readElapsed + "ns.");

		/*
		 * Calculate runtime of count sort
		 */
		long beforeCount = System.nanoTime();
		
		Integer[] cSorted = countSort(array);
		
		long afterCount = System.nanoTime();
		long countElapsed = afterCount - beforeCount;
		
		System.out.println("Count sorted in: " + countElapsed + "ns.");
		
		printArray(cSorted);
		
		/*
		 * Caculate runtime of quick sort
		 */
		long beforeQuick = System.nanoTime();
		
		quickSort(array);
		
		long afterQuick = System.nanoTime();
		long quickElapsed = afterQuick - beforeQuick;
		
		System.out.println("Quick sorted in: " + quickElapsed + "ns.");
		
		printArray(array);
	}
	
	/**
	 * Prints all elements in array
	 * @param array : Array to be printed
	 */
	private static void printArray(Integer[] array) {
		System.out.print("Elements in array: [ ");
		for (int i = 0; i < array.length; i++) {
			if (i != 0) {
				System.out.print(" ,");
			}
			System.out.print(array[i]);
		}
		System.out.println(" ]");
	}
	
	
	/**
	 * Reads and converts input file into an array of integers
	 * 
	 * @param path : Path of the file
	 * @return Array containing the contents of the input file
	 * 
	 */
	private static Integer[] readWholeInput(String path){
		//ArrayList to store output
		List<String> listOfContents = new ArrayList<String>();
		
		//Try-Catch in case error occurs in reading file
		try {
			listOfContents = Files.readAllLines(Paths.get(path, ""));
		} catch (IOException e) {
			//In case of error, print out stack trace to perhaps see the cause
			e.printStackTrace();
			System.out.println("Something went wrong reading the file!");
		}
		
		Integer[] arrayOfInt = new Integer[listOfContents.size()];
		//Loop to turn this elements from string to integer
		for(int i = 0; i < listOfContents.size(); i++) {
			arrayOfInt[i] = Integer.parseInt(listOfContents.get(i));
		}

		return arrayOfInt;
		
		/* Chose to use raw arrays instead of a data structure such as List make
		 * code more legible when implementing the sorts.
		 */
	}
	
	
	/**
	 * Performs count sort on a list of elements of type string
	 * 
	 * Public static to allow use even without instance of class
	 * 
	 * @param array : array to be sorted
	 * @return sorted array of elements
	 */
	public static Integer[] countSort(Integer[] array) {
		int len = array.length; //better to type 3 letters than the entire array.length
		Integer[] sorted = new Integer[len];
		
		//Will provide an artifical range, then
		//Therefore we set it as the higher bound
		int count[] = new int[1000];
		
		//Initialize every value in count to be 0
		for(int i = 0; i < 1000; i++) {
			count[i] = 0;
		}
		
		//Store the count of each integer in corresponding index
		for(int i = 0; i < len; i++) {
			++count[array[i]];
		}
		
		//Changes values to match the proper index for element
		for (int i = 1; i < 1000; i++) {
			count[i] += count[i-1];
		}
		
		//We now construct the sorted array (backwards, to maintain stability between potential keys.. but doesn't matter too much here)
		for(int i = len-1; i >= 0; i--) {
			sorted[count[array[i]]-1] = array[i];
			--count[array[i]];
		}
		
		return sorted;
		
		/*Additional Notes: For this implementation of Count Sort, it will break for any negative input values or
		 * 					any input with a value greater than the range bound stated - 1.
		 * 		            Generally this sort should be reserved for inputs that we are certain of in terms 
		 * 				    of ranges and types, but it is one of my favorite sorts since it is a linear-time, 
		 * 				    non-comparison sorting algorithm. It does take up space, though.
		 */
	}

	
	/**
	 * Recursive implementation of Quick Sort, public as a wrapper
	 * to hide unnecessary confusion of other required paremeters
	 * 
	 * Static to allow use without instance of class
	 * 
	 * @param array : array to be sorted
	 * @return nothing, reference of array of passed and it is altered in quickSort
	 */
	public static void quickSort(Integer[] array) {
		//We know the start is always 0 and end is aways length-1, no need for user to input it.
		recurseQSort(array, 0, array.length-1);
		
		/* Additional notes: Quicksort runtime outgrows Countsort's but it need not rely on knowledge of the range
		 *  of element values
		 */
	}
	
	/**
	 * Actual quick sort, implemented using recursion
	 * 
	 * @param array : Array to be sorted
	 * @param start : Start of array
	 * @param end : End of array
	 */
	private static void recurseQSort(Integer[] array, int start, int end) {
		
		int index = partition(array, start, end);
		
	    //Recursive quicksort on left hand portion of array that was partitioned
		if (start < index - 1) {
			recurseQSort(array, start, index - 1);
		}
		
		//We do the same for the right side
		if (end > index) {
			recurseQSort(array, index, end);
		}
	}
	
	/**
	 * Division of the array into halves, 
	 * effectively places all values less than or greater than pivot into the right place
	 * @param array : Array to be split
	 * @param l : Left of the array
	 * @param r : Right of the array
	 * @return : The index at which the array is split
	 */
	private static int partition(Integer[] array, int l, int r) {
		int pivot = array[l];
		
		while (r >= l) {
			
			while (array[l] < pivot) {
				l++;
			}
			while (array[r] > pivot) {
				r--;
			}
			
			if (l <= r) {
				int temp = array[l];
				array[l] = array[r];
				array[r] = temp;
				
				l++; r--;
			}	
		}	
		return l;
		
		
	}
}