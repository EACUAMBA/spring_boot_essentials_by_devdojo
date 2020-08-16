package academy.devdojo.springboot2.controller;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.service.AnimeService;
import academy.devdojo.springboot2.util.Utils;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("animes")
@RequiredArgsConstructor // Required constructor with arguments
public class AnimeController {

    private final Utils utils;
    private final AnimeService animeService;

//    @GetMapping()
//    public ResponseEntity<List<Anime>> listAll(){
//        log.info("Date Formatted {}", utils.formatLocalDateTimeToDateBaseStyle(LocalDateTime.now()));
//        return ResponseEntity.ok(animeService.listAll());
////        return new ResponseEntity<>(animeRepository.listAll(), HttpStatus.NOT_FOUND);
//    }

    @GetMapping()
    public ResponseEntity<Page<Anime>> listAll(Pageable pageable, @AuthenticationPrincipal UserDetails userDetaisl){
        log.info("Date Formatted {}", utils.formatLocalDateTimeToDateBaseStyle(LocalDateTime.now()));
        log.info("User Details: {}",userDetaisl);
        return ResponseEntity.ok(animeService.listAll(pageable));
//        return new ResponseEntity<>(animeRepository.listAll(), HttpStatus.NOT_FOUND);
    }


    @GetMapping(path = "/{id}")
    public ResponseEntity<Anime> findByID(@PathVariable int id){
        Anime anime = animeService.findById(id);
        return ResponseEntity.ok(anime);
    }

    @GetMapping(path = "/find")
    public ResponseEntity<List<Anime>> findByName(@RequestParam(value = "name", required = false)String name){
        return ResponseEntity.ok(animeService.findByName(name));
    }

    @PostMapping(path = "/admin")
    public ResponseEntity<Anime> save(@RequestBody @Valid Anime anime){
        return ResponseEntity.ok(animeService.save(anime));
    }

    @DeleteMapping(path = "/admin/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id ,@AuthenticationPrincipal UserDetails userDetaisl){
        log.info("User Details: {}",userDetaisl);
        animeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(path = "/admin")
    public ResponseEntity<Void> update(@RequestBody Anime anime){
        animeService.update(anime);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
