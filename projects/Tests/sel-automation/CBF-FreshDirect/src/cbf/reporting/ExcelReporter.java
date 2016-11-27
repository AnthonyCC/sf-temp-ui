/******************************************************************************
$Id : ExcelReporter.java 9/8/2014 1:22:41 PM
Copyright 2014-2016 IGATE GROUP OF COMPANIES. All rights reserved
(Subject to Limited Distribution and Restricted Disclosure Only.)
THIS SOURCE FILE MAY CONTAIN INFORMATION WHICH IS THE PROPRIETARY
INFORMATION OF IGATE GROUP OF COMPANIES AND IS INTENDED FOR USE
ONLY BY THE ENTITY WHO IS ENTITLED TO AND MAY CONTAIN
INFORMATION THAT IS PRIVILEGED, CONFIDENTIAL, OR EXEMPT FROM
DISCLOSURE UNDER APPLICABLE LAW.
YOUR ACCESS TO THIS SOURCE FILE IS GOVERNED BY THE TERMS AND
CONDITIONS OF AN AGREEMENT BETWEEN YOU AND IGATE GROUP OF COMPANIES.
The USE, DISCLOSURE REPRODUCTION OR TRANSFER OF THIS PROGRAM IS
RESTRICTED AS SET FORTH THEREIN.
 ******************************************************************************/

package cbf.reporting;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.NumberCell;
import jxl.Range;
import jxl.Workbook;
import jxl.biff.CellReferenceHelper;
import jxl.format.Colour;
import jxl.format.Pattern;
import jxl.format.UnderlineStyle;
import jxl.read.biff.BiffException;
import jxl.write.DateTime;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableHyperlink;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.commons.io.FileUtils;

import ModuleDrivers.CompositeAppDriver;
import ModuleDrivers.GeneralDriver;

import cbf.engine.BaseModuleDriver;
import cbf.engine.ResultReporter;
import cbf.engine.TestCase;
import cbf.engine.TestIteration;
import cbf.engine.TestResult;
import cbf.engine.TestStep;
import cbf.engine.TestResult.ResultType;
import cbf.harness.ResourcePaths;
import cbf.utils.LogUtils;
import cbf.utils.StringUtils;
import cbf.utils.Utils;



/**
 * 
 * Implements ResultReporter and generates Excel reports
 * 
 */
public class ExcelReporter implements ResultReporter{	
    List<String> testCases = new ArrayList<String>();
    
    /**
     * Constructor to initialize parameters
	 * @param params map containing parameters
     */
	public ExcelReporter(Map params){
		filePath = (String) params.get("filepath");
		String dynamicName=(String) params.get("dynamicName");
		if(filePath.equals("")){
			filePath=ResourcePaths.singleton.getRunResource("ExecutionReport_"+dynamicName+".xls","");
		}
		templatePath= ResourcePaths.singleton.getFrameworkResource("Resources", "ReportTemplate.xls"); 
		SHN_SUMMARY = "Summary";	
		SHN_DETAILS = "Details";
		SHN_COVER = "Cover";
		LINK_COL="A";
	}
	
	/*	Function: open(public)
		Goal: Makes the engine ready for use
		Out Params:	Boolean - Didn't the engine manage to start?*/
	
	public void open(Map headers){
		logger.trace("Report: open");
		File srcFile = new File(templatePath);
		File targetFile = new File(filePath);
		boolean isExists = targetFile.exists();
		if(!isExists){
			try{
				FileUtils.copyFile(srcFile, targetFile);
			} catch(Exception e){	// FIXME: specific exception
				logger.handleError("Error in making a new report file from template", e, srcFile, targetFile);
			}
		}

		openFile(targetFile);

		if (!isExists) { // new file
			writeHeaders(headers);
		}
	}
	
	/*	Function: close(public)
		Goal: Stops the engine
		In Params: None
		Out Params:	None
	 */	
	public void close(){
		logger.trace("Report: Close");
		try{
			oWorkbook.write();
		}catch(IOException e){	
			logger.handleError("Error closing excel", e);
		}
		try{
			oWorkbook.close();
			testCases.size();
		}catch(IOException e){	
			logger.handleError("Error closing excel", e);
		} catch (WriteException e) {
			logger.handleError("Error closing excel", e);
		}
	}
	
	public void start(TestResult result) {
		report("START", result, result.entityDetails);
	}
	
	public void log(TestResult result, ResultType rsType, Map details) {
		report("DETAILS", result, details);
	}
	
	public void finish(TestResult result, ResultType rsType, Object details) {
		report("FINISH", result, details);
	}

	private WritableWorkbook oWorkbook = null;
	private WritableSheet oSummarySheet, oDetailsSheet,oCoverSheet;
	private int detailsRowNum, summaryRowNum;

	/*'Function Name:	openFile
	'Description:	This Function opens a created Result File
	'Parameter:		FilePath - Path of the result file*/		
	private void openFile(File targetFile){
		try{
			Workbook workbook = Workbook.getWorkbook(targetFile);
			oWorkbook = Workbook.createWorkbook(targetFile, workbook);
			oSummarySheet = oWorkbook.getSheet(SHN_SUMMARY);
			oDetailsSheet = oWorkbook.getSheet(SHN_DETAILS);
			oCoverSheet = oWorkbook.getSheet(SHN_COVER);
			
			summaryRowNum = oSummarySheet.getRows(); 
			detailsRowNum = oDetailsSheet.getRows();
		}catch(BiffException e){
			logger.handleError("Error in handling workbook contents", e);
		}catch(IOException e){
			logger.handleError("Error while accessing workbook ", e);
		}
	}
	
	private void writeHeaders(Map headers){
		Iterator iterator = headers.entrySet().iterator();
		while(iterator.hasNext()){
			Entry entry = (Entry) iterator.next();
			try{
				WritableCell cell = oWorkbook.findCellByName("Hdr"+entry.getKey());
				
				if(cell==oWorkbook.findCellByName("HdrBrowser"))
				{
					writeCellValue(oCoverSheet, cell,CompositeAppDriver.startUp);
				}
				else if(cell==oWorkbook.findCellByName("HdrVersion"))
				{
					writeCellValue(oCoverSheet, cell,CompositeAppDriver.BrowserVersion);
				}
				/*else if(cell==oWorkbook.findCellByName("HdrEnvironment"))
				{
					writeCellValue(oCoverSheet, cell,BaseModuleDriver.Environment);
				}*/
				else if(cell==oWorkbook.findCellByName("HdrPlatform"))
				{
					writeCellValue(oCoverSheet, cell,CompositeAppDriver.Platforms);
				}
				else if(cell != null)
					writeCellValue(oCoverSheet, cell,entry.getValue().toString());
			}catch(Exception e){
				logger.warning("Hdr" + entry.getKey() + ": failed to write this header: " + e);
			}
		}
	}

	/*	Function: report(public)
		Goal:Reports an event
	 */	
	private void report (String eventType, TestResult result, Object eventData){
		logger.trace("Report:" + eventType + ":" + StringUtils.toString(result) + ":" + StringUtils.toString(eventData));

		try{
			switch(result.entityType) {
			case ITERATION:
				if(eventType.equals("START")){
					TestIteration iteration = (TestIteration)eventData;
					String iterName = "";
					Map iterParams = iteration.parameters();
					if(iterParams != null){
						if(iterParams.containsKey("_rowId"))
							iterName = iterParams.get("_rowId").toString();
						if (iterName == null || iterName.equals("")) {
							iterName = "(" + result.parent.childCount + " of " + ((TestCase)result.parent.entityDetails).iterationCount()+")";
						}
					}
					String tcName = result.parent.entityName;
					testCases.add(tcName);
					if(iterName != null){
						tcName = tcName + " " + iterName;
					}

					startTestCase(tcName, iteration.stepCount(), "", result.startTime); //' Skip execStatus
				}else if(eventType.equals("FINISH")){
					String msg = "";
					finishTestCase(result.msRsType.isPassed(), msg, result.finishTime, result.childCount, result);
				}
				break;
			case TESTSTEP:	
				if(eventType.equals("START")){
					TestStep testStep = (TestStep)eventData;
					startTestStep(result.parent.childCount, result.entityName, testStep.stepName(), result.startTime);
				}else if(eventType.equals("FINISH")){
					finishTestStep(result.entityName, result.msRsType.isPassed(), result.startTime, result.finishTime);
				}
				break;
			case COMPONENT:
				if(eventType.equals("DETAILS")){
					Map attibs = (Map)eventData;
					reportCheck(attibs.get("name").toString(),result, attibs, attibs.get("screenDump").toString());
				}
				break;
			}
		}catch(Exception e){
			logger.handleError("Error in Excel reporting", e);
		}
	}
	
	private Object hyperlinked(Object main, String sheetName, int rowNum) {
		return new String[] { (String) main, sheetName + "!"+ LINK_COL + rowNum };
	}

	private void startTestCase(String TestCaseName, int stepsCount, String execStatus, Date startTime){
		Map data = Utils.toMap(new Object[]{"TestCase", TestCaseName,
			"Result", hyperlinked("In Progress", SHN_DETAILS, detailsRowNum),
			"StepsCount", stepsCount,
			"StartTime", startTime
		});
		if(!execStatus.equals("") && execStatus != "No Run"){
			data.put("ExecStatus", "*");
		}
	
		writeSumValues(data, false);
	
		writeDtlValues("TC", Utils.toMap(new Object[]{"Name", TestCaseName}));
	}
	
	private void finishTestCase(boolean isPassed, String msg, Date finishTime, int stepsRun, TestResult result){
		String sStatus;
		String[] eventReport={"",""},executionLog={"",""};
		if(((Map)result.miscInfo.get("EventReport"))!=null){
			eventReport[0]=(String) ((Map)result.miscInfo.get("EventReport")).get("fileName");
			eventReport[1]=(String) ((Map)result.miscInfo.get("EventReport")).get("filePath");
		}
		if(isPassed){
			sStatus = "Passed";
		}else{
			sStatus = "Failed";
		}
		Map data = Utils.toMap(new Object[]{"Result", sStatus, "ErrMsg", msg, "FinishTime", finishTime, "StepsRun", stepsRun,"EventReport",eventReport,"ExectionLog",executionLog});
		writeSumValues(data, true);	
		summaryRowNum = summaryRowNum + 1;
	}
	
	private void startTestStep(int stepNum, String stepName, String stepDescription, Date startTime){
		Map data = Utils.toMap(new Object[]{"Ix", stepNum,
				"Name", stepName, "Description", stepDescription});
		writeDtlValues("StepHdr", data);
	}

	private void finishTestStep(String stepName, boolean isPassed, Date startTime, Date finishTime){
		String sStatus;
		if(isPassed){
			sStatus = "Passed";
		}else{
			sStatus = "Failed";
		}
		writeDtlValues("StepFtr", Utils.toMap(new Object[]{"Name", stepName, "Result", sStatus, "StartTime", startTime, "FinishTime", finishTime}));
	}
	
	private void reportCheck(String checkName, TestResult result, Map details, String scrDumpPath){
		String sExpected= "";
		Object sActual ;
		try{
			sExpected = details.get("expected").toString();
			sActual = details.get("actual").toString();
		}catch(Exception e){
			sExpected = "";
			sActual = "";
		}
		
		//' Link screen dump
		if(scrDumpPath.equals("true")) {
			if(!sActual.equals("")){
				String temp[]={sActual+". Click here to view screenshot.",((Map)result.miscInfo.get("screenDump")).containsKey("filePath")?(String)((Map)result.miscInfo.get("screenDump")).get("filePath"):""};
				sActual=temp;
			}
				
				
		}
	
		try {
			writeDtlValues("Ck", Utils.toMap(new Object[]{
				"Name", checkName,
				"Result", result.finalRsType.toString(), 
				"Expected", sExpected,
				"Actual", sActual,
				"Ts", new Date()}));
		} catch (Exception e) {
			logger.handleError("Error while updating report with validation details", e);
		} 
	}

	private void writeDtlValues(String tplType, Map data){
		try{
			writeValuesFromTpl("Dtl" + tplType + "Row", "Dtl"+tplType, data, detailsRowNum, oDetailsSheet, true);
			detailsRowNum = detailsRowNum + 1;	
		}catch(Exception e){
			logger.handleError("Failed to write values in DETAILS sheet", e);
		}
	}

	private void writeSumValues(Map data, boolean bClearValues){
		try{
			writeValuesFromTpl("SumRow", "Sum", data, summaryRowNum, oSummarySheet, bClearValues);
		}catch(Exception e){
			logger.handleError("Failed to write values in SUMMARY sheet", e);
		}
	}
	
	private void writeValuesFromTpl(String tplRowName, String pfx, Map data, int targetRow, WritableSheet sheet, boolean bClearValues){
		Set<Object> keys = data.keySet();
		for(Object key: keys){
			try{
				WritableCell cell =findCellByName(pfx+key);
				if(cell!=null)
					writeCellValue(sheet, cell, data.get(key));
			}catch(Exception e){
				logger.handleError("Template column:" + pfx + key + ":to:" + data.get(key).toString() + ":didnt happen:" + e.getMessage(), e);
			}
		}

		try{  
			copyRow(tplRowName, targetRow, sheet);
		}catch(Exception e){
			logger.handleError("Error while copying result details", e);
		}
		 
		if(bClearValues){
			try{
				clearContents(tplRowName, sheet);
			}catch(Exception e){
				logger.handleError("Error while clearing template values", e);
			}
		}
	}

	private void writeCellValue(WritableSheet sheet, WritableCell cell, Object value){
		try {
		    WritableCellFormat cf = new WritableCellFormat(cell.getCellFormat());
			WritableFont cellFont = new WritableFont(WritableFont.createFont("Candara"),10);
			Label lb = new Label(cell.getColumn(), cell.getRow(), "");
			sheet.addCell(lb);
			if(value.equals("Passed")){
				//	 WritableFont wfontStatus = new WritableFont(WritableFont.createFont("Candara"), WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
				//	 WritableCellFormat cf1 = new WritableCellFormat(wfontStatus);
					 cellFont.setBoldStyle(WritableFont.NO_BOLD); 
					 cellFont.setUnderlineStyle(UnderlineStyle.SINGLE);
					 cf.setFont(cellFont);
					 cf.setBackground(Colour.GREEN,Pattern.SOLID);
					 lb.setCellFormat(cf);
				}
			if(value.equals("Failed")){
		//			 WritableFont wfontStatus = new WritableFont(WritableFont.createFont("Candara"), WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
				//	 WritableCellFormat cf1 = new WritableCellFormat(wfontStatus);
					 cellFont.setBoldStyle(WritableFont.NO_BOLD); 
					 cellFont.setUnderlineStyle(UnderlineStyle.SINGLE);
					 cf.setFont(cellFont);
					 cf.setBackground(Colour.RED,Pattern.SOLID);
					 lb.setCellFormat(cf);
			}
			
			if(value.equals("Warning")||value.equals("WARNING") ){
				//	 WritableFont wfontStatus = new WritableFont(WritableFont.createFont("Candara"), WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
				//	 WritableCellFormat cf1 = new WritableCellFormat(wfontStatus);
					 cellFont.setBoldStyle(WritableFont.BOLD); 	
					 cellFont.setUnderlineStyle(UnderlineStyle.SINGLE);
					 cf.setFont(cellFont);
					 lb.setCellFormat(cf);
				}
			lb.setCellFormat(cf);

			if (value == null) {
				lb.setString("--null--");
				return;
			}
			
			try {	// try hyperlink
				String[] vals = (String[])value;
				if(!vals[0].equals("")&& !vals[1].equals("")){
					WritableHyperlink link;
					if(new File(vals[1]).isFile())
						link=new WritableHyperlink(cell.getColumn(), cell.getRow(),cell.getColumn(), cell.getRow(),new File(vals[1]),vals[0]);
					else	// link to Detail sheet
						link= new WritableHyperlink(cell.getColumn(),cell.getRow(), vals[1], oDetailsSheet,CellReferenceHelper.getColumn(LINK_COL),Integer.parseInt(vals[1].substring(vals[1].indexOf("!"+LINK_COL)+2)));
					sheet.addHyperlink(link);
				}
				return;
			} catch(ClassCastException e) {
			}
		
			try{
				jxl.write.DateFormat date=new jxl.write.DateFormat("dd-mm-yyyy hh:mm:ss");
				WritableCellFormat DATE_FORMAT = new WritableCellFormat(date);
			    WritableCell dateCell=new DateTime(cell.getColumn(),cell.getRow(),(java.util.Date)value, DATE_FORMAT);
				sheet.addCell(dateCell);
				dateCell.setCellFormat(cell.getCellFormat());
				return;
			} catch(ClassCastException e) {
			}
			
			try {
				lb.setString(value.toString());
				return;
			} catch(ClassCastException e) {
			}

		} catch(Exception e){
			logger.handleError("Error while writing to cell", e);
			return;
		}

		logger.handleError("Cant write value of this type:", value, value.getClass());
	}

	private static int[] extremes(Range range) {
		return new int[] {
			range.getTopLeft().getRow(),
			range.getBottomRight().getRow(),
			range.getTopLeft().getColumn(),
			range.getBottomRight().getColumn()
		};
	}
	private static boolean isOverlapping(Range range1, Range range2) {
		int[] extremes1 = extremes(range1);
		int[] extremes2 = extremes(range2);

		if (extremes1[0] > extremes2[1])
			return false;
		if (extremes2[0] > extremes1[1])
			return false;
		if (extremes1[2] > extremes2[3])
			return false;
		if (extremes2[2] > extremes1[3])
			return false;
		return true;
	}

	private void copyRow(String tplRowName, int targetRow, WritableSheet sheet){
		//LogUtils myLogger = new LogUtils(logger + "(" + tplRowName + "," + targetRow + "," + sheet + ")");

		Range range = oWorkbook.findByName(tplRowName)[0];
		int startRow = range.getTopLeft().getRow();
		int endRow = range.getBottomRight().getRow();
		int startCol = range.getTopLeft().getColumn();
		int endCol = range.getBottomRight().getColumn();

		for(int row = startRow;row<=endRow;row++){
			Range[] mergedCells = sheet.getMergedCells();
			//Code to handle merge cells
			for(int i=0; i < mergedCells.length; i++){
				Range mergedCell = mergedCells[i];
				if (!isOverlapping(range, mergedCell))
					continue;

				try{ // TODO: handling templates which merge 2 rows
					sheet.mergeCells(mergedCell.getTopLeft().getColumn(), targetRow, 
									 mergedCell.getBottomRight().getColumn(), targetRow);
				}catch(Exception e){
					logger.handleError("Error while merging cells", e);
				}
			}

			//Code to set value in cell
			try{
				for(int col = startCol;col<=endCol;col++){
					Cell cell = sheet.getCell(col, row);
					copyCell(cell, targetRow, sheet);
				}
			}catch(Exception e){
				logger.handleError("Error while copying updated result row to target row", e);
			}
		}
	}

	private void copyCell(Cell cell, int targetRow, WritableSheet sheet){
		switch(cellType.valueOf(cell.getType().toString().replaceAll("\\W", "").toUpperCase())){
			case EMPTY:
				try {
					Label lb = new Label(cell.getColumn(), targetRow, "");
					lb.setCellFormat(cell.getCellFormat());
					sheet.addCell(lb);
				} catch (Exception e) {
					logger.handleError("Error while copying blank cell", e);
				}
				break;
			case LABEL:
				 try{
					WritableHyperlink[] hyperlinks=sheet.getWritableHyperlinks();
					for(WritableHyperlink hyperlink:hyperlinks){
						if(hyperlink.getColumn()==cell.getColumn()&& hyperlink.getRow()==cell.getRow()){
							if(hyperlink.isLocation()){
								String value=cell.getContents();
								if(value.contains("!"))
									sheet.addHyperlink(new WritableHyperlink(cell.getColumn(),targetRow, value, oDetailsSheet,CellReferenceHelper.getColumn(LINK_COL),Integer.parseInt(value.substring(value.indexOf("!"+LINK_COL)+2))));
								else
									((WritableHyperlink)hyperlink).setDescription(cell.getContents());
								break;
							}else if(hyperlink.isFile()){
								sheet.addHyperlink(new WritableHyperlink(cell.getColumn(), targetRow,hyperlink.getFile(),cell.getContents()));
								//To remove the hyperlink from template row
								sheet.removeHyperlink((WritableHyperlink) hyperlink,true);
								//Restoring the old formatting of the cell
								WritableCellFormat wcf=new WritableCellFormat();
							    wcf.setBackground(cell.getCellFormat().getBackgroundColour());
							    sheet.addCell(new Label(cell.getColumn(),cell.getRow(),"",wcf));							    
								break;
							}
						}
					}
					
					Label wc = (Label) cell;
					Label wc1 = (Label) wc.copyTo(cell.getColumn(), targetRow);
					wc1.setCellFormat(wc.getCellFormat());
					//if(!wc1.getContents().matches("\\d+"))
					/*else
					{
						int wc2 = Integer.parseInt(wc1.getContents());
					sheet.
						sheet.addCell(wc2);
						
					}*/
					sheet.addCell(wc1);
				}catch(RowsExceededException e){
					logger.handleError("Error while copying Label cell", e);
				}catch(WriteException e){
					logger.handleError("Error while writing to Label cell",targetRow,cell.getColumn(), e);
				}
				break;
			case NUMBER:
				try{
					Number wc = new Number((NumberCell)cell);
					Number wc1 = (Number) wc.copyTo(cell.getColumn(), targetRow);
					sheet.addCell(wc1);
				}catch(Exception e){
					logger.handleError("Error while copying Number cell", e);
				}
				break;
			case NUMERICALFORMULA:
				try{
					Number wc = new Number((NumberCell)cell);
					Number wc1 = (Number) wc.copyTo(cell.getColumn(), targetRow);
					sheet.addCell(wc1);
				}catch(Exception e){
					logger.handleError("Error while copying Number Formula cell",targetRow,cell.getColumn(), e);
				}
				break;
			case DATE:
				try{
					jxl.write.DateFormat date=new jxl.write.DateFormat("dd-mm-yyyy hh:mm:ss");
					DateTime dateTime=new DateTime((DateCell)cell);
					dateTime.setCellFormat(new WritableCellFormat(date));
					DateTime dateTime1=(DateTime)dateTime.copyTo(cell.getColumn(), targetRow);
					dateTime1.setCellFormat(cell.getCellFormat());
					sheet.addCell(dateTime1);
				}catch(Exception e){
					logger.handleError("Error while copying Date cell",targetRow,cell.getColumn(), e);
				}
				break;
			case DATEFORMULA:
				try{
					String prefix="";
					if(sheet.getName().equals("Details")){
						prefix="DtlStepFtr";
					}else if(sheet.getName().equals("Summary")){
						prefix="Sum";
					}
					//Creating the formula
					String startTimeCol=CellReferenceHelper.getColumnReference(findCellByName(prefix+"StartTime").getColumn());
					String endTimeCol=CellReferenceHelper.getColumnReference(findCellByName(prefix+"FinishTime").getColumn());
					String formula=endTimeCol+(targetRow+1)+"-"+startTimeCol+(targetRow+1);
					//Adding the formula to the cell
					Formula formulaCell = new Formula(cell.getColumn(), targetRow,formula);
					formulaCell.setCellFormat(new WritableCellFormat(new jxl.write.DateFormat("[hh]:mm:ss")));
					formulaCell.setCellFormat(cell.getCellFormat());
					sheet.addCell(formulaCell);
				}catch(Exception e){
					logger.handleError("Error while copying Date cell",targetRow,cell.getColumn(), e);
				}
				break;
		}
	}
	
	private void clearContents(String tplRowName, WritableSheet sheet){
		Range range = oWorkbook.findByName(tplRowName)[0];
		int startRow = range.getTopLeft().getRow();
		int endRow = range.getBottomRight().getRow();
		int startCol = range.getTopLeft().getColumn();
		int endCol = range.getBottomRight().getColumn();
		
		Cell cells[] = new Cell[endCol+1];
		int index = 0;
		for(int row = startRow;row<=endRow;row++){	// FIXME: as in earlier method
			for(int col=startCol;col<=endCol;col++){
				cells[index] = sheet.getCell(col, row);
				index++;
			}
			try{
				for (int i = 0; i < cells.length; i++) {
					if(cells[i].getType().toString().contains("Formula")){
						logger.trace("Do not clear cell content as cell has formula");
					}else{
						clearCell(cells[i], sheet);
					}
					index=0;
				}
			}catch(Exception e){
				logger.handleError("Error while clearing result row contents", e);
			}
		}
		
	}
	
	private void clearCell(Cell cell, WritableSheet sheet) {
		switch(cellType.valueOf(cell.getType().toString().replaceAll("\\W", "").toUpperCase())){
			case LABEL:
				try{
					Label wc = (Label) cell;
					wc.setString("");
				}catch(Exception e){
					logger.handleError("Error while clearing Label cell", e);
				}
				break;
			case NUMBER:
				try{
					Number wc = new Number((NumberCell)cell);
					sheet.addCell(wc);
					wc.setValue(0.0);
				}catch(Exception e){
					logger.handleError("Error while clearing Number cell", e);
				}
				break;
			case NUMERICALFORMULA:
				try{
					Number wc = new Number((NumberCell)cell);
					sheet.addCell(wc);
					wc.setValue(0.0);
				}catch(Exception e){
					logger.handleError("Error while clearing Number Formula cell", e);
				}
				break;
			case DATE:
				try{
					DateTime wc = new DateTime((DateCell)cell);
					sheet.addCell(wc);
					wc.setDate(new Date("01-Jan-01"));
				}catch(Exception e){
					logger.handleError("Error while clearing Date cell", e);
				}
				break;
			case DATEFORMULA:
				try{
					DateTime wc = new DateTime((DateCell)cell);
					sheet.addCell(wc);
					wc.setDate(new Date("01-Jan-01"));
				}catch(Exception e){
					logger.handleError("Error while clearing Date cell", e);
				}
				break;
		}
	}
	
	private WritableCell findCellByName(String name){
		WritableCell cell=oWorkbook.findCellByName(name);
		if((cell.getType()!=CellType.EMPTY)||!(cell.getColumn()==0 && cell.getRow()==0))
			return cell;
		else 
			return null;
	}
	private Map toMap(Object[] arr) {
		return Utils.toMap(arr);
	}
	
	/**
	 * Overrides toString() method of Object class to return ExcelReporter format string
	 */
	public String toString() {
		return "ExcelReporter(" + filePath + ")";
	}
	
	private static enum cellType{
		EMPTY,
		LABEL,
		NUMBER,
		NUMERICALFORMULA,
		DATE,
		DATEFORMULA,
	};
	private String SHN_SUMMARY, SHN_DETAILS, SHN_COVER,LINK_COL;
	private String filePath, templatePath;
	private LogUtils logger = new LogUtils(this);
			
}