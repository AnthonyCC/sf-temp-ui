package com.freshdirect.transadmin.datamanager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.fop.apps.Driver;
import org.apache.fop.apps.FOPException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.xml.sax.InputSource;

import com.freshdirect.transadmin.datamanager.assembler.IDataAssembler;
import com.freshdirect.transadmin.datamanager.parser.ConfigurationReader;
import com.freshdirect.transadmin.datamanager.parser.FileCreator;
import com.freshdirect.transadmin.datamanager.parser.FileFormat;
import com.freshdirect.transadmin.datamanager.parser.MatchedRecord;
import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormConfigurationValueException;
import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormConversionException;
import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormCreatorException;
import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormInputLineLengthException;
import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormInvalidRecordException;
import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormUnsetFieldValueException;
import com.freshdirect.transadmin.util.TransportationAdminProperties;



public class RouteFileManager implements IRouteFileManager {
	
	public List parseRouteFile(String configurationPath,InputStream in, String recordName,String beanName, IDataAssembler assembler) {
		
		ConfigurationReader parser = new ConfigurationReader();
		List inputList = new ArrayList();
        try {        	
            FileFormat ff = parser.loadConfigurationFile(configurationPath); 
                                              
            BufferedReader bufIn = new BufferedReader(new InputStreamReader(in,TransportationAdminProperties.getFileEncoding()) );
            //InputStreamReader defaultReader = new InputStreamReader(in);
            //BufferedReader bufIn = new BufferedReader(defaultReader );
            //System.out.println("defaultReader.getEncoding()"+defaultReader.getEncoding());
            MatchedRecord results;
            Object tmpBean = null;
            while ((results = ff.getNextRecord(bufIn)) != null) {
                if (results.getRecordName().equals(recordName)) {
                	tmpBean = results.getBean(beanName);
                	if(assembler != null) {
                		tmpBean = assembler.encode(tmpBean);
                	}
                	inputList.add(tmpBean);
                }
            }

        } catch (FlatwormUnsetFieldValueException flatwormUnsetFieldValueError) {
            flatwormUnsetFieldValueError.printStackTrace();  
        } catch (FlatwormConfigurationValueException flatwormConfigurationValueError) {
            flatwormConfigurationValueError.printStackTrace();
        } catch (FlatwormInvalidRecordException e) {
            e.printStackTrace(); 
        } catch (FlatwormInputLineLengthException e) {
            e.printStackTrace(); 
        } catch (FlatwormConversionException e) {
            e.printStackTrace(); 
        } catch (UnsupportedEncodingException  e) {
            e.printStackTrace(); 
        } 
        return inputList;
	}
	
	public boolean generateRouteFile(String configurationPath, String outputFile, String recordName,String beanName, List data, IDataAssembler assembler) {
			
        try {
        	FileCreator creator = new FileCreator(configurationPath, outputFile);
        	creator.setRecordSeperator(TransportationAdminProperties.getFileSeparator());
        	creator.open();
        	
        	if(data != null) {
        		Iterator iterator = data.iterator();
        		Object tmp = null;
	        	while(iterator.hasNext()) {
	        		tmp = iterator.next();
	        		if(assembler != null) {
	        			tmp = assembler.decode(tmp);
                	}
	                creator.setBean(beanName, tmp);
	                creator.write(recordName);
	    		}
        	}
        	
            // Close buffered output to write contents
            creator.close();
            return true;
        } catch (FlatwormCreatorException flatwormUnsetFieldValueError) {
            flatwormUnsetFieldValueError.printStackTrace();  
        } catch (IOException ioExp) {
        	ioExp.printStackTrace();  
        } 
        return false;
	}	
	
	public boolean generateReportFile(String outputFile, String data) {
		
        try {
        	BufferedWriter out = new BufferedWriter(new FileWriter(outputFile));
            out.write(data);
            out.close();

            return true;
        } catch (IOException ioExp) {
        	ioExp.printStackTrace();  
        } 
        return false;
	}
	
	public boolean generateReportFile(String outputFile,  HSSFWorkbook wb) {
		
        try {
        	FileOutputStream out = new FileOutputStream(new File(outputFile));
        	wb.write(out);
            out.close();

            return true;
        } catch (IOException ioExp) {
        	ioExp.printStackTrace();  
        } 
        return false;
	}
	
	public boolean generatePdfReportFile(OutputStream out,  String data) {
		
        try {
        	InputStream is = new ByteArrayInputStream(((String) data).getBytes("UTF-8"));
        	
        	ByteArrayOutputStream dataOut = new ByteArrayOutputStream();
            Driver driver = new Driver(new InputSource(is), dataOut);
            driver.setRenderer(Driver.RENDER_PDF);
            driver.run();
            
            //FileOutputStream out = new FileOutputStream(outputFile);
            //BufferedWriter out = new BufferedWriter(new FileWriter(outputFile));
            out.write(dataOut.toByteArray());
            out.close();

            return true;
        } catch (IOException ioExp) {
        	ioExp.printStackTrace();  
        } catch (FOPException fopExp) {
        	fopExp.printStackTrace();  
        } 
        return false;
	}

	
}
