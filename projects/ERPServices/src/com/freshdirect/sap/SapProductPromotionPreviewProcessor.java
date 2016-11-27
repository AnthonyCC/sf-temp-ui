package com.freshdirect.sap;

import com.freshdirect.sap.command.SapCommandI;
import com.freshdirect.sap.command.SapProductPromotionPreviewCommand;
import com.freshdirect.sap.ejb.SapException;

public class SapProductPromotionPreviewProcessor {

	public static void main(String[] args){
		SapCommandI sapCommand = new SapProductPromotionPreviewCommand("");
		try {
			sapCommand.execute();
		} catch (SapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
