package block;

import org.junit.Assert;
import org.junit.Test;

import drofff.crypto.block.MixColumns;
import utils.ArrayTestUtils;

public class MixColumnsTest {

	@Test
	public void inversionTest() {
		int [] testArray = new int[] { 129, 186, 226, 110 };
		int [] mixedArray = MixColumns.mix(testArray);
		int [] inverseMixedArray = MixColumns.inverseMix(mixedArray);
		Assert.assertFalse(ArrayTestUtils.areEqual(testArray, mixedArray));
		Assert.assertArrayEquals(testArray, inverseMixedArray);
	}

}
