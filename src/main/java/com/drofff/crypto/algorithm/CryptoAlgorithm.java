package com.drofff.crypto.algorithm;

public interface CryptoAlgorithm {

	int[] encrypt(int[] data, int[] key);

	int[] decrypt(int[] cipherText, int[] key);

	int getInputBlockSize();

}
