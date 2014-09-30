/*global jQuery,common, FDModalDialog */
var FreshDirect = FreshDirect || {};


// module initialization
(function (fd) {

  var console = console || window.top.console || { log:function(){} };
  var $=fd.libs.$;

  var delayPopup = 250;
  var popupTimeout;
  var animFinished = false;
  var subDept = " ";
  var dept = " ";
  var $tRealign;
  var POPUPWIDGET = fd.modules.common.popupWidget;
	  $.fn.getHiddenDimensions = function(includeMargin) {
		  var $item = this,
		  	props = { position: 'absolute', visibility: 'hidden', display: 'block' },
	        dim = { width:0, height:0, innerWidth: 0, innerHeight: 0, outerWidth: 0, outerHeight: 0 },
	        $hiddenParents = $item.parents().andSelf().not(':visible'),
	        includeMargin = (includeMargin == null)? false : includeMargin;

	    var oldProps = [];
	    $hiddenParents.each(function() {
	        var old = {};

	        for ( var name in props ) {
	            old[ name ] = this.style[ name ];
	            this.style[ name ] = props[ name ];
	        }

	        oldProps.push(old);
	    });

	    dim.width = $item.width();
	    dim.outerWidth = $item.outerWidth(includeMargin);
	    dim.innerWidth = $item.innerWidth();
	    dim.height = $item.height();
	    dim.innerHeight = $item.innerHeight();
	    dim.outerHeight = $item.outerHeight(includeMargin);

	    $hiddenParents.each(function(i) {
	        var old = oldProps[i];
	        for ( var name in props ) {
	            this.style[ name ] = old[ name ];
	        }
	    });

	    return dim;
	}

  /**
   * This module used to build all Global Navigation popups
   *
   * There are two categories: first level and second level popups
   */

  var overlay = "[data-component='globalnav-overlay']"; // overlay under popup elements  will handle close cases
  var ghost = "[data-component='globalnav-ghost']"; // ghost is a copy of original topnav before the overlay
  var popup = "[data-component='globalnav-popup']"; // generated popup for depts and superdepts

  var topnav = "[data-component='globalnav-menu']"; // the whole nav element put to DOM from media

  var trigger = "[data-component='globalnav-item']"; // triggers department level opens
  var subTrigger = "[data-component='globalnav-submenu-item']"; // triggers department level opens in subdepartment

  var down = false; // handles first level animation reopens
  var subDown = false; // handles second level animation reopens

  /**
   * open second level departments with animations
   */
  function openSub(e){
    e.stopPropagation();

    var $t = $(e.currentTarget),
        $topnav = $(topnav),
        $popup = $(popup),
        $popupBody = $(popup).find(".globalnav-popup-content"),
        $content = $("[data-component='globalnav-popup-body'][data-id='"+ $t.data('id') +"']").children().first();
    
    $tRealign = $t;
        
    //closes popup when cursor is taken away on Sub Dept
    $(".globalnav-popup").mouseover(function(event) {
    	var target = $( event.target );
    	if(target.hasClass("globalnav-popup")){ close(e); }
    });
    
    // check in order to not reload function if hovered over elements inside same department
    if($t.find(".arrow-down").is(":visible") && $t.find("a").html()==subDept){ return; }    
    subDept = $t.find("a").html();
    
    
    animFinished = false;

	if ($content.is(':animated')) { return; }

    removeSubItemHighlights();
    // fancy: hide down arrows from superdepartment level menu items
    $popupBody.find(".arrow-down").hide();

    if($popupBody.length && $content.length){

      // remove previously shown departments
      //$popupBody.children(".animcontainer").remove();
      $popup.children(".deptcontainer").remove();

      // append dept content related to pointed sub dept
      //$popupBody = $popupBody.append("<div class='animcontainer'></div>").children('.animcontainer');
      $content.clone().appendTo($popup);
      $content = $popup.children(".deptcontainer");

      repos($t.closest('.subdepartments_cont'), null, $content, $t);

      // bring up popup content to animate later
      $content.css('top', '-' + ($content.outerHeight() + $popupBody.outerHeight()) + "px");
      $popup.css('margin-bottom', '-'+$content.outerHeight());

      // stop animations before
      $content.stop();
      //$("#globalnavpopup").removeClass("shadow-for-superdepart");

      $popupBody.css('overflow', 'visible');
      $content.css('margin', '');
      $content.css('margin-bottom', '3px');
      $popupBody.css('padding-bottom', '');

      // fancy: show related down arrow on supdept level item
      $(this).find(".arrow-down").show();

      tsubMouseEventBinder($t, $popupBody, $popup, $content);
      }

    // stick with hover color on supdept level item
    addSubItemHighlight($(this).find("a"));

  }

  /**
   * close globalnav popups et all
   */
  function close(e){
    e.stopPropagation();
    
    $(overlay).hide();
    $(ghost).hide();
    removeTopItemHighlights();

    $(popup).find(".globalnav-popup-content").html("");
    $(popup).find(".deptcontainer").remove();

    // init animation logic for both levels
    down = false;
    subDown = false;
    
  }

  /**
   * open first department level popup with animations
   */
  function open(e){
	  
    var $topnav = $(topnav),
        $ghost = $(ghost),
        $overlay = $(overlay),
        $popup = $(popup),
        $t = $(e.currentTarget),
        $ghostMenuItem = $ghost.find("[data-id='" + $t.data('id') + "']"),
        $popupBody = $popup.find(".globalnav-popup-content");
    
    $tRealign = $t;
    
    //if (!fd.modules.common.mouse.isSlow()) { return; }

    // check in order to not reload function if hovered over elements inside same department
    if($t.find("a").hasClass("opened") && $t.html()==dept){ return; }
    
    animFinished = false;

    // close all autocomplete popups
    $('[data-component="autocomplete"]').autocomplete("close");

    // close all select dropdowns
    $('select').blur();


    // precautions
    if(!$popupBody.length){ return; }
    if ($popupBody.is(':animated') && popupTimeout === null) { return; }

    // re-init subdown animation logic
    subDown = false;

    // fill popup with proper content
    $popupBody.html("");
    $popup.find(".deptcontainer").remove();
    $("[data-component='globalnav-popup-body'][data-id='"+ $t.data('id') +"']").children().first().clone().appendTo($popupBody);


    repos($topnav, $ghost, $popup, $t);

    removeTopItemHighlights();
    addTopItemHighlight($ghostMenuItem.find("a"));

    // show items
    $overlay.show();
    $ghost.show();
    $popupBody.css('overflow', 'hidden');
    $popupBody.css('padding-bottom', '3px');
    //$popupBody.css({'padding-bottom': '3px', 'margin-top': '-3px'});

    if($t.hasClass("top-item") || $t.hasClass("top-item-w-sub")){
    	//sets dept variable in order to check for repeat action
    	dept = $t.html();
    	
        // bring up popup content to animate later
        $popupBody.css("top", "-" + $popupBody.outerHeight() + "px");

        // stop animations before
        $popupBody.stop();
        //$("#globalnavpopup").removeClass("shadow-for-superdepart");

        tMouseEventBinder($t, $popupBody, $popup);
      }
  }
        
  function tsubMouseEvent_Over($popupBody, $content) {
	  (function($popupBody) {
	      clearTimeout(popupTimeout);
	      popupTimeout = setTimeout(function(){
	    	  if(subDown === false){
	    		  $content.animate({
		              top: "0px"
		          }, 250, "easeOutQuad", function(){
		          	//$("#globalnavpopup").addClass("shadow-for-superdepart");
		        	 animFinished = true; 
		        	 subDown = true;
		  	          $(".seasonal-media").fadeIn(200);
		            //$popupBody.css('overflow', 'visible');
		          });
	    	  } else {
        	//$("#globalnavpopup").addClass("shadow-for-superdepart");
	    		animFinished = true;
	    		$content.css('top', '0px');
          $(".seasonal-media").show();
	          }  
	      }, delayPopup); 
	  })($popupBody);
  }

  function tsubMouseEvent_Leave($popup) {
	  return function() {
		  if(!animFinished){
			  $popup.find(".deptcontainer").html("");
			  $popup.find(".arrow-down").hide();
		  };
		  clearTimeout(popupTimeout);
	  };
  }

  function tsubMouseEventBinder($t, $popupBody, $popup, $content) {
	  $t.bind({
          mouseenter: tsubMouseEvent_Over($popupBody, $content),
          mouseleave: tsubMouseEvent_Leave($popup)
      });
  }

  function tMouseEvent_Over($popupBody) {
	  (function($popupBody) {
	      clearTimeout(popupTimeout);
	      popupTimeout = setTimeout(function(){
	    	  if(down === false){
	    	  $popupBody.animate({
	              top: "0px"
		          }, 250, "easeOutQuad", function(){
	          	//$("#globalnavpopup").addClass("shadow-for-superdepart");
		        	 animFinished = true; 
	             down = true;
	            $(".seasonal-media").fadeIn(200);
	            //$popupBody.css('overflow', 'visible');
	          });
	    	  } else {
	          	//$("#globalnavpopup").addClass("shadow-for-superdepart");
	    		animFinished = true;
	            $popupBody.css('top', '0px');
	            $(".seasonal-media").show();
	          }  
	      }, delayPopup); 
	  })($popupBody);
  }

  function tMouseEvent_Leave($popup) {
	  return function() {
		  if(!animFinished){
			  $popup.find(".deptcontainer").remove();
			  $popup.find(".subdepartments_cont").remove();
		  };
		  clearTimeout(popupTimeout);
	  };
  }

  function tMouseEventBinder($t, $popupBody, $popup) {
	  $t.bind({
          mouseenter: tMouseEvent_Over($popupBody),
          mouseleave: tMouseEvent_Leave($popup)
      });
  }

  /**
  * appends globalnav popup elements to DOM
  */
  function render(){
    var $topnav = $(topnav),
        $ghost = $("<div class='popupcontentghost' data-component='globalnav-ghost'></div>"),
        $popup = $("<div data-component='globalnav-popup' id='globalnavpopup' class='globalnav-popup'><div class='globalnav-popup-content'></div></div>"),
        $overlay = $("<div class='popupcontentoverlay' data-component='globalnav-overlay'></div>");

    $(document.body).append($overlay);
    $(document.body).append($ghost);
    $(document.body).append($popup);

    // attach event handlers
    $overlay.on('mouseover', close); // attach works only this way in iPad/Safari
    $(document).on('mouseover', trigger, open);
    //$(document).on('mousemove', trigger, open);
    $(document).on('mouseover', subTrigger, openSub);
    $(window).on('resize', function(){ repos($topnav, $ghost, $popup, $tRealign); });

    // set init position
    repos($topnav, $ghost, $popup);

    // we'll show content ghost before the real topnav so we init it now
    $topnav.clone().appendTo($ghost);
  }

function getElemDim($elem, includeMargins){
	var includeMargins = includeMargins || false;
	var retDim = {
		width: 0,
		height: 0,
		offset: {
			top: 0,
			left: 0
		},
		center: 0
	};

	if ($elem !== undefined && $elem != null) {
		retDim.width = $elem.outerWidth(includeMargins);
		retDim.height = $elem.outerHeight(includeMargins);
		retDim.offset = $elem.offset();
		retDim.center = (retDim.width / 2) + retDim.offset.left;
	}

	return retDim;
}

/* adjust left/right positioning of popup elements and their children */
function realigner($topnav, $popup, $t) {
		
    $popup.css('left', ''); //reset popup so realign works properly

	var navDim = getElemDim($topnav);
	var tarDim = getElemDim($t, true);
	
	var $popupContRef = $('#globalnavpopup');
	var $imgContRef = $popupContRef.find('.heroimg_cont').first();
	var $imgRef = $popupContRef.find('.heroimg').first();
	var $deptContRef = $popupContRef.find('.department').first();
	var $subDeptsRef = $popupContRef.find('.subdepartments').first();
	var isRightOfNavCenter = false;
	var possibleOffset = -1;
	var topNavIsSubnav = $topnav.hasClass('subdepartments_cont');

	if (topNavIsSubnav) {
		$popupContRef = $popupContRef.find('.deptcontainer').first();
	}
	
	//check alignments
	if (tarDim.center < navDim.center) { //left of center
		//adjust img to right in contents
		/*if ($imgContRef.length !== 0) {
			$imgContRef.css('float', 'right');
		}*/
	} else { //right of center
		isRightOfNavCenter = true;
		//return $topnav.offset().left;
	}
	
	if ($deptContRef.length !== 0) { //dept popup
		var catColumns = $deptContRef.find('.dropdown-column'), catColumnsMaxWidth = 0, catColumnsSetMaxWidth = false;

		//adhere to min content >= 50% of globalnav
		if ($popupContRef.outerWidth() < (navDim.width/2)) {
			//figure out how wide to make deptCont
			var addToDeptContWidth = (navDim.width/2) - $popupContRef.outerWidth(true);
			if (addToDeptContWidth > 0) {
				$deptContRef.css('width', $deptContRef.outerWidth() + addToDeptContWidth);
			}
		} else if ($popupContRef.outerWidth() > navDim.width) { //also keep it inside maxWidth
			catColumnsSetMaxWidth = true;
			if ($imgContRef.length !== 0) {
				$deptContRef.css('width', navDim.width-$imgContRef.outerWidth(true));
			} else {
				$deptContRef.css('width', navDim.width);
			}
		}
		
		
		//add max catColumns widths
		if (catColumnsSetMaxWidth) {
			$(catColumns).each(function(i,e) {
				$(e).css('max-width',  Math.floor($deptContRef.outerWidth() / catColumns.length));
			});
		}
		
		//do this AFTER the columns are styled
		if ($imgContRef.length !== 0) { //img & dept
			//show more image if deptCont is taller (be sure to include seasonal media's height
			var seasonalMediaHeight = $deptContRef.find('.seasonal-media').outerHeight() || 0;
			if (($deptContRef.outerHeight() + seasonalMediaHeight) > $imgContRef.outerHeight()) {
				$imgContRef.css('height', ($deptContRef.outerHeight() + seasonalMediaHeight));
			}
		} else { //just dept
			
		}
		
		
		//now re-align the popup to the nav item if possible
		if (isRightOfNavCenter) {
			possibleOffset = (navDim.offset.left + navDim.width) - $popupContRef.outerWidth();			
		} else {
			possibleOffset = navDim.offset.left;
		}
		if (topNavIsSubnav) {
			possibleOffset -= navDim.offset.left;
		} 
		return possibleOffset;
	}

	if ($subDeptsRef.length !== 0 && !topNavIsSubnav) { //superdept, subdept popup (first level)
		realigner_superdept($topnav, tarDim, navDim, $subDeptsRef, isRightOfNavCenter);
	}

	return navDim.offset.left;
}

function realigner_superdept($topnav, tarDim, navDim, $subDeptsRef, isRightOfNavCenter) {
	$subDeptsRef.closest('.subdepartments_cont').css('width', $topnav.outerWidth());
	var subDeptsDim = getElemDim($subDeptsRef, true);
	var subDeptsDimNoSpacing = getElemDim($subDeptsRef);
	
	//see if we can center under selected nav item
	var possibleOffset = (tarDim.center - navDim.offset.left) - (subDeptsDim.width / 2);
	
	if (possibleOffset > navDim.width-subDeptsDim.width) {
		//no space
		if (isRightOfNavCenter) {
			possibleOffset = navDim.width - subDeptsDim.width;
		} else {
			possibleOffset = 0;
		}
	}
	
	$subDeptsRef.css('left', possibleOffset);
		
	//create gradient css
	var gradDim = {
		left: {
			active: false,
			start: 0,
			end: 0
		},
		right: {
			active: false,
			start: 0,
			end: 0
		}
	};

	var spaceLeft = false, spaceRight = false, rightGradientStartOffset = (subDeptsDim.width - subDeptsDimNoSpacing.width)/2;
	if (possibleOffset >= 60) { //space on left
		gradDim.left.active = true;
	}
	//calc left setup
	gradDim.left.start = Math.round(possibleOffset-60);
	gradDim.left.end = possibleOffset;
		
	var rightSpacing = navDim.width - (possibleOffset + subDeptsDimNoSpacing.width+rightGradientStartOffset);
	if (rightSpacing > 60) { //space on right
		gradDim.right.active = true;
	}
	
	//calc right setup
	gradDim.right.start = Math.floor(possibleOffset + subDeptsDimNoSpacing.width+rightGradientStartOffset);
	gradDim.right.end = possibleOffset + subDeptsDimNoSpacing.width+rightGradientStartOffset + 60;
		
	//offet to quick-fix gradient being off in IE sometimes (or zoomed in chrome)
	gradDim.left.start = gradDim.left.start + 7;
	gradDim.right.start = gradDim.right.start - 7;

	if (gradDim.left.active && gradDim.right.active) { //both sides
		$subDeptsRef.closest('.subdepartments_cont').css({
			"background-color": "rgba(237,237,237,1)",
			"background-image": "url('/media/images/navigation/global_nav/newglobal/SD_submenu_gradient_left.png'), url('/media/images/navigation/global_nav/newglobal/SD_submenu_gradient_right.png')",
			"background-position": ""+gradDim.left.start+"px 0, "+(gradDim.right.start)+"px 0",
			"background-repeat": "no-repeat, no-repeat"
		});
	} else if (gradDim.left.active && !gradDim.right.active) { //only left, right side is all white
		$subDeptsRef.closest('.subdepartments_cont').css({
			"background-color": "rgba(237,237,237,1)",
			"background-image": "url('/media/images/navigation/global_nav/newglobal/SD_submenu_gradient_left.png'), url('/media/images/navigation/global_nav/newglobal/SD_submenu_white.png')",
			"background-position": ""+gradDim.left.start+"px 0, "+(gradDim.right.start)+"px 0",
			"background-repeat": "no-repeat, no-repeat"
		});
	} else if (!gradDim.left.active && gradDim.right.active) { //only right, left side is all white
		$subDeptsRef.closest('.subdepartments_cont').css({
			"background-color": "rgba(237,237,237,1)",
			"background-image": "url(url('/media/images/navigation/global_nav/newglobal/SD_submenu_white.png', '/media/images/navigation/global_nav/newglobal/SD_submenu_gradient_right.png'))",
			"background-position": ""+gradDim.left.start+"px 0, "+(gradDim.right.start)+"px 0",
			"background-repeat": "no-repeat, no-repeat"
		});
	} else {
		//neither
	}
}

  /**
   * repositioning content ghost and popup according to topnav
   */
  function repos($topnav, $ghost, $popup, $t){
    if(!$topnav.length || !$popup.length){
      return;
    }

    var alignAt = realigner($topnav, $popup, $t);
    if (alignAt < 0) { alignAt = 0; } //safety

    if ($ghost !== null && $ghost.length) {
	    // cloning topnav menu to ghost
	    $ghost.css({
	        top: $topnav.offset().top,
	        left: $topnav.offset().left, //don't change this, ghost is on globalnav
	        height: '50px',
	        'z-index': '1002'
	    });
    }

    $popup.css({
        top: $topnav.offset().top + $topnav.height(),
        left: alignAt
    });
  }

  function removeTopItemHighlights(){
    $("[data-component='globalnav-item']").find("a.opened").removeClass("opened");
  }

  function addTopItemHighlight(elem){
    $(elem).addClass("opened");
  }

  function removeSubItemHighlights(){
    $(popup).find("a.subopened").removeClass("subopened");
  }

  function addSubItemHighlight(elem){
    $(elem).addClass("subopened");
  }

  function showTutorial() {
	  FreshDirect.globalnav.tutorialOverlay = doOverlayDialog('/media/layout/nav/globalnav/tutorial/slide_01.html');
  }

  // load up only if we find new global nav and trigger elements in DOM
  if($(topnav).length > 0 && $(trigger).length > 0){
    render();

    if (fd.globalnav.hasOwnProperty('showTutorial') && fd.globalnav.showTutorial === true) {
    	showTutorial();
    }
  }

}(FreshDirect));
