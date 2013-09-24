/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict";

	var $ = fd.libs.$;
		
    function getWidth(element) {
        return $(element).outerWidth(true);
    }
	
    function getIndent( carousel, nextPage) {

    	var $mask = $('[data-component="carousel-mask"]', carousel );
    	var $list = $('[data-component="carousel-list"]', carousel );
        var pageSize = $mask.width();
        var current = parseInt($list.css('left'),10) || 0;
        var elements = $list.children();


        var result = 0, i = 0, elementWidth;

        if( nextPage ) {
            var border = current - pageSize;

            while( elements.length > i &&
                 (result - ( elementWidth = getWidth(elements[i]) ) >= border) ) {
                i++;
                result = result - elementWidth;
            }

            carousel.removeClass('first');
            result = elements.length === i ? current : result;
            
        } else {
            result = Math.min(current + pageSize, 0);
            carousel.removeClass('last');
        }

        if(result === 0) {
            carousel.addClass('first');            	
        }

        if( (elements.last().position().left - current + result) <= pageSize ) {
        	carousel.addClass('last');
        }

        return result;

    }

	
	var carousel = {
			handleClick:function(event){
		        var carousel = $(event.currentTarget), 
	            	direction,indent;

		        if( direction =  $(event.target).data('carousel-nav') ) {
	
		            indent = getIndent(carousel, direction === "next" );
	
		            $('[data-component="carousel-list"]', carousel ).css("left", indent + "px");
		        }
	    }
	};
	
    $(document).on('click','[data-component="carousel"]',carousel.handleClick.bind(carousel));	

	fd.modules.common.utils.register("components", "carousel", carousel, fd);
}(FreshDirect));
