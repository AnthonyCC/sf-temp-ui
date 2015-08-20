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
	private static final long serialVersionUID = -1347735183710170167L;

	private String id;
	
	public void setId(String id){
		this.id = id;
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
						CallCenterServices.saveTopFaqs(Arrays.asList(new String[]{" "}));
						pageContext.setAttribute("SUCCESS_MSG", "Changes Saved!");
						//actionResult.addError(true, "faqEmptyError", "No FAQs selected. Please select atleast one FAQ.");
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
				List<Map<ContentNodeI,List<ContentNodeI>>> faqSubFolders = new ArrayList<Map<ContentNodeI,List<ContentNodeI>>>();
				Map<ContentNodeI,List<ContentNodeI>> subNodesMap = new LinkedHashMap<ContentNodeI,List<ContentNodeI>>();
				while(st.hasMoreTokens()){
					String nextToken=st.nextToken().trim();
					ContentKey contentKey = new ContentKey(FDContentTypes.FDFOLDER,nextToken);
					ContentNodeI contentNode = manager.getContentNode(contentKey);
					if(null !=contentNode){
						Set<ContentKey> subNodes = contentNode.getChildKeys();	
						List<ContentNodeI> faqList = new ArrayList<ContentNodeI>();
						for (ContentKey subContentKey : subNodes) {
							ContentType contentType=subContentKey.getType(); 
							ContentNodeI subContentNode = manager.getContentNode(subContentKey);
							if(FDContentTypes.FAQ.equals(contentType)){
									faqList.add(subContentNode);																			
							}						
						}
						subNodesMap.put(contentNode, faqList);
					}
					
				}
				faqSubFolders.add(subNodesMap);
				
				pageContext.setAttribute(this.id, faqSubFolders != null ? faqSubFolders : Collections.EMPTY_LIST);
				pageContext.setAttribute("savedFaqs", getTopFaqs(actionResult));
			
		
		return true;
	}

	private List<String> getTopFaqs(ActionResult actionResult){
		try {
			return CallCenterServices.getTopFaqs();
		} catch (FDResourceException e) {
			actionResult.addError(true, "getSavedFaqError", " Failed to get the saved FAQs.");
			return Collections.emptyList();
		}
	}
	
	
	
	public static class TagEI extends AbstractControllerTag.TagEI {
		   public VariableInfo[] getVariableInfo(TagData data) {
		        return new VariableInfo[] {
		            new VariableInfo(data.getAttributeString("id"),
//                        "java.util.List<java.util.Map<com.freshdirect.cms.ContentNodeI,java.util.List<com.freshdirect.cms.ContentNodeI>>>",
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
