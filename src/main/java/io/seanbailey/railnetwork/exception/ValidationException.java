package io.seanbailey.railnetwork.exception;

/**
 * An exception that can be thrown whenever validation of a certain resource
 * fails.
 * @author Sean Bailey c3279343
 */
public class ValidationException extends Throwable {
  
  /**
   * Constructs a new validation exception.
   * @param message Exception message.
   */
  public ValidationException(String message) {
    super(message);
  }

  /**
   * Constructs a new validation exception.
   * @param format Message format.
   * @param values Values to populate format with.
   */
  public ValidationException(String format, Object... values) {
    this(String.format(format, values));
  }
}
