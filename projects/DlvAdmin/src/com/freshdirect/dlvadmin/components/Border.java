package com.freshdirect.dlvadmin.components;



import org.apache.tapestry.BaseComponent;

import com.freshdirect.dlvadmin.DlvPage;
import com.freshdirect.dlvadmin.PageRegistry;

public class Border extends BaseComponent {
	
	/** temp for iterator */
	private String currentPage;
	private int navSize;
			
	public String[] getNavigation() {
		String[] nav = PageRegistry.getNavGroup( this.getPage().getPageName() );
		this.navSize = nav.length; 
		return nav;
	}
	
	
	public String getTitle() {
		return PageRegistry.getTitle( this.getPage().getPageName() );
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}
	
	public int getNavSize(){
		return this.navSize;
	}
	
	public void setNavSize(int navSize){
		this.navSize = navSize;
	}
	
	public String getCurrentTitle() {
		return PageRegistry.getTitle(currentPage);
	}
	
	public boolean isGenericLink() {
		return this.currentPage.indexOf(".jsp")>0;
	}

	public boolean isCurrentNavDisabled() {
		if (!PageRegistry.isAccessable(currentPage,((DlvPage)this.getPage()).getUserRole())) {
				return true;
		}
		return this.currentPage.equals( this.getPage().getPageName() );
	}

}
