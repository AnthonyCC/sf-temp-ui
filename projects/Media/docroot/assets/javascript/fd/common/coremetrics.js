/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict";

	var DISPATCHER = fd.common.dispatcher;
	var $ = fd.libs.$;
  var cmevent = "noevent"; // noevent, page, sort, element, pageview


	var coremetrics = Object.create(fd.common.signalTarget,{
		signal:{
			value:'coremetrics'
		},
		playOneItem:{
			value:function( coremetricsItem ){
				try{
					var fname = coremetricsItem.shift();
					window[fname].apply(window,coremetricsItem);
				} catch(e) {
					// console.log('cm: '+e);
				}
			}
		},
    setEvent:{
      value: function (cme, force) {
        cmevent = (force ? cme :
          (cme === 'pageview' || cmevent === 'pageview') ? 'pageview' : (
          (cme === 'element' || cmevent === 'element') ? 'element' : (
          (cme === 'sort' || cmevent === 'sort') ? 'sort' : (
          (cme === 'page' || cmevent === 'page') ? 'page' :
          'noevent'))));
      }
    },
    serialize:{
      value: function () {
        return {
          browseEvent: cmevent
        };
      }
    },
		callback:{
			value:function( coremetricsData ) {
        if (coremetricsData.each) {
          coremetricsData.each(this.playOneItem);
        } else if (coremetricsData.forEach) {
          coremetricsData.forEach(this.playOneItem);
        }
        cmevent = 'noevent';
			}
		}
	});

	coremetrics.listen();

  $(document).on('click', '[cm-click]', function (e) {
    var cmData = $(e.target).attr('cm-click');

    coremetrics.playOneItem(cmData.split(','));
  });

  if (fd.coremetricsData) {
    fd.coremetricsData.each(coremetrics.playOneItem, coremetrics);
  }

	function addCmData(propertyName,value,event){
		event.cmData[propertyName] = value;
	}

	function populateCmData(propertyName,event){
		var $ct = $(event.currentTarget);
		addCmData(propertyName,$ct.attr('data-cm'+propertyName.toLowerCase()), event);
	}

	$(document.body).on('addToCart','[data-cmsitefeature]',function(event){
		populateCmData('siteFeature',event);
	});

	$(document.body).on('addToCart','[data-cmeventsource]',function(event){
    if (event.cmData && event.cmData.eventSource) {
      return;
    }
		populateCmData('eventSource',event);
	});

	$(document.body).on('addToCart','[data-cmvariantid]',function(event){
		populateCmData('variantId',event);
	});

	$(document.body).on('click','[manual_cm_sp]',function(event){
    if($(event.currentTarget).closest('[carousel-page-number]').length > 0) {
  		var coremetricsItem = [],
          dataString,
          pageNumber = $(event.currentTarget).closest('[carousel-page-number]')[0].attributes['carousel-page-number'].value,
          carouselInfo = $(event.currentTarget).closest('[manual_cm_sp]')[0].attributes['manual_cm_sp'].value.split('-_-');

      if(carouselInfo.length > 2) {
        dataString = carouselInfo[2] + '-_-' + pageNumber + '-_-' + carouselInfo[0];
      } else {
        dataString = '-_-' + pageNumber + '-_-' + carouselInfo[0];
      }

      coremetricsItem.push('cmCreateManualLinkClickTag');
      coremetricsItem.push(event.currentTarget.baseURI + '?cm_sp=' + encodeURI(dataString));
      coremetricsItem.push('');
      coremetricsItem.push('PRODUCT: ' + carouselInfo[1]);
      coremetrics.playOneItem(coremetricsItem);
    }
	});

	$(document.body).on('click','[data-viewallinfo]',function(event){
		var coremetricsItem = [],
        dataString,
        viewAllInfo = $(event.currentTarget).closest('[data-viewallinfo]')[0].attributes['data-viewallinfo'].value.split(',');

      dataString = viewAllInfo[0] + '-_-' + viewAllInfo[1] + '-_--_--_--_-' + FreshDirect.user.cohortName;

    coremetricsItem.push('cmCreateElementTag');
    coremetricsItem.push('View All');
    coremetricsItem.push('View All');
    coremetricsItem.push(dataString);
    coremetrics.playOneItem(coremetricsItem);

	});

	$(document.body).on('addToCart',function(event){
		try {
			addCmData('coremetricsPageId',Coremetrics.pageId,event);
			addCmData('coremetricsPageContentHierarchy',Coremetrics.pageContentHierarchy,event);
			addCmData('coremetricsVirtualCategory',Coremetrics.virtualCategory,event);
		} catch(e){
			// TODO: log coremetrics errors
		}
	});

	fd.modules.common.utils.register("components", "coremetrics", coremetrics, fd);
}(FreshDirect));
