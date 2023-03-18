package com.fokakefir.linkhub.model;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String docId;
    private String name;
    private String imageUrl;
    private List<String> followerIds;
    private List<String> followingIds;
    private List<String> placeIds;

    public User(String name, String imageUrl) {
        this.placeIds = new ArrayList<>();
        this.followerIds = new ArrayList<>();
        this.followingIds = new ArrayList<>();
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public User() {
    }

    @Exclude
    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    @Exclude
    public int getFollowerIdsSize() {
        return this.followerIds.size();
    }
    @Exclude
    public int getFollowingIdsSize() {
        return this.followingIds.size();
    }
    @Exclude
    public int getPlaceIdsSize() {
        return this.placeIds.size();
    }

    public List<String> getFollowerIds() {
        return followerIds;
    }

    public List<String> getFollowingIds() {
        return followingIds;
    }

    public List<String> getPlaceIds() {
        return placeIds;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
