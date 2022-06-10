package musify.service;

import musify.clients.MusicBrainzClient;
import musify.error.WikidataException;
import musify.clients.CoverArtArchiveClient;
import musify.clients.WikidataClient;
import musify.clients.WikipediaClient;
import musify.clients.responses.coverartarchive.ReleaseCoverDto;
import musify.clients.responses.musicbrainz.RelationDto;
import musify.clients.responses.wikidata.WikidataDto;
import musify.clients.responses.wikipedia.WikipediaDto;
import musify.mapper.MusifyMapper;
import musify.response.AlbumDto;
import musify.response.MusifyResponseDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@AllArgsConstructor
public class MusifyService {

  private static final String WIKIDATA = "wikidata";
  public static final String FRONT = "Front";

  private final MusicBrainzClient musicBrainzClient;
  private final WikidataClient wikidataClient;
  private final WikipediaClient wikipediaClient;
  private final CoverArtArchiveClient coverArtArchiveClient;

  public Mono<MusifyResponseDto> musify(String mbid) {
    MusifyResponseDto.MusifyResponseDtoBuilder builder = MusifyResponseDto.builder();

    return callMusicBrainz(mbid, builder)
        .flatMap(wikidataEntityId -> callWikipedia(wikidataEntityId))
        .map(wikipediaDto -> builder.description(wikipediaDto.getExtract()))
        .flatMap(musifyResponseDtoBuilder -> getAlbumsWithCovers(musifyResponseDtoBuilder))
        .map(albums -> builder.albums(albums).build())
        .onErrorReturn(builder.build());
  }

  private Mono<WikipediaDto> callWikipedia(String wikidataEntityId) {
    return wikidataClient
        .call(wikidataEntityId)
        .flatMap(wikidataDto -> wikipediaClient.call(getTitle(wikidataDto, wikidataEntityId)))
        .onErrorReturn(WikipediaDto.builder().extract("").build());
  }

  private Mono<List<AlbumDto>> getAlbumsWithCovers(
      MusifyResponseDto.MusifyResponseDtoBuilder musifyResponseDtoBuilder) {
    return Flux.fromIterable(musifyResponseDtoBuilder.build().getAlbums())
        .flatMap(
            album ->
                coverArtArchiveClient
                    .call(album.getId())
                    .map(releaseCoverDto -> createAlbum(releaseCoverDto, album)))
        .collectList();
  }

  private AlbumDto createAlbum(ReleaseCoverDto releaseCoverDto, AlbumDto album) {
    return AlbumDto.builder()
        .id(album.getId())
        .title(album.getTitle())
        .imageUrl(getImageUrl(releaseCoverDto))
        .build();
  }

  private Mono<String> callMusicBrainz(
      String mbid, MusifyResponseDto.MusifyResponseDtoBuilder builder) {
    return musicBrainzClient
        .call(mbid)
        .map(
            musicBrainzDto -> {
              builder
                  .mbid(mbid)
                  .name(musicBrainzDto.getName())
                  .gender(musicBrainzDto.getGender())
                  .country(musicBrainzDto.getCountry())
                  .disambiguation(musicBrainzDto.getDisambiguation())
                  .albums(MusifyMapper.INSTANCE.map(musicBrainzDto.getReleaseGroups()));
              return getWikidataEntityId(musicBrainzDto.getRelations());
            });
  }

  private String getWikidataEntityId(List<RelationDto> relations) {
    for (RelationDto relation : relations) {
      if (relation.getType().equals(WIKIDATA)) {
        String[] resourceParts = relation.getUrl().getResource().split("/");
        return resourceParts[resourceParts.length - 1];
      }
    }
    throw new WikidataException("Could not find Wikidata Entity.");
  }

  private String getTitle(WikidataDto wikidataDto, String wikidataEntityId) {
    return wikidataDto.getEntities().get(wikidataEntityId).getSitelinks().getEnwiki().getTitle();
  }

  private String getImageUrl(ReleaseCoverDto releaseCoverDto) {
    return releaseCoverDto.getImages().stream()
        .filter(
            coverImageDto ->
                coverImageDto.getTypes().contains(FRONT) || coverImageDto.getTypes().isEmpty())
        .findAny()
        .get()
        .getImage();
  }
}
