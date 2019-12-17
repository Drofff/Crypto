package algorithm;

import org.junit.Assert;
import org.junit.Test;

import drofff.crypto.algorithm.RSA;
import drofff.crypto.dto.RSAKey;
import drofff.crypto.dto.RSAKeys;
import drofff.crypto.utils.ArrayUtils;

public class RSATest {

	@Test
	public void applyKeyToTextTest() {
		RSAKeys rsaKeys = getDefaultRSAKeys();
		assertEncryptionAndDecryptionWithKeys(rsaKeys);
	}

	private RSAKeys getDefaultRSAKeys() {
		long module = 434617;
		RSAKey privateKey = new RSAKey();
		privateKey.setModule(module);
		privateKey.setExponent((long) 338687);
		RSAKey publicKey = new RSAKey();
		publicKey.setModule(module);
		publicKey.setExponent((long) 282527);
		return new RSAKeys(privateKey, publicKey);
	}

	@Test
	public void generateKeysTest() {
		RSAKeys rsaKeys = RSA.generateKeys();
		assertEncryptionAndDecryptionWithKeys(rsaKeys);
	}

	private void assertEncryptionAndDecryptionWithKeys(RSAKeys rsaKeys) {
		String messageStr = "Hello, world! It's me!";
		int[] message = ArrayUtils.strToIntArray(messageStr);
		int[] encryptedMessage = RSA.applyKey(message, rsaKeys.getPublicKey());
		int[] decryptedMessage = RSA.applyKey(encryptedMessage, rsaKeys.getPrivateKey());
		String decryptedMessageStr = ArrayUtils.intArrayToStr(decryptedMessage);
		Assert.assertEquals(messageStr, decryptedMessageStr);
	}

}
