<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.freshdirect.webapp.util.MediaUtils"%>
<%@page import="java.util.regex.Matcher"%>
<%@page import="java.util.regex.Pattern"%>
<%@page import="java.util.Iterator" %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%!
	/* find the registers. only finds register() calls */
	public ArrayList<String> getRegisters(String filePath) {
		String fileSrc = MediaUtils.renderHtmlToString(filePath, null);
		ArrayList<String> fileRegisters = new ArrayList<String>();
		String quotedGroup = "\"([^\"]*)\"";
		String group = "[\"]?([^,\\)]*)[\"]?";
		String seperator = "[,\\s]*";
		String pattern = "register\\("+quotedGroup+seperator+quotedGroup+seperator+group+seperator+group+"\\);";
		Matcher m = Pattern.compile(pattern).matcher(fileSrc);
		while (m.find()) {
			String regString = m.group(1)+"."+m.group(2);
			if (!"window".equals(m.group(4))) {
				/*
				if ("fd".equals(m.group(1))) {
					regString = "FreshDirect."+regString; //add "FreshDirect." equiv
				} else if ("FreshDirect".equals(m.group(1))) {
					regString = "fd."+regString; //add "fd." equiv
				}
				*/
				regString = m.group(4)+"."+regString;
			}
			fileRegisters.add(regString);
		}
		
		return fileRegisters;
	}

	/* use register regKey:filepath map to work out dependencies */
	public ArrayList<String> getDependencies(String filePath, Map<String, String> globalRegisters) {
		ArrayList<String> fileDependencies = new ArrayList<String>();
		//get src
		String fileSrc = MediaUtils.renderHtmlToString(filePath, null);
		
		Iterator it = globalRegisters.entrySet().iterator();
		while (it.hasNext()) {
		    Map.Entry pair = (Map.Entry)it.next();
		    String dep = (String)pair.getKey(); //register ref
		    String fileRef = (String)pair.getValue(); //file ref
		    if (!fileRef.equals(filePath) && fileSrc.contains(dep)) {
				fileDependencies.add(fileRef+";"+dep); //add reg ref for display usage
			}
		}
		
		return fileDependencies;
	}
%>
<%
String JS_BASE = "/assets/javascript";

ArrayList<String> inFiles = new ArrayList<String>();
inFiles.add(JS_BASE+"/fd/browse/breadcrumbs.js");
inFiles.add(JS_BASE+"/fd/browse/browseMain.js");
inFiles.add(JS_BASE+"/fd/browse/carousels.js");
inFiles.add(JS_BASE+"/fd/browse/centermenupopup.js");
inFiles.add(JS_BASE+"/fd/browse/filtertags.js");
inFiles.add(JS_BASE+"/fd/browse/media.js");
inFiles.add(JS_BASE+"/fd/browse/menu.js");
inFiles.add(JS_BASE+"/fd/browse/pager.js");
inFiles.add(JS_BASE+"/fd/browse/pageType.js");
inFiles.add(JS_BASE+"/fd/browse/searchParams.js");
inFiles.add(JS_BASE+"/fd/browse/sections.js");
inFiles.add(JS_BASE+"/fd/browse/sorter.js");
inFiles.add(JS_BASE+"/fd/captcha/captchaWidget.js");
inFiles.add(JS_BASE+"/fd/common/accessibilityTabs.js");
inFiles.add(JS_BASE+"/fd/common/accordionOpener.js");
inFiles.add(JS_BASE+"/fd/common/addtocart.js");
inFiles.add(JS_BASE+"/fd/common/addtolist.js");
inFiles.add(JS_BASE+"/fd/common/ajaxPopup.js");
inFiles.add(JS_BASE+"/fd/common/aria.js");
inFiles.add(JS_BASE+"/fd/common/atcInfo.js");
inFiles.add(JS_BASE+"/fd/common/coremetrics.js");
inFiles.add(JS_BASE+"/fd/common/coremetricsScrolling.js");
inFiles.add(JS_BASE+"/fd/common/criteo.js");
inFiles.add(JS_BASE+"/fd/common/customerRatingPopup.js");
inFiles.add(JS_BASE+"/fd/common/deletefromlist.js");
inFiles.add(JS_BASE+"/fd/common/dialog.js");
inFiles.add(JS_BASE+"/fd/common/dispatcher.js");
inFiles.add(JS_BASE+"/fd/common/ecouponinfo.js");
inFiles.add(JS_BASE+"/fd/common/editinlist.js");
inFiles.add(JS_BASE+"/fd/common/elements.js");
inFiles.add(JS_BASE+"/fd/common/errordialog.js");
inFiles.add(JS_BASE+"/fd/common/externalDataPush.js");
inFiles.add(JS_BASE+"/fd/common/features.js");
inFiles.add(JS_BASE+"/fd/common/form.js");
inFiles.add(JS_BASE+"/fd/common/helppopup.js");
inFiles.add(JS_BASE+"/fd/common/ifrPopup.js");
inFiles.add(JS_BASE+"/fd/common/lazyLoader.js");
inFiles.add(JS_BASE+"/fd/common/liveRecommender.js");
inFiles.add(JS_BASE+"/fd/common/loginlinks.js");
inFiles.add(JS_BASE+"/fd/common/metaData.js");
inFiles.add(JS_BASE+"/fd/common/modifyBRDPopup.js");
inFiles.add(JS_BASE+"/fd/common/mousespeed.js");
inFiles.add(JS_BASE+"/fd/common/overlay.js");
inFiles.add(JS_BASE+"/fd/common/pendingOrderOverlay.js");
inFiles.add(JS_BASE+"/fd/common/popup.js");
inFiles.add(JS_BASE+"/fd/common/popupcontent.js");
inFiles.add(JS_BASE+"/fd/common/productConfigurationChange.js");
inFiles.add(JS_BASE+"/fd/common/productSerialize.js");
inFiles.add(JS_BASE+"/fd/common/redirector.js");
inFiles.add(JS_BASE+"/fd/common/saverecipe.js");
inFiles.add(JS_BASE+"/fd/common/select.js");
inFiles.add(JS_BASE+"/fd/common/server.js");
inFiles.add(JS_BASE+"/fd/common/signalTarget.js");
inFiles.add(JS_BASE+"/fd/common/standingorder.js");
inFiles.add(JS_BASE+"/fd/common/svgHelpers.js");
inFiles.add(JS_BASE+"/fd/common/swapimage.js");
inFiles.add(JS_BASE+"/fd/common/tabbedRecommender.js");
inFiles.add(JS_BASE+"/fd/common/tabpanel.js");
inFiles.add(JS_BASE+"/fd/common/timeslotSelector.js");
inFiles.add(JS_BASE+"/fd/common/tooltip.js");
inFiles.add(JS_BASE+"/fd/common/tooltipPopup.js");
inFiles.add(JS_BASE+"/fd/common/touchDeviceHandler.js");
inFiles.add(JS_BASE+"/fd/common/transactionalPopup.js");
inFiles.add(JS_BASE+"/fd/common/unselectable.js");
inFiles.add(JS_BASE+"/fd/common/USQLegalWarning.js");
inFiles.add(JS_BASE+"/fd/common/USQPopup.js");
inFiles.add(JS_BASE+"/fd/common/widget.js");
inFiles.add(JS_BASE+"/fd/common/zdeliverypasspopup.js");
inFiles.add(JS_BASE+"/fd/common/zipcheck.js");
inFiles.add(JS_BASE+"/fd/common/zipcheckNotify.js");
inFiles.add(JS_BASE+"/fd/components/ajaxContent.js");
inFiles.add(JS_BASE+"/fd/components/autocomplete.js");
inFiles.add(JS_BASE+"/fd/components/backbutton.js");
inFiles.add(JS_BASE+"/fd/components/confirmPopup.js");
inFiles.add(JS_BASE+"/fd/components/contentModulesRender.js");
inFiles.add(JS_BASE+"/fd/components/customizePopup.js");
inFiles.add(JS_BASE+"/fd/components/debitSwitchNoticePopup.js");
inFiles.add(JS_BASE+"/fd/components/dfp.js");
inFiles.add(JS_BASE+"/fd/components/draftindicator.js");
inFiles.add(JS_BASE+"/fd/components/drawerWidget.js");
inFiles.add(JS_BASE+"/fd/components/ecoupons.js");
inFiles.add(JS_BASE+"/fd/components/editorialModule.js");
inFiles.add(JS_BASE+"/fd/components/globalnavpopup.js");
inFiles.add(JS_BASE+"/fd/components/informational.js");
inFiles.add(JS_BASE+"/fd/components/lightcarousel.js");
inFiles.add(JS_BASE+"/fd/components/loginform.js");
inFiles.add(JS_BASE+"/fd/components/menupopup.js");
inFiles.add(JS_BASE+"/fd/components/multiCustomizePopup.js");
inFiles.add(JS_BASE+"/fd/components/navigationHighlighter.js");
inFiles.add(JS_BASE+"/fd/components/quantitybox.js");
inFiles.add(JS_BASE+"/fd/components/readMore.js");
inFiles.add(JS_BASE+"/fd/components/reorderPopup.js");
inFiles.add(JS_BASE+"/fd/components/salesunitselector.js");
inFiles.add(JS_BASE+"/fd/components/spin.js");
inFiles.add(JS_BASE+"/fd/components/subtotal.js");
inFiles.add(JS_BASE+"/fd/components/tagManager.js");
inFiles.add(JS_BASE+"/fd/components/toggle.js");
inFiles.add(JS_BASE+"/fd/components/updateOAS.js");
inFiles.add(JS_BASE+"/fd/components/viewAllPopup.js");
inFiles.add(JS_BASE+"/fd/components/zipCheckPopup.js");
inFiles.add(JS_BASE+"/fd/explore/categoryList.js");
inFiles.add(JS_BASE+"/fd/explore/departmentList.js");
inFiles.add(JS_BASE+"/fd/explore/selector.js");
inFiles.add(JS_BASE+"/fd/explore/superdepartmentList.js");
inFiles.add(JS_BASE+"/fd/expressco/addaddresspopup.js");
inFiles.add(JS_BASE+"/fd/expressco/addpaymentmethodpopup.js");
inFiles.add(JS_BASE+"/fd/expressco/addresses.js");
inFiles.add(JS_BASE+"/fd/expressco/atpFailure.js");
inFiles.add(JS_BASE+"/fd/expressco/cartcontent.js");
inFiles.add(JS_BASE+"/fd/expressco/checkout.js");
inFiles.add(JS_BASE+"/fd/expressco/deliverypasspopup.js");
inFiles.add(JS_BASE+"/fd/expressco/drawer.js");
inFiles.add(JS_BASE+"/fd/expressco/gogreen.js");
inFiles.add(JS_BASE+"/fd/expressco/multicustomizepopup.js");
inFiles.add(JS_BASE+"/fd/expressco/ordermodifystatuspopup.js");
inFiles.add(JS_BASE+"/fd/expressco/paymentmethod.js");
inFiles.add(JS_BASE+"/fd/expressco/restrictionpopups.js");
inFiles.add(JS_BASE+"/fd/expressco/textalertpopup.js");
inFiles.add(JS_BASE+"/fd/expressco/timeselector.js");
inFiles.add(JS_BASE+"/fd/expressco/viewcart.js");
inFiles.add(JS_BASE+"/fd/FDjQuery.js");
inFiles.add(JS_BASE+"/fd/mobileweb/mobileweb_common.js");
inFiles.add(JS_BASE+"/fd/modifyOrder/modifyOrderMessage.js");
inFiles.add(JS_BASE+"/fd/modules/header/locabar_recommenders.js");
inFiles.add(JS_BASE+"/fd/modules/header/popupcart.js");
inFiles.add(JS_BASE+"/fd/modules/search/seemore.js");
inFiles.add(JS_BASE+"/fd/modules/search/statusupdate.js");
inFiles.add(JS_BASE+"/fd/modules/standingorder/nextdlvchooser.js");
inFiles.add(JS_BASE+"/fd/multisearch/searchInput.js");
inFiles.add(JS_BASE+"/fd/multisearch/searchResults.js");
inFiles.add(JS_BASE+"/fd/pdp/annotations.js");
inFiles.add(JS_BASE+"/fd/pdp/bundleDetailsPopup.js");
inFiles.add(JS_BASE+"/fd/pdp/bundleProduct.js");
inFiles.add(JS_BASE+"/fd/pdp/evenBetterPopup.js");
inFiles.add(JS_BASE+"/fd/pdp/menu.js");
inFiles.add(JS_BASE+"/fd/pdp/nutritionPopup.js");
inFiles.add(JS_BASE+"/fd/pdp/pdp_main.js");
inFiles.add(JS_BASE+"/fd/pdp/thumbnails.js");
inFiles.add(JS_BASE+"/fd/quickshop/common/accessibility.js");
inFiles.add(JS_BASE+"/fd/quickshop/common/applyCustomization.js");
inFiles.add(JS_BASE+"/fd/quickshop/common/breadcrumbs.js");
inFiles.add(JS_BASE+"/fd/quickshop/common/datalistWidget.js");
inFiles.add(JS_BASE+"/fd/quickshop/common/departments.js");
inFiles.add(JS_BASE+"/fd/quickshop/common/gridlistchange.js");
inFiles.add(JS_BASE+"/fd/quickshop/common/itemList.js");
inFiles.add(JS_BASE+"/fd/quickshop/common/listheader.js");
inFiles.add(JS_BASE+"/fd/quickshop/common/menu.js");
inFiles.add(JS_BASE+"/fd/quickshop/common/pager.js");
inFiles.add(JS_BASE+"/fd/quickshop/common/preferences.js");
inFiles.add(JS_BASE+"/fd/quickshop/common/search.js");
inFiles.add(JS_BASE+"/fd/quickshop/common/sorter.js");
inFiles.add(JS_BASE+"/fd/quickshop/common/tabMeta.js");
inFiles.add(JS_BASE+"/fd/quickshop/common/tempReplacement.js");
inFiles.add(JS_BASE+"/fd/quickshop/common/updateOAS.js");
inFiles.add(JS_BASE+"/fd/quickshop/common/ymal.js");
inFiles.add(JS_BASE+"/fd/quickshop/FDLists/FDLists.js");
inFiles.add(JS_BASE+"/fd/quickshop/FDLists/includes/fdlists.js");
inFiles.add(JS_BASE+"/fd/quickshop/FDLists/includes/listheader.js");
inFiles.add(JS_BASE+"/fd/quickshop/FDLists/includes/menu.js");
inFiles.add(JS_BASE+"/fd/quickshop/pastOrders/includes/listheader.js");
inFiles.add(JS_BASE+"/fd/quickshop/pastOrders/includes/menu.js");
inFiles.add(JS_BASE+"/fd/quickshop/pastOrders/includes/orders.js");
inFiles.add(JS_BASE+"/fd/quickshop/pastOrders/includes/search.js");
inFiles.add(JS_BASE+"/fd/quickshop/pastOrders/includes/timeFrames.js");
inFiles.add(JS_BASE+"/fd/quickshop/pastOrders/pastOrders.js");
inFiles.add(JS_BASE+"/fd/quickshop/shopFromList/includes/listheader.js");
inFiles.add(JS_BASE+"/fd/quickshop/shopFromList/includes/managelists.js");
inFiles.add(JS_BASE+"/fd/quickshop/shopFromList/includes/menu.js");
inFiles.add(JS_BASE+"/fd/quickshop/shopFromList/includes/shoppinglists.js");
inFiles.add(JS_BASE+"/fd/quickshop/shopFromList/shopFromList.js");
inFiles.add(JS_BASE+"/fd/quickshop/standingOrder/standing_order_3.js");
inFiles.add(JS_BASE+"/fd/quickshop/topItems/includes/listheader.js");
inFiles.add(JS_BASE+"/fd/quickshop/topItems/includes/menu.js");
inFiles.add(JS_BASE+"/fd/quickshop/topItems/includes/orders.js");
inFiles.add(JS_BASE+"/fd/quickshop/topItems/includes/timeFrames.js");
inFiles.add(JS_BASE+"/fd/quickshop/topItems/topItems.js");
inFiles.add(JS_BASE+"/fd/sitemap/sitemap.js");
inFiles.add(JS_BASE+"/fd/utils.js");
inFiles.add(JS_BASE+"/fd/youraccount/paymentinfo.js");

ArrayList<String> globalDependencies = new ArrayList<String>();

Map<String, Map<String, ArrayList<String>>> fileInfos = new HashMap<String, Map<String, ArrayList<String>>>();
Map<String, String> globalRegisters = new HashMap<String, String>();

/*
	manually put some global registers that won't be found automatically
	these need to be overly-specific, which makes it more difficult
	NOTE: add these to inFiles further down for filtering
*/
globalRegisters.put("}(jQuery));", "GLOBAL.jQuery"); //when jQuery is passed in to component
globalRegisters.put("$jq(", "GLOBAL.jQuery"); //when jQuery is used in general
globalRegisters.put(".mmenu(", "jQuery.mmenu"); //mobweb jQuery menu plugin

//create data holds and registers
for (String filePath : inFiles) {
	//get registers
	ArrayList<String> fileRegisters = getRegisters(filePath);
	//create data store
	Map<String, ArrayList<String>> fileInfo = new HashMap<String, ArrayList<String>>();
	fileInfo.put("dependencies", new ArrayList<String>()); //leave empty
	fileInfo.put("registers", fileRegisters); //populate
	
	//store
	fileInfos.put(filePath, fileInfo);
	
	//populate global registers for dependency reference
	for (String register : fileRegisters) {
		globalRegisters.put(register, filePath);
	}
}
//loop again to do dependencies now that we have all registers
for (String filePath : inFiles) {
	fileInfos.get(filePath).put("dependencies", getDependencies(filePath, globalRegisters));
}
%>
<!DOCTYPE html>
<html>
<head>
	<title>Javascript Dependencies</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<jwr:style src="/assets/css/test/js/dependencies.css" media="all" />
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
</head>
<body>
<div class="header">
	<span class="header-page"><span class="inverted">Javascript</span>:Dependencies</span>
	<div class="header-filters-cont">
		<div class="col-left">
			<div class="filter-dependencies-cont"><label for="filter_dependencies">Dependencies : </label><select id="filter_dependencies"></select></div>
			<div><label for="filter_registers">Registers : </label><select id="filter_registers"></select></div>
		</div>
		<div class="col-right">
			<button id="filter-apply" class="inverted">Filter</button>
			<button id="filter-clear">Clear</button>
		</div>
	</div>
</div>
<%
//do display
Iterator it = fileInfos.entrySet().iterator();
while (it.hasNext()) {
    Map.Entry pair = (Map.Entry)it.next();
    out.println("<div class=\"file-cont\">");
	    out.println("<div class=\"file-info\"><span class=\"file-info-label\"></span><span class=\"file-info-file\">"+pair.getKey()+"</span></div>");
	    Map<String, ArrayList<String>> fileInfo = (Map<String, ArrayList<String>>)pair.getValue();
	    ArrayList<String> fileInfoRegisters = (ArrayList<String>)fileInfo.get("registers");
	    int fileInfoRegistersIndex = 0;
	    for (String register : fileInfoRegisters) {
			out.println("<div class=\"file-register"+((fileInfoRegistersIndex == fileInfoRegisters.size()-1)?" last":"")+"\"><span class=\"file-register-label\"></span><span class=\"file-register-register\">"+register+"</span></div>");
			fileInfoRegistersIndex++;
		}
	    for (String dependency : (ArrayList<String>)fileInfo.get("dependencies")) {
	    	String [] disp_dependency = dependency.split(";");
			out.println("<div class=\"file-dependency\"><span class=\"file-dependency-label\"></span><span class=\"file-dependency-dependency\">"+disp_dependency[0]+"</span><span class=\"file-dependency-register\">"+disp_dependency[1]+"</span></div>");
		}
	out.println("</div>");
}

/*
	manually add "in files" to match the manual global registers from above
	so we can use them as filters in UI now that we've done the main display
*/
	inFiles.add("GLOBAL.jQuery");
	inFiles.add("jQuery.mmenu");
%>
<script>
/* output data to UI */
var FreshDirect = $jq.extend(FreshDirect, {
	test: { js: { dependencies: {
		globalFileList: <fd:ToJSON object="<%= inFiles %>" noHeaders="true"/>,
		globalRegisters: <fd:ToJSON object="<%= globalRegisters %>" noHeaders="true"/>,
		fileInfos: <fd:ToJSON object="<%= fileInfos %>" noHeaders="true"/>
	} } }
});
</script>
<jwr:script src="/assets/javascript/test/js/dependencies.js" useRandomParam="false" />
</body>
</html>