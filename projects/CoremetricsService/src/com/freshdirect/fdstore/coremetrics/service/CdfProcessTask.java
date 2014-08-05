package com.freshdirect.fdstore.coremetrics.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;

import com.freshdirect.affiliate.ExternalAgency;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.ProductContainer;
import com.freshdirect.fdstore.content.SuperDepartmentModel;
import com.freshdirect.fdstore.coremetrics.builder.PageViewTagModelBuilder;
import com.freshdirect.fdstore.coremetrics.builder.PageViewTagModelBuilder.CustomCategory;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.event.EnumEventSource;
import com.freshdirect.framework.util.RuntimeServiceUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

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
		
		LOGGER.info("CdfProcessTask complete");
		return new CdfProcessResult(true, null);
	}
	
	private void generateCdfModel(){

		Set<String> categoryKeys = new HashSet<String>(); 

		// [APPDEV-2907] Add mobile keys
		addMobileKeys();

		for (String catIdDir : FDStoreProperties.getCoremetricsCatIdDirs().split(",")) {
			addCmPageViewTagCategory(catIdDir, categoryKeys);
		}

		addCmPageViewTagCategory(FDStoreProperties.getCoremetricsCatIdBlog(), categoryKeys);
		addCmPageViewTagCategory(FDStoreProperties.getCoremetricsCatIdOtherPage(), categoryKeys);

		for (CustomCategory category : PageViewTagModelBuilder.CustomCategory.values()){
			addCmPageViewTagCategory(category.toString(), categoryKeys);
		}
		
		//event source used in shop tags as category
		for (EnumEventSource eventEnum : EnumEventSource.values()){
			addCmPageViewTagCategory(((EnumEventSource) eventEnum).toString(), categoryKeys);
		}
		
		// [APPDEV-2907] site feature used in shop tags as category
		addSiteFeatureKeys(EnumSiteFeature.getEnumList());

		//external agency, e.g. Foodily
		for (ExternalAgency externalAgency : ExternalAgency.values()){
			addCmPageViewTagCategory(externalAgency.toString(), categoryKeys);
		}
		
		
		//super department flow
		Set<DepartmentModel> processedDepartments = new HashSet<DepartmentModel>();
		
		for (ContentKey superDeptKey : CmsManager.getInstance().getContentKeysByType(ContentType.get("SuperDepartment"))) {
			ContentNodeModel superDeptNode = ContentFactory.getInstance().getContentNodeByKey(superDeptKey);
			
			if (superDeptNode instanceof SuperDepartmentModel) {
				SuperDepartmentModel superDept = (SuperDepartmentModel) superDeptNode;
				String superDeptId = superDept.getContentName();
				cdfRowModels.add(new CdfRowModel(superDeptId, superDept.getFullName(), null));

				for (DepartmentModel dept : superDept.getDepartments()){
					if (processedDepartments.add(dept)){
						processCmsCategory(dept, superDeptId);
					}
				}
			}
		}
		
		//remaining departments
		for (DepartmentModel dept : ContentFactory.getInstance().getStore().getDepartments()) {
			if (!processedDepartments.contains(dept)) {
				processCmsCategory(dept, null);
			}
		}
	}
	
	private void addCmPageViewTagCategory(String catId, Set<String> categoryKeys){
		
		if (categoryKeys.add(catId.toUpperCase())) {
			cdfRowModels.add(new CdfRowModel(catId, "Category: " + catId, null));
		}
	}

	private void addSiteFeatureKeys(Collection<EnumSiteFeature> siteFeatures) {
		for (EnumSiteFeature f : siteFeatures) {
			final String key = f.getName();
			cdfRowModels.add(new CdfRowModel(key, "SmartStore: " + key, null));
		}
	}
	
	private void addMobileKeys() {
		// [id],MOBILE,"MOBILE",      <---- there is no parent MOBILE category today
		// [id],MOBILE_OTHER,"MOBILE OTHER",MOBILE
		// [id],MOBILE_ALL,"MOBILE ALL",MOBILE

		cdfRowModels.add(new CdfRowModel("MOBILE", "Category: MOBILE", null));
		cdfRowModels.add(new CdfRowModel("MOBILE_OTHER", "Category: MOBILE_OTHER", "MOBILE"));
		cdfRowModels.add(new CdfRowModel("MOBILE_ALL", "Category: MOBILE ALL", "MOBILE"));
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
			throw new FDResourceException("saveCdfFile failed: "+e.getMessage(), e);
		
		} finally {
			if (writer != null){
				writer.close();
			}
		}
	}
	
	private void uploadFile() throws FDResourceException {
		
		String ftpUrl = FDStoreProperties.getCoremetricsFtpUrl();
		String ftpUser = FDStoreProperties.getCoremetricsClientId() + "-import";
		String ftpPassword = FDStoreProperties.getCoremetricsFtpPassword();
		int sftpPort = FDStoreProperties.getCoremetricsFtpSftpPort();
		boolean secure = FDStoreProperties.isCoremetricsFtpSecure();
		
		LOGGER.info("uploading Coremetrics CDF to " + ftpUser +"@"+ ftpUrl + " (via " + (secure ? "sftp on port " +sftpPort : "ftp") + ")");
		
		if(secure){
			
			ChannelSftp sftp = null;
			Session session = null;
			
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			
			JSch jsch = new JSch();
			try {
				session=jsch.getSession(ftpUser, ftpUrl, sftpPort);
				session.setPassword(ftpPassword);				
				session.setConfig(config);
				
				session.connect();
				sftp = (ChannelSftp) session.openChannel("sftp");
				
				LOGGER.debug("SFTP: Connecting..");
				sftp.connect();

				sftp.put(cdfFilePath, cdfFileName);
				
				sftp.disconnect();
				session.disconnect();
				
			} catch (JSchException e) {
				 throw new FDResourceException("sftp uploadFile: "+e.getMessage(), e);
			
			} catch (SftpException e) {
				throw new FDResourceException("sftp uploadFile: "+e.getMessage(), e);
			
			} finally {
				if (null != sftp && sftp.isConnected()) {
					LOGGER.debug("SFTP: disconnecting");
					sftp.disconnect();			
				}
				
				if (null != session && session.isConnected()) {
					LOGGER.debug("SFTP: disconnecting");
					session.disconnect();			
				}
			}	
			
		} else {
		
			FTPClient client = new FTPClient();
			client.setDefaultTimeout(600000);
			client.setDataTimeout(600000);
			
	        FileInputStream fis = null;
	        
	        try {
	            client.connect(ftpUrl);
	            
	            if (!client.login(ftpUser, ftpPassword)) {
	            	throw new FDResourceException("ftp login failed"); 
	            }
	    		client.enterLocalPassiveMode();
	
	            fis = new FileInputStream(cdfFilePath);
	            if (!client.storeFile(cdfFileName, fis)) {
	            	throw new FDResourceException("ftp file store failed");
	            }
	            
	            client.logout();
	
	        } catch (IOException e) {
	            throw new FDResourceException("ftp uploadFile: "+e.getMessage(), e);
	            
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
}
