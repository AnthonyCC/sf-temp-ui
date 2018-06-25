<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy"%>

<!DOCTYPE html>
<html lang="en-US" xml:lang="en-US">
<head>
    <meta charset="UTF-8">
    <fd:SEOMetaTag title="FreshDirect Carousels"/>
    <%@ include file="/common/template/includes/i_javascripts.jspf" %>
    <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
    <%@ include file="/shared/template/includes/i_head_end.jspf" %>
</head>
<body>
    <header
        <img alt="freshdirect" src="/media_stat/images/template/quickshop/9managers_s.jpg">
        <img alt="freshdirect" height="125" src="/media/images/navigation/department/local/dept_icons/dpt_local_whoslocal_map.gif">
        <h1>Carousels</h1>
    </header>

    <soy:import packageName="common" />
    <soy:import packageName="srch" />

    <form method="get" id="carousel_form">
        <select name="type">
            <option value="search">Search carousel</option>
            <option value="pres-picks">President picks carousel</option>
        </select>
        <input type="text" name="productId" value="productId">
        <input type="submit" value="Submit">
        <div class="carousel"></div>
    </form>

    <script>
        $jq(carousel_form).submit(function(event) {
            event.preventDefault();
            var type = $(this)[0].value;
            var queryParams = 'type=' + type;
            if (type==='search'){
                queryParams += '&productId=' + $(this)[1].value;
            }
            $jq.ajax('/api/carousel?' + queryParams).then(function(data) {
                $jq('.carousel').html(srch.carouselWrapper(data.carousels));
            });
        });
    </script>

</body>
</html>
