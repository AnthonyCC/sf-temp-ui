package com.freshdirect.mobileapi.model;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.fdstore.content.BannerModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.mobileapi.controller.data.Image;
import com.freshdirect.mobileapi.controller.data.Image.ImageSizeType;
import com.freshdirect.mobileapi.util.BrowseUtil;

public class Department extends ProductContainer {
    private String name;

    private String id;
    
    private Image icon;
    
    private Image banner;

    private List<Image> images = new ArrayList<Image>();
    
    private List<DepartmentSection> sections = new ArrayList<DepartmentSection>();
    
    public static Department wrap(DepartmentModel model) {
    	return wrapDepartment(model, 0);
    }
    
    public static Department wrap(DepartmentModel model, long noOfProducts) {
    	return wrapDepartment(model, noOfProducts);
    }
    
    private static Department wrapDepartment(DepartmentModel model, long noOfProducts) {
    	Department result = new Department();
        result.name = model.getFullName();
        result.id = model.getContentKey().getId();
        
        List<Image> images = new ArrayList<Image>();
        result.setImages(images);
        
        com.freshdirect.fdstore.content.Image smallImage = model.getPhotoSmall();
        if(smallImage != null) {
        	Image detailImage = new Image();
        	detailImage.setHeight(smallImage.getHeight());
            detailImage.setWidth(smallImage.getWidth());
            detailImage.setSource(smallImage.getPath());
            detailImage.setType(ImageSizeType.THUMB);
            images.add(detailImage);
        }
        
        com.freshdirect.fdstore.content.Image fullImage = model.getPhoto();
        if(fullImage != null) {
        	Image detailImage = new Image();
        	detailImage.setHeight(fullImage.getHeight());
            detailImage.setWidth(fullImage.getWidth());
            detailImage.setSource(fullImage.getPath());
            detailImage.setType(ImageSizeType.LARGE);
            images.add(detailImage);
        }        
        result.setNoOfProducts(noOfProducts);
        
        com.freshdirect.fdstore.content.Image tabletIcon = model.getTabletIcon();
        
        if (tabletIcon != null) {
        	Image icon = new Image();
        	icon.setHeight(tabletIcon.getHeight());
        	icon.setWidth(tabletIcon.getWidth());
        	icon.setSource(tabletIcon.getPathWithPublishId());
        	icon.setType(ImageSizeType.ICON);
        	result.setIcon(icon);
        	images.add(icon);
        }
        
        final BannerModel banner = model.getTabletHeaderBanner();
        if (banner != null && banner.getImage() != null) {
            result.setBanner(new Image(banner.getImage()));
        }
        addSections(model, result);
        
        return result;
    }
    
    public static void addSections(DepartmentModel storeDepartment, Department result){
    	//Department sections are added here
    	//Call BrowseUtil to populate all the categories.
		   List<DepartmentSection> departmentSections = BrowseUtil.getDepartmentSections(storeDepartment);
		   result.setSections(departmentSections);
    }
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public Image getIcon() {
		return icon;
	}

	public void setIcon(Image icon) {
		this.icon = icon;
	}

	public Image getBanner() {
		return banner;
	}

	public void setBanner(Image banner) {
		this.banner = banner;
	}

	public List<DepartmentSection> getSections() {
		return sections;
	}

	public void setSections(List<DepartmentSection> sections) {
		this.sections = sections;
	}
	
	
}
