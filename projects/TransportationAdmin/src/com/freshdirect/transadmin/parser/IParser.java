package com.freshdirect.transadmin.parser;

import java.util.List;

public interface IParser extends SynchronousParser {
	
	void parseFile(String filename);
	
	List getExceptions();
}
