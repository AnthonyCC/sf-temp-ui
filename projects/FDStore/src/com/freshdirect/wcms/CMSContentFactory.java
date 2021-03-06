package com.freshdirect.wcms;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;
import org.joda.time.DateTime;
import org.joda.time.DateTime.Property;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.freshdirect.cms.CmsServiceLocator;
import com.freshdirect.cms.contentio.xml.FlexContentHandler;
import com.freshdirect.cms.core.converter.ScalarValueConverter;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.core.domain.Scalar;
import com.freshdirect.cms.core.service.ContentTypeInfoService;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.cms.CMSPublishManager;
import com.freshdirect.framework.util.BeanUtil;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.ContentNode;
import com.freshdirect.storeapi.ContentNodeI;
import com.freshdirect.storeapi.application.CmsManager;
import com.freshdirect.storeapi.content.CMSAnchorModel;
import com.freshdirect.storeapi.content.CMSComponentModel;
import com.freshdirect.storeapi.content.CMSComponentType;
import com.freshdirect.storeapi.content.CMSImageBannerModel;
import com.freshdirect.storeapi.content.CMSImageModel;
import com.freshdirect.storeapi.content.CMSPageRequest;
import com.freshdirect.storeapi.content.CMSPickListItemModel;
import com.freshdirect.storeapi.content.CMSPickListModel;
import com.freshdirect.storeapi.content.CMSScheduleModel;
import com.freshdirect.storeapi.content.CMSSectionModel;
import com.freshdirect.storeapi.content.CMSTextComponentModel;
import com.freshdirect.storeapi.content.CMSWebPageModel;
import com.freshdirect.storeapi.content.ContentFactory;

public class CMSContentFactory {

	private static final String FEED_CACHE = "cmsPageCache";
	private static final Category LOG = LoggerFactory.getInstance(CMSContentFactory.class);
	private static CMSContentFactory instance = null;

	private Map<ContentKey,ContentNodeI> contentNodesMap = new HashMap<ContentKey,ContentNodeI>();

	private ContentTypeInfoService contentTypeInfoService = CmsServiceLocator.contentTypeInfoService();

	public static CMSContentFactory getInstance(){
		if(instance == null){
			init();
		}
		return instance;
	}

	public static void init(){
		instance = new CMSContentFactory();
		instance.cacheAllPages();
	}

    public static void evictPageCache() {
        instance = null;
    }
	
	public void cacheAllPages(){
	    CMSPageRequest pageRequest = new CMSPageRequest();
        pageRequest.setPlantId(ContentFactory.getInstance().getCurrentUserContext().getFulfillmentContext().getPlantId());
	    cacheAllPages(pageRequest);
	}

	public void cacheAllPages(CMSPageRequest pageRequest) {
		LOG.debug("Loading all pages in cache "+ new Date());
		List<CMSWebPageModel> pages = getCMSPageByParameters(pageRequest);
		if(pages.size() == 0) {
			CmsServiceLocator.ehCacheUtil().clearCache(FEED_CACHE);
		} else {
			for(CMSWebPageModel page: pages){
				if(page != null){
	                 CmsServiceLocator.ehCacheUtil().putObjectToCache(FEED_CACHE, pageRequest.getCacheKey(page), page);
				}
			}
		}
	}

	public final List<CMSPickListItemModel> getPickListByParameter(CMSPageRequest request){
		List<CMSPickListItemModel> pickLists = new ArrayList<CMSPickListItemModel>();
        Set<ContentKey> contentKeys = CmsManager.getInstance().getContentKeysByType(ContentType.PickList);
        Map<ContentKey, ContentNodeI> contentNodes = CmsManager.getInstance().getContentNodes(contentKeys);
		for(Entry<ContentKey, ContentNodeI> contentNodeEntry: contentNodes.entrySet()){
			ContentNodeI contentNode = contentNodeEntry.getValue();
			CMSPickListItemModel pickList = createPickList(contentNode, request);
			pickLists.add(pickList);
		}
		return pickLists;
	}

	public final List<CMSWebPageModel> getCMSPageByParameters(CMSPageRequest pageRequest){
		loadFeedNodes(pageRequest);
		return getCMSPages(pageRequest);
	}

	public List<CMSWebPageModel> getCMSPages(CMSPageRequest pageRequest) {
        List<CMSWebPageModel> response = new ArrayList<CMSWebPageModel>();
		if(contentNodesMap != null && !contentNodesMap.isEmpty()){
			for(Entry<ContentKey, ContentNodeI> contentNodeEntry: contentNodesMap.entrySet()){
				ContentNodeI contentNode = contentNodeEntry.getValue();

				CMSWebPageModel page = null;
                if (pageRequest.isPreview() || pageRequest.getFeedId() != null) {
					// if it is for preview and feed id is not null, check the id first and the get the relevant feed page
					pageRequest.setIgnoreSchedule(true);
					
					if(contentNode.getKey().getId().toString().equals(pageRequest.getFeedId())) {
						page = getCMSPage(contentNode, pageRequest);
					}
				} else {
					page = getCMSPage(contentNode, pageRequest);
				}
				if(page != null){
					boolean addToResponse = true;
					if(pageRequest.getPageType() != null && ! pageRequest.getPageType().equals(page.getType()) && !pageRequest.isPreview()){
						//if page type is not null and if it is matching the page type from the request and if it is not preview don't return the response.
						addToResponse = false;
					} else {
						//Return all the pages if page type is null in request.
						addToResponse = true;
					}
					if(addToResponse){
						response.add(page);
					}
				}
			}
		}
		return response;
    }

    private void loadFeedNodes(CMSPageRequest pageRequest) {
        //if preview load from cms db, else read from erps feed table.
		if(pageRequest.isPreview()){
			final CmsManager manager = CmsManager.getInstance();
            Set<ContentKey> keys = manager.getContentKeysByType(ContentType.WebPage);
            contentNodesMap = manager.getContentNodes(keys);
		} else {
			String data = getFeedContent();
			if(StringUtils.isNotBlank(data)){
				contentNodesMap = loadNodesFromXMLString(data);
			}
		}
    }

	public final CMSWebPageModel getCMSPageByName(String pageName) {
        return (CMSWebPageModel) CmsServiceLocator.ehCacheUtil().getObjectFromCache(CMSContentFactory.FEED_CACHE, pageName);
	}

	public final CMSWebPageModel getCMSPage(ContentNodeI contentNode, CMSPageRequest request){
		CMSWebPageModel webPage = null;
		if(contentNode != null){
		    webPage = new CMSWebPageModel();
		    webPage.setId(request.getFeedId());
		    
            if (EnumEStoreId.FDX == CmsManager.getInstance().getEStoreEnum()) {
                webPage.setTitle((String) contentNode.getAttributeValue("PAGE_TITLE_FDX"));
                webPage.setSeoMetaDescription((String) contentNode.getAttributeValue("SEO_META_DESC_FDX"));
            } else {
                webPage.setTitle((String) contentNode.getAttributeValue("PAGE_TITLE"));
                webPage.setSeoMetaDescription((String) contentNode.getAttributeValue("SEO_META_DESC"));
            }
			webPage.setType((String)contentNode.getAttributeValue("WebPageType"));
			List<CMSScheduleModel> schedules = createSchedule(contentNode,"WebPageSchedule", request);
			webPage.setSchedule(schedules);

			boolean matchingSchedule = isSchedulesMatches(schedules, request, true);
			if(!matchingSchedule && !request.isIgnoreSchedule()) {
				//LOG.debug("Schedule is not matching for :"+ contentNode.getKey());
				return null;
			}

			List<ContentKey> darkStoreContentkey = (List<ContentKey>) contentNode.getAttributeValue("WebPageDarkStore");
			ArrayList<String> darkStoreFromCMS = new ArrayList<String>();

			if (darkStoreContentkey != null) {
				for (ContentKey key : darkStoreContentkey) {
					ContentNodeI contentnode = getContentNodeByKey(key, request);
					if (contentnode != null)
						darkStoreFromCMS.add(((String) (contentnode.getAttributeValue("value"))));
				}
			}

			if (darkStoreFromCMS.isEmpty() || darkStoreFromCMS.contains(request.getPlantId())) {
				List<CMSSectionModel> sections = getPageSections(contentNode,
						request);
				if (sections != null && !sections.isEmpty()) {
					webPage.setSections(sections);
				}
			}
		}
		if(webPage.getSections() != null){
			return webPage;
		} else {
			return null;
		}
	}

	public final List<CMSSectionModel> getPageSections(ContentNodeI pageNode, CMSPageRequest request){
		List<CMSSectionModel> sections = new ArrayList<CMSSectionModel>();
		List<CMSScheduleModel> schedules = null;
		if(pageNode != null){
			List<ContentKey> sectionKeys = getContentKeysList(pageNode, "WebPageSection");
			if(BeanUtil.isNotEmpty(sectionKeys)){
				for(ContentKey sectionKey: sectionKeys) {
					ContentNodeI sectionNode = getContentNodeByKey(sectionKey, request);
					if(sectionNode!=null) {
						
						CMSSectionModel section = new CMSSectionModel();
						schedules = createSchedule(getContentKeysList(sectionNode,"SectionSchedule"), request);
						
						List<ContentKey> darkStoreContentkey = (List<ContentKey>) sectionNode.getAttributeValue("SectionDarkStore");
						ArrayList<String> darkStoreFromCMS = new ArrayList<String>();

						if (darkStoreContentkey != null) {
							for (ContentKey key : darkStoreContentkey) {
								ContentNodeI contentnode = getContentNodeByKey(key, request);
								if (contentnode != null) {
									darkStoreFromCMS.add(((String) (contentnode.getAttributeValue("value"))));
								}
							}
						}

						if (isSchedulesMatches(schedules, request, false) && sectionNode != null) {
							if (darkStoreFromCMS.isEmpty() || darkStoreFromCMS.contains(request.getPlantId())) {
								section.setName((String)sectionNode.getAttributeValue("name"));
								section.setType((String)sectionNode.getAttributeValue("Type"));
								section.setCaptionText((String)sectionNode.getAttributeValue("captionText"));
								section.setBodyText((String)sectionNode.getAttributeValue("bodyText"));
								section.setLinkTarget(getEncodedContentKey(sectionNode,"linkTarget"));
								section.setDisplayType((String)sectionNode.getAttributeValue("displayType"));
								section.setDrawer((Boolean)sectionNode.getAttributeValue("drawer"));
								section.setHeadlineText((String)sectionNode.getAttributeValue("headlineText"));
								section.setLinkText((String)sectionNode.getAttributeValue("linkText"));
								section.setLinkType((String)sectionNode.getAttributeValue("linkType"));
								section.setLinkURL((String)sectionNode.getAttributeValue("linkURL"));
								Integer maximumProductLimit = (Integer) NVL.apply(sectionNode.getAttributeValue(ContentTypes.Section.maximumProductLimit),
										FDStoreProperties.getSectionProductLimitMaximumDefault());
								Integer minimumProductLimit = (Integer) NVL.apply(sectionNode.getAttributeValue(ContentTypes.Section.minimumProductLimit),
										FDStoreProperties.getSectionProductLimitMinimumDefault());
								section.setMinimumProductLimit(minimumProductLimit);
								section.setMaximumProductLimit(maximumProductLimit);
								if(sectionNode.getAttributeValue("imageBanner")!=null)
									section.setImageBanner(createImageBanner(getContentNodeByKey((ContentKey)sectionNode.getAttributeValue("imageBanner"), request), request));
								List<ContentKey> prodKeys = getContentKeysList(sectionNode, "product");
								List<ContentKey> musthaveprodKeys = getContentKeysList(sectionNode, "mustHaveProduct");
								List<ContentKey> categoryKeys = getContentKeysList(sectionNode, "category");
								List<ContentKey> pickListKeys = getContentKeysList(sectionNode, "pickList");
								List<String> productList = new ArrayList<String>();
								List<String> musthaveproductList = new ArrayList<String>();
								List<String> categoryList = new ArrayList<String>();
								List<CMSPickListModel> pickListList = new ArrayList<CMSPickListModel>();
								if(prodKeys != null){
									for(ContentKey key:prodKeys){
										productList.add(key.getEncoded());
									}
									if(productList!=null && productList.size()>0)
										section.setProductList(productList);
								}
								if(musthaveprodKeys != null){
									for(ContentKey key:musthaveprodKeys){
										musthaveproductList.add(key.getEncoded());
									}
									if(musthaveproductList!=null && musthaveproductList.size()>0)
										section.setMustHaveProdList(musthaveproductList);
								}
								if(categoryKeys != null){
									for(ContentKey key:categoryKeys){
										categoryList.add(key.getEncoded());
									}
									if(categoryList!=null && categoryList.size()>0)
										section.setCategoryList(categoryList);
								}
								if(pickListKeys != null){
									for(ContentKey key:pickListKeys){
										pickListList.add((CMSPickListModel) createPickList(getContentNodeByKey(key, request), request));
									}
									if(pickListList!=null && pickListList.size()>0)
										section.setPickList(pickListList);
								}
								List<CMSComponentModel> components = getSectionComponents(sectionNode, request);
								if(components != null && !components.isEmpty()){
									section.setComponents(components);
								}
								section.setSchedules(schedules);
								sections.add(section);
							}
						} else {
							LOG.debug("Schedule is not matching for :"+ pageNode.getKey());
						}
					} else {
						LOG.debug("FEED-CRITICALERROR01: SECTIONNODE MISSING : "+ pageNode.getKey() + " : " + sectionKey);
					}
				}

				if(sections.isEmpty()){
					sections = null;
				}
			}
		}
        return sections;
	}

	private List<CMSComponentModel> getSectionComponents(ContentNodeI sectionNode, CMSPageRequest request) {
		List<CMSComponentModel> components = new ArrayList<CMSComponentModel>();
		if(sectionNode != null){
			List<ContentKey> componentKeys = getContentKeysList(sectionNode,"SectionComponent");
			if(BeanUtil.isNotEmpty(componentKeys)){
				for(ContentKey componentKey: componentKeys){
					ContentNodeI componentNode = getContentNodeByKey(componentKey, request);
					if("Anchor".equals(componentKey.getType().name())){
						addComponentsToSection(components,createAnchor(componentNode));
					} else if("TextComponent".equals(componentKey.getType().name())){
						CMSTextComponentModel text = new CMSTextComponentModel();
						text.setComponentType(CMSComponentType.TEXT);
						text.setText((String)componentNode.getAttributeValue("Text"));
						text.setType((String)componentNode.getAttributeValue("Type"));
						addComponentsToSection(components,text);
					} else if("PickList".equals(componentKey.getType().name())){
						CMSPickListModel pickList = (CMSPickListModel) createPickList(componentNode, request);
						addComponentsToSection(components,pickList);
					} else if("ImageBanner".equals(componentKey.getType().name())){
						addComponentsToSection(components,createImageBanner(componentNode, request));
					} else {
						//Send as raw component with id.
						addComponentsToSection(components,createGenericComponent(componentKey));
					}
				}
			}
		}
		return components;
	}

	public CMSComponentModel createGenericComponent(ContentKey componentKey){
		String componentType = componentKey.getType().name();
		CMSComponentModel component = new CMSComponentModel();
		component.setId(componentKey.getId());
		component.setComponentType(CMSComponentType.valueOf(componentType.toUpperCase()));
		return component;
	}

	private void addComponentsToSection(List<CMSComponentModel> components, CMSComponentModel component){
		if(component != null){
			components.add(component);
		}
	}

	/*public List<CMSAnchorModel> createAnchor(List<ContentKey> keys){
		List<CMSAnchorModel> anchors = null;
		if(keys != null){
			anchors = new ArrayList<CMSAnchorModel>();
			for(ContentKey key: keys){
				anchors.add(createAnchor(getContentNodeByKey(key)));
			}
		}
		return anchors;
	}*/

	public CMSAnchorModel createAnchor(ContentNodeI componentNode){
		CMSAnchorModel anchor = new CMSAnchorModel();
		anchor.setUrl((String)componentNode.getAttributeValue("Url"));
		anchor.setTarget(getEncodedContentKey(componentNode, "Target"));
		anchor.setText((String)componentNode.getAttributeValue("Text"));
		if("Button".equals(componentNode.getAttributeValue("Type"))){
			anchor.setComponentType(CMSComponentType.BUTTON);
		} else {
			anchor.setComponentType(CMSComponentType.ANCHOR);
		}
		return anchor;
	}

	private CMSImageBannerModel createImageBanner(ContentNodeI componentNode, CMSPageRequest request) {
		CMSImageBannerModel banner = null;
		if(componentNode != null){
			banner = new CMSImageBannerModel();
			banner.setComponentType(CMSComponentType.BANNER);
			banner.setName((String)componentNode.getAttributeValue("Name"));
			banner.setDescription((String)componentNode.getAttributeValue("Description"));
			banner.setType((String)componentNode.getAttributeValue("Type"));
			banner.setImage(createImage((ContentKey)componentNode.getAttributeValue("ImageBannerImage"), request));
			banner.setTarget(getEncodedContentKey(componentNode,"Target"));
			banner.setLinkOneTarget(getEncodedContentKey(componentNode,"linkOneTarget"));
			banner.setLinkOneText((String)componentNode.getAttributeValue("linkOneText"));
			banner.setLinkOneType((String)componentNode.getAttributeValue("linkOneType"));
			banner.setLinkOneURL((String)componentNode.getAttributeValue("linkOneURL"));
			banner.setLinkTwoTarget(getEncodedContentKey(componentNode,"linkTwoTarget"));
			banner.setLinkTwoText((String)componentNode.getAttributeValue("linkTwoText"));
			banner.setLinkTwoType((String)componentNode.getAttributeValue("linkTwoType"));
			banner.setLinkTwoURL((String)componentNode.getAttributeValue("linkTwoURL"));
		}
		return banner;
	}

	public CMSImageModel createImage(ContentKey key, CMSPageRequest request){
		CMSImageModel image = new CMSImageModel();
		if(key!=null){
		ContentNodeI contentNode = getContentNodeByKey(key, request);
		if(contentNode != null){
				if (request.isPreview()) {
					image.setPath(getMediaPath((String) contentNode
							.getAttributeValue("path")));
				} else {
					image.setPath((String) contentNode
							.getAttributeValue("path"));
				}
			image.setHeight((Integer)contentNode.getAttributeValue("height"));
			image.setWidth((Integer)contentNode.getAttributeValue("width"));
		}
		}
		return image;
	}

	private CMSPickListItemModel createPickList(ContentNodeI contentNode, CMSPageRequest request){
		if(contentNode != null){
			if("PickList".equals(contentNode.getKey().getType().name())){
				CMSPickListModel pickList = null;
				List<CMSScheduleModel> schedule = createSchedule(getContentKeysList(contentNode,"PickListSchedule"), request);
				if(isSchedulesMatches(schedule, request, false)){
					pickList = new CMSPickListModel();
					pickList.setComponentType(CMSComponentType.PICKLIST);
					pickList.setName((String)contentNode.getAttributeValue("Name"));
					pickList.setType((String)contentNode.getAttributeValue("Type"));
					pickList.setDescription((String)contentNode.getAttributeValue("Description"));
					ContentKey pickListMedia = (ContentKey)contentNode.getAttributeValue("PickListMedia");
					List<ContentKey> productKeys = getContentKeysList(contentNode, "PickListProduct");
					List<ContentKey> categoryKeys = getContentKeysList(contentNode, "PickListCategory");
					List<String> productList = new ArrayList<String>();
					List<String> categoryList = new ArrayList<String>();
					if(productKeys != null){
						for(ContentKey key:productKeys){
							productList.add(key.getEncoded());
						}
						if(productList!=null && productList.size()>0)
						pickList.setProducts(productList);
					}
					if(categoryKeys != null){
						for(ContentKey key:categoryKeys){
							categoryList.add(key.getEncoded());
						}
						if(categoryList!=null && categoryList.size()>0)
						pickList.setCategories(categoryList);
					}
					if(pickListMedia != null){
						pickList.setImage(createImageBanner(getContentNodeByKey(pickListMedia, request), request));
					}
					pickList.setItems(createPickList(getContentKeysList(contentNode,"PickListPickListItem"), request));
				}
				return pickList;
			} else if ("PickListItem".equals(contentNode.getKey().getType().name())) {
				CMSPickListItemModel pickListItem = new CMSPickListItemModel();
				ContentKey key = (ContentKey)contentNode.getAttributeValue("PickListItemProduct");
				String fullName = key.getId();
				pickListItem.setProduct(fullName);
				return pickListItem;
			}
		}
		return null;
	}

	private List<CMSPickListItemModel> createPickList(List<ContentKey> keys, CMSPageRequest request){
		List<CMSPickListItemModel> pickListItems = null;
		if(BeanUtil.isNotEmpty(keys)){
			pickListItems = new ArrayList<CMSPickListItemModel>();
			for(ContentKey key:keys){
				ContentNodeI contentNode = getContentNodeByKey(key, request);
				pickListItems.add(createPickList(contentNode,request));
			}
		}
		return pickListItems;
	}

	public List<CMSScheduleModel> createSchedule(ContentNodeI contentNode, String scheduleAttributeName, CMSPageRequest request) {
		List<CMSScheduleModel> schedules = null;
		if(contentNode != null ){
			List<ContentKey> contentKeys =  getContentKeysList(contentNode, scheduleAttributeName);
			schedules = createSchedule(contentKeys, request);
		}
		return schedules;
	}

	private List<CMSScheduleModel> createSchedule(List<ContentKey> contentKeys, CMSPageRequest request) {
		List<CMSScheduleModel> schedules = null;
		if(BeanUtil.isNotEmpty(contentKeys)){
			schedules = new ArrayList<CMSScheduleModel>();
			for(ContentKey contentKey: contentKeys){
				try{
					if(contentKey.getType().equals(ContentType.Schedule)){
						ContentNodeI scheduleNode = getContentNodeByKey(contentKey, request);
						CMSScheduleModel schedule = new CMSScheduleModel();
						schedule.setDay((String)scheduleNode.getAttributeValue("Day"));
						schedule.setStartDate((Date)scheduleNode.getAttributeValue("StartDate"));
						schedule.setEndDate((Date)scheduleNode.getAttributeValue("EndDate"));
						schedule.setStartTime((Date)scheduleNode.getAttributeValue("StartTime"));
						schedule.setEndTime((Date)scheduleNode.getAttributeValue("EndTime"));
						schedules.add(schedule);
					}
				}catch(Exception e){
                    LOG.error("Exception happened while creating schedule (" + contentKey + ") : ", e);
				}
			}
		}
		return schedules;
	}

	public ContentNodeI getContentNodeByKey(ContentKey key, CMSPageRequest request){
		ContentNodeI contentNodeI = null;
		try {
			if (request.isPreview() || (key!=null && key.getType()!=null && key.getType().toString().equals("Image"))) {
                contentNodeI = CmsManager.getInstance().getContentNode(key);
			} else {
                if (contentNodesMap != null && contentNodesMap.containsKey(key)) {
                    contentNodeI = contentNodesMap.get(key);
				}
			}
		} catch(Exception e){
			LOG.error("Error in getting node "+key,e);
		}
		return contentNodeI;
	}

	public boolean isSchedulesMatches(List<CMSScheduleModel> schedules, CMSPageRequest request, boolean isFeedLevel){
		boolean isMatchingSchedule = false;
		if(BeanUtil.isNotEmpty(schedules)){
			for(CMSScheduleModel schedule:schedules){
				if(isScheduleMatches(schedule, request, isFeedLevel)){
					isMatchingSchedule = true;
					break;
				}
			}
		} else {
			//If no schedule present, page shouldn't be available but sections should be available.
			if(isFeedLevel && (!request.isPreview())) {
				isMatchingSchedule = false;
			} else if(isFeedLevel && request.isPreview() && request.getRequestedDate()!=null){
				isMatchingSchedule = false;
			} else {
				isMatchingSchedule = true;
			}
		}
		return isMatchingSchedule;
	}

	public boolean isScheduleMatches(CMSScheduleModel schedule, CMSPageRequest request, boolean isFeedLevel){
		boolean isMatchingSchedule = false;
		if(!request.isIgnoreSchedule()){
			Calendar currentDate = Calendar.getInstance();
			TimeOfDay currentTime = new TimeOfDay(currentDate.getTime());
			if(request.getRequestedDate()!= null){
				currentTime = new TimeOfDay(request.getRequestedDate());
				currentDate.setTime(request.getRequestedDate());
			}

			if("AllDay".equalsIgnoreCase(schedule.getDay()) || getCurrentDay(currentDate).equalsIgnoreCase(schedule.getDay())){
				Calendar endDateCalendar = getCalendar(schedule.getEndDate(),1);
				Calendar startDateCalendar = getCalendar(schedule.getStartDate(),0);
				if(startDateCalendar != null && endDateCalendar != null && currentDate.after(startDateCalendar) && currentDate.before(endDateCalendar)){
					if(schedule.getStartTime() != null && schedule.getEndTime() != null){
						TimeOfDay startingTime = new TimeOfDay(schedule.getStartTime());
						TimeOfDay endingTime = new TimeOfDay(schedule.getEndTime());
						if(currentTime.after(startingTime) && (currentTime.before(endingTime) || endingTime.toString().equals("12:00 AM"))){
							isMatchingSchedule = true;
						}
					} else {
						if(isFeedLevel && (!request.isPreview())) {
							isMatchingSchedule = false;
						} else {
						isMatchingSchedule = true;
						}
					}

				}
			}
		} else {
			if(isFeedLevel && (!request.isPreview())) {
				isMatchingSchedule = false;
			} else {
			isMatchingSchedule = true;
			}
		}
		return isMatchingSchedule;
	}

	public final Calendar getCalendar(Date date, int plusDay){
		Calendar calendar = Calendar.getInstance();
		if(date != null){
			calendar.setTime(date);
		}
		calendar.add(Calendar.DATE, plusDay);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
		return calendar;
	}

	public final String getCurrentDay(Calendar day){
		DateTime now = new DateTime(day.getTimeInMillis());
		Property property = now.dayOfWeek();
		return property.getAsText();
	}

	private String getFeedContent(){
		String response = null;
		try {
			response =  CMSPublishManager.getLatestFeed(CmsManager.getInstance().getSingleStoreKey().getId());
		} catch (FDResourceException e) {
			LOG.error(e);
		}
		return response;
	}

	public Map<ContentKey, ContentNodeI> loadNodesFromXMLString(String content) {
		StringReader reader = new StringReader(content);
		try {
		    FlexContentHandler handler = CmsServiceLocator.flexContentHandler();
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware(true);
			factory.setValidating(false);
			SAXParser parser = factory.newSAXParser();
			// handler.setContentService(CmsManager.getInstance());
			InputSource dataInputSource = new InputSource(reader);
			dataInputSource.setEncoding("UTF-8");
			parser.parse(dataInputSource, handler);

			final Map<ContentKey, Map<Attribute, Object>> contentNodes = handler.getContentNodes();

			Map<ContentKey, ContentNodeI> result = new HashMap<ContentKey, ContentNodeI>();
			for (Map.Entry<ContentKey, Map<Attribute, Object>> entry : contentNodes.entrySet()) {
                Map<Attribute, Object> payload = Collections.emptyMap();
                if (!entry.getValue().isEmpty()) {
                    payload = new HashMap<Attribute, Object>(entry.getValue().size());
                    for (Map.Entry<Attribute, Object> rawEntry : entry.getValue().entrySet()) {
                        Attribute attribute = rawEntry.getKey();
                        Object value = rawEntry.getValue();

                        if (attribute instanceof Scalar && value != null) {
                            // FIXME: this is weird: we are converting Objects to Objects by toString() + deserialize()
                            // this seems invalid, but the value is always a String for Scalars,
                            // so we are hopefully converting from Strings to Objects
                            value = ScalarValueConverter.deserializeToObject((Scalar) attribute, value.toString());
                        }

                        payload.put(attribute, value);
                    }
                }
                result.put(entry.getKey(), new ContentNode(entry.getKey(), payload, null, contentTypeInfoService));
			}

            return result;
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		} catch (SAXException se) {
			throw new RuntimeException(se);
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		} finally {
			if (reader != null)
				reader.close();
		}
	}

	public String getEncodedContentKey(ContentNodeI contentNode, String attributeName){
		String encodedKey = null;
		ContentKey key = (ContentKey)contentNode.getAttributeValue(attributeName);
		if(key != null){
			encodedKey = key.getEncoded();
		}
		return encodedKey;
	}

	private List<ContentKey> getContentKeysList(ContentNodeI contentNode,String attributeName){
		return  (List<ContentKey>)contentNode.getAttributeValue(attributeName);
	}

	public String getMediaPath(String url){
		String fullUrl = null;
		if(url != null){
			fullUrl = FDStoreProperties.getMediaPath() != null ? FDStoreProperties.getMediaPath() + url : url;
		}
		return fullUrl;
	}
}