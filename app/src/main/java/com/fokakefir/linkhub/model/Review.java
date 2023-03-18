package com.fokakefir.linkhub.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

public class Review {

    private String documentId;
    private String authorId;
    private String author;
    private String comment;
    private String imageUrl;
    private int rate;
    private Timestamp timestamp;

    public Review(String authorId, String author, String comment, String imageUrl, int rate, Timestamp timestamp) {
        this.authorId = authorId;
        this.author = author;
        this.comment = comment;
        this.imageUrl = imageUrl;
        this.rate = rate;
        this.timestamp = timestamp;
    }

    public Review() {
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
