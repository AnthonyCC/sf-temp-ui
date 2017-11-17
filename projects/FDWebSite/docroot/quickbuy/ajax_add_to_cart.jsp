<%@page import="com.freshdirect.fdstore.customer.FDUserCouponUtil"%>
<%@ page language="java" contentType="text/plain; charset=UTF-8" pageEncoding="UTF-8"
%><%@ taglib uri='freshdirect' prefix='fd'
%><%@page import="com.freshdirect.framework.util.log.LoggerFactory"
%><%@page import="com.freshdirect.framework.webapp.ActionError"
%><%@page import="com.freshdirect.fdstore.content.ContentFactory"
%><%@page import="com.freshdirect.fdstore.content.ContentNodeModel"
%><%@page import="com.freshdirect.fdstore.content.ProductModel"
%><%@page import="java.util.List"
%><%@page import="com.freshdirect.fdstore.customer.FDCartLineI"
%><%@page import="org.apache.log4j.Logger"
%><%@page import="com.freshdirect.fdstore.customer.EnumQuickbuyStatus"
%><%@page import='org.json.JSONObject' 
%><%@page import="com.freshdirect.webapp.taglib.fdstore.ProductCartStatusMessageTag"
%><%@page import="com.freshdirect.fdstore.ecoupon.*"
%><%@ page import="com.freshdirect.webapp.taglib.fdstore.display.FDCouponTag" 
%><%
/* 
 *	changed to return a json object instead of plain html
 *	this also assumes a single item ajax ATC
 */
JSONObject json = new JSONObject();

// add cache-control header to workaround IOS6 bug
response.setHeader("Cache-control", "no-cache");
%><fd:CheckLoginStatus noRedirect="true" id="ajax_atc_user" /><%!
final Logger LOGGER = LoggerFactory.getInstance("/quickbuy/ajax_add_to_cart.jsp");

String getStatusMessage(EnumQuickbuyStatus status) {
	StringBuilder buf = new StringBuilder();
	if (status.isError())
		buf.append("<span class=\"error\">");
	else if (status.isWarning())
		buf.append("<span class=\"warning\">");
	else buf.append("<span class=\"ok\">");

	buf.append(status.getMessage());
	
	buf.append("</span>");

	return buf.toString();
}
%><%
if (session.getAttribute("fd.user") != null) {
	try {
		%><fd:FDShoppingCart result="result" id="cart" action='addMultipleToCart'><%
			if (result.isSuccess()) {
				List<FDCartLineI> cartLines = cart.getRecentOrderLines();
				if (cartLines.size() > 0) {
					LOGGER.info("add to cart successful");
					json.put("statusHtml", getStatusMessage(EnumQuickbuyStatus.ADDED_TO_CART));
					
					FDCartLineI recCartline = cartLines.get(0);

					FDCustomerCoupon curCoupon_viewcart = null;
					curCoupon_viewcart = ajax_atc_user.getCustomerCoupon(cartLines.get(0), EnumCouponContext.VIEWCART);
					if (curCoupon_viewcart != null) {
						FDCouponTag manFdCouponTag = new FDCouponTag();
						manFdCouponTag.setCoupon(curCoupon_viewcart);
						//
						/* if (recCartline.hasCouponApplied()) {
							manFdCouponTag.setCouponStatusText(EnumCouponStatus.COUPON_APPLIED.getDescription());
						} */
						/* if(EnumCouponStatus.COUPON_APPLIED.equals(curCoupon_viewcart.getStatus()) && !cart.getRecentlyAppliedCoupons().contains(curCoupon_viewcart.getCouponId())){
							manFdCouponTag.setCouponStatusText("");
						}else if(EnumCouponStatus.COUPON_CLIPPED_ACTIVE.equals(curCoupon_viewcart.getStatus()) && cart.getRecentlyAppliedCoupons().contains(curCoupon_viewcart.getCouponId())){
							manFdCouponTag.setCouponStatusText(EnumCouponStatus.COUPON_APPLIED.getDescription());
						} */
						EnumCouponStatus status = FDUserCouponUtil.getCouponStatus(curCoupon_viewcart, cart.getRecentlyAppliedCoupons());
						if(null != status && status.isDisplayMessage()){
							manFdCouponTag.setCouponStatusText(status.getDescription());
						}else{
							manFdCouponTag.setCouponStatusText("");
						}
						manFdCouponTag.initContent(pageContext);
						/* uncomment to only return min qty not met */
						if (manFdCouponTag.isClipped()) {
							//if (curCoupon_viewcart.getStatus().equals(EnumCouponStatus.COUPON_MIN_QTY_NOT_MET)) {
								json.put("couponStatusHtml", manFdCouponTag.getStatusTextHtml());
							//} else {
							//	json.put("couponStatusHtml", "");
							//}
						}
					}
				} else {
					LOGGER.warn("no items were added - this is unexpected");
					json.put("statusHtml", getStatusMessage(EnumQuickbuyStatus.NO_OP));
				}
			} else {
				ActionError error = result.getErrors().iterator().next();
				if (error.getType().startsWith("quantity")) {
					if (error.getDescription().indexOf("there is a limit of") != -1) {
						LOGGER.info("quantity at maximum limit");
						json.put("statusHtml", getStatusMessage(EnumQuickbuyStatus.QUANTITY_LIMIT));
					} else {
						LOGGER.info("quantity not specified");
						json.put("statusHtml", getStatusMessage(EnumQuickbuyStatus.SPECIFY_QUANTITY));
					}
				}
			}
		%></fd:FDShoppingCart><%
	} catch (Exception e) {
		LOGGER.error("error processing request: " + request.getQueryString(), e);
		json.put("statusHtml", getStatusMessage(EnumQuickbuyStatus.ERROR));
	}
} else {
	LOGGER.warn("user not logged in");
	json.put("statusHtml", getStatusMessage(EnumQuickbuyStatus.NOT_LOGGED_IN));
}

if (request.getParameter("productId_0") != null) {
	ContentNodeModel pm = ContentFactory.getInstance().getContentNode( "Product", request.getParameter("productId_0") );
	ProductCartStatusMessageTag prodCartStatusTag = new ProductCartStatusMessageTag();
	prodCartStatusTag.setProduct((ProductModel)pm);
	prodCartStatusTag.setPageContext(pageContext);
	prodCartStatusTag.setFDUser(ajax_atc_user);
	String statusHtml = (json.get("statusHtml")!=null)? json.get("statusHtml").toString() : "";
	statusHtml += "<span class=\"in-cart\" hidden style=\"display:none;\">"+prodCartStatusTag.getContent()+"</span>";
	json.put("statusHtml", statusHtml);
} %><%= json.toString() %>