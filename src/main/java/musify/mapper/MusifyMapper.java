package musify.mapper;

import musify.clients.responses.musicbrainz.MusicBrainzDto;
import musify.clients.responses.musicbrainz.ReleaseGroupDto;
import musify.response.AlbumDto;
import musify.response.MusifyResponseDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MusifyMapper {
  MusifyMapper INSTANCE = Mappers.getMapper(MusifyMapper.class);

  @Mapping(target = "mbid", source = "musicBrainzDto.id")
  @Mapping(target = "name", source = "musicBrainzDto.name")
  @Mapping(target = "gender", source = "musicBrainzDto.gender")
  @Mapping(target = "country", source = "musicBrainzDto.country")
  @Mapping(target = "disambiguation", source = "musicBrainzDto.disambiguation")
  @Mapping(target = "description", source = "description")
  @Mapping(
      target = "albums",
      expression = "java( mapAlbums(musicBrainzDto.getReleaseGroups(), urls))")
  MusifyResponseDto map(
          MusicBrainzDto musicBrainzDto, String description, HashMap<String, String> urls);

  @Mapping(target = "imageUrl", source = "imageUrl")
  AlbumDto map(ReleaseGroupDto releaseGroupDto, String imageUrl);

  default List<AlbumDto> mapAlbums(
      List<ReleaseGroupDto> releaseGroups, HashMap<String, String> urls) {
    List<AlbumDto> albums = new ArrayList<>();
    releaseGroups.forEach(
        releaseGroupDto -> albums.add(map(releaseGroupDto, urls.get(releaseGroupDto.getId()))));
    return albums;
  }

  @Mapping(target = "id", source = "id")
  @Mapping(target = "title", source = "title")
  AlbumDto map(ReleaseGroupDto releaseGroup);

  List<AlbumDto> map(List<ReleaseGroupDto> releaseGroups);
}
