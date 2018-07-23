/*global common,jQuery*/
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
        var eventSource = value.eventSource || value.recommenderResult.eventSource || '';

        if(isTabbedSitefeature) { 
          /* update tab when prop changes. data values must be updated, they're used in selectTab() */   
          var $selectedTab = $('[data-component="tabbedRecommender"] [data-component="tabitem"].selected'),
              $tabPanel = $('.tab-container.light-carousel[data-eventsource]');

          $selectedTab.attr('data-tabname', value.tabTitle || $selectedTab.attr('data-tabname'));  
          $selectedTab.data('tabname', $selectedTab.attr('data-tabname'));   
          $selectedTab.attr('data-sitefeature', siteFeature || $selectedTab.attr('data-sitefeature'));   
          $selectedTab.data('sitefeature', $selectedTab.attr('data-sitefeature'));
          $tabPanel.attr('data-eventsource', eventSource || $tabPanel.attr('data-eventsource'));
          $tabPanel.data('eventsource', $selectedTab.attr('data-eventsource'));
          $selectedTab.html(value.tabTitle);
        }
        $('[data-component="tabbedRecommender"] [data-component="tabpanel"]').css('min-height',0).html(this.template(value));

        $('[data-component="carousel"]').each(function (i, el) {
          var $el = $(el);

          if (fd.components && fd.components.carousel && fd.components.carousel.changePage) {
            fd.components.carousel.changePage($el, null);
          }
        });
      }
    },
    selectTab:{
      value:function(tabPanelElement, tabName, clickedTab) {
        var proto = Object.getPrototypeOf(this),
            siteFeature = clickedTab.data('sitefeature')||'',
            impressionId = clickedTab.data('impressionid'),
            parentImpressionId = clickedTab.data('parentimpressionid'),
            parentVariantId = clickedTab.data('parentvariantid'),
            tabPanel, recommender, url, data;
        
        proto.selectTab.call(this,tabPanelElement,tabName);
        if (siteFeature.trim()!=='') {
          recommender = clickedTab.parents('[data-component="tabbedRecommender"]').first();
          tabPanel = recommender.find('[data-component="tabpanel"]');
          tabPanel.attr('data-siteFeature', siteFeature);
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
            }
          });
        }
      }
    }   
  });

  
  tabbedRecommender.listen();

  $(document).on('click','[data-component="tabbedRecommender"]',tabbedRecommender.clickHandler.bind(tabbedRecommender));  
  
  fd.modules.common.utils.register("common", "tabbedRecommender", tabbedRecommender, fd);
}(FreshDirect));
