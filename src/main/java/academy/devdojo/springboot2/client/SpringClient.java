package academy.devdojo.springboot2.client;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.wrapper.PageableResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
public class SpringClient {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Anime> animeResponseEntity = restTemplate.getForEntity("http://localhost:8080/animes/1", Anime.class);
        log.info("Your response using( .getForEntity) is: {}", animeResponseEntity);

        Anime anime = restTemplate.getForObject("http://localhost:8080/animes/1", Anime.class);
        log.info("Your response using (.getForObject) is: {}", anime);

//        /* How to return a array and collection */
//        Anime[] animesArray = restTemplate.getForObject("http://localhost:8080/animes/", Anime[].class);
//        log.info("The length of your array is (it's means that your array is charged) : {}", animesArray.length);

//        //@formatter:off
//        ResponseEntity<List<Anime>> animeListResponseEntity = restTemplate.exchange("http://localhost:8080/animes/", HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {});
//        //@formatter:on
//        log.info("The data in your collection is: {}", animeListResponseEntity.getBody());

        //@formatter:off
        ResponseEntity<PageableResponse<Anime>> animeListResponseEntity = restTemplate.exchange("http://localhost:8080/animes?page=0&size=5&sort=name,desc", HttpMethod.GET, null, new ParameterizedTypeReference<PageableResponse<Anime>>(){});
        //@formatter:on
        log.info("The data in your page is: {}", animeListResponseEntity.getBody());

    }
}
