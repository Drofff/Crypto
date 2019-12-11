package utils;

import java.nio.charset.StandardCharsets;
import java.util.stream.IntStream;

public class ArrayTestUtils {

	private ArrayTestUtils() {}

	public static boolean areEqual(int [] array0, int [] array1) {
		if(array0.length != array1.length) {
			return false;
		}
		for(int i = 0; i < array0.length; i++) {
			if(array0[i] != array1[i]) {
				return false;
			}
		}
		return true;
	}

	public static void print(int [] array) {
		StringBuilder arrayStr = new StringBuilder("[ ");
		for(int elem : array) {
			arrayStr.append(elem);
			arrayStr.append(" ");
		}
		arrayStr.append("]");
		System.out.println(arrayStr);
	}

	public static int [] strToIntArray(String str) {
		int [] intArray = new int[str.length()];
		byte [] bytes = str.getBytes(StandardCharsets.ISO_8859_1);
		IntStream.range(0, bytes.length)
				.forEach(index -> intArray[index] = bytes[index]);
		return intArray;
	}

	public static String intArrayToStr(int [] array) {
		byte [] bytes = new byte[array.length];
		IntStream.range(0, array.length)
				.forEach(index -> bytes[index] = (byte) array[index]);
		return new String(bytes);
	}

}
