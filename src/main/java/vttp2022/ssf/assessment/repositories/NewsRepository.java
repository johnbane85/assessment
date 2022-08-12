package vttp2022.ssf.assessment.repositories;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class NewsRepository {

  @Autowired
  @Qualifier("redislab")
  private RedisTemplate<String, String> redisTemplate;

  public void save(String news, String payload) {
    ValueOperations<String, String> valueOp = redisTemplate.opsForValue();
    valueOp.set(news.toLowerCase(), payload);
  }

  public Optional<String> get(String news) {
    ValueOperations<String, String> valueOp = redisTemplate.opsForValue();
    String value = valueOp.get(news.toLowerCase());
    if (null == value)
      return Optional.empty(); // empty box
    return Optional.of(value); // box with data
  }

}
