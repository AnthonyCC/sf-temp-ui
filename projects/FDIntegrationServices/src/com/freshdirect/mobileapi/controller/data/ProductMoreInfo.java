package com.freshdirect.mobileapi.controller.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.mobileapi.exception.ModelException;
import com.freshdirect.mobileapi.model.Brand;
import com.freshdirect.mobileapi.model.ComponentGroup;
import com.freshdirect.mobileapi.model.Variation;
import com.freshdirect.mobileapi.model.Wine;
import com.freshdirect.mobileapi.model.Product.ImageType;

/**
 * Contains information related to the product that is not strictly necessary to be displayed in product detail screen.
 * Most of the content here are extra information about brands, producers, especial notes or anything else is related to 
 * the product, and user may want to see or not. 
 *  
 * @author fgarcia
 *
 */
public class ProductMoreInfo {
    private static final Logger LOG = Logger.getLogger(ProductMoreInfo.class);

    private String id;

    private String categoryId;

    private String descriptionNote;

    private String description;

    private String fdSource;

    private String fdGrade;

    private String fdFrenching;

    private String fdRipeness;

    private WineProductInfo wineInfo;

    private String heatingInstructions;

    private String fullName;

    private String kosherType;

    private Image kosherSymbol;

    /**
     * Brand name as key, Brand information as value
     */
    private Map<String, String> brands = new HashMap<String, String>();

    /**
     * Variation name as key, variation information as value;
     */
    private Map<String, String> variations = new HashMap<String, String>();

    private Map<String, String> ingredients = new HashMap<String, String>();

    private Map<String, String> nutritionFacts = new HashMap<String, String>();

    private Map<String, String> skuNames = new HashMap<String, String>();

    private List<String> skuCodes = new ArrayList<String>();

    private Set<ProductMoreInfo> componentGroupMealProductMoreInfo = new HashSet<ProductMoreInfo>();

    private List<String> allergens;

    private Image productImage;

    private String partiallyFrozen;

    public ProductMoreInfo(com.freshdirect.mobileapi.model.Product product) throws ModelException, FDResourceException,
            FDSkuNotFoundException {
        LOG.debug("Creating ProductMoreInfo for product: " + product.getProductId());
        id = product.getProductId();
        categoryId = product.getCategoryId();
        descriptionNote = product.getDescriptionNote();
        description = product.getDescription();
        fdSource = product.getFdDefSource();
        fdGrade = product.getFdDefGrade();
        fdFrenching = product.getFdDefFrenching();
        fdRipeness = product.getFdDefRipeness();
        fullName = product.getProductTitle();
        for (Brand brand : product.getBrands()) {
            brands = new HashMap<String, String>();
            brands.put(brand.getName(), brand.getDescription());
        }
        allergens = product.getAllergens();
        for (Variation variation : product.getVariations()) {
            variations.put(variation.getName(), variation.getDescription());
        }

        if (product instanceof Wine) {
            Wine wine = (Wine) product;
            wineInfo = new WineProductInfo();
            wineInfo.setAging(wine.getAging());
            wineInfo.setAlcoholGrade(wine.getAlcohol());
            wineInfo.setClassification(wine.getClassification());
            wineInfo.setGrape(wine.getGrape());
            wineInfo.setImporter(wine.getImporter());
            wineInfo.setRegionDescription(wine.getWineRegionDescription());
            wineInfo.setRegionLabel(wine.getWineRegionLabel());
            for (String iconPath : wine.getWineTypeIcons()) {
                Image iconImage = new Image();
                iconImage.setSource(iconPath);
                wineInfo.getTypeIcons().add(iconImage);
            }
        }

        if (product.getLayout() != "componentGroupMeal") {

            if (product.isMultipleNutrition()) {
                for (com.freshdirect.mobileapi.model.Sku sku : product.getSkus()) {
                    skuCodes.add(sku.getSkuCode());
                    if (product.getDefaultProduct().hasIngredients()) {
                        ingredients.put(sku.getSkuCode(), product.getSkuIngredients(sku));
                    }
                    if (product.getDefaultProduct().hasNutritionFacts()) {
                        nutritionFacts.put(sku.getSkuCode(), product.getSkuNutrition(sku));
                    }
                    if (sku.hasSecondaryDomain()) {
                        skuNames.put(sku.getSkuCode(), sku.getDomainLabel());
                    } else {
                        if (sku.getDomain() != null) {
                            skuNames.put(sku.getSkuCode(), sku.getDomain().getDomainValueLabel());
                        }
                    }
                }
            } else {
                com.freshdirect.mobileapi.model.Sku sku = product.getDefaultSku();
                if (sku != null) {
                    skuCodes.add(sku.getSkuCode());
                    if (product.getDefaultProduct().hasIngredients()) {
                        ingredients.put(sku.getSkuCode(), product.getSkuIngredients(sku));
                    }
                    if (product.getDefaultProduct().hasNutritionFacts()) {
                        nutritionFacts.put(sku.getSkuCode(), product.getSkuNutrition(sku));
                    }
                }
            }
        }
        // If product has componentGroups, in other words a ComponentGroupMeal
        if (product.getComponentGroups().size() > 0) {
            for (ComponentGroup componentGroup : product.getComponentGroups()) {
                LOG.debug("Getting products from ComponetnGroup " + componentGroup.getName());
                for (com.freshdirect.mobileapi.model.Product p : componentGroup.getProductList()) {
                    LOG.debug("Adding product with id " + p.getProductId());
                    ProductMoreInfo componentGroupProductMoreInfo = new ProductMoreInfo(p);
                    componentGroupMealProductMoreInfo.add(componentGroupProductMoreInfo);
                }
            }
        }

        if (product.getKosherSymbol() != null) {
            kosherSymbol = new Image();
            kosherSymbol.setSource(product.getKosherSymbol().getPath());
        }
        kosherType = product.getKosherType();

        // from i_product_image.jspf
        com.freshdirect.fdstore.content.Image detail = product.getImage(ImageType.DETAIL);
        com.freshdirect.fdstore.content.Image zoom = product.getImage(ImageType.ZOOM);

        if (zoom != null && zoom.getPath().indexOf("clear.gif") == -1) {
            productImage = new Image(zoom.getPath(), zoom.getHeight(), zoom.getWidth());
        } else {
            productImage = new Image(detail.getPath(), detail.getHeight(), detail.getWidth());
        }

        partiallyFrozen = StringUtils.replace(product.getPartiallyFrozen(), "\"/media", "\"http://www.freshdirect.com/media");
        partiallyFrozen = StringUtils.remove(partiallyFrozen, "align=right");
        heatingInstructions = StringUtils.replace(product.getHeatingInstructions(), "\"/media", "\"http://www.freshdirect.com/media");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescriptionNote() {
        return descriptionNote;
    }

    public void setDescriptionNote(String descriptionNote) {
        this.descriptionNote = descriptionNote;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFdSource() {
        return fdSource;
    }

    public void setFdSource(String fdSource) {
        this.fdSource = fdSource;
    }

    public String getFdGrade() {
        return fdGrade;
    }

    public void setFdGrade(String fdGrade) {
        this.fdGrade = fdGrade;
    }

    public String getFdFrenching() {
        return fdFrenching;
    }

    public void setFdFrenching(String fdFrenching) {
        this.fdFrenching = fdFrenching;
    }

    public String getFdRipeness() {
        return fdRipeness;
    }

    public void setFdRipeness(String fdRipeness) {
        this.fdRipeness = fdRipeness;
    }

    public WineProductInfo getWineInfo() {
        return wineInfo;
    }

    public void setWineInfo(WineProductInfo wineInfo) {
        this.wineInfo = wineInfo;
    }

    public String getHeatingInstructions() {
        return heatingInstructions;
    }

    public void setHeatingInstructions(String heatingInstructions) {
        this.heatingInstructions = heatingInstructions;
    }

    public Map<String, String> getBrands() {
        return brands;
    }

    public void setBrands(Map<String, String> brands) {
        this.brands = brands;
    }

    public Map<String, String> getVariations() {
        return variations;
    }

    public void setVariations(Map<String, String> variations) {
        this.variations = variations;
    }

    public Map<String, String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Map<String, String> ingredients) {
        this.ingredients = ingredients;
    }

    public Map<String, String> getNutritionFacts() {
        return nutritionFacts;
    }

    public void setNutritionFacts(Map<String, String> nutritionFacts) {
        this.nutritionFacts = nutritionFacts;
    }

    public Map<String, String> getSkuNames() {
        return skuNames;
    }

    public List<String> getSkuCodes() {
        return skuCodes;
    }

    public void setSkuCodes(List<String> skuCodes) {
        this.skuCodes = skuCodes;
    }

    public Set<ProductMoreInfo> getComponentGroupMealProductMoreInfo() {
        return componentGroupMealProductMoreInfo;
    }

    public List<String> getAllergens() {
        return allergens;
    }

    public String getFullName() {
        return fullName;
    }

    public Image getKosherSymbol() {
        return kosherSymbol;
    }

    public String getKosherType() {
        return kosherType;
    }

    public Image getProductImage() {
        return productImage;
    }

    public void setProductImage(Image productImage) {
        this.productImage = productImage;
    }

    public String getPartiallyFrozen() {
        return partiallyFrozen;
    }

    public void setPartiallyFrozen(String partiallyFrozen) {
        this.partiallyFrozen = partiallyFrozen;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((categoryId == null) ? 0 : categoryId.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((fullName == null) ? 0 : fullName.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ProductMoreInfo other = (ProductMoreInfo) obj;
        if (categoryId == null) {
            if (other.categoryId != null)
                return false;
        } else if (!categoryId.equals(other.categoryId))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (fullName == null) {
            if (other.fullName != null)
                return false;
        } else if (!fullName.equals(other.fullName))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
