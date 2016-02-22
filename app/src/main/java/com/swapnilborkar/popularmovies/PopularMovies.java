package com.swapnilborkar.popularmovies;


import android.os.Parcel;
import android.os.Parcelable;

public class PopularMovies implements Parcelable {
    public static final Parcelable.Creator<PopularMovies> CREATOR = new Parcelable.Creator<PopularMovies>() {
        public PopularMovies createFromParcel(Parcel source) {
            return new PopularMovies(source);
        }

        public PopularMovies[] newArray(int size) {
            return new PopularMovies[size];
        }
    };
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

    protected PopularMovies(Parcel in) {
        this.imageUrl = in.readString();
        this.id = in.readInt();
        this.title = in.readString();
        this.synopsis = in.readString();
        this.releaseDate = in.readString();
        this.rating = in.readDouble();
        this.popularity = in.readDouble();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getRating() {
        return rating;
    }

    public double getPopularity() {
        return popularity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageUrl);
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.synopsis);
        dest.writeString(this.releaseDate);
        dest.writeDouble(this.rating);
        dest.writeDouble(this.popularity);
    }
}
