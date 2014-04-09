/*global jQuery,common*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var POPUPWIDGET = fd.modules.common.popupWidget;

  var transactionalPopup = Object.create(POPUPWIDGET,{
    customClass: {
      value: ''
    },
    bodyValue:{
      value:'',
      writable:true
    },
    template:{
      value:common.transactionalPopup
    },
    bodySelector:{
      value:'.transactional-popup-body'
    },
    relatedBodySelector:{
      value:'.transactional-related-body'
    },
    bodyTemplate: {
      value: function(){
        return transactionalPopup.bodyValue;
      }
    },
    $trigger: {
      value: null
    },
    popupId: {
      value: 'transactionalPopup'
    },
    popupConfig: {
      value: {
        valign: 'top',
        halign: 'left',
        placeholder: true,
        openonclick: false,
        stayOnClick: false,
        overlay:true,
        zIndex: 500,
        delay: 10,
        hidecallback: function () { transactionalPopup.closeCB(); }
      }
    },
    adjustWidth: {
      value: true
    },
    closeCB: {
      value: function () {
        if (this.currentTarget) {
          this.currentTarget.attr('data-currentvalue', $('#'+this.popupId+' .transactional-main-item input.qty').val());
          this.currentTarget.attr('data-currentrelatedvalue', $('#'+this.popupId+' .transactional-related-item input.qty').val());
        }
      }
    },
    open: {
      value: function (config) {
        var target = config.element,
            width = $(target).width(),
            outerWidth = $(target).outerWidth(),
            popupId=this.popupId,
            related,
            relatedHolder = $('#'+popupId+' .transactional-related-item'),
            mainHolder = $('#'+popupId+' .transactional-main-item'),
            learnMoreLink = $('#'+popupId+' .transactional-popup-learnmore'),
            cmEvSource = "",
            maxImageSize = 0,
            pimg = $(target).find('.portrait-item-burst_wrapper')[0],
            imgBottom = pimg ? pimg.getBoundingClientRect().bottom : null;

        this.currentTarget = target;

        if (imgBottom) {
          $(target).parent().find('.portrait-item-burst_wrapper').each(function () {
            var h = this.getBoundingClientRect().height,
                bottom = this.getBoundingClientRect().bottom;

            if (Math.abs(bottom - imgBottom) < 10 && maxImageSize < +h) {
              maxImageSize = +h;
            } 
          });

        }

        // clear popup delay
        this.popup.clearDelay();

        $('#'+popupId).removeClass('hasRelated');

        if(target.length){
          this.bodyValue = target[0].innerHTML;
          this.refreshBody({},this.bodyTemplate,common.transactionalPopupHeader(config));

          $('#'+popupId).attr('data-cmeventsource', null);
          cmEvSource = $(target).closest('[data-cmeventsource]').data('cmeventsource');

          if(cmEvSource && cmEvSource.length > 0){
            $('#'+popupId).attr('data-cmeventsource', cmEvSource);
          }

          if (this.adjustWidth) {
            $('#'+popupId+' '+this.bodySelector).width(width);
            $('#'+popupId+' '+this.relatedBodySelector).width(width);
          }

          // set current value
          if (target.data('currentvalue')) {
            mainHolder.find('input.qty').first().val(target.data('currentvalue'));
          }

          // fill related item content
          related = $('#'+popupId+' [data-component="relateditem"] [data-component="product"]');
          if (related.length) {
            relatedHolder.css({
              minHeight: 0,
              left: (-outerWidth)+'px',
              visibility: 'hidden'
            }).removeClass('hidden');
            $('#'+popupId+' '+this.relatedBodySelector).html(related.html());
            related.remove();

            // set current value
            if (target.data('currentrelatedvalue')) {
              relatedHolder.find('input.qty').first().val(target.data('currentrelatedvalue'));
            }

          } else {
            relatedHolder.addClass('hidden');
            $('#'+popupId+' '+this.relatedBodySelector).html('');
          }

          // show subtotal
          $('#'+popupId+' input.qty').change();

          // set learn more url
          learnMoreLink.attr('href', mainHolder.find('[data-productdata-name="productPageUrl"]').first().val());

          this.popup.showWithDelay(target, null, $.proxy(function () {
            var mctop, rctop,
                relatedControls = relatedHolder.find('.portrait-item-controls-content'),
                mainControls = mainHolder.find('.portrait-item-controls-content');

            // adjust image wrapper size
            mainHolder.find('.portrait-item-productimage_wrapper').css('line-height', maxImageSize+'px');

            if (related.length) {

              // adjust image wrapper size
              relatedHolder.find('.portrait-item-productimage_wrapper').css('line-height', maxImageSize+'px');
              relatedHolder.find('.portrait-item-productimage_wrapper img.portrait-item-productimage').css('max-height', (maxImageSize-4)+'px');

              // adjust controls
              mctop = mainControls.find('.portrait-item-addtocart').offset().top;
              rctop = relatedControls.find('.portrait-item-addtocart').offset().top;
              if (mctop < rctop) {
                mainControls.css('padding-top', rctop - mctop);
              } else {
                relatedControls.css('padding-top', mctop - rctop);
              }

              // adjust related popup size
              relatedHolder.css({
                minWidth: mainHolder.width(),
                minHeight: mainHolder.height()
              });

              $('#'+popupId).addClass('hasRelated');

              // show related later
              if (this.showRelated) {
                clearTimeout(this.showRelated);
                this.showRelated = null;
              }

              this.showRelated = setTimeout($.proxy(function () {
                var wwidth = $(".container").size() ? $(".container").first()[0].getBoundingClientRect().right : $(window).width(),
                    ppos = $('#'+popupId).position().left;

                this.showRelated = null;

                if (wwidth > ppos + 2*outerWidth + 10) {
                  relatedHolder.css({
                    visibility: 'visible',
                    left: (-5)+'px'
                  });
                } else {
                  relatedHolder.css({
                    visibility: 'visible',
                    left: (-(2*outerWidth) + 5)+'px'
                  });
                }

                if($("body").hasClass("ie8")){
                  // fix for: icon font does not appear on :before in IE8, only on hover
                  $(relatedHolder).find('[data-component="addToListButton"]').trigger("focus");
                }

              }, this), 500);
            }

          if($("body").hasClass("ie8")){
            // fix for: icon font does not appear on :before in IE8, only on hover
            $('#'+popupId + ' ' + "[data-component='addToListButton']").trigger('focus');
          }

          }, this));
        }
      }
    }
  });

  transactionalPopup.render();

  $(document).on('mouseover','.transactional [data-transactional-trigger]',function(event){
    var element = $(event.currentTarget).closest('[data-component="product"]');
    if (!element.hasClass('unavailable')) {
      transactionalPopup.open({
        element: element,
        productId:element.data('productId'),
        catId:element.data('catId')
      });
    }
  });
  $(document).on('mouseout','.transactional [data-transactional-trigger]',function(event){
    // don't show popup
    transactionalPopup.popup.clearDelay();
  });
  $(document).on('click', '#'+transactionalPopup.popupId+' button.close', transactionalPopup.close.bind(transactionalPopup));

  $(document).on('click', '#'+transactionalPopup.popupId+' [data-product-url]', function (e) {
    window.location.href = $(e.currentTarget).data('product-url');
  });

  fd.modules.common.utils.register("common", "transactionalPopup", transactionalPopup, fd);
}(FreshDirect));
