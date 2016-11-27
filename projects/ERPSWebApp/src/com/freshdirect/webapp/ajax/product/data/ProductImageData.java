package com.freshdirect.webapp.ajax.product.data;

import java.io.Serializable;
import java.util.List;

public class ProductImageData implements Serializable {
  
  private static final long serialVersionUID = 4056728908406517379L;
  
  public static class ImageAtom implements Serializable {
    private static final long serialVersionUID = -6138432217326055993L;

    public String thumbnailUrl;
    public String url;
    public String largeUrl;
    public String altDescription;
    
    
    public ImageAtom( String imgUrl ) {
      this.url = imgUrl;
    }

    public ImageAtom(String imgUrl, String altDescription) {
        this.url = imgUrl;
        this.altDescription = altDescription;
    }

    public ImageAtom(String imgUrl, String thUrl, String lgUrl, String altDescription) {
      this.url = imgUrl;
      this.thumbnailUrl = thUrl;
      this.largeUrl = lgUrl;
      this.altDescription = altDescription;
    }
  }
    
  /**
   * list of image atoms
   */
  protected List<ImageAtom> images;
  
  public List<ImageAtom> getImages() {
    return images;
  }
  
  public void setImages( List<ImageAtom> images ) {
    this.images = images;
  }
  
}
