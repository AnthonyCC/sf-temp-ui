package com.freshdirect.webapp.ajax.browse.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.webapp.ajax.browse.FilteringFlowType;
import com.freshdirect.webapp.ajax.product.data.ProductData;

public class BrowseData implements Serializable {

	//individual data potatoes need to be encapsulated because js soy does not handle lists or primitives
	public static class SectionDataCointainer implements Serializable {
		private static final long serialVersionUID = -4336442837599815573L;
		private List<SectionData> sections;
		private int sectionMaxLevel;
		private boolean allSectionsEmpty;
		private boolean usePopularCategoriesLayout;
		private FilterLabelDataCointainer filterLabels = new FilterLabelDataCointainer();
		private int limit;

		public List<SectionData> getSections() {
			return sections;
		}
		public void setSections(List<SectionData> sections) {
			this.sections = sections;
		}
		public int getSectionMaxLevel() {
			return sectionMaxLevel;
		}
		public void setSectionMaxLevel(int sectionMaxLevel) {
			this.sectionMaxLevel = sectionMaxLevel;
		}
		public boolean isAllSectionsEmpty() {
			return allSectionsEmpty;
		}
		public void setAllSectionsEmpty(boolean allSectionsEmpty) {
			this.allSectionsEmpty = allSectionsEmpty;
		}
		public boolean isUsePopularCategoriesLayout() {
			return usePopularCategoriesLayout;
		}
		public void setUsePopularCategoriesLayout(boolean usePopularCategoriesLayout) {
			this.usePopularCategoriesLayout = usePopularCategoriesLayout;
		}
		public FilterLabelDataCointainer getFilterLabels() {
			return filterLabels;
		}

		public int getLimit() {
			return limit;
		}

		public void setLimit(int limit) {
			this.limit = limit;
		}
		
	}
	
	public static class BreadCrumbDataCointainer implements Serializable {
		private static final long serialVersionUID = -2682964497863787316L;
		private List<BasicData> breadCrumbs;

		public List<BasicData> getBreadCrumbs() {
			return breadCrumbs;
		}
		public void setBreadCrumbs(List<BasicData> breadCrumbs) {
			this.breadCrumbs = breadCrumbs;
		}
	}

	public static class FilterLabelDataCointainer implements Serializable {
		private static final long serialVersionUID = -4249560779935483596L;
		private List<ParentData> filterLabels;

		public List<ParentData> getFilterLabels() {
			return filterLabels;
		}
		public void setFilterLabels(List<ParentData> filterLabels) {
			this.filterLabels = filterLabels;
		}
	}
	
	public static class CarouselDataCointainer implements Serializable {
		private static final long serialVersionUID = 4610591383180714799L;
		private CarouselData carousel1;
		private CarouselData carousel2;
		private CarouselData carousel3;
		private String carouselPosition;
		private String carouselRatio;
		
		public CarouselData getCarousel1() {
			return carousel1;
		}
		public void setCarousel1(CarouselData carousel1) {
			this.carousel1 = carousel1;
		}
		public CarouselData getCarousel2() {
			return carousel2;
		}
		public void setCarousel2(CarouselData carousel2) {
			this.carousel2 = carousel2;
		}
		public String getCarouselPosition() {
			return carouselPosition;
		}
		public void setCarouselPosition(String carouselPosition) {
			this.carouselPosition = carouselPosition;
		}
		public String getCarouselRatio() {
			return carouselRatio;
		}
		public void setCarouselRatio(String carouselRatio) {
			this.carouselRatio = carouselRatio;
		}
		public CarouselData getCarousel3() {
			return carousel3;
		}
		public void setCarousel3(CarouselData carousel3) {
			this.carousel3 = carousel3;
		}
		
	}	
	
	public static class DescriptiveDataCointainer implements Serializable, DescriptiveDataI {
		
		private static final long serialVersionUID = 4991670021932771599L;
		private String url;
		private String titleBar;
		private String media;
		private String mediaLocation;
		private String middleMedia;
		private String pageTitle;
		private String oasSitePage;
		private String navDepth;
		private String contentId; //workaround for ajax response listening...
		private boolean isWineDepartment;
		private String metaDescription;
		
		@Override
        public String getMedia() {
			return media;
		}
		@Override
        public void setMedia(String media) {
			this.media = media;
		}
		@Override
        public String getMediaLocation() {
			return mediaLocation;
		}
		@Override
        public void setMediaLocation(String mediaLocation) {
			this.mediaLocation = mediaLocation;
		}
		@Override
        public String getMiddleMedia() {
			return middleMedia;
		}
		@Override
        public void setMiddleMedia(String middleMedia) {
			this.middleMedia = middleMedia;
		}
		public String getTitleBar() {
			return titleBar;
		}
		public void setTitleBar(String titleBar) {
			this.titleBar = titleBar;
		}
		public String getPageTitle() {
			return pageTitle;
		}
		public void setPageTitle(String pageTitle) {
			this.pageTitle = pageTitle;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getOasSitePage() {
			return oasSitePage;
		}
		public void setOasSitePage(String oasSitePage) {
			this.oasSitePage = oasSitePage;
		}
		public String getNavDepth() {
			return navDepth;
		}
		public void setNavDepth(String navDepth) {
			this.navDepth = navDepth;
		}
		public String getContentId() {
			return contentId;
		}
		public void setContentId(String contentId) {
			this.contentId = contentId;
		}
		public boolean isWineDepartment() {
			return isWineDepartment;
		}
		public void setWineDepartment(boolean isWineDepartment) {
			this.isWineDepartment = isWineDepartment;
		}
		public String getMetaDescription() {
			return metaDescription;
		}
		public void setMetaDescription(String metaDescription) {
			this.metaDescription = metaDescription;
		}
	}

	public static class MenuDataCointainer implements Serializable {
		private static final long serialVersionUID = 4341041465949744118L;
		private List<MenuBoxData> menuBoxes;
		private String menuName;
		private String menuId = null;
		private String menuUrl = null;

		public List<MenuBoxData> getMenuBoxes() {
			return menuBoxes;
		}
		public void setMenuBoxes(List<MenuBoxData> menuBoxes) {
			this.menuBoxes = menuBoxes;
		}

		public String getMenuName() {
			return menuName;
		}
		public void setMenuName(String menuName) {
			this.menuName = menuName;
		}

		public String getMenuId() {
			return menuId;
		}

		public void setMenuId(String menuId) {
			this.menuId = menuId;
		}

		public String getMenuUrl() {
			return menuUrl;
		}

		public void setMenuUrl(String menuUrl) {
			this.menuUrl = menuUrl;
		}
	}

	public static class SortDataCointainer implements Serializable {
		private static final long serialVersionUID = 2253861947116960072L;
		private List<SortOptionData> sortOptions;
		private boolean currentOrderAsc;
		private List<SortDropDownData> sortDropDowns;
		
		public List<SortOptionData> getSortOptions() {
			return sortOptions;
		}
		public void setSortOptions(List<SortOptionData> sortOptions) {
			this.sortOptions = sortOptions;
		}
		public boolean isCurrentOrderAsc() {
			return currentOrderAsc;
		}
		public void setCurrentOrderAsc(boolean currentOrderAsc) {
			this.currentOrderAsc = currentOrderAsc;
		}
		public List<SortDropDownData> getSortDropDowns() {
			return sortDropDowns;
		}
		public void setSortDropDowns(List<SortDropDownData> sortDropDowns) {
			this.sortDropDowns = sortDropDowns;
		}
	}
	
	public static class DDPPProducts implements Serializable {
	
		private static final long serialVersionUID = -5076065046727187803L;
		private List<ProductData> products = new ArrayList<ProductData>();

		public List<ProductData> getProducts() {
			return products;
		}

		public void setProducts(List<ProductData> products) {
			this.products = products;
		}
		
	}
	
	public static class AssortProducts implements Serializable {
		private static final long serialVersionUID = 2252485502495019016L;
		private Map<String, List<ProductData>> cats = new HashMap<String, List<ProductData>>();
		private List<ProductData> products = new ArrayList<ProductData>();

		public List<ProductData> getProducts(String cat) {
			if (cats.containsKey(cat)) {
				return cats.get(cat);
			} else {
				return new ArrayList<ProductData>();
			}
		}

		public Map<String, List<ProductData>> getCats() {
			return cats;
		}

		public void setCats(Map<String, List<ProductData>> cats) {
			this.cats = cats;
		}

		public void addProdDataToCat(String curCat, ProductData productData) {
			/*List<ProductData> temp = this.getProducts(curCat);
			temp.add(productData);
			this.getCats().put(curCat, temp);*/
			if(this.cats.containsKey(curCat)){
            	List<ProductData> productDataList=this.cats.get(curCat);
            	productDataList.add(productData);
            	this.cats.put(curCat, productDataList);
            } else {
            	List<ProductData> productDataList=new ArrayList<ProductData>();
            	productDataList.add(productData);
            	this.cats.put(curCat, productDataList);
            }
		}
	}
	public static class HLBrandAdProducts implements Serializable {
	
		private static final long serialVersionUID = -4153578565069486053L;
		
		
		private List<ProductData> products = new ArrayList<ProductData>();
		private String pageBeacon = null;
		
		
		private Map<String, List<ProductData>> hlSelectionOfProductList = new HashMap<String, List<ProductData>>();
		
		private Map<String, String> hlSelectionsPageBeacons = new HashMap<String, String>();
		
		public Map<String, List<ProductData>> getHlSelectionOfProductList() {
			return hlSelectionOfProductList;
		}

		public void setHlSelectionOfProductList(
				Map<String, List<ProductData>> hlSelectionOfProductList) {
			this.hlSelectionOfProductList = hlSelectionOfProductList;
		}

		public List<ProductData> getProducts() {
			return products;
		}

		public void setProducts(List<ProductData> products) {
			this.products = products;
		}

		public String getPageBeacon() {
			return pageBeacon;
		}

		public void setPageBeacon(String pageBeacon) {
			this.pageBeacon = pageBeacon;
		}

		public Map<String, String> getHlSelectionsPageBeacons() {
			return hlSelectionsPageBeacons;
		}

		public void setHlSelectionsPageBeacons(
				Map<String, String> hlSelectionsPageBeacons) {
			this.hlSelectionsPageBeacons = hlSelectionsPageBeacons;
		}
	}
		
	public static class SearchParams implements Serializable {

		private static final long serialVersionUID = 7747507856218538577L;
		private String searchParams;
		private String listSearchParams;
		private List<String> listSearchParamsList;
		private String searchTerm;
		private List<String> suggestions;
		private List<Tab> tabs = null;
		private FilteringFlowType pageType;
		private Integer numberOfNewProducts = null;
		
		public void buildTabs(String label, String type, int hits, int filteredHits, boolean active) {
			initTabs();
			Tab tab = new Tab(label, type, hits, filteredHits, active);
			tabs.add(tab);
		}
		
		private void initTabs() {
			if (tabs == null) {
				tabs = new ArrayList<Tab>();
			}
		}

		public String getSearchParams() {
			return searchParams;
		}
		public void setSearchParams(String searchParams) {
			this.searchParams = searchParams;
		}
		public List<String> getSuggestions() {
			return suggestions;
		}
		public void setSuggestions(List<String> suggestions) {
			this.suggestions = suggestions;
		}
		public String getSearchTerm() {
			return searchTerm;
		}
		public void setSearchTerm(String searchTerm) {
			this.searchTerm = searchTerm;
		}
		
		public static class Tab implements Serializable {
			private static final long serialVersionUID = 2196644205140925786L;
			private String label;
			private String type;
			private int hits;
			private int filteredHits;
			private boolean active;
			
			public Tab(String label, String type, int hits, int filteredHits, boolean active) {
				this.label = label;
				this.type = type;
				this.hits = hits;
				this.filteredHits = filteredHits;
				this.active = active;
			}
			
			public String getLabel() {
				return label;
			}
			public void setLabel(String label) {
				this.label = label;
			}
			public String getType() {
				return type;
			}
			public void setType(String type) {
				this.type = type;
			}
			public int getHits() {
				return hits;
			}
			public void setHits(int hits) {
				this.hits = hits;
			}
			public int getFilteredHits() {
				return filteredHits;
			}
			public void setFilteredHits(int filteredHits) {
				this.filteredHits = filteredHits;
			}
			public boolean isActive() {
				return active;
			}
			public void setActive(boolean active) {
				this.active = active;
			}
		}
		public FilteringFlowType getPageType() {
			return pageType;
		}
		public void setPageType(FilteringFlowType pageType) {
			this.pageType = pageType;
		}

		public List<Tab> getTabs() {
			return tabs;
		}

		public Integer getNumberOfNewProducts() {
			return numberOfNewProducts;
		}

		public void setNumberOfNewProducts(Integer numberOfNewProducts) {
			this.numberOfNewProducts = numberOfNewProducts;
		}

		public String getListSearchParams() {
			return listSearchParams;
		}

		public void setListSearchParams(String listSearchParams) {
			this.listSearchParams = listSearchParams;
		}

		public List<String> getListSearchParamsList() {
			return listSearchParamsList;
		}

		public void setListSearchParamsList(List<String> listSearchParamsList) {
			this.listSearchParamsList = listSearchParamsList;
		}
	}
	
	//end of inner class definitions

	private static final long serialVersionUID = -1057711757351364777L;
	
	private SectionDataCointainer sections = new SectionDataCointainer();
	private BreadCrumbDataCointainer breadCrumbs = new BreadCrumbDataCointainer();
	private CarouselDataCointainer carousels = new CarouselDataCointainer();
	private DescriptiveDataCointainer descriptiveContent = new DescriptiveDataCointainer();
	private MenuDataCointainer menuBoxes = new MenuDataCointainer();
	private SortDataCointainer sortOptions = new SortDataCointainer();
	private PagerData pager; //not a *DataContainer inner class which only encapsulates lists and primitives
	private SearchParams searchParams = new SearchParams();
	private DDPPProducts ddppProducts = new DDPPProducts();
	private AssortProducts assortProducts = new AssortProducts();
	private HLBrandAdProducts adProducts = new HLBrandAdProducts();
    private String topMedia;
	
	public PagerData getPager() {
		return pager;
	}
	public void setPager(PagerData pager) {
		this.pager = pager;
	}
	public SectionDataCointainer getSections() {
		return sections;
	}
	public BreadCrumbDataCointainer getBreadCrumbs() {
		return breadCrumbs;
	}
	public CarouselDataCointainer getCarousels() {
		return carousels;
	}
	public DescriptiveDataCointainer getDescriptiveContent() {
		return descriptiveContent;
	}
	public MenuDataCointainer getMenuBoxes() {
		return menuBoxes;
	}
	public SortDataCointainer getSortOptions() {
		return sortOptions;
	}
	public SearchParams getSearchParams() {
		return searchParams;
	}
	public void setSearchParams(SearchParams searchParams) {
		this.searchParams = searchParams;
	}	
	public DDPPProducts getDDPPProducts() {
		return ddppProducts;
	}
	public void setDDPPProducts(DDPPProducts ddppProducts) {
		this.ddppProducts = ddppProducts;
    }

	public AssortProducts getAssortProducts() {
		return assortProducts;
	}
	public void setAssortProducts(AssortProducts assortProducts) {
		this.assortProducts = assortProducts;
	}

    public String getTopMedia() {
        return topMedia;
    }

    public void setTopMedia(String topMedia) {
        this.topMedia = topMedia;
    }
	public HLBrandAdProducts getAdProducts() {
		return adProducts;
	}
	public void setAdProducts(HLBrandAdProducts adProducts) {
		this.adProducts = adProducts;
	}

}
