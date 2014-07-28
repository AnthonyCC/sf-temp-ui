package com.freshdirect.webapp.globalnav.data;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.webapp.ajax.browse.data.BasicData;

public class GlobalNavData extends BasicData {

	private static final long serialVersionUID = -3912423911696491345L;
	private List<BasicData> abstractDepartments = new ArrayList<BasicData>();
	private String media;
	
	public List<BasicData> getAbstractDepartments() {
		return abstractDepartments;
	}

	public void setAbstractDepartments(List<BasicData> abstractDepartments) {
		this.abstractDepartments = abstractDepartments;
	}
	
	public void addAbstractDepartment(BasicData abstractDepartments) throws FDResourceException {
		
		if (abstractDepartments instanceof DepartmentData || abstractDepartments instanceof SuperDepartmentData) {
			this.abstractDepartments.add(abstractDepartments);
		} else {
			throw new FDResourceException("Wrong data type!");
		}
		
	}

	public String getMedia() {
		return media;
	}

	public void setMedia(String media) {
		this.media = media;
	}

}
