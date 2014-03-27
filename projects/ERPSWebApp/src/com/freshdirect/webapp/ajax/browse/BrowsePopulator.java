package com.freshdirect.webapp.ajax.browse;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.browse.data.BrowseData;
import com.freshdirect.webapp.ajax.browse.data.CmsFilteringFlowResult;

public class BrowsePopulator {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getInstance(BrowsePopulator.class);

	public static BrowseData createBrowseData(CmsFilteringFlowResult result) throws HttpErrorResponse {
		return result.getBrowseDataPrototype();
	}

	
//	public static BrowseData getMockPotato(FDSessionUser  user, String id){
//		BrowseData data = new BrowseData();
//		CategoryModel cat = (CategoryModel) ContentFactory.getInstance().getContentNode(id);
//		
//		data.setMedia(MediaUtils.renderHtmlToString(cat.getCategoryBanner(), user)); 
//		
//		
//		List<SectionData> superSections = new ArrayList<SectionData>();
//		SectionData superSection = new SectionData();
//		superSections.add(superSection);
//		
//		superSection.setMedia(MediaUtils.renderHtmlToString(cat.getEditorial(),user)); //cat.getDescription(); //TODO use description only
//		data.setSections(superSections);
//		
//
//		List<ProductData> tempProductDatas = null;
//		List<SectionData> sections = new ArrayList<SectionData>();
//		
//		for (CategoryModel subCat : cat.getSubcategories()){
//			
//			SectionData section = createSection(subCat, user);
//			sections.add(section);
//			
//			List<SectionData> subSections = new ArrayList<SectionData>();
//			
//			for (CategoryModel subSubCat : subCat.getSubcategories()){
//				SectionData subSection = createSection(subSubCat, user);
//				appendProductDatas(user, subSection, subSubCat);
//				subSections.add(subSection);
//				
//				tempProductDatas= subSection.getProducts();
//			}
//			
//			
//			section.setSections(checkEmpty(subSections));
//			
//			if (section.getSections() == null){
//				appendProductDatas(user, section, subCat);
//			}
//		}
//
//		superSection.setSections(checkEmpty(sections));
//		
//		
//		List<BasicData> breadCrumbs = new ArrayList<BasicData>();
//		BasicData breadCrumb = new BasicData();
//		breadCrumb.setId("mea");
//		breadCrumb.setName("Meat");
//		breadCrumbs.add(breadCrumb);
//		
//		breadCrumb = new BasicData();
//		breadCrumb.setId("mt_c");
//		breadCrumb.setName("Chicken");
//		breadCrumbs.add(breadCrumb);
//		data.setBreadCrumbs(breadCrumbs);
//		
//		
//		List<MenuBoxData> menuBoxes = new ArrayList<MenuBoxData>();
//		MenuBoxData menuBox = new MenuBoxData();
//		menuBox.setName("Menu 1");
//		List<MenuItemData> menuItems = new ArrayList<MenuItemData>();
//		MenuItemData menuItem = new MenuItemData();
//		menuItem.setName("Item 1.1");
//		menuItem.setId("1.1");
//		menuItems.add(menuItem);
//		menuItem = new MenuItemData();
//		menuItem.setName("Item 1.2");
//		menuItem.setId("1.2");
//		menuItems.add(menuItem);
//		menuBox.setItems(menuItems);
//		menuBoxes.add(menuBox);
//		
//		menuBox = new MenuBoxData();
//		menuBox.setName("Menu 2");
//		menuItems = new ArrayList<MenuItemData>();
//		menuItem = new MenuItemData();
//		menuItem.setName("Item 2.1");
//		menuItem.setId("2.1");
//		menuItems.add(menuItem);
//		menuItem = new MenuItemData();
//		menuItem.setName("Item 2.2");
//		menuItem.setId("2.2");
//		menuItems.add(menuItem);
//		menuBox.setItems(menuItems);
//		menuBoxes.add(menuBox);
//		data.setMenuBoxes(menuBoxes);
//		
//		data.setTitle("/media_stat/images/wine/sort_pop.gif");
//		
//		PagerData pager = new PagerData();
//		pager.setCurrentPage(1);
//		pager.setPageCount(12);
//		pager.setFirstItemIndex(5);
//		pager.setLastItemIndex(9);
//		data.setPager(pager);
//		
//		
//		CarouselData carousel = new CarouselData();
//		carousel.setName("Carousel 1");
//		carousel.setProducts(tempProductDatas);
//		data.setCarousel1(carousel);
//
//		carousel = new CarouselData();
//		carousel.setName("Carousel 2");
//		carousel.setProducts(tempProductDatas);
//		data.setCarousel2(carousel);
//
//		carousel = new CarouselData();
//		carousel.setName("Carousel 3");
//		carousel.setProducts(tempProductDatas);
//		data.setCarousel3(carousel);
//
//		carousel = new CarouselData();
//		carousel.setName("Carousel 4");
//		carousel.setProducts(tempProductDatas);
//		data.setCarousel4(carousel);
//
//		List<SelectableData> sortOptions = new ArrayList<SelectableData>();
//		SelectableData sortOption = new SelectableData();
//		sortOption.setId("1");
//		sortOption.setName("option1");
//		sortOption.setSelected(true);
//		sortOptions.add(sortOption);
//
//		sortOption = new SelectableData();
//		sortOption.setId("2");
//		sortOption.setName("option2");
//		sortOptions.add(sortOption);
//
//		sortOption = new SelectableData();
//		sortOption.setId("3");
//		sortOption.setName("option3");
//		sortOptions.add(sortOption);
//		data.setSortOptions(sortOptions);
//		
//		return data;
//	}
//
//	private static SectionData createSection(CategoryModel cat, FDSessionUser user){
//		SectionData section = new SectionData();
//		Image headerImage = cat.getNameImage();
//		
//		if (headerImage == null){
//			section.setHeaderText(cat.getFullName());
//		} else {
//			section.setHeaderImage(headerImage.getPath());
//		}
//		
//		section.setMedia(MediaUtils.renderHtmlToString(cat.getDescription(),user));
//		return section;
//	}
//	
//	private static void appendProductDatas(FDUserI user, SectionData section, CategoryModel cat) {
//		List<ProductData> productDatas = new ArrayList<ProductData>();
//		for (ProductModel product : cat.getProducts()) {
//			try{
//				productDatas.add(ProductDetailPopulator.createProductData(user, product, true));
//			} catch (Exception e){
//				e.printStackTrace();
//			}
//
//		}
//		
//		section.setProducts(checkEmpty(productDatas));
//	}
//	
//	private static <T extends Collection<?>> T checkEmpty(T col){
//		return col.size()>0 ? col : null; //TODO do we need this?
//	}
}
