package drofff.crypto.dto;

public class RSAKeys {

	private RSAKey privateKey;

	private RSAKey publicKey;

	public RSAKeys() {}

	public RSAKeys(RSAKey privateKey, RSAKey publicKey) {
		this.privateKey = privateKey;
		this.publicKey = publicKey;
	}

	public RSAKey getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(RSAKey privateKey) {
		this.privateKey = privateKey;
	}

	public RSAKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(RSAKey publicKey) {
		this.publicKey = publicKey;
	}
}
