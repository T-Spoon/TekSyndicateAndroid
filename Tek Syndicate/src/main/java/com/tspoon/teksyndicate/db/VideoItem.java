package com.tspoon.teksyndicate.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Items")
public class VideoItem extends Model implements DBItem {

	public static final String WATCH_URL = "http://gta-5-map.com/TekSyndicate/";
	
	@Column(name = "Title")
	public String title;

	@Column(name = "VideoToken", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	public String videoToken;

	@Column(name = "DatePublished")
	public String datePublished;

	@Column(name = "ImageURL")
	public String imageURL;

    @Column(name = "Viewed")
    public int itemViewed;

    @Column(name = "Position")
    public int position;

	public VideoItem(){
		super();
	}

	public VideoItem(String title, String videoToken, String datePublished, String imageURL, int itemViewed, int position) {
		super();
		this.title = title;
		this.videoToken = videoToken;
		this.datePublished = datePublished;
		this.imageURL = imageURL;
        this.itemViewed = itemViewed;
        this.position = position;
	}
	
	public static String getVideoURL(String token) {
		return WATCH_URL + token + ".mp4";
	}

    @Override
    public void setItemViewed(int viewed) {
        itemViewed = viewed;
    }
}
