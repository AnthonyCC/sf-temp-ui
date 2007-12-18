package com.freshdirect.mktAdmin.util;

import java.util.Collection;

import com.freshdirect.mktAdmin.constants.EnumFileContentType;
import com.freshdirect.mktAdmin.exception.MktAdminApplicationException;
import com.freshdirect.mktAdmin.model.FileDownloadBean;
import com.freshdirect.mktAdmin.model.FileUploadBean;

public interface FileParser {

	//public Collection parseFile(byte file[],String fileContentType) throws MktAdminApplicationException;
	public Collection parseFile(FileUploadBean fileUploadBean) throws MktAdminApplicationException;
	
	public String generateFile(FileDownloadBean fileUploadBean) throws MktAdminApplicationException;
	
}
