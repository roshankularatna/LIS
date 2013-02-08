package com.session;

import android.app.Application;
import android.graphics.Bitmap;

public class Sessions extends Application {

	private Bitmap	bmImage;
	private String search_item;
	private String description;
	private double longitude;
	private double latitude;
	
	public void setBmImage(Bitmap bmImage) {
		this.bmImage = bmImage;
	}
	public Bitmap getBmImage() {
		return bmImage;
	}
	public void setSearch_item(String search_item) {
		this.search_item = search_item;
	}
	public String getSearch_item() {
		return search_item;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLatitude() {
		return latitude;
	}
	
	
}
