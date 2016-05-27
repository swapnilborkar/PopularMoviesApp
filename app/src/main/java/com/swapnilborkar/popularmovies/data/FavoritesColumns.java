package com.swapnilborkar.popularmovies.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by SWAPNIL on 26-05-2016.
 */

public interface FavoritesColumns {

    @DataType(DataType.Type.INTEGER)
    @PrimaryKey
    @AutoIncrement
    String _ID = "_id";
    @DataType(DataType.Type.INTEGER)
    String COLUMN_MOVIE_ID = "movie_id";
    @DataType(DataType.Type.TEXT)
    String COLUMN_MOVIE_TITLE = "movie_title";
    @DataType(DataType.Type.TEXT)
    String COLUMN_MOVIE_POSTER = "movie_poster";
    @DataType(DataType.Type.TEXT)
    String COLUMN_MOVIE_BACKDROP = "movie_backdrop";
    @DataType(DataType.Type.TEXT)
    String COLUMN_MOVIE_SYNOPSIS = "movie_synopsis";
    @DataType(DataType.Type.TEXT)
    String COLUMN_MOVIE_RATING = "movie_rating";
    @DataType(DataType.Type.TEXT)
    String COLUMN_MOVIE_POPULARITY = "movie_popularity";
    @DataType(DataType.Type.TEXT)
    String COLUMN_MOVIE_RELEASE = "movie_release";


}
