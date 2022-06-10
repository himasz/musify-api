package Plugsurfing.com.musify.clients;

import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import Plugsurfing.com.musify.BasicTest;
import musify.clients.WikidataClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class WikidataClientTest extends BasicTest {

  private static final String ENTITY = "Q2831";

  @Autowired
  WikidataClient client;

  @Test
  public void call() {
    client.call(ENTITY).block();
    MOCK_SERVER.verify(getRequestedFor(urlEqualTo(WIKIDATA_PATH)));
  }
}
