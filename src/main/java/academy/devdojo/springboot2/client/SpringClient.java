package academy.devdojo.springboot2.client;

import academy.devdojo.springboot2.domain.Anime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class SpringClient {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Anime> animeResponseEntity = restTemplate.getForEntity("http://localhost:8080/animes/1", Anime.class);
        log.info("Your response using( .getForEntity) is: {}", animeResponseEntity);

        Anime anime = restTemplate.getForObject("http://localhost:8080/animes/1", Anime.class);
        log.info("Your response using (.getForObject) is: {}", anime);
    }
}
