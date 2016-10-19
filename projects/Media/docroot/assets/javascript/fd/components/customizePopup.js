/*global common*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var POPUPWIDGET = fd.modules.common.popupWidget;

  var customizePopup = Object.create(POPUPWIDGET,{
    signal:{
      value:'productConfig'
    },
    serialize:{
      value:function(){
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
      value: '',
      writable:true
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
    trigger: {
      value: '[data-component="customizeButton"]'
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
    decorate: {
      value: function () {
        $(this.trigger).attr('aria-haspopup', 'true');
      }
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
        if(!value.atcItemId && this.dataConfig.atcId) {
          value.atcItemId=this.dataConfig.atcId;
        }
        value.hasApply = this.dataConfig.hasApply;
        value.originalLineId = this.dataConfig.lineId;
        value.listId = this.dataConfig.listId;
        value.ATCApply = this.dataConfig.ATCApply;
        value.cmEventSource = this.dataConfig.cmEventSource;
        value.cartData = this.dataConfig.cartData;
        value.pageType = this.dataConfig.pageType;
        value.variantId = this.dataConfig.variantId;
        this.refreshBody(value);
        this.refreshSkuControls();

		$jq('button[data-component="addToSOButton"]').on('click', function() {
			addToSoCustomize($(this));
			return false;
		});
		if($jq('button[data-component="addToSOButton"]').length > 0 && $jq("#customizePopup").hasClass("shown") && $jq("#customizePopup").hasClass("soShow")){
			$jq(document).trigger("soCustomizePopup");
		}
        $('#'+this.popupId+' .so-test-added-toggler').on('click', function(e) {
			e.stopPropagation();

			function sOResultsClose() {
				$('.so-results-content').addClass('so-close');
			}
			
			$jq(this).closest('.so-container').find('.so-results-content').toggleClass('so-close');
			window.setTimeout(sOResultsClose, 3000);
			return false;
		});
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
            variantId:item.variantId,
            ATCApply:config.hasApply && fd.quickshop && fd.quickshop.itemType === 'pastOrders'
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

  $(document).on('click', customizePopup.trigger, function(event){
    var element = event.currentTarget;

    /* go to product instead of showing customize */
    if ($(element).data('bypasscustomizepopup')) {
      if($('.mm-page').length){
    	  $(element).addClass("ATCinProgress");
      }
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

	$('#' + customizePopup.popupId).removeClass('soShow');
    if ($(element).data('soshow')) {
    	$('#' + customizePopup.popupId).addClass('soShow');
    }

    if($(element).data('soshow')){
    	if(!FreshDirect.user.recognized && !FreshDirect.user.guest){
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
    	}
    } else {
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
    }
  });


  $(document).on('change','#' + customizePopup.popupId + ' input[name="skuCode"]',customizePopup.refreshSkuControls.bind(customizePopup));

  $(document).on('click', '#' + customizePopup.popupId + ' [data-popup-control="close"]', customizePopup.close.bind(customizePopup));
  $(document).on('addToCart', function(){
    customizePopup.close();
  });

  fd.modules.common.utils.register("components", "customizePopup", customizePopup, fd);
}(FreshDirect));
