package academy.devdojo.springboot2.repository;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.util.AnimeCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("Anime Repository Test")
class AnimeRepositoryTest {

    @Autowired
    private AnimeRepository animeRepository;

    @Test
    @DisplayName("Save persist anime when successful")
    public void save_PersistAnime_WhenSuccessful() {
        Anime anime = AnimeCreator.createAnimeToBeSaved();

        Anime animeSaved = this.animeRepository.save(anime);

        Assertions.assertThat(animeSaved.getId()).isNotNull();
        Assertions.assertThat(animeSaved.getName()).isNotNull();
        Assertions.assertThat(animeSaved.getName()).isEqualTo(anime.getName());
    }

    @Test
    @DisplayName("Save Update anime when successful")
    public void save_UpdateAnime_WhenSuccessful() {
        Anime anime = AnimeCreator.createAnimeToBeSaved();

        Anime animeSaved = this.animeRepository.save(anime);
        animeSaved.setName("Aybo!");
        Anime animeUpdated = this.animeRepository.save(animeSaved);

        Assertions.assertThat(animeSaved.getId()).isNotNull();
        Assertions.assertThat(animeSaved.getName()).isNotNull();
        Assertions.assertThat(animeSaved.getUrl()).isNotNull();
        Assertions.assertThat(animeSaved.getId()).isEqualTo(animeUpdated.getId());
    }

    @Test
    @DisplayName("Save Update anime when successful")
    public void Delete_RemoveAnime_WhenSuccessful() {
        Anime anime = AnimeCreator.createAnimeToBeSaved();

        Anime animeSaved = this.animeRepository.save(anime);
        animeSaved.setName("Aybo!");
        this.animeRepository.delete(animeSaved);
        Optional<Anime> animeRepositoryById = this.animeRepository.findById(animeSaved.getId());
        Assertions.assertThat(animeSaved.getId()).isNotNull();
        Assertions.assertThat(animeSaved.getName()).isNotNull();
        Assertions.assertThat(animeSaved.getUrl()).isNotNull();
        Assertions.assertThat(animeRepositoryById.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("FindByName Find anime when successful")
    public void FindByName_ReturnAnimes_WhenSuccessful() {
        Anime anime = AnimeCreator.createAnimeToBeSaved();

        Anime animeSaved = this.animeRepository.save(anime);

        List<Anime> animeList = this.animeRepository.findByName(animeSaved.getName());

        Assertions.assertThat(animeList).contains(animeSaved);
        Assertions.assertThat(animeList).isNotEmpty();
    }

    @Test
    @DisplayName("FindByName Find anime return empty when anime doesn't exist")
    public void FindByName_ReturnEmpty_WhenAnimeDoesntExist() {
        Anime anime = AnimeCreator.createAnimeToBeSaved();

        Anime animeSaved = this.animeRepository.save(anime);

        List<Anime> animeList = this.animeRepository.findByName(animeSaved.getName() + "Jinga");
        Assertions.assertThat(animeList).isEmpty();
    }

    @Test
    @DisplayName("Save Throws ConstraintViolationException Name is Empty")
    public void  SaveThrow_ConstrainsViolationException_WhenNameIsEmpty() {
        Anime anime = AnimeCreator.createAnimeToBeSaved();
anime.setName("");
//        Assertions.assertThatThrownBy(()->this.animeRepository.save(anime)).isInstanceOf(ConstraintViolationException.class);

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(()->this.animeRepository.save(anime)).withMessageContaining("The name of the Anime can't be empty");

    }

    
}