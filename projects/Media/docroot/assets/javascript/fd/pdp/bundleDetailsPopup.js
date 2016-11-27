/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;

  var bundleDetailsPopupMenu = {
    popupSelector: '[data-ajaxpopup-type="bundleDetails"]',
    deactivateAllItems: function(){
      $(bundleDetailsPopupMenu.popupSelector + ' [fd-ajaxcontent]').removeClass('active-item');
    },
    activateItem: function(el){
      $(el).toggleClass('active-item');
    }
  };

  $(document).on('click', bundleDetailsPopupMenu.popupSelector + ' [fd-ajaxcontent]', function(e){
    bundleDetailsPopupMenu.deactivateAllItems();
    bundleDetailsPopupMenu.activateItem(e.target);
  });

  // close when clicking outside the popup which is lands on popup div element (due to fixedpopup css) not the overlay behind
  $(document).on('click', '#ajaxpopup' + bundleDetailsPopupMenu.popupSelector, function(e){
    if(e.target === e.currentTarget){
      e.stopPropagation();
      fd.components.ajaxPopup && fd.components.ajaxPopup.close();
    }
  });

  fd.modules.common.utils.register("pdp", "bundleDetailsPopupMenu", bundleDetailsPopupMenu, fd);
}(FreshDirect));
