import static utils.ArrayTestUtils.intArrayToStr;
import static utils.ArrayTestUtils.strToIntArray;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import drofff.crypto.AES;
import drofff.crypto.enums.Size;

public class AESTest {

	@Test
	public void encryptionAndDecryptionTest() {
		String message = "Hello, world! I am glad to see you!".substring(0, 16);
		String key = UUID.randomUUID().toString();
		System.out.println("Message: " + message);

		int [] messageData = strToIntArray(message);
		int [] keyData = strToIntArray(key.substring(0, 16));

		AES aes = new AES(Size._128_BITS, Size._128_BITS);

		int [] encryptedMessage = aes.encrypt(messageData, keyData);
		String encryptedMessageStr = intArrayToStr(encryptedMessage);
		System.out.println("Encrypted message: " + encryptedMessageStr);

		int [] decryptedMessage = aes.decrypt(encryptedMessage, keyData);
		String decryptedMessageStr = intArrayToStr(decryptedMessage);
		System.out.println("Decrypted message: " + decryptedMessageStr);

		Assert.assertArrayEquals(messageData, decryptedMessage);
	}

}
