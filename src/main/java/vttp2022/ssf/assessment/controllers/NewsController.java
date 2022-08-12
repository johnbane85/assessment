package vttp2022.ssf.assessment.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import vttp2022.ssf.assessment.models.News;
import vttp2022.ssf.assessment.services.NewsService;

@Controller
@RequestMapping(path = { "/", "/articles" })
public class NewsController {

  @Autowired
  @Qualifier("redislab")
  private RedisTemplate<String, String> redisTemplate;

  @Autowired
  private NewsService newsSvc;

  @GetMapping(produces = { "text/html" })
  public String getNews(Model model) {
    List<News> news = newsSvc.getArticles();
    model.addAttribute("news", news);
    return "news";
  }

  @PostMapping
  public String postArticles(@RequestBody MultiValueMap<String, String> form,
      Model model) {

    List<News> news = newsSvc.getArticles();
    model.addAttribute("news", news);
    return "articles";

  }

}
