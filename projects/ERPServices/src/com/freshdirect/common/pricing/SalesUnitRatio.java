/*
 * $Workfile: SalesUnitRatio.java$
 *
 * $Date: 8/15/2001 4:48:36 PM$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.common.pricing;

import java.io.Serializable;

/**
 * Class capturing the pricing related info from a sales unit.
 *
 * @version $Revision: 2$
 * @author $Author: Viktor Szathmary$
 */
public class SalesUnitRatio implements Serializable {
	/** Alternate unit of measure */
	private String alternateUnit;

	/** Sales unit of measure */
	private String salesUnit;

	/** Conversion ratio from alternate unit to sales unit */
	private double ratio;

	public SalesUnitRatio(String alternateUnit, String salesUnit, double ratio) {
		this.alternateUnit=alternateUnit;
		this.salesUnit=salesUnit;
		this.ratio=ratio;
	}

	public String getAlternateUnit() {
		return this.alternateUnit;
	}

	public String getSalesUnit() {
		return this.salesUnit;
	}

	public double getRatio() {
		return this.ratio;
	}

	public String toString() {
		return "SalesUnitRatio["+alternateUnit+" -> "+salesUnit+" "+ratio+"]";
	}
	
}
