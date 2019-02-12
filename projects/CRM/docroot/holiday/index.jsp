<%@ page import="java.text.*, java.util.*" %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.content.meal.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<tmpl:insert template='/template/top_nav.jsp'>
	
	<tmpl:put name='title' direct='true'>/ FD CRM : Holiday Order /</tmpl:put>

<%!	
    SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
    SimpleDateFormat longDateFormatter = new SimpleDateFormat("EEEEEEEE, MMMMMMMM d, yyyy");
    Comparator deliveryComparator = new Comparator() {
        public int compare(Object o1, Object o2) {
            MealModel m1 = (MealModel) o1;
            MealModel m2 = (MealModel) o2;
            if (m1.getDelivery().before(m2.getDelivery())) {
                return -1;
            } else if (m1.getDelivery().after(m2.getDelivery())) {
                return 1;
            } else {
                return 0;
            }
        }
    };
%>

<tmpl:put name='content' direct='true'>
<br><br><div align="center">...Holiday Orders Coming Soon...</div>

</tmpl:put>

</tmpl:insert>


