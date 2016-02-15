package com.swapnilborkar.popularmovies;


public class PopularMovies {
    String movieName;
    String movieSummary;
    int moviePoster; //drawable reference id


    public PopularMovies(String movieName, String movieSummary, int moviePoster) {
        this.movieName = movieName;
        this.movieSummary = movieSummary;
        this.moviePoster = moviePoster;
    }
}
