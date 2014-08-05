package com.freshdirect.webapp.ajax.browse.data;

import java.io.Serializable;
import java.util.List;

public class BrowseData implements Serializable {

	//individual data potatoes need to be encapsulated because js soy does not handle lists or primitives
	public static class SectionDataCointainer implements Serializable {
		private static final long serialVersionUID = -4336442837599815573L;
		private List<SectionData> sections;
		private int sectionMaxLevel;
		private boolean allSectionsEmpty;
		private boolean usePopularCategoriesLayout;
		private FilterLabelDataCointainer filterLabels = new FilterLabelDataCointainer();

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
		
		public String getMedia() {
			return media;
		}
		public void setMedia(String media) {
			this.media = media;
		}
		public String getMediaLocation() {
			return mediaLocation;
		}
		public void setMediaLocation(String mediaLocation) {
			this.mediaLocation = mediaLocation;
		}
		public String getMiddleMedia() {
			return middleMedia;
		}
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
	}

	public static class MenuDataCointainer implements Serializable {
		private static final long serialVersionUID = 4341041465949744118L;
		private List<MenuBoxData> menuBoxes;

		public List<MenuBoxData> getMenuBoxes() {
			return menuBoxes;
		}
		public void setMenuBoxes(List<MenuBoxData> menuBoxes) {
			this.menuBoxes = menuBoxes;
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
	
	//end of inner class definitions

	private static final long serialVersionUID = -1057711757351364777L;
	
	private SectionDataCointainer sections = new SectionDataCointainer();
	private BreadCrumbDataCointainer breadCrumbs = new BreadCrumbDataCointainer();
	private CarouselDataCointainer carousels = new CarouselDataCointainer();
	private DescriptiveDataCointainer descriptiveContent = new DescriptiveDataCointainer();
	private MenuDataCointainer menuBoxes = new MenuDataCointainer();
	private SortDataCointainer sortOptions = new SortDataCointainer();
	private PagerData pager; //not a *DataContainer inner class which only encapsulates lists and primitives
	
	
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
}
