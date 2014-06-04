package com.freshdirect.dataloader.payment;

import java.io.File;
import java.io.IOException;
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
			SftpFileSystemConfigBuilder.getInstance().setIdentities(fsOptions, new File[]{new File(context.getOpenSSHPrivateKey())});
			this.context=context;
			initialized=true;
		} catch (FileSystemException e) {
			LOGGER.fatal("Could not initialize SFTPFileProcessor due to"+e.toString() );
		}
	}

	
	private  String getExtension() {
		if(this.context!=null)
			return context.getFileType().getExtension();
		return "";
	}
	
	private FileSelector getFileSelector() {
		
		return new FileSelector(){
       	 
            public boolean includeFile(FileSelectInfo fileSelectInfo) throws Exception {
                return fileSelectInfo.getFile().getName().toString().indexOf(getExtension())!=-1?true:false;
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
		        localFile.copyFrom(remoteFileObject,fs);
		        
		        //extract files and delete from remote server
		        
		        List<String> downloadedFiles=new ArrayList<String>(children.length);
		        for(int i=0;i<children.length;i++) {
					ZipFile zipFile = new ZipFile(context.getLocalHost()+children[i].getName().getBaseName());
					if (zipFile.isEncrypted()) {
						zipFile.setPassword(context.getPassword());
					}
					
					if(PaymentFileType.SETTLEMENT_FAILURE.equals(context.getFileType())) {
						String STFfileName=DataLoaderProperties.getSettlementFailureFileName()+PaymentFileType.SETTLEMENT_FAILURE.getExtension();
						zipFile.extractFile(STFfileName, context.getLocalHost());
						File f=new File(context.getLocalHost()+STFfileName);
						File f1=new File (context.getLocalHost()+DataLoaderProperties.getSettlementFailureFileName()+ new SimpleDateFormat("yyyy_MM_dd_HH_mm").format(new Date())+PaymentFileType.SETTLEMENT_FAILURE.getExtension());
						f.renameTo(f1);
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
	}
}