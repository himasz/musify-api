package Plugsurfing.com.musify.clients;

import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import Plugsurfing.com.musify.BasicTest;
import musify.clients.MusicBrainzClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MusicBrainzClientTest extends BasicTest {
  private static final String MBID = "f27ec8db-af05-4f36-916e-3d57f91ecf5e";

  @Autowired
  MusicBrainzClient client;

  @Test
  public void call() {
    client.call(MBID).block();
    MOCK_SERVER.verify(getRequestedFor(urlEqualTo(MUSIC_BRAINZ_PATH)));
  }
}
