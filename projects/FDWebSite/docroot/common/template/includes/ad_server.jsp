<%@page import="com.freshdirect.fdstore.EnumEStoreId"%>
<%@ page import='com.freshdirect.framework.util.NVL'
%><%@ page import='org.apache.commons.lang.StringUtils'
%><%@ page import='java.util.*'
%><%@ page import='java.net.*'
%><%@ page import='java.io.UnsupportedEncodingException'
%><%@ page import='com.freshdirect.cms.CmsServiceLocator'
%><%@ page import='com.freshdirect.cms.contentio.xml.XmlContentMetadataService'
%><%@ page import='com.freshdirect.customer.ErpSaleInfo'
%><%@ page import='com.freshdirect.customer.EnumDeliveryType'
%><%@ page import='com.freshdirect.fdstore.FDStoreProperties'
%><%@ page import='com.freshdirect.fdstore.customer.FDUserI'
%><%@ page import='com.freshdirect.fdstore.customer.FDOrderI'
%><%@ page import='com.freshdirect.fdstore.customer.FDCartLineI'
%><%@ page import='com.freshdirect.fdstore.customer.ProfileModel'
%><%@ page import='com.freshdirect.fdstore.customer.adapter.CustomerRatingAdaptor'
%><%@ page import='com.freshdirect.fdstore.customer.FDCustomerManager'
%><%@ page import='com.freshdirect.fdstore.customer.FDProductSelectionI'
%><%@ page import='com.freshdirect.fdstore.customer.FDProductCollectionI'
%><%@ page import='com.freshdirect.storeapi.content.ContentFactory'
%><%@ page import='com.freshdirect.common.customer.EnumServiceType'
%><%@ page import='com.freshdirect.webapp.taglib.fdstore.SessionName'
%><%@ page import='com.freshdirect.deliverypass.EnumDlvPassStatus'
%><%@ page import='com.freshdirect.deliverypass.EnumDlvPassProfileType'
%><%@ page import='com.freshdirect.fdstore.deliverypass.DeliveryPassUtil'
%><%@ page import='com.freshdirect.framework.util.DateUtil'
%><%@ page import='com.freshdirect.framework.util.QueryStringBuilder'
%><%@ page import='com.freshdirect.deliverypass.EnumDPAutoRenewalType'
%><%@ page import='com.freshdirect.smartstore.fdstore.SmartStoreUtil'
%><%@ page import='com.freshdirect.fdstore.zone.FDZoneInfoManager'
%><%@ page import='com.freshdirect.fdstore.ZonePriceListing'
%><%@ page import='com.freshdirect.webapp.util.JspMethods'
%><%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"
%><%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"
%><%@ page import='com.freshdirect.fdstore.orderhistory.OrderHistoryService'
%><%@ taglib prefix="fd" uri="freshdirect"
%><%

	if (FDStoreProperties.isAdServerEnabled() || FDStoreProperties.isDfpEnabled()) {


		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);

		String sitePage = request.getAttribute("sitePage") == null ? "www.freshdirect.com"
				: (String) request.getAttribute("sitePage");
		String listPos = request.getAttribute("listPos") == null ? "SystemMessage"
				: (String) request.getAttribute("listPos");

		String[] listPosArray = listPos.split(",");%>

<%
	if (!FDStoreProperties.isDfpEnabled()) {
%>
  <!-- OAS SETUP begin -->
  <script type="text/javascript">
  protocol = 'http://';
  if(document.location.href.substring(0,5) == 'https'){
  	protocol = 'https://';
  }

  //configuration
  OAS_url =  protocol + '<%=FDStoreProperties.getAdServerUrl()%>';
  OAS_sitepage = '<%=sitePage%>';
  OAS_listpos = '<%=listPos%>';
  OAS_query = '<fd:AdQueryString/>';
  OAS_target = '';
  //end of configuration

  OAS_version = 10;
  OAS_rn = '001234567890'; OAS_rns = '1234567890';
  OAS_rn = new String (Math.random()); OAS_rns = OAS_rn.substring (2, 11);
  function OAS_NORMAL(pos) {
    document.write('<A HREF="' + OAS_url + 'click_nx.ads/' + OAS_sitepage + '/1' + OAS_rns + '@'
  + OAS_listpos + '!' + pos + '?' + OAS_query + '" TARGET=' + OAS_target + '>');
    document.write('<IMG SRC="' + OAS_url + 'adstream_nx.ads/' + OAS_sitepage + '/1' + OAS_rns +
  '@' + OAS_listpos + '!' + pos + '?' + OAS_query + '" BORDER=0><\/A>');
  }
  function OAS_RICH(pos) {
  }
  </script>
  <%
  	if (!FDStoreProperties.getAdServerUsesDeferredImageLoading()) {
  %>
  <script type="text/JavaScript">
  OAS_version = 11;
  if ((navigator.userAgent.indexOf('Mozilla/3') != -1) ||
    (navigator.userAgent.indexOf('Mozilla/4.0 WebTV') != -1))
  	OAS_version = 10;

  if (OAS_version >= 11) {
	try {
		if ($jq && fd && fd.common && fd.commmon.dispatcher) {
			$jq(function() {
				fd.common.dispatcher.signal('oas_descriptiveContent', {});
			});
		} else {
			document.write('<scr' + 'ipt type="text/javascript" src="' + OAS_url + 'adstream_mjx.ads/' +
					  OAS_sitepage + '/1' + OAS_rns + '@' +
					  OAS_listpos + '?' + OAS_query + '"><\/script>');
		}
	} catch(e) {
		/* fallback for pages without jquery */
		document.write('<scr' + 'ipt type="text/javascript" src="' + OAS_url + 'adstream_mjx.ads/' +
				  OAS_sitepage + '/1' + OAS_rns + '@' +
				  OAS_listpos + '?' + OAS_query + '"><\/script>');
	}
  }
  </script>
  <script type="text/javascript">
  function OAS_AD(pos) {
  	if (OAS_version >= 11)
  		OAS_RICH(pos);
  	else
  		OAS_NORMAL(pos);
  		if(typeof window.parent['OAS_DONE'] =='function') { OAS_DONE(pos); }
  }
  </script><%
  	}

  		if (FDStoreProperties.getAdServerUsesDeferredImageLoading()) {
  %><iframe name="oasif" id="oas_IF" width="1" height="1" src="about:blank" style="visibility: hidden; border: 0; position: absolute; top: 1px; left: 1px;"></iframe>
  <script type="text/javascript">
  OAD_POS = OAS_listpos.split(/,/);


  function createOASFrame() {
  	// write static content to iframe
  	var ifr = document.getElementById('oas_IF');
  	var idoc;
  	if (ifr.contentWindow) {
  	  // IE way
  	  idoc = ifr.contentWindow.document;
  	} else if (ifr.contentDocument) {
  	  // Mozilla way
  	  idoc = ifr.contentDocument.document ? ifr.contentDocument.document : ifr.contentDocument;
  	}

  	// Inject IFRAME content
  	idoc.open();
  	idoc.writeln('<html lang="en-US" xml:lang="en-US"><body>');

  	// modified document.write function
  	idoc.writeln("<scr" + "ipt type='text/javascript'>");
  	idoc.writeln("document._fragment='';");
  	idoc.writeln("document.fwrite=function(str){document._fragment+=str};");
  	idoc.writeln("document._write=document.write;");

      idoc.writeln("var ads_done = [];");
      idoc.writeln("var tries = 2;");
  <%for (int k = 0; k < listPosArray.length; k++) {%>
      idoc.writeln("ads_done['<%=listPosArray[k]%>'] = false;");
  <%}%>

      idoc.writeln("function copy_ad(oas_id) {");
      idoc.writeln("  var done=false; document._fragment='';");
      idoc.writeln("  var OAS_RICH=OAS_RICH || function() {};");
      idoc.writeln("  document.write=document.fwrite;OAS_RICH(oas_id);document.write=document._write;");
      idoc.writeln("  document.getElementById(oas_id).innerHTML = document._fragment;");
      idoc.writeln("  var pdiv = window.parent.document.getElementById('OAS_' + oas_id);");
      idoc.writeln("  if (pdiv) {");
      idoc.writeln("    pdiv.innerHTML = document.getElementById(oas_id).innerHTML;done=true;");
      idoc.writeln("    if(typeof window.parent['OAS_DONE'] =='function') { window.parent.OAS_DONE(oas_id); }");
      idoc.writeln("  }");
      idoc.writeln("  return done;");
      idoc.writeln("}");

      idoc.writeln("function copy_ads() {");
      idoc.writeln("  var k = 0;");
  <%for (int k = 0; k < listPosArray.length; k++) {%>
      idoc.writeln("  if (ads_done['<%=listPosArray[k]%>'] == true) {");
      idoc.writeln("    ++k;");
      idoc.writeln("  } else if (copy_ad('<%=listPosArray[k]%>')) {");
      idoc.writeln("    ads_done['<%=listPosArray[k]%>'] = true;");
      idoc.writeln("    ++k;");
      idoc.writeln("  }");
  <%}%>
      idoc.writeln("  return (k == <%=listPosArray.length%>);");
      idoc.writeln("}");

      idoc.writeln("function do_copy() {");
      idoc.writeln("  if (tries > 0 && copy_ads() == false) {");
      idoc.writeln("    --tries;");
      idoc.writeln("    window.setTimeout(do_copy, 200);");
      idoc.writeln("  }");
      idoc.writeln("}");


  	idoc.writeln("<\/script>");

  	// Put placeholder DIVs
  <%for (int k = 0; k < listPosArray.length; k++) {%>
  	idoc.writeln("<div id='<%=listPosArray[k]%>'><\/div>");
  <%}%>

  	// bootstrap loader
  	idoc.writeln('<scr' + 'ipt type="text/javascript" src="' + OAS_url + 'adstream_mjx.ads/' +
  	  OAS_sitepage + '/1' + OAS_rns + '@' +
  	  OAS_listpos + '?' + OAS_query + '" defer="defer"><\/script>');

      // handlers
  	idoc.writeln("<scr" + "ipt type='text/javascript' defer='defer'>");
  	idoc.writeln("do_copy();");
  	idoc.writeln("<\/script>");
  	idoc.writeln("<\/body><\/html>");
  	idoc.close();
  }

  // detect Safar 2 or older
  var m=navigator.appVersion.match(/Safari\/(\d+)/);
  var isOldSafari = (m && Number(m[1]) < 500);

  if (isOldSafari) {
      // delayed iframe creation for Safari 2
      document.onDocumentLoaded = createOASFrame;
  } else {
      createOASFrame();
  }


  function OAS_AD(pos) {
  	document.writeln('<div id="oas_' + pos + '"><\/div>');
  }

  // document.onDocumentLoaded = createOASIFrame;
  </script>
  <%
  	} //
  %>
  <!-- OAS SETUP end -->

  <%
  } else {
  %>
    <script>
    window.FreshDirect = window.FreshDirect || {};
    var fd = window.FreshDirect;

    fd.properties = fd.properties || {};
    fd.properties.isDFPEnabled = <%= FDStoreProperties.isDfpEnabled() ? true : false %>;
    fd.properties.dfpId = '<%= FDStoreProperties.getDfpId() %>';

    DFP_query = '<fd:AdQueryString/>';

    function OAS_AD(pos) {
    }

    </script>
  <%
    }
  %>


  <%
  	} else {
  %>
  <script type="text/javascript">
  function OAS_AD(pos) {
  }
  </script>
  <%
  	}
  %>
  <script>
  function OAS_DONE(oas_id) {
  	/*var e = document.getElementById('oas_'+oas_id);
  	if(window.jQuery && e) {
  		jQuery(document).ready(function() {
  			if (FreshDirect && FreshDirect.updateOAS && FreshDirect.updateOAS.done) {
  	  			FreshDirect.updateOAS.done([oas_id]);
  	  		}
  		});
  		jQuery(e).trigger('OAS_DONE',[oas_id]);
  		
  	}*/
  }

  </script>
