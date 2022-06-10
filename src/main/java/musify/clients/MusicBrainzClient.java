package musify.clients;

import musify.clients.responses.musicbrainz.MusicBrainzDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@Service
public class MusicBrainzClient {
  public static final String FORMAT = "fmt";
  public static final String INCLUDE = "inc";
  public static final String JSON = "json";
  public static final String URL_RELS_RELEASE_GROUPS = "url-rels+release-groups";

  private final ClientsFactory factory;

  public Mono<MusicBrainzDto> call(String mbid) {
    return factory
        .getMusicBrainzWebClient()
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path(mbid)
                    .queryParam(FORMAT, JSON)
                    .queryParam(INCLUDE, URL_RELS_RELEASE_GROUPS)
                    .build())
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<>() {});
  }
}
