package com.freshdirect.dataloader.autoorder.create.util;

public class Card {
        
        public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
		public Card (String num, String b) {
            this.number = num;
            this.brand = b;
        }
        
        public String number;
        public String brand;
        
    }