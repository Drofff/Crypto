package drofff.crypto.enums;

public enum Size {

	_128_BITS(128), _192_BITS(192), _256_BITS(256);

	public static final int BITS_IN_BYTE = 8;

	private final int sizeInBits;

	Size(int size) {
		sizeInBits = size;
	}

	public static Size max(Size size0, Size size1) {
		if(size0.sizeInBits > size1.sizeInBits) {
			return size0;
		}
		return size1;
	}

	public int getSizeInBits() {
		return sizeInBits;
	}

	public int getSizeInBytes() {
		return getSizeInBits() / BITS_IN_BYTE;
	}

}
