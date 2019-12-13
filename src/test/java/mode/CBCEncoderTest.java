package mode;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import drofff.crypto.algorithm.AES;
import drofff.crypto.enums.Size;
import drofff.crypto.mode.CBCEncoder;

public class CBCEncoderTest {

	@Test
	public void encryptTest() {
		String message = "Hello, world! I want you to encrypt my message";
		String key = UUID.randomUUID().toString().substring(0, 16);
		System.out.println("Message: " + message);

		AES aes = new AES(Size._128_BITS, Size._128_BITS);
		CBCEncoder mode = CBCEncoder.withCryptoAlgorithm(aes);

		String encryptedMessage = mode.encrypt(message, key);
		System.out.println("Encrypted message: " + encryptedMessage);

		String decryptedMessage = mode.decrypt(encryptedMessage, key);
		System.out.println("Decrypted message:" + decryptedMessage);

		Assert.assertEquals(message, decryptedMessage);
	}

}
