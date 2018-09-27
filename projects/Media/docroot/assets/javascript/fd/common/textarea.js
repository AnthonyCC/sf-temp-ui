/*global jQuery*/
var FreshDirect = window.FreshDirect || {};

(function (fd, $) {
  function collectTextareas(root){
    var SELECTOR = 'textarea[data-max-length-display]:not([data-max-length-display-added])';
    return $(root).find(SELECTOR);
  }

  var Textarea = {
    addMaxLengthCounters: function(root){
      
      collectTextareas(root).each(function(_, el){
        var textarea = $(el);
        var maxLength = textarea.attr('maxlength');
        var countDisplay = $('<div class="text-area-length-display"></div>')
        textarea
        .attr('data-max-length-display-added', 'true')
        .on('input', function(){
          var length = textarea.val().length;
          countDisplay.html(length + '/' + maxLength);
        })
        .trigger('input')
        .after(countDisplay);
      });
    }
  };

  // register in fd namespace
  fd.modules.common.utils.register("modules.common", "Textarea", Textarea, fd);
}(FreshDirect, jQuery));
