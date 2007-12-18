package com.freshdirect.mktAdmin.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.freshdirect.mktAdmin.constants.EnumFileContentType;

public class FileDownloadBean implements Serializable{
		
	
	private Collection fileContents;
    private EnumFileContentType fileContentType=null;

    public FileDownloadBean(){    	
    }
    
    public void setFileContents(Collection file) {
        this.fileContents = file;
    }

    public Collection getFileContents() {
        return fileContents;
    }
        
    public boolean isEmpty()
    {
    	if(this.fileContents.size()==0) 
    		return true;
    	else
    	   return false; // need to throw application exception.    	
    }
    
    
	public EnumFileContentType getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(EnumFileContentType fileContentType) {
		this.fileContentType = fileContentType;
	}

	        
}
