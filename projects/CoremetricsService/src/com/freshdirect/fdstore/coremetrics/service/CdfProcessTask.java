package com.freshdirect.fdstore.coremetrics.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.ProductContainer;
import com.freshdirect.fdstore.coremetrics.builder.PageViewTagModelBuilder;
import com.freshdirect.fdstore.coremetrics.builder.PageViewTagModelBuilder.CustomCategory;
import com.freshdirect.framework.util.RuntimeServiceUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CdfProcessTask {
	
	private static final Logger LOGGER = LoggerFactory.getInstance(CdfProcessTask.class);
	
	private List<CdfRowModel> cdfRowModels = new ArrayList<CdfRowModel>();
	private String cdfFilePath;
	private String cdfFileName;
	
	public CdfProcessResult process(){
		generateCdfModel();
		try {
			saveCdfFile();
			uploadFile();
		
		} catch (FDResourceException e) {
			LOGGER.error("CdfProcessTask failed",e);
			return new CdfProcessResult(false, e.getMessage());
		}
		
		return new CdfProcessResult(true, null);
	}
	
	private void generateCdfModel(){

		for (String catIdDir : FDStoreProperties.getCoremetricsCatIdDirs().split(",")) {
			addCmPageViewTagCategory(catIdDir);
		}

		addCmPageViewTagCategory(FDStoreProperties.getCoremetricsCatIdBlog());
		addCmPageViewTagCategory(FDStoreProperties.getCoremetricsCatIdOtherPage());

		for (CustomCategory category : PageViewTagModelBuilder.CustomCategory.values()){
			addCmPageViewTagCategory(category.toString());
		}
		
		for (DepartmentModel dept : ContentFactory.getInstance().getStore().getDepartments()) {
			processCmsCategory(dept, null);
		}
	}
	
	private void addCmPageViewTagCategory(String catId){
		cdfRowModels.add(new CdfRowModel(catId, "Category: " + catId, null));
	}
	
	private void processCmsCategory(ProductContainer cat, String parentCatId) {
		
		String catId = cat.getContentKey().getId();
		cdfRowModels.add(new CdfRowModel(catId, cat.getFullName(), parentCatId));
		
		List<CategoryModel> subCats = cat.getSubcategories();
		if ( subCats != null) {
			for (ProductContainer subCat : subCats) {
				processCmsCategory(subCat, catId);
			}
		}
	}
	
	private void saveCdfFile() throws FDResourceException {
		String rootDirectory =  RuntimeServiceUtil.getInstance().getRootDirectory();
		cdfFileName = "CDF_" + FDStoreProperties.getCoremetricsClientId() + ".csv";
		cdfFilePath = rootDirectory + File.separator + cdfFileName;
		LOGGER.info("saving Coremetrics CDF to " + cdfFilePath);
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new FileWriter(cdfFilePath));
			
			for (CdfRowModel cdfRowModel : cdfRowModels){
				writer.println(cdfRowModel.toString());
			}
			
			writer.close();
		} catch (IOException e) {
			throw new FDResourceException("saveCdfFile failed", e);
		
		} finally {
			if (writer != null){
				writer.close();
			}
		}
	}
	
	private void uploadFile() throws FDResourceException {
		
		String ftpUrl = FDStoreProperties.getCoremetricsFtpUrl();
		String ftpUser = FDStoreProperties.getCoremetricsClientId() + "-import";
		
		LOGGER.info("uploading Coremetrics CDF to " + ftpUser +"@"+ ftpUrl);
		FTPClient client = new FTPClient();
		client.setDefaultTimeout(600000);
		client.setDataTimeout(600000);
        FileInputStream fis = null;
        
        try {
            client.connect(ftpUrl);
            
            if (!client.login(ftpUser, FDStoreProperties.getCoremetricsFtpPassword())) {
            	throw new FDResourceException("ftp login failed"); 
            }
            
            fis = new FileInputStream(cdfFilePath);
            if (!client.storeFile(cdfFileName, fis)) {
            	throw new FDResourceException("ftp file store failed");
            }
            
            client.logout();

        } catch (IOException e) {
            throw new FDResourceException("uploadFile", e);
            
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                client.disconnect();
            } catch (IOException e) {
            }
        }
    }
}
