package com.tspoon.teksyndicate.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;


@Table(name = "Categories")
public class Category extends Model {
	
	@Column(name = "CatID", index = true)
	public int catID;
	
	@Column(name = "Name", index = true)
	public String catName;
	
	public Category() {
		super();
	}

	public Category(int catID, String catName) {
		super();
		this.catID = catID;
		this.catName = catName;
	}
	
}
