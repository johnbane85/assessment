package vttp2022.ssf.assessment.services;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2022.ssf.assessment.models.News;
import vttp2022.ssf.assessment.repositories.NewsRepository;

@Service
public class NewsService {

  @Value("${API_KEY}")
  private String key;

  private String URL = "https://min-api.cryptocompare.com/data/v2/news/?lang=EN&api_key=" + key;

  @Autowired
  private NewsRepository newsRepo;

  public List<News> getArticles() {

    // Check if we have the news cached
    Optional<String> opt = newsRepo.get("news");
    String payload;

    // System.out.printf(">>> news: %s\n", opt);

    // Check if the box is empty
    if (opt.isEmpty()) {

      System.out.println("Getting news from cryptocompare.com");

      // Create the url with query string

      String url = UriComponentsBuilder.fromUriString(URL)
          // .queryParam("app_key", key)
          .toUriString();

      // Create the GET request, GET url
      RequestEntity<Void> req = RequestEntity.get(url).build();

      // Make the call to cryptocompare
      RestTemplate template = new RestTemplate();
      ResponseEntity<String> resp;

      try {
        // Throws an exception if status code not in between 200 - 399
        resp = template.exchange(req, String.class);
      } catch (Exception ex) {
        System.err.printf("Error: %s\n", ex.getMessage());
        return Collections.emptyList();
      }

      // Get the payload and do something with it
      payload = resp.getBody();
      // System.out.println("payload: " + payload);

      newsRepo.save("news", payload);
    } else {
      // Retrieve the value for the box
      payload = opt.get();
      // System.out.printf(">>>> cache: %s\n", payload);
    }

    // Convert payload to JsonObject

    // Convert the String to a Reader
    Reader strReader = new StringReader(payload);

    // Create a JsonReader from Reader
    JsonReader jsonReader = Json.createReader(strReader);

    // Read the payload as Json object
    JsonObject newsResult = jsonReader.readObject();
    JsonArray news = newsResult.getJsonArray("Data");
    List<News> list = new LinkedList<>();
    for (int i = 0; i < news.size(); i++) {
      // news[0]
      JsonObject jo = news.getJsonObject(i);
      list.add(News.create(jo));
    }
    return list;

  }

  public List<News> saveArticles() {

    Optional<String> opt = newsRepo.get("savedArticle");
    String payload;
    payload = opt.get();
    Reader strReader = new StringReader(payload);
    JsonReader jsonReader = Json.createReader(strReader);

    JsonObject newsResult = jsonReader.readObject();
    JsonArray news = newsResult.getJsonArray("Data");
    List<News> list = new LinkedList<>();
    for (int i = 0; i < news.size(); i++) {
      // news[0]
      JsonObject jo = news.getJsonObject(i);
      list.add(News.create(jo));
    }
    return list;
  }

  public Optional<News> getNewsById(String id) {
    return null;
  }

}
