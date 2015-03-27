/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict"

	var $ = fd.libs.$;
	var DISPATCHER = fd.common.dispatcher;
  var QSVersion = fd.utils.getActive('quickshop');
  var APIURL = QSVersion === '2_0' ? '/api/qs/ymal' : '/api/reorder/recommendation';

	var tabbedRecommender = Object.create(Object.create(fd.common.signalTarget,fd.common.tabPanel),{
		template:{
			value:common.simpleCarousel
		},
		signal:{
			value:'recommenderResult'
		},
		callback:{
			value:function(value){
				var siteFeature = value.siteFeature,
					isTabbedSitefeature = $('[data-component="tabbedRecommender"] [data-sitefeature="'+siteFeature+'"]').length > 0;
				if(isTabbedSitefeature) {
					value.itemType = 'grid';
					$('[data-component="tabbedRecommender"] [data-component="tabpanel"]').css('min-height',0).html(this.template(value));
					fd.components.carousel.changePage($('[data-component="tabbedRecommender"] [data-component="tabpanel"] [data-component="carousel"]'));
				}
			}
		},
		selectTab:{
			value:function(tabPanelElement,tabName,clickedTab) {
				var proto = Object.getPrototypeOf(this),
					siteFeature = clickedTab.data('sitefeature'),
          impressionId = clickedTab.data('impressionid'),
          parentImpressionId = clickedTab.data('parentimpressionid'),
          parentVariantId = clickedTab.data('parentvariantid'),
					tabPanel, recommender, url, data;
				
				proto.selectTab.call(this,tabPanelElement,tabName);
				if(siteFeature.trim()!==''){
          recommender = clickedTab.parents('[data-component="tabbedRecommender"]').first();
					tabPanel = recommender.find('[data-component="tabpanel"]');
					tabPanel.attr('data-cmSiteFeature',siteFeature);
					tabPanel.css('min-height', tabPanel.height());
					tabPanel.html('');
          url = recommender.data('apiendpoint') || APIURL;
          data = { feature: siteFeature };
          if (impressionId) {
            data.impressionId = impressionId;
            data.parentImpressionId = parentImpressionId;
            data.parentVariantId = parentVariantId;
          }
					DISPATCHER.signal('server',{
						url: url,
						method:'GET',
						data: {
              data: JSON.stringify(data)
				    }});
				}
			}
		}		
	});

	
	tabbedRecommender.listen();

	$(document).on('click','[data-component="tabbedRecommender"]',tabbedRecommender.clickHandler.bind(tabbedRecommender));	
	
	fd.modules.common.utils.register("common", "tabbedRecommender", tabbedRecommender, fd);
}(FreshDirect));
