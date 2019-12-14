package drofff.crypto.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CBCUtils {

    private CBCUtils() {}

    public static List<Integer[]> divideIntoBlocks(String text, int blockSize) {
        WordsBuffer blocksBuffer = new WordsBuffer(blockSize);
        int[] textArray = ArrayUtils.strToIntArray(text);
        blocksBuffer.addAllWordsFromArray(textArray);
        return blocksBuffer.getAllWords();
    }

    public static Integer[] mergeBlocks(List<Integer[]> blocks) {
        return blocks.stream()
                .flatMap(Arrays::stream)
                .collect(Collectors.toList())
                .toArray(new Integer[] {});
    }

}
