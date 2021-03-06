package com.drofff.crypto.mode;

import com.drofff.crypto.algorithm.CryptoAlgorithm;
import com.drofff.crypto.utils.ArrayUtils;
import com.drofff.crypto.utils.CBCUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

public class CBCEncoder implements CipherMode {

	private CryptoAlgorithm cryptoAlgorithm;

	private Integer[] initVector;
	private int[] key;
	private List<Integer[]> blocks;
	private List<Integer[]> encryptedBlocks;

	private CBCEncoder() {}

	public static CBCEncoder withCryptoAlgorithm(CryptoAlgorithm cryptoAlgorithm) {
		CBCEncoder cbcEncoder = new CBCEncoder();
		cbcEncoder.cryptoAlgorithm = cryptoAlgorithm;
		return cbcEncoder;
	}

	@Override
	public synchronized String apply(String text, String key) {
		this.key = ArrayUtils.strToIntArray(key);
		text = complementTextIfNeeded(text);
		int blockSize = cryptoAlgorithm.getInputBlockSize();
		blocks = CBCUtils.divideIntoBlocks(text, blockSize);
		Integer[] encryptedText = applyChainEncryption();
		int[] encryptedTextOutbox = ArrayUtils.outboxArray(encryptedText);
		return prependInitializationVectorToText(encryptedTextOutbox);
	}

	private String complementTextIfNeeded(String text) {
		int inputBlockSize = cryptoAlgorithm.getInputBlockSize();
		if(!isDividableIntoBlocks(text, inputBlockSize)) {
			return complementTextToBlockSize(text, inputBlockSize);
		}
		return text;
	}

	private boolean isDividableIntoBlocks(String text, int blockSize) {
		return text.length() % blockSize == 0;
	}

	private String complementTextToBlockSize(String text, int blockSize) {
		int lastBlockSize = text.length() % blockSize;
		int blocksDiff = blockSize - lastBlockSize;
		StringBuilder complementedText = new StringBuilder(text);
		IntStream.range(0, blocksDiff)
				.forEach(i -> complementedText.append(CBCUtils.COMPLEMENTARY_SYMBOL));
		return complementedText.toString();
	}

	private String prependInitializationVectorToText(int[] text) {
		String textStr = ArrayUtils.intArrayToStr(text);
		int[] initVectorOutbox = ArrayUtils.outboxArray(initVector);
		String initVectorStr = ArrayUtils.intArrayToStr(initVectorOutbox);
		return initVectorStr + textStr;
	}

	private Integer[] applyChainEncryption() {
		int lastBlockIndex = blocks.size() - 1;
		encryptedBlocks = new LinkedList<>();
		applyRecursiveChainEncryption(lastBlockIndex);
		return CBCUtils.mergeBlocks(encryptedBlocks);
	}

	private Integer[] applyRecursiveChainEncryption(int blockIndex) {
		if(blockIndex == 0) {
			generateInitializationVector();
			return encryptBlock(initVector, blocks.get(blockIndex));
		}
		Integer[] previousBlock = applyRecursiveChainEncryption(blockIndex - 1);
		return encryptBlock(previousBlock, blocks.get(blockIndex));
	}

	private void generateInitializationVector() {
		String randomUUID = UUID.randomUUID().toString()
				.substring(0, cryptoAlgorithm.getInputBlockSize());
		int[] initializationVector = ArrayUtils.strToIntArray(randomUUID);
		this.initVector = ArrayUtils.inboxArray(initializationVector);
	}

	private Integer[] encryptBlock(Integer[] previousBlock, Integer[] block) {
		Integer[] xoredBlock = ArrayUtils.xorArrays(previousBlock, block);
		int[] xoredBlockOutbox = ArrayUtils.outboxArray(xoredBlock);
		int[] encryptedBlock = cryptoAlgorithm.encrypt(xoredBlockOutbox, key);
		Integer[] encryptedBlockInbox = ArrayUtils.inboxArray(encryptedBlock);
		encryptedBlocks.add(encryptedBlockInbox);
		return encryptedBlockInbox;
	}

}
