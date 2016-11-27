package com.freshdirect.transadmin.parser;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.transadmin.model.ScheduleEmployee;
import com.freshdirect.transadmin.util.TransStringUtil;

public class ScheduleCSVParser extends CSVFileParser implements IParser {
	
	SynchronousParserClient client;
	List<ScheduleEmployee> schedules;
	ScheduleEmployee record;
	
	public ScheduleCSVParser() {
		super();
		schedules = new ArrayList<ScheduleEmployee>();
	}
	
	@Override
    protected void makeObjects(String[] recordLine) throws BadDataException {
		
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
	}
	
	public void setClient(SynchronousParserClient client) {
		this.client = client;
	}

	public SynchronousParserClient getClient() {
		return this.client;
	}

	public List<ScheduleEmployee> getSchedules() {
		return schedules;
	}

	public void setSchedules(List<ScheduleEmployee> schedules) {
		this.schedules = schedules;
	}

 }
