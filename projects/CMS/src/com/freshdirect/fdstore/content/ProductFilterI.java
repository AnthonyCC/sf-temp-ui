package com.freshdirect.fdstore.content;

import java.util.Collection;

import com.freshdirect.fdstore.FDResourceException;

public interface ProductFilterI {
	public Collection apply(Collection nodes) throws FDResourceException;
}
