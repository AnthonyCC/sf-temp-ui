<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
        <title><tmpl:get name='title'/></title>
        <%@ include file="/common/template/includes/metatags.jspf" %>
        <%@ include file="/common/template/includes/i_javascripts.jspf" %>
        <link rel="stylesheet" type="text/css" href="/ccassets/css/crm.css" />
        <link rel="stylesheet" type="text/css" href="/ccassets/css/case.css" />        
        <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
        <fd:css href="/assets/css/giftcards.css"/>
        <fd:css href="/assets/css/timeslots.css"/>
        <%@ include file="/shared/template/includes/ccl.jspf" %>
<%
    {
       String onbeforeunload = (String)request.getAttribute("windowOnBeforeUnload");
       if (onbeforeunload != null && onbeforeunload.length() > 0) {
%>
    <script type="text/javascript">
       window.onbeforeunload = <%= onbeforeunload %>;
    </script>
<%
       } // if
    } // local block
%>
</head>
<body onload="<%=request.getAttribute("bodyOnLoad")%>" onunload="<%=request.getAttribute("bodyOnUnload")%>">
        <div class="crm_container">
                <div class="content">
                <%@ include file="/includes/context_help.jspf" %>

                        <%-- header on top and content below --%>

                        <jsp:include page="/includes/main_nav.jsp"/>

                        <jsp:include page='/includes/customer_header.jsp'/>

                        <jsp:include page='/includes/case_header.jsp'/>

                        <tmpl:get name="content"/>
                </div>
                <div class="footer"><jsp:include page='/includes/copyright.jsp'/></div>
        </div>
 <div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000"></div>
</body>
</html>
