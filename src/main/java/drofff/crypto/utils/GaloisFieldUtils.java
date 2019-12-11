package drofff.crypto.utils;

import static drofff.crypto.enums.Size.BITS_IN_BYTE;

public class GaloisFieldUtils {

	private static final int GALOIS_FIELD_MODULO_POLYNOMIAL = 0x11b;
	private static final int FIRST_BIT_MASK = 0x1;

	private GaloisFieldUtils() {}

	public static int [] galoisMatrixMultiplication(int [] array, int [][] matrix) {
		int [] result = new int[matrix[0].length];
		for(int columnIndex = 0; columnIndex < matrix.length; columnIndex++) {
			int columnSum = 0;
			for(int rowIndex = 0; rowIndex < matrix[0].length; rowIndex++) {
				columnSum ^= multiplyInGaloisField(array[rowIndex], matrix[rowIndex][columnIndex]);
			}
			result[columnIndex] = columnSum;
		}
		return result;
	}

	public static int multiplyInGaloisField(int arg0, int arg1) {
		int result = 0;
		for(int i = 0; i < BITS_IN_BYTE; i++) {
			if(isFirstBitSet(arg0)) {
				result ^= arg1;
			}
			boolean isLastBitSetInArg1 = isLastBitSet(arg1);
			arg1 <<= 1;
			if(isLastBitSetInArg1) {
				arg1 ^= GALOIS_FIELD_MODULO_POLYNOMIAL;
			}
			arg0 >>= 1;
		}
		return result;
	}

	private static boolean isFirstBitSet(int bits) {
		int firstBit = bits & FIRST_BIT_MASK;
		return firstBit != 0;
	}

	private static boolean isLastBitSet(int bits) {
		int lastBit = (bits >> BITS_IN_BYTE - 1) & FIRST_BIT_MASK;
		return lastBit != 0;
	}

}
