package com.sravan.ad.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HP on 3/9/2017.
 */

public class TMDBMovieTrailer implements Parcelable {
    private String id;
    private String key;
    private String name;
    private String site;
    private String size;
    private String type;

    public TMDBMovieTrailer(String id, String key, String name, String site, String size, String type) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
    }

    public TMDBMovieTrailer() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTrailerUrl() {
        return "http://www.youtube.com/watch?v=" + key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.key);
        dest.writeString(this.name);
        dest.writeString(this.site);
        dest.writeString(this.size);
        dest.writeString(this.type);
    }

    protected TMDBMovieTrailer(Parcel in) {
        this.id = in.readString();
        this.key = in.readString();
        this.name = in.readString();
        this.site = in.readString();
        this.size = in.readString();
        this.type = in.readString();
    }

    public static final Parcelable.Creator<TMDBMovieTrailer> CREATOR = new Parcelable.Creator<TMDBMovieTrailer>() {
        @Override
        public TMDBMovieTrailer createFromParcel(Parcel source) {
            return new TMDBMovieTrailer(source);
        }

        @Override
        public TMDBMovieTrailer[] newArray(int size) {
            return new TMDBMovieTrailer[size];
        }
    };
}
