package io.seanbailey.railnetwork.exception;

/**
 * An exception that can be thrown when a file cannot be parsed.
 * @author Sean Bailey c3279343
 */
public class ParseException extends Throwable {

  /**
   * Constructs a new parse exception.
   * @param message Exception message.
   */
  public ParseException(String message) {
    super(message);
  }

  /**
   * Constructs a new parse exception.
   * @param format Message format.
   * @param values Values to populate format with.
   */
  public ParseException(String format, Object... values) {
    this(String.format(format, values));
  }
}
