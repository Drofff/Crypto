import static utils.ArrayTestUtils.intArrayToStr;
import static utils.ArrayTestUtils.strToIntArray;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import drofff.crypto.AES;
import drofff.crypto.block.KeyExpansion;
import drofff.crypto.enums.Size;
import drofff.crypto.utils.WordsBuffer;

public class AESTest {

	private int [] testData;
	private int [] testKey;
	private WordsBuffer expandedTestKey;
	private AES aes;

	@Before
	public void init() {
		aes = new AES(Size._128_BITS, Size._128_BITS);
		String text = "Hello, world! I am glad to see you!".substring(0, 16);
		testData = strToIntArray(text);
		String randomKey = UUID.randomUUID().toString().substring(0, 16);
		testKey = strToIntArray(randomKey);
		expandedTestKey = new KeyExpansion(Size._128_BITS)
				.generateExpandedKey(testKey);
	}

	@Test
	public void addRoundKeyTest() throws Exception {
		Method addRoundKeyMethod = getAddRoundKeyMethod();
		int [] dataXorKey = (int[]) addRoundKeyMethod.invoke(aes, testData, 0, expandedTestKey);
		int [] resultData = (int[]) addRoundKeyMethod.invoke(aes, dataXorKey, 0, expandedTestKey);
		Assert.assertArrayEquals(testData, resultData);
	}

	@Test
	public void encryptionAndDecryptionAddRoundKeyTest() throws Exception {
		int roundsCount = 10;
		Method addRoundMethod = getAddRoundKeyMethod();
		List<int []> encryptionResult = new ArrayList<>();
		for(int round = 0; round <= roundsCount; round++) {
			int [] encryptedData = (int[]) addRoundMethod.invoke(aes, testData, round, expandedTestKey);
			encryptionResult.add(encryptedData);
		}
		for(int round = roundsCount; round >= 0; round--) {
			int [] encryptedData = encryptionResult.get(round);
			int [] decryptedData = (int[]) addRoundMethod.invoke(aes, encryptedData, round, expandedTestKey);
			Assert.assertArrayEquals("Invalid operating at round " + round + " of decryption", testData, decryptedData);
		}
	}

	private Method getAddRoundKeyMethod() throws NoSuchMethodException {
		Method addRoundMethod = aes.getClass()
				.getDeclaredMethod("addRoundKey", int[].class, int.class, WordsBuffer.class);
		addRoundMethod.setAccessible(true);
		return addRoundMethod;
	}

	@Test
	public void encryptionAndDecryptionTest() {
		System.out.println("Message: " + intArrayToStr(testData));

		int [] encryptedMessage = aes.encrypt(testData, testKey);
		String encryptedMessageStr = intArrayToStr(encryptedMessage);
		System.out.println("Encrypted message: " + encryptedMessageStr);

		int [] decryptedMessage = aes.decrypt(encryptedMessage, testKey);
		String decryptedMessageStr = intArrayToStr(decryptedMessage);
		System.out.println("Decrypted message: " + decryptedMessageStr);

		Assert.assertArrayEquals("Decrypted message doesn't match with raw message", testData, decryptedMessage);
	}

}
