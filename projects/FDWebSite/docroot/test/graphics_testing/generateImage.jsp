<%@ page 
		contentType="image/jpg" 
		import='java.net.URL, java.awt.*, java.awt.event.*, java.awt.image.*, java.io.*, javax.imageio.*, com.sun.image.codec.jpeg.*, com.freshdirect.fdstore.content.ProductModel, com.freshdirect.fdstore.content.*, com.freshdirect.webapp.util.*, com.freshdirect.framework.util.log.LoggerFactory, org.apache.log4j.Category, com.freshdirect.fdstore.content.ContentFactory, com.freshdirect.cms.ContentKey, com.freshdirect.framework.util.NVL'
%><% 

/* Make sure there's no wayward returns or spaces outside of code brackets, or the image could be corrupted. */

/*
 *	Query param settable values:
 *
 *		iW : image width
 *		iH : image height
 *		iBG : image background color (hex code as FFF or FFFFFF)
 *		iQ : image quality (float, default set at 1.0)
 *
 *		pId : product ID
 *		pSize : product image size code-letter (cx, p, c, cr, z, etc)
 *		pOx : product offset x
 *		pOy : product offset y
 *
 *		bSize : burst size (sm or lg. no value is also valid for no burst)
 *		bType : burst type (deal, fave, new, X) Using X forces no burst to be used.
 *		bVal : burst value (manually set deal amout to show, otherwise fetches from product)
 *		bOx : burst offset x
 *		bOy : burst offset y
 *
 *	The image is created like:
 *		background
 *		product
 *		overlay
 *
 *		So any offsets are always against the BACKGROUND layer.
 */

%><%!
	static Category LOGGER = LoggerFactory.getInstance("genImg");

	void log (boolean debug, String strToPrint) {
		if (debug) { LOGGER.debug(strToPrint); }
	}
%><%
boolean myDebug = true;
log(myDebug, "Starting generateImage (test)...");

BufferedImage prodImage = null;
BufferedImage overlay = null;
BufferedImage background = null;

/* make sure urls are FULL urls (http://host:port/path/file.ext) */
String baseUrl = "";
String schemeUrl = request.getScheme();
String serverName = request.getServerName();
String serverPort = ( !"".equals(Integer.toString(request.getServerPort())) )?":"+Integer.toString(request.getServerPort()):"";

baseUrl = schemeUrl+"://"+serverName+serverPort;

URL overlayUrl = null;
URL prodImgUrl = new URL(baseUrl+"/media/images/temp/soon_80x80.gif"); //default, including if pSize is unavailable

//deals - /media_stat/images/deals/brst_sm_" + deal + (supportsPNG ? ".png" : ".gif");
//fave - src=\"/media_stat/images/bursts/brst_sm_fave"+(supportsPNG ? ".png" : ".gif")+"\" width=\"35px\" height=\"35px\
//new - /media_stat/images/bursts/brst_sm_new"+(supportsPNG ? ".png" : ".gif")+"\" width=\"35px\" height=\"35px\

//defaults
	int prodWidth = 0;
	int prodHeight = 0;
	String pSize = "c"; //this is the code-letter (cx,p,c,ct,cr,z, etc)
	String pSizeTemp = "";
	int pOx = 0;
	int pOy = 0;

	String bType = "deal";
	String bSize = "sm";
	String bVal = "-1";
	int bValTemp = 0;
	String id = "";
	int overlayWidth = 0;
	int overlayHeight = 0;
	int bOx = 0;
	int bOy = 0;

	int retImageWidth = 0;
	int retImageHeight = 0;
	float iQ = 1;
	String bgColorString = "";
	int bgR = 255;
	int bgG = 255;
	int bgB = 255;

	Color bgColor = new Color(bgR, bgG, bgB); //default to white


//check for a passed iQ
if (request.getParameter("iQ") != null && request.getParameter("iQ") != "") {
	iQ = Float.valueOf(request.getParameter("iQ")).floatValue();
}
//check for a passed retImageWidth
if (request.getParameter("iW") != null && request.getParameter("iW") != "") {
	retImageWidth = Integer.valueOf(request.getParameter("iW")).intValue();
}
//check for a passed retImageHeight
if (request.getParameter("iH") != null && request.getParameter("iH") != "") {
	retImageHeight = Integer.valueOf(request.getParameter("iH")).intValue();
}
//check for a passed bgColor
if (request.getParameter("iBG") != null && request.getParameter("iBG") != "") {
	//parse bgColor into rgb

	bgColorString = request.getParameter("iBG");

	if (bgColorString.length() == 3) {
		bgR = Integer.valueOf(bgColorString.substring(0,1)+bgColorString.substring(0,1), 16).intValue();
		bgG = Integer.valueOf(bgColorString.substring(1,2)+bgColorString.substring(1,2), 16).intValue();
		bgB = Integer.valueOf(bgColorString.substring(2,3)+bgColorString.substring(2,3), 16).intValue();
	}else if (bgColorString.length() == 6) {
		bgR = Integer.valueOf(bgColorString.substring(0,2), 16).intValue();
		bgG = Integer.valueOf(bgColorString.substring(2,4), 16).intValue();
		bgB = Integer.valueOf(bgColorString.substring(4,6), 16).intValue();
	}
	
	bgColor = new Color(bgR, bgG, bgB);

}
//check for a passed bOx
if (request.getParameter("bOx") != null && request.getParameter("bOx") != "") {
	bOx = Integer.valueOf(request.getParameter("bOx")).intValue();
}
//check for a passed bOy
if (request.getParameter("bOy") != null && request.getParameter("bOy") != "") {
	bOy = Integer.valueOf(request.getParameter("bOy")).intValue();
}
//check for a passed pOx
if (request.getParameter("pOx") != null && request.getParameter("pOx") != "") {
	pOx = Integer.valueOf(request.getParameter("pOx")).intValue();
}
//check for a passed pOy
if (request.getParameter("pOy") != null && request.getParameter("pOy") != "") {
	pOy = Integer.valueOf(request.getParameter("pOy")).intValue();
}

//check for a passed pId
if (request.getParameter("pId") != null && request.getParameter("pId") != "") {
	
	//hard-coding as a product here
	id = "Product:"+request.getParameter("pId");

	//get product model
	ProductModel product = (ProductModel) ContentFactory.getInstance().getContentNodeByKey(ContentKey.decode(id));

	//check if product model is null
	if (product != null) {
		//check for pSize
		if (request.getParameter("pSize") != null && request.getParameter("pSize") != "") {
			pSizeTemp = request.getParameter("pSize");
			if ("a".equals(pSizeTemp) || "c".equals(pSizeTemp) || "cr".equals(pSizeTemp) || "ct".equals(pSizeTemp) || "cx".equals(pSizeTemp) || "p".equals(pSizeTemp) || "f".equals(pSizeTemp) || "z".equals(pSizeTemp)) {
				pSize = pSizeTemp;
			}
		}
/*
		log(myDebug, "pSize : "+pSize);
		log(myDebug, "baseUrl+product.getAlternateImage().getPath() : "+baseUrl+product.getAlternateImage().getPath());
		log(myDebug, "pSize2 : "+pSize);
		log(myDebug, "baseUrl+product.getProdImage().getPath() : "+baseUrl+product.getProdImage().getPath());
		log(myDebug, "pSize3 : "+pSize);
		log(myDebug, "baseUrl+product.getCategoryImage().getPath() : "+baseUrl+product.getCategoryImage().getPath());
		log(myDebug, "pSize4 : "+pSize);
		log(myDebug, "baseUrl+product.getConfirmImage().getPath() : "+baseUrl+product.getConfirmImage().getPath());
		log(myDebug, "pSize5 : "+pSize);
		log(myDebug, "baseUrl+product.getDetailImage().getPath() : "+baseUrl+product.getDetailImage().getPath());
		log(myDebug, "pSize6 : "+pSize);
		log(myDebug, "baseUrl+product.getThumbnailImage().getPath() : "+baseUrl+product.getThumbnailImage().getPath());
		log(myDebug, "pSize7 : "+pSize);
		log(myDebug, "baseUrl+product.getZoomImage().getPath() : "+baseUrl+product.getZoomImage().getPath());
		log(myDebug, "pSize8 : "+pSize);
		log(myDebug, "baseUrl+product.getRolloverImage().getPath() : "+baseUrl+product.getRolloverImage().getPath());
		log(myDebug, "pSize9 : "+pSize);
*/
		//check that we have a product image and path
		if ("a".equals(pSize)) {
			if (product.getAlternateImage() != null && product.getAlternateImage().getPath() != "") {
				prodImgUrl = new URL(baseUrl+product.getAlternateImage().getPath());
			}
		}else if ("c".equals(pSize)) {
			if (product.getProdImage() != null && product.getProdImage().getPath() != "") {
				prodImgUrl = new URL(baseUrl+product.getProdImage().getPath());
			}
		}else if ("ct".equals(pSize)) {
			if (product.getCategoryImage() != null && product.getCategoryImage().getPath() != "") {
				prodImgUrl = new URL(baseUrl+product.getCategoryImage().getPath());
			}
		}else if ("cr".equals(pSize)) {
			if (product.getRolloverImage() != null && product.getRolloverImage().getPath() != "") {
				prodImgUrl = new URL(baseUrl+product.getRolloverImage().getPath());
			}
		}else if ("cx".equals(pSize)) {
			if (product.getConfirmImage() != null && product.getConfirmImage().getPath() != "") { 
				prodImgUrl = new URL(baseUrl+product.getConfirmImage().getPath());
			}
		}else if ("p".equals(pSize)) {
			if (product.getDetailImage() != null && product.getDetailImage().getPath() != "") {
				prodImgUrl = new URL(baseUrl+product.getDetailImage().getPath());
			}
		}else if ("f".equals(pSize)) {
			if (product.getThumbnailImage() != null && product.getThumbnailImage().getPath() != "") {
				prodImgUrl = new URL(baseUrl+product.getThumbnailImage().getPath());
			}
		}else if ("z".equals(pSize)) {
			if (product.getZoomImage() != null && product.getZoomImage().getPath() != "") {
				prodImgUrl = new URL(baseUrl+product.getZoomImage().getPath());
			}
		}
			
		//check for passed bSize
		if (request.getParameter("bSize") != null) {
			//see if it's one of our two options
			if ( "sm".equalsIgnoreCase(request.getParameter("bSize")) ) { bSize = "sm"; }
			if ( "lg".equalsIgnoreCase(request.getParameter("bSize")) ) { bSize = "lg"; }
			if ( "".equalsIgnoreCase(request.getParameter("bSize")) ) { bSize = ""; }
		}else{
			//if a value is not passed, try to guess which to use
			if ("p".equals(pSize)) {
				bSize = "lg";
			}
		}

		
		//check for passed bVal
		if (request.getParameter("bVal") != null && request.getParameter("bVal") != "") { bVal = request.getParameter("bVal"); }

		//check for passed bType
		if (request.getParameter("bType") != null) { 
			if ( "deal".equalsIgnoreCase(request.getParameter("bType")) ) {
				if ("".trim().equals(bVal)) { bVal = "-1"; }
				//check for no bVal
				if (bVal=="-1") {
					//try to get product's deal amount
					bValTemp = product.getHighestDealPercentage();
					if (bValTemp>=0) {
						overlayUrl = new URL(baseUrl+"/media_stat/images/deals/brst_"+bSize+"_"+bValTemp+".gif");
					}
				}else{
					overlayUrl = new URL(baseUrl+"/media_stat/images/deals/brst_"+bSize+"_"+bVal+".gif");
				}
			}else if ( "fave".equalsIgnoreCase(request.getParameter("bType")) ) {
				overlayUrl = new URL(baseUrl+"/media_stat/images/bursts/brst_"+bSize+"_fave.gif");
			}else if ( "new".equalsIgnoreCase(request.getParameter("bType")) ) {
				overlayUrl = new URL(baseUrl+"/media_stat/images/bursts/brst_"+bSize+"_new.gif");
			}else if( "X".equalsIgnoreCase(request.getParameter("bType")) ) {
				//force no burst
				overlayUrl = null;
			}
		}else{
			//try to get product's deal amount
			bValTemp = product.getHighestDealPercentage();
			if (bValTemp>=0) {
				overlayUrl = new URL(baseUrl+"/media_stat/images/deals/brst_"+bSize+"_"+bValTemp+".gif");
			}
		}

		//if a value is not passed for bSize, eliminate for pSize == "z"
		if ("z".equals(pSize) && (bSize == null || "".equals(bSize))) { overlayUrl = null; }

		//all these qualify for no burst
		if (
			//no size
			(bSize == null || "".equals(bSize)) ||
			//type == deal, no value
			("deal".equalsIgnoreCase(request.getParameter("bType")) && ("-1".equals(bVal) && bValTemp <= 0))
		) { overlayUrl = null; }

	}

}

//we should always have something here
try {
	prodImage = ImageIO.read(prodImgUrl);
	
	prodWidth = prodImage.getWidth(null);
	prodHeight = prodImage.getHeight(null);

	//check to see if we're making a larger image than we have images for
	//if (retImageWidth > 0 || retImageHeight > 0) {
		//check passed values vs product values
		if (retImageWidth <= 0) { retImageWidth = prodWidth; }
		if (retImageHeight <= 0) { retImageHeight = prodHeight; }

		//we are, create a new one
		background = new BufferedImage(retImageWidth, retImageHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D BGg = (Graphics2D)background.createGraphics();

		//check here for a color
		BGg.setBackground(bgColor);

		//actually paint
		BGg.clearRect(0, 0, retImageWidth, retImageHeight);

		BGg.drawImage(prodImage, pOx, pOy, null);

		
	//}else{
		//background = prodImage;
	//}

	if (overlayUrl != null) {
		/* if we're reading a URL, don't use new File() */
		overlay = ImageIO.read(overlayUrl);

		overlayWidth = overlay.getWidth(null);
		overlayHeight = overlay.getHeight(null);

		Graphics2D g = (Graphics2D)background.createGraphics();
		g.drawImage(overlay, bOx, bOy, null);
	}
}catch(Exception e){
	log(myDebug, "Got an Exception: " + e.getMessage());
}


//and the actual output
	JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(response.getOutputStream());
	JPEGEncodeParam jep = encoder.getDefaultJPEGEncodeParam(background);
	jep.setQuality(iQ, (boolean)true);
	encoder.encode(background, jep);
	response.getOutputStream().close();

/* Print debug messages */
	log(myDebug, "baseUrl : "+baseUrl);

	log(myDebug, "retImageWidth : "+retImageWidth);
	log(myDebug, "retImageHeight : "+retImageHeight);
	log(myDebug, "bgColorString : "+bgColorString);
	log(myDebug, "colors : "+bgR+", "+bgG+", "+bgB);
	log(myDebug, "iQ : "+iQ);

	log(myDebug, "prodImgUrl : "+prodImgUrl);
		log(myDebug, "prodWidth : "+prodWidth);
		log(myDebug, "prodHeight : "+prodHeight);

		log(myDebug, "pId : "+id);
		log(myDebug, "pSize : "+pSize);
		log(myDebug, "pOx : "+pOx);
		log(myDebug, "pOy : "+pOy);

	log(myDebug, "overlayUrl : "+overlayUrl);
		log(myDebug, "overlayWidth : "+overlayWidth);
		log(myDebug, "overlayHeight : "+overlayHeight);

		log(myDebug, "bType : "+bType);
		log(myDebug, "bSize : "+bSize);
		log(myDebug, "bVal : "+bVal);
		log(myDebug, "bValTemp : "+bValTemp);
		log(myDebug, "bOx : "+bOx);
		log(myDebug, "bOy : "+bOy);

log(myDebug, "Ending generateImage (test)...");

%>