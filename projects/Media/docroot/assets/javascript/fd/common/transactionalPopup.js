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
        delay: 300
      }
    },
    adjustWidth: {
      value: true
    },
    open: {
      value: function (config) {
        var target = config.element,
            width = $(target).width(),
            popupId=this.popupId,
            related,
            relatedHolder = $('#'+popupId+' .transactional-related-item'),
            mainHolder = $('#'+popupId+' .transactional-main-item'),
            learnMoreLink = $('#'+popupId+' .transactional-popup-learnmore'),
            cmEvSource = "",
            maxImageSize = 0, topPadding = 0,
            pimg = $(target).find('.portrait-item-burst_wrapper')[0],
            imgBottom = pimg ? pimg.getBoundingClientRect().bottom : null;

        if (imgBottom) {
          $(target).parent().find('.portrait-item-burst_wrapper').each(function () {
            var h = this.getBoundingClientRect().height,
                bottom = this.getBoundingClientRect().bottom;

            if (Math.abs(bottom - imgBottom) < 10 && maxImageSize < +h) {
              maxImageSize = +h;
            } 
          });

          topPadding = maxImageSize - pimg.getBoundingClientRect().height;
          
          $(target).find('.portrait-item-productimage_wrapper').css('padding-top', topPadding);
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

          // fill related item content
          related = $('#'+popupId+' [data-component="relateditem"] [data-component="product"]');
          if (related.length) {
            mainHolder.css({
              minHeight: 0
            });
            relatedHolder.removeClass('hidden').css({
              minHeight: 0,
              left: 0,
              visibility: 'hidden'
            });
            $('#'+popupId+' '+this.relatedBodySelector).html(related.html());
            related.remove();

          } else {
            mainHolder.css({
              minHeight: 0
            });
            relatedHolder.addClass('hidden').css({
              minHeight: 0
            });
            $('#'+popupId+' '+this.relatedBodySelector).html('');
          }

          // set learn more url
          learnMoreLink.attr('href', mainHolder.find('[data-productdata-name="productPageUrl"]').first().val());

          this.popup.showWithDelay(target, null, $.proxy(function () {
            if (related.length) {
              // adjust height
              if (mainHolder.height() < relatedHolder.height()+10) {
                mainHolder.css({
                  minHeight: relatedHolder.height()+15
                });
                relatedHolder.css({
                  minHeight: relatedHolder.height()+5
                });
              } else {
                relatedHolder.css({
                  minHeight: mainHolder.height()-5
                });
                mainHolder.css({
                  minHeight: mainHolder.height()+5
                });
              }
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

                if (wwidth > ppos + 2*width + 20) {
                  relatedHolder.css({
                    visibility: 'visible',
                    left: (width+15)+'px'
                  });
                } else {
                  relatedHolder.css({
                    visibility: 'visible',
                    left: (-width-15)+'px'
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

  fd.modules.common.utils.register("common", "transactionalPopup", transactionalPopup, fd);
}(FreshDirect));
