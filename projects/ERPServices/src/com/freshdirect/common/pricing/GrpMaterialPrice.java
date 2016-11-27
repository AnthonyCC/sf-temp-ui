package com.freshdirect.common.pricing;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class GrpMaterialPrice extends MaterialPrice {

	private final static NumberFormat FORMAT_CURRENCY = NumberFormat.getCurrencyInstance(Locale.US);
	private final static DecimalFormat FORMAT_QUANTITY = new java.text.DecimalFormat("0.##");

	public String getGrpId() {
		return grpId;
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public String getLongDesc() {
		return longDesc;
	}

	private String grpId;
	private String shortDesc;
	private String longDesc;	
	
	public GrpMaterialPrice(String grpId,String longDesc, String shortDesc, double price, String pricingUnit, double scaleLowerBound){		
		super(price, pricingUnit, scaleLowerBound, Double.POSITIVE_INFINITY, pricingUnit, 0.0);
		this.grpId=grpId;
		this.shortDesc=shortDesc;
		this.longDesc=longDesc;
	}
	
	
	/*
	public String getScaleDisplay(double savingsPercentage) {
		StringBuffer buf = new StringBuffer();
		
		buf.append( " SAVE! Any " );

		//check for a grpId, and link to group page
		if (!"".equals(this.grpId)) {
			buf.append( "<a href=\"/group.jsp?grpId="+this.grpId+"\">" );
		}
		
		buf.append( FORMAT_QUANTITY.format( this.getScaleLowerBound() ) );
		buf.append( " " );
		buf.append( this.shortDesc );
		buf.append( " for " );
		buf.append( FORMAT_CURRENCY.format( this.getPrice() * this.getScaleLowerBound() ) );
		
		if (!"".equals(this.grpId)) {
			buf.append( "</a>" );
		}
		
		return buf.toString();
	}
	
	public String getScaleDisplay() {
		StringBuffer buf = new StringBuffer();
		
		buf.append( " SAVE! Any " );
		
		//check for a grpId, and link to group page
		if (!"".equals(this.grpId)) {
			buf.append( "<a href=\"/group.jsp?grpId="+this.grpId+"\">" );
		}
		
		buf.append( FORMAT_QUANTITY.format( this.getScaleLowerBound() ) );
		buf.append( " " );
		buf.append( this.shortDesc );
		buf.append( " for " );
		buf.append( FORMAT_CURRENCY.format( this.getPrice() * this.getScaleLowerBound() ) );
		
		if (!"".equals(this.grpId)) {
			buf.append( "</a>" );
		}
		
		return buf.toString();
	}
	*/
}
