package com.freshdirect.mobileapi.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.content.BrandModel;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.CategoryNodeTree;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.ContentNodeTree.TreeElement;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.ecoupon.EnumCouponContext;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.exception.ModelException;
import com.freshdirect.mobileapi.model.Brand;
import com.freshdirect.mobileapi.model.Category;
import com.freshdirect.mobileapi.model.Department;
import com.freshdirect.mobileapi.model.Product;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.tagwrapper.SmartSearchTagWrapper;
import com.freshdirect.mobileapi.util.SortType;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SmartSearchTag;
import com.freshdirect.webapp.util.AutoCompleteFacade;

public class ProductServiceImpl implements ProductService {

    private static final org.apache.log4j.Category LOG = LoggerFactory.getInstance(ProductServiceImpl.class);

    private Set<Brand> brands;

    private Set<Department> departments;

    private Set<Category> categories;

    private int recentSearchTotalCount;

    private String spellingSuggestion;

//    private ExecutorService es = Executors.newCachedThreadPool();
    private ExecutorService es = Executors.newFixedThreadPool(20);

    /*
     * (non-Javadoc)
     * @see com.freshdirect.mobileapi.service.ProductService#getProduct(java.lang.String, java.lang.String)
     */
    @Override
    @Deprecated
    public Product getProduct(String categoryId, String productId) throws ServiceException {

        ProductModel product = ContentFactory.getInstance().getProductByName(categoryId, productId);
        Product result;
        try {
            result = Product.wrap(product);
        } catch (ModelException e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return result;
    }

    @Override
    public List<Product> search(String searchTerm, Integer page, Integer max, SessionUser user) throws ServiceException {
        return search(searchTerm, null, page, max, null, null, null, null, user);
    }

    public List<Product> search(String searchTerm, String upc, Integer page, Integer max, SortType sortType, String brandId,
            String categoryId, String deparmentId, SessionUser user) throws ServiceException {
        final List<Product> result = new ArrayList<Product>();

        List<ProductModel> productModels = null;
        SmartSearchTag search = null;

        SmartSearchTagWrapper wrapper = new SmartSearchTagWrapper(user);
        ResultBundle resultBundle;
        try {
            //Rsung - Default sort type if missing
            if (sortType == null) {
                sortType = SortType.RELEVANCY;
            }
            resultBundle = wrapper.getSearchResult(searchTerm, upc, deparmentId, categoryId, brandId, (page - 1), max, "", sortType.getSortValue());
            search = (SmartSearchTag) resultBundle.getExtraData(SmartSearchTagWrapper.ID);

            //Set two more mobile specific filters
            LOG.debug("Total Products before iphone filter: " + search.getNoOfProductsBeforeProductFilters());
            LOG.debug("Total Products after iphone filter: " + search.getNoOfProducts());

            productModels = search.getProducts(); // changed from search.getPageProducts(); due to APPDEV-2797
            recentSearchTotalCount = search.getNoOfBrandFilteredProducts();
            Collection<String>spellingSuggestions = search.getSpellingSuggestions();
            if (!spellingSuggestions.isEmpty())
            	spellingSuggestion = spellingSuggestions.iterator().next();
            LOG.debug("SPELLING SUGGESTION - Did you mean? " + spellingSuggestion);
        } catch (FDException e) {
            throw new ServiceException(e);
        }

        LOG.debug("Total Products retrieved: " + search.getNoOfProductsBeforeProductFilters());
        LOG.debug("Total Products filtered retrieved: " + search.getNoOfProducts());

        CategoryNodeTree tree = search.getFilteredCategoryTree();
        departments = new HashSet<Department>();
        categories = new HashSet<Category>();

        //*********************************************************************************************************************
        //DOOR3 FD-iPad FDIP-637
        //Parallelize as many tasks as possible to try and improve performance for this call.
        final Set<TreeElement> treeRoots = tree.getRoots();
        final SortedSet<BrandModel> brandModels = search.getBrands();
        
        //Calculate how many total overall tasks we will be creating, so that we can track when the total
        //job is complete.
        final int numTreeTasks = treeRoots.size();
        final int numProductTasks = productModels.size();
        final int numBrandTasks = brandModels.size();
        final int totalTaskCount = numTreeTasks + numProductTasks + numBrandTasks;
        final List<Boolean> completedTasks = new ArrayList<Boolean>();
        Iterator<TreeElement> categoryIterator = treeRoots.iterator();
        while (categoryIterator.hasNext()) {
            final TreeElement treeElement = categoryIterator.next();
            ContentNodeModel model = treeElement.getModel();

            if (model instanceof DepartmentModel) {
                Department department = Department.wrap((DepartmentModel) model);
                departments.add(department);
            }
            
            es.execute(new Runnable()
            {
            	public void run()
            	{
        	        final Set<Category> localtempCategories = new HashSet<Category>();
            		try
            		{
	                    Collection<TreeElement> childElement = treeElement.getChildren();
	                    Iterator<TreeElement> childIterator = childElement.iterator();
	                    while (childIterator.hasNext()) {
	                        TreeElement grandSonElement = childIterator.next();
	
	                        if (grandSonElement.getModel() instanceof CategoryModel) {
	                            Category category = Category.wrap((CategoryModel) grandSonElement.getModel());
                            	localtempCategories.add(category);
	                        }
	                    }
            		}
            		finally
            		{
            			//merge results
                        synchronized(categories)
                        {
                        	categories.addAll(localtempCategories);
                        }

        				//Signal the controller that this unit of work is complete
            			synchronized(completedTasks)
            			{
            				completedTasks.add(true);
            				completedTasks.notify();
            			}
            		}
            	}
            });
        }

    	final FDSessionUser sessUser = user.getFDSessionUser();
    	final FDUser fduser = sessUser.getUser();
        for (ProductModel prod : productModels)
        {
        	final ProductModel product = prod;
        	
        	es.execute(new Runnable()
        	{
        		public void run()
        		{
                    try
                    {
                    	Product p = Product.wrap(product, fduser, null, EnumCouponContext.PRODUCT);
                    	
                    	synchronized(result)
                    	{
                    		result.add(p);
                    	}
                    }
                    catch (ModelException e)
                    {
                        LOG.warn("ModelException encountered while preparing search result.", e);
                    }
                    finally
                    {
        				//Signal the controller that this unit of work is complete
            			synchronized(completedTasks)
            			{
            				completedTasks.add(true);
            				completedTasks.notify();
            			}
                    }
        		}
        	});
        }      

        brands = new HashSet<Brand>();
      for (BrandModel b : brandModels)
      {
    	  final BrandModel brand = b;
    	  es.execute(new Runnable()
    	  {
    		  public void run()
    		  {
    			  try
    			  {
	    			  Brand b = Brand.wrap(brand);
	    			  
	    			  synchronized(brands)
	    			  {
	    				  brands.add(b);
	    			  }
    			  }
    			  finally
    			  {
					//Signal the controller that this unit of work is complete
					synchronized(completedTasks)
					{
						completedTasks.add(true);
						completedTasks.notify();
					}
    			  }
    		  }
    	  });
      }

      
      //Wait for all results:
      while(true)
      {
      	synchronized(completedTasks)
      	{
      		if( completedTasks.size() == totalTaskCount )
  				break;
  			else
  			{
	        		try
	        		{
	        			completedTasks.wait();
	        		}
	        		catch(InterruptedException IE)
	        		{
	        		}
  			}
      	}
      }
      
        //*********************************************************************************************************************

        return result;
    }

    @Override
    public List<Product> search(String searchTerm) throws ServiceException {
        return search(searchTerm, 1, 25, null);
    }

    public Set<Brand> getBrands() {
        return brands;
    }

    public void setBrands(Set<Brand> brands) {
        this.brands = brands;
    }

    public Set<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(Set<Department> departments) {
        this.departments = departments;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public int getRecentSearchTotalCount() {
        return recentSearchTotalCount;
    }

    public String getSpellingSuggestion() {
        return spellingSuggestion;
    }

    public void setSpellingSuggestion(String spellingSuggestion) {
        this.spellingSuggestion = spellingSuggestion;
    }

	@Override
    public List<String> getAutoSuggestions(String searchTerm) {
        AutoCompleteFacade facade = new AutoCompleteFacade();
        return facade.getTerms(searchTerm);
    }

}
