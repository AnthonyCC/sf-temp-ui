/*global jQuery,common*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var POPUPWIDGET = fd.modules.common.popupWidget;
  var DISPATCHER = fd.common.dispatcher;

  var customizePopup = Object.create(POPUPWIDGET,{
    signal:{
      value:'productConfig'
    },
    serialize:{
      value:function(element){
        return {};
      }
    },
    hasClose: {
      value: true
    },
    headerContent: {
      value: ''
    },
    customClass: {
      value: ''
    },
    template:{
      value:common.customizePopup
    },
    bodySelector:{
      value:'.customizePopup-body'
    },
    bodyTemplate: {
      value: common.productDisplay
    },
    $trigger: {
      value: null
    },
    popupId: {
      value: 'customizePopup'
    },
    popupConfig: {
      value: {
        openonclick: true,
        halign:'right'
      }
    },
    dataConfig:{
      value:{
        atcId:false,
        hasApply:false,
        lineId:false,
        listId:false,
        pageType:false,
        ATCApply:false,
        cmEventSource:''
      },
      writable:true
    },
    skus:{
      writable:true,
      value:{}
    },
    skuControlTemplate:{
      value:common.skuControl
    },
    getSelectedSku:{
      value:function(){
        return $(this.bodySelector + ' input[name="skuCode"]:checked').val();
      }
    },
    refreshSkuControls:{
      value:function(){
        var sku = this.skus[this.getSelectedSku()];
        $(this.bodySelector + ' .productDisplay-skuconfig').html(this.skuControlTemplate({sku:sku}));
      }
    },
    callback:{
      value:function(value){
        this.skus=value.skus.reduce(function(prev,current){
          prev[current.skuCode]=current;
          prev[current.skuCode].soldBySalesUnit=value.soldBySalesUnit;
          prev[current.skuCode].quantityText=value.quantityText;
          return prev;
        },{});
        if(!value.atcItemId && this.dataConfig.atcId) {value.atcItemId=this.dataConfig.atcId;}
        value.hasApply = this.dataConfig.hasApply;
        value.originalLineId = this.dataConfig.lineId;
        value.listId = this.dataConfig.listId;
        value.ATCApply = this.dataConfig.ATCApply;
        value.cmEventSource = this.dataConfig.cmEventSource;
        value.cartData = this.dataConfig.cartData;
        value.pageType = this.dataConfig.pageType;
        this.refreshBody(value);
        this.refreshSkuControls();
      }
    },
    open: {
      value: function (config) {
        var item = config.item,
            cartData = config.cartData,
            request = {};

        this.popup.show($(config.element));
        this.popup.clicked = true;
        
        request.productId = item.productId;
        request.configuration = item.configuration;
        request.quantity = parseFloat(item.quantity);
        request.salesUnit = item.salesUnit;
        if(item.skuCode) { request.skuCode = item.skuCode; }
        if(item.categoryId) { request.categoryId = item.categoryId; }
        this.dataConfig={
            atcId:item.atcItemId || false,
            hasApply:config.hasApply,
            lineId:item.lineId,
            listId:item.listId,
            cartData:cartData,
            pageType:item.pageType,
            cmEventSource:config.cmEventSource || '',
            ATCApply:config.hasApply && fd.quickshop.itemType === 'pastOrders'
        };

        fd.common.dispatcher.signal('server',{
      url:'/api/productconfig',
      data:{data:JSON.stringify(request)},
      method:'GET'      
    });
        
        
      }
    }
  });

  customizePopup.render();
  customizePopup.listen();

  $(document).on('click', '[data-component="customizeButton"]', function(event){
    var element = event.currentTarget;
    
    /* go to product instead of showing customize */
    if ($(element).data('bypasscustomizepopup')) {
    	var $par = $(element).closest('[data-component="product-controls"]');
    	if ($par.length === 0) { $par = $(element).closest('[data-component="product"]').find('[data-component="product-controls"]'); }
    	if ($par) {
	    	var bypassUri = $par.find('[data-productdata-name="productPageUrl"]:first').val();
	    	if (bypassUri) {
	    		document.location = bypassUri; 
	    		return;
	    	}
    	}
    }
    
    var item = fd.modules.common.productSerialize(element).pop();
    var cartData = fd.modules.common.getCartData(element);
    var cmEventSourceElement = $(element).closest('[data-cmeventsource]');
    
    customizePopup.open({
      element:element,
      item:item,
      cartData:cartData,
      hasApply:$(element).data('hasapply') || false,
      cmEventSource:cmEventSourceElement.data('cmeventsource')
    });
  });


  
  $(document).on('change','#' + customizePopup.popupId + ' input[name="skuCode"]',customizePopup.refreshSkuControls.bind(customizePopup));

  $(document).on('click', '#' + customizePopup.popupId + ' [data-popup-control="close"]', customizePopup.close.bind(customizePopup));
  $(document).on('addToCart', function(e){
    customizePopup.close();
  });

  
  
  fd.modules.common.utils.register("components", "customizePopup", customizePopup, fd);
}(FreshDirect));
