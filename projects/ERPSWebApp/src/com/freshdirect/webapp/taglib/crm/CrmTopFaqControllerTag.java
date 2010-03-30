package com.freshdirect.webapp.taglib.crm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.CallCenterServices;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;

public class CrmTopFaqControllerTag extends AbstractControllerTag {
	
	private String customerId;
	private String id;
	
	public void setId(String id){
		this.id = id;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		if("saveFaqs".equalsIgnoreCase(this.getActionName())){
			
			try {		
					String[] ids = request.getParameterValues("faqId");
					if(null !=ids && ids.length >0){
						pageContext.setAttribute("SELECTED_FAQS",Arrays.asList(ids));
					  if(ids.length>5){
						  actionResult.addError(true, "faqsMoreError", "More than 5 FAQs selected.");
					  }else{
						  CallCenterServices.saveTopFaqs(Arrays.asList(ids));
						  pageContext.setAttribute("SUCCESS_MSG", "Changes Saved!");
//						  performGetAction(request,actionResult);
					  }
					}else{
						actionResult.addError(true, "faqEmptyError", "No FAQs selected. Please select atleast one FAQ.");
					}
					performGetAction(request,actionResult);
			} catch (FDResourceException e) {				
				actionResult.addError(true, "saveFaqError", " Failed to save the selected FAQs.");				
				performGetAction(request,actionResult);
				return true;
			}
		}
		return true;
	}
		
	
	protected boolean performGetAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		
			
				CmsManager          manager     = CmsManager.getInstance();	
				
				String faqSections = FDStoreProperties.getFaqSections();
				StringTokenizer st = new StringTokenizer(faqSections,",");
				List faqSubFolders = new ArrayList();
				Map subNodesMap = new LinkedHashMap();
				while(st.hasMoreTokens()){
					String nextToken=st.nextToken().trim();
					ContentKey contentKey = new ContentKey(FDContentTypes.FDFOLDER,nextToken);
					ContentNodeI contentNode = manager.getContentNode(contentKey);
					if(null !=contentNode){
						Set subNodes = contentNode.getChildKeys();	
						List faqList = new ArrayList();
						for (Object object : subNodes) {
							ContentKey subContentKey= (ContentKey)object;
							if(null!=subContentKey){
								ContentType contentType=subContentKey.getType(); 
								ContentNodeI subContentNode = manager.getContentNode(subContentKey);
								if(FDContentTypes.FAQ.equals(contentType)){
										faqList.add(subContentNode);																			
								}						
							}					
						}
						subNodesMap.put(contentNode, faqList);
					}
					
				}
				faqSubFolders.add(subNodesMap);
				
				/*ContentKey key = new ContentKey(FDContentTypes.FDFOLDER, "FAQ");
				ContentNodeI contentNode = manager.getContentNode(key);
				List faqSubFolders = null;
				if(null != contentNode){
					faqSubFolders = new ArrayList();
					Set subNodes = contentNode.getChildKeys();
					Map subNodesMap = new LinkedHashMap();
					for (Object object : subNodes) {
						ContentKey subContentKey= (ContentKey)object;
						if(null!=subContentKey){
							ContentType contentType=subContentKey.getType(); 
							ContentNodeI subContentNode = manager.getContentNode(subContentKey);
							if(!FDContentTypes.FAQ.equals(contentType)){
//								ContentNodeI subContentNode = manager.getContentNode(subContentKey);
								if(null != subContentNode){
									Set children = subContentNode.getChildKeys();
									List faqList = new ArrayList();
									for (Object object1 : children) {
										ContentKey childContentKey= (ContentKey)object1;
										if(null!=childContentKey && FDContentTypes.FAQ.equals(childContentKey.getType())){
											ContentNodeI childContentNode = manager.getContentNode(childContentKey);
											faqList.add(childContentNode);
										}
									}
									subNodesMap.put(subContentNode, faqList);									
								}
							}else{
								faqSubFolders.add(subContentNode);
							}						
						}					
					}
					faqSubFolders.add(subNodesMap);
					
				}	*/			
				pageContext.setAttribute(this.id, faqSubFolders != null ? faqSubFolders : Collections.EMPTY_LIST);
				pageContext.setAttribute("savedFaqs", getTopFaqs(actionResult));
			
		
		return true;
	}

	private List getTopFaqs(ActionResult actionResult){
		try {
			return CallCenterServices.getTopFaqs();
		} catch (FDResourceException e) {
			actionResult.addError(true, "getSavedFaqError", " Failed to get the saved FAQs.");
			return Collections.EMPTY_LIST;
		}
	}
	
	
	
	public static class TagEI extends AbstractControllerTag.TagEI {
		   public VariableInfo[] getVariableInfo(TagData data) {
		        return new VariableInfo[] {
		            new VariableInfo(data.getAttributeString("id"),
		                            "java.util.List",
		                            true,
		                            VariableInfo.NESTED),
						            new VariableInfo(data.getAttributeString("result"),
			                            "com.freshdirect.framework.webapp.ActionResult",
			                            true,
			                            VariableInfo.NESTED)             
		        };

		    }
	}
}
