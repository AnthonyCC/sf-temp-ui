package com.freshdirect.webapp.taglib.content.nutrition;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.content.nutrition.panel.NutritionPanel;
import com.freshdirect.content.nutrition.panel.NutritionPanelType;
import com.freshdirect.content.nutrition.panel.ProtoPanel;
import com.freshdirect.erp.ErpFactory;
import com.freshdirect.fdstore.FDResourceException;

public class NutritionPanelControllerTag extends com.freshdirect.framework.webapp.BodyTagSupport {

	private static final long serialVersionUID = -1519469814297740033L;
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	private String redirectUrl;
	private String skuCode;
	private NutritionPanelType type;
	private boolean isSample = false;

	public void setSkuCode( String skuCode ) {
		this.skuCode = skuCode;
	}	
	public void setType( String type ) {
		try {
			this.type = NutritionPanelType.valueOf( type );
		} catch (Exception e) {
			this.type = null;
		}
	}	
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
	public void setSample(boolean isSample) {
		this.isSample = isSample;
	}
	
	@Override
	public int doStartTag() throws JspException {

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

		try {
			if (skuCode == null) {
				return SKIP_BODY;
			}

			if ( "GET".equalsIgnoreCase( request.getMethod() ) ) {

				doGetInternal();
				
			} else if ( "POST".equalsIgnoreCase( request.getMethod() ) ) {
				
				String deleteSku = request.getParameter( "delete" );
				String panelJson = request.getParameter( "panel" );
				String redirect = request.getParameter("redirect");
				
				if ( deleteSku != null ) {

					ErpFactory.getInstance().deleteNutritionPanel( deleteSku );
					((HttpServletResponse) pageContext.getResponse()).sendRedirect(this.redirectUrl + "?skuCode=" + skuCode);

				} else if ( panelJson != null ) {
					
					NutritionPanel panel = mapper.readValue( panelJson, NutritionPanel.class );
					
					// Check for empty panels
					if ( panel == null || panel.getSections() == null || panel.getSections().isEmpty() ) {
						// Empty panel -> Delete instead
						ErpFactory.getInstance().deleteNutritionPanel( panel.getSkuCode() );
					} else {
						// Save panel
						ErpFactory.getInstance().saveNutritionPanel( panel );
						pageContext.setAttribute( "panel", panelJson );
					}
					
					if(redirect == null || (redirect != null && Boolean.parseBoolean(redirect)) ) {
						((HttpServletResponse) pageContext.getResponse()).sendRedirect(this.redirectUrl + "?skuCode=" + skuCode);						
					}
				} else {
					doGetInternal();
				}

			}

		} catch (FDResourceException e) {
			throw new JspException(e);
		} catch (JsonGenerationException e) {
			throw new JspException(e);
		} catch (JsonMappingException e) {
			throw new JspException(e);
		} catch (IOException e) {
			throw new JspException(e);
		}

		return EVAL_BODY_BUFFERED;

	}
	
	private void doGetInternal() throws JspException, FDResourceException {

		Writer writer = null;

		NutritionPanel panel = ErpFactory.getInstance().getNutritionPanel(skuCode);

		pageContext.setAttribute("skuCode", skuCode);
		pageContext.setAttribute("protoPanel", false);
		
		if (panel == null && type != null) {
			panel = ProtoPanel.createProtoPanel(type,isSample);
			panel.setSkuCode(skuCode);
			pageContext.setAttribute("protoPanel", true);
		}
		
		if ( panel != null ) {
			writer = new StringWriter();
			try {
				mapper.writeValue(writer, panel);
				pageContext.setAttribute("panel", writer.toString());
			} catch (JsonProcessingException e) {
				throw new JspException(e);
			} catch ( IOException e ) {
				throw new JspException(e);
			}
		}

	}
	
}


