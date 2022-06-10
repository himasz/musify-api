package musify.clients;

import musify.clients.responses.wikidata.WikidataDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@Service
public class WikidataClient {
  private final ClientsFactory factory;

  public Mono<WikidataDto> call(String entityId) {
    return factory
        .getWikidataWebClient()
        .get()
        .uri(uriBuilder -> uriBuilder.path("/{entityId}.json").build(entityId))
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<>() {});
  }
}
