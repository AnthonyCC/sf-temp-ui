package com.freshdirect.fdstore.services.tax;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.services.tax.data.CancelTaxRequest;
import com.freshdirect.fdstore.services.tax.data.CancelTaxResult;
import com.freshdirect.fdstore.services.tax.data.GeoTaxResult;
import com.freshdirect.fdstore.services.tax.data.GetTaxRequest;
import com.freshdirect.fdstore.services.tax.data.GetTaxResult;
import com.freshdirect.fdstore.services.tax.data.Line;

/*
 * @author Nakkeeran Annamalai
 */
public class AvalaraTaxService {
	private static final Logger LOGGER = Logger.getLogger(AvalaraTaxService.class);
	public GetTaxResult getTax(GetTaxRequest request)
	{
		String taxget = "/1.0/tax/get";
		for(Line line:request.getLines()){
			LOGGER.info("Tax Code= "+line.getTaxCode());
		}
		return FDTaxUtil.execute(taxget, request, GetTaxResult.class);
	}
	
	public CancelTaxResult cancelTax(CancelTaxRequest request)
	{
		String taxget = "/1.0/tax/cancel";
		return FDTaxUtil.execute(taxget, request, CancelTaxResult.class);
	}
	
	public GeoTaxResult estimateTax(Double latitude, Double longitude, Double saleAmount)
	{
		String taxest = "/1.0/tax/" + latitude.toString() + "," + longitude.toString() + "/get?saleamount=" + saleAmount.toString();
		return FDTaxUtil.execute(taxest, null, GeoTaxResult.class);
	}
    
	public GeoTaxResult Ping() {
		return estimateTax(47.627935,-122.51702,10.0);
	};
}