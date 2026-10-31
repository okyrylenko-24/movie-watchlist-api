package com.example.moviewatchlist;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieRepository repository;

    public MovieController(MovieRepository repository) {
        this.repository = repository;
    }

    public record CreateMovieRequest(
            String title,
            String genre,
            Integer releaseYear
    ) {}

    public record WatchStatusRequest(Boolean watched) {}

    @GetMapping
    public List<Movie> getAllMovies() {
        return repository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Movie addMovie(@RequestBody CreateMovieRequest request) {
        if (request.title() == null || request.title().isBlank()
                || request.title().length() > 150
                || request.genre() == null || request.genre().isBlank()
                || request.genre().length() > 50
                || request.releaseYear() == null || request.releaseYear() < 1888) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid movie data"
            );
        }

        return repository.save(
                request.title().trim(),
                request.genre().trim(),
                request.releaseYear()
        );
    }

    @PatchMapping("/{id}/watched")
    public Movie updateWatched(
            @PathVariable("id") long id,
            @RequestBody WatchStatusRequest request) {

        if (request.watched() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Watched status is required"
            );
        }

        Movie movie = repository.updateWatched(id, request.watched());

        if (movie == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Movie not found"
            );
        }

        return movie;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovie(@PathVariable("id") long id) {
        if (!repository.deleteById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Movie not found"
            );
        }
    }
}
