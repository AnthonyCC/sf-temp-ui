/*global jQuery,quickshop*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;

  var defaultImage = $("img[data-thumbnails-main]"),
      defaultSrc = defaultImage.attr('src');

  //
  // - FUNCTIONS -
  //

  /**
  */
  function offsetEl(el, offsetX, offsetY){
  
    var pos = "-" + offsetX + "px -" + offsetY + "px";    
    
    // $("[data-thumbnails-hd]").css("background-position", pos);  
    $(el).css("margin", "-" + offsetY + "px 0px 0px -" + offsetX + "px");  
  }
  
  /**
  * Calculates offset for given x, y mouse coordinates
  */
  function getOffset(x, y, imgHeight, imgWidth, hdHeight, hdWidth, zoomCenter,
                    maxYoffset, maxXoffset){
    
    // get y offset
    var hdY = y * ( hdHeight / imgHeight ),
        hdOffsetY = hdY - zoomCenter,
        hdX = 0,
        hdOffsetX = 0;
    
    // normalize y offset
    if(hdOffsetY < 0){
      hdOffsetY = 0;
    }
    
    if( hdOffsetY > maxYoffset ){
      hdOffsetY = maxYoffset;
    }       

    // get x offset
    hdX = x * ( hdWidth / imgWidth );
    hdOffsetX = hdX - zoomCenter;
    
    // normalize x offset
    if( hdOffsetX < 0){
      hdOffsetX = 0;
    }
    
    if( hdOffsetX > maxXoffset ){
      hdOffsetX = maxXoffset;
    }
    
    return { 
      x : hdOffsetX,
      y : hdOffsetY
    };
  }

  /**
   * Replace img src on given node
   */
  var replaceImage = function(node, url){
    if(url && url.substr && url.substr.call){
      $(node).attr('src', url);
    }
  };

  /**
   * Load HD images via Image beacons
   */
  function loadHdImages(){

    // selects thumbnails and main image too (if they have large-images)
    $(".pdp img[data-large-url]").each(function(index, el){

        var largeUrl = $(this).attr('data-large-url');

        if(largeUrl && largeUrl.length > 0){
          var beacon = new Image();
          beacon.src = $(this).attr('data-large-url');

          beacon.onload = function(){
              // put it in the container
              $("[data-thumbnails-hd]").append($(this).attr("data-hd-image", ""));
          };
        }
    });
  }

  //
  // - EVENT HANDLERS -
  //
  var ongoing = null;
  var DELAY = 1000;

  $(document).on('mouseover','[data-thumbnails-trigger]',function(event){
    var $target = $(event.currentTarget);
    clearTimeout(ongoing);
    ongoing = setTimeout(function () {
      ongoing = null;
      fd.pdp.replaceImage(defaultImage[0], $target.attr('src'));
    }, DELAY);
  });

  $(document).on('click','[data-thumbnails-trigger]',function(event){
    var $target = $(event.currentTarget);

    clearTimeout(ongoing);
    ongoing = null;

    fd.pdp.replaceImage(defaultImage[0], $target.attr('src'));
    defaultSrc = $target.attr('src');
    defaultImage.attr('data-large-url', $target.attr('data-large-url') || '' );
  });

  $(document).on('mouseleave','[data-thumbnails]',function(event){
    clearTimeout(ongoing);
    ongoing = null;
    fd.pdp.replaceImage(defaultImage[0], defaultSrc);
  });

  
  $('[data-thumbnails-main]').on('mouseleave', function(event){

    clearTimeout(ongoing);
    ongoing = null;

    // zoom window hide
    $("[data-thumbnails-hd]").hide();

    // reset margins and displayed image
    $("[data-thumbnails-hd] img").each(function(){  
        $(this).attr('style', ''); 
        $(this).hide();
    });
  });
  
  var doZoom = function ($img, $hdImg, targetBox) {
    // get offset
    var imgWidth = $img.width(),
        imgHeight = $img.height(),
        hdWidth = $hdImg.width(),
        hdHeight = $hdImg.height(),
        zoomBoxWidthHeight = 400,
        maxYoffset = hdHeight - zoomBoxWidthHeight,
        maxXoffset = hdWidth - zoomBoxWidthHeight,
        zoomCenter = zoomBoxWidthHeight / 2,
        offsetX = $img.data('mouseX') - targetBox.left,
        offsetY = $img.data('mouseY') - targetBox.top;

    var offset = getOffset(offsetX, offsetY, 
      imgHeight, imgWidth, hdHeight, hdWidth,
      zoomCenter, maxYoffset, maxXoffset);

    // move bg with HD offsets
    offsetEl($hdImg, offset.x, offset.y);
  };

  $('[data-thumbnails-main]').on('mousemove', function(event){

      // calculate which hd image to show
      var $img = $(this),
          largeUrl = $img.attr('data-large-url'),
          hdImg,
          targetBox = event.currentTarget.getBoundingClientRect();

      if(!largeUrl){
        return;
      }

      $img.data('mouseX', event.clientX);
      $img.data('mouseY', event.clientY);

      // find hd img in container (but just ONE)
      hdImg = $("[data-thumbnails-hd]").find("img[src='" + largeUrl + "']:first");
      hdImg.show();

      doZoom($img, hdImg, targetBox);

      // show zoom window
      if(!$("[data-thumbnails-hd]").is(":visible") && !ongoing){
        ongoing = setTimeout(function () {
          ongoing = null;
          $("[data-thumbnails-hd]").show();

          doZoom($img, hdImg, targetBox);
        }, DELAY);
      } 
  });

  $('[data-thumbnails-main]').on('click', function(event){
    var largeUrl = $(this).attr('data-large-url');

    if(largeUrl && !$("[data-thumbnails-hd]").is(":visible")){
      clearTimeout(ongoing);
      ongoing = null;
      $("[data-thumbnails-hd]").show();

      doZoom($(this), $("[data-thumbnails-hd]").find("img[src='" + largeUrl + "']:first"), event.currentTarget.getBoundingClientRect());
    } 
  });

  loadHdImages();

  fd.modules.common.utils.register("pdp", "replaceImage", replaceImage, fd);
  fd.modules.common.utils.register("pdp", "loadHdImages", loadHdImages, fd);
}(FreshDirect));
