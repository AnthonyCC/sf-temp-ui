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
		function oas_loader(afterLoad) {
			var oasTestSpots = 'SystemMessage,CategoryNote,BrowseTop1,BrowseTop2,BrowseTop3,BrowseBottom1,BrowseBottom2';
			var oasTestScript = '<script src="http://devpromo.freshdirect.com/RealMedia/ads/adstream_mjx.ads/www.freshdirect.com/kitchen/1657369449@'+oasTestSpots+'?CosFreeDelTest=&baf=0&brand=&camp=fallcamp2&cart=veg&cohort=C1&county=KINGS&ct=1&cti=&cts=WeeklyTest&depot=&do=346&dp=MKT0072950&dpar=n&dpas=&dpcamp=&ecp=1&ecpoc=29&ecppromo=&enteredCampn=OasDefault%2FHPFeatureTop_SweepStakes%2COasDefault%2FSpecialTopbar_SweepStakes_Week1&expd=0&hv=1&id=kitchen&lotype=h&lozn=560&lu=2&mktpro=&mzid=0000100000&nod=2014-07-24&og=&oim=0&pt=&recipe=true&ref_prog_id=&ret=&state=NY&sub=1.99&sv=1988&szid=0000200201&test=false&tofurkey=&type=home&v=4&win=2&wnbk=&zid=0000200201&zip=11231&zonelevel=true"><\/script>';
			if (afterLoad) {
				//dynamic id and spots
				oasTestSpots = $jq('#oasTestSpots').val();
				oasTestScript = '<script src="http://devpromo.freshdirect.com/RealMedia/ads/adstream_mjx.ads/www.freshdirect.com/'+$jq('#oasTest_dept').val()+'/1498860699@'+oasTestSpots+'?CosFreeDelTest=&baf=0&brand=&camp=fallcamp2&cart=veg&cohort=C1&county=KINGS&ct=1&cti=&cts=WeeklyTest&depot=&do=346&dp=MKT0072950&dpar=n&dpas=&dpcamp=&ecp=1&ecpoc=29&ecppromo=&enteredCampn=OasDefault%2FHPFeatureTop_SweepStakes%2COasDefault%2FSpecialTopbar_SweepStakes_Week1&expd=0&hv=1&id=fro&lotype=h&lozn=560&lu=2&mktpro=&mzid=0000100000&nod=2014-07-24&og=&oim=0&pt=&recipe=true&ref_prog_id=&ret=&state=NY&sub=1.99&sv=1988&szid=0000200201&test=false&tofurkey=&type=home&v=4&win=2&wnbk=&zid=0000200201&zip=11231&zonelevel=true"><\/script>';
			}
			postscribe('#oas_contents', oasTestScript, 
					{
						done: function() {
							//empty pre-existing spots (it appends)
							$jq('#oas_contents').empty();
							$jq(oasTestSpots.split(',')).each(function(i,e) {
								var cur = e.trim();
								if (cur !== '') {
									$jq('#oas_contents').append('<div style="text-align: left;">'+cur+':</div><div id="oas_'+cur+'"></div>');
									postscribe('#oas_'+cur, '<script>OAS_RICH("'+cur+'")<\/script>');
								}
							});
						}
					});
			
		}
		
		$jq(function() {
			oas_loader();
		});
		</script>
		<style>
			.testOasCont td, .testOasCont th {
				font-size: 14px;
			}
			.testOasCont th {
				padding-right: 20px;
			}
		</style>
</head>
	<body>
    <div id="content">
    <center>
      <!-- content lands here -->
		<hr />
		<div style="margin-top: 10px; border-top: 1px solid #ccc;" class="testOasCont">
			<table>
				<tr>
					<th rowspan="2" style="font-weight: bold;" width="150px;">Test After Page Has Loaded</th>
					<td>Fetch OAS from this Id : </td>
					<td><input id="oasTest_dept" value="fro" style="text-align: center; font-weight: bold;" /></td>
					<td rowspan="2" valign="middle" style="padding: 10px;">
						<button id="oasAjaxtest" style="padding: 10px;">Test</button>
					</td>
				</tr>
				<tr>
					<td>Use these spots : </td>
					<td><textarea id="oasTestSpots" style="font-weight: bold; font-size: 12px; width: 400px; height: 32px; word-wrap: break-word;">SystemMessage,CategoryNote,BrowseTop1,BrowseTop2,BrowseTop3,BrowseBottom1,BrowseBottom2</textarea></td>
				</tr>
			</table>
			
			
		</div>
		<div style="margin-bottom: 10px; border-bottom: 1px solid #ccc;">This is some other static content</div>
		
		<div id="oas_contents"></div>
		<hr />
		<div style="margin-top: 10px; border-top: 1px solid #ccc;">This is also some other static content</div>
		
		
		<script type="text/javascript">
			$jq('#oasAjaxtest').click(function(event) {
				oas_loader(true);
			});
			
		</script>


      <!-- content ends above here-->
      </center>
    </div>
    <%@ include file="/common/template/includes/i_jsmodules_optimized.jspf" %>
	</body>
</html> 
