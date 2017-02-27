package com.example.latheeshvirupakshi.popularmoviestage2.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by latheeshvirupakshi on 2/16/17.
 */

public class Review {

    private String id;
    private String author;
    private String content;

    public Review() {

    }

    public Review(JSONObject review) throws JSONException {
        this.id = review.getString("id");
        this.author = review.getString("author");
        this.content = review.getString("content");
    }

    public String getId() { return id; }

    public String getAuthor() { return author; }

    public String getContent() { return content; }
}
