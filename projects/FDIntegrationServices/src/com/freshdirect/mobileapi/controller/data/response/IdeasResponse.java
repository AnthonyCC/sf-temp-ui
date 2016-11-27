package com.freshdirect.mobileapi.controller.data.response;

import java.util.List;

import com.freshdirect.mobileapi.controller.data.Image;
import com.freshdirect.mobileapi.controller.data.Message;

public class IdeasResponse extends Message {
	
	private Image ideasBanner;
	private List<Idea> pickLists;
	private List<Idea> recipeLists; 
	private List<Idea> producers;

	public List<Idea> getPickLists() {
		return pickLists;
	}
	public void setPickLists(List<Idea> pickLists) {
		this.pickLists = pickLists;
	}
	public List<Idea> getRecipeLists() {
		return recipeLists;
	}
	public void setRecipeLists(List<Idea> recipeLists) {
		this.recipeLists = recipeLists;
	}
	public List<Idea> getProducers() {
		return producers;
	}
	public void setProducers(List<Idea> producers) {
		this.producers = producers;
	}
	public Image getIdeasBanner() {
		return ideasBanner;
	}
	public void setIdeasBanner(Image ideasBanner) {
		this.ideasBanner = ideasBanner;
	} 
}
