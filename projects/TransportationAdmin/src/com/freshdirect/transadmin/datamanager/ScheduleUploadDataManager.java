package com.freshdirect.transadmin.datamanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import au.com.bytecode.opencsv.CSVReader;

import com.freshdirect.transadmin.constants.EnumUploadSource;
import com.freshdirect.transadmin.datamanager.assembler.IDataAssembler;
import com.freshdirect.transadmin.datamanager.parser.ConfigurationReader;
import com.freshdirect.transadmin.datamanager.parser.FileFormat;
import com.freshdirect.transadmin.datamanager.parser.MatchedRecord;
import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormConfigurationValueException;
import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormConversionException;
import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormInputLineLengthException;
import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormInvalidRecordException;
import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormUnsetFieldValueException;
import com.freshdirect.transadmin.model.ScheduleEmployee;
import com.freshdirect.transadmin.model.Scrib;
import com.freshdirect.transadmin.parser.BadDataException;
import com.freshdirect.transadmin.parser.ParserUtil;
import com.freshdirect.transadmin.util.TransStringUtil;

public class ScheduleUploadDataManager extends ScheduleDataManager{
	
	List<ScheduleEmployee> schedules;
	ScheduleEmployee record;
	
	List<Scrib> scribs;
	Scrib scribRecord;
	
	protected List<BadDataException> exceptions = null;

	public ScheduleUploadDataManager() {
		super();
		exceptions = new ArrayList<BadDataException>();
		schedules = new ArrayList<ScheduleEmployee>();
		scribs = new ArrayList<Scrib>();
	}

	public List<BadDataException> getExceptions() {
		return exceptions;
	}

	
	
	/**
	 * parser the file for the given file name
	 * 
	 * @param filename
	 *            the path to the file to parse
	 * @throws BadDataException
	 *             any unrecoverable problems found within the file. any other
	 *             exceptions are returned from this method.
	 * @return a list of exceptions encountered while parsing the file
	 */
	public void parseFile(InputStream inputStream, EnumUploadSource uploadSource) {
		
		try {
			CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
			String[] nextLine;
			int lineNumber = 0;

			while ((nextLine = reader.readNext()) != null) {
				++lineNumber;
				if (!ParserUtil.isEmpty(nextLine[0]) && lineNumber > 1) {
					try {
						processRecord(nextLine, uploadSource);
					} catch (BadDataException bde) {
						//
						// add exceptions to the exception list
						//
						exceptions.add(new BadDataException(bde,
								"Error at line " + lineNumber + " in file "
										+ ": " + bde.getMessage()));
					}
				}
			}
		} catch (IOException ioe) {
			exceptions.add(new BadDataException(ioe));
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException ioe) {
					exceptions.add(new BadDataException(ioe));
				}
			}
		}
	}
	
	
	/**
	 * parse a single line
	 * 
	 * @param recordLine
	 *            the String to parse
	 * @throws BadDataException
	 *             any problems found within the line.
	 */
	public void processRecord(String[] recordLine, EnumUploadSource uploadSource) throws BadDataException {
		if(EnumUploadSource.SCHEDULE.equals(uploadSource)) { 
			record = new ScheduleEmployee();
			try {			
				record.setDate(ParserUtil.trim(recordLine[0]) != null ? TransStringUtil.getDate(ParserUtil.trim(recordLine[0])) : null);
				record.setEmployeeId(ParserUtil.trim(recordLine[1]));
				record.setRegionS(ParserUtil.trim(recordLine[2]));
				record.setDispatchGroupS(ParserUtil.trim(recordLine[3]));
				record.setDepotFacilityS(ParserUtil.trim(recordLine[4]));			
			} catch(Exception e){
				throw new BadDataException(e);
			}
			schedules.add(record);
		} else if (EnumUploadSource.SCRIB.equals(uploadSource)) {
			scribRecord = new Scrib();
			try {
				scribRecord.setScribDate(ParserUtil.trim(recordLine[0]) != null ? TransStringUtil.getDate(ParserUtil.trim(recordLine[0])) : null);
				scribRecord.setFacilityS(ParserUtil.trim(recordLine[1]));
				scribRecord.setZoneS(ParserUtil.trim(recordLine[2]));
				scribRecord.setRegionS(ParserUtil.trim(recordLine[3]));
				scribRecord.setDispatchGroupS(ParserUtil.trim(recordLine[4]));
				scribRecord.setStartTimeS(ParserUtil.trim(recordLine[5]));
				scribRecord.setEndTimeS(ParserUtil.trim(recordLine[6]));
				scribRecord.setMaxReturnTimeS(ParserUtil.trim(recordLine[7]));
				scribRecord.setTruckCnt(ParserUtil.trim(recordLine[8]) != null ? ParserUtil.parseInt(ParserUtil.trim(recordLine[8])) : 0);
				scribRecord.setHandTruckCnt(ParserUtil.trim(recordLine[9]) != null ? ParserUtil.parseInt(ParserUtil.trim(recordLine[9])) : 0);
				scribRecord.setSupervisorCode(ParserUtil.trim(recordLine[10]));
				scribRecord.setCutOffTimeS(ParserUtil.trim(recordLine[11]));
				scribRecord.setEquipmentTypeS(ParserUtil.trim(recordLine[12]));
			} catch(Exception e){
				throw new BadDataException(e);
			}
			scribs.add(scribRecord);
		}
	}
	
	public List processUploadScrib(HttpServletRequest request, MultipartFile file, EnumUploadSource uploadSource) throws Exception {
		
		System.out.println(" #### Upload Scrib Process Start >"+Calendar.getInstance().getTime());		
		
		parseFile(file.getInputStream(), uploadSource);
		
		System.out.println(" #### Upload Process End >"+Calendar.getInstance().getTime());
		return scribs;	
	}
	
	public List processUploadSchedules(HttpServletRequest request, MultipartFile file, EnumUploadSource uploadSource) throws Exception {
		
		System.out.println(" #### Upload Schedule process Start >"+Calendar.getInstance().getTime());
		
		parseFile(file.getInputStream(), uploadSource);
		
		System.out.println(" #### Upload Process End >"+Calendar.getInstance().getTime());
		return schedules;	
	}
	
	
	public List parseFile(String configurationPath,InputStream in, String recordName,String beanName, IDataAssembler assembler, String encoding) {
		
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
}
