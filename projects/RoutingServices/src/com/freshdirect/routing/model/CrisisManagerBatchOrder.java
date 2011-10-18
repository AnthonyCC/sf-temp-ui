package com.freshdirect.routing.model;

import java.util.Date;

import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.routing.constants.EnumDeliveryType;
import com.freshdirect.routing.constants.EnumReservationType;

public class CrisisManagerBatchOrder extends BaseModel implements ICrisisManagerBatchOrder, Comparable<CrisisManagerBatchOrder>  {
	
	private String batchId;
	private String firstName;
	private String lastName;	
	private String erpCustomerPK;
	private String fdCustomerPK;
	private Date deliveryDate;
	private Date cutOffTime;
	private String area;
	private Date startTime;
	private Date endTime;
	private String orderNumber;
	private String erpOrderNumber;
	private String email;
	private String amount;
	private EnumReservationType reservationType;
	private EnumSaleStatus orderStatus;
	private EnumDeliveryType deliveryType;
	private String homePhone;
	private String businessPhone;
	private String businessExt;
	private String cellPhone;
	private String standingOrderId;
	private boolean isException;
	private String addressId;
	private String companyName;
	private String reservationId;
	
	public CrisisManagerBatchOrder() {
		super();
	}
	
	public String getBatchId() {
		return batchId;
	}

	public String getArea() {
		return area;
	}	

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public Date getCutOffTime() {
		return cutOffTime;
	}

	public void setCutOffTime(Date cutOffTime) {
		this.cutOffTime = cutOffTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public EnumSaleStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(EnumSaleStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getErpOrderNumber() {
		return erpOrderNumber;
	}

	public void setErpOrderNumber(String erpOrderNumber) {
		this.erpOrderNumber = erpOrderNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public EnumReservationType getReservationType() {
		return reservationType;
	}

	public void setReservationType(EnumReservationType reservationType) {
		this.reservationType = reservationType;
	}	
	
	public EnumDeliveryType getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(EnumDeliveryType deliveryType) {
		this.deliveryType = deliveryType;
	}
	
	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getBusinessPhone() {
		return businessPhone;
	}

	public void setBusinessPhone(String businessPhone) {
		this.businessPhone = businessPhone;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}
	
	public String getStandingOrderId() {
		return standingOrderId;
	}

	public void setStandingOrderId(String standingOrderId) {
		this.standingOrderId = standingOrderId;
	}
	
	public boolean isException() {
		return isException;
	}

	public void setException(boolean isException) {
		this.isException = isException;
	}	

	public String getErpCustomerPK() {
		return erpCustomerPK;
	}

	public void setErpCustomerPK(String erpCustomerPK) {
		this.erpCustomerPK = erpCustomerPK;
	}

	public String getFdCustomerPK() {
		return fdCustomerPK;
	}
	
	public void setFdCustomerPK(String fdCustomerPK) {
		this.fdCustomerPK = fdCustomerPK;
	}
	
	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}
	
	public String getBusinessExt() {
		return businessExt;
	}

	public void setBusinessExt(String businessExt) {
		this.businessExt = businessExt;
	}
	
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getReservationId() {
		return reservationId;
	}

	public void setReservationId(String reservationId) {
		this.reservationId = reservationId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();			
		result = prime * result
				+ ((deliveryDate == null) ? 0 : deliveryDate.hashCode());		
		result = prime * result
				+ ((orderNumber == null) ? 0 : orderNumber.hashCode());		
		result = prime * result + ((orderStatus == null) ? 0 : orderStatus.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		CrisisManagerBatchOrder other = (CrisisManagerBatchOrder) obj;
	
		if (deliveryDate == null) {
			if (other.deliveryDate != null)
				return false;
		} else if (!deliveryDate.equals(other.deliveryDate))
			return false;		
		if (orderNumber == null) {
			if (other.orderNumber != null)
				return false;
		} else if (!orderNumber.equals(other.orderNumber))
			return false;		
		if (orderStatus == null) {
			if (other.orderStatus != null)
				return false;
		} else if (!orderStatus.equals(other.orderStatus))
			return false;
		return true;
	}

	@Override
	public int compareTo(CrisisManagerBatchOrder o) {
		// TODO Auto-generated method stub
		if(this.equals(o)) {
			return 0;
		} else {
			return this.toString().compareTo(o.toString());
		}
	}
	
	@Override
	public String toString() {
		return "OrderScenarioBatchOrder [batchId=" + batchId  + ", zone=" + area + ", deliveryDate="
				+ deliveryDate+ ", orderNumber="
				+ orderNumber+ ", erpOrderNumber="
				+ erpOrderNumber + "]";
	}
	
	
	
}
