package com.freshdirect.mktAdmin.util;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Category;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mktAdmin.constants.EnumCompetitorType;
import com.freshdirect.mktAdmin.constants.EnumFileContentType;
import com.freshdirect.mktAdmin.dao.oracle.OracleMarketAdminDAOImpl;
import com.freshdirect.mktAdmin.exception.MktAdminApplicationException;
import com.freshdirect.mktAdmin.exception.MktAdminSystemException;
import com.freshdirect.mktAdmin.model.CompetitorAddressModel;
import com.freshdirect.mktAdmin.model.CustomerAddressModel;
import com.freshdirect.mktAdmin.model.FileDownloadBean;
import com.freshdirect.mktAdmin.model.FileUploadBean;

public class ExcelFileParser implements FileParser {

	private final static Category LOGGER = LoggerFactory.getInstance(ExcelFileParser.class);
	
	public Collection parseFile(FileUploadBean fileUploadBean) throws MktAdminApplicationException {
		// TODO Auto-generated method stub
		Collection modelCollection=null;
		 try {
	
			 if(EnumFileContentType.COMPETITOR_FILE_TYPE.getName().equalsIgnoreCase(fileUploadBean.getFileContentType().getName())){
				 modelCollection=loadCompetitirExcelFileContents(fileUploadBean.getBytes());
			 }
			 else{
				 modelCollection=loadCustomerExcelFileContents(fileUploadBean.getBytes());
			 }
			 			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block				
			throw new MktAdminSystemException("1001",e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new MktAdminSystemException("1001",e);
		}
		        		
		return modelCollection;
	}
	
	private Collection loadCustomerExcelFileContents(byte file[]) throws FileNotFoundException, IOException, MktAdminApplicationException{
		InputStream input=null;
		POIFSFileSystem fs=null;
		List modelList=null;
		try
		{
			input = new ByteArrayInputStream(file);
			fs = new POIFSFileSystem(input);
			HSSFWorkbook wb = new HSSFWorkbook(fs);			   
	        modelList=new ArrayList();
	
	               for (int k = 0; k < wb.getNumberOfSheets(); k++)
	               {
	            	   LOGGER.debug("Sheet " + k);
	                   HSSFSheet sheet = wb.getSheetAt(k);
	                   int       rows  = sheet.getPhysicalNumberOfRows();
	                   LOGGER.debug("rows :"+rows);
	                   for (int r = 0; r < rows; r++)
	                   {                	   
	                       HSSFRow row   =  sheet.getRow(r);	                       	                       	                       
	                       if(r==0 ){
	                    	   try{
	                    		   validateCustomerExcelFileColumns(row);   
	                    	   }catch(MktAdminApplicationException e){
	                    		   if(r==0 && "104".equalsIgnoreCase(e.getErrorCode())){
	                    			   // could be first row is empty. stupid uploaded file. lets see another one
	                    			   r++;
	                    			   HSSFRow rowOne   =  sheet.getRow(r);
	                    			   validateCustomerExcelFileColumns(rowOne);
	                    			   continue;
	                    		   }
	                    	   }
	                    	   
	                    	   continue;
	                       }
	                       
	                       if(row==null) 
	                    	   continue; 
	                       
	                       CustomerAddressModel model=new CustomerAddressModel();                       
	                                              
	                       HSSFCell nameCell  = row.getCell((short)0);
	                       
	                       if(nameCell.getStringCellValue()==null || nameCell.getStringCellValue().trim().length()==0){
	                    	   continue;
	                       }
	                       
	                       model.setCustomerId(nameCell.getStringCellValue());
	                       
	                       HSSFCell address1Cell  = row.getCell((short)1);                                                                             
	                       model.setAddress1(address1Cell.getStringCellValue());
	                       
	                       HSSFCell address2Cell  = row.getCell((short)2);                                                                             
	                       model.setAddress2(address2Cell.getStringCellValue());
	                       
	                       HSSFCell aptCell  = row.getCell((short)3);                                                                             
	                       model.setApartment(aptCell.getStringCellValue());

	
	                       HSSFCell cityCell  = row.getCell((short)4);                                                                             
	                       model.setCity(cityCell.getStringCellValue());
	
	                       HSSFCell stateCell  = row.getCell((short)5);                                                                             
	                       model.setState(stateCell.getStringCellValue());
	
	                       HSSFCell zipCell  = row.getCell((short)6);
	                       //model.setZipCode(zipCell.getStringCellValue());
	                       
	                       if(HSSFCell.CELL_TYPE_NUMERIC==zipCell.getCellType()){
	                          model.setZipCode(MarketAdminUtil.getFormatedZipCode(""+(int)zipCell.getNumericCellValue()));
	                       }else{
	                    	   model.setZipCode(MarketAdminUtil.getFormatedZipCode(zipCell.getStringCellValue()));
	                       }
	                       
	                       modelList.add(model);
	                   }
	               }
				}finally
				{
					try{
					if(input!=null) input.close();
					}catch(IOException ignore){}
				}		
               return modelList;
	}
	
	private Collection loadCompetitirExcelFileContents(byte file[]) throws FileNotFoundException, IOException, MktAdminApplicationException{
		
		InputStream input=null;
		POIFSFileSystem fs=null;
		List modelList=null;
		try
		{
			input = new ByteArrayInputStream(file);
			fs = new POIFSFileSystem(input);
			HSSFWorkbook wb = new HSSFWorkbook(fs);			   
	        modelList=new ArrayList();
	
	               for (int k = 0; k < wb.getNumberOfSheets(); k++)
	               {
	            	   LOGGER.debug("Sheet " + k);
	                   HSSFSheet sheet = wb.getSheetAt(k);
	                   int       rows  = sheet.getPhysicalNumberOfRows();
	                   LOGGER.debug("rows " + rows);
	                   for (int r = 0; r < rows; r++)
	                   {                	   
	                       HSSFRow row   =  sheet.getRow(r);	                       	                       	                       
	                       if(r==0 ){
	                    	   try{
	                    		   validateCompetitorExcelFileColumns(row);   
	                    	   }catch(MktAdminApplicationException e){
	                    		   if(r==0 && "104".equalsIgnoreCase(e.getErrorCode())){
	                    			   // could be first row is empty. stupid uploaded file. lets see another one
	                    			   r++;
	                    			   HSSFRow rowOne   =  sheet.getRow(r);
	                    			   validateCompetitorExcelFileColumns(rowOne);
	                    			   continue;
	                    		   }
	                    	   }
	                    	   
	                    	   continue;
	                       }
	                       	                       
	                       
	                       if(row==null) 
	                    	   continue; 
	                       
	                       CompetitorAddressModel model=new CompetitorAddressModel();                       
	                                              
	                       HSSFCell nameCell  = row.getCell((short)0);	                       
	                       if(nameCell.getStringCellValue()==null || nameCell.getStringCellValue().trim().length()==0){
	                    	   continue;
	                       }
	                       
	                       model.setCompanyName(nameCell.getStringCellValue());
	                       
	                       HSSFCell addressCell  = row.getCell((short)1);                                                                             
	                       model.setAddress1(addressCell.getStringCellValue());
	
	                       HSSFCell cityCell  = row.getCell((short)2);                                                                             
	                       model.setCity(cityCell.getStringCellValue());
	
	                       HSSFCell stateCell  = row.getCell((short)3);                                                                             
	                       model.setState(stateCell.getStringCellValue());
	
	                       HSSFCell zipCell  = row.getCell((short)4);
	                       //model.setZipCode(zipCell.getStringCellValue());
	                       if(HSSFCell.CELL_TYPE_NUMERIC==zipCell.getCellType()){
	                          model.setZipCode(MarketAdminUtil.getFormatedZipCode(""+(int)zipCell.getNumericCellValue()));
	                       }else{
	                    	   model.setZipCode(MarketAdminUtil.getFormatedZipCode(zipCell.getStringCellValue()));
	                       }
	                       HSSFCell levelCell  = row.getCell((short)5);                                                                             
	                       model.setCompetitorType(EnumCompetitorType.getEnum(levelCell.getStringCellValue().toUpperCase()));
	
	                       modelList.add(model);
	                   }
	               }
				}finally
				{
					try{
					if(input!=null) input.close();
					}catch(IOException ignore){}
				}		
               return modelList;
	}
	
	
	
	
          
          	
     public void validateCompetitorExcelFileColumns(HSSFRow row) throws MktAdminApplicationException{
    	 System.out.println("validateExcelFileColumns :"+row);    	 
    	 if(row.getLastCellNum()<6){
    		throw new MktAdminApplicationException("104",new String[]{"6"});
    	 }
         HSSFCell nameCell  = row.getCell((short)0);
         System.out.println("nameCell.getStringCellValue()"+nameCell.getStringCellValue());
         if(!"Name".equalsIgnoreCase(nameCell.getStringCellValue())){
        	 throw new MktAdminApplicationException("105",new String[]{"First","Name"});
         }
         HSSFCell addressCell  = row.getCell((short)1);
         System.out.println("nameCell.getStringCellValue()"+nameCell.getStringCellValue());
         if(!"Address".equalsIgnoreCase(addressCell.getStringCellValue())){
        	 throw new MktAdminApplicationException("105",new String[]{"Second","Address"});
         }
         HSSFCell cityCell  = row.getCell((short)2);
         if(!"City".equalsIgnoreCase(cityCell.getStringCellValue())){
        	 throw new MktAdminApplicationException("105",new String[]{"Third","City"});
         }
         HSSFCell stateCell  = row.getCell((short)3);
         if(!"State".equalsIgnoreCase(stateCell.getStringCellValue())){
        	 throw new MktAdminApplicationException("105",new String[]{"Fourth","State"});
         }
         HSSFCell zipCell  = row.getCell((short)4);
         if(!"zip".equalsIgnoreCase(zipCell.getStringCellValue())){
        	 throw new MktAdminApplicationException("105",new String[]{"Fifth","Zip"});
         }
         HSSFCell levelCell  = row.getCell((short)5);
         if(!"Level Of Competition".equalsIgnoreCase(levelCell.getStringCellValue())){
        	 throw new MktAdminApplicationException("105",new String[]{"Sixth","Level Of Competition"});
         }
    }
     
     public void validateCustomerExcelFileColumns(HSSFRow row) throws MktAdminApplicationException{
    	 System.out.println("validateExcelFileColumns :"+row);    	 
    	 if(row.getLastCellNum()<6){
    		throw new MktAdminApplicationException("104",new String[]{"6"});
    	 }
         HSSFCell nameCell  = row.getCell((short)0);
         System.out.println("nameCell.getStringCellValue()"+nameCell.getStringCellValue());
         if(!"CustomerId".equalsIgnoreCase(nameCell.getStringCellValue())){
        	 throw new MktAdminApplicationException("105",new String[]{"First","CustomerId"});
         }
         HSSFCell address1Cell  = row.getCell((short)1);
         System.out.println("nameCell.getStringCellValue()"+nameCell.getStringCellValue());
         if(!"Address1".equalsIgnoreCase(address1Cell.getStringCellValue())){
        	 throw new MktAdminApplicationException("105",new String[]{"Second","Address1"});
         }
         
         HSSFCell address2Cell  = row.getCell((short)2);
         System.out.println("nameCell.getStringCellValue()"+nameCell.getStringCellValue());
         if(!"Address2".equalsIgnoreCase(address2Cell.getStringCellValue())){
        	 throw new MktAdminApplicationException("105",new String[]{"Second","Address2"});
         }

         HSSFCell aptCell  = row.getCell((short)3);
         System.out.println("nameCell.getStringCellValue()"+nameCell.getStringCellValue());
         if(!"Apartment".equalsIgnoreCase(aptCell.getStringCellValue())){
        	 throw new MktAdminApplicationException("105",new String[]{"Second","Apartment"});
         }

         
         HSSFCell cityCell  = row.getCell((short)4);
         if(!"City".equalsIgnoreCase(cityCell.getStringCellValue())){
        	 throw new MktAdminApplicationException("105",new String[]{"Third","City"});
         }
         HSSFCell stateCell  = row.getCell((short)5);
         if(!"State".equalsIgnoreCase(stateCell.getStringCellValue())){
        	 throw new MktAdminApplicationException("105",new String[]{"Fourth","State"});
         }
         HSSFCell zipCell  = row.getCell((short)6);
         if(!"zip".equalsIgnoreCase(zipCell.getStringCellValue())){
        	 throw new MktAdminApplicationException("105",new String[]{"Fifth","Zip"});
         }
    }
	
	
	public static void main(String args[]) throws MktAdminApplicationException
	{
	    //ExcelFileParser parser=new ExcelFileParser();
	    //parser.parseFile(null);	    	    
	}

	public String generateFile(FileDownloadBean fileUploadBean) throws MktAdminApplicationException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
