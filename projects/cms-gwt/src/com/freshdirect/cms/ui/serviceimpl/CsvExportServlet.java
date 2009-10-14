package com.freshdirect.cms.ui.serviceimpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.EnumAttributeType;
import com.freshdirect.cms.ITable;
import com.freshdirect.cms.ui.client.nodetree.ContentNodeModel;
import com.freshdirect.cms.ui.model.attributes.TableAttribute;
import com.freshdirect.cms.ui.translator.TranslatorToGwt;

public class CsvExportServlet extends HttpServlet {
	
	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
	protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		
		String nodeKey;
		String attributeKey;
		AttributeI attribute; 
		
		try { 
			nodeKey = request.getParameter( "nodeKey" );
			attributeKey = request.getParameter( "attributeKey" );
		
			attribute = ContentKey.decode( nodeKey ).getContentNode().getAttribute( attributeKey );
			EnumAttributeType type = attribute.getDefinition().getAttributeType();
	        
	        if ( type != EnumAttributeType.TABLE ) {
	        	response.sendError( HttpServletResponse.SC_NOT_ACCEPTABLE, "Invalid attribute type. Cannot export to CSV." );
	        	return;
	        }
		} catch (NullPointerException e) {
        	response.sendError( HttpServletResponse.SC_NOT_FOUND, "Content not found. Cannot export to CSV." );
        	return;
		}
        
		TableAttribute table = TranslatorToGwt.createTableAttribute( (ITable)attribute.getValue() );		

		response.setContentType( "text/csv" );
		response.addHeader( "Content-Disposition", "attachment; filename=\""+nodeKey.replace( ':', '-' )+".csv\"" );
		PrintWriter out = response.getWriter();
		
		for ( Serializable[] row : table.getRows() ) {
			for ( int i = 0; i < row.length; i++ ) {
				out.append( '"' );
				Serializable item = row[i];
				if ( item instanceof ContentNodeModel ) {
					out.append( ((ContentNodeModel)item).getLabel() );
					out.append( '[' );
					out.append( ((ContentNodeModel)item).getKey() );				
					out.append( ']' );
				} else {
					out.append( item.toString() );					
				}
				out.append( '"' );
				if ( i != row.length - 1 )
					out.append( ',' );
			}
			out.println();
		}		
	}
}
