package musify.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {

  private Integer status;
  private Error error;
  private T data;

  public static <T> Response<T> fromData(T data) {
    return Response.<T>builder().data(data).status(HttpStatus.OK.value()).build();
  }
}
