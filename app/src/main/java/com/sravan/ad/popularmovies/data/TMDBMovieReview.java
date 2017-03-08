package com.sravan.ad.popularmovies.data;

/**
 * Created by HP on 3/8/2017.
 */

public class TMDBMovieReview {
    String id;
    String author;
    String content;

    public TMDBMovieReview(String id, String author, String content) {
        this.id = id;
        this.author = author;
        this.content = content;
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

    public void setContent(String content) {
        this.content = content;
    }
}
