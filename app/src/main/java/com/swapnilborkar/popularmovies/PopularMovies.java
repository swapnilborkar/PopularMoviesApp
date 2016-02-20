package com.swapnilborkar.popularmovies;


public class PopularMovies {
    String imageUrl;
    int id;
    String title;
    String synopsis;
    String releaseDate;
    double rating;
    double popularity;


    public PopularMovies(String imageUrl, int id, String title, String synopsis, String releaseDate, double rating, double popularity) {
        this.imageUrl = imageUrl;
        this.id = id;
        this.title = title;
        this.synopsis = synopsis;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.popularity = popularity;

    }
}
