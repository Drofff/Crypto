package drofff.crypto.block;

import drofff.crypto.enums.RoundsCount;
import drofff.crypto.enums.Size;
import drofff.crypto.utils.ArrayUtils;
import drofff.crypto.utils.GaloisFieldUtils;
import drofff.crypto.utils.WordsBuffer;

public class KeyExpansion {

	public static final int WORD_SIZE = 4;
	private static final int ROT_WORD_SHIFT = 1;

	private int expandedKeySize;
	private int wordsInRoundKeyCount;

	public KeyExpansion(Size keySize) {
		int roundKeySize = keySize.getSizeInBytes();
		RoundsCount roundsCount = RoundsCount.fromParams(keySize, keySize);
		expandedKeySize = (roundsCount.getRoundsCount() + 1) * roundKeySize;
		wordsInRoundKeyCount = roundKeySize / WORD_SIZE;
	}

	public WordsBuffer generateExpandedKey(int [] cipherKey) {
		Integer [] inboxCipherKey = ArrayUtils.inboxArray(cipherKey);
		return generateExpandedKey(inboxCipherKey);
	}

	public WordsBuffer generateExpandedKey(Integer [] cipherKey) {
		WordsBuffer wordsBuffer = new WordsBuffer(WORD_SIZE);
		wordsBuffer.addAllWordsFromArray(cipherKey);
		int wordsCount = expandedKeySize / WORD_SIZE;
		for(int wordIndex = wordsInRoundKeyCount; wordIndex < wordsCount; wordIndex++) {
			appendRoundKeyIntoWordsBuffer(wordIndex, wordsBuffer);
		}
		return wordsBuffer;
	}

	private void appendRoundKeyIntoWordsBuffer(int wordIndex, WordsBuffer wordsBuffer) {
		Integer [] previousWord = wordsBuffer.getWordAtPosition(wordIndex - 1);
		if(isComplexFunctionNeeded(wordIndex)) {
			int roundIndex = wordIndex / wordsInRoundKeyCount;
			previousWord = applyComplexFunctionOnWordWithRoundIndex(previousWord, roundIndex);
		}
		Integer [] previousRoundWord = wordsBuffer.getWordAtPosition(wordIndex - wordsInRoundKeyCount);
		Integer [] resultWord = ArrayUtils.xorArrays(previousWord, previousRoundWord);
		wordsBuffer.addWord(resultWord);
	}

	private boolean isComplexFunctionNeeded(int position) {
		int positionModuloInterval = position % wordsInRoundKeyCount;
		return positionModuloInterval == 0;
	}

	private Integer [] applyComplexFunctionOnWordWithRoundIndex(Integer [] word, int roundIndex) {
		int [] outboxedWord = ArrayUtils.outboxArray(word);
		int [] shiftedWord = ArrayUtils.cyclicLeftShift(outboxedWord, ROT_WORD_SHIFT);
		int [] substitutedWord = SubBytes.substitute(shiftedWord);
		int [] roundConstantWord = getRoundConstantWordByIndex(roundIndex);
		int [] result = ArrayUtils.xorArrays(substitutedWord, roundConstantWord);
		return ArrayUtils.inboxArray(result);
	}

	private int[] getRoundConstantWordByIndex(int roundIndex) {
		int firstByte = getRoundConstantFirstByteByIndex(roundIndex);
		return new int[] { firstByte, 0x0, 0x0, 0x0 };
	}

	private int getRoundConstantFirstByteByIndex(int roundIndex) {
		if(roundIndex == 1) {
			return 1;
		}
		int previousRoundConstantFirstByte = getRoundConstantFirstByteByIndex(roundIndex - 1);
		return GaloisFieldUtils.multiplyInGaloisField(2, previousRoundConstantFirstByte);
	}

}
