package musify.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.NoArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@NoArgsConstructor
public class ResponseUtil {

  public static final String BILLABLE_ITEMS = "Billable-Items";
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  public static <A> Mono<ServerResponse> ok(
      Mono<Response<A>> response, Map<String, Integer> billableItems) {
    String billableItemsHeader = null;
    try {
      billableItemsHeader = OBJECT_MAPPER.writeValueAsString(billableItems);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .header(BILLABLE_ITEMS, billableItemsHeader)
        .body(response, (new ParameterizedTypeReference<Response<A>>() {}));
  }
}
