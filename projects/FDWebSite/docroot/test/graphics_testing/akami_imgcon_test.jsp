<%@ page import="java.util.*"%>
<%@ taglib uri='freshdirect' prefix='fd'%>
<fd:CheckLoginStatus guestAllowed='true' />
<%
	Map params = new HashMap();

	Enumeration keys = request.getParameterNames();
	while (keys.hasMoreElements()) {
		String key = (String) keys.nextElement();

		//To retrieve a single value  
		String value = request.getParameter(key);

		params.put(key, value);

		// If the same key has multiple values (check boxes)  
		String[] valueArray = request.getParameterValues(key);

		for (int i = 0; i > valueArray.length; i++) {
			params.put(key + valueArray[i], valueArray[i]);
		}
	}

	String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
	params.put("baseUrl", baseUrl);
%>
<!doctype html>
<html lang="en-US" xml:lang="en-US">
<head>
<title>Akami Image Converter Test Page</title>

<fd:javascript src="/assets/javascript/jquery/1.11.3/jquery.js" />
<fd:javascript
	src="/assets/javascript/jquery/ui/1.11.4/jquery-ui.min.js" />
<fd:css
	href="/assets/css/jquery/ui/1.11.3/themes/smoothness/jquery-ui.css" />

<style>
/* Eric Meyer's Reset CSS v2.0 - http://cssreset.com */
html, body, div, span, applet, object, iframe, h1, h2, h3, h4, h5, h6, p,
	blockquote, pre, a, abbr, acronym, address, big, cite, code, del, dfn,
	em, img, ins, kbd, q, s, samp, small, strike, strong, sub, sup, tt, var,
	b, u, i, center, dl, dt, dd, ol, ul, li, fieldset, form, label, legend,
	table, caption, tbody, tfoot, thead, tr, th, td, article, aside, canvas,
	details, embed, figure, figcaption, footer, header, hgroup, menu, nav,
	output, ruby, section, summary, time, mark, audio, video {
	border: 0;
	font-size: 100%;
	font: inherit;
	vertical-align: baseline;
	margin: 0;
	padding: 0
}

article, aside, details, figcaption, figure, footer, header, hgroup,
	menu, nav, section {
	display: block
}

body {
	line-height: 1
}

ol, ul {
	list-style: none
}

blockquote, q {
	quotes: none
}

blockquote:before, blockquote:after, q:before, q:after {
	content: none
}

table {
	border-collapse: collapse;
	border-spacing: 0
}

/* minicolors*/
.minicolors {
	position: relative
}



.minicolors-no-data-uris .minicolors-sprite {
	background-image: url(jquery.minicolors.png)
}

.minicolors-swatch {
	position: absolute;
	vertical-align: middle;
	background-position: -80px 0;
	border: 1px solid #ccc;
	cursor: text;
	padding: 0;
	margin: 0;
	display: inline-block
}

.minicolors-swatch-color {
	position: absolute;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0
}

.minicolors input[type=hidden]+.minicolors-swatch {
	width: 28px;
	position: static;
	cursor: pointer
}

.minicolors-panel {
	position: absolute;
	width: 173px;
	height: 152px;
	background: #fff;
	border: 1px solid #CCC;
	box-shadow: 0 0 20px rgba(0, 0, 0, .2);
	z-index: 99999;
	-moz-box-sizing: content-box;
	-webkit-box-sizing: content-box;
	box-sizing: content-box;
	display: none
}

.minicolors-panel.minicolors-visible {
	display: block
}

.minicolors-position-top .minicolors-panel {
	top: -154px
}

.minicolors-position-right .minicolors-panel {
	right: 0
}

.minicolors-position-bottom .minicolors-panel {
	top: auto
}

.minicolors-position-left .minicolors-panel {
	left: 0
}

.minicolors-with-opacity .minicolors-panel {
	width: 194px
}

.minicolors .minicolors-grid {
	position: absolute;
	top: 1px;
	left: 1px;
	width: 150px;
	height: 150px;
	background-position: -120px 0;
	cursor: crosshair
}

.minicolors .minicolors-grid-inner {
	position: absolute;
	top: 0;
	left: 0;
	width: 150px;
	height: 150px
}

.minicolors-slider-saturation .minicolors-grid {
	background-position: -420px 0
}

.minicolors-slider-saturation .minicolors-grid-inner {
	background-position: -270px 0;
	background-image: inherit
}

.minicolors-slider-brightness .minicolors-grid {
	background-position: -570px 0
}

.minicolors-slider-brightness .minicolors-grid-inner {
	background-color: #000
}

.minicolors-slider-wheel .minicolors-grid {
	background-position: -720px 0
}

.minicolors-opacity-slider, .minicolors-slider {
	position: absolute;
	top: 1px;
	left: 152px;
	width: 20px;
	height: 150px;
	background-color: #fff;
	background-position: 0 0;
	cursor: row-resize
}

.minicolors-slider-saturation .minicolors-slider {
	background-position: -60px 0
}

.minicolors-slider-brightness .minicolors-slider,
	.minicolors-slider-wheel .minicolors-slider {
	background-position: -20px 0
}

.minicolors-opacity-slider {
	left: 173px;
	background-position: -40px 0;
	display: none
}

.minicolors-with-opacity .minicolors-opacity-slider {
	display: block
}

.minicolors-grid .minicolors-picker {
	position: absolute;
	top: 70px;
	left: 70px;
	width: 12px;
	height: 12px;
	border: 1px solid #000;
	border-radius: 10px;
	margin-top: -6px;
	margin-left: -6px;
	background: 0 0
}

.minicolors-grid .minicolors-picker>div {
	position: absolute;
	top: 0;
	left: 0;
	width: 8px;
	height: 8px;
	border-radius: 8px;
	border: 2px solid #fff;
	-moz-box-sizing: content-box;
	-webkit-box-sizing: content-box;
	box-sizing: content-box
}

.minicolors-picker {
	position: absolute;
	top: 0;
	left: 0;
	width: 18px;
	height: 2px;
	background: #fff;
	border: 1px solid #000;
	margin-top: -2px;
	-moz-box-sizing: content-box;
	-webkit-box-sizing: content-box;
	box-sizing: content-box
}

.minicolors-inline {
	display: inline-block
}

.minicolors-inline .minicolors-input {
	display: none !important
}

.minicolors-inline .minicolors-panel {
	position: relative;
	top: auto;
	left: auto;
	box-shadow: none;
	z-index: auto;
	display: inline-block
}

.minicolors-theme-default .minicolors-swatch {
	top: 5px;
	left: 5px;
	width: 18px;
	height: 18px
}

.minicolors-theme-default.minicolors-position-right .minicolors-swatch {
	left: auto;
	right: 5px
}

.minicolors-theme-default.minicolors {
	width: auto;
	display: inline-block
}

.minicolors-theme-default 
			.minicolors-input {
	height: 20px;
	width: auto;
	display: inline-block;
	padding-left: 26px
}

.minicolors-theme-default.minicolors-position-right 
			.minicolors-input {
	padding-right: 26px;
	padding-left: inherit
}

.minicolors-theme-bootstrap 
			.minicolors-swatch {
	top: 3px;
	left: 3px;
	width: 28px;
	height: 28px;
	border-radius: 3px
}

.minicolors-theme-bootstrap .minicolors-swatch-color {
	border-radius: inherit
}

.minicolors-theme-bootstrap.minicolors-position-right .minicolors-swatch
	{
	left: auto;
	right: 3px
}

.minicolors-theme-bootstrap .minicolors-input {
	float: none;
	padding-left: 44px
}

.minicolors-theme-bootstrap.minicolors-position-right .minicolors-input
	{
	padding-right: 44px;
	padding-left: 12px
}

.minicolors-theme-bootstrap .minicolors-input.input-lg+.minicolors-swatch
	{
	top: 4px;
	left: 4px;
	width: 37px;
	height: 37px;
	border-radius: 5px
}

.minicolors-theme-bootstrap .minicolors-input.input-sm+.minicolors-swatch
	{
	width: 24px;
	height: 24px
}
</style>

<style>
body {
	font-size: 14px;
}

.origImages {
	height: 300px;
	overflow-y: scroll;
}

.origImages img {
	vertical-align: top;
	border: 1px dashed #ccc;
	margin: 3px 5px;
}

#akaForm_queue {
	position: relative;
}

#akaTitle {
	font-weight: bold;
	background-color: #ccc;
	padding: 6px;
}

#akaCont {
	border: 2px solid #ccc;
	min-height: 400px;
	overflow-y: scroll;
}

#akaForm {
	float: left;
	width: 900px;
}

#akaCmdAccordion {
	width: 250px;
}

#akaResultCont {
	float: right;
	min-width: 250px;
	min-height: 250px;
}

.akaResultTitle {
	text-align: center;
}

#akaResult {
	text-align: center;
}

#akaResult img {
	vertical-align: top;
	border: 1px dashed #ccc;
	margin: 3px 5px;
}

#cmdCont {
	margin: 2px 5px;
	width: 640px;
}

#cmdTestUrlCont {
	width: 100%;
}

.ajaxImageSizes .ui-state-default {
	color: #a00;
}

.ajaxImageSizes .ui-state-active {
	color: #0a0;
}

.akaTitle {
	padding: 0.2em 0.3em;
	border: 1px solid #aaaaaa;
	background: #cccccc
		url("https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.3/themes/smoothness/images/ui-bg_highlight-soft_75_cccccc_1x100.png")
		50% 50% repeat-x;
	color: #222222;
	font-weight: bold;
	line-height: 2em;
}

.ui-tooltip {
	padding: 10px 20px;
	color: #333;
	border-radius: 20px;
	font: bold 14px "Helvetica Neue", Sans-Serif;
	box-shadow: 0 0 7px black;
	max-width: 700px;
}

.akaCmdAdd, .akaCmdGroupHelp {
	position: absolute;
	top: 50%;
	right: 0;
	margin-top: -8px;
	cursor: pointer;
}

.portlet {
	margin: 0 1em 1em 0;
	padding: 0.3em;
	min-width: 250px;
}

.portlet-header {
	padding: 0.2em 0.3em;
	margin-bottom: 0.5em;
	position: relative;
	cursor: move;
}

.portlet-toggle {
	position: absolute;
	top: 50%;
	right: 0;
	margin-top: -8px;
	cursor: pointer;
}

.portlet-remove {
	position: absolute;
	top: 50%;
	right: 20px;
	margin-top: -8px;
	cursor: pointer;
}

.portlet-helpContent {
	display: none;
}

.portlet-helpIcon {
	position: absolute;
	top: 50%;
	right: 40px;
	margin-top: -8px;
	cursor: pointer;
}

.portlet-content {
	padding: 0.4em;
	text-align: center;
}

.portlet-placeholder {
	border: 1px dotted black;
	margin: 0 1em 1em 0;
	height: 50px;
}

.portlet-content label {
	text-align: right;
}

.portlet-quickCmdCont {
	margin: 0 0.5em;
	display: inline-block;
	color: #338;
	font-size: 10px;
	font-weight: normal;
}

.portlet-quickCmd {
	display: inline-block;
	margin: 0 0.25em;
	max-width: 440px;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
	height: 1.1em;
}

.ui-accordion .ui-accordion-content {
	padding: 0.5em 0.75em;
	font-size: 11px;
}

.ui-accordion .ui-accordion-header {
	font-size: 11px;
}

.sectionContainer {
	border: 1px solid #aaa;
	color: #222222;
}

.slider-opacity, .slider-quality {
	margin: 0 5px 0 5px;
}

.slider-opacity-value, .slider-quality-value {
	font-weight: bold;
	color: #fc0;
}

p {
	margin: 5px 0;
}

.showBulls {
	list-style: disc;
	margin: 11px;
}

label {
	margin: 0 5px;
	min-width: 20px;
	display: inline-block;
	text-align: center;
}

button {
	padding: 0.2em 0.5em;
}

button span, label span {
	font-size: 11px;
}

textarea {
	-webkit-box-sizing: border-box;
	-moz-box-sizing: border-box;
	box-sizing: border-box;
}

hr {
	border-width: 1px 0 0 0;
}

.absRight {
	position: absolute;
	right: 0;
}

.inBlock {
	display: inline-block;
}

.checkboardBg {
	background-color: #fff;
	background-image: linear-gradient(45deg, #eee 25%, transparent 25%, transparent 75%, #eee
		75%, #eee),
		linear-gradient(45deg, #eee 25%, transparent 25%, transparent 75%, #eee
		75%, #eee);
	background-size: 60px 60px;
	background-position: 0 0, 30px 30px;
}

.pad3px {
	padding: 3px;
}

.alignL {
	text-align: left !important;
}

.alignC {
	text-align: left !important;
}

.fleft {
	float: left;
}

.fright {
	float: right;
}

.clrL {
	clear: left;
}

.clrB {
	clear: both;
}

.noBorder {
	border: none !important;
}

.w50px {
	width: 50px;
}

.w75px {
	width: 75px;
}

.w100px {
	width: 100px;
}

.w140px {
	width: 140px;
}

.w150px {
	width: 150px;
}

.w250px {
	width: 250px;
}

.w275px {
	width: 275px;
}

.w300px {
	width: 300px;
}

.w500px {
	width: 500px;
}

.w100p {
	width: 100%;
}

.h2-5em {
	height: 2.5em;
}

.h4em {
	height: 4em;
}
</style>
<script type="text/javascript">
		var $jq = jQuery;
		var ajaxImageSizeEnabled = false; //enable ajax file size calls

		var imgSrcTemplate = '<img src="%%IMG_BASE%%%%IMG_URI%%%%IMG_AKA%%" title="%%IMG_SIZE%% image" />';
		var imgBasePath = 'https://www.freshdirect.com/media/images/product/';
		var imageUri = 'wine_8/win_pid_5002079_%%IMG_SIZE%%.jpg';
		var imgSizes = ['f', 'd', 'c', 'a', 'z', 'p', 'j']; //smallest to largest

		var akaCmds = [
			{
				'accordianElemId': 'akaCmdsOutAcc',
				'header': 'background-color',
				'content': 'Sets the background color for transparent portions of images.',
				'addButtonFunc': function() { addPortlet('background-color', 'akaForm_queue', getUniqueId('akaCmdBackgroundColor_'), 'Cmd: background-color', akaCmdHtml_backgroundColor); }
			},
			{
				'accordianElemId': 'akaCmdsOprAcc',
				'header': 'box',
				'content': 'Draws a rectangle on the image.',
				'addButtonFunc': function() { addPortlet('box', 'akaForm_queue', getUniqueId('akaCmdBox_'), 'Cmd: box', akaCmdHtml_box); }
			},
			{
				'accordianElemId': 'akaCmdsOprAcc',
				'header': 'composite / composite-to',
				'content': '<p>Combines two images. Commonly used to apply a watermark to an image.</p><p>The difference between these commands is which of the two images- the one in the IC command or the one in the base URL or image tag- is superimposed:</p><ul class="showBulls"><li>composite superimposes the image in the IC command to the base image.</li><li>composite-to superimposes the base image onto the image in the IC command.</li></ul>',
				'addButtonFunc': function() { addPortlet('composite', 'akaForm_queue', getUniqueId('akaCmdComposite_') , 'Cmd: composite', akaCmdHtml_composite); }
			},
			{
				'accordianElemId': 'akaCmdsOprAcc',
				'header': 'crop',
				'content': 'Cuts out a section of an image.',
				'addButtonFunc': function() { addPortlet('crop', 'akaForm_queue', getUniqueId('akaCmdCrop_'), 'Cmd: crop', akaCmdHtml_crop); }
			},
			{
				'accordianElemId': 'akaCmdsOprAcc',
				'header': 'downsize',
				'content': 'Scales, or resizes, the image to the dimensions you specify. However, if you enter a dimension that is larger than the original, the image\'s dimension will not be changed.',
				'addButtonFunc': function() { addPortlet('downsize', 'akaForm_queue', getUniqueId('akaCmdDownsize_'), 'Cmd: downsize', akaCmdHtml_downsize); }
			},
			{
				'accordianElemId': 'akaCmdsOprAcc',
				'header': 'fit',
				'content': 'Resize an image into the specified dimensions while maintaining aspect ratio.',
				'addButtonFunc': function() { addPortlet('fit', 'akaForm_queue', getUniqueId('akaCmdfit_'), 'Cmd: fit', akaCmdHtml_fit); }
			},
			{
				'accordianElemId': 'akaCmdsOutAcc',
				'header': 'interpolation',
				'content': 'Indicates which algorithm to use when changing the size of an image.',
				'addButtonFunc': function() { addPortlet('interpolation', 'akaForm_queue', getUniqueId('akaCmdInterpolation_'), 'Cmd: interpolation', akaCmdHtml_interpolation); }
			},
			{
				'accordianElemId': 'akaCmdsOutAcc',
				'header': 'output-format',
				'content': '<p>Renders the image in the specified format.</p><p>The output formats available are GIF, JPEG, and PNG.</p>',
				'addButtonFunc': function() { addPortlet('output-format', 'akaForm_queue', getUniqueId('akaCmdOutputFormat_'), 'Cmd: output-format', akaCmdHtml_outputFormat); }
			},
			{
				'accordianElemId': 'akaCmdsOutAcc',
				'header': 'output-quality',
				'content': 'For JPEG output, sets the desired quality of the output image.',
				'addButtonFunc': function() { addPortlet('output-quality', 'akaForm_queue', getUniqueId('akaCmdOutputQuality_'), 'Cmd: output-quality', akaCmdHtml_outputQuality); }
			},
			{
				'accordianElemId': 'akaCmdsOprAcc',
				'header': 'resize',
				'content': 'Scales, or resizes, the image to the dimensions you specify.',
				'addButtonFunc': function() { addPortlet('resize', 'akaForm_queue', getUniqueId('akaCmdResize_'), 'Cmd: resize', akaCmdHtml_resize); }
			},
			{
				'accordianElemId': 'akaCmdsMeaAcc',
				'header': 'unit',
				'content': 'Defines a new measurement unit in terms of existing units.',
				'helpCont': 'Help info',
				'addButtonFunc': function() { addPortlet('unit', 'akaForm_queue', getUniqueId('akaCmdUnit_'), 'Cmd: unit', akaCmdHtml_unit); }
			}
		];

		/* html for the various portlets
			wrap in div to prevent over-sizing
		 */

		/*
			background-color=<color>
		*/
		var akaCmdHtml_backgroundColor = '';
		akaCmdHtml_backgroundColor += '<div>';
			akaCmdHtml_backgroundColor += '<label for="cmdBackgroundColor_color">Color</label><input type="text" name="cmdBackgroundColor_color" class="cmdBackgroundColor_color w150px minicolors" />';
			
			akaCmdHtml_backgroundColor += '<div class="portlet-helpContent">';
				akaCmdHtml_backgroundColor += 'Help';
			akaCmdHtml_backgroundColor += '</div>';

		akaCmdHtml_backgroundColor += '</div>';

		/*
			box=<rectangle>|color[<color>]|fill|stroke[<linewidth>]
		
			rectangle = <width>:<height>;<x>,<y>
			box=0.25xw:0.25xh;0,0|color[008000]|fill
		*/
		var akaCmdHtml_box = '';
		akaCmdHtml_box += '<div>';
			akaCmdHtml_box += '<label for="cmdBoxDim_w">Width</label><input type="text" name="cmdBoxDim_w" class="cmdBoxDim_w w100px" />';
			akaCmdHtml_box += '<label for="cmdBoxDim_h">Height</label><input type="text" name="cmdBoxDim_h" class="cmdBoxDim_h w100px" />';
			akaCmdHtml_box += '<label for="cmdBoxDim_x">X</label><input type="text" name="cmdBoxDim_x" class="cmdBoxDim_x w100px" />';
			akaCmdHtml_box += '<label for="cmdBoxDim_y">Y</label><input type="text" name="cmdBoxDim_y" class="cmdBoxDim_y w100px" />';
			akaCmdHtml_box += '<br /><br />';
			akaCmdHtml_box += '<label for="cmdBox_color">Color</label><input type="text" name="cmdBox_color" class="cmdBox_color w150px minicolors" />';
			akaCmdHtml_box += '<label for="cmdBox_fill">Fill</label><input type="checkbox" name="cmdBox_fill" class="cmdBox_fill" />';
			akaCmdHtml_box += '<label for="cmdBox_stroke">Stroke</label><input type="text" name="cmdBox_stroke" class="cmdBox_stroke w50px" />';

			akaCmdHtml_box += '<div class="portlet-helpContent">';
				akaCmdHtml_box += 'By default, the box is a black outline of a rectangle with a line width of 1 pixel. Optional values allow you to change the line color, fill, and line width.';
			akaCmdHtml_box += '</div>';
		akaCmdHtml_box += '</div>';

		/*
			composite=<placement>|<image-URL>
			composite=<opacity>|<placement>|<image-URL>
			composite-to=<placement>|<image-URL>
			composite-to=<opacity>|<placement>|<image-URL>

			Location—Use coordinates to indicate where on the bottom image to place the upper-left corner of the superimposed image.
			Rectangle—Describe the dimensions and location of a rectangle on the bottom image. If the rectangle does not have the same dimensions as the top image, IC will resize the image to match. For example, if the image is 400 x 400, a composite command with dimensions of 40:40 would scale the image down to 1% the original size before superimposing it.

			image url:
			With composite, the image URL is the URL of the top image.
			With composite-to, the image URL is the URL of the bottom image.
		*/
		var akaCmdHtml_composite = '';
		akaCmdHtml_composite += '<div>';
			akaCmdHtml_composite += '<div class="buttonset">';
				akaCmdHtml_composite += '<input type="radio" name="cmdCompositeType_cmp" id="%%UNIQUE_ID%%_cmp" checked class="cmdCompositeType_cmp" /><label for="%%UNIQUE_ID%%_cmp" class="alignC">composite</label>';
				akaCmdHtml_composite += '<input type="radio" name="cmdCompositeType_cmp" id="%%UNIQUE_ID%%_cmpto" class="cmdCompositeType_cmpto" /><label for="%%UNIQUE_ID%%_cmpto" class="alignC">composite-to</label>';
			akaCmdHtml_composite += '</div>';
			akaCmdHtml_composite += '<br />';
			akaCmdHtml_composite += '<label for="cmdCompositeDim_w">Width</label><input type="text" name="cmdCompositeDim_w" class="cmdCompositeDim_w w100px" />';
			akaCmdHtml_composite += '<label for="cmdCompositeDim_h">Height</label><input type="text" name="cmdCompositeDim_h" class="cmdCompositeDim_h w100px" />';
			akaCmdHtml_composite += '<label for="cmdCompositeDim_x">X</label><input type="text" name="cmdCompositeDim_x" class="cmdCompositeDim_x w100px" />';
			akaCmdHtml_composite += '<label for="cmdCompositeDim_y">Y</label><input type="text" name="cmdCompositeDim_y" class="cmdCompositeDim_y w100px" />';
			akaCmdHtml_composite += '<br /><br />';
			akaCmdHtml_composite += '<label for="cmdComposite_opa">Opacity</label><input type="text" name="cmdComposite_opa" class="cmdComposite_opa slider-opacity-value w50px noBorder" readonly value="1" /><div class="inBlock w250px"><div class="slider-opacity"></div></div>';
			akaCmdHtml_composite += '<div><label for="cmdComposite_uri"class="w100p alignL">URL</label><textarea type="text" name="cmdComposite_uri" class="cmdComposite_uri w100p h2-5em"></textarea></div>';
			
			akaCmdHtml_composite += '<div class="portlet-helpContent">';
				akaCmdHtml_composite += 'Help';
			akaCmdHtml_composite += '</div>';

		akaCmdHtml_composite += '</div>';
		
		/*
			crop=<dimensions>;<location>
		*/
		var akaCmdHtml_crop = '';
		akaCmdHtml_crop += '<div>';
			akaCmdHtml_crop += '<label for="cmdCropDim_w">Height</label><input type="text" name="cmdCropDim_w" class="cmdCropDim_w w100px" />';
			akaCmdHtml_crop += '<label for="cmdCropDim_h">Width</label><input type="text" name="cmdCropDim_h" class="cmdCropDim_h w100px" />';
			akaCmdHtml_crop += '<label for="cmdCropDim_x">X</label><input type="text" name="cmdCropDim_x" class="cmdCropDim_x w100px" />';
			akaCmdHtml_crop += '<label for="cmdCropDim_y">Y</label><input type="text" name="cmdCropDim_y" class="cmdCropDim_y w100px" />';

			
			akaCmdHtml_crop += '<div class="portlet-helpContent">';
				akaCmdHtml_crop += 'Help';
			akaCmdHtml_crop += '</div>';
		akaCmdHtml_crop += '</div>';
		
		/*
			downsize=<width>:<height>
		*/
		var akaCmdHtml_downsize = '';
		akaCmdHtml_downsize += '<div>';
			akaCmdHtml_downsize += '<label for="cmdDownsizeDim_w">Height</label><input type="text" name="cmdDownsizeDim_w" class="cmdDownsizeDim_w w100px" />';
			akaCmdHtml_downsize += '<label for="cmdDownsizeDim_h">Width</label><input type="text" name="cmdDownsizeDim_h" class="cmdDownsizeDim_h w100px" />';

			akaCmdHtml_downsize += '<div class="portlet-helpContent">';
				akaCmdHtml_downsize += 'Help';
			akaCmdHtml_downsize += '</div>';

		akaCmdHtml_downsize += '</div>';
		
		/*
			fit=inside|<dimensions>
			fit=around|<dimensions>
		*/
		var akaCmdHtml_fit = '';
		akaCmdHtml_fit += '<div>';
			akaCmdHtml_fit += '<div class="buttonset" style="display: inline-block;">';
				akaCmdHtml_fit += '<input type="radio" name="cmdFitType" id="%%UNIQUE_ID%%_in" checked class="cmdFitType_in" /><label for="%%UNIQUE_ID%%_in" class="alignC">inside</label>';
				akaCmdHtml_fit += '<input type="radio" name="cmdFitType" id="%%UNIQUE_ID%%_ar" class="cmdFitType_ar " /><label for="%%UNIQUE_ID%%_ar" class="alignC">around</label>';
			akaCmdHtml_fit += '</div>';
			akaCmdHtml_fit += ' <label for="cmdFitDim_w">Width</label><input type="text" name="cmdFitDim_w" class="cmdFitDim_w w100px" />';
			akaCmdHtml_fit += '<label for="cmdFitDim_h">Height</label><input type="text" name="cmdFitDim_h" class="cmdFitDim_h w100px" />';

			akaCmdHtml_fit += '<div class="portlet-helpContent">';
				akaCmdHtml_fit += 'Help';
			akaCmdHtml_fit += '</div>';
		akaCmdHtml_fit += '</div>';
		
		/*
			interpolation=<algorithm>
		*/
		var akaCmdHtml_interpolation = '';
		akaCmdHtml_interpolation += '<div>';
			akaCmdHtml_interpolation += '<select name="cmdInterpolation_algo" class="cmdInterpolation_algo">';
				akaCmdHtml_interpolation += '<option value="">Select Algorithm</option>';
				akaCmdHtml_interpolation += '<option value="bilinear">bilinear</option>';
				akaCmdHtml_interpolation += '<option value="bicubic">bicubic</option>';
				akaCmdHtml_interpolation += '<option value="nearest-neighbor">nearest-neighbor</option>';
				akaCmdHtml_interpolation += '<option value="progressive-bilinear">progressive-bilinear</option>';
				akaCmdHtml_interpolation += '<option value="progressive-bicubic">progressive-bicubic</option>';
				akaCmdHtml_interpolation += '<option value="lanczos-none">lanczos-none</option>';
			akaCmdHtml_interpolation += '</select>';

			akaCmdHtml_interpolation += '<div class="portlet-helpContent">';
				akaCmdHtml_interpolation += 'Help';
			akaCmdHtml_interpolation += '</div>';
		akaCmdHtml_interpolation += '</div>';
			
		/*
			output-format=<image-format>

			If you convert an image from a format that does support transparency (i.e., GIF or PNG) to a format that does not (JPEG), IC will set the default background color to white. Use the background-color command to override this default.

			gif or image/gif
			jpeg, image/jpeg, jpg, or image/jpg
			png or image/png
			webp or image/webp	-BETA-
			jp2 or image/jpeg2000	-BETA-
			jpegxr or image/vnd.ms-photo	-BETA-
			tiff	-NOT RECOMMENDED-
		*/
		var akaCmdHtml_outputFormat = '';
		akaCmdHtml_outputFormat += '<div>';
			akaCmdHtml_outputFormat += '<select name="cmdOutputFormat_format" class="cmdOutputFormat_format">';
				akaCmdHtml_outputFormat += '<option value="">Select Format</option>';

				akaCmdHtml_outputFormat += '<option value="gif">-----[ GIF ]-----</option>';
				akaCmdHtml_outputFormat += '<option value="gif">gif</option>';
				akaCmdHtml_outputFormat += '<option value="image/gif">image/gif</option>';

				akaCmdHtml_outputFormat += '<option value="jpg">-----[ JPEG ]-----</option>';
				akaCmdHtml_outputFormat += '<option value="jpeg">jpeg</option>';
				akaCmdHtml_outputFormat += '<option value="image/jpeg">image/jpeg</option>';
				akaCmdHtml_outputFormat += '<option value="jpg">jpg</option>';
				akaCmdHtml_outputFormat += '<option value="image/jpg">image/jpg</option>';

				akaCmdHtml_outputFormat += '<option value="png">-----[ PNG ]-----</option>';
				akaCmdHtml_outputFormat += '<option value="png">png</option>';
				akaCmdHtml_outputFormat += '<option value="image/png">image/png</option>';

				akaCmdHtml_outputFormat += '<option value="webp">-----[ WEBP (BETA) ]-----</option>';
				akaCmdHtml_outputFormat += '<option value="webp">webp (BETA)</option>';
				akaCmdHtml_outputFormat += '<option value="image/webp">image/webp (BETA)</option>';

				akaCmdHtml_outputFormat += '<option value="jp2">-----[ JP2 (BETA) ]-----</option>';
				akaCmdHtml_outputFormat += '<option value="jp2">jp2 (BETA)</option>';
				akaCmdHtml_outputFormat += '<option value="image/jpeg2000">image/jpeg2000 (BETA)</option>';

				akaCmdHtml_outputFormat += '<option value="jpegxr">-----[ JPEGXR (BETA) ]-----</option>';
				akaCmdHtml_outputFormat += '<option value="jpegxr">jpegxr (BETA)</option>';
				akaCmdHtml_outputFormat += '<option value="image/vnd.ms-photo">image/vnd.ms-photo (BETA)</option>';

				akaCmdHtml_outputFormat += '<option value="">-----[ TIFF ]-----</option>';
				akaCmdHtml_outputFormat += '<option value="tiff">tiff (Not Recommended)</option>';
			akaCmdHtml_outputFormat += '</select>';
			
			akaCmdHtml_outputFormat += '<div class="portlet-helpContent">';
				akaCmdHtml_outputFormat += 'Help';
			akaCmdHtml_outputFormat += '</div>';
		akaCmdHtml_outputFormat += '</div>';
		
		/*
			output-quality=<quality-value>

			This command can be used with JPEG files only.
		*/
		var akaCmdHtml_outputQuality = '';
		akaCmdHtml_outputQuality += '<div>';
			akaCmdHtml_outputQuality += '<label for="cmdOutputQuality_num">Quality</label><input type="text" name="cmdOutputQuality_num" class="cmdOutputQuality_num slider-quality-value w50px noBorder" readonly value="100" /><div class="inBlock w250px"><div class="slider-quality"></div></div>';
			
			akaCmdHtml_outputQuality += '<div class="portlet-helpContent">';
				akaCmdHtml_outputQuality += 'Help';
			akaCmdHtml_outputQuality += '</div>';
		akaCmdHtml_outputQuality += '</div>';
		
		/*
			resize=<width>:<height>
		*/
		var akaCmdHtml_resize = '';
		akaCmdHtml_resize += '<div>';
			akaCmdHtml_resize += '<label for="cmdResizeDim_w">Width</label><input type="text" name="cmdResizeDim_w" class="cmdResizeDim_w w100px" />';
			akaCmdHtml_resize += '<label for="cmdResizeDim_h">Height</label><input type="text" name="cmdResizeDim_h" class="cmdResizeDim_h w100px" />';
			
			akaCmdHtml_resize += '<div class="portlet-helpContent">';
				akaCmdHtml_resize += 'Help';
			akaCmdHtml_resize += '</div>';
		akaCmdHtml_resize += '</div>';
		
		/*
			unit=<sizeunit>::<sizecurrent>

			Pixels are the default unit of measurement. To use a different unit of measurement, you must define it with the unit command. The unit command defines a new measurement unit in terms of existing units.
		*/
		var akaCmdHtml_unit = '';
		akaCmdHtml_unit += '<div>';
			akaCmdHtml_unit += '<label for="cmdUnitUnit_num" class="w100px">Size Unit</label><input type="text" name="cmdUnitUnit_num" class="cmdUnitUnit_num w150px" />';
			akaCmdHtml_unit += '<label for="cmdUnitCurrent_num" class="w100px">Size Current</label><input type="text" name="cmdUnitCurrent_num" class="cmdUnitCurrent_num w150px" />';
			
			akaCmdHtml_unit += '<div class="portlet-helpContent">';
				akaCmdHtml_unit += 'Help';
			akaCmdHtml_unit += '</div>';
		akaCmdHtml_unit += '</div>';

		/* END html for the various portlets */

		function getUniqueId(prefix) {
			return (''+prefix+(new Date().getTime() + Math.random())).replace(/\./g, '_');
		}

		function createAccordionElemHtml(srcObj) {
			var html = '';
			html += '<h3>'+srcObj.header+'<span class="ui-icon ui-icon-arrowthick-1-e akaCmdAdd" title="add command to queue"></span>'+'</h3>';
			html += '<div>';
				html += srcObj.content;
			html += '</div>';

			return html;
		}

		function appendAccordionElem(accElemId, srcObj) {
			$('#'+accElemId).append(createAccordionElemHtml(srcObj));
		}

		function getImgHtml(imgSrcTemplate, imageUri, imageSize, imgAkaParamsArr) {
			var imgAkaParams = '';
			if ($.isArray(imgAkaParamsArr)) {
				imgAkaParams = '?' + imgAkaParamsArr.join();
			}


			return imgSrcTemplate
				.replace(/%%IMG_BASE%%/g, imgBasePath)
				.replace(/%%IMG_URI%%/g, imageUri)
				.replace(/%%IMG_SIZE%%/g, imageSize)
				.replace(/%%IMG_AKA%%/g, imgAkaParams);
		}

		/* this only works on the same server */
		function getBase64Image(img) {

			// Create an empty canvas element
			var canvas = document.createElement("canvas");
			canvas.width = img.width;
			canvas.height = img.height;

			var canvas2 = document.createElement("canvas");
			canvas2.width = img.width;
			canvas2.height = img.height;

			// Copy the image contents to the canvas
			var ctx = canvas.getContext("2d");
			ctx.drawImage(img, 0, 0);

			// Copy the image contents to the canvas
			var ctx2 = canvas2.getContext("2d");
			ctx2.drawImage(canvas, 0, 0);

			// Get the data-URL formatted image
			// Firefox supports PNG and JPEG. You could check img.src to guess the
			// original format, but be aware the using "image/jpg" will re-encode the image.
			var dataURL = canvas2.toDataURL("image/png");

			return dataURL.split(',')[1];
		}

		
		function getBase64ImageFromRemoteBase64(img) {
			return img.src.split(',')[1].split('"')[0];
		}

		//get image bytes size
		function getImageSize(img) {
			//return ((getBase64Image(img).length * 0.75) / 1024).toFixed(2);
			return ((getBase64ImageFromRemoteBase64(img).length * 0.75) / 1024).toFixed(2);
		}
		
		function getRemoteFileSize(imgUrl, imageElem) {
			$.getImageData({
				url: imgUrl,
				success: function(image){
					// Do something with the now local version of the image
					//console.log(getImageSize(image));
					//console.log(image);

					var imgSizeInKb = getImageSize(image);

					/* 
						workaround for tooltip enabled while callback is happening
						check if tooltip is open, and destroy and reopen if it is
						(requires setting tooltip data attr beforehand)
					*/
					var ttOpen = $(imageElem).data('tooltip');
					if (ttOpen) {
						$(imageElem).tooltip('destroy');
					}
					
					$(imageElem).attr('title', $(imageElem).attr('title') + "\nsize: " + imgSizeInKb + 'kb');

					//$(imageElem).trigger('mouseleave');

					$(imageElem).tooltip({
						content: function() {
							var html = $(this).attr('title').replace(/\n/g, '<br />');
							return '<div>'+html+'</div>';
						}
					});

					if (ttOpen) {
						$(imageElem).trigger('mouseover');
					}
				},
				error: function(xhr, text_status){
					// Handle your error here
					console.log('ERR in getRemoteFileSize');
				}
			});
		}

		function addPortlet(ofType, portletContId, portletId, headerHtml, contentHtml) {
			$('#'+portletContId).append(createCmdPortlet(ofType, portletContId, portletId, headerHtml, contentHtml));
			$('#'+portletId + ' .minicolors').minicolors();

			$('#'+portletContId).sortable({
				handle: '.portlet-header',
				cursor: 'move'
			});
			
			$('#'+portletId).find('.slider-opacity, .slider-quality').each(function(i,e) {
				if ($(e).hasClass('slider-opacity')) {
					$(e).slider({
						range: "min",
						min: 0,
						max: 1,
						value: 1,
						step: 0.01,
						slide: function( event, ui ) {
							$(this).closest('.portlet-content').find('.slider-opacity-value').val( ui.value );
						}
					});
				}
				if ($(e).hasClass('slider-quality')) {
					$(e).slider({
						range: "min",
						min: 1,
						max: 100,
						value: 100,
						step: 1,
						slide: function( event, ui ) {
							$(this).closest('.portlet-content').find('.slider-quality-value').val( ui.value );
						}
					});
				}
			});

			$('#'+portletId).find( '.buttonset' ).buttonset();
			
			var helpCont = $('#'+portletId).find('.portlet-helpContent').html();
			if (helpCont !== '') {
				$('#'+portletId).find('.portlet-helpIcon').attr('title', helpCont);
			}
			$('#'+portletId).find('.ui-icon').tooltip();
		}

		/* returns completed div */
		function createCmdPortlet(ofType, portletContId, portletId, headerHtml, contentHtml) {
			/* allow id usage in html */
			headerHtml = headerHtml.replace(/%%UNIQUE_ID%%/g, portletId);
			contentHtml = contentHtml.replace(/%%UNIQUE_ID%%/g, portletId);

			$('#'+portletContId).append('<div id="'+portletId+'" class="portlet" data-type="'+ofType+'"></div>');

			$( '#'+portletId )
				.append('<div class="portlet-header" id="'+portletId+'_header">'+headerHtml+'<span class="portlet-quickCmdCont" style="display: none">(<span class="portlet-quickCmd"></span>)</span></div>')
				.append('<div class="portlet-content" id="'+portletId+'_content">'+contentHtml+'</div>');
			
			$( '#'+portletId )
				.addClass( "ui-widget ui-widget-content ui-helper-clearfix ui-corner-all" )
				.find( ".portlet-header" )
				.addClass( "ui-widget-header ui-corner-all" )
				.prepend( '<span class="ui-icon ui-icon-help portlet-helpIcon" title=""></span>')
				.prepend( '<span class="ui-icon ui-icon-triangle-1-sw portlet-toggle" title="toggle"></span>')
				.prepend( '<span class="ui-icon ui-icon-trash portlet-remove" title="remove"></span>');
		 
			$( '#'+portletId).find( '.portlet-toggle' ).click(function() {
				var icon = $(this);
				icon.toggleClass( "ui-icon-triangle-1-sw ui-icon-triangle-1-w" );
				icon.closest( ".portlet" ).find( ".portlet-content" ).toggle();

				//update display (triangle-w is a closed portlet)
				updateParsedCmdDisplay(icon.closest('div.portlet'), $(icon).hasClass('ui-icon-triangle-1-w'));
			});
			$( '#'+portletId).find( '.portlet-remove' ).click(function() {
				$( '#'+portletId).remove();
			});
			
			//start collpased
			//$( '#'+portletId).find( '.portlet-toggle' ).click();
		}

		function updateParsedCmdDisplay($portletElem, showBool) {
			console.log($portletElem, showBool);
			var cmdDisplay = $portletElem.find('.portlet-quickCmdCont:first');
			var type = $portletElem.data('type');

			var cmdVal = parsePortletByType($portletElem, type);

			if (showBool) {
				if (cmdVal !== '') {
					cmdDisplay.find('.portlet-quickCmd:first').html(cmdVal);
				} else {
					cmdDisplay.find('.portlet-quickCmd:first').html('INVALID CONFIGURATION');
				}
				cmdDisplay.show();
			} else {
				cmdDisplay.hide();
			}
		}

		/* PARSING */ 
			/* surely there's a better way... */
			//multiple
			function parsePortlets() {
				var cmdParams = [];
				$('#akaForm_queue .portlet').each(function(i,e) {
					console.log($(e).data('type'));
					
					cmdParams.push(parsePortletByType($(e), $(e).data('type')));

				});

				console.log(cmdParams);

				return cmdParams;
			}

			//singular
			function parsePortletByType($e, type) {
				switch (type) {
					case 'background-color': 
						return parseBackgroundColor($e);
						break;
					case 'box': 
						return parseBox($e);
						break;
					case 'composite': 
						return parseComposite($e);
						break;
					case 'crop': 
						return parseCrop($e);
						break;
					case 'downsize': 
						return parseDownsize($e);
						break;
					case 'fit': 
						return parseFit($e);
						break;
					case 'interpolation': 
						return parseInterpolation($e);
						break;
					case 'output-format': 
						return parseOutputFormat($e);
						break;
					case 'output-quality': 
						return parseOutputQuality($e);
						break;
					case 'resize': 
						return parseResize($e);
						break;
					case 'unit': 
						return parseUnit($e);
						break;
				}
			}

			function parseBackgroundColor(elem) {
				var ret = '';
				var colorVal = cleanColorVal($(elem).find('.minicolors-input').val());

				if (colorVal !== '') {
					ret += 'background-color='+colorVal;
				}

				return ret;
			}

			function parseBox(elem) {
				var ret = '';
				
				var w = '', h = '', x= '', y = '';
				var colorVal = cleanColorVal($(elem).find('.minicolors-input').val());
				var fill = '';
				var strokeSize = '';

				w = $(elem).find('.cmdBoxDim_w').val();
				h = $(elem).find('.cmdBoxDim_h').val();
				x = $(elem).find('.cmdBoxDim_x').val();
				y = $(elem).find('.cmdBoxDim_y').val();
				if ($(elem).find('.cmdBox_fill').is(':checked')) {
					fill = 'fill';
				}
				strokeSize = $(elem).find('.cmdBox_stroke').val();

				if (w === '') { w = '0'; }
				if (h === '') { h = '0'; }
				if (x === '') { x = '0'; }
				if (y === '') { y = '0'; }

				//not filled out or useless
				if (w+h+x+y === '0000') { return ''; }

				if (strokeSize === '') { strokeSize = '1'; }

				ret += 'box='+getRectText(w,h,x,y);

				if (colorVal !== '') {
					ret += '|color['+colorVal+']';
				}
				if (fill === 'fill') {
					ret += '|fill';
				}
				
				ret += '|stroke['+strokeSize+']';

				return ret;
			}

			function parseComposite(elem) {
				var ret = '';
				var w = '', h = '', x= '', y = '';
				var type = 'composite';
				if ($(elem).find('.cmdCompositeType_cmpto').is(':checked')) {
					type = 'composite-to';
				}
				var uri = $(elem).find('.cmdComposite_uri').val();
				var opacity = $(elem).find('.cmdComposite_opa').val();

				w = $(elem).find('.cmdCompositeDim_w').val();
				h = $(elem).find('.cmdCompositeDim_h').val();
				x = $(elem).find('.cmdCompositeDim_x').val();
				y = $(elem).find('.cmdCompositeDim_y').val();
				if (w === '') { w = '0'; }
				if (h === '') { h = '0'; }
				if (x === '') { x = '0'; }
				if (y === '') { y = '0'; }

				//not filled out or useless
				if (w+h+x+y === '0000' || uri === '') { return ''; }

				ret += type+'=';
				ret += opacity; //opacity first
				ret += '|'+getDimText(w,h); //placement
				if (x !== '0' || y !== '0') {
					ret += ';'+getLocaText(x,y);
				}
				ret += '|'+uri; //uri

				return ret;
			}

			function parseCrop(elem) {
				var ret = '';
				
				var w = '', h = '', x= '', y = '';

				w = $(elem).find('.cmdCropDim_w').val();
				h = $(elem).find('.cmdCropDim_h').val();
				x = $(elem).find('.cmdCropDim_x').val();
				y = $(elem).find('.cmdCropDim_y').val();

				if (w === '') { w = '0'; }
				if (h === '') { h = '0'; }
				if (x === '') { x = '0'; }
				if (y === '') { y = '0'; }

				//not filled out or useless
				if (w+h+x+y === '0000') { return ''; }

				ret += 'crop='+getRectText(w,h,x,y);

				return ret;
			}

			function parseDownsize(elem) {
				var ret = '';
				
				var w = '', h = '';

				w = $(elem).find('.cmdDownsizeDim_w').val();
				h = $(elem).find('.cmdDownsizeDim_h').val();

				if (w === '') { w = '0'; }
				if (h === '') { h = '0'; }

				//not filled out or useless
				if (w+h === '00') { return ''; }

				ret += 'downsize='+getDimText(w,h);

				return ret;
			}

			function parseFit(elem) {
				var ret = '';
				var type = 'inside';
				if ($(elem).find('.cmdFitType_ar').is(':checked')) {
					type = 'around';
				}
				
				var w = '', h = '';

				w = $(elem).find('.cmdFitDim_w').val();
				h = $(elem).find('.cmdFitDim_h').val();

				if (w === '') { w = '0'; }
				if (h === '') { h = '0'; }

				//not filled out or useless
				if (w+h === '00') { return ''; }

				ret += 'fit='+type+'|'+getDimText(w,h);

				return ret;
			}

			function parseInterpolation(elem) {
				var ret = '';
				var algo = $(elem).find('.cmdInterpolation_algo').val();

				//not filled out or useless
				if (algo === '') { return ''; }

				ret += 'interpolation='+algo;

				return ret;
			}

			function parseOutputFormat(elem) {
				var ret = '';
				var format = $(elem).find('.cmdOutputFormat_format').val();

				//not filled out or useless
				if (format === '') { return ''; }

				ret += 'output-format='+format;

				return ret;
			}

			function parseOutputQuality(elem) {
				var ret = '';
				var quality = $(elem).find('.cmdOutputQuality_num').val();

				ret += 'output-quality='+quality;

				return ret;
			}

			function parseResize(elem) {
				var ret = '';
				
				var w = '', h = '';

				w = $(elem).find('.cmdResizeDim_w').val();
				h = $(elem).find('.cmdResizeDim_h').val();

				if (w === '') { w = '0'; }
				if (h === '') { h = '0'; }

				//not filled out or useless
				if (w+h === '00') { return ''; }

				ret += 'resize='+getDimText(w,h);

				return ret;
			}
			
			function parseUnit(elem) {
				var sUnit = $(elem).find('.cmdUnitUnit_num').val(),
					sCur = $(elem).find('.cmdUnitCurrent_num').val();

				
				//not filled out or useless
				if (sUnit === '' || sCur === '') { return ''; }
				
				return 'unit='+sUnit+'::'+sCur;
			}

			//helpers to keep formats
			function getRectText(w,h,x,y) {
				return getDimText(w,h)+';'+getLocaText(x,y);
			}
			function getDimText(w,h) {
				return w+':'+h;
			}
			function getLocaText(x,y) {
				return x+','+y;
			}
			function cleanColorVal(val) {
				return val.replace(/#/g, '');
			}
		/* END PARSING */


		$('document').ready(function() {
			for (var i = 0; i < imgSizes.length; i++) {
				//console.log( getImgHtml(imgSrcTemplate, imageUri, imgSizes[i]) );
				$('.origImages').append(getImgHtml(imgSrcTemplate, imageUri, imgSizes[i]));
			}
			$('.origImages').find('img').each(function(i,e) {
				$(e).load(function() {
					$( e ).on( "tooltipopen", function( event, ui ) {
						$( e ).data( "tooltip", true )
					} );
					$( e ).on( "tooltipclose", function( event, ui ) {
						$( e ).data( "tooltip", false )
					} );

					$(e).attr('title', $(e).attr('title') + 
						"\ndim : " + $(e).width() + 'w ' + $(e).height() + 'h' +
						"\nurl: " + $(e).attr('src')
					);
					
					if (ajaxImageSizeEnabled) {
						getRemoteFileSize(e.src, e);
					}
					$(e).tooltip({
						content: function() {
							var html = $(this).attr('title').replace(/\n/g, '<br />');
							return '<div>'+html+'</div>';
						}
					});
				});
				
			});

			$('.imgSizesList').html(imgSizes.join(', '));
			$('#origImagesToggle').click(function() { $('.origImages').toggle() });

			//init accordions
			var accSubIndex = {};
			for (var i = 0; i < akaCmds.length; i++) {
				//safety
				if (!akaCmds[i].hasOwnProperty('accordianElemId') || akaCmds[i].accordianElemId === '') {
					akaCmds[i].accordianElemId = 'Other';
				}

				if (!accSubIndex.hasOwnProperty(akaCmds[i].accordianElemId)) {
					accSubIndex[akaCmds[i].accordianElemId] = 0;
				} else {
					accSubIndex[akaCmds[i].accordianElemId]++;
				}
				appendAccordionElem(akaCmds[i].accordianElemId, akaCmds[i]);

				$($('#'+akaCmds[i].accordianElemId+' .akaCmdAdd')[accSubIndex[akaCmds[i].accordianElemId]]).click(akaCmds[i].addButtonFunc);
			}
			$('.accordion').accordion({
				heightStyle: "content"
			});
			$('.accordion').find('.ui-icon').tooltip();

			$('button').button();
			
			//make things resizable
			$('.resizable').resizable({
				grid: 50
			});

			$('#testCmds').click(function() {
				$('#akaResult').empty().html(getResultHtml());
				$('#akaResult').find('img:first').load(function() {
					$( this ).on( "tooltipopen", function( event, ui ) {
						$( this ).data( "tooltip", true )
					} );
					$( this ).on( "tooltipclose", function( event, ui ) {
						$( this ).data( "tooltip", false )
					} );

					$(this).attr('title', $(this).attr('title') + 
						"dim : " + $(this).width() + 'w ' + $(this).height() + 'h' +
						"\nurl: " + $(this).attr('src')
					);
					
					if (ajaxImageSizeEnabled) {
						getRemoteFileSize($(this).attr('src'), $(this));

					}

					$(this).tooltip({
						content: function() {
							var html = $(this).attr('title').replace(/\n/g, '<br />');
							return '<div>'+html+'</div>';
						}
					});
				});
			});

			
			$('.helpToolTip').tooltip({
				content: function() {
					var element = $( this );
					console.log(element);
					var html = $(element).attr('title').replace(/\n/g, '<br />');
					return '<div>'+html+'</div>';
				}
			});

			$('#ajaxImageSizes').button().click(function() {
				ajaxImageSizeEnabled = $('#ajaxImageSizes').is(':checked');
			});
			if (ajaxImageSizeEnabled) {
				$('#ajaxImageSizes').click();
			}
		});

		function getResultHtml() {
			var testUrl = $('#akaCmdUrl').val();
			var cmdParams = (parsePortlets()).join('&');
			//check here for existing ? or &
			var sep = '?'

			var testUrl = testUrl+ ( (cmdParams !== '') ? sep+cmdParams: '' );
			console.log(testUrl);

			return '<img src="'+testUrl+'" title="" />';
		}



	</script>
</head>
<body>

	<div id="akaCont">
		<div class="akaTitle">
			Akami Image Convertor <span class="ajaxImageSizes inBlock fright"><input
				id="ajaxImageSizes" type="checkbox" /><label for="ajaxImageSizes">Ajax
					Image Sizes</label></span>
		</div>
		<div id="akaForm">
			<div id="akaCmdAccordion" class="accordion fleft">
				<h3>
					Measurement <span
						class="ui-icon ui-icon-help akaCmdGroupHelp helpToolTip"
						title="The definitions you create with the unit command affect only the commands that follow it. Therefore, it is recommended that the unit command be the first command (if used)."></span>
				</h3>
				<div class="accordion" id="akaCmdsMeaAcc"></div>
				<h3>
					Operations <span
						class="ui-icon ui-icon-help akaCmdGroupHelp helpToolTip"
						title="The order of operations commands is important.<br /><br /> If you use the same operations command twice, each command is performed based on its order of appearance. <br /><br />As IC reads query strings sequentially from left to right, the left-most command would be performed first. The second command would then be performed based on the output of the first command."></span>
				</h3>
				<div class="accordion" id="akaCmdsOprAcc"></div>
				<h3>
					Output <span
						class="ui-icon ui-icon-help akaCmdGroupHelp helpToolTip"
						title="It is recommended that output commands follow operations
commands.<br /><br /> If you repeat an output command, only the right-most command is performed."></span>
				</h3>
				<div class="accordion" id="akaCmdsOutAcc"></div>
			</div>
			<div id="cmdCont" class="fleft">
				<div id="cmdTestUrlCont" class="fleft">
					<div class="akaTitle" style="position: relative;">
						<label for="akaCmdUrl">Test URL</label> <span
							class="inBlock absRight"><button id="testCmds">Test</button></span>
					</div>
					<textarea class="w100p h4em" name="akaCmdUrl" id="akaCmdUrl">https://www.freshdirect.com/media/images/product/wine_8/win_pid_5002079_p.jpg</textarea>
					<hr />
				</div>
				<div class="sectionContainer fleft clrL w100p">
					<div class="akaTitle">Command Queue (drag to sort)</div>
					<div id="akaForm_queue"></div>
				</div>
			</div>
		</div>
		<div id="akaResultCont" class="checkboardBg sectionContainer">
			<div class="akaTitle akaResultTitle">Result</div>
			<div id="akaResult"></div>

		</div>
	</div>
	<br style="clear: both" />

	<!--
	a c d f j p z
	smallest to largest: f d c a z p j
	https://www.freshdirect.com/media/images/product/wine_8/win_pid_5002079_z.jpg
	transparent image: /media_stat/images/navigation/sidecart/sidecart_buttons.png
-->
	<div class="akaTitle">
		Original Images (<span class="imgSizesList"></span> LtoR, hover to see
		details) <span class="inBlock absRight"><button
				id="origImagesToggle">show/hide</button></span>
	</div>
	<div class="origImages checkboardBg sectionContainer"></div>


	<script type="text/javascript">
	$('document').ready(function() {
		/*
		 *  jQuery $.getImageData Plugin 0.3
		 *  http://www.maxnov.com/getimagedata
		 *
		 *  Written by Max Novakovic (http://www.maxnov.com/)
		 *  Date: Thu Jan 13 2011
		 *
		 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
		 *
		 *  Includes jQuery JSONP Core Plugin 2.4.0 (2012-08-21)
		 *  https://github.com/jaubourg/jquery-jsonp
		 *  Copyright 2012, Julian Aubourg
		 *  Released under the MIT License.
		 *
		 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
		 *
		 *  Copyright 2011, Max Novakovic
		 *  Dual licensed under the MIT or GPL Version 2 licenses.
		 *  http://www.maxnov.com/getimagedata/#license
		 *
		 */
		 /* set to use bps server php page */
			var bpsAddress = 'http://108.46.184.147/work_test/imgToJson.php'; //http://brokenparts.zapto.org/work_test/imgToJson.php
			(function(d){function U(){}function V(a){r=[a]}function e(a,d,e){return a&&a.apply(d.context||d,e)}function g(a){function g(b){l++||(m(),n&&(t[c]={s:[b]}),A&&(b=A.apply(a,[b])),e(u,a,[b,B,a]),e(C,a,[a,B]))}function s(b){l++||(m(),n&&b!=D&&(t[c]=b),e(v,a,[a,b]),e(C,a,[a,b]))}a=d.extend({},E,a);var u=a.success,v=a.error,C=a.complete,A=a.dataFilter,p=a.callbackParameter,F=a.callback,W=a.cache,n=a.pageCache,G=a.charset,c=a.url,f=a.data,H=a.timeout,q,l=0,m=U,b,h,w;I&&I(function(a){a.done(u).fail(v);u=a.resolve;v=a.reject}).promise(a);a.abort=function(){!l++&&m()};if(!1===e(a.beforeSend,a,[a])||l)return a;c=c||x;f=f?"string"==typeof f?f:d.param(f,a.traditional):x;c+=f?(/\?/.test(c)?"&":"?")+f:x;p&&(c+=(/\?/.test(c)?"&":"?")+encodeURIComponent(p)+"=?");W||n||(c+=(/\?/.test(c)?"&":"?")+"_"+(new Date).getTime()+"=");c=c.replace(/=\?(&|$)/,"="+F+"$1");n&&(q=t[c])?q.s?g(q.s[0]):s(q):(J[F]=V,b=d(K)[0],b.id=L+X++,G&&(b[Y]=G),M&&11.6>M.version()?(h=d(K)[0]).text="document.getElementById('"+b.id+"')."+y+"()":b[N]=N,Z&&(b.htmlFor=b.id,b.event=z),b[O]=b[y]=b[P]=function(a){if(!b[Q]||!/i/.test(b[Q])){try{b[z]&&b[z]()}catch(c){}a=r;r=0;a?g(a[0]):s(R)}},b.src=c,m=function(a){w&&clearTimeout(w);b[P]=b[O]=b[y]=null;k[S](b);h&&k[S](h)},k[T](b,p=k.firstChild),h&&k[T](h,p),w=0<H&&setTimeout(function(){s(D)},H));return a}var N="async",Y="charset",x="",R="error",T="insertBefore",L="_jqjsp",z="onclick",y="on"+R,O="onload",P="onreadystatechange",Q="readyState",S="removeChild",K="<script>",B="success",D="timeout",J=window,I=d.Deferred,k=d("head")[0]||document.documentElement,t={},X=0,r,E={callback:L,url:location.href},M=J.opera,Z=!!d("<div>").html("<!--[if IE]><i><![endif]-->").find("i").length;g.setup=function(a){d.extend(E,a)};d.jsonp=g})(jQuery);(function($){$.getImageData=function(args){var regex_url_test=/(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/;if(args.url){var is_secure=location.protocol==="https:";var server_url="";if(args.server&&regex_url_test.test(args.server)&&!(is_secure&&args.server.indexOf("http:")==0)){server_url=args.server}else server_url=bpsAddress;server_url+="?callback=?";$.jsonp({url:server_url,data:{url:escape(args.url)},dataType:"jsonp",timeout:args.timeout||1e4,success:function(data,status){var return_image=new Image;$(return_image).load(function(){this.width=data.width;this.height=data.height;if(typeof args.success==typeof Function){args.success(this)}}).attr("src",data.data)},error:function(xhr,text_status){if(typeof args.error==typeof Function){args.error(xhr,text_status)}}})}else{if(typeof args.error==typeof Function){args.error(null,"no_url")}}}})(jQuery);
		/*
		 * jQuery MiniColors: A tiny color picker built on jQuery
		 *
		 * Copyright: Cory LaViska for A Beautiful Site, LLC
		 *
		 * Contributions and bug reports: https://github.com/claviska/jquery-minicolors
		 *
		 * @license: http://opensource.org/licenses/MIT
		 *
		 */
			jQuery&&function($){function i(i,t){var o=$('<div class="minicolors" />'),n=$.minicolors.defaults;i.data("minicolors-initialized")||(t=$.extend(!0,{},n,t),o.addClass("minicolors-theme-"+t.theme).toggleClass("minicolors-with-opacity",t.opacity).toggleClass("minicolors-no-data-uris",t.dataUris!==!0),void 0!==t.position&&$.each(t.position.split(" "),function(){o.addClass("minicolors-position-"+this)}),i.addClass("minicolors-input").data("minicolors-initialized",!1).data("minicolors-settings",t).prop("size",7).wrap(o).after('<div class="minicolors-panel minicolors-slider-'+t.control+'"><div class="minicolors-slider minicolors-sprite"><div class="minicolors-picker"></div></div><div class="minicolors-opacity-slider minicolors-sprite"><div class="minicolors-picker"></div></div><div class="minicolors-grid minicolors-sprite"><div class="minicolors-grid-inner"></div><div class="minicolors-picker"><div></div></div></div></div>'),t.inline||(i.after('<span class="minicolors-swatch minicolors-sprite"><span class="minicolors-swatch-color"></span></span>'),i.next(".minicolors-swatch").on("click",function(t){t.preventDefault(),i.focus()})),i.parent().find(".minicolors-panel").on("selectstart",function(){return!1}).end(),t.inline&&i.parent().addClass("minicolors-inline"),e(i,!1),i.data("minicolors-initialized",!0))}function t(i){var t=i.parent();i.removeData("minicolors-initialized").removeData("minicolors-settings").removeProp("size").removeClass("minicolors-input"),t.before(i).remove()}function o(i){var t=i.parent(),o=t.find(".minicolors-panel"),s=i.data("minicolors-settings");!i.data("minicolors-initialized")||i.prop("disabled")||t.hasClass("minicolors-inline")||t.hasClass("minicolors-focus")||(n(),t.addClass("minicolors-focus"),o.stop(!0,!0).fadeIn(s.showSpeed,function(){s.show&&s.show.call(i.get(0))}))}function n(){$(".minicolors-focus").each(function(){var i=$(this),t=i.find(".minicolors-input"),o=i.find(".minicolors-panel"),n=t.data("minicolors-settings");o.fadeOut(n.hideSpeed,function(){n.hide&&n.hide.call(t.get(0)),i.removeClass("minicolors-focus")})})}function s(i,t,o){var n=i.parents(".minicolors").find(".minicolors-input"),s=n.data("minicolors-settings"),e=i.find("[class$=-picker]"),r=i.offset().left,c=i.offset().top,l=Math.round(t.pageX-r),h=Math.round(t.pageY-c),d=o?s.animationSpeed:0,u,g,m,p;t.originalEvent.changedTouches&&(l=t.originalEvent.changedTouches[0].pageX-r,h=t.originalEvent.changedTouches[0].pageY-c),0>l&&(l=0),0>h&&(h=0),l>i.width()&&(l=i.width()),h>i.height()&&(h=i.height()),i.parent().is(".minicolors-slider-wheel")&&e.parent().is(".minicolors-grid")&&(u=75-l,g=75-h,m=Math.sqrt(u*u+g*g),p=Math.atan2(g,u),0>p&&(p+=2*Math.PI),m>75&&(m=75,l=75-75*Math.cos(p),h=75-75*Math.sin(p)),l=Math.round(l),h=Math.round(h)),i.is(".minicolors-grid")?e.stop(!0).animate({top:h+"px",left:l+"px"},d,s.animationEasing,function(){a(n,i)}):e.stop(!0).animate({top:h+"px"},d,s.animationEasing,function(){a(n,i)})}function a(i,t){function o(i,t){var o,n;return i.length&&t?(o=i.offset().left,n=i.offset().top,{x:o-t.offset().left+i.outerWidth()/2,y:n-t.offset().top+i.outerHeight()/2}):null}var n,s,a,e,c,l,d,g=i.val(),m=i.attr("data-opacity"),f=i.parent(),v=i.data("minicolors-settings"),b=f.find(".minicolors-swatch"),y=f.find(".minicolors-grid"),M=f.find(".minicolors-slider"),w=f.find(".minicolors-opacity-slider"),x=y.find("[class$=-picker]"),C=M.find("[class$=-picker]"),k=w.find("[class$=-picker]"),S=o(x,y),z=o(C,M),D=o(k,w);if(t.is(".minicolors-grid, .minicolors-slider")){switch(v.control){case"wheel":e=y.width()/2-S.x,c=y.height()/2-S.y,l=Math.sqrt(e*e+c*c),d=Math.atan2(c,e),0>d&&(d+=2*Math.PI),l>75&&(l=75,S.x=69-75*Math.cos(d),S.y=69-75*Math.sin(d)),s=u(l/.75,0,100),n=u(180*d/Math.PI,0,360),a=u(100-Math.floor(z.y*(100/M.height())),0,100),g=p({h:n,s:s,b:a}),M.css("backgroundColor",p({h:n,s:s,b:100}));break;case"saturation":n=u(parseInt(S.x*(360/y.width()),10),0,360),s=u(100-Math.floor(z.y*(100/M.height())),0,100),a=u(100-Math.floor(S.y*(100/y.height())),0,100),g=p({h:n,s:s,b:a}),M.css("backgroundColor",p({h:n,s:100,b:a})),f.find(".minicolors-grid-inner").css("opacity",s/100);break;case"brightness":n=u(parseInt(S.x*(360/y.width()),10),0,360),s=u(100-Math.floor(S.y*(100/y.height())),0,100),a=u(100-Math.floor(z.y*(100/M.height())),0,100),g=p({h:n,s:s,b:a}),M.css("backgroundColor",p({h:n,s:s,b:100})),f.find(".minicolors-grid-inner").css("opacity",1-a/100);break;default:n=u(360-parseInt(z.y*(360/M.height()),10),0,360),s=u(Math.floor(S.x*(100/y.width())),0,100),a=u(100-Math.floor(S.y*(100/y.height())),0,100),g=p({h:n,s:s,b:a}),y.css("backgroundColor",p({h:n,s:100,b:100}))}i.val(h(g,v.letterCase))}t.is(".minicolors-opacity-slider")&&(m=v.opacity?parseFloat(1-D.y/w.height()).toFixed(2):1,v.opacity&&i.attr("data-opacity",m)),b.find("SPAN").css({backgroundColor:g,opacity:m}),r(i,g,m)}function e(i,t){var o,n,s,a,e,c,l,g=i.parent(),m=i.data("minicolors-settings"),v=g.find(".minicolors-swatch"),b=g.find(".minicolors-grid"),y=g.find(".minicolors-slider"),M=g.find(".minicolors-opacity-slider"),w=b.find("[class$=-picker]"),x=y.find("[class$=-picker]"),C=M.find("[class$=-picker]");switch(o=h(d(i.val(),!0),m.letterCase),o||(o=h(d(m.defaultValue,!0),m.letterCase)),n=f(o),t||i.val(o),m.opacity&&(s=""===i.attr("data-opacity")?1:u(parseFloat(i.attr("data-opacity")).toFixed(2),0,1),isNaN(s)&&(s=1),i.attr("data-opacity",s),v.find("SPAN").css("opacity",s),e=u(M.height()-M.height()*s,0,M.height()),C.css("top",e+"px")),v.find("SPAN").css("backgroundColor",o),m.control){case"wheel":c=u(Math.ceil(.75*n.s),0,b.height()/2),l=n.h*Math.PI/180,a=u(75-Math.cos(l)*c,0,b.width()),e=u(75-Math.sin(l)*c,0,b.height()),w.css({top:e+"px",left:a+"px"}),e=150-n.b/(100/b.height()),""===o&&(e=0),x.css("top",e+"px"),y.css("backgroundColor",p({h:n.h,s:n.s,b:100}));break;case"saturation":a=u(5*n.h/12,0,150),e=u(b.height()-Math.ceil(n.b/(100/b.height())),0,b.height()),w.css({top:e+"px",left:a+"px"}),e=u(y.height()-n.s*(y.height()/100),0,y.height()),x.css("top",e+"px"),y.css("backgroundColor",p({h:n.h,s:100,b:n.b})),g.find(".minicolors-grid-inner").css("opacity",n.s/100);break;case"brightness":a=u(5*n.h/12,0,150),e=u(b.height()-Math.ceil(n.s/(100/b.height())),0,b.height()),w.css({top:e+"px",left:a+"px"}),e=u(y.height()-n.b*(y.height()/100),0,y.height()),x.css("top",e+"px"),y.css("backgroundColor",p({h:n.h,s:n.s,b:100})),g.find(".minicolors-grid-inner").css("opacity",1-n.b/100);break;default:a=u(Math.ceil(n.s/(100/b.width())),0,b.width()),e=u(b.height()-Math.ceil(n.b/(100/b.height())),0,b.height()),w.css({top:e+"px",left:a+"px"}),e=u(y.height()-n.h/(360/y.height()),0,y.height()),x.css("top",e+"px"),b.css("backgroundColor",p({h:n.h,s:100,b:100}))}i.data("minicolors-initialized")&&r(i,o,s)}function r(i,t,o){var n=i.data("minicolors-settings"),s=i.data("minicolors-lastChange");s&&s.hex===t&&s.opacity===o||(i.data("minicolors-lastChange",{hex:t,opacity:o}),n.change&&(n.changeDelay?(clearTimeout(i.data("minicolors-changeTimeout")),i.data("minicolors-changeTimeout",setTimeout(function(){n.change.call(i.get(0),t,o)},n.changeDelay))):n.change.call(i.get(0),t,o)),i.trigger("change").trigger("input"))}function c(i){var t=d($(i).val(),!0),o=b(t),n=$(i).attr("data-opacity");return o?(void 0!==n&&$.extend(o,{a:parseFloat(n)}),o):null}function l(i,t){var o=d($(i).val(),!0),n=b(o),s=$(i).attr("data-opacity");return n?(void 0===s&&(s=1),t?"rgba("+n.r+", "+n.g+", "+n.b+", "+parseFloat(s)+")":"rgb("+n.r+", "+n.g+", "+n.b+")"):null}function h(i,t){return"uppercase"===t?i.toUpperCase():i.toLowerCase()}function d(i,t){return i=i.replace(/[^A-F0-9]/gi,""),3!==i.length&&6!==i.length?"":(3===i.length&&t&&(i=i[0]+i[0]+i[1]+i[1]+i[2]+i[2]),"#"+i)}function u(i,t,o){return t>i&&(i=t),i>o&&(i=o),i}function g(i){var t={},o=Math.round(i.h),n=Math.round(255*i.s/100),s=Math.round(255*i.b/100);if(0===n)t.r=t.g=t.b=s;else{var a=s,e=(255-n)*s/255,r=(a-e)*(o%60)/60;360===o&&(o=0),60>o?(t.r=a,t.b=e,t.g=e+r):120>o?(t.g=a,t.b=e,t.r=a-r):180>o?(t.g=a,t.r=e,t.b=e+r):240>o?(t.b=a,t.r=e,t.g=a-r):300>o?(t.b=a,t.g=e,t.r=e+r):360>o?(t.r=a,t.g=e,t.b=a-r):(t.r=0,t.g=0,t.b=0)}return{r:Math.round(t.r),g:Math.round(t.g),b:Math.round(t.b)}}function m(i){var t=[i.r.toString(16),i.g.toString(16),i.b.toString(16)];return $.each(t,function(i,o){1===o.length&&(t[i]="0"+o)}),"#"+t.join("")}function p(i){return m(g(i))}function f(i){var t=v(b(i));return 0===t.s&&(t.h=360),t}function v(i){var t={h:0,s:0,b:0},o=Math.min(i.r,i.g,i.b),n=Math.max(i.r,i.g,i.b),s=n-o;return t.b=n,t.s=0!==n?255*s/n:0,t.h=0!==t.s?i.r===n?(i.g-i.b)/s:i.g===n?2+(i.b-i.r)/s:4+(i.r-i.g)/s:-1,t.h*=60,t.h<0&&(t.h+=360),t.s*=100/255,t.b*=100/255,t}function b(i){return i=parseInt(i.indexOf("#")>-1?i.substring(1):i,16),{r:i>>16,g:(65280&i)>>8,b:255&i}}$.minicolors={defaults:{animationSpeed:50,animationEasing:"swing",change:null,changeDelay:0,control:"hue",dataUris:!0,defaultValue:"",hide:null,hideSpeed:100,inline:!1,letterCase:"lowercase",opacity:!1,position:"bottom left",show:null,showSpeed:100,theme:"default"}},$.extend($.fn,{minicolors:function(s,a){switch(s){case"destroy":return $(this).each(function(){t($(this))}),$(this);case"hide":return n(),$(this);case"opacity":return void 0===a?$(this).attr("data-opacity"):($(this).each(function(){e($(this).attr("data-opacity",a))}),$(this));case"rgbObject":return c($(this),"rgbaObject"===s);case"rgbString":case"rgbaString":return l($(this),"rgbaString"===s);case"settings":return void 0===a?$(this).data("minicolors-settings"):($(this).each(function(){var i=$(this).data("minicolors-settings")||{};t($(this)),$(this).minicolors($.extend(!0,i,a))}),$(this));case"show":return o($(this).eq(0)),$(this);case"value":return void 0===a?$(this).val():($(this).each(function(){e($(this).val(a))}),$(this));default:return"create"!==s&&(a=s),$(this).each(function(){i($(this),a)}),$(this)}}}),$(document).on("mousedown.minicolors touchstart.minicolors",function(i){$(i.target).parents().add(i.target).hasClass("minicolors")||n()}).on("mousedown.minicolors touchstart.minicolors",".minicolors-grid, .minicolors-slider, .minicolors-opacity-slider",function(i){var t=$(this);i.preventDefault(),$(document).data("minicolors-target",t),s(t,i,!0)}).on("mousemove.minicolors touchmove.minicolors",function(i){var t=$(document).data("minicolors-target");t&&s(t,i)}).on("mouseup.minicolors touchend.minicolors",function(){$(this).removeData("minicolors-target")}).on("mousedown.minicolors touchstart.minicolors",".minicolors-swatch",function(i){var t=$(this).parent().find(".minicolors-input");i.preventDefault(),o(t)}).on("focus.minicolors",".minicolors-input",function(){var i=$(this);i.data("minicolors-initialized")&&o(i)}).on("blur.minicolors",".minicolors-input",function(){var i=$(this),t=i.data("minicolors-settings");i.data("minicolors-initialized")&&(i.val(d(i.val(),!0)),""===i.val()&&i.val(d(t.defaultValue,!0)),i.val(h(i.val(),t.letterCase)))}).on("keydown.minicolors",".minicolors-input",function(i){var t=$(this);if(t.data("minicolors-initialized"))switch(i.keyCode){case 9:n();break;case 13:case 27:n(),t.blur()}}).on("keyup.minicolors",".minicolors-input",function(){var i=$(this);i.data("minicolors-initialized")&&e(i,!0)}).on("paste.minicolors",".minicolors-input",function(){var i=$(this);i.data("minicolors-initialized")&&setTimeout(function(){e(i,!0)},1)})}(jQuery);

	});
</script>

</body>
</html>
