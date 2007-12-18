package com.freshdirect.mktAdmin.validation;

import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.mktAdmin.constants.EnumFileType;
import com.freshdirect.mktAdmin.model.FileUploadBean;

public class FileUploadValidator implements Validator {

	public boolean supports(Class clazz) {
		return FileUploadBean.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		FileUploadBean file = (FileUploadBean) obj;

		//  need to decide about the validation part
		System.out.println("inside the validator"+file);
		if(file==null || file.isEmpty() || file.getBytes()==null || file.getBytes().length==0){
		  //ValidationUtils.rejectIfEmpty(errors, "file", "app.error.107", new Object[]{"file"},"required");
			System.out.println("inside validation fails11");
			errors.rejectValue("file", "app.error.107", new Object[]{"file"},"required");
		}
		else if((file.getBytes().length/1000)>FDStoreProperties.getMktAdminFileUploadSize())
		{
			errors.rejectValue("file","app.error.101",new Object[]{""+FDStoreProperties.getMktAdminFileUploadSize()},"file sizecannot exceede the Limit");
			System.out.println("inside validation fails2"+file.getBytes().length);
		}
		else{
			String tmpFileType=file.getFileType();
			System.out.println("tmpFileType"+tmpFileType);
			EnumFileType fileType=EnumFileType.getEnum(tmpFileType.toUpperCase());
			System.out.println("fileType"+fileType);
			List fileList=EnumFileType.getEnumList();
			if(!fileList.contains(fileType))
			{
				errors.rejectValue("file","app.error.103",new Object[]{"EXCEL","CSV"},"file types not supported");
				System.out.println("inside validation fails3");
			}
		}
	}
}