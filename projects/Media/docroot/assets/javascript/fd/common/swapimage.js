/*global jQuery*/
// old swapImage replacement

(function ($) {
  $('[data-swapimage]').on('mouseover', function (e) {
    var target = $(e.currentTarget),
        imgel = target.data('swapimagename') ? $('[name="'+target.data('swapimagename')+'"]') : target,
        origimage;

    if (target.data('origimage')) {
      origimage = target.data('origimage');
    } else {
      origimage = imgel.attr('src');
      target.data('origimage', origimage);
    }

    imgel.attr('src', target.data('swapimage'));
  });
  $('[data-swapimage]').on('mouseout', function (e) {
    var target = $(e.currentTarget),
        imgel = target.data('swapimagename') ? $('[name="'+target.data('swapimagename')+'"]') : target,
        origimage;

    if (target.data('origimage')) {
      origimage = target.data('origimage');
    } else {
      origimage = imgel.attr('src');
      target.data('origimage', origimage);
    }

    imgel.attr('src', origimage);
  });
}(jQuery));
