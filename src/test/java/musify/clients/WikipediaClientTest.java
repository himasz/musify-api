package Plugsurfing.com.musify.clients;

import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import Plugsurfing.com.musify.BasicTest;
import musify.clients.WikipediaClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class WikipediaClientTest extends BasicTest {
  private static final String TITLE = "Michael Jackson";

  @Autowired
  WikipediaClient client;

  @Test
  public void call() {
    client.call(TITLE).block();
    MOCK_SERVER.verify(getRequestedFor(urlEqualTo(WIKIPEDIA_PATH)));
  }
}
