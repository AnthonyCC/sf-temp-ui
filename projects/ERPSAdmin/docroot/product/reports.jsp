<%@ taglib uri='template' prefix='tmpl' %>

<tmpl:insert template='/common/templates/main.jsp'>
    <tmpl:put name='title' content='ERPSY Daisy: Reports' direct='true'/>

     <tmpl:put name='content' direct='true'>
        <center>
            <a href="../nutrition_report.xls">Nutrition Report</a><br>
            <br>
           <!-- <a href="claims_report.jsp">Claims Report</a><br> -->
        </center>
     </tmpl:put>
</tmpl:insert>