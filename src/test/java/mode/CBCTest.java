package mode;

import drofff.crypto.algorithm.AES;
import drofff.crypto.enums.Size;
import drofff.crypto.mode.CBCDecoder;
import drofff.crypto.mode.CBCEncoder;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

public class CBCTest {

	@Test
	public void encryptionAndDecryptionTest() {
		String message = "Hello, world! I want you to encrypt my message";
		String key = UUID.randomUUID().toString().substring(0, 16);
		System.out.println("Message: " + message);

		AES aes = new AES(Size._128_BITS, Size._128_BITS);
		CBCEncoder encoder = CBCEncoder.withCryptoAlgorithm(aes);
		CBCDecoder decoder = CBCDecoder.withCryptoAlgorithm(aes);

		String encryptedMessage = encoder.apply(message, key);
		System.out.println("Encrypted message: " + encryptedMessage);

		String decryptedMessage = decoder.apply(encryptedMessage, key);
		System.out.println("Decrypted message:" + decryptedMessage);

		Assert.assertEquals(message, decryptedMessage);
	}

}
