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
				var siteFeature = value.siteFeature || value.recommenderResult.siteFeature || '',
					isTabbedSitefeature = $('[data-component="tabbedRecommender"] [data-sitefeature="'+siteFeature+'"]').length > 0;
				if(isTabbedSitefeature) {
					/* switch deltemplate */
					if (siteFeature === 'PRODUCT_SAMPLES' || siteFeature === 'PRODUCT_DONATIONS') {
						/* siteFeature defined in ViewCartCarouselService
						 * itemType used in common.carouselItem */
						value.itemType = 'grid_prodSample';
						/* switch siteFeature if it switched with ajax call */
						if (value.tabSiteFeature) {
							siteFeature = value.tabSiteFeature;
						}
						/* determine CM event */
						value.cmEventSource = value.cmEventSource || '';
						if (value.cmEventSource === '') {
							if (siteFeature === 'PRODUCT_SAMPLES') {
								value.cmEventSource += 'ps_carousel_';
							} else if (siteFeature === 'PRODUCT_DONATIONS') {
								value.cmEventSource += 'dn_carousel_';
							}
							
							value.cmEventSource += (
								(window.location.pathname.indexOf('view_cart.jsp')!== -1) 
									? 'view_cart'
									: (window.location.pathname.indexOf('checkout.jsp')!== -1) 
										? 'checkout'
										: ''
							);
						}
						/* update tab when prop changes. data values must be updated, they're used in selectTab() */
						if (value.tabTitle) {
							var $selectedTab = $('[data-component="tabbedRecommender"] [data-component="tabitem"].selected');
							$selectedTab.attr('data-tabname', value.tabTitle || $selectedTab.attr('data-tabname'));
							$selectedTab.data('tabname', $selectedTab.attr('data-tabname'));
							$selectedTab.attr('data-sitefeature', siteFeature || $selectedTab.attr('data-sitefeature'));
							$selectedTab.data('sitefeature', $selectedTab.attr('data-sitefeature'));
							$selectedTab.html(value.tabTitle);
						}
					} else {
						value.itemType = 'grid';
					}
					$('[data-component="tabbedRecommender"] [data-component="tabpanel"]').css('min-height',0).html(this.template(value));
					fd.components.carousel.changePage($('[data-component="tabbedRecommender"] [data-component="tabpanel"] [data-component="carousel"]'));
				}
			}
		},
		selectTab:{
			value:function(tabPanelElement, tabName, clickedTab) {
				var proto = Object.getPrototypeOf(this),
					siteFeature = clickedTab.data('sitefeature'),
					impressionId = clickedTab.data('impressionid'),
					parentImpressionId = clickedTab.data('parentimpressionid'),
					parentVariantId = clickedTab.data('parentvariantid'),
					tabPanel, recommender, url, data;
				
				proto.selectTab.call(this,tabPanelElement,tabName);
				if (siteFeature.trim()!=='') {
					recommender = clickedTab.parents('[data-component="tabbedRecommender"]').first();
					tabPanel = recommender.find('[data-component="tabpanel"]');
					tabPanel.attr('data-cmSiteFeature', siteFeature);
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
