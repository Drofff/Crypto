package drofff.crypto.dto;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import drofff.crypto.exception.AESException;
import drofff.crypto.utils.ArrayUtils;

public class WordsBuffer {

	private List<Integer[]> words = new LinkedList<>();
	private int wordSize;

	public WordsBuffer(int wordSize) {
		this.wordSize = wordSize;
	}

	public void addWord(Integer [] word) {
		if(word.length != wordSize) {
			throw new AESException("Invalid word size. Expected word size is " + wordSize);
		}
		words.add(word);
	}

	public void addAllWordsFromArray(int [] array) {
		Integer [] inboxArray = ArrayUtils.inboxArray(array);
		addAllWordsFromArray(inboxArray);
	}

	public void addAllWordsFromArray(Integer [] array) {
		int wordsCount = array.length / wordSize;
		for(int i = 0; i < wordsCount; i++) {
			int wordStartPosition = i * wordSize;
			int wordEndPosition = wordStartPosition + wordSize;
			Integer [] word = Arrays.copyOfRange(array, wordStartPosition, wordEndPosition);
			words.add(word);
		}
	}

	public Integer[] getWordAtPosition(int position) {
		return words.get(position);
	}

	public List<Integer[]> getWordsAtRange(int from, int to) {
		return words.subList(from, to);
	}

	public List<Integer[]> getAllWords() {
		return new LinkedList<>(words);
	}

	public void processEachWord(UnaryOperator<Integer[]> processingFunction) {
		words = words.stream()
				.map(processingFunction)
				.collect(Collectors.toList());
	}

	public Integer [] toArray() {
		List<Integer> bytesList = words.stream()
				.map(Arrays::stream)
				.flatMap(x -> x)
				.collect(Collectors.toList());
		return bytesList.toArray(new Integer[] {});
	}

}
