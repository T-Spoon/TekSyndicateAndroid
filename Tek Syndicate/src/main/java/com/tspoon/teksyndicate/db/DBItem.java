package com.tspoon.teksyndicate.db;

/**
 * Created by Tspoon on 25/03/2014.
 * - Quick workaround for using inheritance with ActiveAndroid - Not even sure if it's really needed
 */
public interface DBItem {
    // All classes that implement this interface should have the following properties
    // @Column(name = "Viewed")
    // public int itemViewed;

    public void setItemViewed(int viewed);
}
