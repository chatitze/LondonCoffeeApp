package com.chatitzemoumin.londoncoffeeapp.model;

import java.util.List;

/**
 * Place class; model class
 * 
 */
public class Place {

	private String id;

	private String name;
	
	private double latitue;
	
	private double longitude;

	private double rating;
	
	private String icon;
	
	private List<String> types;
	
	private String vicinity;

	private int priceLevel;

	private String openningHours;
	
	private double distance;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public List<String> getTypes() {
		return types;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}

	public String getVicinity() {
		return vicinity;
	}

	public void setVicinity(String vicinity) {
		this.vicinity = vicinity;
	}

	public int getPriceLevel() {
		return priceLevel;
	}

	public void setPriceLevel(int priceLevel) {
		this.priceLevel = priceLevel;
	}

	public String getOpenningHours() {
		return openningHours;
	}

	public void setOpenningHours(String openningHours) {
		this.openningHours = openningHours;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getLatitue() {
		return latitue;
	}

	public void setLatitue(double latitue) {
		this.latitue = latitue;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

}

