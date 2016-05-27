package com.swapnilborkar.popularmovies.data;


import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

@Database(version = FavoritesDatabase.VERSION)
public final class FavoritesDatabase {

    @Table(FavoritesColumns.class)
    public static final String FAVORITES_TABLE = "favorites";
    static final int VERSION = 1;


    private FavoritesDatabase() {
    }


//
//    public static final String CONTENT_AUTHORITY ="com.swapnilborkar.popularmovies";
//    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
//    public static final String PATH_FAVOURITE = "favorites";
//
//    public static final class Favorites implements BaseColumns {
//
//
//        public static final String COLUMN_MOVIE_ID = "movie_id";
//        public static final String COLUMN_TITLE = "movie_title";
//        public static final String COLUMN_POSTER = "movie_poster";
//        public static final String COLUMN_SYNOPSIS = "movie_synopsis";
//        public static final String COLUMN_RATING = "movie_rating";
//        public static final String COLUMN_RELEASE_DATE = "movie_release";
//        public static final String COLUMN_POPULARITY = "movie_popularity";
//        public static final String COLUMN_BACKDROP = "back_drop";
//
//
//        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITE).build();
//
//        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+ "/"
//                + CONTENT_AUTHORITY + "/" + PATH_FAVOURITE;
//
//        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
//                + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITE;
//
//        public static Uri buildFavoriteUri (Long id){
//            return ContentUris.withAppendedId(CONTENT_URI, id);
//        }
//        public static String getFavoriteIdFromUri(Uri uri) {
//            return uri.getPathSegments().get(1);
//        }
//
//
}


