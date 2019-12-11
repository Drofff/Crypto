package drofff.crypto.utils;

import java.util.Arrays;
import java.util.stream.IntStream;

import drofff.crypto.exception.AESException;

public class ArrayUtils {

	private ArrayUtils() {}

	public static <T> boolean isElementInArray(T element, T [] array) {
		for(T arrayElement : array) {
			if(element.equals(arrayElement)) {
				return true;
			}
		}
		return false;
	}

	public static <T> int indexOfElementInArray(T element, T [] array) {
		for(int i = 0; i < array.length; i++) {
			if(element.equals(array[i])) {
				return i;
			}
		}
		throw new AESException("Not element of array");
	}

	public static Integer[] inboxArray(int [] array) {
		Integer[] resultArray = new Integer[array.length];
		IntStream.range(0, array.length)
				.forEach(index -> resultArray[index] = array[index]);
		return resultArray;
	}

	public static int [] outboxArray(Integer [] array) {
		int [] outboxedArray = new int[array.length];
		IntStream.range(0, array.length)
				.forEach(index -> outboxedArray[index] = array[index]);
		return outboxedArray;
	}

	public static int [][] toMatrix(int [] array, int rowsCount) {
		int rowSize = array.length / rowsCount;
		int [][] matrix = new int[rowsCount][rowSize];
		for(int i = 0; i < rowsCount; i++) {
			int arrayStartIndex = i * rowSize;
			int arrayEndIndex = arrayStartIndex + rowSize;
			matrix[i] = Arrays.copyOfRange(array, arrayStartIndex, arrayEndIndex);
		}
		return matrix;
	}

	public static int [] cyclicLeftShift(int [] array, int shift) {
		return cyclicShift(array, shift);
	}

	public static int [] cyclicRightShift(int [] array, int shift) {
		int shiftedSubarrayStartingIndex = array.length - shift;
		return cyclicShift(array, shiftedSubarrayStartingIndex);
	}

	private static int [] cyclicShift(int [] array, int shift) {
		int [] shiftedArray = new int[array.length];
		int shiftedArrayIndex = 0;
		for(int i = shift; i < array.length; i++) {
			shiftedArray[shiftedArrayIndex++] = array[i];
		}
		for(int i = 0; i < shift; i++) {
			shiftedArray[shiftedArrayIndex++] = array[i];
		}
		return shiftedArray;
	}

	public static int [] matrixToArray(int [][] matrix) {
		int [] array = new int[matrix.length * matrix[0].length];
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix[0].length; j++) {
				int arrayIndex = matrixIndexToArrayIndex(i, j, matrix[i].length);
				array[arrayIndex] = matrix[i][j];
			}
		}
		return array;
	}

	private static int matrixIndexToArrayIndex(int i, int j, int rowSize) {
		return rowSize * i + j;
	}

	public static Integer [] xorArrays(Integer [] array0, Integer [] array1) {
		int [] array0Outboxed = outboxArray(array0);
		int [] array1Outboxed = outboxArray(array1);
		int [] xorResult = xorArrays(array0Outboxed, array1Outboxed);
		return inboxArray(xorResult);
	}

	public static int [] xorArrays(int [] array0, int [] array1) {
		int [] result = new int[array0.length];
		for(int i = 0; i < array0.length; i++) {
			result[i] = array0[i] ^ array1[i];
		}
		return result;
	}

}
