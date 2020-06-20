package com.drofff.crypto.block;

import com.drofff.crypto.exception.AESException;
import com.drofff.crypto.utils.ArrayUtils;

public class ShiftRows {

	private static final int ROWS_COUNT = 4;
	private static final int LEFT_SIDE = -1;
	private static final int RIGHT_SIDE = 1;

	private ShiftRows() {}

	public static int [] shift(int [] array) {
		return shiftArrayToSide(array, LEFT_SIDE);
	}

	public static int [] inverseShift(int [] array) {
		return shiftArrayToSide(array, RIGHT_SIDE);
	}

	private static int [] shiftArrayToSide(int [] array, int side) {
		int [][] matrix = ArrayUtils.toMatrix(array, ROWS_COUNT);
		for(int i = 0; i < matrix.length; i++) {
			int shift = getShiftForRowWithIndex(i);
			if(side == LEFT_SIDE) {
				matrix[i] = ArrayUtils.cyclicLeftShift(matrix[i], shift);
			} else if(side == RIGHT_SIDE) {
				matrix[i] = ArrayUtils.cyclicRightShift(matrix[i], shift);
			} else {
				throw new AESException("Invalid side flag");
			}
		}
		return ArrayUtils.matrixToArray(matrix);
	}

	private static int getShiftForRowWithIndex(int index) {
		return index % ROWS_COUNT;
	}

}
