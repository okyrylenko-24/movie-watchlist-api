package com.example.moviewatchlist;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class MovieRepository {
    private final JdbcTemplate jdbc;

    public MovieRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<Movie> MOVIE_MAPPER = (rs, rowNum) ->
            new Movie(
                    rs.getLong("id"),
                    rs.getString("title"),
                    rs.getString("genre"),
                    rs.getInt("release_year"),
                    rs.getBoolean("watched")
            );

    public List<Movie> findAll() {
        return jdbc.query("SELECT * FROM movies ORDER BY id", MOVIE_MAPPER);
    }

    public Movie save(String title, String genre, int releaseYear) {
        return jdbc.queryForObject(
                "INSERT INTO movies (title, genre, release_year) VALUES (?, ?, ?) RETURNING *",
                MOVIE_MAPPER,
                title, genre, releaseYear
        );
    }

    public Movie updateWatched(long id, boolean watched) {
        List<Movie> movies = jdbc.query(
                "UPDATE movies SET watched = ? WHERE id = ? RETURNING *",
                MOVIE_MAPPER,
                watched, id
        );

        return movies.isEmpty() ? null : movies.get(0);
    }

    public boolean deleteById(long id) {
        return jdbc.update("DELETE FROM movies WHERE id = ?", id) > 0;
    }
}
