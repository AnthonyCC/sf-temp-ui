package com.freshdirect.fdstore.dcpd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.DepartmentModel;
import com.freshdirect.storeapi.content.Recipe;

public class DCPDReportQuery {

    List<ContentNodeModel> nodes = Collections.emptyList();
    // Holds the content keys of all the good nodes.
    List<String> deptKeys;
    List<String> catKeys;
    List<String> rcpKeys;

    // list of invalid keys
    List<String> badDeptKeys;
    List<String> badCatKeys;
    List<String> badRcpKeys;

    List<ContentKey> contentKeys = Collections.emptyList(); // obsolete

    List<DepartmentModel> deptNodes = Collections.emptyList(); // department model results
    List<CategoryModel> catNodes = Collections.emptyList(); // category model results
    List<Recipe> rcpNodes = Collections.emptyList(); // recipe results
    List<ContentNodeModel> otherNodes = Collections.emptyList(); // other results (FDFolder, etc.)

    ContentFactory cf = ContentFactory.getInstance();

    public DCPDReportQuery(List<String> deptKeys, List<String> catKeys, List<String> rcpKeys) {
        this.deptKeys = deptKeys;
        this.catKeys = catKeys;
        this.rcpKeys = rcpKeys;
    }

    /**
     * Returns found content nodes
     *
     * @return
     */
    public List<ContentNodeModel> getNodes() {
        return nodes;
    }

    /**
     * @return List of {@link CategoryModel} nodes
     */
    public List<CategoryModel> getCategoryNodes() {
        return catNodes;
    }

    /**
     * @return List of {@link DepartmentModel} nodes
     */
    public List<DepartmentModel> getDepartmentNodes() {
        return deptNodes;
    }

    /**
     * @return List of {@link Recipe} nodes
     */
    public List<Recipe> getRecipeNodes() {
        return rcpNodes;
    }

    public List<String> getDepartmentKeys() {
        return deptKeys;
    }

    public List<String> getRecipeKeys() {
        return rcpKeys;
    }

    public List<String> getBadDepartmentKeys() {
        return badDeptKeys;
    }

    public List<String> getBadCategoryKeys() {
        return badCatKeys;
    }

    public List<String> getBadRecipeKeys() {
        return badRcpKeys;
    }

    public List<ContentKey> getContentKeys() {
        return contentKeys;
    }

    public boolean doQuery() {
        boolean ret = true;

        // reset results
        this.badDeptKeys = new ArrayList<String>();
        this.badCatKeys = new ArrayList<String>();
        this.badRcpKeys = new ArrayList<String>();

        this.nodes = new ArrayList<ContentNodeModel>(); // all nodes
        this.contentKeys = new ArrayList<ContentKey>(); // ??
        this.deptNodes = new ArrayList<DepartmentModel>();
        this.catNodes = new ArrayList<CategoryModel>();
        this.rcpNodes = new ArrayList<Recipe>();

        // collect department nodes
        //
        Iterator<String> it = deptKeys.iterator();
        while (it.hasNext()) {
            String key = it.next();
            if ("".equals(key))
                continue;
            ContentNodeModel node = cf.getContentNode(key);
            if (!(node instanceof DepartmentModel)) {
                badDeptKeys.add(key);
            } else {
                nodes.add(node);
                contentKeys.add(node.getContentKey());
                deptNodes.add((DepartmentModel) node);
            }
        }

        // collect category nodes
        //
        it = catKeys.iterator();
        while (it.hasNext()) {
            String key = it.next();
            if ("".equals(key))
                continue;
            ContentNodeModel node = cf.getContentNode(key);
            if (!(node instanceof CategoryModel)) {
                badCatKeys.add(key);
            } else {
                nodes.add(node);
                contentKeys.add(node.getContentKey());
                catNodes.add((CategoryModel) node);
            }
        }

        // collect recipes
        //
        it = rcpKeys.iterator();
        while (it.hasNext()) {
            String key = it.next();
            if ("".equals(key))
                continue;
            ContentNodeModel node = cf.getContentNode(key);
            if (!(node instanceof Recipe)) {
                badRcpKeys.add(key);
            } else {
                nodes.add(node);
                contentKeys.add(node.getContentKey());
                rcpNodes.add((Recipe) node);
            }
        }

        return ret;
    }

    /**
     * Returns the count of resulted nodes
     *
     * @return count of results
     */
    public int resultCount() {
        return catNodes.size() + deptNodes.size() + rcpNodes.size();
    }

    public boolean hasBadKeys() {
        return badDeptKeys.size() > 0 || badCatKeys.size() > 0 || badRcpKeys.size() > 0;
    }
}
