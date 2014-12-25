package com.freshdirect.mobileapi.controller;

import static java.util.Collections.emptyList;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.content.BrandModel;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.CategorySectionModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.StoreModel;
import com.freshdirect.fdstore.content.TagModel;
import com.freshdirect.fdstore.ecoupon.EnumCouponContext;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.data.BrowseResult;
import com.freshdirect.mobileapi.controller.data.request.BrowseQuery;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.model.Brand;
import com.freshdirect.mobileapi.model.Category;
import com.freshdirect.mobileapi.model.Department;
import com.freshdirect.mobileapi.model.FDGroup;
import com.freshdirect.mobileapi.model.Product;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.Wine;
import com.freshdirect.mobileapi.model.tagwrapper.ItemGrabberTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.ItemSorterTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.LayoutManagerWrapper;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.mobileapi.util.ListPaginator;
import com.freshdirect.webapp.taglib.fdstore.layout.LayoutManager.Settings;

/**
 * @author Sivachandar
 *
 */
public class BrowseController extends BaseController {

	private static final org.apache.log4j.Category LOG = LoggerFactory.getInstance(BrowseController.class);


    private static final String ACTION_GET_DEPARTMENTS = "getDepartments";

    private static final String ACTION_GET_CATEGORIES = "getCategories";

    private static final String ACTION_GET_CATEGORYCONTENT = "getCategoryContent";

    private static final String ACTION_GET_CATEGORYCONTENT_PRODUCTONLY = "getCategoryContentProductOnly";

    private static final String ACTION_GET_GROUP_PRODUCTS = "getGroupProducts";


	private static final String FILTER_KEY_BRANDS = "brands";
    private static final String FILTER_KEY_TAGS = "tags";


    @Override
	protected boolean validateUser() {
		return false;
	}

	/* (non-Javadoc)
     * @see com.freshdirect.mobileapi.controller.BaseController#processRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.web.servlet.ModelAndView, java.lang.String, com.freshdirect.mobileapi.model.SessionUser)
     */
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action,
            SessionUser user) throws FDException, ServiceException, JsonException {

    	if (user == null) {
    		user = fakeUser(request.getSession());
    	}

    	// Retrieving any possible payload
        String postData = getPostData(request, response);
        BrowseQuery requestMessage = null;

        LOG.debug("BrowseController PostData received: [" + postData + "]");
        if (StringUtils.isNotEmpty(postData)) {
            requestMessage = parseRequestObject(request, response, BrowseQuery.class);
        }
        BrowseResult result = new BrowseResult();

        if(requestMessage != null) {
	        if (ACTION_GET_DEPARTMENTS.equals(action)) {
	           StoreModel store = ContentFactory.getInstance().getStore();
	           if(store != null) {
	        	   List<DepartmentModel> storeDepartments = store.getDepartments();

	        	   List<Department> departments = new ArrayList<Department>();
	        	   if(storeDepartments != null) {
		        	   for(DepartmentModel storeDepartment : storeDepartments) {
		        		   if(storeDepartment.getContentKey() != null
		        				   && !storeDepartment.isHidden()
		        				   && !storeDepartment.isHideIphone()) {
		        			   departments.add(Department.wrap(storeDepartment));
		        		   }
		        	   }
	        	   }
	        	   ListPaginator<com.freshdirect.mobileapi.model.Department> paginator = new ListPaginator<com.freshdirect.mobileapi.model.Department>(
	        			   departments, requestMessage.getMax());
	        	   result.setDepartments(paginator.getPage(requestMessage.getPage()));
	        	   result.setTotalResultCount(result.getDepartments() != null ? result.getDepartments().size() : 0);
	           }
	        } else if (ACTION_GET_CATEGORIES.equals(action)
	        								|| ACTION_GET_CATEGORYCONTENT.equals(action)
	        								|| ACTION_GET_CATEGORYCONTENT_PRODUCTONLY.equals(action)) {
	        	String contentId = null;
//	        	if(ACTION_GET_CATEGORIES.equals(action)) {
//	        		contentId = requestMessage.getDepartment();
//	        	} else {
//	        		contentId = requestMessage.getCategory();
//	        	}
	        	
	        	
	        	contentId = requestMessage.getCategory();
	        	if (contentId == null) {
	        		contentId = requestMessage.getDepartment();
	        	}

	        	ContentNodeModel currentFolder = ContentFactory.getInstance().getContentNode(contentId);
	        	List<CategorySectionModel> categorySections = emptyList();
	        	if (currentFolder instanceof DepartmentModel) {
	        		DepartmentModel department = (DepartmentModel) currentFolder;
	        		categorySections = new ArrayList<CategorySectionModel>(department.getCategorySections());
	        	}
	        	//Below is the workaround for handling url based category based redirect url which are setup in CMS (e.g Organic Fruit in Fruit Dept)
	        	if(currentFolder instanceof CategoryModel) {
	        		String redirectURL = ((CategoryModel)currentFolder).getRedirectUrl();
	        		if(redirectURL != null && redirectURL.trim().length() > 0) {
	        			Map<String, String> redirectParams = getQueryMap(redirectURL);
	        			String redirectContentId = (String)redirectParams.get("catId");
	        			if(redirectContentId != null && redirectContentId.trim().length() > 0) {
	        				contentId = redirectContentId;
	        				currentFolder = ContentFactory.getInstance().getContentNode(redirectContentId);
	        			}
	        		}
	        	}
	        	if(currentFolder instanceof CategoryModel && ((CategoryModel)currentFolder).isShowAllByDefault()) { // To Support new left nav flow[APPDEV-3251 : mobile API to utilize showAllByDefault]
	            	action = ACTION_GET_CATEGORYCONTENT_PRODUCTONLY;
	            }

	        	List contents = new ArrayList();

	            LayoutManagerWrapper layoutManagerTagWrapper = new LayoutManagerWrapper(user);
	            Settings layoutManagerSetting = layoutManagerTagWrapper.getLayoutManagerSettings(currentFolder);

	            //We have layout manager
	            if (layoutManagerSetting != null) {
	            	if(layoutManagerSetting.getGrabberDepth() < 0) { // Overridding the hardcoded values done for new 4mm and wine layout
	            		layoutManagerSetting.setGrabberDepth(0);
	            	}

	            	layoutManagerSetting.setReturnSecondaryFolders(true);//Hardcoded for mobile api
	                ItemGrabberTagWrapper itemGrabberTagWrapper = new ItemGrabberTagWrapper(user.getFDSessionUser());
	                contents = itemGrabberTagWrapper.getProducts(layoutManagerSetting, currentFolder);

	                ItemSorterTagWrapper sortTagWrapper = new ItemSorterTagWrapper(user);
	                sortTagWrapper.sort(contents, layoutManagerSetting.getSortStrategy());

	            } else {
	                //Error happened. It's a internal error so don't expose to user. just log and return empty list
	                ActionResult layoutResult = (ActionResult) layoutManagerTagWrapper.getResult();
	                if (layoutResult.isFailure()) {
	                    Collection<ActionError> errors = layoutResult.getErrors();
	                    for (ActionError error : errors) {
	                        LOG.error("Error while trying to retrieve whats good product: ec=" + error.getType() + "::desc="
	                                + error.getDescription());
	                    }
	                }
	            }
	            List<Product> products = new ArrayList<Product>();
	            List<Product> unavailableProducts = new ArrayList<Product>();

	            List<Category> categories = new ArrayList<Category>();
	            Set<String> categoryIDs = new HashSet<String>();

	            SortedSet<String> brands = new TreeSet<String>();
	            SortedSet<String> countries = new TreeSet<String>();
	            SortedSet<String> regions = new TreeSet<String>();
	            SortedSet<String> grapes = new TreeSet<String>();
	            SortedSet<String> typeFilters = new TreeSet<String>();

//	            if (currentFolder instanceof CategoryModel) {
//	            	// add subcategories for browsing
//	            	contents.addAll(((CategoryModel) currentFolder).getSubcategories());
//	            }

	            boolean nextLevelIsBottom = true;

	            for (Object content : contents) {
	                if (content instanceof ProductModel) {
	                    ProductModel productModel = (ProductModel) content;
						try {
	                    	//if(!productModel.isHideIphone()) {			//DOOR3 FD-iPad FDIP-662
	                    		if (passesFilter(productModel, request)) {
		                    		Product product = Product.wrap(productModel, user.getFDSessionUser().getUser(), null, EnumCouponContext.PRODUCT);

		                    		for (Brand brand : product.getBrands()) {
										brands.add(brand.getName());
									}
		                    		if (product instanceof Wine) {
		                    			Wine wine = ((Wine) product);
		                    			if (wine.getWineCountry() != null && wine.getWineRegionName() != null && wine.getGrape() != null) {
			                    			countries.add(wine.getWineCountry());
			                    			regions.add(wine.getWineRegionName());
		                    			
		                    			
			                    			String grape = wine.getGrape();
			                    			
			                    			String [] grapesSplit = grape.split(",");
			                    			
			                    			for(String g : grapesSplit){
			                    				g = g.replaceAll("[\\(\\)\\d\\%]*", "").trim();
			                    				grapes.add(g);
			                    			}
		                    			}
		                    			
		                    		} else {
										SortedSet<String> types = product.getFilters().get("type");
		                    			for (String type : types) {
											typeFilters.add(type);
										}
		                    		}

									if(productModel.isUnavailable()) { // Segregate out unavailable to move them to the end
		                    			unavailableProducts.add(product);
		                    		} else {
		                    			products.add(product);
		                    		}
	                    		}
	                    	//}	//DOOR3 FD-iPad FDIP-662
	                    } catch (Exception e) {
	                        //Don't let one rotten egg ruin it for the bunch
	                        LOG.error("ModelException encountered. Product ID=" + productModel.getFullName(), e);
	                    }
	                } else if(content instanceof CategoryModel) {
	                	CategoryModel categoryModel = (CategoryModel)content;
                        if (StringUtils.equals(contentId, categoryModel.getContentName())) {
                            // don't return recursive models
                            break;
                        }
	                	String parentId = categoryModel.getParentNode().getContentKey().getId();
	                	
						if((categoryModel.isActive()
	                					|| (categoryModel.getRedirectUrl() != null
	                								&& categoryModel.getRedirectUrl().trim().length() > 0))
	                				// && !categoryModel.isHideIphone()
	                				// && !categoryIDs.contains(categoryModel.getParentId())
	                				// && !isEmptyProductGrabberCategory(categoryModel)
	                				&& contentId.equals(parentId)
	                				) {	// Show only one level of category
							// check if the next level in hierarchy has only products
							// it's important for the UI
							if (nextLevelIsBottom && !categoryModel.getSubcategories().isEmpty()) {
								for (CategoryModel subcategory : categoryModel.getSubcategories()) {
									if (!subcategory.getSubcategories().isEmpty()) {
										// we have another level to go
										nextLevelIsBottom = false;
										break;
									}
								}
							}

	                		Category category = Category.wrap(categoryModel);
							addCategoryHeadline(categorySections, categoryModel, category);

							category.setNoOfProducts(0);
							
							if(!categoryModel.getSubcategories().isEmpty())
								category.setBottomLevel(false);
							else
								category.setBottomLevel(true);
								
							
							categories.add(category);
	                		categoryIDs.add(categoryModel.getContentKey().getId());
	                	}
	                }
	            }
	            Map<String, SortedSet<String>> filters = result.getFilters();
	            if (brands.size() > 0) filters.put("brand", brands);
	            if (countries.size() > 0) filters.put("country", countries);
	            if (regions.size() > 0) filters.put("region", regions);
	            if (grapes.size() > 0) filters.put("grape", grapes);
	            if (typeFilters.size() > 0) filters.put("type", typeFilters);

	            if(categories.size() > 0 && !ACTION_GET_CATEGORYCONTENT_PRODUCTONLY.equals(action)) {
	            	ListPaginator<com.freshdirect.mobileapi.model.Category> paginator = new ListPaginator<com.freshdirect.mobileapi.model.Category>(
	            			categories, requestMessage.getMax());

					result.setCategories(paginator.getPage(requestMessage.getPage()));
					result.setResultCount(result.getCategories() != null ? result.getCategories().size() : 0);
					result.setTotalResultCount(categories.size());
	            } else {
	            	products.addAll(unavailableProducts);//add all unavailable to the end of the list

	            	ListPaginator<com.freshdirect.mobileapi.model.Product> paginator = new ListPaginator<com.freshdirect.mobileapi.model.Product>(
	         															products, requestMessage.getMax());

	            	// send subcategories with products
	            	result.setCategories(categories);
	         		result.setProductsFromModel(paginator.getPage(requestMessage.getPage()));
	         		result.setResultCount(result.getProducts() != null ? result.getProducts().size() : 0);
	         		result.setTotalResultCount(products.size());
	            }
//	            if (result.getCategories() == null || result.getCategories().isEmpty()) {
//	            	result.setBottomLevel(true);
//	            }
	            result.setBottomLevel(nextLevelIsBottom);
	        } else if (ACTION_GET_GROUP_PRODUCTS.equals(action)) {
	        	List<Product> products = FDGroup.getGroupScaleProducts(requestMessage.getGroupId(), requestMessage.getGroupVersion(), user);
	        	result.setProductsFromModel(products);
	        }
        }

        setResponseMessage(model, result, user);
        return model;
    }

	private void addCategoryHeadline(
			List<CategorySectionModel> categorySections,
			CategoryModel categoryModel, Category category) {
	    // Simple department
	    if (categorySections.isEmpty()) {
	        if (!categoryModel.isPreferenceCategory()) {
	            final String leftNavHeader = categoryModel.getDepartment().getRegularCategoriesLeftNavBoxHeader();
                final String sectionName = isNotBlank(leftNavHeader) ? leftNavHeader : categoryModel.getDepartment().getFullName();
	            category.setSectionHeader(sectionName);
	        }
	        return;
	    }
	    // Department with sections
		for (CategorySectionModel section : categorySections) {
			for (CategoryModel c : section.getSelectedCategories()) {
				if (c.getContentName().equals(categoryModel.getContentName())) {
					category.setSectionHeader(section.getHeadline());
					return;
				}
			}
		}
	}

    private boolean passesFilter(ProductModel product,
			HttpServletRequest request) {
        return filterTags(product, request) && filterBrands(product, request);
	}

	private boolean filterBrands(ProductModel product,
			HttpServletRequest request) {
		String[] filterBrands = request.getParameterValues(FILTER_KEY_BRANDS);
    	if (filterBrands == null) {
    		return true;
    	}
    	for (String filter : filterBrands) {
			for (BrandModel brand : product.getBrands()) {
				if (StringUtils.equalsIgnoreCase(brand.getName(), filter)) return true;
			}
		}
    	return false;
	}

	private boolean filterTags(ProductModel product, HttpServletRequest request) {
		String[] filterTags = request.getParameterValues(FILTER_KEY_TAGS);
    	if (filterTags == null) {
    		return true;
    	}
    	for (String filter : filterTags) {
			for (TagModel tag : product.getAllTags()) {
				if (StringUtils.equalsIgnoreCase(tag.getName(), filter)) return true;
			}
		}
    	return false;
	}

	private boolean isEmptyProductGrabberCategory(CategoryModel category) { //APPDEV-3659 : Treat empty Product Grabber categories the same on mobile and site
    	if(category.getProductGrabbers() != null && category.getProductGrabbers().size() > 0 ) {
    		List<ProductModel> tmpProducts = category.getProducts();
    		if(tmpProducts == null || tmpProducts.size() == 0) {
    			return true;
    		}
    	}
    	return false;
    }

    @SuppressWarnings("unused")
	private List<Category> getCategories(List<CategoryModel> storeCategories) {
    	List<Category> categories = new ArrayList<Category>();
		if(storeCategories != null) {
			for(CategoryModel storeCategory : storeCategories) {
				if(storeCategory.isActive() && !storeCategory.isHideIphone()) {
					categories.add(Category.wrap(storeCategory));
				}
			}
		}
		return categories;
    }

	private Map<String, String> getQueryMap(String url) {
		Map<String, String> map = new HashMap<String, String>();
		if (url != null) {
			try {
				URI uri = new URI(url);
				String query = uri.getQuery();
				if(query != null) {
					String[] params = query.split("&");
					if(params != null) {
						for (String param : params) {
							map.put(param.split("=")[0], param.split("=")[1]);
						}
					}
				}
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return map;
		}
		return map;
	}

}
