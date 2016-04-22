/*global jQuery,browse,srch*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var WIDGET = fd.modules.common.widget;

  var sections = Object.create(WIDGET,{
    signal:{
      value:'sections'
    },
    template:{
      value:browse.content
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

  if (window.srch) {
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
		var hookLogicRowLimit = 4;
		var modIndex = 0;
		
		//var HLselector = ".browse-adproducts.isHookLogic-true ul.products.transactional";
		var HLprodSelector = ".browse-adproducts.isHookLogic-true .portrait-item.browseTransactionalProduct";
		
		//var prodSelector = ".sectionContent ul.products.transactional";
		//var prodSelector = ".isHookLogic-false portrait-item.browseTransactionalProduct.lastInLine";
		var prodSelector = ".isHookLogic-false .lastInLine";
		var prodSelectorIdArr = [];
		
		//var fakeClassPrefix = "fakeRow_";
		
		//make an array of last in line of each conventional row
		if( $(prodSelector).length > 0 ){
			$(prodSelector).each(function( index ) {
				prodSelectorIdArr.push( $(this).attr("id") );
			});
		}
		
		console.log( "prodSelectorIdArr = ", prodSelectorIdArr );
		
		if( $(HLprodSelector).length > 0 ){
			$(HLprodSelector).each(function( index ) {
				/*var hlItemHeight = $(this).height();
				var regularRowHeight = $( prodSelector + ":nth-of-type("+ index +")" ).height();
				
				if( hlItemHeight < regularRowHeight ){
					$(this).css("min-height", regularRowHeight );
				}else{
					$( prodSelector + ":nth-of-type("+ index +")" ).css("min-height", hlItemHeight );
				}
				*/
				
				
				//hide the hooklogic product if it is sixth or greater or if it is beyond page 1
				if(index > hookLogicRowLimit || ($(".pagination-pager-button.green.selected").attr("data-page") != "1") ){
					$(this).hide();
				}else{
					$(this).show();
					
					//first, get the rowclass of this hooklogic product
					var el = $(this);//get the element whose class value has to be extracted
					var fr_val = el.attr('class').match(/\bfakeRow_(\d+)\b/)[1];
					
					console.log("frau fr_val = " + fr_val);
					
					//correct the classnames of regular product rows
					//$(".browse-sections-top ul.products.transactional .browseTransactionalProduct.fakeRows:nth-of-type(3)")
					
					console.log( "(fr_val * 3) = " + (fr_val * 3) );
					
					/*$(".browse-sections-top ul.products.transactional .browseTransactionalProduct.fakeRows").each(function(index2){
						if( index2 < (index * 3)){
							return true;
						}
						
						var prodClassAttr = $(this).attr("class");
						
						console.log('$(this).attr("class") = ' + $(this).attr("class"));
						
						$(this).attr("class", prodClassAttr.replace(/fakeRow_(\d+)/g, "zzzzzzfakeRow_"+fr_val) );
						
						console.log('$(this).attr("class") = ' + $(this).attr("class"));
						
						if( index2 >= ((index * 3) + 3) ){
							return false;
						}
					})*/
				}
				
				

				
				

				
				/*
				if(index > hookLogicRowLimit ){
					$(this).hide();
				}else{
					var sendbefore = "#" + $(prodSelector)[index].id;
					
					$(this).addClass("isHookLogic-true").removeClass("lastInLine");
					
					//$(this).insertAfter( $(sendbefore) );
				}*/
				
				//$(this).find("portrait-item").appendTo(  $( ".sectionContent ul.products.transactional:nth-of-type("+ index +")" )  );
			});
		}
		
		/*height fixes*/
		$(".fakeRows").each(function(){
			
		})
		
		if( $(".pagination-pager-button.green.selected").attr("data-page") != "1" ){
			$(".browseContent .sectionContent ul.products").addClass("page2plus");
		}else{
			$(".browseContent .sectionContent ul.products").removeClass("page2plus");
		}
		
		//if( $.contains( $(".isHookLogic-false .browse-sections-top .products.transactional"), $(".isHookLogic-true") ) == false ){
		if( $.contains( $(".isHookLogic-false"), $(".isHookLogic-true") ) == false ){
			$(".isHookLogic-true").clone().prependTo( $(".isHookLogic-false .browse-sections-top .products.transactional") );
			
			console.log("some people call me a joeyjowjow");
		}
	}

    topSections.listen();
    bottomSections.listen();
    
    adProductSection.listen();
    
	//adProductSection.fixThoseHooklogicDisplayHeights();
  }

  sections.listen();
  superSections.listen();

  $(document).on('click', '.browse-sections [data-component="categorylink"]', sections.handleClick.bind(sections));
  $(document).on('click', '.superDepartment [data-component="categorylink"]', superSections.handleClick.bind(superSections));
  
  /*$(document).on('page-change', function(){
	  console.log("i'm testy");
	  
	  adProductSection.fixThoseHooklogicDisplayHeights();
  });
  
  $(document).on('change', function(){
	  console.log("i'm righty");
	  
	  adProductSection.fixThoseHooklogicDisplayHeights();
  });*/
  
	$( document ).ajaxComplete(function() {
		adProductSection.fixThoseHooklogicDisplayHeights();
	});

  fd.modules.common.utils.register("browse", "sections", sections, fd);
}(FreshDirect));
