package com.freshdirect.mobileapi.controller.data;

import java.util.List;

import com.freshdirect.mobileapi.model.Department;




public class GlobalNavResult extends Message {

	List<Department> departments;

	public List<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}
	
	
	
	

}
