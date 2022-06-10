package musify.web;

import Plugsurfing.com.musify.BasicTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class MusicApiControllerTest extends BasicTest {

  private static final String MBID = "f27ec8db-af05-4f36-916e-3d57f91ecf5e";
  private static final String NAME = "Michael Jackson";
  private static final String GENDER = "Male";
  private static final String COUNTRY = "US";
  private static final String DISAMBIGUATION = "“King of Pop”";
  private static final String ALBUM_TITLE = "Got to Be There";

  @Test
  public void musify() {
    webTestClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder.path("/api/v1/musify/music-artist/details/").path(MBID).build())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.data.mbid")
        .isEqualTo(MBID)
        .jsonPath("$.data.name")
        .isEqualTo(NAME)
        .jsonPath("$.data.gender")
        .isEqualTo(GENDER)
        .jsonPath("$.data.country")
        .isEqualTo(COUNTRY)
        .jsonPath("$.data.description")
        .isNotEmpty()
        .jsonPath("$.data.disambiguation")
        .isEqualTo(DISAMBIGUATION)
        .jsonPath("$.data.albums.length()")
        .isEqualTo(1)
        .jsonPath("$.data.albums[0].title")
        .isEqualTo(ALBUM_TITLE);
  }
}
