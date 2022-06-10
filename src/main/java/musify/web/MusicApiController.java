package musify.web;

import musify.service.MusifyService;
import musify.response.MusifyResponseDto;
import musify.response.Response;
import musify.response.ResponseEntityDecorator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class MusicApiController {

  private final MusifyService musifyService;
  private final ResponseEntityDecorator responseEntityDecorator;

  @GetMapping("/musify/music-artist/details/{mbid}")
  public Mono<ResponseEntity<Response<MusifyResponseDto>>> musify(@PathVariable String mbid) {
    return musifyService
        .musify(mbid)
        .map(
            musifyResponseDto -> {
              var builder = responseEntityDecorator.ok();
              return builder.body(musifyResponseDto);
            });
  }
}
