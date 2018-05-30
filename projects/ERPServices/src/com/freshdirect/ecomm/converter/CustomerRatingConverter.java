package com.freshdirect.ecomm.converter;

import com.freshdirect.customer.CustomerRatingI;
import com.freshdirect.ecommerce.data.customer.CustomerRatingAdapterData;
import com.freshdirect.ecommerce.data.dlv.ProfileData;
import com.freshdirect.fdstore.customer.ProfileModel;
import com.freshdirect.fdstore.customer.adapter.CustomerRatingAdaptor;

public class CustomerRatingConverter {


	public static CustomerRatingAdapterData buildCustomerRatingData(CustomerRatingI cra) {
		CustomerRatingAdapterData customerRatingData = new CustomerRatingAdapterData();
		CustomerRatingAdaptor ratingAdaptor = (CustomerRatingAdaptor) cra;
		customerRatingData.setCosCustomer(ratingAdaptor.isCosCustomer());
		customerRatingData.setProfile(buildProfileData(ratingAdaptor.getProfile()));
		customerRatingData.setValidOrderCount(ratingAdaptor.getValidOrderCount());
		return customerRatingData;
	}

	private static ProfileData buildProfileData(ProfileModel profile) {
		ProfileData profileData = new ProfileData();
		profileData.setId(profile.getId());
		return profileData;
	}

}
