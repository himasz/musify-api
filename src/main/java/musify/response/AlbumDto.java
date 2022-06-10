package musify.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlbumDto {
  String id;
  String title;
  String imageUrl;
}
