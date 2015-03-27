/*global jQuery,quickshop*/
var FreshDirect = FreshDirect || {};

(function (fd) {
    "use strict";

    var $ = fd.libs.$;
    var WIDGET = fd.modules.common.widget;
    var DISPATCHER = fd.common.dispatcher;
    var CAROUSEL = fd.components.carousel;
    var DEFAULTDEPTID = '';
    var DEFAULTFEATURE = 'CRAZY_QUICKSHOP';
    var QSVersion = fd.utils.getActive('quickshop');
    var APIURL = QSVersion === '2_0' ? '/api/qs/ymal' : '/api/reorder/recommendation';
    var MINDELAY = 4000;
    var MAXDELAY = 8000;
    var DEFAULTLIMIT = 15;

    var liveRecommender = Object.create(WIDGET,{
        signal: {
            value: 'recommenderResult'
        },
        template: {
            value: common.liveRecommender
        },
        placeholder: {
            value: '[data-component="liverecommender"][data-direction="horizontal"]'
        },
        callback: {
            value: function (value) {
                var feature = $(this.placeholder).data('feature') || DEFAULTFEATURE;
                if(value.siteFeature === feature ) {
                    value.itemType = 'grid';
                    value.items.forEach(function(item){
                        item.itemId = 'atc_'+item.productId+'_'+item.skuCode+'_'+(Math.random()*10000).toString(24);
                    });
                    WIDGET.callback.call(this, value );
                }
            }
        },
        render:{
            value:function(data){
                $(this.placeholder).html(this.template(data));
                $(this.placeholder).find('[data-component="product"]').first().addClass('live');
                this.animateTimeout = window.setTimeout(this.animate.bind(this),Math.random()*(MAXDELAY-MINDELAY)+MINDELAY);
            }
        },
        update: {
            value: function (data) {
                var $cnt = $(this.placeholder),
                    deptId = $cnt.data('deptId') || DEFAULTDEPTID,
                    feature = $cnt.data('feature') || DEFAULTFEATURE;
                DISPATCHER.signal('server',{
                    url: APIURL,
                    method: 'GET',
                    data: {
                        data: JSON.stringify({ feature: feature, deptId: deptId })
                    }
                });
            }
        },

        animateTimeout: {
            value: 0,
            writable: true
        },
        animate: {
            value: function () {
                $(this.placeholder).find('[data-component="product"]:not(.live)').first().addClass('live');
                this.animateTimeout = window.setTimeout(this.animate.bind(this),Math.random()*(MAXDELAY-MINDELAY)+MINDELAY);
            }
        }
    });

    var liveRecommenderV = Object.create(liveRecommender,{
        placeholder: {
            value: '[data-component="liverecommender"][data-direction="vertical"]'
        },
        render:{
            value:function(data){
                $(this.placeholder).html(this.template(data));
                $(this.placeholder).find('[data-component="product"]').last().addClass('live');
                this.animateTimeout = window.setTimeout(this.animate.bind(this),Math.random()*(MAXDELAY-MINDELAY)+MINDELAY);
                this.itemLimit = $(this.placeholder).data("limit") || DEFAULTLIMIT;
            }
        },
        animateTimeout: {
            value: 0,
            writable: true
        },
        itemLimit: {
            value: DEFAULTLIMIT,
            writable: true
        },
        animate: {
            value: function () {
                var $cnt = $(this.placeholder);
                $cnt.find('[data-component="product"].live').first().prev().addClass('live');
                if ($cnt.find('[data-component="product"].live').length > this.itemLimit ) {
                    $cnt.find('[data-component="product"].live').last().removeClass('live');
                }
                this.animateTimeout = window.setTimeout(this.animate.bind(this),Math.random()*(MAXDELAY-MINDELAY)+MINDELAY);
            }
        }
    });

    if ($(liveRecommender.placeholder).length) {
        liveRecommender.listen();
        liveRecommender.update();
    }
    if ($(liveRecommenderV.placeholder).length) {
        liveRecommenderV.listen();
        liveRecommenderV.update();
    }

    fd.modules.common.utils.register("modules.common", "liveRecommender", liveRecommender, fd);
}(FreshDirect));
