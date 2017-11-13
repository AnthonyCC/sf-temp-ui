package com.freshdirect.mobileapi.controller.data.response;

import java.util.List;

import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.response.OrderHistory.Order;

public class CreditHistory extends Message {

   private Integer totalResultCount = 0;
   private double remainingAmount = 0;

    public Integer getTotalResultCount() {
		return totalResultCount;
	}

	public void setTotalResultCount(Integer totalResultCount) {
		this.totalResultCount = totalResultCount;
	}
	
	public double getRemainingAmount() {
		return remainingAmount;
	}

	public void setRemainingAmount(double remainingAmount) {
		this.remainingAmount = remainingAmount;
	}

	public static class Credit{
		
		private String date;
	
	    private String type;
	
	    private String order;
	    
	    private String amount;
	    
	    private String estore;
	
	    
	    public String getEstore() {
			return estore;
		}

		public void setEstore(String estore) {
			this.estore = estore;
		}

		public String getDate() {
	        return date;
	    }
	
	    public void setDate(String date) {
	        this.date = date;
	    }
	
	    public String getType() {
	        return type;
	    }
	
	    public void setType(String type) {
	        this.type = type;
	    }
	    
	    public String getOrder() {
	        return order;
	    }
	
	    public void setOrder(String order) {
	        this.order = order;
	    }
	
	    public String getAmount() {
	        return amount;
	    }
	
	    public void setAmount(String amount) {
	        this.amount = amount;
	    }
	}
    
    private List<Credit> credits;
    
    public List<Credit> getCredits() {
        return credits;
    }

    public void setCredits(List<Credit> credits) {
        this.credits = credits;
    }
    
}
