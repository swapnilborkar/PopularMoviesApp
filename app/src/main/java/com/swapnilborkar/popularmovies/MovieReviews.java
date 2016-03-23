package com.swapnilborkar.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SWAPNIL on 11-03-2016.
 */
public class MovieReviews implements Parcelable {
    public static final Parcelable.Creator<MovieReviews> CREATOR = new Parcelable.Creator<MovieReviews>() {
        public MovieReviews createFromParcel(Parcel source) {
            return new MovieReviews(source);
        }

        public MovieReviews[] newArray(int size) {
            return new MovieReviews[size];
        }
    };
    String review_id;
    String author;
    String content;
    String url;

    public MovieReviews(String review_id, String author, String content, String url) {
        this.review_id = review_id;
        this.author = author;
        this.content = content;
        this.url = url;
    }

    protected MovieReviews(Parcel in) {
        this.review_id = in.readString();
        this.author = in.readString();
        this.content = in.readString();
        this.url = in.readString();
    }

    public String getReview_id() {
        return review_id;
    }

    public void setReview_id(String review_id) {
        this.review_id = review_id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.review_id);
        dest.writeString(this.author);
        dest.writeString(this.content);
        dest.writeString(this.url);
    }
}


