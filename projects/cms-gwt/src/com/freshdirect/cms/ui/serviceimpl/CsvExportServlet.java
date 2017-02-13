package com.freshdirect.cms.ui.serviceimpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.EnumAttributeType;
import com.freshdirect.cms.ITable;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.ui.model.ContentNodeModel;
import com.freshdirect.cms.ui.model.attributes.TableAttribute;
import com.freshdirect.cms.ui.translator.TranslatorToGwt;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CsvExportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private static final Logger LOGGER = LoggerFactory.getInstance(CsvExportServlet.class); 

    @Override
	protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		final ContentServiceI svc = CmsManager.getInstance();
		String nodeKey;
		String attributeKey;
		AttributeI attribute; 
		
		try { 
			nodeKey = request.getParameter( "nodeKey" );
			attributeKey = request.getParameter( "attributeKey" );
		
			final ContentKey key = ContentKey.getContentKey( nodeKey );
            attribute = svc.getContentNode(key, DraftContext.MAIN).getAttribute( attributeKey );
			EnumAttributeType type = attribute.getDefinition().getAttributeType();
	        
	        if ( type != EnumAttributeType.TABLE ) {
	        	response.sendError( HttpServletResponse.SC_NOT_ACCEPTABLE, "Invalid attribute type. Cannot export to CSV." );
	        	return;
	        }
		} catch (NullPointerException e) {
        	response.sendError( HttpServletResponse.SC_NOT_FOUND, "Content not found. Cannot export to CSV." );
        	return;
		}
        
		TableAttribute table = TranslatorToGwt.createTableAttribute( (ITable)attribute.getValue(), svc, DraftContext.MAIN );		

		response.setContentType( "text/csv" );
		response.addHeader( "Content-Disposition", "attachment; filename=\""+nodeKey.replace( ':', '-' )+".csv\"" );
		PrintWriter out = response.getWriter();
		
		for ( Serializable[] row : table.getRows() ) {
			for ( int i = 0; i < row.length; i++ ) {
				out.append( '"' );
				StringBuffer buf = new StringBuffer();
				Serializable item = row[i];
				if ( item instanceof ContentNodeModel ) {
					buf.append( ((ContentNodeModel)item).getLabel() );
					buf.append( '[' );
					buf.append( ((ContentNodeModel)item).getKey() );				
					buf.append( ']' );
				} else if ( item != null ) {
					buf.append( item.toString() );				
				} else {
					buf.append( "-N/A-" );					
				}
				escapeQuotes(buf);
				out.append(buf);
				out.append( '"' );
				if ( i != row.length - 1 )
					out.append( ',' );
			}
			out.println();
		}		
	}

	private void escapeQuotes(StringBuffer buf) {
		for (int i = 0; i < buf.length(); i++) {
			if (buf.charAt(i) == '"') {
				LOGGER.info("pitypang");
				buf.insert(i, '"');
				i++;
			}
		}
	}  
}
