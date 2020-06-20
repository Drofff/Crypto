package com.drofff.crypto.algorithm;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import com.drofff.crypto.dto.RSAKeys;
import com.drofff.crypto.dto.RSAKey;
import com.drofff.crypto.utils.MathUtils;

public class RSA {

	private static final Long MAX_RANDOM_PRIME = (long) 1000;
	private static final Long MIN_RANDOM_PRIME = (long) 600;

	private RSA() {}

	public static RSAKeys generateKeys() {
		Long firstPrime = generateRandomPrimeInRange();
		Long secondPrime = generateRandomPrimeInRange();
		Long groupModule = firstPrime * secondPrime;
		Long groupModuleEuler = MathUtils.eulerOfTwoPrimes(firstPrime, secondPrime);
		Long publicKey = getRandomMultiplicativeGroupMember(groupModuleEuler);
		Long privateKey = MathUtils.findInverseInGroup(publicKey, groupModuleEuler);
		return buildRSAKeys(publicKey, privateKey, groupModule);
	}

	private static RSAKeys buildRSAKeys(Long publicKey, Long privateKey, Long groupModule) {
		RSAKey rsaPublicKey = new RSAKey();
		rsaPublicKey.setExponent(publicKey);
		rsaPublicKey.setModule(groupModule);
		RSAKey rsaPrivateKey = new RSAKey();
		rsaPrivateKey.setExponent(privateKey);
		rsaPrivateKey.setModule(groupModule);
		return new RSAKeys(rsaPrivateKey, rsaPublicKey);
	}

	private static Long generateRandomPrimeInRange() {
		long prime = 1;
		while(!MathUtils.isPrime(prime)) {
			prime = ThreadLocalRandom.current()
					.nextLong(MIN_RANDOM_PRIME, MAX_RANDOM_PRIME);
		}
		return prime;
	}

	private static Long getRandomMultiplicativeGroupMember(Long groupModule) {
		long minValue = groupModule / 2;
		long randomGroupMember = groupModule;
		while(!MathUtils.areCoPrime(groupModule, randomGroupMember)) {
			randomGroupMember = ThreadLocalRandom.current()
					.nextLong(minValue, groupModule);
		}
		return randomGroupMember;
	}

	public static int[] applyKey(int[] data, RSAKey rsaKey) {
		return Arrays.stream(data)
				.map(elem -> applyKeyToElement(elem, rsaKey))
				.toArray();
	}

	private static Integer applyKeyToElement(int elem, RSAKey rsaKey) {
		BigInteger element = BigInteger.valueOf(elem);
		int exponent = rsaKey.getExponent().intValue();
		BigInteger module = BigInteger.valueOf(rsaKey.getModule());
		return element.pow(exponent)
				.mod(module)
				.intValue();
	}

}
