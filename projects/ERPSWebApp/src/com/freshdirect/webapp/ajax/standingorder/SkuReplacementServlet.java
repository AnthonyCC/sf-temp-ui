package com.freshdirect.webapp.ajax.standingorder;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.json.JSONObject;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.freshdirect.fdstore.standingorders.FDStandingOrderProductSku;
import com.freshdirect.fdstore.standingorders.FDStandingOrderSkuResultInfo;
import com.freshdirect.fdstore.standingorders.FDStandingOrdersManager;
import com.freshdirect.framework.util.log.LoggerFactory;

public class SkuReplacementServlet extends HttpServlet{

	private static final String REPLACE = "Replace";
	private static final String CHECK = "Check";
	private static final long serialVersionUID = 1L;
	private final static Category LOGGER = LoggerFactory
			.getInstance(SkuReplacementServlet.class);
	
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		FDStandingOrderSkuResultInfo fDStandingOrderSkuResultInfo = null;
		JSONObject json = null;

		try {

			String buttonAction = request.getParameter("buttonVal").trim();
			String existingSku = request.getParameter("existingSKU").trim()
					.toUpperCase();
			String replacementSku = request.getParameter("replacementSKU")
					.trim().toUpperCase();
			
			if(!existingSku.equalsIgnoreCase("") && !replacementSku.equalsIgnoreCase("") ) {
				
			
			if (!existingSku.equalsIgnoreCase(replacementSku)) {

				if (buttonAction.equalsIgnoreCase(CHECK)) {
					fDStandingOrderSkuResultInfo = FDStandingOrdersManager
							.getInstance().validateSkuCode(existingSku,
									replacementSku);
				} else if (buttonAction.equalsIgnoreCase(REPLACE)) {
					fDStandingOrderSkuResultInfo = FDStandingOrdersManager
							.getInstance().replaceSkuCode(existingSku,
									replacementSku);
				}
				
				List<FDStandingOrderProductSku> productList = fDStandingOrderSkuResultInfo
						.getProductSkuList();

				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");

				if (fDStandingOrderSkuResultInfo.getErrorMessage() != null) {
					json = new JSONObject();
					json.put("result",
							fDStandingOrderSkuResultInfo.getErrorMessage());
					response.getWriter().print(json);
				} else {
					ObjectMapper mapper = new ObjectMapper();
					mapper.configure(
							DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
							false);
					mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,
							false);
					mapper.writeValue(response.getOutputStream(), productList);
				}
			} else {
				json = new JSONObject();
				json.put("result",
						"Existing Sku and Replacement Sku cannot be same");
				response.getWriter().print(json);
			}
		}
			else {
				json = new JSONObject();
				json.put("result",
						"Existing Sku or Replacement SKU is Empty");
				response.getWriter().print(json);
			}
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String _msg = sw.getBuffer().toString();
			LOGGER.info(new StringBuilder(
					"SkuReplacementServlet failed with Exception...").append(
					_msg).toString());
			LOGGER.error(_msg);
			json = new JSONObject();
			json.put("result",
					"Operation Failed.Please Contact Development Team.Error Message Details:"
							+ e.getMessage());
			response.getWriter().print(json);
		}
	}

}
