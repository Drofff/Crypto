package com.drofff.crypto.mode;

import com.drofff.crypto.algorithm.CryptoAlgorithm;
import com.drofff.crypto.utils.ArrayUtils;
import com.drofff.crypto.utils.CBCUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CBCDecoder implements CipherMode {

    private static final String COMPLEMENTARY_SYMBOLS_PATTERN = "\\S+(" + CBCUtils.COMPLEMENTARY_SYMBOL_REGEX + "+)$";
    private static final Integer COMPLEMENTARY_SYMBOLS_PATTERN_GROUP = 1;

    private CryptoAlgorithm cryptoAlgorithm;

    private int[] key;

    private CBCDecoder() {}

    public static CBCDecoder withCryptoAlgorithm(CryptoAlgorithm cryptoAlgorithm) {
        CBCDecoder cbcDecoder = new CBCDecoder();
        cbcDecoder.cryptoAlgorithm = cryptoAlgorithm;
        return cbcDecoder;
    }

    @Override
    public synchronized String apply(String text, String key) {
        this.key = ArrayUtils.strToIntArray(key);
        Integer[] initVector = extractInitializationVector(text);
        String cipherText = extractCipherText(text);
        int blockSize = cryptoAlgorithm.getInputBlockSize();
        List<Integer[]> cipherTextBlocks = CBCUtils.divideIntoBlocks(cipherText, blockSize);
        Integer[] decryptedTextBlocks = applyChainDecryption(initVector, cipherTextBlocks);
        int[] decryptedTextBlocksOutbox = ArrayUtils.outboxArray(decryptedTextBlocks);
        String decryptedText = ArrayUtils.intArrayToStr(decryptedTextBlocksOutbox);
        return removeComplementarySymbols(decryptedText);
    }

    private String removeComplementarySymbols(String text) {
        Matcher matcher = Pattern.compile(COMPLEMENTARY_SYMBOLS_PATTERN)
                .matcher(text);
        if(matcher.find()) {
            int complementaryStart = matcher.start(COMPLEMENTARY_SYMBOLS_PATTERN_GROUP);
            return text.substring(0, complementaryStart);
        }
        return text;
    }

    private Integer[] extractInitializationVector(String text) {
        int initVectorSize = cryptoAlgorithm.getInputBlockSize();
        String initVectorStr = text.substring(0, initVectorSize);
        int[] initVector = ArrayUtils.strToIntArray(initVectorStr);
        return ArrayUtils.inboxArray(initVector);
    }

    private String extractCipherText(String text) {
        int initVectorSize = cryptoAlgorithm.getInputBlockSize();
        return text.substring(initVectorSize);
    }

    private Integer[] applyChainDecryption(Integer[] initVector, List<Integer[]> blocks) {
        List<Integer[]> decryptedBlocks = new ArrayList<>();
        for(int blockIndex = 0; blockIndex < blocks.size(); blockIndex++) {
            Integer[] previousBlock = blockIndex == 0 ? initVector : blocks.get(blockIndex - 1);
            Integer[] decryptedBlock = decryptBlock(previousBlock, blocks.get(blockIndex));
            decryptedBlocks.add(decryptedBlock);
        }
        return CBCUtils.mergeBlocks(decryptedBlocks);
    }

    private Integer[] decryptBlock(Integer[] previousBlock, Integer[] block) {
        int[] blockOutboxed = ArrayUtils.outboxArray(block);
        int[] decryptedBlock = cryptoAlgorithm.decrypt(blockOutboxed, key);
        int[] previousBlockOutboxed = ArrayUtils.outboxArray(previousBlock);
        int[] rawBlock = ArrayUtils.xorArrays(decryptedBlock, previousBlockOutboxed);
        return ArrayUtils.inboxArray(rawBlock);
    }

}
