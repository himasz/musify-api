package musify;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import Plugsurfing.com.musify.BasicTest;
import musify.service.MusifyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ApplicationTests extends BasicTest {

  @Autowired private MusifyService service;

  @Test
  public void contextLoads() {
    assertNotNull(service);
  }
}
