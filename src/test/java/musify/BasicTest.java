package Plugsurfing.com.musify;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import musify.Application;
import com.github.tomakehurst.wiremock.WireMockServer;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.http.HttpHeader;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = {Application.class})
@AutoConfigureWebTestClient(timeout = "360000")
@ActiveProfiles("test")
public class BasicTest {
  public static final String WIKIDATA_BODY = resourceFileToString("/responses/wikidata.json");
  public static final String WIKIPEDIA_BODY = resourceFileToString("/responses/wikipedia.json");
  public static final String COVER_ART_BODY =
      resourceFileToString("/responses/art_cover_archive.json");
  public static final String MUICS_BRAINZ_BODY =
      resourceFileToString("/responses/music_brainz.json");

  public static final String WIKIDATA_PATH = "/wiki/Special:EntityData/Q2831.json";
  public static final String WIKIPEDIA_PATH = "/api/rest_v1/page/summary/Michael%20Jackson";
  public static final String MUSIC_BRAINZ_PATH =
      "/.org/ws/2/artist/f27ec8db-af05-4f36-916e-3d57f91ecf5e?fmt=json&inc=url-rels+release-groups";
  public static final String COVER_ART_ARCHIVE_PATH =
      "/release-group/97e0014d-a267-33a0-a868-bb4e2552918a";

  public static final String JSON = MediaType.APPLICATION_JSON.toString();
  public static final String CONTENT_TYPE = HttpHeader.CONTENT_TYPE.asString();

  public static final WireMockServer MOCK_SERVER = new WireMockServer(wireMockConfig().port(1234));

  @Autowired public WebTestClient webTestClient;

  @BeforeEach
  void initClient() {
    initStubs();
    MOCK_SERVER.start();
  }

  @AfterAll
  static void afterAll() {
    MOCK_SERVER.stop();
  }

  public void initStubs() {
    stubGet(MUSIC_BRAINZ_PATH, MUICS_BRAINZ_BODY);
    stubGet(WIKIDATA_PATH, WIKIDATA_BODY);
    stubGet(WIKIPEDIA_PATH, WIKIPEDIA_BODY);
    stubGet(COVER_ART_ARCHIVE_PATH, COVER_ART_BODY);
  }

  private void stubGet(String testUrl, String body) {
    MOCK_SERVER.stubFor(
        get(urlEqualTo(testUrl))
            .willReturn(aResponse().withHeader(CONTENT_TYPE, JSON).withBody(body)));
  }

  public static String resourceFileToString(String file) {
    try {
      return IOUtils.toString(
          IOUtils.resourceToURL(
              file.charAt(0) == '/'
                  ? file
                  : '/' + file), // fix path for java.lang.Class.resolveName
          StandardCharsets.UTF_8);
    } catch (IOException e) {
      e.printStackTrace();
      return "{}";
    }
  }
}
