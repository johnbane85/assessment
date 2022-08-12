package vttp2022.ssf.assessment.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttp2022.ssf.assessment.models.News;
import vttp2022.ssf.assessment.services.NewsService;

@RestController
@RequestMapping(path = "/news", produces = MediaType.APPLICATION_JSON_VALUE)
public class NewsRESTController {

  @Autowired
  private NewsService newsSvc;

  @GetMapping(value = "{id}")
  public ResponseEntity<String> getNews(@PathVariable String id) {
    Optional<News> opt = newsSvc.getNewsById(id);

    if (opt.isEmpty()) {
      JsonObject err = Json.createObjectBuilder()
          .add("error", "Cannot find news article %s".formatted(id))
          .build();
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(err.toString());
    }

    News news = opt.get();
    return ResponseEntity.ok(news.toJson().toString());
  }

}
