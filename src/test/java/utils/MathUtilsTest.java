package utils;

import org.junit.Assert;
import org.junit.Test;

import drofff.crypto.utils.MathUtils;

public class MathUtilsTest {

	@Test
	public void isPrimeTest() {
		long prime = 7;
		boolean isPrime = MathUtils.isPrime(prime);
		Assert.assertTrue(isPrime);
	}

	@Test
	public void isNotPrimeTest() {
		long notPrime = 22;
		boolean isPrime = MathUtils.isPrime(notPrime);
		Assert.assertFalse(isPrime);
	}

	@Test
	public void areCoPrimeTest() {
		long number0 = 53;
		long number1 = 69;
		boolean areCoPrime = MathUtils.areCoPrime(number0, number1);
		Assert.assertTrue(areCoPrime);
	}

	@Test
	public void areNotCoPrimeTest() {
		long number0 = 20;
		long number1 = 40;
		boolean areCoPrime = MathUtils.areCoPrime(number0, number1);
		Assert.assertFalse(areCoPrime);
	}

	@Test
	public void findInverseInGroupTest() {
		long number = 253;
		long groupModule = 500;

		boolean areCoPrime = MathUtils.areCoPrime(groupModule, number);
		Assert.assertTrue(areCoPrime);

		long inverseNumber = MathUtils.findInverseInGroup(number, groupModule);
		long neutralElement = (number * inverseNumber) % groupModule;

		Assert.assertEquals(1, neutralElement);
	}

}
