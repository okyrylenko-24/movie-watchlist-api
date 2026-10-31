package com.example.moviewatchlist;

public record Movie(
        Long id,
        String title,
        String genre,
        Integer releaseYear,
        boolean watched
) {
}
