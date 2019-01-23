<%@ page import="java.util.*" %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import='com.freshdirect.webapp.util.*'%>
<%@ page import='com.freshdirect.framework.util.*'%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-template-1.0" prefix='tmpl' %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic-1.0" prefix='logic'%>
<%@ taglib uri="/WEB-INF/shared/tld/freshdirect.tld" prefix='fd' %>
<%@ taglib uri="/WEB-INF/tld/crm.tld" prefix="crm" %>
<style>
.case_content_red_field {
color: #CC0000;
font-weight: bold;
font-size: 10pt;
}

	.yui-skin-sam .yui-pg-container {
		text-align: right;
	}

	.yui-skin-sam .yui-dt table {
		width: 100% !important;
		BORDER: 0px !important;
		font-family: Verdana,Arial,sans-serif !important;
	}
	
	.yui-skin-sam .yui-dt thead {		
		font-size: 11px;
		font-weight: bold !important;
		border: none;
	}
	
	.yui-skin-sam .yui-dt th, .yui-skin-sam .yui-dt th a {
		font-weight:bold !important;
		text-decoration:underline !important;
		font-size: 10pt;
	}
	
	.yui-skin-sam th.yui-dt-asc .yui-dt-liner	{
		background: none !important;
	}
	
	.yui-skin-sam th.yui-dt-asc, .yui-skin-sam th.yui-dt-desc, .yui-skin-sam th.yui-dt-asc, .yui-skin-sam th.yui-dt-desc {
		background: #DDDDDD !important;
	}
	
	.yui-skin-sam .yui-dt th {
		background: #DDDDDD !important;
	}
	
	.yui-skin-sam .yui-dt th, .yui-skin-sam .yui-dt td {
		border-width: 0 !important;
		text-align: left;		
	}
	
	.yui-dt-label {
		font-size: 11px;
		font-weight: bold !important;
	}
	
	.yui-skin-sam tr.yui-dt-even td.yui-dt-asc, .yui-skin-sam tr.yui-dt-even td.yui-dt-desc {
		background-color: #FFFFFF !important;
	}
	
	.yui-skin-sam tr.yui-dt-odd, .yui-skin-sam tr.yui-dt-odd td.yui-dt-asc, .yui-skin-sam tr.yui-dt-odd td.yui-dt-desc {
		background-color: #EEEEEE !important;		
	}
	
	
	.yui-skin-sam .yui-dt-liner {
		text-align: center;
	}
	
	.yui-skin-sam .yui-pg-page {
		border: 0px !important;
		padding: 2px !important;		
	}
	
	#yui-history-iframe {
	  position:absolute;
	  top:0; left:0;
	  width:1px; height:1px; /* avoid scrollbars */
	  visibility:hidden;
	}
	
	.yui-skin-sam .yui-dt-paginator {
		font-weight: bold;
	}
	
	.yui-skin-sam .yui-pg-first, .yui-skin-sam .yui-pg-previous, .yui-skin-sam .yui-pg-next, .yui-skin-sam .yui-pg-last, .yui-skin-sam .yui-pg-current, .yui-skin-sam .yui-pg-pages, .yui-skin-sam .yui-pg-page {
		font-family: Verdana,Arial,sans-serif !important;
		font-size: 9px;
		font-weight: bold;
	}
}
</style>

<script language="javascript">
Calendar.setup(
		{
			showsTime : false,
			electric : false,
			inputField : "activity_cal_start",
			ifFormat : "%m/%d/%Y",
			singleClick: true,
			button : "activity_cal_start_trigger"
		}
	);
Calendar.setup(
		{
			showsTime : false,
			electric : false,
			inputField : "activity_cal_end",
			ifFormat : "%m/%d/%Y",
			singleClick: true,
			button : "activity_cal_end_trigger"
		}
	);
</script>
