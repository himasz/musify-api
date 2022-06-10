package Plugsurfing.com.musify.clients;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import Plugsurfing.com.musify.BasicTest;
import musify.clients.CoverArtArchiveClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CoverArtArchiveClientTest extends BasicTest {

  private static final String ID = "97e0014d-a267-33a0-a868-bb4e2552918a";

  @Autowired
  CoverArtArchiveClient client;

  @Test
  public void call() {
    client.call(ID).block();
    MOCK_SERVER.verify(getRequestedFor(urlEqualTo(COVER_ART_ARCHIVE_PATH)));
  }
}
