package com.swapnilborkar.popularmovies.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;


@ContentProvider(authority = FavoritesProvider.AUTHORITY, database = FavoritesDatabase.class)
public class FavoritesProvider {

    public static final String AUTHORITY = "com.swapnilborkar.popularmovies.data.FavoritesProvider";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    interface Path {
        String FAVORITES = "favorites";
    }

    @TableEndpoint(table = FavoritesDatabase.FAVORITES_TABLE)
    public static class Favorites {
        @ContentUri(
                path = Path.FAVORITES,
                type = "vnd.android.cursor.dir/favorites",
                defaultSort = FavoritesColumns.COLUMN_MOVIE_POPULARITY + "DES")
        public static final Uri CONTENT_URI = buildUri(Path.FAVORITES);


        @InexactContentUri(
                name = "PLANET_ID",
                path = Path.FAVORITES + "/#",
                type = "vnd.android.cursor.item/favorites",
                whereColumn = FavoritesColumns._ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.FAVORITES, String.valueOf(id));
        }
    }

}
