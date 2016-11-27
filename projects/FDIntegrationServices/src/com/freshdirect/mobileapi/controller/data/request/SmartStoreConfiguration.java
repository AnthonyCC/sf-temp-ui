package com.freshdirect.mobileapi.controller.data.request;

public class SmartStoreConfiguration {

    /*
    <input type="hidden" name="variant" value="<%= recommendations.getVariant().getId() %>">
    <input type="hidden" name="siteFeature" value="<%= recommendations.getVariant().getSiteFeature().getName() %>">
    <input type="hidden" name="rec_product_ids" value="<%= StringEscapeUtils.escapeHtml(recommendations.serializeContentNodes()) %>">
    <input type="hidden" name="rec_current_node" value="<%= recommendations.getSessionInput() != null && recommendations.getSessionInput().getCurrentNode() != null ? 
                    recommendations.getSessionInput().getCurrentNode().getContentKey().getId() : "(null)" %>">
    <input type="hidden" name="rec_ymal_source" value="<%= recommendations.getSessionInput() != null && recommendations.getSessionInput().getCurrentNode() != null ? 
                    recommendations.getSessionInput().getCurrentNode().getContentKey().getId() : "(null)" %>">
    <input type="hidden" name="rec_refreshable" value="<%=  Boolean.toString(recommendations.isRefreshable()) %>">
    <input type="hidden" name="rec_smart_savings" value="<%=  Boolean.toString(recommendations.isSmartSavings()) %>">
    <input type="hidden" name="trk" value="<%= recommendations.getVariant().getSiteFeature().getName().toLowerCase() %>">
     */
    private String parameterBundle;

    public String getParameterBundle() {
        return parameterBundle;
    }

    public void setParameterBundle(String parameterBundle) {
        this.parameterBundle = parameterBundle;
    }

}
