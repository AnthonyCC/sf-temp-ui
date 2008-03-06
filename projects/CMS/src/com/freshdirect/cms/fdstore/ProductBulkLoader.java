package com.freshdirect.cms.fdstore;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.CmsException;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.RelationshipI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.CmsRequest;
import com.freshdirect.cms.application.CmsUser;
import com.freshdirect.cms.node.ContentNodeUtil;
import com.freshdirect.framework.util.StringUtil;

public class ProductBulkLoader {
	/**
	 * Performs a bulkload on the XLS file.
	 * 
	 * @param is XLS file as input stream
	 * @param userId user id
	 * @param successes (RETURN) the list of product keys successfully inserted, or if null nothing
	 * @param failures (RETURN) a map of product keys to exception messages of product keys that failed, or if null nothing
	 */
	// TODO refactor it to ProductBulkLoader class
	public static void XLSBulkLoad(InputStream is, String userId, List successes, Map failures) throws IOException {
		//open file
	
		POIFSFileSystem fs = new POIFSFileSystem(is);
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		HSSFSheet sheet = wb.getSheetAt(0);
	
		// Iterate over each row in the sheet
		Iterator rows = sheet.rowIterator();
		
		Map attributeColumns = new HashMap();
		CmsRequest request = new CmsRequest(new CmsUser(userId));
		while (rows.hasNext()) {
			HSSFRow row = (HSSFRow) rows.next();
	
			if (row.getRowNum() == 0) {
				
				Map columnMap = new HashMap();
	
				Iterator cells = row.cellIterator();
				while (cells.hasNext()) {
					HSSFCell cell = (HSSFCell) cells.next();
	
					if (cell.getCellNum() == 0) {
						columnMap.put(new Integer(0), "PRODUCT_ID");
					} else {
						columnMap.put(new Integer(cell.getCellNum()), cell.getStringCellValue().toUpperCase());
					}
				}
				attributeColumns = columnMap;
				
				continue;
			}
			
			//create product
			String prodKeyString = row.getCell((short) 0).getStringCellValue();
			try {
				// validate and create key
				ContentKey prodKey = ContentKey.create(FDContentTypes.PRODUCT, prodKeyString);

				if(prodKey.lookupContentNode() != null){
					throw new CmsException("Duplicate Node " + prodKey);
				}

				ContentNodeI prod = CmsManager.getInstance().createPrototypeContentNode(prodKey);


				Iterator cells = row.cellIterator();
				while (cells.hasNext()) {
					HSSFCell cell = (HSSFCell) cells.next();
	
					if (cell.getCellNum() != 0 && cell.getCellType() != HSSFCell.CELL_TYPE_BLANK) {
						String attrName = (String) attributeColumns.get(new Integer(cell.getCellNum()));
	
						AttributeI attr = prod.getAttribute(attrName);
						if (attr == null) {
							attr = prod.getAttribute(attrName.toLowerCase());
						}
						
						if(attrName.toLowerCase().equals("skus")){
							ContentKey key = new ContentKey(FDContentTypes.SKU, cell.getStringCellValue());
			                
							ContentNodeUtil.createNode(key, request, attr);
						}	
						
						if(attrName.toLowerCase().equals("brands")){
							String fullname = cell.getStringCellValue();
			                String tmpBrandName = StringUtil.adjustAlphaHTMLEntities(fullname.toLowerCase().trim());
							int strLen=( tmpBrandName.length()>20?21:tmpBrandName.length() );
	
			                String id = "bd_"+ProductBulkLoader.removeNonAlpha(tmpBrandName.substring(0,strLen));
			                ContentKey key = new ContentKey(FDContentTypes.BRAND, id);
			                
			                ContentNodeUtil.createNode(key, request, attr);
							
						} else if (attr instanceof RelationshipI)
							ContentNodeUtil.setRelationshipValue(cell.getStringCellValue(), attr);
						else {
							attr.setValue(cell.getStringCellValue());
						}
					}
				}
	
				request.addNode(prod);
				if (successes != null) successes.add(prod.getKey());
			} catch (Exception e) {
				e.printStackTrace();
				if (failures != null) failures.put(prodKeyString, (e.getMessage() == null ? e.getClass().toString() : e.getMessage()));
				continue;
			}
	
		}
		CmsManager.getInstance().handle(request);
	}



	public static String removeNonAlpha(String inComing) {
	    // convert the html entitied
	    if (inComing==null || inComing.length()<1) return inComing;
	    String wrkString = StringUtil.removeHTMLEntities(inComing);
	    StringBuffer outGoing = new StringBuffer();
	    boolean lastCharNotValid=false;
	    for (int charIdx=0;charIdx<wrkString.length();charIdx++) {
	        char oneChar = wrkString.charAt(charIdx);
	        if (lastCharNotValid  && (Character.isWhitespace(oneChar) || !Character.isLetterOrDigit(oneChar))) continue;
	        if ((!Character.isWhitespace(oneChar) && !Character.isLetterOrDigit(oneChar))) continue; //skip this
	        if (Character.isWhitespace(oneChar)) {
	            lastCharNotValid=true;
	            oneChar = '_';
	        } else {
	            lastCharNotValid = false;
	        }
	        outGoing.append(oneChar);
	    }
	    //LOGGER.info("brndContentName in: ["+inComing+"]  Out:["+outGoing.toString()+"]");
	    return outGoing.toString();
	
	}

}
