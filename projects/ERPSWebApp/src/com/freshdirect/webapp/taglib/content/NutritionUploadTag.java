package com.freshdirect.webapp.taglib.content;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.content.nutrition.ErpNutritionModel;
import com.freshdirect.content.nutrition.ejb.ErpNutritionHome;
import com.freshdirect.content.nutrition.ejb.ErpNutritionSB;
import com.freshdirect.dataloader.nutrition.EshaSpreadsheetParser;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;

/**@author ekracoff on May 6, 2004*/
public class NutritionUploadTag extends AbstractControllerTag {

	public boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {

		List updatedSkus = new ArrayList();

		updatedSkus = this.performUpload(request, actionResult);
			
		//instead of returning this list stick it in the session so the user can browse and still see the list
		HttpSession session = request.getSession();
		if(actionResult.isSuccess()){
			session.setAttribute("updateList", updatedSkus);
		}
		
		return true;
	}

	private List performUpload(HttpServletRequest request, ActionResult actionResult) {
	
		//Check that we have a file upload request
		//boolean isMultipart = FileUpload.isMultipartContent(request);
	
		//Create a new file upload handler
		DiskFileUpload upload = new DiskFileUpload();
	
		//Parse the request
		List items = new ArrayList();
		List updatedSkus = new ArrayList();
		InputStream uploadedStream = null;
		try {
			items = upload.parseRequest(request);
	
			//Process the uploaded items
			System.out.println("Processing uploaded items (" + items.size() + ")");
			Iterator iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
	
				if (item.isFormField()) {
					//processFormField(item);
				} else {
					System.out.println("FileType: " + item.getContentType() + "  size: " + item.getSize());
					if(!item.getContentType().equals("application/vnd.ms-excel")){
						actionResult.addError(new ActionError("invalid_file", "Invalid File Type"));
						return Collections.EMPTY_LIST;
					}
					
					//Process file upload
					uploadedStream = item.getInputStream();
	
					EshaSpreadsheetParser parser = new EshaSpreadsheetParser();
					parser.doParse(uploadedStream);
					
					updatedSkus = this.doLoad(parser);
				}
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
			actionResult.addError(new ActionError("technical_difficulty", e.getMessage()));
		} catch (IOException e1) {
			e1.printStackTrace();
			actionResult.addError(new ActionError("technical_difficulty", e1.getMessage()));
		} finally {
			if (uploadedStream != null){
				try {
					uploadedStream.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
		}
		
		return updatedSkus;
	}

	private List doLoad(EshaSpreadsheetParser parser) {
		System.out.println("\n----- starting doLoad() -----");

		ArrayList nutrition = parser.getNutrition();
		List updatedSkus = new ArrayList();
		
		Context ctx = null;
		try {
			ctx = ErpServicesProperties.getInitialContext();
			ErpNutritionHome nutritionHome = (ErpNutritionHome) ctx.lookup(ErpServicesProperties.getNutritionHome());

			ErpNutritionSB sb = nutritionHome.create();
			for (Iterator nIter = nutrition.iterator(); nIter.hasNext();) {
				ErpNutritionModel enm = (ErpNutritionModel) nIter.next();
				ErpNutritionModel oldEnm = sb.getNutrition(enm.getSkuCode());
				
				for(Iterator i = oldEnm.getKeyIterator(); i.hasNext();){
					String key = (String)i.next();
					if(!enm.containsNutritionInfoFor(key)){
						enm.setValueFor(key, oldEnm.getValueFor(key));
					}
					
				}
				
				if(enm.getHeatingInstructions().equals(""))
					enm.setHeatingInstructions(oldEnm.getHeatingInstructions());
				
				if(enm.getIngredients().equals(""))
					enm.setIngredients(oldEnm.getIngredients());
				
				System.out.println("Loading nutrition for " + enm.getSkuCode());
				sb.updateNutrition(enm);
				updatedSkus.add(enm.getSkuCode());
			}

			System.out.println("\n----- normally exiting doLoad() -----");

		} catch (NamingException ne) {
			ne.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		} finally {
			try {
				ctx.close();
			} catch (NamingException e) {
			}
		}
		
		return updatedSkus;
	}

	public static class TagEI extends AbstractControllerTag.TagEI {
		/*public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {
				 new VariableInfo(
					data.getAttributeString("result"),
					"com.freshdirect.framework.webapp.ActionResult",
					true,
					VariableInfo.NESTED),
				new VariableInfo(
					data.getAttributeString("updateList"),
					"java.util.List",
					true,
					VariableInfo.NESTED)};
		}*/
	}


}
