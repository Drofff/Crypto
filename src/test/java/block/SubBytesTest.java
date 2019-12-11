package block;

import org.junit.Assert;
import org.junit.Test;

import drofff.crypto.block.SubBytes;

public class SubBytesTest {

	@Test
	public void substituteTest() {
		int [] testArray = new int[] { 0xfe, 0x1f, 0x1e, 0x11 };
		int [] expectedResult = new int[] { 0xbb, 0xc0, 0x72, 0x82 };
		int [] substitutedArray = SubBytes.substitute(testArray);
		Assert.assertArrayEquals(expectedResult, substitutedArray);
	}

	@Test
	public void inverseSubstituteTest() {
		int [] testArray = new int[] { 0x56, 0xf4, 0x3e, 0x22 };
		int [] expectedResult = new int[] { 0xb9, 0xba, 0xd1, 0x94 };
		int [] inverseSubstitutedArray = SubBytes.inverseSubstitute(testArray);
		Assert.assertArrayEquals(expectedResult, inverseSubstitutedArray);
	}

	@Test
	public void inversionTest() {
		int [] testArray = new int[] { 0x56, 0x1f, 0x3e, 0x11 };
		int [] substitutedArray = SubBytes.substitute(testArray);
		int [] inverseSubstitutedArray = SubBytes.inverseSubstitute(substitutedArray);
		Assert.assertArrayEquals(testArray, inverseSubstitutedArray);
	}

}
