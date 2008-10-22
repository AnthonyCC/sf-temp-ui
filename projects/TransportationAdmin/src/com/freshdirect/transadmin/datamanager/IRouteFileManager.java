package com.freshdirect.transadmin.datamanager;

import java.io.InputStream;
import java.util.List;

import com.freshdirect.transadmin.datamanager.assembler.IDataAssembler;

public interface IRouteFileManager {
	
	List parseRouteFile(String configurationPath, InputStream in, String recordName,String beanName, IDataAssembler assembler);
	boolean generateRouteFile(String configurationPath, String outputFile, String recordName,String beanName, List data, IDataAssembler assembler);
	//boolean generateOrderFile(String outputFile, List data);

}
