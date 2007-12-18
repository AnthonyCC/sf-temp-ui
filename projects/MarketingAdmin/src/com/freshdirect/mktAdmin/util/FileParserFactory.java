package com.freshdirect.mktAdmin.util;

import com.freshdirect.mktAdmin.constants.EnumFileType;
import com.freshdirect.mktAdmin.exception.MktAdminApplicationException;

public class FileParserFactory {
	
	public static FileParser getFileParser(String fileType) throws MktAdminApplicationException{		
		System.out.println("fileType :"+fileType);			
		
		if(EnumFileType.CSV_FILE_TYPE.getName().equalsIgnoreCase(fileType))
		{
			return new CSVFileParser();
		}
		else if(EnumFileType.EXCEL_FILE_TYPE.getName().equalsIgnoreCase(fileType))
		{
			return new ExcelFileParser();
		}
		// not supported filetype
			throw new MktAdminApplicationException("103",new String[]{"EXCEL OR CSV"});


	}
	
}
