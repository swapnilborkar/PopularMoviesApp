package com.swapnilborkar.popularmovies.data;

import android.provider.BaseColumns;


public class MovieContract {

    public static final class Favorites implements BaseColumns {
        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_MOVIEID = "movie_id";
        public static final String COLUMN_TITLE = "movie_title";
        public static final String COLUMN_POSTER = "movie_poster";
        public static final String COLUMN_SYNOPSIS = "movie_synopsis";
        public static final String COLUMN_RATING = "movie_rating";
        public static final String COLUMN_RELEASEDATE = "movie_release";
        public static final String COLUMN_POPULARITY = "movie_popularity";
    }

}
