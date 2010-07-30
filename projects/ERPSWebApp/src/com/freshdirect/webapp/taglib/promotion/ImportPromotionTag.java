package com.freshdirect.webapp.taglib.promotion;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.promotion.EnumPromotionStatus;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewManager;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewModel;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.webapp.util.json.FDPromotionJSONSerializer;
import com.metaparadigm.jsonrpc.JSONSerializer;

public class ImportPromotionTag extends BodyTagSupport {
	private static final long serialVersionUID = 1L;
	private static Category LOGGER = LoggerFactory.getInstance(ImportPromotionTag.class);

	
	String promo;
	String fieldName;
	String result;
	
	String jsonContent;
	
	public void setPromo(String promo) {
		this.promo = promo;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public void setJsonContent(String jsonContent) {
		this.jsonContent = jsonContent;
	}
	
	
	
	public int doStartTag() throws JspException {
		FDPromotionNewModel result;
		try {

			result = this.getResult();

		} catch (Exception ex) {
			LOGGER.warn("Exception occured in getResult", ex);
			throw new JspException(ex);
		}

		pageContext.setAttribute(this.promo, result);

		return EVAL_BODY_BUFFERED;
	}

	
	
	protected FDPromotionNewModel getResult() throws Exception {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		ActionResult ar = new ActionResult();
		FDPromotionNewModel promotion = null;


		String jsonForm = getSerializedPromo(request, ar);


		promotion = doDeserialize(ar, jsonForm);

		if (promotion != null) {
			checkPromoCode(request, ar, promotion);
	
			// cleanup promotion by removing invalid FKs
			promotion.removeReferences();
			
			// reset state
			promotion.setStatus(EnumPromotionStatus.DRAFT);
		}
		
		if (result != null)
			pageContext.setAttribute(this.result, ar);
		if (jsonForm != null && jsonContent != null)
			pageContext.setAttribute(this.jsonContent, jsonForm);

		return promotion;
	}


	/**
	 * Deserialize promotion from JSON string
	 * 
	 * @param result
	 * @param jsonForm
	 * @return promotion object
	 */
	private FDPromotionNewModel doDeserialize(ActionResult result, String jsonForm) {
		FDPromotionNewModel promotion = null;
		
		if (jsonForm != null) {
			try {
				JSONSerializer ser = new JSONSerializer();
				
				ser.registerDefaultSerializers();
				ser.registerSerializer(FDPromotionJSONSerializer.getInstance());
				
				promotion = (FDPromotionNewModel) ser.fromJSON(jsonForm);
			} catch (Exception e) {
				LOGGER.error("Failed to parse imported promotion", e);
				result.addError(true, "promo.import", "Failed to parse promotion import file.");
			}
		} else {
			result.addError(true, "promo.import", "Failed to load promotion import file.");
		}
		return promotion;
	}




	/**
	 * Extract serialized form of promotion from request.
	 * 
	 * @param request
	 * @param result
	 * @return JSON string containing serialized promotion
	 * @throws FileUploadException
	 */
	private String getSerializedPromo(HttpServletRequest request,
			ActionResult result) throws FileUploadException {
		String jsonForm = null;

		if (ServletFileUpload.isMultipartContent(request)) {
			// try to handle upload case
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			List<FileItem> items = (List<FileItem>) upload.parseRequest(request);
			
			for (FileItem f : items) {
				System.err.println(f.getFieldName());
				if (this.fieldName.equalsIgnoreCase(f.getFieldName()) ) {
					FileItem item = (FileItem) items.get(0);
					jsonForm = item.getString();

					break;
				}
			}
		} else if ( (jsonForm = request.getParameter("promoFile")) == null) {
			// or get promo from simple form PUT data
			result.addError(true, "promo.import", "No import file specified.");
		}
		return jsonForm;
	}


	/**
	 * @param request
	 * @param result
	 * @param promotion
	 * @throws FDResourceException
	 */
	private boolean checkPromoCode(HttpServletRequest request, ActionResult result,
			FDPromotionNewModel promotion) throws FDResourceException {
		
		// check if new promo code is entered in UI
		String promoCode = request.getParameter("newPromoCode");
		if (promoCode != null && !"".equals(promoCode)) {
			LOGGER.debug("Using overridden promo code " + promoCode);
		} else {
			// get default code
			promoCode = promotion.getPromotionCode();
			LOGGER.debug("Using existing promo code " + promoCode);
		}

		if (FDPromotionNewManager.isPromotionCodeUsed(promoCode)) {
			result.addError(true, "promo.code", "Code " + promoCode + " is already used.");
			return false;
		} else {
			// code is fine so store in promotion
			promotion.setPromotionCode(promoCode);
		}

		// set additional new attributes
		String newName = request.getParameter("newName");
		if (newName != null && !"".equals(newName)) {
			LOGGER.debug("Override name " + newName);
			promotion.setName(newName);
		}

		String newRedemptionCode = request.getParameter("newRedemptionCode");
		if (newRedemptionCode != null && !"".equals(newRedemptionCode)) {
			LOGGER.debug("Override redemption code with " + newRedemptionCode);
			promotion.setRedemptionCode(newRedemptionCode);
		} else {
			LOGGER.debug("Usin original redemption code " + promotion.getRedemptionCode());
		}
		return true;
	}


	public static class TagEI extends TagExtraInfo {
		public VariableInfo[] getVariableInfo(TagData data) {

			return new VariableInfo[] {
				new VariableInfo(
					data.getAttributeString("promo"), FDPromotionNewModel.class.getName(),
					true,
					VariableInfo.NESTED ),
				new VariableInfo(
					data.getAttributeString("result"), ActionResult.class.getName(),
					true,
					VariableInfo.NESTED ),
				new VariableInfo(
					data.getAttributeString("jsonContent"),	String.class.getName(),
					true,
					VariableInfo.NESTED )
			};

		}
	}
}
