var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;

  var accessibleTabs = {
    TAB_SELECTOR: "[role='tab']",
    CONTENT_SELECTOR: "[role='tabpanel']",
    updateSelectedTab: function(tabElement){
      if(!tabElement){ return; }
      var tabEl = $(tabElement);
      var container = tabEl.closest("[role='tablist']");
      container.find(accessibleTabs.TAB_SELECTOR).attr('aria-selected', 'false');
      tabEl.attr('aria-selected', 'true');
    },
    updateTabContentLabelledBy: function(tabElement){
      if(!tabElement){ return; }
      var tabEl = $(tabElement);
      var tabContentLink = tabEl.attr("aria-controls");
      var tabPanel = $("#"+tabContentLink);
      tabPanel.attr("aria-labelledby", tabEl.attr("id"));
    },
    tabbedCarouselAriaSupport: function(){
      var tabIndex = 0;
      var contentIndex = 0;

      $('[data-component="tabbedRecommender"]').each(function(){
        var contentIndex = 0;

        var tabContent = $(this).find(accessibleTabs.CONTENT_SELECTOR),
            contentId = '';

        if(!tabContent.attr('id')){
          contentId = 'carouselTabContent' + contentIndex++;
          tabContent.attr('id', contentId);

          $(this).find(accessibleTabs.TAB_SELECTOR).each(function(){
            $(this).attr('id', 'carouselTab' + (tabIndex++));
            $(this).attr('aria-controls', contentId);
          });
        }

        accessibleTabs.updateTabContentLabelledBy($(this).find(accessibleTabs.TAB_SELECTOR + '.selected')[0]);
      });
    }
  };

  $(document).on('click', accessibleTabs.TAB_SELECTOR, function(e){
    accessibleTabs.updateSelectedTab(e.currentTarget);
    accessibleTabs.updateTabContentLabelledBy(e.currentTarget);
  });

  $(document).on('keydown', accessibleTabs.TAB_SELECTOR, function(e){
    if (e.which === 13) {
      $(e.currentTarget).click();
    }
  });

  $(document).ready(accessibleTabs.tabbedCarouselAriaSupport);

  fd.modules.common.utils.register("components", "accessibleTabs", accessibleTabs, fd);
}(FreshDirect));
