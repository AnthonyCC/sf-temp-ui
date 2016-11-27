<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="com.freshdirect.fdstore.coremetrics.builder.*" %>
<%@ page import="com.freshdirect.fdstore.coremetrics.tagmodel.*" %>
<%@ page import="com.freshdirect.webapp.taglib.coremetrics.*" %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus id="user" noRedirect="true" recognizedAllowed="true" guestAllowed="true"/>
<%
try {
	
	List<List<String>> cmData = new ArrayList<List<String>>();
	
	PageViewTagModelBuilder pvTagModelBuilder = new PageViewTagModelBuilder();
	pvTagModelBuilder.setInput( PageViewTagInput.populateFromRequest(request) );

	PageViewTagModel pvTagModel = pvTagModelBuilder.buildTagModel();
	cmData.add( pvTagModel.toStringList() );
	
	
	
	ElementTagModelBuilder eTagModelBuilder = new ElementTagModelBuilder();
	eTagModelBuilder.setUser( user );
	eTagModelBuilder.setElementCategory( "someElementCategory" );
	
	ElementTagModel eTagModel = eTagModelBuilder.buildTagModel();
	cmData.add( eTagModel.toStringList() );
	
	
	
	%><fd:ToJSON object="<%= cmData %>" /><%
			
} catch (Exception e) {
	out.print("Exception:");
	e.printStackTrace();
}	
%>
