package prr.exceptions;

import java.io.Serial;

public class UnknownClientKeyException extends Exception {

  /**
   * Class serial number.
   */
  @Serial
  private static final long serialVersionUID = 202210181053L;

  private final String key;

  public UnknownClientKeyException(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }
}
