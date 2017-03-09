package com.sravan.ad.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HP on 3/8/2017.
 */

public class TMDBMovieReview implements Parcelable {
    private String id;
    private String author;
    private String content;
    private String url;

    public TMDBMovieReview(String id, String author, String content, String url, String movieId) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.url = url;

    }

    public TMDBMovieReview() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.author);
        dest.writeString(this.content);
        dest.writeString(this.url);

    }

    protected TMDBMovieReview(Parcel in) {
        this.id = in.readString();
        this.author = in.readString();
        this.content = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<TMDBMovieReview> CREATOR = new Parcelable.Creator<TMDBMovieReview>() {
        @Override
        public TMDBMovieReview createFromParcel(Parcel source) {
            return new TMDBMovieReview(source);
        }

        @Override
        public TMDBMovieReview[] newArray(int size) {
            return new TMDBMovieReview[size];
        }
    };
}
