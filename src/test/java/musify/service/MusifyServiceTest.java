package Plugsurfing.com.musify.service;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import Plugsurfing.com.musify.BasicTest;
import musify.clients.CoverArtArchiveClient;
import musify.clients.MusicBrainzClient;
import musify.clients.WikidataClient;
import musify.clients.WikipediaClient;
import musify.clients.responses.coverartarchive.ReleaseCoverDto;
import musify.clients.responses.musicbrainz.MusicBrainzDto;
import musify.clients.responses.wikidata.WikidataDto;
import musify.clients.responses.wikipedia.WikipediaDto;
import musify.service.MusifyService;
import musify.response.MusifyResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class MusifyServiceTest extends BasicTest {
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private static final String MBID = "f27ec8db-af05-4f36-916e-3d57f91ecf5e";
  private static final String NAME = "Michael Jackson";
  private static final String GENDER = "Male";
  private static final String COUNTRY = "US";
  private static final String DESCRIPTION = "Michael Joseph Jackson was an American singer";
  private static final String DISAMBIGUATION = "“King of Pop”";
  private static final String ALBUM_TITLE = "Got to Be There";
  private static final String ALBUM_ID = "97e0014d-a267-33a0-a868-bb4e2552918a";

  private static final String ENTITY = "Q2831";

  private MusicBrainzClient musicBrainzClient = mock(MusicBrainzClient.class);
  private WikidataClient wikidataClient = mock(WikidataClient.class);
  private WikipediaClient wikipediaClient = mock(WikipediaClient.class);
  private CoverArtArchiveClient coverArtArchiveClient = mock(CoverArtArchiveClient.class);

  @Test
  public void musify() throws JsonProcessingException {
    // Given
    clientsMocks();

    MusifyService service =
        new MusifyService(
            musicBrainzClient, wikidataClient, wikipediaClient, coverArtArchiveClient);
    // When
    Mono<MusifyResponseDto> musify = service.musify(MBID);
    // THEN
    StepVerifier.create(musify)
        .expectNextMatches(
            musifyResponseDto ->
                musifyResponseDto != null
                    && musifyResponseDto.getMbid().equals(MBID)
                    && musifyResponseDto.getName().equals(NAME)
                    && musifyResponseDto.getGender().equals(GENDER)
                    && musifyResponseDto.getCountry().equals(COUNTRY)
                    && musifyResponseDto.getDescription().contains(DESCRIPTION)
                    && musifyResponseDto.getDisambiguation().equals(DISAMBIGUATION)
                    && musifyResponseDto.getAlbums().get(0).getTitle().equals(ALBUM_TITLE))
        .verifyComplete();
  }

  private void clientsMocks() throws JsonProcessingException {
    MusicBrainzDto musicBrainzDto =
        OBJECT_MAPPER.readValue(MUICS_BRAINZ_BODY, MusicBrainzDto.class);
    when(musicBrainzClient.call(eq(MBID))).thenReturn(Mono.just(musicBrainzDto));

    WikidataDto wikidataDto = OBJECT_MAPPER.readValue(WIKIDATA_BODY, WikidataDto.class);
    when(wikidataClient.call(eq(ENTITY))).thenReturn(Mono.just(wikidataDto));

    WikipediaDto wikipediaDto = OBJECT_MAPPER.readValue(WIKIPEDIA_BODY, WikipediaDto.class);
    when(wikipediaClient.call(eq(NAME))).thenReturn(Mono.just(wikipediaDto));

    ReleaseCoverDto coverDto = OBJECT_MAPPER.readValue(COVER_ART_BODY, ReleaseCoverDto.class);
    when(coverArtArchiveClient.call(eq(ALBUM_ID))).thenReturn(Mono.just(coverDto));
  }
}
