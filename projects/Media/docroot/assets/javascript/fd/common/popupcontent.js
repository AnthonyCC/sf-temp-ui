/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  
  var $=fd.libs.$;
  
  /**
   * Popup content
   *
   * @param {jQuery} $el The popup element (cart) to show
   * @param {jQuery} $trigger The content will show up on this element's mouseover events
   * @param {Object} config Configuration object
   */
  var PopupContent = function ($el, $trigger, config) {
    this.$el = $el;
    this.config = $.extend({}, PopupContent.CONFIG, config);

    this.$alignTo = this.config.aligntoselector && $(this.config.aligntoselector) ||
                    this.config.alignTo || $trigger;

    this.$overlay = null;
    this.$ghost = null;
    this.$placeholder = null;
    this.delay = null;
    this.hidedelay = null;
    this.clicked = false;
    this.shown = false;
    this.disabled = this.config.disabled || false;
    this.config.placeholder = !!(this.config.placeholder && $trigger);
    if(config.align || config.align === false){
      this.config.align=config.align;
    }

    this.hideProxy = $.proxy(this.hide, this);

    // wait for domready event
    $($.proxy(function(){

      // check overlay and ghost
      if (this.config.overlay) {
        this.$overlay = $('<div class="popupcontentoverlay"></div>').appendTo(document.body);

        if (this.config.overlayExtraClass) {
          this.$overlay.addClass(this.config.overlayExtraClass);
        }

        if (this.config.zIndex) {
          this.$overlay.css('z-index', this.config.zIndex);
        }

        this.$overlay.on("mouseover", $.proxy(this.hideDelayed, this));
        this.$overlay.on("mouseout", $.proxy(this.clearHideDelay, this));
        this.$overlay.on("click", this.hideProxy);
      }
      if (this.config.placeholder || this.config.lateplaceholder) {
        this.$ghost = $('<div class="popupcontentghost"></div>').appendTo(document.body);
        this.$placeholder = $('<div class="popupcontentplaceholder"></div>').appendTo(document.body);

        if (this.config.zIndex) {
          this.$ghost.css('z-index', this.config.zIndex+1);
        }

      }

      // move $el to the end of body
      this.$el.appendTo(document.body);

      if (this.config.zIndex) {
        this.$el.css('z-index', this.config.zIndex+2);
      }

      if ((this.config.placeholder || this.config.lateplaceholder) && !this.config.stayonghostclick) {
        this.$ghost.on("click", this.hideProxy);
      }

      this.bindTrigger($trigger);

      if (this.config.stayOnClick) {
        this.$el.on("mousedown", $.proxy(function () {
          if (this.shown && !this.clicked) {
            this.clicked = true;
            this.$el.addClass('clicked');
          }
        }, this));
      }

      if (this.config.closeHandle) {
        $(this.config.closeHandle).on('click', this.hideProxy);
      }


    $(window).on('resize',$.proxy(function(e){
      if(this.shown){
        this.hide();
        this.show();
      }
    },this));

  },this));

  };

  PopupContent.prototype.bindTrigger = function ($trigger) {
    if ($trigger) {
      this.$trigger = $trigger;
      this.alignTo = this.alignTo || $trigger;

      if (this.config.openonclick) {
        this.$trigger.on("click", $.proxy(this.show, this));
      } else {
        this.$trigger.on("mouseover", $.proxy(this.showDelayed, this));
        this.$trigger.on("mouseout", $.proxy(this.clearDelay, this));
        // kill click event on touch devices
        this.$trigger.on("touchstart", $.proxy(function (e) {
          if (!this.shown) {
            e.preventDefault();
            console.log('click prevented?');
            this.showDelayed();
          }
        }, this));
      }
      if (!this.config.overlay && !this.config.openonclick) {
        this.$trigger.on("mouseout", $.proxy(this.hideDelayed, this));
        this.$el.on("mouseover", $.proxy(this.clearHideDelay, this));
        this.$el.on("mouseout", $.proxy(this.hideDelayed, this));
      }
    }
  };

  PopupContent.prototype.clearDelay = function (e) {
    clearTimeout(this.delay);
    this.delay = null;
  };

  PopupContent.prototype.clearHideDelay = function (e) {
    clearTimeout(this.hidedelay);
    this.hidedelay = null;
  };

  PopupContent.prototype.showDelayed = function (e) {
    if (this.delay === null) {
      this.delay = setTimeout($.proxy(this.show, this), this.config.delay);
    }
  };

  PopupContent.prototype.hideDelayed = function (e) {
    if (this.hidedelay === null && !this.clicked && !this.config.noCloseOnOverlay) {
      this.hidedelay = setTimeout($.proxy(this.hide, this), this.config.delay);
    }
  };

  PopupContent.prototype.showWithDelay = function ($trigger, align, callback) {
    if (this.delay === null) {
      this.delay = setTimeout($.proxy(function () {
        this.show($trigger, align);
        if (callback) {
          callback();
        }
      }, this), this.config.delay);
    }
  };

  PopupContent.prototype.show = function ($trigger, align) {
    var screenOffset, boundingRect;

    if (this.delay) { this.clearDelay(); }

    if ($trigger) {
      this.$trigger = $trigger;
      this.$alignTo = $trigger;
    }
    if (align || align === false) {
      this.config.align=align;
    }
    if (this.$trigger && !this.shown && !this.disabled) {

      // close autocomplete
      try {
        $('[data-component="autocomplete"]').autocomplete("close");
      } catch (e) {}

      this.shown = true;
      this.$trigger.addClass("hover");
      this.reposition();
      this.$el.css({display: "block"});
      this.$el.addClass('shown');
      if (this.config.placeholder || this.config.lateplaceholder) {
        this.$ghost.css({display: "block"});
      }
      if (this.config.overlay) {
        this.$overlay.css({display: "block"});
      }
      var el = this.$el;
      setTimeout(function(){
        boundingRect = el[0].getBoundingClientRect();
        
        screenOffset =  $(window).height() - (boundingRect.bottom);
        if(screenOffset<0) {
          try {
            el.smoothScroll();
          } catch (e) {}
        }
      },500);
      
      
    }
  };

  PopupContent.prototype.hide = function () {
    if (this.$trigger) {
      this.$trigger.removeClass("hover");
    }
    this.$el.css({display: "none"});
    this.$el.removeClass('shown');
    this.clicked = false;
    this.shown = false;
    this.placeholderActive = false;
    this.$el.removeClass('clicked');
    if (this.config.placeholder || this.config.lateplaceholder) {
      this.$ghost.css({display: "none"});
      if (this.$alignTo) {
        this.$alignTo.insertBefore(this.$placeholder);
      }
      if (this.$placeholder) {
        this.$placeholder.detach();
      }
    }
    if (this.config.overlay) {
      this.$overlay.css({display: "none"});
    }
    if (this.config.hidecallback) {
      this.config.hidecallback();
    }
    this.clearHideDelay();
  };

  PopupContent.prototype.reposition = function (ignoreCustomFunction) {
    if (!this.$alignTo || !this.$el || !this.shown) {
      return;
    }

    var offset = this.$alignTo.offset(),
        width = this.$alignTo.outerWidth(),
        height = this.$alignTo.outerHeight(),
        fwidth = this.$alignTo.outerWidth(true),
        fheight = this.$alignTo.outerHeight(true),
        contentWidth = this.$el.outerWidth(),
        contentHeight = this.$el.outerHeight(),
        left,
        screenOffset;

    if (this.$alignTo.attr('data-alignpopupfunction') && !ignoreCustomFunction) {
      // call custom alignment function if exists
      // align function should be registered under 
      // FreshDirect.popups.alignment namespace
      var alignFunction = fd.popups && fd.popups.alignment && 
                          fd.popups.alignment[this.$alignTo.attr('data-alignpopupfunction')];

      if (alignFunction) {
        alignFunction.call(this);

        return;
      }
    }

    var align = this.$alignTo.attr('data-alignpopup') || this.config.align;

    if (this.config.placeholder || this.config.lateplaceholder) {
      this.$ghost.css({
        top: offset.top + 'px',
        left: offset.left + 'px',
        width: fwidth + 'px',
        height: fheight + 'px'
      });
      this.$placeholder.insertBefore(this.$alignTo);
      this.$placeholder.css({
        width: fwidth + 'px',
        height: fheight + 'px',
        float: this.$alignTo.css('float')
      });
      this.$alignTo.appendTo(this.$ghost);
      this.placeholderActive = true;
    }
    
    if(align) {
      var position={};
      // trigger, vertical
      if(align[0]==='t') {
        position.top = offset.top;
      } else if(align[0]==='b') {
        position.top = offset.top + height;
      } else if(align[0]==='c') {
        position.top = offset.top + height/2;
      }
      
      // trigger, horizontal
      if(align[1]==='l') {
        position.left = offset.left;
      } else if(align[1]==='r') {
        position.left = offset.left + width;
      } else if(align[1]==='c') {
        position.left = offset.left + width/2;
      }
      
      // popup, vertical
      if(align[3]==='c') {
        position.top = position.top - contentHeight/2;
      } else if(align[3]==='b') {
        position.top = position.top - contentHeight;
      }
      
      // popup, horizontal
      if(align[4]==='c') {
        position.left = position.left - contentWidth/2;
      } else if(align[4]==='r') {
        position.left = position.left - contentWidth;
      }

      // popup, vertical viewport check
      if (align[6] === 'c') {
        if (position.left < $(window).scrollLeft()) {
          position.left = $(window).scrollLeft();
        }
        if (position.left + contentWidth > $(window).width() + $(window).scrollLeft()) {
          position.left = $(window).width() + $(window).scrollLeft() - contentWidth;
        }
      }
            
      this.$el.css({
        top:position.top+'px',
        left:position.left+'px',
        right:'auto',
        bottom:'auto'
      });
      
    } else if(align!==false) {
      if (this.config.valign === 'bottom') {
          this.$el.css({top: (offset.top + height) + 'px', bottom: 'auto'});
      } else {
          this.$el.css({top: offset.top + 'px', bottom: 'auto'});
      }

      if (this.config.halign === 'right') {
        left = offset.left + width - contentWidth;

        if (left < 0) {
          left = 10;
        }

        this.$el.css({left: left + 'px', right: 'auto'});
      } else {
        this.$el.css({left: offset.left + 'px', right: 'auto'});
      }
    }
    


  };

  PopupContent.CONFIG = {
    valign: 'bottom',
    halign: 'left',
    delay: 300,
    placeholder: true,
    overlay: true,
    stayOnClick: true
    // closehandle: '.close'
    // alignTo
    // aligntoselector
    // openonclick
  };

  // register in fd namespace
  fd.modules.common.utils.register("modules.common", "PopupContent", PopupContent, fd);
}(FreshDirect));
