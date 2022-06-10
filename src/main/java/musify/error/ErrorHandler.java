package musify.error;

import static org.springframework.http.HttpStatus.*;

import musify.response.Error;
import musify.response.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ConnectTimeoutException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Component
@Order(-2)
@Slf4j
public class ErrorHandler implements WebExceptionHandler {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @Override
  public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

    try {
      log.info("exchange attributes : " + exchange.getAttributes().values());
      log.error("errors response status: ", exchange.getResponse().getStatusCode());
      log.error("errors details: ", ex);

      if (ex instanceof MusifyApiException) {
        return responseWrite(exchange, INTERNAL_SERVER_ERROR, ex.getMessage());
      }

      if (ex instanceof ResponseStatusException
          && ex.getMessage().equals(ErrorCode.NOT_FOUND_MESSAGE)) {
        return responseWrite(exchange, BAD_REQUEST, ex.getMessage());
      }

      if (ex instanceof ConnectTimeoutException) {
        return responseWrite(exchange, SERVICE_UNAVAILABLE, ErrorCode.CONNECTION_TIMEOUT);
      }

      return responseWrite(exchange, INTERNAL_SERVER_ERROR, ErrorCode.UNKNOWN);
    } catch (Exception e) {
      log.error("unexpected errors : ", e);
    }
    return Mono.error(ex);
  }

  private Mono<Void> responseWrite(
      ServerWebExchange exchange, HttpStatus httpStatus, String message)
      throws JsonProcessingException {

    exchange.getResponse().setStatusCode(httpStatus);
    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);

    return exchange
        .getResponse()
        .writeWith(Mono.just(getDataBuffer(exchange, httpStatus, message)));
  }

  private DataBuffer getDataBuffer(
      ServerWebExchange exchange, HttpStatus httpStatus, String errorDescription)
      throws JsonProcessingException {
    return new DefaultDataBufferFactory()
        .wrap(
            OBJECT_MAPPER.writeValueAsBytes(
                Response.builder()
                    .error(
                        Error.builder()
                            .code(httpStatus.name())
                            .description(errorDescription)
                            .details(
                                List.of(
                                    String.format(
                                        "Timestamp: %s",
                                        Instant.now().atZone(ZoneId.systemDefault())),
                                    String.format("Url: %s", exchange.getRequest().getPath()),
                                    String.format(
                                        "Method: %s", exchange.getRequest().getMethodValue())))
                            .build())
                    .data(List.of())
                    .status(httpStatus.value())
                    .build()));
  }
}
