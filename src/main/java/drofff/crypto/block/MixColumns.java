package drofff.crypto.block;

import static drofff.crypto.utils.GaloisFieldUtils.galoisMatrixMultiplication;

import drofff.crypto.exception.AESException;

public class MixColumns {

	private MixColumns() {}

	private static final int [][] MIX_COLUMN_MATRIX = new int[][] {
			{ 2, 3, 1, 1 },
			{ 1, 2, 3, 1 },
			{ 1, 1, 2, 3 },
			{ 3, 1, 1, 2 }
	};
	private static final int [][] INVERSE_MIX_COLUMN_MATRIX = new int[][] {
			{ 14, 11, 13, 9 },
			{ 9, 14, 11, 13 },
			{ 13, 9, 14, 11 },
			{ 11, 13, 9, 14 }
	};

	public static int [] mix(int [] array) {
		validateInputArraySize(array);
		return galoisMatrixMultiplication(array, MIX_COLUMN_MATRIX);
	}

	public static int [] inverseMix(int [] array) {
		validateInputArraySize(array);
		return galoisMatrixMultiplication(array, INVERSE_MIX_COLUMN_MATRIX);
	}

	private static void validateInputArraySize(int [] array) {
		int requiredInputArraySize = MIX_COLUMN_MATRIX.length;
		if(array.length != requiredInputArraySize) {
			throw new AESException("Invalid array size. Size should be " + requiredInputArraySize + " bytes");
		}
	}

}