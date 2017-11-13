/*global jQuery,pdp*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var POPUPWIDGET = fd.modules.common.popupWidget;

  var evenBetterPopup = Object.create(POPUPWIDGET,{
    customClass: {
      value: ''
    },
    bodyValue:{
      value:'',
      writable:true
    },
    template:{
      value:pdp.evenBetterPopup
    },
    bodySelector:{
      value:'.evenbetter-popup-body'
    },
    bodyTemplate: {
      value: function(){
        return evenBetterPopup.bodyValue;
      }
    },
    $trigger: {
      value: null
    },
    popupId: {
      value: 'evenBetterPopup'
    },
    popupConfig: {
      value: {
      valign: 'top',
      halign: 'left',
      placeholder: true,
      stayOnClick: true,
      zIndex: 500,
        overlay:true,
        delay: 300
      }
    },
    open: {
      value: function (config) {
        var target = config.element;

        if(target.length){
          this.bodyValue = target[0].innerHTML;
          this.refreshBody({},this.bodyTemplate,pdp.evenBetterPopupHeader(config));
          this.popup.show(target);

          	/* add events on things only in popup */
			$('#'+this.popupId+' .so-test-added-toggler').on('click', function(e) {
				e.stopPropagation();
				$(this).closest('.so-container').find('.so-results-content').toggleClass('so-close');

				function sOResultsClose() {
					$('.so-results-content').addClass('so-close');
				}
				
				window.setTimeout(sOResultsClose, 3000);
		        return false;
			});
			$('#'+this.popupId+' .cssbutton[data-component="showSOButton"]').on('click', function(e) {
				if(!FreshDirect.user.recognized && !FreshDirect.user.guest){
					e.stopPropagation();
					$(this).closest('.pdp-evenbetter-soPreShow').toggleClass('pdp-evenbetter-soPreShow pdp-evenbetter-soShow');
					return false;
				}
			});
			$('#'+this.popupId+' button[data-component="addToSOButton"]').on('click', function() {
				if(!FreshDirect.user.recognized && !FreshDirect.user.guest){
					addToSoEvenBetter($jq(this));
					return false;
				}
			});
          
          // make ID-s unique
          $('#'+this.popupId+' '+this.bodySelector+' [id]').each(function (i, el) {
            el.id = 'trnp_'+el.id;
          });
          $('#'+this.popupId+' '+this.bodySelector+' [for]').each(function (i, el) {
            $(el).attr('for', 'trnp_'+$(el).attr('for'));
          });
          $('#'+this.popupId+' '+this.bodySelector+' [fdform]').each(function (i, el) {
            $(el).attr('fdform', 'trnp_'+$(el).attr('fdform'));
          });

          /* hooklogic click event */
          $('#'+this.popupId + ' [data-hooklogic-beacon-click]').closest('#'+this.popupId).find('a,button,.portrait-item-productimage_wrapper').each(function(i,e) {
        	  if (!$(e).data('hooklogic-beacon-click')) {
                	/* exclusion elems */
                	if (
                		$(this).is('[data-component-extra="showSOButton"], .quantity_minus, .quantity_plus')
                	) { return;
                	} else {
                		$(e).data('hooklogic-beacon-click', 'true');
                		$(e).on('click', function(event) {
                			var $parent = $(this).closest('.pdp-evenbetter-popupContainer');
                			var id = $parent.data('product-id') + '_hlClick';
                			if ($parent.find('img#'+id).length !== 0) {
                				return; //stop multiple firings
                			}
                        	var url = $parent.data('hooklogic-beacon-click');
                        	$parent.append('<img class="hl-beacon-click" id="'+id+'" src="'+url+'&rand='+new Date().getTime()+'" style="display: none;" />');
                		});
                	}
        	  }
          });
        }
      }
    }
  });

  evenBetterPopup.render();

  $(document).on('mouseover','[data-evenbetteritem-trigger]',function(event){
    var element = $(event.currentTarget).closest('[data-component="evenBetterItem"]');
    evenBetterPopup.open({
      element: element,
      productId:element.data('productId'),
      catId:element.data('catId'),
      grpId: element.data('grpId')||null,
      grpVersion: element.data('grpVersion')||null
    });
  });

  fd.modules.common.utils.register("pdp", "evenBetterPopup", evenBetterPopup, fd);
}(FreshDirect));
