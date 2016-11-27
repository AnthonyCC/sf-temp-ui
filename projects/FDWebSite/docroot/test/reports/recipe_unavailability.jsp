<%@ page import="java.text.NumberFormat" %>
<%@ page import="com.freshdirect.cms.*" %>
<%@ page import="com.freshdirect.cms.application.*" %>
<%@ page import="com.freshdirect.fdstore.content.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.fdstore.attributes.*" %>
<%@ page import='java.text.*, java.util.*' %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%!	
	SimpleDateFormat timeFormat = new SimpleDateFormat("MM.dd.yyyy hh:mm a");

    /**
     *  A class to compare two recipes based on their production
     *  status. The 'more active' a recipe is, the 'lower' the
     *  comparator puts it.
     */
    public class RecipeProductionStatusComporator implements Comparator {
        /**
         *  Compare two recipes. The ordering of the recipes based on their
         *  production status is as follows, with highest first:
         *
         *  <ul>
         *      <li>completed</li>
         *      <li>pending</li>
         *      <li>limited</li>
         *      <li>active</li>
         *  </ul>
         *
         *  @param o1 one of the recipes to compare, must be an object
         *            of type Recipe
         *  @param o2 the other of the recipes to compare, must be an object
         *            of type Recipe
         *  @return a negative number if the first recipe is 'lower' in
         *          terms of production status,
         *          zero, if the two recipes are of equal production status,
         *          a positive number if the first is 'higher' in terms of
         *          production status
         *  @throws ClassCastException of at least one of the parameters is
         *          not a recipe.
         */
        public int compare(Object o1, Object o2) {

            if (!(o1 instanceof Recipe) || !(o2 instanceof Recipe)) {
                throw new ClassCastException();
            }

            Recipe  recipe1  = (Recipe) o1;
            Recipe  recipe2  = (Recipe) o2;
            String  pStatus1 = recipe1.getProductionStatus();
            String  pStatus2 = recipe2.getProductionStatus();

            if (pStatus1.equals(pStatus2)) {
                return 0;
            }

            if (EnumProductionStatus.ACTIVE.equals(pStatus1)) {
                return -1;
            }

            if (EnumProductionStatus.LIMITED.equals(pStatus1)) {
                if (EnumProductionStatus.ACTIVE.equals(pStatus2)) {
                    return 1;
                } else {
                    return -1;
                }
            }

            if (EnumProductionStatus.PENDING.equals(pStatus1)) {
                if (EnumProductionStatus.COMPLETED.equals(pStatus2)) {
                    return -1;
                } else {
                    return 1;
                }
            }

            return 1;
        }

        /**
         *  Compare this comparator to another one.
         *
         *  @param object the other object to compare to.
         *  @return true if the other object is a comparator equal to this
         *          one,
         *          false otherwise.
         */
        public boolean equals(Object object) {
            return object instanceof RecipeProductionStatusComporator;
        }
    }
%>
<%
	ContentType recipeType    = ContentType.get("Recipe");
	Set         allRecipeKeys = CmsManager.getInstance().getContentKeysByType(recipeType);

%>
<html>
<head>
<title>/ FD Product Unavailability Report /</title>
</head>
<body>
<div style="width: 100%; height: auto; padding-bottom: 4px; border-bottom: solid 1px #000000;">
<table width="100%">
	<tr valign="bottom">
		<td width="20%"><a href="?">Overview & Details</a></td>
		<td width="60%" class="report_header" align="center">FreshDirect Recipe Unavailability Report</td>
	</tr>
</table>
</div>
<div id="overview" class="overview">
	<table width="95%" cellpadding="4" cellspacing="0" border="0" align="center">
		<tr align="center">
			<td>Recipe Name</td>
			<td>Recipe Id</td>
			<td>Production Status</td>
			<td>Current Availability</td>
			<td>Actions</td>
			<td>Date</td>
		</tr>
        <%
            Vector recipes = new Vector();
        %>
		<logic:iterate id="contentKey" indexId="i" collection="<%= allRecipeKeys %>" type="com.freshdirect.cms.ContentKey">
            <%
                Recipe recipe = (Recipe) ContentFactory.getInstance().getContentNode(contentKey.getId());
                recipes.add(recipe);
            %>
        </logic:iterate>
        <%
            // make sure the recipes are sorted according to their production
            // status
            Collections.sort(recipes, new RecipeProductionStatusComporator());
        %>

        <logic:iterate id="recipe" indexId="i" collection="<%= recipes %>" type="com.freshdirect.fdstore.content.Recipe">
            <logic:iterate id="variant" indexId="j" collection="<%= recipe.getVariants() %>" type="com.freshdirect.fdstore.content.RecipeVariant">
                <tr>
                    <td><%= recipe.getFullName() %> &gt; <%= variant.getFullName() %></td>
                    <td><%= recipe.getContentName() %> &gt; <%= variant.getContentName() %></td>
                    <td><%= recipe.getProductionStatus() %></td>
                    <td>
                        <%= variant.isAllAvailable()
                            ? "OK"
                            : variant.isAvailable()
                              ? "Ingredient Unavailable"
                              : "Recipe Unavailable"
                        %>
                    </td>
                    <td>
                        <a href="/tell_a_friend/step_1_compose.jsp?recipeId=<%= recipe.getContentName() %>">Send via e-mail</a> |
                        <a href="http://www1.dev.nyc1.freshdirect.com:8200/recipe.jsp?recipeId=<%= recipe.getContentName() %>&trk=srch">INT</a> |
                        <a href="http://newstage.freshdirect.com/recipe.jsp?recipeId=<%= recipe.getContentName() %>&trk=srch">STAGE</a>
                        <a href="http://www.freshdirect.com/recipe.jsp?recipeId=<%= recipe.getContentName() %>&trk=srch">WWW</a>
                    </td>
                    <td><%= timeFormat.format(new Date()) %></td>
                </tr>
            </logic:iterate>
		</logic:iterate>
	</table>
</div>
<div style="width: 100%; height: 1px; border-top: solid 1px #000000;"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></div>
</body>
</html>
