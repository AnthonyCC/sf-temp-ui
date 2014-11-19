package com.freshdirect.mobileapi.controller.data.response;

import java.util.List;

import com.freshdirect.mobileapi.controller.data.Image;
import com.freshdirect.mobileapi.controller.data.Message;

public class ProducerListResponse extends Message {
	Image bannerImage;
	private List<Idea> producers;
	public List<Idea> getProducers() {
		return producers;
	}
	public void setProducers(List<Idea> producers) {
		this.producers = producers;
	} 
}