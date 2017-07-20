<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='com.freshdirect.fdstore.customer.FDUserI' %>

<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>

<potato:googleAnalytics />

<script>
  window.FreshDirect = window.FreshDirect || {};
  var fd = window.FreshDirect;
  var dataLayer = window.dataLayer || [];
  
  fd.gtm = fd.gtm || {};
  fd.gtm.key = '<%= FDStoreProperties.getGoogleTagManagerKey() %>';
  fd.gtm.auth = '<%= FDStoreProperties.getGoogleTagManagerAuthToken() %>';
  fd.gtm.preview = '<%= FDStoreProperties.getGoogleTagManagerPreviewId() %>';
  fd.gtm.data = <fd:ToJSON object="${googleAnalyticsPotato}" noHeaders="true"/>;

  dataLayer.push({
    'config-ga-key': '<%= FDStoreProperties.getGoogleAnalyticsKey() %>',
    'config-ga-domain': '<%= FDStoreProperties.getGoogleAnlayticsDomain() %>'
  });
</script>

