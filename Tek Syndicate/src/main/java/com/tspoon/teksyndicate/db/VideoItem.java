package com.tspoon.teksyndicate.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Items")
public class VideoItem extends Model implements DBItem {

	public static final String WATCH_URL = "http://gta-5-map.com/TekSyndicate/";
	
	@Column(name = "VideoID", index = true)
	public int id;
	
	@Column(name = "Title")
	public String title;

	@Column(name = "VideoToken")
	public String videoToken;

	@Column(name = "DatePublished")
	public String datePublished;

	@Column(name = "ImageURL")
	public String imageURL;

    @Column(name = "Viewed")
    public int itemViewed;

	public VideoItem(){
		super();
	}

	public VideoItem(int id, String title, String videoToken, String datePublished, String imageURL, int itemViewed) {
		super();
		this.id = id;
		this.title = title;
		this.videoToken = videoToken;
		this.datePublished = datePublished;
		this.imageURL = imageURL;
        this.itemViewed = itemViewed;
	}
	
	public static String getVideoURL(String token) {
		return WATCH_URL + token + ".mp4";
	}

    @Override
    public void setItemViewed(int viewed) {
        itemViewed = viewed;
    }
}
