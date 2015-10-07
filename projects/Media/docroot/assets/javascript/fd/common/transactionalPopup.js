/*global common*/
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
        delay: 100,
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
          this.currentTarget.attr('data-currentselectvalue', $('#'+this.popupId+' .transactional-main-item select.salesunit').val());
          this.currentTarget.attr('data-currentrelatedselectvalue', $('#'+this.popupId+' .transactional-related-item select.salesunit').val());
          // hiding tooltips on ecoupons
          $('.cDetToolTipClickToApply, .cDetToolTip').hide();
        }
      }
    },
    open: {
      value: function (config) {
        var target = config.element,
            width = $(target).width(),
            popupId=this.popupId,
            related, outerWidth = $(target).outerWidth(),
            relatedHolder = $('#'+popupId+' .transactional-related-item'),
            mainHolder = $('#'+popupId+' .transactional-main-item'),
            learnMoreLink = $('#'+popupId+' .transactional-popup-learnmore'),
            cmEvSource = "",
            maxImageSize = 0,
            pimg = $(target).find('.portrait-item-burst_wrapper')[0],
            $img = $(pimg).find('img.portrait-item-productimage'),
            imgBottom = pimg ? pimg.getBoundingClientRect().bottom : null,
            parentContainer = $(target).closest('[data-transactional-align="true"]');

        this.currentTarget = target;

        if (imgBottom) {

          if(!parentContainer.length){
            parentContainer = $(target).parent();
          }

          parentContainer.find('.portrait-item-burst_wrapper').each(function () {
            var h = this.getBoundingClientRect().height,
                bottom = this.getBoundingClientRect().bottom;

            if (Math.abs(bottom - imgBottom) < 10 && maxImageSize < +h) {
              maxImageSize = +h;
            }
          });

          $(pimg).css({
            marginTop: (maxImageSize-pimg.getBoundingClientRect().height+parseInt($img.css('marginTop'), 10))+'px'
          });

          if ($img[0].getBoundingClientRect().height > 30) {
            $img.css({
              height: $img[0].getBoundingClientRect().height,
              width: $img[0].getBoundingClientRect().width
            });
          }
        }

        // clear popup delay
        this.popup.clearDelay();

        $('#'+popupId).removeClass('hasRelated');

        if(target.length){
          this.bodyValue = '';
          this.refreshBody({},this.bodyTemplate,common.transactionalPopupHeader(config));

          $('#'+popupId+' '+this.bodySelector).html('').append(target.clone());

          $('#'+popupId).attr('data-cmeventsource', null);
          cmEvSource = $(target).closest('[data-cmeventsource]').data('cmeventsource');

          if(cmEvSource && cmEvSource.length > 0){
            $('#'+popupId).attr('data-cmeventsource', cmEvSource);
          }

          if (this.adjustWidth) {
            $('#'+popupId+' '+this.bodySelector+' .portrait-item').width(width);
            $('#'+popupId+' '+this.relatedBodySelector).width(width);
          }

          // set current value
          if (target.attr('data-currentvalue')) {
            mainHolder.find('input.qty').first().val(target.attr('data-currentvalue'));
          }
          if (target.attr('data-currentselectvalue')) {
            mainHolder.find('select.salesunit').first().val(target.attr('data-currentselectvalue'));
          }

          // remove transactional atc state
          $('#'+popupId).find('.atc-info-message').removeClass('atc-info-message');

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
            if (target.attr('data-currentrelatedvalue')) {
              relatedHolder.find('input.qty').first().val(target.attr('data-currentrelatedvalue'));
            }
            if (target.attr('data-currentrelatedselectvalue')) {
              relatedHolder.find('select.salesunit').first().val(target.attr('data-currentrelatedselectvalue'));
            }

          } else {
            relatedHolder.addClass('hidden');
            $('#'+popupId+' '+this.relatedBodySelector).html('');
          }

          // show subtotal
          $('#'+popupId+' [data-component="product"]').trigger('transactionalPopup-open');

          // set learn more url
          learnMoreLink.attr('href', mainHolder.find('[data-productdata-name="productPageUrl"]').first().val());

          if ($('#'+popupId+' [data-component="product"]').hasClass('discontinued')) {
            learnMoreLink.hide();
          } else {
            learnMoreLink.show();
          }

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
                minHeight: mainHolder.height(),
                marginLeft: 0,
                marginRight: 0,
                paddingLeft: 0,
                paddingRight: 0
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
                outerWidth = mainHolder.outerWidth();

                if (wwidth > ppos + 2*outerWidth + 10) {
                  relatedHolder.css({
                    visibility: 'visible',
                    left: 0,
                    paddingLeft: '15px',
                    marginLeft: '-15px'
                  });
                } else {
                  relatedHolder.css({
                    visibility: 'visible',
                    left: -(2*outerWidth) +'px',
                    paddingRight: '15px',
                    marginRight: '-15px'
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

  var transactionalCustomize = function () {
    var popupBox = this.$el[0].getBoundingClientRect(),
        trPopupBox = transactionalPopup.popup && transactionalPopup.popup.$el && transactionalPopup.popup.$el[0].getBoundingClientRect(),
        relatedBox = transactionalPopup.popup && transactionalPopup.popup.$el && transactionalPopup.popup.$el.find('.transactional-related-item')[0].getBoundingClientRect(),
        scLeft = $(window).scrollLeft(),
        scTop = $(window).scrollTop(),
        positions = {}, wwidth;

    if (relatedBox && relatedBox.width !== 0) {
      trPopupBox = {
        top: trPopupBox.top,
        bottom: trPopupBox.bottom,
        height: trPopupBox.height,
        left: trPopupBox.left < relatedBox.left ? trPopupBox.left : relatedBox.left,
        right: trPopupBox.right > relatedBox.right ? trPopupBox.right : relatedBox.right,
        width: trPopupBox.right - trPopupBox.left
      };
    }

    if (trPopupBox && trPopupBox.width !== 0) {
      positions = {
        left: trPopupBox.left + scLeft,
        top: trPopupBox.top + scTop,
        minHeight: trPopupBox.height
      };

      wwidth = $(".container").size() ? $(".container").first()[0].getBoundingClientRect().right : $(window).width();

      if (trPopupBox.left + popupBox.width > wwidth) {
        positions.left = trPopupBox.right - popupBox.width + scLeft;
      }

      if (positions.left < 0) {
        positions.left = 0;
      }

      if (popupBox.width === 0) {
        positions.visibility = "hidden";
        positions.top = 0;
        positions.left = 0;
      } else {
        positions.visibility = "visible";
      }

      this.$el.css(positions);

    } else {
      this.$el.css("min-height", 0);
      this.reposition(true);
    }

  };

  $(".product-name-no-brand").ellipsis({ lines: 4 });
  
   $(document).on('mouseover','.transactional [data-transactional-trigger] *',function(event){

    // block popup open if we force it in browseMain
    if($('.browseContent').hasClass('no-transactional')){
      event.stopPropagation();
      return false;
    }

    var element = $(event.currentTarget).closest('[data-component="product"]');
    if ((!element.hasClass('unavailable') || element.hasClass('useReplacement')) && element.closest('.stepping[data-component="carousel"]').size() === 0) {
      transactionalPopup.open({
        element: element,
        productId:element.data('productId'),
        catId:element.data('catId')
      });
    }

   });
  $(document).on('mouseout','.transactional [data-transactional-trigger]',function(){
    // don't show popup
    transactionalPopup.popup.clearDelay();
  });

  $(document).on('click', '#'+transactionalPopup.popupId+' button.close', transactionalPopup.close.bind(transactionalPopup));

  $(document).on('click', '#'+transactionalPopup.popupId+' [data-product-url]', function (e) {
    var product = $(e.currentTarget).closest('[data-component="product"]');

    if (!product.hasClass('discontinued')) {
      window.location.href = $(e.currentTarget).data('product-url');
    }
  });


  fd.modules.common.utils.register("popups.alignment", "transactionalCustomize", transactionalCustomize, fd);
  fd.modules.common.utils.register("common", "transactionalPopup", transactionalPopup, fd);
}(FreshDirect));
