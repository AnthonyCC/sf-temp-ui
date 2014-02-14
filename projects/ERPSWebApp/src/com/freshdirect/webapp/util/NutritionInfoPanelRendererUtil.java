package com.freshdirect.webapp.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

import com.freshdirect.content.nutrition.ErpNutritionModel;
import com.freshdirect.content.nutrition.ErpNutritionType;
import com.freshdirect.fdstore.FDNutrition;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.xml.XMLSerializer;

public class NutritionInfoPanelRendererUtil {
	private static final Logger LOG = LoggerFactory.getInstance( NutritionInfoPanelRendererUtil.class );
	private static Transformer transformer = null;
	
	public static boolean renderClassicPanel(ErpNutritionModel nutritionModel, boolean showErpsExtra, Writer out, ServletContext srvCtx) throws IOException {
		
        // JspWriter out = pageContext.getOut();
        
        // If we did not get the nutritionModel then get it from the cache
        /** if ( nutritionModel == null ) {
        	nutritionModel = FDNutritionCache.getInstance().getNutrition( skuCode );
        }**/
        
        if ( nutritionModel == null ) {
        	LOG.error( "Did not find any nutrition information:");
        	return false;
        }
        
        if ( showErpsExtra ) {
        	
	        String infoSource = nutritionModel.getUomFor(ErpNutritionType.SOURCE);
	        if ( infoSource != null && infoSource.trim().length() > 0 ) {
	        	out.write( "Information source: " + infoSource + "<br/>" );
	        }
	
			double netCarbs = nutritionModel.getNetCarbs();		
			if ( netCarbs > 0 ) {
				out.write( "Net Carbs: " + netCarbs + "<br/>");
			}
			
			out.write( "<br/>" );
			
        }
		
		List<FDNutrition> nutritionList = new ArrayList<FDNutrition>();
		for ( Iterator<String> nIter = nutritionModel.getKeyIterator(); nIter.hasNext(); ) {
			String key = nIter.next();
			if ( "IGNORE".equalsIgnoreCase( key ) ) {
				out.write( "<b>This information is currently hidden from the website</b><br/><br/>" );
			}
			FDNutrition fdn = new FDNutrition( ErpNutritionType.getType( key ).getDisplayName(), nutritionModel.getValueFor( key ), nutritionModel.getUomFor( key ) );
			nutritionList.add( fdn );
		}		
        
        
        try {
        	
        	// ServletContext srvCtx = pageContext.getServletContext();
	        if (transformer == null) {
	        	InputStream xslStream = srvCtx.getResourceAsStream( "/WEB-INF/shared/xml/nutrition_label.xsl" );
				transformer = TransformerFactory.newInstance().newTransformer( new StreamSource( xslStream ) );
	        }
			
			org.dom4j.Document doc = new XMLSerializer().serializeDocument("nutrition", nutritionList);
	        StringWriter st = new StringWriter();
			synchronized(transformer) {
				transformer.transform(new org.dom4j.io.DocumentSource(doc), new StreamResult(st));
			}
			
			out.write( st.toString() );
			
			return true;

        } catch ( TransformerException e) {
        	LOG.error( "XSL Transformer error while rendering classic nutrition panel for sku:"+nutritionModel.getSkuCode(), e );
        	return false;
		}
	}
}
