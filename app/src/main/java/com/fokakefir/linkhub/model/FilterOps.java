package com.fokakefir.linkhub.model;

import java.util.ArrayList;

public class FilterOps {

    private ArrayList<String> kinds;
    private ArrayList<String> ratings;
    boolean isSorted;
    private String sortByThisField;

    public FilterOps(ArrayList<String> kinds, ArrayList<String> ratings, boolean isSorted, String sortByThisField) {
        this.kinds = kinds;
        this.ratings = ratings;
        this.isSorted = isSorted;
        this.sortByThisField = sortByThisField;
    }

    public FilterOps(){
        kinds = new ArrayList<>();
        ratings = new ArrayList<>();
        isSorted = false;
        sortByThisField = "";
    }

    public ArrayList<String> getKinds() {
        return kinds;
    }

    public void setKinds(ArrayList<String> kinds) {
        this.kinds = kinds;
    }

    public ArrayList<String> getRatings() {
        return ratings;
    }

    public void setRatings(ArrayList<String> ratings) {
        this.ratings = ratings;
    }

    public boolean isSorted() {
        return isSorted;
    }

    public void setSorted(boolean sorted) {
        isSorted = sorted;
    }

    public String getSortByThisField() {
        return sortByThisField;
    }

    public void setSortByThisField(String sortByThisField) {
        this.sortByThisField = sortByThisField;
    }
}
