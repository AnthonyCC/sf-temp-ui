<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>

<potato:qsRecommender />
<soy:render template="common.qsBottomTabbedCarousel" data="${qsBottomPotato}" />