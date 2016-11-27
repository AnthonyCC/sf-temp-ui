package com.freshdirect.webapp.ajax.browse.data;

public enum NavDepth {
	
	DEPARTMENT(0), CATEGORY(1), SUB_CATEGORY(2), SUB_SUB_CATEGORY(3);
	
	
	private int level;
	
	NavDepth(int level){
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public static NavDepth getNavDepthByLevel(int level){
		for(NavDepth nd : values()){
			if(nd.getLevel()==level){
				return nd;
			}
		}
		return null;
	}
	
	public static int getMaxLevel(){
		return values().length-1;
	}

}
