package musify.clients;

import musify.clients.responses.wikipedia.WikipediaDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@Service
public class WikipediaClient {
  private final ClientsFactory factory;

  public Mono<WikipediaDto> call(String title) {
    return factory
        .getWikipediaWebClient()
        .get()
        .uri(uriBuilder -> uriBuilder.path(title).build())
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<>() {});
  }
}
