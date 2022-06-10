package musify.clients.responses.musicbrainz;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MusicBrainzDto {
  String id;
  String name;
  String gender;
  String country;
  String disambiguation;
  List<RelationDto> relations;

  @JsonProperty("release-groups")
  List<ReleaseGroupDto> releaseGroups;
}
