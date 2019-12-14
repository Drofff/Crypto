package drofff.crypto.mode;

import drofff.crypto.algorithm.CryptoAlgorithm;
import drofff.crypto.utils.ArrayUtils;
import drofff.crypto.utils.CBCUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static drofff.crypto.utils.CBCUtils.divideIntoBlocks;

public class CBCEncoder implements CipherMode {

	private static final String COMPLEMENTARY_SYMBOL = " ";

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
		String preprocessedText = preprocessText(text);
		int blockSize = cryptoAlgorithm.getInputBlockSize();
		this.blocks = divideIntoBlocks(preprocessedText, blockSize);
		Integer[] encryptedText = applyChainEncryption();
		int[] encryptedTextOutbox = ArrayUtils.outboxArray(encryptedText);
		return postprocessText(encryptedTextOutbox);
	}

	private String preprocessText(String text) {
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
				.forEach(i -> complementedText.append(COMPLEMENTARY_SYMBOL));
		return complementedText.toString();
	}

	private String postprocessText(int[] encryptedText) {
		String encryptedTextStr = ArrayUtils.intArrayToStr(encryptedText);
		int[] initVectorOutbox = ArrayUtils.outboxArray(initVector);
		String initVectorStr = ArrayUtils.intArrayToStr(initVectorOutbox);
		return initVectorStr + encryptedTextStr;
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
