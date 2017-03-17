/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $=fd.libs.$,
      editorialModules = $('.content-module.editorial-module'),
      editorialModule = {
    trigger: '[learn-more]',
    closeTrigger: '[show-less]',
    showLearnMore: function (e) {
      var editorialBodyHeight = $(e).find('.editorial-body')[0].getBoundingClientRect().height,
          editorialBodyTextHeight = $(e).find('.editorial-body div')[0].getBoundingClientRect().height;

      if (editorialBodyTextHeight <= editorialBodyHeight) {
        var learnMore = $(e).find('.learn-more')[0];
        $(learnMore).removeClass('visible');
      }
    },
    learnMore: function (ev) {
      var module = $(ev.target).closest('.content-module.editorial-module')[0],
          showLessButton = $(module).find('.learn-more button[show-less]')[0];
      $(module).addClass('learn-more');
      $(ev.target).removeClass('visible');
      $(showLessButton).addClass('visible');
    },
    showLess: function (ev) {
      var module = $(ev.target).closest('.content-module.editorial-module')[0],
          learnMoreButton = $(module).find('.learn-more button[learn-more]')[0];
      $(module).removeClass('learn-more');
      $(ev.target).removeClass('visible');
      $(learnMoreButton).addClass('visible');
    }
  };

  for(var i = 0; i < editorialModules.length; i++) {
    editorialModule.showLearnMore(editorialModules[i]);
  }

  $(document).on('click', editorialModule.trigger, function (ev) {
    editorialModule.learnMore(ev);
  });
  $(document).on('click', editorialModule.closeTrigger, function (ev) {
    editorialModule.showLess(ev);
  });

  fd.modules.common.utils.register('components', 'editorialModule', editorialModule, fd);
}(FreshDirect));
