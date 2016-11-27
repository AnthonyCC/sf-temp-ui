package com.freshdirect.mobileapi.controller.data.response;

import java.util.List;
import java.util.Map;

import com.freshdirect.mobileapi.controller.data.Image;
import com.freshdirect.mobileapi.controller.data.Message;

public class ProducerDetailResponse extends Message {
	private String producerTitle; 
	private String producerText;
	private Image producerBanner;
	private List<Image> producerImages;
	private Map<String, Image> otherImages;
	public String getProducerTitle() {
		return producerTitle;
	}
	public void setProducerTitle(String producerTitle) {
		this.producerTitle = producerTitle;
	}
	public String getProducerText() {
		return producerText;
	}
	public void setProducerText(String producerText) {
		this.producerText = producerText;
	}
	public Image getProducerBanner() {
		return producerBanner;
	}
	public void setProducerBanner(Image producerBanner) {
		this.producerBanner = producerBanner;
	}
	public List<Image> getProducerImages() {
		return producerImages;
	}
	public void setProducerImages(List<Image> producerImages) {
		this.producerImages = producerImages;
	}
	public Map<String, Image> getOtherImages() {
		return otherImages;
	}
	public void setOtherImages(Map<String, Image> otherImages) {
		this.otherImages = otherImages;
	} 
}