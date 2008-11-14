package com.freshdirect.fdstore.content;

import java.util.Collection;
import java.util.List;

import com.freshdirect.fdstore.FDResourceException;

public interface ProductFilterI {
	public List apply(Collection nodes) throws FDResourceException;
}
