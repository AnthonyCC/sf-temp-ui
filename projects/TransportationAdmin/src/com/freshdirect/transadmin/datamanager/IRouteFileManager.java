package com.freshdirect.transadmin.datamanager;

import java.io.InputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.freshdirect.transadmin.datamanager.assembler.IDataAssembler;

public interface IRouteFileManager {
	
	List parseRouteFile(String configurationPath, InputStream in, String recordName,String beanName, IDataAssembler assembler);
	
	List parseRouteFile(String configurationPath, InputStream in, String recordName,String beanName, IDataAssembler assembler, String encoding);

	boolean generateRouteFile(String configurationPath, String outputFile, String recordName,String beanName, List data, IDataAssembler assembler);

	boolean generateReportFile(String outputFile, String data);
	
	boolean generateReportFile(String outputFile,  HSSFWorkbook wb);

}
