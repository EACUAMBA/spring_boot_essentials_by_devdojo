package academy.devdojo.springboot2.integration;

import academy.devdojo.springboot2.controller.AnimeController;
import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.service.AnimeService;
import academy.devdojo.springboot2.util.AnimeCreator;
import academy.devdojo.springboot2.util.Utils;
import academy.devdojo.springboot2.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AnimeControllerIT {


//    @LocalServerPort
//    private int port;

    @Autowired
    @Qualifier(value = "testRestTemplateRoleUser")
    private TestRestTemplate testRestTemplateRoleUser;

    @Autowired
    @Qualifier(value = "testRestTemplateRoleAdmin")
    private TestRestTemplate testRestTemplateRoleAdmin;



    @MockBean
    private AnimeRepository animeRepository;

    @Lazy
    @TestConfiguration
    static class Config{

        @Bean(name = "testRestTemplateRoleUser")
        public TestRestTemplate testRestTemplateUserCreator(@Value("${local.server.port}") int port){
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port)
                    .basicAuthentication("devdojo", "12345");
            return new TestRestTemplate(restTemplateBuilder);
        }

        @Bean(name = "testRestTemplateRoleAdmin")
        public TestRestTemplate testRestTemplateAdminCreator(@Value("${local.server.port}") int port){
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port)
                    .basicAuthentication("eac", "12345");
            return new TestRestTemplate(restTemplateBuilder);
        }
    }

    @BeforeEach
    public void setUp() {
        BDDMockito.when(animeRepository.findAll()).thenReturn(List.of(AnimeCreator.createValidAnime()));

        Page<Anime> animePage = new PageImpl<>(Arrays.asList(AnimeCreator.createAnimeToBeSaved()));
        BDDMockito.when(animeRepository.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(animePage);

        BDDMockito.when(animeRepository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeRepository.findByName(ArgumentMatchers.anyString())).thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.doNothing().when(animeRepository).delete(ArgumentMatchers.any(Anime.class));/*The method in anime repository class has an argument an anime, so we will put an anime here*/

        BDDMockito.when(animeRepository.save(AnimeCreator.createAnimeToBeSaved())).thenReturn(AnimeCreator.createValidAnime());

    }

    @Test
    @DisplayName("List All return a pageable list of animes when successful")
    public void listAll_ReturnListOfAnimeInsidePageObject_WhenSuccessful() {
        String expectedName = AnimeCreator.createAnimeToBeSaved().getName();

        Page<Anime> animePage = this.testRestTemplateRoleUser.exchange("/animes", HttpMethod.GET, null, new ParameterizedTypeReference<PageableResponse<Anime>>() {}).getBody();

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList()).isNotEmpty();
        Assertions.assertThat((animePage.toList()).get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("findById return an Anime when successful")
    public void findByID_ReturnAnAnimeObject_WhenSuccessful() {
        Integer expectedID = AnimeCreator.createValidAnime().getId();

        Anime animeReturned = this.testRestTemplateRoleUser.exchange("/animes/1", HttpMethod.GET, null, new ParameterizedTypeReference<Anime>() {}).getBody();

        Assertions.assertThat(animeReturned).isNotNull();
        Assertions.assertThat(animeReturned.getName()).isNotEmpty();
        Assertions.assertThat(animeReturned.getId()).isEqualTo(expectedID);

    }

    @Test
    @DisplayName("findByName return a List of Animes when successful")
    public void findByName_ReturnAListOfAnimeObject_WhenSuccessful() {
        String expectedName = AnimeCreator.createValidAnime().getName();

        List<Anime> animesReturned = this.testRestTemplateRoleUser.exchange("/animes/find?name='dgb'", HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
        }).getBody();

        Assertions.assertThat(animesReturned).isNotNull();
        Assertions.assertThat(animesReturned.get(0)).isNotNull();
        Assertions.assertThat(animesReturned.get(0).getName()).isNotEmpty();
        Assertions.assertThat(animesReturned.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("save return an Anime when successful")
    public void save_ReturnAnAnimeObject_WhenSuccessful() {
        Integer expectedID = AnimeCreator.createValidAnime().getId();

        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();

        Anime animeReturned = this.testRestTemplateRoleAdmin.exchange("/animes/admin", HttpMethod.POST, getHttpEntityJson(animeToBeSaved), Anime.class).getBody();

        Assertions.assertThat(animeReturned).isNotNull();
        Assertions.assertThat(animeReturned.getName()).isNotEmpty();
        Assertions.assertThat(animeReturned.getId()).isEqualTo(expectedID);

    }

    @Test
    @DisplayName("delete remove an Anime when successful")
    public void delete_RemoveAnAnimeObject_WhenSuccessful() {
        ResponseEntity<Void> voidResponseEntity = this.testRestTemplateRoleAdmin.exchange("/animes/admin/1", HttpMethod.DELETE, null, Void.class);

        Assertions.assertThat(voidResponseEntity).isNotNull();
        Assertions.assertThat(voidResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(voidResponseEntity.getBody()).isNull();

    }

    @Test
    @DisplayName("save Updated Anime  and return an Anime when successful")
    public void save_UpdatedAnimeAndReturnAnAnimeObject_WhenSuccessful() {
        Anime validUpdatedAnime = AnimeCreator.createValidUpdatedAnime();

        ResponseEntity<Void> voidResponseEntity = this.testRestTemplateRoleAdmin.exchange("/animes/admin", HttpMethod.PUT, new HttpEntity<>(validUpdatedAnime), Void.class);

        Assertions.assertThat(voidResponseEntity).isNotNull();
        Assertions.assertThat(voidResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(voidResponseEntity.getBody()).isNull();

    }

    public HttpEntity getHttpEntityJson(Anime anime)
    {
        HttpEntity httpEntity = new HttpEntity(anime, getHttpHeaders());
        return httpEntity;
    }
    public HttpHeaders getHttpHeaders(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}
