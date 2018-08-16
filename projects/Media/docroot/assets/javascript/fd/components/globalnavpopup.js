var FreshDirect = FreshDirect || {};

// module initialization
(function (fd, $) {
  "use strict";

  // TODO
  // - iPad - add class on touch, prevent click
  $(function() {
    var $container = $('[data-component="globalnav-menu"]'), containerOffsetLeft, containerWidth;
    if ($container.length) {

      containerOffsetLeft = $container.offset().left;
      containerWidth = $container.width();

      // copy menu popups to menu items
      ["globalnav-item", "globalnav-submenu-item"].forEach(function (item) {
        $('[data-component="globalnav-menu"] [data-component="'+item+'"]').each(function () {
          var $menuitem = $(this),
              $popupcontent = $menuitem.find('[data-component="globalnav-popup-body"]'),
              left,
              id = $menuitem.data('id'),
              center, width;

          window.requestAnimationFrame(function() {
            left = $menuitem.offset().left - containerOffsetLeft;
          });

          if( $('[data-component="globalnav-popup-body"][data-id="'+id+'"]').length ) {
              $menuitem.find('.top-item-link a,.submenuitem-link a').attr({
                  'aria-haspopup': true
              });
          }
          
          if (left > containerWidth / 2) {
            window.requestAnimationFrame(function() {
              $menuitem.addClass('alignPopupRight');
            });
          }
    
          if ($popupcontent.length === 0) {
            $popupcontent = $('[data-component="globalnav-popups"] [data-id="'+id+'"]').first();
            
            if ($popupcontent.length > 0) {
              $popupcontent.insertAfter($menuitem.children('span').first());
              if ($popupcontent.attr('data-popup-type') === 'superdepartment') {
                window.requestAnimationFrame(function() {
                  center = left + $menuitem.outerWidth() / 2;
                  width = 160; // 2 * 80 for the gradients
                  $popupcontent.find('li.submenuitem').each(function () {
                    width += $(this).outerWidth();
                  });
                  $popupcontent.find('.subdepartments').css({
                    'padding-left': Math.max(0, Math.min(center - width / 2, 960 - width))
                  });
                });
              }
            }
          }
        });
      });
      
      // set popup height
      /* $('.top-nav-items [data-component="globalnav-popup-body"]').each(function () {
        var $el = $(this),
            depHeight = +$el.find('.department').outerHeight();

        $el.find('.heroimg_cont').height(depHeight);
      });*/
    }
  });

  // for dynamic fallback
  $('[data-component="globalnav-menu"]').on('mouseover', '[data-component="globalnav-item"],[data-component="globalnav-submenu-item"]', function (e) {
    var $menuitem = $(e.currentTarget),
        $popupcontent = $menuitem.find('[data-component="globalnav-popup-body"]'),
        $container = $('[data-component="globalnav-menu"]'),
        left = $menuitem.offset().left - $container.offset().left,
        id, center, width;

    if (left > $container.width() / 2) {
      $menuitem.addClass('alignPopupRight');
    }
    
    $popupcontent.attr('aria-hidden', false);

    if ($popupcontent.length === 0) {
      id = $menuitem.data('id');
      $popupcontent = $('[data-component="globalnav-popups"] [data-id="'+id+'"]').first();
      if ($popupcontent.length > 0) {
        $popupcontent.insertAfter($menuitem.children('span').first());
      }
    }

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
    $menuitem.on('mouseleave', function (e) {
  	  $popupcontent.attr('aria-hidden', true);
    })
  });
  
  
 
  
  $('[data-component="globalnav-item"],[data-component="globalnav-submenu-item"]').on("touchstart, focusin", function (e) {
    var $t = $(this),
    	$openedPopup = $t.children('[data-component="globalnav-popup-body"]'),
    	$previousOpenedPopup = $t.siblings('li').children('[data-component="globalnav-popup-body"]');
    	

    if (!$t.hasClass('touched')) {
      e.preventDefault();
      if ($openedPopup.attr('aria-hidden') === 'true') {
    	  $openedPopup.attr('aria-hidden', false);
      }
       
      if ($previousOpenedPopup.attr('aria-hidden') === 'false') {
    	 $previousOpenedPopup.attr('aria-hidden', true);
      }
     
      $t.parent().find('.touched').removeClass('touched');  
      $t.addClass('touched');
    }
  });
  
  $(document).on('touchstart, focusin', function (e) {
    var $gnavparent = $(e.target).parents('[data-component="globalnav-menu"]'); 
  
    if ($gnavparent.length === 0) {
      $('[data-component="globalnav-menu"]').find('.touched').removeClass('touched');
    }
  });
}(FreshDirect, FreshDirect.libs.$));
