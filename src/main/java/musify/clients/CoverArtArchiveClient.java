package musify.clients;

import musify.clients.responses.coverartarchive.ReleaseCoverDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@Service
public class CoverArtArchiveClient {
  private final ClientsFactory factory;

  public Mono<ReleaseCoverDto> call(String id) {
    return factory
        .getCoverArtArchiveWebClient()
        .get()
        .uri(uriBuilder -> uriBuilder.path(id).build())
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<>() {});
  }
}
