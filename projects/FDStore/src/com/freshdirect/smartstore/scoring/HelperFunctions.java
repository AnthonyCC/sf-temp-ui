package com.freshdirect.smartstore.scoring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.FavoriteList;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.StoreModel;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.fdstore.SmartStoreUtil;

/**
 * This class contains functions which used by the generated code.
 * @author zsombor
 *
 */
public class HelperFunctions {
    private HelperFunctions() {}
    
    
    /**
     * used by the generated code
     * @param value
     * @param min
     * @param max
     * @return
     */
    public static final int between(double value, double min, double max) {
        return (min<=value && value<= max ? 1 : 0);
    }

    /**
     * used by the generated code
     * @param value
     * @param min
     * @param max
     * @return
     */
    public static final int between(int value, int min, int max) {
        return (min<=value && value<= max ? 1 : 0);
    }

    /**
     * This method called from the generated code! Modify the signature with extreme care.
     * @param params the list of parameters, it can contains strings, nodes, and lists
     * @return
     */
    public static final List recursiveNodes(Object[] params) {
        List result = new ArrayList();
        for (int i=0;i<params.length;i++) {
            Object obj = params[i];
            if (obj instanceof ContentNodeModel) {
                result.addAll(recursiveNodes((ContentNodeModel) obj));
            } else if (obj instanceof String) {
                result.addAll(recursiveNodes(lookup((String) obj)));
            } else if (obj instanceof List) {
                result.addAll(recursiveNodes((List) obj));
            }
        }
        return result;
    }

    /**
     * 
     * @param model List<ContentNodeModel>
     * @return
     */
    public static final List recursiveNodes(List list) {
        List result = new ArrayList();
        for (int i=0;i<list.size();i++) {
            result.addAll(recursiveNodes((ContentNodeModel) list.get(i)));
        }
        return result;
    }

    /**
     * Return a parent category, or null if there is no category in the hierarchy.
     * @param model
     * @return
     */
    public static final ContentNodeModel getParentCategory(ContentNodeModel model) {
        if (model!=null) {
            ContentNodeModel parentNode = model.getParentNode();
            if (parentNode!=null) {
                if (parentNode instanceof CategoryModel) {
                    return parentNode;
                } else {
                    return getParentCategory(parentNode);
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Return a parent category, or null if there is no category in the hierarchy.
     * @param model
     * @return
     */
    public static final ContentNodeModel getParentDepartment(ContentNodeModel model) {
        if (model!=null) {
            ContentNodeModel parentNode = model.getParentNode();
            if (parentNode!=null) {
                if (parentNode instanceof DepartmentModel) {
                    return parentNode;
                } else {
                    return getParentDepartment(parentNode);
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
    
    
    /**
     * Return the top level category from the hierarchy. Top level category means, category which parent isn't a category.
     * @param model
     * @return
     */
    public static final ContentNodeModel getToplevelCategory(ContentNodeModel model) {
        if (model!=null) {
            ContentNodeModel parentNode = model.getParentNode();
            if (parentNode!=null) {
                if (parentNode instanceof CategoryModel) {
                    return getToplevelCategory(parentNode);
                } else {
                    return model;
                }
            } else {
                return model;
            }
        } else {
            return null;
        }
        
    }

    /**
     * Return a list of nodes, under the given one.
     * @param model
     * @return
     */
    public static final List recursiveNodes(ContentNodeModel model) {
        List result = new ArrayList();
        collect(model, result);
        return result;
    }

    /**
     * Collect nodes into the result list starting from the first parameter.
     * 
     * @param model
     * @param result
     */
    private static void collect(ContentNodeModel model, Collection result) {
        if (model instanceof ProductModel) {
            result.add(SmartStoreUtil.addConfiguredProductToCache((ProductModel) model));
        } else if (model instanceof CategoryModel) {
            CategoryModel cat = (CategoryModel) model;
            for (Iterator iter = cat.getProducts().iterator(); iter.hasNext();) {
                result.add(iter.next());
            }
            for (Iterator iter = cat.getSubcategories().iterator(); iter.hasNext();) {
                collect( (ContentNodeModel) iter.next(), result);
            }
        } else if (model instanceof FavoriteList) {
            FavoriteList fl = (FavoriteList) model;
            for (Iterator iter = fl.getFavoriteItems().iterator(); iter.hasNext();) {
                collect( (ContentNodeModel) iter.next(), result); 
            }
        } else if (model instanceof DepartmentModel) {
            DepartmentModel dep = (DepartmentModel) model;
            for (Iterator iter = dep.getCategories().iterator(); iter.hasNext();) {
                collect( (ContentNodeModel) iter.next(), result);
            }
        } else if (model instanceof StoreModel) {
            StoreModel store = (StoreModel) model;
            for (Iterator iter = store.getDepartments().iterator(); iter.hasNext();) {
                collect( (ContentNodeModel) iter.next(), result);
            }
        }
    }

    /**
     * Return a list of nodes, under the given one, except the specified values from the exceptionObject.
     * @param model node
     * @param exceptionObject can be string, ContentNodeModel or Collection 
     * @return
     */
    public static final List recursiveNodesExcept(ContentNodeModel model,Object exceptionObject) {
        return collectExcept(model, collectIds(exceptionObject));
    }
    
    /**
     * Return a list of nodes, under the given one, except the specified values from the exceptionIds collection.
     * @param model
     * @param exceptionObject can be an array of strings, ContentNodeModel or Collection. 
     * @return
     */
    public static final List recursiveNodesExcept(ContentNodeModel model,Object[] exceptionObject) {
        return collectExcept(model, collectIds(exceptionObject));
    }

    /**
     * Return a list of nodes, under the given ones, except the specified values from the exceptionIds collection.
     * @param models a collections of nodes
     * @param exceptionObject can be string, ContentNodeModel or Collection 
     * @return
     */
    public static final List recursiveNodesExcept(Collection models,Object exceptionObject) {
        return collectExcept(models, collectIds(exceptionObject));
    }

    /**
     * Return a list of nodes, under the given one, except the specified values from the exceptionIds collection.
     * @param models a collections of nodes
     * @param exceptionObject can be an array of strings, ContentNodeModel or Collection. 
     * @return
     */
    public static final List recursiveNodesExcept(Collection models,Object[] exceptionObject) {
        return collectExcept(models, collectIds(exceptionObject));
    }

    private static Set collectIds(Object exceptionObject) {
        Set exceptionIds = new HashSet(); 
        collectIds(exceptionIds, exceptionObject);
        return exceptionIds;
    }

    private static Set collectIds(Object[] exceptionObject) {
        Set exceptionIds = new HashSet(); 
        for (int i=0;i<exceptionObject.length;i++) {
            Object obj = exceptionObject[i];
            collectIds(exceptionIds, obj);
        }
        return exceptionIds;
    }

    private static List collectExcept(ContentNodeModel model, Set exceptionIds) {
        Collection result = new HashSet();
        collectExcept(model, result, exceptionIds);
        return new ArrayList(result);
    }

    private static List collectExcept(Collection models, Set exceptionIds) {
        Collection result = new HashSet();
        for (Iterator iter= models.iterator(); iter.hasNext();) {
            ContentNodeModel model = (ContentNodeModel) iter.next();
            collectExcept(model, result, exceptionIds);
        }
        return new ArrayList(result);
    }

    private static void collectIds(Set exceptionIds, Object obj) {
        if (obj instanceof String) {
            exceptionIds.add((String) obj); 
        } else if (obj instanceof ContentNodeModel) {
            exceptionIds.add(((ContentNodeModel)obj).getContentKey().getId());
        } else if (obj instanceof Collection) {
            Collection col = (Collection) obj;
            for (Iterator iter= col.iterator();iter.hasNext();) {
                collectIds(exceptionIds, iter.next());
            }
        }
    }
    
    /**
     * Collect nodes into the result list starting from the first parameter, except under the specified nodes.
     * 
     * @param model
     * @param result
     * @param exceptionIds collection of strings, representing the content keys.
     */
    private static void collectExcept(ContentNodeModel model, Collection result, Collection exceptionIds) {
        if (exceptionIds.contains(model.getContentKey().getId())) {
            return;
        }
        if (model instanceof ProductModel) {
            result.add(SmartStoreUtil.addConfiguredProductToCache((ProductModel) model));
        } else if (model instanceof CategoryModel) {
            CategoryModel cat = (CategoryModel) model;
            for (Iterator iter = cat.getProducts().iterator(); iter.hasNext();) {
                collectExcept((ContentNodeModel) iter.next(), result, exceptionIds);
            }
            for (Iterator iter = cat.getSubcategories().iterator(); iter.hasNext();) {
                collectExcept( (ContentNodeModel) iter.next(), result, exceptionIds);
            }
        } else if (model instanceof FavoriteList) {
            FavoriteList fl = (FavoriteList) model;
            for (Iterator iter = fl.getFavoriteItems().iterator(); iter.hasNext();) {
                collectExcept( (ContentNodeModel) iter.next(), result, exceptionIds); 
            }
        } else if (model instanceof DepartmentModel) {
            DepartmentModel dep = (DepartmentModel) model;
            for (Iterator iter = dep.getCategories().iterator(); iter.hasNext();) {
                collectExcept( (ContentNodeModel) iter.next(), result, exceptionIds);
            }
        } else if (model instanceof StoreModel) {
            StoreModel store = (StoreModel) model;
            for (Iterator iter = store.getDepartments().iterator(); iter.hasNext();) {
                collectExcept( (ContentNodeModel) iter.next(), result, exceptionIds);
            }
        }
    }
    
    
    

    public static ContentNodeModel lookup(String id) {
        return ContentFactory.getInstance().getContentNode(id);
    }

    public static List toList(Object[] params) {
        List result = new ArrayList();
        for (int i=0;i<params.length;i++) {
            Object obj = params[i];
            if (obj instanceof ContentNodeModel) {
                result.add((ContentNodeModel) obj);
            } else if (obj instanceof String) {
                result.add(lookup((String) obj));
            } else if (obj instanceof List) {
                result.addAll((List) obj);
            }
        }
        return result;
    }
    
    public static List toList(ContentNodeModel model) {
        return Collections.singletonList(model);
    }

    public static List toList(String id) {
        return Collections.singletonList(lookup(id));
    }

    /**
     * Return the part of the cache key for the current node.
     * @param input
     * @return
     */
    public static String getCurrentNodeCacheKey(SessionInput input) {
        return (input!=null && input.getCurrentNode()!=null) ? input.getCurrentNode().getContentKey().getId() : "<null>";
    }
    
    public static String getExplicitListCacheKey(SessionInput input) {
        if (input!=null && input.getExplicitList()!=null) {
            StringBuffer buf = new StringBuffer("[");
            boolean first = true;
            for (Iterator iter=input.getExplicitList().iterator();iter.hasNext();) {
                ContentNodeModel model = (ContentNodeModel) iter.next();
                if (!first) {
                    buf.append(',');
                } else {
                    first = false;
                }
                buf.append(model.getContentKey().getId());
            }
            buf.append(']');
            return buf.toString();
        }
        return "<empty-list>";
    }
    
    
    
    
}
