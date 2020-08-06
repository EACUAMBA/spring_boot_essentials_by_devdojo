package academy.devdojo.springboot2.service;

import academy.devdojo.springboot2.controller.AnimeController;
import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.exception.ResourceNotFoundException;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.util.AnimeCreator;
import academy.devdojo.springboot2.util.Utils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class AnimeServiceTest {
    @InjectMocks
    private AnimeService animeService;

    @Mock
    private Utils utilsMock;

    @Mock
    private AnimeRepository animeRepository;

    @BeforeEach
    public void setUp() {
        BDDMockito.when(animeRepository.findAll()).thenReturn(List.of(AnimeCreator.createValidAnime()));

        Page<Anime> animePage = new PageImpl<>(Arrays.asList(AnimeCreator.createAnimeToBeSaved()));
        BDDMockito.when(animeRepository.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(animePage);

        BDDMockito.when(animeRepository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeRepository.findByName(ArgumentMatchers.anyString())).thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.doNothing().when(animeRepository).delete(ArgumentMatchers.any(Anime.class));/*The method in anime repository class has an argument an anime, so we will put an anime here*/

        BDDMockito.when(animeRepository.save(AnimeCreator.createAnimeToBeSaved())).thenReturn(AnimeCreator.createValidAnime());

        BDDMockito.when(utilsMock.findAnimeOrThrowNotFound(ArgumentMatchers.anyInt(), ArgumentMatchers.any(AnimeRepository.class))).thenReturn(AnimeCreator.createValidAnime());


    }
    @Test
    @DisplayName("List All return a  list of animes when successful")
    public void listAll_ReturnListOfAnimeObject_WhenSuccessful() {
        String expectedName = AnimeCreator.createAnimeToBeSaved().getName();

        List<Anime> animePage = this.animeService.listAll();

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage).isNotEmpty();
        Assertions.assertThat((animePage).get(0).getName()).isEqualTo(expectedName);

    }
    @Test
    @DisplayName("List All return a pageable list of animes when successful")
    public void listAll_ReturnListOfAnimeInsidePageObject_WhenSuccessful() {
        String expectedName = AnimeCreator.createAnimeToBeSaved().getName();

        Page<Anime> animePage = this.animeService.listAll(PageRequest.of(1,1));

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList()).isNotEmpty();
        Assertions.assertThat((animePage.toList()).get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("findById return an Anime when successful")
    public void findByID_ReturnAnAnimeObject_WhenSuccessful() {
        Integer expectedID = AnimeCreator.createValidAnime().getId();

        Anime animeReturned = this.animeService.findById(1);

        Assertions.assertThat(animeReturned).isNotNull();
        Assertions.assertThat(animeReturned.getName()).isNotEmpty();
        Assertions.assertThat(animeReturned.getId()).isEqualTo(expectedID);

    }

    @Test
    @DisplayName("findByName return a List of Animes when successful")
    public void findByName_ReturnAListOfAnimeObject_WhenSuccessful() {
        String expectedName = AnimeCreator.createValidAnime().getName();

        List<Anime> animesReturned = this.animeService.findByName(expectedName);

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

        Anime animeReturned = this.animeService.save(AnimeCreator.createAnimeToBeSaved());

        Assertions.assertThat(animeReturned).isNotNull();
        Assertions.assertThat(animeReturned.getName()).isNotEmpty();
        Assertions.assertThat(animeReturned).isEqualTo(animeSaved);
        Assertions.assertThat(animeReturned.getId()).isEqualTo(expectedID);

    }

    @Test
    @DisplayName("delete remove an Anime when successful")
    public void delete_RemoveAnAnimeObject_WhenSuccessful() {
        Assertions.assertThatCode(()->this.animeService.delete(1)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("delete Throws ResourceNotFoundException(\"Anime Not Found\") an Anime when successful")
    public void delete_ThrowsResourceNotFoundException_WhenDoesNotFindAnime() {
        BDDMockito.when(utilsMock.findAnimeOrThrowNotFound(ArgumentMatchers.anyInt(), ArgumentMatchers.any(AnimeRepository.class))).thenThrow(new ResourceNotFoundException("Anime not Found"));
        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(()->this.animeService.delete(1));
    }
    @Test
    @DisplayName("save Updated Anime  and return an Anime when successful")
    public void save_UpdatedAnimeAndReturnAnAnimeObject_WhenSuccessful() {
        Assertions.assertThatCode(()->this.animeService.update(AnimeCreator.createValidAnime())).doesNotThrowAnyException();
    }
}