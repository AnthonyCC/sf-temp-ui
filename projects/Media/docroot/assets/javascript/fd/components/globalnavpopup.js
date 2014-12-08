var FreshDirect = FreshDirect || {};

// module initialization
(function (fd, $) {
  "use strict";

  // TODO
  // - iPad - add class on touch, prevent click

  $(document).ready(function () {
    // copy menu popups to menu items
    ["globalnav-item", "globalnav-submenu-item"].forEach(function (item) {
      $('[data-component="globalnav-menu"] [data-component="'+item+'"]').each(function () {
        var $menuitem = $(this),
            $popupcontent = $menuitem.find('[data-component="globalnav-popup-body"]'),
            $container = $('[data-component="globalnav-menu"]'),
            left = $menuitem.offset().left - $container.offset().left,
            center, width, id;

        if (left > $container.width() / 2) {
          $menuitem.addClass('alignPopupRight');
        }

        if ($popupcontent.size() === 0) {
          id = $menuitem.data('id');
          $popupcontent = $('[data-component="globalnav-popups"] [data-id="'+id+'"]').first();
          if ($popupcontent.size() > 0) {
            $popupcontent.insertAfter($menuitem.children('span').first());
            if ($popupcontent.attr('data-popup-type') === 'superdepartment') {
              center = left + $menuitem.outerWidth() / 2;
              width = 160; // 2 * 80 for the gradients
              $popupcontent.find('li.submenuitem').each(function () {
                width += $(this).outerWidth();
              });
              $popupcontent.find('.subdepartments').css({
                'padding-left': Math.max(0, Math.min(center - width / 2, 960 - width))
              });
            }
          }
        }
      });
    });

    // set popup height
    $('[data-component="globalnav-popup-body"]').each(function () {
      var $el = $(this),
          depHeight = +$el.find('.department').outerHeight();

      $el.find('.heroimg_cont').height(depHeight);
    });

    // for dynamic fallback
    $('[data-component="globalnav-menu"]').on('mouseover', '[data-component="globalnav-item"],[data-component="globalnav-submenu-item"]', function (e) {
      var $menuitem = $(e.currentTarget),
          $popupcontent = $menuitem.find('[data-component="globalnav-popup-body"]'),
          id;

      if ($popupcontent.size() === 0) {
        id = $menuitem.data('id');
        $popupcontent = $('[data-component="globalnav-popups"] [data-id="'+id+'"]').first();
        if ($popupcontent.size() > 0) {
          $popupcontent.insertAfter($menuitem.children('span').first());
        }
      }
    });

    $('[data-component="globalnav-item"],[data-component="globalnav-submenu-item"]').on("touchstart", function (e) {
      var $t = $(this);

      if (!$t.hasClass('touched')) {
        e.preventDefault();
        $t.parent().find('.touched').removeClass('touched');
        $t.addClass('touched');
      }
    });
  });
}(FreshDirect, FreshDirect.libs.$));
