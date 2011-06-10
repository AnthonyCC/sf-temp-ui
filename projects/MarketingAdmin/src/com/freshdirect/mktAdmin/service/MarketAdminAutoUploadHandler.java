package com.freshdirect.mktAdmin.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Category;

import au.com.bytecode.opencsv.CSVWriter;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;
import com.freshdirect.mktAdmin.constants.EnumFileContentType;
import com.freshdirect.mktAdmin.constants.EnumListUploadActionType;
import com.freshdirect.mktAdmin.exception.MktAdminApplicationException;
import com.freshdirect.mktAdmin.model.FileUploadBean;
import com.freshdirect.mktAdmin.model.FileUploadedInfo;
import com.freshdirect.mktAdmin.model.RestrictedPromoCustomerModel;
import com.freshdirect.mktAdmin.model.RestrictionListUploadBean;
import com.freshdirect.mktAdmin.util.MarketAdminSpringUtil;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class MarketAdminAutoUploadHandler {
	
	private static Category LOGGER = LoggerFactory.getInstance(MarketAdminAutoUploadHandler.class);
	private final static JSch jsch=new JSch();
	private static final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(10);
	private static final ThreadPoolExecutor  executor = new ThreadPoolExecutor(5,5,10,TimeUnit.MINUTES,queue);
	private static MarketAdminServiceIntf marketAdminService;
	private static Map<String, Boolean> uploadStatus = new HashMap<String, Boolean>();
	private static Map<String, FileUploadedInfo> filesUploadInfoMap = new HashMap<String, FileUploadedInfo>();
	private static boolean isPrevUploadCompleted = true;
	private static final String sftpLocalDirectory = ErpServicesProperties.getProperty(ErpServicesProperties.PROP_MKTADMIN_AUTO_UPLOADER_LOCAL_WORKDIR);
	private static final String sftpLocalBackupDirectory = ErpServicesProperties.getProperty(ErpServicesProperties.PROP_MKTADMIN_AUTO_UPLOADER_LOCAL_BACKUPDIR);
	private static final String sftpLocalFailedDirectory = ErpServicesProperties.getProperty(ErpServicesProperties.PROP_MKTADMIN_AUTO_UPLOADER_LOCAL_FAILEDDIR);
	private static final String sftpHost = ErpServicesProperties.getProperty(ErpServicesProperties.PROP_MKTADMIN_AUTO_UPLOADER_FTP_HOST);
	private static final String sftpUser = ErpServicesProperties.getProperty(ErpServicesProperties.PROP_MKTADMIN_AUTO_UPLOADER_FTP_USER);
	private static final String sftpPasswd = ErpServicesProperties.getProperty(ErpServicesProperties.PROP_MKTADMIN_AUTO_UPLOADER_FTP_PASSWD);
	private static final String sftpDirectory = ErpServicesProperties.getProperty(ErpServicesProperties.PROP_MKTADMIN_AUTO_UPLOADER_FTP_WORKDIR);
	private static final String sftpBackupDirectory = ErpServicesProperties.getProperty(ErpServicesProperties.PROP_MKTADMIN_AUTO_UPLOADER_FTP_BACKUPDIR);
	private static final String sftpFailedDirectory = ErpServicesProperties.getProperty(ErpServicesProperties.PROP_MKTADMIN_AUTO_UPLOADER_FTP_FAILEDDIR);

	public static void main(String[] args){		
		LOGGER.info("Inside MarketAdminAutoUploadHandler-is previous upload completed:"+isPrevUploadCompleted);
		if(isPrevUploadCompleted){
			isPrevUploadCompleted = false;
			uploadStatus = new HashMap<String, Boolean>();
			filesUploadInfoMap = new HashMap<String, FileUploadedInfo>();
			marketAdminService =  (MarketAdminServiceIntf)MarketAdminSpringUtil.getInstance().getBeanFactory().getBean("marketAdminService");
			
			
			ChannelSftp sftp = null;
			Session session = null;
			Channel channel = null;
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
			Date date = new Date();
			String dirName = sftpLocalDirectory + df.format(date);
			String failedDir = sftpLocalFailedDirectory+ df.format(date);
			File workDir = new File(dirName);		
			
			FTPClient client = new FTPClient();
			client.setDefaultTimeout(30000);
			client.setDataTimeout(30000);
			try {
	
				LOGGER.info("SFTP: connecting to host " + sftpHost);
				Properties config = new Properties();
				config.put("StrictHostKeyChecking", "no");
				
				session = getSftpSession(config);
				session.connect();
				channel = session.openChannel("sftp");
				sftp = (ChannelSftp) channel;
				LOGGER.info("SFTP: Connecting..");
				sftp.connect();
				
				//Download the files to a local main directory, remove them from the remote directory, parse those files from local main directory.
				final Vector files = sftp.ls(sftpDirectory+"*.csv");
				LOGGER.info("SFTP: "+files.size()+ " csv files to be uploaded.");
				boolean filesAvailable = (files.size() > 0);
				if(filesAvailable){
					workDir.mkdirs();
					Iterator itFiles = files.iterator();
					
					while (itFiles.hasNext()) {
						ChannelSftp.LsEntry entry =(ChannelSftp.LsEntry)itFiles.next();
						String fileName = entry.getFilename();
						System.out.println("Index: " + fileName);
						FileOutputStream fos = new FileOutputStream(new File(workDir, fileName));
						sftp.get(sftpDirectory+fileName, fos);
						sftp.rm(sftpDirectory+fileName);
					}
				}
				LOGGER.info("SFTP: Disconnecting..");
				
				sftp.disconnect();
				session.disconnect();
				

				String[] children = workDir.list();
				File localBackupDir = new File(sftpLocalBackupDirectory);
				for (int i = 0; i < children.length; i++) {
					String fileName = children[i];
					LOGGER.info(fileName+" is getting uploaded.");
					uploadStatus.put(fileName, false);
					File file =new File(workDir, fileName);
					File destFile =new File(localBackupDir, fileName);
					RestrictionListUploadBean fileUploadBean = new RestrictionListUploadBean();
					fileUploadBean.setAutoUpload(true);
					fileUploadBean.setAutoUploadFile(file);
					fileUploadBean.setFileContentType(EnumFileContentType.RESTRICTION_LIST_FILE_TYPE);
					fileUploadBean.setActionType(EnumListUploadActionType.ADD_MULTI_PROMO);				
					AutoUploadThread thread = new AutoUploadThread();
					thread.setFileUploadBean(fileUploadBean);
					thread.setFile(file);
					executor.execute(thread);
					
//					createLocalBackupFiles(file, destFile);
					 	
				}	
				
				//Check whether all the files are uploaded until its uploaded and then create backup/failed files.
				while(!isPrevUploadCompleted && filesAvailable){
					boolean allFilesUploaded = true;
					for (Iterator iterator = uploadStatus.keySet().iterator(); iterator.hasNext();) {
						boolean fileUploadStatus = (boolean)uploadStatus.get(iterator.next());
						if(!fileUploadStatus){
							allFilesUploaded = false;
							break;
						}
					}
					if(allFilesUploaded){
						session = getSftpSession(config);
						session.connect();
						channel = session.openChannel("sftp");
						sftp = (ChannelSftp) channel;
						sftp.connect();
						createFilesForFailedUpload(sftp, failedDir, workDir,
								children);
						isPrevUploadCompleted = true;
						sftp.disconnect();
						session.disconnect();
					}else{
						//Sleep for some time before checking the status of upload.
						 Thread.sleep(5*60*1000);
					}					
				}
	
			} catch(InterruptedException ie){
				LOGGER.error("InterruptedException :"+ie.getMessage());
				ie.printStackTrace();
			} catch(IOException ioe){
				LOGGER.error("IOException :"+ioe.getMessage());
				ioe.printStackTrace();
			} catch(JSchException je){
				LOGGER.error("JSchException :"+je.getMessage());
				je.printStackTrace();
			} catch(SftpException se){
				LOGGER.error("SftpException :"+se.getMessage());
				se.printStackTrace();
			} catch(Exception e){
				LOGGER.error("Exception :"+e.getMessage());
				e.printStackTrace();
			}finally {
				isPrevUploadCompleted = true;
				if (null != sftp && sftp.isConnected()) {
					LOGGER.info("SFTP: disconnecting");
					sftp.disconnect();			
				}
				if (null != session && session.isConnected()) {
					LOGGER.info("SFTP: disconnecting");
					session.disconnect();			
				}
			}	
		}
		LOGGER.info("MarketAdminAutoUploadHandler job completed");

	}

	//Creating local backup for all the files.
	private static void createLocalBackupFiles(File file, File destFile)
			throws FileNotFoundException, IOException {
		FileChannel source = null;
		FileChannel destination = null;
		try {
		  source = new FileInputStream(file).getChannel();
		  destination = new FileOutputStream(destFile).getChannel();
		  destination.transferFrom(source, 0, source.size());
		  
		}
		finally {
			if(source != null) {
				source.close();
			}
		    if(destination != null) {
		    	destination.close();
		    }
//				  file.delete();
		}
//				boolean success =  file.delete();
//				System.out.println(success);
	}

	private static Session getSftpSession(Properties config)
			throws JSchException {
		Session session;
		session=jsch.getSession(sftpUser, sftpHost, 22);
		session.setPassword(sftpPasswd);				
		session.setConfig(config);
		return session;
	}

	//From the local directory, copy the files into 'backup' folder for successful one's and create the files with just the failed records in the 'failed' folder for failed files.
	private static void createFilesForFailedUpload(ChannelSftp sftp,
			String failedDir, File workDir, String[] children)
			throws FileNotFoundException, SftpException, IOException {
		for (int i = 0; i < children.length; i++) {
			String fileName = children[i];
			System.out.println(fileName);
			File file =new File(workDir, fileName);
			FileInputStream fis = new FileInputStream(file);
			sftp.put(fis,sftpBackupDirectory+fileName);
			if(null !=filesUploadInfoMap.get(fileName)){
				FileUploadedInfo info =(FileUploadedInfo)filesUploadInfoMap.get(fileName);
				if(!info.isSuccessful()){
					sftp.put(fis,sftpFailedDirectory+fileName);
				}else if(null !=info.getFailedCustInfo() && !info.getFailedCustInfo().isEmpty()){
					File localFailedDir = new File(failedDir);
					localFailedDir.mkdirs();
					file = new File(localFailedDir,fileName);
					CSVWriter writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(file)));
					writer.writeNext(new String[]{"CUSTOMER_FDID","PROMO_SYS_CODE"});
					for(Iterator itr = info.getFailedCustInfo().iterator();itr.hasNext();){
						RestrictedPromoCustomerModel model =(RestrictedPromoCustomerModel)itr.next();
						writer.writeNext(new String[]{model.getCustomerId(),model.getPromotionCode()});										
					}
					writer.flush();
					fis = new FileInputStream(file);
					sftp.put(fis,sftpFailedDirectory+fileName);
				}
			}
		}
	}
	
	public static class AutoUploadThread extends Thread {
		
		private File file;	
		private FileUploadBean fileUploadBean;
		
		public AutoUploadThread() {
			super();
		}

		public File getFile() {
			return file;
		}

		public void setFile(File file) {
			this.file = file;
		}
		
		public FileUploadBean getFileUploadBean() {
			return fileUploadBean;
		}

		public void setFileUploadBean(FileUploadBean fileUploadBean) {
			this.fileUploadBean = fileUploadBean;
		}


		@Override
		public void run() {
			FileUploadedInfo info = null;
			try {
				info =marketAdminService.parseMktAdminAutoUploadFile(fileUploadBean);
			} catch (MktAdminApplicationException e) {
				LOGGER.debug(fileUploadBean.getAutoUploadFile().getName()+" failed. "+e);
				e.printStackTrace();
			} catch(Exception e){
				LOGGER.debug(fileUploadBean.getAutoUploadFile().getName()+" failed. "+e);
				e.printStackTrace();
			} finally{
				uploadStatus.put(fileUploadBean.getAutoUploadFile().getName(), true);
				filesUploadInfoMap.put(fileUploadBean.getAutoUploadFile().getName(), info);
				sendEmail(info,fileUploadBean);				  
			}
			
		}

		//Email Notification for each of the upload files.
		private void sendEmail(FileUploadedInfo info, FileUploadBean fileUploadBean) {
			if(FDStoreProperties.isMktAdminAutouploadEmailEnabled() ){
				try {
					ErpMailSender mailer = new ErpMailSender();
					StringBuffer buffer = new StringBuffer();
					buffer.append("<html>").append("<body>");
					buffer.append("<b>"+info.getFileName()+"</b>");
					if(info.isSuccessful()){
						buffer.append(" is uploaded successfully.");
						if(null !=info.getFailedCustInfo() && !info.getFailedCustInfo().isEmpty()){
							buffer.append("<br/>");
							buffer.append(info.getTotalCustInfo().size()+ " records are uploaded.<br/> ");
							buffer.append(info.getFailedCustInfo().size()+ " records failed to upload. Please check the customer id and promotion code.");
						}else{
							buffer.append("<br/>");
							buffer.append("Total "+info.getTotalCustInfo().size()+ " records are uploaded successfully. ");
						}
					}else{
						buffer.append(" is failed to get uploaded. Please check the file format. It should have exactly 2 columns.");
					}
					
					mailer.sendMail(FDStoreProperties.getCustomerServiceEmail(),
								FDStoreProperties.getMktAdminAutouploadReportToEmail(),
								FDStoreProperties.getMktAdminAutouploadReportCCEmail(),
								FDStoreProperties.getMktAdminAutouploadReportEmailSubject()+"-"+new Date(), buffer.toString(), true, "");
				} catch (MessagingException e) {						
						LOGGER.warn("Error sending the marketing admin upload status of a file ", e);						
				}
			}
		}	

	}
}
