package academy.devdojo.springboot2.client;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.wrapper.PageableResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.temporal.TemporalField;
import java.util.List;

@Slf4j
public class SpringClient {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        deleteRestTemplateExchange(restTemplate);
    }
    private  static void deleteRestTemplateExchange(RestTemplate restTemplate){
        ResponseEntity<Void> voidResponseEntity = restTemplate.exchange("http://localhost:8080/animes/{id}", HttpMethod.DELETE, null, Void.class, new Integer(5));
        log.info("The status of the delete is: {}", voidResponseEntity);
    }
    private static void putRestTemplateExchange(RestTemplate restTemplate){
        Anime animeToPut = Anime.builder().id(2).name("Naruto").url("http://narutoTV.jp").build();
        ResponseEntity<Void> voidResponseEntity = restTemplate.exchange("http://localhost:8080/animes", HttpMethod.PUT, new HttpEntity<>(animeToPut), Void.class);
        log.info("The status of the request is: " + voidResponseEntity.getStatusCode().value());
    }
    private static void postRestTemplateExchange(RestTemplate restTemplate) {
        Anime animeToPost = Anime.builder().name("Suzuki").url("http://suzukiTV.org").build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Anime> animePosted = restTemplate.exchange("http://localhost:8080/animes/", HttpMethod.POST, new HttpEntity<>(animeToPost, httpHeaders), Anime.class);
        log.info("The anime posted is: {}", animePosted);
    }

    private static void postRestTemplate(RestTemplate restTemplate) {
        Anime animeToPost = Anime.builder().name("One peace").url("http://google.com").build();
        Anime animePosted = restTemplate.postForObject("http://localhost:8080/animes/", animeToPost, Anime.class);
        log.info("The anime saved is: {}", animePosted);
    }

    private static void getRestTampletOneObject(RestTemplate restTemplate) {

        ResponseEntity<Anime> animeResponseEntity = restTemplate.getForEntity("http://localhost:8080/animes/1", Anime.class);
        log.info("Your response using( .getForEntity) is: {}", animeResponseEntity);

        Anime anime = restTemplate.getForObject("http://localhost:8080/animes/1", Anime.class);
        log.info("Your response using (.getForObject) is: {}", anime);
    }

    private static void getRestTampletOneOrMoreObject(RestTemplate restTemplate) {
        /* How to return a array and collection */
        Anime[] animesArray = restTemplate.getForObject("http://localhost:8080/animes/", Anime[].class);
        log.info("The length of your array is (it's means that your array is charged) : {}", animesArray.length);

        //@formatter:off
        ResponseEntity<List<Anime>> animeListResponseEntity = restTemplate.exchange("http://localhost:8080/animes/", HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {});
        //@formatter:on
        log.info("The data in your collection is: {}", animeListResponseEntity.getBody());
    }

    private static void getRestTemplatePageable(RestTemplate restTemplate) {
        //@formatter:off
        ResponseEntity<PageableResponse<Anime>> animeListResponseEntity = restTemplate.exchange("http://localhost:8080/animes?page=0&size=5&sort=name,desc", HttpMethod.GET, null, new ParameterizedTypeReference<PageableResponse<Anime>>(){});
        //@formatter:on
        log.info("The data in your page is: {}", animeListResponseEntity.getBody());
    }
}
