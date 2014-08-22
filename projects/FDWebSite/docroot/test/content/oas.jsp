<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties'%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>OAS Test page</title>
		<%@ include file="/common/template/includes/metatags.jspf" %>
		<%@ include file="/common/template/includes/i_javascripts_optimized.jspf" %>
		
		
		<%@ include file="/shared/template/includes/i_stylesheets_optimized.jspf" %>
		
		
		<script type="text/javascript" src="/assets/javascript/htmlParser/htmlParser.js"></script>
		<script type="text/javascript" src="/assets/javascript/postscribe.min.js"></script>
		<script type="text/javascript">
		// jQuery used as an example of delaying until load.
		$jq(function() {
			postscribe('#oas_contents', '<script src="http://devpromo.freshdirect.com/RealMedia/ads/adstream_mjx.ads/www.freshdirect.com/kitchen/1657369449@SystemMessage,CategoryNote,BrowseTop1,BrowseTop2,BrowseTop3,BrowseBottom1,BrowseBottom2?CosFreeDelTest=&baf=0&brand=&camp=fallcamp2&cart=veg&cohort=C1&county=KINGS&ct=1&cti=&cts=WeeklyTest&depot=&do=346&dp=MKT0072950&dpar=n&dpas=&dpcamp=&ecp=1&ecpoc=29&ecppromo=&enteredCampn=OasDefault%2FHPFeatureTop_SweepStakes%2COasDefault%2FSpecialTopbar_SweepStakes_Week1&expd=0&hv=1&id=kitchen&lotype=h&lozn=560&lu=2&mktpro=&mzid=0000100000&nod=2014-07-24&og=&oim=0&pt=&recipe=true&ref_prog_id=&ret=&state=NY&sub=1.99&sv=1988&szid=0000200201&test=false&tofurkey=&type=home&v=4&win=2&wnbk=&zid=0000200201&zip=11231&zonelevel=true"><\/script>', 
			{
				done: function() {
					postscribe('#oas_SystemMessage', '<script>OAS_RICH("SystemMessage")<\/script>');
					postscribe('#oas_CategoryNote', '<script>OAS_RICH("CategoryNote")<\/script>');
					postscribe('#oas_CategoryNote', '<script>OAS_RICH("BrowseTop1")<\/script>');
					postscribe('#oas_CategoryNote', '<script>OAS_RICH("BrowseTop2")<\/script>');
					postscribe('#oas_CategoryNote', '<script>OAS_RICH("BrowseTop3")<\/script>');
					postscribe('#oas_CategoryNote', '<script>OAS_RICH("BrowseBottom1")<\/script>');
					postscribe('#oas_CategoryNote', '<script>OAS_RICH("BrowseBottom2")<\/script>');
				}
			});
		});
		</script>
</head>
	<body>
    <div id="content">
      <center class="text10">
      <!-- content lands here -->
		<div style="margin-bottom: 10px; border-bottom: 1px solid #ccc;">This is some other content</div>
		
		<div id="oas_contents">
			<div id="oas_SystemMessage"></div>
			<div id="oas_CategoryNote"></div>
			<div id="oas_BrowseTop1"></div>
			<div id="oas_BrowseTop2"></div>
			<div id="oas_BrowseTop3"></div>
			<div id="oas_BrowseBottom1"></div>
			<div id="oas_BrowseBottom2"></div>
		</div>
		<hr />
		<div style="margin-top: 10px; border-top: 1px solid #ccc;">This is also some other content</div>


      <!-- content ends above here-->
      </center>
    </div>
    <%@ include file="/common/template/includes/i_jsmodules_optimized.jspf" %>
	</body>
</html> 
