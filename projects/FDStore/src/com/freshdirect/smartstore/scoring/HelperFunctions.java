package com.freshdirect.smartstore.scoring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.content.BrandModel;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.FavoriteList;
import com.freshdirect.fdstore.content.ProductContainer;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.content.StoreModel;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.external.ExternalRecommender;
import com.freshdirect.smartstore.external.ExternalRecommenderCommunicationException;
import com.freshdirect.smartstore.external.ExternalRecommenderRegistry;
import com.freshdirect.smartstore.external.ExternalRecommenderRequest;
import com.freshdirect.smartstore.external.ExternalRecommenderType;
import com.freshdirect.smartstore.external.NoSuchExternalRecommenderException;
import com.freshdirect.smartstore.external.RecommendationItem;
import com.freshdirect.smartstore.fdstore.DatabaseScoreFactorProvider;
import com.freshdirect.smartstore.fdstore.SmartStoreUtil;
import com.freshdirect.smartstore.filter.ContentFilter;
import com.freshdirect.smartstore.filter.FilterFactory;
import com.freshdirect.smartstore.sampling.RankedContent;

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
    
    /**
     * used by 'MatchBrandFilter'
     * 
     * @param models
     * @return
     */
    public static ContentNodeModel findBrand(List<ContentNodeModel> models) {
        for (ContentNodeModel m : models) {
            if (m instanceof BrandModel) {
                return m;
            }
        }
        return null;
    }
    
    /**
     * used by 'MatchBrandFilter'
     * 
     * @param model
     * @param filterBrand
     * @return
     */
    public static boolean matchBrand(ContentNodeModel model, ContentNodeModel filterBrand) {
        if (model instanceof ProductModel) {
            List<BrandModel> brands = ((ProductModel) model).getBrands();
            for (BrandModel brand : brands) {
                if (brand.equals(filterBrand)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static List<ContentNodeModel> toList(ContentNodeModel model) {
        ArrayList<ContentNodeModel> a = new ArrayList<ContentNodeModel>();
        a.add(model);
        return a;
    }

    public static List<ContentNodeModel> toList(String id) {
        ArrayList<ContentNodeModel> a = new ArrayList<ContentNodeModel>();
        a.add(lookup(id));
        return a;
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
    
    /**
     * Return a list of product recommendation for a given product by a
     * recommender vendor.
     * 
     * @param recommender
     * @param model
     * @return List<ContentNodeModel>
     */
    public static List getProductRecommendationFromVendor(String recommender, ContentNodeModel model) {
        return SmartStoreUtil.toContentNodesFromKeys(DatabaseScoreFactorProvider.getInstance().getProductRecommendations(recommender, model.getContentKey()));
    }

    public static List getUserRecommendationFromVendor(String recommender, String erpCustomerId) {
        return SmartStoreUtil.toContentNodesFromKeys(DatabaseScoreFactorProvider.getInstance().getPersonalRecommendations(recommender, erpCustomerId));
    }

    /**
     * Return a list of featured items.
     * 
     * @param model
     * @return
     */
    public static List getFeaturedItems(ContentNodeModel model) {
        if(model instanceof ProductContainer) {
            return ((ProductContainer) model).getFeaturedProducts();
        } else if (model instanceof ProductModel) {
                return ((CategoryModel) HelperFunctions.getToplevelCategory(model)).getFeaturedProducts();
        }
        return Collections.EMPTY_LIST;
    }
    
    public static List getCandidateLists(ContentNodeModel model) {
		List nodes = new ArrayList();
		if (model instanceof CategoryModel) {
			CategoryModel category = (CategoryModel) model;
			List candidateList = category.getCandidateList();
			if (candidateList != null && candidateList.size() > 0) {
				nodes.addAll(recursiveNodes(candidateList));
			} else {
				nodes.addAll(recursiveNodes(model));
			}
		}
		return nodes;
	}

    public static List getManuallyOverriddenSlots(ContentNodeModel model,
			SessionInput input, DataAccess dataAccess) {
		List nodes = new ArrayList();
		if (model instanceof CategoryModel) {
			CategoryModel category = (CategoryModel) model;
			int slots = category.getManualSelectionSlots();
			if (slots > 0) {
				ContentFilter filter = FilterFactory.getInstance().createFilter(input.getExclusions(), input.isUseAlternatives(), input.isShowTemporaryUnavailable());
				List fprods = category.getFeaturedProducts();
				Random rnd = new Random();

				while (nodes.size() < slots && fprods.size() > 0) {
					int pos = input.isNoShuffle() ? 0 : rnd.nextInt(fprods
							.size());
					Object product = fprods.remove(pos);

					ProductModel pm = (ProductModel) product;
					// it does all checks against cart include, displaying, uniqueness, etc.
					final ProductModel filteredModel = filter.filter(pm);
                                        if (filteredModel != null) {
                                            nodes.add(filteredModel);
                                            slots--;
					}
				}
			}
		}
		return nodes;
	}

    public static List getManuallyOverriddenSlotsP(ContentNodeModel model,
			SessionInput input, DataAccess dataAccess) {
		List nodes = new ArrayList();
		if (model instanceof CategoryModel) {
			CategoryModel category = (CategoryModel) model;
			int slots = category.getManualSelectionSlots();
			if (slots > 0) {
				List fprods = category.getFeaturedProducts();
				Random rnd = new Random();

				while (slots > 0 && fprods.size() > 0) {
					int pos = input.isNoShuffle() ? 0 : rnd.nextInt(fprods
							.size());
					Object product = fprods.remove(pos);

					ProductModel pm = (ProductModel) product;
					// it does all checks against cart include, displaying, uniqueness, etc.
					if (dataAccess.addPrioritizedNode(pm))
						slots--;
					// we do not return prioritized nodes 
				}
			}
		}
		return nodes;
	}
    
    public static List getTopN(List nodes, String factorName, int n, SessionInput input, final DataAccess dataAccess) {
        String userId = input.getCustomerId();
        PricingContext pricingCtx = input.getPricingContext();
        String[] variables = { factorName };
        OrderingFunction of = new OrderingFunction();
        for (Object node : nodes) {
            ContentNodeModel model = (ContentNodeModel) node;
            of.addScore(model, dataAccess.getVariables(userId, pricingCtx, model, variables));
        }
        List results = new ArrayList(n);
        List ranked = of.getRankedContents();
        int topN = Math.min(n, ranked.size());
        ContentFilter filter = FilterFactory.getInstance().createFilter(input.getExclusions(), input.isUseAlternatives(), input.isShowTemporaryUnavailable());
        Iterator it = ranked.iterator();
        while (topN > 0 && it.hasNext()) {
            RankedContent.Single rc = (RankedContent.Single) it.next();
            ContentNodeModel node = rc.getModel();
            if (node instanceof ProductModel) {
                ProductModel filteredModel = filter.filter((ProductModel) node); 
                if (filteredModel != null) {
                    results.add(filteredModel);
                    topN--;
                }
            }
        }
        return results;
    }    
    
    public static boolean matchSkuPrefix(ContentNodeModel model, String[] prefixes) {
        if (model instanceof ProductModel) {
            ProductModel pm = (ProductModel) model;
            List<SkuModel> skus = pm.getSkus();
            for (SkuModel sku : skus) {
                for (String prefix : prefixes) {
                    if (sku.getSkuCode().startsWith(prefix)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * @param model
     * @param key
     * @return the model, if it's the same as the key refers , or look up the model from the ContentFactory. 
     */
    public static ContentNodeModel getContentNodeModelOrLookup(ContentKey key, ContentNodeModel model) {
        ContentNodeModel result = key.equals(model.getContentKey()) ? model : ContentFactory.getInstance().getContentNodeByKey(key);
        return result;
    }
    
    /**
     * Add the node to the prioritized sets, and returns 0, because this function is used as a filter
     * and 0 means that the node should be removed from the active list.
     * 
     * @param da the data access object
     * @param model
     * @return 0
     */
    public static int addPrioritizedNode(DataAccess da, ContentNodeModel model) {
        da.addPrioritizedNode(model);
        return 0;
    }

    @SuppressWarnings("unchecked")
	public static List getPersonalizedExternalRecommendations(String providerName, SessionInput input) {
    	try {
			ExternalRecommender recommender = ExternalRecommenderRegistry.getInstance(providerName, ExternalRecommenderType.PERSONALIZED);
			List<RecommendationItem> items = recommender.recommendItems(new ExternalRecommenderRequest(input.getCustomerId()));
			List<ContentNodeModel> nodes = new ArrayList<ContentNodeModel>(items.size());
			for (RecommendationItem item : items) {
				ContentNodeModel node = ContentFactory.getInstance().getContentNode(item.getId());
				if (node != null)
					nodes.add(node);
			}
			return nodes;
		} catch (IllegalArgumentException e) {
		} catch (NoSuchExternalRecommenderException e) {
		} catch (ExternalRecommenderCommunicationException e) {
		} catch (NullPointerException e) {
		}
		return new ArrayList();
    }

    @SuppressWarnings("unchecked")
	public static List getRelatedExternalRecommendations(List paramNodes, String providerName, SessionInput input) {
    	try {
			ExternalRecommender recommender = ExternalRecommenderRegistry.getInstance(providerName, ExternalRecommenderType.RELATED);
			List<RecommendationItem> requestItems = new ArrayList<RecommendationItem>();
			for (Object o : paramNodes) {
				ContentNodeModel node = (ContentNodeModel) o;
				requestItems.add(new RecommendationItem(node.getContentKey().getId()));
			}
			List<RecommendationItem> items = recommender.recommendItems(new ExternalRecommenderRequest(requestItems));
			List<ContentNodeModel> nodes = new ArrayList<ContentNodeModel>(items.size());
			for (RecommendationItem item : items) {
				ContentNodeModel node = ContentFactory.getInstance().getContentNode(item.getId());
				if (node != null)
					nodes.add(node);
			}
			return nodes;
		} catch (IllegalArgumentException e) {
		} catch (NoSuchExternalRecommenderException e) {
		} catch (ExternalRecommenderCommunicationException e) {
		} catch (NullPointerException e) {
		}
		return new ArrayList();
    }
}
