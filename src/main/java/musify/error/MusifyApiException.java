package musify.error;

import lombok.Getter;

@Getter
public class MusifyApiException extends RuntimeException {

  public MusifyApiException(String message) {
    super(message);
  }
}
