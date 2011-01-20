package com.freshdirect.mobileapi.model;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.mobileapi.controller.data.Image;
import com.freshdirect.mobileapi.controller.data.Image.ImageSizeType;

public class Department {
    String name;

    String id;
    
    private List<Image> images = new ArrayList<Image>();
    
    public static Department wrap(DepartmentModel model) {
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
        
        com.freshdirect.fdstore.content.Image fullImage = model.getPhotoSmall();
        if(fullImage != null) {
        	Image detailImage = new Image();
        	detailImage.setHeight(fullImage.getHeight());
            detailImage.setWidth(fullImage.getWidth());
            detailImage.setSource(fullImage.getPath());
            detailImage.setType(ImageSizeType.LARGE);
            images.add(detailImage);
        }        
        
        return result;
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
        
}
