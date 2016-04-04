package com.swapnilborkar.popularmovies;


import android.os.Parcel;
import android.os.Parcelable;

public class MovieTrailer implements Parcelable {
    public static final String PARCEL_TAG = "trailer_tag";
    public static final Parcelable.Creator<MovieTrailer> CREATOR = new Parcelable.Creator<MovieTrailer>() {
        @Override
        public MovieTrailer createFromParcel(Parcel source) {
            return new MovieTrailer(source);
        }

        @Override
        public MovieTrailer[] newArray(int size) {
            return new MovieTrailer[size];
        }
    };
    public String name;
    public String key;

    public MovieTrailer() {
    }

    protected MovieTrailer(Parcel in) {
        this.name = in.readString();
        this.key = in.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.key);
    }
}
