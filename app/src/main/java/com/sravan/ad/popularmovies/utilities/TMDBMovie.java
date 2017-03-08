package com.sravan.ad.popularmovies.utilities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sravan on 1/4/2017.
 */

public class TMDBMovie implements Parcelable {
    private String posterPath;
    private String overview;
    private String releaseDate;
    private String originalTitle;
    private String voteAverage;
    private String movieId;

    public TMDBMovie(String posterPath, String overview, String releaseDate, String[] genreIds, String id, String originalTitle, String title, String backdropPath, String voteAverage, String movieId) {
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.originalTitle = originalTitle;
        this.voteAverage = voteAverage;
        this.movieId = movieId;
    }

    public TMDBMovie() {
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.posterPath);
        dest.writeString(this.overview);
        dest.writeString(this.releaseDate);
        dest.writeString(this.originalTitle);
        dest.writeString(this.voteAverage);
        dest.writeString(this.movieId);
    }

    protected TMDBMovie(Parcel in) {
        this.posterPath = in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.originalTitle = in.readString();
        this.voteAverage = in.readString();
        this.movieId = in.readString();
    }

    public static final Parcelable.Creator<TMDBMovie> CREATOR = new Parcelable.Creator<TMDBMovie>() {
        @Override
        public TMDBMovie createFromParcel(Parcel source) {
            return new TMDBMovie(source);
        }

        @Override
        public TMDBMovie[] newArray(int size) {
            return new TMDBMovie[size];
        }
    };
}
