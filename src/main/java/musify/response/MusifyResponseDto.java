package musify.response;

import java.util.List;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MusifyResponseDto {
  String mbid;
  String name;
  String gender;
  String country;
  String description;
  String disambiguation;
  List<AlbumDto> albums;
}
