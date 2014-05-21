<#assign botNav="" />
<#if parameters["botNav"]??><#assign botNav=parameters["botNav"] /></#if>
<#assign hasMyFD="false" />
<#if parameters["hasMyFD"]??><#assign hasMyFD=parameters["hasMyFD"] /></#if>
<#assign careerLink="" />
<#if parameters["careerLink"]??><#assign careerLink=parameters["careerLink"] /></#if>
<#assign giftPath="" />
<#if parameters["giftPath"]??><#assign giftPath=parameters["giftPath"] /></#if>

<#assign catId="" />
<#if parameters["catId"]??><#assign catId=parameters["catId"] /></#if>
<#assign deptId="" />
<#if parameters["deptId"]??><#assign deptId=parameters["deptId"] /></#if>

<#assign validAboutUs = ["about_overview", "about_overview_ourstory", "about_overview_ourpassion", "about_overview_environment", "about_overview_ourteam", "about_ourteam_ldrship", "about_overview_ourcustomers", "about_overview_partners", "about_press_contact"] />
<#if catId != "" && validAboutUs?seq_contains(catId?string) == true >
		<#assign botNav="aboutus" />
</#if>

<style>
.gnav-footer{
width: 968px;
height: 54px;
	background-color: #88a75c;
font: 18px/54px 'TradeGothic', 'Trebuchet MS', Trebuchet, 'Lucida Grande', sans-serif;
color: #fff;
       text-shadow: 1px 1px 0 rgb(116,145,76);
margin:0 auto;
}

.gnav-footer .link,
.gnav-footer,
.gnav-footer .dot:after{
  line-height:54px;
}

.gnav-footer .link{
  text-decoration:none;
  padding:0;
  margin:0;
  color:#FFF;  
  display:inline-block;
  vertical-align:top;
  padding:0px 4px;
  font-size:18px;
  font-weight:normal;
}

.gnav-footer .link:hover{
  background-color:#728d4b;
}

.gnav-footer.shadow-above{
  -webkit-box-shadow: 0px -2px 3px 0px rgba(79, 79, 79, 0.68);
  -moz-box-shadow:    0px -2px 3px 0px rgba(79, 79, 79, 0.68);
  box-shadow:         0px -2px 3px 0px rgba(79, 79, 79, 0.68);
}

.gnav-footer .dot:after{
  content:'\00B7';
}

.gnav-footer .right{
  float:right;
}

.gnav-footer .first{
  padding-left:9px;
}

.gnav-footer .last{
  padding-right:9px;
}
</style>

<div class="gnav-footer shadow-above">
  <a class="link first" href="/department.jsp?deptId=about&trk=bnav">About Us</a>
  <a class="link" href="/category.jsp?catId=food_safety_freshdirect&trk=bnav">Food Safety</a>
  <a class="link" href="${careerLink}">Careers</a>
  <!-- right side -->
  <span class="right">
    <a class="link" href="/department.jsp?deptId=COS&trk=bnav">At The Office</a>
    <a class="link" href="/department.jsp?deptId=rec&trk=gnav">Recipes</a>
    <a class="link" href="/myfd/index.jsp?trk=bnav">MYFD</a>
    <a class="link" href="${giftPath}">Gift Card</a>
    <a  class="link last" href="/newproducts.jsp?trk=bnav">New Products</a>
  </span>
</div>
</div>
