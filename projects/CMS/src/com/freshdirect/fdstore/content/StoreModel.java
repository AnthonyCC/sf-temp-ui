package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class StoreModel extends ContentNodeModelImpl {

	private static final long	serialVersionUID	= -7256497583339960753L;
	
	private List<DepartmentModel> departments = new ArrayList<DepartmentModel>();
	private List<BrandModel> brands;
	private List<Domain> domains;

	public StoreModel(com.freshdirect.cms.ContentKey cKey) {
		super(cKey);
	}
	
	public int getVersion() {
	    return 100;
	}

	public String getName() {
		return this.getAttribute("FULL_NAME", "FreshDirect");
	}

	public List<DepartmentModel> getDepartments() {
		ContentNodeModelUtil.refreshModels(this, "departments", departments, true);
		return new ArrayList<DepartmentModel>(departments);
	}
	
	public Set<DepartmentModel> getSortedDepartments(Comparator<DepartmentModel> comp) {
		ContentNodeModelUtil.refreshModels(this, "departments", departments, true);
	    Set<DepartmentModel> result = new TreeSet<DepartmentModel>(comp);
	    result.addAll(departments);
	    return result;
	}
	
	public List<BrandModel> getBrands() {
	    return new ArrayList<BrandModel>(brands);
	}

	public void setBrands(List<BrandModel> brands) {
		this.brands = brands;
		for ( BrandModel brand : brands ) {
		    brand.setParentNode(this);
		}
	}

	public List<Domain> getDomains() {
	    return new ArrayList<Domain>(domains);
	}

	public void setDomains(List<Domain> domains) {
		this.domains = domains;
		for ( Domain domain : domains ) {
		    domain.setParentNode(this);
		}
	}
	
}
