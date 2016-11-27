package com.freshdirect.fdstore.brandads.service;

import com.freshdirect.fdstore.brandads.model.HLBrandProductAdRequest;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("testing the Hooklogic");
		
		try {
				HLBrandProductAdRequest hLRequestData;
				HLBrandProductAdProviderFactory.getInstance().newService().getSearchbykeyword(null);
		} catch (BrandProductAdServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		}
	}

