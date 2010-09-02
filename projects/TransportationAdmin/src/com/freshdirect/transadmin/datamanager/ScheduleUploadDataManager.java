package com.freshdirect.transadmin.datamanager;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import java.util.ArrayList;
import java.util.Calendar;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.freshdirect.transadmin.datamanager.assembler.IDataAssembler;
import com.freshdirect.transadmin.datamanager.parser.ConfigurationReader;
import com.freshdirect.transadmin.datamanager.parser.FileFormat;
import com.freshdirect.transadmin.datamanager.parser.MatchedRecord;
import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormConfigurationValueException;
import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormConversionException;
import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormInputLineLengthException;
import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormInvalidRecordException;
import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormUnsetFieldValueException;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.util.TransportationAdminProperties;

public class ScheduleUploadDataManager extends ScheduleDataManager{
	
	public List processUploadSchedules(HttpServletRequest request, byte[] inputStream, String userName, DomainManagerI domainManagerService) {
		
		System.out.println(" #### Upload Process Start >"+Calendar.getInstance().getTime());		
	
		List updatedSchedules = new ArrayList();		
		updatedSchedules = parseEmployeeScheduleFile(TransportationAdminProperties.getEmployeeScheduleOutputFormat(), 
														new ByteArrayInputStream(inputStream), 
														ROW_IDENTIFIER, ROW_BEAN_IDENTIFIER, null, null);
		return updatedSchedules;	
	}
	
	
	public List parseEmployeeScheduleFile(String configurationPath,InputStream in, String recordName,String beanName, IDataAssembler assembler, String encoding) {
		
		ConfigurationReader parser = new ConfigurationReader();
		List inputList = new ArrayList();
        try {        	
            FileFormat ff = parser.loadConfigurationFile(configurationPath); 
                                              
            BufferedReader bufIn = null;
            if(encoding == null) {
            	bufIn =new BufferedReader(new InputStreamReader(in) );
            } else {
            	bufIn =new BufferedReader(new InputStreamReader(in, encoding) );
            }
           
            MatchedRecord results = null;
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
}
