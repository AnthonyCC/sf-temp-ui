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
     *  A small container class to hold a ConfiguredProduct and the
     *  corresponding Recipe item.
     */
    private class RecipeProductContainer {
        private Recipe            recipe;

        private RecipeVariant     variant;

        private ConfiguredProduct product;

        public RecipeProductContainer(Recipe            recipe,
                                      RecipeVariant     variant,
                                      ConfiguredProduct product) {
            this.recipe  = recipe;
            this.variant = variant;
            this.product = product;
        }

        public Recipe getRecipe() {
            return recipe;
        }
        
        public void setRecipe(Recipe recipe) {
            this.recipe = recipe;
        }

        public RecipeVariant getVariant() {
            return variant;
        }
        
        public void setVariant(RecipeVariant variant) {
            this.variant = variant;
        }

        public ConfiguredProduct getProduct() {
            return product;
        }

        public void setProduct(ConfiguredProduct product) {
            this.product = product;
        }

        public boolean equals(Object object) {
            if (!(object instanceof RecipeProductContainer)) {
                return false;
            }

            RecipeProductContainer rpc = (RecipeProductContainer) object;

            return recipe.equals(rpc.recipe)
                && variant.equals(rpc.variant)
                && product.equals(rpc.product);
        }
    }

    /**
     *  A class to compare two RecipeProductContainers based on the product
     *  availability. The 'more available' a product is, the 'lower' the
     *  comparator puts it.
     */
    public class RecipeProductContainerComparator implements Comparator {
        /**
         *  Compare two RecipeProductContainers.
         *  The order will be done according to availability:
         *
         *  <ul>
         *      <li>unable to determine availability - highest</li>
         *      <li>not available</li>
         *      <li>not available by tomorrow</li>
         *      <li>available - lowest</li>
         *  </ul>
         *
         *  @param o1 one of the RecipeProductContainers to compare
         *            of type Recipe
         *  @param o2 the other of the RecipeProductContainers to compare
         *  @return a negative number if the product in the first container
         *          is more readily availble.
         *          zero, if the two containers hold products is the same
         *          availability
         *          a positive number if the first container holds a product
         *          that is less available than the one in the second container
         *  @throws ClassCastException of at least one of the parameters is
         *          not a RecipeProductContainer.
         */
        public int compare(Object o1, Object o2) {

            if (!(o1 instanceof RecipeProductContainer)
             || !(o2 instanceof RecipeProductContainer)) {
                throw new ClassCastException();
            }

            RecipeProductContainer  rpc1  = (RecipeProductContainer) o1;
            RecipeProductContainer  rpc2  = (RecipeProductContainer) o2;
            int                     availability1 = 0;
            int                     availability2 = 0;

            // guard against empty configured product groups by catching
            // the NullPointerException they throw
            try {
                availability1 = rpc1.getProduct().isAvailableWithin(1)
                              ? 0
                              : rpc1.getProduct().isUnavailable()
                                ? 2
                                : 1;
            } catch (NullPointerException e) {
                return -1;
            }

            try {
                availability2 = rpc2.getProduct().isAvailableWithin(1)
                              ? 0
                              : rpc2.getProduct().isUnavailable()
                                ? 2
                                : 1;
            } catch (NullPointerException e) {
                return 1;
            }

            return availability2 - availability1;
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
            return object instanceof RecipeProductContainerComparator;
        }
    }
%>
<%
	ContentType recipeType    = ContentType.get("Recipe");
	Set         allRecipeKeys = CmsManager.getInstance().getContentKeysByType(recipeType);

%>
<html>
<head>
<title>/ FD CLIG Unavailability Report /</title>
</head>
<body>
<div style="width: 100%; height: auto; padding-bottom: 4px; border-bottom: solid 1px #000000;">
<table width="100%">
	<tr valign="bottom">
		<td width="20%"><a href="?">Overview & Details</a></td>
		<td width="60%" class="report_header" align="center">FreshDirect CLIG Unavailability Report</td>
	</tr>
</table>
</div>
<div id="overview" class="overview">
	<table width="95%" cellpadding="4" cellspacing="0" border="0" align="center">
		<tr align="center">
			<td>CLIG Name</td>
			<td>CLIG Id</td>
			<td>Recipe name</td>
			<td>Current Availability</td>
			<td>Actions</td>
			<td>Date</td>
		</tr>
        <%
            Set distinctProducts = new HashSet();
        %>
		<logic:iterate id         = "contentKey"
                       indexId    = "i"
                       collection = "<%= allRecipeKeys %>"
                       type       = "com.freshdirect.cms.ContentKey"
        >
            <%
                Recipe recipe = (Recipe) ContentFactory.getInstance().getContentNode(contentKey.getId());
            %>
            <logic:iterate id         = "variant"
                           indexId    = "j"
                           collection = "<%= recipe.getVariants() %>"
                           type       = "com.freshdirect.fdstore.content.RecipeVariant"
            >
                <logic:iterate id         = "section"
                               indexId    = "k"
                               collection = "<%= variant.getSections() %>"
                               type       = "com.freshdirect.fdstore.content.RecipeSection"
                >
                    <logic:iterate id         = "product"
                                   indexId    = "l"
                                   collection = "<%= section.getIngredients() %>"
                                   type       = "com.freshdirect.fdstore.content.ConfiguredProduct"
                    >
                    <%
                        distinctProducts.add(new RecipeProductContainer(recipe,
                                                                        variant,
                                                                        product));
                    %>
                    </logic:iterate>
                </logic:iterate>
            </logic:iterate>
		</logic:iterate>
        <%
            List products = new Vector(distinctProducts);
            Collections.sort(products, new RecipeProductContainerComparator());
        %>

        <logic:iterate id         = "product"
                       indexId    = "i"
                       collection = "<%= products %>"
                       type       = "RecipeProductContainer"
        >
            <tr>
                <td><%= product.getProduct().getFullName() %></td>
                <td><%= product.getProduct().getContentName() %></td>
                <td><%= product.getRecipe().getFullName() %></td>
                <td>
                <%
                    // guard against empty configured product groups by catching
                    // the NullPointerException they throw

                    String availability = "Unable to determine availability";

                    try {
                        availability = product.getProduct().isAvailableWithin(1)
                                     ? "OK"
                                     : product.getProduct().isUnavailable()
                                       ? "Long term unavailability"
                                       : "Short term unavailability";
                    } catch (NullPointerException e) {
                    }
                    %>
                    <%= availability %>
                </td>
                <td>
                <%
                    String linkParams = "recipeId=" + product.getRecipe().getContentName()
                                      + "&variantId=" + product.getVariant().getContentName();
                %>
                    <a href="http://www1.dev.nyc1.freshdirect.com:8200/recipe.jsp?<%= linkParams %>&trk=srch">INT</a> |
                    <a href="http://newstage.freshdirect.com/recipe.jsp?<%= linkParams %>&trk=srch">STAGE</a>
                    <a href="http://www.freshdirect.com/recipe.jsp?<%= linkParams %>&trk=srch">WWW</a>
                </td>
                <td><%= timeFormat.format(new Date()) %></td>
            </tr>
		</logic:iterate>
	</table>
</div>
<div style="width: 100%; height: 1px; border-top: solid 1px #000000;"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></div>
</body>
</html>

