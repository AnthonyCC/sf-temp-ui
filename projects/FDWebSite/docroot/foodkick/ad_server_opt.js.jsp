<%@ page language="java" contentType="application/javascript; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
request.setAttribute("sitePage", "foodkick.freshdirect.com/index.jsp");
//request.setAttribute("listPos", "4mmAd1,SystemMessage,HPFeatureTop,HPFeature,HPTab1,HPTab2,HPTab3,HPTab4,HPFeatureBottom,HPWideBottom,HPLeftBottom,HPMiddleBottom,HPRightBottom");
request.setAttribute("listPos", "HPFeatureTop,HPTab2,HPTab3");
%>
<c:set var="content"><jsp:include page="/common/template/includes/ad_server.jsp" flush="false"/></c:set>
<%
String ad_srv_str = (String)pageContext.getAttribute("content");
out.println( ad_srv_str.replaceAll("(?i)<(/?script[^>]*)>|(?s)<!--.*?-->", "") );
%>