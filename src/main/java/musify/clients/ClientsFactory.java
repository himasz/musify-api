package musify.clients;

import musify.config.MusifyApiProperties;
import java.time.Duration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Component
public class ClientsFactory {

  private static final int SECONDS = 30;
  private static final int MEMORY_SIZE = 5 * 1024 * 1024;
  private static final HttpClient client =
      HttpClient.create().responseTimeout(Duration.ofSeconds(SECONDS)).followRedirect(true);
  //          .followRedirect(
  //              (req, res) -> res.responseHeaders().get("Location") != null);

  private WebClient musicBrainzWebClient;
  private WebClient wikidataWebClient;
  private WebClient wikipediaWebClient;
  private WebClient coverArtArchiveWebClient;

  private final MusifyApiProperties musifyApiProperties;

  public ClientsFactory(MusifyApiProperties musifyApiProperties) {
    this.musifyApiProperties = musifyApiProperties;
  }

  public WebClient getMusicBrainzWebClient() {
    musicBrainzWebClient =
        musicBrainzWebClient != null
            ? musicBrainzWebClient
            : WebClient.builder()
                .baseUrl(musifyApiProperties.getMusicBrainzUrl())
                .clientConnector(new ReactorClientHttpConnector(client))
                .build();
    return musicBrainzWebClient;
  }

  public WebClient getWikidataWebClient() {
    final ExchangeStrategies strategies =
        ExchangeStrategies.builder()
            .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(MEMORY_SIZE))
            .build();

    wikidataWebClient =
        wikidataWebClient != null
            ? wikidataWebClient
            : WebClient.builder()
                .exchangeStrategies(strategies)
                .baseUrl(musifyApiProperties.getWikidataUrl())
                .clientConnector(new ReactorClientHttpConnector(client))
                .build();
    return wikidataWebClient;
  }

  public WebClient getWikipediaWebClient() {
    wikipediaWebClient =
        wikipediaWebClient != null
            ? wikipediaWebClient
            : WebClient.builder()
                .baseUrl(musifyApiProperties.getWikipediaUrl())
                .clientConnector(new ReactorClientHttpConnector(client))
                .build();
    return wikipediaWebClient;
  }

  public WebClient getCoverArtArchiveWebClient() {
    coverArtArchiveWebClient =
        coverArtArchiveWebClient != null
            ? coverArtArchiveWebClient
            : WebClient.builder()
                .baseUrl(musifyApiProperties.getCoverArtArchiveUrl())
                .clientConnector(new ReactorClientHttpConnector(client))
                .build();
    return coverArtArchiveWebClient;
  }
}
