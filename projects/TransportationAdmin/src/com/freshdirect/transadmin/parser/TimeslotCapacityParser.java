package com.freshdirect.transadmin.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.web.multipart.MultipartFile;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.routing.model.IDeliveryWindowMetrics;
import com.freshdirect.routing.model.TimeslotCapacityModel;
import com.freshdirect.routing.service.proxy.DeliveryServiceProxy;
import com.freshdirect.transadmin.util.TransStringUtil;

public class TimeslotCapacityParser {

	private static Category LOGGER = LoggerFactory.getInstance(TimeslotCapacityParser.class);
	
	/**
	 * list of exceptions occurred during parsing
	 */
	List exceptionList = null;
	List<TimeslotCapacityModel> timeslotMetrics = null;
	boolean isTimeslotMetricsValid = true;
	List<TimeslotCapacityModel> timeslotsNotInUploadFileWithException = null;
	

	public TimeslotCapacityParser() {
		super();
		this.exceptionList = new ArrayList();
		this.timeslotMetrics = new ArrayList<TimeslotCapacityModel>();
		timeslotsNotInUploadFileWithException = new ArrayList<TimeslotCapacityModel>();
	}

	/**
	 * indicates whether any parsing errors were found
	 * 
	 * @return true if no exceptions occurred during loading
	 */
	public boolean parseSuccessful() {
		return (this.exceptionList.size() == 0);
	}

	/** @return list of BadDataExceptions */
	public List getParsingExceptions() {
		return this.exceptionList;
	}
		
	public List<TimeslotCapacityModel> getTimeslotMetrics() {
		return timeslotMetrics;
	}

	public void setTimeslotMetrics(List<TimeslotCapacityModel> timeslotMetrics) {
		this.timeslotMetrics = timeslotMetrics;
	}

	/**
	 * indicates whether any time slot metrics errors were found
	 * 
	 * @return true if no exceptions occurred during loading
	 */
	public boolean metricsSuccessful() {
		return isTimeslotMetricsValid;
	}

	/** loads and parses all of the information from inputed file
	 * @throws BadDataException any unrecoverable errors encountered during loading
	 */
	@SuppressWarnings("unchecked")
	public void parseFile(MultipartFile file) {

		LOGGER.info("----- Parse Timeslot Capacity Metrics starting...");
		if (file != null) {
			try {				
				POIFSFileSystem fs = new POIFSFileSystem(new ByteArrayInputStream(file.getBytes()));
				HSSFWorkbook workbook = new HSSFWorkbook(fs);
				HSSFFormulaEvaluator evaluator = new HSSFFormulaEvaluator(workbook);
				HSSFDataFormatter formatter = new HSSFDataFormatter();
				formatter.setDefaultNumberFormat(new DecimalFormat());
				HSSFSheet sheet = workbook.getSheetAt(0);
				
				int rowCount = sheet.getLastRowNum() + 1;

				Row firstRow = sheet.getRow(2);

				if (firstRow == null) {
					this.exceptionList.add(new BadDataException("Header row not found (maybe empty)"));
				}
				
				TimeslotCapacityModel _tsCapacityModel = null;
				for (int iCurrentRow = 3; iCurrentRow < rowCount; iCurrentRow++) {
					HSSFRow row = sheet.getRow(iCurrentRow);					
					if(row == null)	{
						this.exceptionList.add(new BadDataException("Row #"+ row.getRowNum() + " is empty"));
					} else {						
						int cellIndex = 0;
						int noOfCells = row.getPhysicalNumberOfCells();	
						String[] cellValues = new String[noOfCells]; 
						short firstCellNum = row.getFirstCellNum();
						short lastCellNum = row.getLastCellNum();

						if (firstCellNum >= 0 && lastCellNum >= 0) {
							for (short iCurrent = firstCellNum; iCurrent < lastCellNum; iCurrent++) {
								Cell cell = row.getCell(iCurrent);
								if (cell == null) {
									this.exceptionList.add(new BadDataException("Empty data found at Row #"+ row.getRowNum() + " Cell #"+ iCurrent));
									cellIndex++;
									continue;
								} else {									
									cellValues[cellIndex] = getCellStringValue(cell, cell.getCellType());
									cellIndex++;
								}
							}
						}
						_tsCapacityModel = new TimeslotCapacityModel();
						
						_tsCapacityModel.setBaseDate(HSSFDateUtil.getJavaDate(Double.parseDouble(cellValues[0])));
						_tsCapacityModel.setArea(cellValues[1]);
						if(cellValues[1] != null && cellValues[2].length() > 0)
							_tsCapacityModel.setStartTime(TransStringUtil.getDatewithTime(TransStringUtil.MASTER_DATE+" "+cellValues[2].substring(0, cellValues[2].indexOf("-"))));
						if(cellValues[1] != null && cellValues[2].length() > 0)
							_tsCapacityModel.setEndTime(TransStringUtil.getDatewithTime(TransStringUtil.MASTER_DATE+" "+cellValues[2].substring(cellValues[2].indexOf("-")+1, cellValues[2].length())));
						_tsCapacityModel.setCapacity(Integer.parseInt(cellValues[3]));
						_tsCapacityModel.setChefsTableCapacity(Integer.parseInt(cellValues[4]));
						_tsCapacityModel.setPremiumCapacity(Integer.parseInt(cellValues[5]));
						_tsCapacityModel.setPremiumCtCapacity(Integer.parseInt(cellValues[6]));
						
						this.timeslotMetrics.add(_tsCapacityModel);
					}
				}
				
			} catch (IOException e) {
				LOGGER.error("error during parsing XLS file: " + file.getOriginalFilename() , e);
				this.exceptionList.add(new BadDataException("Error during parsing XLS file: " + file.getOriginalFilename()) + e.getMessage());
			} catch (Exception e) {
				LOGGER.error("error reading XLS file: " + file.getOriginalFilename(), e);
				this.exceptionList.add(new BadDataException("Error during reading XLS file: " + file.getOriginalFilename()) + e.getMessage());
				
			}
		}
		
		LOGGER.info("----- Parsing Timeslot Capacity metrics complete -----");
	}
	
	@SuppressWarnings("unchecked")
	private String getCellStringValue(Cell cell, int type) {
		String stringValue = "";
		switch (type) {
		case Cell.CELL_TYPE_BLANK:
			break;
		case Cell.CELL_TYPE_STRING:
			stringValue = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			stringValue = Boolean.toString(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_NUMERIC:
			double value = cell.getNumericCellValue();
			if (HSSFDateUtil.isCellDateFormatted(cell))	{
				if (HSSFDateUtil.isValidExcelDate(value)) {																		
					stringValue = Double.toString(cell.getNumericCellValue());
				} else {
					this.exceptionList
						.add(new BadDataException(
								"Invalid Date value found at row # "
										+ cell.getRow().getRowNum()
										+ " and column # "
										+ cell.getColumnIndex()));
				}
			} else {
				stringValue = Integer.toString((int)cell.getNumericCellValue());
			}			
			break;
		case Cell.CELL_TYPE_ERROR:
			this.exceptionList.add(new BadDataException("XLS error in row " + cell.getRow().getRowNum() + " at cell " + cell.getColumnIndex()
					+ ": error type: " + Byte.toString(cell.getErrorCellValue())));
			break;
		default:
			this.exceptionList.add(new BadDataException("Error in row " + cell.getRow().getRowNum() + " at cell " + cell.getColumnIndex()
					+ ": unknown data type: " + cell.getCellType() + ", upgrade POI"));
		}
		return stringValue;
	}
	
	public void validateTimeslotMetrics() {
		
		DeliveryServiceProxy deliveryProxy = new DeliveryServiceProxy();
		
		// Delivery Date - Zone - TimeslotMetrics list
		Map<Date, Map<String, List<IDeliveryWindowMetrics>>> slotsByDateZoneMapping = new HashMap<Date, Map<String,List<IDeliveryWindowMetrics>>>();
		
		// Delivery Date - Zone - TimeslotMetrics list
		Map<Date, Map<String, List<TimeslotCapacityModel>>> uploadFileSlotsByDateZoneMapping = new HashMap<Date, Map<String,List<TimeslotCapacityModel>>>();
		
		if(this.timeslotMetrics.size() > 0) {
			Collections.sort(getTimeslotMetrics(), timeslotCapacityComparator);
			for(TimeslotCapacityModel _ts : this.timeslotMetrics) {	
				if(!uploadFileSlotsByDateZoneMapping.containsKey(_ts.getBaseDate())) {
					uploadFileSlotsByDateZoneMapping.put(_ts.getBaseDate(), new HashMap<String, List<TimeslotCapacityModel>>());
				} 
				if(!uploadFileSlotsByDateZoneMapping.get(_ts.getBaseDate()).containsKey(_ts.getArea())) {
					uploadFileSlotsByDateZoneMapping.get(_ts.getBaseDate()).put(_ts.getArea(), new ArrayList<TimeslotCapacityModel>());
				} 
				uploadFileSlotsByDateZoneMapping.get(_ts.getBaseDate()).get(_ts.getArea()).add(_ts);
			}
			
			for(Map.Entry<Date, Map<String, List<TimeslotCapacityModel>>> _dateEntry : uploadFileSlotsByDateZoneMapping.entrySet()) {
				Map<String, List<IDeliveryWindowMetrics>> _slotsByZone = deliveryProxy.getTimeslotsByDateEx(_dateEntry.getKey(), null, null, null, false);
				for(Map.Entry<String, List<IDeliveryWindowMetrics>> _tempMap : _slotsByZone.entrySet()){
					List<IDeliveryWindowMetrics> _zoneTsList = _tempMap.getValue();
					for(IDeliveryWindowMetrics _ts : _zoneTsList) {
						if(!slotsByDateZoneMapping.containsKey(_ts.getDeliveryDate())) {
							slotsByDateZoneMapping.put(_ts.getDeliveryDate(), new HashMap<String, List<IDeliveryWindowMetrics>>());
						} 
						if(!slotsByDateZoneMapping.get(_ts.getDeliveryDate()).containsKey(_tempMap.getKey())) {
							slotsByDateZoneMapping.get(_ts.getDeliveryDate()).put(_tempMap.getKey(), new ArrayList<IDeliveryWindowMetrics>());
						} 
						slotsByDateZoneMapping.get(_ts.getDeliveryDate()).get(_tempMap.getKey()).add(_ts);
					}
				}	
			}
			
			Iterator<TimeslotCapacityModel> _tsItr = this.getTimeslotMetrics().iterator();
			while(_tsItr.hasNext()) {
				TimeslotCapacityModel _ts = _tsItr.next();
				matchSlotToMetrics(slotsByDateZoneMapping.get(_ts.getBaseDate()).get(_ts.getArea()), _ts);
			}
			
			TimeslotCapacityModel _ts = null;
			// check for time slots not in the upload XLS file
			// if found any, set the capacity to 0, if no allocation exists for the time slot
			for(Map.Entry<Date, Map<String, List<IDeliveryWindowMetrics>>> _slotByDateZoneEntry : slotsByDateZoneMapping.entrySet()) {
				
				if(uploadFileSlotsByDateZoneMapping.containsKey(_slotByDateZoneEntry.getKey())) {
					
					for(Map.Entry<String, List<IDeliveryWindowMetrics>> _metricEntry : _slotByDateZoneEntry.getValue().entrySet()) {
						
						if(uploadFileSlotsByDateZoneMapping.get(_slotByDateZoneEntry.getKey()).containsKey(_metricEntry.getKey())){
							List<IDeliveryWindowMetrics> _metrics = _metricEntry.getValue();
							if(_metrics != null) {
								Iterator<IDeliveryWindowMetrics> _metricsItr = _metrics.iterator();
								while(_metricsItr.hasNext()) {
									IDeliveryWindowMetrics _tsMetrics = _metricsItr.next();
									_ts = new TimeslotCapacityModel();
									_ts.setBaseDate(_tsMetrics.getDeliveryDate());
									_ts.setStartTime(_tsMetrics.getDisplayStartTime());
									_ts.setEndTime(_tsMetrics.getDisplayEndTime());
									_ts.setArea(_metricEntry.getKey());
									
									if(_tsMetrics.getTotalAllocatedOrders() > 0) {	
										_ts.getExceptions().add("Can't upload capacity to 0 if alloction exists");
										isTimeslotMetricsValid = false;
										timeslotsNotInUploadFileWithException.add(_ts);
									} else if(!_tsMetrics.isDynamic()) {
										_ts.setCapacity(0);
										_ts.setChefsTableCapacity(0);
										_ts.setPremiumCapacity(0);
										_ts.setPremiumCtCapacity(0);
										this.getTimeslotMetrics().add(_ts);
									}
								}
							}
						}

					}
				}
			}
		}
	}
	
	private boolean matchMetricToSlot(List<TimeslotCapacityModel> timeslotLst, IDeliveryWindowMetrics tsMetrics) {		
		
		boolean foundTs = false;
		if(timeslotLst != null) {
			Iterator<TimeslotCapacityModel> _tsItr = timeslotLst.iterator();
			while(_tsItr.hasNext()) {
				TimeslotCapacityModel _tmpTimeslot =  _tsItr.next();				
				if (tsMetrics.getDeliveryDate().equals(_tmpTimeslot.getBaseDate())
						&& isMatchingTime(_tmpTimeslot.getStartTime(),
								tsMetrics.getDisplayStartTime())
						&& isMatchingTime(_tmpTimeslot.getEndTime(),
								tsMetrics.getDisplayEndTime())) {
					foundTs = true;;
					break;
				}
			}
		}
		return foundTs;
	}
	
	private void matchSlotToMetrics(List<IDeliveryWindowMetrics> metrics, TimeslotCapacityModel ts) {		
		boolean foundTS = false;
		IDeliveryWindowMetrics _tsMetric = null;
		if(metrics != null) {
			Iterator<IDeliveryWindowMetrics> _metricsItr = metrics.iterator();
			while(_metricsItr.hasNext()) {
				IDeliveryWindowMetrics _tmpTsMetric =  _metricsItr.next();				
				if (_tmpTsMetric.getDeliveryDate().equals(ts.getBaseDate())
						&& isMatchingTime(ts.getStartTime(),
								_tmpTsMetric.getDisplayStartTime())
						&& isMatchingTime(ts.getEndTime(),
								_tmpTsMetric.getDisplayEndTime())) {
					foundTS = true;
					_tsMetric = _tmpTsMetric;
					_metricsItr.remove();
					break;
				}
			}
			
			// if found, check if the time slot is STATIC & Allocation > defined Capacity
			if(foundTS) {
				if(_tsMetric.isDynamic()) {
					ts.getExceptions().add("Timeslot is DYNAMIC. Can't upload capacity for DYNAMIC timeslots");
					isTimeslotMetricsValid = false;
				} else {
					
					// validate capacity metrics to allocation
					/**
					 * Current allocation has a time-dependent definition:
					 * <ul>
					 *  <li>before CT Release Time = Base Allocation + CT Capacity + Premium capacity,</li>
					 *  <li>after CT Release Time = Base Allocation + CT Allocation + Premium capacity.</li>
					 * </ul	 
					 * @return current allocation based on current date.
					 */
					if(ts.getCapacity() < (_tsMetric.getBaseAllocation() + ts.getChefsTableCapacity() + ts.getPremiumCapacity())) {
						ts.getExceptions().add(
								"Total capacity should meet current allocation # "
										+ (_tsMetric.getBaseAllocation() + ts.getChefsTableCapacity() + ts.getPremiumCapacity()));
						isTimeslotMetricsValid = false;
					}
					
					// MIN CT capacity
					if(ts.getChefsTableCapacity() < _tsMetric.getChefsTableAllocation()) {
						ts.getExceptions().add(
								"CT capacity should be MIN of CT allocation # "
										+ _tsMetric.getChefsTableAllocation());
						isTimeslotMetricsValid = false;
					}
					// MAX CT capacity
					if(ts.getChefsTableCapacity() > (ts.getCapacity() - _tsMetric.getBaseAllocation())) {
						ts.getExceptions().add(
								"CT capacity should be MAX of Total capacity - Base allocation");
						isTimeslotMetricsValid = false;
					}
					
					// MIN PREMIUM capacity
					if(ts.getPremiumCapacity() < (ts.getPremiumCtCapacity() + _tsMetric.getPremiumAllocation())) {
						ts.getExceptions().add(
								"Premium capacity should be MIN of allocation # "
										+ (ts.getPremiumCtCapacity() + _tsMetric.getPremiumAllocation()));
						isTimeslotMetricsValid = false;
					}
					
					// MAX PREMIUM capacity
					if(ts.getPremiumCapacity() > (ts.getCapacity() - ts.getChefsTableCapacity())) {
						ts.getExceptions().add(
								"Premium capacity should be MAX of Total capacity - CT capacity");
						isTimeslotMetricsValid = false;
					}
					
					// MIN PREMIUM CT capacity
					if(ts.getPremiumCtCapacity() < _tsMetric.getPremiumCtAllocation()) {
						ts.getExceptions().add(
								"Premium CT capacity should be MIN of allocation # "
										+ _tsMetric.getPremiumCtAllocation());
						isTimeslotMetricsValid = false;
					}
					// MAX PREMIUM CT capacity
					if(ts.getPremiumCtCapacity() > (ts.getPremiumCapacity() - _tsMetric.getPremiumAllocation())) {
						ts.getExceptions().add(
								"Premium CT capacity should be MAX of Premium capacity - Premium allocation ");
						isTimeslotMetricsValid = false;
					}
				}	
			} else {
				ts.getExceptions().add("No matching timeslot exists");
				isTimeslotMetricsValid = false;
			}				
		} else {
			ts.getExceptions().add("No matching timeslot exists");
			isTimeslotMetricsValid = false;
		}	
	}
	
	private boolean isMatchingTime(Date date1, Date date2) {
		boolean result = false;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		int hour1 = cal.get(Calendar.HOUR_OF_DAY);
		int minute1 = cal.get(Calendar.MINUTE);
		
		cal.setTime(date2);
		int hour2 = cal.get(Calendar.HOUR_OF_DAY);
		int minute2 = cal.get(Calendar.MINUTE);
		
		if(hour1 == hour2 && minute1 == minute2) {
			result = true; 
		}
		return result;
	}
	
	private Comparator<TimeslotCapacityModel> timeslotCapacityComparator = new Comparator<TimeslotCapacityModel>() {
		public int compare(TimeslotCapacityModel ts1, TimeslotCapacityModel ts2) {
			if (ts1.getBaseDate().compareTo(ts2.getBaseDate()) == 0) {
				return ts1.getArea().compareTo(ts2.getArea());
			} else {
				return ts1.getBaseDate().compareTo(ts2.getBaseDate());
			}
		}
	};
	
	@SuppressWarnings("unchecked")
	public String buildResponseHtml() {
		
		StringBuffer buff = new StringBuffer();
		
		if(!isTimeslotMetricsValid || this.exceptionList.size() > 0) {

			buff.append("<html>").append("<body>");
			
			try {
				buff.append("<table class=\"summaryTable\" valign=\"top\" width=\"780\" align=\"left\" cellpadding=\"0\" cellspacing=\"0\">");
				if(this.exceptionList.size() > 0) {
					buff.append("<tr>").append("<th class=\"submenu\"  colspan=\"4\" align=\"center\">").append("DATA EXCEPTIONS").append("</th>").append("</tr>");
					Iterator<BadDataException> _Itr = this.exceptionList.iterator();
					buff.append("<tr>");
					buff.append("<td class=\"first\" colspan=\"4\">");
					while(_Itr.hasNext()) {
						BadDataException _exception = _Itr.next();
						buff.append(_exception.getMessage()).append("<br/>");
					}				
					buff.append("<br/></td>");
					buff.append("</tr>");	
				}
				
				if(!isTimeslotMetricsValid) {
					buff.append("<tr>").append("<th class=\"submenu\" colspan=\"4\" align=\"center\">").append("TIMESLOT VALIDATION ERRORS").append("</th>");
					buff.append("<tr>").append("<th>").append("Delivery Date").append("</th>")
						.append("<th>").append("Zone").append("</th>")
						.append("<th>").append("Timeslot Window").append("</th>")
						.append("<th>").append("Exceptions").append("</th>").append("</tr>");
				
					if(this.getTimeslotMetrics() != null) {
						TimeslotCapacityModel _dlvTimeSlot = null;	
						Iterator<TimeslotCapacityModel> _itr = this.getTimeslotMetrics().iterator();			
						
						while(_itr.hasNext()) {
							_dlvTimeSlot = _itr.next();	
							if(_dlvTimeSlot.getExceptions().size() > 0) {
								buff.append("<tr>")
									.append("<td class=\"first\" align=\"center\">").append(TransStringUtil.getDate(_dlvTimeSlot.getBaseDate())).append("</td>")
									.append("<td align=\"center\">").append(_dlvTimeSlot.getArea()).append("</td>")
									.append("<td align=\"center\">").append(_dlvTimeSlot.getDispalyWindow()).append("</td>");
								buff.append("<td class=\"last\">");				
								
								Iterator<String> _tsExceptionItr = _dlvTimeSlot.getExceptions().iterator();
								while(_tsExceptionItr.hasNext()) {
									String _exception = _tsExceptionItr.next();
									buff.append(_exception).append("<br/>");
								}
								buff.append("</td>");
								buff.append("</tr>");
							}
						}					
					}
					
					if(this.timeslotsNotInUploadFileWithException != null) {
						TimeslotCapacityModel _dlvTimeSlot = null;	
						Iterator<TimeslotCapacityModel> _itr = this.timeslotsNotInUploadFileWithException.iterator();			
						
						while(_itr.hasNext()) {
							_dlvTimeSlot = _itr.next();	
							if(_dlvTimeSlot.getExceptions().size() > 0) {
								buff.append("<tr>")
									.append("<td class=\"first\" align=\"center\">").append(TransStringUtil.getDate(_dlvTimeSlot.getBaseDate())).append("</td>")
									.append("<td align=\"center\">").append(_dlvTimeSlot.getArea()).append("</td>")
									.append("<td align=\"center\">").append(_dlvTimeSlot.getDispalyWindow()).append("</td>");
								buff.append("<td class=\"last\">");				
								
								Iterator<String> _tsExceptionItr = _dlvTimeSlot.getExceptions().iterator();
								while(_tsExceptionItr.hasNext()) {
									String _exception = _tsExceptionItr.next();
									buff.append(_exception).append("<br/>");
								}
								buff.append("</td>");
								buff.append("</tr>");
							}
						}					
					}
				}
				buff.append("</table>");
			} catch(ParseException ex) {
				LOGGER.error(ex.getMessage());
			}
			buff.append("</body>").append("</html>");
		}
		
		return buff.toString();
	}

}
