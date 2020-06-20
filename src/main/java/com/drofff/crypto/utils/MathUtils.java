package com.drofff.crypto.utils;

import static com.drofff.crypto.enums.Size.BITS_IN_BYTE;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MathUtils {

	private static final int GALOIS_FIELD_MODULO_POLYNOMIAL = 0x11b;
	private static final int FIRST_BIT_MASK = 0x1;

	private MathUtils() {}

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

	public static boolean isPrime(Long number) {
		if(number <= 1) {
			return false;
		}
		for(long divider = 2; divider < number; divider++) {
			if(isDividable(number, divider)) {
				return false;
			}
		}
		return true;
	}

	private static boolean isDividable(Long number, Long divider) {
		return number % divider == 0;
	}

	public static boolean areCoPrime(Long number0, Long number1) {
		return calculateGreatestCommonDivisor(number0, number1) == 1;
	}

	private static long calculateGreatestCommonDivisor(Long number0, Long number1) {
		if(number1 == 0) {
			return number0;
		}
		long rest = number0 % number1;
		return calculateGreatestCommonDivisor(number1, rest);
	}

	public static Long eulerOfTwoPrimes(Long prime0, Long prime1) {
		return (prime0 - 1) * (prime1 - 1);
	}

	public static Long findInverseInGroup(Long number, Long groupModule) {
		long a = number;
		long b = groupModule;
		List<Long> numberCoefficients = new LinkedList<>(Arrays.asList((long) 1, (long) 0));
		for(int i = 1; ; i++) {
			if(b == 0) {
				break;
			}
			long rest = a % b;
			long quotient = a / b;
			a = b;
			b = rest;
			long numberCoefficient = calculateNextCoefficient(quotient, i, numberCoefficients);
			numberCoefficients.add(numberCoefficient);
		}
		int lastCoefficientIndex = numberCoefficients.size() - 1;
		long inverse = numberCoefficients.get(lastCoefficientIndex - 1);
		return convertInGroupToPositiveIfNeeded(inverse, groupModule);
	}

	private static Long calculateNextCoefficient(Long quotient, int i, List<Long> coefficients) {
		return coefficients.get(i - 1) - quotient * coefficients.get(i);
	}

	private static Long convertInGroupToPositiveIfNeeded(Long number, Long groupModule) {
		return number < 0 ? number + groupModule : number;
	}

}
