package musify.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ResponseEntityDecorator {

  private final ObjectMapper objectMapper;

  public BodyBuilderDecorator ok() {
    return new BodyBuilderDecorator(objectMapper, ResponseEntity.ok());
  }

  public static class BodyBuilderDecorator {

    private final ObjectMapper objectMapper;

    @Getter private final ResponseEntity.BodyBuilder bodyBuilder;

    public BodyBuilderDecorator(ObjectMapper objectMapper, ResponseEntity.BodyBuilder bodyBuilder) {
      this.objectMapper = objectMapper;
      this.bodyBuilder = bodyBuilder.contentType(MediaType.APPLICATION_JSON);
    }

    public ResponseEntity<Response<MusifyResponseDto>> body(MusifyResponseDto data) {
      return bodyBuilder.body(Response.fromData(data));
    }
  }
}
