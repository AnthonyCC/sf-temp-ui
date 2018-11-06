package com.freshdirect.test;

import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.fdlogistics.services.impl.LogisticsServiceLocator;
import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.storeapi.application.CmsManager;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.Html;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.TagModel;
import com.freshdirect.storeapi.fdstore.FDContentTypes;
import com.freshdirect.test.ejb.TestSupportHome;
import com.freshdirect.test.ejb.TestSupportSB;



/**
 * Test supporter class
 *
 * @author segabor
 *
 */
public class TestSupport {
	private static TestSupport sharedInstance = null;

    private static final Comparator<ProductModel> SORT_PRODUCT_BY_FULL_NAME = new Comparator<ProductModel>() {

        @Override
        public int compare(ProductModel p1, ProductModel p2) {
            CategoryModel c1 = p1.getCategory();
            CategoryModel c2 = p2.getCategory();

            int c = c1.getFullName().compareTo(c2.getFullName());
            int p = p1.getFullName().compareTo(p2.getFullName());
            if (c == 0)
                return p;

            return c;
        }
    };

	private ServiceLocator serviceLocator = null;

	private TestSupport () throws NamingException {
		serviceLocator = new ServiceLocator(ErpServicesProperties.getInitialContext());
	}

	private TestSupportHome getTestSupportHome() {
		try {
			return (TestSupportHome) serviceLocator.getRemoteHome(
				"freshdirect.test.TestSupport");
		} catch (NamingException e) {
			throw new FDRuntimeException(e);
		}
	}


	/**
	 * Get shared instance.
	 * @return instance
	 */
	synchronized public static TestSupport getInstance() {
		if (sharedInstance == null) {
			try {
				sharedInstance = new TestSupport();
			} catch (NamingException e) {
				throw new FDRuntimeException(e,"Could not create test support shared instance");
			}
		}
		return sharedInstance;
	}


	/**
	 * Dummy method. Ignore.
	 */
	public void ping() {
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.TestSupportSB)){
			LogisticsServiceLocator.getInstance().getCommerceService().ping();
			}else {
			TestSupportSB bean = this.getTestSupportHome().create();
			bean.ping();
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}




	public List<Long> getDYFEligibleCustomerIDs() {
		List<Long> customersIds = null;
		try {

			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.TestSupportSB)){
				customersIds= LogisticsServiceLocator.getInstance().getCommerceService().getDYFEligibleCustomerIDs();

			}else {
			TestSupportSB bean = this.getTestSupportHome().create();
			customersIds= bean.getDYFEligibleCustomerIDs();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
			throw new FDRuntimeException(e);
		} catch (CreateException e) {
			e.printStackTrace();
			throw new FDRuntimeException(e);
		}
		return customersIds;
	}


	public List<Long> getErpCustomerIDs() {
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.TestSupportSB)){
			return LogisticsServiceLocator.getInstance().getCommerceService().getErpCustomerIds();

			}else {
			TestSupportSB bean = this.getTestSupportHome().create();

			return bean.getErpCustomerIds();
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Collections.emptyList();

	}

	public String getFDCustomerIDForErpId(String erp_id) {
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.TestSupportSB)){
				return LogisticsServiceLocator.getInstance().getCommerceService().getFDCustomerIDForErpId(erp_id);

				}else {
			TestSupportSB bean = this.getTestSupportHome().create();
			return bean.getFDCustomerIDForErpId(erp_id);
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String getErpIDForUserID(String user_id) {
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.TestSupportSB)){
				return LogisticsServiceLocator.getInstance().getCommerceService().getErpIDForUserID(user_id);
				}else {
			TestSupportSB bean = this.getTestSupportHome().create();
			return bean.getErpIDForUserID(user_id);
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}




	final static ContentType tagType = ContentType.Tag;
	public Collection<TagModel> getTags() {
		final Collection<ContentKey> tagKeys = CmsManager.getInstance().getContentKeysByType(tagType);

		final Map<ContentKey,TagModel> parentMap = new HashMap<ContentKey,TagModel>();

		final List<TagModel> tags = new ArrayList<TagModel>();
		for (ContentKey aKey : tagKeys) {
			final TagModel tm = (TagModel) ContentFactory.getInstance().getContentNodeByKey(aKey);

			for (TagModel ctm : tm.getChildren()) {
				parentMap.put(ctm.getContentKey(), tm);
			}

			tags.add( tm );
		}

		// sort tags in alphabetical order
		Collections.sort(tags, new Comparator<TagModel>() {
			@Override
			public int compare(TagModel o1, TagModel o2) {
				TagModel p1 = parentMap.get(o1.getContentKey());
				TagModel p2 = parentMap.get(o2.getContentKey());

				final int childOrd = o1.getName().compareTo(o2.getName());
				if (p1 == null && p2 == null)
					return childOrd;
				if (p1 == null && p2 != null) {
					final int z = o1.getName().compareTo(p2.getName());
					return z == 0 ? -1: z;
				}
				if (p1 != null && p2 == null){
					final int z = p1.getName().compareTo(o2.getName());
					return z == 0 ? 1: z;
				}

				final int parentOrd = p1.getName().compareTo(p2.getName());
				if (parentOrd == 0)
					return childOrd;

				return parentOrd;
			}
		});

		return tags;
	}

	public List<ProductModel> getTaggedProducts(String tagKey) {
		if (tagKey == null)
			return Collections.emptyList();

		// may throw IllegalArgumentException !
		ContentKey tKey = ContentKeyFactory.get(tagKey);

		List<ProductModel> taggedProducts = new ArrayList<ProductModel>();

		Set<ContentKey> productKeys = CmsManager.getInstance().getContentKeysByType(ContentType.Product);
		for (ContentKey productKey: productKeys) {
		    if (CmsManager.getInstance().isNodeOrphan(productKey)) {
		        continue;
		    }
            ProductModel product = (ProductModel) ContentFactory.getInstance().getContentNodeByKey(productKey);
            if (product == null || product.getDepartment() == null) {
                continue;
            }
            if ("Archive".equals(product.getDepartment().getContentKey().id)) {
                continue;
            }

            // filter products by tag
            for (TagModel assignedTag : product.getAllTags()) {
                if (tKey.equals(assignedTag.getContentKey())) {
                    taggedProducts.add(product);
                }
            }
		}

        Collections.sort(taggedProducts, SORT_PRODUCT_BY_FULL_NAME);

        return taggedProducts;
	}

	public List<CategoryModel> getCategories() {

		final List<ContentKey> categoryKeys = new ArrayList<ContentKey>(CmsManager.getInstance().getContentKeysByType(FDContentTypes.CATEGORY));
		List<CategoryModel> categories = new ArrayList<CategoryModel>();

		for (ContentKey categoryKey: categoryKeys) {
			ContentNodeModel m = ContentFactory.getInstance().getContentNodeByKey(categoryKey);
			if (m instanceof CategoryModel) {
				categories.add((CategoryModel)m);
			}

		}

		Collections.sort(categories, new Comparator<CategoryModel>() {
			@Override
			public int compare(CategoryModel o1, CategoryModel o2) {
				if (o1.getFullName() == null) return -1;
				if (o2.getFullName() == null) return  1;
				return o1.getFullName().compareTo(o2.getFullName());
			}
		});

		return categories;
	}

	public List<Method> getMediaMethods() {

		Method[] methods = CategoryModel.class.getMethods();
		List<Method> mediaMethodNames = new ArrayList<Method>();

		for (Method method : methods) {
			if (method.getReturnType().equals(Html.class)) {
				mediaMethodNames.add(method);
			}
		}

		Collections.sort(mediaMethodNames, new Comparator<Method>() {
			@Override
			public int compare(Method o1, Method o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		return mediaMethodNames;

	}


	public Collection<String> getSkuCodes() {
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.TestSupportSB)){
				return LogisticsServiceLocator.getInstance().getCommerceService().getSkuCodes();
			}else{
			TestSupportSB ejb = this.getTestSupportHome().create();
			return ejb.getSkuCodes();
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


}
