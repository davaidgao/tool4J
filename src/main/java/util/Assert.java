package util;

/**
 * Assertion utility class that assists in validating arguments.
 * 
 * <p>Typically used to validate method arguments rather than configuration
 * properties, to check for cases that are usually programmer errors rather
 * than configuration errors. In contrast to configuration initialization
 * code, there is usually no point in falling back to defaults in such methods.
 *
 * <p>This class is similar to JUnit's assertion library. If an argument value is
 * deemed invalid, an {@link IllegalArgumentException} is thrown (typically).
 * 
 * @author gaodawei
 * @since 1.0.0
 */
public abstract class Assert {
	/**
	 * Assert that an object is {@code null} .
	 * @param object the object to check
	 * @param message the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if the object is not {@code null}
	 */
	public static void isNull(Object object, String message) {
		if (object != null) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * Assert that an object is {@code null} .
	 * @param object the object to check
	 * @throws IllegalArgumentException if the object is not {@code null}
	 */
	public static void isNull(Object object) {
		isNull(object, "[Assertion failed] - the object argument must be null");
	}
	
	/**
	 * Assert that an object is not {@code null} .
	 * @param object the object to check
	 * @param message the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if the object is {@code null}
	 */
	public static void notNull(Object object, String message) {
		if (object == null) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * Assert that an object is not {@code null} .
	 * @param object the object to check
	 * @throws IllegalArgumentException if the object is {@code null}
	 */
	public static void notNull(Object object) {
		notNull(object, "[Assertion failed] - this argument is required; it must not be null");
	}
}
