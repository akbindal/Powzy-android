package com.powzyapp.powzy.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;



public class BusinessView implements Serializable {
	/**
	 * 
	 */

	public Long id;

	public String name;
	
	public String email;
	
	public String password;
	
	public String businessDescription;
	
	public Position location;
	
	public Set<Long> categories;
	
	public String isLove;
	
	public ContactDetail contactDetail;
	
	public OpeningTime openingTime;
	
	public String logoUrl;
	
	public String imageUrl;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBusinessDescription() {
		return businessDescription;
	}

	public void setBusinessDescription(String businessDescription) {
		this.businessDescription = businessDescription;
	}

	public Position getLocation() {
		return location;
	}

	public void setLocation(Position location) {
		this.location = location;
	}

	public Set<Long> getCategories() {
		return categories;
	}

	public void setCategories(Set<Long> categories) {
		this.categories = categories;
	}

	public String getIsLove() {
		return isLove;
	}

	public void setIsLove(String isLove) {
		this.isLove = isLove;
	}

	public ContactDetail getContactDetail() {
		return contactDetail;
	}

	public void setContactDetail(ContactDetail contactDetail) {
		this.contactDetail = contactDetail;
	}

	public OpeningTime getOpeningTime() {
		return openingTime;
	}

	public void setOpeningTime(OpeningTime openingTime) {
		this.openingTime = openingTime;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	
}
