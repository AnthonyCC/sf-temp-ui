package com.freshdirect.wcms;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimerTask;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;
import org.joda.time.DateTime;
import org.joda.time.DateTime.Property;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.service.xml.CmsNodeHandler;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.cache.EhCacheUtil;
import com.freshdirect.fdstore.cms.CMSPublishManager;
import com.freshdirect.fdstore.content.CMSAnchorModel;
import com.freshdirect.fdstore.content.CMSComponentModel;
import com.freshdirect.fdstore.content.CMSComponentType;
import com.freshdirect.fdstore.content.CMSImageBannerModel;
import com.freshdirect.fdstore.content.CMSImageModel;
import com.freshdirect.fdstore.content.CMSPageRequest;
import com.freshdirect.fdstore.content.CMSPickListItemModel;
import com.freshdirect.fdstore.content.CMSPickListModel;
import com.freshdirect.fdstore.content.CMSScheduleModel;
import com.freshdirect.fdstore.content.CMSSectionModel;
import com.freshdirect.fdstore.content.CMSTextComponentModel;
import com.freshdirect.fdstore.content.CMSWebPageModel;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CMSContentFactory {
	
	private static final String FEED_CACHE = "cmsPageCache";
	private static CMSContentFactory instance = null;
	
	private static final Category LOG = LoggerFactory.getInstance(CMSContentFactory.class);
	
	public static CMSContentFactory getInstance(){
		if(instance == null){
			init();
		}
		return instance;
	}
	
	public static void init(){
		instance = new CMSContentFactory();
		//pageTimer = new Timer();
		//pickListTimer = new Timer();
		//Run first time during initialization
		instance.cacheAllPages();
		
		//Schedule the task after every 15 mins.
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.MINUTE, 0);
		//pageTimer.scheduleAtFixedRate(instance.new PageLoaderTask(), calendar.getTime(), 15 * 60 *1000);
		//pickListTimer.scheduleAtFixedRate(instance.new PickListLoaderTask(), calendar.getTime(), 15 * 60 *1000);
	}
	
	
	public class PageLoaderTask extends TimerTask{

		@Override
		public void run() {
			try{
				instance.cacheAllPages();
			} catch (Exception e){
				LOG.error(e);
			}
		}
	}
	
	public void cacheAllPages(){
		LOG.debug("Loading all pages in cache "+ new Date());
		CMSPageRequest pageRequest = new CMSPageRequest();
		List<CMSWebPageModel> pages = getCMSPageByParameters(pageRequest);
		EhCacheUtil.clearCache(FEED_CACHE);
		for(CMSWebPageModel page: pages){
			if(page != null){
				EhCacheUtil.putObjectToCache(FEED_CACHE, page.getTitle(),page);
			}
		}
	}
	
	
	public class PickListLoaderTask extends TimerTask {

		@Override
		public void run() {
			LOG.debug("Loading PickList"+ new Date());
			CMSPageRequest request = new CMSPageRequest();
			List<CMSPickListItemModel> items = getPickListByParameter(request);
			if(items != null){
				for(CMSPickListItemModel pickList: items){
					if(pickList != null){
						EhCacheUtil.putObjectToCache(FEED_CACHE, pickList.getName(),pickList);
					}
				}
			}
		}
		
	}
	
	public final List<CMSPickListItemModel> getPickListByParameter(CMSPageRequest request){
		List<CMSPickListItemModel> pickLists = new ArrayList<CMSPickListItemModel>();
		Set<ContentKey> contentKeys = getContentService().getContentKeysByType(ContentType.get("PickList"));
		Map<ContentKey,ContentNodeI> contentNodes = getContentService().getContentNodes(contentKeys);
		for(Entry<ContentKey, ContentNodeI> contentNodeEntry: contentNodes.entrySet()){
			ContentNodeI contentNode = contentNodeEntry.getValue();
			CMSPickListItemModel pickList = createPickList(contentNode, request);
			pickLists.add(pickList);
		}
		return pickLists;
	}
	
	public final List<CMSWebPageModel> getCMSPageByParameters(CMSPageRequest pageRequest){
		List<CMSWebPageModel> response = new ArrayList<CMSWebPageModel>();
		Map<ContentKey,ContentNodeI> contentNodes = new HashMap<ContentKey,ContentNodeI>();
		//if preview load from cms db, else read from erps feed table.
		if(pageRequest.isPreview()){
			Set<ContentKey> keys = getContentService().getContentKeysByType(ContentType.get("WebPage"));
			contentNodes = getContentService().getContentNodes(keys);
		} else {
			String data = getFeedContent();
			if(StringUtils.isNotBlank(data)){
				contentNodes = loadNodesFromXMLString(data);
			} 
		}
		if(contentNodes != null && !contentNodes.isEmpty()){
			for(Entry<ContentKey, ContentNodeI> contentNodeEntry: contentNodes.entrySet()){
				ContentNodeI contentNode = contentNodeEntry.getValue();
				CMSWebPageModel page = getCMSPage(contentNode, pageRequest);
				if(page != null){
					boolean addToResponse = true;
					//Return all the pages if page name is null in request.
					if(pageRequest.getPageType() != null && ! pageRequest.getPageType().equals(page.getType())){
						addToResponse = false;
					} else {
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
	
	
	public final CMSWebPageModel getCMSPageByName(String pageName){
		CMSWebPageModel page = (CMSWebPageModel) EhCacheUtil.getObjectFromCache(CMSContentFactory.FEED_CACHE, pageName);
		return page;
	}
	
	public final CMSWebPageModel getCMSPage(ContentNodeI contentNode, CMSPageRequest request){
		CMSWebPageModel webPage = null;
		if(contentNode != null){
			webPage = new CMSWebPageModel();
			webPage.setTitle((String)contentNode.getAttributeValue("PAGE_TITLE"));
			webPage.setSeoMetaDescription((String)contentNode.getAttributeValue("SEO_META_DESCRIPTION"));
			webPage.setType((String)contentNode.getAttributeValue("WebPageType"));
			List<CMSScheduleModel> schedules = createSchedule(contentNode,"WebPageSchedule");
			
			boolean matchingSchedule = isSchedulesMatches(schedules, request);
			if(!matchingSchedule){
				LOG.debug("Schedule is not matching for :"+ contentNode.getKey());
				return null;
			}
			
			List<CMSSectionModel> sections = getPageSections(contentNode, request);
			if(sections != null && !sections.isEmpty()){
				webPage.setSections(sections);
			}
		}
		return webPage;
	}
	
	public final List<CMSSectionModel> getPageSections(ContentNodeI pageNode, CMSPageRequest request){
		List<CMSSectionModel> sections = new ArrayList<CMSSectionModel>();
		if(pageNode != null){
			List<ContentKey> sectionKeys = getContentKeysList(pageNode, "WebPageSection");
			if(CollectionUtils.isNotEmpty(sectionKeys)){
				for(ContentKey sectionKey: sectionKeys){
					ContentNodeI sectionNode = getContentNodeByKey(sectionKey);
					CMSSectionModel section = new CMSSectionModel();
					
					List<CMSScheduleModel> schedules = createSchedule(getContentKeysList(sectionNode,"SectionSchedule"));
					if(isSchedulesMatches(schedules, request)){
						section.setType((String)sectionNode.getAttributeValue("Name"));
						section.setType((String)sectionNode.getAttributeValue("Type"));
						List<CMSComponentModel> components = getSectionComponents(sectionNode, request);
						if(components != null && !components.isEmpty()){
							section.setComponents(components);
							//When no components present section will not be added to the page.
							sections.add(section);
						}
					} else {
						LOG.debug("Schedule is not matching for :"+ pageNode.getKey());
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
			if(CollectionUtils.isNotEmpty(componentKeys)){
				for(ContentKey componentKey: componentKeys){
					ContentNodeI componentNode = getContentNodeByKey(componentKey);
					if("Anchor".equals(componentKey.getType().getName())){
						addComponentsToSection(components,createAnchor(componentNode));
					} else if("TextComponent".equals(componentKey.getType().getName())){
						CMSTextComponentModel text = new CMSTextComponentModel();
						text.setComponentType(CMSComponentType.TEXT);
						text.setText((String)componentNode.getAttributeValue("Text"));
						text.setType((String)componentNode.getAttributeValue("Type"));
						addComponentsToSection(components,text);
					} else if("PickList".equals(componentKey.getType().getName())){
						CMSPickListModel pickList = (CMSPickListModel) createPickList(componentNode, request);
						addComponentsToSection(components,pickList);
					} else if("ImageBanner".equals(componentKey.getType().getName())){
						addComponentsToSection(components,createImageBanner(componentNode));
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
		String componentType = componentKey.getType().getName();
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
			
	public List<CMSAnchorModel> createAnchor(List<ContentKey> keys){
		List<CMSAnchorModel> anchors = null;
		if(keys != null){
			anchors = new ArrayList<CMSAnchorModel>();
			for(ContentKey key: keys){
				anchors.add(createAnchor(getContentNodeByKey(key)));
			}
		}
		return anchors;
	}
	
	public CMSAnchorModel createAnchor(ContentNodeI componentNode){
		CMSAnchorModel anchor = new CMSAnchorModel();
		anchor.setUrl((String)componentNode.getAttributeValue("Url"));
		ContentKey contentKey = (ContentKey)componentNode.getAttributeValue("Target");
		if(contentKey != null){
			anchor.setTarget(contentKey.getEncoded());
		}
		anchor.setText((String)componentNode.getAttributeValue("Text"));
		
		if("Button".equals(componentNode.getAttributeValue("Type"))){
			anchor.setComponentType(CMSComponentType.BUTTON);
		} else {
			anchor.setComponentType(CMSComponentType.ANCHOR);
		}
		return anchor;
	}
	
	private CMSImageBannerModel createImageBanner(ContentNodeI componentNode) {
		CMSImageBannerModel banner = null;
		if(componentNode != null){
			banner = new CMSImageBannerModel();
			banner.setComponentType(CMSComponentType.BANNER);
			banner.setName((String)componentNode.getAttributeValue("Name"));
			banner.setDescription((String)componentNode.getAttributeValue("Description"));
			banner.setType((String)componentNode.getAttributeValue("Type"));
			banner.setImage(createImage((ContentKey)componentNode.getAttributeValue("ImageBannerImage")));
			banner.setTarget(((ContentKey)componentNode.getAttributeValue("ImageBannerImage")).getEncoded());
			banner.setAnchors(createAnchor(getContentKeysList(componentNode,"ImageBannerLink")));
		}
		return banner;
	}

	public CMSPickListItemModel createPickList(String pickListName, CMSPageRequest request){
		ContentKey contentKey = new ContentKey(ContentType.get("CMSPickList"), pickListName);
		return createPickList(getContentNodeByKey(contentKey), request);
	}
	
	public CMSImageModel createImage(ContentKey key){
		CMSImageModel image = new CMSImageModel();
		ContentNodeI contentNode = getContentNodeByKey(key);
		image.setPath(getMediaPath((String)contentNode.getAttributeValue("path")));
		image.setHeight((Integer)contentNode.getAttributeValue("height"));
		image.setWidth((Integer)contentNode.getAttributeValue("width"));
		return image;
	}
	
	private CMSPickListItemModel createPickList(ContentNodeI contentNode, CMSPageRequest request){	
		if(contentNode != null){
			if("PickList".equals(contentNode.getKey().getType().getName())){
				CMSPickListModel pickList = null;
				List<CMSScheduleModel> schedule = createSchedule(getContentKeysList(contentNode,"PickListSchedule"));
				if(isSchedulesMatches(schedule, request)){
					pickList = new CMSPickListModel();
					pickList.setComponentType(CMSComponentType.PICKLIST);
					pickList.setName((String)contentNode.getAttributeValue("Name"));
					pickList.setType((String)contentNode.getAttributeValue("Type"));
					pickList.setDescription((String)contentNode.getAttributeValue("Description"));
					ContentKey pickListMedia = (ContentKey)contentNode.getAttributeValue("PickListMedia");
					if(pickListMedia != null){
						pickList.setImage(createImageBanner(getContentNodeByKey(pickListMedia)));
					}
					pickList.setItems(createPickList(getContentKeysList(contentNode,"PickListPickListItem"), request));
				} 
				return pickList;
			} else if ("PickListItem".equals(contentNode.getKey().getType().getName())) {
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
		if(CollectionUtils.isNotEmpty(keys)){
			pickListItems = new ArrayList<CMSPickListItemModel>();
			for(ContentKey key:keys){
				ContentNodeI contentNode = getContentNodeByKey(key);
				pickListItems.add(createPickList(contentNode,request));
			}
		}
		return pickListItems;
	}

	public List<CMSScheduleModel> createSchedule(ContentNodeI contentNode, String scheduleAttributeName) {
		List<CMSScheduleModel> schedules = null;
		if(contentNode != null ){
			List<ContentKey> contentKeys =  getContentKeysList(contentNode, scheduleAttributeName);
			schedules = createSchedule(contentKeys);
		}
		return schedules;
	}
	
	private List<CMSScheduleModel> createSchedule(List<ContentKey> contentKeys) {
		List<CMSScheduleModel> schedules = null;
		if(CollectionUtils.isNotEmpty(contentKeys)){
			schedules = new ArrayList<CMSScheduleModel>();
			for(ContentKey contentKey: contentKeys){
				try{
					if(contentKey.getType().equals(ContentType.get("Schedule"))){
						ContentNodeI scheduleNode = getContentNodeByKey(contentKey);
						CMSScheduleModel schedule = new CMSScheduleModel();
						schedule.setDay((String)scheduleNode.getAttributeValue("Day"));
						schedule.setStartDate((Date)scheduleNode.getAttributeValue("StartDate"));
						schedule.setEndDate((Date)scheduleNode.getAttributeValue("EndDate"));
						schedule.setStartTime((Date)scheduleNode.getAttributeValue("StartTime"));
						schedule.setEndTime((Date)scheduleNode.getAttributeValue("EndTime"));
						schedules.add(schedule);
					}
				}catch(Exception e){
					LOG.error(e);
				}
			}
		}
		return schedules;
	}
	
	public ContentNodeI getContentNodeByKey(ContentKey key){
		ContentNodeI contentNodeI = null;
		try{
			contentNodeI =  getContentService().getContentNode(key);
		} catch(Exception e){
			LOG.error("Error in getting node "+key,e);
		}
		return contentNodeI;
	}
	
	public boolean isSchedulesMatches(List<CMSScheduleModel> schedules, CMSPageRequest request){
		boolean isMatchingSchedule = false;
		if(CollectionUtils.isNotEmpty(schedules)){
			for(CMSScheduleModel schedule:schedules){
				if(isScheduleMatches(schedule, request)){
					isMatchingSchedule = true;
					break;
				}
			}
		} else {
			//If no schedule present! Pages/Sections should be available.
			isMatchingSchedule = true;
		}
		return isMatchingSchedule;
	}
	
	public boolean isScheduleMatches(CMSScheduleModel schedule, CMSPageRequest request){
		boolean isMatchingSchedule = false;
		if(request.isIgnoreSchedule()){
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
						if(currentTime.after(startingTime) && currentTime.before(endingTime)){
							isMatchingSchedule = true;
						}
					} else {
						isMatchingSchedule = true;
					}
					
				}
			} 
		} else {
			isMatchingSchedule = true;
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
	
	private ContentServiceI getContentService(){
		return CmsManager.getInstance();
	}
	
	private List<ContentKey> getContentKeysList(ContentNodeI contentNode,String attributeName){
		return  (List<ContentKey>)contentNode.getAttributeValue(attributeName);	
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
			CmsNodeHandler handler = new FlexContentHandler();
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware(true);
			factory.setValidating(false);
			SAXParser parser = factory.newSAXParser();
			handler.setContentService(getContentService());
			InputSource dataInputSource = new InputSource(reader);
			dataInputSource.setEncoding("UTF-8");
			parser.parse(dataInputSource, handler);
			return handler.getContentNodes();
		} catch (IOException ioe) {
			throw new CmsRuntimeException(ioe);
		} catch (SAXException se) {
			throw new CmsRuntimeException(se);
		} catch (ParserConfigurationException e) {
			throw new CmsRuntimeException(e);
		} finally {
			if (reader != null)
				reader.close();
		}
	}
	
	public String getMediaPath(String url){
		String fullUrl = null; 
		if(url != null){
			fullUrl = FDStoreProperties.getMediaPath() != null ? FDStoreProperties.getMediaPath() + url : url;
		}
		return fullUrl;
	}
}