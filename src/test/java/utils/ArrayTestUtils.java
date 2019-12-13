package utils;

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

}
