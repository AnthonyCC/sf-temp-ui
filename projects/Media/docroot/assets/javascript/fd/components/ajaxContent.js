var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;

  var ajaxContent = {
    TRIGGER: 'fd-ajaxcontent',
    TARGET: 'fd-ajaxcontent-target',
    TEMPLATE: 'fd-ajaxcontent-template',
    CALLBACK: 'fd-ajaxcontent-callback',
    URL: 'href',
    updateHandler: function (e) {
      ajaxContent.update($(e.currentTarget));

      e.stopPropagation();
      e.preventDefault();
    },
    update: function(contentElement, successHandler){
      if (!contentElement) { return; }

      var $contentElement = $(contentElement),
      $target = $($contentElement.attr(ajaxContent.TARGET)),
      url = $contentElement.attr(ajaxContent.URL),
      template = $contentElement.attr(ajaxContent.TEMPLATE) &&
        fd.modules.common.utils.discover($contentElement.attr(ajaxContent.TEMPLATE)),
      callback = fd.modules.common.utils.discover($contentElement.attr(ajaxContent.CALLBACK));

      if (!$target) { return; }

      $.get(url, function (data) {
        var renderedTemplate;
        if (template) {
          renderedTemplate = template(data);
        }
        $target.html(renderedTemplate || data);
        successHandler && successHandler();
        callback && callback($target, data);
      });
    }
  };

  $(document).on('click', '['+ajaxContent.TRIGGER+']', ajaxContent.updateHandler.bind(ajaxContent));

  fd.modules.common.utils.register("components", "ajaxContent", ajaxContent, fd);
}(FreshDirect));
