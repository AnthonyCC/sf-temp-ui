package com.freshdirect.erp.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

import com.freshdirect.customer.EnumNotificationType;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.framework.core.PrimaryKey;

public class NotificationModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 499901870580395384L;
	
	private PrimaryKey sale_id;
	private EnumNotificationType notification_type;
	private EnumSaleStatus notification_status;
	private String third_party_name;
	private double amount;
	private PrimaryKey pk;
	private Date insertDate;
	
	
	public NotificationModel(PrimaryKey sale_id, EnumNotificationType notification_type, 
			EnumSaleStatus  notification_status, String third_party_name, double amount){
		this.sale_id = sale_id;
		this.notification_status = notification_status;
		this.notification_type = notification_type;
		this.third_party_name = third_party_name;
		this.amount = amount;
		this.insertDate = new Date();
	}
	
	
	 /** gets the primary key associated with a model
     * @return the primary key of the entity represented by this model
     */    
    public PrimaryKey getPK() {
        return this.pk!=null?this.pk:null;
    }
    
    /** sets the primary key for a model
     * @param pk the primary key to associate with this model
     */    
    public void setPK(PrimaryKey pk) {
        this.pk = pk;
    }
	
	public PrimaryKey getSale_id() {
		return sale_id;
	}
	
	public void setSale_id(PrimaryKey sale_id) {
		this.sale_id = sale_id;
	}
	
	public EnumNotificationType getNotification_type() {
		return notification_type;
	}
	
	public void setNotification_type(EnumNotificationType notification_type) {
		this.notification_type = notification_type;
	}
	
	public PrimaryKey getPk() {
		return pk;
	}

	public void setPk(PrimaryKey pk) {
		this.pk = pk;
	}

	public Date getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	public EnumSaleStatus getNotification_status() {
		return notification_status;
	}
	public void setNotification_status(EnumSaleStatus notification_status) {
		this.notification_status = notification_status;
	}

	public String getThird_party_name() {
		return third_party_name;
	}
	
	public void setThird_party_name(String third_party_name) {
		this.third_party_name = third_party_name;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public NotificationModel getModel(){
		     // do a serialization / de-serialization cycle as a trick
	        // against explicit deep cloning

	        // serialization
	        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
	        ObjectOutputStream    oas  = null;
	        try {
	            oas = new ObjectOutputStream(baos);
	            oas.writeObject(this);
	            oas.close();
	        } catch (IOException e) {
	            return null;
	        }
	        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
	        ObjectInputStream    oin = null;
	        try {
	            oin = new ObjectInputStream(bais);
	            return (NotificationModel) oin.readObject();
	        } catch (ClassNotFoundException e) {
	            return null;
	        } catch (IOException e) {
	            return null;
	        }
	}
	
}
