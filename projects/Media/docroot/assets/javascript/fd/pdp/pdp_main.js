/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  var $ = fd.libs.$;
  
  fd.components.Subtotal.update($('[data-component="product"][data-cmeventsource="pdp_main"]'));
  
  var productId=$('#BVRRContainer').data('productid');
  
  if(window.$BV){
    window.$BV.ui('rr', 'show_reviews', { productId: productId });
  } else {
    //console.log("missing $BV");
  }
  
    window.pop=function(url,width,height){
      fd.components.ifrPopup.open({
        width:width,
        height:height,
        url:url
      });
    };
  
  fd.modules.common.utils.register("components", "pdp", {}, fd);
}(FreshDirect));
