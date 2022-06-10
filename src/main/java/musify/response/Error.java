package musify.response;

import java.util.List;
import lombok.*;

@Getter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Error {

  private String code;
  private String description;
  private List<String> details;
}
