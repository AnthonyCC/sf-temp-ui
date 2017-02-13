package com.freshdirect.dataloader.payment;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSelectInfo;
import org.apache.commons.vfs2.FileSelector;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.provider.local.LocalFile;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;
import org.apache.log4j.Category;

import com.freshdirect.dataloader.DataLoaderProperties;
import com.freshdirect.framework.util.log.LoggerFactory;

public class SFTPFileProcessor {
	
	private final static Category LOGGER = LoggerFactory.getInstance(SFTPFileProcessor.class);
	FileSystemOptions fsOptions = new FileSystemOptions();
	private FileContext context;
	private boolean initialized;
	
	public SFTPFileProcessor(FileContext context ) {
		
		try {
			SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(fsOptions, "no");
			if (!PaymentFileType.PAYPAL_SETTLEMENT.equals(context.getFileType()))
				SftpFileSystemConfigBuilder.getInstance().setIdentities(fsOptions, new File[]{new File(context.getOpenSSHPrivateKey())});
			this.context=context;
			initialized=true;
		} catch (FileSystemException e) {
			LOGGER.fatal("Could not initialize SFTPFileProcessor due to"+e.toString() );
		}
	}
	
	private FileSelector getPPFileSelector() {
		
		return new FileSelector(){
       	 
            public boolean includeFile(FileSelectInfo fileSelectInfo) throws Exception {
            	String absPath = fileSelectInfo.getFile().getName().toString();
            	String filename = absPath.substring(absPath.lastIndexOf("/") + 1);
            	
            	if (filename.startsWith(DataLoaderProperties.getPayPalStlmntFilePrefix()
            			+ context.getPayPalFileDate() + ".A") &&
            			filename.endsWith(DataLoaderProperties.getPayPalStlmntFileSuffix() +
            									DataLoaderProperties.getPayPalStlmntFileExtn())) {
            		return true; 
            	}
            	
            	return false;
            }
 
            public boolean traverseDescendents(FileSelectInfo fileSelectInfo) throws Exception {
            	if (fileSelectInfo.getDepth() == 0)
            		return true;
            	else
            		return false;
            }
		};
	}
	
	private  String getExtension() {
		if(this.context!=null)
			return context.getFileType().getExtension();
		return "";
	}
	
	private FileSelector getFileSelector() {
		
		return new FileSelector(){
       	 
            public boolean includeFile(FileSelectInfo fileSelectInfo) throws Exception {
                return fileSelectInfo.getFile().getName().toString().toLowerCase().indexOf(getExtension().toLowerCase())!=-1?true:false;
            }
 
            public boolean traverseDescendents(FileSelectInfo fileSelectInfo) throws Exception {
            
              if (fileSelectInfo.getDepth() > 1) {
                return false;
              } else {
                return true;
              }
            }
        };
	
	}
	
	public List<File> getPPFiles() throws IOException, URISyntaxException {
		FileSystemManager fsManager = VFS.getManager();

        String remoteURL = "";
    	String userCreds = context.getUserName() +":"+context.getPassword();
    	String path = DataLoaderProperties.getPayPalStlmntFolder();
    	URI uri = new URI("sftp", userCreds, context.getRemoteHost(), -1, path, null, null);
    	remoteURL = uri.toString();

        
        FileObject remoteFileObject = fsManager.resolveFile(remoteURL, fsOptions);
        FileObject[] children = remoteFileObject.findFiles(getPPFileSelector());
        LocalFile localFile =	  (LocalFile) fsManager.resolveFile(context.getLocalHost());
        localFile.copyFrom(remoteFileObject,getPPFileSelector());
        
        List<File> downloadedFiles=new ArrayList<File>(children.length);
        for(int i=0;i<children.length;i++) {
       		downloadedFiles.add(new File(context.getLocalHost()+children[i].getName().getBaseName()));
        }

        boolean deleteFiles=DataLoaderProperties.isPaymentechSFTPFileDeletionEnabled();
	    if(deleteFiles)
	      	deleteFiles(children);
	    deleteFiles = DataLoaderProperties.isPayPalSFTPFileDeletionEnabled();
	    if (deleteFiles)
	    	deleteFiles(children);
	    
	    return  downloadedFiles;
	}
	
	
	/**
	 *  This method is just for testing purpose 
	 *  keep extracted zip file in work location to process settlement files
	 * @return
	 * @throws IOException
	 */
	
	public List<String> getLocalFiles() throws IOException{
		
		File fileDir=new File(context.getLocalHost());

        List<String> downloadedFiles=new ArrayList<String>(fileDir.listFiles().length);

        for(File f:fileDir.listFiles()){
        	
        	if(f.getName().indexOf("A")>0|| f.getName().indexOf("B")>0 && !f.isDirectory()){
        		
			  downloadedFiles.add(f.getName());
        }}

        return downloadedFiles;
	}
	
	public List<String> getFiles() throws IOException {
		
		
		if(!initialized) {
			throw new RuntimeException(this.getClass().getName()+" is not initialized");
		}
	   FileSystemManager fsManager = null;
	   try {
		        fsManager = VFS.getManager();
		        String remoteURL = "sftp://" + context.getUserName() +":"+context.getPassword()+ "@" + context.getRemoteHost()  ;
		        FileObject remoteFileObject = fsManager.resolveFile(remoteURL, fsOptions);
		        FileSelector fs= getFileSelector();
		        
		        FileObject[] children=remoteFileObject.findFiles(fs);
		        LocalFile localFile =	  (LocalFile) fsManager.resolveFile(context.getLocalHost());
		        LOGGER.info("Copying files to local folder..");
		        localFile.copyFrom(remoteFileObject,fs);
		        LOGGER.info("Copied "+children.length+" files to local folder..");
		        //extract files and delete from remote server
		        
		        List<String> downloadedFiles=new ArrayList<String>(children.length);
		        LOGGER.info("Extracting zip files..");
		        for(int i=0;i<children.length;i++) {
		        	LOGGER.info("Extracting zip file : "+children[i].getName().getBaseName());
					ZipFile zipFile = new ZipFile(context.getLocalHost()+children[i].getName().getBaseName());
					if (zipFile.isEncrypted()) {
						LOGGER.info("Zip file "+children[i].getName().getBaseName()+" is encrypted.");
						zipFile.setPassword(context.getPassword());
					}
					
					if(PaymentFileType.SETTLEMENT_FAILURE.equals(context.getFileType())) {
						LOGGER.info("This file contains settlement failures..");
						String STFfileName=DataLoaderProperties.getSettlementFailureFileName()+PaymentFileType.SETTLEMENT_FAILURE.getExtension();
						zipFile.extractFile(STFfileName, context.getLocalHost());
						File f=new File(context.getLocalHost()+STFfileName);
						File f1=new File (context.getLocalHost()+DataLoaderProperties.getSettlementFailureFileName()+ new SimpleDateFormat("yyyy_MM_dd_HH_mm").format(new Date())+PaymentFileType.SETTLEMENT_FAILURE.getExtension());
						boolean success=f.renameTo(f1);
						if(success)
							LOGGER.info("Renamed file :"+f.getName()+" to "+f1.getName());
						else 
							LOGGER.info("Could not rename file :"+f.getName()+" to "+f1.getName());
						downloadedFiles.add(f1.getName());
						
					} else {
						downloadedFiles.add(children[i].getName().getBaseName().substring(0, children[i].getName().getBaseName().length()-9));
						zipFile.extractFile(children[i].getName().getBaseName().substring(0, children[i].getName().getBaseName().length()-9), context.getLocalHost());
					}
		        }
		        boolean deleteFiles=DataLoaderProperties.isPaymentechSFTPFileDeletionEnabled();
		        if(deleteFiles)
		        	deleteFiles(children);
		        
		       return  downloadedFiles;
		    } catch (FileSystemException e) {
		        LOGGER.error("Problem retrieving from " + context.getRemoteHost() + " to " + context.getLocalHost(),e );
		        throw new IOException(e);
		    } catch (ZipException ze) {
		    	LOGGER.error("Problem unzipping file from " + context.getRemoteHost() + " to " + context.getLocalHost(),ze );
		        throw new IOException(ze);
			}
	}
	
	private void deleteFiles(FileObject[] files) throws FileSystemException {
		 for(int i=0;i<files.length;i++) {
			 files[i].delete();
		 }
	}
	
	public static void main(String[] a) throws Exception {
		FileContext ctx=new FileContext();
		ctx.setFileType(PaymentFileType.PAYPAL_SETTLEMENT);
		ctx.setLocalHost(DataLoaderProperties.getWorkingDir());
		ctx.setPayPalFileDate("20160222");
		//ctx.setOpenSSHPrivateKey(DataLoaderProperties.getWorkingDir()+DataLoaderProperties.getPaymentSFTPKey());
		ctx.setRemoteHost(DataLoaderProperties.getPayPalFtpIp());
		ctx.setUserName(DataLoaderProperties.getPayPalFtpUser());
		ctx.setPassword(DataLoaderProperties.getPayPalFtpPassword());
		SFTPFileProcessor fp=new SFTPFileProcessor(ctx);
		List<String> files=fp.getFiles();
		System.out.println("Files downloaded are:");
		for(String v:files) {
			System.out.println(v);
		}
		/*
		FileContext ctx=new FileContext();
		ctx.setFileType(PaymentFileType.DFR);
		ctx.setLocalHost(DataLoaderProperties.getWorkingDir());
		ctx.setOpenSSHPrivateKey(DataLoaderProperties.getWorkingDir()+DataLoaderProperties.getPaymentSFTPKey());
		ctx.setRemoteHost(DataLoaderProperties.getPaymentSFTPHost());
		ctx.setUserName(DataLoaderProperties.getPaymentSFTPUser());
		ctx.setPassword(DataLoaderProperties.getPaymentSFTPPassword());
		SFTPFileProcessor fp=new SFTPFileProcessor(ctx);
		List<String> files=fp.getFiles();
		System.out.println("Files downloaded are:");
		for(String v:files) {
			System.out.println(v);
		}
		*/
	}
}