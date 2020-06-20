package com.drofff.crypto.mode;

public interface CipherMode {

	String apply(String text, String key);

}
