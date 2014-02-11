package com.freshdirect.webapp.taglib.content.nutrition;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.content.nutrition.ErpNutritionModel;
import com.freshdirect.content.nutrition.ErpNutritionType;
import com.freshdirect.content.nutrition.panel.NutritionPanel;
import com.freshdirect.erp.ErpFactory;
import com.freshdirect.fdstore.FDNutrition;
import com.freshdirect.fdstore.FDNutritionCache;
import com.freshdirect.fdstore.FDNutritionPanelCache;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.framework.xml.XMLSerializer;
import com.freshdirect.webapp.util.NutritionInfoPanelRendererUtil;

/**
 *	Simple JSP tag to display a nutrition panel given a sku-code.
 *	
 *	input : skuCode
 *
 *	output : nutrition panel will be rendered into the page
 *
 * @author treer
 */
public class NutritionPanelTag extends BodyTagSupport {

	private static final long serialVersionUID = -5565493276032796655L;
	
	private static final Logger LOG = LoggerFactory.getInstance( NutritionPanelTag.class );

	private static final ObjectMapper	mapper = new ObjectMapper();

	private String	skuCode;
	private ErpNutritionModel nutritionModel;
	private boolean showErpsExtra = false;
	private boolean useCache = true;

	
	// Html fragments to embed into page
				
	private static final String htmlCommonIncludes = 
			"<script type=\"text/javascript\" src=\"/assets/javascript/json2.min.js\"></script>\n"
			+"<script type=\"text/javascript\" src=\"/assets/javascript/es5-shim.min.js\"></script>\n"
			+"<script type=\"text/javascript\" src=\"/assets/javascript/jquery.mustache.js\"></script>\n"
			+"<script type=\"text/javascript\" src=\"/assets/javascript/nutrition_panel.js\"></script>\n";
	
	private static final String htmlJsonMarker = "###JSON###";
	private static final String htmlDrugInclude = 
			"<div id=\"drugpanel\"></div>\n"
			+"<script>drugPanel(jQuery,"+htmlJsonMarker+", { container:jQuery('#drugpanel') })</script>\n";
	

	
	public void setSkuCode( String skuCode ) {
		this.skuCode = skuCode;
	}

	public void setNutritionModel( ErpNutritionModel nutritionModel ) {
		this.nutritionModel = nutritionModel;
	}

	public void setShowErpsExtra( boolean showErpsExtra ) {
		this.showErpsExtra = showErpsExtra;
	}

	public void setUseCache( boolean useCache ) {
		this.useCache = useCache;
	}
	
	@Override
	public int doStartTag() throws JspException {

		try {
			
			if ( skuCode == null ) {
				LOG.error( "Error in nutrition panel: skuCode is null !" );
				return SKIP_BODY;
			}

			NutritionPanel panel = null;
			
			if ( useCache ) {
				// For storefront get panel from cache
				panel = FDNutritionPanelCache.getInstance().getNutritionPanel( skuCode );
			} else {
				// No caching for erpsadmin, just get the real stuff directly
				panel = ErpFactory.getInstance().getNutritionPanel( skuCode );
			}

			if ( panel == null ) {
				return renderClassicPanel();
			}			
			
			// write out the stuff
			Writer writer = new StringWriter();
			mapper.writeValue( writer, panel );
			
            JspWriter out = pageContext.getOut();
			out.print( htmlCommonIncludes );
			out.print( htmlDrugInclude.replace( htmlJsonMarker, writer.toString() ) );

			// set pagecontext attributes 
			pageContext.setAttribute( "nutritionType", panel.getType().getJsName() );
			
			return EVAL_BODY_BUFFERED;

		} catch ( FDResourceException e ) {
			LOG.error( "Error in nutrition panel for sku:"+skuCode, e );
		} catch ( JsonProcessingException e ) {
			LOG.error( "Error in nutrition panel for sku:"+skuCode, e );
		} catch ( IOException e ) {
			LOG.error( "Error in nutrition panel for sku:"+skuCode, e );
		}

		return SKIP_BODY;
	}

	
	private int renderClassicPanel() throws IOException {
		
        JspWriter out = pageContext.getOut();
        
        // If we did not get the nutritionModel then get it from the cache
        if ( nutritionModel == null ) {
        	nutritionModel = FDNutritionCache.getInstance().getNutrition( skuCode );
        }
        
        if ( nutritionModel == null ) {
        	LOG.error( "Did not find any nutrition information for sku:"+skuCode );
        	return SKIP_BODY;
        }
        
        /**
        
        if ( showErpsExtra ) {
        	
	        String infoSource = nutritionModel.getUomFor(ErpNutritionType.SOURCE);
	        if ( infoSource != null && infoSource.trim().length() > 0 ) {
	        	out.print( "Information source: " + infoSource + "<br/>" );
	        }
	
			double netCarbs = nutritionModel.getNetCarbs();		
			if ( netCarbs > 0 ) {
				out.print( "Net Carbs: " + netCarbs + "<br/>");
			}
			
			out.print( "<br/>" );
			
        }
		
		List<FDNutrition> nutritionList = new ArrayList<FDNutrition>();
		for ( Iterator<String> nIter = nutritionModel.getKeyIterator(); nIter.hasNext(); ) {
			String key = nIter.next();
			if ( "IGNORE".equalsIgnoreCase( key ) ) {
				out.print( "<b>This information is currently hidden from the website</b><br/><br/>" );
			}
			FDNutrition fdn = new FDNutrition( ErpNutritionType.getType( key ).getDisplayName(), nutritionModel.getValueFor( key ), nutritionModel.getUomFor( key ) );
			nutritionList.add( fdn );
		}		
        
        
        try {
        	
        	ServletContext srvCtx = pageContext.getServletContext();
	        InputStream xslStream = srvCtx.getResourceAsStream( "/WEB-INF/shared/xml/nutrition_label.xsl" );
			Transformer transformer = TransformerFactory.newInstance().newTransformer( new StreamSource( xslStream ) );
			
			org.dom4j.Document doc = new XMLSerializer().serializeDocument("nutrition", nutritionList);
	        StringWriter st = new StringWriter();
			transformer.transform(new org.dom4j.io.DocumentSource(doc), new StreamResult(st));
			
			out.print( st.toString() );
			
			return EVAL_BODY_BUFFERED;

        } catch ( TransformerException e) {
        	LOG.error( "XSL Transformer error while rendering classic nutrition panel for sku:"+skuCode, e );
        	return SKIP_BODY;
		}
		
		**/
        
        if (NutritionInfoPanelRendererUtil.renderClassicPanel(nutritionModel, showErpsExtra, out, pageContext.getServletContext()) ) {
			return EVAL_BODY_BUFFERED;
        }
    	return SKIP_BODY;
	}
}
