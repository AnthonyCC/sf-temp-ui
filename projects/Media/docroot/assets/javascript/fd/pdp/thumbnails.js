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
    $("[data-thumbnails-trigger]").each(function(index, el){

        var largeUrl = $(this).attr('data-large-url');

        if(largeUrl && largeUrl.length > 0){
          var beacon = new Image();
          beacon.src = $(this).attr('data-large-url');

          beacon.onload = function() {
            // put it in the container
            $("[data-thumbnails-hd]").append($(this).attr("data-hd-image", ""));
          };
        }
    });
  }

  //
  // - EVENT HANDLERS -
  //
  $(document).on('mouseover','[data-thumbnails-trigger]',function(event){
      fd.pdp.replaceImage(defaultImage[0], $(event.currentTarget).attr('src'));
  });

  $(document).on('click','[data-thumbnails-trigger]',function(event){
      defaultSrc = $(event.currentTarget).attr('src');

      defaultImage.attr('data-large-url', $(event.currentTarget).attr('data-large-url') || '' );
  });

  $(document).on('mouseleave','[data-thumbnails]',function(event){
      fd.pdp.replaceImage(defaultImage[0], defaultSrc);
  });

  
  $('[data-thumbnails-main]').on('mouseleave', function(event){

    // zoom window hide
    $("[data-thumbnails-hd]").hide();

    // reset margins and displayed image
    $("[data-thumbnails-hd] img").each(function(){  
        $(this).attr('style', ''); 
        $(this).hide();
    });
  });
  
  $('[data-thumbnails-main]').on('mousemove', function(event){

      // calculate which hd image to show
      var largeUrl = $(this).attr('data-large-url'),
          hdImg,
          targetBox = event.currentTarget.getBoundingClientRect();

      if(!largeUrl){
        return;
      }

      // find hd img in container
      hdImg = $("[data-thumbnails-hd]").find("img[src='" + largeUrl + "']");
      hdImg.show();

      // get offset
      var imgWidth = $(this).width(),
          imgHeight = $(this).height(),
          hdWidth = hdImg.width(),
          hdHeight = hdImg.height(),
          zoomBoxWidthHeight = 400,
          maxYoffset = hdHeight - zoomBoxWidthHeight,
          maxXoffset = hdWidth - zoomBoxWidthHeight,
          zoomCenter = zoomBoxWidthHeight / 2,
          offsetX = event.clientX - targetBox.left,
          offsetY = event.clientY - targetBox.top;

      var offset = getOffset(offsetX, offsetY, 
        imgHeight, imgWidth, hdHeight, hdWidth,
        zoomCenter, maxYoffset, maxXoffset);

      // move bg with HD offsets
      offsetEl(hdImg, offset.x, offset.y);

      // show zoom window
      if(!$("[data-thumbnails-hd]").is(":visible")){
        $("[data-thumbnails-hd]").show();
      } 
  });

  loadHdImages();

  fd.modules.common.utils.register("pdp", "replaceImage", replaceImage, fd);
  fd.modules.common.utils.register("pdp", "loadHdImages", loadHdImages, fd);
}(FreshDirect));
