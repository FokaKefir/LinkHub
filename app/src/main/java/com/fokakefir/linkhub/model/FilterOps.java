package com.fokakefir.linkhub.model;

import java.util.ArrayList;

public class FilterOps {

    private ArrayList<String> kinds;
    private ArrayList<String> ratings;

    private ArrayList<String> exclude;


    boolean isSorted;
    private String sortByThisField;

    private static FilterOps instance;

    public static FilterOps getInstance(){
        if(instance == null){
            instance = new FilterOps();
        }
        return instance;
    }
    private FilterOps(){}


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
    public ArrayList<String> getExclude() {
        return exclude;
    }

    public void setExclude(ArrayList<String> exclude) {
        this.exclude = exclude;
    }
}
