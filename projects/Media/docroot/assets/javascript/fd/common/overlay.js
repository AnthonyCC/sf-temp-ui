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
    close: {
      value: function (e) {
        if (e && e.stopPropagation) {
          e.stopPropagation();
        }
        $(e.currentTarget).closest('.overlay').css({display: "none"});
        $('body').removeClass('overlay-opened');
      }
    },
    render:{
      value:function(data){
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
    }
  });

  $(document).on('click', overlayWidget.closeTrigger, function (e) {
    overlayWidget.close(e);
  });

  fd.modules.common.utils.register("modules.common", "overlayWidget", overlayWidget, fd);

}(FreshDirect));
