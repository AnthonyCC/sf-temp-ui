package com.freshdirect.dataloader.geocodefilter;

import java.io.IOException;
import java.util.List;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.dataloader.SynchronousParser;

public interface IParser extends SynchronousParser {
	void parseFile(String filename);
	List getExceptions();		
}
