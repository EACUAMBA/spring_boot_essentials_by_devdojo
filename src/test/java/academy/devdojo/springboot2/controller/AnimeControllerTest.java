package academy.devdojo.springboot2.controller;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.service.AnimeService;
import academy.devdojo.springboot2.util.AnimeCreator;
import academy.devdojo.springboot2.util.Utils;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class AnimeControllerTest {
    @InjectMocks
    private AnimeController animeController;

    @Mock
    private AnimeService animeServiceMock;

    @Mock
    private Utils utilsMock;

    @BeforeEach
    public void setUp() {
        Page<Anime> animePage = new PageImpl<>(Arrays.asList(AnimeCreator.createAnimeToBeSaved()));
        BDDMockito.when(animeServiceMock.listAll(ArgumentMatchers.any())).thenReturn(animePage);

        BDDMockito.when(animeServiceMock.findById(ArgumentMatchers.anyInt())).thenReturn(AnimeCreator.createValidAnime());

        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString())).thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeServiceMock.save(AnimeCreator.createAnimeToBeSaved())).thenReturn(AnimeCreator.createValidAnime());

        BDDMockito.doNothing().when(animeServiceMock).delete(ArgumentMatchers.anyInt());

        BDDMockito.doNothing().when(animeServiceMock).update(AnimeCreator.createValidAnime());
    }

    @Test
    @DisplayName("List All return a pageable list of animes when successful")
    public void listAll_ReturnListOfAnimeInsidePageObject_WhenSuccessful() {
        String expectedName = AnimeCreator.createAnimeToBeSaved().getName();

        Page<Anime> animePage = this.animeController.listAll(null,null).getBody();

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList()).isNotEmpty();
        Assertions.assertThat((animePage.toList()).get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("findById return an Anime when successful")
    public void findByID_ReturnAnAnimeObject_WhenSuccessful() {
        Integer expectedID = AnimeCreator.createValidAnime().getId();

        Anime animeReturned = this.animeController.findByID(1).getBody();

        Assertions.assertThat(animeReturned).isNotNull();
        Assertions.assertThat(animeReturned.getName()).isNotEmpty();
        Assertions.assertThat(animeReturned.getId()).isEqualTo(expectedID);

    }

    @Test
    @DisplayName("findByName return a List of Animes when successful")
    public void findByName_ReturnAListOfAnimeObject_WhenSuccessful() {
        String expectedName = AnimeCreator.createValidAnime().getName();

        List<Anime> animesReturned = this.animeController.findByName(expectedName).getBody();

        Assertions.assertThat(animesReturned).isNotNull();
        Assertions.assertThat(animesReturned.get(0)).isNotNull();
        Assertions.assertThat(animesReturned.get(0).getName()).isNotEmpty();
        Assertions.assertThat(animesReturned.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("save return an Anime when successful")
    public void save_ReturnAnAnimeObject_WhenSuccessful() {
        Integer expectedID = AnimeCreator.createValidAnime().getId();

        Anime animeSaved = AnimeCreator.createValidAnime();

        Anime animeReturned = this.animeController.save(AnimeCreator.createAnimeToBeSaved()).getBody();

        Assertions.assertThat(animeReturned).isNotNull();
        Assertions.assertThat(animeReturned.getName()).isNotEmpty();
        Assertions.assertThat(animeReturned).isEqualTo(animeSaved);
        Assertions.assertThat(animeReturned.getId()).isEqualTo(expectedID);

    }

    @Test
    @DisplayName("delete remove an Anime when successful")
    public void delete_RemoveAnAnimeObject_WhenSuccessful() {
        ResponseEntity<Void> voidResponseEntity = this.animeController.delete(1, null);

        Assertions.assertThat(voidResponseEntity).isNotNull();
        Assertions.assertThat(voidResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(voidResponseEntity.getBody()).isNull();

    }

    @Test
    @DisplayName("save Updated Anime  and return an Anime when successful")
    public void save_UpdatedAnimeAndReturnAnAnimeObject_WhenSuccessful() {
        Anime validUpdatedAnime = AnimeCreator.createValidUpdatedAnime();

        ResponseEntity<Void> voidResponseEntity = this.animeController.update(AnimeCreator.createValidAnime());

        Assertions.assertThat(voidResponseEntity).isNotNull();
        Assertions.assertThat(voidResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(voidResponseEntity.getBody()).isNull();

    }
}