package com.drofff.crypto.enums;

public enum RoundsCount {

	SMALL(10), MIDDLE(12), BIG(14);

	private final int roundCount;

	RoundsCount(int count) {
		roundCount = count;
	}

	public static RoundsCount fromParams(Size keySize, Size blockSize) {
		Size maxSize = Size.max(keySize, blockSize);
		return sizeToRoundsCount(maxSize);
	}

	private static RoundsCount sizeToRoundsCount(Size size) {
		int ordinal = size.ordinal();
		return RoundsCount.values()[ordinal];
	}

	public int getRoundsCount() {
		return roundCount;
	}
}
