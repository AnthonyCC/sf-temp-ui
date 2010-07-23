package com.freshdirect.fdstore.promotion.pxp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.promotion.EnumPromotionStatus;
import com.freshdirect.fdstore.promotion.PromotionFactory;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewManager;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewModel;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.util.json.FDPromotionJSONSerializer;
import com.metaparadigm.jsonrpc.JSONSerializer;
import com.metaparadigm.jsonrpc.MarshallException;
import com.metaparadigm.jsonrpc.UnmarshallException;


/**
 * Promo Publish Replica Service Point.
 * 
 * @author segabor
 *
 */
public class PromoPublishServlet extends HttpServlet {
	private static final long serialVersionUID = 4693560915139423591L;

	private static Category		LOGGER				= LoggerFactory.getInstance( PromoPublishServlet.class );

	
	private static JSONSerializer ser = new JSONSerializer();
	static {
		try {
			ser.registerDefaultSerializers();
			ser.registerSerializer(FDPromotionJSONSerializer.getInstance());
		} catch (Exception e) {
			LOGGER.error("Failed to setup serializer", e);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// super.doPost(request, response);
		
		final String userAgent = request.getHeader("User-Agent");
		if (!"PromoPublish/1.0".equalsIgnoreCase(userAgent)) {
			LOGGER.error("Invalid user agent " + userAgent);
			// Invalid user agent
			sendError(response, ":(");
			return;
		}

		String payload = request.getParameter("payload");
		if (payload == null) {
			LOGGER.error("Empty payload");
			// no payload
			sendError(response, ":/");
			return;
		}
		
		LOGGER.debug("Payload:\n" + payload);
		
		List<FDPromotionNewModel> models = extractPayload(payload);
		
		if (models == null) {
			LOGGER.error("Cannot extract models from payload");
			// no payload
			sendError(response, ":[");
			return;
		}
		
		// Assume everything is fine...
		Map<String,Boolean> result =  applyChanges(models);
		
		PrintWriter writer = response.getWriter();
		try {
			writer.append(ser.toJSON(result));
		} catch (MarshallException e) {
			LOGGER.error("Failed to generate response" + e);
			writer.append("OK");
		}
	}

	protected Map<String, Boolean> applyChanges(List<FDPromotionNewModel> models) {
		Map<String,Boolean> result = new HashMap<String, Boolean>();
		
		for (FDPromotionNewModel promo : models) {
			promo.setAssignedCustomerUserIds(Collections.<String>emptySet());

			final String promoCode = promo.getPromotionCode();
			final EnumPromotionStatus status = promo.getStatus();

			boolean success = false;

			if (EnumPromotionStatus.APPROVE.equals(status)) {
				// publish or modify promotion
				try {
					LOGGER.debug("Will publish promotion " + promoCode);
					success = FDPromotionNewManager.publishPromotion(promo);
				} catch (FDResourceException e) {
					LOGGER.error("Failed to publish promotion " + promoCode, e);
				}
			} else if (EnumPromotionStatus.CANCELLING.equals(status)) {
				// cancel live promotion
				try {
					LOGGER.debug("Will cancel promotion " + promoCode);
					success = FDPromotionNewManager.cancelPromotion(promo);
					if (success) {
						// remove from cache
						PromotionFactory.getInstance().forceRefresh(promoCode);
					}
				} catch (FDResourceException e) {
					LOGGER.error("Failed to cancel promotion " + promoCode, e);
				}
			} else {
				LOGGER.error("Promotion " + promoCode + " has invalid status " + status.getName());
			}

			result.put(promoCode, success);
		}

		return result;
	}

	protected void sendError(HttpServletResponse response, String message) {
		//
		try {
			response.getWriter().append(message);
			response.setStatus(500);
		} catch (IOException e) {
			LOGGER.error("Failed to append message to response", e);
		}
	}



	/**
	 * Unserialize promotions from JSON string
	 * 
	 * @param payload serialized form of promotions
	 * @return list of promotions
	 */
	protected List<FDPromotionNewModel> extractPayload(String payload) {

		
		List<FDPromotionNewModel> models = null;
		try {
			models = (List<FDPromotionNewModel>) ser.fromJSON(payload);
		} catch (ClassCastException e) {
			LOGGER.error("Error raised during model deserialization", e);
			return null;
		} catch (UnmarshallException e) {
			LOGGER.error("Error raised during model deserialization", e);
			return null;
		} catch (Exception e) {
			LOGGER.error("Error raised during model deserialization", e);
			return null;
		}
		
		return models;
	}
}
