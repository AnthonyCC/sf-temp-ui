package com.freshdirect.webapp.ajax.expresscheckout.receipt.data;

import java.util.HashMap;
import java.util.Map;

public class SuccessPageData {

    private String header;
    private String rightBlock;
    private String orderId;
    private ReceiptData receipt;
    private String soName;
    private String soOrderDate;
    private boolean isOrderModifiable;
    private boolean hasSettledOrder=false;
    private String deliveryType;
    private String id;
	private String text;
	private String value;
	private Map<String, Object> other = new HashMap<String, Object>();

    // START SO 3.1 ACTIVATION SUCCESS PAGE

	private boolean soActivate=false;
    
    private String soFrequency;
   
    private Double soEstimatedTotal;
    
    private String soId;
    
    private String soDeliveryTime; // EEEE,MMMM d - hh pm - hh pm 
    
    private String soDeliveryDay; // EEEE 
    
    // END SO 3.1 ACTIVATION SUCCESS PAGE
    
    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getRightBlock() {
        return rightBlock;
    }

    public void setRightBlock(String rightBlock) {
        this.rightBlock = rightBlock;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public ReceiptData getReceipt() {
        return receipt;
    }

    public void setReceipt(ReceiptData receipt) {
        this.receipt = receipt;
    }

    public boolean isHasSettledOrder() {
		return hasSettledOrder;
	}

	public void setHasSettledOrder(boolean hasSettledOrder) {
		this.hasSettledOrder = hasSettledOrder;
	}
    
	public String getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	
    /**
     * @return the soName
     */
    public String getSoName() {
        return soName;
    }

    /**
     * @param soName
     *            the soName to set
     */
    public void setSoName(String soName) {
        this.soName = soName;
    }

    /**
     * @return the soOrderDate
     */
    public String getSoOrderDate() {
        return soOrderDate;
    }

    /**
     * @param soOrderDate
     *            the soOrderDate to set
     */
    public void setSoOrderDate(String soOrderDate) {
        this.soOrderDate = soOrderDate;
    }

    public boolean isOrderModifiable() {
        return isOrderModifiable;
    }

    public void setOrderModifiable(boolean isOrderModifiable) {
        this.isOrderModifiable = isOrderModifiable;
    }



	public boolean isSoActivate() {
		return soActivate;
	}

	public void setSoActivate(boolean soActivate) {
		this.soActivate = soActivate;
	}

	/**
	 * @return the soFrequency
	 */
	public String getSoFrequency() {
		return soFrequency;
	}

	/**
	 * @param soFrequency the soFrequency to set
	 */
	public void setSoFrequency(String soFrequency) {
		this.soFrequency = soFrequency;
	}

	/**
	 * @return the soEstimatedTotal
	 */
	public Double getSoEstimatedTotal() {
		return soEstimatedTotal;
	}

	/**
	 * @param soEstimatedTotal the soEstimatedTotal to set
	 */
	public void setSoEstimatedTotal(Double soEstimatedTotal) {
		this.soEstimatedTotal = soEstimatedTotal;
	}

	/**
	 * @return the soId
	 */
	public String getSoId() {
		return soId;
	}

	/**
	 * @param soId the soId to set
	 */
	public void setSoId(String soId) {
		this.soId = soId;
	}

	/**
	 * @return the soDeliveryTime
	 */
	public String getSoDeliveryTime() {
		return soDeliveryTime;
	}

	/**
	 * @param soDeliveryTime the soDeliveryTime to set
	 */
	public void setSoDeliveryTime(String soDeliveryTime) {
		this.soDeliveryTime = soDeliveryTime;
	}

	/**
	 * @return the soDeliveryDay
	 */
	public String getSoDeliveryDay() {
		return soDeliveryDay;
	}

	/**
	 * @param soDeliveryDay the soDeliveryDay to set
	 */
	public void setSoDeliveryDay(String soDeliveryDay) {
		this.soDeliveryDay = soDeliveryDay;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Map<String, Object> getOther() {
		return other;
	}

	public void setOther(Map<String, Object> other) {
		this.other = other;
	}
    
}
