package drofff.crypto;

import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import drofff.crypto.block.KeyExpansion;
import drofff.crypto.block.MixColumns;
import drofff.crypto.block.ShiftRows;
import drofff.crypto.block.SubBytes;
import drofff.crypto.enums.RoundsCount;
import drofff.crypto.enums.Size;
import drofff.crypto.exception.AESException;
import drofff.crypto.utils.ArrayUtils;
import drofff.crypto.utils.WordsBuffer;

public class AES {

	private int keySize;
	private int blockSize;
	private int roundsCount;

	private KeyExpansion keyExpansionBlock;

	public AES(Size keySize, Size blockSize) {
		this.keySize = keySize.getSizeInBytes();
		this.blockSize = blockSize.getSizeInBytes();
		RoundsCount count = RoundsCount.fromParams(keySize, blockSize);
		this.roundsCount = count.getRoundsCount();
		this.keyExpansionBlock = new KeyExpansion(keySize);
	}

	public int [] encrypt(int [] data, int [] key) {
		validateBlockSize(data);
		validateKeySize(key);
		WordsBuffer expandedKey = keyExpansionBlock.generateExpandedKey(key);
		data = addRoundKey(data, 0, expandedKey);
		for(int round = 1; round <= roundsCount; round++) {
			int [] substitutedData = SubBytes.substitute(data);
			int [] shiftedData = ShiftRows.shift(substitutedData);
			if(isNotLastRound(round)) {
				int [] mixedData = mixColumns(shiftedData);
				data = addRoundKey(mixedData, round, expandedKey);
			} else {
				data = addRoundKey(shiftedData, round, expandedKey);
			}
		}
		return data;
	}

	public int [] decrypt(int [] data, int [] key) {
		validateBlockSize(data);
		validateKeySize(key);
		WordsBuffer expandedKey = keyExpansionBlock.generateExpandedKey(key);
		data = addRoundKey(data, roundsCount, expandedKey);
		for(int round = roundsCount - 1; round >= 0; round--) {
			int [] inverseShiftedData = ShiftRows.inverseShift(data);
			int [] inverseSubstitutedData = SubBytes.inverseSubstitute(inverseShiftedData);
			data = addRoundKey(inverseSubstitutedData, round, expandedKey);
			if(isNotFirstRound(round)) {
				data = inverseMixColumns(data);
			}
		}
		return data;
	}

	private boolean isNotLastRound(int round) {
		return round != roundsCount;
	}

	private boolean isNotFirstRound(int round) {
		return round != 0;
	}

	private void validateBlockSize(int [] block) {
		if(block.length != blockSize) {
			throw new AESException("Invalid block size. Block should have " + blockSize + " bytes");
		}
	}

	private void validateKeySize(int [] key) {
		if(key.length != keySize) {
			throw new AESException("Invalid key size. Key should be " + keySize + " bytes long");
		}
	}

	private int [] addRoundKey(int [] data, int round, WordsBuffer expandedKey) {
		int [] roundKey = getRoundKeyFromExpandedKey(round, expandedKey);
		return ArrayUtils.xorArrays(data, roundKey);
	}

	private int [] getRoundKeyFromExpandedKey(int round, WordsBuffer expandedKey) {
		int wordsInKey = keySize / KeyExpansion.WORD_SIZE;
		int roundWordsRangeStart = round * wordsInKey;
		int roundWordsRangeEnd = roundWordsRangeStart + wordsInKey;
		List<Integer[]> roundKeyWords = expandedKey.getWordsAtRange(roundWordsRangeStart, roundWordsRangeEnd);
		List<Integer> roundKeyBytesList = roundKeyWords.stream()
				.flatMap(Arrays::stream)
				.collect(Collectors.toList());
		Integer[] roundKey = roundKeyBytesList.toArray(new Integer[] {});
		return ArrayUtils.outboxArray(roundKey);
	}

	private int [] mixColumns(int [] block) {
		return applyToEachWordOfBlock(block, MixColumns::mix);
	}

	private int [] inverseMixColumns(int [] block) {
		return applyToEachWordOfBlock(block, MixColumns::inverseMix);
	}

	private int [] applyToEachWordOfBlock(int [] block, UnaryOperator<int[]> function) {
		WordsBuffer wordsBuffer = new WordsBuffer(KeyExpansion.WORD_SIZE);
		wordsBuffer.addAllWordsFromArray(block);
		wordsBuffer.processEachWord(word -> {
			int [] outboxWord = ArrayUtils.outboxArray(word);
			int [] resultWord = function.apply(outboxWord);
			return ArrayUtils.inboxArray(resultWord);
		});
		Integer [] resultBlock = wordsBuffer.toArray();
		return ArrayUtils.outboxArray(resultBlock);
	}

}