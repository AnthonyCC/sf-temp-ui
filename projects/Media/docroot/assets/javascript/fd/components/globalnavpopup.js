var FreshDirect = FreshDirect || {};

// module initialization
(function (fd, $) {
	"use strict";

	/* one-time initializer */
	function globalnavPopupInitializer($menuitem) {
		if (!$menuitem || $menuitem === 'undefined' || $menuitem.length === 0 || $menuitem.attr('data-init') === 'true') {
			return; //error or already initialized
		}

		var	$popupcontent = $menuitem.find('[data-component="globalnav-popup-body"]'),
			$container = $('[data-component="globalnav-menu"]');
		
		/* change container since globalnav-menu is 100% */
		if (fd && fd.features && fd.features.active && fd.features.active['productCard'] === '2018') {
			$container = $('[data-component="globalnav-menu"] .top-nav-items');
		}

		if ($popupcontent.length === 0) {
			$popupcontent = $('[data-component="globalnav-popups"] [data-id="'+$menuitem.data('id')+'"]').first();
			if ($popupcontent.length) {
				$popupcontent.insertAfter($menuitem.children('span').first());
			}
		}

		if ($popupcontent.length && $container.length) {
			/* lazy load AFTER insert */
			$popupcontent.find('.lazyload:not(.lazy-loaded)').trigger('lazyLoad');

			var left = $menuitem.offset().left - $container.offset().left,
				center, width,
				containerWidth = $container.width();

			if (left > containerWidth / 2) {
				$menuitem.addClass('alignPopupRight');
			}
			
			if ($popupcontent.attr('data-popup-type') === 'superdepartment') {
				center = left + $menuitem.outerWidth() / 2;
				width = 160; // 2 * 80 for the gradients
				$popupcontent.find('li.submenuitem').each(function () {
					width += $(this).outerWidth();
				});
				$popupcontent.find('.subdepartments').css({
					'padding-left': Math.floor(Math.max(0, Math.min(center - width / 2, containerWidth - width)))
				});
			}

			/* mark as initialized and stop listening */
			$menuitem.attr('data-init', true);
			$menuitem.off('initializer');
		}
	}

	/* initializer pass-through */
	$('[data-component="globalnav-item"],[data-component="globalnav-submenu-item"]').on('initializer', function () {
		globalnavPopupInitializer($(this));
	});

	$('[data-component="globalnav-item"],[data-component="globalnav-submenu-item"]').on('mouseover', function () {
		$(this).trigger('initializer');
		$(this).find('[data-component="globalnav-popup-body"]').attr('aria-hidden', false);
	});
	$('[data-component="globalnav-item"],[data-component="globalnav-submenu-item"]').on('mouseleave', function () {
		$(this).find('[data-component="globalnav-popup-body"]').attr('aria-hidden', true);
	});
	
	$('[data-component="globalnav-item"],[data-component="globalnav-submenu-item"]').on("touchstart, focusin", function (e) {
		var $t = $(this);
		$t.trigger('initializer');

		/* stop propagation on sub items so aria tags are updated correctly */
		if ($t.attr('data-component') === 'globalnav-submenu-item') {
			e.stopPropagation();
		}

		var $openedPopup = $t.children('[data-component="globalnav-popup-body"]'),
			$previousOpenedPopup = $t.siblings('li').children('[data-component="globalnav-popup-body"]');
		
		if (!$t.hasClass('touched')) {
			e.preventDefault();
			
			if ($previousOpenedPopup.attr('aria-hidden') === 'false') {
				$previousOpenedPopup.attr('aria-hidden', true);
			}
		
			$t.parent().find('.touched').removeClass('touched');	
			$t.addClass('touched');
		}

		$openedPopup.attr('aria-hidden', false);
	});
	
	$(document).on('touchstart, focusin', function (e) {
		var $gnavparent = $(e.target).parents('[data-component="globalnav-menu"]'); 
		
		if ($gnavparent.length === 0) {
			$('[data-component="globalnav-menu"]').find('.touched').removeClass('touched');
		}
	});
}(FreshDirect, FreshDirect.libs.$));
