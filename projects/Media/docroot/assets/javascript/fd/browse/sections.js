/*global jQuery,browse,srch*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var WIDGET = fd.modules.common.widget;

  var sections = Object.create(WIDGET,{
    signal:{
    	value: (window.srch && fd.browse && fd.browse.data &&  fd.browse.data.pageType === 'STAFF_PICKS') ? 'assortProducts' : 'sections'
    	},
    template:{     	
    	value: (window.srch && fd.browse && fd.browse.data &&  fd.browse.data.pageType === 'STAFF_PICKS') ? srch.staffPicksContent : browse.content

    },
    placeholder:{
      value:'.browse-sections'
    },
    handleClick:{
      value:function(clickEvent){
        var clicked = $(clickEvent.currentTarget),
            parent = clicked.parents(this.placeholder),
            menu = fd.browse.menu;

        if (menu) {
          clickEvent.preventDefault();
          menu.setId(clicked.data('id'));
          parent.trigger('menu-change');
        }

      }
    }
  });
 
  var superSections = Object.create(WIDGET,{
    signal:{
      value:'sections'
    },
    template:{
      value:browse.superDepartment
    },
    placeholder:{
      value:'.browse-superdepartment'
    },
    handleClick:{
      value:function(clickEvent){
        var clicked = $(clickEvent.currentTarget),
            parent = clicked.parents(this.placeholder),
            menu = fd.browse.menu;

        if (menu) {
          clickEvent.preventDefault();
          menu.setId(clicked.data('id'));
          parent.trigger('menu-change');
        }

      }
    }
  });


	var adProductSection = Object.create(WIDGET,{
		signal:{
			value:'adProducts'
		},
		template:{
			value:common.simpleFixedProductList
		},
		placeholder:{
			value:'.browse-adproducts'
		}
	});

	adProductSection.fixThoseHooklogicDisplayHeights = function(){
		/* make all items in row the same min height (the height of the tallest elem in row) */
		$('ul.products.transactional').each(function (i,e) {
			var $children = $(this).children('li.browseTransactionalProduct');
			$children.css('min-height', Math.max.apply(null, 
				$children.map(function() {
					return $jq(this).outerHeight(true);
				})
			)+'px');
		});
		
		//fetch all skus for page beacons
		var activePage = (FreshDirect.browse.data.pager && FreshDirect.browse.data.pager.activePage) ? FreshDirect.browse.data.pager.activePage : 1
		if ($('.searchinput').length) { //look for search input box
			//prob search
			
			var hlSkus = [];
			var hlSkusStr = '';
			
			$('[data-hooklogic-beacon-impress]').each(function(i,e) {
				//find sku
				hlSkus.push($(this).find('[data-productdata-name="skuCode"]').val());
				//fire impression
				if ($('.browseContent .HLpageBeaconImg.impress-page_'+activePage+'_id_'+i+'_'+$(this).attr('id')).length === 0) {
					$('.browseContent').append('<img style="display: none;" class="HLpageBeaconImg impress-page_'+activePage+'_id_'+i+'_'+$(this).attr('id')+'" src="' + $(this).attr('data-hooklogic-beacon-impress') + '&random=' + new Date().getTime() + '" />');
				}
			});
			
			hlSkusStr = hlSkus.join(',');
			
			/* add page beacon (if it doesn't already exist) and we're on the first page only */
			if(activePage == 1){
				if (hlSkusStr !== '' && $(".browse-sections-top .browseContent .HLpageBeaconImg.page_SEARCH").length === 0) { /* only one instance at a time */
					if (FreshDirect.browse.data.adProducts.products.length === FreshDirect.browse.data.adProducts.hlProductsCount) {
						
						//all hlprods
						$(".browse-sections-top .browseContent").append('<img style="display: none;" class="HLpageBeaconImg page_SEARCH" src="' + window.FreshDirect.browse.data.adProducts.pageBeacon + 'all&random=' + new Date().getTime() + '" />');
					} else {
						//not ALL hlprods
						$(".browse-sections-top .browseContent").append('<img style="display: none;" class="HLpageBeaconImg page_SEARCH" src="' + window.FreshDirect.browse.data.adProducts.pageBeacon + hlSkusStr + '&random=' + new Date().getTime() + '" />');
					}
					
				} else if (hlSkusStr === '' && $(".browse-sections-top .browseContent .HLpageBeaconImg.page_SEARCH").length === 0) { /* only one instance at a time */
					//no hlprods
					$(".browse-sections-top .browseContent").append('<img style="display: none;" class="HLpageBeaconImg page_SEARCH" src="' + window.FreshDirect.browse.data.adProducts.pageBeacon + 'none&random=' + new Date().getTime() + '" />');
				}
			}
		} else {
			//prob NOT search
			/* this uses a marker class on the page beacon image to determine if it needs to be fired again,
			 * since it can be on any page, not just the first one. */
			
			for (var cur in FreshDirect.browse.data.adProducts.hlSelectionOfProductList) {
				//get sku code in HL items for cat
				var hlSkus = [], hlSkusStr = '';
				
				$('.sectionContent[data-section-catid="'+cur+'"]>ul [data-hooklogic-beacon-impress]').each(function(i,e) {
					//find sku and hold it
					hlSkus.push($(this).find('[data-productdata-name="skuCode"]').val());
					//fire impression
					if ($('.browseContent .HLpageBeaconImg.impress-page_'+activePage+'_id_'+cur+'_'+$(this).attr('id')).length === 0) {
						$('.browseContent').append('<img style="display: none;" class="HLpageBeaconImg impress-page_'+activePage+'_id_'+cur+'_'+$(this).attr('id')+'" src="' + $(this).attr('data-hooklogic-beacon-impress') + '&random=' + new Date().getTime() + '" />');
					}
				});
				
				//now, page beacon
				hlSkusStr = hlSkus.join(',');
				if (window.FreshDirect.browse.data.adProducts.hlSelectionsPageBeacons.hasOwnProperty(cur)) { /* this is required for the src url */
					if (hlSkusStr !== '' && $('.browseContent .HLpageBeaconImg.page_'+activePage+'_id_'+cur).length === 0) {
						if (FreshDirect.browse.data.adProducts.hlSelectionOfProductList[cur].length === FreshDirect.browse.data.adProducts.hlCatProductsCount[cur]) {
							//all hlprods
							$(".browseContent").append('<img style="display: none;" class="HLpageBeaconImg page_'+activePage+'_id_'+cur+'" src="' + window.FreshDirect.browse.data.adProducts.hlSelectionsPageBeacons[cur] + 'all&random=' + new Date().getTime() + '" />');
						} else {
							//not ALL hlprods
							$(".browseContent").append('<img style="display: none;" class="HLpageBeaconImg page_'+activePage+'_id_'+cur+'" src="' + window.FreshDirect.browse.data.adProducts.hlSelectionsPageBeacons[cur] + hlSkusStr + '&random=' + new Date().getTime() + '" />');
						}
					} else if (hlSkusStr === '' && $('.browseContent .HLpageBeaconImg.page_'+activePage+'_id_'+cur).length === 0) {
						//no hlprods
						$(".browseContent").append('<img style="display: none;" class="HLpageBeaconImg page_'+activePage+'_id_'+cur+'" src="' + window.FreshDirect.browse.data.adProducts.hlSelectionsPageBeacons[cur] + 'none&random=' + new Date().getTime() + '" />');
					}
				}
			}
		}
		
		/* every ajax load, defined in mobileweb_common.js */
		if (FreshDirect && FreshDirect.mobWeb && window['hlClickHandler']) {
			window['hlClickHandler']();
		}
	}//end adProductSection.fixThoseHooklogicDisplayHeights
	
	if(window.srch){
		var topSections = Object.create(WIDGET,{
			signal:{
				value:'sections'
			},
			template:{
				value:srch.topContent
			},
			placeholder:{
				value:'.browse-sections-top'
			}
		});

		var bottomSections = Object.create(WIDGET,{
			signal:{
				value:'sections'
			},
			template:{
				value:srch.bottomContent
			},
			placeholder:{
				value:'.browse-sections-bottom'
			}
		});
    

		topSections.listen();
		bottomSections.listen();
    
		//new for HookLogic
		adProductSection.listen();
    
	}//end if(window.srch)
	

	//needed?
	adProductSection.fixThoseHooklogicDisplayHeights();
	
	window.isHLchangable = false;

	sections.listen();
	superSections.listen();

	$(document).on('click', '.browse-sections [data-component="categorylink"]', sections.handleClick.bind(sections));
	$(document).on('click', '.superDepartment [data-component="categorylink"]', superSections.handleClick.bind(superSections));

	// page button change
	$(document).on('page-change', function(x){
		window.isHLchangable = true;
		
		
		//class="pagination-pager-button cssbutton green transparent"
	});
  
	//fires upon using search box
	//$(document).on('change', function(){
	$(".tabs li span, .searchbutton, .menuBox li span, .menupopup li span, .sorter button span, .pagination-showall-cssbutton").click(function(){
		window.isHLchangable = true;
	});
	
	/*window.onpopstate = function() {
		alert("pop!");
		
		window.isHLchangable = true;
	}*/
	
	$(window).on('popstate', function(event) {
		//alert("pop");
		
		window.isHLchangable = true;
		
		//change it here for the back button
		adProductSection.fixThoseHooklogicDisplayHeights();
	});

	//this always fires upon each set of products load success
	$( document ).ajaxSuccess(function(event, xhr, settings) {
		//update display data
		if (xhr && xhr.hasOwnProperty('responseJSON') && $.isPlainObject(xhr.responseJSON)) {
			$.extend(window.FreshDirect.browse.data, xhr.responseJSON);
		}
		
		//update the page beacon url
		/*if( xhr.responseJSON !== undefined && xhr.responseJSON.adProducts !== undefined &&
		xhr.responseJSON.adProducts.pageBeacon !== undefined &&  xhr.responseJSON.adProducts.pageBeacon != null &&
		xhr.responseJSON.adProducts.pageBeacon.length > 3 ){
			window.FreshDirect.browse.data.adProducts.pageBeacon = xhr.responseJSON.adProducts.pageBeacon;
		}*/
		
		if( (adProductSection && adProductSection.hasOwnProperty('fixThoseHooklogicDisplayHeights')) && (window.isHLchangable == true || (settings.url.indexOf("/api/filter?data=") != -1 ) || (settings.url.indexOf("cartdata?change=") != -1 )) ){
			adProductSection.fixThoseHooklogicDisplayHeights();
		}
		
		window.isHLchangable = false;

		try {
			if (fd.browse && fd.browse.data && fd.browse.data.descriptiveContent) {
				var allClassNames = 'browse-breadcrumbs-category browse-breadcrumbs-subcategory browse-breadcrumbs-department';
				allClassNames += 'browse-pager-category browse-pager-subcategory browse-pager-department';
				var $placeholder = $('.browse-breadcrumbs');
				var $pager = $('.pager-holder.top');
				$placeholder.removeClass(allClassNames);
				$pager.removeClass(allClassNames);
				switch (fd.browse.data.descriptiveContent.navDepth) {
					case 'DEPARTMENT':
						$placeholder.addClass('browse-breadcrumbs-department');
						$pager.removeClass(allClassNames).addClass('browse-pager-department');
						break;
					case 'SUB_CATEGORY':
						$placeholder.addClass('browse-breadcrumbs-subcategory');
						$pager.removeClass(allClassNames).addClass('browse-pager-subcategory');
						/* flow in to category*/
					case 'CATEGORY':
						$placeholder.addClass('browse-breadcrumbs-category');
						$pager.addClass('browse-pager-category');
						break;
					default: 
				}
			}
		} catch (e) {
		}
	});

	fd.modules.common.utils.register("browse", "sections", sections, fd);
	
}(FreshDirect));
