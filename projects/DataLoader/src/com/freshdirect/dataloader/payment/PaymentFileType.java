package com.freshdirect.dataloader.payment;

public enum PaymentFileType implements java.io.Serializable {
	
	DFR("Delimited file report",".dfr"),
	BIN("Bank Identification Number",".actbin"),
	SETTLEMENT_FAILURE("Settlement Failure",".CSV"),
	PAYPAL_SETTLEMENT("PayPal Settlement file", ".CSV");
		
	    private String name;
	    private String extension;
	    private PaymentFileType(String  name, String extension) {
	        this.name=name;
	        this.extension=extension;
	    }
	    public String toString() {
	        final StringBuilder sb = new StringBuilder();
	        sb.append(this.getClass().getName());
	        sb.append("{name=").append(name);
	        sb.append(", extension='").append(extension).append('\'');
	        sb.append('}');
	        return sb.toString();
	    }
	    public String getName() {
	        return name;
	    }
	    public String getExtension() {
		        return extension;
		}

}
