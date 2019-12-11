package drofff.crypto.utils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import drofff.crypto.exception.AESException;

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

}
