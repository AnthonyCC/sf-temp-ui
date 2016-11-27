package com.freshdirect.webapp.globalnav.data;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.webapp.ajax.browse.data.BasicData;

public class SuperDepartmentData extends BasicData {

	private static final long serialVersionUID = 8879251906066147816L;
	
	private List<DepartmentData> departments = new ArrayList<DepartmentData>();
    private String browseName;
    private boolean hideDropDown;
    
	public List<DepartmentData> getDepartments() {
		return departments;
	}
	public void setDepartments(List<DepartmentData> departments) {
		this.departments = departments;
	}
	public void addDepartment(DepartmentData department) {
		this.departments.add(department);
	}
	public String getBrowseName() {
		return browseName;
	}
	public void setBrowseName(String browseName) {
		this.browseName = browseName;
	}
	public boolean isHideDropDown() {
		return hideDropDown;
	}
	public void setHideDropDown(boolean hideDropDown) {
		this.hideDropDown = hideDropDown;
	}
}