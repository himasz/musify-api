package musify.config;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MusifyApiProperties {
  @Autowired private Environment env;

  public String getMusicBrainzUrl() {
    return getProperty("music_brainz_url");
  }

  public String getCoverArtArchiveUrl() {
    return getProperty("cover_art_archive_url");
  }

  public String getWikidataUrl() {
    return getProperty("wikidata_url");
  }

  public String getWikipediaUrl() {
    return getProperty("wikipedia_url");
  }

  private String getProperty(String key) {
    return Optional.ofNullable(System.getenv(key)).orElseGet(() -> env.getProperty(key));
  }
}
