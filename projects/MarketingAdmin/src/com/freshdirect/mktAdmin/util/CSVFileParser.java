package com.freshdirect.mktAdmin.util;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Category;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import com.freshdirect.fdstore.promotion.FDPromotionModelFactory;
import com.freshdirect.fdstore.promotion.management.FDPromotionModel;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mktAdmin.constants.EnumFileContentType;
import com.freshdirect.mktAdmin.exception.MktAdminApplicationException;
import com.freshdirect.mktAdmin.exception.MktAdminSystemException;
import com.freshdirect.mktAdmin.model.FileDownloadBean;
import com.freshdirect.mktAdmin.model.FileUploadBean;
import com.freshdirect.mktAdmin.model.RestrictedPromoCustomerModel;
import com.freshdirect.mktAdmin.model.RestrictionListUploadBean;

public class CSVFileParser implements FileParser {

	private final static Category LOGGER = LoggerFactory.getInstance(CSVFileParser.class);
	
	private static final String FILE_COLUMN_HEADER[]=new String[]{"CUSTOMER_FDID","EMAIL_ADDRESS","FIRST_NAME","LAST_NAME"};
	
	public Collection parseFile(FileUploadBean fileUploadBean) throws MktAdminApplicationException {
		// TODO Auto-generated method stub
		Collection modelCollection=null;
		 try {
	         LOGGER.debug("inside CSVFileParser");
			 if(EnumFileContentType.RESTRICTION_LIST_FILE_TYPE.getName().equalsIgnoreCase(fileUploadBean.getFileContentType().getName())){
				 modelCollection=loadrestrictedCustomerCSVFileContents((RestrictionListUploadBean)fileUploadBean);
			 }
			 else{
				 throw new MktAdminApplicationException("1001",new String[]{EnumFileContentType.RESTRICTION_LIST_FILE_TYPE.getDescription()});
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

	private Collection loadrestrictedCustomerCSVFileContents(RestrictionListUploadBean fileUploadBean) throws FileNotFoundException, IOException, MktAdminApplicationException{
		InputStream input=null;
		POIFSFileSystem fs=null;
		Set modelList=null;
		try
		{
						
			input = new ByteArrayInputStream(fileUploadBean.getBytes());			
			//FileReader reader=new FileReader(new InputStreamReader(input));
			CSVReader reader=new CSVReader(new InputStreamReader(input));
			//fs = new POIFSFileSystem(input);
			modelList=new HashSet();
			String [] nextLine;
			FDPromotionModel fdModel=FDPromotionModelFactory.getInstance().getPromotion(fileUploadBean.getPromotionCode());
			String promotionId=fdModel.getId();
			boolean isHearderRead=false;
		        while ((nextLine = reader.readNext()) != null) {
		        // nextLine[] is an array of values from the line
 	        		   //LOGGER.debug(nextLine[0] + nextLine[1] + "etc...");		    					    	                       	                       	                       	                       
                       if(!isHearderRead){
                    	   isHearderRead=true;	                    	   
                    	   validateRestrictedCustomerCSVFileColumns(nextLine);
                    	   continue;
                       }
	                       
                       if(nextLine==null) 
                    	   continue; 
	                       
                       RestrictedPromoCustomerModel model=new RestrictedPromoCustomerModel();                       	                                                                     
	                       
                       if(nextLine[0]==null || nextLine[0].trim().length()<2){                    	  
                    	   continue;
                       }
	                       
                       model.setCustomerId(nextLine[0]);                                              
	                                                                                                                           
                       model.setCustEmailAddress(nextLine[1]);
	                                                                                                                           
                       model.setFirstName(nextLine[2]);
                                                                             
                       model.setLastName(nextLine[3]);
                       
                       model.setPromotionId(promotionId);
	                       
                       modelList.add(model);                    
		          }
		        
				}finally
				{
					try{
					if(input!=null) input.close();
					}catch(IOException ignore){}
				}		
               return modelList;
	}

    public void validateRestrictedCustomerCSVFileColumns(String  columnNames[]) throws MktAdminApplicationException{
   	 System.out.println("validateCSVFileColumns :"+columnNames);    	 
   	 if(columnNames.length<4){
   		throw new MktAdminApplicationException("118",new String[]{"4"});
   	 }
        
        System.out.println("nameCell.getStringCellValue()"+columnNames[0]);
        if(!FILE_COLUMN_HEADER[0].equalsIgnoreCase(columnNames[0])){
       	 throw new MktAdminApplicationException("119",new String[]{"First",FILE_COLUMN_HEADER[0]});
        }
        
        System.out.println("nameCell.getStringCellValue()"+columnNames[1]);
        if(!FILE_COLUMN_HEADER[1].equalsIgnoreCase(columnNames[1])){
       	 throw new MktAdminApplicationException("119",new String[]{"Second",FILE_COLUMN_HEADER[1]});
        }
        
        if(!FILE_COLUMN_HEADER[2].equalsIgnoreCase(columnNames[2])){
       	 throw new MktAdminApplicationException("119",new String[]{"Third",FILE_COLUMN_HEADER[2]});
        }
        
        if(!FILE_COLUMN_HEADER[3].equalsIgnoreCase(columnNames[3])){
       	 throw new MktAdminApplicationException("119",new String[]{"Fourth",FILE_COLUMN_HEADER[3]});
        }
   }

	public static void main(String args[]){
//		File file=new File();
//		ByteArrayInputStream inp=new ByteArrayInputStream(file);
//		
//		CSVFileParser parser=new CSVFileParser(i.)
		
	}

	public String generateFile(FileDownloadBean fileUploadBean) throws MktAdminApplicationException {
		// TODO Auto-generated method stub
		//create String[] of each elements id, email, first name and last name
		LOGGER.debug("IN CSVPARSER collection size"+fileUploadBean.getFileContents().size());
		List fileContentList=new ArrayList();
		Iterator iterator=fileUploadBean.getFileContents().iterator();
		fileContentList.add(FILE_COLUMN_HEADER);
		while(iterator.hasNext()){
			RestrictedPromoCustomerModel model=(RestrictedPromoCustomerModel)iterator.next();
			String line[]=new String[4];
			line[0]=model.getCustomerId();
			line[1]=model.getCustEmailAddress();
			line[2]=model.getFirstName();
			line[3]=model.getLastName();
			fileContentList.add(line);
		}
		StringWriter sw = new StringWriter();
		CSVWriter writer=null;
		try{
		writer = new CSVWriter(sw);
		writer.writeAll(fileContentList);
		}finally{
			try {
				writer.close();
			} catch (IOException ignore) {
				// TODO Auto-generated catch block				
			}
		}
		//
		return sw.toString();
	}
	
}
