package com.freshdirect.mobileapi.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.content.DomainValue;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.mobileapi.exception.ModelException;
import com.freshdirect.mobileapi.util.ProductUtil;
import com.freshdirect.smartstore.Variant;

public class Wine extends Product {
    private static final Logger LOG = Logger.getLogger(Wine.class);

    private String wineCountry;

    private String wineRegionLabel;

    private String wineRegionName;

    private String wineRegionDescription;

    private String wineCity;

    private String grape;

    private String classification;

    private String importer;

    private String alcohol;

    private String aging;

    List<String> ratingIcons = new ArrayList<String>();

    List<String> reviews = new ArrayList<String>(3);

    List<String> wineTypeIcons = new ArrayList<String>();

    public Wine(ProductModel productModel) throws ModelException {
        this(productModel, null, null);
    }

    public Wine(ProductModel productModel, FDUserI user) throws ModelException {
        this(productModel, user, null);
    }

    /**
     * DUP: /shared/includes/product/usq_wine_info.jspf
     * DATE: 10/07/2009   
     * WHY: The following logic was duplicate because it was specified in a JSP file.
     * WHAT: Retrieves wine specific data. 
     * @param productModel
     * @throws ModelException
     */
    public Wine(ProductModel productModel, FDUserI user, Variant variant) throws ModelException {
        super(productModel, user, variant);
        ProductModel productNode = this.product.getProductModel();

        wineCity = productNode.getWineCity();
        grape = productNode.getWineType();
        classification = productNode.getWineClassification();
        importer = productNode.getWineImporter();
        alcohol = productNode.getWineAlchoholContent();
        aging = productNode.getWineAging();

        DomainValue _wineCountry = productNode.getWineCountry();
        if (_wineCountry != null) {
            wineCountry = _wineCountry.getLabel();
        }

        List wineRegionList = productNode.getNewWineRegion();
        DomainValue _wineRegion = null;
        if (wineRegionList != null && wineRegionList.size() > 0) {
            _wineRegion = (DomainValue) wineRegionList.get(0);
            if (_wineRegion != null) {
                wineRegionLabel = _wineRegion.getLabel();
                wineRegionName = _wineRegion.getContentName(); // Removed the resource path, since the clien't will be in charge of that
                String wineRegionDescriptionFile = "/media/editorial/win_usq/maps/" + wineRegionName + ".html";

                try {
                    wineRegionDescription = ProductUtil.readContent(ProductUtil.resolve(wineRegionDescriptionFile));
                } catch (IOException e) {
                    wineRegionDescription = "";
                    LOG.warn("Unable to read wine region description file " + wineRegionDescriptionFile);
                }

            }
        }
        List wineRating1 = productNode.getWineRating1();

        if (wineRating1 != null && wineRating1.size() > 1) {
            wineRating1 = wineRating1.subList(0, 1);
        }
        List wineRating2 = productNode.getWineRating2();
        if (wineRating2 != null && wineRating2.size() > 1) {
            wineRating2 = wineRating2.subList(0, 1);
        }

        List wineRating3 = productNode.getWineRating3();
        if (wineRating3 != null && wineRating3.size() > 1) {
            wineRating3 = wineRating3.subList(0, 1);
        }
        List wineType = productNode.getNewWineType();
        List ratings = productNode.getRating();
        reviews.add(ProductUtil.readHtml(productNode.getWineReview1()));
        reviews.add(ProductUtil.readHtml(productNode.getWineReview2()));
        reviews.add(ProductUtil.readHtml(productNode.getWineReview3()));

        wineTypeIcons.addAll(getIconPath(wineType, "/media/editorial/win_usq/icons/", ".gif"));
        wineTypeIcons.addAll(getIconPath(ratings, "/media/editorial/win_usq/icons/", ".gif"));

        ratingIcons.addAll(getIconPath(wineRating1, "/media/editorial/win_usq/icons/rating/", ".gif"));
        ratingIcons.addAll(getIconPath(wineRating2, "/media/editorial/win_usq/icons/rating/", ".gif"));
        ratingIcons.addAll(getIconPath(wineRating3, "/media/editorial/win_usq/icons/rating/", ".gif"));
    }

    public String getWineCountry() {
        return wineCountry;
    }

    public void setWineCountry(String wineCountry) {
        this.wineCountry = wineCountry;
    }

    public String getWineRegionLabel() {
        return wineRegionLabel;
    }

    public void setWineRegionLabel(String wineRegionLabel) {
        this.wineRegionLabel = wineRegionLabel;
    }

    public String getWineRegionName() {
        return wineRegionName;
    }

    public void setWineRegionName(String wineRegionName) {
        this.wineRegionName = wineRegionName;
    }

    public String getWineCity() {
        return wineCity;
    }

    public void setWineCity(String wineCity) {
        this.wineCity = wineCity;
    }

    public String getGrape() {
        return grape;
    }

    public void setGrape(String grape) {
        this.grape = grape;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getImporter() {
        return importer;
    }

    public void setImporter(String importer) {
        this.importer = importer;
    }

    public String getAlcohol() {
        return alcohol;
    }

    public void setAlcohol(String alcohol) {
        this.alcohol = alcohol;
    }

    public String getAging() {
        return aging;
    }

    public void setAging(String aging) {
        this.aging = aging;
    }

    public String getWineRegionDescription() {
        return wineRegionDescription;
    }

    public void setWineRegionDescription(String wineRegionDescription) {
        this.wineRegionDescription = wineRegionDescription;
    }

    public List<String> getWineTypeIcons() {
        return wineTypeIcons;
    }

    public void setWineTypeIcons(List<String> wineTypeIcons) {
        this.wineTypeIcons = wineTypeIcons;
    }

    @Override
    public List<String> getOrigin() {
        List<String> origins = super.getOrigin();
        origins.add(wineCountry);
        return origins;
    }

    private List<String> getIconPath(List domain, String mediaPath, String mediaType) {

        List<String> iconPath = new ArrayList<String>();
        if (domain != null && domain.size() > 0) {
            String imagePath = "";
            for (int i = 0; i < domain.size(); i++) {
                DomainValue domainVal = (DomainValue) domain.get(i);
                imagePath = mediaPath + domainVal.getContentName() + mediaType;
                iconPath.add(imagePath);
            }
        }
        return iconPath;
    }

}
