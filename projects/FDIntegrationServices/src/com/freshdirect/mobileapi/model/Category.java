package com.freshdirect.mobileapi.model;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.mobileapi.controller.data.Image;
import com.freshdirect.mobileapi.controller.data.Image.ImageSizeType;

public class Category {

    String name;

    String id;
    
    private List<Image> images = new ArrayList<Image>();
    
    public static Category wrap(CategoryModel model) {
        Category result = new Category();
        result.name = model.getFullName();
        result.id = model.getContentKey().getId();
        
        List<Image> images = new ArrayList<Image>();
        result.setImages(images);
        
        com.freshdirect.fdstore.content.Image smallImage = model.getCategoryPhoto();
        if(smallImage != null) {
        	Image detailImage = new Image();
        	detailImage.setHeight(smallImage.getHeight());
            detailImage.setWidth(smallImage.getWidth());
            detailImage.setSource(smallImage.getPath());
            detailImage.setType(ImageSizeType.THUMB);
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
