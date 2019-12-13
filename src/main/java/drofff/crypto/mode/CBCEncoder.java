package drofff.crypto.mode;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import drofff.crypto.algorithm.CryptoAlgorithm;
import drofff.crypto.utils.ArrayUtils;
import drofff.crypto.utils.WordsBuffer;

public class CBCEncoder implements CipherMode {

	private static final String COMPLEMENTARY_SYMBOL = " ";

	private CryptoAlgorithm cryptoAlgorithm;

	private Integer[] initVector;
	private int[] key;
	private List<Integer[]> blocks;

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
		this.blocks = divideIntoBlocks(preprocessedText);
		int rounds = blocks.size() - 1;
		Integer[] encryptedText = doRecursiveChainEncryption(rounds);
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

	private List<Integer[]> divideIntoBlocks(String text) {
		int blockSize = cryptoAlgorithm.getInputBlockSize();
		WordsBuffer blocksBuffer = new WordsBuffer(blockSize);
		int[] textArray = ArrayUtils.strToIntArray(text);
		blocksBuffer.addAllWordsFromArray(textArray);
		return blocksBuffer.getAllWords();
	}

	private Integer[] doRecursiveChainEncryption(int rounds) {
		if(rounds == 0) {
			generateInitializationVector();
			return doEncrypt(initVector, blocks.get(rounds));
		}
		Integer[] previousCipherText = doRecursiveChainEncryption(rounds - 1);
		return doEncrypt(previousCipherText, blocks.get(rounds));
	}

	private void generateInitializationVector() {
		String randomUUID = UUID.randomUUID().toString()
				.substring(0, cryptoAlgorithm.getInputBlockSize());
		int[] initializationVector = ArrayUtils.strToIntArray(randomUUID);
		this.initVector = ArrayUtils.inboxArray(initializationVector);
	}

	private Integer[] doEncrypt(Integer[] previousCipherText, Integer[] text) {
		Integer[] inputBlock = ArrayUtils.xorArrays(previousCipherText, text);
		int[] inputBlockOutbox = ArrayUtils.outboxArray(inputBlock);
		int[] cipherText = cryptoAlgorithm.encrypt(inputBlockOutbox, key);
		return ArrayUtils.inboxArray(cipherText);
	}

}
