/*global jQuery,common*/
var FreshDirect = FreshDirect || {};

(function (fd) {

    var $ = fd.libs.$;
    var WIDGET = fd.modules.common.widget;
    var DISPATCHER = fd.common.dispatcher;

    // TMP: move to popupwidget.js?
    var popupWidget = Object.create(WIDGET,{
        signal:{
            value: null
        },
        template:{
            value:common.popup
        },
        bodyTemplate:{
            value:null,
            writable: true
        },
        trigger:{
            // value:null
            // TMP
            value: ''
        },
        $trigger:{
            // value:null
            // TMP
            value: null
        },
        popupId:{
            value:''
        },
        customClass: {
          value: ''
        },
        hasClose: {
          value: false
        },
        helpTemplate: {
          value: null
        },
        helpHeader: {
          value: ''
        },
        bodySelector:{
            value:'.qs-popup-content'
        },
        headerContent:{
            value:''
        },
        placeholder:{
            value:'body'
        },
        popupConfig:{
            value:{},
            writable:true
        },
        close: {
            value: function (e) {
              if (this.popup) {
                this.popup.hide(e);
              }
            }
        },
        noscroll: {
          value: function (force) {
            var tooHigh = false, boxes = [];
            
            if (this.scrollCheck) {
              boxes = [].concat(this.scrollCheck);
            } else {
              boxes = [this.bodySelector];
            }

            // check popup size
            this.popup.$el.removeClass('noscroll').css({
              top: 0
            });
            
            boxes.forEach(function (box) {
              var contentBox = this.popup.$el.find(box).first()[0];
              
              if (contentBox) {
                tooHigh = tooHigh || contentBox.clientHeight < contentBox.scrollHeight;
              }
            }, this);

            if ((tooHigh || 
                navigator.userAgent.toLowerCase().indexOf("ipad") > -1) || force) {
              this.popup.$el.addClass('noscroll').css({
                top: $(window).scrollTop() < 200 ? 220 : $(window).scrollTop() + 20
              });
            }
          }
        },
        initTrigger:{
            value:function(){
                var cnt = $('#' + this.popupId);
                this.popup = new fd.modules.common.PopupContent(
                        cnt.hide(),
                        this.$trigger,
                        this.popupConfig
                    );

                cnt.on('click', '.qs-popup-close-icon', this.close.bind(this));

                if(this.popupConfig.hideOnOverlayClick){
                  var self = this;
                  cnt.on('click', function(e){
                    if(e.target === e.currentTarget){ self.popup.hide(); }
                  });
                }
            }
        },
        render:{
            value:function(data){
                var $popupBody=$('#'+this.popupId).find(this.bodySelector),
                    bt = this.bodyTemplate === '' + this.bodyTemplate ? window[this.bodyTemplate.split('.')[0]][this.bodyTemplate.split('.')[1]] : this.bodyTemplate;

                if (!bt) {
                  return;
                }

                if($popupBody.length===0){
                    $(this.placeholder).append(this.template({
                        popupId: this.popupId,
                        headerContent: this.headerContent,
                        hasClose: this.hasClose,
                        helpTemplate: this.helpTemplate,
                        helpHeader: this.helpHeader,
                        customClass: this.customClass,
                        bodyContent: bt({data: data || {}})
                    }));
                    this.initTrigger();
                }else{
                    $popupBody.html(bt({data: data || {}}));
                }

                // reposition the popups
                if (this.popup && !this.popup.placeholderActive && this.popup.shown) {
                  this.popup.reposition();
                }
            }
        },
        refreshBody:{
            value:function(data,template,header){
                if(template){
                    this.bodyTemplate=template;
                }
                this.render(data);
                if (header) {
                  this.popup.$el.find('.qs-popup-header span').html(header);
                }
            }
        },
        open:{
          value: function (e) {
            var $t = e && $(e.currentTarget) || $(document.body);

            this.refreshBody();
            this.popup.show($t);
            this.popup.clicked = true;
          }
        }
    });

    fd.modules.common.utils.register("modules.common", "popupWidget", popupWidget, fd);


  /**
   * Init popup
   *
   * @param {jQuery} config trigger element w data attributes
   * OR
   * @param {Object} config
        options:
            {
                widgetConfig or data-widget-* : {
                    signal,
                    bodyTemplate, *required
                    trigger, *required
                    popupId, *required
                    headerContent, *required
                    bodySelector: '.qs-popup-content',
                    popupConfig or data-config-* : {
                        valign: 'bottom',
                        halign: 'right',
                        delay: 300,
                        placeholder: false,
                        overlay: false,
                        alignTo: null,
                        aligntoselector: null,
                        stayOnClick: null,
                        closehandle: null,
                        openonclick: null
                    }
                }
            }
   * @param {Object} config Configuration object
   */
    var initPopup = function (config) {
        var popupConfig = {
                halign: 'right',
                placeholder: false,
                overlay: false
            },
            widgetConfig = {},
            $trigger,
            signal,
            newPopup = Object.create(popupWidget);
        if (config.jquery) {
            $trigger = config;
            $.each($trigger.data(), function(k, v) {
                if (k !== 'popup' && k.indexOf('popup')===0) { // data-popup-*
                    var key = k.charAt(11).toLowerCase()+k.substring(12);
                    if (k.indexOf('popupWidget')===0) { // data-popup-widget-*
                        Object.defineProperty(newPopup,key,{value:v});
                    } else if (k.indexOf('popupConfig')===0) { // data-popup-config-*
                        popupConfig[key]=v;
                    }
                    if (k==='popupWidgetSignal') {
                        signal = v;
                    }
                }
            });
            Object.defineProperty(newPopup,'popupConfig',{value:popupConfig});
            Object.defineProperty(newPopup,'$trigger',{value:$trigger});
        } else {
            widgetConfig = config.widgetConfig;
            Object.getOwnPropertyNames(config.widgetConfig).forEach(function(key){
                if (key==='popupConfig') {
                    Object.getOwnPropertyNames(config.widgetConfig.popupConfig).forEach(function(k2){
                        popupConfig[k2]=config.widgetConfig.popupConfig[k2];
                    });
                    Object.defineProperty(newPopup,'popupConfig',{value:popupConfig});
                } else {
                    Object.defineProperty(newPopup,key,{value:config.widgetConfig[key]});
                }
            });
            signal = widgetConfig.signal;
            $trigger = widgetConfig.$trigger;
        }
        if (signal) {
            newPopup.listen();
        } else {
            newPopup.render();
        }
    };

    // init popups based on annotated triggers

    var popupTriggers = $('[data-popup]');

    popupTriggers.each(function(i,trigger){
        initPopup($(trigger));
    });

}(FreshDirect));
