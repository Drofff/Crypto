package drofff.crypto.mode;

import drofff.crypto.algorithm.CryptoAlgorithm;
import drofff.crypto.utils.ArrayUtils;
import drofff.crypto.utils.CBCUtils;

import java.util.ArrayList;
import java.util.List;

public class CBCDecoder implements CipherMode {

    private CryptoAlgorithm cryptoAlgorithm;

    private int[] key;

    private CBCDecoder() {}

    public static CBCDecoder withCryptoAlgorithm(CryptoAlgorithm cryptoAlgorithm) {
        CBCDecoder cbcDecoder = new CBCDecoder();
        cbcDecoder.cryptoAlgorithm = cryptoAlgorithm;
        return cbcDecoder;
    }

    @Override
    public synchronized String apply(String cipherText, String key) {
        this.key = ArrayUtils.strToIntArray(key);
        Integer[] initVector = extractInitializationVector(cipherText);
        String cipherTextPayload = extractPayload(cipherText);
        int blockSize = cryptoAlgorithm.getInputBlockSize();
        List<Integer[]> payloadBlocks = CBCUtils.divideIntoBlocks(cipherTextPayload, blockSize);
        Integer[] decryptedText = applyChainDecryption(initVector, payloadBlocks);
        int[] decryptedTextOutbox = ArrayUtils.outboxArray(decryptedText);
        return ArrayUtils.intArrayToStr(decryptedTextOutbox);
    }

    private Integer[] extractInitializationVector(String cipherText) {
        int initVectorSize = cryptoAlgorithm.getInputBlockSize();
        String initVectorStr = cipherText.substring(0, initVectorSize);
        int[] initVector = ArrayUtils.strToIntArray(initVectorStr);
        return ArrayUtils.inboxArray(initVector);
    }

    private String extractPayload(String cipherText) {
        int initVectorSize = cryptoAlgorithm.getInputBlockSize();
        return cipherText.substring(initVectorSize);
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
