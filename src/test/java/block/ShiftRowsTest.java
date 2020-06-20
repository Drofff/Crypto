package block;

import org.junit.Assert;
import org.junit.Test;

import com.drofff.crypto.block.ShiftRows;

public class ShiftRowsTest {

	@Test
	public void shiftTest() {
		int [] testArray = new int[] { 22, 64, 3, 18, 74, 102, 3, 1, 35, 12, 94, 42, 67, 34, 12, 1 };
		int [] expectedArray = new int[] { 22, 64, 3, 18, 102, 3, 1, 74, 94, 42, 35, 12, 1, 67, 34, 12 };
		int [] shiftedArray = ShiftRows.shift(testArray);
		Assert.assertArrayEquals(expectedArray, shiftedArray);
	}

	@Test
	public void inverseShiftTest() {
		int [] testArray = new int[] { 22, 64, 3, 18, 74, 102, 3, 1, 35, 12, 94, 42, 67, 34, 12, 1 };
		int [] expectedArray = new int[] { 22, 64, 3, 18, 1, 74, 102, 3, 94, 42, 35, 12, 34, 12, 1, 67 };
		int [] shiftedArray = ShiftRows.inverseShift(testArray);
		Assert.assertArrayEquals(expectedArray, shiftedArray);
	}

	@Test
	public void shiftInversionTest() {
		int [] testArray = new int[] { 22, 64, 3, 18, 74, 102, 3, 1, 35, 12, 94, 42, 67, 34, 12, 1 };
		int [] shiftedArray = ShiftRows.shift(testArray);
		int [] inverseShiftedArray = ShiftRows.inverseShift(shiftedArray);
		Assert.assertArrayEquals(testArray, inverseShiftedArray);
	}

}
