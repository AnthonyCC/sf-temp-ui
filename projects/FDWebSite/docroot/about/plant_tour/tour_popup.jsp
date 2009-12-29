<%@ page import='com.freshdirect.fdstore.content.*'  %>
<%@ page import='com.freshdirect.fdstore.attributes.*'  %>

<%@ page import='com.freshdirect.fdstore.customer.*'  %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='template' prefix='tmpl' %>


<%
String deptName = request.getParameter("deptName");
String imgNum = request.getParameter("imgNum");
String maxNum = request.getParameter("maxNum");
String imgSrc = "/media_stat/images/template/about/plant_tour/";
String currImgSrc = "";

int currNum;
int deptMaxNum;

try {

currNum = Integer.parseInt(imgNum);
deptMaxNum = Integer.parseInt(maxNum);

} catch (NumberFormatException Ex){
	currNum = 1;
	deptMaxNum = 1;
	//
}

imgSrc += deptName + "/" + deptName + "_z_";

currImgSrc = imgSrc + currNum + ".jpg";

%>

<tmpl:insert template='/common/template/small_pop.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - A Photographic Tour</tmpl:put>
		<tmpl:put name='content' direct='true'>
<script>
var imgNum = <%=imgNum%>;
var maxNum = <%=deptMaxNum%>;
var imgSrc = "<%=imgSrc%>";

function buildImgSrc (numDirection) {
	if ("next" == numDirection){
		imgNum++;
		if (imgNum > maxNum) imgNum = 1;
	}
	
	if ("prev" == numDirection){
		imgNum--;
		if (imgNum < 1) imgNum = maxNum;
	}

	return (imgSrc + imgNum + ".jpg");
}
</script>


<div align="center"><a href="javascript:window.close();"><img src="<%=currImgSrc%>" width="235" height="294" name="deptImg" border="0"></a>
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="10"><br><span class="text12"><a href="javascript:swapImage('deptImg',buildImgSrc('prev'));">< previous image</a> | <a href="javascript:swapImage('deptImg',buildImgSrc('next'));">next image ></a>
</span><br><img src="/media_stat/images/layout/clear.gif" width="1" height="18"></div>
	</tmpl:put>
</tmpl:insert>


