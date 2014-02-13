/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict"

	var $ = fd.libs.$;
	var DISPATCHER = fd.common.dispatcher;

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
				}
			}
		},
		selectTab:{
			value:function(tabPanelElement,tabName,clickedTab) {
				var proto = Object.getPrototypeOf(this),
					siteFeature = clickedTab.data('sitefeature'),
					tabPanel;
				
				proto.selectTab.call(this,tabPanelElement,tabName);
				if(siteFeature.trim()!==''){
					tabPanel = $('[data-component="tabbedRecommender"] [data-component="tabpanel"]');
					tabPanel.attr('data-cmSiteFeature',siteFeature);
					tabPanel.css('min-height', tabPanel.height());
					tabPanel.html('');
					DISPATCHER.signal('server',{
						url:'/api/qs/ymal',
						method:'GET',
						data: {
				            data: JSON.stringify({ feature:siteFeature })
				    }});
				}
			}
		}		
	});

	
	tabbedRecommender.listen();

	$(document).on('click','[data-component="tabbedRecommender"]',tabbedRecommender.clickHandler.bind(tabbedRecommender));	
	
	fd.modules.common.utils.register("common", "tabbedRecommender", tabbedRecommender, fd);
}(FreshDirect));
