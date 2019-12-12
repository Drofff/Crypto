package drofff.crypto.block;

import java.util.Arrays;
import java.util.stream.Collectors;

import drofff.crypto.utils.ArrayUtils;

public class SubBytes {

	private static final int MASK = 15;

	private SubBytes() {
	}

	public static int[] substitute(int[] array) {
		return substituteArrayByTable(array, SubTable.SUBSTITUTION_TABLE);
	}

	public static int[] inverseSubstitute(int[] array) {
		return substituteArrayByTable(array, SubTable.INVERSE_SUBSTITUTION_TABLE);
	}

	private static int[] substituteArrayByTable(int[] array, SubTable substitutionTable) {
		Integer[] inboxArray = ArrayUtils.inboxArray(array);
		Integer[] substitutedArray = Arrays.stream(inboxArray)
				.map(elem -> substituteByteByTable(elem, substitutionTable))
				.collect(Collectors.toList())
				.toArray(new Integer[] {});
		return ArrayUtils.outboxArray(substitutedArray);
	}

	private static int substituteByteByTable(int elem, SubTable substitutionTable) {
		int rowIndex = getRowIndexFromByte(elem);
		int columnIndex = getColumnIndexFromByte(elem);
		return substitutionTable.get()[rowIndex][columnIndex];
	}

	private static int getColumnIndexFromByte(int elem) {
		return elem & MASK;
	}

	private static int getRowIndexFromByte(int elem) {
		int shifted = elem >> 4;
		return shifted & MASK;
	}

}
