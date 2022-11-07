package prr.exceptions;

import java.io.Serial;

/**
 * Exception for known import file entries that do not provide the correct
 * fields.
 */
public class InvalidEntryException extends Exception {

  /**
   * Class serial number.
   */
  @Serial
  private static final long serialVersionUID = 2022101921433L;

  /**
   * Illegal entry fields.
   */
  private String[] _entryFields;

  /**
   * @param entryFields
   */
  public InvalidEntryException(String[] entryFields) {
    _entryFields = entryFields;
  }

  /**
   * @param entryFields
   * @param cause
   */
  public InvalidEntryException(String[] entryFields, Exception cause) {
    super(cause);
    _entryFields = entryFields;
  }

  /**
   * @return the illegal entry specification.
   */
  public String[] getEntrySpecification() {
    return _entryFields;
  }

}
