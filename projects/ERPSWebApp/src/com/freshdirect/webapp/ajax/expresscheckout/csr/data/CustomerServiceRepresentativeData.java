package com.freshdirect.webapp.ajax.expresscheckout.csr.data;

import java.util.List;

import com.freshdirect.customer.ErpComplaintReason;

public class CustomerServiceRepresentativeData {

    private boolean isDeliveryForceWaived;
    private boolean isPremiumDeliveryForceWaived;
    private boolean isPhoneHandlingChargeWaived;
    private boolean isEmailInvoiceSendingSkipped;
    private String customerInvoiceMessage;
    private double phoneCharge;
    private double deliveryCharge;
    private double deliveryChargePremium;
    private List<ErpComplaintReason> complaintReasons;

    public boolean isDeliveryForceWaived() {
        return isDeliveryForceWaived;
    }

    public void setDeliveryForceWaived(boolean isDeliveryForceWaived) {
        this.isDeliveryForceWaived = isDeliveryForceWaived;
    }

    public boolean isPremiumDeliveryForceWaived() {
        return isPremiumDeliveryForceWaived;
    }

    
    public void setPremiumDeliveryForceWaived(boolean isPremiumDeliveryForceWaived) {
        this.isPremiumDeliveryForceWaived = isPremiumDeliveryForceWaived;
    }

    public boolean isPhoneHandlingChargeWaived() {
        return isPhoneHandlingChargeWaived;
    }

    public void setPhoneHandlingChargeWaived(boolean isPhoneHandlingChargeWaived) {
        this.isPhoneHandlingChargeWaived = isPhoneHandlingChargeWaived;
    }

    public boolean isEmailInvoiceSendingSkipped() {
        return isEmailInvoiceSendingSkipped;
    }

    public void setEmailInvoiceSendingSkipped(boolean isEmailInvoiceSendingSkipped) {
        this.isEmailInvoiceSendingSkipped = isEmailInvoiceSendingSkipped;
    }

    public String getCustomerInvoiceMessage() {
        return customerInvoiceMessage;
    }

    public void setCustomerInvoiceMessage(String customerInvoiceMessage) {
        this.customerInvoiceMessage = customerInvoiceMessage;
    }

    public double getPhoneCharge() {
        return phoneCharge;
    }

    public void setPhoneCharge(double phoneCharge) {
        this.phoneCharge = phoneCharge;
    }
    
    public double getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(double deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public double getDeliveryChargePremium() {
        return deliveryChargePremium;
    }

    
    public void setDeliveryChargePremium(double deliveryChargePremium) {
        this.deliveryChargePremium = deliveryChargePremium;
    }

    public List<ErpComplaintReason> getComplaintReasons() {
        return complaintReasons;
    }

    public void setComplaintReasons(List<ErpComplaintReason> complaintReasons) {
        this.complaintReasons = complaintReasons;
    }

}
