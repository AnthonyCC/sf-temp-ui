<%@ page import='com.freshdirect.fdstore.FDStoreProperties, java.util.*, com.freshdirect.framework.util.NVL, com.freshdirect.fdstore.sempixel.FDSemPixelCache,
com.freshdirect.fdstore.sempixel.SemPixelModel, java.net.URL' %><%@ taglib uri='freshdirect' prefix='fd' %>
<% String semPixelNames = NVL.apply(request.getParameter("pixelNames"), ""); %>
<fd:CheckLoginStatus pixelNames="<%= semPixelNames %>" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title> SEM Project Test Page </title>
		<style>
			.pageTitle {
				font-weight: bold;
				font-size: 14px;
			}
			.title {
				font-weight: bold;
			}
			.empty {
				font-weight: bold;
				font-style: italic;
				color: #ccc;
			}
			.smallText {
				font-size: 11px;
			}
			hr {
				border: 1px solid #ccc;
			}
			.pageTitleHR {
				border: 1px solid #333;
			}
		</style>
	</head>
<body>
<%
	//force refresh property file
	FDStoreProperties.forceRefresh();

	out.print("<span class=\"pageTitle\">SEM Pixel Test Page</span>");

	out.print("<pre>");
	
	out.print("<hr class=\"pageTitleHR\" />");
	out.println("Set pixelNames this page will use via a queryParam (sem.jsp?pixelNames=Pixel1,Pixel2). By default, no pixelNames are used.");
	out.println("<hr class=\"pageTitleHR\" />");

	String emptyText = "<span class=\"empty\">(Empty)</span>";
	
	String semPixels = FDStoreProperties.getSemPixels();
	String semConfigs = FDStoreProperties.getSemConfigs();
	String semRefreshPeriod = Integer.toString(FDStoreProperties.getSemPixelRefreshPeriod());
	
	semPixels = ("".equals(semPixels)) ? emptyText : semPixels;
	semConfigs = ("".equals(semConfigs)) ? emptyText : semConfigs;
	
	out.println("<span class=\"title\">SEM Prop (fdstore.sem.pixels): </span>"+semPixels);
	out.println("<span class=\"title\">SEM Prop (fdstore.sem.configs): </span>"+semConfigs);
	out.println("<span class=\"title\">SEM Prop (fdstore.sem.refresh): </span>"+semRefreshPeriod);

	semPixels = (semPixels.equals(semPixels)) ? "" : semPixels;
	semConfigs = (semConfigs.equals(emptyText)) ? "" : semConfigs;
	
	out.println("<span class=\"title\">SEM SemPixelCache: </span>");
	FDSemPixelCache spc = FDSemPixelCache.getInstance();
	Collection keys = spc.getCachedSemPixels().keySet();
		
	//test adding to cache outside of property
	spc.addSemPixelToCache("testPagePixel1", new SemPixelModel());
	//test adding to cache outside of property
	spc.addSemPixelToCache("testPagePixel2", new SemPixelModel());
	spc.removeSemPixelFromCache("testPagePixel2");

	String semPixelModelLayout = "SemPixelModel[";
		semPixelModelLayout += "\n\tname";
		semPixelModelLayout += "\n\tenabled";
		semPixelModelLayout += "\n\tvalidReferers";
		semPixelModelLayout += "\n\tvalidZipCodes";
		semPixelModelLayout += "\n\tmediaPath";
		semPixelModelLayout += "\n\tlastModifiedDate";
		semPixelModelLayout += "\n\tparams";
	semPixelModelLayout += "\n]";
	
	out.println("<table style=\"text-align: left;\">");
    for(Object key: keys){
    	out.println("<tr><td>&nbsp;&nbsp;&nbsp;</td><th class=\"smallText\">"+semPixelModelLayout+"</th><td>&nbsp;</td>");
    	out.println("<td valign=\"top\" class=\"smallText\">"+spc.getSemPixel((String) key).toString()+"</td></tr>");
    }
	out.println("</table>");

	out.println("Along with the pixels loaded from the property, you should also see 'testPagePixel1'. This pixel is loaded from the test page.");
	out.println("You should <b>not</b> see 'testPagePixel2'. This pixel is added and then removed from the cache by the test page.\n");

	String[] sem_zipParse = new String[0];
	String sem_referer = NVL.apply(request.getHeader("referer"), "null");
	String sem_zipCode = "";
	
	if ( !"".equals(sem_referer) && !"null".equals(sem_referer) ) {
		URL sem_refUrl = new URL(sem_referer);
		sem_referer = sem_refUrl.getHost();

		//parse out zip code from referer params
		String sem_refererQueryParams = NVL.apply(sem_refUrl.getQuery(), "");
		
		if (!"".equals(sem_refererQueryParams)) {
			sem_zipParse = sem_refererQueryParams.split("&");
			
			for (int m = 0; m < sem_zipParse.length; m++) {
				StringTokenizer sem_stQueryParams = new StringTokenizer(sem_zipParse[m], "=");
				while(sem_stQueryParams.hasMoreTokens()) {
					String key = sem_stQueryParams.nextToken();
					String val = sem_stQueryParams.nextToken();
					
					//we only care about zip code here
					if ("zipcode".equals(key)) {
						sem_zipCode = val;
					}
				}
			}
		}
	}
	
	

	sem_referer = ("".equals(sem_referer) || "null".equals(sem_referer)) ? emptyText : sem_referer;
	sem_zipCode = ("".equals(sem_zipCode)) ? emptyText : sem_zipCode;
	semPixelNames = ("".equals(semPixelNames)) ? emptyText : semPixelNames;
	
	out.println("<span class=\"title\">SEM Referer: </span>"+sem_referer);
	out.println("<span class=\"title\">SEM Referer Zip: </span>"+sem_zipCode);
	out.println("<span class=\"title\">SEM CheckLoginStatus pixelNames: </span>"+semPixelNames);
	
	semPixelNames = (semPixelNames.equals(emptyText)) ? "" : semPixelNames;
	
	out.println("<span class=\"title\">Media Include example 1 (&lt;fd:SemPixelIncludeMedia pixelNames=\"*\" /&gt;): </span>");
	out.print("<hr />");
		%><fd:SemPixelIncludeMedia pixelNames="*" /><%
	out.println("<hr />");
		
	out.println("<span class=\"title\">Media Include example 2 (&lt;fd:SemPixelIncludeMedia pixelNames=\""+semPixelNames+"\" /&gt;): </span>");
	out.print("<hr />");
		%><fd:SemPixelIncludeMedia pixelNames="<%= semPixelNames %>" /><%
	out.println("<hr />");
		
	out.println("</pre>");
%>
</body>
</html>
