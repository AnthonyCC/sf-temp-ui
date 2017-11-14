package com.freshdirect.storeapi.content;

import java.io.Serializable;
import java.util.Comparator;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.storeapi.CmsLegacy;

@CmsLegacy
public interface ContentNodeModel extends PrioritizedI, Serializable {

	public final static Comparator<ContentNodeModel> FULL_NAME_COMPARATOR = new Comparator<ContentNodeModel>() {
		@Override
        public int compare(ContentNodeModel cn1, ContentNodeModel cn2) {
			String name1 = cn1.getFullName().toLowerCase();
			String name2 = cn2.getFullName().toLowerCase();

			if (name1 == null)
				name1 = "";
			if (name2 == null)
				name2 = "";

			return name1.compareTo(name2);
		}
	};

	/**
	 * This comparator sort the content nodes according to theirs fullname, and if the full names are equals, sort by it's content ids.
	 *
	 */
    public final static Comparator<ContentNodeModel> FULL_NAME_WITH_ID_COMPARATOR = new Comparator<ContentNodeModel>() {
        @Override
        public int compare(ContentNodeModel cn1, ContentNodeModel cn2) {
            String name1 = cn1.getFullName();
            String name2 = cn2.getFullName();

            if (name1 == null)
                    name1 = "";
            if (name2 == null)
                    name2 = "";

            int result = name1.compareTo(name2);
            if (result==0) {
                result = cn1.getContentKey().id.compareTo(cn2.getContentKey().id);
            }
            return result;
        }
    };

	public static Comparator<ContentNodeModel> PRIORITY_COMPARATOR = new Comparator<ContentNodeModel>() {
		@Override
        public int compare(ContentNodeModel o1, ContentNodeModel o2) {
			int p1 = o1.getPriority();
			int p2 = o2.getPriority();
			return p1 == p2 ? 0 : (p1 < p2 ? -1 : 1);
		}
	};

    // from the old ContentNodeI

	@Deprecated
    String TYPE_STORE = "X";
    @Deprecated
    String TYPE_TEMPLATE = "T";
    @Deprecated
    String TYPE_DEPARTMENT = "D";
    @Deprecated
    String TYPE_CATEGORY = "C";
    @Deprecated
    String TYPE_PRODUCT = "P";
    @Deprecated
    String TYPE_CONFIGURED_PRODUCT = "PC";
    @Deprecated
    String TYPE_SKU = "S";
    @Deprecated
    String TYPE_BRAND = "B";
    @Deprecated
    String TYPE_PRODUCTPROXY = "Q";
    @Deprecated
    String TYPE_DOMAIN = "Z";
    @Deprecated
    String TYPE_DOMAINVALUE = "V";
    @Deprecated
    String TYPE_COMPONENTGROUP = "G";
    @Deprecated
    String TYPE_RECIPE_DEPARTMENT = "RD";
    @Deprecated
    String TYPE_RECIPE_CATEGORY = "RC";
    @Deprecated
    String TYPE_RECIPE_SUBCATEGORY = "RSC";
    @Deprecated
    String TYPE_RECIPE = "R";
    @Deprecated
    String TYPE_RECIPE_VARIANT = "RV";
    @Deprecated
    String TYPE_RECIPE_SECTION = "RS";
    @Deprecated
    String TYPE_RECIPE_SOURCE = "RSO";
    @Deprecated
    String TYPE_RECIPE_AUTHOR = "RA";
    @Deprecated
    String TYPE_FD_FOLDER = "FDF";
    @Deprecated
    String TYPE_BOOK_RETAILER = "BKR";
    @Deprecated
    String TYPE_RECIPE_SEARCH_PAGE = "RSP";
    @Deprecated
    String TYPE_RECIPE_SEARCH_CRITERIA = "RSPC";
    @Deprecated
    String TYPE_YMAL_SET = "YS";
    @Deprecated
    String TYPE_STARTER_LIST = "SL";
    @Deprecated
    String TYPE_FAVORITE_LIST = "FL";
    @Deprecated
    String TYPE_RECOMMENDER = "RDR";
    @Deprecated
    String TYPE_RECOMMENDER_STRATEGY = "RDS";
    @Deprecated
    String TYPE_FAQ="FAQ";
    @Deprecated
    String TYPE_PAGE="PAGE";
    @Deprecated
    String TYPE_SUPERDEPARTMENT = "SD";
    @Deprecated
    String TYPE_CATEGORY_SECTION = "CS";
    @Deprecated
    String TYPE_GLOBAL_NAVIGATINO = "GN";

    public ContentKey getContentKey();

    /**
     *	Attention! - getContentName() returns the content id part of content keys,
     *	and not the name of the content nodes, as you might think.
     *
     * 	Method name is seriously misleading!
     * 	Should be something like getContentId() ...
     *
     * @return The id part of the content key!
     */
    public String getContentName();

    /**
     * @deprecated Please use {@link #getContentKey()}.type instead
     *
     * @return Type string defined by constants above.
     */
    @Deprecated
    public String getContentType();


    /**
     * This method is need to be declared here, because some helper class FDAttributeFactory
     * and ContentNodeModelUtil needs this, but do not use from JSP-s or other 'client' codes.
     * @param name
     * @return
     */
    public Object getCmsAttributeValue(String name);


    public ContentNodeModel getParentNode();

    public boolean hasParentWithName(String[] contentNames);

    public String getParentId();

    // the end of the ContentNodeI methods

    // public AttributeDefI getAttributeDef(String name);

	public String getAltText();

	public String getBlurb();

	public Html getEditorial();

	public String getEditorialTitle();

	public String getFullName();

	public String getGlanceName();

	public String getNavName();

	public String getKeywords();

	public boolean isSearchable();

	public boolean isHidden();

	public String getHideUrl();

	public Image getSideNavImage();

	/**
	 * Generate a path string to this content object, that is suitable for
	 * OpenAdServer.
	 *
	 * @return sitepage path for OAS
	 */
	public String getPath();

	public boolean isOrphan();
}
