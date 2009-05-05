package com.freshdirect.transadmin.datamanager.report;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;

import com.freshdirect.transadmin.datamanager.RouteFileManager;
import com.freshdirect.transadmin.datamanager.model.OrderRouteInfoModel;
import com.freshdirect.transadmin.datamanager.report.model.CutOffReportData;
import com.freshdirect.transadmin.datamanager.report.model.CutOffReportKey;
import com.freshdirect.transadmin.datamanager.report.model.TimeWindow;
import com.freshdirect.transadmin.util.TransStringUtil;

public class XlsCutOffReport implements ICutOffReport  {
	
	public static final short DEFAULT_FONT_HEIGHT = 8;
	public static final int WIDTH_MULT = 240; // width per char
	public static final int DEFAULT_WIDTH = 12; // width per char
    public static final int MIN_CHARS = 8; // minimum char width
    private short rownum;	
    private short cellnum;
    
        
	public void generateCutOffReport(String file, CutOffReportData reportData)
										throws ReportGenerationException {

		HSSFWorkbook wb = new HSSFWorkbook();
		Map styles = initStyles(wb);
				
		if (reportData != null) {
			List cutOffKeys = getCutOffReportKeys(reportData.getReportData().keySet());
			Set timeSlots = (Set) cutOffKeys.get(0);
			Set routIds = (Set) cutOffKeys.get(1);
			Set orderDates = (Set) cutOffKeys.get(2);
			//System.out.println("timeSlots >>"+timeSlots);
			Iterator _orderDateItr = orderDates.iterator();
			int totalRoutes = 0;
			int totalOrders = 0;
			
			while (_orderDateItr.hasNext()) {
				Date orderDate = (Date) _orderDateItr.next();
				
				HSSFSheet sheet = wb.createSheet(getFormattedDate(orderDate));
				sheet.setDefaultColumnWidth((short)DEFAULT_WIDTH);	
				sheet.setPrintGridlines(true);
 			    
			    HSSFPrintSetup ps = sheet.getPrintSetup();
			    sheet.setAutobreaks(true);
		        ps.setFitHeight((short) 1);
		        ps.setFitWidth((short) 1);
		        
			    rownum = 0;	
			    cellnum = 0;
			    
			    HSSFRow row = sheet.createRow(rownum++);
			    HSSFCell hssfCell = row.createCell(cellnum);

		        setCellEncoding(hssfCell);

		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("titleStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(CUTOFFREPORT_TITLE));
		        //int valWidth = (CUTOFFREPORT_TITLE).length() * WIDTH_MULT;
		        
				sheet.addMergedRegion(new Region(0,(short)0,0,(short)8));
				row = sheet.createRow(rownum++);//blank Row
				
		        cellnum = 0;
		        row = sheet.createRow(rownum++);
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(CUTOFFREPORT_DATETITLE));
		        		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(getFormattedDate(orderDate)));
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(CUTOFFREPORT_CUTOFFTITLE));
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(reportData.getCutOff()));
		        
		        
		        row = sheet.createRow(rownum++);//blank Row
		        
		        Iterator _colsItr = timeSlots.iterator();
		        
		        cellnum = 0;
		        row = sheet.createRow(rownum++);
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(CUTOFFREPORT_ROUTETITLE));
		        		        
		        while (_colsItr.hasNext()) {

					TimeWindow timeWindow = (TimeWindow) _colsItr.next();
					String strWindow = TransStringUtil.formatTime(timeWindow
															.getTimeWindowStart());
					hssfCell = row.createCell(cellnum++);		        
				    hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldRightAlignStyle"));
				    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				    hssfCell.setCellValue(new HSSFRichTextString(strWindow));	
				    //sheet.autoSizeColumn((short)(cellnum-1));				
				}
		        
		        hssfCell = row.createCell(cellnum++);		        
		        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldRightAlignStyle"));
		        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		        hssfCell.setCellValue(new HSSFRichTextString(CUTOFFREPORT_STOPSTITLE));
		        
				Iterator _routeItr = routIds.iterator();
				
				while (_routeItr.hasNext()) {

					String routeId = (String) _routeItr.next();
					Iterator _timeSlotItr = timeSlots.iterator();
					
					cellnum = 0;
					row = sheet.createRow(rownum++);
			        hssfCell = row.createCell(cellnum++);		        
			        hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
			        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			        hssfCell.setCellValue(new HSSFRichTextString(routeId));
			        totalRoutes++;
			        
			        int totalStops = 0;
					while (_timeSlotItr.hasNext()) {

						TimeWindow timeWindow = (TimeWindow) _timeSlotItr
								.next();
						

						CutOffReportKey tmpKey = new CutOffReportKey(orderDate,
								timeWindow.getTimeWindowStart(), timeWindow
										.getTimeWindowStop(), routeId);
						
						int stopCnt = 0;
						List stopLst = (List) reportData.getReportData().get(
								tmpKey);
						if (stopLst != null) {
							stopCnt = stopLst.size();
						}
						hssfCell = row.createCell(cellnum++);		        
				        hssfCell.setCellStyle((HSSFCellStyle) styles.get("numericStyle"));
				        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				        if(stopCnt == 0) {
				        	hssfCell.setCellValue(new HSSFRichTextString(""));
				        } else {
				        	hssfCell.setCellValue(stopCnt);
				        }
				        totalStops = totalStops + stopCnt;
					}
					hssfCell = row.createCell(cellnum++);		        
			        hssfCell.setCellStyle((HSSFCellStyle) styles.get("numericStyleBold"));
			        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			        if(totalStops == 0) {
			        	hssfCell.setCellValue(new HSSFRichTextString(""));
			        } else {
			        	hssfCell.setCellValue(totalStops);
			        }
			        totalOrders = totalOrders + totalStops;
				}
				createTotalRows(sheet, rownum, totalRoutes, totalOrders, styles);
			}
			
			createSummarySheet(wb, reportData, styles);
			createTripInfoSheet(wb, reportData, styles);			
		}		 

		new RouteFileManager().generateReportFile(file, wb);
	}
	
	private void createTotalRows(HSSFSheet sheet, int rowNum, int totalRoutes, int totalOrders, Map styles) {
		
		cellnum = 0;
		HSSFRow row = sheet.createRow(rowNum++);
		row = sheet.createRow(rowNum++);
        
		HSSFCell hssfCell = row.createCell(cellnum++);		        
        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
        hssfCell.setCellValue(new HSSFRichTextString("Total Routes"));
        		        
        hssfCell = row.createCell(cellnum++);		        
        hssfCell.setCellStyle((HSSFCellStyle) styles.get("numericStyle"));
        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
        hssfCell.setCellValue(new HSSFRichTextString(totalRoutes+""));
        
        cellnum = 0;
		row = sheet.createRow(rowNum++);
        
		hssfCell = row.createCell(cellnum++);		        
        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
        hssfCell.setCellValue(new HSSFRichTextString("Total Orders"));
        		        
        hssfCell = row.createCell(cellnum++);		        
        hssfCell.setCellStyle((HSSFCellStyle) styles.get("numericStyle"));
        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
        hssfCell.setCellValue(new HSSFRichTextString(totalOrders+""));
        
        
	}
	
	private short createTripInfoSheet(HSSFWorkbook wb, CutOffReportData reportData, Map styles) {
		
		if(reportData.getTripReportData() != null) {
			Set tripKeys = reportData.getTripReportData().keySet();
			
			Iterator _tripKeyItr = tripKeys.iterator();
			
			short rownum = 0;	
		    short cellnum = 0;
		    
		    HSSFSheet sheet = wb.createSheet("Trip Summary");
		    sheet.setDefaultColumnWidth((short)DEFAULT_WIDTH);	
			sheet.setPrintGridlines(true);
		    
		    HSSFPrintSetup ps = sheet.getPrintSetup();
		    sheet.setAutobreaks(true);
	        ps.setFitHeight((short) 1);
	        ps.setFitWidth((short) 1);
	       		    
		    HSSFRow row = sheet.createRow(rownum++);
		    HSSFCell hssfCell = row.createCell(cellnum);

	        setCellEncoding(hssfCell);

	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("titleStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Trip Summary Info"));
	        			        
			sheet.addMergedRegion(new Region(0,(short)0,0,(short)8));
			row = sheet.createRow(rownum++);//blank Row
			
	        row = sheet.createRow(rownum++);
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Route Id"));
	        		        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Trip Id"));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Stop No"));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Order No"));
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Address"));
	        
			while (_tripKeyItr.hasNext()) {
				String _tripRouteId = (String) _tripKeyItr.next();
						        			        
		        Iterator _colsTripItr = ((List)reportData.getTripReportData().get(_tripRouteId)).iterator();
	       			        		        
		        while (_colsTripItr.hasNext()) {
		        	cellnum = 0;
		        	OrderRouteInfoModel _model = (OrderRouteInfoModel)_colsTripItr.next();
		        	
		        	row = sheet.createRow(rownum++);
		        	
					hssfCell = row.createCell(cellnum++);		        
				    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
				    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				    hssfCell.setCellValue(new HSSFRichTextString(_tripRouteId));
				    
				    hssfCell = row.createCell(cellnum++);		        
				    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
				    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				    hssfCell.setCellValue(new HSSFRichTextString(_model.getTripId()));
				    
				    hssfCell = row.createCell(cellnum++);		        
				    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
				    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				    hssfCell.setCellValue(new HSSFRichTextString(_model.getStopNumber()));
				    
				    hssfCell = row.createCell(cellnum++);		        
				    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
				    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				    hssfCell.setCellValue(new HSSFRichTextString(_model.getOrderNumber()));
				    
				    hssfCell = row.createCell(cellnum++);		        
				    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyleNoWrap"));
				    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				    hssfCell.setCellValue(new HSSFRichTextString(_model.getAddress()+","+_model.getZipcode()));
				    //sheet.autoSizeColumn((short)(cellnum-1));				
				}		        			        
				
			}
		}
		
        
        return rownum;
	}
	
	private short createSummarySheet(HSSFWorkbook wb, CutOffReportData reportData, Map styles) {
		
		if(reportData.getSummaryData() != null) {
			Set summaryKeys = reportData.getSummaryData().keySet();
			
			Iterator _itr = summaryKeys.iterator();
			
			short rownum = 0;	
		    short cellnum = 0;
		    
		    int totalRoutes = 0;
			int totalOrders = 0;
		    
		    HSSFSheet sheet = wb.createSheet("Route Summary");
		    sheet.setDefaultColumnWidth((short)DEFAULT_WIDTH);	
			sheet.setPrintGridlines(true);
		    
		    HSSFPrintSetup ps = sheet.getPrintSetup();
		    sheet.setAutobreaks(true);
	        ps.setFitHeight((short) 1);
	        ps.setFitWidth((short) 1);
	       		    
		    HSSFRow row = sheet.createRow(rownum++);
		    HSSFCell hssfCell = row.createCell(cellnum);

	        setCellEncoding(hssfCell);

	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("titleStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Route Summary Report"));
	        			        
			sheet.addMergedRegion(new Region(0,(short)0,0,(short)8));
			row = sheet.createRow(rownum++);//blank Row
			
	        row = sheet.createRow(rownum++);
	        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("Route Id"));
	       
	        		        
	        hssfCell = row.createCell(cellnum++);		        
	        hssfCell.setCellStyle((HSSFCellStyle) styles.get("boldStyle"));
	        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        hssfCell.setCellValue(new HSSFRichTextString("No of Orders"));
	        	        
			while (_itr.hasNext()) {
				String _routeId = (String) _itr.next();
				List _orders = 	(List)reportData.getSummaryData().get(_routeId);        			        
		        int totalStops =  _orders != null ? _orders.size():0;      
		        cellnum = 0;
	        	        	
	        	row = sheet.createRow(rownum++);
	        	
				hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(_routeId));
			    totalRoutes++;
			    
			    hssfCell = row.createCell(cellnum++);		        
			    hssfCell.setCellStyle((HSSFCellStyle) styles.get("textStyle"));
			    hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    hssfCell.setCellValue(new HSSFRichTextString(totalStops+""));
			    totalOrders = totalOrders+totalStops;
			}
			createTotalRows(sheet, rownum, totalRoutes, totalOrders, styles);
		}
		       
        return rownum;
	}

	private List getCutOffReportKeys(Set keys) {

		List result = new ArrayList();
		Set timeSlots = new TreeSet();
		Set routIds = new TreeSet();
		Set orderDates = new TreeSet();
		result.add(timeSlots);
		result.add(routIds);
		result.add(orderDates);

		if (keys != null) {
			Iterator _iterator = keys.iterator();
			CutOffReportKey _key = null;
			while (_iterator.hasNext()) {
				_key = (CutOffReportKey) _iterator.next();
				timeSlots.add(new TimeWindow(_key.getTimeWindowStart(), _key
						.getTimeWindowStop()));
				routIds.add(_key.getRouteId());
				orderDates.add(_key.getOrderDate());
			}
		}
		return result;
	}
	
	private Map initStyles(HSSFWorkbook wb) {
        return initStyles(wb, DEFAULT_FONT_HEIGHT);
    }

    private Map initStyles(HSSFWorkbook wb, short fontHeight) {
        Map result = new HashMap();
        HSSFCellStyle titleStyle = wb.createCellStyle();
        HSSFCellStyle textStyle = wb.createCellStyle();
        HSSFCellStyle boldStyle = wb.createCellStyle();
        HSSFCellStyle boldRightAlignStyle = wb.createCellStyle();
        HSSFCellStyle textStyleNoWrap = wb.createCellStyle();
        HSSFCellStyle numericStyle = wb.createCellStyle();
        HSSFCellStyle numericStyleBold = wb.createCellStyle();
        
        HSSFCellStyle numericStyle_Totals = wb.createCellStyle();
        HSSFCellStyle percentStyle_Totals = wb.createCellStyle();
        HSSFCellStyle textStyle_Totals = wb.createCellStyle();

        result.put("titleStyle", titleStyle);
        result.put("textStyle", textStyle);
        result.put("boldStyle", boldStyle);
        result.put("boldRightAlignStyle", boldRightAlignStyle);
        result.put("textStyleNoWrap", textStyleNoWrap);
        
        result.put("numericStyle", numericStyle);
        result.put("numericStyleBold", numericStyleBold);
        result.put("numericStyle_Totals", numericStyle_Totals);
        result.put("percentStyle_Totals", percentStyle_Totals);
        result.put("textStyle_Totals", textStyle_Totals);

        // Global fonts
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        font.setColor(HSSFColor.BLACK.index);
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints(fontHeight);
        
        HSSFFont titleFont = wb.createFont();
        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        titleFont.setColor(HSSFColor.BLACK.index);
        titleFont.setFontName(HSSFFont.FONT_ARIAL);
        titleFont.setFontHeightInPoints((short)(fontHeight+4));
        

        HSSFFont fontBold = wb.createFont();
        fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        fontBold.setColor(HSSFColor.BLACK.index);
        fontBold.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints(fontHeight);
     

        // Standard Numeric Style
        numericStyle.setFont(font);
        numericStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

        // Standard Numeric Style Bold
        numericStyleBold.setFont(fontBold);
        numericStyleBold.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

        // Title Style
        titleStyle.setFont(titleFont);
        titleStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        titleStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        titleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        titleStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        titleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        titleStyle.setRightBorderColor(HSSFColor.BLACK.index);
        titleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        titleStyle.setTopBorderColor(HSSFColor.BLACK.index);
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        // Standard Text Style
        textStyle.setFont(font);
        textStyle.setWrapText(true);

        // Standard Text Style
        boldStyle.setFont(fontBold);
        boldStyle.setWrapText(true);
        
        boldRightAlignStyle.setFont(fontBold);
        boldRightAlignStyle.setWrapText(true);
        boldRightAlignStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        
        textStyleNoWrap.setFont(font);
        textStyleNoWrap.setWrapText(false);
          
        
        
       
        // Numeric Style Total
        numericStyle_Totals.setFont(fontBold);
        numericStyle_Totals.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        numericStyle_Totals.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        numericStyle_Totals.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        numericStyle_Totals.setBottomBorderColor(HSSFColor.BLACK.index);
        numericStyle_Totals.setBorderTop(HSSFCellStyle.BORDER_THIN);
        numericStyle_Totals.setTopBorderColor(HSSFColor.BLACK.index);
        numericStyle_Totals.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        numericStyle_Totals.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        
        // Text Style Total
        textStyle_Totals.setFont(fontBold);
        textStyle_Totals.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        textStyle_Totals.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        textStyle_Totals.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        textStyle_Totals.setBottomBorderColor(HSSFColor.BLACK.index);
        textStyle_Totals.setBorderTop(HSSFCellStyle.BORDER_THIN);
        textStyle_Totals.setTopBorderColor(HSSFColor.BLACK.index);
        textStyle_Totals.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        textStyle_Totals.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        return result;
    }
    
    //  add to set Cell encoding
    private void setCellEncoding(HSSFCell cell) {
    	cell.setEncoding(HSSFCell.ENCODING_UTF_16);
    }
    
    private String getFormattedDate(Date date) {
    	try {
			return TransStringUtil.getServerDate(date);
		} catch (ParseException exp) {
			return "Error Formatting Date";
		}
    }
}
