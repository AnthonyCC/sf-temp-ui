var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;

  function enableDisableAddToCart() {
    var termsEl = $('[data-component="agree-terms"]');
    var termsChecked = termsEl.is(':checked');
    var container = $('[data-addtocart-functionality]');
    var atcButton = container.find('[data-component="ATCButton"]');

    if(termsChecked || !termsEl.length){
      atcButton.removeAttr('data-atc-disable');
      container.attr('data-addtocart-functionality', 'enabled');
    } else {
      atcButton.attr('data-atc-disable', 'true');
      container.attr('data-addtocart-functionality', 'disabled');
    }
  }

  function selectBundlePopupContentAfterRender(ajaxPopup, target) {
    if(!target){ return; }

    var $target = $(target),
        mealSideProduct = $target.attr('data-meal-side-product'),
        mealSideCategory = $target.attr('data-meal-side-category');

    if(mealSideProduct && mealSideCategory){
      var $ajaxContentEl = $("#" + ajaxPopup.popupId + " a[fd-ajaxcontent][href*='categoryId=" + mealSideCategory + "&productId=" + mealSideProduct + "']");

      fd.pdp.bundleDetailsPopupMenu.deactivateAllItems();
      fd.pdp.bundleDetailsPopupMenu.activateItem($ajaxContentEl[0]);

      if($ajaxContentEl.length){
        fd.components.ajaxContent.update($ajaxContentEl[0], function(){
            ajaxPopup.popup.show($('body'),false);
            ajaxPopup.noscroll();
        });
      }
    }
  }

  function mealDetailsMenuItemAfterAjaxContentRender(target, data) {
    var nutritionPanel = data.productExtraData && data.productExtraData.nutritionPanel;
    if(nutritionPanel && nutritionPanel.type){
      $('<link rel="stylesheet" type="text/css" href="/assets/css/'+ nutritionPanel.type.toLowerCase() + '_nutrition_display.css">').prependTo(target);
    }

    FreshDirect.components.ajaxPopup.noScroll();
  }

  $('[data-component="agree-terms"]').on('change', function(){
    enableDisableAddToCart();
  });

  $(document).ready(function(){ enableDisableAddToCart(); });

  fd.modules.common.utils.register("pdp", "enableDisableAddToCart", enableDisableAddToCart, fd);
  fd.modules.common.utils.register("pdp", "selectBundlePopupContentAfterRender", selectBundlePopupContentAfterRender, fd);
  fd.modules.common.utils.register("pdp", "mealDetailsMenuItemAfterAjaxContentRender", mealDetailsMenuItemAfterAjaxContentRender, fd);
}(FreshDirect));
