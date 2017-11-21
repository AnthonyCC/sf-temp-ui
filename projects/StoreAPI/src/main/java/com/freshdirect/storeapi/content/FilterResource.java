package com.freshdirect.storeapi.content;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;

/**
 * A simple resource keeper class that retrieves resource on-demand.
 * 
 * @author segabor
 *
 * @param <T> Type of expected resource
 */
public abstract class FilterResource<T> {
	private T resource;
	
	/**
	 * Is retrieve attempted yet?
	 */
	private boolean attempted = false; 

	/**
	 * Try to retrieve the specified resource otherwise throw a {@link FDResourceException} exception.
	 * 
	 * @return The retrieved resource
	 * @throws FDResourceException
	 */
	public T getResource() throws FDResourceException {
		if (!attempted) {
			try {
				attempted = true;

				resource = obtainResource();
			} catch (Exception exc) {
				throw new FDResourceException(exc, "FilterResource: Failed to acquire resource ");
			}
		}

		if (resource == null) {
			throw new FDResourceException("Missing resource!");
		}

		return resource;
	}
	
	protected abstract T obtainResource() throws FDResourceException, FDSkuNotFoundException;
}
