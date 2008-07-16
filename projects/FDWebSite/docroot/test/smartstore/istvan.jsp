
<%@page import="com.freshdirect.webapp.taglib.test.SmartStoreSession" %>
<%@page import="com.freshdirect.smartstore.fdstore.FDStoreRecommender" %>
<%@page import="com.freshdirect.smartstore.fdstore.Recommendations" %>
<%@page import="com.freshdirect.smartstore.Trigger" %>
<%@page import="com.freshdirect.fdstore.util.EnumSiteFeature" %>
<%@page import="com.freshdirect.fdstore.customer.FDUserI" %>
<%@page import="com.freshdirect.fdstore.customer.FDCartModel" %>
<%@page import="com.freshdirect.fdstore.customer.FDCartLineModel" %>

<%@page import="com.freshdirect.cms.ContentKey" %>
<%@page import="com.freshdirect.cms.fdstore.FDContentTypes" %>

<%@page import="com.freshdirect.fdstore.content.ProductRef" %>
<%@page import="com.freshdirect.fdstore.content.SkuModel" %>

<%@page import="com.freshdirect.fdstore.FDProductInfo" %>
<%@page import="com.freshdirect.fdstore.FDCachedFactory" %>
<%@page import="com.freshdirect.fdstore.FDProduct" %>
<%@page import="com.freshdirect.fdstore.FDSku" %>
<%@page import="com.freshdirect.fdstore.FDConfiguration" %>

<%@page import="java.util.List" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.Map" %>
<%@page import="java.util.HashMap" %>
Hello Gabor!


<%



FDStoreRecommender recommender = FDStoreRecommender.getInstance();

String istvanErpId = "516328642";
HttpSession mockSession = new SmartStoreSession(istvanErpId);

String exampleSkuCode = "HMR0067915"; // sku code

FDProductInfo productInfo = FDCachedFactory.getProductInfo(exampleSkuCode);
FDProduct fdProd = FDCachedFactory.getProduct( exampleSkuCode, productInfo.getVersion() );
ContentKey ckey = new ContentKey(FDContentTypes.SKU,exampleSkuCode);
SkuModel skuModel = new SkuModel(ckey);

Map options = new HashMap();
options.put("C_HMR_GRAD_DES1","300720111"); // config
options.put("C_HMR_GRAD_DIP1","300720017");
options.put("C_HMR_GRAD_ENT1","300720045");
options.put("C_HMR_GRAD_PLT1","300720018");

FDConfiguration conf = new FDConfiguration(3,"EA",options);
FDCartLineModel line = new FDCartLineModel(new FDSku(fdProd),skuModel.getProductModel().getProductRef(),conf,null);



FDUserI looser = (FDUserI)mockSession.getAttribute("fd.user");
FDCartModel model = looser.getShoppingCart();
model.clearOrderLines();

List lines = new ArrayList();
lines.add(line);
model.addOrderLines(lines);

Recommendations recommendations = recommender.getRecommendations(new Trigger(EnumSiteFeature.DYF,10), mockSession);






%>





