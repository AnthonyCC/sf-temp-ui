/*global jQuery,common*/
var FreshDirect = FreshDirect || {};

(function (fd) {

  var $ = fd.libs.$;
  var WIDGET = fd.modules.common.widget;
  var DISPATCHER = fd.common.dispatcher;

  // TMP: move to popupwidget.js?
  var overlayWidget = Object.create(WIDGET,{
    signal:{
      value: null
    },
    template:{
      value:common.overlay
    },
    bodyTemplate:{
      value:null,
      writable: true
    },
    headerTemplate:{
      value:null,
      writable: true
    },
    overlayId:{
      value:''
    },
    ariaDescribedby:{
      value:''
    },
    ariaLabelledby:{
      value:''
    },
    customClass: {
      value: ''
    },
    closeTrigger : {
      value: '[data-close-overlay]'
    },
    bodySelector:{
      value:'.overlay-content'
    },
    headerSelector:{
      value:'.overlay-header'
    },
    placeholder:{
      value:'body'
    },
    tabIndixes:{
      value:[],
      writable: true
    },
    lastFocused:{
      value: '',
      writable: true
    },
    close: {
      value: function (e) {
        var $overlay = e ? $(e.currentTarget).closest('.overlay') : $(document).find('.overlay');

        if (e && e.stopPropagation) {
          e.stopPropagation();
        }
        $overlay.css({display: "none"});
        $('body').removeClass('overlay-opened');
        setTimeout(function () {
          this.reset();
        }.bind(this), 10);
        if ($overlay.attr('data-close-cb')) {
          var closeCB = fd.utils.discover($overlay.attr('data-close-cb'));

          if (closeCB) {
            try {
              closeCB();
            } catch (e) {}
          }
        }
      }
    },
    render:{
      value:function(data){

        overlayWidget.lastFocused = document.activeElement;

        var $overlayBody = $('#'+this.overlayId).find(this.bodySelector),
            $overlayHeader = $('#'+this.overlayId).find(this.headerSelector),
            bt = this.bodyTemplate === '' + this.bodyTemplate ? window[this.bodyTemplate.split('.')[0]][this.bodyTemplate.split('.')[1]] : this.bodyTemplate,
            ht = this.headerTemplate === '' + this.headerTemplate ? window[this.headerTemplate.split('.')[0]][this.headerTemplate.split('.')[1]] : this.headerTemplate;

        if (!bt) {
          return;
        }

        if($overlayBody.length===0){
          $(this.placeholder).append(this.template({
            overlayId: this.overlayId,
            hasClose: this.hasClose,
            customClass: this.customClass,
            bodyContent: bt({data: data || {}}),
            headerContent: ht({data: data || {}}),
            ariaLabelledby: this.ariaLabelledby,
            ariaDescribedby: this.ariaDescribedby
          }));
        } else {
          $overlayBody.html(bt({data: data || {}}));
          $overlayHeader.html(ht({data: data || {}}));
        }

        setTimeout(function () {
          if (fd.modules.common.Elements) {
            fd.modules.common.Elements.decorate($overlayBody);
          }
          if (fd.modules.common.Select) {
            fd.modules.common.Select.selectize($overlayBody);
          }
          if (fd.modules.common.Select) {
            fd.modules.common.aria.decorate();
          }
        }, 10);

        setTimeout(function () {
          this.focus();
        }.bind(this), 10);

        $('#' + this.overlayId).css({display: "block", "z-index": this.overlayConfig.zIndex});
        $('#' + this.overlayId + ' .overlay-close-icon').css({"z-index": this.overlayConfig.zIndex+2});
        $('#' + this.overlayId + ' .overlay-header-helper').css({"z-index": this.overlayConfig.zIndex+1});
        $('body').addClass('overlay-opened');
      }
    },
    refresh:{
      value:function(data){
        this.render(data);
      }
    },
    open:{
      value: function (e) {
        var $t = e && $(e.currentTarget) || $(document.body);
        this.render();
      }
    },
    focus:{
      value:function() {
        var $el = $('#' + this.overlayId),
            lastEl = '';

        $('[tabindex]').each(function (i, tiel) {
          var $tiel = $(tiel),
              ti = $tiel.prop('tabindex');

          if (+ti !== -1) {
            this.tabIndixes.push({
              $el: $tiel,
              ti: ti
            });
          }
        }.bind(this));
        $('input, button, textarea, select, a, [tabindex]').each(function (i, tiel) {
          var $tiel = $(tiel);

          $tiel.attr('tabindex', '-1');
        });

        $el.find('input, button, textarea, select, a').not('[disabled]').not('[type="hidden"]').not('[nofocus]').not('.overlay [data-view-all-popup]').each(function (i, tiel) {
          var $tiel = $(tiel);

          lastEl = i+1;

          $tiel.attr('tabindex', lastEl);
        });

        if ($el.find('.overlay-close-icon').size() > 0 ) {
          var close = $el.find('.overlay-close-icon').each(function (i, close) {
            var $close = $(close);

            lastEl = lastEl+i+1;

            $close.attr('tabindex', lastEl);
          });
        }

        var $lowestTabElem = null;

        $el.find('[tabindex]').not('[tabindex="-1"]').each(function(i,e) {
            if ($lowestTabElem === null || parseInt($(e).attr('tabindex')) < parseInt($($lowestTabElem).attr('tabindex'))) {
                $lowestTabElem = $(e);
            }
        });

        if ($lowestTabElem !== null) {
            $lowestTabElem.focus();
        }
      }
    },
    reset:{
      value: function () {
        var $el = $(this.bodySelector),
        rect;

        $('[tabindex="-1"]').each(function (i, tiel) {
          var $tiel = $(tiel);

          $tiel.attr('tabindex', null);
        });

        $el.find('[tabindex]').each(function (i, tiel) {
          var $tiel = $(tiel);

          $tiel.attr('tabindex', '-1');
        });

        this.tabIndixes.forEach(function (tiel) {
          tiel.$el.attr('tabindex', tiel.ti);
        });

        if (this.lastFocused) {
          this.lastFocused.focus();
          this.lastFocused = null;
        }
      }
    }
  });

  $(document).on('click', overlayWidget.closeTrigger, overlayWidget.close.bind(overlayWidget));

  fd.modules.common.utils.register("modules.common", "overlayWidget", overlayWidget, fd);

}(FreshDirect));
