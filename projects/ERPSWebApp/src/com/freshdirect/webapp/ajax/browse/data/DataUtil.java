package com.freshdirect.webapp.ajax.browse.data;

import java.util.Comparator;

public class DataUtil {

	public static final Comparator<BasicData> NAME_COMPARATOR = new Comparator<BasicData>(){
		@Override
		public int compare(BasicData data1, BasicData data2) {
			String name1 = data1.getName();
			if (name1==null){
				return -1;
			} else {
				return name1.compareToIgnoreCase(data2.getName());
			}
		}
	};
	
	public static final Comparator<MenuBoxData> MENUBOX_POSITION_COMPARATOR = new Comparator<MenuBoxData>(){
		@Override
		public int compare(MenuBoxData data1, MenuBoxData data2) {
			
			if(data1==null){
				return -1;
			} else {
				return data1.getPosition().compareTo(data2.getPosition());				
			}
		}
	};

	
}
