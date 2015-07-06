package com.freshdirect.webapp.ajax.checkout.data;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.webapp.ajax.AbstractCoremetricsResponse;
import com.freshdirect.webapp.ajax.product.data.CartLineData;
import com.freshdirect.webapp.ajax.product.data.ProductData;

public class UnavailabilityData extends AbstractCoremetricsResponse {
	
	public static class Line {
		private String availableQuantity;
		private CartLineData cartLine;
		private List<ProductData> recommendedProducts = new ArrayList<ProductData>();
		private String description;
		
		public String getAvailableQuantity() {
			return availableQuantity;
		}
		public void setAvailableQuantity(String availableQuantity) {
			this.availableQuantity = availableQuantity;
		}
		public List<ProductData> getRecommendedProducts() {
			return recommendedProducts;
		}
		public void setRecommendedProducts(List<ProductData> recommendedProducts) {
			this.recommendedProducts = recommendedProducts;
		}
		public CartLineData getCartLine() {
			return cartLine;
		}
		public void setCartLine(CartLineData cartLine) {
			this.cartLine = cartLine;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
	}
	
	
	private String deliveryDate;
	private String deliveryTimeSlot;
	private String notMetMinAmount; //contains an amount in case of a problem, else null 
	private List<UnavailabilityData.Line> replaceableLines = new ArrayList<UnavailabilityData.Line>();
	private List<UnavailabilityData.Line> nonReplaceableLines = new ArrayList<UnavailabilityData.Line>();
	private List<UnavailabilityData.Line> passes = new ArrayList<UnavailabilityData.Line>();
	
	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getNotMetMinAmount() {
		return notMetMinAmount;
	}

	public void setNotMetMinAmount(String notMetMinAmount) {
		this.notMetMinAmount = notMetMinAmount;
	}

	public String getDeliveryTimeSlot() {
		return deliveryTimeSlot;
	}

	public void setDeliveryTimeSlot(String deliveryTimeSlot) {
		this.deliveryTimeSlot = deliveryTimeSlot;
	}

	public List<UnavailabilityData.Line> getPasses() {
		return passes;
	}

	public void setPasses(List<UnavailabilityData.Line> passes) {
		this.passes = passes;
	}

	public List<UnavailabilityData.Line> getReplaceableLines() {
		return replaceableLines;
	}

	public void setReplaceableLines(List<UnavailabilityData.Line> replaceableLines) {
		this.replaceableLines = replaceableLines;
	}

	public List<UnavailabilityData.Line> getNonReplaceableLines() {
		return nonReplaceableLines;
	}

	public void setNonReplaceableLines(List<UnavailabilityData.Line> nonReplaceableLines) {
		this.nonReplaceableLines = nonReplaceableLines;
	}

}

