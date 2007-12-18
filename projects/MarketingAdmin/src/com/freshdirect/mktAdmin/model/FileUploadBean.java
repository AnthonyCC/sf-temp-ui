package com.freshdirect.mktAdmin.model;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

import com.freshdirect.mktAdmin.constants.EnumFileContentType;

public class FileUploadBean implements Serializable{
		
	
	private MultipartFile file;
    private EnumFileContentType fileContentType=null;

    public FileUploadBean(){    	
    }
    
    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public MultipartFile getFile() {
        return file;
    }
    
    public String getName(){
    	if(this.file!=null) 
    		return file.getOriginalFilename();
    	else
    	   return null; // need to throw application exception.
    }
    
    public boolean isEmpty()
    {
    	if(this.file!=null) 
    		return file.isEmpty();
    	else
    	   return false; // need to throw application exception.    	
    }
    
    public File createFile(String name)
    {
    	 File fileNew=new File(name);
    	 try {
			file.transferTo(fileNew);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 return fileNew;
    }
    
    public byte[] getBytes()
    {
    	if(this.file!=null)
			try {
				return file.getBytes();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// throw application exception
				return null;
			}
		else
    	   return null; // need to throw application exception.    	
    }

    public String getFileType() {
    	String fileName=this.getName();
    	String fileType= fileName.substring(fileName.indexOf(".")+1,fileName.length());
    	return fileType;
    }

	public EnumFileContentType getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(EnumFileContentType fileContentType) {
		this.fileContentType = fileContentType;
	}

	        
}
