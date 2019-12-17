package drofff.crypto.exception;

public class MathUtilsException extends RuntimeException {

	public MathUtilsException() {
		super();
	}

	public MathUtilsException(String message) {
		super(message);
	}

	public MathUtilsException(String message, Throwable cause) {
		super(message, cause);
	}

	public MathUtilsException(Throwable cause) {
		super(cause);
	}

	protected MathUtilsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
