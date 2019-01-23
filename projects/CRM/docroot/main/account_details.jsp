<%@page import="com.freshdirect.fdstore.FDStoreProperties"%>
<%@page import="com.freshdirect.fdstore.customer.ejb.FDCustomerEStoreModel"%>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@ page import="com.freshdirect.fdstore.customer.FDIdentity"%>
<%@ page import="com.freshdirect.fdstore.FDDeliveryManager"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.FDSessionUser"%>
<%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession"%>
<%@ page import="com.freshdirect.customer.ErpCustomerInfoModel"%>
<%@ page import="com.freshdirect.customer.ErpPaymentMethodI"%>
<%@ page import="com.freshdirect.customer.ErpPaymentMethodModel"%>
<%@ page import="com.freshdirect.payment.EnumPaymentMethodType"%>
<%@ page import="com.freshdirect.payment.fraud.RestrictedPaymentMethodModel"%>
<%@ page import="com.freshdirect.customer.EnumDeliverySetting"%>
<%@ page import="com.freshdirect.customer.EnumUnattendedDeliveryFlag"%>
<%@ page import="com.freshdirect.common.address.PhoneNumber"%>
<%@ page import="com.freshdirect.common.customer.EnumServiceType"%>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerManager"%>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.crm.CrmAgentModel"%>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import='com.freshdirect.webapp.util.*'%>
<%@ page import='com.freshdirect.framework.util.*'%>
<%@ page import="com.freshdirect.fdlogistics.model.EnumRestrictedAddressReason"%>
<%@ page import="com.freshdirect.fdlogistics.model.FDReservation" %>
<%@ page import="com.freshdirect.crm.CrmCaseTemplate" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter" %>
<%@ page import="com.freshdirect.framework.core.PrimaryKey"  %>
<%@ page import="com.freshdirect.fdstore.referral.FDReferralManager" %>
<%@ page import="com.freshdirect.webapp.crm.security.CrmSecurityManager" %>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature" %>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter" %>
<%@ page import='java.util.*' %>
<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri="crm" prefix="crm" %>
<%@ taglib uri='freshdirect' prefix='fd' %>
